<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css" href="assets/css/master.css" />
<script src="assets/js/feedback.js" type="text/javascript"></script>
<script src="assets/js/community.js" type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Welcome - ICE Alert</title>
</head>
<body style="background:#FFFFFF">
<html:form action="fetchContactsAction"  method="post" onsubmit="return validateLogin();">
<div class="communities" style="overflow:hidden; width:515px;">
	<div style="height:18px;padding-bottom:0px;overflow:hidden;"> 
       <div class="error"><div id="feedback-content"></div></div>
    </div> 
	<div class="form_selecting_the_provider">
		<div class="header_stp">
			<div class="text">Import Email's</div>
		</div>
		<div class="cntnt">
		<div class="paragraph">
			Import contacts from your email address book. ICE Alert will not store your email login or password.
		</div>
	</div> 
	<div class="row01">
    	<div class="name">
        	<div class="text">email ID :</div>
		</div>
		<div class="t_box">
      		<html:text styleClass="text_box" styleId ="id_uname" property="id_uname" value=""/> @
		</div>
		<div class="t_box">
        	<html:select styleId="mail_providers" property="mail_providers" styleClass="combo_box">
	    		<html:option value="00">Select</html:option>
				<html:option value="01">GMail</html:option>
	        </html:select>
      </div>
    </div> 
	    <div class="row01">
	      <div class="name">
	        <div class="text">password :</div>
	      </div>
	      <div class="t_box">
	      	<html:password styleClass="password" styleId ="id_passwd" property="id_passwd" value=""/>
	      </div>
	    </div>
	    <!-- REGISTER -->
	     <div class="row02">
			<div class="button">
		        <div class="get_friends">
		        	<html:submit>Get Friends</html:submit>
		        </div>
        		<div class="cancel">
        			<html:button property="Cancel" onclick="backToInvite();">Cancel</html:button>
		        </div>
    		</div>
		</div>
  </div>
</div>
<%if(request.getParameter("prev_contact")!=null){%>
	<input type="hidden" id="prev_contact" name="prev_contact" value="<%=request.getParameter("prev_contact")%>">
<%}%>
<%if(request.getParameter("prev_contact1")!=null){%>
	<input type="hidden" id="prev_contact" name="prev_contact" value="<%=request.getParameter("prev_contact1")%>">
<%}%>
</html:form>
<logic:present name="loginFailed">
	<script>
		feedback.show("Login Failed",1);
	</script>
</logic:present>
<div style="display:none">
	<form id="backToInviteForm" action="./backToInviteAction.ice">
		<input type="hidden" id="backToInvite" name="backToInvite"/>
		<input type="submit"/>
	</form>
	<form id="backToHomeForm" action="./Home_Frame.ice">
		<input type="hidden" id="backToHome" name="backToHome"/>
		<input type="submit"/>
	</form>
</div>
</body>
</html>
