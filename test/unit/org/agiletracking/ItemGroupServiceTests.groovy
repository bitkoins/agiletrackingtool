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
		
	itemsA = Defaults.getItems(2,[groupA],project,1)
     	itemsB = Defaults.getItems(3,[groupB],project,10)
     	items = itemsA + itemsB
     	mockDomain(Item, items)
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testDeleteWholeGroup()
    {
    	def iteration = Defaults.getIterations(1,project)[0]
    	mockDomain(Iteration, [iteration] )
    	
    	items.each{ iteration.addItem(it) }
    	
    	itemGroupService.deleteWholeGroup(groupA)
    	
    	assertEquals 1, ItemGroup.count()
    	assertEquals itemsB.size(), Item.count()
    	assertEquals itemsB.size(), iteration.items.size()
    }
    
    void testTransformToItemsByGroup()
    {
	def itemsByGroup = itemGroupService.transformToItemsByGroup(groups,items)
	
	assertEquals groups.size(), itemsByGroup.size()
	assertEquals itemsA, itemsByGroup[groupA] 
	assertEquals itemsB, itemsByGroup[groupB]
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
