/*----------------------------------------------------------------------------
Project: Agile Tracking Tool

Copyright 2008, 2009, 2010   Ben Schreur
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
------------------------------------------------------------------------------*/

var newSubItemsCounter = 0;
 
function hideItem(itemId) {
	var divElement = document.getElementById(itemId);
	divElement.style.display='none';
}

function showItem(itemId) {
	var divElement = document.getElementById(itemId);
	divElement.style.display='block';
}

function getSubItemContainer()
{
	return document.getElementById("subItemContainer")
}

function deleteChild(id) {
    var subItem = document.getElementById(id)
	getSubItemContainer().removeChild(subItem)
}

function addNewSubItem() {
	var node = document.getElementById("subItemTemplate").cloneNode(true)
	var subItemId = "newSubItem_" + newSubItemsCounter;
	node.setAttribute("id", subItemId)
	node.getElementsByTagName("a")[0].onclick = function() { deleteChild(subItemId); };
	
	getSubItemContainer().appendChild(node)
	newSubItemsCounter++;
}

