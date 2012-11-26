<%@include file='../../includes/jspinclude.jsp'%>

<%
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String APNID = request.getParameter("pedId");
if(APNID == null){
	APNID = (String)request.getAttribute("testapn");
}
System.out.println("****** The APNID is :"+APNID);
String sessionID = request.getParameter("sessionID");
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");

String HTMLROW = "";
String PedDocDT = "";
String FromAddr = "";
String ToAddr = "";
String SHIPROW = "";
String PRODS = "";
String MANUFACT = "";
String TOPLINE = "";
String CUSTODY = "";
String totProducts = "";
String totManufacturers = "";
String totCustodians = "";
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
xQuery = xQuery + "{  if(data($b/To/TransactionType) = 'Order') then  ";
xQuery = xQuery + "(  ";
xQuery = xQuery + " <A HREF =\"javascript:newWindow('ePedigree_ViewOrder.jsp?trType={data($b/To/TransactionType)}&amp;trNum={data($b/To/TransactionNumber)}&amp;tp_company_nm=" + tp_company_nm + "&amp;pagenm=" + pagenm + "')\" > ";
xQuery = xQuery + "<FONT color='#000099'><STRONG>View</STRONG></FONT> </A> ";
xQuery = xQuery + ") ";
xQuery = xQuery + "else (  if(data($b/To/TransactionType) = 'DespatchAdvice') then ";
xQuery = xQuery + " <A HREF = \" javascript:newWindow('ASN_Details.jsp?trType={data($b/To/TransactionType)}&amp;trNum={data($b/To/TransactionNumber)}&amp;tp_company_nm=" + tp_company_nm + "&amp;pagenm=" + pagenm + "')\" > ";
xQuery = xQuery + "<FONT color='#000099'><STRONG>View</STRONG></FONT> </A> ";
xQuery = xQuery + "else  ";
xQuery = xQuery + " <A HREF = \" javascript:newWindow('Invoice_Details.jsp?trType={data($b/To/TransactionType)}&amp;trNum={data($b/To/TransactionNumber)}&amp;tp_company_nm=" + tp_company_nm + "&amp;pagenm=" + pagenm + "')\" > ";
xQuery = xQuery + "<FONT color='#000099'><STRONG>View</STRONG></FONT> </A> ";
xQuery = xQuery + " ) } ";
xQuery = xQuery + "</td>  ";
xQuery = xQuery + "</tr> ";

xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	TOPLINE = new String(xmlResults);
}

//GET Ship From Info
xQuery = "for $b in collection('tig:///ePharma/APN')/APN ";
xQuery = xQuery + "where $b/DocumentId = '"+APNID+"' ";
xQuery = xQuery + "return ";
xQuery = xQuery + "<table border='0' cellpadding='0' cellspacing='0'><tr><td class='td-menu'>Company:</td> ";
xQuery = xQuery + "<td class='td-menu bold'>{data($b/From/Name)}</td></tr> ";
xQuery = xQuery + "<tr><td class='td-menu'>Address:</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($b/From/Address)}</td></tr> ";
xQuery = xQuery + "<tr><td class='td-menu'>Contact:</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($b/From/ContactName)}</td></tr> ";
xQuery = xQuery + "<tr><td class='td-menu'>Phone:</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($b/From/Phone)}</td></tr> ";
xQuery = xQuery + "<tr><td class='td-menu'>Fax:</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($b/From/Fax)}</td></tr> ";
xQuery = xQuery + "<tr><td class='td-menu'>Email:</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($b/From/Email)}</td></tr> ";
xQuery = xQuery + "<tr><td class='td-menu'>License:</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($b/From/LicenseNumber)}</td></tr> ";
xQuery = xQuery + "<TR class='tableRow_Off'> ";
xQuery = xQuery + "<TD class='td-menu'>Signature:</TD> ";
xQuery = xQuery + "<TD class='td-menu'><STRONG><FONT color='#009900'>VALID</FONT></STRONG></TD> ";
xQuery = xQuery + "</TR> ";
xQuery = xQuery + "<TR class='tableRow_Off'> ";
xQuery = xQuery + "<TD class='td-menu' colspan='2' align='center'><A href='SignAPN.jsp?APNID="+APNID+"&amp;sessionID="+sessionID+"&amp;tp_company_nm="+tp_company_nm+"&amp;pagenm=pedigree&amp;sect=fromto'>SIGN</A></TD> ";
xQuery = xQuery + "</TR> ";
xQuery = xQuery + "</table> ";
						
xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
FromAddr = new String(xmlResults);
}

//GET Ship To Info
xQuery = "for $b in collection('tig:///ePharma/APN')/APN ";
xQuery = xQuery + "where $b/DocumentId = '"+APNID+"' ";
xQuery = xQuery + "return ";
xQuery = xQuery + "<table border='0' cellpadding='0' cellspacing='0'><tr><td class='td-menu'>Company:</td> ";
xQuery = xQuery + "<td class='td-menu bold'>{data($b/To/Name)}</td></tr> ";
xQuery = xQuery + "<tr><td class='td-menu'>Address:</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($b/To/Address)}</td></tr> ";
xQuery = xQuery + "<tr><td class='td-menu'>Contact:</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($b/To/ContactName)}</td></tr> ";
xQuery = xQuery + "<tr><td class='td-menu'>Phone:</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($b/To/Phone)}</td></tr> ";
xQuery = xQuery + "<tr><td class='td-menu'>Fax:</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($b/To/Fax)}</td></tr> ";
xQuery = xQuery + "<tr><td class='td-menu'>Email:</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($b/To/Email)}</td></tr> ";
xQuery = xQuery + "<tr><td class='td-menu'>License:</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($b/To/LicenseNumber)}</td></tr> ";
xQuery = xQuery + "<TR class='tableRow_Off'> ";
xQuery = xQuery + "<TD class='td-menu'>Signature:</TD> ";
xQuery = xQuery + "<TD class='td-menu'><STRONG><FONT color='#009900'>VALID</FONT></STRONG></TD> ";
xQuery = xQuery + "</TR> ";
xQuery = xQuery + "<TR class='tableRow_Off'> ";
xQuery = xQuery + "<TD class='td-menu' colspan='2' align='center'><A href='SignAPN.jsp?APNID="+APNID+"&amp;sessionID="+sessionID+"&amp;tp_company_nm="+tp_company_nm+"&amp;pagenm=pedigree&amp;sect=fromto'>SIGN</A></TD> ";
xQuery = xQuery + "</TR> ";
xQuery = xQuery + "</table> ";

xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	ToAddr = new String(xmlResults);
}



CloseConnectionTL(connection);
%>


<html>
	<head>
		<title>Raining Data ePharma - ePedigree - APN Manager - Create APN</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<LINK href="../../assets/epedigree1.css" type="text/css" rel="stylesheet">
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

function MM_reloadPage(init) {  //reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
}
MM_reloadPage(true);

function MM_popupMsg(msg) { //v1.0
  alert(msg);
}

function newWindow(  htmFile  ) {
   msgWindow = window.open( htmFile, "WindowName", "left=20,top=20,width= 700,height=500,toolbar=0,scrollbar=1,resizable=1,location=0,status=0");
}


//-->
			</script>
	</head>
	<body>
		<%@include file='topMenu.jsp'%>
		
			<table id="Table1" cellSpacing="0" cellPadding="0" width="100%" border="0">
				<tr>
					<td></td>
					<td rowSpan="2">&nbsp;</td>
				</tr>
				<tr>
					<td>
						<!-- info goes here -->
						<table id="Table2" cellSpacing="1" cellPadding="1" width="100%" align="left" bgColor="white"
							border="0">
							<tr bgColor="white">
								<td class="td-typeblack">Pedigree Detail</td>
								<td class="td-typeblack" align="right"><FONT color="#0000cc">Anti Counterfeit Measures</FONT>
								</td>
							</tr>
							<tr class="tableRow_Header">
								<td colSpan="2">
									<table id="Table2" cellSpacing="1" cellPadding="1" width="100%" align="left" bgColor="white"
										border="0">
										<tr>
											<td colSpan="5">
												<table border="1" cellpadding="1" cellspacing="1" width="100%">
												<TR bgcolor="#ccffff">
													<TD class="type-whrite"  align="center"><STRONG><A href="ePedigree_ShipManager_Reconcile.jsp?pedId=<%=APNID%>&sessionID=<%=sessionID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree"><FONT color="#000099">Summary</FONT></A></STRONG></TD>
													<TD class="type-whrite" bgcolor="gold" align="center"><STRONG><a href="ePedigree_ShipManager_FromTo.jsp?pedId=<%=APNID%>&sessionID=<%=sessionID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree"><FONT color="#000099">From-To</FONT></a></STRONG>
													</TD>
													<TD class="type-whrite"  align="center"><STRONG><a href="ePedigree_ShipManager_Products.jsp?pedId=<%=APNID%>&sessionID=<%=sessionID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree"><FONT color="#000099">Products</FONT></a></STRONG>
													</TD>
													<TD class="type-whrite"  align="center"><STRONG><a href="ePedigree_ShipManager_Manufacturer.jsp?pedId=<%=APNID%>&sessionID=<%=sessionID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree"><FONT color="#000099">Manufacturer(s)</FONT></a></STRONG>
													</TD>
													<TD class="type-whrite"  align="center"><STRONG><a href="ePedigree_ShipManager_Custody.jsp?pedId=<%=APNID%>&sessionID=<%=sessionID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree"><FONT color="#000099">Custody</FONT></a></STRONG>
													</TD>
													<TD class="type-whrite"  align="center"><STRONG><a href="ePedigree_ShipManager_Status.jsp?pedId=<%=APNID%>&sessionID=<%=sessionID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree"><FONT color="#000099">Status 
																	History</FONT></a></STRONG>
													</TD>
													<TD class="type-whrite"  align="center"><STRONG><a href="ePedigree_ShipManager_Returns.jsp?pedId=<%=APNID%>&sessionID=<%=sessionID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree"><FONT color="#000099">Returns</FONT></a></STRONG>
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
								<td colSpan="2"><br>
									<table id="Table5" cellSpacing="1" cellPadding="0" width="100%" border="0">
									</table>
								</td>
							</tr>
							<TR>
								<TD colSpan="2"><BR>
									<A onclick="MM_openBrWindow('ePedigree_Manager_Create4b.html','APN','scrollbars=yes,resizable=yes,width=800,height=600')"
										href="#"><IMG height="27" hspace="10" src="../../assets/images/print.gif" width="27" border="0"></A>EXPORT 
									AS: <INPUT id="Submit1" type="submit" value="XML" name="Submit1">&nbsp;&nbsp; <INPUT id="Submit2" type="submit" value="CSV" name="Submit1">&nbsp;&nbsp;&nbsp;<INPUT id="Submit3" type="submit" value="EDI" name="Submit1">&nbsp;&nbsp;
									<INPUT id="Button1" onclick="MM_openBrWindow('DH2129_PedigreeForm.pdf','APN','scrollbars=yes,resizable=yes,width=800,height=600')"
										type="button" value="PDF" name="Button1">&nbsp;&nbsp;
								</TD>
							</TR>
						</table>
						<DIV></DIV>
						<div id="footer">
							<table id="Table8" height="24" cellSpacing="0" cellPadding="0" width="100%" border="0">
								<tr>
									<td class="td-menu" align="left" width="100%" bgColor="#d0d0ff" height="24">&nbsp;&nbsp;Copyright 
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
