
    <div id="content">

        <h2 class="block-title">Create Page</h2>
        
        <div class="content">
        
        <div class="reg_form">
        
       <form name="user" action="<@s.url action='page' method="create"/>" method="post" >
       
        <ul>
          <li><div id="error_div"  class="errormsg"></div></li>
          <li><label><@s.text name="label.block.name"/>:</label>
        	  <input type="text" class="txt" value="${page.name!?html}" name="page.name" id="firstname"/></li>
           
		       
		         <li><label><@s.text name="label.page.theme"/>:</label>
			       <select name="page.tid">
					   <#if themes?exists  >
					        <#list themes as theme >
					              <option value="${theme.id}">${theme.name}</option>
					   		</#list>
				   		</#if>
	    			</select>
	    		</li>
		      <li><label><@s.text name="label.page.title"/>:</label>
		       <input type="text" class="txt" value="${page.title!?html}" name="page.title" id="lastname"/></li>
	   
		       <li><label><@s.text name="label.page.description"/>:</label>
		     <textarea type="text" class="txt"     name="page.description" value="${page.description!?html}" id="email"></textarea>
		 
		 
	        <input type="button" value="Reset" name="" class="submit_btn" />
	        <input type="submit" value="Save"  class="submit_btn"/>
          
 			</form> 
        </div>
        </div>

    </div>
