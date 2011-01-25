package org.agiletracking
import grails.test.*

class ItemGroupServiceTests extends GrailsUnitTestCase {
	 def itemGroupService
	 def project
	 def groupA
	 def groupB
	 def groups
	 def itemsA
    def itemsB
    def items

    protected void setUp() {
      super.setUp()
        
      itemGroupService = new ItemGroupService()
        
      project = Defaults.getProjects(1)[0]
    	mockDomain(Project, [project] )
    	
      groups = Defaults.getGroups(2,project)
      (groupA, groupB) = groups
		mockDomain(ItemGroup, groups)
		
 		itemsA = Defaults.getItems(1,[groupA],project,1)
     	itemsB = Defaults.getItems(2,[groupB],project,10)
     	items = itemsA + itemsB
     	mockDomain(Item, items)
    }

    protected void tearDown() {
        super.tearDown()
    }

	void testDeleteItem() { 
		 def item = itemsA[0]
		 itemGroupService.deleteItem(item)
		 assertFalse groupA.hasItem(item.id)
	}
    
    void testTransformToItemsByGroup()
    {
		  def itemsByGroup = itemGroupService.transformToItemsByGroup(groups,items)
	
  	     assertEquals groups.size(), itemsByGroup.size()		  
		  def equalItems = { itemsA, itemsB -> 
					def uids = { items -> items.collect{it.uid}.sort() }
					assertEquals uids(itemsA), uids(itemsB) 
		  }
	     equalItems itemsA, itemsByGroup[groupA] 
	     equalItems itemsB, itemsByGroup[groupB]
    }
    
    void testTransformToItemsByGroupWhenNoItemsPresent()
    {
    	def itemsByGroup = itemGroupService.transformToItemsByGroup(groups,[])
		
		assertEquals groups.size(), itemsByGroup.size()
		assertEquals 0, itemsByGroup[groupA].size() 
		assertEquals 0, itemsByGroup[groupB].size()
    }

    def findItemInMap = { item, map -> map.collect{it.value}.flatten().find{ it == item } }
    void testRemoveItemsFromGroupMap()
    {
    	def itemsByGroup = itemGroupService.transformToItemsByGroup(groups,items)
		def itemsToRemove = items[1..2]
      itemGroupService.removeItemsFromGroupMap(itemsToRemove, itemsByGroup)
		assertEquals groups.size(), itemsByGroup.size()
		itemsToRemove.each{ item -> assertNull findItemInMap(item,itemsByGroup) }
		(items-itemsToRemove).each{ item -> assertEquals item, findItemInMap(item, itemsByGroup) }
    }

    void testRemoveItemsFromGroupMapIgnoresUnknownItem()
    {
		def itemLeftOut = items.pop()
	 	def itemsByGroup = itemGroupService.transformToItemsByGroup(groups,items)
      itemGroupService.removeItemsFromGroupMap([itemLeftOut], itemsByGroup)
		assertNull findItemInMap(itemLeftOut,itemsByGroup)
		items.each{ item -> assertEquals item, findItemInMap(item, itemsByGroup) }
    }
}
