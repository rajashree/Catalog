 <div id="content">	
      <h1 style="float: left;">Email Settings</h1><br clear="all"/>
      <div class="sourcen-content-box">    
<p>
Use the form below to send a test message.
</p>

<form  name="testMailSettings" method="post" action="<@s.url action='testmail.mail.settings' />">

<table cellspacing="0" cellpadding="3" border="0">
<tbody>
    <tr>
        <td>Mail Server:</td>
        <td> ${host?default('')}</td>
    </tr>
    <tr>
        <td>From:</td>
        <td>
            <input type="text" value="${from?default('')}" name="from"/>            
            <span class="jive-description">
            (<a href="${base}/people/edit/${user.username}">Update Your Address</a>)
            </span>
        </td>
    </tr>
    <tr>
        <td>
            To:
        </td>
        <td>
            <input type="text" maxlength="100" size="40" value="${to?default('')}" name="to"/>
        </td>
    </tr>
    <tr>
        <td>
            Subject:
        </td>
        <td>
            <input type="text" maxlength="100" size="40" value="${subject?default('')}" name="subject"/>
        </td>
    </tr>
    <tr valign="top">
        <td>
            Body:
        </td>
        <td>
            <textarea wrap="virtual" rows="5" cols="45" name="body">${body?default('')}</textarea>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <br/>
            <input type="submit" value="Send Email" name="test"/>
            <input type="submit" value="Cancel/Go Back" name="cancel"/>
        </td>
    </tr>
</tbody>
</table>

</form>

                    
                </div>
                <!-- END content box -->

            </div>