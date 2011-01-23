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

    def transformToItemsByGroup(def groups, def items)
    {
    	def itemsByGroup = [:]
		groups.each{ group -> 
    		itemsByGroup[group] = group.items.findAll{ itemOfGroup -> items.find{ it.uid == itemOfGroup.uid } }  
    	}
    	return itemsByGroup 
    }

     def removeItemsFromGroupMap(def itemsToRemove, def itemsByGroup)
     {
		itemsByGroup.keySet().each{ group -> 
				itemsByGroup[group].removeAll( itemsToRemove ) 
		}
    }
	   
    def unloadItemsAndDelete(def group)
    {
	   group.items?.collect{it}.each{ item ->			
        	group.deleteItem(item.id)
		}	
		group.delete()
   }

	void deleteItem(Item item) {
		def groups = ItemGroup.findAllByProject(item.project).findAll{ it.hasItem(item.id) }
		groups.each{ group -> 
			group.deleteItem(item.id)
		}
	}
}
