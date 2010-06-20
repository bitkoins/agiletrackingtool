<html>
    <head>
        <title>Current Work</title>
		<link rel="stylesheet" href="${createLinkTo(dir:'css',file:'main.css')}" />
		<meta name="layout" content="main" />
		<nav:resources override="false"/>	
    </head>
    <body>
    	<g:javascript library="prototype" />
    	<nav:renderSubItems group="tags"/>

	<g:if test="${iteration}">
 	<div class="iteration">
	<h2>${iteration.workingTitle}</h2>
	<g:if test="${iteration.status == org.agiletracking.IterationStatus.Ongoing}">
		<g:link action="closeCurrent">Close and copy unfinished items to the next iteration</g:link>
	</g:if>

	<g:if test="${plotData}">
		<div style="float:right">		
			<g:render template="/shared/plots/linePlot" model="[plotData:plotData, plotSize:[250,140] ]"/>
		</div>
		<div style="clear:both"></div>
	</g:if>
		
	<div><br/><br/></div>
		
	<table>
	<tr>
		<td>Total Points</td>
		<td>Finished Points</td>
		<td>Remaining Points</td>				
	</tr>
	<tr>
		<td>${iteration.totalPoints()}</td>
		<td>${iteration.getFinishedPoints()}</td>
		<td>${iteration.totalPoints()-iteration.getFinishedPoints()}</td>
	</tr>
	</table>
	</div>
   <g:render template="showItems" model="[items:items]"/>

	</g:if>
	<g:else>
    	<p>
      Please <g:link controller="iteration" action="create">create an iteration</g:link> or 
      <g:link controller="item" action="backlog">update your backlog</g:link>.
      </p>
	</g:else>
		
	<div><br/></div>

   </body>
</html>
