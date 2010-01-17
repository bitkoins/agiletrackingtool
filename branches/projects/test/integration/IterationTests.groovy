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

class IterationTests extends GroovyTestCase {
	
	Iteration iter
	def items
	def groups
	def projects 
	def project
	
	void setUp() {
		projects = Defaults.getProjects(1)
		projects*.save()
		project = projects[0]
	
		iter = Defaults.getIterations(1,project)[0]
		iter.endTime = iter.startTime + 10
		iter.save()
	
			
		groups = Defaults.getGroups(5,projects)
    	groups*.save()
    	items = Defaults.getItems(5,groups)
    	items*.save()    		
		items.each{ item ->
			item.points = 100
			item.status = ItemStatus.Finished
			iter.addItem(item)
		}
		items[0].status = ItemStatus.Blocking
	}
	
	void tearDown() {
		Util.emptyDataBase()
	}

    void testSave() 
    {
    	if ( !iter.validate() )
    		iter.errors.allErrors.each { println it }
    	
    	assertNotNull iter.save()
    }
    
    void testDurationAndDaysLeft()
    {
    	def now = new Date()
    	iter.startTime = now - 5
    	iter.endTime = now + 5
    	assertTrue iter.getDaysLeft(now) == 5
    	assertTrue iter.getDurationInDays() == 10
    }
    
    void testChangingItems()
    {
    	assertTrue iter.items.size() == items.size()
    	
    	iter.addItem(items[1])
    	assertTrue iter.items.size() == items.size()
    	
    	assertNotNull iter.getItem(items[1].id)
    	assertTrue iter.hasItem(items[1].id)
    	
    	iter.deleteItem(items[0].id)
    	assertTrue iter.items.size() == (items.size()-1)
    }
    
    void testSetIterationForItem()
    {
    	def newItem = Defaults.getItems(1,groups)[0]
    	newItem.save()
    	assertTrue !newItem.iteration
    	def newIteration= Defaults.getIterations(1,project)[0]
    	newIteration.addItem(newItem)
    	newIteration.save()
    	
    	assertTrue newItem.iteration == newIteration
    	assertTrue newIteration.hasItem(newItem.id)
    }
    
    void testMoveItemToOtherIteration()
    {
    	assertTrue iter.hasItem(items[0].id)
    	def newIteration= Defaults.getIterations(1,project)[0]
    	newIteration.save()
    	newIteration.addItem(items[0])
    	assertTrue newIteration.hasItem(items[0].id)
    	assertFalse iter.hasItem(items[0].id)
    }
    
    void testDeleteItemFromIteration()
    {
    	iter.deleteItem(items[0].id)
    	assertFalse iter.hasItem(items[0].id)
    	assertNull items[0].iteration
    }
    
    void testDeleteIteration()
    {
    	def id = iter.id
    	iter.unloadItemsAndDelete()
    	assertNull Iteration.get(id)
    }
    
    void testGroupListing()
    {
    	items.each{ it.group = groups[0]; it.status = ItemStatus.Request  }
    	items[1].group = groups[1]
    	items[2].group = groups[2]
    	
    	assertTrue iter.listGroups()?.size() == 3 
    	assertTrue iter.listUnfinishedItemsForGroup(groups[0])?.size() == (items.size()-2)
    	assertTrue iter.listUnfinishedItemsForGroup(groups[1])?.size() == 1    			
    	assertTrue iter.listUnfinishedItemsForGroup(groups[1])[0] == items[1]
    	assertTrue iter.listUnfinishedItemsForGroup(groups[2])?.size() == 1  
    	assertTrue iter.listUnfinishedItemsForGroup(groups[2])[0] == items[2]
    	
    	items[2].status = ItemStatus.Finished
    	assertTrue iter.listUnfinishedItemsForGroup(groups[2])?.size() == 0
    }
    
    void testGetOngoingIteration()
    {
    	def iters = Defaults.getIterations(10,project)
    	iters.each{ it.status = IterationStatus.Finished ; it.save() }    	
    	def current = iters[5]
    	current.status = IterationStatus.Ongoing  	
    	def retrievedCurrent = Iteration.getOngoingIteration(project)
    	
    	assertTrue current.id == retrievedCurrent.id
    }
    
    void testGetNextIteration_WithNoFutureIterations()
    {
    	assertNull iter.retrieveNextIteration()
    }
    
    void testGetNextIteration_WithFinishedFutureIteration()
    {
    	def iterNext = Defaults.getIterations(1,project)[0]
    	iterNext.startTime = iter.startTime+10
    	iterNext.endTime = iter.endTime + 10
    	iterNext.status = IterationStatus.Finished 
    	iterNext.save()
    	
    	assertNull iter.retrieveNextIteration()
    	iterNext.delete()
    }
    
    void testGetNextIteration_WithTwoFutureIterationsPresent()
    {
    	def iters = []
    	2.times { 
	    	def iterNext = Defaults.getIterations(1,project)[0]
	    	iterNext.startTime = iter.startTime+10*(it+1)
	    	iterNext.endTime = iter.endTime + 10*(it+1)
	    	iterNext.save()
	    	iters << iterNext
    	}
    	
    	assertTrue iter.retrieveNextIteration() == iters[0]    	
    }
    
    void testCreateNextIteration_WillCreateAnIterationWithTheUnfinishedItems()
    {
    	def iterNew = iter.createTheNextIteration()
    	iterNew.save()
    	assertTrue iterNew.items.size() == 1
    	assertTrue iterNew.hasItem(items[0].id)
    }
    
    void testCreateNextIteration_WillCreateWithTheSameLength()
    {
    	def iterNew = iter.createTheNextIteration()
    	iterNew.save()
    	assertTrue Util.getDaysInBetween(iterNew.startTime,iterNew.endTime) == 10
    }
    
    void testCloseIteration_WillKeepFinishedItemsAndSetStatusToFinished()
    {
    	iter.closeIteration()

    	assertTrue iter.status == IterationStatus.Finished
    	assertTrue iter.items.size() == (items.size() -1)
    }
     
    void testPointsPerDay()
    {
    	def nrFinishedItems = items.size()-1
    	assertTrue iter.getPointsPerDay() == nrFinishedItems*100/10
    	assertTrue iter.getFinishedPoints() == 100*nrFinishedItems
    	assertTrue iter.totalPoints() == 100*items.size()
    }
    
    void testStrangeArtifactOnListMethodOfIterationProbablyRelatedToEagerFetchingOfItems()
    {
    	def iters = Defaults.getIterations(10,project)
    	items.each{ iters[2].addItem(it) }
    	iters*.save()
    	assertTrue Iteration.list().size() == 11    	
    }
}
