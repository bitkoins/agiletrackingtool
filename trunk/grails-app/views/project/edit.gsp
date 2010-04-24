<html>
    <head>
		<link rel="stylesheet" href="${createLinkTo(dir:'css',file:'main.css')}" />
		<meta name="layout" content="main" />
		<nav:resources override="false"/>	
    </head>

    <body>
    	<g:javascript library="prototype" />
    	<nav:renderSubItems group="tags"/>
    	
    	<h2>Project</h2>
    	<br/>
        <g:form name="ProjectEditForm" action="save">
        	<table>
        	<tr>
        		<g:hiddenField name="id" value="${project.id}"/>
        		<td width="100">Project Name:</td>
        		<td><g:textField name="name" value="${project.name}"/></td>
        	</tr>
        	</table>
        	<button>Save</button>
        </g:form>
        
    	
    </body>
</html>
