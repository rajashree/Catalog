<%@include file='../../includes/jspinclude.jsp'%>

<%
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String APNID = request.getParameter("pedid");
//String sessionID = request.getParameter("sessionID");
String sessionID = (String)session.getAttribute("sessionID");
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");


byte[] xmlResults;

com.rdta.tlapi.xql.Connection connection = ConnectTL();
com.rdta.tlapi.xql.Statement statement = getStatement(connection);

String xQuery = "";
String APNSigStatus = "";
String APNSigStatusDate = "";



xQuery = "for $i in collection('tig:///ePharma/APNSignatureStatus')";
xQuery = xQuery + "where $i/verifySig/verifyLevel='APN' and $i/verifySig/verifyDocumentId = '"+APNID+"' ";
xQuery = xQuery + "return data($i/verifySig/verifyStatus)";

xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	APNSigStatus = new String(xmlResults);
}	

servIP = request.getServerName();
		clientIP = request.getRemoteAddr();
		fromDT = request.getParameter("fromDT");
		toDT = request.getParameter("toDT");
		lotNum = request.getParameter("lotNum");
		prodNDC = request.getParameter("prodNDC");
		orderNum = request.getParameter("orderNum");
		dispNum = request.getParameter("poNum");
		invoiceNum = request.getParameter("invoiceNum");
		//sessionID = request.getParameter("sessionID");
		pagenm = request.getParameter("pagenm");
		tp_company_nm = request.getParameter("tp_company_nm");
		SSCC = request.getParameter("SSCC");
		apndocId = request.getParameter("apndocId");
		tpName = request.getParameter("tpName");

		HttpSession sess = request.getSession();
		sessionID = (String)sess.getAttribute("sessionID");
		System.out.println("sess in receiving action:"+sessionID);
		if(orderNum == null) { orderNum = "";}
		if(dispNum == null) { dispNum = "";}
		if(invoiceNum == null) { invoiceNum = "";}
		if(lotNum == null) { lotNum = "";}
		if(prodNDC == null) { prodNDC = "";}
		if(SSCC == null) { SSCC = "";}
		if(fromDT == null) { fromDT = "";}
		if(toDT == null) { toDT = "";}
		if(pagenm == null) { pagenm = "";}
		if(tp_company_nm == null) { tp_company_nm = "";}
		if(apndocId == null) { apndocId = "";}
		if(tpName == null) { tpName = "";}
		if(transactionType == null) { transactionType = ""; }

		SimpleDateFormat df = new SimpleDateFormat();
		df.applyPattern("yyyy-MM-dd");

		if(fromDT.equals("")) {
			screenEnteredDate = df.format(new java.util.Date());
		} else {
			screenEnteredDate = fromDT+" to "+toDT;
		}
		String idDate = df.format(new java.util.Date())+"T00:00:00";
		request.setAttribute("screenEnteredDate", screenEnteredDate);

		if (tp_company_nm.equals("")) {
		buff1.append("for $i in collection('tig:///CatalogManager/TradingPartner')/TradingPartner ");
		buff1.append("return <Name>{data($i/name)}</Name>");
		} else {
		buff1.append("<Name>"+tp_company_nm+"</Name>");
		}

		
		if(orderNum.equals("") && dispNum.equals("") && invoiceNum.equals("") && lotNum.equals("") && prodNDC.equals("") && SSCC.equals("") && apndocId.equals("") && tpName.equals("")) {

			buff.append("for $b in collection('tig:///ePharma/APN')/APN");
			if(fromDT.equals("")) {
				buff.append(" where $b/DateTime >= '"+idDate+"' order by $b/From/Name ");
			} else {
				buff.append( " where $b/DateTime >= '"+fromDT+"T00:00:00' and $b/DateTime <= '"+toDT+"T00:00:00' order by $b/DateTime, $b/From/Name ");
			}

			log.info("Querying over for From Date - To Date !!!!");

		}else {
			if(!lotNum.equals("")) { //by Lot Number
				buff.append("for $b in collection('tig:///ePharma/APN')/APN ");
				buff.append(" where $b/Pedigrees/Pedigree/Products/Product/LotNumber = '"+lotNum+"' order by $b/From/Name ");
			}else{
				if(!orderNum.equals("")) { //by ORDER
					buff.append("for $o in collection('tig:///ePharma/Orders')/Order, $b in collection('tig:///ePharma/APN')/APN ");
					buff.append(" where $b/To/TransactionNumber = $o/BuyersID and $o/BuyersID = '"+orderNum+"' order by $b/DateTime, $b/From/Name ");
				}else{
					if(!dispNum.equals("")) { //by DISPATCH ADVISE
						buff.append("for $o in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice, $b in collection('tig:///ePharma/APN')/APN ");
						buff.append(" where $b/To/TransactionNumber = $o/OrderReference/BuyersID and $o/ID = '"+dispNum+"' order by $b/DateTime, $b/From/Name ");
					}else {
						if(!SSCC.equals("")) { //by SSCC
							buff.append("for $b in collection('tig:///ePharma/APN')/APN ");
							buff.append(" where $b/Pedigrees/Pedigree/Products/Product/ParentEPC = '"+SSCC+"' order by $b/DateTime, $b/From/Name ");
						}else { //INVOICE
							if(!invoiceNum.equals("")){
								buff.append("for $o in collection('tig:///ePharma/Invoices')/Invoice, $b in collection('tig:///ePharma/APN')/APN ");
								buff.append(" where $b/To/TransactionNumber = $o/OrderReference/BuyersID and $o/ID = '"+invoiceNum+"' order by $b/DateTime, $b/From/Name ");
							}else { //INVOICE
								if(!prodNDC.equals("")){
									buff.append("for $b in collection('tig:///ePharma/APN')/APN");
									buff.append(" where $b/Pedigrees/Pedigree/Products/Product/NDC = '"+prodNDC+"' order by $b/DateTime, $b/From/Name ");
								}else { //INVOICE
									if(!tpName.equals("")){
										buff.append("for $b in collection('tig:///ePharma/APN')/APN");
										buff.append(" where $b/From/Name = '"+tpName+"' order by $b/DateTime, $b/From/Name ");
									}else { //INVOICE
										if(!apndocId.equals("")){
											buff.append("for $b in collection('tig:///ePharma/APN')/APN");
											buff.append(" where $b/DocumentId = '"+apndocId+"' order by $b/DateTime, $b/From/Name ");
										}
									}
								}
							}
						}
					}
				}
			}
		}

		buff.append(" return ");
		buff.append("<output>{ ");
		buff.append("<DocumentId>{data($b/DocumentId)}</DocumentId>, ");
		buff.append("<TransactionNumber>{data($b/To/TransactionNumber)}</TransactionNumber>, ");
		buff.append("<TransactionType>{data($b/To/TransactionType)}</TransactionType>, ");
		buff.append("<DateTime>{data($b/DateTime)}</DateTime>, ");
		buff.append("<Name>{data($b/From/Name)}</Name>, ");
		buff.append("<LegendDrugName>{data($b/Pedigrees/Pedigree/Products/Product/LegendDrugName)}</LegendDrugName>, ");
		buff.append("<Count>{count($b/Pedigrees/Pedigree/Products/Product)}</Count>, ");
		buff.append("<Status>{data($b/Pedigrees/Pedigree/PedigreeStatus/Status/status)}</Status> ");
		buff.append("}</output>");



CloseConnectionTL(connection);
%>

<html>
	<head>
		<title>Raining Data ePharma - ePedigree - APN Manager - Create APN</title>
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
function MM_openBrWindow(theURL,winName,features) { //v2.0
  window.open(theURL,winName,features);
}

function MM_openOrder(theURL) { //v2.0
  window.open(theURL,'Transaction Order Detail','scrollbars=yes,resizable=yes,width=800,height=600');
}


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
								<td class="td-typeblack">Pedigree Detail</td>
								<td class="td-typeblack" align="right"><FONT color="#0000cc">Anti Counterfeit Measures</FONT>
								</td>
							</tr>
							<tr class="tableRow_Header">
								<td colspan="2">
									<table width="100%" id="Table2" cellSpacing="1" cellPadding="1" align="left" border="0"
										bgcolor="white">
										<tr>
											<td colspan="5">
												<table border="1" cellpadding="1" cellspacing="1" width="100%">
													<TR bgcolor="#ccffff">
														<TD class="type-whrite" bgcolor="gold" align="center"><STRONG><A href="ePedigree_Manager_Reconcile.jsp?pedid=<%=APNID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree&selpedid=<%=PedigreeOrder%>"><FONT color="#000099">Summary</FONT></A></STRONG></TD>
														<TD class="type-whrite" align="center"><STRONG><a href="ePedigree_Manager_ReconcileFromTo.jsp?pedid=<%=APNID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree&selpedid=<%=PedigreeOrder%>"><FONT color="#000099">From-To</FONT></a></STRONG>
														</TD>
														<TD class="type-whrite" align="center"><STRONG><a href="ePedigree_Manager_Reconcile_Products.jsp?pedid=<%=APNID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree&selpedid=<%=PedigreeOrder%>"><FONT color="#000099">Products</FONT></a></STRONG>
														</TD>
														<TD class="type-whrite" align="center"><STRONG><a href="ePedigree_Manager_Reconcile_Manufacturer.jsp?pedid=<%=APNID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree&selpedid=<%=PedigreeOrder%>"><FONT color="#000099">Manufacturer(s)</FONT></a></STRONG>
														</TD>
														<TD class="type-whrite" align="center"><STRONG><a href="ePedigree_Manager_Reconcile_Custody.jsp?pedid=<%=APNID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree&selpedid=<%=PedigreeOrder%>"><FONT color="#000099">Custody</FONT></a></STRONG>
														</TD>
														<TD class="type-whrite" align="center"><STRONG><a href="ePedigree_Manager_Reconcile_Status.jsp?pedid=<%=APNID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree&selpedid=<%=PedigreeOrder%>"><FONT color="#000099">Status 
																		History</FONT></a></STRONG>
														</TD>
														<TD class="type-whrite" align="center"><STRONG><a href="ePedigree_Manager_Reconcile_Returns.jsp?pedid=<%=APNID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree&selpedid=<%=PedigreeOrder%>"><FONT color="#000099">Returns</FONT></a></STRONG>
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
							<tr class="tableRow_On">
								<td class="td-menu bold">From</td>
								<td class="td-menu bold">To</td>
							</tr>
							<tr class="tableRow_Off">
								<td><%=FromAddr%>
								</td>
								<td><%=ToAddr%>
								</td>
							</tr>
							<tr>
								<td colspan="2"><br>
									<table border="0" cellpadding="0" cellspacing="1" width="100%" ID="Table5">
										<tr>
											<td class="td-typeblack" colspan="7">Products - <FONT color="#336600">Total: <%=totProducts%> </FONT>
											</td>
										</tr>
										<tr class="tableRow_Header">
											<td class="type-whrite">Name</td>
											<td class="type-whrite">NDC</td>
											<td class="type-whrite">Manufacturer</td>
											<td class="type-whrite">QTY</td>
											<td class="type-whrite">Lot Number</td>
											<td class="type-whrite">Lot Expire Date</td>
											<td class="type-whrite">EPC Number</td>
										</tr>
										<%=PRODS%>
									</table>
								</td>
							</tr>
							<tr>
								<td colspan="2"><br>
									<table border="0" cellpadding="0" cellspacing="1" width="100%" ID="Table6">
										<tr>
											<td class="td-typeblack" colspan="7">Manufacturers In Shipment - <FONT color="#336600">Total: 
													<%=totManufacturers%></FONT>
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
										<%=MANUFACT%>
									</table>
								</td>
							</tr>
							<tr>
								<Td colspan="2"><br>
									<table border="0" cellpadding="0" cellspacing="1" width="100%" ID="Table7">
									</table>
									<TABLE id="Table9" cellSpacing="1" cellPadding="0" width="100%" border="0">
										<TR>
											<TD class="td-typeblack" colSpan="7">Drug Sales &amp; Distribution - <FONT color="#336600">
													Total: <%=totCustodians%></FONT></TD>
										</TR>
										<TR class="tableRow_Header">
											<TD class="type-whrite" height="13">Name</TD>
											<TD class="type-whrite" height="13">Address</TD>
											<TD class="type-whrite" height="13">Contact</TD>
											<TD class="type-whrite" height="13">Phone</TD>
											<TD class="type-whrite" height="13">Email</TD>
											<TD class="type-whrite" height="13">Dates in Custody</TD>
										</TR>
										<%=CUSTODY%>
									</TABLE>
								</Td>
							</tr>
							<TR>
								<TD colSpan="2"><BR>
									<TABLE id="Table10" cellSpacing="1" cellPadding="0" width="100%" border="0">
										<TR>
											<TD class="td-typeblack" colSpan="8">** Document Validity Status **</TD>
										</TR>
										<TR class="tableRow_Header">
											<TD class="tableRow_On"><STRONG><FONT color="#0000cc">&nbsp; LEVEL</FONT></STRONG></TD>
											<TD class="type-whrite" width="83">APN Message</TD>
											<TD class="type-whrite" width="84">Pedigrees (Total: 1)</TD>
											<TD class="type-whrite" width="91">Manufacturers (Total: 1)</TD>
											<TD class="type-whrite" width="84">Custodians (Total: 1)</TD>
											<TD class="type-whrite" width="83">Products (Total: 12)</TD>
											<TD class="type-whrite" align="center" valign="middle">
												<TABLE width="301" cellSpacing="1" cellPadding="1" border="1" height="20">
													<TR>
														<TD class="type-whrite" colspan="4" align="center">Product Verification</TD>
													</TR>
												</TABLE>
											</TD>
										</TR>
										<TR class="tableRow_Off">
											<TD class="td-menu"><STRONG><FONT color="#3300cc">STATUS</FONT></STRONG></TD>
											<TD class="td-menu" width="83">
												<P align="center"><STRONG><FONT color="#009900"><%=APNSigStatus%></FONT></STRONG></P>
											</TD>
											<TD class="td-menu" width="84">
												<P align="center"><STRONG><FONT color="#009900">&nbsp;</FONT></STRONG></P>
											</TD>
											<TD class="td-menu" width="91">
												<P align="center"><STRONG><FONT color="#009900">&nbsp;</FONT></STRONG></P>
											</TD>
											<TD class="td-menu" width="84">
												<P align="center"><STRONG><FONT color="#009900">&nbsp;</FONT></STRONG></P>
											</TD>
											<TD class="td-menu" width="83">
												<P align="center"><STRONG><FONT color="#009900">&nbsp;</FONT></STRONG></P>
												
											</TD>
											<TD class="type-whrite" align="center" valign="middle">
												<TABLE width="301" cellSpacing="1" cellPadding="1" border="1" height="32">
													<TR class="tableRow_Header">
														<TD class="type-whrite" width="51">Threshold</TD>
														<TD class="type-whrite">
															<P align="center"><FONT color="#ffffff">Recall</FONT></P>
														</TD>
														<TD class="type-whrite">
															Content/Dosage</TD>
														<TD class="type-whrite">Signature</TD>
														
													</TR>
													<TR class="tableRow_On">
														<TD width="51">
															<P align="center"><STRONG><FONT color="#669933">&nbsp;</FONT></STRONG></P>
														</TD>
														<TD>
															<P align="center"><STRONG><FONT color="#669933">&nbsp;</FONT></STRONG></P>
														</TD>
														<TD>
															<P align="center"><STRONG><FONT color="#669933">&nbsp;</FONT></STRONG></P>
														</TD>
														<TD>
															<P align="center"><STRONG><FONT color="#669933">&nbsp;</FONT></STRONG></P>
														</TD>
														
													</TR>
												</TABLE>
											</TD>
										</TR>
										<TR class="tableRow_On">
											<TD class="td-menu" height="13"><STRONG><FONT color="#3300cc">DATE</FONT></STRONG></TD>
											<TD class="td-menu" width="83" height="13"><%=APNSigStatusDate%></TD>
											<TD class="td-menu" width="84" height="13">&nbsp;</TD>
											<TD class="td-menu" width="91" height="13">&nbsp;</TD>
											<TD class="td-menu" width="84" height="13">&nbsp;</TD>
											<TD class="td-menu" width="83" height="13">&nbsp;</TD>
											<TD class="td-menu" height="13">
												<P align="center">&nbsp;</P>
											</TD>
										</TR>
										<TR class="tableRow_Off">
											<TD class="td-menu"><STRONG><FONT color="#3300cc"></FONT></STRONG></TD>
											<TD class="td-menu" width="83"><STRONG><FONT color="#0000ff"><A HREF="ValidateSignature.jsp?pedid=<%=APNID%>&part=APN&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree">AUTHENTICATE</A></FONT></STRONG></TD>
											<TD class="td-menu" width="84"><STRONG><FONT color="#0000ff">AUTHENTICATE</FONT></STRONG></TD>
											<TD class="td-menu" width="91"><STRONG><FONT color="#0000ff">AUTHENTICATE</FONT></STRONG></TD>
											<TD class="td-menu" width="84"><STRONG><FONT color="#0000ff">AUTHENTICATE</FONT></STRONG></TD>
											<TD class="td-menu" width="83"><STRONG><FONT color="#0000ff">AUTHENTICATE</FONT></STRONG></TD>
											<TD class="td-menu">
												<P align="center"><STRONG><FONT color="#0000ff">AUTHENTICATE</FONT></STRONG></P>
											</TD>
										</TR>
										<TR class="tableRow_Off">
											<TD class="td-menu"><STRONG><FONT color="#3300cc"></FONT></STRONG></TD>
											<TD class="td-menu" width="83"><STRONG><FONT color="#0000ff"><A HREF="ApplySignature.jsp?pedid=<%=APNID%>&part=APN&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree">SIGN</A></FONT></STRONG></TD>
											<TD class="td-menu" width="84"><STRONG><FONT color="#0000ff"><A HREF="ApplySignature.jsp?pedid=<%=APNID%>&part=APN&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree">SIGN</A></FONT></STRONG></TD>
											<TD class="td-menu" width="91"><STRONG><FONT color="#0000ff"><A HREF="ApplySignature.jsp?pedid=<%=APNID%>&part=APN&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree">SIGN</A></FONT></STRONG></TD>
											<TD class="td-menu" width="84"><STRONG><FONT color="#0000ff"><A HREF="ApplySignature.jsp?pedid=<%=APNID%>&part=APN&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree">SIGN</A></FONT></STRONG></TD>
											<TD class="td-menu" width="83"><STRONG><FONT color="#0000ff"><A HREF="ApplySignature.jsp?pedid=<%=APNID%>&part=APN&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree">SIGN</A></FONT></STRONG></TD>
											<TD class="td-menu">
												<P align="center"><STRONG><FONT color="#0000ff"><A HREF="ApplySignature.jsp?pedid=<%=APNID%>&part=APN&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree">SIGN</A></FONT></STRONG></P>
											</TD>
										</TR>
										
									</TABLE>
								</TD>
							</TR>
							<TR>
								<TD colSpan="2"><BR>
									<A onclick="MM_openBrWindow('PrintPedigree.jsp?apnId=<%=APNID%>','APN','scrollbars=yes,resizable=yes,width=800,height=600')"
										href="#"><IMG height="27" hspace="10" src="../../assets/images/print.gif" width="27" border="0"></A>EXPORT 
									AS: <INPUT id="Submit1" type="submit" value="XML" name="Submit1">
									    <INPUT id="Button1" onclick="MM_openBrWindow('DH2129_PedigreeForm.pdf','APN','scrollbars=yes,resizable=yes,width=800,height=600')"
										type="button" value="PDF" name="Button1">&nbsp;&nbsp;&nbsp;
										<INPUT id="Button1" onClick="MM_openBrWindow('GenerateAlert.jsp?sessionID=<%=sessionID%>&pedid=<%=APNID%>','APN','scrollbars=yes,resizable=yes,width=800,height=600')"
										type="button" value="Generate Alert" name="Button1">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									
									<input type="hidden" value="sessionID">
								</TD>
							</TR>
						</table>
						<DIV></DIV>
						<div id="footer">
							<table width="100%" height="24" border="0" cellpadding="0" cellspacing="0" ID="Table8">
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
