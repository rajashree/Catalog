<html>
<head>
    <title><@s.text name="account.createNewAccount.title" /></title>
            
</head>
<body>
	<div id="wrapper">
	    <div id="content">
			<form action="<@s.url action='account' method="edit" />" method="post" onsubmit="return validateUpdateProfile();"/>
	        <h2 class="block-title"><@s.text name="label.edit.account" /></h2>
	        <div class="content">
	        <div class="reg_form">
	        <#include "/freemarker/global/form-message.ftl" />
	        <ul>
	           <li><div id="error_div"></div></li>
	          <@macroFieldErrors name="firstname"/>
		          <li>
		          		<label><@s.text name="global.firstname"/><@s.text name="global.asterix"/></label>
		          		<input name="firstName" id="firstName" type="text" class="txt" style="width:200px !important;"  value="${user.firstName!?html}" />
		          		
				  </li>
				  <@macroFieldErrors name="lastname"/>
				  <li>
		          		<label><@s.text name="global.lastname"/><@s.text name="global.asterix"/></label>
		          		<input name="lastName" id="lastName" type="text" class="txt" style="width:200px !important;"  value="${user.lastName!?html}" />
		          </li>
				  <@macroFieldErrors name="email"/>
				  <li>
		          		<label><@s.text name="global.email"/><@s.text name="global.asterix"/></label>
		          		<input name="email" id="email" type="text" class="txt" style="width:200px !important;" value="${user.email!?html}" />
		          </li>
				  <@macroFieldErrors name="username"/>
				  <li>
		          		<label><@s.text name="global.username"/></label>
		          		<input name="username" id="username" type="text" disabled="disabled" class="txt" style="width:200px !important;"  value="${user.username!?html}" />
		          </li>
		          <li/>
		         
	        </ul>
	        <input class="submit_btn" type="submit" onclick="form.onsubmit=null" value="Cancel" name="action:settings" id="settings"/>
	       	 <input class="submit_btn" name="" type="submit" value="<@s.text name="global.update"/>" />
	        </div>
	        </div>
	
	    </div>
	</div>
</body>
</html>

  

