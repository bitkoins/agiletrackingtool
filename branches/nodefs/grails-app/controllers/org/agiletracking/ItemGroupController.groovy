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

class ItemGroupController {
	def itemGroupService
	def pointsSnapShotService
	def projectService
	
	static navigation = [
		group:'tags',
		action:'list',
		order:25,
		title:'Categories',
		isVisible: { session.project != null }, 
		subItems: [
			[action:'list', order:1, title:'Show'],
			[action:'create', order:10, title:'New category']			
		] 
	]

	def list = { [groups:ItemGroup.findAllByProject(session.project) ] }
	
	def create = {
		render(view:'edit', model : [group:new ItemGroup()] ) 	
	}
	
	def edit = {
		def group = ItemGroup.get(params.id)
		flash.projectCheckPassed = projectService.executeWhenProjectIsCorrect(session.project, group)
		return [group:group]
	}
	
	def save = {
		def isNew = (params.id?.size() == 0)
		def group = isNew ? new ItemGroup(project:session.project) : ItemGroup.get(params.id) 
		group.name = params.name
		
		flash.projectCheckPassed = projectService.executeWhenProjectIsCorrect(session.project, group, {group.save()} )
			
		redirect(controller:'item',action:'backlog')
	}
		
	def delete = { 
		def group = ItemGroup.get(Integer.parseInt(params.id))
		
		flash.projectCheckPassed = projectService.executeWhenProjectIsCorrect(session.project, group,	
		        {  pointsSnapShotService.deleteWholeGroup(group)
			       itemGroupService.deleteWholeGroup(group) } )
		
		redirect(action:'list')
	}
}
