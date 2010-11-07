package org.agiletracking
import grails.test.*
import org.codehaus.groovy.grails.commons.*

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
        
        assertEquals projectIsCorrect, projectService.executeWhenProjectIsCorrect(project, objectToCheck, { called = true; } )
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
    
    void testDontExecuteWhenOneIsNull()
    {
    	def called = []
    	def project = Defaults.getProjects(1)[0]
    	assertTrue !projectService.executeWhenProjectIsCorrect(null, null, { called << true } )
    	assertTrue !projectService.executeWhenProjectIsCorrect(project, null, { called << true } )
    	assertEquals 0, called.size()
    }
    
    void testExportProjectToXmlString()
    {
        def projectControl = mockFor(Project)
        def groupControl = mockFor(ItemGroup)
        def itemControl = mockFor(Item)
        def iterationControl = mockFor(Iteration)
        def snapshotControl = mockFor(PointsSnapShot)
        
		  def project = new Project()
        def (groups,items,iterations,snapshots) = (1..4).collect{ new Object() }
        groupControl.demand.static.findAllByProject(1..1) { _project -> return groups }
        itemControl.demand.static.findAllByProject(1..1) { _project -> return items }
        iterationControl.demand.static.findAllByProject(1..1) { _project -> return iterations }
        snapshotControl.demand.static.findAllByProject(1..1) { _project -> return snapshots }
        
        def utilXmlControl = mockFor(UtilXml)
        def anyString = "Now is here"
        utilXmlControl.demand.static.exportToXmlString(1..1) {  
            _project, _groups, _items, _iterations, _pointsSnapShots, _exportDate, _docVersion ->
                assertSame project, _project
                assertSame items, _items
                assertSame groups, _groups
                assertSame iterations, _iterations
                assertSame snapshots, _pointsSnapShots
                   return anyString
        }
        
        assertEquals anyString, projectService.exportToXmlString(project)
        
        def mockControllers = [projectControl,groupControl,itemControl,iterationControl,
	                            snapshotControl, utilXmlControl]
        mockControllers*.verify()
    }
    
    void testDeleteWholeProject()
    {
        def project = Defaults.getProjects(1)[0]
        mockDomain(Project, [project])
        
        def now = new Date()
        mockDomain(PointsSnapShot, Defaults.getSnapShots([], now-10, now, project) )
        
        def group = new Object()
        def groupControl = mockFor(ItemGroup)
        groupControl.demand.static.findAllByProject(1..1) { _project -> return [ group ] }        
        def itemGroupServiceControl = mockFor(ItemGroupService)
        itemGroupServiceControl.demand.static.deleteWholeGroup(1..1) { _group -> assertSame group, _group}
        
        def iteration = new Object()
        def iterationControl = mockFor(Iteration)
        iterationControl.demand.static.findAllByProject(1..1) { _iteration-> return [iteration] }
        def iterationServiceControl = mockFor(IterationService)
        iterationServiceControl.demand.static.unloadItemsAndDelete(1..1) { _iteration -> assertSame iteration, _iteration }
        
        projectService.iterationService = iterationServiceControl.createMock()
        projectService.itemGroupService = itemGroupServiceControl.createMock()
        projectService.delete(project)
        
        [iterationServiceControl, iterationServiceControl]*.verify()
        assertEquals 0, Project.count()
    }

    void testAddGroupToNewProject()
    {
        def project = Defaults.getProjects(1)[0]
        mockDomain(Project, [project])
        mockDomain(ItemGroup, [] )
        projectService.addGroupToNewProject(project)
        assertEquals 1, ItemGroup.count()
        assertEquals project, ItemGroup.list()[0].project	
    }

}
