 <div  class="errormsg"> 
<#if !actionErrors.empty>
   <#assign unknownErrorMessage><@s.text name='error.unknown_error.text'/></#assign>
     <#list actionErrors as actionError>
        <span >  ${actionError?default(unknownErrorMessage)?html}</span ><br/>
      </#list>  
</#if>   
</div>      
            