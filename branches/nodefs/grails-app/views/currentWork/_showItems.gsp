<div class="currentWork">	
	<g:each var="item" in="${items.collect{it}}">
		<div id="itemBox${item.id}" class="itemBox">
			<div id="item${item.id}">
			   <g:render template="itemView" model="[item:item]"/>
			</div>
		</div>
	</g:each>
</div>
