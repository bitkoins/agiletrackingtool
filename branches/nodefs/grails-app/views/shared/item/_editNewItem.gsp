<div id="itemBox${item.id}" class="itemBox">
	<div id="item${item.id}" class="item">
		<g:render template="/shared/item/edit" model="[item:item]"/>
	</div>
</div>

<div class="itemSeperator"></div>

<div id="newItem${newItemId}">
	<g:render template="/shared/item/addItemToGroup" model="[groupId:groupId,newItemId:newItemId]"/>
</div>


