<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en" dir="ltr">
    <head>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
        <title><g:layoutTitle default="Agile Tracking Tool" /></title>
        <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'main.css')}" />
        <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'yui-custom.css')}" />
        <link rel="shortcut icon" href="${createLinkTo(dir:'images',file:'logo.ico')}" type="image/x-icon" />
        
        <g:layoutHead />
        <g:javascript library="application" />
        <g:javascript library="item" />
		
        <nav:resources override="false"/>        
        <gui:resources components="['toolTip']"/>
    </head>
 
    <body>
    	<div style="margin-right:130px;">
 			<nav:render group="tags" />
 		</div>
     	
        <div id="spinner" class="spinner" style="display:none;">
            <img src="${createLinkTo(dir:'images',file:'spinner.gif')}" alt="Spinner" />
        </div>	
        <div style="position:absolute;top:2px;right:10px;"><img width="125" src="${createLinkTo(dir:'images',file:'logo.jpg')}" alt="Grails" /></div>	
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
