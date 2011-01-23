<h1>(${iteration.calculateTotalPoints()} points)</h1>
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


