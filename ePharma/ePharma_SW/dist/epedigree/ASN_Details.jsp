
<%@include file='../../includes/jspinclude.jsp'%>

<%
String tranType = request.getParameter("trType");
String tranNumber = request.getParameter("trNum");
System.out.println("order no:"+tranNumber);
String HTMLROW = "";
String TRANSROW = "";
String SHIPROW1 = "";
String SHIPROW2 = "";
String SHIPROW3 = "";
String LINEITEMROW = "";
byte[] xmlResults;
String tp_company_nm=request.getParameter("tp_company_nm");
String pagenm=request.getParameter("pagenm");
//String ASNNumber = request.getParameter("ASNNum").trim();

com.rdta.tlapi.xql.Connection connection = ConnectTL();
com.rdta.tlapi.xql.Statement statement = getStatement(connection);

String xQuery = "";	

xQuery = "for $b in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice  ";
xQuery = xQuery + "where $b/ID = '"+tranNumber+"' ";
xQuery = xQuery + "return ";
xQuery = xQuery + "<tr class='tableRow_On'> ";
xQuery = xQuery + "<td class='td-menu' width='25%'>{data($b/OrderReference/BuyersID)}</td> ";
xQuery = xQuery + "<td class='td-menu' width='25%'>{data($b/OrderReference/SellersID)}</td> ";
xQuery = xQuery + "<td class='td-menu' width='25%'>{data($b/IssueDate)}</td> "; //tr date
xQuery = xQuery + "<td class='td-menu' width='25%'>{data($b/Delivery/ActualDeliveryDateTime)}</td> ";  //ship date
xQuery = xQuery + "</tr> ";

System.out.println("query1: "+xQuery);
xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
TRANSROW = new String(xmlResults);
}

//GET buyer party Info
xQuery = "for $b in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice ";
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
xQuery = "for $b in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice ";
xQuery = xQuery + "where $b/ID = '"+tranNumber+"' ";
xQuery = xQuery + "return ";
xQuery = xQuery + "<tr class='tableRow_On'> ";
xQuery = xQuery + "<td class='td-menu' width='30%'>{data($b/SellerParty/Party/PartyName)}</td> ";
xQuery = xQuery + "<td class='td-menu' width='30%'>{concat($b/SellerParty/Party/Address/StreetName,' ',$b/SellerParty/Party/Address/CityName,' ',$b/SellerParty/Party/Address/CountrySubentityCode, ' ', $b/SellerParty/Party/Address/PostalZone)}</td> ";
xQuery = xQuery + "<td class='td-menu' width='20%'>{data($b/SellerParty/ShippingContact/Name)}</td> ";
xQuery = xQuery + "<td class='td-menu' width='20%'>{data($b/SellerParty/ShippingContact/Telephone)}</td> ";
xQuery = xQuery + "</tr> ";
xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
SHIPROW2 = new String(xmlResults);
}


//GET Product Info
xQuery = " let $lotno :=(  for $i in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice/DespatchLine/Item/AdditionalItemIdentification/PhysicalAttribute where $i/Description = 'LOTNO'	and $i/../../../../ID = '"+tranNumber+"' return data( $i/../ID)  )   ";
xQuery = xQuery + "for $b in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice ";
xQuery = xQuery + "where $b/ID = '"+tranNumber+"' ";
xQuery = xQuery + "return ";
xQuery = xQuery + "<tr class='tableRow_On'> ";
xQuery = xQuery + "<td class='td-menu' width='30%'>{data($b/DespatchLine/Item/SellersItemIdentification/ID)}</td> ";
xQuery = xQuery + "<td class='td-menu' width='30%'>{data($b/DespatchLine/Item/Description)}</td> ";
xQuery = xQuery + "<td class='td-menu' width='20%'>{data($b/DespatchLine/DeliveredQuantity)}</td> ";
xQuery = xQuery + "<td class='td-menu' width='20%'>{$lotno}</td> ";
xQuery = xQuery + "</tr> ";
xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
SHIPROW3 = new String(xmlResults);
}

CloseConnectionTL(connection);
%>


<html>
<head>
<title>Raining Data ePharma - ePedigree - Transaction Details</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">
<style type="text/css">

</style>
<script language="JavaScript" type="text/JavaScript">

</script>
</head>

<body>

	
<br><br><br>

<table width="100%" id="Table1" cellSpacing="1" cellPadding="1" align="left" border="0" bgcolor="white">
	
	<tr bgcolor="white">
		<td class="type-red" colspan="2">TRANSACTION DOCUMENT DETAILS: <%=tranType%></td>
	</tr>
	<tr bgcolor="white">
			<td class="type-red" colspan="2"><hr></td>
	</tr>
	<tr bgcolor="white">
		<td  colspan="2"><strong>TRANSACTION NUMBER: <%=tranNumber%></strong></td>
	</tr>
	<tr>
	<Td colspan="2">
	<table border="0" cellpadding="0" cellspacing="1" width="100%">
	<tr>
		<td class="type-red" colspan="6">Header Data</td>
	</tr>

	<tr class="tableRow_Header">
	<td class="type-whrite" width="25%">Buyer ID Number:</td>
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
			    <td class="type-whrite" width="30%">Seller Party Name:</td>
			    <td class="type-whrite" width="30%">Address:</td>
			     <td class="type-whrite" width="20%">Contact Name:</td>
			      <td class="type-whrite" width="20%">Contact Phone:</td>
			  </tr>
			   <%=SHIPROW2%><tr><td height="20"></td></tr>
			</table>
			</Td>
			</tr>
			
			
			<tr>
			<Td colspan="2">
			<table border="0" cellpadding="0" cellspacing="1" width="100%">
			  <tr>
			   <td class="type-red" colspan="11">Product Information </td>
			  </tr>
			  <tr class="tableRow_Header">
			    <td class="type-whrite" width="30%">Product Code:</td>
			    <td class="type-whrite" width="30%">Product Name:</td>
			     <td class="type-whrite" width="20%">Quantity:</td>
			      <td class="type-whrite" width="20%">Lotno:</td>
			  </tr>
			   <%=SHIPROW3%><tr><td height="20"></td></tr>
			</table>
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
