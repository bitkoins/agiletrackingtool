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
			
			<br/><br/>
		
			<div id="planningResult" class="planningResult">
			</div>
			
    </body>
</html>



