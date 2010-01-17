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
	
	static navigation = [
		group:'tags', 
		order:20, 
		title:'Backlog', 
		action:'backlog' ,
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
			def itemsByGroup
			
			if ( params.priorities ) {				
				itemsByGroup = Item.getUnfinishedItemsGroupMap(session.project, Util.parsePriorities(params.priorities))
			}
			else {
				itemsByGroup = Item.getUnfinishedItemsGroupMap(session.project)
			}
			
			return [itemsByGroup:itemsByGroup,title:title]
	}
	
	def notInIteration = {
			def itemsByGroup = Item.getUnfinishedItemsGroupMap(session.project)
			
			def itemsByGroupFiltered = [:]
			itemsByGroup.each{ group, items ->
			itemsByGroupFiltered[group] = []
			items.each{ item ->
					if ( !item.iteration )itemsByGroupFiltered[group] << item
				}
			}
			
			def title = "Backlog for items not in Iteration"
			render(view:"backlog",model:[itemsByGroup:itemsByGroupFiltered,title:title])
	}
	
	def showIterationItems = {
			def iter 
			if (params.id) {
				def id = Integer.parseInt(params.id)
				iter = Iteration.get(id)
			}
			else {
				iter = Iteration.getOngoingIteration()	
			}
			
			if(!belongsToProject(iter))
			{
				redirect(controller:'project',action:'list')
				return
			}
			
			def itemsByGroup = [:]
			
			def items = iter.items
			items.each{ item ->
				if ( !itemsByGroup.containsKey(item.group)) {
					itemsByGroup[item.group] = []
				}
				
				if ( item.checkUnfinished() ) {
					itemsByGroup[item.group] << item
				}
			}
			
			def title = "Items in ${iter.workingTitle}"
			render(view:"backlog",model:[iteration:iter,itemsByGroup:itemsByGroup,title:title])
	}
		
	def editItem = {
		def item = Item.get(Integer.parseInt(params.id))
		
		if(!belongsToProject(item))
		{
			redirect(controller:'project',action:'list')
			return
		}
		
		render(template:'/shared/item/edit',model:[item:item])
	}
	
	def saveItem = {
		def item = Item.get(Integer.parseInt(params.id))
		
		if(!belongsToProject(item))
		{
			redirect(controller:'project',action:'list')
			return
		}
		
		ItemParamsParser.updateItemWithParams(item,params, {param -> request.getParameterValues(param)} )
		item.subItems*.save()
		item.save()
		render(template:'/shared/item/show',model:[item:item])
	}
	
	def deleteItem = {
		def item = Item.get(Integer.parseInt(params.id))
		
		if(!belongsToProject(item))
		{
			redirect(controller:'project',action:'list')
			return
		}
		
		item.iteration?.deleteItem(item.id)
		item.group?.deleteItem(item.id)
		item.delete()
		render ""
	}
	
	def addItemToGroup = {
		def group = ItemGroup.get(Integer.parseInt(params.id))
		
		if(!belongsToProject(group))
		{
			redirect(controller:'project',action:'list')
			return	
		}
			
		def item = new Item(session.project,group)
		item.save()
		
		group.addItem(item)
		group.save()

		def newItemId = Integer.parseInt(params.newItemId)
		newItemId = newItemId + 1
		
		render(template:'/shared/item/editNewItem',model:[item:item,groupId:item.group.id,newItemId:newItemId])
	}
	
	def showAll = {
			def groups = ItemGroup.findAllByProject(session.project)
			
			def totalPoints = 0
			def unFinishedPoints = 0
			groups.each{ group -> group.items.each{ 
				totalPoints += it.points
					if ( it.checkUnfinished() ) unFinishedPoints += it.points
				}
			}
			return [groups:groups, totalPoints:totalPoints, unFinishedPoints:unFinishedPoints ] 
	}
	
	def showSorted = {
			def allItems = []
			def groups = Item.getUnfinishedItemsGroupMap(session.project)
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
