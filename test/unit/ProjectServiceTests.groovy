import grails.test.*

class ProjectServiceTests extends GrailsUnitTestCase {
    def projectService

    protected void setUp() {
        super.setUp()
        projectService = new ProjectService()
    }

    protected void tearDown() {
        super.tearDown()
    }
    
    void performTestExecutionWithProjectCheck(def projectIsCorrect)
    {
        def called = false
        def project, otherProject 
        (project, otherProject) = Defaults.getProjects(2)
        
        def objectToCheck = new Expando(project: projectIsCorrect ? project : otherProject)
        
        assertEquals !projectIsCorrect, projectService.executeWhenProjectIsCorrect(project, objectToCheck, { called = true; } )
        assertEquals projectIsCorrect, called
    }
    
    void testExecuteWhenProjectIsCorrect()
    {
        performTestExecutionWithProjectCheck(true)
    }

    void testDontExecuteWhenProjectIsNotCorrect()
    {
        performTestExecutionWithProjectCheck(false)
    }
    
    void testExportProjectToXmlString()
    {
    	def project = Defaults.getProjects(1)[0]
    	mockDomain(Project, [project] )
    	
		def groups = Defaults.getGroups(Util.random(3..8), project)
		mockDomain(ItemGroup, groups )
		
		def items = Defaults.getItems(Util.random(5..10), groups, project, 10 )
		mockDomain(Item, items )
		
		def iterations = Defaults.getIterations(Util.random(2..5), project)
		mockDomain(Iteration, iterations )
		
		def now = new Date()
		def snapShots = Defaults.getSnapShots(groups, now-10, now-5, project)
		mockDomain(PointsSnapShot, snapShots )
    
    	def control = mockFor(UtilXml)
    	def anyString = "Now is here"
    	control.demand.static.exportToXmlString(1..1) { 
    	    _project, _groups, _items, _iterations, _pointsSnapShots, _exportDate, _docVersion ->
    	    	assertEquals project, _project
    	    	assertEquals items.size(), _items.size()
    	    	assertEquals groups.size(), _groups.size()
    	    	assertEquals iterations.size(), _iterations.size()
    	    	assertEquals snapShots.size(), _pointsSnapShots.size()
    	   		return anyString 
    	}
    	
    	assertEquals anyString, projectService.exportToXmlString(project)
    	control.verify()
    }
   
}