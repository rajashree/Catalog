<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.catalog.Constants" %>
<%@ page import="com.rdta.epharma.epedigree.action.APNSearchForm"%>
<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.commons.xml.XMLUtil"%>
<%@ page import="java.io.InputStream"%>

 
 
<%@ include file='../../includes/jspinclude.jsp'%>

<% String status = "";
	status = (String)request.getAttribute("status"); 
	String buttonname = (String)request.getAttribute("buttonname");
	//System.out.println("Access status in APN search:  "+status);
 %>
 
<script language="JavaScript" type="text/JavaScript">



function callAction(off){	
	var frm = document.forms[0];		
	frm.action = "<html:rewrite action='./dist/epedigree/PedigreeSearch.do?accesslevel=apnsearch'/>";
	document.getElementsByName("offset")[0].value = off;
	frm.submit();
}




function clearForm() {


	document.forms[0].fromDT.value ="";
	document.forms[0].toDT.value ="";
	document.forms[0].lotNum.value ="";
	document.forms[0].drugName.value ="";
	document.forms[0].prodNDC.value ="";
	document.forms[0].trNum.value = "";
	document.forms[0].apndocId.value ="";
	document.forms[0].sscc.value ="";
	document.forms[0].tpName.value = "";
	document.forms[0].pedstate.value = "";

}


function submitform(){

var i = "<%=status%>";
var fdt=document.forms[0].fromDT.value
var tdt=document.forms[0].toDT.value

 if(fdt>tdt){
	    alert("from Date should be less than to Date");
   	 	return false;
	}else{
	 	document.forms[0].submit();
	}
if( i == "false" ){

  alert("Access Denied....!");
  return false;
}else{ 

	document.forms[0].submit();
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
System.out.println("session ID : "+sessionID);
String screenEnteredDate = (String)request.getAttribute("screenEnteredDate");
if(SSCC == null) { SSCC = "";}
if(pagenm == null) { pagenm = "";}
if(tp_company_nm == null) { tp_company_nm = "";}
System.out.println("*********Inside APNSearch jsp********");

com.rdta.tlapi.xql.Connection connection = ConnectTL();
com.rdta.tlapi.xql.Statement statement = getStatement(connection);

String query = null;
String tpNames = null;
String state = null;
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

		
		<% 
		 
		    List list = (List)request.getAttribute("List");
		    System.out.println("INSIDE THIS LIST "+list);
			String fromDT = request.getParameter("fromDT") == null ? "" : request.getParameter("fromDT");
			String toDT = request.getParameter("toDT") == null ? "" : request.getParameter("toDT");;
			String lotNum = request.getParameter("lotNum") == null ? "" : request.getParameter("lotNum");;
			String drugName = request.getParameter("drugName") == null ? "" : request.getParameter("drugName");;
			String prodNDC = request.getParameter("prodNDC") == null ? "" : request.getParameter("prodNDC");;
			String trNum = request.getParameter("trNum") == null ? "" : request.getParameter("trNum");;
			
			String apndocId = request.getParameter("apndocId") == null ? "" : request.getParameter("apndocId");;
			String tpName = request.getParameter("tpName") == null ? "" : request.getParameter("tpName");;
		    String pedigreeState = request.getParameter("pedstate") == null ? "" : request.getParameter("pedstate");;;
			String sscc = request.getParameter("sscc") == null ? "" : request.getParameter("sscc");;
			System.out.println("INSIDE THISsssssss ");
			String recs = com.rdta.catalog.Constants.NO_OF_RECORDS;

		
		%>
		
		
		
<%
		String offset = (String)request.getAttribute("offset");
		int intOffSet = -1;
		if(offset == null){
			offset = "0";
		}else{
			intOffSet = Integer.parseInt(offset);
			System.out.println("intOffSet in jsp is :"+intOffSet);
		}
		
		int totCount=0;
			
			if( list !=  null ){				
				if(list.size()==0){
					totCount = 0;			
				}else{
					totCount = list.size();
					System.out.println("The totCount is :"+totCount);
				}	
			
			}
			
	%>
	
	<% 
			int c = 0;
			for(int count=0 ; count<totCount; count++){
				 c = totCount/2;
			 	 int extraCount = totCount - c*2;
				 if(extraCount > 0){
			 		c += 1;
			 		System.out.println("c = "+c);
			 	 } 	 
			} 																		
			int intRecs = Integer.parseInt(recs);
			int dispLast = totCount/intRecs;
			if( totCount % intRecs > 0 ) {
				 dispLast++;
			}
			System.out.println("dispLast in jsp is :"+dispLast);
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


<script language="JavaScript" type="text/JavaScript">

function deleteFunction()
{

 var checkVal='';
 var allchecks = document.getElementsByName('selected');
var Mine ;
 var checkSel =false;
 
for(i=0;i<allchecks.length;i++){
   if( allchecks[i].checked ){
     checkSel=true; 
     checkVal = ( checkVal.length == 0 ? '' : '&' ) +  allchecks[i].name + "="  + allchecks[i].value;
     
    }
 }
if( checkSel == false ){
  alert("Please select the  Pedigree!!! "); 
  return false;
 }else {
 				frm =  document.forms[0];
				frm.action = '/ePharma/dist/epedigree/PedigreeSearch.do?accesslevel=apnsearch';
				document.getElementsByName("offset")[0].value = <%=intOffSet%>;
				frm.submit();
				return true;
				}
		}
</script>

</head>
<script language="JavaScript" type="text/JavaScript">
</script>
	<body >
		
		<%@include file='topMenu.jsp'%>
		
		
		
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
													
													<form action="/dist/epedigree/PedigreeSearch.do?accesslevel=apnsearch"  method="post" >
															<html:hidden property="pagenm" value="<%=pagenm%>" />
															<html:hidden property="offset" value="<%=pagenm%>" />
															<input type = "hidden" name="operationType" value=""/>
													
															<html:hidden property="sessionID" value="<%=sessionID%>" />
															<html:hidden property="pagenm" value="<%=pagenm%>" />
															<html:hidden property="tp_company_nm" value="<%=tp_company_nm%>" />
															<!--<table border="1" cellspacing="0" cellpadding="0" width="100%" align="left">
																<tr>
																<td colspan=10 align=center><FONT face="Arial" size="2"><STRONG>&nbsp;<FONT color="#009900">Pedigree Search</FONT></STRONG></FONT></td>
																</tr>
																<tr class="tableRow_Header">
																	<td class="type-whrite" align="left">From Date (yyyy-mm-dd)</td>
																	<td class="type-whrite" align="left">To Date (yyyy-mm-dd)</td>
																	<TD class="type-whrite" align="left">Container Code</TD>
																	<TD class="type-whrite" align="left">Lot #</TD>
																	<TD class="type-whrite" align="left">NDC #</TD>
																	<TD class="type-whrite" align="left">Transaction #</TD>
																	<td></td>
																	<td></td>
																
																</tr>
																<TR class="tableRow_On">
																	<td><html:text size="20" value="<%= fromDT %>" property="fromDT" readonly="false"/><img src="<html:rewrite page='/IMG/img.gif' />" width="16" height="16" onclick="return showCalendar('fromDT', '%Y-%m-%d', '24', true);"></td>
																	<td><html:text size="20" value="<%= toDT %>" property="toDT" readonly="false"/><img src="<html:rewrite page='/IMG/img.gif' />" width="16" height="16" onclick="return showCalendar('toDT', '%Y-%m-%d', '24', true);"></td>
               													    <td><html:text size="20" value="<%= sscc %>" property="sscc"/></td>
																	<td><html:text size="10" value="<%= lotNum %>" property="lotNum" /></td>
																	
																	<td><html:text size="10" value="<%= prodNDC %>" property="prodNDC" /></td>
																	<td><html:text size="10" value="<%= trNum %>"  property="trNum" /></td>
																	<td></td>
																	<td></td>
														
																</TR>
																
																<tr class="tableRow_Header">
																
																	<TD class="type-whrite" align="left">PedigreeID</TD>
																	<TD class="type-whrite" align="left">Trading Partner Name</TD>
																	<TD class="type-whrite" align="left">State</TD>
																										  
																	<td></td>
																	<td></td>
																	<td></td>
																	<td></td>
																	<td></td>
																
																</tr>
																<TR class="tableRow_On">
																<% System.out.println("stauts in jsp : "+request.getParameter("status")); %>
																	<td><html:text size="19" value="<%= apndocId %>" property="apndocId" /></td>
																	
																	<td><html:select property="tpName" value="<%=tpName%>"><html:option value = "" >SelectOne</html:option>
																	
																	<% List tpNamesss = (List)session.getAttribute("tpNames");
																  	   System.out.println("tp names in jsp: "+tpNamesss);
																       for(int i=0;i<tpNamesss.size();i++){
																     %>
																	<html:option value ="<%=tpNamesss.get(i).toString()%>"><%=tpNamesss.get(i).toString()%></html:option>
																	<% } %>
																	
																	</html:select></td>
																	
																	<td>
																	<html:select property="pedstate" value="<%=pedigreeState%>"> 
																			<html:option value = "">SelectOne</html:option>
																			<html:option value = "Certified">Certified</html:option>
																			<html:option value = "Received">Received</html:option>
																			<html:option value = "Authenticated">Authenticated</html:option>
																			<html:option value = "Created Unsigned">Created Unsigned</html:option> 
																			<html:option value = "Created Signed">Created Signed</html:option> 
																			<html:option value = "Goods Received Full">Goods Received Full</html:option>
																			<html:option value = "Goods Received Partial">Goods Received Partial</html:option>
																			<html:option value = "Sent-Problem">Sent-Problem</html:option>
																			<html:option value = "Sent">Sent</html:option>
																			<html:option value = "Not Authenticated">Not Authenticated</html:option>
																			<html:option value = "Decommisioned">Decommisioned</html:option>
																			<html:option value = "Quarantined">Quarantined</html:option>
																	</html:select>
																	</td>
																	
																	<td></td>
																	<td></td>
																	<td></td>
																	<td></td>
																	<td></td>
																</TR>
																
																<TR class="tableRow_Off">
							
																	<td colspan=10 align=center>
																	<html:button value="Search" property="submit1"  onclick = "return callAction(0);"/>
																	<html:button property="button1" value="Clear" onclick="return clearForm()" /> 

																	</td>
																	  

																</TR>
																<TR class="tableRow_Off"><TD colspan=10></TD></TR>
																
															</table>-->
															
															
															
															
															
															
															
															
															<table border="1" cellspacing="0" cellpadding="0" width="100%" align="left">
																<tr>
																<td colspan=10 align=center><FONT face="Arial" size="2"><STRONG>&nbsp;<FONT color="#009900">Pedigree Search</FONT></STRONG></FONT></td>
																</tr>
																<tr class="tableRow_Header">
																	<td class="type-whrite" width="250" align="left">From Date (yyyy-mm-dd)</td>
																	<td class="type-whrite" width="250" align="left">To Date (yyyy-mm-dd)</td>
																	<TD class="type-whrite" width="250" align="left">Transaction#</TD>
																	
																	
																
																</tr>
																<TR class="tableRow_On">
																	<td><html:text size="25" value="<%= fromDT %>" property="fromDT" readonly="false"/><img src="<html:rewrite page='/IMG/img.gif' />" width="16" height="16" onclick="return showCalendar('fromDT', '%Y-%m-%d', '24', true);"></td>
																	<td><html:text size="25" value="<%= toDT %>" property="toDT" readonly="false"/><img src="<html:rewrite page='/IMG/img.gif' />" width="16" height="16" onclick="return showCalendar('toDT', '%Y-%m-%d', '24', true);"></td>
               													    <td><html:text size="25" value="<%= trNum %>"  property="trNum" /></td>
               													    
																															
																</TR>
																<tr class="tableRow_Header">
																	
																	<TD class="type-whrite" width="250" align="left">Drug Name</TD>
																	<TD class="type-whrite" width="250" align="left">NDC #</TD>
																	<TD class="type-whrite" width="250" align="left">Manufacturer</TD>
																	
																</tr>
																<TR class="tableRow_On">
																	
               													    <td><html:text size="25" value="<%= drugName %>" property="drugName" /></td>
																	<td><html:text size="25" value="<%= prodNDC %>" property="prodNDC" /></td>
																	<td><html:select property="tpName"  value="<%=tpName%>"><html:option value = "" >SelectOne</html:option>
																	
																	<% List tpNamesss = (List)session.getAttribute("tpNames");
																  	   System.out.println("tp names in jsp: "+tpNamesss);
																       for(int i=0;i<tpNamesss.size();i++){
																     %>
																	<html:option value ="<%=tpNamesss.get(i).toString()%>"><%=tpNamesss.get(i).toString()%></html:option>
																	<% } %>
																	
																	</html:select></td>
																	
																</TR>
																<!--
																<tr class="tableRow_Header">
																
																	<TD class="type-whrite" align="left">PedigreeID</TD>
																	<TD class="type-whrite" align="left">Trading Partner Name</TD>
																	<TD class="type-whrite" align="left">State</TD>
																										  
																	<td></td>
																	<td></td>
																	<td></td>
																	<td></td>
																	<td></td>
																
																</tr>
																<TR class="tableRow_On">
																<% System.out.println("stauts in jsp : "+request.getParameter("status")); %>
																	<td><html:text size="19" value="<%= apndocId %>" property="apndocId" /></td>
																	
																	
																	
																	
																	<td></td>
																	<td></td>
																	<td></td>
																	<td></td>
																	<td></td>
																</TR>
																-->
																<TR class="tableRow_Off">
							
																	<td colspan=10 align=center>
																	<html:button value="Search" property="submit1"  onclick = "return callAction(0);"/>
																	<html:button property="button1" value="Clear" onclick="return clearForm()" /> 

																	</td>
																	  

																</TR>
																<TR class="tableRow_Off"><TD colspan=10></TD></TR>
																
															</table>
															
															
															
															
															
															
															
													
														
																							
														</TD>
													</TR>
																									
										
													<tr bgColor="white">
														<td class="td-typeblack" colSpan="1">PEDIGREE DETAILS:</td>
													</tr>
													
													<tr>
														<td align="left">
															<!-- Dashboard Start -->
															<tr>
														<td align="left">
														
															<table id="Table12" cellSpacing="1" cellPadding="0" width="100%" align="center" border="0">
																<TBODY>
																	
																<tr class="tableRow_Header">
																		 <td class="type-whrite" align="center" ></td>
																		<td class="type-whrite"  align="left">PedigreeID</td>
																		<td class="type-whrite"  align="left">DrugName</td>
																		<td class="type-whrite"  align="left">Manufacturer</td>
																		<td class="type-whrite" align="left">NDC</td>
																		<td class="type-whrite"  align="left">Lot Number</td>
																		<td class="type-whrite"  align="left">Quantity</td>
																		<TD class="type-whrite"  align="left">Transaction Date</TD>
																		<TD class="type-whrite" align="left">Trading Partner</TD>
																		<td class="type-whrite"  align="left">Transaction #</td>
																		
																	</tr>
																	
																	<% 
																	String result = (String)request.getAttribute("listsize");
																	if (result == null) result = "";
																	System.out.println("Result value is :"+result);
																	if (result.equals("0")) { 
																	System.out.println("*******Inside no Data**********");
																	%>
																	<TR class='tableRow_Off'><TD class='td-content' colspan='10' align='center'>No data...</TD></TR>
																	
																	<% } else { 
																	
																	System.out.println("*******Inside  Data**********");
																	%>
																	
																	
																	
																		
																	<logic:iterate name="<%=Constants.APN_DETAILS%>" id="apn" length="<%=recs%>" offset="<%=offset%>">
																	<tr class="tableRow_Off">
																		
																																				
																		<td align="center" >
	 																	<input type="checkbox" name="selected" value="<bean:write name='apn' property='pedigreeId'/>" />	
																		<td>
																				<bean:write name="apn" property="pedigreeId"/>
																		</td>	
																		
																		
	 																	</td>
																			
																		<td>
																			<bean:write name="apn" property="drugName"/>
																		</td>
																		<td>
																			<bean:write name="apn" property="manufacturer"/>
																		</td>
																		<td>
																			<bean:write name="apn" property="ndc"/>
																		</td>
																		<td>
																			<bean:write name="apn" property="lotNo"/>
																		</td>
																		<td>
																			<bean:write name="apn" property="quantity"/>
																		</td>	
																			
																			
																																				
																		<td>
																			<bean:write name="apn" property="dataRcvd"/>
																		</td>
																		
																		<td>
																			<bean:write name="apn" property="trdPrtnr"/>
																		</td>
																		
																		<td class='td-content'>
																			<bean:write name="apn" property="trNum"/>
																		</td>
																		
																																				
																	</tr>
																	</logic:iterate>	
																	
																	<% } %>
																	<% if(totCount > Integer.parseInt(recs)) {%>
		<a href="javascript:callAction(0)">First</a>
		<a href="javascript:callAction(<%=(dispLast-1)*intRecs%>)">Last</a>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<% if(intOffSet+intRecs < totCount){	%>																				 
				<a href="javascript:callAction(<%=intOffSet+intRecs%>)">Next</a>
			<% } %>	
			<% if(intOffSet > 0) { %>
				<a href="javascript:callAction(<%=intOffSet-intRecs%>)">Previous</a>
			<% } %>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			(Displaying records <%=intOffSet+1%> to <%= (intOffSet+intRecs) > totCount ? totCount : (intOffSet+intRecs)%> of <%=totCount%> matching the search criteria)
			<br>
			<br>
	<% } %>	
 	<TR class="tableRow_On">
																		<TD align="left" colspan="10">&nbsp;</TD>
																		
																	</TR>



			<% 
																	String result1 = (String)request.getAttribute("listsize");
																	if (result1 == null) result1 = "";
																	System.out.println("Result value is :"+result1);
																	if (!result1.equals("0")) { 
																	
																	%>
																	<table><tr></tr><tr></tr><tr></tr><tr></tr>
																	   <tr>
																	   
																			   <td class="type-whrite" align="left" >
																			   <input type="button" value="Delete"  onclick="deleteFunction();"/>
																			   </td>
																	   </tr>	
																	 </table> 
																	 <% }%>
																				

																	
																	
														</td>
													</tr>
													<tr>
														<TD align="left"></TD>
														<TD align="left"></TD>
													</tr>
													
												</table>	</form>
												<br>
												<br>
														<% if(totCount > Integer.parseInt(recs)) {%>
		<a href="javascript:callAction(0)">First</a>
		<a href="javascript:callAction(<%=(dispLast-1)*intRecs%>)">Last</a>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<% if(intOffSet+intRecs < totCount){	%>																				 
				<a href="javascript:callAction(<%=intOffSet+intRecs%>)">Next</a>
			<% } %>	
			<% if(intOffSet > 0) { %>
				<a href="javascript:callAction(<%=intOffSet-intRecs%>)">Previous</a>
			<% } %>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			(Displaying records <%=intOffSet+1%> to <%= (intOffSet+intRecs) > totCount ? totCount : (intOffSet+intRecs)%> of <%=totCount%> matching the search criteria)
			<br>
			<br>
	<% } %>	
												
												<DIV></DIV>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						
						</table>
						
						
  
						
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
</html:html>

