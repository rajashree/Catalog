<%@include file='../../includes/jspinclude.jsp'%>
<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.catalog.Constants" %>
<%@ page import="com.rdta.epharma.epedigree.action.EpedigreeForm"%>


<%

String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
//String sessionID = request.getParameter("sessionID");
String sessionID = (String)session.getAttribute("sessionID");
String pagenm = request.getParameter("pagenm");
String accessLevel = (String)request.getAttribute("status");

String xmlResultString = "";
String redirURL = "LoginFailed.html";
String sessQuery = "";
String tp_company_nm = "";
String tp_company_id = "";
String ALERTLINES = "";
String xQuery  = "";
String userName = "";
System.out.println("session id in index.jsp: "+sessionID);
if(pagenm == null) { pagenm = "pedigree"; }

//session.setAttribute("sessionID",sessionID);

com.rdta.tlapi.xql.Connection connection = ConnectTL();
com.rdta.tlapi.xql.Statement statement = getStatement(connection);

//CHECK SESSION IF IT IS A TRADE PARTNER
//IF IS - <div class="roleIcon-distributor"></div> CHANGE TO <div class="roleIcon-extranet"></div>
sessQuery = "for $b in collection('tig:///EAGRFID/SysSessions') ";
sessQuery = sessQuery + "where  $b/session/sessionid = '"+sessionID+"' ";
sessQuery = sessQuery + "return data($b/session/tp_company_nm)";
byte[] xmlResults = ReadTL(statement, sessQuery);
if(xmlResults != null) {
	tp_company_nm = new String(xmlResults);	
	sessQuery = "for $b in collection('tig:///EAGRFID/SysSessions') ";
	sessQuery = sessQuery + "where  $b/session/sessionid = '"+sessionID+"' ";
	sessQuery = sessQuery + "return data($b/session/tp_company_id)";
	xmlResults = ReadTL(statement, sessQuery);
	if(xmlResults != null) {
		tp_company_id = new String(xmlResults);	
	}
}
			
String query =" for $x in collection('tig:///EAGRFID/SysSessions')/session ";
	   query = query + " where $x/sessionid = '"+sessionID+"' ";
	   query = query + " return data($x/username) ";
	xmlResults = ReadTL(statement, query);
	if(xmlResults != null) {
	userName = new String(xmlResults);
	}

CloseConnectionTL(connection);
					


%>


<html:html>
	<head>
		<title>Raining Data ePharma - ePedigree</title>
		<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">
<script language="javascript">
<!--
function checkFormValues(frm)
{
 var allchecks = document.getElementsByName('check');
 var checkSel =false;
 
for(i=0;i<allchecks.length;i++){
   if( allchecks[i].checked ){
     checkSel=true;break;
    }
 }
if( checkSel == false ){
  alert("Please Select The Message! "); 
  return false;
 }
 return true;
}
-->
</script>


		
	</head>
	<body>
		<%@include file='topMenu.jsp'%>
		<% List list = (List)request.getAttribute("List"); %>
		
		
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
				
				<form action="Resolve.do" method="post" onsubmit="return checkFormValues(this);">
				
				<html:hidden property='Status' value='' /> 
				<html:hidden property='buttonname' value='' />
						
					<td width="1%" valign="middle" class="td-rightmenu"><img src="../../assets/images/space.gif" width="10" height="10"></td>
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
				
				<tr>
					<td>&nbsp;</td>
					<td><table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
							<tr>
								<td>
									<!-- info goes here -->
									<table width="100%" id="Table1" cellSpacing="1" cellPadding="1" align="center" border="0"
										bgcolor="white">
										<tr bgcolor="white">
											<td class="td-typeblack" colspan="3"><!-- Dashboard Start -->
												<table width="100%" id="Table2" cellSpacing="1" cellPadding="1" align="left" border="0"
													bgcolor="white">
													<tr bgcolor="white">
														<td class="td-typeblack" colspan="3">Inbox Message For: <font color="red" ><%=userName%></font></td>
													</tr>
													<tr>
														<td align="left">
															<!-- Dashboard Start -->
															<table align="center" border="0" cellpadding="0" cellspacing="0" width="770" ID="Table3">
																<tr>
																	<!-- Begin charts -->
																	<td width="120" valign="top" align="center">
																		<TABLE id="Table8" cellSpacing="0" cellPadding="0" width="676" align="center" border="0">
																			<TR>
																				<TD align="left" vAlign="top" width="255" bgColor="#a29fcb" colSpan="3" height="18"><STRONG><FONT color="#ffffff">INBOX</FONT></STRONG></TD>
																			</TR>
																			<TR>
																				<TD align="center" width="1" bgColor="#a29fcb" ><IMG height="1" alt="" src="../../assets/images/EEImages/blank.gif" width="1" align="top"
																						border="0"></TD>
																				<TD align="center" >
																					<TABLE align="center" id="Table9" cellSpacing="0" cellPadding="0" width="100%" border="0">
																						<TR>
																							<TD vAlign="top" bgColor="#dfdeed" height="16" align="center" width="8"><STRONG><U></U></STRONG></TD>
																							<TD vAlign="top" bgColor="#dfdeed" height="16" align="center" colspan="2" width="250"><STRONG><U>MESSAGE</U></STRONG></TD>
																							<TD vAlign="top" bgColor="#dfdeed" height="16" align="center" width="40"><STRONG><U>Date</U></STRONG></TD>
																							<TD vAlign="top" bgColor="#dfdeed" height="16" align="center" width="30"><STRONG><U>Time</U></STRONG></TD>
																							<TD vAlign="top" bgColor="#dfdeed" height="16" align="center" width="90"><STRONG><U>Status</U></STRONG></TD>
																							<TD vAlign="top" bgColor="#dfdeed" height="16" align="center" width="55"><STRONG><U>Priority</U></STRONG></TD>
																							<TD vAlign="top" bgColor="#dfdeed" height="16" align="center"><STRONG><U>Created By</U></STRONG></TD>
																						</TR>
																						<TR>
																
																<% if (list.size() == 0) { 
																	System.out.println("*******Inside no Messages**********");
																%>
																	<TR class='tableRow_Off'><TD class='td-content' colspan='9' align='center'>No Messages...</TD></TR>
																<% } else { 
																
																							
																%>
																
																<logic:iterate name="<%=Constants.ALERT_MSG_DETAILS%>" id="msg" type= "com.rdta.epharma.epedigree.action.EpedigreeForm">
																						
																
																<TR><TR>
																<TD vAlign='top' width='50%' bgColor='#dfdeed' colSpan='8' height='0'>
																<TR><TD vAlign='top' width='50%' bgColor='#dfdeed' colSpan='8' height='0'>
																	
																	</TD></TR>
																<TR><TD vAlign='middle' width='3%' height='25'><input type='checkbox' name='check' value=<bean:write property = "messageID" name ="msg"/> /></TD>																						
																	<TD vAlign='middle' width='40%' height='25'>
																		<A href="ViewMessage.do?pagenm=pedigree&tp_company_nm=&MessageID=<bean:write property = "messageID" name ="msg"/> "><bean:write property = "messageTitle" name ="msg"/></A></TD>
																	<TD width='1%' bgColor='#dfdeed' height='25'> 
																		<IMG height='1' src='../../assets/images/EEImages/blank.gif' width='1' align='top' border='0'/></TD> 
																	<TD vAlign='middle' width='15%' height='25'>
																	 		<% String dateAndTime = msg.getCreatedDate(); 
																 			String date[] = dateAndTime.split("T");
																 			System.out.println("Date in index.jsp: "+date[0]);
																	 		%>
																	 		<%=date[0]%>
																	 	</TD>
																	 	<TD vAlign='middle' width='10%' height='25'>
																	 		<%=date[1]%>
																	 	</TD>
																	 	
																	<TD vAlign='middle' width='16%' height='25' align='center'> 
																	<select name='StatusList<bean:write property = "messageID" name ="msg"/>'>
																		<option><bean:write property = "status" name ="msg"/></option>
																		
																		<% 
																			String str = msg.getStatus();
																			System.out.println("status values in index.jsp  "+str);
																			String sta[] = {"Accepted","Rejected","Verified","Resolved"};
																		   	for(int i=0; i<sta.length;i++){
																		   		if(!sta[i].equalsIgnoreCase(str)){
																		%>
																		
																		<option><%=sta[i]%></option>
																		
																		<% } } %>
																	</select></TD>
																	
																	<TD vAlign='middle' width='10%' height='25' align='center'>
																	
																		<bean:write property = "severityLevel" name ="msg"/>
																	
																	</TD>
																	
																	<TD vAlign='middle' width='15%' height='25' align='center'>
																	
																	 	<bean:write property = "createdBy" name ="msg"/>
																	 	
																	</TD> </TR></TR>
																	</logic:iterate>
																	</TR>
																	<% } %>	
																						
																						
																						<TR>
																							<TD vAlign="top" bgColor="#a29fcb" colSpan="8" height="1"><IMG height="1" alt="" src="../../assets/images/EEImages/blank.gif" width="1" align="top"
																									border="0"></TD>
																						</TR>
																					</TABLE>
																					
																				</TD>
																				<TD width="1" bgColor="#a29fcb" ><IMG height="1" alt="" src="../../assets/images/EEImages/blank.gif" width="1" align="top"
																						border="0">
																				</TD>
																			
																			</TR>
																			<TABLE id="Table10" cellSpacing="0" cellPadding="0" width="100%" border="0">
																					<TR>
																					<% String status = request.getParameter("Status");
																						String check = request.getParameter("check");
																					System.out.println("message status value in index.jsp: "+status+"    "+check); 
																					%>
																						<TD vAlign="top" height="16" align="center" width="45"><html:submit property="submit" value="Resolve" /></TD>
																						<TD vAlign="top"  height="16" align="center" width="60"><html:submit property="submit" value="Change Status" /></TD>
																					</TR>
																				</TABLE>
																			
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<DIV></DIV>
						<div id="footer">
							<table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td width="100%" height="24" bgcolor="#d0d0ff" class="td-menu" align="left">&nbsp;&nbsp;Copyright 
										2005. Raining Data.</td>
								</tr>
							</table>
						</div>
					</td> </form>
				</tr>
				
			</table>
		</div>
	</body>
</html:html>
