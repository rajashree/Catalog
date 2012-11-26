<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css" href="assets/css/master.css" />
<script src="assets/js/sms.js" type="text/javascript"></script>
<script src="assets/js/feedback.js" type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>ICE Alert</title>
</head>
<body style="background:#FFFFFF">
<div class="communities" style="overflow:hidden; width:520px;">
  <div class="header_upload_confirmation">
    <div class="header">
      <div class="text" style="width:200px;">Registration Confirmation</div>
    </div>
  </div>
  <div class="form_upload_confirmation">
    <div class="row01">
      <div class="sub_header">
        <div class="text">An E-mail and a SMS has been sent to you containing the confirmation code.Please enter the confirmation code to complete your registration.</div>
      </div>
    </div>
    <div class="row02">
    <div style="height:20px;padding-bottom:5px;overflow:hidden;"> 
		<div class="error"><div id="feedback-content"></div></div>
	</div> 
    <form id="reqfrm" method="post" action="./Register_Conform.ice" onsubmit="return validateCode(this);">
      <div class="description">
        <div class="text">Please Enter the Code :</div>
      </div>
      <div class="t_box">
        <input type="text" class="text_box" id="id_code" name="id_code"/>
      </div>
      <div class="button">
        <input type="submit" value="Submit" />
      </div>
    </div>
  </div>
</div>
<logic:present name="code">
	<script>
   		feedback.show("Please enter valid code",1);
   	</script>
</logic:present>
</body>
</html>
