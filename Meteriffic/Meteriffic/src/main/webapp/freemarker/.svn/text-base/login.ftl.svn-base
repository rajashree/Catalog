<html>
	<head>
		<title><@s.text name="login.user_login.title"/></title>
	</head>
	<body>
		<div id="wrapper">
			<div id="content">
				<h2 class="block-title">Welcome to Meterrific</h2>
				<div class="content">
					<div class="msg"><@s.text name="text.login.page"/></div>
				</div>
			</div>
		</div>
		
		<div id="leftbar">
			<#include "/freemarker/global/form-message.ftl"/>
			<@macroFieldErrors name="login.error"/>
			<div id="error_div" style="display:none"></div>
			<form action="${base}/security_check" method="post" onsubmit="return validateLogin();">
				<div class="login">
					<h2>Log In</h2>
					<div class="content">
						<ul>
							<li><label><@s.text name="global.username"/>:</label><input name="username" id="username" type="text" class="txt"/></li>
							<li><label><@s.text name="global.password"/>:</label><input name="password" id="password" type="password" class="txt"/></li>
							
						</ul>
						<a class="forgot" href="<@s.url action='forgot.password' method='input'/>">Forgot Password.</a><input class="submit_btn" name="" type="submit" value="Login"/>
						<a class="notregistered" href="<@s.url action='signup' method='input'/>">Register Now.</a>
					</div>
				</div>
			</form>
		</div>
	</body>
</html>