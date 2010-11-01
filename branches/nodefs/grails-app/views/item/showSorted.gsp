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
