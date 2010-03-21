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
        <title>Compose your new Iteration</title>
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
    	
    	<script>
    		function hideItem(itemId) {
    			var divElement = document.getElementById(itemId);
    			divElement.style.display='none';
    		}
    		
    		function showItem(itemId) {
    			var divElement = document.getElementById(itemId);
    			divElement.style.display='block';
    		}
    	</script>
		
    </head>

    <body>
    	<g:javascript library="prototype" />
    	
			<h2>Items in the iteration: ${iteration.workingTitle} </h2>
			
			<div id="iteration" class="iteration">
				<g:render template="iterationOverview" model="[iteration:iteration,showIteration:showIteration,isCompositionView:isCompositionView]"/>
			</div>
	
			<br/>
			<h2>Select items for iteration:</h2>
			<g:each var="entry" in="${itemsByGroup}">
				<g:set var="group" value="${entry.key}" />
				<g:set var="items" value="${entry.value.sort{it.priority}}" />
				
				<g:if test="${items.size() > 0}">
				<div id="groupItems${group.id}" class="itemGroup" style="margin-right:50px">
					<h1>${group.name}</h1>	
					
					<g:each var="item" in="${items}">
					<div id="itemBox${item.id}" class="itemBox">
						<div id="item${item.id}" class="item" style="width:88%">
							<g:render template="/shared/item/show" model="[item:item]"/>
						</div>
						
						<div class="itemActions" style="width:10%">
							<g:remoteLink action="addItemToIteration" id="${item.id}" update="iteration">
								<div onclick="hideItem('itemBox${item.id}');">
									<g:if test="${item.iteration}">
								       	Move from Iteration ${item.iteration.id}
									</g:if>
									<g:else>
										Add to iteration																		
									</g:else>	
								</div>
							</g:remoteLink>
						</div>
					</div>
					
					<div class="itemSeperator"></div>
					</g:each>
				</div>
				<br></br>
				</g:if>
				
			</g:each>
    </body>
</html>
