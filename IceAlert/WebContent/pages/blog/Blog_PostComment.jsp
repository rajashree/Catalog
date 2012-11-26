<%@page import="com.snipl.ice.utility.GeneralUtility"%>
<%@page import="com.snipl.ice.blog.SaveBlogCommentAction"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@taglib prefix="html-el"  uri="http://struts.apache.org/tags-html-el" %>
<%@taglib prefix="logic-el"  uri="http://struts.apache.org/tags-logic-el" %>
<% 
   	String hash = GeneralUtility.getRandomString(20); 
   	String captchString = GeneralUtility.getRandomString(5);
   	SaveBlogCommentAction.addCaptcha(hash, captchString); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css" href="assets/css/master.css" />
<script src="assets/js/feedback.js" type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>ICE Alert</title>
<script>
	function $(id){
		return document.getElementById(id);
	}
	function validateComment(){
		if($("comm").value=="")
		{
			feedback.show('Please enter comments',1);
	   	    $("comm").focus();
	   	   	return false;
		}
		if($("captchaid").value=="")
		{
			feedback.show('Please enter code',1);
	   	   	$("captchaid").focus();
	   	   	return false;
		}
	}
	function errorHandle(val){
		$("comm").value=val;
		feedback.show('Please enter correct code',1);
   	   	$("captchaid").focus();
	}
</script>
</head>
<body style="background:#FFFFFF; overflow:hidden; width:505px;">
<form action="./SaveBlogComment.ice" method="post" onsubmit="return validateComment();"  style="overflow: hidden">
<div class="form_bpc">
	<div style="height:20px;padding-bottom:5px;overflow:hidden;"> 
         <div class="error"><div id="feedback-content"></div></div>
     </div> 
    <div class="send_alert_header">
      <div class="text">post comments</div>
    </div>
    <div class="row02">
      <div class="message">
        <div class="text">comments :</div>
      </div>
      <div class="t_box">
        <textarea class="text_area"  id="comm" name="comm"></textarea>
      </div>
    </div>
    <div class="row01">
      <div class="subject">
        <div class="text">enter the code :</div>
      </div>
      <div class="t_box">
        <input type="text" class="text_box" id="captchaid" name="captchaid"/>
        <input type="hidden" name="captchaIds" value="<%=hash%>" />
      </div>
      <div class="secret_code">
        <div class="img"><img  src="Captcha.ice?captchaReg=<%=hash%>" /></div>
      </div>
    </div>
    <div class="button">
      <div class="cancel">
        <input type="button" value="Cancel" onclick="document.location='./Blog_Frame.ice'"/>
      </div>
      <div class="send">
        <input type="submit" value="Post" />
      </div>
    </div>
  </div>
  </form>
  <logic:present name="comm">
	<script>
		errorHandle('<bean:write name="comm"/>');
	</script>
</logic:present>
</body>
</html>
