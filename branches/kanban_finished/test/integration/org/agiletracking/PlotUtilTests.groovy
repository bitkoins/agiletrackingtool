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

class PlotUtilTests extends GroovyTestCase
{
	def legend
	def nowDate
	def overViews 
	def dates 
	
	void setUp()
	{
		legend = "Any legend"
		nowDate = new Date()
		overViews = []
		dates = []
		
		3.times{ index ->
			dates << nowDate+index
			overViews <<  Defaults.getPointsOverView()
		}
	}
	
	void testCreatingCurveForAnyItemStatus()
	{
		ItemStatus.each{ status -> 
			def curve = PlotUtil.getPlotCurveForItemStatus(overViews,dates,nowDate,legend, status)
	
			assertTrue curve.legend == legend
			assertTrue curve.xValues.size() == dates.size()
			assertTrue curve.yValues.size() == overViews.size()
			
			overViews.eachWithIndex{ overView, index -> 
				assertTrue curve.xValues[index] == index 
				assertTrue curve.yValues[index] == overView.getPointsForItemStatus(status) 
			}
		}
	}
	
	void testCreatingTotalCurve()
	{
		def curve = PlotUtil.getTotalPlotCurve(overViews,dates,nowDate,legend)
		overViews.eachWithIndex{ overView, index ->
			assertTrue curve.yValues[index] == overView.getTotalPoints()
		}
	}
		
	def closureForTestingPriorityCurve = { prio, itemStatus, curve ->
		overViews.eachWithIndex{ overView, index -> 
			assertTrue curve.xValues[index] == index 
			assertTrue curve.yValues[index] == overView.getPointsForView(prio,itemStatus)  
		}
	}
	
	void testCreatingFinishedCurveForAnyPriorityAndItemStatus()
	{
		
		Priority.each{ prio -> ItemStatus.each{ status -> 
			def curve = PlotUtil.getPlotCurveForView(overViews,dates,nowDate,legend,prio,status)
			closureForTestingPriorityCurve(prio, status, curve )
			}
		}
	}
		
	
	void testCreatingTotalCurveForAnyPriority()
	{
		Priority.each{ prio ->
			def curve = PlotUtil.getTotalPlotCurveForPriority(overViews,dates,nowDate,legend, prio)
			overViews.eachWithIndex{ overView, index -> 
				assertTrue curve.xValues[index] == index 
				assertTrue curve.yValues[index] == overView.getPointsForPriority(prio)
			}
		}
	}	
}
