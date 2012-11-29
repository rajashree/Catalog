<head>
<title><@s.text name="label.timezone"/> </title>

</head>


    <div id="content">     
       <p>
        </p><table class="list" width="100%" >
         <tbody><tr>
          <th> Id</th><th>Name</th><th>Title</th><th>Description</th><th>Created</th><th>Actions</th></tr>      
           <#if pages?exists  >
	        <#list pages as page >
	   		  <tr class="odd"> <td>${page.id}</td> <td><a class="link" href="<@s.url action='viewpage' namespace="user"  />?pid=${page.id}">${page.name}</a></td><td>${page.title}</td><td>${page.description}</td><td>${page.created}</td>
	   		  <td><input type="submit" onclick="javascript:document.location='<@s.url action='page' method="edit" />?page.name=${page.name}'" value="Edit"/></td>
	   		  <td><input type="submit" onclick="javascript:document.location='<@s.url action='page' method="remove" />?page.name=${page.name}'" value="Delete"/></td>
          	  </tr>
	   		</#list>			
 		
 		</#if>                 
         </table>   
         
         </div>
      
            
  
   