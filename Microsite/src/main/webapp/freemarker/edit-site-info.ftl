
    <div id="content">

        <h2 class="block-title"><#if page?exists  >Update--${page.title?default('')}</#if> </h2>
        
        <div class="content">
        
        <div class="reg_form">
        
       <form name="user" action="<@s.url action='site.edit' />" method="post"  enctype="multipart/form-data">
       
        <ul>
          <li><div id="error_div"  class="errormsg"></div></li>
          <li><label><@s.text name="label.site.name"/>:</label>
          <input type="hidden" name="site.id" value="${site.id!?html}" />
        	  <input type="text" class="txt" value="${site.name!?html}" name="site.name" id="firstname"/></li>
           
		   <li><label><@s.text name="label.site.description"/>:</label>
		   <input type="text" class="txt" value="${site.description!?html}" name="site.description" id="lastname"/></li>
	           
		 
	       <li><label><@s.text name="label.site.theme"/>:</label>
		   <input type="text" class="txt" value="${site.theme!?html}" name="site.theme" id="lastname"/></li>
	       
	       <li><label><@s.text name="label.site.username"/>:</label>
		   <input type="text" class="txt" value="${site.username!?html}" name="site.username" id="lastname"/></li>
	      
	      
	       <li><label><@s.text name="label.existing.logo"/>:</label>
		   <img src="/microsite/sites/${site.id!?html}/logo.jpg" /></li>
	      
	      
	       <li><label><@s.text name="label.change.logo"/>:</label>
		   <input type="file" class="txt" value="" name="site.logo" id="logo"/></li>
	      
	       
	        <input class="submit_btn" type="submit" onclick="form.onsubmit=null" value="Cancel" name="action:site" id="index"/>
	        <input type="submit" value="Update"  class="submit_btn"/>
          
 			</form> 
        </div>
        </div>

    </div>
