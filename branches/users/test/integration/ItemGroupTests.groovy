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

class ItemGroupTests extends GroovyTestCase {
	
	def group
	def items
	def project
	
	void setUp()
	{
		project = Defaults.getProjects(1)[0]
		group = Defaults.getGroups(1,project)[0]
		items = Defaults.getItems(5,[group],project)
	}
	
	void tearDown()
	{
		ItemGroup.list()*.delete()
	}

    void testSave() {
    	if ( !group.validate() )
    		group.errors.allErrors.each { println it }
    	project.save()
    	assertNotNull group.save()
    	project.delete()
    }
    
    void testAddingAndDeletingItem()
    {
    	def item = items[0]
    	def otherGroup = Defaults.getGroups(1,project)[0]
    	otherGroup.id = group.id + 1
    	
    	otherGroup.addItem(item)
    	
    	project.save()
    	group.save()
    	assertTrue otherGroup.hasItem(item.id)
    	assertEquals item.group , otherGroup
    	
    	assertFalse group.hasItem(item.id)
    	
    	otherGroup.deleteItem(item.id)
    	otherGroup.save()
    	assertFalse otherGroup.hasItem(item.id)
    	assertTrue item.group == null
    	project.delete()
    }
    
    void testAddingMultipleItems()
    {
    	items.each{ item -> group.addItem(item) }
    	assertTrue group.items.size() == items.size() 
    }
    
    void testFinishedPointsWhenAllPointsAreFinished()
    {
    	items.each{ it.status = ItemStatus.Finished }
    	assertTrue group.finishedPoints() == items.sum{ it.points }
    }
    	
    void testFinishedPointsWhenAllPointsAreRequest()
    {
       	items.each{ it.status = ItemStatus.Request }
       	assertTrue group.finishedPoints() == 0
    }
    
    void testFindByProject()
    {
    	def projectA = Defaults.getProjects(1)[0]
    	
    	def groupsA = Defaults.getGroups(2,projectA)
    	def groupsB = Defaults.getGroups(3, Defaults.getProjects(1)[0])
    	
    	def groups = groupsA + groupsB    	
    	groups.each{ it.project.save(); it.save() }
    	
    	def foundGroups = ItemGroup.findAllByProject(projectA) 
    	assertTrue foundGroups.size() == groupsA.size()
    	groupsA.each{ group -> assertTrue foundGroups.contains(group)  }
    	
    	groups.collect{ it.project }.unique()*.delete()
    }
    
    void testSavingGroupCascadesToItem()
    {
    	project.save()
    	def groupA = Defaults.getGroups(1,project)[0]
    	def groupB = Defaults.getGroups(1,project)[0]
    	[groupA,groupB]*.save()
    	
    	def itemA = Defaults.getItems(1,[groupA],project)[0]
    	itemA.save()
    	
    	groupB.addItem(itemA)
    	groupB.save()
    	
    	def savedItemA = Item.get(itemA.id)
    	assertEquals groupB.id, savedItemA.group.id   	
    }
    
}
