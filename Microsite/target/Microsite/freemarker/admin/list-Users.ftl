<head>
<title><@s.text name="label.timezone"/> </title>
<style type="text/css">

</style>
<script type='text/javascript' >
	function enableUserAccount(username){
				
		adminDwrService.enableUserAccount(username,false ,function(data) {
		   	 if(data)
		     alert("Operation Successfull ");
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


function enableUserAccount(username) {

              adminDwrService.enableUserAccount(username,true, {
                        callback:function() {
                              showDiv("link-user-enable-"+username);
                           hideDiv("link-user-disable-"+username)
                         
                        }
                });
            }

            function disableUserAccount(username) {
               
                 adminDwrService.enableUserAccount(username,false, {
                        callback:function() {
                           showDiv("link-user-disable-"+username);
                           hideDiv("link-user-enable-"+username);
                          

                        }
                });
            }
            </script>
</head>
<body>

    <div id="content">
    <h1 style="float: left;">User Summary</h1>
      <br clear="all"/>
	 <div class="sourcen-content-box">               
      <p>       
       Number of users : <b>${searchCount}</b>
       	Showing ${range} per page, sorted by user ID. Use the select box at the
	bottom of the page to adjust the number of users per page.
       
       <br/><br/>
       Search for users with login starting with :
       <form name="searchUser" action="<@s.url action='users'  />" method="post" />
	      
       <input type="text" value="" size="30" maxlength="200" name="username"/>
       <input type="submit"  value="Submit"/>
      </form>
      </p>
       <p>
        </p>
        <div class="table table-users">
        <table class="list" width="100%" >
        <tr>
          <th> Login</th><th>First name</th><th>Last name</th><th>Email</th><th>Actions</th></tr>      
           <#if userList?exists  >
	        <#list userList as user >
	   		  <tr class="jive-even"> <td><a href="${base}/people/${user.username}" >${user.username}</a></td><td>${user.firstName}</td><td>${user.lastName}</td><td style="text-align: center;"><a href="mailto:${user.email}">${user.email}</a> </td>
         		 <td>
		               <div  id="link-user-enable-${user.id}"  <#if !user.enabled  > style="display:none;"  </#if>>
                           <a href="javascript:void(0);" onclick="disableUserAccount('${user.id}');" title="" class="user-active">  </a>
                       </div>
                        <div id="link-user-disable-${user.id}"  <#if user.enabled  > style="display:none;"  </#if>>
                              <a href="javascript:void(0);" onclick="enableUserAccount('${user.id}');" title="" class="user-inactive">  </a>
                       </div>                     
            	</td>
         	  </tr>
	   		</#list>
 		</#if>                 
         </table>  
         </div> 
		<table width="100%" cellspacing="0" cellpadding="3" border="0">
	    <tbody><tr>
	        <td width="1%" nowrap="nowrap">            
	        </td>
	        <td width="98%"> </td>
	        <td width="1%" nowrap="" class="jive-description">
	            Number of Users per page:
	            <select size="1" onchange="location.href='<@s.url action='users'  />?start=0&range='+this.options[this.selectedIndex].value;">
	              <option <#if range == 15>selected="true" </#if> value="15">15</option >
	                <option value="30" <#if range == 30>selected="true" </#if>>30</option >
	                <option value="50" <#if range == 50>selected="true" </#if>>50</option >
	                <option <#if range == 100>selected="true" </#if> value="100">100</option>
	          </option>
	          </select>
	        </td>
	    </tr>
	</tbody></table>  
      
   </div>
   </div>
   </body>