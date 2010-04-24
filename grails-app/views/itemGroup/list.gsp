<html>
    <head>
        <title>${title}</title>
		<link rel="stylesheet" href="${createLinkTo(dir:'css',file:'main.css')}" />
		<meta name="layout" content="main" />
		<nav:resources override="false"/>	
    </head>

    <body>
    	<g:javascript library="prototype" />
    	<nav:renderSubItems group="tags"/>
        
    	<h2>Categories overview</h2>
		<br/>
		
		<div class="itemGroup">
		<table>
			<tr>
				<td>Name</td>
				<td>Number of Items</td>
				<td>Finished points</td>
				<td>Total points</td>
				<td/>
				<td/>
			</tr>
		
			<g:each var="group" in="${groups}">
				<tr>
					<td>${group.name}</td>
					<td>${group.items?.size()}</td>
					<td>${group.finishedPoints()}</td>
					<td>${group.totalPoints()}</td>
					<td><g:link action="edit" id="${group.id}" >Edit</g:link></td>
					<td><g:link action="delete" id="${group.id}" onclick="return confirm('This will delete all items and history of this group. Sure?');" >Delete</g:link></td>
				</tr>
			</g:each>
		</table>
		</div>
    </body>
</html>
