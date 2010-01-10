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
        <title>Iteration listing</title>
		<link rel="stylesheet" href="${createLinkTo(dir:'css',file:'main.css')}" />
		<meta name="layout" content="main" />
    	<nav:resources override="false"/>	
    </head>

    <body>
    	<g:javascript library="prototype" />
    	<nav:renderSubItems group="tags" />
		
		<div class="itemGroup">
		<table>				
		<g:each var="project" in="${projects}">
			<tr>
				<td width="80"><g:link action="select" id="${project.id}">${project.name}</g:link></td>			
				<td width="200">${project.email}</td>				
				<td width="80"><g:link action="edit" id="${project.id}">edit</g:link></td>
				<td width="80"><g:link action="delete" id="${project.id}">delete</g:link></td>
			</tr>
		</g:each>		
		</table>
		</div>
		
		
			
	</body>
</html>
