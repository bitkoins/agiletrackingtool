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
		group = Defaults.getGroups(1,[project])[0]
		items = Defaults.getItems(5,[group])
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
    	group.addItem(item)
    	project.save()
    	group.save()
    	assertTrue group.hasItem(item.id)
    	assertTrue item.group == group
    	
    	group.deleteItem(item.id)
    	group.save()
    	assertFalse group.hasItem(item.id)
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
    	def groupsA = Defaults.getGroups(2,[projectA])
    	def groupsB = Defaults.getGroups(3,Defaults.getProjects(3))
    	
    	def groups = groupsA + groupsB    	
    	groups.each{ it.project.save(); it.save() }
    	
    	def foundGroups = ItemGroup.findAllByProject(projectA) 
    	assertTrue foundGroups.size() == groupsA.size()
    	groupsA.each{ group -> assertTrue foundGroups.contains(group)  }
    	
    	groups.collect{ it.project }.unique()*.delete()
    }
    
    void testDeleteWholeGroup()
    {
    	def project = Defaults.getProjects(1)[0]
		def groups = Defaults.getGroups(2,[project])    
    	def (groupA, groupB) = groups
    	
    	def itemsA = Defaults.getItems(2,[groupA])
    	def itemsB = Defaults.getItems(3,[groupB])
    	items = (itemsA + itemsB)
    	
    	def iteration = Defaults.getIterations(1,project)[0]
    	
		project.save()    	    	
    	groups*.save()
    	iteration.save()    	
    	items.each{ it.save(); iteration.addItem(it) }
    	
    	groupA.deleteWholeGroup()
    	
    	assertTrue ItemGroup.count() == 1
    	assertTrue Item.count() == itemsB.size()
    	assertTrue iteration.items.size() == itemsB.size()

		iteration.delete()
		groupB.deleteWholeGroup()
		project.delete()
    }
}
