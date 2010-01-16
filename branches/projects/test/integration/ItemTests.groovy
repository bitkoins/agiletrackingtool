/*----------------------------------------------------------------------------
Project: Agile Tracking Tool

Copyright 2008, 2009   Ben Schreur
------------------------------------------------------------------------------
This file is part of Agile Tracking Tool.

Agile Tracking Tool is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Agile Tracking Tool is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Agile Tracking Tool.  If not, see <http://www.gnu.org/licenses/>.
------------------------------------------------------------------------------*/

class ItemTests extends GroovyTestCase {

	def projects
	def groups
	def items
	
	void setUp()
	{
		projects	 = Defaults.getProjects(1)
		groups = Defaults.getGroups(2,projects)
		items = Defaults.getItems(5,groups)
	}
	
	void tearDown()
	{
		items*.delete()
		projects*.delete()
		groups*.delete()
	}
	
	void saveItemsAndGroups()
	{
		projects*.save()
		groups*.save()
	}
	
    void testSave() {
    	def item = items[0]
    	item.group.project.save()
		item.group.save()
    	
    	if ( !item.validate() )
    		item.errors.allErrors.each { println it }
    	assertNotNull item.save()
    }
    
    void testGetUnfinishedItemsGroupMap()
    {
    	saveItemsAndGroups()
    	def nr = 0
    	    	
    	items.each{ it.status = ItemStatus.Blocking; }
    	def itemsGroupMap = Item.getUnfinishedItemsGroupMap()
    	itemsGroupMap.each{group,itemList->
    		nr += itemList.size() 
    	}  
    	
    	assertTrue nr == items.size()
    }
    
    void testGetUnfinishedItemsGroupMapWhenAllItemsAreFinished()
    {
    	saveItemsAndGroups()
    	def nr = 0
    	items.each{ it.status = ItemStatus.Finished; }
    	def itemsGroupMap = Item.getUnfinishedItemsGroupMap()
    	itemsGroupMap.each{group,items-> nr += items.size() }    	   	
    	assertTrue nr == 0
    }
    
    void testGetUnfinishedItemsGroupMapForOnePriority()
    {
    	saveItemsAndGroups()
    	def nr = 0
    	def prios = [Priority.High]
    	def itemsGroupMap = Item.getUnfinishedItemsGroupMap(prios)
    	itemsGroupMap.each{group,items-> nr += items.size() }    	   	
    	assertTrue nr == items.findAll{ it.priority == prios[0] }.size() 
    }
    
    void testGetUnfinishedItemsGroupMapForMorePriorities()
    {
    	saveItemsAndGroups()
    	def nr = 0
    	items.each{ it.priority = Priority.Low }
    	def prios = [Priority.High, Priority.Low]
    	def itemsGroupMap = Item.getUnfinishedItemsGroupMap(prios)
    	itemsGroupMap.each{group,items-> nr += items.size() }    	   	
    	assertTrue nr == items.size() 
    }
    
    void testSubItemSave()
    {
    	def subItem = Defaults.getSubItems(1,[items[0]])[0]
    	items[0].save()
    	
    	assertTrue subItem.item.id == items[0].id
    	
    	if ( !subItem.validate() )
    		subItem.errors.allErrors.each { println it }
    	
    	assertNotNull subItem.save()
    	subItem.delete()
    }
    
    void testAddingAndDeletingSubItems()
    {
    	def subItems = Defaults.getSubItems(10,[items[0]])
    	subItems.each{ it.item = null }
    	def item = items[0]
    	item.subItems = []
    	
    	item.addSubItem(subItems[0])
    	assertTrue subItems[0].item.id == items[0].id
    	assertTrue item.subItems.size() == 1
    	
    	def deletedSubItemId = subItems[0].id
    	item.deleteSubItem(deletedSubItemId)
    	assertTrue item.subItems.size() == 0
    	assertNull SubItem.get(deletedSubItemId)
    	
    	subItems.each{ item.addSubItem(it) }
    	assertTrue item.subItems.size() == subItems.size()
    }
    
    void testPointsWithSubItems()
    {
    	def subItems = Defaults.getSubItems(10,[items[0]])
    	def item = items[0]
    		
    	item.points = subItems.size()*1 + 5
    	subItems.each{ it.points = 1 }
    	item.save()
    	item = Item.get(items[0].id)
    		
    	assertTrue item.points == subItems.size()*1 + 5
    	assertTrue item.getPoints() == subItems.size()*1 + 5
    		
    	item.points = 4
    	assertTrue item.points == subItems.size()*1
    	assertTrue item.getPoints() == subItems.size()*1
    }
        
    void testUniqueItemIdIsInitiallyOne()
    {   
    	def item = items[0]
    	item.stampUid()
    	assertTrue item.uid == 1
    }
    
    void testUniqueItemIdTakesMaximumValue()
    {
    	saveItemsAndGroups()
    	def maxItem = items[0]
    	def aValue = 1234
    	maxItem.uid = items.size() + aValue
    	maxItem.save()
    	def newItem = new Item()
    	newItem.stampUid()
    	assertTrue newItem.uid == (items.size()+aValue+1)
    }
    
    void testItemHasNoCriteria()
    {
    	def values = [null, "  ", " ", ""]
    	values.each{
    		items[0].criteria = null
        	assertFalse items[0].hasCriteria()	
    	}
    }
    
    void testItemHasCriteria()
    {
    	items[0].criteria = " something? "
        assertTrue items[0].hasCriteria()    	
    }
}
    
