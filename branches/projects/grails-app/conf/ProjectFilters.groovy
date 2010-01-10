class ProjectFilters {

    def filters = {
        
        projectCheck(controller:"(project|admin)", action:"*",invert:true) {
            
            before = {
            	if(!session.project) { 
            		redirect(controller:'project',action:'list ')
            		return false	
            	}
            }
            
            
        }
    }
}
