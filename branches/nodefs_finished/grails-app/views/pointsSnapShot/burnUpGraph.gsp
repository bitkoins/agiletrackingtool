<html>
    <head>
        <title>Iteration History</title>
		<link rel="stylesheet" href="${createLinkTo(dir:'css',file:'main.css')}" />
		<meta name="layout" content="main" />
		
		<style type="text/css">
			.plots {
				margin-left: 100px;
			}
		</style>
		<nav:resources override="false"/>	
    </head>
    <body>
    	<g:javascript library="prototype" />
    	<nav:renderSubItems group="tags" />
    	
		<g:render template="/shared/plots/linePlot" model="[plotData:plotData, plotSize:[540,300] ]"/>
	
    </body>
</html>



