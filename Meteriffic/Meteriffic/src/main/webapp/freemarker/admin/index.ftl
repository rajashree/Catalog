<head>
<title><@s.text name="label.timezone"/></title>
<script type='text/javascript' src='${base}/dwr/interface/adminDwrService.js'></script>
<script type='text/javascript' src='${base}/dwr/engine.js'></script>
<script type='text/javascript' src='${base}/dwr/util.js'></script>

<script type="text/javascript">

loadDefaultValues();
function loadDefaultValues(){
	adminDwrService.isEmailValidationEnabled(function(data){
		if(data)
			dwr.util.setValue("emailValidation","true");
		else
			dwr.util.setValue("emailValidation","false");
		});
	adminDwrService.isResetPasswordEnabled(function(data){
		if(data)
			dwr.util.setValue("password","true");
		else
			dwr.util.setValue("password","false");
		});
	adminDwrService.isUserRegistrationEnabled(function(data){
		if(data)
			dwr.util.setValue("userRegistration","true");
		else
			dwr.util.setValue("userRegistration","false");
		});
	
	adminDwrService.getDefaultTimeZone(function(data){
		if(data){
			dwr.util.setValue("timezone",data);
			
		}
		
		});
	adminDwrService.getAdminEmail(function(data){
		if(data)
			dwr.util.setValue("adminEmail",data);
		});
}

function updateTimeZone(){
	var timezoneId = dwr.util.getValue("timezone");
	adminDwrService.setDefaultTimeZone(timezoneId,function(data){
		if(data)
			alert("Operation Successful");
	});
}

function enableUserRegistration(){
	var userRegistration = dwr.util.getValue("userRegistration");
	adminDwrService.enableUserRegistration(userRegistration,function(data){
		if(data)
			alert("Operation Successful");
	});
}

function enableResetPassword(){
	var password=dwr.util.getValue("password");
	adminDwrService.enableResetPassword(password,function(data){
		if(data)
			alert("Operation Successful");
	});
}

function enableEmailValidation(){
	var emailValidation = dwr.util.getValue("emailValidation");
	adminDwrService.enableEmailValidation(emailValidation,function(data){
		if(data)
			alert("Operation Successful");
	});
}

function enableHumanValidation(){
	var humanValidation=dwr.util.getValue("humanValidation");
	adminDwrService.enableHumanValidation(humanValidation,function(data){
		if(data)
			alert("Operation Successful");
		});
}

function updateAdminEmail(){
	var adminEmail=dwr.util.getValue("adminEmail");
	adminDwrService.updateAdminEmail(adminEmail,function(data){
		if(data)
			alert("Operation Successful");
		});
}
</script>
</head>

<body>
	<div id="wrapper">
		<div id="content">
			<div class="block-rep">
				<h2 class="block-title"><@s.text name="label.settings"/></h2>
					<div class="admincontent">
						<ul>
							<select id="timezone" name="timezone" class="dropDown"/>
								<#list applicationManager.availableTimeZones as timezone>
									<option name="timezone" value="${timezone}">${timezone}</option>
								</#list>
							</select>
							<input class="admin_update" type="button" onclick="updateTimeZone();" value="<@s.text name="label.update"/>"/>
						</ul>
						<ul>
							<li>
								<input type="radio" id="" checked="" value="true" name="userRegistration"/> 
								<span class="Julp9"><lable for=":5t">Enable</lable></span>
								
								<input type="radio" id="" checked="" value="false" name="userRegistration"/>
								<span class="Julp9"><lable for=":5t">Disable</lable></span> - <span>User Registration </span>
								
								<input  class="admin_update" type="button" onclick="enableUserRegistration();" value="<@s.text name="label.update"/>"/>
							</li>
						</ul>
						<ul>
							<li>
								<input type="radio" id="" checked="" value="true" name="password"/>
								<span class="JuLp9"><label for=":5t">Enable</label></span>
								
								<input type="radio" id="" checked="" value="false" name="password"/>
								<span class="JuLp9"><label for=":5t">Disable</label></span> - <span>Reset Password</span>
							
								<input  class="admin_update" type="button" onclick="enableResetPassword();" value="<@s.text name="label.update"/>"/>
							</li>
						</ul>
						<ul>
							<li>
								<input type="radio" id="" checked="" value="true" name="emailValidation"/>
								<span class="JuLp9"><label for=":5t">Enable</label></span>
							
								<input type="radio" id="" checked="" value="false" name="emailValidation"/>
								<span class="JuLp9><label for=":5t">Disable</label></span> - <span>Email Validation</span>
								
								<input  class="admin_update" type="button" onclick="enableEmailValidation();" value="<@s.text name="label.update"/>" />
							</li>
						</ul>
						<!--<ul>
							<li>
								<input type="radio" id="" checked="" value="true" name="humanValidation"/>
								<span class="JuLp9"><label for=":5t">Enable</label></span>
								
								<input type="radio" id="" checked="" value="false" name="humanValidation"/>
								<span class="JuLp9"><label for=":5t">Disable</label></span> - <span>Human Validation </span>
								
								<input  class="admin_update" type="button" onclick="enableHumanValidation();" value="<@s.text name="label.update"/>"/>
							</li>
						</ul>-->
						<ul>
							<li>
								<input type="text" id="" value="" name="adminEmail"/> 
								<span class="JuLp9"><label for=":5t">Email</label><span> - <span>Administator Email</span>
								
								<input  class="admin_update" type="button" onclick="updateAdminEmail();" value="<@s.text name="label.update"/>"/>
							</li>
						</ul>
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
	      
 </body>