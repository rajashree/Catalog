<%@include file='../../includes/jspinclude.jsp'%>

<%@ page import="com.rdta.rules.CreateAPNAction"%>
<%@ page import="com.rdta.catalog.OperationType"%>

<% HttpSession sess = request.getSession();

	//added raju
String  searchSelect = 	request.getParameter("searchSelect");
String  selectedRow = 	request.getParameter("selectedRow");
String APNdate =(String)sess.getAttribute("APNDate");
String Pedigreedate =(String)sess.getAttribute("pedigreeDate");
String Mfgdate =(String)sess.getAttribute("mfgDate");
String Custdate =(String)sess.getAttribute("custDate");
String Productdate =(String)sess.getAttribute("productDate");

String APNStatus =(String)sess.getAttribute("APNSigStatus");

String PedigreeStatus =(String)sess.getAttribute("PedigreeStatus");
String mfgstatus =(String)sess.getAttribute("mfgstatus");
String custstatus =(String)sess.getAttribute("custstatus");
String Productstatus =(String)sess.getAttribute("Productstatus");


String  FromShippingSearchPage = 	request.getParameter("FromShippingSearchPage");

System.out.println("Search select : " + searchSelect  + "    selectedRow "  + selectedRow );
String APNID = "";

if(FromShippingSearchPage != null) {
	CreateAPNAction action = new CreateAPNAction();

	//APNID = action.createAPNBasedNDC(searchSelect,selectedRow);
} else {
	APNID = request.getParameter("pedId");
	String str = (String) session.getAttribute("testapn");
	String dstr = (String) session.getAttribute("dtestapn");
	System.out.println("checking apnid "+str);
	if(str == null && dstr == null ){
		session.setAttribute("testapn",APNID);
		session.setAttribute("dtestapn",APNID);
		System.out.println("checking apnid "+APNID);
	}else if(str.equals(APNID))
	{
			System.out.println("checking apnid "+APNID);
			session.setAttribute("testapn",APNID);
			session.setAttribute("dtestapn",APNID);
			
	}else{
				APNStatus ="";
				sess.setAttribute("APNSigStatus","");	
				session.setAttribute("testapn",APNID);
				APNdate ="";
				sess.setAttribute("APNDate","");	
				session.setAttribute("dtestapn",APNID);
				PedigreeStatus ="";
				sess.setAttribute("PedigreeStatus","");
				session.setAttribute("testapn",APNID);
				Pedigreedate = "";
				sess.setAttribute("pedigreeDate","");	
				session.setAttribute("dtestapn",APNID);
				mfgstatus ="";
				sess.setAttribute("mfgstatus","");
				session.setAttribute("testapn",APNID);
				Mfgdate  ="";
				sess.setAttribute("mfgDate","");	
				session.setAttribute("dtestapn",APNID);
				custstatus ="";
				sess.setAttribute("custstatus","");
				session.setAttribute("testapn",APNID);
				Custdate  ="";
				sess.setAttribute("custDate","");	
				session.setAttribute("dtestapn",APNID);
				Productstatus ="";
				session.setAttribute("Productstatus","");
				session.setAttribute("testapn",APNID);
				Productdate  ="";
				sess.setAttribute("productDate","");	
				session.setAttribute("dtestapn",APNID);
	}
	
} 

 System.out.println(" APNID : " + APNID  );


%>
<SCRIPT LANGUAGE="javascript">
function newWindow(  htmFile  ) {
   msgWindow = window.open( htmFile, "WindowName", "left=20,top=20,width= 700,height=500,toolbar=0,scrollbar=1,resizable=1,location=0,status=0");
}

</SCRIPT>
<%
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
APNID = request.getParameter("pedId");
String sessionID = request.getParameter("sessionID");

System.out.println("***********sessionID is :"+sessionID);
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
xQuery = xQuery + "<td class='td-menu'>{data($b/From/LicenseNumber)}</td></tr></table> ";
						
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
xQuery = xQuery + "<td class='td-menu'>{data($b/To/LicenseNumber)}</td></tr></table> ";
						
xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	ToAddr = new String(xmlResults);
}

//GET Total Products
xQuery = "for $b in collection('tig:///ePharma/APN')/APN ";
xQuery = xQuery + "where $b/DocumentId = '"+APNID+"'  ";
xQuery = xQuery + "return  ";
xQuery = xQuery + "count($b/Pedigrees/Pedigree/Products/Product)";

xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	totProducts = new String(xmlResults);
}

//GET Product LineItem Info
xQuery = "let $maxtime  := (for $s in collection('tig:///ePharma/APN')/APN[DocumentId = '"+APNID+"']/Pedigrees/Pedigree/IssueDate ";
xQuery = xQuery + "order by $s descending  return data($s))[1]";
xQuery = xQuery + "for $b in collection('tig:///ePharma/APN')/APN ";
xQuery = xQuery + "where $b/DocumentId = '"+APNID+"'  ";
xQuery = xQuery + "return  ";
xQuery = xQuery + "let $prds := $b/Pedigrees/Pedigree[IssueDate = $maxtime]/Products ";
xQuery = xQuery + "let $mftr := $b/Pedigrees/Pedigree/Manufacturer ";
xQuery = xQuery + "for $p in $prds/Product ";
xQuery = xQuery + "order by $p/BrandName ";
xQuery = xQuery + "return ";
xQuery = xQuery + "<tr class='tableRow_On'> ";
xQuery = xQuery + "<td class='td-menu'><A HREF='ePedigree_Manager_Verify_Product.jsp?prdid={data($p/NDC)}'>{data($p/ProductName)}</A></td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/NDC)}</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($mftr[LicenseNumber=data($p/ManufacturerLicense)]/Name)}</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/Quantity)}</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/LotNumber)}</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/LotExpireDate)}</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/EPC)}</td> ";
xQuery = xQuery + "</tr>";

System.out.println("********Query to get the products is :"+xQuery);
									
xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	PRODS = new String(xmlResults);
}

//GET Total Manufacturers
xQuery = "for $b in collection('tig:///ePharma/APN')/APN ";
xQuery = xQuery + "where $b/DocumentId = '"+APNID+"'  ";
xQuery = xQuery + "return  ";
xQuery = xQuery + "count($b/Pedigrees/Pedigree/Manufacturer)";

xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	totManufacturers = new String(xmlResults);
}

xQuery = "for $p in collection('tig:///ePharma/APN')/APN ";
xQuery = xQuery + "where $p/DocumentId = '"+APNID+"'  ";
xQuery = xQuery + "return ";
xQuery = xQuery + "<tr class='tableRow_On'> ";
xQuery = xQuery + "<td class='td-menu'><A href='ViewShipManufacturerDetail.do?mfrName={data($p/Pedigrees/Pedigree/Manufacturer/Name)}{codepoints-to-string(38)}pagenm="+pagenm+"{codepoints-to-string(38)}tp_company_nm="+tp_company_nm+"{codepoints-to-string(38)}pedId="+APNID+"{codepoints-to-string(38)}sessionID="+sessionID+"'>{data($p/Pedigrees/Pedigree/Manufacturer/Name)}</A></td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/Pedigrees/Pedigree/Manufacturer/Address)}</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/Pedigrees/Pedigree/Manufacturer/Contact)}</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/Pedigrees/Pedigree/Manufacturer/ContactPhone)}</td> ";
xQuery = xQuery + "<td class='td-menu'><a href='mailto:{data($p/Pedigrees/Pedigree/Manufacturer/ContactEmail)}'>{data($p/Pedigrees/Pedigree/Manufacturer/ContactEmail)}</a></td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/Pedigrees/Pedigree/Manufacturer/LicenseNumber)}</td> ";
xQuery = xQuery + "</tr>";

xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	MANUFACT = new String(xmlResults);
}

//GET Total Custodians
xQuery = "for $b in collection('tig:///ePharma/APN')/APN ";
xQuery = xQuery + "where $b/DocumentId = '"+APNID+"'  ";
xQuery = xQuery + "return  ";
xQuery = xQuery + "count($b/Pedigrees/Pedigree/Custody)";

xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	totCustodians = new String(xmlResults);
}

xQuery = "for $p in collection('tig:///ePharma/APN')/APN ";
xQuery = xQuery + "where $p/DocumentId = '"+APNID+"'  ";
xQuery = xQuery + "return ";
xQuery = xQuery + "<tr class='tableRow_On'> ";
xQuery = xQuery + "<td class='td-menu'><A href='ViewShippingCustodianDetail.do?name={data($p/Pedigrees/Pedigree/Custody/Name)}{codepoints-to-string(38)}pagenm="+pagenm+"{codepoints-to-string(38)}tp_company_nm="+tp_company_nm+"{codepoints-to-string(38)}pedId="+APNID+"{codepoints-to-string(38)}sessionID="+sessionID+"'>{data($p/Pedigrees/Pedigree/Custody/Name)}</A></td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/Pedigrees/Pedigree/Custody/Address)}</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/Pedigrees/Pedigree/Custody/Contact)}</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/Pedigrees/Pedigree/Custody/AuthenticatorPhone)}</td> ";
xQuery = xQuery + "<td class='td-menu'><a href='mailto:{data($p/Pedigrees/Pedigree/Custody/AuthenticatorEmail)}'>{data($p/Pedigrees/Pedigree/Custody/AuthenticatorEmail)}</a></td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/Pedigrees/Pedigree/Custody/InCustodyFromDate)} - {data($p/Pedigrees/Pedigree/Custody/InCustodyToDate)}</td> ";
xQuery = xQuery + "</tr>";

xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	CUSTODY = new String(xmlResults);
}

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
														<TD class="type-whrite"  bgcolor="gold" align="center"><STRONG><A href="ePedigree_ShipManager_Reconcile.jsp?pedId=<%=APNID%>&sessionID=<%=sessionID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree"><FONT color="#000099">Summary</FONT></A></STRONG></TD>
														<TD class="type-whrite" align="center"><STRONG><a href="ePedigree_ShipManager_FromTo.jsp?pedId=<%=APNID%>&sessionID=<%=sessionID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree"><FONT color="#000099">From-To</FONT></a></STRONG>
														</TD>
														<TD class="type-whrite"  align="center"><STRONG><a href="shipProductList.do?pedId=<%=APNID%>&sessionID=<%=sessionID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree"><FONT color="#000099">Products</FONT></a></STRONG>
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
											<TD class="type-whrite" width="84">Pedigrees (Total: )</TD>
											<TD class="type-whrite" width="91">Manufacturers (Total: )</TD>
											<TD class="type-whrite" width="84">Custodians (Total: )</TD>
											<TD class="type-whrite" width="83">Products (Total: )</TD>
											<TD class="type-whrite" align="center" valign="middle">
												<TABLE width="301" cellSpacing="1" cellPadding="1" border="1" height="20">
													<TR>
														<TD class="type-whrite" colspan="4" align="center">Violations</TD>
													</TR>
												</TABLE>
											</TD>
										</TR>
										<TR class="tableRow_Off">
											<TD class="td-menu"><STRONG><FONT color="#3300cc">STATUS</FONT></STRONG></TD>
											<%if(APNStatus==null){APNStatus= "";%>
											<TD class="td-menu" width="83">
												<P align="center"><STRONG><FONT color="#009900"><%=APNStatus %></FONT></STRONG></P>
											</TD>
											<%}else{%>
											<TD class="td-menu" width="83">
												<P align="center"><STRONG><FONT color="#009900"><%=APNStatus %></FONT></STRONG></P>
											</TD>
											<%}%>
											<%if(PedigreeStatus==null){%>
											<TD class="td-menu" width="83">
												<P align="center"><STRONG><FONT color="#009900">&nbsp;</FONT></STRONG></P>
											</TD>
											<%}else{%>
											<TD class="td-menu" width="83">
												<P align="center"><STRONG><FONT color="#009900"><%=PedigreeStatus%></FONT></STRONG></P>
												
											</TD>
											<%}%>
											<%if(mfgstatus==null){%>
											<TD class="td-menu" width="83">
												<P align="center"><STRONG><FONT color="#009900">&nbsp;</FONT></STRONG></P>
											</TD>
											<%}else{%>
											<TD class="td-menu" width="84">
												<P align="center"><STRONG><FONT color="#009900"><%=mfgstatus%></FONT></STRONG></P>
											</TD>
											<%}%>
											<%if(custstatus==null){%>
											<TD class="td-menu" width="83">
												<P align="center"><STRONG><FONT color="#009900">&nbsp;</FONT></STRONG></P>
											</TD>
											<%}else{%>
											<TD class="td-menu" width="91">
												<P align="center"><STRONG><FONT color="#009900"><%=custstatus%></FONT></STRONG></P>
											</TD>
											<%}%>
											<%if(Productstatus==null){%>
											<TD class="td-menu" width="83">
												<P align="center"><STRONG><FONT color="#009900">&nbsp;</FONT></STRONG></P>
											</TD>
											<%}else{%>
											<TD class="td-menu" width="84">
												<P align="center"><STRONG><FONT color="#009900"><%=Productstatus%></FONT></STRONG></P>
											</TD>
											<%}%>
											
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
														<TD class="type-whrite">Other
														</TD>
													</TR>
													<TR class="tableRow_On">
														<TD width="51">
															<P align="center"><STRONG><FONT color="#669933">&nbsp;</FONT></STRONG></P>
														</TD>
														<TD>
															<P align="center"><FONT color="#ff0066" size="3"><STRONG>&nbsp;</STRONG></FONT></P>
														</TD>
														<TD>
															<P align="center"><STRONG><FONT color="#669933">&nbsp;</FONT></STRONG></P>
														</TD>
														<TD>
															<P align="center"><STRONG><FONT color="#669933">&nbsp;</FONT></STRONG></P>
														</TD>
														<TD><STRONG><FONT color="#669933">&nbsp;</FONT></STRONG>
														</TD>
													</TR>
												</TABLE>
											</TD>
										</TR>
										<TR class="tableRow_On">
											<TD class="td-menu" height="13"><STRONG><FONT color="#3300cc">DATE</FONT></STRONG></TD>
											<%if(APNdate==null){%>
											<TD class="td-menu" width="83" height="13">&nbsp;</TD>
											<%}else{%>
											<TD class="td-menu" width="83" height="13"><%=APNdate%></TD>
											<%}%>
											<%if(Pedigreedate==null){%>
											<TD class="td-menu" width="83" height="13">&nbsp;</TD>
											<%}else{%>
											<TD class="td-menu" width="84" height="13"><%=Pedigreedate%></TD>
											<%}%>
											<%if(Mfgdate==null){%>
											<TD class="td-menu" width="83" height="13">&nbsp;</TD>
											<%}else{%>
											<TD class="td-menu" width="84" height="13"><%=Mfgdate%></TD>
											<%}%>
											<%if(Custdate==null){%>
											<TD class="td-menu" width="83" height="13">&nbsp;</TD>
											<%}else{%>
											<TD class="td-menu" width="91" height="13"><%=Custdate%></TD>
											<%}%>
											<%if(Productdate==null){%>
											<TD class="td-menu" width="83" height="13">&nbsp;</TD>
											<%}else{%>
											<TD class="td-menu" width="83" height="13"><%=Productdate%></TD>
											<%}%>
											<TD class="td-menu" height="13">
												<P align="center"></P>
											</TD>
										</TR>
										<TR class="tableRow_Off">
											<TD class="td-menu"><STRONG><FONT color="#3300cc"></FONT></STRONG></TD>
											<TD class="td-menu" width="83"><STRONG><FONT color="#0000ff"><A href='SignPedigreeShip.do?sect=summary&pedId=<%=APNID%>&partId=APN&sessionID=<%=sessionID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree'><FONT color="#0000ff"><STRONG>SIGN</STRONG></FONT></A></FONT></STRONG></TD>
											<TD class="td-menu" width="84"><STRONG><FONT color="#0000ff"><A href='SignPedigreeShip.do?sect=summary&pedId=<%=APNID%>&partId=Pedigrees&sessionID=<%=sessionID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree'><FONT color="#0000ff"><STRONG>SIGN</STRONG></FONT></A></FONT></STRONG></TD>
											<TD class="td-menu" width="91"><STRONG><FONT color="#0000ff"><A href='SignPedigreeShip.do?sect=summary&pedId=<%=APNID%>&partId=Manufacturers&sessionID=<%=sessionID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree'><FONT color="#0000ff"><STRONG>SIGN</STRONG></FONT></A></FONT></STRONG></TD>
											<TD class="td-menu" width="84"><STRONG><FONT color="#0000ff"><A href='SignPedigreeShip.do?sect=summary&pedId=<%=APNID%>&partId=Custodians&sessionID=<%=sessionID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree'><FONT color="#0000ff"><STRONG>SIGN</STRONG></FONT></A></FONT></STRONG></TD>
											<TD class="td-menu" width="83"><STRONG><FONT color="#0000ff"><A href='SignPedigreeShip.do?sect=summary&pedId=<%=APNID%>&partId=Products&sessionID=<%=sessionID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree'><FONT color="#0000ff"><STRONG>SIGN</STRONG></FONT></A></FONT></STRONG></TD>
											<TD class="td-menu">
												<P align="center"><STRONG><FONT color="#0000ff">SIGN</FONT></STRONG></P>
											</TD>
										</TR>
										
									</TABLE>
								</TD>
							</TR>
							<TR>
								<TD colSpan="2"><BR>
									<A onclick="MM_openBrWindow('ePedigree_Manager_Create4b.html','APN','scrollbars=yes,resizable=yes,width=800,height=600')"
										href="#"><IMG height="27" hspace="10" src="../../assets/images/print.gif" width="27" border="0"></A>EXPORT 
									AS: <INPUT id="Submit1" type="submit" value="XML" name="Submit1">&nbsp;&nbsp; <INPUT id="Submit2" type="submit" value="CSV" name="Submit1">&nbsp;&nbsp;&nbsp;<INPUT id="Submit3" type="submit" value="EDI" name="Submit1">&nbsp;&nbsp;
									<INPUT id="Button1" onclick="MM_openBrWindow('DH2129_PedigreeForm.pdf','APN','scrollbars=yes,resizable=yes,width=800,height=600')"
										type="button" value="PDF" name="Button1">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<A onclick="MM_openBrWindow('ePedigreeSubmitScan.html','APN','scrollbars=yes,resizable=yes,width=800,height=600')"><INPUT id="Submit4" type="" value="Submit Scanned Copy" name="Submit4"></a>
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
