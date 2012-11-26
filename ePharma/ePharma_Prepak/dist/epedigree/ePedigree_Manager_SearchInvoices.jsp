<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.catalog.Constants" %>
<%@ page import="com.rdta.epharma.epedigree.action.SearchInvoicesForm"%>
<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.commons.xml.XMLUtil"%>
<%@ page import="java.io.InputStream"%>


<%@ include file='../../includes/jspinclude.jsp'%>

<% String status = "";
	status = (String)request.getAttribute("status"); 
	String buttonname = (String)request.getAttribute("buttonname");
	System.out.println("Access Status in Search Invoices:  "+status);
 %>

<script language="JavaScript" type="text/JavaScript">

function clearForm() {

	document.forms[0].fromDT.value ="";
	document.forms[0].toDT.value ="";
	document.forms[0].lotNum.value ="";
	document.forms[0].prodNDC.value ="";
	document.forms[0].invoiceID.value = "";
	document.forms[0].apndocId.value ="";
	document.forms[0].tpName.value = "";
}

function submitform(){

var i = "<%=status%>";
var fdt=document.search.fromDT.value
var tdt=document.search.toDT.value

 if(fdt>tdt){
	    alert("fromDate  is less than toDate");
   	 	return false;
	}else{
	 	document.search.submit();
	}

if( i == "false" ){

  alert("Access Denied....!");
  return false;
}else{ 

	document.search.submit();
	return true;
}
}

</script>

<%
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();

String sessionID = (String)session.getAttribute("sessionID");
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");

if(pagenm == null) { pagenm = "";}
if(tp_company_nm == null) { tp_company_nm = "";}

com.rdta.tlapi.xql.Connection connection = ConnectTL();
com.rdta.tlapi.xql.Statement statement = getStatement(connection);

String query = null;
String tpNames = null;
byte[] xmlResult;

query = " for $i in collection('tig:///" + Constants.CATALOG_DB + "/" + Constants.TRADING_PARTNER_COLL + "')/TradingPartner";
query = query + " return <option value ='{data($i/name)}'>{data($i/name)}</option> ";
xmlResult = ReadTL(statement, query);

if(xmlResult != null) {
	tpNames = new String(xmlResult);
}

CloseConnectionTL(connection);


%>


<html>
	<head>
		<title>Raining Data ePharma - ePedigree</title><LINK href="../../assets/epedigree1.css" type="text/css" rel="stylesheet">
		<script language="JavaScript" src="gen_validatorv2.js" type="text/javascript"></script>
<script language="JavaScript" src="calendar.js"></script>
<script language="JavaScript" src="calendar-en.js"></script>
<script language="JavaScript" src="calendar-setup.js"></script>
<script language="JavaScript" src="callCalendar.js"></script>
<link href="/ePharma/assets/images/calendar-win2k-1.css" rel="stylesheet" type="text/css">
   

</head>
<script language="JavaScript" type="text/JavaScript">
</script>
	<body>
		
		
		<%@include file='topMenu.jsp'%>


			<% 
		
		     List list = (List)request.getAttribute("List"); 
		     System.out.println("list size *********** "+list.size());
		     
			String fromDT = request.getParameter("fromDT") == null ? "" : request.getParameter("fromDT");
			String toDT = request.getParameter("toDT") == null ? "" : request.getParameter("toDT");;
			String lotNum = request.getParameter("lotNum") == null ? "" : request.getParameter("lotNum");;
			String prodNDC = request.getParameter("prodNDC") == null ? "" : request.getParameter("prodNDC");;
			String invoiceIDReq = request.getParameter("invoiceID") == null ? "" : request.getParameter("invoiceID");;
			String apndocId = request.getParameter("apndocId") == null ? "" : request.getParameter("apndocId");;
			String tpName = request.getParameter("tpName") == null ? "" : request.getParameter("tpName");;
		
		%>
																	
			<table id="Table6" cellSpacing="0" cellPadding="0" width="100%" border="0">
				<tr>
					<td class="td-rightmenu" vAlign="middle" width="1%"><IMG height="10" src="../../assets/images/space.gif" width="10"></td>
					<!-- Messaging -->
					<td class="td-rightmenu" vAlign="middle" width="99%">
						<table id="Table7" cellSpacing="0" cellPadding="0" width="100%" border="0">
							<tr>
								<td align="left"></td>
								<td align="right"><!-- <img src="../../assets/images/3320.jpg" width="200" height="27"> --><IMG src="../../assets/images/space.gif" width="5"></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>
						<table id="Table8" cellSpacing="0" cellPadding="0" width="100%" border="0">
							<tr>
								<td><IMG height="12" src="../../assets/images/space.gif" width="30"></td>
								<td rowSpan="2">&nbsp;</td>
							</tr>
							<tr>
								<td>
									<!-- info goes here -->
									<table id="Table9" cellSpacing="1" cellPadding="1" width="100%" align="left" bgColor="white"
										border="0">
										<tr>
											<td align="left">
												<table id="Table11" cellSpacing="1" cellPadding="1" width="100%" align="left" bgColor="white"
													border="0">
													<TR>
														<TD class="td-typeblack">
														<form action="SearchInvoices.do?accesslevel=invoicesearch" name="search" >
															
															<html:hidden property="pagenm" value="<%=pagenm%>" />
															<html:hidden property="tp_company_nm" value="<%=tp_company_nm%>" />

															<table border="1" cellspacing="0" cellpadding="0" width="100%" align="left">
																<tr>
																<td colspan=8 align=center><FONT face="Arial" size="2"><STRONG>&nbsp;<FONT color="#009900"> Invoices Search</FONT></STRONG></FONT></td>
																</tr>
																<tr class="tableRow_Header">
																	<td class="type-whrite" align="left">From Date (yyyy-mm-dd)</td>
																	<td class="type-whrite" align="left">To Date (yyyy-mm-dd)</td>
																	<TD class="type-whrite" align="left">Lot #</TD>
																	<TD class="type-whrite" align="left">NDC #</TD>
																	<TD class="type-whrite" align="left">Invoice ID #</TD>
																	
																
																<TR class="tableRow_On">
																	<td><html:text size="20" value="<%= fromDT %>" property="fromDT" readonly="true"/><img src="<html:rewrite page='/IMG/img.gif' />" width="16" height="16" onclick="return showCalendar('fromDT', '%Y-%m-%d', '24', true);"></td>
																	<td><html:text size="20" value="<%= toDT %>" property="toDT" readonly="true"/><img src="<html:rewrite page='/IMG/img.gif' />" width="16" height="16" onclick="return showCalendar('toDT', '%Y-%m-%d', '24', true);"></td>
																	<td><html:text size="15" value="<%= lotNum %>"  property="lotNum" /></td>
																	<td><html:text size="15" value="<%= prodNDC %>"  property="prodNDC" /></td>
																	<td><html:text size="15" value="<%= invoiceIDReq %>"  property="invoiceID" /></td>
													
																</TR>
																</tr>
																
																<tr class="tableRow_Header">
																
																<TD class="type-whrite" align="left">PedigreeID</TD>
																<TD class="type-whrite" align="left">Trading Partner Name</TD>

																<TD class="type-whrite" align="left"></TD>
																<TD class="type-whrite" align="left"></TD>
																<TD class="type-whrite" align="left"></TD>
																</tr>
																<TR class="tableRow_On">
																
																<td><html:text size="20" value="<%= apndocId %>" property="apndocId" /></td>
																	
																	<td><html:select property="tpName" value="<%=tpName%>"><html:option value = "" >SelectOne</html:option>
																	
																	<% List tpNamesss = (List)session.getAttribute("tpNames");
																  	   System.out.println("tp names in jsp: "+tpNamesss);
																       for(int i=0;i<tpNamesss.size();i++){
																     %>
																	<html:option value ="<%=tpNamesss.get(i).toString()%>"><%=tpNamesss.get(i).toString()%></html:option>
																	<% } %>
																	
																	</html:select></td>
																	<td></td>
																	<td></td>
																	<td></td>

																</TR>
																<TR class="tableRow_Off">

																	<td colspan=8 align=center>
																	
																	<html:button value="Search" property="submit1" onclick="return submitform()" />
																	<html:button property="button1" value="Clear" onclick="return clearForm()" /> 
																	
																	</td>
																
																</TR>
															</table>
														</FORM>
														</TD>
													</TR>
													<tr bgColor="white">
														<td class="td-typeblack" colSpan="1">INVOICE DETAILS:</td>
													</tr>
													<tr>
														<td align="left">
															<!-- Dashboard Start -->
															<table id="Table12" cellSpacing="1" cellPadding="0" width="100%" align="center" border="0">
																	<tr class="tableRow_Header">

																	   <td class="type-whrite" noWrap align="center">Invoice ID</td>
																		<td class="type-whrite" noWrap align="center">Trading Partner</td>
																		<td class="type-whrite" noWrap align="center">Address</td>
																		<TD class="type-whrite" noWrap align="center">Issue Date</TD>
																		<TD class="type-whrite" align="center">Total No. Of NDCs</TD>
																		<TD class="type-whrite" align="center">Total Line Items</TD>
																		<td class="type-whrite" noWrap align="center"> Amount</td>

																	
																	</tr>
															<%System.out.println("list size in Search Invoices: "+list.size()); 
															if (list.size() == 0) { 
																	System.out.println("*******Inside no Data**********");
																	%>
																	<TR class='tableRow_Off'><TD class='td-content' colspan='9' align='center'>No data...</TD></TR>
																	
																	<% } else { 
																	%>
																	
																		
																	<logic:iterate name="<%=Constants.INVOICE_DETAILS%>" id="invoice">
																	
																	<tr class="tableRow_Off">
																		<td class='td-content' height='20'><a href='InvoiceDetails.do?pagenm=pedigree&tp_company_nm=&trNum=<bean:write name="invoice" property="invoiceNum"/>&trType=Invoice'> 
																		
																			<bean:write name="invoice" property="invoiceNum"/>
																		</td>	
																		
																		<td>
																			<bean:write name="invoice" property="tradingPartner"/>
																		</td>
																		<td>
																			<bean:write name="invoice" property="address"/>
																		</td>
																		<td>
																			<bean:write name="invoice" property="issueDate"/>
																		</td>
																		<td>
																			<bean:write name="invoice" property="numOfNDCs"/>
																		</td>
																		<td>
																			<bean:write name="invoice" property="numOfLineItems"/>
																		</td>
																		<td>
																			<bean:write name="invoice" property="amount"/>
																		</td>
																		
																	</tr>
																	</logic:iterate>	
																	
																	<% } %>
																			
													
																	<TR class="tableRow_On">
																		<TD align="left" colspan="9">&nbsp;</TD>
																	</TR>																
																	
														</table>
														</td>
													</tr>
													<tr>
														<TD align="left"></TD>
														<TD align="left"></TD>
													</tr>
												</table>
										
												<DIV></DIV>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<DIV></DIV>
						<div id="footer" class="td-menu">
							<table width="100%" height="24" border="0" cellpadding="0" cellspacing="0" ID="Table10">
								<tr>
									<td width="100%" height="24" bgcolor="#d0d0ff" class="td-menu" align="left">&nbsp;&nbsp;Copyright 
										2005. Raining Data.</td>
								</tr>
							</table>
						</div>
					</td>
				</tr>
			</table>
			<DIV><EM></EM>&nbsp;</DIV>
			</TD></TR></TABLE></div>
	</body>
</html>

