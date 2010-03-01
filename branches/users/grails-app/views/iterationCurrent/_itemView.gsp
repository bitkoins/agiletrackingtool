<!----------------------------------------------------------------------------
Project: Agile Tracking Tool

Copyright 2008, 2009   Ben Schreur
------------------------------------------------------------------------------
This file is part of Agile Tracking Tool.

Agile Tracking Tool is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or:
(at your option) any later version.

Agile Tracking Tool is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Agile Tracking Tool.  If not, see <http://www.gnu.org/licenses/>.
------------------------------------------------------------------------------>

<table class="status${item.status}">
	<tr>
		<td width="20"><g:link controller="item" action="edit" id="${item.id}">${item.uid}</g:link></td>
		
		<td>
			<gui:toolTip text="${item.comment ? 'Comment: ' + item.comment : '	'}">
				${item.description}
			</gui:toolTip>			
		</td>
		
		<td width ="50">${item.points}</td>
				
				
			<td width ="50">	
				<g:if test="${item.status != org.agiletracking.ItemStatus.InProgress}">
					<g:remoteLink action="itemInProgress" id="${item.id}" update="item${item.id}">${item.status == org.agiletracking.ItemStatus.Finished ? 'Back' : 'Set'} 'In Progress'</g:remoteLink>
				</g:if>
			</td>
			
		<g:if test="${item.status != org.agiletracking.ItemStatus.Finished}">		
			<td width ="50">			
				<g:if test="${ item.status != org.agiletracking.ItemStatus.Finished && item.status != org.agiletracking.ItemStatus.Request}">
					<gui:toolTip text="Done: ${item.hasCriteria() ? item.criteria : ' ? '}">
   						<g:remoteLink action="itemDone" id="${item.id}" update="item${item.id}">Set 'Finished'</g:remoteLink>
					</gui:toolTip>
				</g:if>
			</td>	
			
			<td width ="50">
			 	<g:if test="${item.status != org.agiletracking.ItemStatus.Blocking}">	
			 		<g:remoteLink action="itemBlocking" id="${item.id}" update="item${item.id}">Set 'Blocking'</g:remoteLink>
			 	</g:if>	 		
			</td>
		</g:if>
		
		<td width ="50">		
		  <g:if test="${!item.hasCriteria()}">
		  <gui:toolTip text="The item has no acceptance criteria.">
		     <img src="${createLinkTo(dir:'images',file:'QuestionMark.png')}" alt="QuestionMark" width="35" height="35"/>
		  </gui:toolTip>		     
		</g:if>
				 	
		<g:else>
			<td width ="200">${item.criteria}</td>
		</g:else>
		<td width="10"><g:remoteLink action="editItem" id="${item.id}" update="item${item.id}">Edit</g:remoteLink></td>
	</tr>
</table>

<div class="subItems">
	<g:each var="subItem" in="${item.subItems.collect{it}.sort{it.id}}">
	<div class="subItem" id="subItem${subItem.id}">
	<table class="status${subItem.status}">
		<tr>
			<td>${subItem.description}</td>
			<td width="20">${subItem.points}</td>
			
			<g:if test="${subItem.status != org.agiletracking.ItemStatus.Finished}">	
					<td width="60">
			 		<g:remoteLink action="subItemFinished" id="${subItem.id}" update="item${item.id}">Set Finished</g:remoteLink>
			 		</td>
			 </g:if>
			 <g:else>
			 		<td width="40">${subItem.status}</td>
			 </g:else>
			 	 		
		</tr>
	</table>
	</div>
	</g:each>
</div>
