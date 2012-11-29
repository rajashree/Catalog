 <head>
	    <title><@s.text name="label.settings"/></title>
	    <meta name="nosidebar" content="true" /> 
 </head>


<body>
<#if tab == 0>
	<@s.action id="changePassword" name="account!view"  executeResult="true" />
<#elseif tab==1>
	<@s.action id="view.account" name="change.password!input"  executeResult="true" />
 </#if> 
<div id="leftbar">
    <div class="actions">
      <h2><@s.text name="label.actions"/></h2>
      <div class="content">
        <ul>
          <li><a <#if tab == 0> class="active"  </#if>  href="<@s.url action='settings'><@s.param name='tab' value='0' /></@s.url>"><@s.text name="label.my.account"/></a></li>
          <li><a <#if tab == 1> class="active"  </#if>  href="<@s.url action='settings'><@s.param name='tab' value='1' /></@s.url>"><@s.text name="label.change.password"/></a></li>          
		    
        </ul>
       
       </div>
    </div>
  </div>
</body>