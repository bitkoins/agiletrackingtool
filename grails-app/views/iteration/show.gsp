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
        <title>View current iteration</title>
		<link rel="stylesheet" href="${createLinkTo(dir:'css',file:'main.css')}" />
		<meta name="layout" content="main" />
		<nav:resources override="false"/>	
    </head>
    <body>
    	<g:javascript library="prototype" />
    	<nav:renderSubItems group="tags" />
    	
    	<div class="iteration">
			<h2>Iteration: <g:link action="edit" id="${iteration.id}">${iteration.workingTitle}</g:link></h2>
			
			<g:if test="${plotData}">
			<div style="float:right">		
				<g:render template="/shared/plots/linePlot" model="[plotData:plotData, plotSize:[250,140] ]"/>
			</div>
			<div style="clear:both"></div>
			</g:if>
			
			<br/><br/>
			
			<table>
			<tr>
				<td>Total Points</td>
				<td>Finished Points</td>
				<td>Remaining Points</td>				
			<tr/>
			<tr>
				<td>${iteration.totalPoints()}</td>
				<td>${iteration.getFinishedPoints()}</td>
				<td>${iteration.totalPoints()-iteration.getFinishedPoints()}</td>
			<tr/>
			</table>
			<br/><br/>
			
			<g:each var="item" in="${items.collect{it}}">
				<div id="itemBox${item.id}" class="itemBox">
					<div id="item${item.id}">
					<g:render template="itemView" model="[item:item]"/>
					</div>
				</div>
			</g:each>
			
		</div>
		<br/>
		
		<h2><g:link action="closeCurrent">Close iteration and copy items to the next iteration</g:link></h2>
		
			
    </body>
</html>



