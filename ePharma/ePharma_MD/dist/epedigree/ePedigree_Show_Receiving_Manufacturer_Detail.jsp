<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<%@ page import="com.rdta.catalog.Constants" %>
<%@ page import="java.util.List" %>

<html:html>
	<head>
		<title>Raining Data ePharma - ePedigree - Manufacturer Details</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">
			<style type="text/css"></style>
			
		<SCRIPT LANGUAGE="javascript">

			function newWindow(  htmFile  ) {
		   		 msgWindow = window.open( htmFile, "WindowName", "left=20,top=20,width= 600,height=400,toolbar=0,scrollbar=1,resizable=1,location=0,status=0");
			}
			
			function newWindow1(  htmFile  ) {
  				 msgWindow = window.open( htmFile, "WindowName", "left=20,top=20,width= 700,height=430,toolbar=0,scrollbar=1,resizable=1,location=0,status=0");
			}
		</SCRIPT>
	</head>
	
	<%
		String pedigreeID = request.getParameter("PedigreeId");
		String pagenm = request.getParameter("pagenm");
		String tp_company_nm = request.getParameter("tp_company_nm");	
		String sessionID = "";	
		if(pagenm == null){
			pagenm = "";
		}
		if(tp_company_nm == null){
			tp_company_nm = "";
		}	
		
	 %>
	<body >
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
								<td class="td-typeblack" align="right"><FONT color="#0000cc"></FONT>
								</td>
							</tr>
	  
							<tr class="tableRow_Header">
								<td colspan="2">
									<table width="100%" id="Table3" cellSpacing="1" cellPadding="1" align="left" border="0"
										bgcolor="white">
										<tr>
											<td colspan="5">
												<table border="1" cellpadding="1" cellspacing="1" width="100%">
                                                       <%@ include file='Head2.jsp'%>									                                                               
												</table>
											</td>
										</tr></td>
							   	 </tr>
							     <tr class="tableRow_Header">
							     <logic:iterate name="<%=Constants.SHIPPED_DETAILS%>" id="env" type= "com.rdta.epharma.epedigree.action.ShippingMfrDetailForm">
								<tr class="tableRow_Header">
									     	<td class="type-whrite">Pedigree ID :  <br><a href = "#" onclick="javascript:newWindow1('NewPedigreeDetails.do?PedigreeId=<bean:write name="env" property="pedigreeid"/>&pagenm=pedigree&tp_company_nm=&linkname=ShipSum')" class="typeblue3-link"><STRONG><bean:write name="env" property="pedigreeid"/></STRONG></a></td>
									     	<td class="type-whrite">Transaction Date : <br><FONT color="#ffff00"><STRONG>
									     	
									     	
									     	<% String DateAndTime = env.getDate();
											   String date[] = DateAndTime.split("T"); 
											   if(date.length>1){
												   String time = date[1];
												   String t[] = time.split("\\.");
											%>
									     	<%=date[0]%>
									     	</STRONG></FONT></td>
									     	
									     	<% }else { %>	<%=date[0]%></td><td class="type-whrite">Transaction Time:  <br>	<FONT color="#ffff00"><STRONG>
												N/A
											</STRONG></FONT></td>
									     	
									     	<% } %>
									     	
									     	
									     	
									     	<td class="type-whrite">Transaction Type: <br><FONT color="#ffff00"><STRONG><bean:write name="env" property="transactiontype"/></STRONG></FONT></td>
									     	<!-- <td class="type-whrite">Transaction # : <br><STRONG><FONT color="#ffff00">
									     	<% String trNum = env.getTrnsaction();
									     	   String trType = env.getTransactiontype(); 
									     	   if(trType.equals("PurchaseOrderNumber")){
									     	  %>
									     	<A HREF=" javascript:newWindow('ePedigree_ViewOrder.jsp?trType=PurchaseOrder&trNum=<bean:write name="env" property="trnsaction"/>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree')" class="typeblue3-link" ><bean:write name="env" property="trnsaction"/></A></FONT></STRONG></td>
									     	<%} else{ %>
									     	  
									     	<A HREF=" javascript:newWindow('ASN_Details.jsp?trType=DespatchAdvice&trNum=<%=trNum%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree')" class="typeblue3-link" >
									     	<bean:write name="env" property="trnsaction"/></A></FONT></STRONG></td> 
									     </tr>
									     <%}%>-->
								</tr>
								     </logic:iterate>							
									</table>
								</td>
							</tr>
						<tr>
								<Td colspan="2"><br>
									<table border="0" cellpadding="0" cellspacing="1" width="100%" ID="Table9">
									</table>
									<TABLE id="Table10" cellSpacing="1" cellPadding="0" width="100%" border="0">
										<TR>
											<TD class="td-typeblack" colSpan="4">Manufacturer Details :</TD>
										</TR>
										<TR>
											<TD align="left" vAlign="top"  bgColor="#a29fcb" colSpan="0" height="18"><STRONG><FONT color="#ffffff">Manufacturer</FONT></STRONG></TD>
										</TR>
										
										<tr class="tableRow_Off">
			
				<td>
				
				<logic:iterate name="<%=Constants.SHIPPED_DETAILS%>" id="from">
				<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td class="td-menu" height="15">Company: &nbsp;&nbsp;&nbsp;&nbsp;<STRONG><bean:write name="from" property="name"/></STRONG></td>
				</tr>
				<tr>
					<td class="td-menu" height="15">Address: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="from" property="line1"/><br>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="from" property="line2"/><br>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="from" property="city"/><br>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="from" property="state"/>
					,<bean:write name="from" property="country"/>
					&nbsp;&nbsp;<bean:write name="from" property="zip"/>
					
					</td>
				</tr>
                  
              	<tr>
              		<td class="td-menu" height="15">Contact: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="from" property="contact"/></td>
              	</tr>
              	
                <tr>
                	<td class="td-menu" height="15">Phone: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="from" property="phone"/></td>
                </tr>
                <tr>
                	<td class="td-menu" height="15">Email: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="from" property="email"/></td>
                </tr>
                <tr>
                	<td class="td-menu" height="15">License: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="from" property="license"/></td>
                </tr>
                </table>
                </logic:iterate>
                
				</td>
						  </TABLE>
								</Td>
							</tr>
							<TR>
							</TR>
						</table>
						<DIV></DIV>
						<div id="footer">
							<table width="100%" height="24" border="0" cellpadding="0" cellspacing="0" ID="Table19">
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
							
</html:html>							
