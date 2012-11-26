<%@include file='../../includes/jspinclude.jsp'%>

<%
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String APNID = request.getParameter("APNID");
String sessionID = (String)session.getAttribute("sessionID");
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");
String NDC = request.getParameter("prdid");
String lotNum = request.getParameter("lotnum");
String pedOrd = request.getParameter("pedOrd");


System.out.println("apnid "+APNID);

String TOPLINE = "";
String EXCEPTIONVALUES = "";
String SYSROLESVALUES="";
String DocType="";
byte[] xmlResults;

com.rdta.tlapi.xql.Connection connection = ConnectTL();
com.rdta.tlapi.xql.Statement statement = getStatement(connection);

String xQuery = "";

xQuery = "for $b in collection('tig:///ePharma/APN')/APN  ";
xQuery = xQuery + "where $b/DocumentId = '"+APNID+"' ";
xQuery = xQuery + "return  ";
xQuery = xQuery + "<tr class='tableRow_Header'>    ";
xQuery = xQuery + "<td class='type-whrite'>Pedigree ID : <STRONG><FONT color='#ffff00'>"+APNID+"</FONT></STRONG></td> ";
xQuery = xQuery + "<td class='type-whrite'>Issue Date : <FONT color='#ffff00'><STRONG>{data($b/DateTime)}</STRONG></FONT></td>     ";
xQuery = xQuery + "<td class='type-whrite'>Transaction Type: <FONT color='#ffff00'><STRONG>{data($b/To/TransactionType)}</STRONG></FONT></td> ";
xQuery = xQuery + "<td class='type-whrite'>Transaction # : <STRONG><FONT color='#ffff00'>{data($b/To/TransactionNumber)}</FONT></STRONG></td>   ";
xQuery = xQuery + "<td class='type=whrite' bgcolor='#ccffff'> ";
xQuery = xQuery + "<A HREF='ePedigree_ViewOrder.jsp?trType={data($b/To/TransactionType)}&amp;trNum={data($b/To/TransactionNumber)}&amp;tp_company_nm="+tp_company_nm+"&amp;pagenm="+pagenm+"' target='_new'> ";
xQuery = xQuery + "<FONT color='#000099'><STRONG>View</STRONG></FONT> ";

xQuery = xQuery + "</A></td>  ";
xQuery = xQuery + "</tr> ";

xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	TOPLINE = new String(xmlResults);
}


//xQuery = "for $i in collection('tig:///EAGRFID/SystemVocabulary')//SysVocabulary ";
//xQuery = xQuery + " return <option value = '{data($i/category/PedigreeExceptions)}'>{data($i/category/PedigreeExceptions)}</option> ";

//System.out.println(xQuery+"is my query");
//xmlResults=ReadTL(statement,xQuery);
//if(xmlResults != null){
//	EXCEPTIONVALUES=new String(xmlResults);
//}

//xQuery = "for $i in collection('tig:///EAGRFID/SystemVocabulary')//SysVocabulary ";
//xQuery = xQuery + " return <option value = '{data($i/category/SystemRoles)}'>{data($i/category/SystemRoles)}</option> ";
//
//System.out.println(xQuery+"is my query");
//xmlResults=ReadTL(statement,xQuery);
//if(xmlResults != null){
//	SYSROLESVALUES=new String(xmlResults);
//}


//xQuery = "for $b in collection('tig:///ePharma/APN')/APN  ";
//xQuery = xQuery + "where $b/DocumentId = '"+APNID+"' ";
//xQuery = xQuery + "return data($b/To/TransactionType) ";
//
//System.out.println("Query for Doc Type: "+xQuery);
//xmlResults=ReadTL(statement,xQuery);
//if(xmlResults!=null){
//	DocType=new String(xmlResults);
//}
//System.out.println("Doc Type: "+DocType);


//GET Product Details Info
xQuery = "for $b in collection('tig:///ePharma/APN')/APN[DocumentId = '"+APNID+"']/Pedigrees/Pedigree[@order='"+pedOrd+"']/Products/Product[fn:upper-case(NDC)='"+NDC+"' and fn:upper-case(LotNumber)='"+lotNum+"'] ";
xQuery = xQuery + "return ";
xQuery = xQuery + "$b"; //BUG -  WORKAROUND IS TO USE fn:upper-case
								
xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	
}

String manufName = "";
//GET Product Details Info
xQuery = "for $b in collection('tig:///ePharma/APN')/APN[DocumentId = '"+APNID+"']/Pedigrees/Pedigree[@order='"+pedOrd+"'] ";
xQuery = xQuery + "return  ";
xQuery = xQuery + "let $prds := $b/Products ";
xQuery = xQuery + "let $mftr := $b/Manufacturer ";
xQuery = xQuery + "for $p in $prds/Product where $p/NDC='"+NDC+"' and $p/LotNumber='"+lotNum+"' ";
xQuery = xQuery + "return data($mftr[LicenseNumber=data($p/ManufacturerLicense)]/Name) ";
xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	manufName = new String(xmlResults);
}

CloseConnectionTL(connection);
%>
<html>
	<head>
		<title>Raining Data ePharma - ePedigree - Receiving Manager</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">
			<style type="text/css"></style>
			<script language="JavaScript" type="text/JavaScript">
<!--
function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}
//-->
			</script>
			<script language="JavaScript" type="text/JavaScript">
<!--


function MM_reloadPage(init) {  //reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
}
MM_reloadPage(true);

function MM_popupMsg(msg) { //v1.0
  alert(msg);
}
//-->
			</script>
	</head>
	<body>
		<%@include file='topMenu.jsp'%>

			<table border="0" cellspacing="0" cellpadding="0" width="100%" ID="Table1">
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
								<td class="td-typeblack"><FONT size="4">Product Details &amp; Authenticity</FONT></td>
								<td class="td-typeblack" align="right"><FONT color="#0000cc">Anti Counterfeit Measures</FONT>
								</td>
							</tr>
							<tr class="tableRow_Header">
								<td colspan="2">
									<table width="100%" id="Table3" cellSpacing="1" cellPadding="1" align="left" border="0"
										bgcolor="white">
										<tr>
											<td colspan="5">
												<table border="1" cellpadding="1" cellspacing="1" width="100%" ID="Table8">
													<TR bgcolor="#ccffff">
														<TD class="type-whrite" align="center"><STRONG><A href="ePedigree_Manager_Reconcile.html"><FONT color="#000099">Summary</FONT></A></STRONG></TD>
														<TD class="type-whrite" align="center"><STRONG><a href="ePedigree_Manager_ReconcileFromTo.html"><FONT color="#000099">From-To</FONT></a></STRONG>
														</TD>
														<TD class="type-whrite" bgcolor="gold" align="center"><STRONG><a href="ePedigree_Manager_Reconcile_Products.html"><FONT color="#000099">Products</FONT></a></STRONG>
														</TD>
														<TD class="type-whrite" align="center"><STRONG><a href="ePedigree_Manager_Reconcile_Manufacturer.html"><FONT color="#000099">Manufacturer(s)</FONT></a></STRONG>
														</TD>
														<TD class="type-whrite" align="center"><P align="center"><STRONG><a href="ePedigree_Manager_Reconcile_Custody.html"><FONT color="#000099">Custody</FONT></a></STRONG></P>
														</TD>
														<TD class="type-whrite" align="center"><STRONG><a href="ePedigree_Manager_Reconcile_Status.html"><FONT color="#000099">Status 
																		History</FONT></a></STRONG>
														</TD>
														<TD class="type-whrite" align="center"><STRONG><a href="ePedigree_Manager_Reconcile_Returns.html"><FONT color="#000099">Returns</FONT></a></STRONG>
														</TD>
														<TD class="type-whrite" align="center"><STRONG><a href="ePedigree_Manager_Reconcile_Trail.jsp?pedid=<%=APNID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree"><FONT color="#000099">Audit Trail</FONT></a></STRONG>
														</TD>
													</TR>
												</table>
											</td>
										</tr>
										<%=TOPLINE%>
									</table>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<table border="0" cellpadding="0" cellspacing="1" width="100%" ID="Table6">
										<tr>
											<td class="td-typeblack" colspan="7">
												<TABLE id="Table10" cellSpacing="1" cellPadding="0" width="100%" border="0">
													<TBODY>
														<TR class="tableRow_Header">
															<TD class="type-whrite" align="center"><STRONG><FONT size="3" color="#ffffff"> Biaxin </FONT>
																</STRONG>
															</TD>
															<TD class="type-whrite" align="center">
																<FONT size="3" color="#ffffff"><STRONG>AUTHENTICITY</STRONG></FONT>
															</TD>
														</TR>
														<TR class="tableRow_On">
															<TD class="tableRow_On" align="center" height="142"><STRONG><FONT color="#3300cc">
																		<P align="center"><IMG height="80" alt="" src="../../assets/images/Pfizer.gif" width="75"></P>
																		<P align="center">
																			<TABLE id="Table12" cellSpacing="1" cellPadding="1" border="0" width="100%" align="center">
																				<TR class="tableRow_Off">
																					<TD width="66"><STRONG>NDC:</STRONG>
																					</TD>
																					<TD>
																						<P><INPUT class="tableRow_Yellow" id="Text7" readOnly type="text" size="18" value="0978-0451-01"
																								name="ndc"></P>
																					</TD>
																				</TR>
																				<TR class="tableRow_On">
																					<TD width="66"><STRONG>EPC:</STRONG></TD>
																					<TD>0100 00A8 9000 16F0 0016 9D00</TD>
																				</TR>
																				<TR class="tableRow_Off">
																					<TD width="66"><STRONG>UPC/SKU:</STRONG></TD>
																					<TD><INPUT id="Text1" type="text" value="3241593302" name="upcsku" readonly class="tableRow_Yellow"
																							size="18"></TD>
																				</TR>
																				<TR class="tableRow_On">
																					<TD width="66"><STRONG>In Stock:</STRONG></TD>
																					<TD>1500 units</TD>
																				</TR>
																			</TABLE>
																		</P>
																	</FONT></STRONG>
															</TD>
															<TD class="tableRow_On" align="center">
																<TABLE id="Table14" cellSpacing="1" cellPadding="0" border="0" align="center" width="100%">
																	<TR class="tableRow_Off">
																		<TD><STRONG><FONT size="1">Threshold:</FONT></STRONG></TD>
																		<TD>
																			<P align="center"><STRONG><FONT color="#669933" size="1">VALID</FONT></STRONG></P>
																		</TD>
																		<TD><STRONG><FONT color="#006600">2005-07-20 at 09:18:10</FONT></STRONG></TD>
																		<TD><A href="">AUTHENTICATE</A></TD>
																	</TR>
																	<TR class="tableRow_On">
																		<TD><STRONG><FONT size="1">Recall:</FONT></STRONG></TD>
																		<TD>
																			<P align="center"><STRONG><FONT color="#669933" size="1">VALID</FONT></STRONG></P>
																		</TD>
																		<TD><STRONG><FONT color="#006600">2005-07-20 at 09:18:10</FONT></STRONG></TD>
																		<TD><A href="">AUTHENTICATE</A></TD>
																	</TR>
																	<TR class="tableRow_Off">
																		<TD><STRONG><FONT size="1">Contents:</FONT></STRONG></TD>
																		<TD>
																			<P align="center"><STRONG><FONT color="#669933" size="1">VALID</FONT></STRONG></P>
																		</TD>
																		<TD><STRONG><FONT color="#006600">2005-07-20 at 09:18:10</FONT></STRONG></TD>
																		<TD><A href="">AUTHENTICATE</A></TD>
																	</TR>
																	<TR class="tableRow_On">
																		<TD><STRONG><FONT size="1">Dosage:</FONT></STRONG></TD>
																		<TD>
																			<P align="center"><STRONG><FONT color="#669933" size="1">VALID</FONT></STRONG></P>
																		</TD>
																		<TD><STRONG><FONT color="#006600">2005-07-20 at 09:18:10</FONT></STRONG></TD>
																		<TD><A href="">AUTHENTICATE</A></TD>
																	</TR>
																	<TR class="tableRow_Off">
																		<TD><STRONG><FONT size="1">Signature:</FONT></STRONG></TD>
																		<TD>
																			<P align="center"><STRONG><FONT color="#669933" size="1">VALID</FONT></STRONG></P>
																		</TD>
																		<TD><STRONG><FONT color="#006600">2005-07-20 at 09:18:10</FONT></STRONG></TD>
																		<TD><a href="">AUTHENTICATE</a></TD>
																	</TR>
																</TABLE>
															</TD>
														</TR>
														<TR>
															<TD class="type-whrite" width="265" height="13">&nbsp;</TD>
															<TD class="type-whrite" width="141" height="13"></TD>
														</TR>
														<TR class="tableRow_Header">
															<TD class="type-whrite" align="center"><FONT size="3" color="#ffffff"><STRONG>LOT 
																		INFORMATION</STRONG></FONT></STRONG></TD>
															<TD class="type-whrite" align="center"><FONT color="#ffffff" size="3"><STRONG>PRODUCT 
																		INFORMATION</STRONG></FONT>&nbsp;</TD>
														</TR>
														<TR class="tableRow_On">
															<TD class="type-whrite">
																<TABLE id="Table4" cellSpacing="1" cellPadding="0" align="center" border="0" width="100%">
																	<TR class="tableRow_Off">
																		<TD><STRONG><FONT size="1">Lot #:</FONT></STRONG></TD>
																		<TD><INPUT class="tableRow_Yellow" id="Text2" readOnly type="text" size="11" value="52345 "
																				name="lotNumber">
																		</TD>
																	</TR>
																	<TR class="tableRow_On">
																		<TD><STRONG><FONT size="1">Manufactured Date:</FONT></STRONG></TD>
																		<TD><INPUT class="tableRow_Yellow" id="Text3" readOnly type="text" size="11" value="2005-05-01"
																				name="manufactDate">
																		</TD>
																	</TR>
																	<TR class="tableRow_Off">
																		<TD height="14"><STRONG><FONT size="1">Product Expiration:</FONT></STRONG></TD>
																		<TD height="14"><INPUT class="tableRow_Yellow" id="Text4" readOnly type="text" size="11" value="2005-05-01"
																				name="PrdExpireDate">
																		</TD>
																	</TR>
																	<TR class="tableRow_On">
																		<TD><STRONG><FONT size="1">Lot Expiration:</FONT></STRONG></TD>
																		<TD>
																			<STRONG><FONT color="#669933" size="1"><INPUT class="tableRow_Yellow" id="Text5" readOnly type="text" size="11" value="2005-05-01"
																						name="LotExpireDate"></FONT></STRONG>
																		</TD>
																	</TR>
																	<TR class="tableRow_Off">
																		<TD><STRONG><FONT size="1">Manufacturer:</FONT></STRONG></TD>
																		<TD>
																			<STRONG><FONT color="#669933" size="1">Abbott</FONT></STRONG>
																		</TD>
																	</TR>
																</TABLE>
															</TD>
															<TD class="type-whrite">
																<TABLE id="Table15" cellSpacing="1" cellPadding="0" align="center" border="0" width="100%">
																	<TR class="tableRow_On">
																		<TD height="15"><STRONG><FONT size="1">Dosage Form:</FONT></STRONG></TD>
																		<TD align="left" height="15">
																			<STRONG><FONT color="#669933" size="1"></FONT></STRONG><INPUT class="tableRow_Yellow" id="Text6" readOnly type="text" value="Oral" name="DosageForm">
																		</TD>
																	</TR>
																	<TR class="tableRow_Off">
																		<TD><STRONG><FONT size="1">Dosage Strength:</FONT></STRONG></TD>
																		<TD align="left"><INPUT class="tableRow_Yellow" id="Text8" readOnly type="text" value="50 mg" name="DosageStrength">
																		</TD>
																	</TR>
																	<TR class="tableRow_On">
																		<TD><STRONG><FONT size="1">Package Size:</FONT></STRONG></TD>
																		<TD align="left">
																			<STRONG><FONT color="#669933" size="1"><INPUT class="tableRow_Yellow" id="Text9" readOnly type="text" value="10 cm" name="PackageSize"></FONT></STRONG>
																		</TD>
																	</TR>
																	<TR class="tableRow_Off">
																		<TD><STRONG><FONT size="1">Description:</FONT></STRONG></TD>
																		<TD align="left"><INPUT class="tableRow_Yellow" id="Text11" readOnly type="text" size="34" value="FOR SUSPENSION; ORAL"
																				name="Description">
																		</TD>
																	</TR>
																	<TR class="tableRow_On">
																		<TD><STRONG><FONT size="1">Marketing Status:</FONT></STRONG></TD>
																		<TD align="left"><STRONG><FONT color="#669933" size="1"><INPUT class="tableRow_Yellow" id="Text10" readOnly type="text" value="Prescription" name="MarketingStat">
																				</FONT></STRONG>
																		</TD>
																	</TR>
																	<TR class="tableRow_Off">
																		<TD><STRONG><FONT size="1">Quantity:</FONT></STRONG></TD>
																		<TD align="left"><INPUT class="tableRow_Yellow" id="Text12" readOnly type="text" value="1" name="ProdQTY"></TD>
																	</TR>
																	<TR class="tableRow_On">
																		<TD><STRONG><FONT size="1">Threshold:</FONT></STRONG></TD>
																		<TD align="left">
																			<STRONG><FONT color="#669933" size="1">Temp &lt;= 90F&nbsp;</FONT></STRONG>
																		</TD>
																	</TR>
																	<TR class="tableRow_Off">
																		<TD colspan="2" align="center"><a href="ViewShipmentSensorData.html">View Sensor 
																				Recording Details</a></TD>
																	</TR>
																</TABLE>
															</TD>
														</TR>
														<TR class="tableRow_On">
															<TD class="type-whrite" colspan="2" align="center"><INPUT id="Submit1" type="submit" value="Save Changes" name="Submit1"></TD>
														</TR>
													</TBODY></TABLE>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td colspan="2"><br>
									<table border="0" cellpadding="0" cellspacing="1" width="100%" ID="Table7">
										<tr>
											<td class="td-typeblack" colspan="7">Manufacturer Info&nbsp;
											</td>
										</tr>
										<tr class="tableRow_Header">
											<td class="type-whrite">Name</td>
											<td class="type-whrite">Address</td>
											<td class="type-whrite">Contact</td>
											<td class="type-whrite">Phone</td>
											<td class="type-whrite">Email</td>
											<td class="type-whrite">License</td>
										</tr>
										<tr class="tableRow_On">
											<td class="td-menu">Abbott</td>
											<td class="td-menu">1234 AbbottWay Herschem PA 99999</td>
											<td class="td-menu">Mike Jognes</td>
											<td class="td-menu">(213) 444-5555</td>
											<td class="td-menu">mike@abbott.com</td>
											<td class="td-menu">LP321456</td>
										</tr>
									</table>
								</td>
							</tr>
							<TR>
								<TD colSpan="2"><BR>
								</TD>
							</TR>
						</table>
						<DIV></DIV>
						<div id="footer">
							<table width="100%" height="24" border="0" cellpadding="0" cellspacing="0" ID="Table13">
								<tr>
									<td width="100%" height="24" bgcolor="#d0d0ff" class="td-menu" align="left">&nbsp;&nbsp;Copyright 
										2005. Raining Data.</td>
								</tr>
							</table>
						</div>
					</td>
				</tr>
			</table>
		
	</body>
</html>
