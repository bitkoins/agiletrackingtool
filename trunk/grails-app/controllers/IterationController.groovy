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

	def itemService
	def plotService
	def iterationService
	def projectService
	
	static navigation = [
		group:'tags', 
		order:40, 
		title:'Iterations', 
		action:'show',
		isVisible: { session.project != null },
		subItems: [
			[action:'show', order:1, title:'Show current iteration'],
			[action:'list', order:10, title:"Manage"],
			[action:'create', order:20, title:'New iteration'],
			[action:'history', order:50, title:'List all iterations']
		] 
	]
	
	def list = {
			def iterations = Iteration.findAllByProject(session.project)?.sort{it.startTime }.reverse()
			def plotData = plotService.createIterationHistoryPlot(iterations)
			
			return [ iterations:iterations, plotData:plotData ]
	}
	
	def history = {
			def iterations = Iteration.findAllByProject(session.project)?.collect{it}.sort{it.startTime }.reverse()
			return [ iterations:iterations ]			
	}
	
	def show = {
		def iteration = params.id ? Iteration.get(params.id) : Iteration.getOngoingIteration(session.project)

		flash.projectCheckFailed = projectService.executeWhenProjectIsCorrect(session.project, iteration )
		
		def mlist = []
		ItemStatus.each{ status -> mlist += iteration.items?.findAll{ it.status == status }.sort{ it.uid } }
		def plotData = plotService.createBurnUpPlotData(iteration)
		
		return [iteration:iteration,items:mlist,plotData:plotData]
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
		
		item.status = newState
		if ( item.status == ItemStatus.Finished ) {
			item.subItems.each{ it.status = ItemStatus.Finished }
		}
		
		flash.projectCheckFailed = projectService.executeWhenProjectIsCorrect(session.project,  item,{ item.save() })
		render(template:'itemView',model:[item:item] )
	}
	
	def subItemFinished = {
		def id = Integer.parseInt(params.id)
		def subItem = SubItem.get(id)
		subItem.status = ItemStatus.Finished
		if (subItem.item.status == ItemStatus.Request) {
				subItem.item.status = ItemStatus.InProgress
		}
		
		flash.projectCheckFailed = projectService.executeWhenProjectIsCorrect(session.project,  subItem.item,{ subItem.save() })
			
		render(template:'itemView',model:[item:subItem.item] )
	}
	
	def editItem = {
		def item = Item.get(params.id)
		flash.objectForProjectCheck = item
		render(template:'/shared/item/edit',model:[item:item])
	}
	
	def saveItem = {
		def item = Item.get(Integer.parseInt(params.id))
		ItemParamsParser.updateItemWithParams(item,params, {param -> request.getParameterValues(param)} )
		
		flash.projectCheckFailed = projectService.executeWhenProjectIsCorrect(session.project,  item,{ itemService.saveItem(item) }) 
		
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
		flash.projectCheckFailed = projectService.executeWhenProjectIsCorrect(session.project,  iteration )
		return [iteration:iteration]
	}	
	
	def save = {
		def isNew = (params.id?.size() == 0)
		def iteration = isNew ? new Iteration(project:session.project) : Iteration.get(params.id)
		
		iteration.workingTitle = params.workingTitle
		iteration.startTime = params.startTime
		iteration.endTime = params.startTime + Integer.parseInt(params.duration)
		iteration.status = IterationStatus.valueOf(params.status)
	
		flash.projectCheckFailed = projectService.executeWhenProjectIsCorrect(session.project, iteration, { iteration.save() } )
		
		redirect(action:'list')
	}
	
	def delete = {
			def iteration = Iteration.get(params.id)
			
			flash.projectCheckFailed = projectService.executeWhenProjectIsCorrect(session.project, iteration, 
			      { iterationService.unloadItemsAndDelete(iteration) } )
			
			redirect(action:"list")
	}
}
