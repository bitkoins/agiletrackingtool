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
    	
    	<g:render template="/shared/plots/linePlot" model="[plotData:plotData, plotSize:[540,240] ]"/>
	
		<div class="iteration">
		<h1>Current iteration:</h1>
		<table border="1">
		<tr>
			<td/>
			<td width="60">Finished Points</td>
			<td width="100">Closing date</td>
			<td width="50"></td>
			<td width="50"></td>
			<td width="50"></td>
		</tr>
		
		<g:each var="iter" in="${iterations.findAll{it.status == org.agiletracking.IterationStatus.Ongoing} }">
			<tr class="status${iter.status}">
				<td><g:link controller="currentWork" action="show" id="${iter.id}">${iter.workingTitle}</g:link></td>
				<td>${iter.calculateFinishedPoints()}</td>
				<td><g:formatDate format="dd-MMM yyyy" date="${iter.endTime}"/></td>
				<td><g:link action="edit" id="${iter.id}">Edit</g:link></td>
				<td><g:link action="delete" id="${iter.id}" onclick="return confirm('This will delete the iteration. Sure?');" >Delete</g:link></td>
				<td width="150">
					<g:if test="${iter.status != org.agiletracking.IterationStatus.Finished}">
						<g:link controller="iterationComposer" action="compose" id="${iter.id}">Compose Items</g:link>
					</g:if>
				</td>
			</tr>
		</g:each>
		</table>
		</div>
		
		<div class="iteration">
		<h1>Future iterations:</h1>
		<table border="1">
		<tr>
			<td/>
			<td width="60">Remaining points</td>
			<td width="100">Closing date</td>
			<td width="50"></td>
			<td width="50"></td>
			<td width="50"></td>
		</tr>
		
		<g:each var="iter" in="${iterations.findAll{it.status == org.agiletracking.IterationStatus.FutureWork} }">
			<tr class="status${iter.status}">
				<td><g:link controller="currentWork" action="show" id="${iter.id}">${iter.workingTitle}</g:link></td>
				<td>${iter.calculateTotalPoints()}</td>
				<td><g:formatDate format="dd-MMM yyyy" date="${iter.endTime}"/></td>
				<td><g:link action="edit" id="${iter.id}">Edit</g:link></td>
				<td><g:link action="delete" id="${iter.id}" onclick="return confirm('This will delete the iteration. Sure?');" >Delete</g:link></td>
				<td width="150"><g:link controller="iterationComposer" action="compose" id="${iter.id}">Compose Items</g:link></td>
			</tr>
		</g:each>
		</table>
		</div>
		
		
		<div class="iteration">
		<h1>Finished iterations:</h1>
		<table border="1">
		<tr>
			<td/>
			
			<td width="60">Finished points</td>
			<td width="100">Closing date</td>
			<td width="70">Duration [days]</td>
			<td width="70">Points/Week</td>
			<td width="50"></td>
			<td width="50"></td>
		</tr>
		
		<g:each var="iter" in="${iterations.findAll{it.status == org.agiletracking.IterationStatus.Finished} }">
			<tr class="status${iter.status}">
				<td><g:link controller="currentWork" action="show" id="${iter.id}">${iter.workingTitle}</g:link></td>
				<td>${iter.calculateFinishedPoints()}</td>
				<td><g:formatDate format="dd-MMM yyyy" date="${iter.endTime}"/></td>
				<td>${iter.calculateDurationInDays()}</td>
				<td>${String.format("%5.3g",iter.calculatePointsPerDay()*7)}</td>
				<td><g:link action="edit" id="${iter.id}">Edit</g:link></td>
				<td><g:link action="delete" id="${iter.id}" onclick="return confirm('This will delete the iteration. Sure?');" >Delete</g:link></td>
			</tr>
		</g:each>
		</table>
		</div>
			
			
	</body>
</html>
