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

class Defaults {
	
	static Collection<Iteration> getIterations(Integer nr, Project project)
	{
		Collection<Iteration> ret = []
		nr.times {
			Iteration iter = new Iteration(project:project)
			iter.workingTitle = "Iteration-${it}"
			iter.status = IterationStatus.FutureWork
			iter.startTime = new Date() - 10
			iter.endTime = new Date()
			iter.items = []
			
			ret << iter
		}
		
		return ret
	}
	
	static Collection<ItemGroup> getGroups(Integer nr, Project project = getProjects(1)[0] )
	{
		Collection<ItemGroup> ret = []
		nr.times {
			def group = new ItemGroup()			
			group.name = "Category-${it}"
			group.id = it + 1
			group.items = []
			group.project = project
			
			ret << group
		}
		
		return ret
	}
	
	static Collection<Item> getItems(Integer nr, Collection<ItemGroup> groups, Project project = getProjects(1)[0], Integer maxUid = 0)
	{
		maxUid = maxUid ? maxUid : Item.maxUid()				
		Collection<Item> ret = []
		def prios = [Priority.Low, Priority.Medium, Priority.High]
		
		Collection points = []
		9.times{ points << it }
		
		nr.times{ index -> 
			def item = new Item(project:project)
			item.uid = index + (maxUid + 1)
			item.id = item.uid + 1 
			item.description = "Example Item  ${item.uid}"			
			item.points = Util.random(points)
			item.dateCreated = new Date() - 10
			item.lastUpdated = new Date()
			
			if (groups)
			{
				def group = Util.random(groups)
				group.addItem(item)
			}
			else
			{
				item.group = null
			}
			
			item.status = ItemStatus.Request
			item.priority = Util.random(prios)
			item.subItems = []
			item.comment = "Comment for item ${index}"
			item.criteria = "Criteria for item ${index}"
			
			ret << item
		}
		return ret
	}
	
	static Collection<SubItem> getSubItems(Integer nr, Collection<Item> items)
	{
		Collection<SubItem> ret = []
		
		Collection points = []
		6.times{ points << it }
		
		nr.times{
			def subItem = new SubItem()
			subItem.id = it
			subItem.description = "SubItem ${it}"
			subItem.points = Util.random(points)
			subItem.status = ItemStatus.Request
			def item = Util.random(items)
			item.addSubItem(subItem)
			ret << subItem
		}
		return ret
	}
	
	static PointsOverView getPointsOverView()
	{
		def myRandom = { Math.round(Math.random()*100.0) }
		def overView = new PointsOverView()
		Priority.each{ prio -> 
			ItemStatus.each{ status -> 
								overView.setPointsForView(prio, status, myRandom()) 
			}
		}
		return overView	
	}
	
	static Collection<PointsSnapShot> getSnapShots(Collection<ItemGroup> groups, 
				Date startDate, Date endDate, Project project = getProjects(1)[0])
	{
		def snapShots = []
							
		(startDate..endDate).eachWithIndex{ date, index ->
			def snapShot = new PointsSnapShot(project, date)
			snapShot.id = index + 1
			snapShot.overView = getPointsOverView()
			
			groups.each{ group ->
				def pointsForGroup = new PointsForGroup(group,snapShot)
				pointsForGroup.overView = getPointsOverView()
				snapShot.pointsForGroups << pointsForGroup
			}
			snapShots << snapShot
		}
		return snapShots
	}
	
	static Collection<Project> getProjects(Integer nr)
	{
		def projects = []
		nr.times{
			def name = "Project-${it}"
			def project = new Project(name:"${name}" )
			project.id = it 
			projects << project
		}
		return projects 
	}
}
