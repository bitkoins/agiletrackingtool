/*----------------------------------------------------------------------------
Project: Agile Tracking Tool

Copyright 2008, 2009, 2010   Ben Schreur
------------------------------------------------------------------------------
This file is part of Agile Tracking Tool.

Agile Tracking Tool is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Agile Tracking Tool is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Agile Tracking Tool.  If not, see <http://www.gnu.org/licenses/>.
------------------------------------------------------------------------------*/
import org.agiletracking.*

class ProjectFilters {
	def cookieService
	def projectIdCookieString = "projectId"
	
    def filters = {

       checkIfProjectIsSelected(controller:"(project|admin|login|logout|register|captcha)", action:"*",invert:true) {
             before = {
            	if(controllerName && !session.project) {
            		redirect(controller:'project',action:'list ')
            		return false
            	}
             }
        }
 
        checkIfProjectCheckPassed(controller:"*", action:"*") {
             after = { model -> 
                 if(flash.projectCheckPassed == false) {
                 	redirect(controller:'project',action:'list ')
            	        return false
                 }
             }	 
        }
    }
}
