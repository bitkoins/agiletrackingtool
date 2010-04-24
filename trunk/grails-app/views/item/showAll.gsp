<html>
    <head>
        <title>Work items overview</title>
		<link rel="stylesheet" href="${createLinkTo(dir:'css',file:'main.css')}" />
		<meta name="layout" content="main" />
     	<nav:resources override="false"/>	
    </head>
    
    <body>
    	<g:javascript library="prototype" />
    	<nav:renderSubItems group="tags" />
    	
    		<h2>Work items overview:</h2>
			<br/><br/>
			
		<g:each var="group" in="${groups}">
			<div id="groupItems${group.id}" class="itemGroup">
			<h1 class="groupName">${group}</h1>
			<g:each var="item" in="${group.items.collect{it}.sort{it.uid}.reverse()}">
				<div class="item">
				<table>
				<tr>
					<td width="15">${item.uid}</td>
					<td>${item.description}</td>
					<td width="20">${item.getPoints()}</td>
					<td width="50">${item.status}</td>
				</tr>
				</table>
				</div>
	                        <div class="itemSeperator"></div>
			</g:each>
			</div>
		</g:each>	
    </body>
</html>
