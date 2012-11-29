
<head>
<title><@s.text name="label.timezone"/> </title>
  <script type='text/javascript' src='${base}/dwr/interface/adminDwrService.js'></script>
  <script type='text/javascript' src='${base}/dwr/engine.js'></script>
 <script type='text/javascript' src='${base}/dwr/util.js'></script>

<script type='text/javascript' >
	function enableUserAccount(username,enable){
		adminDwrService.enableUserAccount(username,enable ,function(data) {
		   	 if(data)
		   		window.location.reload();
		});
		   
		
		
	}
</script>
</head>
<div id="wrapper">
	<div id="content">
		<div class="block-rep manageusers">
			<h2 class="block-title"><@s.text name="label.user.management"/></h2>
			<div class="admincontent">
				<table class="list">
			         <tbody><tr>
			          <th> Login</th><th>First name</th><th>Last name</th><th>Email</th><th>Actions</th></tr>      
			           <#if userList?exists>
				        <#list userList as user>
				   		  <tr class="odd"> <td>${user.userName}</td><td>${user.firstName}</td><td>${user.lastName}</td><td style="text-align: center;"><a href="mailto:${user.email}">${user.email}</a> </td>
			         		 <#if user.enabled.toString() == "true">
			         		 	<td style="text-align: center;"><input id="${user.userName}"  type="submit" onclick="enableUserAccount('${user.userName}','false');" value="Disable"/> </td>
			         		 <#elseif  user.enabled.toString() == "false">
			         		 	<td style="text-align: center;"><input id="${user.userName}" type="submit" onclick="enableUserAccount('${user.userName}','true');" value="Enable"/> </td>
			         		 </#if>
			         	  </tr>
				   		</#list>			
			 		
			 		</#if>                 
			    </table>
			</div>
		 </div>
   </div>
</div>

<div id="leftbar">
	<div class="actions">
		<h2><@s.text name="label.actions"/></h2>
		<div class="content">
			<ul>
				<li><a <#if tab == 1> class="active"</#if> href="<@s.url action='index' namespace='/admin'><@s.param name='tab' value='1' /><@s.param name='tabIndex' value='1' /></@s.url>"><@s.text name="label.general"/></a></li>
				<li><a <#if tab == 2> class="active"</#if> href="<@s.url action='taxonomy!input' namespace='/admin'><@s.param name='tab' value='2' /><@s.param name='tabIndex' value='1' /></@s.url>"><@s.text name="label.taxonomy.update"/></a></li>
				<li><a <#if tab == 3> class="active"</#if> href="<@s.url action='index!listUser' namespace='/admin'><@s.param name='tab' value='3' /><@s.param name='tabIndex' value='1' /></@s.url>"><@s.text name="label.user.management"/></a></li>
			</ul>
		</div>
	</div>
</div>

 		