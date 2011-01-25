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

class ItemParamsParserTests extends GroovyTestCase 
{
	def params
	def item
	def subItems
	def points
	def prio
	
	void setUp() {
		params = [:]
		params['description'] = "Right"
		params['comment'] = "None"
		params['criteria'] = "Some"
		points = 4.4
		params['points'] = "${points}"
		prio = Priority.Medium
		params['priority'] = "${prio}"
		
		item = Defaults.getItems(1, [] )[0]		
		subItems = Defaults.getSubItems(2, [item])
	}
	
	void testUpdatingItemFields()
	{
		ItemParamsParser.updateItemWithParams(item,params)
		
		assertTrue item.description == params['description']
		assertTrue item.comment == params['comment']
		assertTrue item.criteria == params['criteria']
		assertTrue item.points == points
		assertTrue item.priority == prio
	}
	
	void testPointsAreUnchangedWhenNotCorrectlySet()
	{
		def originalPoints = 7
		item.points = originalPoints
		params['points'] = "SomethingThatDoesntLookLikePoints"
		ItemParamsParser.updateItemWithParams(item,params)
		assertTrue item.points == originalPoints
	}
	
	void testWithNullItemPoitsParams()
	{
		params['points'] = null
		ItemParamsParser.updateItemWithParams(item,params)
		assertTrue item.points == 0
	}
	
	void testWithNullSubItemPoitsParams()
	{
		params['points'] = null
		ItemParamsParser.updateItemWithParams(item,params)
		assertTrue item.points == 0
	}
	
	void testSetSubItems()
	{
		def descFunc = { "Something" + it }
		def pointsFunc = { new Double(10.5) + it }
		
		subItems.eachWithIndex{ subItem, index ->
			params['subItem_description_' + subItem.id ] = descFunc(index)
			params['subItem_points_' + subItem.id ] = "${pointsFunc(index)}"
		}
		
		ItemParamsParser.updateItemWithParams(item,params)
		
		assertTrue item.subItems.size() == subItems.size()
		subItems.eachWithIndex{ subItem, index -> 
			assertTrue subItem.points == pointsFunc(index)
			assertTrue subItem.description == descFunc(index) 
		}
	}
	
	void testSetSubItemWithNullPoints()
	{
		def subItem = subItems[0]
		params['subItem_description_' + subItem.id ] = "bla"
		params['subItem_points_' + subItem.id ] = null
		
		ItemParamsParser.updateItemWithParams(item,params)
		
		assertTrue item.subItems.size() == 1
		item.subItems.each{ assertTrue it.points == 0 }
	}
	
	void testAddOneNewSubItem()
	{
		def points = 4.4
		def desc = "New"
		params['newSubItem_description'] = desc
		params['newSubItem_points'] = "${points}"
		
		def getParameterValuesClosure = { param -> [ params[param] ] }
		ItemParamsParser.updateItemWithParams(item,params, getParameterValuesClosure)
		
		assertTrue item.subItems.size() == 1
		assertTrue item.subItems.find{ it.description == desc } != null
		assertTrue item.subItems.find{ it.points == points } != null
	}
	
	void testAddOneNewSubItemWithNullPoints()
	{
		params['newSubItem_description'] = "bla"
		params['newSubItem_points'] = null
		
		def getParameterValuesClosure = { param -> [ params[param] ] }
		ItemParamsParser.updateItemWithParams(item,params, getParameterValuesClosure)
		
		assertTrue item.subItems.size() == 1
		item.subItems.each{ assertTrue it.points == 0 } 
	}
		
	void testAddNewSubItems()
	{
		def pointList = [1,2,3]
		def descriptionList = ["a","b","c"]
		
		params['newSubItem_description'] = descriptionList 
		params['newSubItem_points'] = pointList.collect{ "${it}" }
		
		def getParameterValuesClosure = { param -> params[param] }
		ItemParamsParser.updateItemWithParams(item,params, getParameterValuesClosure)
		
		assertTrue item.subItems.size() == pointList.size()
		descriptionList.eachWithIndex{ description, index ->
			def subItem = item.subItems.find{ it.description == description }
			assertTrue subItem != null
			assertTrue subItem.points == pointList[index]
		}
	}
}
