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
import groovy.util.slurpersupport.GPathResult

class UtilXml {
	static Collection<String> supportedVersions = [UtilXml_v0_5.docVersion, UtilXml_v0_4.docVersion]
	static String currentDocVersion = supportedVersions[0]
		
	static String exportToXmlString(Project project, Collection<ItemGroup> groups, 
						Collection<Item> items, Collection<Iteration> iterations, 
						Collection<PointsSnapShot> pointsSnapShots = [], Date exportDate, 
						String docVersion = currentDocVersion )
	{
		String ret		
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
	
	static Map importFromXmlString(String xmlString)
	{
		Map ret 
		GPathResult doc = new XmlSlurper().parseText(xmlString)
		String docVersion = doc.DocumentVersion.text()
		
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
	
	static void setRelationToDomainObjects(Map map)
	{
		map.itemsByIteration.each{ iter, items ->
			items.each{ item -> iter.addItem(item) } 
		}
	
		map.itemsByGroup.each{ group, items ->
			items.each{ item -> group.addItem(item) }
		}
		
		map.with {
		   (groups+items+iterations+snapShots).each{ it.project = map.project }
		}
	}
}

