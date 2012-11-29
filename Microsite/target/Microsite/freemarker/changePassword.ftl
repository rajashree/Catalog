
		<div id="wrapper">
		    <div id="content">
		
		        <h2 class="block-title"><@s.text name="label.change.password"/></h2>
		        <div class="content">
		        <div class="reg_form">
		        <form target="_top" action="<@s.url action='change.password' />" method="post" onsubmit="return validateChangePassword();">
		        <#include "/freemarker/global/form-message.ftl" />
		        <ul>
		          <li><div id="error_div"><@macroFieldErrors name="oldPassword"/></li>
		          <li><label><@s.text name="label.old.password"/></label><input name="oldPassword" id="oldPassword" type="password" class="txt"  /></li>
		          
		          <li><label><@s.text name="label.new.password"/></label><input name="newPassword" id="newPassword" type="password" class="txt"  /></li>
		          <@macroFieldErrors name="newPassword"/>
		          <li><label>Retype</label><input name="newConformPassword" id="newConformPassword" type="password" class="txt"  /></li>
		          <@macroFieldErrors name="newConformPassword"/>
		          <@macroFieldErrors name="password.match"/>
		        </ul>
		           <input class="submit_btn" type="submit" onclick="form.onsubmit=null" value="Cancel" name="action:index" id="index"/>
	     
		       <input class="submit_btn" name="" type="submit" value="<@s.text name="global.update"/>" />
		        </div>
		        </div>
		
		    </div>
		  </div>

