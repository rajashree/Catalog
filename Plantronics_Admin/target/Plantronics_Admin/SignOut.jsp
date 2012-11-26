<%  
    session.setAttribute("login",null);
	session.setAttribute("display",null);
	session.invalidate();
%>
<HTML>
<HEAD><title>Welcome</title></HEAD>
<BODY>

<br><br><br><br><br><br><br><br><br>
<TABLE WIDTH="50%" BORDERCOLOR=navyblue ALIGN=center BORDER=3 CELLSPACING=1 CELLPADDING=1>
	<TR>
		<TD align=left valign=center >
			<form>
			<table width="100%">
				<tr><td><font color="navy">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>You have been logged out </b></td></tr>	
				<tr><td><font color="navy">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><a href="Login.jsp"><b>Please login again</b></td></tr>	
			</table>
			</form>		
		</TD>
	</TR>
			
</TABLE>
</BODY>
</HTML>
