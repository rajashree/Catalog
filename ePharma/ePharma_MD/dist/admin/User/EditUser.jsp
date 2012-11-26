<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<%@include file='../../../includes/jspinclude.jsp'%>
<%@ page import='java.util.Properties' %>
<%@ page import='java.io.*' %>
<%@ page import='java.util.*' %>
<%@ page import='org.apache.jsp.*' %>
<%@ page import="java.net.*" %>
<%@ page import="org.xml.sax.InputSource" %>
<%@ page import="javax.xml.parsers.DocumentBuilderFactory,javax.xml.parsers.DocumentBuilder,org.w3c.dom.*" %>
<%@ page language="java" import="com.rdta.Admin.servlet.RepConstants" %>

<%	
//GET PATH TO SERVER SO CAN DYNAMICALLY CREATE HREFS
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String sessionID = request.getParameter("sessionID");
String HTMLROW = (String)request.getAttribute("HTMLROW");
String redirectURL = "";
byte[] xmlResults;
String ses = (String)session.getAttribute("sessionID");
%>
<html:html>
<script language="JavaScript">
		<!--
			function AddNew() {
				var action="<%= RepConstants.ACCESS_INSERT %>";
				frm = document.forms[0];				
				frm.action = "<html:rewrite  action='AddUser.do?exec=AddNewUser&sessionID=<%=ses%>'/>"+"&action="+action;	
				frm.submit();
			}		

		//-->
</script>
<head>
<style type="text/css" media="all"> @import "includes/page.css";
	@import "../includes/page.css"; @import "assets/epedigree1.css";
	@import "includes/nav.css"; @import "../includes/nav.css"; </style>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>User Group</title>
</head>
<body>
<html:form action="/AddUser" method="post">
	<bean:size id="totRecords" name="<%=RepConstants.USER_FORMS_KEY %>" />
	<input type="hidden" name="totRecords" value="<%=totRecords %>">	
	<BR><BR>
	<TABLE cellSpacing="1" cellPadding="1" align="center" border="1">
	    
		<TR class="tableRow_Header">
			<TD align="center" colSpan="7"><STRONG>Edit Users</STRONG></TD>
		</TR>
		<TR bgcolor="D3E5ED">
			<TD align="center"><STRONG>First Name</STRONG></TD>
			<TD align="center"><STRONG>Last Name</STRONG></TD>
			<TD align="center"><STRONG>Company</STRONG></TD>
			<TD align="center"><STRONG>Role</STRONG></TD>
			<TD align="center"><STRONG>Phone</STRONG></TD>
			<TD align="center"><STRONG>Email</STRONG></TD>
			<TD align="center"><STRONG>View / Edit</STRONG></TD>
		</TR>		
		<logic:equal name="totRecords" value="0">
		<tr> 
			<td colspan="7" align="left"> 
				There are no Records to Edit.											
			</td>
		</tr> 					
		</logic:equal> 
		<logic:notEqual name="totRecords" value="0">
			<% int i = 0; %>	
			<logic:iterate name ="<%=RepConstants.USER_FORMS_KEY %>" id="user">
				<tr class="<%= i % 2 == 0 ? "tableRow_Off" : "tableRow_On" %>">	
					<td>
						<bean:write name="user" property="firstName" />
					</td>		
					<td>
						<bean:write name="user" property="lastName" />
					</td>	
					<td>
						<bean:write name="user" property="belongsToCompany" />
					</td>		
					<td>
						<bean:write name="user" property="userRole" />
					</td>	
					<td>
						<bean:write name="user" property="phone" />
					</td>
					<td>
						<bean:write name="user" property="email" />
					</td>
					<td>
						<a href="AddUser.do?exec=fetchDetails&userID=<bean:write name="user" property="userID"/>"><Strong>View / Edit</Strong></a>
					</td>
				</tr>
			<% i++; %>
			</logic:iterate>
		</logic:notEqual> 
		 <logic:notEqual name="totRecords" value="0">
			<TR >
			<TD colspan="7">
				<P align="center"><EM><FONT color="#000099">Click on the View/Edit to edit it...</FONT></EM></P>
			</TD>
			</TR>
		</logic:notEqual>		
	    <TR class="tableRow_Off">
		<TD align="center" colspan=7><html:submit  property="SubmitType" value="New"  onclick="AddNew();"/> 	
		</TD>		
	
			
		</TR>		
	</TABLE>		
</html:form>
</body>
</html:html>

<%

//} else { //ELSE MAKE THEM RE-LOGIN
//	CloseConnectionTL(connection);
//	String getCustURL = "LoginFailed.html"; 
//	response.setContentType("text/html; charset=ISO-8859-1");
//	response.setHeader("Location", getCustURL);
//	response.setStatus(303);
//}
%>