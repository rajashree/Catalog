<#if !fieldErrors.empty>
            <@s.text name="error.field_error.text"/>     
<#elseif !actionErrors.empty>
            <#assign unknownErrorMessage><@s.text name='error.unknown_error.text'/></#assign>
            <#list actionErrors as actionError>
                ${actionError?default(unknownErrorMessage)?html}
            </#list>       
<#elseif !actionMessages.empty>
   
            <#assign successMessage><@s.text name='global.success'/></#assign>
            <#list actionMessages as actionMessage>
                ${actionMessage?default(successMessage)?html}
            </#list>      
</#if>

<#macro macroFieldErrors name=''>
    <#if !fieldErrors.empty && fieldErrors[name]?exists>
        <#list fieldErrors[name] as error>
        <div  class="errormsg"> <span >${error?html}</span> </div>            
        </#list>
    </#if>
</#macro>

