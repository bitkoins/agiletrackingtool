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



