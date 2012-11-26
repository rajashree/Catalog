<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.commons.xml.XMLUtil"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.catalog.session.TradingPartnerContext"%>
<%@ page import="com.rdta.epharma.epedigree.action.ReceivedPedigreeSearchFormBean"%>
<%@ page import="com.rdta.catalog.Constants"%>
<%@ include file='../../includes/jspinclude.jsp'%>

<%@ page import="com.rdta.catalog.trading.model.ProductMaster,java.util.*"%>

<%
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String sessionID = (String)session.getAttribute("sessionID");
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");
String  RefNum= (String)session.getAttribute("RefNum");
String searchSelectValue = request.getParameter("searchSelect");
System.out.println("searchSelectValue in jsp: "+searchSelectValue);
String  statuse= (String)request.getAttribute("statuss");
String  shipm= (String)request.getAttribute("statusace");
boolean orderFlag = true;

  if(searchSelectValue != null && !searchSelectValue.equals("DespatchAdvice")) {

		orderFlag = false;
  }

%>

<%
com.rdta.tlapi.xql.Connection connection = ConnectTL();
com.rdta.tlapi.xql.Statement statement = getStatement(connection);
String query = null;
String tpNames = null;
byte[] xmlResult;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
<head>
<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE"> 
<title>Raining Data ePharma - ePedigree Manager</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">

<script language="JavaScript" src="gen_validatorv2.js" type="text/javascript"></script>
<script language="JavaScript" src="calendar.js"></script>
<script language="JavaScript" src="calendar-en.js"></script>
<script language="JavaScript" src="calendar-setup.js"></script>
<script language="JavaScript" src="callCalendar.js"></script>
<link href="/ePharma/assets/images/calendar-win2k-1.css" rel="stylesheet" type="text/css">

</head>
<script language="JavaScript" type="text/JavaScript">
<!--
function submitCreateAPN()
{

//	alert(document.searchScreen.selectedRow.value);
//	alert(document.searchScreen.searchSelect.value);
	document.searchScreen.action = "ePedigree_ShipManager_Reconcile.jsp";
	document.searchScreen.submit();
	return true;
}
function shipsearch(){
	var fdt=document.createAPN.fromDtReceived.value
	var tdt=document.createAPN.toDtReceived.value
	var sace="<%=(String ) request.getAttribute("statuss")%>";
  
  if(sace=="false"){	
  		alert("Access Denied.....!");
  	return false;
  } else if(sace=="true"){
  	if(fdt!="" || tdt!=""){
  	
  if(fdt<tdt){
     document.createAPN.submit();
		return true;

	}else{
	 	alert("fromdate is less than todate")
	 	return false;
	}
	}
  }
    document.createAPN.submit();
 }
function AcessAPN()
{
var j ="<%= (String ) request.getAttribute("statusc")%>"
var checkVal='';
var allchecks = document.getElementsByName('selectedRow');
var checkSel =false;

for(i=0;i<allchecks.length;i++){
   if( allchecks[i].checked ){
     checkSel=true; 
     checkVal = ( checkVal.length == 0 ? '' : '&' ) +  allchecks[i].name + "=" + allchecks[i].value;
         }
 }
		if ( j == "false" ){
		alert("Access denied ...");
		return false;
	
	}
	else if(j == "true"){
	if( checkSel == false ){
  alert("Please Select A Reference Number!!!! "); 
  return false;
 }else{
	document.createAPN.y.value="CreatePedigree";
	document.createAPN.submit();
	
	return true;
	}
	}
	//document.createAPN.submit();
	}

function Sign()
{
 var checkVal='';
 var allchecks = document.getElementsByName('check');
 var checkSel =false;

for(i=0;i<allchecks.length;i++){
   if( allchecks[i].checked ){
     checkSel=true; 
     checkVal = ( checkVal.length == 0 ? '' : '&' ) +  allchecks[i].name + "=" + allchecks[i].value;
     
    }
 }
if( checkSel == false ){
  alert("Please select the PedigreeEnvelope!!! "); 
  return false;
 }
	
if (window.showModalDialog) { 
	window.showModalDialog('Signpedigree.do?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>&' + checkVal ,'','dialogWidth:300px;dialogHeight:200px;scroll=no;status=no');
}else {
window.open('Signpedigree.do?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>&' + checkVal ,'name','left=380,top=250,height=180,width=300,toolbar=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=no,modal=yes');
}
	
	//window.showModalDialog('Signpedigree.do?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>&' + checkVal ,'','dialogWidth:300px;dialogHeight:200px;scroll=no;status=no')

}
function clearForm() {
	document.forms[0].fromDT.value ="";
	document.forms[0].toDT.value ="";
	document.forms[0].lotNum.value ="";
	document.forms[0].prodNDC.value ="";
	document.forms[0].trNum.value = "";
	document.forms[0].apndocId.value ="";
	document.forms[0].SSCC.value ="";
	document.forms[0].status.value ="";
	document.forms[0].trdPrtnr.value ="";
	
}
function submitform(){
    var checkVal='';
	var allchecks = document.getElementsByName('check');
 	var checkSel =false;

for(i=0;i<allchecks.length;i++){
   if( allchecks[i].checked ){
     checkSel=true; 
     checkVal = ( checkVal.length == 0 ? '' : '&' ) +  allchecks[i].name + "=" + allchecks[i].value;
    }
 }
if( checkSel == false ){
  alert("Please select the PedigreeEnvelope!!! "); 
  return false;
 }
 	window.showModalDialog('EmailAPN.do?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>&' + checkVal ,'apnwin','dialogWidth:300px;dialogHeight:200px;scroll=no;status=no')
}

function accessdenied() {
  document.createAPN.submit();
  
}
//-->
</script>
</head>
<body>
	<%@include file='topMenu.jsp'%>
<%
String fromDT = request.getParameter("fromDT") == null ? "" : request.getParameter("fromDT");
			String toDT = request.getParameter("toDT") == null ? "" : request.getParameter("toDT");;
			String lotNum = request.getParameter("lotNum") == null ? "" : request.getParameter("lotNum");;
			String prodNDC = request.getParameter("prodNDC") == null ? "" : request.getParameter("prodNDC");;
			String trNum = request.getParameter("trNum") == null ? "" : request.getParameter("trNum");;
			String apndocId = request.getParameter("apndocId") == null ? "" : request.getParameter("apndocId");;
			String tpName = request.getParameter("tpName") == null ? "" : request.getParameter("tpName");;
			String sscc = request.getParameter("SSCC") == null ? "" : request.getParameter("SSCC");;
			
%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="1%" valign="middle" class="td-rightmenu"><img src="../../assets/images/space.gif" width="10" height="10"></td>
					<!-- Messaging -->
					<td width="99%" valign="middle" class="td-rightmenu"><table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td align="left">
								</td>
								<td align="right"><!-- <img src="../../assets/images/3320.jpg" width="200" height="27"> --><img src="../../assets/images/space.gif" width="5"></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<html:form action="/dist/epedigree/ePedigree_ReceivedPedigree.do" method="get">
															<html:hidden property="sessionID" value="<%=sessionID%>" />
															<html:hidden property="pagenm" value="<%=pagenm%>" />
															<html:hidden property="tp_company_nm" value="<%=tp_company_nm%>" />
															<table border="1"  width="100%" cellspacing="0" cellpadding="0" >
																<tr>
																<td colspan=10 align=center><FONT face="Arial" size="2"><STRONG>&nbsp;<FONT color="#009900">Quick Find</FONT></STRONG></FONT></td>
																</tr>
																<tr class="tableRow_Header">
																	<td class="type-whrite" align="left">From Date (yyyy-mm-dd)</td>
																	<td class="type-whrite" align="left">To Date (yyyy-mm-dd)</td>
																	<TD class="type-whrite" align="left">Container Code</TD>
																	<TD class="type-whrite" align="left">NDC #</TD>
																	<TD class="type-whrite" align="left">Lot #</TD>
																	<TD class="type-whrite" align="left">Transaction #</TD>																	
																</tr>
																	<!-- <TD class="type-whrite" align="left">ASN #</TD> -->
																
																<TR class="tableRow_On">
																<td><html:text property="fromDT" maxlength="15" size="15" value="<%= fromDT %>" /><img src="<html:rewrite page='/IMG/img.gif' />" width="16" height="16" onclick="return showCalendar('fromDT', '%Y-%m-%d', '24', true);">
                                                                </td>
																	<td><html:text property="toDT" maxlength="15" size="15" value="<%= toDT %>" /><img src="<html:rewrite page='/IMG/img.gif' />" width="16" height="16" onclick="return showCalendar('toDT', '%Y-%m-%d', '24', true);">
                                                                     </td>
																	<td><html:text  size="15" property="SSCC" value="<%= sscc %>" /></td>
																	<td><html:text  size="12" property="prodNDC" value="<%= prodNDC %>" /></td>
																	<td><html:text  size="12" property="lotNum" value="<%= lotNum %>" /></td>
																	<td><html:text  size="12" property="trNum" value="<%= trNum %>" /></td>																	
																
																</TR> 
																
																<tr class="tableRow_Header">
																			
																    <TD class="type-whrite" align="left">PedigreeID</TD>
																    <TD class="type-whrite" align="left">Trading Partner Name</TD>
																    <TD class="type-whrite" align="left">State</TD>
																    <TD class="type-whrite" align="left"></TD>
  																    <TD class="type-whrite" align="left"></TD>
  																    <TD class="type-whrite" align="left"></TD>
																</tr>
																
																<TR class="tableRow_On">
																	<td><INPUT type="text" size="15" name="apndocId" value="<%= apndocId %>"></td>
																	
																	<td><html:select property="trdPrtnr"><html:option value = "" >Select One...</html:option>
																	
																	<% List tpNamesss = (List)session.getAttribute("tpNames");
																  	   System.out.println("tp names in jsp: "+tpNamesss);
																       for(int i=0;i<tpNamesss.size();i++){
																     %>
																	<html:option value ="<%=tpNamesss.get(i).toString()%>"><%=tpNamesss.get(i).toString()%></html:option>
																	<% } %>
																	
																	</html:select></td>
																	<td><html:select property="status" ><html:option value = "">Select One...</html:option>
																										  <html:option value = "Certified">Certified</html:option>
																										  <html:option value = "Created Unsigned">Created Unsigned</html:option> 
																										  <html:option value = "Created Signed">Created Signed</html:option> 
																										  <html:option value = "Sent-Problem">Sent-Problem</html:option>
																										  <html:option value = "Sent">Sent</html:option>
																		</html:select></td>
																	<td></td>
																	<td></td>
																	<td></td>
																</TR>
																															 
																<TR class="tableRow_Off">
							
																	<td colspan=10 align=center><html:submit value="Find" property="Submit1" />
																								<html:button property="button1" value="Clear" onclick="return clearForm()" /></td>

																</TR>
															</table>
														</html:form>
<form name="createAPN" action="<%=servPath%>/dist/epedigree/ShippingManagerSearchEmpty.do" method="get" >
<INPUT id="hidden1" type="hidden" name="sessionID" value="<%=sessionID%>">
<INPUT id="hidden12" type="hidden" name="pagenm" value="<%=pagenm%>">
<INPUT id="hidden14" type="hidden" name="tp_company_nm" value="<%=tp_company_nm%>">
<INPUT id="hidden15" type="hidden" name="FromShippingSearchPage" value="true">
<INPUT type="hidden" name="res" value="false">


					<td><table border="0" cellspacing="1" cellpadding="0" width="100%">
							<tr>
								<td>
									<P>
										<TABLE id="Table2" cellSpacing="1" cellPadding="0" width="100%" >
											<TR>
												<TD><!-- info goes here -->
													<TABLE id="Table3" cellSpacing="1" cellPadding="0" width="100%" align="left" bgColor="white"
														border="0">
														<TR bgColor="white">
															<TD class="td-typeblack" colSpan="3">
																<P><FONT size="2">Create Pedigree From - Search </FONT>
																</P>
															</TD>
														</TR>
														
											
<%          String frDT = request.getParameter("fromDtReceived") == null ? "" : request.getParameter("fromDtReceived");
			String toDate = request.getParameter("toDtReceived") == null ? "" : request.getParameter("toDtReceived");;
			String refNum = request.getParameter("refNumber") == null ? "" : request.getParameter("refNumber");;
			String company = request.getParameter("fromCompany") == null ? "" : request.getParameter("fromCompany");;
			String NDCNum = request.getParameter("ndc") == null ? "" : request.getParameter("ndc");;
%>			
														<TR>
															<TD align="left"><!-- Dashboard Start -->
																<TABLE id="Table4" cellSpacing="0" cellPadding="0" width="100%" align="center" border="0">
																	<TR class="tableRow_Header">

																		<TD colspan= "2" class="type-whrite" align="center" height="20">
																	<STRONG><FONT size="2"> SEARCH:</FONT></STRONG> 
																		<INPUT id="Radio3" type="radio" value="DespatchAdvice" name="searchSelect" <% if(orderFlag) out.print("checked");  %> >&nbsp;<STRONG><FONT color="#ffff00">Invoice </FONT></STRONG>
																		<INPUT id="Radio2" type="radio" value="Order" name="searchSelect"  <% if(!orderFlag) out.print("checked");  %>>&nbsp;<STRONG><FONT color="#ffff00">Purchase Order</FONT></STRONG></TD>
																	</TR>
																	<TR>
																		<TD class="td-content" align="center" colspan="2">														
																		<table width="100%" id="Table5" cellSpacing="1" cellPadding="1" align="left" border="0"
																					bgcolor="white" class="td-menu">
																					<tr>
																						<td align="left">
																							<TABLE id="Table6" cellSpacing="0" cellPadding="0" border="0" width="100%" align="left" class="td-menu">
																								<tr>
																									<td bgcolor="white">
																										<!-- table goes here -->
																										<TABLE id="Table7" cellSpacing="0" cellPadding="0" border="0" class="td-menu" align="left"
																											width="100%">
																											<TR class="tableRow_Header">
																												<TD class="type-whrite" align="center">
																													<STRONG>SEARCH ON</STRONG>
																												</TD>
																												<TD class="type-whrite" align="center">
																													<STRONG>VALUE</STRONG>
																												</TD>
																											
																											</TR>
																											<TR class="tableRow_On">
																												<TD><STRONG>Date Received (yyyy-mm-dd):</STRONG></TD>
																												<td>
																												From:<input type="text" name="fromDtReceived" maxlength="22" size="20" value="<%= frDT %>"><img src="<html:rewrite page='/IMG/img.gif' />" width="16" height="16" onclick="return showCalendar('fromDtReceived', '%Y-%m-%d', '24', true);">
                                                                                                                
																												
																												To:<input type="text" name="toDtReceived" maxlength="22" size="20"  value="<%= toDate %>"><img src="<html:rewrite page='/IMG/img.gif' />" width="16" height="16" onclick="return showCalendar('toDtReceived', '%Y-%m-%d', '24', true);">
                                                                                                                </td>
																												
																												
																											</TR>
																											<TR class="tableRow_Off">
																												<TD><STRONG> Reference #:</STRONG></TD>
																												<TD><INPUT type="text" id="Text2"  size="24" name="refNumber"  value="<%= refNum %>"/>&nbsp;</TD>
																											
																											</TR>
																											<TR class="tableRow_On">
																												<TD><STRONG>From Name/Company:</STRONG></TD>
																												<TD><INPUT type="text" id="Text3"  size="24" name="fromCompany"  value="<%= company %>"/></TD>
																												
																											</TR>
																											<TR class="tableRow_Off">
																												<TD><STRONG>NDC:</STRONG></TD>
																												<TD><INPUT id="Text9" type="text" size="24" name="ndc"  value="<%= NDCNum %>" ></TD>
																												
																											</TR>

																																																					
																											<tr class="tableRow_Header">
																												<td colspan="2" align="center">
																												    <INPUT type="hidden" name="x" value=""> 
																													<INPUT name="Submit3" type="submit" class="fButton" value="Search" >
																												</td>
																											</tr>
																											<tr><td/></tr><tr><td/></tr>						
																										</TABLE>

																									</td>
																								</tr>
<tr> 
              <td align="left">
            <% String but = (String)request.getAttribute("buttonValue");
            System.out.println("button name in jsp: "+but);
            if(but.equalsIgnoreCase("Find")){
            %>
            <%  List list1 = (List)session.getAttribute("RPList");
	String button = (String)request.getAttribute("buttonname");
	System.out.println("*********List values :"+list1.size()+"  "+button);
	
			
%>
<TABLE id="Table9" cellSpacing="1" cellPadding="0" width="100%" align="center" border="0">
										<TBODY>
										<tr class="tableRow_Header">
											<td class="type-whrite" noWrap align="center">Select</td>
											<td class="type-whrite" noWrap align="center">EnvelopeId&nbsp;#</td>
											<td class="type-whrite" noWrap align="center">Transaction #</td>
											<td class="type-whrite" noWrap align="center">Date Created</td>
											<td class="type-whrite" noWrap align="center">Trading Partner</td>
											<td class="type-whrite" noWrap align="center">Number Of Pedigrees</td>
											
											
										</tr>
<% if (list1.size() == 0 ){  %>			<TR class='tableRow_Off'><TD class='td-content' colspan='9' align='center'>No Matching Pedigrees are Found</TD></TR>
<% } %>
		
																	<logic:iterate name="<%=Constants.PEDIGREE_DETAILS%>" id="apn" type="com.rdta.epharma.epedigree.action.ReceivedPedigreeSearchFormBean">
																	<tr class="tableRow_Off">
																		<td>
																		<input type="radio" name="check" value="<bean:write name="apn" property="pedigreeNum"/>">
																		</td>
																		<td class='td-content' height='20'>
																		<A href='pedigreeEnvelopeDetails.do?envelopeId=<bean:write name ="apn" property ="pedigreeNum"/>&pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>'>
																			<bean:write name="apn" property="pedigreeNum"/>
																		</td>	
																		<td class='td-content'>
																			<bean:write name="apn" property="trNum"/>
																		</td>
																		
																		<td>
																		<% String createdDateAndTime = apn.getDataRcvd();
																		   System.out.println("Date and time in jsp: "+createdDateAndTime);
																		   String createdDate[] = createdDateAndTime.split("T"); 
																		   
																		%>
																			<%=createdDate[0]%>
																			
																		</td>
																		<td>
																			<bean:write name="apn" property="trdPrtnr"/>
																		</td>
																																	
																		<td>
																			<bean:write name="apn" property="numOfPedigrees"/>
																		</td>
																		
																		
																		
																	</tr>
																	</logic:iterate>	
																	<TR class="tableRow_On">
																		<TD align="left" colspan="9">&nbsp;</TD>
																		
																	</TR></table>
																	<TABLE id="Table10" cellSpacing="1" cellPadding="0" width="100%" align="center" border="0">
																	<tr class="tableRow_Header">
   <td align='center' >
     <!-- <A onclick="MM_openBrWindow('ePedigreeSubmitScan.html','APN','scrollbars=yes,width=100%')"> -->
<INPUT id="Submit4" type="button" class="fButton_large" value="Send Pedigree(s)" value="Send Pedigree(s)" onClick = "return submitform()"></a>
   </td>
 
   <td align='center' >
	<INPUT id="Submit4" type="button" class="fButton_large" value="Sign Pedigree(s)" onClick="Sign()"></a>
   </td>
																	
																	</tr></table>
            
            <%}else{%>
              
              <% List list =  (List)request.getAttribute("SearchResult");
		System.out.println("DA result in jsp: "+list);
		
		%>
              <%searchSelectValue = "DespatchAdvice";
              if(searchSelectValue!=null && list != null  ){%>
					 <TABLE id="Table8" cellSpacing="1" cellPadding="3" border="0" width="100%">				
					      <tr class="tableRow_Header">
						     <td class="type-whrite">Select</td>
						  	    <td class="type-whrite">Reference Number</td>
						     <td class="type-whrite">From Company Name</td>
						     <td class="type-whrite">NDC</td>
						     <td class="type-whrite">Date Received</td>
						    
					      </tr>
					      <% if(list.size() ==0){%>
				<TR class='tableRow_Off'><TD class='td-content' colspan='9' align='center'>No data...</TD></TR>
				<%}%>
                            <% /*
					           if(DAList != null ) {
					           String refNumber = "";
						       String dtReceived = "";
						       String NDC = "";
						       String fromCompany = "";
			 			       String searchSelect = (String)request.getAttribute("searchSelect");
					    	   String name= "selectedRow";
					    	   List sampleList=(List)request.getAttribute(Constants.Ship_MNGR_DETAILS);
					    	   
					    	   String selectedRowNew=request.getParameter("selectedRow");
					    	System.out.println("search select is :" +searchSelect);  
                               for(int i=0; i < DAList.size(); i++) {
		                        
									name = "selectedRow" + i;
							
							if( searchSelect != null &&  searchSelect.trim().equalsIgnoreCase("DespatchAdvice")) {


								Node DespatchNode = XMLUtil.parse((InputStream) DAList.get(i) );
								System.out.println("dddd :"+DespatchNode.getNodeName());
								refNumber = CommonUtil.jspDisplayValue(DespatchNode,"ID");
								System.out.println("refNumber : "+refNumber);

								dtReceived = CommonUtil.jspDisplayValue(DespatchNode,"Delivery/ActualDeliveryDateTime");

								NDC = CommonUtil.jspDisplayValue(DespatchNode,"DespatchLine/Item/SellersItemIdentification/ID");
								fromCompany = CommonUtil.jspDisplayValue(DespatchNode,"SellerParty/Party/PartyName/Name");
								
							}
							*/							
			   %>
			   <%
					
					System.out.println("List values in jst: "+list.size());

					if(list != null ) {

						 String refNumber = "";
						       String dtReceived = "";
						       String NDC = "";
						       String fromCompany = "";
			 			       String searchSelect = request.getParameter("searchSelect");
					    	   String name= "selectedRow";
					    	   List sampleList=(List)request.getAttribute(Constants.Ship_MNGR_DETAILS);
					    	   
					    	   String selectedRowNew=request.getParameter("selectedRow");
					    	System.out.println("search select is :" +searchSelect);  
                              
						for(int i=0; i < list.size(); i++) {


							name = "selectedRow" + i;
							if(searchSelect != null && searchSelect.trim().equalsIgnoreCase("Order")) {
						
								Node OrderNode = XMLUtil.parse((InputStream) list.get(i) );
								
								refNumber = CommonUtil.jspDisplayValue(OrderNode,"BuyersID");
								System.out.println("Order ID : "+refNumber);
								dtReceived = CommonUtil.jspDisplayValue(OrderNode,"Delivery/RequestedDeliveryDateTime");

								NDC = CommonUtil.jspDisplayValue(OrderNode,"OrderLine/LineItem/Item/SellersItemIdentification/ID");
								fromCompany = CommonUtil.jspDisplayValue(OrderNode,"SellerParty/PartyName/Name");

							}	else if( searchSelect != null &&  searchSelect.trim().equalsIgnoreCase("DespatchAdvice")) {


								if( searchSelect != null &&  searchSelect.trim().equalsIgnoreCase("DespatchAdvice")) {


								Node DespatchNode = XMLUtil.parse((InputStream) list.get(i) );
								System.out.println("dddd :"+DespatchNode.getNodeName());
								refNumber = CommonUtil.jspDisplayValue(DespatchNode,"ID");
								System.out.println("refNumber : "+refNumber);

								dtReceived = CommonUtil.jspDisplayValue(DespatchNode,"Delivery/ActualDeliveryDateTime");

								NDC = CommonUtil.jspDisplayValue(DespatchNode,"DespatchLine/Item/SellersItemIdentification/ID");
								fromCompany = CommonUtil.jspDisplayValue(DespatchNode,"SellerParty/Party/PartyName/Name");
								
							}

							}

			   %>
		<tr class="tableRow_On">
						<td><INPUT id="Radio2" type="radio" value="<%= refNumber %>" name="selectedRow" <%if(selectedRowNew != null && selectedRowNew.equalsIgnoreCase(refNumber)) out.println("checked"); %> >&nbsp;</STRONG> </td>
						<td><%= refNumber %></td>
						<td><%= fromCompany %></td>
						<td><%= NDC %></td>
						<td><%String transDate[] = dtReceived.split("T"); %><%= transDate[0] %></td>
					</tr>

                <%	}//end of for loop
						
					}//end of if loop
				
                 %>
			
				</TABLE>
            </td>
 </tr>
	<% System.out.println("*****************");
	if(list!= null && list.size() > 0 ) {	%>
	   <tr class="tableRow_Header">
	      <td colspan="2" align="center">
	      <INPUT type="hidden" name="y" value=""> 
	      <INPUT name="Submit5" type="button" class="fButton_large"  value="Create Pedigree Envelope" onClick="AcessAPN();"></td>
	   </tr><%}%>
								     </TABLE>
								</td>
							  </tr>
					    	</table>
						  </TD>
					 </TR>
				   </TABLE>
			     </TD>
				</TR>
              </TABLE>
			<P></P>
		  </TD>
		<td rowspan="2">&nbsp;</td>
	</TR>
 <!-- Breadcrumb --><!-- <tr> <td><a href="#" class="typegray1-link">Home</a><span class="td-typegray"> - </span>
  <a href="#" class="typegray1-link">ePedigree</a></td></tr> -->
</TABLE>
									
<% }System.out.println("Inside loop "+request.getParameter("selectedRow"));
List sampleList=(List)request.getAttribute(Constants.Ship_MNGR_DETAILS);
if ( sampleList != null ){
%>
<TABLE id="Table9" cellSpacing="1" cellPadding="3" width="100%" align="center" border="0">
										<TBODY>
										<tr class="tableRow_Header">
											<td class="type-whrite" noWrap align="center">Select</td>
											<td class="type-whrite" noWrap align="center">EnvelopeID&nbsp;#</td>
											<td class="type-whrite" noWrap align="center">Transaction #</td>
											<td class="type-whrite" noWrap align="center">Created Date</td>
											<td class="type-whrite" noWrap align="center">Trading Partner</td>
											<td class="type-whrite" noWrap align="center">Number of Pedigrees</td>
										</tr>
																	
	   <% if (sampleList.size() == 0) { 
		  System.out.println("*******Inside no Data**********");
	   %>
		<TR class='tableRow_Off'><TD class='td-content' colspan='9' align='center'>No data...</TD></TR>
		<% } else { 
				System.out.println("==========LIST2 IN JSP=============="+list);
				System.out.println("====SHIP MGR======"+Constants.Ship_MNGR_DETAILS);	
		%>
		<%}%>
																	
		<logic:iterate name="<%=Constants.Ship_MNGR_DETAILS%>" id="ship" type="com.rdta.epharma.epedigree.action.ShipingManagerForm">
		<tr class="tableRow_Off">
		
		 <td><input type ="radio"  name="check" value='<bean:write name ="ship" property ="pedigreeNum"/>'></td> 
						
		<td class='td-content'><A href='pedigreeEnvelopeDetails.do?envelopeId=<bean:write name ="ship" property ="pedigreeNum"/>&pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>'>
				<bean:write name="ship" property="pedigreeNum"/>
		</td>		
		<td class='td-content'>	<bean:write name="ship" property="trNum"/>
       </td>
		<td>
			<% String createDateAndTime = ship.getDataRcvd();
			   String createDate[] = createDateAndTime.split("T"); 
			 %>
			<%=createDate[0]%>
		</td>
		
		<td>
			<bean:write name="ship" property="trdPrtnr"/>
		</td>
		<td>
			<bean:write name="ship" property="quantity"/>
		</td>
				
	</logic:iterate>	
   

</TABLE>
<TABLE id="Table10" cellSpacing="1" cellPadding="0" width="100%" align="center" border="0">
  <tr class="tableRow_Header">
   <td align='center' >
     <!-- <A onclick="MM_openBrWindow('ePedigreeSubmitScan.html','APN','scrollbars=yes,width=100%')"> -->
<INPUT id="Submit4" type="button" class="fButton_large" value="Send PedigreeEnvelope" value="Send Pedigree(s)" onClick = "return submitform()"></a>
   </td>
 
   <td align='center' >
	<INPUT id="Submit4" type="button" class="fButton_large" value="Sign Pedigree(s)" onClick="Sign()"></a>
   </td>
   <% } }%>
   <%%>
   </form>
 </tr> 				
			
			<%@include file='onLoad.jsp'%>
</TABLE>

	<jsp:include page="../globalIncludes/Footer.jsp" />
</body>
</html:html>
																	
														