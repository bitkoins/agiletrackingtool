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
		<link rel="stylesheet" href="${createLinkTo(dir:'css',file:'main.css')}" />
		<meta name="layout" content="main" />
		<nav:resources override="false"/>	
    </head>

    <body>
    	<g:javascript library="prototype" />
    	<nav:renderSubItems group="tags"/>
    	
        <h2>Iteration</h2>
        <br/>
        
        <g:form name="IterationEditForm" action="save">
        	<g:hiddenField name="id" value="${iteration.id}"/>
        	<table style="width:50%">
        	<tr>
        		<td width="150">Working Title:</td>
        		<td><g:textField size="64" name="workingTitle" value="${iteration.workingTitle}"/></td>
        	</tr>
        	
        	<tr>
        		<td>Start Date:</td>
        		<td><g:datePicker name="startTime" value="${iteration.startTime}" precision="day" years="${Util.getDefaultYearRange()}"/></td>
			</tr>
			<tr>
        		<td>Duration in days:</td>
        		<td><g:textField size="5" name="duration" value="${Util.getDaysInBetween(iteration.startTime,iteration.endTime)}"/></td>
			</tr>        		
			
			<tr>
				<td>Status:</td>
				<td><g:select name="status" from="${org.agiletracking.IterationStatus}" value="${iteration.status}"/></td>
			</tr>
			
        	</table>
        	<button>Save</button>
        </g:form>
    	
    </body>
</html>
