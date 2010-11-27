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

class Util {
	
	static def random(Collection list)
	{
		 return list[ Math.round(Math.random()*(list.size()-1)).toInteger() ]
	}
	
	static Collection reScale(Collection list, Double min, Double max)
	{
		def scale = max-min
		if ( Math.abs(scale) < 1e-9 ) return list
	
		return list.collect{ (it*1.0-min)/scale*100 }
	}

	
	static Integer _calculateReductionFactor(Integer listSize, Integer maxSize)
	{
		return Math.ceil(new Double(listSize) / maxSize).toInteger()
	}
	
	static Collection makeListShorterWithScaling(def list, def maxSize)
	{
		if ( list.size() <= maxSize) return list
		def retList = []
		if ( maxSize == 0) return retList
		def factor = _calculateReductionFactor(list.size(), maxSize)
		
		def nrElements = Math.ceil(list.size()/factor)
		nrElements.times{ time ->
			def startIndex = factor*time
			def endIndex = startIndex + (factor-1)

			if ( endIndex  >= list.size() ) endIndex = (list.size()-1)
			retList << list[startIndex..endIndex].sum{it}/(endIndex-startIndex+1)
		}
		return retList
	}
	
	static Integer getDaysInBetween(Date d1, Date d2)
	{
		def days = (d2.getTime()-d1.getTime() )/ (24*60*60*1000)
		def ret =  Math.round(days).toInteger()	
		return ret
	}
	
	static Collection<String> parsePriorities(String priorities)
	{
		priorities.split(",").collect{ prio -> Priority.valueOf(prio) }
	}
	
	static void emptyDataBase()
	{
		PointsSnapShot.list()*.delete()
		def allitems = Item.list()
		def alliterations = Iteration.list()
		allitems.each{ item -> item.group.deleteItem(item.id); item.iteration?.deleteItem(item.id) }
		alliterations*.delete()
		ItemGroup.list()*.delete()
		allitems*.delete()
		Project.list()*.delete()
	}
	
	static IntRange getDefaultYearRange()
	{
		def year = new Date().year + 1900
		return (year-3)..(year+3)
	}
}

