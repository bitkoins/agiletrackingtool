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

<html>
    <head>
        <title><g:layoutTitle default="Grails" /></title>
        <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'main.css')}" />
        <link rel="shortcut icon" href="${createLinkTo(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
        
        <g:layoutHead />
        <g:javascript library="application" />
        
        <script>
    		function hideItem(itemId) {
    			var divElement = document.getElementById(itemId);
    			divElement.style.display='none';
    		}
    		
    		function showItem(itemId) {
    			var divElement = document.getElementById(itemId);
    			divElement.style.display='block';
    		}
    	
			var newSubItemsCounter = 0;
	
			function getSubItemContainer()
			{
				return document.getElementById("subItemContainer")
			}
			
			function deleteChild(id) {
			    var subItem = document.getElementById(id)
				getSubItemContainer().removeChild(subItem)
			}
			
			function addNewSubItem() {
				var node = document.getElementById("subItemTemplate").cloneNode(true)
				var subItemId = "newSubItem_" + newSubItemsCounter;
				node.setAttribute("id", subItemId)
				node.getElementsByTagName("a")[0].onclick = function() { deleteChild(subItemId); };
				
				getSubItemContainer().appendChild(node)
				newSubItemsCounter++;
			}
		</script>
        <nav:resources override="false"/>			
    </head>
 
    <body>
    	<div style="margin-right:130px;">
 			<nav:render group="tags" />
 		</div>
     	
        <div id="spinner" class="spinner" style="display:none;">
            <img src="${createLinkTo(dir:'images',file:'spinner.gif')}" alt="Spinner" />
        </div>	
        <div style="position:absolute;top:2px;right:10px;"><a href="http://sites.google.com/site/agiletrackingtool/"><img width="125" src="${createLinkTo(dir:'images',file:'logo.jpg')}" alt="Grails" /></a></div>	
        <div style="color:#006dba;float:left;font-size:200%;margin-left:20px;margin-bottom:3px;margin-top:0px"><g:meta name="app.projectName"/></div>
        <div style="float:right;font-size:85%;font-weight:bold;margin-right:1px;margin-top:2px"><a href="http://sites.google.com/site/agiletrackingtool/">Powered by: Agile Tracking Tool</a></div>
        <div style="clear:both" />
        
        
        <g:layoutBody />		
    </body>	
</html>