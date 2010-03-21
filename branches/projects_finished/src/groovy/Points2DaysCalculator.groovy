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

class Points2DaysCalculator
{
	def pointsPerDayMin
	def pointsPerDayMax
	def pointsUncertaintyPercentage
	
	def points2DaysRange(def points)
	{
		def minDays = this.pointsPerDayMin ? (points/this.pointsPerDayMax ) : 0
		def maxDays = this.pointsPerDayMax ? (points/this.pointsPerDayMin ) : 0
				
		if (pointsUncertaintyPercentage > 0)
		{
			maxDays = maxDays*(1.0+this.pointsUncertaintyPercentage/100.0)
		}
		else
		{
			minDays = minDays*(1.0+this.pointsUncertaintyPercentage/100.0)			
		}
		
		return minDays.toInteger()..maxDays.toInteger()
	}
	
	def points2Days(def points)
	{
		def range = points2DaysRange(points)				
		return Math.round((range.min()+range.max())/2.0).toInteger()
	}
}