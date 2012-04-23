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

class AdminController {
	def projectService
	def authenticateService	

	static navigation = [
		group:'tags', 
		order:1000, 
		title:'Administration', 
		action:'index',
		isVisible: { authenticateService.ifAnyGranted("ROLE_ADMIN") }
	]
	
	def index = { }

    def exportFile = {
    		if(!session.project) {
    			redirect(controller:'project',action:'list')
    			return
    		}
    		
    		def docVersion = params.docVersion ? params.docVersion : UtilXml.currentDocVersion
    		def xmlString = projectService.exportToXmlString(Project.get(session.project.id))
    
    		render(contentType: "text/xml", text:xmlString ) 
    }
    		
    def importFile = {
			def xml = request.getFile("file").inputStream.text
			def map = UtilXml.importFromXmlString(xml)

			map.project.user = authenticateService.userDomain()
			
			map.project.save()
			map.items*.save()
			map.groups*.save()
			map.iterations*.save()
			map.snapShots*.save()
			
			redirect(controller:'project', action:'list')
    }
}
