<#assign seq = ["html header","header", "banner", "Left Bar", "Right Bar","Footer","html footer"]>
 

<div id="content">

        <h2 class="block-title">Create Page</h2>
        
        <div class="content">
		    <form name="user" action="<@s.url action='page.block' />" method="post" >
    
		 <li><label><@s.text name="label.pages"/>:</label>
					       <select name="pid">
							   <#if pages?exists  >
							        <#list pages as page >
							              <option value="${page.id}">${page.name}</option>
							   		</#list>
						   		</#if>
			    			</select>
				</li>
				
				 <li><label><@s.text name="label.blocks"/>:</label>
					       <select name="bid">
							   <#if blocks?exists  >
							        <#list blocks as block >
							              <option value="${block.id}">${block.name}</option>
							   		</#list>
						   		</#if>
			    			</select>
			    			 <select name="pos">							  
							       <#list seq as x>
									            <option value="${x_index }">  ${x}</option>
							 		</#list> 						   		
			    			</select>
				</li>
			<input type="button" value="Cancel" name="" class="submit_btn" />
	        <input type="submit" value="Save"  class="submit_btn"/>
          
		</div>
</div>