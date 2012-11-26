<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.catalog.Constants" %>
<%@ page import="com.rdta.epharma.epedigree.action.EnvelopeForm"%>
<%@ include file='../../includes/jspinclude.jsp'%>



<%
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");
String sessionID = (String)session.getAttribute("sessionID");
System.out.println("session Id in view message :"+sessionID);
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String flag = "";
String docId = "";
String MessageID = request.getParameter("MessageID");
System.out.println("message ID :"+MessageID);


%>


<html>
	<head>
		<title>Raining Data ePharma - ePedigree</title>
		<LINK href="../../assets/epedigree1.css" type="text/css" rel="stylesheet"></head>
	<body>
		
		<%@include file='topMenu.jsp'%>
	
		
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
			<td width="1%" valign="middle" class="td-rightmenu">
			<img src="../../assets/images/space.gif" width="10" height="10"></td>
					<!-- Messaging -->
					<td width="99%" valign="middle" class="td-rightmenu"><table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td align="left">
								</td>
								<td align="right"><img src="../../assets/images/space.gif" width="5"></td>
							</tr>
						</table>
					</td>
				</tr>
			<table border="0" cellspacing="0" cellpadding="0" width="80%" ID="Table1" align="center">
			<tr>
			
			<tr>
			
				<td></td>
				<td rowspan="2">&nbsp;</td>
			</tr>
			<tr>
			
				<td>
					<!-- info goes here -->
					<table width="100%" id="Table2" cellSpacing="1" cellPadding="1" align="left" border="0"
						bgcolor="white">

						<tr bgcolor="white">
							<td class="td-typeblack">Alert Message Details :</td>
						</tr>
						<tr>
						<html:form  action="GenerateAlert.do" method="post">
							<Td colspan="2"><br>
							
								<table border="0" cellpadding="0" cellspacing="1" width="100%" ID="Table9">
								</table>
								<TABLE id="Table10" cellSpacing="1" cellPadding="0" width="100%" border="0">
									<TR class="tableRow_Header">
										<TD class="type-whrite" height="13"></TD>
										<TD class="type-whrite" height="13"></TD>
									</TR>
									
									
									<logic:equal name="MsgDetails" value="0">
								            <tr> <td>No records</td></tr>
									</logic:equal>
									     
									<logic:notEqual name="MsgDetails" value="0">
									<logic:iterate name="<%=Constants.MESSAGE_DETAILS%>" id="env" type="com.rdta.epharma.epedigree.action.ViewMessageForm">
									
									
									<TR class="tableRow_On">
										<TD class="td-menu" width="200" height="30"><STRONG>Created By:</STRONG></a></TD>
										<TD class="td-menu"><FONT color="#009900"><bean:write name="env" property="createdBy"/></FONT></TD>
										
									</TR>
																													
									<TR class="tableRow_On">
										<TD class="td-menu" width="200" height="30"><STRONG>Message Title:</STRONG></a></TD>
										<TD class="td-menu"><bean:write name="env" property="messageTitle"/></TD>
									</TR>
									
									<TR class="tableRow_On">
										<TD class="td-menu" width="200" height="30"><STRONG>Required Action:</STRONG></TD>
										<TD class="td-menu"><bean:write name="env" property="requiredAction"/></TD>
									</TR>
									
									<TR class="tableRow_On">
										<TD class="td-menu" width="80" height="30"><STRONG>Comments:</STRONG></TD>
										<TD class="td-menu"><bean:write name="env" property="comments"/></TD>
									</TR>
									<TR class="tableRow_On">
										<TD class="td-menu" width="80" height="30"><STRONG>Priority Level:</STRONG></TD>
										<TD class="td-menu"><bean:write name="env" property="priorityLevel"/></TD>
									</TR>
									<% flag = env.getFlag();
									   docId = env.getDocId();
									   System.out.println("The flag value is: "+flag);
   									   System.out.println("The Doc Id  value is: "+docId);
										%>
										
									</logic:iterate>	
					 				</logic:notEqual>		
					 								
									<BR><BR>
									</TABLE><br><br>
									<% System.out.println("Flag value in view message jsp :"+flag); 
										if(flag.equals("EnvLevel")){
									%> 
									<center>
									<input type="button" name="button1" value="View Document" onClick="window.location='envelopeDetails.do?envelopeId=<%=docId%>&pagenm=pedigree&tp_company_nm=&linkName=recvngmang'" />
									<% }else{ %>
									<center><input type="button" name="button1" value="View Document" onClick="window.location='PedigreeDetails.do?PedigreeId=<%=docId%>&pagenm=pedigree&tp_company_nm=&linkname=ShipSum'" />
									<%}%>
									</center>
									</html:form>
							</Td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<div id="footer">
							<table width="100%" height="24" border="0" cellpadding="0" cellspacing="0" ID="Table8">
								<tr>
									<td width="100%" height="24" bgcolor="#d0d0ff" class="td-menu" align="left">&nbsp;&nbsp;Copyright 
										2005. Raining Data.</td>
								</tr>
							</table>
						</div>																				
	</body>
<html>