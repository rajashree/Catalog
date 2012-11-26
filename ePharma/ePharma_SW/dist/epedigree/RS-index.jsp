<%@include file='../../includes/jspinclude.jsp'%>

<%
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String sessionID = request.getParameter("sessionID");
String pagenm = request.getParameter("pagenm");

String xmlResultString = "";
String redirURL = "LoginFailed.html";
String sessQuery = "";
String tp_company_nm = "";
String tp_company_id = "";

if(pagenm == null) { pagenm = "pedigree"; }

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

CloseConnectionTL(connection);

%>

<html>
	<head>
		<title>Raining Data ePharma - ePedigree</title>
		<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">
	</head>
	<body>
		<%@include file='topMenu.jsp'%>
		
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="1%" valign="middle" class="td-rightmenu"><img src="../../assets/images/space.gif" width="10" height="10"></td>
					<!-- Messaging -->
					<td width="99%" valign="middle" class="td-rightmenu"><table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td align="left">
								</td>
								<td align="right"><!-- <img src="../../assets/images/3320.jpg" width="200" height="27"> --><img src="../../assets/images/space.gif" width="5"></td>
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
														<td class="td-typeblack" colspan="3">User's Name Goes Here</td>
													</tr>
													<tr>
														<td align="left">
															<!-- Dashboard Start -->
															<table align="center" border="0" cellpadding="0" cellspacing="0" width="770" ID="Table3">
																<tr>
																	<!-- Begin charts -->
																	<td width="120" valign="top" align="center">
																		<TABLE id="Table8" cellSpacing="0" cellPadding="0" width="676" align="center" border="0"
																			height="401">
																			<TR>
																				<TD align="left" vAlign="top" width="255" bgColor="#a29fcb" colSpan="3" height="18"><STRONG><FONT color="#ffffff">INBOX</FONT></STRONG></TD>
																			</TR>
																			<TR>
																				<TD align="center" width="1" bgColor="#a29fcb" height="238"><IMG height="1" alt="" src="../../assets/images/EEImages/blank.gif" width="1" align="top"
																						border="0"></TD>
																				<TD align="center" height="238">
																					<TABLE align="center" id="Table9" cellSpacing="0" cellPadding="0" width="100%" border="0">
																						<TR>
																							<TD vAlign="top" bgColor="#dfdeed" height="16" align="center" colspan="2" width="286"><STRONG><U>MESSAGE</U></STRONG></TD>
																							<TD vAlign="top" bgColor="#dfdeed" height="16" align="center" width="25"><STRONG><U>Date</U></STRONG></TD>
																							<TD vAlign="top" bgColor="#dfdeed" height="16" align="center" width="26"><STRONG><U>Status</U></STRONG></TD>
																							<TD vAlign="top" bgColor="#dfdeed" height="16" align="center" width="64"><STRONG><U>Priority</U></STRONG></TD>
																							<TD vAlign="top" bgColor="#dfdeed" height="16" align="center"><STRONG><U>Created By</U></STRONG></TD>
																						</TR>
																						<TR>
																							<TD vAlign="top" width="255" bgColor="#dfdeed" colSpan="6" height="16" align="right"><STRONG><FONT color="#ff0066" size="2">Receiving 
																										Management</FONT></STRONG></TD>
																						</TR>
																						<TR>
																							<TD vAlign="top" width="248" bgColor="#dfdeed" colSpan="6" height="1"><IMG height="1" alt="" src="../../assets/images/EEImages/blank.gif" width="1" align="top"
																									border="0"></TD>
																						</TR>
																						<TR>
																							<TD class="menu" width="283" height="25">&nbsp; <A href="InboxAlertDetail.html">Pedigree 
																									Product Threshold Verification Exception</A>
																							</TD>
																							<TD width="2" bgColor="#dfdeed" height="25"><IMG height="1" alt="" src="../../assets/images/EEImages/blank.gif" width="1" align="top"
																									border="0"></TD>
																							<TD class="kpis" vAlign="middle" width="25" height="25"><FONT color="#0000ff">2005/8/17</FONT></TD>
																							<TD width="26" height="25">Pending</TD>
																							<TD width="64" height="25" align="left"><IMG height="19" alt="" src="../../assets/images/EEImages/dboard/alerts_icon_on.gif"
																									width="24" align="top" border="0"></TD>
																							<TD width="16" height="25">System</TD>
																						</TR>
																						<TR>
																							<TD vAlign="top" width="248" bgColor="#dfdeed" colSpan="6" height="2"><IMG height="1" alt="" src="../../assets/images/EEImages/blank.gif" width="1" align="top"
																									border="0"></TD>
																						</TR>
																						<TR>
																							<TD class="menu" width="283" height="25">&nbsp; <A href="InboxAlertDetail.html">No 
																									corresponding ship notification found</A>
																							</TD>
																							<TD width="2" bgColor="#dfdeed" height="25" align="center"><IMG height="1" alt="" src="../../assets/images/EEImages/blank.gif" width="1" align="top"
																									border="0"></TD>
																							<TD class="kpis" vAlign="middle" width="25" height="25"><FONT color="#0000ff">2005/8/17</FONT></TD>
																							<TD width="26" height="25">Pending</TD>
																							<TD width="64" height="25" align="left"><IMG height="19" alt="" src="../../assets/images/EEImages/dboard/alerts_icon_on.gif"
																									width="24" align="top" border="0"></TD>
																							<TD width="16" height="25">System</TD>
																						</TR>
																						<TR>
																							<TD vAlign="top" width="253" bgColor="#dfdeed" colSpan="6" height="2"><IMG height="1" alt="" src="../../assets/images/EEImages/blank.gif" width="1" align="top"
																									border="0"></TD>
																						</TR>
																						<TR>
																							<TD class="menu" vAlign="top" width="283">&nbsp; <A href="InboxAlertDetail.html">Possible 
																									Counterfit Product</A>
																							</TD>
																							<TD width="2" bgColor="#dfdeed" height="16"><IMG height="1" alt="" src="../../assets/images/EEImages/blank.gif" width="1" align="top"
																									border="0"></TD>
																							<TD class="kpis" vAlign="middle" width="25"><FONT color="#0000ff">2005/8/17</FONT></TD>
																							<TD width="26">Pending</TD>
																							<TD width="64" align="left"><IMG height="16" alt="" src="../../assets/images/EEImages/dboard/alerts_icon_on.gif"
																									width="24" align="top" border="0"><IMG height="16" alt="" src="../../assets/images/EEImages/dboard/alerts_icon_on.gif"
																									width="24" align="top" border="0"></TD>
																							<TD width="16">System</TD>
																						</TR>
																						<TR>
																							<TD vAlign="top" width="253" bgColor="#a29fcb" colSpan="6" height="1"><IMG height="1" alt="" src="../../assets/images/EEImages/blank.gif" width="1" align="top"
																									border="0"></TD>
																						</TR>
																						<TR>
																							<TD vAlign="top" width="255" bgColor="#dfdeed" colSpan="6" height="16" align="right"><FONT color="#ff0066" size="2"><STRONG>Shipping 
																										Manager</STRONG></FONT></TD>
																						</TR>
																						<TR>
																							<TD vAlign="top" width="248" bgColor="#dfdeed" colSpan="6" height="1"><IMG height="1" alt="" src="../../assets/images/EEImages/blank.gif" width="1" align="top"
																									border="0"></TD>
																						</TR>
																						<TR>
																							<TD class="menu" vAlign="top" width="283">&nbsp; <A href="InboxAlertDetail.html">% RFID 
																									Tag Failure</A>
																							</TD>
																							<TD width="2" bgColor="#dfdeed" height="16"><IMG height="1" alt="" src="../../assets/images/EEImages/blank.gif" width="1" align="top"
																									border="0"></TD>
																							<TD class="kpis" vAlign="middle" width="25"><FONT color="#0000ff">2005/8/17</FONT></TD>
																							<TD width="26">Pending</TD>
																							<TD width="64"></TD>
																							<TD width="16">System</TD>
																						</TR>
																						<TR>
																							<TD vAlign="top" width="248" bgColor="#dfdeed" colSpan="6" height="1"><IMG height="1" alt="" src="../../assets/images/EEImages/blank.gif" width="1" align="top"
																									border="0"></TD>
																						</TR>
																						<TR>
																							<TD class="menu" vAlign="top" width="283">&nbsp; <A href="InboxAlertDetail.html">% of 
																									Pedigree Verification Failures</A>
																							</TD>
																							<TD width="2" bgColor="#dfdeed" height="16"><IMG height="1" alt="" src="../../assets/images/EEImages/blank.gif" width="1" align="top"
																									border="0"></TD>
																							<TD class="kpis" vAlign="middle" width="25"><FONT color="#0000ff">2005/8/17</FONT></TD>
																							<TD width="26">Pending</TD>
																							<TD width="64"></TD>
																							<TD width="16">System</TD>
																						</TR>
																						<TR>
																							<TD vAlign="top" width="248" bgColor="#dfdeed" colSpan="6" height="1"><IMG height="1" alt="" src="../../assets/images/EEImages/blank.gif" width="1" align="top"
																									border="0"></TD>
																						</TR>
																						<TR>
																							<TD vAlign="top" width="253" bgColor="#a29fcb" colSpan="6" height="1"><IMG height="1" alt="" src="../../assets/images/EEImages/blank.gif" width="1" align="top"
																									border="0"></TD>
																						</TR>
																						<TR>
																							<TD vAlign="top" width="255" bgColor="#dfdeed" colSpan="6" height="16" align="right"><STRONG><FONT color="#ff0066" size="2">Packaging</FONT></STRONG></TD>
																						</TR>
																						<TR>
																							<TD vAlign="top" width="248" bgColor="#dfdeed" colSpan="6" height="1"><IMG height="1" alt="" src="../../assets/images/EEImages/blank.gif" width="1" align="top"
																									border="0"></TD>
																						</TR>
																						<TR>
																							<TD class="menu" vAlign="top" width="283">&nbsp;<A href="InboxAlertDetail.html">Commission 
																									Failure Rate/Day</A>
																							</TD>
																							<TD width="2" bgColor="#dfdeed" height="16"><IMG height="1" alt="" src="../../assets/images/EEImages/blank.gif" width="1" align="top"
																									border="0"></TD>
																							<TD class="kpis" vAlign="middle" width="25"><FONT color="#0000ff">2005/8/17</FONT></TD>
																							<TD width="26">Pending</TD>
																							<TD width="64"></TD>
																							<TD width="16">System</TD>
																						</TR>
																						<TR>
																							<TD vAlign="top" width="248" bgColor="#dfdeed" colSpan="6" height="1"><IMG height="1" alt="" src="../../assets/images/EEImages/blank.gif" width="1" align="top"
																									border="0"></TD>
																						</TR>
																						<TR>
																							<TD vAlign="top" width="255" bgColor="#dfdeed" colSpan="6" height="16" align="right"><STRONG><FONT color="#ff0066" size="2">Fulfillment</FONT></STRONG></TD>
																						</TR>
																						<TR>
																							<TD vAlign="top" width="248" bgColor="#dfdeed" colSpan="6" height="1"><IMG height="1" alt="" src="../../assets/images/EEImages/blank.gif" width="1" align="top"
																									border="0"></TD>
																						</TR>
																						<TR>
																							<TD class="menu" vAlign="top" width="283">&nbsp; <A href="InboxAlertDetail.html">Order 
																									Fill Rate</A>
																							</TD>
																							<TD width="2" bgColor="#dfdeed" height="16"><IMG height="1" alt="" src="../../assets/images/EEImages/blank.gif" width="1" align="top"
																									border="0"></TD>
																							<TD class="kpis" vAlign="middle" width="25"><FONT color="#0000ff">2005/8/17</FONT><A href="javascript:openWindow('KPIs/KPI23.html','','yes','yes',255,450,100,100)"></A></TD>
																							<TD width="26">Pending</TD>
																							<TD width="64"></TD>
																							<TD width="16">System</TD>
																						</TR>
																						<TR>
																							<TD vAlign="top" width="248" bgColor="#dfdeed" colSpan="6" height="1"><IMG height="1" alt="" src="../../assets/images/EEImages/blank.gif" width="1" align="top"
																									border="0"></TD>
																						</TR>
																						<TR>
																							<TD class="menu" vAlign="top" width="283">&nbsp; <A href="InboxAlertDetail.html">On 
																									Time Shipment</A>
																							</TD>
																							<TD width="2" bgColor="#dfdeed" height="16"><IMG height="1" alt="" src="../../assets/images/EEImages/blank.gif" width="1" align="top"
																									border="0"></TD>
																							<TD class="kpis" vAlign="middle" width="25"><FONT color="#0000ff">2005/8/17</FONT><A href="javascript:openWindow('KPIs/KPI24.html','','yes','yes',255,450,100,100)"></A></TD>
																							<TD width="26">Pending</TD>
																							<TD width="64"></TD>
																							<TD width="16">System</TD>
																						</TR>
																						<TR>
																							<TD vAlign="top" width="253" bgColor="#a29fcb" colSpan="6" height="1"><IMG height="1" alt="" src="../../assets/images/EEImages/blank.gif" width="1" align="top"
																									border="0"></TD>
																						</TR>
																						<TR>
																							<TD vAlign="top" width="255" bgColor="#dfdeed" colSpan="6" height="16" align="right"><STRONG><FONT color="#ff0066" size="2">MISC</FONT></STRONG></TD>
																						</TR>
																						<TR>
																							<TD vAlign="top" bgColor="#dfdeed" colSpan="6" height="1"><IMG height="1" alt="" src="../../assets/images/EEImages/blank.gif" width="1" align="top"
																									border="0"></TD>
																						</TR>
																						<TR>
																							<TD class="menu" vAlign="top" width="283">&nbsp; <A href="InboxAlertDetail.html">On 
																									Time Delivery</A>
																							</TD>
																							<TD width="2" bgColor="#dfdeed" height="16"><IMG height="1" alt="" src="../../assets/images/EEImages/blank.gif" width="1" align="top"
																									border="0"></TD>
																							<TD class="kpis" vAlign="middle" width="25"><FONT color="#0000ff">2005/8/17</FONT><A href="javascript:openWindow('KPIs/KPI25.html','','yes','yes',255,450,100,100)"></A></TD>
																							<TD width="26">Pending</TD>
																							<TD width="64"></TD>
																							<TD width="16">System</TD>
																						</TR>
																						<TR>
																							<TD vAlign="top" bgColor="#a29fcb" colSpan="6" height="1"><IMG height="1" alt="" src="../../assets/images/EEImages/blank.gif" width="1" align="top"
																									border="0"></TD>
																						</TR>
																					</TABLE>
																				</TD>
																				<TD width="1" bgColor="#a29fcb" height="238"><IMG height="1" alt="" src="../../assets/images/EEImages/blank.gif" width="1" align="top"
																						border="0">
																				</TD>
																			</TR>
																			<TR> <!-----------alerts------------------>
																				<TD vAlign="top" width="300" colSpan="3">
																					<TABLE id="Table10" cellSpacing="0" cellPadding="0" width="300" border="0">
																						<TR>
																							<TD vAlign="top" width="300" colSpan="3" height="35"><IMG height="10" src="../../assets/images/EEImages/blank.gif" width="1"><BR>
																								<IMG height="25" alt="Key Performance Indicators" src="../../assets/images/EEImages/dboard/alerts_top.gif"
																									width="300" align="top" border="0"></TD>
																						</TR>
																						<TR>
																							<TD width="33" height="24"><IMG height="24" alt="" src="../../assets/images/EEImages/dboard/alerts_icon_on.gif"
																									width="33" align="top" border="0"></TD>
																							<TD class="menu" width="221" height="24">
																								Critical
																							</TD>
																							<TD align="right" width="1" height="24"><IMG height="24" alt="" src="../../assets/images/EEImages/dboard/alerts_bg_black.gif"
																									width="1" align="top" border="0"></TD>
																						</TR>
																						<TR>
																							<TD width="33" height="24"><IMG height="24" alt="" src="../../assets/images/EEImages/dboard/alerts_icon_off.gif"
																									width="33" align="top" border="0"></TD>
																							<TD class="menu" width="221" height="24">
																								Medium
																							</TD>
																							<TD align="right" width="1" height="24"><IMG height="24" src="../../assets/images/EEImages/dboard/alerts_bg_black.gif" width="1"
																									align="top"></TD>
																						</TR>
																						<TR>
																							<TD vAlign="top" width="300" colSpan="3" height="1"><IMG height="1" src="../../assets/images/EEImages/dboard/alerts_bg_black.gif" width="300"
																									align="top"></TD>
																						</TR>
																					</TABLE>
																				</TD>
																			</TR>
																		</TABLE>
																		<BR>
																	</td>
																	<!-- End charts -->
																	<!-- Begin KPIs -->
																	<!-- End KPIs -->
																</tr>
															</table>
															<!-- End Dashboard-->
														</td>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<DIV></DIV>
						<div id="footer" class="td-menu">
							<table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td width="100%" height="24" bgcolor="#d0d0ff" class="td-menu" align="left">&nbsp;&nbsp;Copyright 
										2005. Raining Data.</td>
								</tr>
							</table>
						</div>
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>
