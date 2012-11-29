<div class="saved_rep">
      <h2><@s.text name="recent.search"/></h2>
      <div class="content">
       <ul>
       
        <#if savedSearch?exists  >
	        <#list savedSearch as search>
	   			 <li><a href="javascript:void(0);">${search.title}<span class="date_saved">${search.modified}</span></a></li>
	 		</#list>
			<a href="<@s.url action='save.search' />" class="more_link">more</a> </div> 
 		 <#else>
 		 <@s.text name="no.save.search"/>
 		</#if>
         </ul>
        
    </div>
 