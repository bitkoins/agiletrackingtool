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

class ProjectService {
    static transactional = true
    
    def itemGroupService
    def iterationService

    void addGroupToNewProject(Project project)
    {
		def group = new ItemGroup(project:project,name:'Default category',items:[])
		group.save()
    }
    
    void delete(Project project) {
    	PointsSnapShot.findAllByProject(project).each{ it.delete() }
		Iteration.findAllByProject(project).each{ iteration -> iterationService.unloadItemsAndDelete(iteration) }
		ItemGroup.findAllByProject(project).each{ group -> itemGroupService.deleteWholeGroup(group) }
		project.delete()
    }
    
    boolean executeWhenProjectIsCorrect(Project project, Object objectForProjectCheck, 
											    Closure closureToCallWhenProjectIsValid = { } )
    {
  		 def projectCheckPassed = false
		 
       if( project && objectForProjectCheck && 
            objectForProjectCheck.project.id == project.id ) {
        	   projectCheckPassed = true
        }
        
        if(projectCheckPassed) closureToCallWhenProjectIsValid()
        
        return projectCheckPassed
    }
	
    String exportToXmlString(Project project, String docVersion = UtilXml.currentDocVersion)
    {
		def findAllForProject = { domain -> domain.findAllByProject(project) }
				 
    	return UtilXml.exportToXmlString(project,
					 findAllForProject(ItemGroup), 
                findAllForProject(Item), 
                findAllForProject(Iteration), 
                findAllForProject(PointsSnapShot),
                new Date(), docVersion )
    }
}
