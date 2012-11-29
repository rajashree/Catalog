<html>
<head>
  <title><@s.text name="account.createNewAccount.title" /></title>
  <meta name="nosidebar" content="true" />  
</head>
<body>
 
    <div id="content">
     <div class="sourcen-content-box">
   
 <#include "/freemarker/global/form-message.ftl" />
        <h2 class="block-title">User Registration</h2>
        
              
        <div class="reg_form">       
         <form name="user" action="<@s.url action='create.user' />" onsubmit="return validateRegistration();">
 
        <ul>          
          <li><label><@s.text name="global.firstname"/>:</label>
          <input type="text" class="txt" value="${firstname!?html}" name="firstName" id="firstname"/><@macroFieldErrors name="firstname"/>
          </li>           
	      <li><label><@s.text name="global.lastname"/>:</label>
	       <input type="text" class="txt" value="${lastname!?html}" name="lastName" id="lastname"/>
            <@macroFieldErrors name="lastname"/>
           </li>
	      <li><label><@s.text name="global.email"/>:</label><input type="text" class="txt" 
	    name="email" value="${email!?html}" id="email"/><span class="sd"><input type="checkbox" checked="" value="true" name="notifyUser" id="Positive"/>Notify User
        <@macroFieldErrors name="email"/> </li>           	
		<li><label><@s.text name="global.username"/>:</label>
		  <input type="text" class="txt" name="username" id="username"/><@macroFieldErrors name="username"/></li>
          
	    <li><label><@s.text name="global.password"/>:</label>
	    <input type="password" class="txt" name="password" id="password"/></li>
          <li><label>Retype Password:</label>
          <input type="password" class="txt" name="conformPassword" id="conformPassword"/></li>
     
          </ul>         
        
        <input class="submit_btn" type="button" value="Reset" name="" class="submit_btn"  onClick="document.user.reset();"/>
        <input class="submit_btn" type="submit" value="Register"  class="submit_btn"/>
        <input class="submit_btn" type="submit" onclick="form.onsubmit=null" value="Cancel" name="action:users" />
	  
        
 </form>
    
    
    </div>
    
  </div>
  </div>
 

</body>
</html>