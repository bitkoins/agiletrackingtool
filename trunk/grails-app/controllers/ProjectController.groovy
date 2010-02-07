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

class ProjectController {
	def scaffold = Project
	
	def projectService
	 
	static navigation = [
		group:'tags', 
		order:1, 
		title:'Projects', 
		action:'list' ,
		subItems: [
			[action:'list', order:1, title:'Overview'],
			[action:'create', order:10, title:'Create new project'],
		] 
	]
	
	def list = {
		return [projects:Project.list()]
	} 
	
	def show = {
		redirect(action:'list')
	}
	
	def select = {		
		redirect(controller:'item',action:'backlog')
	}
	
	def delete = {
		def project = Project.get(params.id)
		
		if(project) {
		
			if (session.project?.id == project.id)
			{
				session.project = null
			}	
			
			projectService.delete(project)
		}
		
		redirect(action:'list')
	}
}
