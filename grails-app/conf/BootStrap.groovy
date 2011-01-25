/*----------------------------------------------------------------------------
Project: Agile Tracking Tool

Copyright 2008, 2009   Ben Schreur
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
import org.codehaus.groovy.grails.commons.*

class BootStrap {
	
     def authenticateService

     def init = { servletContext ->
     		DataSourceUtils.tuneDataSource(servletContext) 
		   def md5pass = authenticateService.passwordEncoder(ConfigurationHolder.config.agile.demo.password)
	  		def userAgile = new User(username:ConfigurationHolder.config.agile.demo.username,
                              userRealName:"Mister Demo", passwd:md5pass, 
                              enabled:true,email:"agiletracking@gmail.com",
                              emailShow:true,description:"None",
			                     agreeToTermsOfUse:true)

	  		def userRole = new Role(description:"userRole", authority:"ROLE_USER")
     		userRole.addToPeople(userAgile)
 	  		userRole.save()
     }
     
     def destroy = {
    		 
     }
} 
