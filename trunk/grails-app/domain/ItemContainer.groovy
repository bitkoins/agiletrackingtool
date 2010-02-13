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

class ItemContainer {
	static hasMany = [items:Item]
	static fetchMode = [items:"eager"]
	
	/* Cannot define project-releation in ItemContainer as for grails-1.2.0.
	 * Therefore project relation is defined twice in Iteration and ItemGroup. 
	 */
					
    ItemContainer()
    {
        items = []
    }

	void _addItem(Item item, String belongsToFieldAsString)
	{
		def belongsToInstance = item[belongsToFieldAsString]
	
		if(belongsToInstance)
		{
			if ( this.id != belongsToInstance.id ) {
				belongsToInstance.deleteItem(item?.id)
			}
		}
		
		if ( !this.items?.find{ it == item } ) {
			this.addToItems(item)
		}
		
		item[belongsToFieldAsString] = this
	}
	
	
	void deleteItem(def id)
	{
		if ( hasItem(id) ) {
			def item = getItem(id)
			this.removeFromItems(item)
		}
	}
	
	Item getItem(def id) 
	{
		return items.find{ it.id == id }
	}
	
	boolean hasItem(def id)
	{
		return getItem(id) != null
	}
}	
