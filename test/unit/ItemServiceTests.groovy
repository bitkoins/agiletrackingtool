import grails.test.*

class ItemServiceTests extends GrailsUnitTestCase {
	def itemService
	def nr 
	
	def project 
	def projects
	def groups
	def items

    protected void setUp() {
        super.setUp()
        itemService = new ItemService()
        itemService.itemGroupService = new ItemGroupService()
        nr = 0
        
        projects = Defaults.getProjects(1)
		project = projects[0]
		groups = Defaults.getGroups(2,projects)
		mockDomain(ItemGroup, groups)
		items = Defaults.getItems(5,groups,project, 123)
		mockDomain(Item, items)
    }

    protected void tearDown() {
        super.tearDown()
    }
    
    void testGetUnfinishedItemsGroupMap()
    {
    	items.each{ it.status = ItemStatus.Blocking; }
    	def itemsGroupMap = itemService.getUnfinishedItemsGroupMap(project)
    	itemsGroupMap.each{group,itemList->
    		nr += itemList.size() 
    	}  
    	assertTrue nr == items.size()
    }
    
    void testGetUnfinishedItemsGroupMapWhenAllItemsAreFinished()
    {
    	items.each{ it.status = ItemStatus.Finished; }
    	def itemsGroupMap = itemService.getUnfinishedItemsGroupMap(project)
    	itemsGroupMap.each{group,items-> nr += items.size() }    	   	
    	assertTrue nr == 0
    }
    
    void testGetUnfinishedItemsGroupMapForOnePriority()
    {
    	def prios = [Priority.High]
    	def itemsGroupMap = itemService.getUnfinishedItemsGroupMap(project,prios)
    	itemsGroupMap.each{group,items-> nr += items.size() }    	   	
    	assertTrue nr == items.findAll{ it.priority == prios[0] }.size() 
    }
    
    void testGetUnfinishedItemsGroupMapForMorePriorities()
    {
    	items.each{ it.priority = Priority.Low }
    	def prios = [Priority.High, Priority.Low]
    	def itemsGroupMap = itemService.getUnfinishedItemsGroupMap(project,prios)
    	itemsGroupMap.each{group,items-> nr += items.size() }    	   	
    	assertTrue nr == items.size() 
    }
    
    void testRemoveItemsWithIterationBenzie()
    {
    	def nrWithIteration = 2
    
        def groupA = groups[0]
        def groupB = groups[1]
        
    	def itemsA = Defaults.getItems(nrWithIteration + 3, groups,project, 123)
    	nrWithIteration.times{ itemsA[it].iteration = new Iteration() }
    	
    	def itemsB = Defaults.getItems(4, groups,project, 123)
    	
    	def itemsByGroup = [:]
    	itemsByGroup[groupA] = itemsA
    	itemsByGroup[groupB] = itemsB
    	 
    	def itemsByGroupFiltered = itemService.removeItemsWithIteration(itemsByGroup)
    	
    	assertEquals itemsA.size() - nrWithIteration, itemsByGroupFiltered[groupA].size()
    	assertEquals itemsB.size(), itemsByGroupFiltered[groupB].size()
    }
}
