import grails.test.*

class ItemGroupServiceTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

     void testDeleteWholeGroup()
    {
    	def project = Defaults.getProjects(1)[0]
    	mockDomain(Project, [project] )
    	
		def groups = Defaults.getGroups(2,[project])
		mockDomain(ItemGroup, groups)
    	def (groupA, groupB) = groups
    	
    	def itemsA = Defaults.getItems(2,[groupA],project,1)
    	def itemsB = Defaults.getItems(3,[groupB],project,10)
    	def items = (itemsA + itemsB)
    	mockDomain(Item, items)
    	
    	def iteration = Defaults.getIterations(1,project)[0]
    	mockDomain(Iteration, [iteration] )
    	
    	items.each{ iteration.addItem(it) }
    	
    	new ItemGroupService().deleteWholeGroup(groupA)
    	
    	assertEquals 1, ItemGroup.count()
    	assertEquals itemsB.size(), Item.count()
    	assertEquals itemsB.size(), iteration.items.size()
    }
}
