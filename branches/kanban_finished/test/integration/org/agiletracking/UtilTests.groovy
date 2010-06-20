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

class UtilTests extends GroovyTestCase 
{
	void testParsingPriorities()
	{
		def priorities = [ Priority.High, Priority.Low ] 
		def parsed = Util.parsePriorities( priorities.collect{ it.toString() }.join(",") )
		assertTrue parsed.size() == priorities.size()
		priorities.eachWithIndex{ it,index -> assertTrue parsed[index] == it }
	}
	
	void testDaysInBetween()
	{
		def now = new Date()
		assertTrue Util.getDaysInBetween(now, now ) == 0
		assertTrue Util.getDaysInBetween(now , now + 10 ) == 10
		assertTrue Util.getDaysInBetween(now + 10, now ) == -10
	}
	
	void testmakeListShorterWithScalingForAlreadyShortList()
	{
		assertTrue Util.makeListShorterWithScaling([4,2,3],10) == [4,2,3]
	}
	

	static void checkListsEqual(def list1, def list2)
	{
		assertTrue list1.size() == list2.size()
		list1.eachWithIndex{ it, index -> assertTrue Math.abs(it-list2[index]) < 1e-9 }
	}

	void testmakeListShorterWithScalingToSizeWhenListIsAbout2TimesTooLarge()
	{
		assertTrue Util.makeListShorterWithScaling([4,2,10],2) == [3,10] 
		assertTrue Util.makeListShorterWithScaling([4,2,20,10],2) == [3,15]
	}
	
	void testmakeListShorterWithScalingToSizeWhenListIsAbout3TimesTooLarge()
	{
		assertTrue Util.makeListShorterWithScaling([1,1,1,2,2,2,3,3,3],4) == [1,2,3]
		assertTrue Util.makeListShorterWithScaling([1,1,1,2,2,2,3,3,3,4,4],4) == [1,2,3,4]
	}
	
	void testFactorCalculation()
	{
		assertTrue Util._calculateReductionFactor(1,1) == 1
		assertTrue Util._calculateReductionFactor(2,1) == 2
		assertTrue Util._calculateReductionFactor(5,2) == 3
		assertTrue Util._calculateReductionFactor(8,4) == 2
		assertTrue Util._calculateReductionFactor(9,4) == 3
	}
}
