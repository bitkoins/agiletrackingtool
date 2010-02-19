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
}