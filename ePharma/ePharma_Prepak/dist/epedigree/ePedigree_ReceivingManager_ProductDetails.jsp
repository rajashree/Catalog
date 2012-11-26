<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.catalog.Constants" %>
<%@ include file='../../includes/jspinclude.jsp'%>
<% String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");
String sessionID = (String)session.getAttribute("sessionID");
String pedigreeID = request.getParameter("PedigreeId");
if(pedigreeID == null )
pedigreeID = request.getParameter("PedigreeId");
if(pagenm == null) { pagenm = "";}
if(tp_company_nm == null) { tp_company_nm = "";}

%>
<html:html>
   <head>
      <title>Raining Data ePharma - ePedigree</title>
      <LINK href="../../assets/epedigree1.css" type="text/css" rel="stylesheet">
      
   </head>
	<body>
	  <%@include file='topMenu.jsp'%>
	     	   <table border="0" cellspacing="0" cellpadding="0" width="100%" ID="Table1">
				<tr>
					<td></td>
					<td rowspan="2">&nbsp;</td>
				</tr>
				<td>
				  <!-- info goes here -->
						<table width="100%" id="Table2" cellSpacing="1" cellPadding="1" align="left" border="0" bgcolor="white">
							<tr bgcolor="white">
								<td class="td-typeblack"><FONT size="4">Product Details &amp; Authenticity</FONT></td>
								<td class="td-typeblack" align="right"><FONT color="#0000cc">Anti Counterfeit Measures</FONT>
								</td>
							</tr>
						  <html:form action="/dist/epedigree/RecProductsDetails.do"  method="post">
							<tr class="tableRow_Header">
								<td colspan="2">
										<tr>
											<td colspan="5">
												<table border="1" cellpadding="1" cellspacing="1" width="100%">
													<%@ include file="Head2.jsp"%>
												</table>
											</td>
										</tr>
										<tr>
										  <td colspan="2">
										    <table border="0" cellpadding="0" cellspacing="1" width="100%" ID="Table6">
										      <tr>
										       <td class="td-typeblack" colspan="7">
										        <table id="Table10" cellSpacing="1" cellPadding="0" width="100%" border="0">
										          <TBODY> 
										            <TR class="tableRow_Header">
															<TD class="type-whrite" align="center"><STRONG><bean:write name='recprddetails' property="drugName"/><FONT size="3" color="#ffffff"></FONT>
																</STRONG>
															</TD>
															<TD class="type-whrite" align="center">
																<FONT size="3" color="#ffffff"><STRONG>AUTHENTICITY</STRONG></FONT>
															</TD>
													</TR>
													<TR class="tableRow_On">
													  <TD class="tableRow_On" align="center" height="142"><STRONG><FONT color="#3300cc">
													 	<P align="center">
													 		<img src="<%=request.getContextPath()%>/servlet/showImageFromTL?dbname=EAGRFID&collname=ProductImage&topnode=Product&nodename=ProdIMG&ID=<bean:write name='recprddetails' property="pdcId"/>"/>
													 	</p>
													 	<P align="center">
													 	  <TABLE id="Table12" cellSpacing="1" cellPadding="1" border="0" width="100%" align="center">
													 	  
													 	   <TR class="tableRow_On">
															 <TD width="66"><STRONG>NDC:</STRONG>
									        				 </TD>
															 <TD>
																<P><html:text readonly='true' property="ndc" /></P>
			    										     </TD>
														   </TR>
														   <TR class="tableRow_On">
																<TD width="66"><STRONG>EPC:</STRONG></TD>
																 <TD>
																	<html:text readonly='true' property="epc" />
																</TD>
																<TD>&nbsp;</TD>
														  </TR>
													 	  <TR class="tableRow_On">
																<TD width="66"><STRONG>UPC/SKU:</STRONG></TD>
																 <TD>
																	<html:text readonly='true' property="upc" />
																</TD>
														  </TR>
														  <TR class="tableRow_On">
																<TD width="66"><STRONG>PBC:</STRONG></TD>
																 <TD>
																	<html:text readonly='true' property="pbc" />
																</TD>
														  </TR>
														  </TABLE>
													 	</p>
													 	</FONT></STRONG>
													  </TD>
													  <TD class="tableRow_On" align="center">
																<TABLE id="Table14" cellSpacing="1" cellPadding="0" border="0" align="center" width="100%">
																	<TR class="tableRow_Off">
																		<TD><STRONG><FONT size="1">Threshold:</FONT></STRONG></TD>
																		<TD>
																			<P align="center"><STRONG><FONT color="#669933" size="1">VALID</FONT></STRONG></P>
																		</TD>
																		<TD><STRONG><FONT color="#006600">2005-10-13</FONT></STRONG></TD>
																		<TD>&nbsp;</TD>
																	</TR>
																	<TR class="tableRow_On">
																		<TD><STRONG><FONT size="1">Recall:</FONT></STRONG></TD>
																		<TD>
																			<P align="center"><STRONG><FONT color="#669933" size="1">VALID</FONT></STRONG></P>
																		</TD>
																		<TD><STRONG><FONT color="#006600">2005-10-13</FONT></STRONG></TD>
																		<TD>&nbsp;</TD>
																	</TR>
																	<TR class="tableRow_Off">
																		<TD><STRONG><FONT size="1">Contents:</FONT></STRONG></TD>
																		<TD>
																			<P align="center"><STRONG><FONT color="#669933" size="1">VALID</FONT></STRONG></P>
																		</TD>
																		<TD><STRONG><FONT color="#006600">2005-10-13</FONT></STRONG></TD>
																		<TD>&nbsp;</TD>
																	</TR>
																	<TR class="tableRow_On">
																		<TD><STRONG><FONT size="1">Dosage:</FONT></STRONG></TD>
																		<TD>
																			<P align="center"><STRONG><FONT color="#669933" size="1">VALID</FONT></STRONG></P>
																		</TD>
																		<TD><STRONG><FONT color="#006600">2005-10-13</FONT></STRONG></TD>
																		<TD>&nbsp;</TD>
																	</TR>
																	<TR class="tableRow_Off">
																		<TD><STRONG><FONT size="1">Signature:</FONT></STRONG></TD>
																		<TD>
																			<P align="center"><STRONG><FONT color="#669933" size="1">VALID</FONT></STRONG></P>
																		</TD>
																		<TD><STRONG><FONT color="#006600">2005-10-13</FONT></STRONG></TD>
																		<TD>&nbsp;</TD>
																	</TR>
																</TABLE>
															</TD>
													</TR>
													<TR>
													   <TD class="type-whrite" width="265" height="13">&nbsp;</TD>
													   <TD class="type-whrite" width="141" height="13"></TD>
													</TR>
													<TR class="tableRow_Header">
													    <TD class="type-whrite" align="center"><FONT size="3" color="#ffffff"><STRONG>LOT INFORMATION</STRONG></FONT></STRONG>
														</TD>
														<TD class="type-whrite" align="center"><FONT color="#ffffff" size="3"><STRONG>PRODUCT INFORMATION</STRONG></FONT>&nbsp;
														</TD>
													</TR>
													<TR class="tableRow_On">
													 <TD class="type-whrite">
													   <TABLE id="Table4" cellSpacing="1" cellPadding="0" align="center" border="0" width="100%">
													     <TR class="tableRow_Off">
															<TD><STRONG><FONT size="1">Lot #:</FONT></STRONG></TD>
												            <TD>
																<html:text readonly='true' property="lot" />
															</TD>																				
														</TR>
														<TR class="tableRow_Off">
															<TD><STRONG><FONT size="1">Manufactured Date:</FONT></STRONG></TD>
												            <TD>
																<html:text readonly='true' property="mfgdate" styleClass='tableRow_Yellow' />
															</TD>																				
														</TR>
														<TR class="tableRow_Off">
															<TD><STRONG><FONT size="1">Product Expiration:</FONT></STRONG></TD>
												            <TD>
																<html:text readonly='true' property="pdcexpdate" styleClass='tableRow_Yellow' />
															</TD>																				
														</TR>
														<TR class="tableRow_Off">
															<TD><STRONG><FONT size="1">Lot Expiration:</FONT></STRONG></TD>
												            <TD>
																<html:text readonly='true' property="lotexpdate" styleClass='tableRow_Red' />
															</TD>																				
														</TR>
														<TR class="tableRow_Off">
															<TD><STRONG><FONT size="1">Manufacturer:</FONT></STRONG></TD>
												            <TD>
																<html:text readonly='true' property="mfg" />
															</TD>																				
														</TR>
													   </TABLE>
													 </TD>
													 
													 <TD class="type-whrite">
													 <TABLE id="Table15" cellSpacing="1" cellPadding="0" align="center" border="0" width="100%">
													  <TR class="tableRow_Off">
															<TD><STRONG><FONT size="1">Dosage Form:</FONT></STRONG></TD>
															
												            <TD><html:text readonly='true' property="doseForm"/> </TD>	
												            																			
													 </TR>
													 
													 <TR class="tableRow_Off">
															<TD><STRONG><FONT size="1">Dosage Strength:</FONT></STRONG></TD>
												             <TD><html:text  readonly='true' property="dosestr"/> </TD>	
													 </TR>
													 <TR class="tableRow_Off">
															<TD><STRONG><FONT size="1">Package Size:</FONT></STRONG></TD>
												            <TD><html:text readonly='true' property="paksize" /></TD>																				
													 </TR>
													 <TR class="tableRow_Off">
															<TD><STRONG><FONT size="1">Description:</FONT></STRONG></TD>
												            <TD><html:text readonly='true' property="dec" /></TD>																				
													 </TR>
													 <TR class="tableRow_Off">
															<TD><STRONG><FONT size="1">Marketing Status:</FONT></STRONG></TD>
												            <TD><html:text readonly='true' property="makstatus" styleClass='tableRow_Yellow' /></TD>																				
													 </TR>
													 <TR class="tableRow_Off">
															<TD><STRONG><FONT size="1">Quantity:</FONT></STRONG></TD>
												            <TD><html:text readonly='true' property="quantity" /></TD>																				
													 </TR>
													 <TR class="tableRow_On">
													  <TD><STRONG><FONT size="1">Threshold:</FONT></STRONG></TD>
														<TD align="left">
															<STRONG><FONT color="#669933" size="1">NA</FONT></STRONG>
														</TD>
													</TR>
													<TR class="tableRow_Off">
													 <TD colspan="2" align="center"><a href="ViewShipmentSensorData.html">View Sensor Recording Details</a>
													 </TD>
													</TR>
													
			    										 </TABLE>
													 </TD>
													</TR>
										          </TBODY>
										        </table>
										       </td>
										      </tr>
										    </table>
										  </td>
										</tr>
								
									
								</td>
							</tr>
						 </html:form>
						</table>
				
				</td>
						
	   </table>
	   <jsp:include page="../globalIncludes/Footer.jsp" />
	</body>
</html:html>	  