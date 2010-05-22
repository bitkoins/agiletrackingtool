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

class IterationComposerController {
	def itemService
	def itemGroupService
	def projectService
		
	def compose = {
		def iter = Iteration.get(params.id)
		
		if ( !iter ) {
			redirect(action:"list")
			return			
		}

		flash.projectCheckPassed = projectService.executeWhenProjectIsCorrect(session.project, iter)
		
      def items = itemService.getUnfinishedItems(session.project)
		def prioItemUidList = Project.get(session.project.id).getPrioritizedItemUidList()
		def prioItems = itemService.matchItemsWithUid(items,prioItemUidList)

		itemService.removeItemsFromList(items,prioItems.collect{it.id})
		def iterItemIds = iter.items.collect{it.id}
		[items,prioItems].each{ mitems -> itemService.removeItemsFromList(mitems,iterItemIds) }
		
		def itemsByGroup = [:] 
		def prioGroup = new ItemGroup(name:"Prioritized items")
		itemsByGroup[prioGroup]= prioItems
		itemsByGroup += itemGroupService.transformToItemsByGroup(items.collect{it.group}.unique(),
                                                               items)
		
		return [iteration:iter, itemsByGroup:itemsByGroup, isCompositionView:true]
	}
	
	def addItemToIteration = {
			def iter = Iteration.get(Integer.parseInt(params.iterId))
			def item = Item.get(params.id)
			
			flash.projectCheckPassed = iter && item && (item.project.id == iter.project.id) &&
                       projectService.executeWhenProjectIsCorrect(
                               session.project,  item, {  iter.addItem(item) } ) 	
	
			render(template:'iterationOverview',model:[iteration:iter])
	}
	
	def deleteItemFromIteration = {
			def iter = Iteration.get(params.iterId)
			
			def item = Item.get(params.id)
			flash.projectCheckPassed = projectService.executeWhenProjectIsCorrect(session.project,  item,
																				 { iter.deleteItem(item.id) } ) 
			
			render(template:'iterationOverview',model:[iteration:iter])
	}
	
	def editItem = {
		redirect(controller:'item',action:'editItem',params:params)
	}	
	
	def saveItem = {
		redirect(controller:'item',action:'saveItem',params:params)
	}
}
