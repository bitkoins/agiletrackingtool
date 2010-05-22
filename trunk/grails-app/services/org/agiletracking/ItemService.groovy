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

class ItemService {
    static transactional = true
	    
    def itemGroupService

	def getUnfinishedItemsGroupMap(def project)
	{
		def items = getUnfinishedItems(project)
		def groups = ItemGroup.findAllByProject(project)
		return itemGroupService.transformToItemsByGroup(groups, items)
	}

	def getUnfinishedItems(def project)
	{
		return Item.findAllByProjectAndStatusNotEqual(project,ItemStatus.Finished)
	}

	def deleteItem(def item)
	{
		item.iteration?.deleteItem(item.id)
		item.group?.deleteItem(item.id)
		item.delete()
	}

	def retrieveUnfinishedItemsForProject(def project, def itemIdList)
	{
		return itemIdList.collect{ Item.get(it) }.findAll{ 
			it?.project?.id == project.id && it?.checkUnfinished() }
	}

	void removeItemsFromList(def itemList, def itemIdsToRemove)
	{
		itemIdsToRemove.each{ rid ->
			def itemToRemove = itemList.find{ it.id == rid} 
			itemList.remove(itemToRemove)
		}		
	}

	def matchItemsWithUid(def items, def uidList)
	{
		return uidList.collect{ uid -> items.find{it.uid == uid} }.findAll{ it }
	}
}
