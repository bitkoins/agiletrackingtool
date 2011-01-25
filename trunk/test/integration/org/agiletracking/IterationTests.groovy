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

class IterationTests extends GroovyTestCase {
	
	Iteration iter
	def items
	def groups
	 
	def project
	
	void setUp() {
		project = Defaults.getProjects(1)[0]
		project.save()
	
		iter = Defaults.getIterations(1,project)[0]
		iter.endTime = iter.startTime + 10
		iter.save()
			
		groups = Defaults.getGroups(2,project)
    	groups*.save()
    	items = Defaults.getItems(3,groups,project)
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
    	assertTrue iter.calculateDaysLeft(now) == 5
    	assertTrue iter.calculateDurationInDays() == 10
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
    
    void testDeleteItemFromIteration()
    {
    	iter.deleteItem(items[0].id)
    	assertFalse iter.hasItem(items[0].id)
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
    	assertTrue iter.calculatePointsPerDay() == nrFinishedItems*100/10
    	assertTrue iter.calculateFinishedPoints() == 100*nrFinishedItems
    	assertTrue iter.calculateTotalPoints() == 100*items.size()
    }
    
    void testStrangeArtifactOnListMethodOfIterationProbablyRelatedToEagerFetchingOfItems()
    {
    	def iters = Defaults.getIterations(10,project)
    	items.each{ iters[2].addItem(it) }
    	iters*.save()
    	assertTrue Iteration.list().size() == 11    	
    }
}
