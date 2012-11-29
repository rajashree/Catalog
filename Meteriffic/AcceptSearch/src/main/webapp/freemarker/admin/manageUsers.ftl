<head>
<title><@s.text name="label.timezone"/> </title>
<script type='text/javascript' src='${base}/dwr/interface/adminDwrService.js'></script>
<script type='text/javascript' src='${base}/dwr/engine.js'></script>
<script type='text/javascript' src='${base}/dwr/util.js'></script>

<script type='text/javascript' >
	function enableUserAccount(username){
		adminDwrService.enableUserAccount(username,false ,function(data) {
		   	 if(data)
		     alert("Operation Successfull ");
		   });
				
	}
</script>
</head>

<td style="border: 1px solid rgb(192, 192, 192); width: 100%; vertical-align: top; text-align: center;">
    <div align="center" style="min-height: 400px;">
    
     
      <p>
       <input type="hidden" value="users" name="page"/>
       <input type="hidden" value="cancel" name="method"/>
       <input type="hidden" value="" name="login"/>
       Number of users : <b>2</b>
       <br/><br/>
       Search for users with login starting with :
       <input type="text" value="" size="30" maxlength="200" name="loginStart"/>
       <input type="submit" onclick="document.forms[0].elements['method'].value='searchUser';" value="Submit"/>
      </p>
       
       <p>
        </p><table class="list" width="100%" >
         <tbody><tr>
          <th> Login</th><th>First name</th><th>Last name</th><th>Email</th><th>Actions</th></tr>      
           <#if userList?exists  >
	        <#list userList as user >
	   		  <tr class="odd"> <td>${user.username}</td><td>${user.firstName}</td><td>${user.lastName}</td><td style="text-align: center;"><a href="mailto:${user.email}">${user.email}</a> </td>
         		 <td style="text-align: center;"><input type="submit" onclick="enableUserAccount('${user.username}');" value="Disable"/> </td>
         	  </tr>
	   		</#list>			
 		
 		</#if>                 
         </table>      
   </div>
   </td>