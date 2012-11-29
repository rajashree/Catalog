<#if !actionMessages.empty>
        <#list actionMessages as success>
         <body>
		   <div class="content">
			<div class="reg_form">
				<div  class="successmsg"> <span >${success?html}</span> </div>        
			</div>
		   </div>
		 </body>    
        </#list>
</#if>