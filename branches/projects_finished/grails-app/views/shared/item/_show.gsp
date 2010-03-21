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

<div onmouseover="showItem('infoForItem${item.id}')" onmouseout="hideItem('infoForItem${item.id}')">
<table>
	<tr onmouseover="this.className='on'" onmouseout="this.className='out'"> 
		<td width="20">${item.uid}</td>
	 	<td>${item.description}</td>
		<td width="20">${item.points}</td>
		<td width="50">${item.priority}</td>
		<td width="10"><g:remoteLink action="editItem" id="${item.id}" update="item${item.id}">Edit</g:remoteLink></td>
		<td width="10"><g:remoteLink controller="item" action="deleteItem" id="${item.id}" update="itemBox${item.id}">Delete</g:remoteLink></td>							
	</tr>
		
</table>
</div>

<div id="infoForItem${item.id}" class="itemInfo" style="display:none">
	<g:if test="${item.comment}">
	<p>
		<b>Comment:</b>
		<br/>
		${item.comment}
	</p>
	<br/>
	</g:if>
	
	<g:if test="${item.criteria}">
	<p>
		<b>Acceptance criteria:</b>
		<br/>
		${item.criteria}
	</p>
	</g:if>
</div>


<g:if test="${!isCompositionView}">
	<div class="subItems">
		<g:each var="subItem" in in="${item.subItems.collect{it}.sort{it.id}}">
			<g:render template="/shared/subItem/show" model="[subItem:subItem]"/> 
		</g:each>
	</div>
</g:if>
