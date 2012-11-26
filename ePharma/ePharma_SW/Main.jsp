<%
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();

String sessionID = request.getParameter("sessionID");
		session.setAttribute("sessionID",sessionID);
		System.out.println("-------------->-"+session.getAttribute("sessionID"));

%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Frameset//EN">
<html>
	<head>
		<TITLE>EPCIS Administration Console</TITLE></head>
	<frameset cols="170,74%" border="0">
		<frame name="contents" src="Menu.jsp?sessionID=<%=sessionID%>"   noresize="noresize">
		<frame name="main" src="./dist/admin/Introduction.html">
		</frameset><noframes>
			<p>This page requires frames, but your browser does not support them.</p>
		</noframes>
</html>
