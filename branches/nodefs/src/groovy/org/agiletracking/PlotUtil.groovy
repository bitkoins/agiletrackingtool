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

class PlotUtil
{
	static def getPlotCurveForItemStatus(def overViews, def dates, def zeroDate, def legend, def status)
	{
		def overViewSelector = { overView -> overView.getPointsForItemStatus(status) }
		return _getPlotCurveWithOverViewSelector(overViews, dates, zeroDate, legend, overViewSelector)
	}
	
	static def getTotalPlotCurve(def overViews, def dates, def zeroDate, def legend)
	{
		def overViewSelector = { overView -> overView.totalPoints() }
		return _getPlotCurveWithOverViewSelector(overViews, dates, zeroDate, legend, overViewSelector)
	}
	
	static def getPlotCurveForView(def overViews, def dates, def zeroDate, def legend, def priority, def status)
	{
		def overViewSelector = { overView -> overView.getPointsForView(priority, status) }
		return _getPlotCurveWithOverViewSelector(overViews, dates, zeroDate, legend, overViewSelector)
	}
	
	static def getTotalPlotCurveForPriority(def overViews, def dates, def zeroDate, def legend, def priority)
	{
		def overViewSelector = { overView -> overView.getPointsForPriority(priority) }
		return _getPlotCurveWithOverViewSelector(overViews, dates, zeroDate, legend, overViewSelector)
	}
	
	static def _getPlotCurveWithOverViewSelector(def overViews, def dates, def zeroDate, def legend, def overViewSelector)
	{
		def curve = new PlotCurve(legend)
		if ( overViews.size() != dates.size() ) 
		{
			throw new Exception("Cannot create a plotCurve when the number of dates and overView values are different.")
		}
		
		overViews.each{ overView ->
			curve.yValues << overViewSelector(overView)
		}
		
		dates.each{ date ->
			curve.xValues << Util.getDaysInBetween(zeroDate,date)
		}
		
		return curve
	}
}
