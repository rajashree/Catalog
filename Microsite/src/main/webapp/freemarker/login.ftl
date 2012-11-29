<html>
<head>
    <title><@s.text name="login.user_login.title"/></title>
    <meta name="nosidebar" content="true" /> 
</head>
<body>


<div id="wrapper">
    <div id="content">

        <h2 class="block-title">Welcome to Sourcen MicroSiteBuilder</h2>
        <div class="content">
        <div class="msg"><@s.text name="text.login.page"/></div>
        </div>

    </div>
  </div>
  <div id="leftbar">
  <#include "/freemarker/global/form-message.ftl" />
  
   <div id="error_div"></div>
   <form action="${base}/security_check" method="post" onsubmit="return validateLogin();"/>
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
           <@macroFieldErrors name="login.error"/>
        </ul>        
         <a class="forgot" href="<@s.url action='forget.password' method="input" />"><@s.text name="label.forgot.password"/></a><input class="submit_btn" name="" type="submit" value="Login" />
		 <a class="forgot" href="<@s.url action='signup' method="input" />">Not registered, click here.</a>     

       </div>
        
    </div>
    </form>
  </div>
 

</body>
</html>