<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@include file='../../includes/jspinclude.jsp'%>

<%
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String sessionID = request.getParameter("sessionID");
String pagenm = request.getParameter("pagenm");
String searchKey = request.getParameter("searchKey");
String criteria = request.getParameter("criteria");
String tp_company_nm = request.getParameter("tp_company_nm");
String tp_company_id = request.getParameter("tp_company_id");

String xmlResultString = "";
String redirURL = "LoginFailed.html";
String sessQuery = "";

String FindResults = "";
String FindQuery = "";
String FindCriteria = "";

FindCriteria = "Search on: "+criteria+" , for value: "+searchKey;

com.rdta.tlapi.xql.Connection connection = ConnectTL();
com.rdta.tlapi.xql.Statement statement = getStatement(connection);


if(criteria.equals("Pedigree")) {
	FindQuery = "for $ped in collection('tig://root/ePharma/APN'), $e in collection('tig://root/EAGRFID/FilteredEvents') ";
	FindQuery = FindQuery + "where $e/RDTA-Raw-Event/Observations/Observation/TagID = distinct-values($ped/APN/Pedigrees/Pedigree/Products/Product/EPC) ";
	FindQuery = FindQuery + "and $ped/APN/DocumentId = '"+searchKey+"' " ;
	if(!tp_company_nm.equals("")) //IF TRADE PARTNER USER - SHOW ONLY THEIR APNS
	{
		FindQuery = FindQuery + "and ($ped/APN/From/Name = '"+tp_company_nm+"' or $ped/APN/To/Name = '"+tp_company_nm+"')" ;
	}
	
} else if(criteria.equals("SSCC") || criteria.equals("EPC") || criteria.equals("SGTIN")) {
	FindQuery = "for $e in collection('tig://root/EAGRFID/FilteredEvents') ";
	FindQuery = FindQuery + "where $e/RDTA-Raw-Event/Observations/Observation/TagID = '"+searchKey+"' " ;
	
		
} else if(criteria.equals("OrderID") || criteria.equals("InvoiceNum") || criteria.equals("DespatchAdv")) {
	FindQuery = "for $ped in collection('tig://root/ePharma/APN'), $e in collection('tig://root/EAGRFID/FilteredEvents') ";
	FindQuery = FindQuery + "where $e/RDTA-Raw-Event/Observations/Observation/TagID = distinct-values($ped/APN/Pedigrees/Pedigree/Products/Product/EPC) ";
	FindQuery = FindQuery + "and $ped/APN/To/TransactionNumber = '"+searchKey+"' " ;
	if(!tp_company_nm.equals("")) //IF TRADE PARTNER USER - SHOW ONLY THEIR APNS
	{
		FindQuery = FindQuery + "and ($ped/APN/From/Name = '"+tp_company_nm+"' or $ped/APN/To/Name = '"+tp_company_nm+"')" ;
	}
	
} 

FindQuery = FindQuery + "return ( <TR class='tableRow_Off'> ";
FindQuery = FindQuery + "<TD class='td-menu'>{data($e/RDTA-Raw-Event/Observations/Observation/LastSeenTime)}</TD> ";	
FindQuery = FindQuery + "<TD class='td-menu'>{data($e/RDTA-Raw-Event/Observations/Observation/TagID)}</TD> ";
FindQuery = FindQuery + "<TD class='td-menu'>{ ";
FindQuery = FindQuery + "for $l in collection('tig://root/EAGRFID/LocationDefinitions') ";
FindQuery = FindQuery + "where $l/LocationDef/LocationDetail/devices/device/deviceID = $e/RDTA-Raw-Event/Observations/Observation/ReaderID ";
FindQuery = FindQuery + "return data($l/LocationDef/LocationDetail/locationName) ";
FindQuery = FindQuery + "}</TD> ";
FindQuery = FindQuery + "<TD class='td-menu'>{ ";
FindQuery = FindQuery + "for $p in collection('tig://root/EAGRFID/Products') ";
FindQuery = FindQuery + "where $p/Product/TagID = $e/RDTA-Raw-Event/Observations/Observation/TagID ";
FindQuery = FindQuery + "return data($p/Product/ProductName) ";
FindQuery = FindQuery + "}</TD> ";
FindQuery = FindQuery + "<TD class='td-menu'>{ ";
FindQuery = FindQuery + "for $d in collection('tig://root/EAGRFID/Devices') ";
FindQuery = FindQuery + "where  $d/DeviceDef/DeviceDetail/deviceID = $e/RDTA-Raw-Event/Observations/Observation/ReaderID ";
FindQuery = FindQuery + "return data( $d/DeviceDef/DeviceDetail/deviceEvent) ";
FindQuery = FindQuery + "}</TD></TR>) ";
	
byte[] xmlResults = ReadTL(statement, FindQuery);
if (xmlResults != null) {
	FindResults = new String(xmlResults);
}

CloseConnectionTL(connection);

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>Raining Data ePharma - Track and Trace</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<link href="././assets/epedigree1.css" rel="stylesheet" type="text/css">
		<link href="xml-inline.css" rel="stylesheet" type="text/css">
			<script language="JavaScript" type="text/JavaScript">
<!--
function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}
//-->
			</script>
	</head>

<body>
<%@include file='/dist/epedigree/topMenu.jsp'%>

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr>
		<td><img src="././assets/images/space.gif" width="30" height="12"></td>
		<td rowspan="2">&nbsp;</td>
	</tr>
	<tr>
		<FORM action="results.jsp" method="post"><INPUT type="hidden"
			name="sessionID" value="<%=sessionID%>"> <INPUT type="hidden"
			name="pagenm" value="<%=pagenm%>"> <INPUT type="hidden"
			name="tp_company_nm" value="<%=tp_company_nm%>">
			<INPUT type="hidden"
			name="tp_company_id" value="<%=tp_company_id%>">

		<TR bgcolor="#8494ca">
			<TD class="type-whrite"><strong>Search Value:</strong></TD>
			<TD align="left"><INPUT id="Text2" type="text" name="searchKey" value=""></TD>
			<TD class="type-whrite"><STRONG>Value Type:</STRONG></TD>
			<TD valign="middle" colspan=2><SELECT id="Select1" name="criteria">
				<OPTION value="" selected>Select...</OPTION>
				<OPTION value="Pedigree">Pedigree Ref Num</OPTION>
				<OPTION value="SSCC">SSCC</OPTION>
				<OPTION value="EPC">EPC</OPTION>
				<OPTION value="SGTIN">SGTIN</OPTION>
				<OPTION value="OrderID">Order ID</OPTION>
				<option value="InvoiceNum">Invoice Num</option>
				<option value="DespatchAdv">Despatch Advice Num</option>
			</SELECT>
			</TD>
			
		</TR>
		<BR>
		<TR>
			<TD align="center" colSpan="5"><INPUT type="submit" class="fButton"
				value="LOCATE"></TD>
		</TR>
		<BR>
		</FORM>
	</tr>
	<tr>
		<table width="100%" id="Table1" cellSpacing="1" cellPadding="1"
			align="left" border="0" bgcolor="white">
			<tr bgcolor="#ffffff">
				<td align="left" colspan="2" class="td-typeblack">&nbsp;Object
				Observation Details</td>
				
				<td align="right" colspan="3" class="td-typeblack"><%=FindCriteria%></td>
			</tr>
			<TR bgcolor="#8494ca">
				<TD class="type-whrite"><strong>TIMESTAMP</strong></TD>
				<TD class="type-whrite"><STRONG>TAG VALUE</STRONG></TD>
				<TD class="type-whrite"><STRONG>LOCATION</STRONG></TD>
				<TD class="type-whrite"><STRONG>PRODUCT NAME</STRONG></TD>
				<TD class="type-whrite"><STRONG>EVENT</STRONG></TD>
			</TR>

			<%=FindResults%>

		</table>
	</tr>
</table>
</div>
</body>
</html>