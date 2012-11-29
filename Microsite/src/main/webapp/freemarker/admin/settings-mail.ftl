 <div id="content">	
  <h1 style="float: left;">
                     Email Server</h1>
                 <br clear="all"/>
 <div class="sourcen-content-box">
<div class="table">
<table width="100%" cellspacing="0" cellpadding="6" border="0">
<tbody>
<tr>
    <td> </td>
</tr>

<tr>

<td>
<p>
Use the form below to set the host and port of your email server (SMTP). At a minimum
you should set the host and optionally you can set the port, username and password and whether
or not to connect over SSL. If you have problems sending email please check the SMTP
configuration on your mail server. Note, if you choose to enable mail debugging the debug
output will be written to your appserver's standard out log.
</p>

<form method="post" name="mailSettings" action="<@s.url action='save.mail.settings' />">

<b>SMTP Settings</b>

<ul>
    <table cellspacing="0" cellpadding="3" border="0">
   <tr>
        <td width="1%" nowrap="nowrap">
            <label for="txtHost">Mail Host: </label>
        </td>
        <td width="1%" nowrap="nowrap">
            <input type="text" maxlength="150" size="40" value="${host?default('')}" name="host" id="txtHost"/>
        </td>
        <td width="98%" style="border-left: 1px solid rgb(204, 204, 204);" rowspan="7">
            <div style="padding: 3px; font-size: 8pt; font-family: verdana;">
            Changes to these fields will require an appserver restart.
            </div>
        </td>
    </tr>
    <tr>
        <td width="1%" nowrap="nowrap">
            <label for="txtPort">Server Port (Optional): </label>
        </td>
        <td width="1%" nowrap="nowrap">
            <input type="text" maxlength="15" size="10" value="${port?default('25')}" name="port" id="txtPort"/>
        </td>
    </tr>
    <tr>
        <td width="1%" nowrap="nowrap">
            Mail Debugging:
        </td>
        <td width="1%" nowrap="nowrap">
            <input type="radio" id="rb01" value="true" name="debug"/> <label for="rb01">On</label>
             
            <input type="radio" id="rb02" checked="" value="false" name="debug"/> <label for="rb02">Off</label>
              (may require appserver restart)
        </td>
    </tr>    
    <tr><td colspan="2"> </td></tr>
    <tr>
        <td width="1%" nowrap="nowrap">
            <label for="txtUsername">Server Username (Optional): </label>
        </td>
        <td width="1%" nowrap="nowrap">
            <input type="text" maxlength="150" size="40" value="${username?default('')}" name="username" id="txtUsername"/>
        </td>
    </tr>
    <tr>
        <td width="1%" nowrap="nowrap">
            <label for="txtPassword">Server Password (Optional): </label>
        </td>
        <td width="1%" nowrap="nowrap">
            <input type="password" maxlength="150" size="40" value="${password?default('')}" name="password" id="txtPassword"/>
        </td>
    </tr>
    <tr>
        <td width="1%" nowrap="nowrap">
            <label for="chkSSL">ReStart Email Manager: </label>
        </td>
        <td width="1%" nowrap="nowrap">
            <input type="checkbox" name="restart" value="true" />
        </td>
    </tr>
    <tr><td colspan="3"> </td></tr>


  </table>
</ul>

<br/>

<input type="submit" value="Save Changes" name="save"/>
<input type="submit" value="Send Test Email..." name="test" onClick="location.href='<@s.url action='test.mail.settings' />'; return false"/>

</form>



</td>
</tr>
</tbody>
</table>
</div>
</div>
</div>