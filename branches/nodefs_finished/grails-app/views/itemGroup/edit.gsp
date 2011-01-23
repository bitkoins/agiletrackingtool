<html>
    <head>
		<link rel="stylesheet" href="${createLinkTo(dir:'css',file:'main.css')}" />
		<meta name="layout" content="main" />
		<nav:resources override="false"/>	
    </head>

    <body>
    	<g:javascript library="prototype" />
    	<nav:renderSubItems group="tags"/>
    	
    	<h2>Category</h2>
    	<br/>
        <g:form name="ItemGroupEditForm" action="save">
        	<table>
        	<tr>
        		<g:hiddenField name="id" value="${group.id}"/>
        		<td width="100">Name:</td>
        		<td><g:textField name="name" value="${group.name}"/></td>
        	</tr>
        	</table>
        	<button>Save</button>
        </g:form>
        
    	
    </body>
</html>
