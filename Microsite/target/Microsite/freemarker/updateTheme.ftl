
    <div id="content">

        <h2 class="block-title">Update Theme</h2>
        
        <div class="content">
        
        <div class="reg_form">
        
       <form name="user" action="<@s.url action='theme' method="update"/>" method="post" >
       
        <ul>
          <li><div id="error_div"  class="errormsg"></div></li>
           <input type="hidden" name="theme.id" value="${theme.id!?html}" />
          <li><label><@s.text name="label.theme.name"/>:</label>
        	  <input type="text" class="txt" value="${theme.name!?html}" name="theme.name" id="firstname"/></li>
           
		   <li><label><@s.text name="label.theme.description"/>:</label>
		   <input type="text" class="txt" value="${theme.description!?html}" name="theme.description" id="lastname"/></li>
	           
		    
	        <input type="submit" value="Cancel" onclick="form.onsubmit=null"  name="action:theme" id="index" class="submit_btn" />
	        <input type="submit" value="Update"  class="submit_btn"/>
          
 			</form> 
        </div>
        </div>

    </div>
