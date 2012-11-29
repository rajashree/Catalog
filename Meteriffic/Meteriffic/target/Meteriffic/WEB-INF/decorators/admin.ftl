<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html >
 <head>
<title><@s.text name="label.header.title"/>- ${title} </title>
 <link rel="icon" type="image/gif" href="${base}/theme/default/favicon.gif"/>
    <script type='text/javascript' src='${base}/dwr/engine.js'></script>
    <script type='text/javascript' src='${base}/dwr/util.js'></script>
<script type='text/javascript' src='${base}/js/mastercss_browser_selector.js'></script> 
    <style type="text/css" media="screen">
        @import "<@s.url value='/theme/default/css/style.css'/>";       
	@import "<@s.url value='/theme/default/css/style_crossbrowser.css'/>"; 
	
    </style>
    <script type='text/javascript' src='${base}/js/commons.js'></script>   
      
 <style type="text/css">

		#modalContainer {
			background-color:transparent;
			position:absolute;
			width:100%;
			height:100%;
			top:0px;
			left:0px;
			z-index:10000;
			background-image:url(tp.png); /* required by MSIE to prevent actions on lower z-index elements */
		}
		
		#alertBox {
			position:relative;
			width:300px;
			min-height:100px;
			margin-top:100px;
			border:2px solid #000;
			background-color:#F2F5F6;
			background-image:url(${base}/theme/default/css/images/alert.png);
			background-repeat:no-repeat;
			background-position:20px 30px;
		}
		
		#modalContainer > #alertBox {
			position:fixed;
		}
		
		#alertBox h1 {
			margin:0;
			font:bold 0.9em verdana,arial;
			background-color:#78919B;
			color:#FFF;
			border-bottom:1px solid #000;
			padding:2px 0 2px 5px;
		}
		
		#alertBox p {
			font:1.3em verdana,arial;
			height:50px;
			padding-left:5px;
			margin-left:55px;
		}
		
		#alertBox #closeBtn {
			display:block;
			position:relative;
			margin:5px auto;
			padding:3px;
			border:2px solid #000;
			width:70px;
			font:0.7em verdana,arial;
			text-transform:uppercase;
			text-align:center;
			color:#FFF;
			background-color:#78919B;
			text-decoration:none;
		}
		
		/* unrelated styles */
		
		#mContainer {
			position:relative;
			width:600px;
			margin:auto;
			padding:5px;
			border-top:2px solid #000;
			border-bottom:2px solid #000;
			font:0.7em verdana,arial;
		}
		
		h1,h2 {
			margin:0;
			padding:4px;
			font:bold 1.5em verdana;
			border-bottom:1px solid #000;
		}
		
		code {
			font-size:1.5em;
			color:#069;
		}
		
		#credits {
			position:relative;
			margin:25px auto 0px auto;
			width:350px; 
			font:0.7em verdana;
			border-top:1px solid #000;
			border-bottom:1px solid #000;
			height:90px;
			padding-top:4px;
		}
		
		#credits img {
			float:left;
			margin:5px 10px 5px 0px;
			border:1px solid #000000;
			width:80px;
			height:79px;
		}
		
		.important {
			background-color:#F5FCC8;
			padding:2px;
		}
		
		code span {
			color:green;
		}
</style>
  ${head}
 </head>

 <body >
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
       <ul > <div  id="disabledImageZone" style="float:right"><img id="imageZone" ></img></div>
       </ul>
      <ul id="options" class="options"  style="display:none">
      <li class="date">${applicationManager.applicationTime}</li>
        
        <#if request.remoteUser?exists>
        	<#if request.remoteUser == "admin">
        		<li class="administration"><a href="<@s.url action='index' namespace='/' includeParams="none" />" ><@s.text name="label.application.view"/></a></li>
        	</#if>
        	<li class="settings"><a href="<@s.url action='settings' includeParams="none" />" ><@s.text name="label.settings"/></a></li>
        </#if> 
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
        <li <#if tabIndex == 1> class="active" </#if> ><a href="<@s.url action='index' namespace='/admin'/>"><@s.text name="label.admin.settings"/></a><span/></li>         
              
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
</HTML>
