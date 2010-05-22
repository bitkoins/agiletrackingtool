<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <gui:resources components="['draggableList']"/>
    <style>
    div.workarea {
        padding: 0px;
    }

    ul.draglist {
        list-style: none;
        margin: 0;
        padding: 0;
        /*
           The bottom padding provides the cushion that makes the empty
           list targetable.  Alternatively, we could leave the padding
           off by default, adding it when we detect that the list is empty.
        */
        padding-bottom: 10px;
    }

    ul.draglist li {
        margin: 4px;
        cursor: move;
	maring-top:2px;
    }

    li.list {
        background-color: white;
        border: 1px solid #7EA6B2;
	padding:2px;
    }

    .columnA, .columnB {
       width:45%;
       padding-left:10px;
       padding-right:10px;
       float:left;
       position:relative;
       margin:5px;
       border: 0px inset;
    }

    .columnB { 
	margin-left:30px;
    }
 
    #item_dummy_0_1 {
	background-color:lightyellow;
        padding:20px;
        text-align:center;
    }

    .saveButton {
	padding:5px;
	margin-top:10px;
	margin-bottom:10px;
    }

    </style>

	<nav:resources override="false"/>

</head>

<body>

    <nav:renderSubItems group="tags" />
    <g:form name="myForm" update="updateMe"
            url="[action: 'savePriorities']">

        <gui:draggableListWorkArea formReady="true">
      	
	<div class="columnA">
	<h2>Prioritized work:</h2>

        <button type="submit" class="saveButton">Save changes</button>
	<div class="itemGroup">
	 <p style="background-color:lightgrey">Highest priority</p>	
	 <gui:draggableList id="prioItems" class="list" prepend="item_">
	  <g:if test="${prioItems.size() == 0}">
	    <li id="dummy_0">Drag item to this area</li>
	  </g:if>
	   <g:each var="item" in="${prioItems}">
               	<li id="${item.uid}">${item.uid} &nbsp ${item.description}</li>
	    </g:each>
            </gui:draggableList>
	  <p style="background-color:lightgrey">Lowest priority</p>	
	</div>
	</div>


     <div class="columnB">
	<h2>Remaining work:</h2>
	<br/>
	<g:each var="group" in="${itemsByGroup.collect{it.key}}">
	<g:if test="${itemsByGroup[group].size()}">
	    <div class="itemGroup">
	    <h1>${group.name}</h1>		    
            <gui:draggableList id="${group}" class="list" prepend="item_">
	     <g:each var="item" in="${itemsByGroup[group]}">i
               	<li id="${item.uid}">${item.uid} &nbsp ${item.description}</li>
	    </g:each>
            </gui:draggableList> 
	    </div>
	</g:if>
	</g:each>
        </div>
 

        </gui:draggableListWorkArea>
        <br clear="left"/>
    </g:form>

</body>

</html>
