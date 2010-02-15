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
    	assertEquals nr , items.size()
    }
    
    void testGetUnfinishedItemsGroupMapWhenAllItemsAreFinished()
    {
    	items.each{ it.status = ItemStatus.Finished; }
    	def itemsGroupMap = itemService.getUnfinishedItemsGroupMap(project)
    	itemsGroupMap.each{group,items-> nr += items.size() }    	   	
    	assertEquals nr , 0
    }
    
    void testGetUnfinishedItemsGroupMapForOnePriority()
    {
    	def prios = [Priority.High]
    	def itemsGroupMap = itemService.getUnfinishedItemsGroupMap(project,prios)
    	itemsGroupMap.each{group,items-> nr += items.size() }    	   	
    	assertEquals nr , items.findAll{ it.priority == prios[0] }.size() 
    }
    
    void testGetUnfinishedItemsGroupMapForMorePriorities()
    {
    	items.each{ it.priority = Priority.Low }
    	def prios = [Priority.High, Priority.Low]
    	def itemsGroupMap = itemService.getUnfinishedItemsGroupMap(project,prios)
    	itemsGroupMap.each{group,items-> nr += items.size() }    	   	
    	assertEquals nr , items.size() 
    }
    
    void testDeleteItemRemovesFromIterationAndGroup()
    {
    	def item = items[0]
    	def group = item.group
    	
    	def iteration = Defaults.getIterations(1,project)[0]
    	mockDomain(Iteration, [iteration])
    	
    	iteration.addItem(item)
    	itemService.deleteItem(item)
    	
    	assertNull Iteration.get(item.id)
    	assertFalse group.hasItem(item.id)
    	assertFalse iteration.hasItem(item.id)
    	
    }
}
