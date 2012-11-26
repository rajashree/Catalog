<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="java.util.Date,com.rdta.commons.CommonUtil" %> 
<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Florida Department of Health</title>
<style type="text/css">
body, html{
	padding: 25px;
	margin: 0px;

	font-family:Arial, Helvetica, sans-serif;
	font-size: 18px;
	color:#333333;
}
p{
 	margin:0px;
	 padding:0px;
 	font-family:Arial, Helvetica, sans-serif;
	font-size: 18px;
}
.logotd{
	width: 314px;
	padding: 0 0 0 22px;
}
.fs22{
	font-size: 22px;
}
.fs16{
	font-size: 15px;
	font-weight: bold;
	font-style: italic;
}
.fs16a{
	font-size: 15px;
	font-weight: bold;

}
.st1{
	padding-top: 15px;
	padding-bottom: 15px;
}
.st1{
	padding-top: 15px;
	padding-bottom: 15px;
}
.st2{
	border: 1px solid #666666;
}
.st2 tr td{
	padding: 4px 10px 4px 10px;
		border: 1px solid #666666;
}
.st4{
	padding-right:40px;
	padding-top:15px;
}
.st5{
	padding: 15px;
	padding-right: 40px;
	border: 2px solid #666666;
}
.st6{
	padding:15px;
	border: 2px solid #666666;
}
</style>
</head>

<body>
<table width="1450"  border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><table width="100%"  border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="233" valign="top" class="logotd"><img src="IMG/health_logo.gif" width="223" height="89"></td>
        <td height="122"><table width="100%"  border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
          <tr>
            <td width="723" style="border-bottom-style:solid; border-bottom-color:black;" align="center"><bean:write name="pedigreeLegendForm" property="businessName"/> </td>
            <td>&nbsp;&nbsp;&nbsp;&nbsp;<em>(Repackagers Name)</em></td>
          </tr>
          <tr>
            <td><div align="center" class="fs22">PRESCRIPTION (LEGEND) DRUG PEDIGREE</div>
				<div align="center">History of Drug Sales and Distributions for a Repackaged Prescription Drug </div></td>
            <td>&nbsp;</td>
          </tr>
        </table></td>
        </tr>
    </table></td>
  </tr>
  <tr>
    <td>
	<div>Description of drug being distributed: </div>
	<div>Legend Drug Name, Strength, Dosage Form, Container Size: <u> <bean:write name="pedigreeLegendForm" property="drugName"/>
	
	<logic:present name="pedigreeLegendForm" property="strength">
	,</logic:present>
	
	<bean:write name="pedigreeLegendForm" property="strength"/>
	
	<logic:present name="pedigreeLegendForm" property="dosageForm">
	,</logic:present>
	
	<bean:write name="pedigreeLegendForm" property="dosageForm"/>
	
	<logic:present name="pedigreeLegendForm" property="containerSize">
	,</logic:present>
	
	<bean:write name="pedigreeLegendForm" property="containerSize"/> </u> </div>
	
	<div>Maufacturer / Repacker Name on container: <u> <bean:write name="pedigreeLegendForm" property="manufacturer"/> </u></div>
	<div>NDC on container (optional): <u> <bean:write name="pedigreeLegendForm" property="productCode"/> </u></div></td>
  </tr>
  <tr>
    <td><table width="100%"  border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="720" class="st1"><table width="100%" height="126"  border="1" cellpadding="0" cellspacing="0" class="st2">
          <tr>
            <td width="200" height="31"><strong>Lot Number </strong></td>
            <td width="160"><strong>Quantity</strong></td>
            <td><strong>Unique Serial# </strong></td>
          </tr>
          
          <logic:iterate name="pedigreeLegendForm" property="repackagedLotList" type="com.rdta.epharma.epedigree.action.print.RepackagedLotInfo" id="itemInfo">
          <tr>
            <td height="31"><bean:write name="itemInfo" property="lot"/></td>
            <td><bean:write name="itemInfo" property="quantity"/></td>
            <td><bean:write name="itemInfo" property="serialNumber"/></td>
          </tr>
          </logic:iterate>
          
          
        </table></td>
        <td valign="top" class="st4">&nbsp;</td>
        <td width="550" valign="top" class="st4"><table width="480" height="126" cellpadding="0" cellspacing="0">
          <tr>
            <td valign="top" class="st5">
			<div>Reference* Number: <u> <bean:write name="pedigreeLegendForm" property="identifier"/></u></div>
			<div>Document Type: <u> <bean:write name="pedigreeLegendForm" property="identifierType"/></u></div>
			<div>Reference* Date: <u> <bean:write name="pedigreeLegendForm" property="transactionDate"/></u></div>
			
			<div class="fs16">(related to the sale by the repacker identified above) </div>
			</td>
          </tr>
        </table></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td>History of the drugs included in the repackaged lot identified above: (include pedigrees for additional drugs on attached pages as necessary) </td>
  </tr>
  <tr>
    <td class="st1"><table width="100%"  border="0" cellspacing="0" cellpadding="0">
      <tr>
      
      	
      	
      	<%int index = 1; %>
        <td width="250" valign="top" class="st6">
        <logic:iterate name="pedigreeLegendForm" property="previousInfoList" type="com.rdta.epharma.epedigree.action.print.PreviousInfo" id="previousInfo">
          
        <% if( index >1 ){ %>
		 <div class="fs16">Repeat as applicable </div>
		 <%} index++; %>
		   
		<div>Lot Number: <u><bean:write name="previousInfo" property="lot"/></u></div>
		<div>Quantity: <u><bean:write name="previousInfo" property="quantity"/></u></div>
		<div>Manufacturer &amp; Information for authentication: __________<br>
		  <u><bean:write name="previousInfo" property="manufacturer"/></u><br>
		  <u><bean:write name="previousInfo" property="contactInfoName"/></u><br>
		  
		  <u><bean:write name="previousInfo" property="contactInfoTitle"/>
		  
		  <logic:present name="previousInfo" property="contactInfoTelphone">
		  ,</logic:present>
		  
		  <bean:write name="previousInfo" property="contactInfoTelphone"/></u><br>
		 
		 <u><bean:write name="previousInfo" property="contactInfoEmail"/>
		 
		 <logic:present name="previousInfo" property="contactInfoURL">
		  ,</logic:present>
		  
		 <bean:write name="previousInfo" property="contactInfoURL"/></u>
		 
		</div>
		<div>NDC: <u><bean:write name="previousInfo" property="productCode"/></u> </div>
		<div>Unique Serial#: <br>
		  <u><bean:write name="previousInfo" property="itemSerialNumber"/></u><br>
		  _______________________<br>
		  _______________________
		</div>
		 
		
		
		</logic:iterate>
		</td>
		
		
      	
      	
      	

        <td width="17">&nbsp;</td>
        <td valign="top" class="st6"><table width="100%"  border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="49%" align="center"><strong>OWNERSHIP HISTORY</strong> </td>
            <td width="17">&nbsp;</td>
            <td align="center"><strong>PHYSICAL DISTRIBUTION HISTORY (if different) </strong> </td>
          </tr>
          
          
          <tr>
            <td colspan="3"><em><strong>1. WHOLESALER / REPACKAGER THAT PURCHASED FROM THE MANUFACTURER </strong></em></td>
          </tr>
    
          
          
          
        <% int i = 1; %>
        <logic:iterate name="pedigreeLegendForm" property="custodyInfoList" type="com.rdta.epharma.epedigree.action.print.CustodyInfo" id="custodyInfo">
        
        <%if(i>1){%>
        	
        	<tr align="center">
            <td colspan="3"><em><strong>SUBSEQUENT WHOLESALE DISTRIBUTIONS (repeat as necessary) </strong></em></td>
            </tr>
        <%}%>
            
          
          <tr>
            <td width="49%" class="st6">
            <%if(i>1){%>
            <div><strong><%=i%>. &nbsp;&nbsp;&nbsp;#1 Above SOLD TO: </strong></div>
            <%}%>
			<div>Name: <u><bean:write name="custodyInfo" property="businessName"/></u>
			
			<logic:notPresent name="custodyInfo" property="businessName">______________________________________________</logic:notPresent>
			
			</div>
			
			<div>Address: <u><bean:write name="custodyInfo" property="businessAddressStreet1"/>
			
			<logic:notPresent name="custodyInfo" property="businessAddressStreet1">______________________________________________</logic:notPresent>
			<logic:present name="custodyInfo" property="businessAddressStreet2">
		 	 ,</logic:present>
			
			<bean:write name="custodyInfo" property="businessAddressStreet2"/></u><br>
			
			<u><bean:write name="custodyInfo" property="businessAddressCity"/>
			
			<logic:present name="custodyInfo" property="businessAddressStateOrRegion">
		 	 ,</logic:present>
		 	 
			<bean:write name="custodyInfo" property="businessAddressStateOrRegion"/>
			
			<logic:present name="custodyInfo" property="businessAddressPostalCode">
		 	 ,</logic:present>
			
			<bean:write name="custodyInfo" property="businessAddressPostalCode"/>
			
			<logic:present name="custodyInfo" property="businessAddressCountry">
		 	 ,</logic:present>
		 	 
			<bean:write name="custodyInfo" property="businessAddressCountry"/></u>
			 
			 <logic:notPresent name="custodyInfo" property="businessAddressCountry">______________________________________________</logic:notPresent>
			  </div>
			<div>Date Purchased &amp; Ref * #: <u><bean:write name="custodyInfo" property="transactionDate"/>
			
			<logic:present name="custodyInfo" property="identifierType">
		 	 ,</logic:present>
		 	 
			<bean:write name="custodyInfo" property="identifierType"/>
			
			<logic:present name="custodyInfo" property="identifier">
		 	 ,</logic:present>
		 	 
			<bean:write name="custodyInfo" property="identifier"/></u></div>
			<div>Print Name of Recipient: <u><bean:write name="custodyInfo" property="recipientName"/></u></div>
			<div>Signature of Recipient: <u><bean:write name="custodyInfo" property="recipientName"/></u> </div>
			<div>
			  <p></p>
			  <p>&nbsp;</p>
			</div>
			<div class="fs16" align="center">To authenticate a subsequent transaction, contact: </div>
			<div>Name: <u><bean:write name="custodyInfo" property="name"/></u> </div>
			<div>Telephone Number: <u><bean:write name="custodyInfo" property="telphone"/></u></div>
			<div>Email address: <u><bean:write name="custodyInfo" property="email"/></u></div>
			</td>
            <td width="17">&nbsp;</td>
            
            <td width="49%" class="st6">
           
           
            <% if( i > 1){%>
				<div align="center"><strong>SHIPPED TO </strong></div>
			<%}%>
            
			<div>Name: <u><bean:write name="custodyInfo" property="shippingBusinessName"/></u>
			
			<logic:notPresent name="custodyInfo" property="shippingBusinessName">______________________________________________</logic:notPresent>
			
			</div>
			
			<div>Address: <u><bean:write name="custodyInfo" property="shippingBusinessAddressStreet1"/>
			
			<logic:notPresent name="custodyInfo" property="shippingBusinessAddressStreet1">______________________________________________</logic:notPresent>
			<logic:present name="custodyInfo" property="shippingBusinessAddressStreet2">
		 	 ,</logic:present>
			
			<bean:write name="custodyInfo" property="shippingBusinessAddressStreet2"/></u><br>
			
			<u><bean:write name="custodyInfo" property="shippingBusinessAddressCity"/>
			
			<logic:present name="custodyInfo" property="shippingBusinessAddressStateOrRegion">
		 	 ,</logic:present>
		 	 
			<bean:write name="custodyInfo" property="shippingBusinessAddressStateOrRegion"/>
			
			<logic:present name="custodyInfo" property="shippingBusinessAddressPostalCode">
		 	 ,</logic:present>
			
			<bean:write name="custodyInfo" property="shippingBusinessAddressPostalCode"/>
			
			<logic:present name="custodyInfo" property="shippingBusinessAddressCountry">
		 	 ,</logic:present>
		 	 
			<bean:write name="custodyInfo" property="shippingBusinessAddressCountry"/></u>
			 
			 <logic:notPresent name="custodyInfo" property="shippingBusinessAddressCountry">______________________________________________</logic:notPresent>
			  </div>
			<div>Date Purchased &amp; Ref * #: <u><bean:write name="custodyInfo" property="shippingTransactionDate"/>
			
			<logic:present name="custodyInfo" property="shippingIdentifierType">
		 	 ,</logic:present>
		 	 
			<bean:write name="custodyInfo" property="shippingIdentifierType"/>
			
			<logic:present name="custodyInfo" property="shippingIdentifier">
		 	 ,</logic:present>
		 	 
			<bean:write name="custodyInfo" property="shippingIdentifier"/></u></div>
			<div>Print Name of Recipient: <u><bean:write name="custodyInfo" property="shippingRecipientName"/></u></div>
			<div>Signature of Recipient: <u><bean:write name="custodyInfo" property="shippingRecipientName"/></u> </div>
			<div>
			  <p></p>
			  <p>&nbsp;</p>
			</div>
			<div class="fs16" align="center">To authenticate a subsequent transaction, contact: </div>
			<div>Name: <u><bean:write name="custodyInfo" property="shippingName"/></u> </div>
			<div>Telephone Number: <u><bean:write name="custodyInfo" property="shippingTelphone"/></u></div>
			<div>Email address: <u><bean:write name="custodyInfo" property="shippingEmail"/></u></div>
			</td>
          </tr>
          <% i++; %>
          </logic:iterate>
          
        </table></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td>I swear or affirm that the information contained on this pedigree is accurate and complete and that prior sales and distributions have been authenticated, if required. </td>
  </tr>
  <tr>
    <td><table width="100%" height="75"  border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="400" height="21" valign="bottom">
        <p><logic:notEmpty property='signatureId' name='pedigreeLegendForm'>
          	<img src="<%=request.getContextPath()%>/servlet/showImageFromTL?dbname=EAGRFID&collname=UserSign&topnode=User&nodename=UserSign&UserID=<bean:write property='signatureId' name='pedigreeLegendForm'/>" />
			</logic:notEmpty></p>
          <p>Signature (authorised to bind the company)</p></td>
        <td width="35">&nbsp;</td>
        <td width="500" valign="bottom"><p><u><bean:write name="pedigreeLegendForm" property ="signerName"/>,&nbsp;<bean:write name="pedigreeLegendForm" property ="signerTitle"/></u></p>
          <p>Print Name and Title </p></td>
        <td width="35">&nbsp;</td>
        <td width="135" valign="bottom"><p><u><%= CommonUtil.dateToString(new Date())%></u></p>
          <p>Date</p></td>
        <td width="35">&nbsp;</td>
        <td><p>Page _________ of  _________</p>
          </td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td class="fs16a">&nbsp;</td>
  </tr>
  <tr>
    <td class="fs16a"><p class="fs16a">* Reference number may be an invoice, purcahse order, shipping document number or similar unique identifier for the transaction. </p>
    <p class="fs16a">Prescription (Legend) Drug Pedigree - Repacker DH 2135, 7/06 (obsoletes previous editions) </p></td>
  </tr>
</table>
</body>

<logic:present name="altPedigree">
<iframe src = "dist/epedigree/altPedigree.jsp" width="100%" height="100%"/>
</logic:present>

</html>
