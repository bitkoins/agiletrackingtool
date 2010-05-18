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

class ItemController {
	def itemService
	def itemGroupService
	def projectService
       	
	static navigation = [
		group:'tags',
		order:20, 
		title:'Backlog', 
		action:'backlog' ,
		isVisible: { session.project != null },
		subItems: [
			[action:'backlog', order:1, title:'Backlog'],
			[action:'prioritize', order:5, title:'Prioritize'],
			[action:'showAll', order:10, title:'Show all items'],
			[action:'createGroup', order:15, title:'Create new category'],
			[action:'listGroups', order:90, title:'Show categories']
		] 
	]
	
	def backlog = {
			def itemsByGroup = itemService.getUnfinishedItemsGroupMap(session.project)
			return [itemsByGroup:itemsByGroup]
	}
	
	def showAll = {
			return [groups:ItemGroup.findAllByProject(session.project)] 
	}
	
	def editItem = {
		def item = Item.get(params.id)
		flash.projectCheckPassed = projectService.executeWhenProjectIsCorrect(session.project, item)
		render(template:'/shared/item/edit',model:[item:item])
	}
	
	def saveItem = {
		def item = Item.get(params.id)
		
		ItemParamsParser.updateItemWithParams(item,params, {param -> request.getParameterValues(param)} )
		flash.projectCheckPassed = projectService.executeWhenProjectIsCorrect(session.project, item,
		                                                                     { item.save() }) 

		render(template:'/shared/item/show',model:[item:item] )
	}
	
	def deleteItem = {
		def item = Item.get(params.id)
		flash.projectCheckPassed = projectService.executeWhenProjectIsCorrect(session.project, item,
																	{ itemService.deleteItem(item) } )
		render(text:"")
	}
	
	def addItemToGroup = {
		def group = ItemGroup.get(params.id)
		def item = new Item(session.project,group)
		flash.projectCheckPassed = projectService.executeWhenProjectIsCorrect(session.project, group,
																	{ group.addItem(item); group.save() })
		
	    def newItemId = Integer.parseInt(params.newItemId) + 1
		render(template:'/shared/item/editNewItem',
		       model:[item:item,groupId:item.group.id,newItemId:newItemId]) 
	}
	
	def listGroups = {
		redirect(controller:'itemGroup', action:'list')
	}

	def createGroup = {
		redirect(controller:'itemGroup', action:'create')
	}

	def prioritize = { 
				  def itemIdList = Project.get(session.project.id).getPrioritizedItemIdList()
				  def items = itemService.getUnfinishedItems(session.project)
				  def prioItems = itemService.retrieveUnfinishedItemsForProject(session.project, itemIdList) 
				  def itemsByGroup = itemGroupService.transformToItemsByGroup(items.collect{it.group}.unique(),
																										  items-prioItems)

				  return [prioItems:prioItems, itemsByGroup:itemsByGroup, message:flash.message ]
   }

   def savePriorities = {
				  def itemIdList = params['prioItems_input'].split(" ")
				  itemIdList = itemIdList.findAll{ it && !it.contains("dummy") }
				  itemIdList = itemIdList.collect{ Integer.parseInt(it.split("_")[1]) } 
				  
				  def prioItems = itemService.retrieveUnfinishedItemsForProject(session.project,itemIdList)
				  def project = Project.get(session.project.id)
				  project.setPrioritizedItemIdList(prioItems.collect{it.id})
				  project.save()
				  redirect(action:"backlog")
   }
}
