<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%
	session.setAttribute("security_prev","1");	
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css" href="assets/css/master.css" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="assets/js/feedback.js" type="text/javascript"></script>
<script src="assets/js/community.js" type="text/javascript"></script>
<title>ICE Alert</title>
</head>
<body style="background:#FFFFFF">
<div class="communities" style="overflow:hidden; width:515px;">
  <div class="form_add_com">
  <div class="error"><div id="feedback-content"></div></div>
  <html:form styleId="d" action="/AddCommunityDB" method="post" enctype="multipart/form-data" onsubmit="return validateAddComm(this);" style="overflow:hidden">		          	
  <div class="header_ac">
    <div class="text">add community</div>
  </div>
    <div class="row01">
      <div class="name">
        <div class="text">name :</div>
      </div>
      <div class="t_box">
      	<html:text property="community_name" styleClass="text_box" value=""/>
      </div>
    </div>
    <div class="row02">
      <div class="description">
        <div class="text">description :</div>
      </div>
      <div class="t_box">
        <html:textarea property="desciption" styleClass="text_area"></html:textarea>
      </div>
    </div>
    <div class="row03">
      <div class="image">
        <div class="text">image :</div>
      </div>
      <div class="browse">
        <html:file property="file" accept="*.jpg,*.gif,*.jpeg,*.bmp,*.png" onkeyup="myfunction()"/>
      </div>
    </div>
    <div class="row04">
      <div class="button">
        <div class="cancel"><input type="button" value="Cancel" /></div>
        <div class="add"><input type="submit" value="Add" /></div>
      </div>
    </div>
    </html:form>
  </div>
</div>
</body>
</html>