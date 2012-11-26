
<%
String pagenm = (String)request.getParameter("pagenm");
if(pagenm != null && pagenm.equals("")) pagenm = null;
if(pagenm == null) pagenm = (String)request.getAttribute("pagenm");
String tp_company_nm = (String)request.getParameter("tp_company_nm");
String sessionID = (String)session.getAttribute("sessionID");
System.out.println("tp_company_nm: "+tp_company_nm);
System.out.println("pagenm : "+pagenm);
request.setAttribute("pagenm",pagenm);

%>


<%@ page language="java" isErrorPage="true" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>

<html:html>
	<head>
		<title>ePharma</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<link href="/ePharma/assets/epedigree1.css" rel="stylesheet" type="text/css">
		<style type="text/css" media="all"> @import "includes/page.css";
		@import "../includes/page.css"; 
		@import "includes/nav.css"; @import "../includes/nav.css"; </style>
	</head>
	<body>

	<logic:present name="pagenm">
		 <%@include file='/dist/epedigree/topMenu.jsp'%>
	</logic:present>
	
		<table    align=center valign=middle border=0>
			<tr height="150"><td>&nbsp;</td></tr>
			<tr><td>
			<table border=1 cellspacing="0" cellpadding="5" align="center" >
								<tr><td>
								<table border=0 cellspacing="2" cellpadding="2">
								
									<logic:messagesPresent>
										<% int j=0;%>
										<html:messages id="error">
											<table width="100%" border="0" cellpadding="0" cellspacing="0" >
												<tr>												
													<%if(j == 0){ %>
														<td><bean:write name="error"/></td>
													<%} else { %>
												</tr>
											</table>
											<br>
													<% if(j==1) {%>
											<table width="100%" border="0" cellpadding="0" cellspacing="0" >
												<tr>
													<td>
														<li><font color="red"><bean:write name="error"/></font></li><br>
													<%} else if(j==3) { %>
														<li><font color="red"><bean:write name="error"/></font></li><br>
													<%} else {%>
														<li><bean:write name="error" filter="false"/></li><br>
													<%}%>
													<%}%>
													<%j++;%>
													</html:messages>
													</td>
												</tr>
											</table>
									</logic:messagesPresent>
								</table>
								</td></tr>
				</table>	
			
		</table>	
					
		</td>
		</tr>
	</body>
</html:html>
