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
	
	static navigation = [
		group:'tags', 
		order:40, 
		title:'Iterations', 
		action:'show',
		subItems: [
			[action:'show', order:1, title:'Show current iteration'],
			[action:'list', order:10, title:"Manage"],
			[action:'create', order:20, title:'New iteration'],
			[action:'history', order:50, title:'List all iterations']
		] 
	]
	
	def list = {
			def iterations = Iteration.findAllByProject(session.project)?.sort{it.startTime }.reverse()
			
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
			
			return [ iterations:iterations, plotData:plotData ]
	}
	
	def delete = {
			if ( params.id ) {
				def iteration = Iteration.get(Integer.parseInt(params.id))
				
				if(belongsToProject(iteration)) {
				    iteration.unloadItemsAndDelete() 
				}
				else
				{
					redirect(controller:'project',action:'list')
					return
				}
			}
			redirect(action:"list")
	}	
	
	def history = {
			def iterations = Iteration.findAllByProject(session.project)?.collect{it}.sort{it.startTime }.reverse()
			return [ iterations:iterations ]			
	}
	
	def show = {
		def iteration = params.id ? Iteration.get(params.id) : Iteration.getOngoingIteration(session.project)
				
		if (!belongsToProject(iteration)) { 
			redirect(controller:'project',action:'list')
			return
		}
		
		def mlist = []
		ItemStatus.each{ status -> mlist += iteration.items?.findAll{ it.status == status }.sort{ it.uid } }
		
		return [iteration:iteration,items:mlist,plotData:iteration.getBurnUpPlotData()]
	}
	
	def closeCurrent = {
			def nextAction
			def iteration = Iteration.getOngoingIteration(session.project)
	    	def iterationNew = iteration.retrieveNextIteration()
			
	    	if (iterationNew) {
	    		iteration.copyUnfinishedItems(iterNew)
	    		nextAction = "list"
	    	} 
	    	else {
	    		iterationNew = iteration.createTheNextIteration()
	    		nextAction = "edit"
	    	}

	    	iteration.closeIteration()
	    	iterationNew.openIteration()
	    	iterationNew.save()
			iteration.save()
	    	
			redirect(action:nextAction,id:iterationNew.id)	    	
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
		
		if( !belongsToProject(item))
		{
			render "Item does not belong to project."
			return
		}
		
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
		
		if( !belongsToProject(subItem.item))
		{
			render "Subitem does not belong to project."
			return
		}
		
		subItem.status = ItemStatus.Finished
		subItem.save()
			
		if (subItem.item.status == ItemStatus.Request) {
			subItem.item.status = ItemStatus.InProgress
			subItem.save()
		}
			
		render(template:'itemView',model:[item:subItem.item])
	}
	
	def editItem = {
		def item = Item.get(params.id)
		
		if(!belongsToProject(item))
		{
			render "Item does not belong to project."
			return
		}
		
		render(template:'/shared/item/edit',model:[item:item])
	}
	
	def saveItem = {
		def item = Item.get(Integer.parseInt(params.id))
		
		if(!belongsToProject(item))
		{
			redirect(controller:'project',action:'list')
			return
		}
		
		ItemParamsParser.updateItemWithParams(item,params, {param -> request.getParameterValues(param)} )
		item.subItems*.save()
		item.save()
		render(template:'itemView',model:[item:item])
	}
		
	def create = {
		def iteration = new Iteration()
		iteration.startTime = new Date()
		iteration.endTime = iteration.startTime + 14
		render(view:'edit', model : [iteration:iteration] ) 	
	}
	
	def edit = {
		def iteration = Iteration.get(params.id)
		if(belongsToProject(iteration)) {
			return [iteration:iteration]
		}
		else {
			redirect(controller:'project',action:'list')
		}
	}
	
	def updateIterationWithParams(def iteration, def params)
	{
		iteration.workingTitle = params.workingTitle
		iteration.startTime = params.startTime
		iteration.endTime = params.startTime + Integer.parseInt(params.duration)
		iteration.status = IterationStatus.valueOf(params.status)
	}
	
	def save = {
		def isNew = (params.id?.size() == 0)
		if(isNew) {
			def iteration = new Iteration(project:session.project)
			updateIterationWithParams(iteration,params)
			iteration.save()
		}
		else
		{
			def iteration = Iteration.get(params.id)
			if ( belongsToProject(iteration) ) {
				updateIterationWithParams(iteration,params)
				iteration.save()
			}
			else
			{
				redirect(controller:'project',action:'list')
				return
			}	
		}
		redirect(action:'list')
	}
	
	def belongsToProject(def itemThatBelongsToProject)
	{
		return (itemThatBelongsToProject && (itemThatBelongsToProject.project.id == session.project.id) )
	}
}
