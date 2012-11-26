<%@ page import="com.rdta.commons.xml.XMLUtil" %>
<%@include file='../../includes/jspinclude.jsp'%>
<%@ page import="com.rdta.catalog.Constants" %>

<SCRIPT LANGUAGE="javascript">

function newWindow(  htmFile  ) {
   msgWindow = window.open( htmFile, "WindowName", "left=20,top=20,width= 700,height=500,toolbar=0,scrollbar=1,resizable=1,location=0,status=0");
}
</SCRIPT>

<%
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String APNID = request.getParameter("APNID");
String sessionID = (String)session.getAttribute("sessionID");
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");
String NDC = request.getParameter("prdid");
String pedigreeID = request.getParameter("PedigreeId");
String lotNum = request.getParameter("lotnum");
String pedOrd = request.getParameter("pedOrd");

String valPBC = request.getParameter("pbc");
String PedigreeOrder = pedOrd;
//System.out.println("*********************************");
Enumeration enum = request.getParameterNames();
		while ( enum.hasMoreElements()){
			String str= (String ) enum.nextElement();
			//System.out.println(str +"="+request.getParameter(str));
		}
				

String  image= request.getParameter("image");
System.out.println("image : "+image);
if (image == null) { image = "../../assets/images/tylenol2.gif"; }
if (image.equals("Pfizer")) { image = "../../assets/images/Pfizer.gif"; }


//System.out.println("apnid "+APNID);

String TOPLINE = "";
String EXCEPTIONVALUES = "";
String SYSROLESVALUES="";
String DocType="";
byte[] xmlResults;
String EPC="";
String ndc="";
String LegendDrugName="";
String DosageForm="";
String productExpiration="" ;	
String lotExpiration ="" ;	
String Manufacturer="" ;
String dosageForm="" ;
String dosageStrength="" ;
String packageSize="" ;
String description="" ;
String marketingStatus="" ;
String quantity="" ;
String threshold="" ;
String recall="" ;
String  contents="" ;
String dosage="" ;
String upc="";
String manufacturedDate="";

com.rdta.tlapi.xql.Connection connection = ConnectTL();
com.rdta.tlapi.xql.Statement statement = getStatement(connection);

String prodId = (String)request.getAttribute("ProdId");
String xQuery = "";
String manufName = "";
//GET Product Details Info
CloseConnectionTL(connection);
Node listNode = (Node)request.getAttribute("Result");

if (listNode != null)
{
	image =(String ) request.getAttribute("imagefile");
	System.out.println("image in jsp : "+image);
	
	EPC = XMLUtil.getValue(listNode,"root/Product/EPC");
	EPC = (EPC == null)?"N/A":EPC ;

	valPBC = XMLUtil.getValue(listNode,"root/Product/BarCode");
	valPBC =(valPBC == null)?"N/A":valPBC ;
	ndc = XMLUtil.getValue(listNode,"root/Product/NDC");
	ndc =(ndc == null)?"N/A":ndc ;
	lotNum = XMLUtil.getValue(listNode,"item/itemInfo/lot");
	lotNum =(lotNum == null)?"N/A":lotNum ;

	manufacturedDate =XMLUtil.getValue(listNode,"products/Product/ManufacturedDate");
	System.out.println("manufactured date: "+manufacturedDate);
	manufacturedDate =(manufacturedDate == null)?"N/A":manufacturedDate ;

	productExpiration =XMLUtil.getValue(listNode,"productExpiration");	
	productExpiration =(productExpiration == null)?"N/A":productExpiration ;

	lotExpiration 	=XMLUtil.getValue(listNode,"item/itemInfo/expirationDate");
	lotExpiration =(lotExpiration == null)?"N/A":lotExpiration ;

	Manufacturer=XMLUtil.getValue(listNode,"root/Product/ManufacturerName");
	Manufacturer =(Manufacturer == null)?"N/A":Manufacturer ;
	
	dosageForm=XMLUtil.getValue(listNode,"root/Product/DosageForm");
	dosageForm =(dosageForm == null)?"N/A":dosageForm ;
	
	
	dosageStrength=XMLUtil.getValue(listNode,"root/Product/DosageStrength");
	dosageStrength =(dosageStrength == null)?"N/A":dosageStrength ;
	
	packageSize=XMLUtil.getValue(listNode,"productInfo/containerSize");
	packageSize =(packageSize == null)?"N/A":packageSize ;
	
	description=XMLUtil.getValue(listNode,"products/Product/Description");
	description =(description == null)?"N/A":description ;
	

	marketingStatus=XMLUtil.getValue(listNode,"root/Product/MarketingStatus");
    marketingStatus =(marketingStatus == null)?"N/A":marketingStatus ;
	
	quantity=XMLUtil.getValue(listNode,"item/itemInfo/quantity");
	quantity =(quantity == null)?"N/A":quantity ;
	
	threshold=XMLUtil.getValue(listNode,"products/Product/Threshold");
    threshold =(threshold == null)?"N/A":threshold ;
	
	recall=XMLUtil.getValue(listNode,"Recall");
	recall =(recall == null)?"N/A":recall ;
	
	contents=XMLUtil.getValue(listNode,"Contents");
	contents =(contents == null)?"N/A":contents ;
	
	dosage=XMLUtil.getValue(listNode,"Dosage");
	dosage =(dosage == null)?"N/A":dosage ;
	upc= XMLUtil.getValue(listNode,"root/Product/PackageUPC");
	upc =(upc == null)?"N/A":upc ;
	LegendDrugName=XMLUtil.getValue(listNode,"root/Product/ProductName");
	LegendDrugName =(LegendDrugName == null)?"N/A":LegendDrugName ;
	
	}


%>
<html>
	<head>
		<title>Raining Data ePharma - ePedigree - Receiving Manager</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">
			<style type="text/css"></style>
			<script language="JavaScript" type="text/JavaScript">
<!--
function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}
//-->
			</script>
			<script language="JavaScript" type="text/JavaScript">
<!--


function MM_reloadPage(init) {  //reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
}
MM_reloadPage(true);

function MM_popupMsg(msg) { //v1.0
  alert(msg);
}
//-->
			</script>
	</head>
	<body>
		<%@include file='topMenu.jsp'%>

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
								<td class="td-typeblack"><FONT size="4">Product Details &amp; Authenticity</FONT></td>
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
													<%@include file= 'Header.jsp'%>
												</table>
											</td>
										</tr>
								
									</table>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<table border="0" cellpadding="0" cellspacing="1" width="100%" ID="Table6">
										<tr>
											<td class="td-typeblack" colspan="7">
												<TABLE id="Table10" cellSpacing="1" cellPadding="0" width="100%" border="0">
													<TBODY>
														<TR class="tableRow_Header">
															<TD class="type-whrite" align="center"><STRONG><FONT size="3" color="#ffffff"><%=LegendDrugName%> </FONT>
																</STRONG>
															</TD>
															<TD class="type-whrite" align="center">
																<FONT size="3" color="#ffffff"><STRONG>AUTHENTICITY</STRONG></FONT>
															</TD>
														</TR>
														<TR class="tableRow_On">
															<TD class="tableRow_On" align="center" height="142"><STRONG><FONT color="#3300cc">
																	<% // Have to get Image from EAGRFID %>
																		<P align="center">
							
																			<img src="<%=request.getContextPath()%>/servlet/showImageFromTL?dbname=EAGRFID&collname=ProductImage&topnode=Product&nodename=ProdIMG&ID=<%=prodId%>"/>
																		
																		</P>
																		<P align="center">
																			<TABLE id="Table12" cellSpacing="1" cellPadding="1" border="0" width="100%" align="center">
																			 <logic:iterate name="<%=Constants.PRODUCTDISPLAY_DETAILS%>" id="product">
																				<TR class="tableRow_On">
																					<TD width="66"><STRONG>NDC:</STRONG>
																					</TD>
																					<TD>
																						<P>
																						<INPUT class="tableRow_White" id="Text7" readOnly type="text" size="18" value="<%=ndc%>" name="ndc"></P>
																					</TD>
																				</TR>
																				<TR class="tableRow_On">
																					<TD width="66">EPC:</TD>
																					<TD><INPUT id="Text1" type="text" value="<%=EPC%>" name="EPC" readonly class="tableRow_White"
																							></TD>
																					<TD>&nbsp;</TD>
																				</TR>
																				<TR class="tableRow_On">
																					<TD width="66">UPC/SKU:</TD>
																					<TD><INPUT id="Text1" type="text" value="<%=upc%>" name="upcsku" readonly class="tableRow_White"
																							></TD>
																				</TR>
																				<TR class="tableRow_On">
																					<TD width="66">PBC:</TD>
																					<TD><INPUT id="Text1" type="text" value="<%=valPBC%>" name="pbc" readonly class="tableRow_White"
																							></TD></TD>
																				</TR>
																			</TABLE>
																		</P>
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
															<TD class="type-whrite" align="center"><FONT size="3" color="#ffffff"><STRONG>LOT 
																		INFORMATION</STRONG></FONT></STRONG></TD>
															<TD class="type-whrite" align="center"><FONT color="#ffffff" size="3"><STRONG>PRODUCT 
																		INFORMATION</STRONG></FONT>&nbsp;</TD>
														</TR>
														<TR class="tableRow_On">
															<TD class="type-whrite">
																<TABLE id="Table4" cellSpacing="1" cellPadding="0" align="center" border="0" width="100%">
																	<TR class="tableRow_Off">
																		<TD><STRONG><FONT size="1">Lot #:</FONT></STRONG></TD>
																		<TD><INPUT class="tableRow_White" id="Text2" readOnly type="text" size="11" value="<%=lotNum%>"
																				name="lotNumber">
																		</TD>
																	</TR>
																	<TR class="tableRow_On">
																		<TD><STRONG><FONT size="1">Manufactured Date:</FONT></STRONG></TD>
																		<TD><INPUT class="tableRow_Yellow" id="Text3" readOnly type="text" size="11" value="<%=manufacturedDate%>"
																				name="manufactDate">
																		</TD>
																	</TR>
																	<TR class="tableRow_Off">
																		<TD height="14"><STRONG><FONT size="1">Product Expiration:</FONT></STRONG></TD>
																		<TD height="14"><INPUT class="tableRow_Yellow" id="Text4" readOnly type="text" size="11" value="2009-06-01"
																				name="PrdExpireDate">
																		</TD>
																	</TR>
																	<TR class="tableRow_On">
																		<TD><STRONG><FONT size="1">Lot Expiration:</FONT></STRONG></TD>
																		<TD>
																			<STRONG><FONT color="#669933" size="1"><INPUT class="tableRow_Red" id="Text5" readOnly type="text" size="11" value="<%=lotExpiration%>"
																						name="LotExpireDate"></FONT></STRONG>
																		</TD>
																	</TR>
																	<TR class="tableRow_Off">
																		<TD><STRONG><FONT size="1">Manufacturer:</FONT></STRONG></TD>
																		<TD>
																			<STRONG><FONT color="#669933" size="1"></FONT></STRONG>
																			<INPUT class="tableRow_White" id="Text10" readOnly type="text" value="<%=Manufacturer%>" name="Manufacturer">
																	
																		</TD>
																	</TR>
																</TABLE>
															</TD>
															<TD class="type-whrite">
																<TABLE id="Table15" cellSpacing="1" cellPadding="0" align="center" border="0" width="100%">
																	<TR class="tableRow_On">
																		<TD height="15"><STRONG><FONT size="1">Dosage Form:</FONT></STRONG></TD>
																		<TD align="left" height="15">
																			<STRONG><FONT color="#669933" size="1"></FONT></STRONG><INPUT class="tableRow_White" id="Text6" readOnly type="text" value="<%=dosageForm%>" name="DosageForm">
																		</TD>
																	</TR>
																	<TR class="tableRow_Off">
																		<TD><STRONG><FONT size="1">Dosage Strength:</FONT></STRONG></TD>
																		<TD align="left"><INPUT class="tableRow_White" id="Text8" readOnly type="text" value="<%=dosageStrength%>" name="DosageStrength">
																		</TD>
																	</TR>
																	<TR class="tableRow_On">
																		<TD><STRONG><FONT size="1">Package Size:</FONT></STRONG></TD>
																		<TD align="left">
																			<STRONG><FONT color="#669933" size="1"><INPUT class="tableRow_White" id="Text9" readOnly type="text" value="<%=packageSize%>" name="PackageSize"></FONT></STRONG>
																		</TD>
																	</TR>
																	<TR class="tableRow_Off">
																		<TD><STRONG><FONT size="1">Description:</FONT></STRONG></TD>
																		<TD align="left"><INPUT class="tableRow_White" id="Text11" readOnly type="text" size="34" value=<%=description%>
																				name="Description">
																		</TD>
																	</TR>
																	<TR class="tableRow_On">
																		<TD><STRONG><FONT size="1">Marketing Status:</FONT></STRONG></TD>
																		<TD align="left"><STRONG><FONT color="#669933" size="1"><INPUT class="tableRow_Yellow" id="Text10" readOnly type="text" value="<%=marketingStatus%>" name="MarketingStat">
																				</FONT></STRONG>
																		</TD>
																	</TR>
																	<TR class="tableRow_On">
																		<TD><STRONG><FONT size="1">Quantity:</FONT></STRONG></TD>
																		<TD align="left"><INPUT class="tableRow_White" id="Text12" readOnly type="text" value="<%=quantity%>" name="ProdQTY"></TD>
																	</TR>
																	<TR class="tableRow_On">
																		<TD><STRONG><FONT size="1">Threshold:</FONT></STRONG></TD>
																		<TD align="left">
																			<STRONG><FONT color="#669933" size="1">NA</FONT></STRONG>
																		</TD>
																	</TR>
																	<TR class="tableRow_Off">
																		<TD colspan="2" align="center"><a href="ViewShipmentSensorData.html">View Sensor 
																				Recording Details</a></TD>
																	</TR>
																</TABLE>
															</TD>
														</TR>
														
													</TBODY></TABLE>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							
							<TR>
								<TD colSpan="2"><BR>
								</TD>
							</TR>
						</table>
						<DIV></DIV>
						<div id="footer">
							<table width="100%" height="24" border="0" cellpadding="0" cellspacing="0" ID="Table13">
								<tr>
									<td width="100%" height="24" bgcolor="#d0d0ff" class="td-menu" align="left">&nbsp;&nbsp;Copyright 
										2005. Raining Data.</td>
								</tr>
							</table>
						</div>
					</td>
				</tr>
			</table>
		
	</body>
</html>
