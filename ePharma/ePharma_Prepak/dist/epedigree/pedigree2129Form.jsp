<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="java.util.Date,com.rdta.commons.CommonUtil" %> 
<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Untitled Document</title>
<style type="text/css">
<!--
body,td,th {
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 12px;
}
.style1 {
	font-size: 14px;
	font-weight: bold;
	color: #333333;
}

.style5 {font-size: 12px; font-weight: bold; font-style: italic; }
-->
</style></head>
<% System.out.println("In side Jsp file"); %>
<body>
<form id="form1" name="form1" method="post" action="">
  <table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr>
      <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="17%"><div align="center"><img src="IMG/health_logo.gif" alt="" name="HealthDepartment" width="125" height="50" id="HealthDepartment" style="background-color: #006699" /></div></td>
            <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td><div align="center"> <u> <bean:write name="pedigree2129Form" property="businessName"/></u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(Wholesaler's Name) </div></td>
                </tr>
                <tr>
                  <td><div align="center" class="style1">Prescription (Legend) Drug Pedigree &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                </tr>
            </table></td>
          </tr>
      </table></td>
    </tr>
    <tr>
      <td>Legend Drug Name, Strength, Dosage Form, Container Size: <u> <bean:write name="pedigree2129Form" property="drugName"/>
	
	<logic:present name="pedigree2129Form" property="strength">
	,</logic:present>
	
	<bean:write name="pedigree2129Form" property="strength"/>
	
	<logic:present name="pedigree2129Form" property="dosageForm">
	,</logic:present>
	
	<bean:write name="pedigree2129Form" property="dosageForm"/>
	
	<logic:present name="pedigree2129Form" property="containerSize">
	,</logic:present>
	
	<bean:write name="pedigree2129Form" property="containerSize"/> </u> 
	
	</td>
    </tr>
    <tr>
      <td><input name="textfield" type="text" size="5" /> 
        This is a repackaged drug (requires repackager's pedigree information and authentication of repackager's pedigree) </td>
    </tr>
    <tr>
      <td>NDC (Optional)<u> <bean:write name="pedigree2129Form" property="productCode"/> </u></td>
    </tr>
    <tr>
      <td><table width="100%" border="0" cellspacing="0" cellpadding="10">
        <tr>
          <td><table width="100%"  border="1" cellpadding="5" cellspacing="0">
            <tr>
              <td width="115" ><strong>Lot Number </strong></td>
              <td width="80"><strong>Quantity</strong></td>
              <td width="250"><strong>Unique Serial# </strong></td>
            </tr>
            <logic:iterate name="pedigree2129Form" property="itemInfoList" type="com.rdta.epharma.epedigree.action.print.ItemInfo" id="itemInfo">
          	<tr>
            	<td><bean:write name="itemInfo" property="lot"/></td>
            	<td><bean:write name="itemInfo" property="quantity"/></td>
            	<td><bean:write name="itemInfo" property="serialNumber"/></td>
          	</tr>
          	</logic:iterate>
          </table></td>
          <td><table width="100%" border="1" cellspacing="0" cellpadding="0">
            <tr>
              <td><table width="100%" border="0" cellpadding="5" cellspacing="0" bordercolor="#CCCCCC">
                <tr>
                  <td>Reference* Number:<u> <bean:write name="pedigree2129Form" property="identifier"/> </u> </td>
                </tr>
                <tr>
                  <td>Document Type: <u> <bean:write name="pedigree2129Form" property="identifierType"/> </u> </td>
                </tr>
                <tr>
                  <td>Reference* Date: <u> <bean:write name="pedigree2129Form" property="transactionDate"/> </u></td>
                </tr>
                <tr>
                  <td><em>(related to the sale by the repacker identified above) </em></td>
                </tr>
              </table></td>
            </tr>
          </table></td>
        </tr>
      </table>      </td>
    </tr>
    <tr>
      <td>
      
      <table width="100%"  border="1" cellspacing="0" cellpadding="10">
        <tr>
          <td width="462" align="center"><strong>OWNERSHIP HISTORY</strong> </td>
          <td><strong>PHYSICAL DISTRIBUTION HISTORY (if different) </strong> </td>
        </tr>
        <tr>
          <td colspan="2"><p>Manufacturer's Name : <u> <bean:write name="pedigree2129Form" property="manufacturerName"/> </u> <br />
                  <br />
            Manufacturer's Information for Authentication :<u> <bean:write name="pedigree2129Form" property="manufacturerContactName"/> </u> <br />
          </p></td>
        </tr>
        
        <tr>
          <td colspan="2"><em><strong> 1. Wholesaler that purchased from the Manufacturer or a Repackager ( which requires authentication ) </strong></em></td>
        </tr>
        

        
        
        <% int i = 1; %>
        <logic:iterate name="pedigree2129Form" property="custodyInfoList" type="com.rdta.epharma.epedigree.action.print.CustodyInfo" id="custodyInfo">
        <tr>
          <td class="st6">
          <table width="100%" border="0" cellspacing="0" cellpadding="15">
            <tr>
            
            <% if( i > 1){%>
            	<div><%=i%>. &nbsp;&nbsp;&nbsp;#1 Above <strong>SOLD TO</strong>: </div>
            <%}%>
            
              <td><div>Name:<u> <bean:write name="custodyInfo" property="businessName"/> </u> </div>
              
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
             
                <div>Print Name of Recipient:<u> <bean:write name="custodyInfo" property="recipientName"/> </u> </div>
                <div>Signature of Recipient: <u> <bean:write name="custodyInfo" property="recipientName"/> </u>
                </div>
                <div>Name of the Authenticator :<u> <bean:write name="custodyInfo" property="authenticatorName"/> </u> </div>
                <div>Signature of the Authenticator: <u> <bean:write name="custodyInfo" property="authenticatorName"/> </u> </div>
                <br />
                <br />
                <div align="center"><em><strong>To authenticate a subsequent transaction, contact: </strong></em></div>
                <div>Name: <u> <bean:write name="custodyInfo" property="name"/> </u> </div>
                <div>Telephone Number: <u> <bean:write name="custodyInfo" property="telphone"/> </u> </div>
                <div>Email address: <u> <bean:write name="custodyInfo" property="email"/> </u> </div></td>
            </tr>
            
          </table></td>
          <td class="st6"><table width="100%" border="0" cellspacing="0" cellpadding="15">
            <tr>
				<td>
				<% if( i > 1){%>
				<div align="center"><strong>SHIPPED TO </strong></div>
				<%}%>
				
				<DIV>
					<DIV>
						 Name:<U> <bean:write name="custodyInfo" property="shippingBusinessName" /></U>
					</DIV>
					<DIV>
						 Address: <U><bean:write name="custodyInfo" property="shippingBusinessAddressStreet1" />
						
						<logic:present name="custodyInfo" property="shippingBusinessAddressStreet2">,</logic:present> 
						<bean:write name="custodyInfo" property="shippingBusinessAddressStreet2" /></U>
						<BR>
						<U><bean:write name="custodyInfo" property="shippingBusinessAddressCity" />
						<logic:present name="custodyInfo" property="shippingBusinessAddressStateOrRegion">,</logic:present> 
							<bean:write name="custodyInfo" property="shippingBusinessAddressStateOrRegion" />
							<logic:present name="custodyInfo" property="shippingBusinessAddressPostalCode">,</logic:present> 
							<bean:write name="custodyInfo" property="shippingBusinessAddressPostalCode" />
							<logic:present name="custodyInfo" property="shippingBusinessAddressCountry">,</logic:present> 
							<bean:write name="custodyInfo" property="shippingBusinessAddressCountry" /></U>
							
					</DIV>
					<DIV>
						 Date Purchased &amp; Ref * #: <U><bean:write name="custodyInfo" property="shippingTransactionDate" />
						<logic:present name="custodyInfo" property="shippingIdentifierType">,</logic:present> 
						<bean:write name="custodyInfo" property="shippingIdentifierType" />
						<logic:present name="custodyInfo" property="shippingIdentifier">,</logic:present> 
						<bean:write name="custodyInfo" property="shippingIdentifier" /></U>
					</DIV>
					 <div>Print Name of Recipient:<u> <bean:write name="custodyInfo" property="shippingRecipientName"/> </u> </div>
                	  <div>Signature of Recipient: <u> <bean:write name="custodyInfo" property="shippingRecipientName"/> </u>
               		 </div>
                	<div>Name of the Authenticator :<u> <bean:write name="custodyInfo" property="shippingAuthenticatorName"/> </u> </div>
                     <div>Signature of the Authenticator: <u> <bean:write name="custodyInfo" property="shippingAuthenticatorName"/> </u> </div>
               
					
					<div>
					<div></div>
					</div>
					<br />
					<br />
					<div align="center"><em><strong>To authenticate a subsequent transaction, contact: </strong></em></div>
                	<div>Name: <u> <bean:write name="custodyInfo" property="shippingName"/> </u> </div>
                	<div>Telephone Number: <u> <bean:write name="custodyInfo" property="shippingTelphone"/> </u> </div>
                	<div>Email address: <u> <bean:write name="custodyInfo" property="shippingEmail"/> </u> </div>
					</td>
					</tr>
          </table></td>
        </tr>
        <% i++ ; %>
        </logic:iterate>
      </table></td>
    </tr>
    <tr>
      <td>I swear or affirm that the information contained on this pedigree is accurate and complete and that prior sales and distribution is authenticated, if required </td>
    </tr>
    <tr>
      <td><br />
      <br />
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="28%">
          <logic:notEmpty property='signatureId' name='pedigree2129Form'>
          	<img src="<%=request.getContextPath()%>/servlet/showImageFromTL?dbname=EAGRFID&collname=UserSign&topnode=User&nodename=UserSign&UserID=<bean:write property='signatureId' name='pedigree2129Form'/>" />
			</logic:notEmpty>__________________________________      
            Signature (Authorized to bind the company) </td>
         <td width="32%" align="center"><u><bean:write name="pedigree2129Form" property ="signerName"/>,&nbsp;<bean:write name="pedigree2129Form" property ="signerTitle"/></u><br />
            Print name and Title </td>
          <td width="19%">Date : <u><%= CommonUtil.dateToString(new Date())%></u><br /></td>
          <td width="20%">Page ____ of ____ pages. </td>
        </tr>
      </table></td>
    </tr>
    <tr>
      <td>*Reference Number should be identified as an invoice, purchase order, shipping document number or similar unique identifier. </td>
    </tr>
    <tr>
      <td>Prescription (legend) Drug Pedigree DH 2129, 7/06 (obsoletes previous editions). </td>
    </tr>
  </table>
</form>
</body>
<logic:present name="altPedigree">
<iframe src = "dist/epedigree/altPedigree.jsp" width="100%" height="100%"/>
</logic:present>
</html>
