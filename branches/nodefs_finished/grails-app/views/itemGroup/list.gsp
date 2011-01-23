<html>
    <head>
	<link rel="stylesheet" href="${createLinkTo(dir:'css',file:'main.css')}" />
	<meta name="layout" content="main" />
	<nav:resources override="false"/>	
    </head>

    <body>
    	<g:javascript library="prototype" />
    	<nav:renderSubItems group="tags"/>
        
	<div class="itemGroup" style="width:70%;padding:10px">
		<g:each var="group" in="${groups}">
			<div class="item">
			<table>
			<tr>
				<td swidth="400">${group.name}</td>
				<td width="80"><g:link action="edit" id="${group.id}" >Edit</g:link></td>
				<td width="80"><g:link action="delete" id="${group.id}" onclick="return confirm('This will delete all items and history of this group. Sure?');" >Delete</g:link></td>
			</tr>
			</table>
			</div>
	      <div class="itemSeperator"></div>
		</g:each>

	</div>
    </body>
</html>
