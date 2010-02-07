import grails.test.*

class IterationServiceTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testDeleteIteration()
    {
    	def project = Defaults.getProjects(1)[0]
		mockDomain(Project, [project])
	
		def iteration = Defaults.getIterations(1,project)[0]
		mockDomain(Iteration, [iteration] )
    
    	def id = iteration.id
    	new IterationService().unloadItemsAndDelete(iteration)
    	assertNull Iteration.get(id)
    }
}
