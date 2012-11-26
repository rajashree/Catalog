<!-- Top Header -->
<%@ include file='../../includes/jspinclude.jsp'%>
<%
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String APNID = request.getParameter("pedid");
//String sessionID = request.getParameter("sessionID");
String sessionID = (String)session.getAttribute("sessionID");
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");

com.rdta.tlapi.xql.Connection connection = ConnectTL();
com.rdta.tlapi.xql.Statement statement = getStatement(connection);
sessionID = getSessionByClientIP(statement,clientIP);
CloseConnectionTL(connection);


%>
<div id="bg">
	<div class="roleIcon-manufacturer">&nbsp;</div>
	<div class="navIcons">
		<a href="./landing.html" target="_parent">
		<img src="./assets/images/home.gif" width="22" height="27" hspace="10" border="0"></a>
		<img src="./assets/images/account.gif" width="41" height="27" hspace="10">
		<img src="./assets/images/help.gif" width="21" height="27" hspace="10">
		<img src="./assets/images/print.gif" width="27" height="27" hspace="10">
		<img src="./assets/images/logout.gif" width="34" height="27" hspace="10">

		<a href="dist/epedigree/epedigree.do?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>" target="_top"><IMG height="27" hspace="10" src="./assets/images/inbox.gif" border="0"></a>

		<img src="./assets/images/space.gif" width="20">
	</div>
	<div class="logo"><img src="./assets/images/logos_combined.jpg"></div>
</div>