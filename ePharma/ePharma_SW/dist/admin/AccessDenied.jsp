<%
String sessionID = (String)session.getAttribute("sessionID");
String pagenm = (String)request.getAttribute("pagenm");
String tp_company_nm = (String)request.getAttribute("tp_company_nm");
if(tp_company_nm  == null) tp_company_nm = "";
%>

<TITLE>Raining Data</TITLE>
<P>&nbsp;</P>
<P>&nbsp;</P>
<P>&nbsp;</P>
<Head><META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
<style type="text/css" media="all"> @import "includes/page.css";
		@import "../includes/page.css"; @import "assets/epedigree1.css";
		@import "includes/nav.css"; @import "../includes/nav.css"; </style>
</Head>
<%@include file='../epedigree/topMenu.jsp'%>
<FORM action="DoUserLogin.jsp" method="post" target=_top>
	<TABLE id="Table1" align="center" cellSpacing="1" cellPadding="1" width="300" border="1">
		<BR>
		<BR>

		<TR>
			<TD align="center"><IMG alt="" src="IMG\RainingData_logo.jpg"></TD>
		</TR>
		<BR>
		<BR>
		<BR>
		<TR>
			<TD colspan="2" bgcolor="#ffffcc">
				<P align="center"><STRONG><FONT color="Red">Access Denied ....</FONT></STRONG></P>
			</TD>
		</TR>
		
	</TABLE>
</FORM>
<P>&nbsp;</P>
<P align="center"><FONT face="Arial" size="1"><STRONG>Powered by: Tiger<FONT color="#3300ff">Logic</FONT>
			XDMS</STRONG></FONT></P>
