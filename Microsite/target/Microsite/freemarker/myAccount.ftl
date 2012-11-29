
		 <div id="wrapper">
		    <div id="content">
				<form action="<@s.url action='account' method="input"/>">
		        <h2 class="block-title">Account Settings</h2>
		        <div class="content">
		        <div class="reg_form">
		        <#include "/freemarker/global/form-message.ftl" />
		        
		        <ul>
		          <@macroFieldErrors name="firstname"/>
		          <li>
		          		<label><@s.text name="global.firstname"/><@s.text name="global.asterix"/></label>
		          		<input name="" type="text" class="txt" style="width:200px !important;"  disabled="disabled" value="${user.firstName!?html}" />
		          		
				  </li>
				  <@macroFieldErrors name="lastname"/>
				  <li>
		          		<label><@s.text name="global.lastname"/><@s.text name="global.asterix"/></label>
		          		<input name="" type="text" class="txt" disabled="disabled" style="width:200px !important;" value="${user.lastName!?html}" />
		          </li>
				  <@macroFieldErrors name="email"/>
				  <li>
		          		<label><@s.text name="global.email"/><@s.text name="global.asterix"/></label>
		          		<input name="" type="text" class="txt" style="width:200px !important;"  disabled="disabled" value="${user.email!?html}" />
		          </li>
				  <@macroFieldErrors name="username"/>
				  <li>
		          		<label><@s.text name="global.username"/><@s.text name="global.asterix"/></label>
		          		<input name="" type="text" class="txt" style="width:200px !important;"  disabled="disabled" value="${user.username!?html}" />
		          </li>
		          <li/>
		           
		        </ul>
		           <input class="submit_btn" type="submit" onclick="form.onsubmit=null" value="Cancel" name="action:index" id="index"/>
	     
		        <input class="submit_btn" name="" type="submit" value="<@s.text name="global.edit"/>" />
		        </div>
		        </div>
		
		    </div>
		  </div>
