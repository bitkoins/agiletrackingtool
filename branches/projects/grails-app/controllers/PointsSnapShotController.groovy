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

class PointsSnapShotController {
	def scaffold = PointsSnapShot
	
	static navigation = [
		group:'tags', 
		order:60, 
		title:'History',
		action:'plot', 
		subItems: [
			[action:'plot', order:1, title:"Overall history"],
			[action:'allPlots', order:10, title:'Group history'],			
			[action:'burnUpGraph', order:100, title:'Burn up graph'],
			[action:'flowPlot', order:80, title:'Flow history'],
			[action:'showBugHistory', order:50, title:'Bug history']
		]
	]
	
	def __getSnapShots(def now, def params)
	{
		def endTime = new Date()
		def startTime = endTime - 90
		
		if (params.dayRange) {
			def dayRange = params.dayRange.toInteger()
			endTime = now
			startTime = endTime - dayRange
		}
		else if ( params['startDate_year'])
		{
			def startCal = Calendar.getInstance()
			def endCal = Calendar.getInstance()
		
			startCal.set(Integer.parseInt(params['startDate_year']),Integer.parseInt(params['startDate_month'])-1,Integer.parseInt(params['startDate_day']))
			endCal.set(Integer.parseInt(params['endDate_year']),Integer.parseInt(params['endDate_month'])-1,Integer.parseInt(params['endDate_day']))
		
			startTime = startCal.getTime()
			endTime = endCal.getTime()
		}
		
		[ PointsSnapShot.getSnapShotsBetween(session.project,startTime, endTime), startTime, endTime]
	}
	
	def plot = {
			def now = new Date()
			def snapShots, startTime, endTime
			(snapShots,startTime, endTime) = __getSnapShots(now, params)
			
			def overViews = snapShots.collect{ it.overView }
			def dates = snapShots.collect{ it.date }
			
			def plotData = new PlotData("All points")
			
			plotData.yMin = plotData.curves.collect{ it.yValues?.min() }?.min()
			plotData.xLabel = "Days ago from now"
			plotData.yLabel = "Points"
			
			plotData.curves << PlotUtil.getPlotCurveForItemStatus(overViews,dates,now,"Finished",ItemStatus.Finished)
			plotData.curves << PlotUtil.getTotalPlotCurveForPriority(overViews,dates,now,"Total High priority", Priority.High)
			
			def curveCombined = PlotUtil.getTotalPlotCurveForPriority(overViews,dates,now,"Total", Priority.High).add(PlotUtil.getTotalPlotCurveForPriority(overViews,dates,now,"Total", Priority.Medium), "Total High-Medium Priority")
			plotData.curves << curveCombined
			
			return [startTime:startTime, endTime:endTime, plots:[plotData], formAction:"plot"]
	}
	
	def allPlots = {
		def snapShots, startTime, endTime
		(snapShots,startTime, endTime) = __getSnapShots(new Date(), params)
		def plots = []

		plots += _makePlots("for all items", snapShots.collect{it.overView}, snapShots.collect{it.date} )
			
		ItemGroup.list().each{ group ->
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
		
		render(view:"plot", model:[startTime:startTime, endTime:endTime, plots:plots, formAction:"allPlots"])
	}
	
	def showBugHistory = {
			def iterations = Iteration.list()?.findAll{ it.status != IterationStatus.FutureWork }
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
			
			render(view:"plot", model:[plots:[plotData]])
	}
	
	def flowPlot = {
			def now = new Date()
			def snapShots, startTime, endTime
			(snapShots,startTime, endTime) = __getSnapShots(now, params)
			
			def overViews = snapShots.collect{ it.overView }
			def dates = snapShots.collect{ it.date }
			
			def plotData = new PlotData("Flow plots")
			
			plotData.yMin = plotData.curves.collect{ it.yValues?.min() }?.min()
			plotData.xLabel = "Days ago from now"
			plotData.yLabel = "Points"
			
			plotData.curves << PlotUtil.getPlotCurveForItemStatus(overViews,dates,now,"Blocked",ItemStatus.Blocking)
			plotData.curves << PlotUtil.getPlotCurveForItemStatus(overViews,dates,now,"InProgress",ItemStatus.InProgress)
			
			render(view:"plot", model:[startTime:startTime, endTime:endTime, plots:[plotData], formAction:"flowPlot"])
	}
		
	def _makePlots(def title, def overViews,def dates) 
	{
		def now = new Date()
		
		def plotData = new PlotData("Finished points " + title)
		plotData.xLabel = "Days ago from now"
		plotData.yLabel = "Points"
		
		def plotDataTotal = new PlotData("Total points " + title)
		plotDataTotal.xLabel = "Days ago from now"
		plotDataTotal.yLabel = "Points"
		
		[Priority.High,Priority.Medium,Priority.Low].each{ prio ->
			plotData.curves << PlotUtil.getPlotCurveForView(overViews,dates,now,"${prio}-priority",prio, ItemStatus.Finished)
			plotDataTotal.curves << PlotUtil.getTotalPlotCurveForPriority(overViews,dates,now,"${prio}-priority",prio)
		}
		
		def plots = []
		plots << [plotData,plotDataTotal]		 
		return plots
	}
	
	def burnUpGraph = {		    	
			return [plotData:Iteration.getOngoingIteration()?.getBurnUpPlotData()]
	}
}
