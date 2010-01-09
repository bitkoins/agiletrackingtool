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
		
		<style type="text/css">
    		tr.on {
    			 color: red;
    			 background-color: lightgrey;
    		} 
    		tr.off {    			 
    		}
    		
    		.groupName {
    			
    		}
    	</style>
    	<nav:resources override="false"/>	
    </head>

    <body>
    	<g:javascript library="prototype" />
    	<nav:renderSubItems group="tags" />
    	
    	<g:render template="/shared/plots/linePlot" model="[plotData:plotData, plotSize:[540,240] "/>
	
		<br/>
		<h1>Iterations:</h1>
		<br/>
		
		<div class="iteration">
		<h1>Current iteration:</h1>
		<table border="1">
		<tr>
			<td width="20">Id</td>
			<td>Release Title</td>
			<td width="60">Number of items</td>
			<td width="20">Finished points</td>
			<td width="20">Total points</td>
			<td width="100">Closing date</td>
			<td width="50"></td>
		</tr>
		
		<g:each var="iter" in="${iterations.findAll{it.status == IterationStatus.Ongoing} }">
			<tr class="status${iter.status}">
				<td width="20"><g:link action="show" id="${iter.id}">${iter.id}</g:link></td>
				<td><g:link action="edit" id="${iter.id}">${iter.workingTitle}</g:link></td>
				<td width="60">${iter.items?.size()}</td>
				<td width="20">${iter.finishedPoints}</td>
				<td width="20">${iter.totalPoints()}</td>
				<td width="100"><g:formatDate format="dd-MMM yyyy" date="${iter.endTime}"/></td>
				<td width="150">
					<g:if test="${iter.status != IterationStatus.Finished}">
						<g:link controller="iterationComposer" action="compose" id="${iter.id}">Select Items</g:link>
					</g:if>
				</td>
			</tr>
		</g:each>
		</table>
		</div>
		
		<div class="iteration">
		<h1>Future Releases:</h1>
		<table border="1">
		<tr>
			<td width="20">Id</td>
			<td>Release Title</td>
			<td width="60">Remaining number of items</td>
			<td width="20">Remaining points</td>
			<td width="100">Closing date</td>
			<td width="50"></td>
		</tr>
		
		<g:each var="iter" in="${iterations.findAll{it.status == IterationStatus.FutureWork} }">
			<tr class="status${iter.status}">
				<td width="20"><g:link action="show" id="${iter.id}">${iter.id}</g:link></td>
				<td><g:link action="edit" id="${iter.id}">${iter.workingTitle}</g:link></td>
				<td width="60">${iter.items?.size()}</td>
				<td width="20">${iter.totalPoints()}</td>
				<td width="100"><g:formatDate format="dd-MMM yyyy" date="${iter.endTime}"/></td>
				<td width="150"><g:link controller="iterationComposer" action="compose" id="${iter.id}">Select Items</g:link></td>
			</tr>
		</g:each>
		</table>
		</div>
		
		
		<div class="iteration">
		<h1>Finished iterations:</h1>
		<table border="1">
		<tr>
			<td width="20">Id</td>
			<td>Iteration Title</td>
			<td width="60">Number of items</td>
			<td width="20">Points/Week</td>
			<td width="20">Finished points</td>
			<td width="50">Duration[days]</td>
			<td width="100">Closing date</td>
			<td with="50"/>
		</tr>
		
		<g:each var="iter" in="${iterations.findAll{it.status == IterationStatus.Finished} }">
			<tr class="status${iter.status}">
				<td width="20"><g:link action="show" id="${iter.id}">${iter.id}</g:link></td>
				<td><g:link action="edit" id="${iter.id}">${iter.workingTitle}</g:link></td>
				<td width="60">${iter.items?.size()}</td>
				<td width="20">${String.format("%5.3g",iter.pointsPerDay*7)}</td>
				<td width="20">${iter.finishedPoints}</td>
				<td width="50">${iter.durationInDays}</td>
				<td width="100"><g:formatDate format="dd-MMM yyyy" date="${iter.endTime}"/></td>
				<td><g:link action="showCurrent" id="${iter.id}">Show</g:link></td>
			</tr>
		</g:each>
		</table>
		</div>
			
			
	</body>
</html>
