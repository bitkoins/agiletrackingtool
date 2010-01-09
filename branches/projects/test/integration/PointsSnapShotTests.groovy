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

class PointsSnapShotTests extends GroovyTestCase {
	def groups 
	def items
	def snapShot
	def defaultItemPoint = 2
	def date 
		
	void setUp() {
		groups = Defaults.getGroups(3)
		items = Defaults.getItems(3,groups)
		items.each{ it.itemPoints = defaultItemPoint }
		this.date = new Date()
		takeSnapShot()
	}
	
	void tearDown() {
	}
	
	void takeSnapShot()
	{
		snapShot = PointsSnapShot.takeSnapShot(groups,this.date)
	}
	
	void testDateIsCorrect() 
	{
		assertTrue date == snapShot.date
	}
	
	void testNumberOfGroups()
	{
		assertTrue snapShot.pointsForGroups.size() == groups.size()
	}
	
	void testRetrieveCorrectGroupAndDate()
	{
		groups.each{ group ->
			def pointsForGroup = snapShot.getPointsForGroup(group)
			assertTrue pointsForGroup.group == group
		}
	}
	
	void testCorrectTotalPointsForGroup()
	{
		def group = groups[0]
		items.each{ group.addItem(it) }
		takeSnapShot()
		def pointsForGroup = snapShot.getPointsForGroup(group)
		assertTrue pointsForGroup.overView.equals( PointsOverView.createOverView(items))
	}
	
	void testSavingAndRetrievingSnapShot()
	{
		groups*.save()
		items*.save()
		
		if ( !snapShot.validate() )
			snapShot.errors.allErrors.each { println it }
    	
		assertNotNull snapShot.save(flush:true)
		
		def snapShotSaved = PointsSnapShot.get(snapShot.id)
		assertTrue snapShotSaved.pointsForGroups.size() == snapShot.pointsForGroups.size()
		
		snapShot.delete()
		items*.delete()
		groups*.delete()
	}
	
	static List setUpSnapShotList(def nowDate, def groups)
	{
        def snapShots = []
        10.times{
            def ps, pointsForGroupList
    		ps = PointsSnapShot.takeSnapShot(groups,nowDate - it)
    		snapShots << ps
        }
        
        return snapShots
	}
	
	void testRetrievingSnapShotClosestToDate()
    {
        groups*.save()
        items*.save()
        
     	def nowDate = new Date()   
        def snapShots = setUpSnapShotList(nowDate,groups)
        snapShots*.save()
     
       
        assertTrue PointsSnapShot.getSnapShotClosestTo(nowDate,1)?.id == snapShots[0].id
        assertTrue PointsSnapShot.getSnapShotClosestTo(nowDate-5,1)?.id == snapShots[5].id
   		assertTrue PointsSnapShot.getSnapShotClosestTo(nowDate-14,5)?.id == snapShots[9].id
   		assertTrue PointsSnapShot.getSnapShotClosestTo(nowDate-15,5)?.id == null
        				
        snapShots*.delete()
        items*.delete()
        groups*.delete()
    }
	
	void testRetrieveSnapShotsWithinRange()
	{
		groups*.save()
        items*.save()
        
		def nowDate = new Date()   
        def snapShots = setUpSnapShotList(nowDate,groups)
        snapShots*.save()
        
        assertTrue PointsSnapShot.getSnapShotsBetween(nowDate-7,nowDate-5)?.size() == 3
        assertTrue PointsSnapShot.getSnapShotsBetween(nowDate-1,nowDate)?.size() == 2
        assertTrue PointsSnapShot.getSnapShotsBetween(nowDate+1,nowDate+4)?.size() == 0
        
        snapShots*.delete()
        items*.delete()
        groups*.delete()
	}
	
	void testSnapShotGroupsAreSavedCorrectly()
	{
		groups*.save()
		items*.save()
		
		def ps = PointsSnapShot.takeSnapShot(groups,new Date())
		ps.save()
		
		def reloaded = PointsSnapShot.get(ps.id)
		assertTrue reloaded.pointsForGroups?.size() == groups.size()
		ps.delete()
		items*.delete()
		groups*.delete()
	}
}








