<#if !actionErrors.empty>
	<#assign unknownErrorMessage><@s.text name='error.unknow_error.text'/></#assign>
	<div  id="error_div" class="error_div"><#list actionErrors as actionError>
		${actionError?default(unknownErrorMessage)?html}
	</#list></div>
<#elseif !actionMessages.empty>
	<#assign successMessage><@s.text name='global.success'/></#assign>
	<div  id="error_div" class="error_div"><#list actionMessages as actionMessage>
		${actionMessage?default(successMessage)?html}
	</#list></div>
</#if>

<#macro macroFieldErrors name=''>
	<#if !fieldErrors.empty && fieldErrors[name]?exists>
		<div id="error_div" class="error_div"><#list fieldErrors[name] as error>
			<div class="errormsg"><span>${error?html}</span></div>
		</#list></div>
	</#if>
</#macro>
