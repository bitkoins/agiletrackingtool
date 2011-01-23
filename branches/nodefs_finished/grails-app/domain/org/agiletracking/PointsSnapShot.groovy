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

import java.util.Date

class PointsSnapShot
{
	Date            date
	PointsOverView  overView
	
	static  hasMany = [ pointsForGroups: PointsForGroup ]
	static belongsTo = [project:Project]
	
	static constraints = {
		project(nullable:false)
	}
	
	PointsSnapShot() { }

	PointsSnapShot(Project project, Date date)
	{
		this.project = project
		this.date = date
		overView = new PointsOverView()
		pointsForGroups = []
	}	
		
	PointsForGroup getPointsForGroup(ItemGroup group)
	{
		return pointsForGroups.find{ it.group == group }		
	}
	
	static PointsSnapShot getSnapShotClosestTo(Project project, Date date, Integer maximumDaysOffset)
    {
        def snapShots = getSnapShotsBetween(project,date-maximumDaysOffset, date+maximumDaysOffset)
        if (!snapShots) return null
        return snapShots.sort{ Math.abs(Util.getDaysInBetween(it.date,date)) }[0]
    }
	
	static Collection<PointsSnapShot> getSnapShotsBetween(Project myProject, Date date1, Date date2)
	{
		 return PointsSnapShot.createCriteria().list {
		 	 and {
		 	 	project { eq("id",myProject.id) } 		 	 
			 	between('date',date1,date2)
			 }
		 }
	}
	
	def beforeInsert = {
		this.overView.save()
		
		this.pointsForGroups.each{ 
			it.overView.save()
			it.snapShot = null
			it.save()
			it.snapShot = this
		}
	}
	
	static PointsSnapShot takeSnapShot(Project project, Date date) 
	{
		def ret = new PointsSnapShot(project, date)
		def groups = ItemGroup.findAllByProject(project)
		def allItems = []
		groups.each{ group -> group.items.each{ allItems << it } }
		ret.overView = PointsOverView.createOverView(allItems)
		
		groups.each{ group ->
			def pointsForGroup = new PointsForGroup(group, ret)
			pointsForGroup.overView = PointsOverView.createOverView(group.items)
			ret.pointsForGroups << pointsForGroup
		}
		
		return ret
	}
}
