package org.agiletracking

class ItemDeletionService {

    static transactional = true
	 def pointsSnapShotService
	 def itemGroupService
	 def iterationService

    def deleteGroupAndItems(ItemGroup group) {
				def items = group.items.collect{it}
				pointsSnapShotService.deleteWholeGroup(group)				
				items.each{ item -> iterationService.deleteItem(item)
										  itemGroupService.deleteItem(item)
				}
				items*.delete() 
				group.delete()
    }
}
