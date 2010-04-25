<html>
    <head>
	<link rel="stylesheet" href="${createLinkTo(dir:'css',file:'main.css')}" />
	<meta name="layout" content="main" />
	
	<style type="text/css">
    	tr.on {
    		 color: red;
    		 background-color: lightgrey;
    	} 
    	tr.off {    			 
    	}
    	
    	.groupName {
    		
    	}
    	</style> 
    	<nav:resources override="false"/>	
    </head>

    <body>
    	<g:javascript library="prototype" />
    	<nav:renderSubItems group="tags" />
	<g:set var="newItemId" value="${0}"/>
	<g:each var="entry" in="${itemsByGroup}">
		<g:set var="group" value="${entry.key}" />
		<g:set var="items" value="${entry.value.sort{it.priority}}" />
		<g:set var="newItemId" value="${newItemId+100}"/>
		
		<div id="groupItems${group.id}" class="itemGroup">
			<g:render template="itemsPerGroup" model="[items:items,group:group,newItemId:newItemId]"/>					
		</div>
	</g:each>
	<div>
	<br/>
	<br/>
	</div>
    </body>
</html>
