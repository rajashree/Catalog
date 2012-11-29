<script type='text/javascript' >
	function removeGroupUser( gid, uid){
				
		adminDwrService.removeGroupUser( gid, uid ,{
                        callback:function() {
                           showDiv("link-user-disable-"+uid);
                           hideDiv("link-user-enable-"+uid);
                          

                        }
		   });
				
	}
	   function addGroupUser( gid, uid) {
               
                 adminDwrService.addGroupUser( gid, uid, {
                        callback:function() {
                           showDiv("link-user-disable-"+uid);
                           hideDiv("link-user-enable-"+uid);
                          

                        }
                });
            }
function hideDiv(divId) {
    if (document.layers) {
        document.layers[divId].visibility = 'hide';
    }
    else if (document.all) {
        document.all[divId].style.visibility = 'hidden';
    }
    else if (document.getElementById) {
        document.getElementById(divId).style.display = 'none';
    }
}

function showDiv(divId) {
  
   if (document.layers) {
        document.layers[divId].display = 'block';
    }
    else if (document.all) {
        document.all[divId].style.display = 'block';
    }
    else if (document.getElementById) {
        document.getElementById(divId).style.display = 'block';
    }
}

         
            </script>
<p>Add and remove members  to this group using the form below.</p>

  <br/>


<p><b>Group Members</b></p>

<div class="table">
<table width="100%" cellspacing="0" cellpadding="3" border="0">
    <tr>
        <th> </th>
        <th>Username</th>
        <th style="text-align: center;">Action</th>
    </tr>
	<#if memberList?exists  >
	        <#list memberList as user >
        <tr>
            <td width="1%">1</td>
            <td width="79%"><a href="${base}/people/${user.username}" title="Click here view this User">${user.username}</a>
 </td>
            <td width="20%" align="center">
                  
	             
	              <div  id="link-user-enable-${user.id}" >
                               <a href="javascript:void(0);" title="Click here to remove this User" onclick="removeGroupUser('${group.id}','${user.id}');" title="" class="user-inactive">  </a>

                       </div>
                        <div id="link-user-disable-${user.id}"  style="display:none;"  >
                              <a href="javascript:void(0);" title="Click here to add this User" onclick="addGroupUser('${group.id}','${user.id}');" title="" class="user-active">  </a>

                       </div>
            </td>
        </tr>
        </#list>	
 		
 		</#if> 
 		
 		<#if nonMemberList?exists  >
	        <#list nonMemberList as user >
        <tr>
            <td width="1%">1</td>
            <td width="79%"><a href="${base}/people/${user.username}">${user.username}</a>
 </td>
            <td width="20%" align="center">
                 <div  id="link-user-disable-${user.id}"   style="display:none;">
                               <a href="javascript:void(0);" title="Click here to Remove this User" onclick="removeGroupUser('${group.id}','${user.id}');" title="" class="user-inactive">  </a>

                       </div>
                        <div id="link-user-enable-${user.id}"    >
                              <a href="javascript:void(0);" title="Click here to add this User" onclick="addGroupUser('${group.id}','${user.id}');" title="" class="user-active">  </a>

                       </div> 
            </td>
        </tr>
        </#list>	
 		
 		</#if>         

    <tr>
      
       
    </tr>
</tfoot>
</table>
</div>
 </div>
           