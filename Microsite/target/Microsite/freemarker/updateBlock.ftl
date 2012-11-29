
    <div id="content">

        <h2 class="block-title">Create Block</h2>
        
        <div class="content">
        
        <div class="reg_form">
        
       <form name="user" action="<@s.url action='block' method="update"/>" method="get" >
       
        <ul>
          <li><div id="error_div"  class="errormsg"></div></li>
          <li><label><@s.text name="label.block.name"/>:</label>
          <input type="hidden" name="block.id" value="${block.id!?html}" />
        	  <input type="text" class="txt" value="${block.name!?html}" name="block.name" id="firstname"/></li>
           
		   <li><label><@s.text name="label.block.description"/>:</label>
		   <input type="text" class="txt" value="${block.description!?html}" name="block.description" id="lastname"/></li>
	           
		    <li><label><@s.text name="label.block.content"/>:</label>
		    <textarea type="text" class="txt"  rows="10"   name="block.content" value="${block.content!?html}" id="email">${block.content!?html}</textarea>
		     <li><label><@s.text name="label.block.dynamic"/>:</label>
		    <input type="checkbox" class="txt" ${block.dynamic?string("checked=checked", "")} value="true" name="block.dynamic" id="lastname"/></li>
	      
	        <input class="submit_btn" type="submit" onclick="form.onsubmit=null" value="Cancel" name="action:block" id="index"/>
	        <input type="submit" value="Update"  class="submit_btn"/>
          
 			</form> 
        </div>
        </div>

    </div>
