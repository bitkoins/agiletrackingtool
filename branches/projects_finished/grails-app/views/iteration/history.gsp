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
        <title>Iteration History</title>
		<link rel="stylesheet" href="${createLinkTo(dir:'css',file:'main.css')}" />
		<meta name="layout" content="main" />
		
		<style type="text/css">
			.plots {
				margin-left: 100px;
			}
		</style>
		<nav:resources override="false"/>	
    </head>
    <body>
    	<g:javascript library="prototype" />
    	<nav:renderSubItems group="tags" />
    	
		<br/>
		<h2>Iteration history:</h2>
		
		<g:each var="iter" in="${iterations}">
			<div id="iteration${iter.id}" class="iteration">
			<h2>${iter.workingTitle}</h2>
			<p>Finished/Total Points: ${iter.getFinishedPoints()} / ${iter.totalPoints()} </p>
			<br/>
			<table>
			<tr>
				<td width="20">Id</td>
				<td>Description</td>
				<td width="20">Points</td>
				<td width="50">Status</td>
			<tr>
			
			<g:each var="item" in="${iter.items.collect{it}.sort{it.uid}}">
			<tr>
				<td>${item.uid}</td>
				
				<td>
				<gui:toolTip text="Done: ${item.hasCriteria() ? item.criteria : ' ?'}">
				    ${item.description}
				</gui:toolTip>
				</td>
				<td>${item.points}</td>
				<td>${item.status}</td>
			<tr>
			</g:each>
			
			</table>
			</div> 
		</g:each>
		
    </body>
</html>



