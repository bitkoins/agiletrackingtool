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

class IterationComposerController {
		
	def compose = {
		def iter
		if ( !params.id ) {
			redirect(action:"list")			
		}
		else {
			iter = Iteration.get(params.id)
		}
		
		session.iterId = iter?.id
				
		def itemsByGroup
		if ( params.priorities) {
			itemsByGroup = Item.getUnfinishedItemsGroupMap(session.project,Util.parsePriorities(params.priorities))
		}
		else {
			itemsByGroup = Item.getUnfinishedItemsGroupMap(session.project)
		}
		
		itemsByGroup.each{ group, items ->
				iter.items.each{ items.remove(it) } 
		}
		
		return [iteration:iter, itemsByGroup:itemsByGroup, isCompositionView:true]
	}
	
	def addItemToIteration = {
			def iter = Iteration.get(session.iterId)
			def id = Integer.parseInt(params.id)
			def item = Item.get(params.id)
			iter.addItem(item)
			
			render(template:'iterationOverview',model:[iteration:iter])
	}
	
	def deleteItemFromIteration = {
			def iter = Iteration.get(session.iterId)
			def id = Integer.parseInt(params.id)
			iter.deleteItem(id)
				
			render(template:'iterationOverview',model:[iteration:iter])
	}
	
	def editItem = {
		redirect(controller:'item',action:'editItem',params:params)
	}	
	
	def saveItem = {
		redirect(controller:'item',action:'saveItem',params:params)
	}
	
	def belongsToProject(def item)
	{
		return (item && (item.project.id == session.project.id))
	}
}
