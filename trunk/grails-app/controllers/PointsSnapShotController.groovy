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
	
	def plotService
	
	static navigation = [
		group:'tags', 
		order:60, 
		title:'History',
		action:'plot',
		isVisible: { session.project != null }, 
		subItems: [
			[action:'plot', order:1, title:"Overall history"],
			[action:'allPlots', order:10, title:'Group history'],			
			[action:'burnUpGraph', order:100, title:'Burn up graph'],
			[action:'flowPlot', order:80, title:'Flow history'],
			[action:'showBugHistory', order:50, title:'Bug history']
		]
	]
	
	def __startAndEndTimes(def params)
	{
		def startTime, endTime
		
		if(params.startTime && params.endTime) {
			startTime = params.startTime
			endTime = params.endTime
		}
		else {
			endTime = new Date()
			startTime = endTime - 90
		}
		
		return [startTime,endTime]
	}
	
	def plot = {
			def startTime, endTime
			(startTime,endTime) = __startAndEndTimes(params)
			def snapShots =  PointsSnapShot.getSnapShotsBetween(session.project,startTime, endTime)
			
			def plotData = plotService.createPointsHistoryPlot(snapShots)
			
			return [startTime:startTime, endTime:endTime, plots:[plotData], formAction:"plot"]
	}
	
	def allPlots = {
		def startTime, endTime
		(startTime,endTime) = __startAndEndTimes(params)
		def snapShots =  PointsSnapShot.getSnapShotsBetween(session.project,startTime, endTime)
		def plots = plotService.createGroupPointsHistoryPlots(snapShots, ItemGroup.findAllByProject(session.project) )
		
		render(view:"plot", model:[startTime:startTime, endTime:endTime, plots:plots, formAction:"allPlots"])
	}
	
	def showBugHistory = {
		def iterations = Iteration.findAllByProject(session.project)?.findAll{ it.status != IterationStatus.FutureWork }
		def plotData = plotService.createBugHistoryPlot(iterations)
		render(view:"plot", model:[plots:[plotData]])
	}
	
	def flowPlot = {
		def startTime, endTime
		(startTime,endTime) = __startAndEndTimes(params)
		def snapShots =  PointsSnapShot.getSnapShotsBetween(session.project,startTime, endTime)
		
		def plotData = plotService.createFlowPlot(snapShots)
		
		render(view:"plot", model:[startTime:startTime, endTime:endTime, plots:[plotData], formAction:"flowPlot"])
	}
	
	def burnUpGraph = {
			def iteration = Iteration.getOngoingIteration(session.project) 		    
			def plotData = plotService.createBurnUpPlotData(iteration)
			return [plotData:plotData]
	}
}
