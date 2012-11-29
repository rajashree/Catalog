<html>
<head>
 
    <title><@s.text name="account.createNewAccount.title" /></title>
    <meta name="nosidebar" content="true" />  
 </head>
<body>

  
<form name="user" action="<@s.url action='signup' method="execute"/>" onsubmit="return validateRegistration();">
<div id="wrapper">
    <div id="content">
 <#include "/freemarker/global/form-message.ftl" />
        <h2 class="block-title">Mail Server Settings </h2>
        
        <div class="content">
        
        <div class="reg_form">
               
        <ul>
          <li><div id="error_div"  class="errormsg"></div></li>
          <li><label><@s.text name="label.smtp.host"/>:</label><input type="text" class="txt" value="${firstname!?html}" name="firstname" id="firstname"/></li>
           <@macroFieldErrors name="firstname"/>
	   <li><label><@s.text name="label.smtp.port"/>:</label><input type="text" class="txt" value="${lastname!?html}" name="lastname" id="lastname"/></li>
            <@macroFieldErrors name="lastname"/>
	    <li><label><@s.text name="label.smtp.user"/>:</label><input type="text" class="txt" 
	    name="email" value="${email!?html}" id="email"/><span class="sd">    
           <@macroFieldErrors name="email"/>	
	    <li><label><@s.text name="label.smtp.password"/>:</label><input type="text" class="txt" name="username" id="username"/></li>
          <@macroFieldErrors name="username"/>
	   
        </ul>
         
        
        <input type="button" value="Reset" name="" class="submit_btn"  onClick="document.user.reset();"/><input type="submit" value="Update"  class="submit_btn"/>
        </div>
        </div>

    </div>
  </div>
 </form> 

</body>
</html>