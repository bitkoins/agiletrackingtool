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
class Item {
	String        description
	Double        itemPoints	
	ItemStatus    status
	Priority      priority
	String        comment
	String        criteria
	Integer       uid
	
	static hasMany = [subItems:SubItem]
	static belongsTo = [iteration:Iteration,group:ItemGroup]//, project:Project]
	static fetchMode = [subItems:"eager"]
	
	static constraints  = {
		description(maxSize:255)
		iteration(nullable:true)
		group(nullable:true)
		comment(nullable:true,maxSize:1024)
		criteria(nullable:true,maxSize:1024)
		itemPoints(scale:1)	
		uid(nullable:true)
		//project(nullable:false)
	}

	Item()  { subItems = [] }
	
	Item(def group)
	{
		stampUid()
		group = group
		description = "...."
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
	
	static def _getUnfinishedItemsGroupMapWithItemCheck(Closure itemCheck)
	{
		def itemsByGroup = [:]		
		def groups = ItemGroup.list()
						
		groups.each{ group ->
			itemsByGroup[group] = []
			group.items?.each{ item ->
				if ( item.status != ItemStatus.Finished) {
					if (itemCheck(item))
					{
						itemsByGroup[group] << item
					}
				} 
			}
		}
		
		return itemsByGroup
	}
	
	static def getUnfinishedItemsGroupMap(List priorityList)
	{
		def itemCheck = { item ->
			return priorityList.contains(item.priority)   
		}
		
		return 	_getUnfinishedItemsGroupMapWithItemCheck(itemCheck)
	}
	
	static def getUnfinishedItemsGroupMap()
	{
		return 	_getUnfinishedItemsGroupMapWithItemCheck({ item -> true})
	} 
	
	static def maxUid()
	{
		def items = Item.list(max:1,sort:'uid',order:'desc')
		return items ? (items[0].uid + 1) : 1
	}
	
	def stampUid()
	{		
		this.uid = maxUid()				
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
}
