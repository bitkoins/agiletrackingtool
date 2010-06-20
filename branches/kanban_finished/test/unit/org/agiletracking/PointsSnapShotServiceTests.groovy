package org.agiletracking
import grails.test.*

class PointsSnapShotServiceTests extends GrailsUnitTestCase {
    def snapShotService, group, groups, snapShots, project

    protected void setUp() {
        super.setUp()
        snapShotService = new PointsSnapShotService()
        
        def now = new Date()
    	def projects = Defaults.getProjects(2)    	
    	mockDomain(Project, projects)
    	project = projects[0]
    	    	
    	groups = Defaults.getGroups(3,project)
    	mockDomain(ItemGroup, groups)
    	group = groups[0]
    	
    	snapShots = Defaults.getSnapShots(groups, now-3, now, project)
    	mockDomain(PointsSnapShot, snapShots)
    	
    	def pointsForGroups = [] 
    	snapShots.each{ snapShot -> pointsForGroups += snapShot.pointsForGroups.collect{it } }
    	mockDomain(PointsForGroup, pointsForGroups )
    }

    protected void tearDown() {
        super.tearDown()
    }
    
    void testDeleteWholeGroupRemovesGroupFromSnapShots()
    {
    	snapShotService.deleteWholeGroup(group)
    	
    	assertEquals snapShots.size(), PointsSnapShot.count()
    	snapShots.each { snapShot ->  assertEquals groups.size()-1, snapShot.pointsForGroups.size() }
    }
    
    void testDeleteWholeGroupRemovesPointsForGroups()
    {
    	snapShotService.deleteWholeGroup(group)
    	
    	def findNumberPointsForGroups = { PointsForGroup.findAllByGroup(it).size() }
    	assertEquals 0, findNumberPointsForGroups(group)
    	(groups-group).each{ otherGroup -> assertTrue findNumberPointsForGroups != 0 }
    }

    void testPerformSnapJobWhenNoChangeMadeToItems()
    {
    	def itemControl = mockFor(Item)
    	def psControl = mockFor(PointsSnapShot)
    	def now = new Date()
    	
    	itemControl.demand.static.lastUpdateDateForProject(1..1) { _project -> assertEquals project, _project; return now - 2 }
    	psControl.demand.takeSnapShot(0..0) { _project, _date -> }
    	    	
    	snapShotService.performSnapShotJob(project, now)
    }
    
    void testPerformSnapJobWhenChangeMadeToItems()
    {
    	def now = new Date()
    	def itemControl = mockFor(Item)
    	itemControl.demand.static.lastUpdateDateForProject(1..1) { _project -> assertEquals project, _project; return now - 1 }

    	/* Note: mixing up mockDomain and mockFor does not seem to support save. Workaround by using Expando: */
    	def newSnapShot = new Expando()
    	def psControl = mockFor(PointsSnapShot)
    	psControl.demand.static.takeSnapShot(1..1) { _project, _date ->
    			assertEquals _project, project
    			assertEquals _date, now
    			return newSnapShot 
    	}
    	
    	def saved = false
    	newSnapShot.save = { saved = true }
    	
    	snapShotService.performSnapShotJob(project, now)
    	assertTrue saved
    }
}
        
