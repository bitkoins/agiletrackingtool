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

class ItemController {
	def scaffold = Item
	def itemService
	def itemGroupService
	
	static navigation = [
		group:'tags',
		order:20, 
		title:'Backlog', 
		action:'backlog' ,
		isVisible: { session.project != null },
		subItems: [
			[action:'backlog', order:1, title:'Backlog'],
			[action:'showAll', order:10, title:'Show all items'],
			[action:'showSorted', order:50, title:'Show items sorted by points'],
			[action:'notInIteration', order:70, title:'Show items without iteration'],
			[action:'listGroups', order:90, title:'Show groups']
		] 
	]
	
	def backlog = {
			def title = "Backlog"
			def itemsByGroup =  params.priorities ? 
						itemService.getUnfinishedItemsGroupMap(session.project, Util.parsePriorities(params.priorities)) :
			            itemService.getUnfinishedItemsGroupMap(session.project)
			
			return [itemsByGroup:itemsByGroup,title:title]
	}
	
	def notInIteration = {
			def itemsByGroup = itemService.getUnfinishedItemsGroupMap(session.project)
			def itemsByGroupFiltered = itemService.removeItemsWithIteration(itemsByGroup)
			
			def title = "Backlog for items not in Iteration"
			render(view:"backlog",model:[itemsByGroup:itemsByGroupFiltered,title:title])
	}
	
	def showIterationItems = {
			def iter = params.id ? Iteration.get(params.id) : Iteration.getOngoingIteration()
			
			if(!belongsToProject(iter))
			{
				redirect(controller:'project',action:'list')
				return
			}
			
			def unfinishedItems = iter.items.findAll{ item.checkUnfinished() } 
			def itemsByGroup = itemGroupService.transformToItemsByGroup( unfinishedItems )
			
			def title = "Items in ${iter.workingTitle}"
			render(view:"backlog",model:[iteration:iter,itemsByGroup:itemsByGroup,title:title])
	}
		
	def editItem = {
		def item = Item.get(params.id)
		
		if(!belongsToProject(item))
		{
			redirect(controller:'project',action:'list')
			return
		}
		
		render(template:'/shared/item/edit',model:[item:item])
	}
	
	def saveItem = {
		def item = Item.get(params.id)
		
		if(!belongsToProject(item))
		{
			redirect(controller:'project',action:'list')
			return
		}
		
		ItemParamsParser.updateItemWithParams(item,params, {param -> request.getParameterValues(param)} )
		item.save()
		render(template:'/shared/item/show',model:[item:item])
	}
	
	def deleteItem = {
		def item = Item.get(params.id)
		
		if(!belongsToProject(item))
		{
			redirect(controller:'project',action:'list')
			return
		}
		
		itemService.deleteItem(item)
		render ""
	}
	
	def addItemToGroup = {
		def group = ItemGroup.get(params.id)
		
		if(!belongsToProject(group))
		{
			redirect(controller:'project',action:'list')
			return	
		}
			
		def item = new Item(session.project,group)
		itemGroupService.addItem(group, item )
		def newItemId = Integer.parseInt(params.newItemId) + 1
		
		render(template:'/shared/item/editNewItem',model:[item:item,groupId:item.group.id,newItemId:newItemId])
	}
	
	def showAll = {
			return [groups:ItemGroup.findAllByProject(session.project)] 
	}
	
	def showSorted = {
			def allItems = []
			def groups = itemService.getUnfinishedItemsGroupMap(session.project)
			
			groups.each{ group, items -> items.each{ item ->
					allItems << item 
				}
			}
			
			def sum = allItems.sum{it.points}
			def average = allItems.size() ? sum/ allItems.size() : 0 
						
			return [ items: allItems.collect{it}.sort{it.points}, sum:sum, average:average]
	}	
	
	def listGroups = {
		redirect(controller:'itemGroup', action:'list')
	}
	
	def belongsToProject(def item)
	{
		return (item && (item.project.id == session.project.id))
	}
}
