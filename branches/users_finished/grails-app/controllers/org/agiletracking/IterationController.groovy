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

class IterationController {

	def itemService
	def plotService
	def iterationService
	def projectService
	
	static navigation = [
		group:'tags', 
		order:40, 
		title:'Iterations', 
		action:'list',
		isVisible: { session.project != null },
		subItems: [
			[action:'list', order:10, title:"Manage"],
			[action:'history', order:20, title:'List all iterations'],
			[action:'create', order:40, title:'New iteration']
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

	def create = {
		def iteration = new Iteration()
		iteration.startTime = new Date()
		iteration.endTime = iteration.startTime + 14
		render(view:'edit', model : [iteration:iteration] ) 	
	}
	
	def edit = {
		def iteration = Iteration.get(params.id)
		flash.projectCheckPassed = projectService.executeWhenProjectIsCorrect(session.project,  iteration )
		return [iteration:iteration]
	}	
	
	def save = {
		def isNew = (params.id?.size() == 0)
		def iteration = isNew ? new Iteration(project:session.project) : Iteration.get(params.id)
		
		iteration.workingTitle = params.workingTitle
		iteration.startTime = params.startTime
		iteration.endTime = params.startTime + Integer.parseInt(params.duration)
		iteration.status = IterationStatus.valueOf(params.status)
	
		flash.projectCheckPassed = projectService.executeWhenProjectIsCorrect(session.project, iteration, { iteration.save() } )
		
		redirect(action:'list')
	}
	
	def delete = {
			def iteration = Iteration.get(params.id)
			
			flash.projectCheckPassed = projectService.executeWhenProjectIsCorrect(session.project, iteration, 
			      { iterationService.unloadItemsAndDelete(iteration) } )
			
			redirect(action:"list")
	}
}
