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

class PlanCalculatorTests extends GroovyTestCase {
	
	def items
	def groups
	def calc
	def pointsPerDay
	def pointsPerDayMin
	def pointsPerDayMax
	def pointsUncertaintyPercentage
	def totalPoints
	
	void setUp() {
		items = []
		totalPoints = 0
		groups = Defaults.getGroups(5)
		groups.eachWithIndex{ group, index ->
			def groupItems = Defaults.getItems(5,[group])	
			groupItems.each{ 
				it.status = ItemStatus.Request
				it.points = 10 + index
				it.priority = Priority.High
				totalPoints += it.points
				items << it
			}
		}
		
		pointsPerDayMin = 2.5
		pointsPerDayMax = 2.5
		pointsPerDay = 2.5 
		pointsUncertaintyPercentage = 0.0
		calc = new PlanCalculator(items,pointsPerDayMin,pointsPerDayMax,pointsUncertaintyPercentage)
	}
	
	void testGroupListing()
	{
		assert calc.getGroups().size() == groups.size()
	}
	
	void testUnfinishedPointsForGroup_WithUnFinishedItemsOfHighPriority()
	{
		groups.eachWithIndex{ group,index ->
			assertTrue calc.getUnfinishedPoints(group,Priority.High) == 5*(10+index)
			assertTrue calc.getWorkingDaysLeft(group,Priority.High) == (5*(10+index)/pointsPerDay).toInteger()
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
	
	void testDaysLeftRange()
	{
		calc.p2dCalc.pointsPerDayMin = 2.0
		calc.p2dCalc.pointsPerDayMax = 3.0
		
		def minDays = calc.getWorkingDaysRangeLeft(Priority.High).min()
		def maxDays = calc.getWorkingDaysRangeLeft(Priority.High).max()
		
		assertTrue maxDays == (totalPoints/calc.p2dCalc.pointsPerDayMin).toInteger()
		assertTrue minDays == (totalPoints/calc.p2dCalc.pointsPerDayMax).toInteger()
		assertTrue calc.getWorkingDaysLeft(Priority.High) == (minDays+maxDays)/2.0
	}
	
	void testPointsUncertaintyPercentagePositive()
	{
		calc.p2dCalc.pointsUncertaintyPercentage = 10
		def range = calc.getWorkingDaysRangeLeft(Priority.High)
		assertTrue range.max() == (range.min()*1.10).toInteger()
	}
	
	
}