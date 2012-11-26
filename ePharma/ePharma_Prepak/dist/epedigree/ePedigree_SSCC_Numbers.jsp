

<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.commons.xml.XMLUtil"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.catalog.session.TradingPartnerContext"%>
<%@ page import="com.rdta.catalog.Constants"%>

<%@ page import="com.rdta.catalog.trading.model.ProductMaster"%>





<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Raining Data ePharma - Trading Partner Manager</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="./assets/epedigree1.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}



function submitForm(operationType) {

	document.CollectSSCCNumbers.OperationType.value =operationType;

	document.CollectSSCCNumbers.submit();

	return true;

}


//-->
</script>
</head>
<body>
		
		<jsp:include page="../globalIncludes/TopHeader.jsp" />
<jsp:include page="../globalIncludes/TopLevelLayer1.jsp" />
<jsp:include page="../globalIncludes/TopLevelLayer2.jsp" />




		<div id="rightwhite">
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


					<%
							String previousValue = (String)request.getAttribute("listOfSSCCNumbers");

							if(previousValue == null) {
								previousValue = "";

							}

					%>

<form action="CollectSSCCNumbers.do" method="post" name="CollectSSCCNumbers">

	<input type="hidden" name="listOfSSCCNumbers" value="<%= previousValue%>">
		<input type="hidden" name="OperationType" value="">

					<td><table border="0" cellspacing="0" cellpadding="0" width="100%">
							<tr>
								<td>
									<P>
										<TABLE id="Table2" cellSpacing="0" cellPadding="0" width="100%" border="0">
											<TR>
												<TD><!-- info goes here -->
													<TABLE id="Table3" cellSpacing="1" cellPadding="1" width="100%" align="left" bgColor="white"
														border="0">
														
														<TR>
															<TD align="left"><!-- Dashboard Start -->
																<TABLE id="Table4" cellSpacing="1" cellPadding="0" width="100%" align="center" border="0">
																	
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
																		
																			<TR class="tableRow_Off">
																				<TD><STRONG> SSCC #:</STRONG></TD>
																				<TD><INPUT id="Text2" type="text" size="24" name="ssccNum">&nbsp;</TD>
																			
																			</TR>
																			
																																													
																			<tr class="tableRow_On">
																				<td colspan="2" align="center">
																					<INPUT name="Submit3" type="button" class="fButton" onclick="submitForm('ADD')" value="Add">
																		
																				</td>
																			</tr>
																		</TABLE>

																	</td>
															</tr>

																<tr> 
																	  <td align="left">
																					<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">				
																					<tr class="tableRow_Header">
																						<td class="type-whrite">List Of SSCC numbers</td>
																					</tr>

																			<%
																				if(previousValue != null && !previousValue.trim().equals("")) {
																					


																					String[] values   = previousValue.split(";");

																					System.out.println(" Values length " +values.length  );
																					for(int k=0;k<values.length; k++) {
																						String	value = values[k];
																						//System.out.println(" Values " + value);
 
																			%>
																						<tr  class="tableRow_On">
																							<td ><%= value%></td>
																						</tr>

																			<%		
																					}//end of for loop

																				}//end of if loop
																			%>


																		<tr class="tableRow_On">
																				<td colspan="2" align="center">
																						<INPUT name="Submit4" type="button" class="fButton" onclick="submitForm('DONE')" value="Done">
																				</td>
																			</tr>
																					</TABLE>

																	</td>
																</tr>

								
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





										</DIV>




<jsp:include page="../includes/Footer.jsp" />


</body>
</html>
