 <div id="header">
    
    <div class="topnav">
     
     <ul class="userbar">       
        <li class="first"><@s.text name="label.welcome"/> <span>
	        <#if user?exists>
	      		 ${user.username}			    
			   </#if>     
	       </span>
         </li>
        <li class="last">
        	<a href="<@s.url action='index' />"> 
	          <a href="<@s.url action='logout' namespace="/"  />" ><@s.text name="global.logout"/></a> 			 
		 </li>
      </ul>
        <ul class="options">
      <li class="date"></li>
        <#if request.remoteUser?exists>
        <li class="first"><a href="<@s.url action='settings' namespace="/user" includeParams="none" />" ><@s.text name="label.settings"/></a></li>
        <li><a href="#">Tools</a></li>
        </#if>    
        <li class="last"><a href="#"><@s.text name="label.help"/></a></li>
      </ul>
    </div>
  
  <#include "admin-top-menu.ftl"/>
    
  </div>