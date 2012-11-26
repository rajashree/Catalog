
<%@include file='../../includes/jspinclude.jsp'%>

<%
String tranType = request.getParameter("trType");
String tranNumber = request.getParameter("trNum");
String tp_company_nm=request.getParameter("tp_company_nm");
String pagenm=request.getParameter("pagenm");

System.out.println("order no:"+tranNumber);
String HTMLROW = "";
String TRANSROW = "";
String SHIPROW1 = "";
String SHIPROW2 = "";
String LINEITEMROW = "";
byte[] xmlResults;


com.rdta.tlapi.xql.Connection connection = ConnectTL();
com.rdta.tlapi.xql.Statement statement = getStatement(connection);

String xQuery = "";	

xQuery = "for $b in collection('tig:///ePharma/Orders')/Order ";
xQuery = xQuery + "where $b/BuyersID = '"+tranNumber+"' ";
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
xQuery = "for $b in collection('tig:///ePharma/Orders')/Order ";
xQuery = xQuery + "where $b/BuyersID = '"+tranNumber+"' ";
xQuery = xQuery + "return ";
xQuery = xQuery + "<tr class='tableRow_On'> ";
xQuery = xQuery + "<td class='td-menu' width='25%'>{data($b/BuyersID)}</td> ";
xQuery = xQuery + "<td class='td-menu' width='25%'>{data($b/SellersID)}</td> ";
xQuery = xQuery + "<td class='td-menu' width='25%'>{concat(fn:get-month-from-dateTime($b/IssueDate) cast as string,'/',fn:get-day-from-dateTime($b/IssueDate)  cast as string,'/',fn:get-year-from-dateTime($b/IssueDate)  cast as string)}</td> "; //tr date
xQuery = xQuery + "<td class='td-menu' width='25%'>{concat(fn:get-month-from-dateTime($b/Delivery/RequestedDeliveryDateTime) cast as string,'/',fn:get-day-from-dateTime($b/Delivery/RequestedDeliveryDateTime)  cast as string,'/',fn:get-year-from-dateTime($b/Delivery/RequestedDeliveryDateTime)  cast as string)}</td> ";  //ship date
xQuery = xQuery + "</tr> ";

System.out.println("query2: "+xQuery);
xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
TRANSROW = new String(xmlResults);
}

//GET buyer party Info
xQuery = "for $b in collection('tig:///ePharma/Orders')/Order ";
xQuery = xQuery + "where $b/BuyersID = '"+tranNumber+"' ";
xQuery = xQuery + "return ";
xQuery = xQuery + "<tr class='tableRow_On'> ";
xQuery = xQuery + "<td class='td-menu' width='40%'>{data($b/BuyerParty/PartyName)}</td> ";
xQuery = xQuery + "<td class='td-menu' width='30%'>{concat($b/BuyerParty/Address/StreetName,' ',$b/BuyerParty/Address/CityName,' ',$b/BuyerParty/Address/CountrySubentityCode, ' ', $b/BuyerParty/Address/PostalZone)}</td> ";
xQuery = xQuery + "<td class='td-menu' width='30%'>{data($b/BuyerParty/BuyerContact/Name)}</td> ";
xQuery = xQuery + "</tr> ";
xmlResults = ReadTL(statement, xQuery);
System.out.println("Query&&&&&&&&&&"+xQuery);
if(xmlResults != null) {
SHIPROW1 = new String(xmlResults);
}

//GET Seller party Info
xQuery = "for $b in collection('tig:///ePharma/Orders')/Order ";
xQuery = xQuery + "where $b/BuyersID = '"+tranNumber+"' ";
xQuery = xQuery + "return ";
xQuery = xQuery + "<tr class='tableRow_On'> ";
xQuery = xQuery + "<td class='td-menu' width='50%'>{data($b/SellerParty/PartyName)}</td> ";
xQuery = xQuery + "<td class='td-menu' width='50%'>{concat($b/SellerParty/Address/StreetName,' ',$b/SellerParty/Address/CityName,' ',$b/SellerParty/Address/CountrySubentityCode, ' ', $b/SellerParty/Address/PostalZone)}</td> ";
xQuery = xQuery + "</tr> ";
xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
SHIPROW2 = new String(xmlResults);
}

//GET LineItem Info
xQuery = "for $b in collection('tig:///ePharma/Orders')/Order ";
xQuery = xQuery + "where $b/BuyersID = '"+tranNumber+"' ";
xQuery = xQuery + "return ";
xQuery = xQuery + "<tr class='tableRow_On'> ";
xQuery = xQuery + "<td class='td-menu' width='25%'>{data($b/OrderLine/LineItem/Item/SellersItemIdentification/ID)}</td> ";
xQuery = xQuery + "<td class='td-menu' width='25%'>{data($b/OrderLine/LineItem/Item/Description)}</td> ";
xQuery = xQuery + "<td class='td-menu' width='25%'>{data($b/OrderLine/LineItem/Quantity)}</td> ";
xQuery = xQuery + "<td class='td-menu' width='25%'>{data($b/OrderLine/LineItem/LineExtensionAmount)}</td> ";
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
	<td class="type-whrite" width="25%">Order ID Number:</td>
	<td class="type-whrite" width="25%">Seller ID Number:</td>
	<td class="type-whrite" width="25%">Order Date:</td>
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
						<td class="type-red" colspan="9">Order Line Item Data</td>
					</tr>
					
					<tr class="tableRow_Header">			
					  <td class="type-whrite" width="25%">Item Identification Number:</td>
					  <td class="type-whrite" width="25%">Item Description:</td>
		 			  <td class="type-whrite" width="25%">Quantity Ordered:</td>
					  <td class="type-whrite" width="25%">Order Amount:</td>
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
