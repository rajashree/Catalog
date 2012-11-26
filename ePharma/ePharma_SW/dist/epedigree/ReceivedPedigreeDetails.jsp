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

if(pedigreeID == null )
pedigreeID = request.getParameter("PedigreeId");

System.out.println("pedigreeID  : "+pedigreeID);
if(pagenm == null) { pagenm = "";}
if(tp_company_nm == null) { tp_company_nm = "";}
String xQuery="";
byte[] xmlResults;

com.rdta.tlapi.xql.Connection connection = ConnectTL();
com.rdta.tlapi.xql.Statement statement = getStatement(connection);
String pedigree="";

%>


<html:html>
<head>
<title>Raining Data ePharma - ePedigree</title><LINK href="../../assets/epedigree1.css" type="text/css" rel="stylesheet">
</head>
<body>
<%@include file='topMenu.jsp'%>
<%List replist =(List)request.getAttribute("receiveped");
  System.out.println("receiveped list "+replist);
%>
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
								<td class="td-typeblack" align="right"><FONT color="#0000cc">Anti Counterfeit Measures</FONT>
								</td>
							</tr>
							<tr class="tableRow_Header">
								<td colspan="2">
		<table width="100%" id="Table3" cellSpacing="1" cellPadding="1" align="left" border="0"
		bgcolor="white">
		<tr>
		<td colspan="5">

		<table border="1" cellpadding="1" cellspacing="1" width="100%">
         <html:form action="/dist/epedigree/ReceivedPedigreeDetails.do" method="post">
          <bean:size id="rped" name="<%=Constants.RECEIVEDPEDIGREE_DETAILS %>" />
	      <input type="hidden" name="rped" value="<%=rped%>">
	      
			<%@ include file="Head2.jsp"%>
		</table>	
		
		</td>
		</tr>
		
								
								
								<logic:equal name="ped" value="0">
								         <tr> <td>No records</td></tr>
								</logic:equal>
									     
								<logic:notEqual name="ped" value="0">
									<logic:iterate name="<%=Constants.RECEIVEDPEDIGREE_DETAILS%>" id="recvd" type="com.rdta.epharma.epedigree.action.ReceivedPedigreeForm">
									     
									     <tr class="tableRow_Header">
									     	<td class="type-whrite">Pedigree ID : <STRONG><FONT color="#ffff00"><bean:write name="recvd" property="pedigreeid"/></FONT></STRONG></td>
									     	
									     	
									     	<td class="type-whrite">Date Received : <FONT color="#ffff00"><STRONG>
									     	<% String transDateAndTime = recvd.getDaterecvd();
											   String transdate[] = transDateAndTime.split("T"); 
											   if(transdate.length>0){
												   String time = transdate[1];
												   String t[] = time.split("\\.");
											%>
											<%=transdate[0]%>
											</STRONG></FONT></td>
											<td class="type-whrite">Time Received : <FONT color="#ffff00"><STRONG>											
												<%=t[0]%>
											</STRONG></FONT></td>
											<% } %>
									     
									     </tr>
									     
									</logic:iterate>							
								</logic:notEqual>
								
				</table>
				</tr>
			  	 </td>				
								
							   <tr>
								<Td colspan="2"><br>
									<table border="0" cellpadding="0" cellspacing="1" width="100%" ID="Table7">
									</table>
									<TABLE id="Table9" cellSpacing="1" cellPadding="0" width="100%" border="0">
										<TR>
											<TD class="td-typeblack" colSpan="7">Item Information<FONT color="#336600"></FONT></TD>
										</TR>
										<TR class="tableRow_Header">
											
											
											<TD class="type-whrite" height="13">Lot Number</TD>
											<TD class="type-whrite" height="13">Quantity</TD>
											<TD class="type-whrite" height="13">Expiration Date</TD>
											<TD class="type-whrite" height="13">Item Serial Number</TD>
										</TR>
										                            <logic:equal name="rped" value="0">
									                          			<tr>       
									                          			<td>No records</td>
									                          			</tr>
									                                 </logic:equal>
									                                 <logic:notEqual name="rped" value="0">
									                                  <logic:iterate name="<%=Constants.RECEIVEDPEDIGREE_DETAILS%>" id="rp">
									                                 	
																	<tr class="tableRow_Off">
																		 <td>  	
																		   	<bean:write name="rp" property="lotnum"/>
																		</td>
																		<td>
																			<bean:write name="rp" property="quantity"/>
																		</td>
																		<td>
																			<bean:write name="rp" property="expirationdate"/>
																		</td>	
																		<td>
																			<bean:write name="rp" property="itemserialnumber"/>
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
										
										
										                            <logic:equal name="rped" value="0">
									                          			<tr>       
									                          			<td>No records</td>
									                          			</tr>
									                                 </logic:equal>
									                                 <logic:notEqual name="rped" value="0">
									                                  <logic:iterate name="<%=Constants.RECEIVEDPEDIGREE_DETAILS%>" id="rp" type="com.rdta.epharma.epedigree.action.ReceivedPedigreeForm">
									                                 	
																	<tr class="tableRow_Off">
																		 <td>  	
																		   	<bean:write name="rp" property="signame"/>
																		</td>
																		<td>
																			<bean:write name="rp" property="title"/>
																		</td>
																		<td>
																			<bean:write name="rp" property="telephone"/>
																		</td>	
																		<td>
																			<bean:write name="rp" property="sigemail"/>
																		</td>
																		<td>
																			<bean:write name="rp" property="url"/>
																		</td>	
																		<td>
																		<% String DateAndTime = rp.getSignaturedate();
																		   String date[] = DateAndTime.split("T"); 
																		   if(date.length>0){
																		   String time = date[1];
																		   String t[] = time.split("\\.");
																		   System.out.println("time size : "+t.length);
																		%>
																			<%=date[0]%>
																		</td>
																		<td>
																			<%=t[0]%>
																		</td>
																		 <% } %>
																	</tr>
																	</logic:iterate>							
																	</logic:notEqual>									
									</TABLE>
								</Td>
							</tr>
							
							
													
												</TABLE>
												
										
													
										
										
												
												</table>
											</TD>
										</TR>
									</TABLE>
								</Td>
							
							</tr>
							
										
										
							
			 </table>
			 </html:form>
			  </TD>
			 </tr>
	</table>										
<jsp:include page="../globalIncludes/Footer.jsp" />
</body>
</html:html>