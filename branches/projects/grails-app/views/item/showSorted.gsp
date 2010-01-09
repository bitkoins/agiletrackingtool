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
        <title>Unfinished items sorted by points:</title>
		<link rel="stylesheet" href="${createLinkTo(dir:'css',file:'main.css')}" />
		<meta name="layout" content="main" />
		<nav:resources override="false"/>	
    </head>

    <body>
    	<g:javascript library="prototype" />
    	<nav:renderSubItems group="tags" />
    	
    	<h2>Unfinished items sorted by points:</h2>
    	<br/>
    	Current date: <g:formatDate format="dd-MMM yyyy" date="${new Date()}"/>
    	<br/>
    	
    	<p>Number of items: ${items.size()}</p>
    	<p>Average/point: ${average}</p>
    	<p>Total points: ${sum}</p>
    	<br/>        	

		<div class="itemGroup">
		<table border="1">
		<tr>
			<td width="15">Item id.</td>
			<td>Description</td>
			<td width="100">Group</td>
			<td width="150">Iteration</td>
			<td width="50">Status</td>
			<td width="50">Points</td>
		</tr>
					
		<g:each var="item" in="${items}">
			<div class="item">
			<tr>
				<td width="15"><g:link action="edit" id="${item.id}">${item.uid}</g:link></td>
				<td>${item.description}</td>
				<td>${item.group}</td>
				<td>${item.iteration}</td>
				<td>${item.status}</td>
				<td>${item.points}</td>
			</tr>
			</div>
		</g:each>
		</table>
		</div>

    </body>
</html>
