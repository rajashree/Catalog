 <head>
	    <title><@s.text name="label.settings"/></title>
	    <meta name="nosidebar" content="true" /> 
 </head>


<body>
<#if actionIndex == 0>
	<@s.action id="changePassword" name="account!view"  executeResult="true" />
 </#if> 
<div id="leftbar">
    <div class="actions">
      <h2><@s.text name="label.actions"/></h2>
      <div class="content">
        <ul>
          <li><a <#if actionIndex == 0> class="active"  </#if>  href="<@s.url action='settings'></@s.url>"><@s.text name="label.my.account"/></a></li>
          <li><a <#if actionIndex == 1> class="active"  </#if>  href="<@s.url action='change.password!input' ></@s.url>"><@s.text name="label.change.password"/></a></li>          
		    
        </ul>
       
       </div>
    </div>
  </div>
</body>