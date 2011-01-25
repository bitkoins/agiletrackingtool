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

import org.agiletracking.Project
import org.agiletracking.ItemGroup

class Item {
	String        description
	Double        points	
	ItemStatus    status
	Priority      priority
	String        comment
	String        criteria
	Integer       uid
	
	Date          dateCreated
   Date          lastUpdated
   
	static hasMany = [subItems:SubItem]
	static belongsTo = [project:Project]
	
	static constraints  = {
		description(maxSize:255)
		comment(nullable:true,maxSize:1024)
		criteria(nullable:true,maxSize:1024)
		points(scale:1)	
		uid(nullable:true)
		project(nullable:false)
		lastUpdated(nullable:true)
	}

	Item()  { subItems = [] }
	
	Item(Project project)
	{
		uid = maxUid(project) + 1
		this.project = project
		description = ""
		points = 1
		status = ItemStatus.Request
		priority = Priority.High
		subItems = []
	}
	
	String toString() { return description }
	
	boolean checkUnfinished()
	{
		return (status != ItemStatus.Finished)
	}
	
	SubItem getSubItem(Long id) 
	{
		subItems?.find{ it.id == id }
	}
	
	boolean hasSubItem(Long id)
	{
		return getSubItem(id) != null
	}
	
	void addSubItem(SubItem subItem)
	{
		this.addToSubItems(subItem)		
	}
	
	void deleteSubItem(Long id)
	{
		if ( hasSubItem(id) ) {
			def subItem = getSubItem(id)
			this.removeFromSubItems(subItem)	
			subItem.delete()
		}
	}
	
	Double getPoints()
	{
		def p = 0
		if ( !(subItems?.size() == 0)){ 
			p = subItems.sum{ it.points }
		}
		return [this.@points,p].max()
	}
		
	boolean hasCriteria()
	{
		return criteria ? criteria.trim().size() != 0 : false
	}
	
	static Integer maxUid(Project project)
	{	
		return _retrieveMaxValueForField(project,"uid")
	}	

	static Date lastUpdateDateForProject(Project project)
	{	
		return _retrieveMaxValueForField(project,"lastUpdated")
	}

	static def _retrieveMaxValueForField(Project project, String fieldAsString)
	{
 		def max = Item.createCriteria().get {
			eq("project",project)
			projections {
				max(fieldAsString)
			}
		}

		return max ?: 0
	}

	static Integer parseUid(String uidString)
	{
		return Integer.parseInt(uidString)
	}	
}
