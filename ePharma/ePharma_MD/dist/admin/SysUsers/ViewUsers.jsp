<%@include file='../../../includes/jspinclude.jsp'%>
<%@ page import='java.util.Properties' %>
<%@ page import='java.io.*' %>
<%@ page import='java.util.*' %>
<%@ page import='org.apache.jsp.*' %>
<%@ page import="java.net.*" %>
<%@ page import="org.xml.sax.InputSource" %>
<%@ page import="javax.xml.parsers.DocumentBuilderFactory,javax.xml.parsers.DocumentBuilder,org.w3c.dom.*" %>

<%
	
//GET PATH TO SERVER SO CAN DYNAMICALLY CREATE HREFS
String servPath = "EPCRepository/AddUser.do?exec=fetchDetails&amp;userID=";
servPath = "http://"+request.getServerName()+":"+request.getServerPort()+"/"+servPath;               
System.out.println(servPath);
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String sessionID = request.getParameter("sessionID");
String tp_company_nm = request.getParameter("tp_company_nm");

if(tp_company_nm==null) { tp_company_nm = ""; }

String redirectURL = "";
byte[] xmlResults;

com.rdta.tlapi.xql.Connection connection = ConnectTL();
com.rdta.tlapi.xql.Statement statement = getStatement(connection);

//String validateResult = ValidateUserSession(statement, sessionID, clientIP);

//if(validateResult.equals("VALID")) {  //IF USER SESSION IS VALID, CONTINUE

String xQuery = "";
String HTMLROW = "";

if(tp_company_nm.equals("")) {	//no trading partner - get all
	System.out.println("tlsp:getSysUsers()");
	xQuery = "tlsp:getSysUsers()";
} else {			//get only for trading partner
	System.out.println("XQuery by TP");
	xQuery = "for $b in collection('tig:///EAGRFID/SysUsers')/User  ";
	xQuery = xQuery + " where $b/BelongsToCompany = '"+tp_company_nm+"' order by $b/LastName return  ";
	xQuery = xQuery + " <tr class='tableRow_Off'>  ";
	xQuery = xQuery + " <td class='td-menu'><A HREF='"+servPath+"={data($b/UserID)}&amp;sessionID="+sessionID+"'>{data($b/FirstName)}</A></td> ";
	xQuery = xQuery + " <td class='td-menu'>{data($b/LastName)}</td>  ";
	xQuery = xQuery + " <td class='td-menu'>"+tp_company_nm+"</td>  ";
	xQuery = xQuery + " <td class='td-menu'>{data($b/UserRole)}</td>  ";
	xQuery = xQuery + " <td class='td-menu'>{data($b/Phone)}</td>  ";
	xQuery = xQuery + " <td class='td-menu'>{data($b/Email)}</td>  ";
	xQuery = xQuery + " </tr> ";
}

xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	HTMLROW = new String(xmlResults);
}
 
CloseConnectionTL(connection);

%>

<HTML>
<style type="text/css" media="all"> @import "../includes/nav.css"; @import "../../../assets/epedigree1.css"; 
	</style>
<TITLE>ePharma - Trade Partner Users</TITLE>
<body><BR><BR>
	<TABLE cellSpacing="1" cellPadding="1" align="center" border="1">
		
		<TR class="tableRow_Header">
			<TD align="center" colSpan="6"><STRONG><FONT>Existing Accounts</FONT></STRONG></TD>
		</TR>
		<TR class="tableRow_Header">
			<TD align="center"><STRONG>First Name</STRONG></TD>
			<TD align="center"><STRONG>Last Name</STRONG></TD>
			<TD align="center"><STRONG>Trade Partner</STRONG></TD>
			<TD align="center"><STRONG>Role</STRONG></TD>
			<TD align="center"><STRONG>Phone</STRONG></TD>
			<TD align="center"><STRONG>Email</STRONG></TD>
		</TR>
		<%=HTMLROW%>
		<TR class="tableRow_On" >
			<TD colspan=6>&nbsp;</TD>	
		</TR>
		<TR class="tableRow_Off" colspan=6>
			<TD colspan="6">
				<P align="center"><EM>Click on the name to view or edit account details...</EM></P>
			</TD>
		</TR>
		<TR class="tableRow_On">
			<TD colspan="3" align="center">
				<P align="center"><A HREF="NewUser.jsp?userid=0&sessionID=<%=sessionID%>&tp_company_nm=<%=tp_company_nm%>">New User</A></P>
			</TD>
			<TD colspan="3" align="center">
				<A HREF="" ONCLICK="history.go(-1)">Go Back</A>
			</TD>
		</TR>
		
	</TABLE>
	
	
</body>
</HTML>

<%

//} else { //ELSE MAKE THEM RE-LOGIN
//	CloseConnectionTL(connection);
//	String getCustURL = "LoginFailed.html"; 
//	response.setContentType("text/html; charset=ISO-8859-1");
//	response.setHeader("Location", getCustURL);
//	response.setStatus(303);
//}
%>