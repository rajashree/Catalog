<head>
<title><@s.text name="label.timezone"/> </title>

</head>


    <div id="content">     
       <p>
        </p><table class="list" width="100%" >
         <tbody><tr>
          <th> Id</th><th>Name</th><th>Description</th><th>Created</th><th>Actions</th></tr>      
           <#if blocks?exists  >
	        <#list blocks as block >
	   		  <tr class="odd"> <td>${block.id}</td> <td>${block.name}</td><td>${block.description}</td><td>${block.created}</td>
	   		  <td><input type="submit" onclick="javascript:document.location='<@s.url action='block' method="edit" />?block.name=${block.name}'" value="Edit"/></td>
	   		  <td><input type="submit" onclick="javascript:document.location='<@s.url action='block' method="remove" />?block.name=${block.name}'" value="Delete"/></td>
          	  </tr>
	   		</#list>			
 		
 		</#if>                 
         </table>   
         
         </div>
      
            
  
   