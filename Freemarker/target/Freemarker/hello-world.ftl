<#ftl/>
<html>
Welcome ${user!"Anonymous"}!
<br/>
<#if user??>
<h1>Welcome ${user}!</h1>
    <#else>
    Welcome Guest!
</#if>
<br/>
</html>