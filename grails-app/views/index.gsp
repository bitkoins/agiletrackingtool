<!----------------------------------------------------------------------------
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
------------------------------------------------------------------------------>

<html>
    <head>
        <title>Welcome to Agile Planner</title>
		<meta name="layout" content="main" />
    </head>
    <body>
    	<div class="navTopBar">
-   	</div>

		<div class="dialog" style="margin-left:20px;width:60%;">
          <h1>What would you like to do?</h1>
	        <ul>
		        <li class="controller"><g:link controller="iteration" action="list">Manage the Iterations</g:link></li>
		        <br/>
				<li><g:link style=";" controller="item" action="backlog">Work on the Backlog</g:link></li>						        
				<br/>
		        <li><g:link controller="pointsSnapShot" action="plot">Look at the History</g:link></li>
		        <li><g:link style=";" controller="forecast" action="forecast">Look at the Planning</g:link></li>
		        <li><g:link style=";" controller="iterationCurrent" action="show">See the Current Iteration</g:link></li>
	        </ul>
	    </div>
	    
	    <br/>
        <br/>
        <br/>
        <br/>        
        <br/>
        <br/>
        <br/>
        <br/>
        
        <div class="dialog" style="margin-left:20px;width:60%;">
        	<h1>Others:</h1>
            <ul>
                <li class="controller"><g:link controller="admin">Administration</g:link></li>
            </ul>
        </div>
                        
    </body>
</html>
