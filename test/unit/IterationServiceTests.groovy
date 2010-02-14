import grails.test.*

class IterationServiceTests extends GrailsUnitTestCase {
	def project
	def iterationService
	
    protected void setUp() {
    	project = Defaults.getProjects(1)[0]
    	iterationService = new IterationService()
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
    	iterationService.unloadItemsAndDelete(iteration)
    	assertNull Iteration.get(id)
    }
    
    void testGetOngoingIteration()
    {
    	def iters = Defaults.getIterations(10,project)
    	iters.each{ it.status = IterationStatus.Finished }
    	mockDomain(Iteration, iters)    	
    	def current = iters[5]
    	current.status = IterationStatus.Ongoing  	
    	def retrievedCurrent = iterationService.getOngoingIteration(project)
    	
    	assertTrue current.id == retrievedCurrent.id
    }
}
