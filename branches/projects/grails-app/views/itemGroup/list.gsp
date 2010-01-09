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
        <title>${title}</title>
		<link rel="stylesheet" href="${createLinkTo(dir:'css',file:'main.css')}" />
		<meta name="layout" content="main" />
		<nav:resources override="false"/>	
    </head>

    <body>
    	<g:javascript library="prototype" />
    	<nav:renderSubItems group="itemGroup"/>
        
    	<h2>Groups overview</h2>
		<br/>
		
		<div class="itemGroup">
		<table>
			<tr>
				<td>Id</td>
				<td>Name</td>
				<td>Number of Items</td>
				<td>Finished points</td>
				<td>Total points</td>
				<td/>
				<td/>
			</tr>
		
			<g:each var="group" in="${groups}">
				<tr>
					<td>${group.id}</td>
					<td>${group.name}</td>
					<td>${group.items?.size()}</td>
					<td>${group.finishedPoints()}</td>
					<td>${group.totalPoints()}</td>
					<td><g:link action="edit" id="${group.id}" >Edit</g:link></td>
					<td><g:link action="delete" id="${group.id}" onclick="return confirm('This will delete all items and history of this group. Sure?');" >Delete</g:link></td>
				</tr>
			</g:each>
		</table>
		</div>
    </body>
</html>
