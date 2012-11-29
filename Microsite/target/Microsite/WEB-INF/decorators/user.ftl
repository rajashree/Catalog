<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
<title><@s.text name="label.header.title"/>- ${title} </title>
 <link rel="icon" type="image/gif" href="${base}/theme/default/favicon.gif"/>
 <script type='text/javascript' src='${base}/dwr/engine.js'></script>
 <script type='text/javascript' src='${base}/dwr/util.js'></script>
 <script type='text/javascript' src='${base}/js/validation.js'></script>  
    
 <style type="text/css" >
     @import "<@s.url value='/theme/default/css/style.css'/>";       
 </style>   
   ${head}
 </head>

 <body onload="${page.getProperty("body.onload")!}" class="${page.getProperty("body.class")!} ">
  
 <div id="container">
  <div id="header">  
   <#include "user-header.ftl"/>  
  <#include "user-top-menu.ftl"/>
  </div>
 	${body}		
<#include "footer.ftl"/> 
 </body>
</html>
