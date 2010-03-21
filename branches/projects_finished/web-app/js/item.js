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

