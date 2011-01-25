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

class PlanCalculatorTests extends GroovyTestCase {
	def groups
	def calc
	def pointsPerDay
	def pointsPerDayMin
	def pointsPerDayMax
	def pointsUncertaintyPercentage
	def totalPoints
	def magicNr = 10
	
	void setUp() {
		totalPoints = 0
		groups = Defaults.getGroups(2)
		def itemsByGroup = [:]
		groups.eachWithIndex{ group, index ->
			itemsByGroup[group] = []
			def groupItems = Defaults.getItems(2,[group])				
			groupItems.each{ 
				it.status = ItemStatus.Request
				it.points = magicNr + index
				it.priority = Priority.High
				totalPoints += it.points
				itemsByGroup[group] << it
			}
		}
		
		pointsPerDayMin = 2.5
		pointsPerDayMax = 2.5
		pointsPerDay = 2.5 
		pointsUncertaintyPercentage = 0.0
		calc = new PlanCalculator(itemsByGroup,pointsPerDayMin,pointsPerDayMax,pointsUncertaintyPercentage)
	}
	
	void testGroupListing()
	{
		assert calc.getGroups().size() == groups.size()
	}
	
	void testUnfinishedPointsForGroup_WithUnFinishedItemsOfHighPriority()
	{
	   def nrGroups = groups.size()
		groups.eachWithIndex{ group,index ->
			assertTrue calc.getUnfinishedPoints(group,Priority.High) == nrGroups*(magicNr+index)
			assertTrue calc.getWorkingDaysLeft(group,Priority.High) == (nrGroups*(magicNr+index)/pointsPerDay).toInteger()
			assertTrue calc.getUnfinishedPoints(group,Priority.Low) == 0
		}
	}
	
	void testDaysWorkLeft()
	{
		assertTrue calc.getUnfinishedPoints(Priority.High) == totalPoints
		assertTrue calc.getWorkingDaysLeft(Priority.High) == (totalPoints/pointsPerDay).toInteger()
		assertTrue calc.getUnfinishedPoints(Priority.Low) == 0
		assertTrue calc.getWorkingDaysLeft(Priority.Low) == 0
	}
	
	static def round = { Math.round(it).toInteger() }
	void testDaysLeftRange()
	{
		calc.p2dCalc.pointsPerDayMin = 2
		calc.p2dCalc.pointsPerDayMax = 3
		
		def minDays = calc.getWorkingDaysRangeLeft(Priority.High).min()
		def maxDays = calc.getWorkingDaysRangeLeft(Priority.High).max()
	
		assertEquals( round(totalPoints/calc.p2dCalc.pointsPerDayMin), maxDays )
		assertEquals( round(totalPoints/calc.p2dCalc.pointsPerDayMax), minDays )

		def expectedDuration = (totalPoints/calc.p2dCalc.pointsPerDayMin + 
										totalPoints/calc.p2dCalc.pointsPerDayMax)/2
		assertEquals( round(expectedDuration), calc.getWorkingDaysLeft(Priority.High) )
	}
	
	void testPointsUncertaintyPercentagePositive()
	{
		calc.p2dCalc.pointsUncertaintyPercentage = 10
		def range = calc.getWorkingDaysRangeLeft(Priority.High)
		assertEquals( round(range.min()*1.10), range.max() )
	}
	
	
}
