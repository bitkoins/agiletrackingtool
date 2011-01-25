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
		items = Defaults.getItems(3,groups,project, 123)
		mockDomain(Item, items)
    }

	protected void tearDown() {
   	super.tearDown()
   }
    
   void testGetUnfinishedItemsByGroup()
   {
    	items.each{ it.status = ItemStatus.Blocking; }
    	def itemsGroupMap = itemService.getUnfinishedItemsByGroup(project)
		assertEquals itemsGroupMap.values().flatten().size(), items.size()
   }
   
	void testGetFinishedItemsByGroup() 
	{
		items.each{ it.status = ItemStatus.Finished }
		def itemsGroupMap = itemService.getFinishedItemsByGroup(project)
		assertEquals itemsGroupMap.values().flatten().size(), items.size()
	}
 
   void testGetUnfinishedItemsByGroupWhenAllItemsAreFinished()
   {
    	items.each{ it.status = ItemStatus.Finished; }
    	def itemsGroupMap = itemService.getUnfinishedItemsByGroup(project)
    	assertEquals 0, itemsGroupMap.values().flatten().size()
   }
   
   void testDeleteItemRemovesFromIterationAndGroup()
   {
    	def item = items[0]

		def itemGroupServiceMock = mockFor(ItemGroupService)
		def iterationServiceMock = mockFor(IterationService)
		def checkItem = { anItem -> assertEquals item.uid, anItem.uid }
		def mockControls = [itemGroupServiceMock, iterationServiceMock]
		mockControls.each{ it.demand.deleteItem(1..1) { itm -> checkItem(itm) } }
		
		itemService.itemGroupService = itemGroupServiceMock.createMock()
		itemService.iterationService = iterationServiceMock.createMock()
    	
    	itemService.deleteItem(item)
    	
		mockControls*.verify()
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
		def itemUidsToRemove = items[1..2].collect{it.uid}
		def newSize = items.size() - itemUidsToRemove.size()
		itemService.removeItemsFromList( items, itemUidsToRemove)
		assertEquals newSize, items.size()
		itemUidsToRemove.each{ removedId -> assertNull items.find{ it.uid == removedId } }
	}
   
	void testMatchUidWhenNoneAreThere()
	{
		def matchList = itemService.matchItemsWithUid(items, [] )
		assertEquals 0, matchList.size()
	}

	void testMatchUidWhenNoneExistingAreThere()
	{
		def max = items.max{ it.uid }.uid
		def matchList = itemService.matchItemsWithUid(items, [max+1, max+10] )
		assertEquals 0, matchList.size()
	}

	void testMatchUidWithCorrectSequenceWhenSomeMatch()
	{
		def uidList = items[0..2].collect{it.uid}.reverse() 
		def matchList = itemService.matchItemsWithUid(items, uidList)
		assertEquals uidList.size(), matchList.size() 
		assertEquals uidList, matchList.collect{ it.uid } 
	}
}
