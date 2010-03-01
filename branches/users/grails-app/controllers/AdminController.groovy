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

class AdminController {
	def projectService
	
	static navigation = [
		group:'tags', 
		order:1000, 
		title:'Administration', 
		action:'index'
	]
	
	def index = { }

    def exportFile = {
    		if(!session.project) {
    			redirect(controller:'project',action:'list')
    			return
    		}
    		
    		def docVersion = params.docVersion ? params.docVersion : UtilXml.currentDocVersion
    		def xmlString = projectService.exportToXmlString(session.project)
    
    		render(contentType: "text/xml", text:xmlString ) 
    }
    		
    def importFile = {
			def xml = request.getFile("file").inputStream.text
			def map = UtilXml.importFromXmlString(xml)

			map.project.save()
			map.groups*.save()
			map.items*.save()
			map.iterations*.save()
			
			UtilXml.setRelationToDomainObjects(map)
			
			map.groups*.save()
			map.items*.save()
			map.iterations*.save()
			map.snapShots*.save()
			
			redirect(controller:'project', action:'list')
    }
    
    def loadDefaults = {
    		3.times { projectId ->
    			def project = new Project(name:"Example project ${projectId}")
    			project.save()
    
    			def groups = Defaults.getGroups(5,project)
    			groups*.save()
    			def items = Defaults.getItems(25,groups,project)
    			items*.save()
    		
    			Defaults.getSubItems(items.size(),items)*.save()    			
    			def iters = Defaults.getIterations(3,project)
     		
    			def snapShots = []
    		
     			def nowDate = new Date()
    			def durationInDays = 10
    			def startTime = nowDate - iters.size()*durationInDays
    			iters.eachWithIndex{ iter, iterIndex ->
    				iter.startTime = startTime + iterIndex*durationInDays
    				iter.endTime = iter.startTime+ durationInDays
    				iter.status = IterationStatus.Finished
    			    			
    				5.times{ itemIndex -> 
    					def item = Util.random(items)
    					if (item)
    					{
	    					item.status = ItemStatus.Finished
    						item.save()
    						items = items - item
    						iter.addItem(item)
    					}
    				
    					def snapShot = PointsSnapShot.takeSnapShot(project, iter.startTime+itemIndex)    	
    					snapShot.save()
    				}
    			}
    		
    			iters[-1].status = IterationStatus.Ongoing 
    			iters*.save()
    		}
    		
    		redirect(controller:"project", action:"list")
    }
}
