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

class Iteration extends ItemContainer {	
	String workingTitle
	IterationStatus status
	Date startTime
	Date endTime
	
	static constraints = {
		pointsPerDay(nullable:true)
		finishedPoints(nullable:true)
		workingTitle(nullable:true)
		startTime(nullable:true)
		endTime(nullable:true)
		status(nullable:true)
		//project(nullable:false)
	}
	
	static def getOngoingIteration()
	{
		return Iteration.list()?.unique().find{ it.status == IterationStatus.Ongoing }
	}
	
	Iteration retrieveNextIteration()
	{
		return Iteration.list()?.unique().findAll{ (it.endTime > this.endTime) && (it.status != IterationStatus.Finished) }?.sort{it.endTime }[0]
	}
	
	String toString() { return workingTitle }

	List listGroups()
	{
		def ret = new HashSet()
		items.each{ ret << it.group }
		return ret.collect{ it }
	}
	
	List listUnfinishedItemsForGroup(def group)
	{
		def unItems = []
		items.each{ 
			if ((it.group == group) && it.checkUnfinished()) unItems << it
		}		
		return unItems
	}
	
	void closeIteration()
	{ 
		status = IterationStatus.Finished
		def unItems = getUnfinishedItems()		
		unItems.each{ this.deleteItem(it.id) }
	}
	
	void openIteration()
	{
		status = IterationStatus.Ongoing
	}

	Iteration createTheNextIteration()
	{
		Iteration newIter = new Iteration()
		newIter.status = IterationStatus.Ongoing
		newIter.startTime = new Date()
		newIter.endTime = newIter.startTime + Util.getDaysInBetween(startTime,endTime)
		newIter.workingTitle = "<<Please fill in>>"
		newIter.items = []
		this.copyUnfinishedItems(newIter)
		
		return newIter
	}
	
	void copyUnfinishedItems(Iteration iterDest)
	{
		def unItems = getUnfinishedItems()
		unItems.each{ item -> 
			iterDest.addItem(item) 
		}
	}
	
	def getDurationInDays()
	{
		return Util.getDaysInBetween(startTime,endTime)
	}
	
	def getDaysLeft(def now)
	{
		def days =  Util.getDaysInBetween(now,endTime) 
		if ( days < 0) return 0 
		return days
	}
	
	def getPointsPerDay()
	{
		def ret = 0
		
		if ( getDurationInDays() != 0) {
			ret =  getFinishedPoints()*1.0/ getDurationInDays()
		}
		return ret
	}
	
	def getFinishedPoints()
	{
		def sum = 0
		items.each{ if ( it.status == ItemStatus.Finished ) sum+= it.points}
		return sum
	}
	
	def totalPoints()
	{
		def sum = 0;
		items.each{ sum+= it.points }
		return sum
	}
	
	def getUnfinishedPoints()
	{
		return (totalPoints()-getFinishedPoints())
	}
	
	def getUnfinishedItems()
	{
		def unfinishedItems = []
		items.collect{it}.each {
			if (it.checkUnfinished() ) unfinishedItems << it
		}
		return unfinishedItems
	}
	
	def setFinishedPoints() { return }
	def setPointsPerDay(){return}
	def setDurationInDays(){ return }
	def setDaysLeft() {return}
	
	void addItem(Item item)
	{
		if(item?.iteration)
		{
			if ( this != item.iteration) {
				item.iteration.deleteItem(item?.id)
			}
		}
		super.addItem(item)
	}
	
	void unloadItemsAndDelete()
	{
	    items.collect{ it }.each{ item -> this.deleteItem(item.id) } 
	   	this.delete()
	}

	def getBurnUpPlotData()
    {
        def plotData = new PlotData("Burnup chart") 
        plotData.xLabel = "Days since iteration start"
        plotData.yLabel = "Points"
        
        def iter = this
        def now = new Date()
		def snapShots = PointsSnapShot.getSnapShotsBetween(iter.startTime,iter.endTime)
		def dates = snapShots.collect{it.date}
		def overViews = snapShots.collect{it.overView}
				
		dates = dates.collect{it + (now-iter.startTime)}
		def iterationCurve = PlotUtil.getPlotCurveForItemStatus(overViews,dates, now,"Finished", ItemStatus.Finished)
		if ( iterationCurve.yValues.size() > 0 ) {
			iterationCurve.yValues = iterationCurve.yValues.collect{ it - iterationCurve.yValues[0] }
		} 
		plotData.curves << iterationCurve
				
		def totalCurve = new PlotCurve("Total points")
		totalCurve.xValues = [0, iter.endTime-iter.startTime]
		totalCurve.yValues = [iter.totalPoints(),iter.totalPoints()]
		plotData.curves << totalCurve
			
		def nominalCurve = new PlotCurve("Steady Pace")
		nominalCurve.xValues = totalCurve.xValues
		nominalCurve.yValues = [0,iter.totalPoints()]
		plotData.curves << nominalCurve  
	
        return plotData
   }	
}


