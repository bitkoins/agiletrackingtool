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

class CurrentWorkController {

	def itemService
	def plotService
	def iterationService
	def projectService
	
	static navigation = [
		group:'tags', 
		order:10, 
		title:'Current work', 
		action:'show',
		isVisible: { session.project != null }
	]

	static def defaultAction = "show"
	
	def show = {
		def iteration = params.id ? Iteration.get(params.id) : iterationService.getOngoingIteration(session.project)
		
		if(!iteration) {
			redirect(controller:'iteration', action:'list')
			return
		}

		flash.projectCheckPassed = projectService.executeWhenProjectIsCorrect(session.project, iteration )
		
		def mlist = []
		ItemStatus.each{ status -> mlist += iteration.items?.findAll{ it.status == status }.sort{ it.uid } }
		def plotData = plotService.createBurnUpPlotData(iteration)
		
		return [iteration:iteration,items:mlist,plotData:plotData]
	}
	
	def closeCurrent = {
			def nextAction = "list"
			def iteration = iterationService.getOngoingIteration(session.project)			
	    	def iterationNext = iteration.retrieveNextIteration()
	    	
	    	if (!iterationNext) {
	    		iterationNext = iteration.createTheNextIteration()
	    		nextAction = "edit"
	    	}
	    	
	    	iterationService.transferUnfinishedItems(iteration, iterationNext)
	    	
			redirect(controller:'iteration', action:nextAction, id:iterationNext.id)	    	
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
		
		flash.projectCheckPassed = projectService.executeWhenProjectIsCorrect(session.project,  item,{ item.save() })
		render(template:'itemView',model:[item:item] )
	}
	
	def subItemFinished = {
		def id = Integer.parseInt(params.id)
		def subItem = SubItem.get(id)
		subItem.status = ItemStatus.Finished
		if (subItem.item.status == ItemStatus.Request) {
				subItem.item.status = ItemStatus.InProgress
		}
		
		flash.projectCheckPassed = projectService.executeWhenProjectIsCorrect(session.project,  subItem.item,{ subItem.save() })
			
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
		
		flash.projectCheckPassed = projectService.executeWhenProjectIsCorrect(session.project,  item,{ item.save() }) 
		
		render(template:'itemView',model:[item:item])
	}
}
