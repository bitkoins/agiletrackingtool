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
package org.agiletracking

class PlotFormatUtilTests extends GroovyTestCase
{
	def plotData
	def xValuesScaled = [0.0,50.0,100.0]
	def yValuesScaled = [0.0,20.0,50.0,100.0]
	
	void setUp()
	{
		plotData = new PlotData("Bla")
		def plotCurve = new PlotCurve("TestCurve")
		plotCurve.xValues = [0,1,2]
		plotCurve.yValues = [0,2,5,10]
		plotData.curves << plotCurve
	}
	
	void testSingleCurveFormatting()
	{
		plotData.xMin = 0
		plotData.xMax = 2
		plotData.yMin = 0
		plotData.yMax = 10
		assertTrue PlotFormatUtil.formatDataForCurves(plotData)== [ xValuesScaled , yValuesScaled ]
	}
	
	void testSingleCurveFormattingWithAutomaticScaling()	{
		
		assertTrue PlotFormatUtil.formatDataForCurves(plotData)== [ xValuesScaled, yValuesScaled ] 
	}
	
	void testDoubleCurveFormatting()
	{
		plotData.curves << plotData.curves[0]
		plotData.xMin = 0
		plotData.xMax = 2
		plotData.yMin = 0
		plotData.yMax = 10
		assertTrue PlotFormatUtil.formatDataForCurves(plotData)== [ xValuesScaled, yValuesScaled, xValuesScaled, yValuesScaled ] 
	}
		
	void testPlotDataHasPoints()
	{
		assertTrue plotData.hasValidCurves()
	}
	
	void testTwoCurvesWithValidPoints()
	{
		plotData.curves << plotData.curves[0]
		assertTrue plotData.hasValidCurves()
	}
	
	void testPlotDataHasNoPoints()
	{
		plotData.curves[0].xValues = []
		plotData.curves[0].yValues = []
		assertTrue !plotData.hasValidCurves()
	}
	
	void testOnlyOneCurveHasPoints()
	{
		def plotCurve = new PlotCurve("TestCurve")
		plotData.curves << plotCurve
		
		assertTrue plotData.curves[0].hasPoints()
		assertTrue !plotData.curves[1].hasPoints()
		assertTrue !plotData.hasValidCurves()
	}

	void testExceptionWhenFormattingTooManyColors() {
		shouldFail(Exception) { PlotFormatUtil.formatColorsForCurves(5) }
	}

	void testFormatOneColors() {
		assertEquals "ff0000" , PlotFormatUtil.formatColorsForCurves(1)
	}

	void testFormatTwoColors() {
		assertEquals "ff0000,00ff00" , PlotFormatUtil.formatColorsForCurves(2)
	}
}
