
<%@include file='../../includes/jspinclude.jsp'%>

<%
String tranType = request.getParameter("trType");
String tranNumber = request.getParameter("trNum");
String tp_company_nm=request.getParameter("tp_company_nm");
String pagenm=request.getParameter("pagenm");
String HTMLROW = "";
String TRANSROW = "";
String SHIPROW1 = "";
String SHIPROW2 = "";
String LINEITEMROW = "";
byte[] xmlResults;

System.out.println("**********Inside Invoice Details.jsp***************");
com.rdta.tlapi.xql.Connection connection = ConnectTL();
com.rdta.tlapi.xql.Statement statement = getStatement(connection);

String xQuery = "";	

xQuery = "for $b in collection('tig:///ePharma/Invoices')/Invoice ";
xQuery = xQuery + "where $b/ID = '"+tranNumber+"' ";
xQuery = xQuery + "return ";
xQuery = xQuery + "let $epcs := ($b/Container/Product/EPCs/EPC) ";
xQuery = xQuery + "for $e in $epcs ";
xQuery = xQuery + "return  ";
xQuery = xQuery + "concat(data($e),'<br>') ";		

System.out.println("query1: "+xQuery);
xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
HTMLROW = new String(xmlResults);
}

//GET Transaction Info
xQuery = "for $b in collection('tig:///ePharma/Invoices')/Invoice ";
xQuery = xQuery + "where $b/ID = '"+tranNumber+"' ";
xQuery = xQuery + "return ";
xQuery = xQuery + "<tr class='tableRow_On'> ";
xQuery = xQuery + "<td class='td-menu' width='25%'>{data($b/ID)}</td> ";
xQuery = xQuery + "<td class='td-menu' width='25%'>{data($b/OrderReference/SellersID)}</td> ";
xQuery = xQuery + "<td class='td-menu' width='25%'>{concat(fn:get-month-from-dateTime($b/IssueDate) cast as string,'/',fn:get-day-from-dateTime($b/IssueDate)  cast as string,'/',fn:get-year-from-dateTime($b/IssueDate)  cast as string)}</td> "; //tr date
xQuery = xQuery + "<td class='td-menu' width='25%'>{concat(fn:get-month-from-dateTime($b/Delivery/RequestedDeliveryDateTime) cast as string,'/',fn:get-day-from-dateTime($b/Delivery/RequestedDeliveryDateTime)  cast as string,'/',fn:get-year-from-dateTime($b/Delivery/RequestedDeliveryDateTime)  cast as string)}</td> ";  //ship date
xQuery = xQuery + "</tr> ";

System.out.println("query2: "+xQuery);
xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
TRANSROW = new String(xmlResults);
}

//GET buyer party Info
xQuery = "for $b in collection('tig:///ePharma/Invoices')/Invoice ";
xQuery = xQuery + "where $b/ID = '"+tranNumber+"' ";
xQuery = xQuery + "return ";
xQuery = xQuery + "<tr class='tableRow_On'> ";
xQuery = xQuery + "<td class='td-menu' width='40%'>{data($b/BuyerParty/Party/PartyName)}</td> ";
xQuery = xQuery + "<td class='td-menu' width='30%'>{concat($b/BuyerParty/Party/Address/StreetName,' ',$b/BuyerParty/Party/Address/CityName,' ',$b/BuyerParty/Party/Address/CountrySubentityCode, ' ', $b/BuyerParty/Party/Address/PostalZone)}</td> ";
xQuery = xQuery + "<td class='td-menu' width='30%'>{data($b/$b/BuyerParty/Party/BuyerContact/Name)}</td> ";
xQuery = xQuery + "</tr> ";
xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
SHIPROW1 = new String(xmlResults);
}

//GET Seller party Info
xQuery = "for $b in collection('tig:///ePharma/Invoices')/Invoice ";
xQuery = xQuery + "where $b/ID = '"+tranNumber+"' ";
xQuery = xQuery + "return ";
xQuery = xQuery + "<tr class='tableRow_On'> ";
xQuery = xQuery + "<td class='td-menu' width='50%'>{data($b/SellerParty/Party/PartyName)}</td> ";
xQuery = xQuery + "<td class='td-menu' width='50%'>{concat($b/SellerParty/Party/Address/StreetName,' ',$b/SellerParty/Party/Address/CityName,' ',$b/SellerParty/Party/Address/CountrySubentityCode, ' ', $b/SellerParty/Party/Address/PostalZone)}</td> ";
xQuery = xQuery + "</tr> ";
xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
SHIPROW2 = new String(xmlResults);
}

//GET LineItem Info
xQuery = "for $b in collection('tig:///ePharma/Invoices')/Invoice ";
xQuery = xQuery + "where $b/ID = '"+tranNumber+"' ";
xQuery = xQuery + "return ";
xQuery = xQuery + "<tr class='tableRow_On'> ";
xQuery = xQuery + "<td class='td-menu' width='25%'>{data($b/InvoiceLine/Item/SellersItemIdentification/ID)}</td> ";
xQuery = xQuery + "<td class='td-menu' width='25%'>{data($b/InvoiceLine/Item/Description)}</td> ";
xQuery = xQuery + "<td class='td-menu' width='25%'>{data($b/InvoiceLine/InvoicedQuantity)}</td> ";
xQuery = xQuery + "<td class='td-menu' width='25%'>{data($b/InvoiceLine/LineExtensionAmount)}</td> ";
xQuery = xQuery + "</tr> ";
xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
LINEITEMROW = new String(xmlResults);
}


CloseConnectionTL(connection);
%>


<html>
<head>
<title>Raining Data ePharma - ePedigree</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">
<style type="text/css">

</style>
<script language="JavaScript" type="text/JavaScript">

</script>
</head>

<body>
	
	
	<BR>

<table width="100%" id="Table1" cellSpacing="1" cellPadding="1" align="left" border="0" bgcolor="white">
	<tr bgcolor="white">
		<td class="type-red" colspan="2">TRANSACTION DOCUMENT DETAILS: <%=tranType%></td>
	</tr>
	<tr bgcolor="white">
			<td class="type-red" colspan="2"><hr></td>
	</tr>
	<tr>
	<Td colspan="2">
	<table border="0" cellpadding="0" cellspacing="1" width="100%">
	<tr>
		<td class="type-red" colspan="6">Header Data</td>
	</tr>

	<tr class="tableRow_Header">
	<td class="type-whrite" width="25%">Invoice Number:</td>
	<td class="type-whrite" width="25%">Seller ID Number:</td>
	<td class="type-whrite" width="25%">Invoice Date:</td>
	<td class="type-whrite" width="25%">Requested Delivery Date:</td>
	</tr>
	<%=TRANSROW%>
	</table>
	</Td></tr>
			<tr>
			<Td colspan="2"><br>
			<table border="0" cellpadding="0" cellspacing="1" width="100%">
			  <tr>
			   <td class="type-red" colspan="11">Buyer Party</td>
			  </tr>
			  <tr class="tableRow_Header">
			    <td class="type-whrite" width="40%">Buyer Party Name:</td>
		            <td class="type-whrite" width="30%">Address:</td>
		            <td class="type-whrite" width="30%">Contact:</td>

	                  </tr>

			   <%=SHIPROW1%>
			</table>
				</Td>
			</tr>
			
			<tr>
			<Td colspan="2"><br>
			<table border="0" cellpadding="0" cellspacing="1" width="100%">
			  <tr>
			   <td class="type-red" colspan="11">Seller Party</td>
			  </tr>
			  <tr class="tableRow_Header">
			    <td class="type-whrite" width="50%">Seller Party Name:</td>
			    <td class="type-whrite" width="50%">Address:</td>
			  </tr>

			   <%=SHIPROW2%>
			</table>
				</Td>
			</tr>
			
			<tr>
				<Td colspan="2"><br>
					<table border="0" cellpadding="0" cellspacing="1" width="100%">
					<tr>
						<td class="type-red" colspan="9">Invoice Line Item Data</td>
					</tr>
					
					<tr class="tableRow_Header">			
					  <td class="type-whrite" width="25%">Item Identification Number:</td>
					  <td class="type-whrite" width="25%">Item Description:</td>
		 			  <td class="type-whrite" width="25%">Quantity Ordered:</td>
					  <td class="type-whrite" width="25%">Invoice Amount:</td>
					</tr>
					<%=LINEITEMROW%>
					
				</Td>
			</tr>
<!--			<tr>
				<Td colspan="2"><br>
					<table border="0" cellpadding="0" cellspacing="1" width="100%">
 					<tr>
						<td class="type-red" colspan="2">EPC</td>
					</tr> -->
					<!-- <tr><td align="right" class="td-typegray" colspan="5">Displaying 1 - 25 of 141 <a href="#" class="typegray-link">Prev</a> | <a href="#" class="typegray-link">Next </a>|<a href="#" class="typegray-link"> View All</a></td>
			</tr> -->
	<!-- 				<tr class="tableRow_Header">
		<td class="type-whrite">&nbsp;</td>
	</tr>
	
	</table> 
	
					
				</Td>
			</tr>-->
			
		</table> 
		</td>
    </tr>
  </table>
  

<div id="footer"> 
  <table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
    <tr> 
      <td width="100%" height="24" bgcolor="#D0D0FF" class="td-menu" align="left">&nbsp;&nbsp;Copyright 2005. 
        Raining Data.</td>
    </tr>
  </table>
</div>
</body>
</html>
