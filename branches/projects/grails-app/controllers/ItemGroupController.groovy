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

class ItemGroupController {
	def scaffold = ItemGroup
	
	static navigation = [
		group:'itemGroup', 
		subItems: [
			[action:'list', order:1, title:'Show groups'],
			[action:'create', order:10, title:'Create new group']			
		] 
	]

	def list = { [groups:ItemGroup.list()] }
	
	def delete = { 
		def group = ItemGroup.get(Integer.parseInt(params.id))
		if ( group) {
			
			group.items.collect{it}.each{ item ->			
				item.iteration?.deleteItem(item.id)
				item.group?.deleteItem(item.id)
	    		item.delete()
			}
			
			PointsSnapShot.list().each{ snapShot ->
				def pointsForGroup = snapShot.getPointsForGroup(group)
				
				if (pointsForGroup) {
	   		        pointsForGroup.snapShot.removeFromPointsForGroups(pointsForGroup)
					pointsForGroup.delete()					
				}
			}
			
			group.delete(flush:true)
		}
			
		redirect(action:'list')
	}
}
