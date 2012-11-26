<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.catalog.Constants" %>
<%@ include file='../../includes/jspinclude.jsp'%>

<%
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");
String sessionID = (String)session.getAttribute("sessionID");
String pedigreeID =(String) request.getAttribute("PedigreeID");
String envId = request.getParameter("envId");
request.setAttribute("envId",envId);
if(pedigreeID == null )
pedigreeID = request.getParameter("PedigreeId");

System.out.println("Pedigree ID is "+pedigreeID);
session.setAttribute("pedigreeID",pedigreeID);

if(pagenm == null) { pagenm = "";}
if(tp_company_nm == null) { tp_company_nm = "";}
String xQuery="";
byte[] xmlResults;

com.rdta.tlapi.xql.Connection connection = ConnectTL();
com.rdta.tlapi.xql.Statement statement = getStatement(connection);
String pedigree="";
String pedId = (String)request.getAttribute("pedId");

%>

<script>

function newWindow(  htmFile  ) {
   msgWindow = window.open( htmFile, "WindowName", "left=20,top=20,width= 600,height=400,toolbar=0,scrollbar=1,resizable=1,location=0,status=0");
}

function MM_openBrWindow(theURL,winName,features) { 
alert
  window.open(theURL,winName,features);
}

 function newWindow1(  htmFile  ) {
		   msgWindow = window.open( htmFile, "WindowName", "left=20,top=20,width= 700,height=430,toolbar=0,scrollbar=1,resizable=1,location=0,status=0");
	  }

</script>

<html:html>
<head>
<title>Raining Data ePharma - ePedigree</title><LINK href="../../assets/epedigree1.css" type="text/css" rel="stylesheet">
</head>
<body>



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
								<td class="td-typeblack"></td>
							</tr>
							<tr class="tableRow_Header">
								<td class="type-whrite" colspan="2"><font size="2" ><STRONG>REPACKAGED PRODUCT  DETAILS:<STRONG></font></td>
							</tr>
							<tr class="tableRow_Header">
								<td colspan="2">
									<table width="100%" id="Table3" cellSpacing="1" cellPadding="1" align="left" border="0"
										bgcolor="white">
										<tr>
											<td colspan="5">
												<table border="1" cellpadding="1" cellspacing="1" width="100%">
         									
									</TR>
												
											</td>
									</tr>
								</td>
							   	</tr>
							   	<table border="1" cellpadding="1" cellspacing="1" width="100%">
							    <tr class="tableRow_Header">
								 		
								</table>
																	
						</table>
						
					
						
						</tr>
						
			<tr class="tableRow_Off">
			
				<td>
			
		
				</td>
				
			</tr>
							   <tr>
								<Td colspan="2"><br>
									<table border="0" cellpadding="0" cellspacing="1" width="100%" ID="Table7">
									</table>
									<TABLE id="Table9" cellSpacing="1" cellPadding="0" width="100%" border="0">
										<TR>
											<TD class="td-typeblack" colSpan="7">PREVIOUS PRODUCT DETAILS: <FONT color="#336600"></FONT></TD>
										</TR>
										<TR class="tableRow_Header">
											<TD class="type-whrite" height="13">Product Code</TD>
											<TD class="type-whrite" height="13">Manufacturer</TD>
											<TD class="type-whrite" height="13">Quantity</TD>
											<TD class="type-whrite" height="13">LotNumber</TD>
											<TD class="type-whrite" height="13">ExpirationDate</TD>
											</TR>
											  <logic:iterate name="previousProducts11" id="product" type="com.rdta.epharma.epedigree.action.PreviousProductForm">
   	    <tr class="tableRow_Off">
	    <td align="left"><bean:write name="product" property="productCode"/></td>
	    <td align="left"><bean:write name="product" property="manuFacture"/></td>
        <td align="left"><bean:write name="product" property="quantity"/></td>
	    <td align="left"><bean:write name="product" property="previousLot"/></td>
	    <td align="left"><bean:write name="product" property="previousExpDate"/></td>
	    </tr>
	    </logic:iterate>
											
											
											
																				
									</TABLE>
								</Td>
							</tr>
							
							 <tr>
								<Td colspan="2"><br>
									<table border="0" cellpadding="0" cellspacing="1" width="100%" ID="Table7">
									</table>
									<TABLE id="Table9" cellSpacing="1" cellPadding="0" width="100%" border="0">
										<TR>
											<TD class="td-typeblack" colSpan="7">NEW PRODUCT DETAILS :<FONT color="#336600"></FONT></TD>
										</TR>
										<TR class="tableRow_Header">
											 
											<TD class="type-whrite" height="13">Product Code</TD>
											<TD class="type-whrite" height="13">Manufacturer</TD>
											<TD class="type-whrite" height="13">Quantity</TD>
											<TD class="type-whrite" height="13">LotNumber</TD>
											<TD class="type-whrite" height="13">ExpirationDate</TD>
											
										</TR>
		<logic:iterate name="productInfo" id="product1" type="com.rdta.epharma.epedigree.action.RepackedForm">
	    <tr class="tableRow_Off">
			   
		       <td align="left"><bean:write name="product1" property="newNDC"/></td>
	    	   <td align="left"><bean:write name="product1" property="currentManufacture"/></td>
			    <td align="left"><bean:write name="product1" property="quantity"/></td>
	    	    <td align="left"><bean:write name="product1" property="lotNo"/></td>
   	    	    <td align="left"><bean:write name="product1" property="expDate"/></td>

	     </tr>
	    </logic:iterate>
										
										                        		</TABLE>
								</Td>
							</tr>
							
							
							
							<tr>
							  <TD colSpan="2"><BR>
							        <table border="0" cellpadding="0" cellspacing="1" width="100%" ID="Table7">
									
									
								</Td>
								<TD align="center">
									
									 <html:button property="close" value="Close" onclick="window.close()"/> 
								</TD>
							
							</tr>
							
							</table>			
										
							<tr>
							
							</tr>
			 </table>
			 </form>
			  </TD>
			 </tr>
	</table>	<%@include file='onLoad.jsp'%> 									
<jsp:include page="../globalIncludes/Footer.jsp" />
</body>
</html:html>
