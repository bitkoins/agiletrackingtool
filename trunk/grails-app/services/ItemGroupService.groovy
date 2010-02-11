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

class ItemGroupService {
    static transactional = true
    static scope = "session"

    def transformToItemsByGroup(def groups, def items)
    {
    	def itemsByGroup = [:]
    	groups.each{ group -> itemsByGroup[group] = [] }
    	items.each{ item -> itemsByGroup[ groups.find{ item.group.id == it.id} ] << item }
    	return itemsByGroup 
    }
    
    def addItem(def group, def item)
    {
    	item.save()		
		group.addItem(item)
		group.save()
	}
	
	def deleteWholeGroup(def group)
	{
		group.items.collect{it}.each{ item ->
			item.iteration?.deleteItem(item.id)
			item.group?.deleteItem(item.id)
	    	item.delete()
		}
		
		group.delete()
	}
}
