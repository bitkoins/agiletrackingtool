<g:set var="prefix" value="${isNew ? 'newS' : 's'}" />
<g:set var="postfix" value="${isNew ? '' : '_' + subItem.id }" />

<table>
	<tr>	
		<td><g:textField name="${prefix + 'ubItem_description' + postfix}" value="${subItem?.description}" size="100" /></td>
		<td width="100"><g:textField name="${prefix + 'ubItem_points' + postfix}" value="${subItem?.getPoints()}" maxLength="30" /></td>
		<td width="50"><a onclick="deleteChild('${divId}')"><b>Delete</b></a></td>
	</tr>
</table>
