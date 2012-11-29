
    <div id="content">

        <h2 class="block-title">Create Theme</h2>
        
        <div class="content">
        
        <div class="reg_form">
        
       <form name="user" action="<@s.url action='theme' method="create"/>"  enctype="multipart/form-data" method="post" >
       
        <ul>
          <li><div id="error_div"  class="errormsg"></div></li>
          <li><label><@s.text name="label.theme.name"/>:</label>
        	  <input type="text" class="txt" value="${theme.name!?html}" name="theme.name" id="firstname"/></li>
           
		   <li><label><@s.text name="label.theme.description"/>:</label>
		   <input type="text" class="txt" value="${theme.description!?html}" name="theme.description" id="lastname"/></li>
	           
		   <li><label><@s.text name="label.upload.css.image.zip"/>:</label>
		   <input type="file" class="txt" value="" name="zipFile" id="logo"/></li>
	     
	        <input type="button" value="Reset" name="" class="submit_btn" />
	        <input type="submit" value="Save"  class="submit_btn"/>
          
 			</form> 
        </div>
        </div>

    </div>
