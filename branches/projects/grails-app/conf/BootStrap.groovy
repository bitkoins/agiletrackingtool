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

class BootStrap {
	
     def init = { servletContext ->
     
     	return 
     	
     	/* Added some code here, so that database is filled automatically.
     	 * Code duplication with AdminController.loadDefaults...
     	 */
     	def groups = Defaults.getGroups(5)
    		groups*.save()
    		def items = Defaults.getItems(25,groups)
    		items*.save()
    		
    		Defaults.getSubItems(items.size(),items)*.save()    			
    		def iters = Defaults.getIterations(3)
     		
    		def snapShots = []
    		
     		def nowDate = new Date()
    		def durationInDays = 3
    		def startDate = nowDate - iters.size()*durationInDays
    		iters.eachWithIndex{ iter, iterIndex ->
    			iter.startTime = startDate + iterIndex*durationInDays
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
    				
    				def snapShot = PointsSnapShot.takeSnapShot(groups,iter.startTime+itemIndex)    	
    				snapShot.save()
    			}
    		}
    		
    		iters[-1].status = IterationStatus.Ongoing 
    		iters*.save()  	
     }
     
     def destroy = {
    		 
     }
} 