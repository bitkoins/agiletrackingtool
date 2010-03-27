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


	<h1>(${iteration.totalPoints()} points)</h1>
	<table border="1">
	<tr>
		<td>Id</td>
		<td>Item Name</td>
		<td>Points</td>		
	</tr>
	<g:each var="item" in="${iteration.items.sort{it.points}.reverse()}">
		<tr>
			<td width="20">${item.uid}</td>
		 	<td>${item}</td>
		 	<td width="50">${item.points}</td>
		 	<td width="150"><g:remoteLink action="deleteItemFromIteration" params="[iterId:iteration.id]" id="${item.id}" update="iteration">Delete from iteration</g:remoteLink></td>
		 </tr>
	</g:each>
	</table>


