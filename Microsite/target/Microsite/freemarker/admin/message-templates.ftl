 <div id="content">	
<div id="jive-body-maincol">

                <h1 style="float: left;">
                    Message Templates </h1>
      <br clear="all"/>
          <div class="sourcen-content-box">
               
<p>
  <div class="table table-message-templates">

        <table width="100%" cellspacing="0" cellpadding="0" border="0">
        <thead>
        <tr>
                <th>Name</th>
                <th>Description</th>
                <th>Versions</th>
                <th>Actions</th>
        </tr>
            <#if templateList?exists  >
	        <#list templateList as template >
	  
        <tr>
            <td nowrap="nowrap" style="border-right: 1px solid rgb(204, 204, 204);">
               ${template.name} 
            </td>
            <td width="50%" style="border-right: 1px solid rgb(204, 204, 204);">
                ${template.description} 
            </td>
            <td width="15%" nowrap="" style="border-right: 1px solid rgb(204, 204, 204);">
                
                
                    English
                
            </td>
            <td nowrap="" align="center" style="padding-left: 2px; padding-right: 2px;">
                <a href="<@s.url action='edit.email.templates' ></@s.url>?tid=${template.tid} "><img border="0" alt="Edit Email Template" src="${base}/theme/default/css/images/edit-button.gif"/></a>
                  
            </td>
        </tr>  
      	</#list>
 		</#if> 
        </table>

    </div>
              </div>
 

            </div>
            </div>