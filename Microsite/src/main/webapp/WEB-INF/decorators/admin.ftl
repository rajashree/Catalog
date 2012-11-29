<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
<title> ${page.title?default("")}</title>
 </title>
 <style type="text/css" >
        @import "<@s.url value='/theme/default/css/style.css'/>";       
    </style>  
   <script type='text/javascript' src='${base}/js/validation.js'></script>   
  ${head}
 </head>

 <body >
 <div id="container"> 
 
 
  <div id="header">  
   <#include "user-header.ftl"/>  
  <#include "admin-top-menu.ftl"/>
  </div>
  	${body}
	
	<#include "footer.ftl"/> 
  
 </body>
</HTML>
