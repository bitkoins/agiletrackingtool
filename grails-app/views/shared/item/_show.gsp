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
		<g:each var="subItem" in="${item.subItems.collect{it}.sort{it.id}}">
			<g:render template="/shared/subItem/show" model="[subItem:subItem]"/> 
		</g:each>
	</div>
</g:if>
