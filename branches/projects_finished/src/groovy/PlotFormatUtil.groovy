/*----------------------------------------------------------------------------
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
------------------------------------------------------------------------------*/

class PlotFormatUtil
{
	static def formatDataForCurve(List<PlotCurve> plotCurves, def xMin, def xMax, def yMin, def yMax)
	{
		def formattedPlotCurves = []
		plotCurves.each{ plotCurve ->
			def xValuesScaled = plotCurve.xValues
			def yValuesScaled = plotCurve.yValues
			
			def maxNumberOfPoints = 100 // freechart|googlechart can plot about this amount of points before going blank
			xValuesScaled = Util.makeListShorterWithScaling(xValuesScaled, maxNumberOfPoints)
			yValuesScaled = Util.makeListShorterWithScaling(yValuesScaled, maxNumberOfPoints)
			
			xValuesScaled = Util.reScale(xValuesScaled, xMin, xMax)
			yValuesScaled = Util.reScale(yValuesScaled, yMin, yMax)
			
			def roundToOneDigit = { new BigDecimal(it).setScale(1,java.math.RoundingMode.HALF_UP) }
			formattedPlotCurves << xValuesScaled.collect{ roundToOneDigit(it) }
			formattedPlotCurves << yValuesScaled.collect{ roundToOneDigit(it) }
		}
		return formattedPlotCurves
	}
	
	static def calculateRangeValues(PlotData plotData)
	{
		if (plotData.xMin == null) plotData.xMin = plotData.curves.collect{ it.xValues.min() }.min()
		if (plotData.xMax == null) plotData.xMax = plotData.curves.collect{ it.xValues.max() }.max()
		if (plotData.yMin == null) plotData.yMin = plotData.curves.collect{ it.yValues.min() }.min()
		if (plotData.yMax == null) plotData.yMax = plotData.curves.collect{ it.yValues.max() }.max()
		
		if(plotData.yMax == plotData.yMin) plotData.yMax = plotData.yMin + 1.0
	}
	
	static def formatDataForCurves(PlotData plotData)
	{
		calculateRangeValues(plotData)
		return formatDataForCurve(plotData.curves, plotData.xMin, plotData.xMax, plotData.yMin, plotData.yMax)
	}
	
	static def formatLegendString(PlotData plotData)
	{
		return plotData.curves.collect{it.legend}.join("|")
	}
	
	static def formatColorsForCurves(Integer nrCurves)
	{
		if (!nrCurves) return ""
		def colors = ["ff0000","00ff00","0000ff","ff00ff"]
		def myList = []
		def colorIndex=0
		nrCurves.times{ it ->
			myList << colors[colorIndex]
			colorIndex++
		}
		return myList.join(",")
	}
}