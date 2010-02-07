class ItemGroupService {

    boolean transactional = true

    def transformToItemsByGroup(def items)
    {
    	def itemsByGroup = [:]
    	def groups = items.collect{ it.group }.unique()
    	groups.each{ group -> itemsByGroup[group] = [] }
    	items.each{ item -> itemsByGroup[item.group] << item }
    	return itemsByGroup 
    }
    
    def addItem(def group, def item)
    {
    	item.save()		
		group.addItem(item)
		group.save()
	}
	
	def deleteWholeGroup(def group)
	{
		group.items.collect{it}.each{ item ->
			item.iteration?.deleteItem(item.id)
			item.group?.deleteItem(item.id)
	    	item.delete()
		}
		
		group.delete()
	}
}
