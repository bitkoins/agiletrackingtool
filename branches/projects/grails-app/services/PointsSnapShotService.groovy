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
    boolean transactional = true
    def exportDir = ConfigurationHolder.config.agile.exportDirectory

    def performDailyJob() {
    	def groups = ItemGroup.list()
	   	if ( groups?.size() > 0 )
	    {
	       	def snapShot = PointsSnapShot.takeSnapShot(groups,new Date())
	       	snapShot.save()
	       	println "Snapshot saved."
	        	
	       	def exportDate = new Date()
	       	def dateTimeString = exportDate.toString().replace(" ","_").replace(":","_")
	       	def fileName = exportDir + "AgilePlannerExport_${dateTimeString}.xml"
	       	println "Writing to file: " + fileName
	       	def file = new File(fileName)
	       	file.write(UtilXml.exportToXmlString(groups, Item.list(), Iteration.list(), PointsSnapShot.list(), exportDate))
	   }
	   else
	   {
	     	println "No groups are present"
	   }	
    }
}
