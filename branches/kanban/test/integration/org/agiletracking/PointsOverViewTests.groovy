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

class PointsOverViewTests extends GroovyTestCase {
	def groups 
	def items
	def snapShot
	def defaultItemPoint = 2
	def overView
		
	void setUp() {
		groups = Defaults.getGroups(3)
		items = Defaults.getItems(3,groups)
		items.each{ it.itemPoints = defaultItemPoint }
		overView = new PointsOverView()
	}
	
	void tearDown() {
	}
	
	void testInitialValuesAreZero()
	{
		assertTrue overView.getTotalPoints() == 0
	}
	
	void testPointsForItemStatusAreInitiallyZero()
	{
		ItemStatus.each{ status -> 
			assertTrue overView.getPointsForItemStatus(status) == 0
		}
	}
	
	void testPointsForPriorityAreInitiallyZero()
	{
		Priority.each{ prio ->
			assertTrue overView.getPointsForPriority(prio) == 0
		}
	}
	
	void testPointsForViewAreInitiallyZero()
	{
		Priority.each{ prio -> ItemStatus.each{ status ->
			assertTrue overView.getPointsForView(prio,status) == 0
			}
		}
	}
	
	void testPointsForViewAreSetCorrectly()
	{
		def points = 10
		Priority.each{ prio -> ItemStatus.each{ status ->
			overView.setPointsForView(prio,status,points)
			assertTrue overView.getPointsForView(prio,status) == points
			points += 1
			}
		}
	}
	
	void testPriorityIsIntegratedCorrectly()
	{
		def points = 3
		ItemStatus.each{ status ->
			def sum = 0
			Priority.each{ prio ->  
				overView.setPointsForView(prio,status,points) 
				sum += points
			}
			assertTrue overView.getPointsForItemStatus(status) == sum
		}
	}
	
	void testItemStatusIsIntegratedCorrectly()
	{
		def points = 3
		Priority.each{ prio ->
			def sum = 0
			ItemStatus.each{ status ->
			overView.setPointsForView(prio,status,points) 
			sum += points
			}	
			assertTrue overView.getPointsForPriority(prio) == sum
		}
	}
	
	void testEqual()
	{
		overView = Defaults.getPointsOverView()
		assertTrue overView.equals(overView)
	}
	
	void testNotEqual()
	{
		assertTrue overView.equals(Defaults.getPointsOverView())
	}
	
	void testAddingPointsForView()
	{
		def sum = 0
		def prio = Priority.High
		def status = ItemStatus.Finished
		2.times{ value -> 
			overView.addPointsForView(prio,status,value)
			sum += value
			assertTrue overView.getPointsForView(prio,status) == sum
			assertTrue overView.getTotalPoints() == sum
		}
	}
	
	void testCreateOverView()
	{
		overView = PointsOverView.createOverView(items)
		assertTrue overView.getTotalPoints() == defaultItemPoint * items.size()
	}
	
	void testTiming()
	{
		250.times{ points -> 
			Priority.each{ prio -> ItemStatus.each{ status -> overView.setPointsForView(prio,status,points) }}
			Priority.each{ prio -> ItemStatus.each{ status -> overView.getPointsForView(prio,status) }}
		}
	}
}








