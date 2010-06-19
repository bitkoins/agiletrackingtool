<html>
    <head>
        <title>Project listing</title>
		<link rel="stylesheet" href="${createLinkTo(dir:'css',file:'main.css')}" />
		<meta name="layout" content="main" />
    	<nav:resources override="false"/>	
    </head>

    <body>
    	<g:javascript library="prototype" />
    	<nav:renderSubItems group="tags" />

	<div class="itemGroup" style="width:50%;padding:10px">
	<g:each var="project" in="${projects}">
		<div class="item" style="margin:2px">
		<table>				
		<tr>
			<td>${project.name}</td>			
			<td width="60"><g:link action="select" id="${project.id}">Select</g:link></td>			
			<td width="40"><g:link action="edit" id="${project.id}">Edit</g:link></td>
			<td width="80"><g:link action="delete" id="${project.id}" onclick="return confirm('The whole project will be destroyed!!! Are you sure?');">Delete</g:link></td>
		</tr>
		</table>
		</div>
	        <div class="itemSeperator"></div>
	</g:each>		

	<g:if test="${projects?.size() == 0}">		
           <g:link controller="admin" action="loadDefaults">Load example project data</g:link>
	</g:if>

	</div>
	</body>
</html>
