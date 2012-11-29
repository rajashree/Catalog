
    <div id="content">

        <h2 class="block-title">Update Page</h2>
        
        <div class="content">
        
        <div class="reg_form">
        
       <form name="user" action="<@s.url action='site' method="update"/>" method="post"  enctype="multipart/form-data">
       
        <ul>
          <li><div id="error_div"  class="errormsg"></div></li>
          <li><label><@s.text name="label.page.name"/>:</label>
          <input type="hidden" name="site.id" value="${page.id!?html}" />
        	  <input type="text" class="txt" value="${page.name!?html}" name="site.name" id="firstname"/></li>
           
           
            <li><label><@s.text name="label.page.theme"/>:</label>
			       <select name="page.tid">
					   <#if themes?exists  >
					        <#list themes as theme >
					              <option value="${theme.id}">${theme.name}</option>
					   		</#list>
				   		</#if>
	    			</select>
	    		</li>
	    		
		   <li><label><@s.text name="label.page.description"/>:</label>
		   <input type="text" class="txt" value="${page.description!?html}" name="page.description" id="lastname"/></li>
	           
		    <li><label><@s.text name="label.page.title"/>:</label>
		   <input type="text" class="txt" value="${page.title!?html}" name="page.title" id="lastname"/></li>
	       
	         
	        <input class="submit_btn" type="submit" onclick="form.onsubmit=null" value="Cancel" name="action:site" id="index"/>
	        <input type="submit" value="Update"  class="submit_btn"/>
          
 			</form> 
        </div>
        </div>

    </div>
