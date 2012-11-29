<html>
<head>
    <title><@s.text name="login.user_login.title"/></title>
    <meta name="nosidebar" content="true" /> 
       
</head>
<body>


<div id="wrapper">
    <div id="content">
		<h2 class="block-title">You are a registered member of Accept Search 360 now!</h2>
        <div class="content">
       <div class="msg">
        <p>
        Thank you !! . You have succesfully registered for Accept Search 360. For any enquries please contact us</p><p>To log in your account, please use the login form in left hand side. 
        </p>
       
        </div>
        </div>

    </div>
  </div>
  <div id="leftbar">
  <#include "/freemarker/global/form-message.ftl" />
   <@macroFieldErrors name="error"/>
   <div id="error_div"></div>
   <form action="${base}/security_check" onsubmit="return validateLogin();"/>
    <div class="login">
      <h2>Log In</h2>
      <div class="content">
        <ul>
          <li><label><@s.text name="global.username"/>:</label><input name="username" id="username" type="text" class="txt" /></li>
        
          <li><label><@s.text name="global.password"/>:</label><input name="password" id="password" type="password" class="txt" /></li>
          <li class="remember">            
            <input name="Positive" type="checkbox" value="" checked />
            <label><@s.text name="login.auto.login"/></label>
          </li>
        </ul>        
         <a class="forgot" href="<@s.url action='forget.password' method="input" />"><@s.text name="global.forget.passowrd"/></a><input class="submit_btn" name="" type="submit" value="Login" />
       </div>
    </div>
    </form>
    <br/>
  </div>
 

</body>
</html>