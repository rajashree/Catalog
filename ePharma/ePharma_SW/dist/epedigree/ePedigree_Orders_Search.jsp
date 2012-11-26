<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.catalog.Constants" %>
<%@ page import="com.rdta.epharma.epedigree.action.OrderSearchForm"%>
<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.commons.xml.XMLUtil"%>
<%@ page import="java.io.InputStream"%>


<%@ include file='../../includes/jspinclude.jsp'%>

<% String status = (String)request.getAttribute("status"); 
	String buttonname = (String)request.getAttribute("buttonname");
	System.out.println("Access Status in Orders Search:  "+status);
 %>
 
 
<script language="JavaScript" type="text/JavaScript">

function clearForm() {

	document.forms[0].fromDT.value ="";
	document.forms[0].toDT.value ="";
	document.forms[0].lotNum.value ="";
	document.forms[0].prodNDC.value ="";
	document.forms[0].orderNum.value = "";
	document.forms[0].apndocId.value ="";
	document.forms[0].tpName.value = "";
	
}

function accessdenied() {
  var i = <%=status%>
  
  if(i)
  {	
  		alert("Access Denied.....!")
  	
  } 
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
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");
String SSCC = request.getParameter("SSCC");
//String sessionID = request.getParameter("sessionID");
String sessionID = (String)session.getAttribute("sessionID");
System.out.println("sess : "+sessionID);
String screenEnteredDate = (String)request.getAttribute("screenEnteredDate");
if(SSCC == null) { SSCC = "";}
if(pagenm == null) { pagenm = "";}
if(tp_company_nm == null) { tp_company_nm = "";}
System.out.println("*********Inside OrdersSearch jsp********");

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
System.out.println("tp names :"+tpNames);
CloseConnectionTL(connection);
%>
<html:html>
	<head>
		<title>Raining Data ePharma - ePedigree</title>
		<LINK href="../../assets/epedigree1.css" type="text/css" rel="stylesheet">
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
		     List list = (List)session.getAttribute("List"); 
		     System.out.println("The List object :"+list.toString());

			String fromDT = request.getParameter("fromDT") == null ? "" : request.getParameter("fromDT");
			String toDT = request.getParameter("toDT") == null ? "" : request.getParameter("toDT");;
			String lotNum = request.getParameter("lotNum") == null ? "" : request.getParameter("lotNum");;
			String prodNDC = request.getParameter("prodNDC") == null ? "" : request.getParameter("prodNDC");;
			String orderNumParam = request.getParameter("orderNum") == null ? "" : request.getParameter("orderNum");;
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
													
													
													<form action="OrderSearch.do?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>&accesslevel=ordersearch" name="search" method="post">

															<html:hidden property="sessionID" value="<%=sessionID%>" />
															<html:hidden property="pagenm" value="<%=pagenm%>" />
															<html:hidden property="tp_company_nm" value="<%=tp_company_nm%>" />
															<table border="1" cellspacing="0" cellpadding="0" width="100%" align="left">
																<tr>
																<td colspan=10 align=center><FONT face="Arial" size="2"><STRONG>&nbsp;<FONT color="#009900">Order Search</FONT></STRONG></FONT></td>
																</tr>
																<tr class="tableRow_Header">
																	<td class="type-whrite" align="left">From Date (yyyy-mm-dd)</td>
																	<td class="type-whrite" align="left">To Date (yyyy-mm-dd)</td>
																
																	<TD class="type-whrite" align="left">Lot #</TD>
																	<TD class="type-whrite" align="left">NDC #</TD>
																	<TD class="type-whrite" align="left">Order #</TD>
																	<td></td>
																	<td></td>
																
																</tr>
																<TR class="tableRow_On">
																	<td><html:text size="20" value="<%= fromDT %>" property="fromDT" readonly="true"/><img src="<html:rewrite page='/IMG/img.gif' />" width="16" height="16" onclick="return showCalendar('fromDT', '%Y-%m-%d', '24', true);"></td>
																	<td><html:text size="20" value="<%= toDT %>" property="toDT" readonly="true"/><img src="<html:rewrite page='/IMG/img.gif' />" width="16" height="16" onclick="return showCalendar('toDT', '%Y-%m-%d', '24', true);"></td>
																
																	<td><html:text size="15" value="<%= lotNum %>" property="lotNum" /></td>
																	<td><html:text size="15" value="<%= prodNDC %>" property="prodNDC" /></td>
																	<td><html:text size="15" value="<%= orderNumParam %>"  property="orderNum" /></td>
																	<td></td>
																	<td></td>
														
																</TR>
																
																<tr class="tableRow_Header">
																
																	<TD class="type-whrite" align="left">PedigreeID</TD>
																	<TD class="type-whrite" align="left">Trading Partner Name</TD>
																	<td></td>
																	<td></td>
																	<td></td>
																	<td></td>
																	<td></td>
																
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
																	<td></td>
																	<td></td>
																</TR>
																
																<TR class="tableRow_Off">
							
																	<td colspan=10 align=center>
																	<html:button value="Search" property="submit1" onclick="return submitform()"/>
																	<html:button property="button1" value="Clear" onclick="return clearForm()" /> 

																	</td>
																	  

																</TR>
																<TR class="tableRow_Off"><TD colspan=10></TD></TR>
																
															</table>
														
														
																							
														</TD>
													</TR>
																									
										
													<tr bgColor="white">
														<td class="td-typeblack" colSpan="1">Order DETAILS:</td>
													</tr>
													
													<tr>
														<td align="left">
															<!-- Dashboard Start -->
															<table id="Table12" cellSpacing="1" cellPadding="0" width="100%" align="center" border="0">
																<TBODY>
																	
																	<tr class="tableRow_Header">
																		<td class="type-whrite" noWrap align="center">Order ID</td>
																		<td class="type-whrite" noWrap align="center">Trading Partner</td>
																		<td class="type-whrite" noWrap align="center">Address</td>
																		<TD class="type-whrite" noWrap align="center">Issue Date</TD>
																		<TD class="type-whrite" align="center">Total No. Of NDCs</TD>
																		<TD class="type-whrite" align="center">Total Line Items</TD>
																		<td class="type-whrite" noWrap align="center">Order Amount</td>
																		
																	</tr>
																	
																<% if (list.size() == 0) { 
																	System.out.println("*******Inside no Data**********");
																	%>
																	<TR class='tableRow_Off'><TD class='td-content' colspan='9' align='center'>No data...</TD></TR>
																	
																	<% } else { 
																	
																	%>
																	
																		
																	<logic:iterate name="<%=Constants.ORDER_DETAILS%>" id="rcvng">
																	<tr class="tableRow_Off">
																		
																		<td class='td-content' height='20'><A href='View_Order_Details.jsp?pagenm=pedigree&tp_company_nm=&trNum=<bean:write name="rcvng" property="orderNum"/>&trType=purchaseorder'>
																			<bean:write name="rcvng" property="orderNum"/>
																		</td>	
																		
																		<td>
																			<bean:write name="rcvng" property="tradingPartner"/>
																		</td>
																		<td>
																			<bean:write name="rcvng" property="address"/>
																		</td>
																		<td>
																			<bean:write name="rcvng" property="issueDate"/>
																		</td>
																		<td>
																			<bean:write name="rcvng" property="numOfNDCs"/>
																		</td>
																		<td>
																			<bean:write name="rcvng" property="numOfLineItems"/>
																		</td>
																		<td>
																			<bean:write name="rcvng" property="amount"/>
																		</td>
																		
																	</tr>
																	</logic:iterate>	
																	
																	<% } %>
																	
																	

																	<TR class="tableRow_On">
																		<TD align="left" colspan="9">&nbsp;</TD>
																		
																	</TR>
																	
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
			</TD></TR></TBODY></TABLE>
			
			</div>
			
	</body>
	</form>
</html:html>

