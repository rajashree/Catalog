<head>
<title><@s.text name="title.manage.themes"/> </title>

</head>


    <div id="content">     
       <p>
        </p><table class="list" width="100%" >
         <tbody><tr>
          <th> Id</th><th>Name</th><th>Description</th><th>Created</th><th>Actions</th></tr>      
           <#if themes?exists  >
	        <#list themes as theme >
	   		  <tr class="odd"> <td>${theme.id}</td> <td>${theme.name}</td><td>${theme.description}</td><td>${theme.created}</td>
	   		  <td><input type="submit" onclick="javascript:document.location='<@s.url action='theme' method="edit" />?theme.name=${theme.name}'" value="Edit"/></td>
	   		  <td><input type="submit" onclick="javascript:document.location='<@s.url action='theme' method="remove" />?theme.name=${theme.name}'" value="Delete"/></td>
          	  </tr>
	   		</#list>			
 		
 		</#if>                 
         </table>   
         
         </div>
      
            
  
   