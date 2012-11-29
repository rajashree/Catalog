<div id="content">

<h1 style="float: left;">Create Group </h1>              
      <br clear="all"/>               
               <!-- BEGIN content box -->
    <div class="sourcen-content-box">
 
<p>
This creates a group with no permissions, no admins, and no users. Once you create
this group, you should edit its properties.
</p>

<form name="createGroup" method="post" action="<@s.url action='create.group' />">
<div class="table">
<table width="100%" cellspacing="0" cellpadding="0" border="0">
    <tr>
        <th colspan="2"> <#include "/freemarker/global/form-message.ftl" /> </th>
        
    </tr>
    <tr>
        <td>Group Name:</td>
       
        <td>
       
              <#if editMode>
               <input type="hidden"  name="id"  value="${id?default('')}" />
              </#if>              
            <input type="text"  name="name" maxlength="50" size="30" value="${name?default('')}" /><@macroFieldErrors name="groupname"/>
            
        </td>
    </tr>
    <tr>
        <td>Description (optional):</td>
        <td>
            <textarea rows="4" cols="35" wrap="virtual" name="description" value="${description!?default('')}">${description!?default('')}</textarea>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <input class="submit_btn" type="submit" value=" <#if editMode>Update Group <#else> Create Group</#if>"/>
            <input class="submit_btn" type="submit" onclick="form.onsubmit=null" value="Cancel" name="action:groups" />
	     </td>
    </tr>
</table>
</div>

</form>

	<#if id?exists  >
	<@s.action  name="group.user.info" namespace="/admin/settings" executeResult="true" >
	<@s.param name="gid"     value="${id}"/> 	
 </@s.action>

</#if>
<script type="text/javascript" language="JavaScript">
document.createGroup.name.focus();
</script>              
</div>
</div>