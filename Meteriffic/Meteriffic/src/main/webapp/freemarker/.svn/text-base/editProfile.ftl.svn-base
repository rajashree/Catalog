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
	        <div class="editprofile_form">
		 <div id="error_div"></div>
	        <#include "/freemarker/global/form-message.ftl" />
	        <ul>
	                    <li>
		          		<label><@s.text name="global.firstname"/><@s.text name="global.asterix"/></label>
		          		<input name="firstName" id="firstName" type="text" class="txt" style="width:200px !important;"  value="${user.firstName!?html}" />
		          		<@macroFieldErrors name="firstname"/>
				  </li>
				  
				  <li>
		          		<label><@s.text name="global.lastname"/><@s.text name="global.asterix"/></label>
		          		<input name="lastName" id="lastName" type="text" class="txt" style="width:200px !important;"  value="${user.lastName!?html}" />
					<@macroFieldErrors name="lastname"/>
		          </li>
				  
				  <li>
		          		<label><@s.text name="global.email"/><@s.text name="global.asterix"/></label>
		          		<input name="email" id="email" type="text" class="txt" style="width:200px !important;" value="${user.email!?html}" />
					<@macroFieldErrors name="email"/>
		          </li>
				  
				  <li>
		          		<label><@s.text name="global.username"/></label>
		          		<input name="username" id="username" type="text" disabled="disabled" class="txt" style="width:200px !important;"  value="${user.userName!?html}" />
					<@macroFieldErrors name="username"/>
		          </li>
		          <li/>
		         
	        </ul>
	        <input class="submit_btn" type="submit" onclick="form.onsubmit=null" value="Cancel" name="action:settings" id="settings"/>
	       	 <input class="submit_btn" name="" type="submit" value="<@s.text name="global.update"/>" />
	        </div>
	        </div>
	
	    </div>
	<div id="leftbar">
	 	<div class="actions">
	 		<h2><@s.text name="label.actions"/></h2>
	 		<div class="content">
	 			<ul>
	 				<li><a <#if tab==1 || tab == 0> class="active" </#if> href="<@s.url action='settings'><@s.param name='tab' value='1'/></@s.url>"><@s.text name="label.my.account"/></a></li>
					<li><a <#if tab==2> class="active" </#if> href="<@s.url action='settings'><@s.param name='tab' value='2'/></@s.url>"><@s.text name="label.change.password"/></a></li>
				</ul>
			</div>
		</div>
	</div>
	</div>
</body>
</html>

  

