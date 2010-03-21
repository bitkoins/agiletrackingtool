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

<g:link controller="itemGroup" action="edit" id="${group.id}"><h1 class="groupName">${group}</h1></g:link>

<g:each var="item" in="${items.collect{it}.sort{it.uid}}">
	<div id="itemBox${item.id}" class="itemBox">
		<div id="item${item.id}" class="item">
			<g:render template="/shared/item/show" model="[item:item]"/>
		</div>		
	</div>
	<div class="itemSeperator"></div>
</g:each>

<div id="newItem${newItemId}">
	<g:render template="/shared/item/addItemToGroup" model="[groupId:group.id,newItemId:newItemId]"/>
</div>

