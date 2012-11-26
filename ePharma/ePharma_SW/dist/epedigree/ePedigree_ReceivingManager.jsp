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

if(pagenm == null) { pagenm = "";}
if(tp_company_nm == null) { tp_company_nm = "";}

System.out.println("**************I am inside ePedigree_ReceivingManager.jsp ******************** ");
String recs = com.rdta.catalog.Constants.NO_OF_RECORDS;


%>
<html:html>
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
<!--
function find()
{
var acessfind ="<%= (String )request.getAttribute("accessfind")%>"
frm = document.forms[0];
var fdt=frm.fromDt.value;
var tdt=frm.toDt.value;
if(acessfind == "false")
{
 alert("Acess Denied !...");
  return false;
 } else if(acessfind == "true"){
  	if(fdt!="" || tdt!=""){
  	
  if(fdt<tdt){
     frm.submit();
		return true;

	}else{
	 	alert("from date shlould be less than to date")
	 	return false;
	}
	}
	}
frm.submit();

}
function clearform(){
document.forms[0].fromDt.value = "";
document.forms[0].toDt.value = "";
document.forms[0].containerCode.value = "";
document.forms[0].prodNDC.value = "";
document.forms[0].lotNum.value = "";
document.forms[0].trNum.value = "";
document.forms[0].envelopeId.value = "";
document.forms[0].status.value = "";
document.forms[0].tpName.value = "";
}

function receiptform(){
 
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
  alert("Select an Envelope to Certify "); 
  return false;
 }
//window.open('CertifyReceiptReceiving.do?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>&' + checkVal ,'apnwin','width=300,height=150')
if (window.showModalDialog) {	
	window.showModalDialog('CertifyReceiptReceiving.do?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>&' + checkVal ,'apnwin','dialogWidth:300px;dialogHeight:200px;scroll=no;status=no');
	 }else {
		window.open('CertifyReceiptReceiving.do?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>&' + checkVal,'name','left=380,top=250,height=180,width=300,toolbar=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=no,modal=yes');
	}

}

function callAction(offsetter){	
	var frm = document.forms[0];
	document.getElementsByName('offset')[0].value = offsetter;	
	frm.submit();
}

function authenticate()
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
  alert("Please Select an Envelope"); 
  return false;
 }
  
 //window.open('AuthenticatePedigreesReceiving.do?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>&' + checkVal,'apnwin','width=300,height=150')
 if (window.showModalDialog) {	
	window.showModalDialog('AuthenticatePedigreesReceiving.do?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>&' + checkVal ,'apnwin','dialogWidth:300px;dialogHeight:200px;scroll=no;status=no');
	 }else {
		window.open('AuthenticatePedigreesReceiving.do?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>&' + checkVal,'name','left=380,top=250,height=180,width=300,toolbar=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=no,modal=yes');
	}

 
}



-->
</script>

</script>

<body>
		
		<%@include file='topMenu.jsp'%>
		<% List list = (List)request.getAttribute("List"); %>
		
		<%
			 System.out.println("The List object from ReceivingManagerAction in ReceivingManager Form is:"+list.toString()); 
				
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
		
			
				int c = 0;
				for(int count=0 ; count<totCount; count++){
					 c = totCount/(Integer.parseInt(recs));
				 	 int extraCount = totCount - c*(Integer.parseInt(recs));
									 
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
								<table id="Table9" cellSpacing="1" cellPadding="1" width="100%" align="left" bgColor="white" border="0">
									<tr>
										   <td class="td-typeblack">
										   <html:form  action="/dist/epedigree/ePedigree_ReceivingManager.do" method="get">
										   <html:hidden property="offset" value="" />
											<bean:size id="rec" name="<%=Constants.RCVNG_MNGR_DETAILS%>" />
                                                               <html:hidden property="sessionID" value="<%=sessionID%>" />
															   <html:hidden property="pagenm" value="<%=pagenm%>" />
															   <html:hidden property="tp_company_nm" value="<%=tp_company_nm%>" />
															
												<table border="1" cellspacing="0" cellpadding="0" align="center">
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
																<TR class="tableRow_On">

																<td><html:text property="fromDt" size="10" /><img src="<html:rewrite page='/IMG/img.gif' />" width="16" height="16" onclick="return showCalendar('fromDt', '%Y-%m-%d', '24', true);">

																

																	<td><html:text property="toDt" size="10" /><img src="<html:rewrite page='/IMG/img.gif' />" width="16" height="16" onclick="return showCalendar('toDt', '%Y-%m-%d', '24', true);">

																	
																	<td><html:text property="containerCode" size="20" /></td>
																<td><html:text property="prodNDC" size="20" /></td>
																<td><html:text property="lotNum" size="15"/></td>
																<td><html:text property="trNum" size="15" /></td>
																	
																 </tr>
																 
																 <tr class="tableRow_Header">
																    <TD class="type-whrite" align="left">PedigreeID</TD>
																    <TD class="type-whrite" align="left">Status</TD>
															        <TD class="type-whrite" align="left">Trading Partner Name</TD>
																    <td></td>
																	<td></td>
																    <td></td>
															</tr>
																 
															
														 <tr class="tableRow_On">
																<td><html:text property="envelopeId" size="20" /></td>
																
																<td><html:select property="status" >
																<html:option value = "">SelectOne</html:option>
																<html:option value = "Received">Received</html:option>
																<html:option value = "Authenticated">Authenticated</html:option> 
																<html:option value = "Certified">Certified</html:option>
																<html:option value = "Goods Received Full">Goods Received Full</html:option>
																<html:option value = "Goods Received Partial">Goods Received Partial</html:option>
																<html:option value = "Not Authenticated">Not Authenticated</html:option>
																<html:option value = "Decommisioned">Decommisioned</html:option>
																<html:option value = "Quarantined">Quarantined </html:option>
																</html:select></td>
																<td><html:select property="tpName" ><html:option value = "" >SelectOne</html:option>
																	
																	<% List tpNamesss = (List)request.getAttribute("tpNames");
																  	   System.out.println("tp names in jsp: "+tpNamesss);
																       for(int i=0;i<tpNamesss.size();i++){
																     %>
																	<html:option value ="<%=tpNamesss.get(i).toString()%>"><%=tpNamesss.get(i).toString()%></html:option>
																	<% } %>
																	
																	</html:select></td>
																<td></td>
																<td></td>
																<td></td>
                                                         </tr>
														<tr class="tableRow_Off">	
														<td colspan=10 align=center>
														<html:button property="button1" value="Find" onclick="find()" />
														<html:button property="button2" value="Clear" onclick="clearform()" /></td>

																	<td></td>
																	<td></td>
																	<td></td>
														</tr> 
													</TR>
																	
													<tr>
														<td align="left">
															<!-- Dashboard Start -->
															<tr>
														  
															<table id="Table12" cellSpacing="1" cellPadding="0" width="100%" align="center" border="0">
																
																<tr></tr>
																<tr bgColor="white">
														</tr>
														
														</table>
														
														<tr>
														<td>

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
												
												
											<% } %>
														
														</td>
													<tr>
														
															
															<tr bgColor="white">
														<td class="td-typeblack" colSpan="1">ENVELOPE DETAILS:</td>
													</tr>
													
													    <tr>
														  <td align="left">
															<!-- Dashboard Start -->
															<tr>
														<td align="left">
														
															<table id="Table12" cellSpacing="1" cellPadding="0" width="100%" align="center" border="0">
																<TBODY>
																	 
																<tr class="tableRow_Header">       
																		<td class="type-whrite" noWrap align="center">Select</td>
																		<td class="type-whrite"  align="center">Envelope#</td>
																		<td class="type-whrite"  noWrap align="center">Date Received</td>
																		<td class="type-whrite"  align="center">Trading Partner-Source</td>
																		<td class="type-whrite"  align="center">Trading Partner-Destination</td>
																		<td class="type-whrite"   align="center">Number of Pedigrees</td>
																		
																</tr>
																<logic:equal name="rec" value="0">
																	 <TR class='tableRow_Off'><TD class='td-content' colspan='9' align='center'>No PedigreeEnvelopes Match the Input Criteria...</TD></TR>
									                            </logic:equal>
																<logic:notEqual name="rec" value="0">										           
													               <logic:iterate name="<%=Constants.RCVNG_MNGR_DETAILS%>" id="rcvng" length="<%=recs%>" offset="<%=offset%>">
																	    <tr class="tableRow_Off">
																	
																		    <td>
																		     <input type ="radio"  name="check" value='<bean:write name="rcvng" property="envelopeId"/>'>  																	
																		    </td>
																		
																		    <td class='td-content'><a href="envelopeDetails.do?envelopeId=<bean:write name='rcvng' property='envelopeId'/>&pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>">
																			 <bean:write name="rcvng" property="envelopeId"/>
																		    </td>
																		
																		    <td>
																			  <bean:write name="rcvng" property="dateReceived"/>
																		    </td>
																		    <td>
																			 <bean:write name="rcvng" property="source"/>
																		    </td>
																		    <td>
																			  <bean:write name="rcvng" property="destination"/>
																		   </td>
																		    <td>
																			 <bean:write name="rcvng" property="numOfPedigrees"/>
																		   </td>
																		
																		
																	    </tr>
															     </logic:iterate>
													           </logic:notEqual>
													    </tr>
														<TR class="tableRow_Header">
															<TD align="center" colspan=9>
															     <html:button property="button4" value="Authenticate All Pedigrees" onclick = "authenticate()" />
															     <html:button property="button3" value="Reconcile Goods" onclick = "receiptform()" />	
															</TD>
														</TR>			    
												</table>
												
												<tr> <td>
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
												
												
												<% } %>														    							
										 </html:form>	
										  </td>
										</tr>
								</table>	
					  </table>
					</td>
			    </tr>
			</table>
    <jsp:include page="../globalIncludes/Footer.jsp" />
</body>			
</html:html>	
	