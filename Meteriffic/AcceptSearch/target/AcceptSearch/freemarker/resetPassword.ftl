<div id="wrapper">
		    <div id="content">
		
		        <h2 class="block-title"><@s.text name="label.change.password"/></h2>
		        <div class="content">
		        <div class="reg_form">
		        <form target="_top" action="<@s.url action='reset.password' />" method="post" onsubmit="return validateChangePassword();">

		        <#include "/freemarker/global/form-message.ftl" />
		        <ul>
		          <input name="keycode" id="keycode" type="hidden"  value="${keycode}"  />  
		          <input name="username" id="username" type="hidden"  value="${username}"  />	           
		          <li><label><@s.text name="label.new.password"/></label><input name="newPassword" id="newPassword" type="password" class="txt"  /></li>
		          <@macroFieldErrors name="newPassword"/>
		          <li><label>Retype</label><input name="newconformPassword" id="newconformPassword" type="password" class="txt"  /></li>
		          <@macroFieldErrors name="newConformPassword"/>
		          <@macroFieldErrors name="password.match"/>
		        </ul>
		        <input class="submit_btn" name="" type="button" value="Cancel" /><input class="submit_btn" name="" type="submit" value="Save" />
		        </div>
		        </div>
		
		    </div>
		  </div>
