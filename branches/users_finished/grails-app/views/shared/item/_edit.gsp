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

<div style="display:none;">
	<g:set var="divId" value="newSubItem" />
	<div id="subItemTemplate">
		<div id="${divId}" class="subItem">
		 	<g:render template="/shared/subItem/editTemplate" model="[isNew:true,subItem:null,divId:divId]"/>
		</div>
	</div/
</div>

<div style="padding-bottom:20px;background-color: #99FF66">
<g:formRemote url="[action:'saveItem',id:item.id]" name="editForm" update="item${item.id}" >
	<table>
	<tr>
		<td witdh="20">${item.uid}</td>
		<td><g:textField name="description" value="${item.description}" size="100" /></td>
		<td width="10"><g:textField name="points" value="${item.getPoints()}" size="3"/></td>
		<td width="50"><g:select name="priority" from="${org.agiletracking.Priority}" value="${item.priority}" /></td>
	</tr>
	</table>
	
	<table>
	<tr>
		<td width="40">Comment:</td>
		<td span="2">
			<g:textArea name="comment" value="${item.comment}" cols="125" rows="3"/>
		</td>
	</tr>
	<tr>
		<td>Acceptance criteria:</td>
		<td>
			<g:textArea name="criteria" value="${item.criteria}" cols="125" rows="5"/>
		</td>
	</tr>
	</table>
    
    <div class="subItems">
	<div id="subItemContainer">
		<h2>SubItems:</h2>
		<g:each var="subItem" in in="${item.subItems.collect{it}.sort{it.id}}">
		<g:set var="divId" value="subItem_${subItem.id}" />
		<div id="${divId}" class="subItem">
			<g:render template="/shared/subItem/editTemplate" model="[isNew:false,subItem:subItem,divId:divId]"/>
		</div>
		</g:each>
	</div>
	<a onclick="addNewSubItem();"><b>Add SubItem</b></a>
	</div>
	
	<div style="text-align:right;margin-right:20px;margin-top:5px">
		<button>Save</button>
	</div>
	
</g:formRemote>
</div>
