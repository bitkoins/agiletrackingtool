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

class ForecastController {
	
	static navigation = [
		group:'tags', 
		order:80, 
		title:'Forecast', 
		action:'forecast'
	]
	
	def forecast = {
		render(view:'forecast')	
	}
	
	def calculatePlan = 
	{
	    def daysPerWeek = 7.0
		def pointsPerDayMin = params.pointsPerWeekMin ? Double.parseDouble(params.pointsPerWeekMin) / daysPerWeek : 1.0
		def pointsPerDayMax = params.pointsPerWeekMax ? Double.parseDouble(params.pointsPerWeekMax) / daysPerWeek : 1.0
		def pointsUncertaintyPercentage = params.pointsUncertaintyPercentage ? Double.parseDouble(params.pointsUncertaintyPercentage) : 0
	    def daysHoliday = params.weeksHolidays ? Math.round(Double.parseDouble(params.weeksHolidays) * daysPerWeek).toInteger() : 0
	    	    		
		def planCalculator = new PlanCalculator(Item.list().unique(),pointsPerDayMin,pointsPerDayMax, pointsUncertaintyPercentage)
		def dateByPriority = planCalculator.getWorkingDaysRangeByPriority(daysHoliday)
		
		def iterations = Iteration.list().unique().findAll{ it.status != IterationStatus.Finished }.sort{it.startTime }
		
		render(template:'forecastResult',model:[planCalculator:planCalculator, dateByPriority:dateByPriority, iterations:iterations] )
	}
}
