<head>
 
<title><@s.text name="label.timezone"/> </title>

<script type='text/javascript' >
function updatTimeZone(){
	
	var timezoneId=dwr.util.getValue("timezone");
	adminDwrService.setDefaultTimeZone(timezoneId ,function(data) {
	   	 if(data)
	     alert("Operation Successfull ");
	   });
			
}
function enableUserRegistration(){
	
	var userRegistration=dwr.util.getValue("userRegistration");
	adminDwrService.enableUserRegistration(userRegistration,function(data) {
	   	 if(data)
	     alert("Operation Successfull ");
	   });
}
function enableResetPassword(){
	
	var password=dwr.util.getValue("password");
	adminDwrService.enableResetPassword(password,function(data) {
	   	 if(data)
	     alert("Operation Successfull ");
	   });
}
function enableEmailValidation(){
	
	var emailValidation=dwr.util.getValue("emailValidation");

	adminDwrService.enableEmailValidation(emailValidation,function(data) {
	   	 if(data)
	     alert("Operation Successfull ");
	   });
}
function enableHumanValidation(){
	
	var humanValidation=dwr.util.getValue("humanValidation");
	
	adminDwrService.enableHumanValidation(humanValidation,function(data) {
	   	 if(data)
	     alert("Operation Successfull ");
	   });
}
function updateAdminEmail(){
	
	var adminEmail=dwr.util.getValue("adminEmail");
	adminDwrService.updateAdminEmail(adminEmail,function(data) {
	   	 if(data)
	     alert("Operation Successfull ");;
	   });
}
</script>


</head>
<body>
<div id="content">	
                <h1 style="float: left;">
                    Registration Settings             </h1>               
                    <br clear="all"/>               
          <div class="sourcen-content-box">        
<p>
    Manage registration settings and validating new user accounts.
</p>

<form method="post" name="registrationForm" action="/admin/settings-registration.jspa" id="registrationForm">

<h3>Allow user-created accounts</h3>
<div class="table">
<table width="100%" cellspacing="0" cellpadding="5" border="0">
<tbody>
<tr>
    <td>
        Enable or disable the ability for users to create their own accounts <br/> <br/>
        <table cellspacing="0" cellpadding="2" border="0">
            <tbody>
                <tr valign="top">
                    <td>
                        <input type="radio" id="rb7" checked="checked" value="true" name="userRegistration"/>
                    </td>
                    <td>
                        <label for="rb7">Enabled</label>
                    </td>
                </tr>
                <tr valign="top">
                    <td>
                        <input type="radio"  id="rb8" value="false" name="userRegistration"/>
                    </td>
                    <td>
                        <label for="rb8">Disabled</label>
                    </td>
                      <input type="button" onclick="enableUserRegistration();" value="<@s.text name="label.update"/>" />
		        
                </tr>
            </tbody>
        </table>
    </td>
</tr>
</tbody>
</table>
</div>
<br/>
<div id="jive-account-settings">
<h3>New Account Settings</h3>
<div class="table">
<table width="100%" cellspacing="0" cellpadding="5" border="0">
<tbody>

<tr>
    <td class="jive-label">Human Input Validation:</td>
    <td>
        <table cellspacing="0" cellpadding="2" border="0">
            <tbody>
                <tr>
                    <td>
                        <input type="radio" value="true" id="humanValidationEnabled0" name="humanValidation"/>
                    </td>
                    <td>
                        <label for="humanValidationEnabled0">Enabled</label>
                    </td>
                </tr>
                <tr>
                    <td>
                        <input type="radio" value="false" checked="checked" id="humanValidationEnabled1" name="humanValidation"/>
                    </td>
                    <td>
                        <label for="humanValidationEnabled1">Disabled</label>
                    </td>
                          <input type="button" onclick="enableHumanValidation();" value="<@s.text name="label.update"/>" />
         
                </tr>
            </tbody>
        </table>
        When enabled presents a captcha to discourage span accounts.
    </td>
</tr>
<tr>
    <td class="jive-label">
        Email Validation Settings:
    </td>
    <td>
        <table cellspacing="0" cellpadding="0" border="0">
            <tbody><tr>
                <td>
                    <input type="radio" value="true" id="validationEnabled0" name="emailValidation"/>
                </td>
                <td>
                    <label for="validationEnabled0">Enabled</label>
                </td>
            </tr>
            <tr>
                <td>
                    <input type="radio" value="false" checked="checked" id="validationEnabled1" name="emailValidation"/>
                </td>
                <td>
                    <label for="validationEnabled1">Disabled</label>
                </td>
                	   <input type="button" onclick="enableEmailValidation();" value="<@s.text name="label.update"/>" />
          	
            </tr>
        </tbody></table>
        Requires that the email account provided in registration be validated before the user is permitted to login.
        <font color="#006600">
            Edit the registration email template on the <a href="message-templates.jsp?action=edit&template=validation.email">message settings page</a>.
        </font>
    </td>
</tr>
<tr>
    <td class="jive-label">
        Terms & Conditions:
    </td>
    <td>
        <table cellspacing="0" cellpadding="2" border="0">
            <tbody>
                <tr>
                    <td colspan="2">
                        <table cellspacing="0" cellpadding="0" border="0">
                            <tbody><tr>
                                <td>
                                    <input type="radio" value="true" id="termsEnabled0" name="termsEnabled"/>
                                </td>
                                <td>
                                    <label for="termsEnabled0">Enabled</label>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <input type="radio" value="false" checked="checked" id="termsEnabled1" name="termsEnabled"/>
                                </td>
                                <td>
                                    <label for="termsEnabled1">Disabled</label>
                                </td>
                            </tr>
                        </tbody></table>
                    </td>
                </tr>
                <tr>
                    <td nowrap="nowrap">URL:</td>
                    <td>
                        <input type="text" value="" size="50" name="termsUrl"/>
                    </td>
                </tr>
            </tbody>
        </table>
        Requires that the account creator accept a set of terms and conditons when creating their account.
    </td>
</tr>
<tr>
    <td class="jive-label">
        Welcome Email Settings:
    </td>
    <td>
        <table cellspacing="0" cellpadding="0" border="0">
            <tbody><tr>
                <td>
                    <input type="radio" value="true" checked="checked" id="welcomeEnabled0" name="welcomeEmailEnabled"/>
                </td>
                <td>
                    <label for="welcomeEnabled0">Enabled</label>
                </td>
            </tr>
            <tr>
                <td>
                    <input type="radio" value="false" id="welcomeEnabled1" name="welcomeEmailEnabled"/>
                </td>
                <td>
                    <label for="welcomeEnabled1">Disabled</label>
                </td>
            </tr>
        </tbody></table>
        Sends a welcome email after account creation.
        <font color="#006600">
            Edit the welcome email template on the <a href="message-templates.jsp?action=edit&template=registration.welcome.email">message
            settings page</a>.
        </font>

    </td>
</tr>
<tr>
    <td class="jive-label">
        Reset Password:
    </td>
    <td>
        <table cellspacing="0" cellpadding="0" border="0">
            <tbody><tr>
                <td>
                    <input type="radio" value="true" id="registrationModerationEnabled0" name="password"/>
                </td>
                <td>
                    <label for="registrationModerationEnabled0">Enabled</label>
                </td>
            </tr>
            <tr>
                <td>
                    <input type="radio" value="false" checked="checked" id="registrationModerationEnabled1" name="password"/>
                </td>
                <td>
                    <label for="registrationModerationEnabled1">Disabled</label>
                </td>			 <input type="button" onclick="enableResetPassword();" value="<@s.text name="label.update"/>" />
                 
            </tr>
        </tbody></table>
        All users who create an account will need to be approved before using Clearspace.
    </td>
</tr>

        </table>
       
    </td>
</tr>

</tbody>
</table>
</div>
</div>


</form>

           </div>
                <!-- END content box -->

            </div>