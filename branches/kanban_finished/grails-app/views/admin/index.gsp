<html>
    <head>
        <title>Admin page</title>
		<link rel="stylesheet" href="${createLinkTo(dir:'css',file:'main.css')}" />
		<meta name="layout" content="main" />
		<nav:resources override="false"/>
    </head>
    <body>
	<div>
    	<g:javascript library="prototype" />
    	<nav:renderSubItems group="tags" />
    	
	        <ul>
       		        <li><g:link controller="register" action="edit">Edit User Profile</g:link></li>
			<br/>
		        <li>
		        	<g:link style=";"  action="exportFile">Export all data to xml</g:link>
		        	<ul>
		        	<g:each var="docVersion" in="${org.agiletracking.UtilXml.supportedVersions}">
		        		<li><g:link action="exportFile" params="['docVersion':docVersion]">version ${docVersion}</g:link></li>
		        	</g:each>
		        	</ul>
		        </li>
		        <br/>
		        <li>Import all data from xml file:
		        	<g:form method="post" action="importFile" enctype="multipart/form-data">
    					<input size="60" type="file" name="file"/>
    				<button>Send</button>
	        		</g:form>
		        </li>
		        <br/>
		        <li><g:link style=";"  action="loadDefaults">Load example project data</g:link></li>
	        </ul>
	</div>
    </body>
</html>
