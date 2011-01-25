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
package org.agiletracking

class ItemTests extends GroovyTestCase {

	def project 
	def projects
	def groups
	def items
	
	void setUp()
	{
		projects = Defaults.getProjects(2)
		project = projects[0]
		groups = Defaults.getGroups(2,project)
		items = Defaults.getItems(3,groups,project)
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
    	project.save()
		item.save()
    	
    	if ( !item.validate() )
    		item.errors.allErrors.each { println it }
    	assertNotNull item.save()
    }
       
    void testSubItemSave()
    {
    	def subItem = Defaults.getSubItems(1,[items[0]])[0]
    	project.save()
    	items[0].save()
    	
    	if ( !subItem.validate() )
    		subItem.errors.allErrors.each { println it }
    	
    	assertNotNull subItem.save()
    	subItem.delete()
    }
    
    void testAddingAndDeletingSubItems()
    {
    	def subItems = Defaults.getSubItems(10,[items[0]])
    	def item = items[0]
    	item.subItems = []
    	
    	item.addSubItem(subItems[0])
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
    	project.save()
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
    	assertTrue items[0].uid == 1
    }
   
    void testUidIsCorrectlyTakenWhenMultipleProjectsArePresent()
    {
	saveItemsAndGroups()
	
	def aValue = 234
	def otherProject = projects[1]
	def otherGroup = Defaults.getGroups(1,project)[0]
	otherGroup.save()
	def otherItems = Defaults.getItems(2,[otherGroup],otherProject)
	otherItems[-1].uid = aValue
	(otherItems)*.save()

        assertEquals aValue, Item.maxUid(otherProject)
	assertEquals items.max{it.uid}.uid, Item.maxUid(project) 
    }
    
    void testUniqueItemIdTakesMaximumValue()
    {
    	saveItemsAndGroups()
    	def maxItem = items[0]
    	def aValue = 1234
    	maxItem.uid = aValue
    	maxItem.save()
    	assertEquals aValue, Item.maxUid(project) 
    }

    void testRetrieveLatestItem()
    {
		/* Hard to test because when saving the item date is updated again. 
		 * Implementation wise it's the same as maxUid() */
		saveItemsAndGroups()
		assertNotNull Item.lastUpdateDateForProject(project).class
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

    
