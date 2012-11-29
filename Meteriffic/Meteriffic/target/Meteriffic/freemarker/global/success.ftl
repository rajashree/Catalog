<#if !actionMessages.empty>
        <#list actionMessages as success>
         <body>
		   <div class="content">
		 	<div class="success_page">
				<div  class="successmsg"> <span >${success}</span> </div>        
			</div>
		   </div>
		 </body>    
        </#list>
</#if>