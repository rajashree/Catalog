<head>
 
<title>aaaaaaaaaaaaaa<@s.text name="label.timezone"/> </title>

<script type='text/javascript' >
function updatTimeZone(){
	
	var timezoneId=dwr.util.getValue("timezone");
	adminDwrService.setDefaultTimeZone(timezoneId ,function(data) {
	   	 if(data)
	     alert("Operation Successfull ");
	   });
			
}
function enableUserRegistration(){
	
	var userRegistration=dwr.util.getValue("userRegistration");
	adminDwrService.enableUserRegistration(userRegistration,function(data) {
	   	 if(data)
	     alert("Operation Successfull ");
	   });
}
function enableResetPassword(){
	
	var password=dwr.util.getValue("password");
	adminDwrService.enableResetPassword(password,function(data) {
	   	 if(data)
	     alert("Operation Successfull ");
	   });
}
function enableEmailValidation(){
	
	var emailValidation=dwr.util.getValue("emailValidation");
	adminDwrService.enableEmailValidation(emailValidation,function(data) {
	   	 if(data)
	     alert("Operation Successfull ");
	   });
}
function enableHumanValidation(){
	
	var humanValidation=dwr.util.getValue("humanValidation");
	adminDwrService.enableHumanValidation(humanValidation,function(data) {
	   	 if(data)
	     alert("Operation Successfull ");
	   });
}
function updateAdminEmail(){
	
	var adminEmail=dwr.util.getValue("adminEmail");
	adminDwrService.updateAdminEmail(adminEmail,function(data) {
	   	 if(data)
	     alert("Operation Successfull ");;
	   });
}
</script>

</title>
</head>
<body>

		    <div id="content">
		    <div class="bock-rep">
      		  <h2 class="block-title"><@s.text name="label.common.settings"/></h2>
  
      <div class="admincontent">
       <ul>
        <select id="selCombo" name="timezone" class="dropDown"/>
        <#list applicationManager.availableTimeZones as timezone>   			
   			 <option name="timezone" value="${timezone}">${timezone}</option>    			  				
 		</#list>
 		</select> 
 		<input type="button" onclick="updatTimeZone();" value="<@s.text name="label.update"/>" />
         </ul>
       
         <ul>
	        <li>  
	        	<input type="radio" id="" checked="" value="true" name="userRegistration"/><span class="JuLp9"><label for=":5t">Enable</label></span> 
	       
	          <input type="radio" id="" checked="" value="false" name="userRegistration"/><span class="JuLp9"><label for=":5t">Disable</label></span> - <span>User Registration </span>
	       
       		   <input type="button" onclick="enableUserRegistration();" value="<@s.text name="label.update"/>" />
          </li>
         </ul>
         <ul>
	        <li>  
	        	<input type="radio" id="" checked="" value="true" name="password"/><span class="JuLp9"><label for=":5t">Enable</label></span> 
	        
	          <input type="radio" id="" checked="" value="false" name="password"/><span class="JuLp9"><label for=":5t">Disable</label></span> - <span>Reset Password</span>
	      
       		   <input type="button" onclick="enableResetPassword();" value="<@s.text name="label.update"/>" />
             </li>
         </ul>
         <ul>
	        <li>  
	        	<input type="radio" id="" checked="" value="true" name="emailValidation"/><span class="JuLp9"><label for=":5t">Enable</label></span>	       
	          <input type="radio" id="" checked="" value="false" name="emailValidation"/><span class="JuLp9"><label for=":5t">Disable</label></span> - <span>Email Validation</span>
	        
       		   <input type="button" onclick="enableEmailValidation();" value="<@s.text name="label.update"/>" />
           </li>
         </ul>
         <ul>
	        <li>  
	        	<input type="radio" id="" checked="" value="true" name="humanValidation"/><span class="JuLp9"><label for=":5t">Enable</label></span> 
	        
	          <input type="radio" id="" checked="" value="false" name="humanValidation"/><span class="JuLp9"><label for=":5t">Disable</label></span> - <span>Human Validation </span>
	        
       		   <input type="button" onclick="enableHumanValidation();" value="<@s.text name="label.update"/>" />
            </li>
         </ul>
          <ul>
	        <li>  
	        	<input type="text" id="" value="" name="adminEmail"/><span class="JuLp9"><label for=":5t">Email</label></span> - <span>Adminstator Email</span>
	       
	           <input type="button" onclick="updateAdminEmail();" value="<@s.text name="label.update"/>" />
          </li>
         </ul>
        
        
    </div>
 </div>
 </div>
 	
	      
 </body>