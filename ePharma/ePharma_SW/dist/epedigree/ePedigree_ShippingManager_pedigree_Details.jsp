<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.catalog.Constants" %>
<%@ include file='../../includes/jspinclude.jsp'%>

<%
String pagenm = request.getParameter("pagenm");
if(pagenm==null)pagenm="pedigree";
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
function repackProducts(url){
	var envId='<%=envId%>';
	var PedigreeId='<%=pedigreeID%>';
	var tp_company_nm='<%=tp_company_nm%>'
	var tempurl=url+"&PedigreeId="+PedigreeId+"&tp_company_nm="+tp_company_nm+"&collection=ShippedPedigree";
	
window.open(tempurl,"repackgedProduct","left=20,top=20,width= 550,height=300,toolbar=0,scrollbar=1,resizable=1,location=0,status=0");
	return false;
}
function newWindow(  htmFile  ) {
	msgWindow = window.open( htmFile, "WindowName", "left=20,top=20,width= 600,height=400,toolbar=0,scrollbar=1,resizable=1,location=0,status=0");
}

function MM_openBrWindow(theURL,winName,features) { 
	var frm =document.forms[0];
	var pedigreeID= frm.PedigreeId.value;
	var url = "<html:rewrite action='/checkPedigree?pedigreeID='/>"+pedigreeID;
	
	//frm.submit();
	window.open(url,winName,features);
}

 function newWindow1(  htmFile  ) {
		   msgWindow = window.open( htmFile, "WindowName", "left=20,top=20,width= 700,height=430,toolbar=0,scrollbar=1,resizable=1,location=0,status=0");
	  }


function sendEmail(){
	var frm= document.forms[0];
	var pedigreeID= frm.PedigreeId.value;
	frm.action="<html:rewrite action='/dist/epedigree/sendPedigree?pedigreeID='/>"+pedigreeID;
	frm.submit();
}
</script>

<html:html>
<head>
<title>Raining Data ePharma - ePedigree</title><LINK href="../../assets/epedigree1.css" type="text/css" rel="stylesheet">
</head>
<body>
<%@include file='topMenu.jsp'%>
<%List lists=(List)request.getAttribute("sped");%>

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
								<td class="td-typeblack">Pedigree Details</td>
							</tr>
							<tr class="tableRow_Header">
								<td colspan="2">
									<table width="100%" id="Table3" cellSpacing="1" cellPadding="1" align="left" border="0"
										bgcolor="white">
										<tr>
											<td colspan="5">
												<table border="1" cellpadding="1" cellspacing="1" width="100%">
         										<form>
         										<bean:size id="ped" name="<%=Constants.SHIPPEDPEDIGREE_DETAILS %>" />
	   											<input type="hidden" name="ped" value="<%=ped%>">
	      									<%@include file= 'Header.jsp'%>
									</TR>
												
											</td>
									</tr>
								</td>
							   	</tr>
							   	<table border="1" cellpadding="1" cellspacing="1" width="100%">
							    <tr class="tableRow_Header">
								 		
								 		 <logic:equal name="ped" value="0">
								            <tr> <td>No records</td></tr>
									     </logic:equal>
									     
									     <logic:notEqual name="ped" value="0">
									     <logic:iterate name="<%=Constants.SHIPPEDPEDIGREE_DETAILS%>" id="env" type="com.rdta.epharma.epedigree.action.PedigreeDetailsForm">
									     
									     <tr class="tableRow_Header">
									     	<td class="type-whrite">Pedigree ID : <br><STRONG><FONT color="#ffff00"><STRONG><bean:write name="env" property="pedigreeId"/></FONT></STRONG></td>
									     	<td class="type-whrite">Transaction Date : <br>	<FONT color="#ffff00"><STRONG>
									     	<% String transDateAndTime = env.getTransactionDate();
											   String transdate[] = transDateAndTime.split("T"); 
											   if(transdate.length>0){
											%>
											<%=transdate[0]%>
											</STRONG></FONT></td>
											<td class="type-whrite">Transaction Time: <br><FONT color="#ffff00"><STRONG>
											<%if(transdate.length>1){
												   String time = transdate[1];
												   String t[] = time.split("\\.");%>
												<%=t[0]%>
											<% } else{%>N/A <%}}%>
									     	</STRONG></FONT></td>
									     	<td class="type-whrite">Transaction Type: <br><FONT color="#ffff00"><STRONG><bean:write name="env" property="transactionType"/></STRONG></FONT></td>
									     	<!-- <td class="type-whrite">Transaction # : <br><STRONG><FONT color="#ffff00">
									     	<% String trType = env.getTransactionType();
									     	   System.out.println("trType in jsp: "+trType);
									     	   if(trType.equals("PurchaseOrderNumber")){
									     	  %>
									     	<A HREF=" javascript:newWindow('ePedigree_ViewOrder.jsp?trType=PurchaseOrder&trNum=<bean:write name="env" property="transactionNo"/>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree')" class="typeblue3-link" ><bean:write name="env" property="transactionNo"/></A></FONT></STRONG></td>
									     	<%} else{ %>
									     	<A HREF=" javascript:newWindow('ASN_Details.jsp?trType=DespatchAdvice&trNum=<bean:write name="env" property="transactionNo"/>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree')" class="typeblue3-link" ><bean:write name="env" property="transactionNo"/></A></FONT></STRONG></td>
									     	<% } %> -->
									     </tr>
									     
										</logic:iterate>							
										</logic:notEqual>
								</table>
																	
						</table>
						
						<tr class="tableRow_On">
								<td class="td-menu bold" width="50%">From</td>
								<td class="td-menu bold">To</td>
						</tr>
						
						</tr>
						
			<tr class="tableRow_Off">
			
				<td>
				
				<logic:iterate name="<%=Constants.SHIPPEDPEDIGREE_DETAILS%>" id="from">
				<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td class="td-menu">Company: &nbsp;&nbsp;&nbsp;&nbsp;<STRONG><bean:write name="from" property="fromCompany"/></STRONG></td>
				</tr>
				<tr>
					<td class="td-menu">Ship Address: <bean:write name="from" property="fromShipAddress"/></td>
				</tr>
                  
              	<TR>
                	<TD class="td-menu">Bill Address: &nbsp;<bean:write name="from" property="fromBillAddress"/></TD>
              	</TR>
              	<tr>
              		<td class="td-menu">Contact: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="from" property="fromContact"/></td>
              	</tr>
              	<TR>
                	<TD class="td-menu">Title: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="from" property="fromTitle"/></TD>
                </TR>
                <tr>
                	<td class="td-menu">Phone: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="from" property="fromPhone"/></td>
                </tr>
                <tr>
                	<td class="td-menu">Email: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="from" property="fromEmail"/></td>
                </tr>
                <tr>
                	<td class="td-menu">License: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="from" property="fromLicense"/></td>
                </tr>
                </table>
                </logic:iterate>
                
				</td>
								
				<td>
				
				<logic:iterate name="<%=Constants.SHIPPEDPEDIGREE_DETAILS%>" id="to">
				<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td class="td-menu">Company: &nbsp;&nbsp;&nbsp;&nbsp;<STRONG><bean:write name="to" property="toCompany"/></STRONG></td>
				</tr>
				<tr>
					<td class="td-menu">Ship Address: <bean:write name="to" property="toShipAddress"/></td>
				</tr>
                <TR>
                	<TD class="td-menu">Bill Address: &nbsp;<bean:write name="to" property="toBillAddress"/></TD>
                </TR>
                <tr>
                	<td class="td-menu">Contact: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="to" property="toContact"/></td>
                </tr>
                <TR>
                	<TD class="td-menu">Title: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="to" property="toTitle"/></TD>
                </TR>
                <tr>
                	<td class="td-menu">Phone: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="to" property="toPhone"/></td>
                </tr>
                <tr>
                	<td class="td-menu">Email: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="to" property="toEmail"/></td>
                </tr>
                <tr>
                	<td class="td-menu">License: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="to" property="toLicense"/></td>
				</tr>
				</table>
				</logic:iterate>
				
				</td>
				
			</tr>
							   <tr>
								<Td colspan="2"><br>
									<table border="0" cellpadding="0" cellspacing="1" width="100%" ID="Table7">
									</table>
									<TABLE id="Table9" cellSpacing="1" cellPadding="0" width="100%" border="0">
										<TR>
											<TD class="td-typeblack" colSpan="7">Products <FONT color="#336600"></FONT></TD>
										</TR>
										<TR class="tableRow_Header">
											<TD class="type-whrite" height="13">Name</TD>
											<TD class="type-whrite" height="13">Product Code</TD>
											<TD class="type-whrite" height="13">Code Type</TD>
											<TD class="type-whrite" height="13">Manufacturer</TD>
											<TD class="type-whrite" height="13">Quantity</TD>
											<TD class="type-whrite" height="13">Dosage Form</TD>
											<TD class="type-whrite" height="13">Strength</TD>
											<TD class="type-whrite" height="13">Container Size</TD>
											<TD class="type-whrite" height="13">Repackaged</TD>
										</TR>
										
											<logic:equal name="ped" value="0">
									   		<tr>       
									  		<td>No records</td>
									   		</tr>
									   		</logic:equal>
									      
									      <logic:notEqual name="ped" value="0">
									      <logic:iterate name="<%=Constants.SHIPPEDPEDIGREE_DETAILS%>" id="en">
									                                 	
											<tr class="tableRow_Off">
											 <td>  	
											 	<bean:write name="en" property="drugName"/>
											</td>
											<td>
												<bean:write name="en" property="productCode"/>
											</td>
											<td>
												<bean:write name="en" property="codeType"/>
											</td>
											<td>
												<bean:write name="en" property="manufacturer"/>
											</td>	
										    <td>  	
											   	<bean:write name="en" property="quantity"/>
											</td>
											<td>
												<bean:write name="en" property="dosageForm"/>
											</td>
											<td>
												<bean:write name="en" property="strength"/>
											</td>
											<td>
												<bean:write name="en" property="containerSize"/>
											</td>	
											<td>
											<logic:equal name="en" property="rePack" value="true">
											<html:link href="#" onclick="javascript:repackProducts('repackagedProducts.do?')">
											YES
											</html:link>
											</logic:equal>
											<logic:equal name="en" property="rePack" value="false">
						   					 NO
											</logic:equal>
											</td>															
											</tr>
										</logic:iterate>							
										</logic:notEqual> 
																	
																	
									</TABLE>
								</Td>
							</tr>
							
							 <tr>
								<Td colspan="2"><br>
									<table border="0" cellpadding="0" cellspacing="1" width="100%" ID="Table7">
									</table>
									<TABLE id="Table9" cellSpacing="1" cellPadding="0" width="100%" border="0">
										<TR>
											<TD class="td-typeblack" colSpan="7">Drug Sales &amp; Distribution  <FONT color="#336600"></FONT></TD>
										</TR>
										<TR class="tableRow_Header">
											<TD class="type-whrite" height="13">Name</TD>
											<TD class="type-whrite" height="13">Address</TD>
											<TD class="type-whrite" height="13">Contact</TD>
											<TD class="type-whrite" height="13">Phone</TD>
											<TD class="type-whrite" height="13">Email</TD>
											
										</TR>
										
										                        	<logic:equal name="ped" value="0">
									                          			<tr>       
									                          			<td>No records</td>
									                          			</tr>
									                                 </logic:equal>
									                                 <logic:notEqual name="ped" value="0">
									                                  <logic:iterate name="<%=Constants.SHIPPEDPEDIGREE_DETAILS%>" id="dr">
									                                 	
																	<tr class="tableRow_Off">
																		 <td>  	
																		  <bean:write name="dr" property="custName"/>
																		</td>
																		<td>
																			<bean:write name="dr" property="custName"/>
																		</td>
																		<td>
																			<bean:write name="dr" property="custContact"/>
																		</td>	
																		<td>
																			<bean:write name="dr" property="custPhone"/>
																		</td>
																		<td>
																			<bean:write name="dr" property="custEmail"/>
																		</td>	
																		
																	</tr>
																	</logic:iterate>							
																	</logic:notEqual>								
									</TABLE>
								</Td>
							</tr>
							
							 <tr>
								<Td colspan="2"><br>
									<table border="0" cellpadding="0" cellspacing="1" width="100%" ID="Table7">
									</table>
									<TABLE id="Table9" cellSpacing="1" cellPadding="0" width="100%" border="0">
										<TR>
											<TD class="td-typeblack" colSpan="7">Signature - ContactInformation <FONT color="#336600"></FONT></TD>
										</TR>
										<TR class="tableRow_Header">
											<TD class="type-whrite" height="13">Name</TD>
											<TD class="type-whrite" height="13">Title</TD>
											<TD class="type-whrite" height="13">Telephone</TD>
											<TD class="type-whrite" height="13">Email</TD>
											<TD class="type-whrite" height="13">URL</TD>
											<TD class="type-whrite" height="13">SignatureDate</TD>
											<TD class="type-whrite" height="13">SignatureTime</TD>
										</TR>
										
										                            <logic:equal name="ped" value="0">
									                          			<tr>       
									                          			<td>No records</td>
									                          			</tr>
									                                 </logic:equal>
									                                 <logic:notEqual name="ped" value="0">
									                                  <logic:iterate name="<%=Constants.SHIPPEDPEDIGREE_DETAILS%>" id="dr1" type="com.rdta.epharma.epedigree.action.PedigreeDetailsForm">
									                                 	
																	<tr class="tableRow_Off">
																		 <td>  	
																		   	<bean:write name="dr1" property="signatureInfoName"/>
																		</td>
																		<td>
																			<bean:write name="dr1" property="signatureInfoTitle"/>
																		</td>
																		<td>
																			<bean:write name="dr1" property="signatureInfoTelephone"/>
																		</td>	
																		<td>
																			<bean:write name="dr1" property="signatureInfoEmail"/>
																		</td>
																		<td>
																			<bean:write name="dr1" property="signatureInfoUrl"/>
																		</td>	
																		<td>
																		<% String DateAndTime =dr1.getSignatureInfoDate();
																		   String date[] = DateAndTime.split("T"); 
																		   
																		%>
																			<%=date[0]%>
																		</td>
																		<td>
																		<%if(date.length>1){
																		   String time = date[1];
																		   String t[] = time.split("\\.");
																		   %>
																			<%=t[0]%>
																		</td>
																		 <% } else{%>
																			N/A
																			<%}%>
																		
																	</tr>
																	</logic:iterate>							
																	</logic:notEqual>									
									</TABLE>
								</Td>
							</tr>
							
							<tr>
							  <TD colSpan="2"><BR>
							        <table border="0" cellpadding="0" cellspacing="1" width="100%" ID="Table7">
									
									<TABLE id="Table10" cellSpacing="1" cellPadding="0" width="100%" border="0">
										<TR>
											<TD class="td-typeblack" colSpan="8">** Document Validity Status **</TD>
										</TR>
										<TR class="tableRow_Header">
											<TD class="tableRow_On"><STRONG><FONT color="#0000cc">&nbsp; LEVEL</FONT></STRONG></TD>
											<TD class="type-whrite" width="83" align="center">PedigreeSignature</TD>
											
								<% String status = (String)request.getAttribute("APNSigStatus"); 
								   System.out.println("Signature Staus in Jsp : "+status);	
								   if(status == null) status = "";		
								   String dateAndTime = (String)request.getAttribute("APNDate");
								   System.out.println("Date in jsp: "+dateAndTime);
								   String dat = ""; String time = "";
								   if(dateAndTime != null){
								   String date[] = dateAndTime.split("T");
								   dat = date[0];
								   time = date[1];
								   }
								%>
										<TR class="tableRow_Off">
											<TD class="td-menu"><STRONG><FONT color="#3300cc">STATUS</FONT></STRONG></TD>
											<TD class="td-menu" width="83">
												<P align="center"><STRONG><FONT color="#009900"><%=status %></FONT></STRONG></P>
											</TD>
											
										  
												
											</TD> 
										</TR>
										<TR class="tableRow_Off">
											<TD class="td-menu"><STRONG><FONT color="#3300cc">DATE</FONT></STRONG></TD>
											
											<TD class="td-menu" width="91">
												<P align="center"><STRONG><FONT color="#009900">&nbsp;</FONT><%=dat%></STRONG></P>
											</TD>
										   																					
										</TR>
										<TR class="tableRow_Off">
											<TD class="td-menu"><STRONG><FONT color="#3300cc">TIME</FONT></STRONG></TD>
											
											<TD class="td-menu" width="91">
												<P align="center"><STRONG><FONT color="#009900">&nbsp;</FONT><%=time%></STRONG></P>
											</TD>
										   																					
										</TR>
										<TR class="tableRow_Off">
											<TD class="td-menu"><STRONG><FONT color="#3300cc"></FONT></STRONG></TD>
											     <input type="hidden" name="PedigreeId" value="<%=pedId%>" />
											     <input type="hidden" name="pedigreeID" value="<%=pedId%>" />
										  <TD class="td-menu"><STRONG><P align="center"><FONT color="#3300cc"><a href='SignPedigreeShip.do?envId=<%=envId%>&pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>&PedigreeId=<%=pedId%>&linkname=ShipSum'>SIGN</a></FONT></STRONG></p></TD>	
										 																					 
										</TR>
										
										</table>
											</TD>
										</TR>
									</TABLE>
								</Td>
							
							</tr>
							
							</table>			
										
							<tr>
							  <TD colSpan="2"><BR>
									 EXPORT AS: 
										 <html:button  	property="button" onclick="MM_openBrWindow('/repackagePDF','APN','scrollbars=yes,resizable=yes,width=800,height=600,menubar=yes')" value="Print" />&nbsp;&nbsp;&nbsp;
									</TD>
									
									<TD colSpan="2">
									 SEND AS: 
										 <html:button  	property="button" onclick="sendEmail();" value="e-Mail" />
									</TD>
							
							</tr>
			 </table>
			 </form>
			  </TD>
			 </tr>
	</table>	<%@include file='onLoad.jsp'%> 									
<jsp:include page="../globalIncludes/Footer.jsp" />
</body>
</html:html>