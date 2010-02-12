<!----------------------------------------------------------------------------
Project: Agile Tracking Tool

Copyright 2008, 2009   Ben Schreur
------------------------------------------------------------------------------
This file is part of Agile Tracking Tool.

Agile Tracking Tool is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Agile Tracking Tool is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Agile Tracking Tool.  If not, see <http://www.gnu.org/licenses/>.
------------------------------------------------------------------------------>

<html>
    <head>
        <title>Admin page</title>
		<link rel="stylesheet" href="${createLinkTo(dir:'css',file:'main.css')}" />
		<meta name="layout" content="main" />
		<nav:resources override="false"/>
    </head>
    <body>
    	<g:javascript library="prototype" />
    	<nav:renderSubItems group="tags" />
    	
    		<h1>Administration tasks: </h1>
			<br/>
			
	        <ul>
		        <li>
		        	<g:link style=";"  action="exportFile">Export all data to xml</g:link>
		        	<ul>
		        	<g:each var="docVersion" in="${UtilXml.supportedVersions}">
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
	        
	        <br/>
	    
        	<h1>Access to the underlying domain model:</h1>
            <ul>
                <li class="controller"><g:link controller="item">Work Items</g:link></li>
                <li class="controller"><g:link controller="pointsSnapShot">PointsSnapShots</g:link></li>
            </ul>
    </body>
</html>



