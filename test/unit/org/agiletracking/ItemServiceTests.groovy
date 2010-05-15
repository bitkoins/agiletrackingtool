package org.agiletracking
import grails.test.*

class ItemServiceTests extends GrailsUnitTestCase {
	def itemService
	def nr 
	
	def project 
	def groups
	def items

	protected void setUp() {
      super.setUp()
      itemService = new ItemService()
      itemService.itemGroupService = new ItemGroupService()
      nr = 0
        
		project = Defaults.getProjects(1)[0]
		groups = Defaults.getGroups(2,project)
		mockDomain(ItemGroup, groups)
		items = Defaults.getItems(5,groups,project, 123)
		mockDomain(Item, items)
    }

	protected void tearDown() {
   	super.tearDown()
   }
    
   void testGetUnfinishedItemsGroupMap()
   {
    	items.each{ it.status = ItemStatus.Blocking; }
    	def itemsGroupMap = itemService.getUnfinishedItemsGroupMap(project)
    	itemsGroupMap.each{group,itemList->
    		nr += itemList.size() 
    	}  
    	assertEquals nr , items.size()
   }
    
   void testGetUnfinishedItemsGroupMapWhenAllItemsAreFinished()
   {
    	items.each{ it.status = ItemStatus.Finished; }
    	def itemsGroupMap = itemService.getUnfinishedItemsGroupMap(project)
    	itemsGroupMap.each{group,items-> nr += items.size() }    	   	
    	assertEquals nr , 0
   }
   
   void testDeleteItemRemovesFromIterationAndGroup()
   {
    	def item = items[0]
    	def group = item.group
    	
    	def iteration = Defaults.getIterations(1,project)[0]
    	mockDomain(Iteration, [iteration])
    	
    	iteration.addItem(item)
    	itemService.deleteItem(item)
    	
    	assertNull Iteration.get(item.id)
    	assertFalse group.hasItem(item.id)
    	assertFalse iteration.hasItem(item.id)
    	
   }

   void testRetrievingItemsThatBelongToProject()
   {
		def retrievedItems = itemService.retrieveUnfinishedItemsForProject(project,items.collect{it.id})
		assertEquals items, retrievedItems
   }

   void testRetrievingItemsThatBelongToProjectPlusTwoFakeIds()
   {
		def retrievedItems = itemService.retrieveUnfinishedItemsForProject(
                            project,items.collect{it.id} + [4,5])
		assertEquals items, retrievedItems
   }

	void testRetrievingItemsThatBelongToProjectReturnsNoneWhenAllAreFinished()
   {
		items.each{ item -> item.status = ItemStatus.Finished }
		items*.save()
		def retrievedItems = itemService.retrieveUnfinishedItemsForProject(
                            project,items.collect{it.id})
		assertEquals 0, retrievedItems.size()
   }
   void testRemoveItemsFromListThatDoNotExist()
   {
		def oldItems = items.collect{it}

		itemService.removeItemsFromList(items, [] )
		assertEquals oldItems, items
		
		itemService.removeItemsFromList(items, [items.max{it.id}.id+1] )
		assertEquals oldItems, items
   }

   void testRemoveItemsFromListThatExist()
   {
		def itemIdsToRemove = items[2..3].collect{it.id}
		def newSize = items.size() - itemIdsToRemove.size()
		itemService.removeItemsFromList( items, itemIdsToRemove)
		assertEquals newSize, items.size()
		itemIdsToRemove.each{ removedId -> assertNull items.find{ it.id == removedId } }
	}

}
