<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@taglib prefix="html-el"  uri="http://struts.apache.org/tags-html-el" %>
<%@taglib prefix="logic-el"  uri="http://struts.apache.org/tags-logic-el" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css" href="assets/css/master.css" />
<script src="assets/js/feedback.js" type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Welcome - ICE Alert</title>
<script>
	
function $(id){
	return document.getElementById(id);
}
	function validateLogin(){
	var user = $("buser").value;
	if(user == ""){
        feedback.show('Please Enter Username',1);
   	   	$("buser").focus();
       	return false;
    }
    
    var pass = $("bpwd").value;
	if(pass == ""){
        feedback.show('Please Enter Password',1);
   	   	$("bpwd").focus();
       	return false;
    }
}
	
</script>
</head>
<body style="background:#FFFFFF; overflow:hidden; width:505px;">
<div class="blog_login_section">	
  <div class="blog_login_area">  	
    <div class="blog_login">
    <div style="height:20px;padding-bottom:5px;overflow:hidden;"> 
         <div class="error"><div id="feedback-content"></div></div>
     </div> 
     <form action="./BlogLogin.ice" method="post" style="overflow:hidden" onsubmit="return validateLogin();">
      <div class="un">
        <div class="user_name">
          <div class="text">User Name :</div>
        </div>
        <div class="t_box">
          <input type="text" class="text_box" id="buser" name="buser"/>
        </div>
      </div>
      <div class="pw">
        <div class="password">
          <div class="text">Password :</div>
        </div>
        <div class="t_box">
          <input type="password" class="text_box" id="bpwd" name="bpwd"/>
        </div>
      </div>
      <div class="form_login">
        <div class="button">
          <div class="rb">
            <input type="reset" value="Reset" />
          </div>
          <div class="lb">
            <input type="submit" value="Login" />
          </div>
        </div>
      </div>
      </form>
    </div>    
  </div>
</div>
<logic:present name="login">
	<logic:equal name="login" value="1">
		<script>
			feedback.show('Invalid Username/Password',1);
		</script>
	</logic:equal>
</logic:present>
</body>
</html>
