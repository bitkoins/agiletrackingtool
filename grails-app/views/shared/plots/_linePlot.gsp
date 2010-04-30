<div class="plot">

  <g:if test="${plotData?.hasValidCurves()}">
	  <%  
	  	  org.agiletracking.PlotFormatUtil.calculateRangeValues(plotData)
	  %>

    	 <!-- Switch to easyChart by chart:lineChart ... , to googleChart: g:lineChart .../ -->
	 <g:lineChart title='${plotData.title}' dataType='text' data='${org.agiletracking.PlotFormatUtil.formatDataForCurves(plotData)}' 
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
