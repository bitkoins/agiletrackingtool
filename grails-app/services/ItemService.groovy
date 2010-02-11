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

class ItemService {
    static transactional = true
	static scope = "session"
	    
    def itemGroupService

    def _getUnfinishedItemsGroupMapWithItemCheck(def project, Closure itemCheck)
	{
		def itemsByGroup = [:]		
		def items = Item.findAllByProject(project)
		def selectedItems = items.findAll{ item -> (item.status != ItemStatus.Finished) && itemCheck(item) }  
		def groups = ItemGroup.findAllByProject(project)
		
		return itemGroupService.transformToItemsByGroup(groups, selectedItems)
	}
	
	def getUnfinishedItemsGroupMap(def project, List priorityList)
	{
		def itemCheck = { item ->
			return priorityList.contains(item.priority)   
		}
		
		return 	_getUnfinishedItemsGroupMapWithItemCheck(project,itemCheck)
	}
	
	def getUnfinishedItemsGroupMap(def project)
	{
		return 	_getUnfinishedItemsGroupMapWithItemCheck(project, { item -> true})
	}
	
	def removeItemsWithIteration(def itemsByGroup)
	{
		def itemsByGroupFiltered = [:]
		itemsByGroup.each{ group, items ->
		
			itemsByGroupFiltered[group] = []
			items.each{ item ->	
					if (!item.iteration) itemsByGroupFiltered[group] << item
			}
		}
		return itemsByGroupFiltered
	}
	
	def deleteItem(def item)
	{
		item.iteration?.deleteItem(item.id)
		item.group?.deleteItem(item.id)
		item.delete()
	}
	
	def saveItem(def item)
	{
		item.subItems*.save()
		item.save()
	}
	
}
