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

class UtilXml {
	static def supportedVersions = [UtilXml_v0_5.docVersion, UtilXml_v0_4.docVersion ]
	static def currentDocVersion = supportedVersions[0]
		
	static def exportToXmlString(def project, def groups, def items, def iterations, def pointsSnapShots = [], def exportDate, 
			def docVersion = currentDocVersion )
	{
		def ret		
		switch(docVersion)
		{
			case UtilXml_v0_5.docVersion:
				ret = UtilXml_v0_5.exportToXmlString(project, groups, items, iterations, pointsSnapShots, exportDate)
				break;
			case UtilXml_v0_4.docVersion:
				ret = UtilXml_v0_4.exportToXmlString(groups, items, iterations, pointsSnapShots, exportDate)
				break;
			default:
				throw new Exception("Version ${docVersion} is not supported.")
				break;
		}
		return ret
	}
	
	static def importFromXmlString(def xmlString)
	{
		def ret 
		def doc = new XmlSlurper().parseText(xmlString)
		def docVersion = doc.DocumentVersion.text()
		
		switch(docVersion)
		{
			case UtilXml_v0_5.docVersion:
				ret = UtilXml_v0_5.importFromXmlDoc(doc)
				break;
			case UtilXml_v0_4.docVersion:
				ret = UtilXml_v0_4.importFromXmlDoc(doc)
				break;
			default:
				throw new Exception("Version ${docVersion} is not supported.")
				break;
		}
		return ret
	}
	
	static void setRelationToDomainObjects(def map)
	{
		map.itemsByIteration.each{ iter, items ->
			items.each{ item -> iter.addItem(item) } 
		}
	
		map.itemsByGroup.each{ group, items ->
			items.each{ item -> group.addItem(item) }
		}
		
		def setProject = { it.project = map.project }
		
		map.groups.each{ setProject(it) }
		map.items.each{ setProject(it) }
		map.iterations.each{ setProject(it) }
		map.snapShots.each{ setProject(it) }
	}
}

