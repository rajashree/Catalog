<div class="saved_rep">
      <h2><@s.text name="saved.search"/></h2>
      <div class="content">
       <ul>
       
        <#if savedSearch?exists  >
	        <#list savedSearch as search>
	   			 <li><a href="#">${search.title}<span class="date_saved">${search.modified}</span></a></li>
	 		</#list>
			<a href="#" class="more_link">more</a> </div> 
 		 <#else>
 		 <@s.text name="no.save.search"/>
 		</#if>
         </ul>
        
    </div>
       
       
    