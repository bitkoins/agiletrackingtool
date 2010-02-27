import grails.test.*

class PointsSnapShotServiceTests extends GrailsUnitTestCase {
    def snapShotService, group, groups, snapShots

    protected void setUp() {
        super.setUp()
        snapShotService = new PointsSnapShotService()
        
        def now = new Date()
    	def projects = Defaults.getProjects(2)    	
    	mockDomain(Project, projects)
    	def project = projects[0]
    	    	
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
}
        