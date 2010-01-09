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

class IterationController {
	def scaffold = Iteration
	
	static navigation = [
		group:'tags', 
		order:40, 
		title:'Iterations', 
		action:'showCurrent',
		subItems: [
			[action:'showCurrent', order:1, title:'Show current iteration'],
			[action:'list', order:10, title:"Manage"],
			[action:'create', order:20, title:'New iteration'],
			[action:'history', order:50, title:'List all iterations']
		] 
	]
	
	def list = {
			def iterations = Iteration.list()?.unique().sort{it.startTime }.reverse()
			
			def plotData = new PlotData("Iteration history")
			plotData.xLabel = "Days ago from now"
			plotData.yLabel = "Points/Week"
			plotData.yMin = 0
			
			def plotCurve = new PlotCurve("PointsPerWeek")
			def now = new Date()
			
			iterations?.each{ iter ->
				def daysFromNow = Util.getDaysInBetween(now, iter.endTime)
				if ( daysFromNow < 0 )
				{
					plotCurve.xValues << daysFromNow
					plotCurve.yValues << iter.getPointsPerDay()*7
				}
			}
			plotData.curves << plotCurve
			
			return [ iterations:iterations, plotData:plotData ]
	}
	
	def delete = {
			if ( params.id ) {
				Iteration.get(Integer.parseInt(params.id)).unloadItemsAndDelete()
			}
			redirect(action:"list")
	}	
	
	def history = {
			def iterations = Iteration.list().unique().collect{it}.sort{it.startTime }.reverse()
			return [ iterations:iterations ]			
	}
	
	def showCurrent = {
		def iter 
		if(params.id) {
			def id = Integer.parseInt(params.id)
			iter = Iteration.get(id)
		}
		else {
			iter = Iteration.getOngoingIteration()
		}
		
		if (!iter) { 
			redirect(action:'list')
			return
		}
		
		session.iterId = iter?.id
		
		def mlist = []
		ItemStatus.each{ status -> mlist += iter?.items.findAll{ it.status == status }.sort{ it.uid } }
		
		return [iteration:iter,items:mlist,plotData:iter.getBurnUpPlotData()]
	}
	
	def closeCurrent = {
			def nextAction
			def iter = Iteration.getOngoingIteration()
	    	def iterNew = iter.retrieveNextIteration()
			
	    	if (iterNew) {
	    		iter.copyUnfinishedItems(iterNew)
	    		nextAction = "list"
	    	} 
	    	else {
	    		iterNew = iter.createTheNextIteration()
	    		nextAction = "edit"
	    	}

	    	iter.closeIteration()
	    	iterNew.openIteration()
	    	iterNew.save()
			iter.save()
	    	
			redirect(action:nextAction,id:iterNew.id)	    	
	}
	
	def itemDone = {
			updateItemStatus(params,ItemStatus.Finished)
	}
	
	def itemInProgress = {
			updateItemStatus(params,ItemStatus.InProgress)
	}
		
	def itemBlocking = {
			updateItemStatus(params,ItemStatus.Blocking)
	}
	
	private void updateItemStatus(def params, def newState)
	{
		def id = Integer.parseInt(params.id)
		def item = Item.get(id)
		
		item.status = newState
		if ( item.status == ItemStatus.Finished ) {
			item.subItems.each{ it.status = ItemStatus.Finished }
		}
		
		item.save()		
		render(template:'itemView',model:[item:item])
	}
	
	def subItemFinished = {
		def id = Integer.parseInt(params.id)
		def subItem = SubItem.get(id)
		subItem.status = ItemStatus.Finished
		subItem.save()
			
		if (subItem.item.status == ItemStatus.Request) {
			subItem.item.status = ItemStatus.InProgress
			subItem.save()
		}
			
		render(template:'itemView',model:[item:subItem.item])
	}
	
	def editItem = {
		def item = Item.get(Integer.parseInt(params.id))
		render(template:'/shared/item/edit',model:[item:item])
	}
	
	def saveItem = {
		def item = Item.get(Integer.parseInt(params.id))
		ItemParamsParser.updateItemWithParams(item,params, {param -> request.getParameterValues(param)} )
		item.subItems*.save()
		item.save()
		render(template:'itemView',model:[item:item])
	}
}
