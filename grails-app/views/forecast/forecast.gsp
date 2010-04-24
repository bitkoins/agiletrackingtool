<html>
    <head>
        <title>Planning estimate</title>
		<link rel="stylesheet" href="${createLinkTo(dir:'css',file:'main.css')}" />
		<meta name="layout" content="main" />
		<nav:resources override="false"/>	
    </head>
    <body>
    	<g:javascript library="prototype" />
    	<nav:renderSubItems group="tags" />
    	    <h2>Forecast overview: <td><g:formatDate format="dd-MMM yyyy" date="${new Date()}"/></h2>
			<br/>
			<div class="formInput">
				<g:formRemote name="planningInput" url="[action:'calculatePlan']" update="planningResult">
					<table>
						<tr>
							<td>Velocity:</td>
							<td>
								Min:<input size="6" name="pointsPerWeekMin" type="text" value="1.2"/>
							</td>
							<td>
								Max:<input size ="6" name="pointsPerWeekMax" type="text" value="1.6">&nbsp[PointsPerWeek]</input>
							</td>
						</tr>
						
						<tr>
							<td>Uncertainty remaining points:</td>
							<td>
								<input size="6" name="pointsUncertaintyPercentage" type="text" value="15">&nbsp[%]</input>
							</td>							
						</tr>
						
						<tr>
							<td>Holidays:</td>
							<td>
								<input size="6" name="weeksHolidays" type="text" value="4">&nbsp[weeks]</input>
							</td>							
						</tr>
						
					</table>
					<button>Calculate</button>
				</g:formRemote>
			</div>
			<br/>
			<div id="planningResult" class="planningResult">
			</div>
			
    </body>
</html>



