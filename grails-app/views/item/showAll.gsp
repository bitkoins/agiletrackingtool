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
