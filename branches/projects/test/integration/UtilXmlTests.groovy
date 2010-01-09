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

class UtilXmlTests extends GroovyTestCase {	
	def items
	def subItems 
	def groups
	def iterations
	def snapShots 
	
	void setUp()
	{
		groups = Defaults.getGroups(5)
    	items = Defaults.getItems(5,groups, null)
    	subItems = Defaults.getSubItems(20,items)
    	iterations = Defaults.getIterations(3)
    	
    	[groups,items,subItems,iterations].each{ it.eachWithIndex{ element, index -> element.id = index } }
    	
    	iterations.eachWithIndex{ iter,index -> 
    		iter.addItem(items[index])
    	}
    	snapShots = Defaults.getSnapShots(groups, new Date()-3, new Date())
    	
    	/*----------------------------------------------------------*/
    	/* Some magic, to support only the supported statuses at the moment... has to be updated to support all ItemStatus fields ... */
    	def setOverViewToSupportStatus = { overView -> 
    		[ ItemStatus.Blocking, ItemStatus.InProgress].each{ status ->
    			Priority.each{ prio ->
    				def points = overView.getPointsForView(prio,status)
    				if (points) {
    					overView.setPointsForView(prio,status,0)
    					overView.addPointsForView(prio,ItemStatus.Request,points)
    				}
    			}
    		}
    	}
    	
    	snapShots.each{ snapShot ->
    		setOverViewToSupportStatus(snapShot.overView)
    		snapShot.pointsForGroups.each{ setOverViewToSupportStatus(it.overView) }
    	}
    	/*------------------------------*/
	}
	
	void testExportImport_v0_3()
	{
		VersiontestExportImport(UtilXml_v0_3.docVersion)
	}
	
	void testExportImport_v0_4()
	{
		VersiontestExportImport(UtilXml_v0_4.docVersion)
	}
	
	void VersiontestExportImport(def docVersion)
	{
		def xmlString = UtilXml.exportToXmlString(groups, items, iterations, snapShots, new Date(), docVersion)
		
		def outputMap = UtilXml.importFromXmlString(xmlString)		
		def importGroups = outputMap.groups
		def importItems = outputMap.items
		def importIterations = outputMap.iterations
		def importSnapShots = outputMap.snapShots
				
		UtilXml.setRelationToDomainObjects(outputMap)
		
		outputMap.itemsByIteration.each{ iter, items ->
			items.each{ item -> iter.addItem(item) }
		}
		
		outputMap.itemsByGroup.each{ group, items ->
			items.each{ item -> group.addItem(item) }
		}
		
		assertTrue groups.size() == importGroups.size()
		assertTrue items.size() == importItems.size()
		assertTrue iterations.size() == importIterations.size()
		
		groups.eachWithIndex{ group, index ->
			assertTrue group.id == importGroups[index].id
			assertTrue group.name == importGroups[index].name
			assertTrue group.description == importGroups[index].description
		}
				
		items.eachWithIndex{ item, index ->
			assertTrue item.uid == importItems[index].uid
			assertTrue item.description == importItems[index].description
			assertTrue item.points == importItems[index].points
			assertTrue item.status == importItems[index].status
			assertTrue item.priority == importItems[index].priority
			assertTrue item.comment == importItems[index].comment
			assertTrue item.criteria == importItems[index].criteria
			
			assertTrue item.group.id == importItems[index].group.id
			
			assertTrue item.subItems.size() == importItems[index].subItems.size()
			item.subItems.each{ subItem ->
				def importSubItem = importItems[index].getSubItem(subItem.id)

				assertTrue subItem.item.uid == importSubItem.item.uid
				assertTrue subItem.id      == importSubItem.id
				assertTrue subItem.points  == importSubItem.points
				assertTrue subItem.status  == importSubItem.status
			}
		}
		
		iterations.eachWithIndex{ iter,index ->
			assertTrue iter.id == importIterations[index].id
			assertTrue iter.workingTitle == importIterations[index].workingTitle
			assertTrue iter.status == importIterations[index].status
			assertTrue Util.getDaysInBetween(iter.startTime, importIterations[index].startTime) == 0
			assertTrue Util.getDaysInBetween(iter.endTime, importIterations[index].endTime) == 0
			
			assertTrue iter.items.size() == importIterations[index].items.size()
			iter.items.each{ item ->
				assertTrue importIterations[index].items.find{ it.uid == item.uid } != null
			}
		}
		
		assertTrue importSnapShots.size() == snapShots.size()
		snapShots.eachWithIndex{ snapShot, index ->
		    def importSnapShot = importSnapShots[index]
			assertTrue Util.getDaysInBetween(snapShot.date, importSnapShot.date) == 0
		    
		    def overViewsAreEqual = { a, b -> 
		        if ( docVersion != "0.3")
		        {
		        	return a.equals(b)
		        }
		    	boolean equal = true
		    	Priority.each{ prio -> 
		    		if ( a.getPointsForView(prio,ItemStatus.Finished) != b.getPointsForView(prio,ItemStatus.Finished)) equal = false  
		    		if ( a.getPointsForPriority(prio) != b.getPointsForPriority(prio) ) equal = false
		    	}
		    	return equal
	    	}
		    
		    assertTrue overViewsAreEqual(snapShot.overView, importSnapShot.overView)
		    
		    assertTrue importSnapShot.pointsForGroups.size() == snapShot.pointsForGroups.size()
		    snapShot.pointsForGroups.eachWithIndex{ pointsForGroup, groupIndex ->
		    	def importPointsForGroup = importSnapShot.pointsForGroups.find{ it.group.id == pointsForGroup.group.id }
		    	assertTrue importPointsForGroup != null
		    	assertTrue overViewsAreEqual(pointsForGroup.overView, importPointsForGroup.overView)
		    }
		}
	}
}