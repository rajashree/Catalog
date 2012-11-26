<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE> Welcome </TITLE>
<SCRIPT>
	function validate()
	{
		username=login.username.value;
		if (username=='')
		{
			alert("Enter UserName");
			document.login.username.focus();
			return false;
		}
		
		password=login.password.value;
		if (password=='')
		{
			alert("Enter Password");
			document.login.password.focus();
			return false;
		}		
	}
</SCRIPT>
</HEAD>
<BODY>
<%
	int status = Integer.parseInt(request.getParameter("status"));	
	if(status==1)
	{
		out.println("onLoad='document.login.username.focus()'>");
		out.println("<div style='position:absolute;left:425;top:130;'><b><font color=midnightblue size=3>Invalid Username</font></b></div>");
	}
	else
	{
		out.println("onLoad='document.login.password.focus()'>");
		out.println("<div style='position:absolute;left:425;top:130;'><b><font color=midnightblue size=3>Invalid Password</font></b></div>");
	}
%>


<br><br><br><br><br><br><br><br><br>
<TABLE WIDTH="50%" BORDERCOLOR=lightskyblue ALIGN=center BORDER=3 CELLSPACING=1 CELLPADDING=1>
<TR><TD vAlign=center align=middle bgcolor=midnightblue><FONT face="Bodoni MT" 
      color=white size=5><STRONG>Admin Login</STRONG></FONT></TD></TR>
	<TR>
		<TD><TABLE WIDTH="100%" ALIGN=center BORDER=0 CELLSPACING=0 CELLPADDING=0>
	<TR>
		<TD align=middle valign=center>
			<form id="login" name="login" action="./ControlServlet" method=post onsubmit="return validate(this);">
			<input type="hidden" value="Login" id="controller" name="controller">			
			<table width="100%" align=right>
			<tr><td><font color="navy"><b>User Name</b></font></td><td><input name='username' id='username' width="20" 
			<%
				if(status==2)
				{
					String name = request.getParameter("name");
					out.println("value='"+name+"'");
				}
			%>
			></td></tr>
			<tr><td><font color="navy"><b>Password</b></font></td><td><input type=password name="password"  id="password" width="20"></td></tr>
			
			<tr><td colspan=2 align=middle><input type=submit value="Login" id="login" name="login"></td></tr>
			</table>
			</form>		
		</TD>
	</TR>
</TABLE>
</TD>
</TR>
</TABLE>
</BODY>
</HTML>
