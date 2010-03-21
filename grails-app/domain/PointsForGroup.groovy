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

class PointsForGroup
{	
	PointsOverView overView	 
	static belongsTo = [ group : ItemGroup, snapShot : PointsSnapShot ]
	
	PointsForGroup(def group, def snapShot) 
	{
		this.group = group
		this.snapShot = snapShot
		overView = new PointsOverView()
	}

	PointsForGroup() {} 
	
	String toString()
	{
		return "${group}: ${overView}"
	}
	
	static def deleteWholeGroup(def group)
	{
		findAllByGroup(group).each{ pointsForGroup ->
			    pointsForGroup.snapShot.removeFromPointsForGroups(pointsForGroup)
				pointsForGroup.delete()					
		}
	}
}

