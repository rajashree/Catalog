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
String SSCC = request.getParameter("SSCC");
//String sessionID = request.getParameter("sessionID");
String sessionID = (String)session.getAttribute("sessionID");
System.out.println("sess : "+sessionID);
String screenEnteredDate = (String)request.getAttribute("screenEnteredDate");
if(SSCC == null) { SSCC = "";}
if(pagenm == null) { pagenm = "";}
if(tp_company_nm == null) { tp_company_nm = "";}
System.out.println("*********Inside Recieving Manager jsp********");

com.rdta.tlapi.xql.Connection connection = ConnectTL();
com.rdta.tlapi.xql.Statement statement = getStatement(connection);

String query = null;
String tpNames = null;
String Values = null;
byte[] xmlResult;

query = " for $i in collection('tig:///ePharma/APN')/APN ";
query = query + " return <option value ='{data($i/From/Name)}'>{data($i/From/Name)}</option> ";
xmlResult = ReadTL(statement, query);

if(xmlResult != null) {
	tpNames = new String(xmlResult);
}

query = "for $i in collection('tig:///ePharma/APN')//APN ";
query = query + "where $i/Pedigrees/Pedigree/Custody/TransactionType='purchaseorder' ";
query = query + " return <option value = '{data($i/Pedigrees/Pedigree/Custody/TransactionNumber)}'>{data($i/Pedigrees/Pedigree/Custody/TransactionNumber)}</option> ";

System.out.println(query+"  is my query for po numbers");
xmlResult=ReadTL(statement,query);
if(xmlResult != null){
	Values=new String(xmlResult);
}



CloseConnectionTL(connection);
%>
<html>
	<head>
		<title>Raining Data ePharma - ePedigree</title><LINK href="../../assets/epedigree1.css" type="text/css" rel="stylesheet"></head>
	<body>
		
		<%@include file='topMenu.jsp'%>
		<% List list = (List)request.getAttribute("List"); %>
		
		<%System.out.println("The List object from ReceivingManagerAction in ReceivingManager Form is:"+list.toString()); %>
		
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
														<FORM action="/ePharma/dist/epedigree/ePedigree_POs_Search.do" method="post">
															<INPUT type="hidden" name="sessionID" value="<%=sessionID%>">
															<INPUT type="hidden" name="pagenm" value="<%=pagenm%>">
															<INPUT type="hidden" name="tp_company_nm" value="<%=tp_company_nm%>">
															<table border="1" cellspacing="0" cellpadding="0" align="center">
																<tr>
																<td colspan=10 align=center><FONT face="Arial" size="2"><STRONG>&nbsp;<FONT color="#009900">Quick Find</FONT></STRONG></FONT></td>
																</tr>
																<tr class="tableRow_Header">
																	<td class="type-whrite" align="left">From Date (yyyy-mm-dd)</td>
																	<td class="type-whrite" align="left">To Date (yyyy-mm-dd)</td>
																	<TD class="type-whrite" align="left">SSCC</TD>
																	<TD class="type-whrite" align="left">Lot #</TD>
																	<TD class="type-whrite" align="left">NDC #</TD>
																	<TD class="type-whrite" align="left">Order #</TD>
																	<TD class="type-whrite" align="left">ASN #</TD>
																</tr>
																<TR class="tableRow_On">
																	<td><INPUT type="text" size="20" name="fromDT"></td>
																	<td><INPUT type="text" size="19" name="toDT"></td>
																	<td><INPUT type="text" size="19" name="SSCC"></td>
																	<td><INPUT type="text" size="10" name="lotNum"></td>
																	<td><INPUT type="text" size="10" name="prodNDC"></td>
																	<td><INPUT type="text" size="10" name="orderNum"></td>
																	<td><INPUT type="text" size="10" name="poNum"></td>
																</TR>
																
																<tr class="tableRow_Header">
																	<TD class="type-whrite" align="left">Invoice #</TD>
																	<TD class="type-whrite" align="left">APN DOCID</TD>
																	<TD class="type-whrite" align="left">Trading Partner Name</TD>
																	<td></td>
																	<td></td>
																	<td></td>
																	<td></td>
																</tr>
																<TR class="tableRow_On">
																	<td><INPUT type="text" size="20" name="invoiceNum"></td>
																	<td><INPUT type="text" size="19" name="apndocId"></td>
																	<td><SELECT id="select" name="tpName"><option></option><%=tpNames%>/></SELECT></td>
																	<td></td>
																	<td></td>
																	<td></td>
																	<td></td>
																	
																</TR>
																<TR class="tableRow_Off">
							
																	<td colspan=10 align=center><INPUT type="submit" value="Find" name="Submit1"></td>
																</TR>
															</table><br><br>
															
															<table border="1" cellspacing="0" cellpadding="0" align="center">
																<tr>
																<td colspan=10 align=center><FONT face="Arial" size="2"><STRONG>&nbsp;<FONT color="#009900">Search by Options</FONT></STRONG></FONT></td>
																</tr>
																														
															<TR class="tableRow_On">
																<TD class="td-menu bold" align="center" width="200">Purchase Order Number :</TD>
																<TD class="td-menu" align="center"><select name="PONum">
																										   <option value="PO45678"><%=Values%></option></select></TD>
														</TR>
																<TR class="tableRow_Off">
							
																	<td colspan=10 align=center><INPUT type="submit" value="Search" name="Submit1"></td>
																</TR>
															</table>
																													
														</FORM>
														
<% if ( request.getParameter("PONum") != "Search"){

		List list1 =  (List)request.getAttribute("APNResultSet");
		List list2 =  (List)request.getAttribute("OrderResultSet");	
		List list3 =  (List)request.getAttribute("Date ReceivedResultSet");
		List list4 =  (List)request.getAttribute("ProductResultSet");
		List list5 =  (List)request.getAttribute("QuantityResultSet");
		List list6 =  (List)request.getAttribute("StatusResultSet");
								
		if(list1 != null ) {
		for(int i=0;i<list1.size();i++){
%>		
<TABLE id="Table9" cellSpacing="1" cellPadding="0" width="100%" align="center" border="0">
		<TBODY>
				<tr class="tableRow_Header">
															
					<td class="type-whrite" noWrap align="center" height="20">Pedigree&nbsp;#</td>
					<td class="type-whrite" noWrap align="center" height="20">Order #</td>
					<td class="type-whrite" noWrap align="center" height="20">Date Received</td>
					<td class="type-whrite" align="center" height="20">Product</td>
					<td class="type-whrite" noWrap align="center" height="20">Quantity</td>
					<td class="type-whrite" noWrap align="center" height="20">Status</td>
					<td class="type-whrite" noWrap align="center" height="20">Created By</td>
				
				</tr>
																								

			<tr class="tableRow_On" >
							
						<%							
							String	temp = (String)list1.get(i);
							System.out.println("temp: "+temp);

							out.println("<td align='center'height='20'><a href='ePedigree_Manager_Reconcile.jsp?pedid="+temp+"&pagenm="+pagenm+"&tp_company_nm='>"+temp+"</td></a>");						
							out.println("<td align='center'height='20'><a href='ePedigree_ViewOrder.jsp?trNum="+list2.get(i)+"&pagenm="+pagenm+"&tp_company_nm='>"+list2.get(i)+" </td>");
							out.println("<td align='center'height='20'>"+list3.get(i)+" </td>");
							out.println("<td align='center'height='20'>"+list4.get(i)+" </td>");
							out.println("<td align='center'height='20'>"+list5.get(i)+" </td>");
							out.println("<td></td>");
						    out.println("<td align='center'>System</td>");
						%>
			</tr>
						<%
                   			}		
						}
	}
%> 
</TABLE>
														
														
														
														</TD>
													</TR>
													<tr bgColor="white">
															
															<td class="td-typeblack" colSpan="1">Pedigrees Dated:<%=screenEnteredDate%> </td>
															
															
															
													</tr>
													<tr>
														<td align="left">
															<!-- Dashboard Start -->
															<table id="Table12" cellSpacing="1" cellPadding="0" width="100%" align="center" border="0">
																<TBODY>
																	
																	<tr class="tableRow_Header">
																		<td class="type-whrite" noWrap align="center">Select</td>
																		<td class="type-whrite" noWrap align="center">Pedigree&nbsp;#</td>
																		<td class="type-whrite" noWrap align="center">Order #</td>
																		<TD class="type-whrite" noWrap align="center">Date Received</TD>
																		<TD class="type-whrite" align="center">Trading Partner</TD>
																		<TD class="type-whrite" align="center">Product</TD>
																		<td class="type-whrite" noWrap align="center">Quantity</td>
																		<TD class="type-whrite" noWrap align="center">Status</TD>
																		<TD class="type-whrite" noWrap align="center">Created By</TD>
																	</tr>
																	
																	<% if (list.size() == 0) { 
																	System.out.println("*******Inside no Data**********");
																	%>
																	<TR class='tableRow_Off'><TD class='td-content' colspan='9' align='center'>No data...</TD></TR>
																	<% } else { 
																	%>
																	
																		
																	<logic:iterate name="<%=Constants.RCVNG_MNGR_DETAILS%>" id="rcvng">
																	<tr class="tableRow_Off">
																		<td>
																		 <input type ="radio"  name="check" value='<bean:write property = "pedigreeNum" name ="rcvng"/>'>  																	
																		</td>
																		<td class='td-content'><A href='ePedigree_Manager_Reconcile.jsp?pedid=<bean:write name="rcvng" property="pedigreeNum"/>&pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>'>
																			<bean:write name="rcvng" property="pedigreeNum"/>
																		</td>	
																		<td class='td-content'><A href='ePedigree_ViewOrder.jsp?trType=<bean:write name="rcvng" property="transactionType"/>&trNum=<bean:write name="rcvng" property="orderNum"/>' target='_new'>
																			<bean:write name="rcvng" property="orderNum"/>
																		</td>
																		<td>
																			<bean:write name="rcvng" property="dataRcvd"/>
																		</td>
																		<td>
																			<bean:write name="rcvng" property="trdPrtnr"/>
																		</td>
																		<td>
																			<bean:write name="rcvng" property="product"/>
																		</td>
																		<td>
																			<bean:write name="rcvng" property="quantity"/>
																		</td>
																		<td>
																			<bean:write name="rcvng" property="status"/>
																		</td>
																		<td>
																			<bean:write name="rcvng" property="createdBy"/>
																		</td>
																		
																	
																	</logic:iterate>	
																	</tr>
																	<% } %>
																	
																	
																	
																	
																	<TR class="tableRow_On">
																		<TD align="left" colspan="9">&nbsp;</TD>
																		
																	</TR>
																	<TR class="tableRow_Header">
																		<TD align="center" colspan=5>
																		<A onclick="MM_openBrWindow('ePedigreeSubmitScan.html','APN','scrollbars=yes,resizable=yes,width=800,height=600')"><INPUT id="Submit4" type="button" value="Send APN(s)" name="Submit4"></a>
																		</TD>
																		<TD align="center" colspan=4>
																		<A onclick="MM_openBrWindow('ePedigreeSubmitScan.html','APN','scrollbars=yes,resizable=yes,width=800,height=600')"><INPUT id="Submit4" type="button" value="Certify Pedigree(s)" name="Submit4"></a>
																		</TD>
																		
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
			</TD></TR></TBODY></TABLE></div>
	</body>
</html>

