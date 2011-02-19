/*----------------------------------------------------------------------------
Project: Agile Tracking Tool

Copyright 2008, 2009, 2010   Ben Schreur
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

class ProjectController {
	def projectService
	def authenticateService

	static navigation = [
		group:'tags', 
		order:1, 
		title:'Projects', 
		action:'list' ,
		isVisible: { authenticateService.userDomain() != null },
		subItems: [
			[action:'list', order:0, title:'Show'],
			[action:'create', order:10, title:'New project']
		] 
	]
	
	def select = {	
		def project = Project.get(params.id)
      flash.projectCheckPassed = checkProjectForUser(project) 
	   if(flash.projectCheckPassed) session.project = project

	   redirect(controller:'currentWork')
	}
	
	def delete = {
		def project = Project.get(params.id)
		if (session.project?.id == project.id)
		{
			session.project = null
		}			
		projectService.delete(project)
		
		redirect(action:'list')
	}

	def list = { [projects:Project.findAllByUser(authenticateService.userDomain() ) ] }
	
	def create = {
		render(view:'edit', model : [project:new Project()] ) 	
	}
	
	def edit = {
		def project = Project.get(params.id)
      flash.projectCheckPassed = checkProjectForUser(project) 
		return [project:project]
	}
	
	def save = {
		def isNew = (params.id?.size() == 0)
		def project = isNew ? new Project(user:authenticateService.userDomain()) : Project.get(params.id)
		
		project.name = params.name
		project.type = ProjectType.valueOf(params.type)
		
      flash.projectCheckPassed = checkProjectForUser(project) 
      if(flash.projectCheckPassed) {
			project.save()
			session.project = project
			if(isNew) projectService.addGroupToNewProject(project)
		}
			
		redirect(action:'list')
	}

	def checkProjectForUser(def project)
	{
		return (project.user.id == authenticateService.userDomain().id)
	}
}
