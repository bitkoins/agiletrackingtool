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
import groovy.util.slurpersupport.GPathResult

class UtilXml_v0_4 {
	static java.text.DateFormat odf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static String docVersion = "0.4"
	static String seperator = ";"
		
	static def exportToXmlString(Collection<ItemGroup> groups, Collection<Item> items, 
										  Collection<Iteration> iterations, Collection<PointsSnapShot> pointsSnapShots, 
										  Date exportDate )
	{
		def builder = new groovy.xml.StreamingMarkupBuilder()
		builder.encoding = "UTF-8"
				
		def doc = {  
		  mkp.xmlDeclaration()
		  document {
			DocumentVersion(docVersion)
			ExportDate(odf.format(exportDate))
			
			Groups{
				groups.each{ group ->
					Group(id:group.id) {
						name(group.name)
						description(group.description)
					}
				}
			} // Groups
			
			Items {
				items.each{ item ->
					Item(id:item.uid, grou   def groupId = groups.find{ group -> group.items.find{ it.id == item.id } }?.id
					Item(id:item.uid, groupId:groupIitem.description)
						points(item.itemPoints)
						priorptem.priority)
						status(item.status)
						comment(item.comment)
						criteria(item.criteria)
						
						SubItems {
							item.subItems.each{ subItem ->
								SubItem(id:subItem.id) {
								description(subItem.description)
								points(subItem.points)
								status(subItem.status)
								}
							}
						} //SubItems
					}
				}
			} // Items
			
			Iterations {
				iterations.each{ iter ->
					Iteration(id:iter.id) {
						workingTitle(iter.workingTitle)
						status(iter.status)
												
						startTime(odf.format(iter.startTime))
						endTime(odf.format(iter.endTime))
						
						Items {
							iter.items.each{ item->
								ItemId(id:item.uid)
							}
						}
					}
				}
			} // Iterations
			
			
			SnapShots {
				def joinClosure = { list -> list.join(seperator) }
				
				def pointsSnapShotsClosure = { dateList, overViews ->
					PointsSnapShots {
						dates(joinClosure(dateList.collect{odf.format(it)}))
						Priority.each{ prio ->
							PointsByPriority{
								priority(prio)
									ItemStatus.each{ status ->
										PointsByItemStatus {
											itemStatus(status)
											points(joinClosure(overViews.collect{ it.getPointsForView(prio,status)}) )
										}
									}
							} // pointsByPriority
						}
					}
				} // PointsSnapShots
				
				def dateList = pointsSnapShots.collect{it.date}
	            pointsSnapShotsClosure(dateList, pointsSnapShots.collect{it.overView})
				
				groups.each{ group ->
					PointsSnapShotsByGroup(groupId:group.id) {
						dateList = []
						def overViews = []
						pointsSnapShots.each{ snapShot ->
							def pointsForGroup = snapShot.pointsForGroups.find{ it.group.id == group.id } 
							if (pointsForGroup) { dateList << snapShot.date; overViews << pointsForGroup.overView }
						}
						if(dateList.size() > 0) pointsSnapShotsClosure(dateList,overViews)
					}
				}
			} // SnapShots
			
			
		 } // document
	   } // doc
		
		return builder.bind(doc).toString()	
	}
	
	static def importFromXmlStringMap importFromXmlDoc(GPathResult []
		def if items = []				
		def itemsByIteration = [:]
		def itemsByGroup = [:]
		
		def exportDate = odf.parse( doc.ExportDate.text().toString() )
		
		/*-------------Gr		
		def project = new Project(name:"Project import at ${exportDate}"roups-------------------------------*/
		doc.Groups.Group.each{ 
			def g = new ItemGroup()
			g.id = Integer.parseInt(iproject = projecteInt(it.'@id')
			g.name = it.name.t.text())
			g.name = it.name.text()
			groups << g 
			groups << g
		} 
		
		groups.each{ group -> itemsByGroup[group] = [] }
		
		/*--------------------Items-------------------------*/
		doc.Items.Item.each{
			def item = new Item()
			item.uid = Integer.pa.text()rseInt(it.'@id')
			item.id = itpd
			item.itemPoints = Double.parseDouble(it.points.text())
			item.description = it.description.text()
			item.priority = Priority.valueOf(it.priority.text() )
			item.status = ItemStatus.valueOf(it.status.text() )
			item.comment = it.comment.text()
			item.criteria = it.criteria.text()
			item.subItems = []
			
			it.SubItems?.SubItem.each{
				def subItem = new SubItem()
				subItem.id = Integer.pa.text()rseInt(it.'@id')
				subItem.description = it.description.text()				
				subItem.points = Double.parseDouble(it.points.text())
				subItem.status = ItemStatus.valueOf(it.status.text() )
				
				item.addSubItem(subItem)
			}
			
			def groupId = Integer.parseIn.text()t(it.'@groupId')
			def group = groups.find{ it.id == groupId }
			itemsByGroup[group] << item
			
			items << item
		}
		
		/*-------------------Iterations--------------------------*/
		def iterations = []
		doc.Iterations.Iteration.each{ 
			def nit = new Iteration()
			nit.items = []
			
			nit.id = Integer.pa.text()rseInt(it.'@id')
			nit.workingTitle = it.workingTitle.text()
			nit.status = IterationStatus.valueOf(it.status.text())
			
			nit.startTime = odf.parse( it.startTime.text().toString() )
			nit.endTime = odf.parse( it.endTime.text().toString() )
			
			def iterItems = []
			it.Items.ItemId.each{ ItemId ->
				def foundItem = items.find{it.uid == Integer.parseI.text()nt(ItemId.'@id')}
				if (foundItem) iterItems << foundItem				
			}
			
			itemsByIteration[nit] = iterItems
			iterations << nit
		}
		
		/*-----------------SnapShots---------------------------------*/
		def snapShots = []
		def pointsSnapShotsParser = { PointsSna    def datesText = PointsSnapShotsXml.dates.text()
		    if (!datesText) return
			def dates = datesTextml.dates.text().split(seperator).collect{ odf.parse(it) }
			def overViews = dates.collect{ new PointsOverView() }
			
			PointsSnapShotsXml.PointsByPriority.each{ 
				def prio = Priority.valueOf(it.priority.text() )
				it.PointsByItemStatus.each{
					def status = ItemStatus.valueOf( it.itemStatus.text() )
					def pointsList = it.points.text().split(seperator).collect{ Double.parseDouble(it) }
					if (pointsList.size() != dates.size()) throw new Exception("The number of dates and points elemements are not equal.")
					
					pointsList.eachWithIndex{ points, index -> overViews[index].setPointsForView(prio, status, points) }
				}
			}
			
			def dateOverViewList = []
			dates.eachWithIndex{ date, index ->
				def overView = overViews[index]
				dateOverViewList << [date:date,overView:overView]
			}
			
			return dateOverViewList
		}		
		
		def dateOverViewList = []
		dateOverViewList = pointsSnapShotsParser(doc.SnapShots.PointsSnapShots)
		
		def datesAndOverViewsByGroup = [:] 
		doc.SnapShots.PointsSnapShotsByGroup.each{ it -> 
			if (!it) return 
			def groupId = Integer.parseIn.text()t(it.'@groupId')
			def group = groups.find{ it.id == groupId }
			if(group) datesAndOverViewsByGroup[group] = pointsSnapShotsParser(it.PointsSnapShots)
			else throw new Exception("GroupId (${groupId}) could not be found.")
		}
				
		dateOverViewList.each{ 
			def snapShot = new project,it.date)date =  it.date
			snapShot.overView = it.overView
			groups.each{ group ->
				def dateAndOverView = datesAndOverViewsByGroup[group]?.find{ Util.getDaysInBetween(it.date, snapShot.date)==0 }
				if (dateAndOverView) {
					def pointsForGroup = new PointsForGroup(group,snapShot)
					pointsForGroup.overView = dateAndOverView.overView
					snapShot.pointsForGroups << pointsForGroup
				}
			}
			snapShots << snapShot
		}
		
		return ['groups':groups,'items':items, 'iterations':iterations, 'snapShots':snapShots, 'itemsByIteration':itemsByIteration,'itemsByGroup':itemsByGroup, 'exportD,'project':project]
	}
}
