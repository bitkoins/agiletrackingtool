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

class UtilXmlTests extends GroovyTestCase {	
	def items
	def subItems 
	def groups
	def iterations
	def snapShots
	def project 
	
	void setUp()
	{
		project = Defaults.getProjects(1)[0]
		project.prioritizedItemIds = "someTextWillDoForTesting"
		project.type = ProjectType.Kanban
		groups = Defaults.getGroups(2)
    	items = Defaults.getItems(3,groups,project)
    	subItems = Defaults.getSubItems(2,items)
    	iterations = Defaults.getIterations(2,project)
    	
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
	
	void testExportImport_v0_4()
	{
		VersiontestExportImport(UtilXml_v0_4.docVersion)
	}
	
	void testExportImport_v0_5()
	{
		VersiontestExportImport(UtilXml_v0_5.docVersion)
	}
	
	void VersiontestExportImport(def docVersion)
	{
		def xmlString = UtilXml.exportToXmlString(project, groups, items, iterations, snapShots, new Date(), docVersion)
		
		def outputMap = UtilXml.importFromXmlString(xmlString)		
		def importGroups = outputMap.groups
		def importItems = outputMap.items
		def importIterations = outputMap.iterations
		def importSnapShots = outputMap.snapShots
		def importProject = outputMap.project
				
		UtilXml.setRelationToDomainObjects(outputMap)
		
		outputMap.itemsByIteration.each{ iter, items ->
			items.each{ item -> iter.addItem(item) }
		}
		
		outputMap.itemsByGroup.each{ group, items ->
			items.each{ item -> group.addItem(item) }
		}
		
		assertTrue importProject != null
		if(docVersion ==  UtilXml_v0_5.docVersion) { 
			assertEquals importProject.name, project.name
			assertEquals importProject.prioritizedItemIds, project.prioritizedItemIds
			assertEquals importProject.type, project.type
	   }
		
		assertEquals groups.size() , importGroups.size()
		assertEquals items.size() , importItems.size()
		assertEquals iterations.size() , importIterations.size()
		
		groups.eachWithIndex{ group, index ->
			assertEquals group.id , importGroups[index].id
			assertEquals group.name , importGroups[index].name
			assertEquals importProject , importGroups[index].project
		}
				
		items.eachWithIndex{ item, index ->
			assertEquals item.uid , importItems[index].uid
			assertEquals item.description , importItems[index].description
			assertEquals item.points , importItems[index].points
			assertEquals item.status , importItems[index].status
			assertEquals item.priority , importItems[index].priority
			assertEquals item.comment , importItems[index].comment
			assertEquals item.criteria , importItems[index].criteria
			assertEquals importProject , importItems[index].project
			
			assertEquals item.subItems.size() ,portItems[index].group.id
			
			assertTrue item.subItems.size() == importItems[index].subItems.size()
			item.subItems.each{ subItem ->
				def imEquals subItem.id      , importSubItem.id
				assertEquals subItem.points  , importSubItem.points
				assertEquals subItem.status  ,m.points  == importSubItem.points
				assertTrue subItem.status  == importSubItem.staEquals iter.id , importIterations[index].id
			assertEquals iter.workingTitle , importIterations[index].workingTitle
			assertEquals iter.status , importIterations[index].status
			assertEquals importProject , importIterations[index].project
			assertEqualsertTrue iter.status == importIterations[index].status
			assertTrue Util.g, 0
			assertEquals Util.getDaysInBetween(iter.endTime, importIterations[index].endTime) , 0
			
			assertEquals iter.items.size() ,rations[index].endTime) == 0
			
			assertTrue iter.items.size() == importIterations[index].items.size()
			iter.items.each{ item ->
				assertTrue importIterations[iEquals importSnapShots.size() ,em.uid } != null
			}
		}
		
		assertTrue importSnapShots.size() == snapShots.size()
		snapShots.eachWithIndex{ snap    assertEquals importProject , importSnapShot.project
			 assertEquals Util.getDaysInBetween(snapShot.date, importSnapShot.date) ,rtTrue Util.getDaysInBetween(snapShot.date, importreturn a.equals(b) rPriority(prio) ) equal = false
		    	}
		    	return equal
	    	}
		    
		    assertTrue overViewsAreEqEquals importSnapShot.pointsForGroups.size() ,w)
		    
		    assertTrue importSnapShot.pointsForGroups.size() == snapShot.pointsForGroups.size()
		    snapShot.pointsForGroups.eachWithIndex{ pointsForGroup, groupIndex ->
		    	def importPointsForGroup = importSnapSh    	ointsForGroups.find{ it.group.id == pointsForGroup.group.id }
		    	assertTrue importPointsForGroup != null
		    	assertTrue overViewsAreEqual(point
