<html>
<head>

     
    <title><@s.text name="account.createNewAccount.title" /></title>
    <meta name="nosidebar" content="true" />  
    
    
<script type='text/javascript' >

</script>  
</head>
<body>

  
<form name="user" method="post" enctype="multipart/form-data" action="<@s.url action='taxonomy' />" >
<div id="wrapper">
    <div id="content">
 <#include "/freemarker/global/form-message.ftl" />
        <h2 class="block-title">Taxonomy (Product/Feature) Update</h2>
        
        <div class="content">
        
        <div class="reg_form">
        
       
        <ul>
             <li><div id="error_div"  class="errormsg"></div></li>
        
       	 	<li>
		         <ul id="check" class="sec1"> 
					    <li class="label-group">Metric</li>	         
							          <li>
							            <label>
							            <input type="radio"  name="product" value="false" checked="checked" id="RadioGroup1_0"/>
							            Feature Taxonomy</label>
							          </li>
							          <li>
							            <label>	  <input type="radio" name="product" value="true" id="RadioGroup1_1"/>
							            Product Taxonomy</label>
					    </li>
				</ul>
			</li>
            <li><label><@s.text name="Upload Taxonomy File(XML)"/>:</label>
           			<input type="file""  name="upload" />
           	</li>
           
	    
        </ul>
         
        
       <input type="submit" value="Update"  class="submit_btn"/>
        </div>
        </div>

    </div>
  </div>
 </form> 

</body>
</html>