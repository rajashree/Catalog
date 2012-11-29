<head>
<title><@s.text name="title.manage.user.sites"/> </title>
</head>

    <div id="content">     
       <p>
        </p><table class="list" width="100%" >
         <tbody><tr>
          <th> Id</th><th><@s.text name="label.name"/></th><th><@s.text name="label.user"/></th><th><@s.text name="label.description"/></th><th><@s.text name="label.created"/></th><th><@s.text name="label.last.modified"/></th><th colspan="3" align="center" ><@s.text name="label.actions"/></th></tr>      
           <#if sites?exists  >
	        <#list sites as site >
	   		  <tr class="odd"> <td>${site.id}</td> <td><a class="link" href="<#if site.status == 1 > ${base}/sites/${site.id}/index.html <#else> javascript:void(0);</#if>">${site.name}</a></td><td>${site.username}</td><td>${site.description}</td><td>${site.created}</td><td>${site.modified}</td>
	   		  <td><input type="submit" onclick="javascript:document.location='<@s.url action='site.edit' method="input" />?site.id=${site.id}'" value="Edit"/></td>
	   		  <td><input type="submit" onclick="javascript:document.location='<@s.url action='site' method="remove" />?sid=${site.id}'" value="Delete"/></td>
          	  <td><#if site.status == 1 > <input type="submit" onclick="javascript:document.location='<@s.url action='site' method="download" />?sid=${site.id}'" value="Download"/> <#else>	<input type="submit" onclick="javascript:document.location='<@s.url action='siteInfo-publish'  />?sid=${site.id}'" value="Publish"/>	
          	  </#if></td>
          
          	  </tr>
	   		</#list>			
 		
 		</#if>                 
         </table>   
         
         </div>
  