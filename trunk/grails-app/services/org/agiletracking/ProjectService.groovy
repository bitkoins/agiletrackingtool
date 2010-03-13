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
import org.codehaus.groovy.grails.commons.*

class ProjectService {
    static transactional = true
    
    def itemGroupService
    def iterationService
    
    def delete(def project) {
    	PointsSnapShot.findAllByProject(project).each{ it.delete() }
		Iteration.findAllByProject(project).each{ iteration -> iterationService.unloadItemsAndDelete(iteration) }
		ItemGroup.findAllByProject(project).each{ group -> itemGroupService.deleteWholeGroup(group) }
		project.delete()
    }
    
    def executeWhenProjectIsCorrect(def project, def objectForProjectCheck, def closureWhenProjectIsValid = {})
	{
		def projectCheckPassed = false
		 
        if( project && objectForProjectCheck && 
            objectForProjectCheck.project.id == project.id ) {
        	projectCheckPassed = true
        }
        
        if(projectCheckPassed) closureWhenProjectIsValid()
        
        return projectCheckPassed
	}
	
	def exportToXmlString(def project, def docVersion = UtilXml.currentDocVersion)
	{
		def findAllForProject = { domain -> domain.findAllByProject(project) }
				 
    	return UtilXml.exportToXmlString(project,
    			  						 findAllForProject(ItemGroup), 
    	                                 findAllForProject(Item), 
    	                                 findAllForProject(Iteration), 
    	                                 findAllForProject(PointsSnapShot),
    	                                 new Date(), docVersion )
	}
		
	def getProjectExportFileName(def projectName, def date)
	{
		def exportDir = ConfigurationHolder.config?.agile?.exportDirectory
			def dateTimeString = date.toString().replace(" ","_").replace(":","_")
    	return exportDir ? exportDir + "${projectName.replace(" ", "_")}_${dateTimeString}.xml" : null
	}
}
