<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
<title><@s.text name="label.header.title"/>- ${title} </title>
 <link rel="icon" type="image/gif" href="${base}/theme/default/favicon.gif"/>
    <script type='text/javascript' src='${base}/dwr/engine.js'></script>
    <script type='text/javascript' src='${base}/dwr/util.js'></script>
     <script type='text/javascript' src='${base}/js/validation.js'></script>  
    <script type='text/javascript' src='${base}/js/commons.js'></script>   
<script type='text/javascript' src='${base}/js/mastercss_browser_selector.js'></script> 
    <style type="text/css" >
        @import "<@s.url value='/theme/default/css/style.css'/>";    
	@import "<@s.url value='/theme/default/css/style_crossbrowser.css'/>"; 
    </style>
    
 	
<style type="text/css">
		
		.Accordion, .AquaAccordion {
			width: 300px;
		}
		
		#Acc1.Accordion {
			width: 100%;
		}
		.AccordionPanelTab a {
			display: block;
			color: black;
			text-decoration: none;
		}
		.AccordionPanelContent {
			height: 250px;
		}
		
		span.AccordionPanelContent {
			display: block;
		}
		
		/* Here's an example of an accordion Aqua Gradient theme that uses
		 * different class names from the ones used in SpryAccordion.css,
		 * to style the different parts of the accordion.
		 */
		.AquaAccordion {
			border-left: solid 1px gray;
			border-right: solid 1px black;
			border-bottom: solid 1px gray;
			overflow: hidden;
			font-family: "Times New Roman", Times, serif;
			font-size: 16px;
		}
		
		.AquaAccordion .Tab {
			height: 24px;
			background-image: url(images/aqua-gradient.gif);
			background-repeat: repeat-x;
			background-color: #6dc1fc;
			border-top: solid 1px black;
			border-bottom: solid 1px gray;
			margin: 0px;
			padding: 2px;
			cursor: pointer;
			-moz-user-select: none;
			-khtml-user-select: none;
			text-align: center;
		}
		
		.AquaAccordion .Content {
			overflow: auto;
			height: 300px;
			margin: 0px;
			padding: 0px;
			background-image: url(images/gray-gradient.gif);
			background-repeat: repeat-x;
		}
		
		.AquaAccordion .hover {
			background-image: none;
			background-color: #33CCFF;
		}
		
		.AquaAccordion .open {
			/* Add properties here. */
		}
		
		.AquaAccordion .closed {
			/* Add properties here. */
		}
		
		.AquaAccordion .focused {
			/* Add properties here. */
		}

</style>
${head}
 </head>

 <body >
   <script type='text/javascript' src='${base}/js/wz_tooltip.js'></script>   
  
 <div id="container">
 <div id="header">
    <div class="topnav">
      <ul class="userbar">       
        <li class="welcome"  onclick="toggleLayer('options')"><@s.text name="label.welcome"/> <span>
	        <#if request.remoteUser?exists>
	      		 ${request.remoteUser}			    
			   </#if>     
	       </span>
         </li>
        
      </ul>
      <ul id="options" class="options"  style="display:none">
        <li class="date">${applicationManager.applicationTime}</li>
        <#if request.remoteUser == "admin">
        		<li class="administration"><a href="<@s.url action='index' namespace='/admin' includeParams="none" />" ><@s.text name="label.administration"/></a></li>
        </#if>       
        <li class="settings"><a href="<@s.url action='settings'  />" ><@s.text name="label.settings"/></a></li>    
         <li class="logout">
        	<a href="<@s.url action='index' />"> 
	          <a href="<@s.url action='logout' namespace="/"  />" ><@s.text name="global.logout"/></a> 			 
		 </li>  
           <li class="help"><a href="mailto:meteriffic@sourcen.com"><@s.text name="label.help"/></a></li>
      </ul>
    </div>
    <h1><a href="<@s.url action='index' />"><@s.text name="label.header.title"/> </a></h1>
    <div class="mainmenu">
		<ul>
        	<li <#if tabIndex == 1> class="active" </#if> ><a href="<@s.url action='index' />"><@s.text name="label.dashboard"/></a><span/></li>         
      	</ul>
    </div>
  </div>
  
 
	${body}
	
	
	
<div id="footer">
    <div class="foot-left">
      <ul>
        <li><a target="_blank" href="http://www.sourcen.com/">About</a></li>
        <li class="last"><a href="mailto:meteriffic@sourcen.com">Support</a></li>
      </ul>
    </div>
    <div class="foot-msg"> <@s.text name="global.copyright"/></div>
    <div class="foot-right"></div>
  </div>
</div>
   <script type='text/javascript' >
      	 useLoadingImage("${base}/theme/default/css/images/ajax-loader.gif");
    
     </script>   
 </body>
</html>
