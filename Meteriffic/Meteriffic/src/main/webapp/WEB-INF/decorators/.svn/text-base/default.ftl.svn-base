<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
<title>${title} </title>
 <style type="text/css" >
        @import "<@s.url value='/theme/default/css/style.css'/>"; 
	@import "<@s.url value='/theme/default/css/style_crossbrowser.css'/>";      
    </style>  
   <script type='text/javascript' src='${base}/js/validation.js'></script>   
<script type='text/javascript' src='${base}/js/commons.js'></script>   
<script type='text/javascript' src='${base}/js/mastercss_browser_selector.js'></script> 
  ${head}
 </head>

 <body>
 <div id="container"> 
  <div id="header">
    <div class="topnav">
     <ul class="userbar">
        <li class="welcome"  onclick="toggleLayer('options')"><@s.text name="label.welcome"/> <span>Guest</span></li>
            
       </ul>
     <ul id="options" class="options"  style="display:none">
        <li class="date"></li>
        <#if true>
	     <li class="register"><a href="<@s.url action='signup' method="input" />" ><@s.text name="global.signup"/></a></li>
	</#if>  
	<li class="help"><a href="mailto:meteriffic@sourcen.com"><@s.text name="label.help"/></a></li>
     </ul>
    </div>
    <h1><a href="<@s.url action='index' />"><@s.text name="label.header.title"/> </a></h1> 
  </div>
  	${body}
	
	<div id="footer">
    <div class="foot-left">
      <ul>
        <li><a  target="_blank"  href="http://www.sourcen.com/">About</a></li>
        <li class="last"><a href="mailto:meteriffic@sourcen.com">Support</a></li>
      </ul>
    </div>
    <div class="foot-msg"> <@s.text name="global.copyright"/></div>
    <div class="foot-right"></div>
  </div>
</div>
	  
  
 </body>
</HTML>
