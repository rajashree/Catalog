<%
String sessionID = request.getParameter("sessionID");
String tp_company_nm = request.getParameter("tp_company_nm");
String pagenm = request.getParameter("pagenm");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Frameset//EN">
<html>
	<head>
		<TITLE>ePharma - Extranet Admin Console</TITLE>
		<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">
	</head>
	<frameset rows="78,*">
		<frame name="search" scrolling="no" src="exmenu.jsp?sessionID=<%=sessionID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>">
		<frame name="resultsFRM" scrolling="auto" src="SysUsers/ViewUsers.jsp?sessionID=<%=sessionID%>&tp_company_nm=<%=tp_company_nm%>">
	</frameset>
	<noframes>
		<p>This page requires frames, but your browser does not support them.</p>
	</noframes>
	</frameset>
	<noframes>
		<p>This page requires frames, but your browser does not support them.</p>
	</noframes>
</html>
