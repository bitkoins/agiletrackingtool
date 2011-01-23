<g:if test="${!session.project.usesKanban() && iterations}">

<h1>Working days for scheduled iterations:</h1>
<table>

<g:set var="dayRanges" value="${iterations.collect{iter->planCalculator.p2dCalc.points2DaysRange(iter.calculateUnfinishedPoints())} }"/>
<g:set var="minTotal" value="${dayRanges.sum{it.min()}}"/> 
<g:set var="maxTotal" value="${dayRanges.sum{it.max()}}"/>

<tr style="background:lightgrey">
	<td>Total days:</td>
	<td>${minTotal}..${maxTotal}</td>
</tr>

<tr style="background:yellow">
	<td>Estimated date range:</td>
	<td><g:formatDate format="dd-MMM " date="${new Date() + minTotal}"/> .. <g:formatDate format="dd-MMM  (yyyy)" date="${new Date() + maxTotal}"/></td>
</tr>

<tr/><tr/><tr/><tr/><tr/><tr/>

<tr style="background:yellow">
	<td>Iteration</td>
	<td width="200">Days Work Range</td>
</tr>

<g:each var="iter" in="${iterations}">
	<g:set var="dayRange" value="${planCalculator.p2dCalc.points2DaysRange(iter.calculateUnfinishedPoints())}"/>
	<tr>
		<td><g:link controller="iterationComposer" action="compose" id="${iter.id}">${iter.workingTitle}</g:link></td>
		<td>${dayRange.min()}..${dayRange.max()}</td>
	</tr>
</g:each>

</table>

<br/>
<br/>

</g:if>

<h1>Working days for all remaining work:</h1>
<table>
	
	<tr style="background:lightgrey">
		<td>Total days:</td>
		<td>${planCalculator.getWorkingDaysRangeLeft(org.agiletracking.Priority.High).min()} .. ${planCalculator.getWorkingDaysRangeLeft(org.agiletracking.Priority.High).max()}</td>
		<td> + ${planCalculator.getWorkingDaysRangeLeft(org.agiletracking.Priority.Medium).min()} .. ${planCalculator.getWorkingDaysRangeLeft(org.agiletracking.Priority.Medium).max()}</td>
		<td> + ${planCalculator.getWorkingDaysRangeLeft(org.agiletracking.Priority.Low).min()} .. ${planCalculator.getWorkingDaysRangeLeft(org.agiletracking.Priority.Low).max()}</td>
	</tr>
	
	<tr style="background:yellow">
		<td>Estimated date range:</td>
		<td><g:formatDate format="dd-MMM " date="${dateByPriority[org.agiletracking.Priority.High].min()}"/> .. <g:formatDate format="dd-MMM  (yyyy)" date="${dateByPriority[org.agiletracking.Priority.High].max()}"/></td>
		<td><g:formatDate format="dd-MMM " date="${dateByPriority[org.agiletracking.Priority.Medium].min()}"/> .. <g:formatDate format="dd-MMM  (yyyy)" date="${dateByPriority[org.agiletracking.Priority.Medium].max()}"/></td>
		<td><g:formatDate format="dd-MMM " date="${dateByPriority[org.agiletracking.Priority.Low].min()}"/> .. <g:formatDate format="dd-MMM  (yyyy)" date="${dateByPriority[org.agiletracking.Priority.Low].max()}"/></td>
	<tr>
	
	<tr><td></td></tr>

	<tr style="background:yellow">
		<td>Category:</td>
		<td width="180">Must Haves [days]</td>
		<td width="180">+ Could Haves [days]</td>
		<td width="180">+ Nice to Haves [days]</td>
	</tr>
	<g:each var="group" in="${planCalculator.getGroups().collect{it}.sort{ planCalculator.getWorkingDaysLeft(it,org.agiletracking.Priority.High)}.reverse() }">
		<tr>
			<td><g:link controller="item" action="backlog">${group}</g:link></td>
			<td>${planCalculator.getWorkingDaysRangeLeft(group,org.agiletracking.Priority.High).min()} .. ${planCalculator.getWorkingDaysRangeLeft(group,org.agiletracking.Priority.High).max()}</td>
			<td> + ${planCalculator.getWorkingDaysRangeLeft(group,org.agiletracking.Priority.Medium).min()} .. ${planCalculator.getWorkingDaysRangeLeft(group,org.agiletracking.Priority.Medium).max()}</td>
			<td> + ${planCalculator.getWorkingDaysRangeLeft(group,org.agiletracking.Priority.Low).min()} .. ${planCalculator.getWorkingDaysRangeLeft(group,org.agiletracking.Priority.Low).max()}</td>
		</tr>
	</g:each>
	
</table>



