<% 
    if(session.getAttribute("type")==null)
    {
	    response.sendRedirect("Failure.jsp");
	}
	else
	{
		if(session.getAttribute("type").equals("seller"))
	    {
	    	session.setAttribute("status", "Access Violation for Buyer");
	    	response.sendRedirect("Failure.jsp");    	
	    }
	}
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE> Welcome </TITLE>
<STYLE type=text/css>
	a { text-decoration: none }
</STYLE>
<link rel="stylesheet" href="css\Buttons.css" type="text/css">
</HEAD>
<BODY>

<br><br><br><br><br><br><br><br><br>

<TABLE cellSpacing=0 cellPadding=0 width="50%" align=center border=0 bgcolor="white" background="">
  <TR><TD vAlign=center align=right>
  	  <IMG id=IMG3 alt="" src="Images\blue_right_arrow.gif">&nbsp;
  	  <STRONG><A href="SignOut.jsp"><FONT color=navy>Sign Out
      </FONT></A> &nbsp;&nbsp; </STRONG></TD></TR>
</TABLE>

<TABLE WIDTH="50%" BORDERCOLOR=lightskyblue ALIGN=center BORDER=3 CELLSPACING=1 CELLPADDING=1>
<TR><TD vAlign=center align=middle bgcolor=midnightblue><FONT face="Bodoni MT" 
      color=lightblue size=5><STRONG> Buyer 
    Home</STRONG></FONT></TD></TR>
	<TR>
		<TD><TABLE WIDTH="100%" ALIGN=center BORDER=0 CELLSPACING=0 CELLPADDING=0>
	<TR>
		<TD align=middle valign=center><IMG id=IMG1  alt="" src="Images\Buyer.jpg"  ></TD>
		<TD align=middle valign=center>
		<TABLE WIDTH="100%" BORDERCOLOR=slategray ALIGN=center BORDER=0 CELLSPACING=0 CELLPADDING=0 
           >
	<TR>
		<TD><form id="POGeneration" name="POGeneration" action="POGeneration.jsp">
<input type="submit" value="Generate PO" class="btn" style="WIDTH: 110px; HEIGHT: 26px" size=26>
</form>
</TD>
	</TR>
	<TR>
		<TD><form id="POSubmit" name="POSubmit" action="POSubmit.jsp">
<input type="submit" value="Submit PO" class="btn" style="WIDTH: 110px; HEIGHT: 26px" size=26>
</form>
</TD>
	</TR>
</TABLE>
		
		</TD>
	</TR>
</TABLE>
</TD>
</TR>
</TABLE>
</BODY>
</HTML>