<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.catalog.Constants" %>
<%@ page import="com.rdta.epharma.epedigree.action.ReceivingManagerForm"%>
<%@ include file='../../includes/jspinclude.jsp'%>

<%
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");
String sessionID = (String)session.getAttribute("sessionID");
String pedigreeID =(String) request.getAttribute("PedigreeID");
System.out.println("Pedigree ID is "+pedigreeID);
if(pedigreeID == null )
pedigreeID = request.getParameter("PedigreeId");

System.out.println("Pedigree ID is "+pedigreeID);
session.setAttribute("pedigreeID",pedigreeID);

if(pagenm == null) { pagenm = "";}
if(tp_company_nm == null) { tp_company_nm = "";}
String pedigree="";

%>



<html:html>
<head>


<title>Raining Data ePharma - ePedigree</title><LINK href="../../assets/epedigree1.css" type="text/css" rel="stylesheet">
</head>


<body >

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
								<td class="td-typeblack"></td>
							</tr>
							<tr class="tableRow_Header">
								<td class="type-whrite" colspan="2"><font size="2" ><STRONG>Pedigree Details : <%=pedigreeID%><STRONG></font></td>
							</tr>
							<tr class="tableRow_Header">
								<td colspan="2">
									<table width="100%" id="Table3" cellSpacing="1" cellPadding="1" align="left" border="0"
										bgcolor="white">
										<tr>
											<td colspan="5">
												<table border="1" cellpadding="1" cellspacing="1" width="100%">
         										
         										<bean:size id="ped" name="<%=Constants.SHIPPEDPEDIGREE_DETAILS %>" />
	   											<input type="hidden" name="ped" value="<%=ped%>">
	      
													
												
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
                	<TD class="td-menu">Bill Address: &nbsp;&nbsp;<bean:write name="from" property="fromBillAddress"/></TD>
              	</TR>
              	<tr>
              		<td class="td-menu">Contact: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="from" property="fromContact"/></td>
              	</tr>
              	<TR>
                	<TD class="td-menu">Title: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="from" property="fromTitle"/></TD>
                </TR>
                <tr>
                	<td class="td-menu">Phone: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="from" property="fromPhone"/></td>
                </tr>
                <tr>
                	<td class="td-menu">Email: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="from" property="fromEmail"/></td>
                </tr>
                <tr>
                	<td class="td-menu">License: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="from" property="fromLicense"/></td>
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
                	<TD class="td-menu">Bill Address: &nbsp;&nbsp;<bean:write name="to" property="toBillAddress"/></TD>
                </TR>
                <tr>
                	<td class="td-menu">Contact: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="to" property="toContact"/></td>
                </tr>
                <TR>
                	<TD class="td-menu">Title: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="to" property="toTitle"/></TD>
                </TR>
                <tr>
                	<td class="td-menu">Phone: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="to" property="toPhone"/></td>
                </tr>
                <tr>
                	<td class="td-menu">Email: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="to" property="toEmail"/></td>
                </tr>
                <tr>
                	<td class="td-menu">License: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="to" property="toLicense"/></td>
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
											   <!--<a href = "RecProductsDetails.do?PedigreeId=<bean:write name="env" property="pedigreeId"/>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree&linkname=Products"> -->
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
																		System.out.println("Date and time : "+DateAndTime);
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
									
									
								</Td>
							<TD align="center">
									
									 <html:button property="close" value="Close" onclick="window.close()"/> 
								</TD>
							</tr>
							
							</table>			
										
							
			 </table>
			 
			  </TD>
			 </tr>
	</table> <%@include file='onLoad.jsp'%>										
<jsp:include page="../globalIncludes/Footer.jsp" />
</body>
</html:html>