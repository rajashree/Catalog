<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
<title> ${title} </title>
 <style type="text/css" >
        @import "<@s.url value='/theme/default/css/style.css'/>";       
    </style>  
   <script type='text/javascript' src='${base}/js/validation.js'></script>   
  ${head}
 </head>

 <body >
 <div id="container"> 
  <div id="header">
    <div class="topnav">
     <ul class="userbar">
        <li class="first"><@s.text name="label.welcome"/> <span>Guest</span></li>
         <#if true>
	        <li class="last"><a href="<@s.url action='signup' method="input" />" ><@s.text name="global.signup"/></a></li>
		 
		  </#if>     
       </ul>
     <ul class="options">
        <li class="date"></li>
        <li class="last"><a href="mailto:search@acceptsoftware.com"><@s.text name="label.help"/></a></li>
     </ul>
    </div>
    <h1><a href="<@s.url action='index' />"><@s.text name="label.header.title"/> </a></h1> 
  </div>
  	${body}
	
	<div id="footer">
    <div class="foot-left">
      <ul>
        <li><a  target="_blank"  href="http://acceptsoftware.com">About</a></li>
        <li class="last"><a href="mailto:search@acceptsoftware.com">Support</a></li>
      </ul>
    </div>
    <div class="foot-msg"> <@s.text name="global.copyright"/></div>
    <div class="foot-right"></div>
  </div>
</div>
	  
  
 </body>
</HTML>
