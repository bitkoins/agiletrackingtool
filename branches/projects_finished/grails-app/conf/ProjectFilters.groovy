class ProjectFilters {
	def cookieService
	def projectIdCookieString = "projectId"
	
    def filters = {
        
        setProjectCookie(controller:"project", action:"select") {
        	before = {
        		session.project = Project.get(params.id)
        		def cookie = new javax.servlet.http.Cookie(projectIdCookieString, "${session.project?.id}")
        		cookie.path = "/" 
        		cookie.maxAge = 60*60*24*365  
        		response.addCookie( cookie) 
        	}
        }
        
        projectCheck(controller:"(project|admin)", action:"*",invert:true) {
            
            before = {
            	if(controllerName && !session.project) {
            		def projectId = cookieService.get(projectIdCookieString)
            		def project = projectId ? Project.get(projectId) : null
            		
            		if ( project) {
            			session.project = project
            		}          		
            		else {
            			redirect(controller:'project',action:'list ')
            			return false
            		}	
            	}
            }
        }
    }
}
