<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html >
<head>
<script type='text/javascript' src='${base}/dwr/engine.js'></script>
 <script type='text/javascript' src='${base}/dwr/util.js'></script>
 <script type='text/javascript' src='${base}/dwr/interface/adminDwrService.js'></script>

<title><@s.text name="label.header.title"/>- ${title} </title>
 <link rel="icon" type="image/gif" href="${base}/theme/default/favicon.gif"/>
    <style type="text/css" >
        @import "<@s.url value='/theme/default/css/style.css'/>";       
    </style>
  
  ${head}
</head>

 <body >
 <div id="container">

  <#include "header.ftl"/>
  	${body}
  	
	<div id="leftbar">
    <div class="actions">
      <h2><@s.text name="label.actions"/></h2>
      <div class="content">
        <ul>
            <li><a <#if actionIndex == 0> class="active"  </#if>  href="<@s.url action='index'></@s.url>"><@s.text name="label.registration.settings"/></a></li>          
            <li><a <#if actionIndex == 6> class="active"  </#if>  href="<@s.url action='users'></@s.url>"><@s.text name="label.user.summary"/></a></li>          
            <li><a <#if actionIndex == 1> class="active"  </#if>  href="<@s.url action='new.user' method="input"></@s.url>"><@s.text name="label.create.user"/></a></li>
            <li><a <#if actionIndex == 2> class="active"  </#if>  href="<@s.url action='mail.settings'></@s.url>"><@s.text name="label.email.server"/></a></li>
            <li><a <#if actionIndex == 3> class="active"  </#if>  href="<@s.url action='email.templates'></@s.url>"><@s.text name="label.email.templates"/></a></li>
    		<li><a <#if actionIndex == 4> class="active"  </#if>  href="<@s.url action='groups'></@s.url>"><@s.text name="label.group.summary"/></a></li>
            <li><a <#if actionIndex == 5> class="active"  </#if>  href="<@s.url action='create.group' method="input"></@s.url>"><@s.text name="label.create.group"/></a></li>
            <li><a <#if actionIndex == 7> class="active"  </#if>  href="<@s.url action='locale.settings' ></@s.url>"><@s.text name="label.locale.settings"/></a></li>
    
          </ul>
       </div>
    </div>
  </div>
	
<#include "footer.ftl"/>
  
    
 </body>
</html>
