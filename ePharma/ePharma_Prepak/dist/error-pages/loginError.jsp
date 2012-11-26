<%@ page contentType="text/html; charset=utf-8" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<html:html locale="true">
<head>
  <html:base/>
  <title>Raining Data ePharma</title>
<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="/ePharma/assets/epedigree1.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/JavaScript">
if( window != window.top ){
  window.top.location = window.location;
 }
<!--
function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}
//-->


if( window != window.top ){
  window.top.location = window.location;
}

</script>
</head>

<body >
<%@ page isErrorPage="true" %>
<!-- Top Header -->
<div id="bg">
	<div class="roleIcon-prePack">&nbsp;</div>
  <div class="logo"><img src="/ePharma/assets/images/logos_combined.jpg"></div>
</div>
  
<!-- Left channel -->
<div id="leftgray_home">
  <table width="170" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr> 
      <td width="170" colspan="2" class="td-leftred">Welcome</td>
    </tr>
    <tr> 
      <td width="10" valign="top" bgcolor="#DCDCDC">&nbsp;</td>
      <td valign="top" class="td-left"></td>
    </tr> 
    <tr valign="bottom"> 
      <td height="80" colspan="2" class="td-left"><p>&nbsp;</p>
        <p>&nbsp;</p>
        <p>&nbsp;</p>
        <p>&nbsp;</p>
        <p>&nbsp;</p>
        <p><img src="/ePharma/assets/images/logo_poweredby.gif" width="150" height="37"></p>
      </td>
    </tr>
  </table>
</div>

<div id="rightwhite_home">
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr> 
      <td width="1%" valign="middle" class="td-rightmenu"><img src="/ePharma/assets/images/space.gif" width="10" height="10"></td>
	  
	  <!-- Messaging -->
      <td width="99%" valign="middle" class="td-rightmenu"><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td align="left"></td>
            <td align="right"></td>
			
          </tr>
        </table></td>
    </tr>
    <tr> 
      <td>&nbsp;</td>
      <form action="/ePharma/doUserLogin.do" method=post>
      <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td><img src="/ePharma/assets/images/space.gif" width="30" height="12"></td>
            <td rowspan="2">&nbsp;</td>
          </tr>
          
          <tr>	   
          <td style="font-family:arial,sans-serif; font-size:13px; color:red; font-weight:700; text-decoration:none"><html:errors/></td>
          </tr>
          
          
          <tr> 
            <td class="td-typeblack">Login</td>
            <td align="right" class="td-typegray"></td>
          </tr>
		  <tr>
		  	<td align="left" class="td-menu">User Name: <input name="uname" type="text" size="30">&nbsp;&nbsp;&nbsp;&nbsp;Password: <input name="password" type="password" size="30">&nbsp;&nbsp;&nbsp;&nbsp;<input name="submit" type="submit" class="fButton" value="Login"></td>
		  </tr>
        </table></td>
		<td>&nbsp;</td>
		</form>
    </tr>
  </table>
</div>

<div id="footer_home"> 
  <table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
    <tr> 
      <td height="24" bgcolor="D0D0FF" class="td-menu">&nbsp;&nbsp;Copyright 2005. 
        Raining Data.</td>
    </tr>
  </table>
</div>
</body>
</html:html>
