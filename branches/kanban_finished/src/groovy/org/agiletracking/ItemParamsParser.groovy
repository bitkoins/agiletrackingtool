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

class ItemParamsParser {
	
	/* Note: the getParameterValuesClosure is used because the params return a single String or a
	 *       string list depending on the amount of new parameters. 
	 *       request.getParameterValue("newSubItem_...") always return a list of strings.
	 */
	static def updateItemWithParams(def item, def params, def getParameterValuesClosure = null)
	
	{	
		item.description = params.description
		item.comment = params.comment
		item.criteria = params.criteria
		def pointsParser = { oldPoints, newPoints ->
			def ret = oldPoints
			try {
				ret = newPoints ? Double.parseDouble(newPoints) : 0
			}
			catch(Exception) {	}					
			return ret
		}
		
		item.itemPoints = pointsParser(item.itemPoints, params.points)
		item.priority = Priority.valueOf(params.priority)
		
		def deletedSubItemIdList = []		
		item.subItems.each{ subItem ->
			def description = params['subItem_description_' + subItem.id]
			if (description) subItem.description = description
			def points = params['subItem_points_' + subItem.id]
			subItem.points = pointsParser(subItem.points, points)
			if (!description && !points) deletedSubItemIdList << subItem.id
		}
		
		deletedSubItemIdList.each{ id ->
			item.deleteSubItem(id) 
		}
		
		def newSubItemDescriptions = params.containsKey("newSubItem_description") ? getParameterValuesClosure("newSubItem_description") : []
		def newSubItemPoints = params.containsKey("newSubItem_points") ? getParameterValuesClosure("newSubItem_points") : []
		
		if ( newSubItemDescriptions?.size() != newSubItemPoints?.size()) throw new Exception("The number of new subItem descriptions and points does not match.")
		
		newSubItemDescriptions?.eachWithIndex{ description, index ->
			def subItem = new SubItem(description, pointsParser(0, newSubItemPoints[index]))
			item.addSubItem(subItem)
		}
	}
}