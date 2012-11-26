<%  
    session.setAttribute("login",null);
	session.setAttribute("display",null);
	session.invalidate();
%>
<html><head><title>Welcome</title>
<style type="text/css">
<!--
.font1{font-family:verdana;font-size:11px;text-decoration:none}
.font2{font-family:verdana;font-size:12px;text-decoration:none}
.font3{font-family:verdana;font-size:13px;text-decoration:none}
.font4{font-family:verdana;font-size:18px;text-decoration:none}
//-->
</style>
</head>
<body leftmargin="0" topmargin="0" alink="#0066ff" bgcolor="#ffffff" link="#0066ff" marginheight="0" marginwidth="0" vlink="#0066ff">
<form>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
<tbody><tr>
<td>
<br><br>
</td>
</tr>
<tr>
<td bgcolor="#000000" height="6">
<table border="0" cellpadding="0" cellspacing="0"><tbody><tr><td height="6"></td></tr></tbody></table>
</td>
</tr>
<tr>
<td align="center" bgcolor="#ffeecc">
<br><br><br>
<table bgcolor="#ffffff" border="0" cellpadding="1" cellspacing="0" width="406">
<tbody><tr>
<td>
<table bgcolor="#ffffee" border="0" cellpadding="0" cellspacing="0" width="404">
<tbody><tr>
<td align="center">
<table border="0" cellpadding="0" cellspacing="0"><tbody><tr><td height="9"></td></tr></tbody></table>

<font class="font4">You have been logged out.</font><br><br>
<font class="font3">
Your session is invalid.<br><br>
<a href="Login.jsp"><b>Please login again</b></a><br><br>
</font>
</td>
</tr>
</tbody></table>
</td>
</tr>
</tbody></table>
<br><br><br><br><br>
</td>
</tr>
<tr>
<td class="font1" align="center" height="35">
</td>
</tr>
</tbody></table>
</form>

</body></html>