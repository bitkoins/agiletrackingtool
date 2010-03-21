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

class PlanCalculator {
	def itemsByGroup 
	Points2DaysCalculator p2dCalc
	
	PlanCalculator(def items, def pointsPerDayMin, def pointsPerDayMax, def pointsUncertaintyPercentage)
	{
		p2dCalc = new Points2DaysCalculator()
		p2dCalc.pointsPerDayMin = pointsPerDayMin
		p2dCalc.pointsPerDayMax = pointsPerDayMax
		p2dCalc.pointsUncertaintyPercentage = pointsUncertaintyPercentage
		
		itemsByGroup = [:]
		items.each{
			if ( it.checkUnfinished() )
			{
				if ( !itemsByGroup.containsKey(it.group) ) itemsByGroup[it.group] = [it]
				else itemsByGroup[it.group] << it
			}
		}
	}
	
	def getGroups()
	{
		return itemsByGroup.keySet()
	}
	
	def getUnfinishedPoints(def group, def priority)
	{
		def sum = 0
		itemsByGroup[group].findAll{ it.priority == priority }?.each{ sum += it.points }
		return sum
	}
	
	def getWorkingDaysLeft(def group, def priority)
	{
		return points2Days(getUnfinishedPoints(group,priority));
	}
	
	def getWorkingDaysRangeLeft(def group, def priority)
	{
		return points2DaysRange(getUnfinishedPoints(group, priority))
	}
	
	def getUnfinishedPoints(def priority)
	{
		def points = 0
		getGroups()?.each{ group -> points += getUnfinishedPoints(group,priority) }
		return points
	}
	
	def getWorkingDaysLeft(def priority)
	{
		return points2Days(getUnfinishedPoints(priority))
	}
	
	def getWorkingDaysRangeLeft(def priority)
	{
		return points2DaysRange(getUnfinishedPoints(priority))
	}
	
	def getWorkingDaysRangeByPriority(def daysHoliday = 0)
	{
		def dateByPriority = [:]
		
		def range = this.getWorkingDaysRangeLeft(Priority.High)
		def nowDate = new Date() + daysHoliday
	
		dateByPriority[Priority.High] = (nowDate + range.min())..(nowDate+range.max())
		
		range = this.getWorkingDaysRangeLeft(Priority.Medium)
		dateByPriority[Priority.Medium] = (dateByPriority[Priority.High].min()+range.min())..(dateByPriority[Priority.High].max()+range.max()) 
		
		range = this.getWorkingDaysRangeLeft(Priority.Low)
		dateByPriority[Priority.Low] = (dateByPriority[Priority.Medium].min()+range.min())..(dateByPriority[Priority.Medium].max()+range.max())
		
		return dateByPriority
	}
	
	def points2DaysRange(def points)
	{
		return p2dCalc.points2DaysRange(points)
	}
	
	def points2Days(def points)
	{
		return p2dCalc.points2Days(points)
	}
}