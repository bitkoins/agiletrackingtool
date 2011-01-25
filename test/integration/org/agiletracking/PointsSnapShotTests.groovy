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

class PointsSnapShotTests extends GroovyTestCase {
	def project
	def groups 
	def items
	def snapShot
	def snapShots
	def defaultItemPoint = 2
	def date 
		
	void setUp() {
		project = Defaults.getProjects(1)[0]
		groups = Defaults.getGroups(2,project)
		items = Defaults.getItems(3,groups,project)
		items.each{ it.points = defaultItemPoint }
		this.date = new Date()
		takeSnapShot()
		snapShots = []
	}	
	
	void tearDown() {
	}
	
	void saveInstances()
	{
		project.save()
		groups*.save()
		items*.save()
	}
	
	void deleteInstances()
	{
		snapShots*.delete()
		snapShot.delete()
		items*.delete()
		groups*.delete()
		project.delete()
	}
	
	void takeSnapShot()
	{
		saveInstances()
		snapShot = PointsSnapShot.takeSnapShot(project, this.date)
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
		saveInstances()
		
		if ( !snapShot.validate() )
			snapShot.errors.allErrors.each { println it }
    	
		assertNotNull snapShot.save()
		
		def snapShotSaved = PointsSnapShot.get(snapShot.id)
		assertTrue snapShotSaved.pointsForGroups.size() == snapShot.pointsForGroups.size()
		
		deleteInstances()		
	}
	
	static List setUpSnapShotList(def project, def nowDate, def groups)
	{
        def snapShots = []
        10.times{
            def ps, pointsForGroupList
    		ps = PointsSnapShot.takeSnapShot(project,nowDate - it)
    		snapShots << ps
        }
        
        return snapShots
	}
	
	void testRetrievingSnapShotClosestToDate()
    {
        saveInstances()
        
     	def nowDate = new Date()   
        def snapShots = setUpSnapShotList(project,nowDate,groups)
        snapShots*.save()
            
        assertTrue PointsSnapShot.getSnapShotClosestTo(project,nowDate,1)?.id == snapShots[0].id
        assertTrue PointsSnapShot.getSnapShotClosestTo(project,nowDate-5,1)?.id == snapShots[5].id
   		assertTrue PointsSnapShot.getSnapShotClosestTo(project,nowDate-14,5)?.id == snapShots[9].id
   		assertTrue PointsSnapShot.getSnapShotClosestTo(project,nowDate-15,5)?.id == null
        						
        deleteInstances()
    }
	
	void testRetrieveSnapShotsWithinRange()
	{
		saveInstances()
        
		def nowDate = new Date()   
        def snapShots = setUpSnapShotList(project,nowDate,groups)
        snapShots*.save()
        
        assertTrue PointsSnapShot.getSnapShotsBetween(project,nowDate-7,nowDate-5)?.size() == 3
        assertTrue PointsSnapShot.getSnapShotsBetween(project,nowDate-1,nowDate)?.size() == 2
        assertTrue PointsSnapShot.getSnapShotsBetween(project,nowDate+1,nowDate+4)?.size() == 0
        
        deleteInstances()
	}
	
	
	void testRetrieveSnapShotsWithinRangeFiltersOutCorrect()
	{
		saveInstances()
        
		def nowDate = new Date()   
        def snapShots = setUpSnapShotList(project,nowDate,groups)
        snapShots*.save()
        
        def otherProject = Defaults.getProjects(1)[0]
        otherProject.id = 123
        
        assertTrue PointsSnapShot.getSnapShotsBetween(otherProject,nowDate-10,nowDate)?.size() == 0
                
        deleteInstances()
	}
	
	void testSnapShotGroupsAreSavedCorrectly()
	{
		saveInstances()
				
		takeSnapShot()
		snapShot.save()
		
		def reloaded = PointsSnapShot.get(snapShot.id)
		assertTrue reloaded.pointsForGroups?.size() == groups.size()
		
		deleteInstances()
	}
	

	void testDeleteWholeGroup()
	{
		def nrSnapShots = 2
		def snapShots = Defaults.getSnapShots(groups, date -(nrSnapShots-1), date, project)
		assertTrue snapShots.size() == nrSnapShots
		def deletedGroup = groups[0]
		
		saveInstances()
		snapShots*.save()
		
		PointsForGroup.deleteWholeGroup(deletedGroup)
			
		assertTrue PointsForGroup.findAllByGroup(deletedGroup).size() == 0
		snapShots.each{ assertTrue it.pointsForGroups.size() == (groups.size() -1) } 			
		
		deleteInstances()
	}
}








