<div id="wrapper">
	<div id="content">
		<h2 class="block-title"><@s.text name="label.change.password"/></h2>
		<div class="content">
			<div class="reg_form">
		        <form target="_top" action="<@s.url action='reset.password'/>" method="post" onsubmit="return validateChangePassword();">
			        <#include "/freemarker/global/form-message.ftl" />
			        <ul>
			          <input name="keycode" id="keycode" type="hidden"  value="${keycode}"  />  
		    	      <input name="username" id="username" type="hidden"  value="${username}"  />	           
		        	  <li><label><@s.text name="label.new.password"/></label><input name="newPassword" id="newPassword" type="password" class="txt"  /><@macroFieldErrors name="newPassword"/></li>
		          	  
		          	  <li><label>Retype</label><input name="newConfirmPassword" id="newConfirmPassword" type="password" class="txt"  /> <@macroFieldErrors name="newConfirmPassword"/>
		              <@macroFieldErrors name="password.match"/></li>
		             
		        	</ul>
		        	<input class="submit_btn" name="" type="button" value="Cancel" /><input class="submit_btn" name="" type="submit" value="Save" />
		         </form>
		     </div>
		 </div>
	</div>
</div>
