<h1 class="groupName">${group}</h1>

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

