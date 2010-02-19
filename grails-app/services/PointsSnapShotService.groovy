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

import org.codehaus.groovy.grails.commons.*

class PointsSnapShotService {
    static transactional = true
        
    def exportDir = ConfigurationHolder.config.agile.exportDirectory

    def performDailyJob() {
    
        Project.list().each{ project ->
            def groups = ItemGroup.findAllByProject(project)
               if ( groups?.size() > 0 )
            {
                   def snapShot = PointsSnapShot.takeSnapShot(project,groups,new Date())
                   snapShot.save()
                   println "Snapshot saved."
                
                if(!exportDir) return
                    
                   def exportDate = new Date()
                   def dateTimeString = exportDate.toString().replace(" ","_").replace(":","_")
                   def fileName = exportDir + "${project.name.replace(" ", "_")}_${dateTimeString}.xml"
                   println "Writing to file: " + fileName
                   def file = new File(fileName)
                
                def findAllForProject = { domain -> domain.findAllByProject(project) } 
                def xml = UtilXml.exportToXmlString(project,
                                                    findAllForProject(ItemGroup), 
                                                    findAllForProject(Item), 
                                                    findAllForProject(Iteration), 
                                                    findAllForProject(PointsSnapShot),
                                                    exportDate)
                   file.write(xml)
           }
           else
           {
                 println "No groups are present"
           }
        }    
    }
    
    def deleteWholeGroup(def group)        
    {        
        PointsSnapShot.findAllByProject(group.project).each{ snapShot ->        
            def pointsForGroup = snapShot.getPointsForGroup(group)        
            if (pointsForGroup) {        
                   pointsForGroup.snapShot.removeFromPointsForGroups(pointsForGroup)        
                pointsForGroup.delete()
            }        
        }        
    }    
}
