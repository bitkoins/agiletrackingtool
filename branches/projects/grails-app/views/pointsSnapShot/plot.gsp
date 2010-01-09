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
   
    	<g:if test="${startTime && endTime}">
	    	<h2>Select day range for history plot:</h2>
	    	<div class="formInput">
	    	<g:form name="dateRangeForm" action="${formAction}">
	    		<table>
	    		<tr>
		    		<td>StartDate</td>
		    		<td>
		    			<g:datePicker name="startDate" value="${startTime}" precision="day"/>
		    		</td>
	    		</tr>
	    
	    		<tr>
		    		<td>EndDate</td>
		    		<td>
		    			<g:datePicker name="endDate" value="${endTime}" precision="day"/>
		    		</td>
	    		</table>
	    		<button>Submit</button>
	    	</g:form>
	    	</div>
	    	<br/>
    	</g:if>
		
		<g:each var="plotDataSet" in="${plots}">
		<table>
			<tr>
			<g:each var="plotData" in="${plotDataSet}">
				<td>
					<g:render template="/shared/plots/linePlot" model="[plotData:plotData, plotSize:[540,300] ]"/>
				</td>
			</g:each>
			</tr>
		</table>
		<br/>
		</g:each>
		
    </body>
</html>



