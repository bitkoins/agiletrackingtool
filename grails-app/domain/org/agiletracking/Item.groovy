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

class Item {
	String        description
	Double        itemPoints	
	ItemStatus    status
	Priority      priority
	String        comment
	String        criteria
	Integer       uid
	
	Date          dateCreated
        Date          lastUpdated
   
	static hasMany = [subItems:SubItem]
	static belongsTo = [iteration:Iteration,group:ItemGroup, project:Project]
	static fetchMode = [subItems:"eager"]
	
	static constraints  = {
		description(maxSize:255)
		iteration(nullable:true)
		group(nullable:true)
		comment(nullable:true,maxSize:1024)
		criteria(nullable:true,maxSize:1024)
		itemPoints(scale:1)	
		uid(nullable:true)
		project(nullable:false)
		lastUpdated(nullable:true)
	}

	Item()  { subItems = [] }
	
	Item(def project, def group)
	{
		uid = maxUid(project) + 1
		this.project = project
		this.group = group
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
	
	SubItem getSubItem(def id) 
	{
		subItems?.find{ it.id == id }
	}
	
	boolean hasSubItem(def id)
	{
		return getSubItem(id) != null
	}
	
	void addSubItem(SubItem subItem)
	{
		subItem.item = this
		this.addToSubItems(subItem)		
	}
	
	void deleteSubItem(def id)
	{
		if ( hasSubItem(id) ) {
			def subItem = getSubItem(id)
			this.removeFromSubItems(subItem)	
			subItem.delete()
		}
	}
	
	def getPoints()
	{
		def p = 0
		if ( !(subItems?.size() == 0)){ 
			p = subItems.sum{ it.points }
		}
		return [itemPoints,p].max()
	}
	
	def setPoints(def points)
	{
		this.itemPoints = points
	}
	
	def hasCriteria()
	{
		return criteria ? criteria.trim().size() != 0 : false
	}
	
	def removeItemRelations()
	{
		iteration?.deleteItem(id)
		group?.deleteItem(id)
	}
	
	static def maxUid(def project)
	{	
		return _retrieveMaxValueForField(project,"uid")
	}	

	static def lastUpdateDateForProject(def project)
	{	
		return _retrieveMaxValueForField(project,"lastUpdated")
	}

	static def _retrieveMaxValueForField(def project, def fieldAsString)
	{
 		def max = Item.createCriteria().get {
			eq("project",project)
			projections {
				max(fieldAsString)
			}
		}

		return max ?: 0
	}
}
