 <html>
	 <head>
	    <title><@s.text name="label.account"/></title>
	    <meta name="nosidebar" content="true" /> 
	     
	</head>
	<body>
	 	<div id="wrapper">
		    <div id="content">
				<form target="_top" action="<@s.url action='forget.password' />" method="post"  onsubmit="return validateForgotPassword();"/>
					<#include "/freemarker/global/form-message.ftl" />
			        <h2 class="block-title"><@s.text name="label.forgot.password"/></h2>
			        <div class="content">
			        <div class="reg_form">
			        <ul>
			          <li>Submit your username in the field below to start the password recovery process.</li>
			          <li/>
			          
			          <li><label><@s.text name="global.username"/></label><input name="username" id="username" type="text" class="txt" /></li>
			           <@macroFieldErrors name="username"/>
			           <div id="error_div"></div>
			          </li>
			            <#if registrationManager.humanValidationEnabled>
			          <li><img width="100" height="40" src="${base}/jcaptcha"><input name="keycode" id="keycode" type="text" class="txt" /></li>
			            <@macroFieldErrors name="keycode"/>
			           <div id="error_div"></div>
			          </li>
			           </#if>
			        </ul>
			        <input class="submit_btn" name="" type="button" value="Cancel" /><input class="submit_btn" name="" type="submit" value="Submit" />
			        </div>
			        </div>
				</form>
		    </div>
		  </div>
	 </body>
</html>
 
  
