<%
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String sessionID = (String)session.getAttribute("sessionID");
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");
System.out.println("In AdmMenu sessionID: "+sessionID);

//added
%>
<html>
	<head>
		<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">
	</head>
<%@include file='../epedigree/topMenu.jsp'%>
</html>