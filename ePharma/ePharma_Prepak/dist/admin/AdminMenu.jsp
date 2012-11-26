<%

String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String sessionID = (String)session.getAttribute("sessionID");
String pagenm = (String)request.getAttribute("pagenm");
String tp_company_nm = (String)request.getParameter("tp_company_nm");
System.out.println("tp_company_nm: "+tp_company_nm);
System.out.println("sessionID: "+sessionID);

%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Frameset//EN">
<html>
	<head>
		<TITLE>ePharma - Admin Console</TITLE>
	</head>
	<frameset rows="73,*" border="0">
		<frame name="search" scrolling="no" src="/ePharma/dist/admin/LoadAdminmenu.jsp?pagenm=epcadmin&tp_company_nm=<%=tp_company_nm%>" noresize="noresize">				
		<frame name="resultsFRM" scrolling="auto" src="./Main.jsp?sessionID=<%=sessionID%>" noresize="noresize">		
	</frameset>
	
</html>
