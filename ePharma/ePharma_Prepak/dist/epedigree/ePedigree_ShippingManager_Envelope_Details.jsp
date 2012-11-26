<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.catalog.Constants" %>
<%@ page import="com.rdta.epharma.epedigree.action.EnvelopeForm"%>
<%@ include file='../../includes/jspinclude.jsp'%>

<%
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");
String sessionID = (String)session.getAttribute("sessionID");
String tdate = (String)request.getAttribute("date");
String ttime = (String)request.getAttribute("time");
String transdate = (String)request.getAttribute("trnsDate");
String transtime = (String)request.getAttribute("transTime");
if(pagenm == null) { pagenm = "";}
if(tp_company_nm == null) { tp_company_nm = "";}
String envelopeId = request.getParameter("envelopeId");
System.out.println("Envelope ID in jsp: "+envelopeId);
request.setAttribute("envId",envelopeId);
%>

<html:html>
<head>
<title>Raining Data ePharma - ePedigree</title><LINK href="../../assets/epedigree1.css" type="text/css" rel="stylesheet">
</head>
<script language="JavaScript" type="text/JavaScript">
<!--
function Alert()
{
 frm = document.forms[0];
 
 frm.action ="window.open(','width=600,height=500')";
   frm.submit();

}
-->
</script>
<body>
<%@include file='topMenu.jsp'%>

  <% List list = (List)request.getAttribute("envDetails"); %>
	<table id="Table6" cellSpacing="0" cellPadding="0" width="100%" border="0">
		<tr>
		  <td class="td-rightmenu" vAlign="middle" width="1%"><IMG height="10" src="../../assets/images/space.gif" width="10"></td>
		</tr>	
		<tr>	
           <TD class="td-typeblack">
               <html:form action="/dist/epedigree/envelopeDetails.do" method="post">
                   <table id="Table8" cellSpacing="0" cellPadding="0" width="100%" border="0">
						<tr>
							 <td colspan="2"><br>
							   <table id="Table11" cellSpacing="1" cellPadding="1" width="100%" align="left" bgColor="white" border="0">
							        <tr>
									 <td class="td-typeblack" colspan="7">Envelope Details  <FONT color="#336600"></FONT></td>
									</tr>
									   <logic:equal name="envId" value="0">
									     	<tr>       
									        	<td>No records</td>
									     	</tr>
									   </logic:equal>
									    <logic:notEqual name="envId" value="0">
										     <tr class="tableRow_Header">
												<td class="type-whrite">Envelope ID: <br>	<FONT color="#ffff00"><STRONG>  	
										           <bean:write name="EnvelopeForm" property="envelopeID"/></STRONG></FONT>
												</td>
												<td class="type-whrite">Date : <br>	<FONT color="#ffff00"><STRONG>
													<%=tdate%></STRONG></FONT>
												</td>
												<td class="type-whrite">Time : <br>	<FONT color="#ffff00"><STRONG>
													<%=ttime%></STRONG></FONT>
												</td>
											<td class="type-whrite">Source : <br>	<FONT color="#ffff00"><STRONG>
											    <bean:write name="EnvelopeForm" property="source"/></STRONG></FONT>
											</td>	 
											<td class="type-whrite">Destination : <br>	<FONT color="#ffff00"><STRONG>
												<bean:write name="EnvelopeForm" property="destination"/></STRONG></FONT>
							           		</td>																										
										  </tr>
									</logic:notEqual> 
							  </table>
							 </td>
							</tr>
							
							   <tr>
								<Td colspan="2"><br>
									<TABLE id="Table9" cellSpacing="1" cellPadding="0" width="100%" border="0">
										<TR>
											<TD class="td-typeblack" colSpan="7">Container/Packaging <FONT color="#336600"></FONT></TD>
										</TR>
										<TR class="tableRow_Header">
											<TD class="type-whrite" height="13">Container Code</TD>
											<TD class="type-whrite" height="13">Pedigree ID</TD>
											<TD class="type-whrite" height="13">Quantity</TD>
											<TD class="type-whrite" height="13">Lot Number</TD>
										</TR>										
										 <logic:equal name="envDetails" value="0">
									            <tr>       
									               <td>No records</td>
									            </tr>
									      </logic:equal>
									      <logic:notEqual name="envDetails" value="0">
												<logic:iterate name="<%=Constants.ENVELOPE_DETAILS%>" id="envd">
													<tr class="tableRow_Off">
														<td>
															<bean:write name="envd" property="containercode"/>
														</td>
														<td>
															<bean:write name="envd" property="pedigreeID"/>
														</td>
														<td>
															<bean:write name="envd" property="quantity"/>
														</td>
														<td>
															<bean:write name="envd" property="lotnumber"/>
														</td>
													</tr>
											</logic:iterate>	
									  </logic:notEqual>	
    								</TABLE>
								</Td>
							</tr>
							
							 <tr>
								<Td colspan="2"><br>
									<TABLE id="Table9" cellSpacing="1" cellPadding="0" width="100%" border="0">
										<TR>
											<TD class="td-typeblack" colSpan="7">Drug Sales &amp; Distribution - <FONT color="#336600">Total:<bean:write name="envd" property="count"/></FONT></TD>
										</TR>
										<TR class="tableRow_Header">
											<TD class="type-whrite" height="13">Pedigree ID</TD>
											<TD class="type-whrite" height="13">Status</TD>
											<TD class="type-whrite" height="13">Transaction Date</TD>
											<TD class="type-whrite" height="13">Transaction Time</TD>
											<TD class="type-whrite" height="13">Drug Name</TD>
											<TD class="type-whrite" height="13">NDC/Drug Code</TD>
											<TD class="type-whrite" height="13">Attachment</TD>
										</TR>	
										   <logic:equal name="envDetails" value="0">
									    		<tr>       
									        		<td>No records</td>
									        	</tr>
									       </logic:equal>
									       <logic:notEqual name="envDetails" value="0">
												<logic:iterate name="<%=Constants.ENVELOPE_DETAILS%>" id="envd">
													<tr class="tableRow_Off">
														<td>
														
															<a href="Shipping_PedigreeDetails.do?envId=<bean:write name="EnvelopeForm" property="envelopeID"/>&PedigreeId=<bean:write name="envd" property="pedigreeID"/>&pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>&linkname=ShipSum">
															<bean:write name="envd" property="pedigreeID"/>
														</td>	
														<td>
															<bean:write name="envd" property="status"/>
														</td>	
														<td>
														    <%=transdate%>
														</td>
														<td>
															<%=transtime%>
														</td>
														<td>
															<bean:write name="envd" property="drugname"/>
														</td>
														<td>
															<bean:write name="envd" property="drugcode"/>
														</td>
														<td>
															<bean:write name="envd" property="attachment"/>
														</td>
													</tr>
											  </logic:iterate>
									    </logic:notEqual>
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