class ItemService {
    boolean transactional = true
    def itemGroupService

    def _getUnfinishedItemsGroupMapWithItemCheck(def project, Closure itemCheck)
	{
		def itemsByGroup = [:]		
		def items = Item.findAllByProject(project)
		def selectedItems = items.findAll{ item -> (item.status != ItemStatus.Finished) && itemCheck(item) }  
		
		return itemGroupService.transformToItemsByGroup(selectedItems)
	}
	
	def getUnfinishedItemsGroupMap(def project, List priorityList)
	{
		def itemCheck = { item ->
			return priorityList.contains(item.priority)   
		}
		
		return 	_getUnfinishedItemsGroupMapWithItemCheck(project,itemCheck)
	}
	
	def getUnfinishedItemsGroupMap(def project)
	{
		return 	_getUnfinishedItemsGroupMapWithItemCheck(project, { item -> true})
	}
	
	def removeItemsWithIteration(def itemsByGroup)
	{
		def itemsByGroupFiltered = [:]
		itemsByGroup.each{ group, items ->
		
			itemsByGroupFiltered[group] = []
			items.each{ item ->	
					if (!item.iteration) itemsByGroupFiltered[group] << item
			}
		}
		return itemsByGroupFiltered
	}
	
	def deleteItem(def item)
	{
		item.iteration?.deleteItem(item.id)
		item.group?.deleteItem(item.id)
		item.delete()
	}
	
}
