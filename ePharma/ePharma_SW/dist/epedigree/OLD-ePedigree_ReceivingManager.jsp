<%@include file='../../includes/jspinclude.jsp'%>

<%
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String fromDT = request.getParameter("fromDT");
String toDT = request.getParameter("toDT");
String sessionID = request.getParameter("sessionID");
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");
String screenEnteredDate = "";

if(fromDT == null) { fromDT = "";}
if(toDT == null) { toDT = "";}

if(pagenm == null) { pagenm = "";}
if(tp_company_nm == null) { tp_company_nm = "";}

String HRMLROW = "";
byte[] xmlResults;

com.rdta.tlapi.xql.Connection connection = ConnectTL();
com.rdta.tlapi.xql.Statement statement = getStatement(connection);

String xQuery = "";	

SimpleDateFormat df = new SimpleDateFormat();
df.applyPattern("yyyy-MM-dd");

if(fromDT.equals("")) {
	screenEnteredDate = df.format(new java.util.Date());
} else {
	screenEnteredDate = fromDT+" to "+toDT; 
}

String idDate = df.format(new java.util.Date())+"T00:00:00";

xQuery = "for $b in collection('tig:///ePharma/APN')/APN ";

if(fromDT.equals("")) {
	xQuery = xQuery + "where $b/DateTime >= '"+idDate+"' order by $b/From/Name ";
} else {
	xQuery = xQuery + "where $b/DateTime >= '"+fromDT+"T00:00:00' and $b/DateTime <= '"+toDT+"T00:00:00' order by $b/DateTime, $b/From/Name ";
}

xQuery = xQuery + "return ";
xQuery = xQuery + "<TR class='tableRow_On'>	 ";		
xQuery = xQuery + "<TD class='td-content'><A href='ePedigree_Manager_Reconcile.jsp?pedid={data($b/DocumentId)}'>{data($b/DocumentId)}</A></TD> ";
xQuery = xQuery + "<TD class='td-content'><A href='ePedigree_ViewOrder.jsp?trType={data($b/To/TransactionType)}&amp;trNum={data($b/To/TransactionNumber)}'>{data($b/To/TransactionNumber)}</A></TD> ";
xQuery = xQuery + "<TD class='td-content'>{data($b/DateTime)}</TD> ";
xQuery = xQuery + "<TD class='td-content'>{data($b/From/Name)}</TD> ";
xQuery = xQuery + "<TD class='td-content'>{data($b/Pedigrees/Pedigree/Products/Product/LegendDrugName)}</TD> ";
xQuery = xQuery + "<TD class='td-content'>{count($b/Pedigrees/Pedigree/Products/Product)}</TD> ";
xQuery = xQuery + "<TD class='td-content'>{data($b/Pedigrees/Pedigree/PedigreeStatus/Status/Status)}</TD> ";
xQuery = xQuery + "<TD class='td-content'>System</TD> ";
xQuery = xQuery + "</TR> ";
																		
xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	HRMLROW = new String(xmlResults);
}

CloseConnectionTL(connection);
%>


<html>
	<head>
		<title>Raining Data ePharma - ePedigree</title><LINK href="../../assets/epedigree1.css" type="text/css" rel="stylesheet"></head>
	<body>
		
		<%@include file='topMenu.jsp'%>
			<table id="Table6" cellSpacing="0" cellPadding="0" width="100%" border="0">
				<tr>
					<td class="td-rightmenu" vAlign="middle" width="1%"><IMG height="10" src="../../assets/images/space.gif" width="10"></td>
					<!-- Messaging -->
					<td class="td-rightmenu" vAlign="middle" width="99%">
						<table id="Table7" cellSpacing="0" cellPadding="0" width="100%" border="0">
							<tr>
								<td align="left"></td>
								<td align="right"><!-- <img src="../../assets/images/3320.jpg" width="200" height="27"> --><IMG src="../../assets/images/space.gif" width="5"></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>
						<table id="Table8" cellSpacing="0" cellPadding="0" width="100%" border="0">
							<tr>
								<td><IMG height="12" src="../../assets/images/space.gif" width="30"></td>
								<td rowSpan="2">&nbsp;</td>
							</tr>
							<tr>
								<td>
									<!-- info goes here -->
									<table id="Table9" cellSpacing="1" cellPadding="1" width="100%" align="left" bgColor="white"
										border="0">
										<tr>
											<td align="left">
												<table id="Table11" cellSpacing="1" cellPadding="1" width="100%" align="left" bgColor="white"
													border="0">
													<TR>
														<TD class="td-typeblack">
														<FORM action="ePedigree_ReceivingManager.jsp" method="post">
															<INPUT type="hidden" name="sessionID" value="<%=sessionID%>">
															<INPUT type="hidden" name="pagenm" value="<%=pagenm%>">
															<INPUT type="hidden" name="tp_company_nm" value="<%=tp_company_nm%>">
															<table border="0" cellspacing="0" cellpadding="0" width="100%" Table ID="Table13">
																<TR>
																	<td><FONT face="Arial" size="2"><STRONG>&nbsp;<FONT color="#009900">Quick Find</FONT></STRONG></FONT></td>
																	<td><STRONG>From Date:</STRONG> <INPUT id="Text1" type="text" size="10" name="fromDT">(yyyy-mm-dd)</td>
																	<td><STRONG>To Date:</STRONG> <INPUT id="Text2" type="text" size="9" name="toDT">(yyyy-mm-dd)</td>
																	<td><INPUT type="submit" value="Find" name="Submit1"></td>
																</TR>
															</table>
														</FORM>
														</TD>
													</TR>
													<tr bgColor="white">
														<td class="td-typeblack" colSpan="1">Pedigrees Dated: <%=screenEnteredDate%></td>
													</tr>
													<tr>
														<td align="left">
															<!-- Dashboard Start -->
															<table id="Table12" cellSpacing="1" cellPadding="0" width="100%" align="center" border="0">
																<TBODY>
																	
																	<tr class="tableRow_Header">
																		<td class="type-whrite" noWrap align="center">Pedigree&nbsp;#</td>
																		<td class="type-whrite" noWrap align="center">Order #</td>
																		<TD class="type-whrite" noWrap align="center">Date Received</TD>
																		<TD class="type-whrite" align="center">Trading Partner</TD>
																		<TD class="type-whrite" align="center">Product</TD>
																		<td class="type-whrite" noWrap align="center">Quantity</td>
																		<TD class="type-whrite" noWrap align="center">Status</TD>
																		<TD class="type-whrite" noWrap align="center">Created By</TD>
																	</tr>
																	<%=HRMLROW%>
																	
																	<TR class="tableRow_On">
																		<TD align="left" colspan="7">&nbsp;</TD>
																		<TD align="left"></TD>
																	</TR>
																	<TR class="tableRow_Header">
																		<TD align="left"></TD>
																		<TD align="left"></TD>
																		<TD align="left"></TD>
																		<TD align="left"></TD>
																		<TD align="left"></TD>
																		<TD align="left"></TD>
																		<TD align="left"></TD>
																		<TD align="left"></TD>
																	</TR>
														</td>
													</tr>
													<tr>
														<TD align="left"></TD>
														<TD align="left"></TD>
													</tr>
												</table>
												<DIV></DIV>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<DIV></DIV>
						<div id="footer" class="td-menu">
							<table width="100%" height="24" border="0" cellpadding="0" cellspacing="0" ID="Table10">
								<tr>
									<td width="100%" height="24" bgcolor="#d0d0ff" class="td-menu" align="left">&nbsp;&nbsp;Copyright 
										2005. Raining Data.</td>
								</tr>
							</table>
						</div>
					</td>
				</tr>
			</table>
			<DIV><EM></EM>&nbsp;</DIV>
			</TD></TR></TBODY></TABLE></div>
	</body>
</html>
