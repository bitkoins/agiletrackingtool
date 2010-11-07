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

class ItemGroupService {
    static transactional = true

    Map<ItemGroup,Collection<Item>> transformToItemsByGroup(
					Collection<ItemGroup> groups, Collection<Item> items)
    {
    	def itemsByGroup = [:]

    	groups.each{ group -> itemsByGroup[group] = [] }
    	items.each{ item ->
    		def foundGroup = groups.find{ item.group.id == it.id}  
    		if(foundGroup) itemsByGroup[foundGroup] << item 
    	}
    	return itemsByGroup 
    }

     void removeItemsFromGroupMap(Collection<Item> itemsToRemove, 
							   			 Map<ItemGroup,Collection<Item>> itemsByGroup)
     {
     	  itemsToRemove.each{ item ->
		     def foundGroup = itemsByGroup.find{ it.key.id == item.group.id }.key
   		  if(foundGroup) itemsByGroup[foundGroup] -= item
        }
     }
	   
     void deleteWholeGroup(ItemGroup group)
     {
		group.items.collect{it}.each{ item ->
			item.iteration?.deleteItem(item.id)
			item.group?.deleteItem(item.id)
	        	item.delete()
		}
		group.delete()
    }
}
