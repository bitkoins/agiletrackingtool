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
        <title><g:layoutTitle default="Agile Tracking Tool" /></title>
        <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'main.css')}" />
        <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'yui-custom.css')}" />
        <link rel="shortcut icon" href="${createLinkTo(dir:'images',file:'logo.ico')}" type="image/x-icon" />
        
        <g:layoutHead />
        <g:javascript library="application" />
        <g:javascript library="item" />
		
        <nav:resources override="false"/>        
        <gui:resources components="['toolTip']"/>
        
        <style type="text/css">
		
    
    </style>
        			
    </head>
 
    <body>
    
    	<div style="margin-right:130px;">
 			<nav:render group="tags" />
 		</div>
     	
        <div id="spinner" class="spinner" style="display:none;">
            <img src="${createLinkTo(dir:'images',file:'spinner.gif')}" alt="Spinner" />
        </div>	
        <div style="position:absolute;top:2px;right:10px;"><a href="http://sites.google.com/site/agiletrackingtool/"><img width="125" src="${createLinkTo(dir:'images',file:'logo.jpg')}" alt="Grails" /></a></div>	
        <div style="color:#006dba;float:left;font-size:200%;margin-left:20px;margin-bottom:3px;margin-top:-9px">${session.project?.name}</div>
        <div style="position:absolute;font-size:75%;font-weight:bold;top:69px;right:12px;"><a href="http://sites.google.com/site/agiletrackingtool/">Powered by: Agile Tracking Tool</a></div>
	<div style="position:absolute;font-size:75%;right:150px;top:18px">
	   <p><g:isLoggedIn><g:loggedInUserInfo field="userRealName"/></p>
           <p><g:link controller="logout">logout</g:link></g:isLoggedIn></p>
	</div>
        <div style="clear:both" />
        
        <g:layoutBody />
        
    </body>	
</html>
