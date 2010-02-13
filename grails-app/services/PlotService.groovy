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

class PlotService {
    static transactional = true
    static scope = "session"
        
    def createIterationHistoryPlot(def iterations)
    {
    	def plotData = new PlotData("Iteration history")
		plotData.xLabel = "Days ago from now"
		plotData.yLabel = "Points/Week"
		plotData.yMin = 0
			
		def plotCurve = new PlotCurve("PointsPerWeek")
		def now = new Date()
		
		iterations?.each{ iteration ->
			def daysFromNow = Util.getDaysInBetween(now, iteration.endTime)
			if ( daysFromNow < 0 )
			{
				plotCurve.xValues << daysFromNow
				plotCurve.yValues << iteration.getPointsPerDay()*7
			}
		}
		plotData.curves << plotCurve
		
		return plotData
    
    }
    
    def createPointsHistoryPlot(def snapShots)
    {
    	def overViews = snapShots.collect{ it.overView }
		def dates = snapShots.collect{ it.date }
	
    	def plotData = new PlotData("All points")
		plotData.yMin = plotData.curves.collect{ it.yValues?.min() }?.min()
		plotData.xLabel = "Days ago from now"
		plotData.yLabel = "Points"

		def now = new Date()		
		plotData.curves << PlotUtil.getPlotCurveForItemStatus(overViews,dates,now,"Finished",ItemStatus.Finished)
		plotData.curves << PlotUtil.getTotalPlotCurveForPriority(overViews,dates,now,"Total High priority", Priority.High)
			
		def curveCombined = PlotUtil.getTotalPlotCurveForPriority(overViews,dates,now,"Total", Priority.High).add(PlotUtil.getTotalPlotCurveForPriority(overViews,dates,now,"Total", Priority.Medium), "Total High-Medium Priority")
		plotData.curves << curveCombined
			
		return plotData
	}
	
	def createGroupPointsHistoryPlots(def snapShots, def groups)
	{
		def plots = []
		plots += _makePlots("for all items", snapShots.collect{it.overView}, snapShots.collect{it.date} )
			
		groups.each{ group ->
			def title = "for ${group}"
			def overViews = []
			def dates = []
				
			snapShots.each{ snapShot -> 
				def pointsForGroup = snapShot.getPointsForGroup(group)
				if (pointsForGroup)
				{
					overViews << pointsForGroup.overView
					dates << snapShot.date
				}
			}
	
			plots += _makePlots(title, overViews,dates)
		}	
		return plots
	}
	
	def createFlowPlot(def snapShots)
	{
		def overViews = snapShots.collect{ it.overView }
		def dates = snapShots.collect{ it.date }
			
		def plotData = new PlotData("Flow plots")
		
		plotData.yMin = plotData.curves.collect{ it.yValues?.min() }?.min()
		plotData.xLabel = "Days ago from now"
		plotData.yLabel = "Points"
		def now = new Date()
		
		plotData.curves << PlotUtil.getPlotCurveForItemStatus(overViews,dates,now,"Blocked",ItemStatus.Blocking)
		plotData.curves << PlotUtil.getPlotCurveForItemStatus(overViews,dates,now,"InProgress",ItemStatus.InProgress)
		
		return plotData
	}
			
	def _makePlots(def title, def overViews,def dates) 
	{
		def plotData = new PlotData("Finished points " + title)
		plotData.xLabel = "Days ago from now"
		plotData.yLabel = "Points"
		
		def plotDataTotal = new PlotData("Total points " + title)
		plotDataTotal.xLabel = "Days ago from now"
		plotDataTotal.yLabel = "Points"

		def now = new Date()		
		[Priority.High,Priority.Medium,Priority.Low].each{ prio ->
			plotData.curves << PlotUtil.getPlotCurveForView(overViews,dates,now,"${prio}-priority",prio, ItemStatus.Finished)
			plotDataTotal.curves << PlotUtil.getTotalPlotCurveForPriority(overViews,dates,now,"${prio}-priority",prio)
		}
		
		def plots = []
		plots << [plotData,plotDataTotal]		 
		return plots
	}
	
	def createBurnUpPlotData(def iteration)
    {
        def plotData = new PlotData("BurnDown chart") 
        plotData.xLabel = "Days since iteration start"
        plotData.yLabel = "Points"
        
		def snapShots = PointsSnapShot.getSnapShotsBetween(iteration.project,iteration.startTime,iteration.endTime)
		def dates = snapShots.collect{it.date}
		def overViews = snapShots.collect{it.overView}

        def now = new Date()				
		dates = dates.collect{it + (now-iteration.startTime)}
		def iterationCurve = PlotUtil.getPlotCurveForItemStatus(overViews,dates, now,"Finished", ItemStatus.Finished)
		if ( iterationCurve.yValues.size() > 0 ) {
			iterationCurve.yValues = iterationCurve.yValues.collect{ iteration.totalPoints() - (it - iterationCurve.yValues[0]) }
		} 
		plotData.curves << iterationCurve
				
		def nominalCurve = new PlotCurve("Steady Pace")
		nominalCurve.xValues = [0, iteration.endTime-iteration.startTime]
		nominalCurve.yValues = [iteration.totalPoints(), 0]
		plotData.curves << nominalCurve  
	
        return plotData
   }
   
   def createBugHistoryPlot(def iterations)
   {
		def plotCurve = new PlotCurve("Number of bugs solved")
		
		def now = new Date()	
		def totalBugs = 0
		iterations.sort{ it.endTime}.each{ iter ->
			def nrBugs = 0
			iter.items.each{ item ->
				if( item.group.name.contains("Bug") && (item.status == ItemStatus.Finished) ) nrBugs++
			}
			
			def daysAgo = Util.getDaysInBetween(now, iter.endTime)
			
			if (true || (daysAgo <= 0) )  {
				plotCurve.xValues << daysAgo
				totalBugs += nrBugs
				plotCurve.yValues << totalBugs					
			}
		}
			
		def total = plotCurve.yValues.sum{ it }
		def plotData = new PlotData("Bug history (${totalBugs} solved)")
		plotData.curves << plotCurve
		plotData.xLabel = "Days ago from now"
		plotData.yLabel = "Number of bugs"
		plotData.xMin = plotCurve.xValues.min()
		plotData.xMax = plotCurve.xValues.max()
		plotData.yMin = 0
		plotData.yMax = plotCurve.yValues.max()
		   		
		return plotData
   }
	
}
