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
    
    void testTransferItems()
    {
		def iterCurrent, iterNext
		def iters = Defaults.getIterations(2,project)
		(iterCurrent, iterNext) = iters
		
		mockDomain(Iteration, iters)
		
		def groups = Defaults.getGroups(1,[project])
		mockDomain(ItemGroup, groups)		
		def itemsUnfinished = Defaults.getItems(3,groups, project, 10)
		def itemsFinished = Defaults.getItems(4, groups, project, 100)
		
		itemsUnfinished.each{ it.status = ItemStatus.Request }
		itemsFinished.each{ it.status = ItemStatus.Finished }
		
		def items = (itemsUnfinished + itemsFinished)
		mockDomain(Item, items)
		items.each{ iterCurrent.addItem(it) }
		
		iterationService.transferUnfinishedItems(iterCurrent, iterNext)
		
		assertEquals itemsFinished.size(), iterCurrent.items.size() 
		itemsFinished.each{ assertNotNull iterCurrent.items.find{ it } }
		
		assertEquals itemsUnfinished.size(), iterNext.items.size()
		itemsUnfinished.each{ assertNotNull iterNext.items.find{ it } }
    }
}
