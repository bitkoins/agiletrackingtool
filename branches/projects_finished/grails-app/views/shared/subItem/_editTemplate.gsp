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

<g:set var="prefix" value="${isNew ? 'newS' : 's'}" />
<g:set var="postfix" value="${isNew ? '' : '_' + subItem.id }" />

<table>
	<tr>	
		<td><g:textField name="${prefix + 'ubItem_description' + postfix}" value="${subItem?.description}" size="100" /></td>
		<td width="100"><g:textField name="${prefix + 'ubItem_points' + postfix}" value="${subItem?.getPoints()}" maxLength="30" /></td>
		<td width="50"><a onclick="deleteChild('${divId}')"><b>Delete</b></a></td>
	</tr>
</table>