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

<div class="plot">

  <g:if test="${plotData?.hasValidCurves()}">
	  <%  
	  	  org.agiletracking.PlotFormatUtil.calculateRangeValues(plotData)
	  %>
	 <chart:lineChart title='${plotData.title}' dataType='text' data='${org.agiletracking.PlotFormatUtil.formatDataForCurves(plotData)}' 
	     colors="${org.agiletracking.PlotFormatUtil.formatColorsForCurves(plotData.curves.size())}"
	     size="${plotSize}"
	     fill="${[ 'c,lg,0,76A4FB,1,ffffff,0' , 'bg,s,EFEFEF' ]}"
         axes="x,y" type='xy'  
         legend="${plotData.curves.collect{ it.legend }}"  
         axesRanges="${[0:[plotData.xMin,plotData.xMax],1:[plotData.yMin,plotData.yMax]]}" />
      
	 <p>y-axis: ${plotData.yLabel}</p>
	 <p>x-axis: ${plotData.xLabel}</p>
  </g:if>	
  <g:else>
  	<p>No valid plot data available.</p>
  </g:else>
  
</div>
