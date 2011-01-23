<html>
    <head>
      <title>All items</title>
		<link rel="stylesheet" href="${createLinkTo(dir:'css',file:'main.css')}" />
		<meta name="layout" content="main" />
     	<nav:resources override="false"/>	
    </head>
    
    <body>
    	<g:javascript library="prototype" />
    	<nav:renderSubItems group="tags" />
    	
      <div><br/><br/></div>
			
		<g:each var="keyValue" in="${itemsByGroup}">
			<g:set var="group" value="${keyValue.key}"/>
			<g:set var="items" value="${keyValue.value}"/>
			<div id="groupItems${group.id}" class="itemGroup">
			<h1 class="groupName">${group}</h1>
			<g:each var="item" in="${items.collect{it}.sort{it.uid}.reverse()}">
				<div class="item">
				<table>
				<tr>
					<td width="15">${item.uid}</td>
					<td>${item.description}</td>
					<td width="20">${item.getPoints()}</td>
				</tr>
				</table>
				</div>
	         <div class="itemSeperator"></div>
			</g:each>
			</div>
		</g:each>	
    </body>
</html>
