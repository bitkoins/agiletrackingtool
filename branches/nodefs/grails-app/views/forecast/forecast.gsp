<html>
    <head>
	<link rel="stylesheet" href="${createLinkTo(dir:'css',file:'main.css')}" />
	<meta name="layout" content="main" />
	<nav:resources override="false"/>	
    </head>
    <body>
    	<g:javascript library="prototype" />
    	<nav:renderSubItems group="tags" />
	<div>
    	    <h2>Forecast overview: <g:formatDate format="dd-MMM yyyy" date="${new Date()}"/></h2>
		<br/>
		<p>Tip: look at your measured <g:link controller="pointsSnapShot" action="plot">velocity history</g:link> for a good estimate of your velocity ranges.</p>
		<br/>
		<div class="formInput">
			<g:formRemote name="planningInput" url="[action:'calculatePlan']" update="planningResult">
				<table>
					<tr>
						<td>Velocity:</td>
						<td>
							Min:<input size="6" name="pointsPerWeekMin" type="text" value="8"/>
						</td>
						<td>
							Max:<input size ="6" name="pointsPerWeekMax" type="text" value="10">[PointsPerWeek]</input>
						</td>
					</tr>
					
					<tr>
						<td>Uncertainty remaining points:</td>
						<td>
							<input size="6" name="pointsUncertaintyPercentage" type="text" value="10">[%]</input>
						</td>							
					</tr>
					
					<tr>
						<td>Holidays:</td>
						<td>
							<input size="6" name="weeksHolidays" type="text" value="2">[weeks]</input>
						</td>							
					</tr>
					
				</table>
				<button>Calculate</button>
			</g:formRemote>
		</div>
		<br/>
		<div id="planningResult" class="planningResult">
		</div>
	</div>	
    </body>
</html>



