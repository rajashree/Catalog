
    <div id="content">

        <h2 class="block-title">Create Block</h2>
        
        <div class="content">
        
        <div class="reg_form">
        
       <form name="user" action="<@s.url action='block' method="create"/>" method="post" >
       
        <ul>
          <li><div id="error_div"  class="errormsg"></div></li>
          <li><label><@s.text name="label.block.name"/>:</label>
        	  <input type="text" class="txt" value="${block.name!?html}" name="block.name" id="firstname"/></li>
           
		   <li><label><@s.text name="label.block.description"/>:</label>
		   <input type="text" class="txt" value="${block.description!?html}" name="block.description" id="lastname"/></li>
	         
	      
		    <li><label><@s.text name="label.block.content"/>:</label>
		    <textarea type="text" class="txt"     name="block.content" value="${block.content!?html}" id="email"></textarea>
		     <li><label><@s.text name="label.block.dynamic"/>:</label>
		    <input type="checkbox" class="txt" value="true" name="block.dynamic" id="lastname"/></li>
	       
	        <input type="button" value="Reset" name="" class="submit_btn" />
	        <input type="submit" value="Save"  class="submit_btn"/>
          
 			</form> 
        </div>
        </div>

    </div>
