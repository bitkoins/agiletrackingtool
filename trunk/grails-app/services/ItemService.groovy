class ItemService {

    boolean transactional = true

    def _getUnfinishedItemsGroupMapWithItemCheck(def project, Closure itemCheck)
	{
		def itemsByGroup = [:]		
		def groups = ItemGroup.findAllByProject(project)
						
		groups.each{ group ->
			itemsByGroup[group] = []
			group.items?.each{ item ->
				if ( item.status != ItemStatus.Finished) {
					if (itemCheck(item))
					{
						itemsByGroup[group] << item
					}
				} 
			}
		}
		
		return itemsByGroup
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
		
			println group
		
			itemsByGroupFiltered[group] = []
			items.each{ item ->	
					if (!item.iteration) itemsByGroupFiltered[group] << item
			}
		}
		return itemsByGroupFiltered
	}
}
