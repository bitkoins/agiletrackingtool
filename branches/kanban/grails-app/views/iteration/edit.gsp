<html>
    <head>
		<meta name="layout" content="main" />
		<nav:resources override="false"/>	
    </head>

    <body>
	<div>
    	<g:javascript library="prototype" />
    	<nav:renderSubItems group="tags"/>
    	
        <h2>Iteration</h2>
        <br/>
        
        <g:form name="IterationEditForm" action="save">
        	<g:hiddenField name="id" value="${iteration.id}"/>
        	<table style="width:50%">
        	<tr>
        		<td width="150">Working Title:</td>
        		<td><g:textField size="64" name="workingTitle" value="${iteration.workingTitle}"/></td>
        	</tr>
        	<tr>
        		<td>Start Date:</td>
        		<td><g:datePicker name="startTime" value="${iteration.startTime}" precision="day" years="${org.agiletracking.Util.getDefaultYearRange()}"/></td>
		</tr>
		<tr>
        		<td>Duration in days:</td>
	        	<td><g:textField size="5" name="duration" value="${org.agiletracking.Util.getDaysInBetween(iteration.startTime,iteration.endTime)}"/></td>
		</tr>        		
		<tr>
			<td>Status:</td>
			<td><g:select name="status" from="${org.agiletracking.IterationStatus}" value="${iteration.status}"/></td>
		</tr>
        	</table>
        	<button>Save</button>
        </g:form>
    	</div>
    </body>
</html>
