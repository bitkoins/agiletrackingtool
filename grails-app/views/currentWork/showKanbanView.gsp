<html>
   <head>
       <title>Current Work</title>
		<link rel="stylesheet" href="${createLinkTo(dir:'css',file:'main.css')}" />
		<meta name="layout" content="main" />
		<nav:resources override="false"/>	
   </head>
   <body>
   <g:javascript library="prototype" />
   <nav:renderSubItems group="tags"/>

   <g:render template="showItems" model="[items:items]"/>
		
	<div><br/></div>

	<p>If you see no items, please <g:link controller="item" action="prioritize">prioritize</g:link>
	some items on your backlog.<p>

   </body>
</html>
