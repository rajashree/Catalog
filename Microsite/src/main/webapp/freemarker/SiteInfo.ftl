<#include "/freemarker/global/form-message.ftl" />
    <div id="content">

        <h2 class="site-title">Create site</h2>
        
        <div class="content">
        
        <div class="reg_form">
        
       <form name="user" action="<@s.url action='site.create' namespace="/user" />" method="post"  enctype="multipart/form-data">
       
        <ul>
          <li><div id="error_div"  class="errormsg"></div></li>
          <li><label><@s.text name="label.site.name"/>:</label>
        	  <input type="text" class="txt" value="${site.name!?html}" name="site.name" id="firstname"/></li>
           <@macroFieldErrors name="name"/>
		   <li><label><@s.text name="label.site.description"/>:</label>
		   <input type="text" class="txt" value="${site.description!?html}" name="site.description" id="lastname"/></li>
	        <li><label><@s.text name="label.page.theme"/>:</label>
			       <select name="site.theme">
					   <#if themes?exists  >
					        <#list themes as theme >
					              <option value="${theme.name}">${theme.name}</option>
					   		</#list>
				   		</#if>
	    			</select>
	    			 <@macroFieldErrors name="theme"/>
	    	</li>
	       
		    <li><label><@s.text name="label.upload.logo"/>:</label>
		   <input type="file" class="txt" value="" name="site.logo" id="logo"/></li>
	        <input type="button" value="Reset" name="" class="submit_btn" />
	        <input type="submit" value="Next"  class="submit_btn"/>
          
 			</form> 
        </div>
        </div>

    </div>
