<%@include file='../includes/jspinclude.jsp'%>
<% System.out.println("Inside logout.jsp.....******************"); %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html lang="en">
<head>
	<title>Raining Data ePharma</title>
	<!-- Refresh login page every 15 minutes -->
	<META HTTP-EQUIV="Expires" CONTENT="Mon, 1 Jan 1990 00:00:00 GMT">
	

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="assets/epedigree1.css" rel="stylesheet" type="text/css">

</head>

<%
	request.getSession().invalidate();
%>
<body>
<!-- 100% Width -->
<div id="bg">
  <table width="100%" height="50" border="0" cellpadding="0" cellspacing="0">
    <tr>
      <td width="205" valign="middle"><img src="assets/images/southwood.GIF" height="50"></td>
      <td><img src="assets/images/space.gif" width="20"></td>
    
      
	<td style="font-family:arial,sans-serif; font-size:13px; color:red; font-weight:700; text-decoration:none">
	<CENTER><li>You have successfully logged out of ePharma Application </li>
	</CENTER><BR><BR></td>
	
      <td width="261" align="right" valign="top"><img src="assets/images/logo.gif" width="180" height="50"></td>
    </tr>
  </table>

 
<div id="footer_home">
  <table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
    <tr>
      <td height="24" bgcolor="D0D0FF" class="td-menu">&nbsp;&nbsp;Copyright 2005.
        Raining Data.</td>
    </tr>
  </table>
</div>


<div id="rightwhite_home">
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    
    <tr> 
      <td>&nbsp;</td>
      <form action="/ePharma/doUserLogin.do" method=post>
      <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td><img src="/ePharma/assets/images/space.gif" width="30" height="12"></td>
            <td rowspan="2">&nbsp;</td>
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
</html>
