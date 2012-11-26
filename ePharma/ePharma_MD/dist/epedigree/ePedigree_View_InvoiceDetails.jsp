<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.catalog.Constants" %>
<%@ page import="com.rdta.epharma.epedigree.action.EnvelopeForm"%>
<%@ include file='../../includes/jspinclude.jsp'%>


<%
String tranType = request.getParameter("trType");
String tranNumber = request.getParameter("trNum");
String tp_company_nm=request.getParameter("tp_company_nm");
String pagenm=request.getParameter("pagenm");
String sessionID = (String)session.getAttribute("sessionID");

System.out.println("order no:"+tranNumber);
%>

<html:html>
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
	<%@include file='topMenu.jsp'%>
	
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
	<logic:equal name="InvDetails" value="0">
	   <tr>       
	  	 <td>No records</td>
	   </tr>
	</logic:equal>
	
 	<logic:notEqual name="InvDetails" value="0">
	<logic:iterate name="<%=Constants.INVOICE_DETAILS%>" id="envd">
	<tr class="tableRow_Off">
 	<td><bean:write name="envd" property="invoiceNumber"/></td>
 	<td><bean:write name="envd" property="sellersID"/></td>
 	<td><bean:write name="envd" property="invoiceDate"/></td>
 	<td><bean:write name="envd" property="requestedDeliveryDate"/></td>
 	</tr>
 	</logic:iterate>	
 	</logic:notEqual>	
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
				<logic:equal name="InvDetails" value="0">
	   			<tr>       
	  	 		<td>No records</td>
	  			</tr>
				</logic:equal>
	
 				<logic:notEqual name="InvDetails" value="0">
				<logic:iterate name="<%=Constants.INVOICE_DETAILS%>" id="envd">
				<tr class="tableRow_Off">
 				<td><bean:write name="envd" property="buyerPartyName"/></td>
 				<td><bean:write name="envd" property="buyerAddress"/></td>
 				<td><bean:write name="envd" property="buyerContact"/></td>
 	  			</tr>
 				</logic:iterate>	
 				</logic:notEqual>	

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
			    <logic:equal name="InvDetails" value="0">
	   			<tr>       
	  	 		<td>No records</td>
	  			</tr>
				</logic:equal>
	
 				<logic:notEqual name="InvDetails" value="0">
				<logic:iterate name="<%=Constants.INVOICE_DETAILS%>" id="envd">
				<tr class="tableRow_Off">
 				<td><bean:write name="envd" property="sellerPartyName"/></td>
 				<td><bean:write name="envd" property="sellerAddress"/></td>
 				</tr>
 				</logic:iterate>	
 				</logic:notEqual>
			  
			  
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
				</Td>
			</tr>
			<logic:equal name="InvDetails" value="0">
	   		<tr>       
	  	 	<td>No records</td>
	   		</tr>
			</logic:equal>
	
 			<logic:notEqual name="InvDetails" value="0">
			<logic:iterate name="<%=Constants.INVOICE_DETAILS%>" id="envd">
			<tr class="tableRow_Off">
 			<td><bean:write name="envd" property="itemIdentificationNumber"/></td>
 			<td><bean:write name="envd" property="itemDescription"/></td>
 			<td><bean:write name="envd" property="quantityOrdered"/></td>
 			<td><bean:write name="envd" property="invoiceAmount"/></td>
 			</tr>
 			</logic:iterate>	
 			</logic:notEqual>
		
		</table> 
		
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
</html:html>
