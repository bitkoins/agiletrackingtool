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
		    			<g:datePicker name="startTime" value="${startTime}" precision="day" years="${org.agiletracking.Util.getDefaultYearRange()}"/>
		    		</td>
	    		</tr>
	    
	    		<tr>
		    		<td>EndDate</td>
		    		<td>
		    			<g:datePicker name="endTime" value="${endTime}" precision="day" years="${org.agiletracking.Util.getDefaultYearRange()}"/>
		    		</td>
	    		</table>
	    		<button>Submit</button>
	    	</g:form>
	    	</div>
	    	<br/>
    	</g:if>

	<g:if test="${note}">
            <p>Note: ${note}</p>
	    <br></br>
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



