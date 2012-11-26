<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Raining Data ePharma - Reports</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}
//-->
function goAdvancedReports()
{
	AdvancedReportsForm.submit();
}
</script>
</head>
<body>
<jsp:include page="../globalIncludes/TopHeader.jsp" />
<jsp:include page="../globalIncludes/TopLevelLayer1.jsp" />
<jsp:include page="../globalIncludes/TopLevelLayer2.jsp" />
<jsp:include page="./LeftNav.jsp" />

<FORM name= "AdvancedReportsForm" ACTION="ShowAdvancedReports.do"  method="post">
<!-- Top Header -->
<div id="bg">
	<div class="roleIcon-manufacturer">&nbsp;</div>
	<div class="navIcons">
		<a href="../../landing.html"><img src="../../assets/images/home.gif" width="22" height="27" hspace="10" border="0"></a>
		<img src="../../assets/images/account.gif" width="41" height="27" hspace="10">
		<img src="../../assets/images/help.gif" width="21" height="27" hspace="10">
		<img src="../../assets/images/print.gif" width="27" height="27" hspace="10">
		<img src="../../assets/images/logout.gif" width="34" height="27" hspace="10">
		<img src="../../assets/images/space.gif" width="20">
	</div>

  <div class="logo"><img src="../../assets/images/logos_combined.jpg"></div>
</div>

<!-- Top level nav layer 1 -->
<div id="menu">
  <table width="100%" height="25" border="0" cellpadding="0" cellspacing="0">
    <tr>
      <td valign="middle" background="../../assets/images/bg_menu.jpg"><table width="800" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td align="center" class="primaryNav_Off"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../admin/index.html" class="menu-link">Admin Console</a></td>
            <td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
            <td align="center" class="primaryNav_Off"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../logistics/index.html" class="menu-link">Logistics</a></td>
            <td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
            <td align="center" class="primaryNav_Off"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../epedigree/index.html" class="menu-link">ePedigree</a></td>
            <td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
            <td align="center" class="primaryNav_Off"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../prodRecall/index.html" class="menu-link">Product Recall</a></td>
            <td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
            <td align="center" class="primaryNav_Off"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../trackTrace/index.html" class="menu-link">Track and Trace</a></td>
            <td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
            <td align="center" class="primaryNav_Off"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../bizIntel/index.html" class="menu-link">Business Intelligence</a></td>
            <td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
            <td align="center" class="primaryNav_Off"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../tradingPartnerManager/index.html" class="menu-link">Trading Partner Manager</a></td>
            <td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
          </tr>
        </table></td>
    </tr>
  </table>
</div>

<!-- Top level nav layer 2 -->
<div id="menu1">
  <table width="100%" height="25" border="0" cellpadding="0" cellspacing="0">
    <tr>
      <td valign="bottom" background="../../assets/images/bg_menu.jpg"><table width="700" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td align="center" class="primaryNav_Off"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../diversion/index.html" class="menu-link">Diversion</a></td>
            <td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
            <td align="center" class="primaryNav_Off"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../clinicalTrials/index.html" class="menu-link">Clinical Trials</a></td>
            <td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
            <td align="center" class="primaryNav_Off"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../catalog/index.html" class="menu-link">Catalog</a></td>
            <td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
            <td align="center" class="primaryNav_Off"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../authentication/index.html" class="menu-link">Authentication</a></td>
            <td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
            <td align="center" class="primaryNav_Off"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../productIntegrity/index.html" class="menu-link">Product Integrity</a></td>
            <td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
            <td align="center" class="primaryNav_On"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../reports/Reports.html" class="menu-link menuBlack">Reports</a></td>
            <td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
            <td align="center" class="primaryNav_Off"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../kpiDashboard/index.html" class="menu-link">KPI Dashboard</a></td>
            <td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
          </tr>
        </table></td>
    </tr>
  </table>
</div>

<!-- Left channel -->
<div id="leftgray">
  <table width="170" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
      <td width="170" colspan="2" class="td-leftred">Reports</td>
    </tr>
    <tr>
      <td width="10" valign="top" bgcolor="#DCDCDC"></td>
      <td valign="top" class="td-left"><p><br>
          <a href="index.html" class="typeblue1-link">Reports</a><br>
		  <a href="javascript:goAdvancedReports();" class="typeblue1-link-sub">Advanced Reports</a><br>
          <a href="Reports_Logistics.html" class="typeblue1-link-sub">Logistics
          Reports</a><br>
          <a href="Reports_Logistics_ShipmentProduct.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Shipment
          by Product</a><br>
          <a href="Reports_Logistics_ShipmentDate.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Shipment
          by Date Span</a><br>
          <a href="Reports_Logistics_ReceivingProduct.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Receiving
          by Product</a><br>
          <a href="Reports_Logistics_ReceivingDate.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Receiving
          by Date Span</a><br>
          <a href="Reports_Logistics_ReturnsProduct.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Returns
          by Product</a><br>
          <a href="Reports_Logistics_ReturnsDate.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Returns
          by Date Span</a><br>
          <a href="Reports_ePedigree.html" class="typeblue1-link-sub">ePedigree
          Reports</a><br>
          <a href="Reports_ePedigree_Product.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;ePedigree
          per Product</a><br>
          <a href="Reports_ePedigree_DateSpan.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;ePedigree
          by Date Span</a><br>
          <a href="Reports_ePedigree_APNTradingPartner.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;APN
          by Trading Partner</a><br>
          <a href="Reports_ePedigree_APNDateSpan.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;APN
          by Date Span</a><br>
          <a href="Reports_ePedigree_Commissioning.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Commissioning
          by Product</a><br>
          <a href="Reports_ePedigree_FailedCommissioning.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Failed
          Commissioning by Product</a><br>
          <a href="Reports_ProductRecall.html" class="typeblue1-link-sub">Product
          Recall Reports</a><br>
          <a href="Reports_ProductRecall_Product.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Recalls
          per Product</a><br>
          <a href="Reports_ProductRecall_DateSpan.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Recalls
          by Date Span</a><br>
          <a href="Reports_ProductRecall_ThreatLevel.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Recalls
          by Threat Level</a>
          <a href="Reports_Trading.html" class="typeblue1-link-sub">Trading Partner Manager Reports</a><br>
          <a href="Reports_Trading_SLAFulfillment.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;SLA fulfillment per Trading Product</a><br>
          <a href="Reports_Trading_ShipmentTime.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Shipment Time per 3PL carrier</a><br>

          <a href="Reports_Diversion.html" class="typeblue1-link-sub">Diversion Reports</a><br>
          <a href="Reports_Diversion_ASN3PL.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;ASN/RM discrepancy per 3PL</a><br>
          <a href="Reports_Diversion_ASNLocation.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;ASN/RM discrepancy per Location</a><br>
          <a href="Reports_Diversion_TheftLocation.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Theft Alerts per Location</a><br>
          <a href="Reports_Diversion_TheftProduct.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Theft Alerts per Product</a><br>

          <a href="Reports_ClinicalTrials.html" class="typeblue1-link-sub">Clinical Trials Reports</a><br>
          <a href="Reports_ClinicalTrials_PatientName.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Alerts by Patient Name</a><br>
          <a href="Reports_ClinicalTrials_DateSpan.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Alerts by Date Span</a><br>

          <a href="Reports_Authentication.html" class="typeblue1-link-sub">Authentication Reports</a><br>
          <a href="Reports_Authentication_Product.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Authentication Failures per Product</a><br>
          <a href="Reports_Authentication_ReasonCode.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Authentication Failures per Reason Code</a><br>

          <a href="Reports_ProductIntegrity.html" class="typeblue1-link-sub">Product Integrity Reports<br>
          </a> <a href="Reports_ProductIntegrity_TradingPartner.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Sensor
          Violations per Trading Partner</a><br>
          <a href="Reports_ProductIntegrity_3PL.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Sensor
          Violations per 3PL</a><br>
          <a href="Reports_ProductIntegrity_Product.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Sensor
          Violations per Product</a><br>
        </p></td>
    </tr>
    <tr valign="bottom">
      <td height="80" colspan="2" class="td-left"><img src="../../assets/images/logo_poweredby.gif" width="150" height="37"></td>
    </tr>
  </table>
</div>

<div id="rightwhite"> <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="1%" valign="middle" class="td-rightmenu"><img src="../../assets/images/space.gif" width="10" height="10"></td>
    <!-- Messaging -->
    <td width="99%" valign="middle" class="td-rightmenu"><table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td align="left">
            <!-- <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td class="type-red">[View] </td>
                  <td><a href="#" class="typered-link">Create</a> </td>
                  <td><a href="#" class="typered-link">Delete</a> </td>
                  <td><a href="#" class="typered-link">Duplicate </a></td>
                  <td><a href="#" class="typered-link">Search </a></td>
                  <td><a href="#" class="typered-link">Audit </a></td>
                  <td><a href="#" class="typered-link">Trail</a></td>
                </tr>
              </table> -->
          </td>
          <td align="right">
            <!-- <img src="../../assets/images/3320.jpg" width="200" height="27"> -->
            <img src="../../assets/images/space.gif" width="5"></td>
        </tr>
      </table></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>
      <!--  href="#" class="typegray1-link">ePedigree</a></td>
          </tr>
		          <tr>
          <td>

            <form action="ePedigree_Commissioning_ProductRun3.html" method="post" ID="Form1">
              <table width="100%" id="Table1" cellSpacing="1" cellPadding="1" align="left" border="0"
			bgcolor="white" class="td-menu">
                <tr bgcolor="white">
                  <td class="td-typeblack">You can enter multiple values separated by comma.</td>
                </tr>
                <tr>
                  <td align="left">
				  <TABLE id="Table3" cellSpacing="1" cellPadding="1" border="0" width="100%" class="td-menu">

             <tr>
			 	<td bgcolor="white">

					<TABLE id="Table3" cellSpacing="1" cellPadding="1" border="0" class="td-menu" align="center" width="100%">
						<TR class="tableRow_Header">
							<TD class="type-whrite" align="center">
								<STRONG>SEARCH ON</STRONG>
							</TD>
							<TD class="type-whrite" align="center">

								<STRONG>VALUE</STRONG>
							</TD>
							<TD class="type-whrite" align="center">
								<STRONG>CRITERIA</STRONG>
							</TD>
						</TR>
						<TR class="tableRow_On">
							<TD><STRONG>Date Received (yyyy-mm-dd):</STRONG></TD>

							<TD>From:<INPUT id="Text4" type="text" name="fromDtReceived" size="10">&nbsp;To: <INPUT id="Text1" type="text" size="10" name="toDtReceived"></TD>
							<TD><SELECT id="Select4" name="dateReceivedAndOr">
									<OPTION value="AND" selected>AND</OPTION>
									<OPTION value="OR">OR</OPTION>
								</SELECT></TD>
						</TR>
						<TR class="tableRow_Off">

							<TD><STRONG>Pedigree Reference #:</STRONG></TD>
							<TD><INPUT id="Text2" type="text" size="24" name="fromDtPublished">&nbsp;</TD>
							<TD><SELECT id="Select5" name="datePublishedAndOr">
									<OPTION value="AND" selected>AND</OPTION>
									<OPTION value="OR">OR</OPTION>
								</SELECT></TD>
						</TR>

						<TR class="tableRow_On">
							<TD><STRONG>From Name/Company:</STRONG></TD>
							<TD><INPUT id="Text3" type="text" size="24" name="fromDtPublished"></TD>
							<TD><SELECT id="Select6" name="msStageAndOr">
									<OPTION value="AND" selected>AND</OPTION>
									<OPTION value="OR">OR</OPTION>
								</SELECT></TD>

						</TR>
						<TR class="tableRow_Off">
							<TD><STRONG><STRONG>To&nbsp;Name/Company:</STRONG></STRONG></TD>
							<TD><INPUT id="Text5" type="text" size="24" name="fromDtPublished"></TD>
							<TD><SELECT id="Select7" name="msTypeAndOr">
									<OPTION value="AND" selected>AND</OPTION>
									<OPTION value="OR">OR</OPTION>

								</SELECT></TD>
						</TR>
						<TR class="tableRow_On">
							  <TD><STRONG> Manufacturing Facility:</STRONG></TD>
							<TD><INPUT id="Text9" type="text" size="24" name="fromDtPublished"></TD>
							<TD><SELECT id="Select2" name="msBilledAndOr">
									<OPTION value="AND" selected>AND</OPTION>

									<OPTION value="OR">OR</OPTION>
								</SELECT></TD>
						</TR>
						<TR class="tableRow_Off">
							<TD><STRONG>License Number:</STRONG></TD>
							<TD><INPUT id="Text6" type="text" size="30" name="keywords"></TD>
							<TD><SELECT id="Select8" name="keywordsAndOr">
									<OPTION value="AND" selected>AND</OPTION>

									<OPTION value="OR">OR</OPTION>
								</SELECT></TD>
						</TR>
						<TR class="tableRow_On">
							<TD><STRONG>Date In Custody:</STRONG></TD>
							<TD>From:<INPUT id="Text7" type="text" size="10" name="fromDtReceived">&nbsp;To: <INPUT id="Text10" type="text" size="10" name="toDtReceived"></TD>
							<TD><SELECT id="Select9" name="authLastNameAndOr">

									<OPTION value="AND" selected>AND</OPTION>
									<OPTION value="OR">OR</OPTION>
								</SELECT></TD>
						</TR>
						<TR class="tableRow_Off">
							<TD><STRONG> Product EPC:</STRONG>
							</TD>

							<TD><INPUT id="Text8" type="text" name="coAuthorLastName" size="30"></TD>
							<TD><SELECT id="Select10" name="coAuthorLastNameAndOr">
									<OPTION value="AND" selected>AND</OPTION>
									<OPTION value="OR">OR</OPTION>
								</SELECT></TD>
						</TR>
						<TR class="tableRow_On">
							<TD><STRONG>Parent EPC:</STRONG></TD>

							<TD><INPUT id="Text11" type="text" size="30" name="coAuthorLastName"></TD>
							<TD><SELECT id="Select12" name="acodeAndOr">
									<OPTION value="AND" selected>AND</OPTION>
									<OPTION value="OR">OR</OPTION>
								</SELECT></TD>
						</TR>
						<TR class="tableRow_Off">
							<TD><STRONG>Bar Code:</STRONG></TD>

							<TD><INPUT id="Text18" type="text" size="30" name="coAuthorLastName"></TD>

							<TD><SELECT id="Select13" name="acodeAndOr">
									<OPTION value="AND" selected>AND</OPTION>
									<OPTION value="OR">OR</OPTION>
								</SELECT></TD>
						</TR>
						<TR class="tableRow_On">
							<TD><STRONG>Brand Name:</STRONG></TD>

							<TD><INPUT id="Text12" type="text" size="30" name="coAuthorLastName"></TD>
							<TD><SELECT id="Select15" name="departmentAndOr">
									<OPTION value="AND" selected>AND</OPTION>
									<OPTION value="OR">OR</OPTION>
								</SELECT></TD>
						</TR>
						<TR class="tableRow_Off">
							<TD><STRONG>Lot Number:</STRONG></TD>

							<TD><INPUT id="Text17" type="text" size="30" name="coAuthorLastName"></TD>
							<TD><SELECT id="Select1" name="departmentAndOr">
									<OPTION value="AND" selected>AND</OPTION>
									<OPTION value="OR">OR</OPTION>
								</SELECT></TD>
						</TR>
						<TR class="tableRow_On">
							<TD><STRONG>Lot Expiration Date:</STRONG></TD>

							<TD>From:<INPUT id="Text13" type="text" size="10" name="fromDtReceived">&nbsp;To: <INPUT id="Text14" type="text" size="10" name="toDtReceived"></TD>
							<TD><SELECT id="Select3" name="departmentAndOr">
									<OPTION value="AND" selected>AND</OPTION>
									<OPTION value="OR">OR</OPTION>
								</SELECT></TD>
						</TR>
						<TR class="tableRow_Off">

							<TD><STRONG>Product Expiration Date:</STRONG></TD>
							<TD>From:<INPUT id="Text15" type="text" size="10" name="fromDtReceived">&nbsp;To: <INPUT id="Text16" type="text" size="10" name="toDtReceived"></TD>
							<TD><SELECT id="Select11" name="departmentAndOr">
									<OPTION value="AND" selected>AND</OPTION>
									<OPTION value="OR">OR</OPTION>
								</SELECT></TD>

						</TR>
						<TR class="tableRow_On">
							<TD colspan="3"><STRONG>Show All: <INPUT id="Checkbox2" type="checkbox" value="showAll" name="showAll"></STRONG>
								| <STRONG>Show Verified:</STRONG><input type="checkbox" name="showAll" value="showAll" ID="Checkbox1">&nbsp;|
								<STRONG>Show Pending:</STRONG> <INPUT id="Checkbox3" type="checkbox" value="showAll" name="showAll">&nbsp;|
								<STRONG>Show Violations:</STRONG> <INPUT id="Checkbox4" type="checkbox" value="showAll" name="showAll">

							</TD>
						</TR>
						<tr class="tableRow_Header">
						<td colspan="3" align="center">
						<INPUT name="Submit3" type="submit" class="fButton" id="Submit2" onClick="MM_goToURL('parent','ePedigree_Manager_AdvancedSearchResults.html');return document.MM_returnValue" value="Search">
						</td>
						</tr>
					</TABLE>
				</td>
			</tr>

                    </TABLE></td>
                </tr>
              </table>
            </form></td>
        </tr>
      </table> -->
  </div>
<div id="footer" class="td-menu" >
  <table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
    <tr>
      <td width="100%" height="24" bgcolor="#D0D0FF" class="td-menu" align="left">&nbsp;&nbsp;Copyright
        2005. Raining Data.</td>
    </tr>
  </table>
</div>
</form>
</body>
</html>
