

<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.commons.xml.XMLUtil"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.catalog.session.TradingPartnerContext"%>
<%@ page import="com.rdta.catalog.Constants"%>

<%@ page import="com.rdta.catalog.trading.model.ProductMaster"%>


<%

String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String sessionID = request.getParameter("sessionID");
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");

%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Raining Data ePharma - Trading Partner Manager</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}

function submitCreateAPN( docType1)
{

	
	document.searchScreen.docType.value = docType1;
	


	return true;
}




//-->
</script>
</head>
<body>
		

<%@include file='topMenu.jsp'%>


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

<form action="/ePharma/dist/epedigree/ShippingManagerSearch.do" method="post" name="searchScreen" ID="Form1">


<INPUT id="hidden1" type="hidden" name="sessionID" value="<%=sessionID%>">
<INPUT id="hidden12" type="hidden" name="pagenm" value="<%=pagenm%>">
<INPUT id="hidden14" type="hidden" name="tp_company_nm" value="<%=tp_company_nm%>">


					<td><table border="0" cellspacing="0" cellpadding="0" width="100%">
							<tr>
								<td>
									<P>
										<TABLE id="Table2" cellSpacing="0" cellPadding="0" width="100%" border="0">
											<TR>
												<TD><!-- info goes here -->
													<TABLE id="Table3" cellSpacing="1" cellPadding="1" width="100%" align="left" bgColor="white"
														border="0">
														<TR bgColor="white">
															<TD class="td-typeblack" colSpan="3">
																<P><FONT size="3">Create Pedigree From - Search </FONT>
																</P>
															</TD>
														</TR>
														<TR>
															<TD align="left"><!-- Dashboard Start -->
																<TABLE id="Table4" cellSpacing="1" cellPadding="0" width="100%" align="center" border="0">
																	<TR class="tableRow_Header">

																		<TD colspan= "2" class="type-whrite" align="center" height="20">
																	<STRONG><FONT size="2"> SEARCH:</FONT></STRONG> 
																		<INPUT id="Radio2" type="radio" value="Order" name="searchSelect"  >&nbsp;<STRONG><FONT color="#ffff00">Order</FONT></STRONG>
																		<INPUT id="Radio3" type="radio" value="DespatchAdvice" name="searchSelect">&nbsp;<STRONG><FONT color="#ffff00">Despatch Advice</FONT></STRONG></TD>
																	</TR>
																	<TR>
																		<TD class="td-content" align="center" colspan="2">

																			



																				<table width="100%" id="Table5" cellSpacing="1" cellPadding="1" align="left" border="0"
																					bgcolor="white" class="td-menu">
																					<tr>
																						<td align="left">
																							<TABLE id="Table6" cellSpacing="1" cellPadding="1" border="0" width="100%" class="td-menu">
																								<tr>
																									<td bgcolor="white">
																										<!-- table goes here -->
																										<TABLE id="Table7" cellSpacing="1" cellPadding="1" border="0" class="td-menu" align="center"
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
																												<TD>From:<INPUT id="Text4" type="text" name="fromDtReceived" size="10">&nbsp;To: <INPUT id="Text1" type="text" size="10" name="toDtReceived"></TD>
																												
																											</TR>
																											<TR class="tableRow_Off">
																												<TD><STRONG> Reference #:</STRONG></TD>
																												<TD><INPUT id="Text2" type="text" size="24" name="refNumber">&nbsp;</TD>
																											
																											</TR>
																											<TR class="tableRow_On">
																												<TD><STRONG>From Name/Company:</STRONG></TD>
																												<TD><INPUT id="Text3" type="text" size="24" name="fromCompany"></TD>
																												
																											</TR>
																											<TR class="tableRow_On">
																												<TD><STRONG>NDC:</STRONG></TD>
																												<TD><INPUT id="Text9" type="text" size="24" name="ndc"></TD>
																												
																											</TR>

																																																					
																											<tr class="tableRow_Header">
																												<td colspan="2" align="center">
																													<INPUT name="Submit3" type="submit" class="fButton" value="Search">
																												</td>
																											</tr>
																										</TABLE>

																										


																									</td>
																							</tr>

<tr> 
                  <td align="left">
					<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">				
					<tr class="tableRow_Header">
						<td class="type-whrite">Reference Number</td>
						<td class="type-whrite">From Company Name</td>
						<td class="type-whrite">NDC</td>
						<td class="type-whrite">Data Received</td>
						<td> </td>
					</tr>


				<%
					List list =  (List)request.getAttribute("SearchResult");

					if(list != null ) {

						String refNumber = "";
						String dtReceived = "";
						String NDC = "";
						String fromCompany = "";
			
						String searchSelect = request.getParameter("searchSelect");
					
						String name= "selectedRow";

						for(int i=0; i < list.size(); i++) {


							name = "selectedRow" + i;
							if(searchSelect != null && searchSelect.trim().equalsIgnoreCase("Order")) {
						
								Node OrderNode = XMLUtil.parse((InputStream) list.get(i) );

								refNumber = CommonUtil.jspDisplayValue(OrderNode,"BuyersID");

								dtReceived = CommonUtil.jspDisplayValue(OrderNode,"Delivery/RequestedDeliveryDateTime");

								NDC = CommonUtil.jspDisplayValue(OrderNode,"OrderLine/LineItem/Item/SellersItemIdentification/ID");
								fromCompany = CommonUtil.jspDisplayValue(OrderNode,"SellerParty/Party/PartyName/Name");

							}	else if( searchSelect != null &&  searchSelect.trim().equalsIgnoreCase("DespatchAdvice")) {


								Node DespatchNode = XMLUtil.parse((InputStream) list.get(i) );

								refNumber = CommonUtil.jspDisplayValue(DespatchNode,"ID");

								dtReceived = CommonUtil.jspDisplayValue(DespatchNode,"Delivery/ActualDeliveryDateTime");

								NDC = CommonUtil.jspDisplayValue(DespatchNode,"DespatchLine/Item/SellersItemIdentification/ID");
								fromCompany = CommonUtil.jspDisplayValue(DespatchNode,"SellerParty/Party/PartyName/Name");

							}

			   %>


					<tr class="tableRow_On">

						<td><%= refNumber %></td>
						<td><%= fromCompany %></td>
						<td><%= NDC %></td>
						<td><%= dtReceived %></td>
						
<td>
						<INPUT id="Radio2" type="radio" value="<%= refNumber %>" name="selectedRow" >&nbsp;</STRONG> </td>

					</tr>

                <%

						}//end of for loop
						
					}//end of if loop
				
                 %>
			
				</TABLE>
            </td>



																								</tr>

																							<%
																									if(list!= null && list.size() > 0 ) {	
																							%>


	<tr class="tableRow_Header">
																												<td colspan="2" align="center">
																													<INPUT name="Submit3" type="submit" class="fButton" onclick="submitCreateAPN('<%= request.getParameter("searchSelect")%>')" value="Create APN">
																												</td>
																											</tr>


																	<%
																								}
																	 %>
																
																							</TABLE>
																						</td>
																					</tr>
																				</table>
																			</form>
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
											<!-- Breadcrumb -->
											<!-- <tr> 
            <td><a href="#" class="typegray1-link">Home</a><span class="td-typegray"> 
              - </span><a href="#" class="typegray1-link">ePedigree</a></td>
          </tr> -->
										</TABLE>





						




<jsp:include page="../includes/Footer.jsp" />


</body>
</html>
