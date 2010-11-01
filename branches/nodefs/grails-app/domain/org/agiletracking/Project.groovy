/*----------------------------------------------------------------------------
Project: Agile Tracking Tool

Copyright 2008, 2009, 2010   Ben Schreur
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

class Project {
	String name
	String prioritizedItemIds
   ProjectType type	

	static belongsTo = [user:User]
	static String seperator = ","

        static mapping = {
           prioritizedItemIds type: 'text'
        }

	static constraints  = {
	   name(blank:false)
	   user(nullable:true)
	   prioritizedItemIds(nullable:true)
		type(nullable:true)
	}

	void setPrioritizedItemUidList(Collection<Integer> itemUidList)	
	{
	    prioritizedItemIds = itemUidList.join(seperator)
	}

	Collection<Integer> getPrioritizedItemUidList()		
	{
	    return prioritizedItemIds?.size() ?
	           prioritizedItemIds.split(seperator).collect{ Item.parseUid(it) } : []
	}

	boolean usesKanban() {
		return type == ProjectType.Kanban
	} 
}
