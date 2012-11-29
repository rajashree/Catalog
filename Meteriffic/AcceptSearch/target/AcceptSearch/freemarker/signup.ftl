<html>
<head>

<script type='text/javascript' src='${base}/dwr/interface/userDwrService.js'></script>
  <script type='text/javascript' src='${base}/dwr/engine.js'></script>
    <script type='text/javascript' src='${base}/dwr/util.js'></script>
     <script type='text/javascript' src='${base}/js/valdiation/util.js'></script>
   
      
    <title><@s.text name="account.createNewAccount.title" /></title>
    <meta name="nosidebar" content="true" />  
    
    
<script type='text/javascript' >

function check(){


 if(dwr.util.getValue("username") == "")
  {
   alert("Enter UserName");
   return true;
   }

 userDwrService.isUserAvailable(document.user.username.value,handleGetData);

}
function handleGetData(data) {

if(data == true)
	alert(document.user.username.value+" is not Available.");
else
	alert(document.user.username.value+" is Available.");	
	
}

</script>  
</head>
<body>

  
<form name="user" action="<@s.url action='signup' method="execute"/>" onsubmit="return validateRegistration();">
<div id="wrapper">
    <div id="content">
 <#include "/freemarker/global/form-message.ftl" />
        <h2 class="block-title">User Registration</h2>
        
        <div class="content">
        
        <div class="reg_form">
        
       
        <ul>
          <li><div id="error_div"  class="errormsg"></div></li>
          <li><label><@s.text name="global.firstname"/>:</label><input type="text" class="txt" value="${firstname!?html}" name="firstname" id="firstname"/></li>
           <@macroFieldErrors name="firstname"/>
	   <li><label><@s.text name="global.lastname"/>:</label><input type="text" class="txt" value="${lastname!?html}" name="lastname" id="lastname"/></li>
            <@macroFieldErrors name="lastname"/>
	    <li><label><@s.text name="global.email"/>:</label><input type="text" class="txt" 
	    name="email" value="${email!?html}" id="email"/><span class="sd">    <#if registrationManager.emailValidationEnabled>will be subjected to verification</span></#if></li>
           <@macroFieldErrors name="email"/>	
	   <li><label><@s.text name="global.username"/>:</label><input type="text" class="txt" name="username" id="username"/><span class="sd"> <a href="#" onclick="check();">Check Availability</a></span></li>
          <@macroFieldErrors name="username"/>
	    <li><label><@s.text name="global.password"/>:</label><input type="password" class="txt" name="password" id="password"/></li>
          <li><label>Retype Password:</label><input type="password" class="txt" name="conformPassword" id="conformPassword"/></li>
          <#if registrationManager.humanValidationEnabled>
			          <li><img width="100" height="40" src="${base}/jcaptcha"><input name="keycode" id="keycode" type="text" class="txt" /></li>
			            <@macroFieldErrors name="error.keycode"/>
			           <div id="error_div"></div>
			          </li>
		     </#if>
         
      
          <textarea readonly="readonly" rows="5" cols="45" id="EULA1" name="EULA1">Software License Agreement 
          NOTICE TO USER: PLEASE READ THIS AGREEMENT CAREFULLY. BY COPYING,	INSTALLING OR USING ALL OR ANY PORTION OF THE SOFTWARE YOU ACCEPT ALLTHE TERMS AND CONDITIONS OF THIS AGREEMENT, INCLUDING, IN PARTICULARTHE LIMITATIONS ON: USE CONTAINED IN SECTION 2; TRANSFERABILITY IN	SECTION 4; WARRANTY IN SECTIONS 6 AND 7; LIABILITY IN SECTION 8; ANDSPECIFIC PROVISIONS AND EXCEPTIONS IN SECTION 14. YOU AGREE THAT THISAGREEMENT IS LIKE ANY WRITTEN NEGOTIATED AGREEMENT SIGNED BY YOU. THIS	AGREEMENT IS ENFORCEABLE AGAINST YOU AND ANY LEGAL ENTITY THAT OBTAINED THE SOFTWARE AND ON WHOSE BEHALF IT IS USED: FOR EXAMPLE, IF APPLICABLE, YOUR EMPLOYER. IF YOU DO NOT AGREE TO THE TERMS OF THIS AGREEMENT, DO NOT USE THE SOFTWARE. VISIT http://www.mysite.com FOR TERMS OF AND LIMITATIONS ON RETURNING THE SOFTWARE FOR A REFUND Do you accept the terms and conditions stated above?</textarea>
           <li class="agreement"> <input type="checkbox" checked="" value="true" name="agree" id="Positive"/><label>I agree <a href="#">Terms and conditions</a></label>
          
          </li>
        </ul>
         
        
        <input type="button" value="Reset" name="" class="submit_btn"  onClick="document.user.reset();"/><input type="submit" value="Register"  class="submit_btn"/>
        </div>
        </div>

    </div>
  </div>
 </form> 

</body>
</html>