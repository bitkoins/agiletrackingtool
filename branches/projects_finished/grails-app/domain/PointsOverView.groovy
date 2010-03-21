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

class PointsOverView
{
		Map map
		static belongsTo = PointsSnapShot
		static hasMany = [map:Double]
		
		PointsOverView()
		{
			map = [:]
		}
		
		def key(def priority,status)
		{
			return new String("${priority}.${status}")
		}
		
		void setPointsForView(def priority,def status,def points)
		{
			map[key(priority,status)] = new Double(points)
		}
		
		def getPointsForView(def priority, def status)
		{
			def k = key(priority,status)
			return map.containsKey(k) ? map[k] : 0.0
		}
		
		def getTotalPoints()
		{
			def total = 0.0
			map.each{ key, points -> total += points }
			return total
		}
		
		def getPointsForPriority(def priority)
		{
			def total = 0.0
			ItemStatus.each{ status -> total += getPointsForView(priority,status) }
			return total
		}
		
		def getPointsForItemStatus(def status)
		{
			def total = 0.0
			Priority.each{ priority -> total += getPointsForView(priority,status) }
			return total
		}
		
		static def createOverView(def items)
		{
			def ret = new PointsOverView()
			items.each{ item ->
				ret.addPointsForView(item.priority, item.status, item.points)
				print "."
			}
			return ret
		}
		
		def addPointsForView(def priority, def status, def points)
		{
			def newPoints = points + getPointsForView(priority,status)
			setPointsForView(priority,status, newPoints)
		}
		
		String toString()
		{
			return "PointsOverView - Total/Finished: ${getTotalPoints()}/${getPointsForItemStatus(ItemStatus.Finished)}"
		}
		
		boolean equals(def other)
		{
			if (!(other instanceof PointsOverView))  return false
			boolean equal = true
			map.each{ key, points ->
				if (equal && (points != other.map[key]) ) {
					equal = false
				}
			}
			return equal
		}
}

