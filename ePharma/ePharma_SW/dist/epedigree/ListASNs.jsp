<%@include file='../../includes/jspinclude.jsp'%>

<%
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String HRMLROW = "";
byte[] xmlResults;

com.rdta.tlapi.xql.Connection connection = ConnectTL();
com.rdta.tlapi.xql.Statement statement = getStatement(connection);

String xQuery = "";	

xQuery = "for $b in collection('tig:///EAGRFID/ASN')/AdvanceShippingNotice ";
xQuery = xQuery + "order by $b/OrderDate ";
xQuery = xQuery + "return ";
xQuery = xQuery + "<TR class='tableRow_On'>	 ";		
xQuery = xQuery + "<TD class='td-content'><div align='center'> ";
xQuery = xQuery + "<A href='ViewASN.jsp?asn={data($b/@id)}' target='_blank' class='type-red'>{data($b/@id)}</A></div></TD> ";
xQuery = xQuery + "<TD class='td-content'>{data($b/OrderDate)}</TD> ";
xQuery = xQuery + "<TD class='td-content'>{data($b/DeliveryTime/DateTime)}</TD> ";
xQuery = xQuery + "<TD class='td-content'>{data($b/ToLocation/Name)}</TD> ";
xQuery = xQuery + "<TD class='td-content'>{count($b/Container/Product/EPCs/EPC)}</TD> ";
xQuery = xQuery + "<TD class='td-content'><STRONG><FONT color='#006600'>1000</FONT></STRONG></TD> ";
xQuery = xQuery + "<TD class='td-content'><STRONG><FONT color='#006600'>{data($b/OrderFulfillment)}</FONT></STRONG></TD> ";
xQuery = xQuery + "<TD class='td-content'> ";
xQuery = xQuery + "<P align='center'><INPUT id='Checkbox1' type='checkbox' name='Checkbox1'/></P> ";
xQuery = xQuery + "</TD> ";
xQuery = xQuery + "</TR> ";

xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
HRMLROW = new String(xmlResults);
}

CloseConnectionTL(connection);
%>


<html>
<head>
<title>Raining Data ePharma - ePedigree - APN Manager - Create APN</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">
<style type="text/css">
<!--
.style1 {color: #E0DFE3}
-->
</style>
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}
//-->
</script>
</head>

<body>

<!-- Top Header -->
<div id="bg">
	<div class="roleIcon-manufacturer">&nbsp;</div>
	<div class="navIcons">
		<a href="../../index.html"><img src="../../assets/images/home.gif" width="22" height="27" hspace="10" border="0"></a>
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
      <td valign="middle" background="../../assets/images/bg_menu.jpg"><table width="760" border="0" cellpadding="0" cellspacing="0">
          <tr> 
            <td align="center" class="primaryNav_Off"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../admin/AdminConsole.html" class="menu-link">Admin Console</a></td>
            <td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
            <td align="center" class="primaryNav_Off" 
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../logistics/Logistics.html" class="menu-link">Logistics</a></td>
            <td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
            <td align="center" class="primaryNav_On" 
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="ePedigree.html" class="menu-link menuBlack">ePedigree</a></td>
            <td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
            <td align="center" class="primaryNav_Off" 
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../prodRecall/ProductRecall.html" class="menu-link">Product Recall</a></td>
            <td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
            <td align="center" class="primaryNav_Off" 
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../trackTrace/TrackTrace.html" class="menu-link">Track and Trace</a></td>
            <td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
            <td align="center" class="primaryNav_Off" 
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../bizIntel/BusinessIntelligence.html" class="menu-link">Business Intelligence</a></td>
            <td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
            <td align="center" class="primaryNav_Off"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../tradingPartnerManager/TradingPartnerManager.html" class="menu-link">Trading Partner Manager</a></td>
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
      <td valign="bottom" background="../../assets/images/bg_menu.jpg"><table width="650" border="0" cellpadding="0" cellspacing="0">
          <tr> 
            <td align="center" class="primaryNav_Off"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../diversion/Diversion.html" class="menu-link">Diversion</a></td>
            <td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
            <td align="center" class="primaryNav_Off"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../clinicalTrials/ClinicalTrials.html" class="menu-link">Clinical Trials</a></td>
            <td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
            <td align="center" class="primaryNav_Off"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../catalog/Catalog.html" class="menu-link">Catalog</a></td>
            <td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
            <td align="center" class="primaryNav_Off"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../authentication/Authentication.html" class="menu-link">Authentication</a></td>
            <td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
            <td align="center" class="primaryNav_Off"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../productIntegrity/ProductIntegrity.html" class="menu-link">Product Integrity</a></td>
            <td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
            <td align="center" class="primaryNav_Off"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../reports/Reports.html" class="menu-link">Reports</a></td>
            <td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
            <td align="center" class="primaryNav_Off"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../kpiDashboard/KPIDashboard.html" class="menu-link">KPI Dashboard</a></td>
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
      <td width="170" colspan="2" class="td-leftred">ePedigree</td>
    </tr>
    <tr> 
      <td width="10" valign="top" bgcolor="#DCDCDC"></td>
      <td valign="top" class="td-left"><br>
	  	<a href="ePedigree.html" class="typeblue1-link">ePedigree</a><br>
        <!-- <a href="ePedigree.html" class="typeblue1-link-sub">Dashboard</a><br> -->
        <a href="ePedigree_Commissioning_Station_Item.html" class="typeblue1-link-sub">Commissioning Station<br></a>
		
        <a href="ePedigree_Commissioning_Station_Item.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Item</a><br>
		<a href="ePedigree_Commissioning_Station_Package.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Package</a><br>
		<a href="ePedigree_Commissioning_ProductionRun.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Production Run</a><br>
		<a href="ePedigree_Commissioning_AggregationStation.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Aggregation Station</a><br>
        <a href="ePedigree_Commissioning_View.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;View Commissioned Items</a><br>
		
        <a href="ePedigree_Manager_Search.html" class="typeblue1-link-sub">ePedigree and APN Manager</a><br>
        <a href="ePedigree_Manager_Search.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Search ePedigree</a><br>
        <a href="ListASNs.jsp" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Create APN</a><br>
        <a href="ePedigree_APNManager_Search.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Search APNs</a><br>
		
        <a href="ePedigree_Reports.html" class="typeblue1-link-sub">Reports</a><br>
        <a href="ePedigree_Reports.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;ePedigree</a><br> 
        <a href="ePedigree_Reports_APN.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;APN</a><br>
        <a href="ePedigree_Reports_Commissioning.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Commissioning</a><br>
		
        <a href="ePedigree_Workflow.html" class="typeblue1-link-sub">Workflow</a>
		</td>
    </tr>
    <tr valign="bottom"> 
      <td height="80" colspan="2" class="td-left"><img src="../../assets/images/logo_poweredby.gif" width="150" height="37"></td>
    </tr>
  </table>
</div>

<div id="rightwhite"> 
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr> 
      <td width="1%" valign="middle" class="td-rightmenu"><img src="../../assets/images/space.gif" width="10" height="10"></td>
      <!-- Messaging -->
      <td width="99%" valign="middle" class="td-rightmenu"><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td align="left"> <!-- <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr> 
                  <td class="type-red">[View] </td>
                  <td><a href="#" class="typered-link">Create</a> </td>
                  <td><a href="#" class="typered-link">Delete</a> </td>
                  <td><a href="#" class="typered-link">Duplicate </a></td>
                  <td><a href="#" class="typered-link">Search </a></td>
                  <td><a href="#" class="typered-link">Audit </a></td>
                  <td><a href="#" class="typered-link">Trail</a></td>
                </tr>
              </table> --></td>
            <td align="right"><!-- <img src="../../assets/images/3320.jpg" width="200" height="27"> --><img src="../../assets/images/space.gif" width="5"></td>
          </tr>
        </table></td>
    </tr>
    <tr> 
      <td>&nbsp;</td>
      <td><table border="0" cellspacing="0" cellpadding="0" width="100%">
          <tr> 
            <td><img src="../../assets/images/space.gif" width="30" height="12"></td>
            <td rowspan="2">&nbsp;</td>
          </tr>
          <!-- Breadcrumb -->
          <!-- <tr> 
            <td><a href="#" class="typegray1-link">Home</a><span class="td-typegray"> 
              - </span><a href="#" class="typegray1-link">ePedigree</a></td>
          </tr> -->
          <tr>
		  <td> 
           
		<!-- info goes here --> 
		<table width="100%" id="Table1" cellSpacing="1" cellPadding="1" align="left" border="0" bgcolor="white">
			<tr bgcolor="white">
				<td class="td-typeblack" colspan="3">APN Manager - Create APN</td>
			</tr>
			<tr>
				<td align="left">
					<!-- Dashboard Start -->
				<table align="center" width="100%" border="0" cellspacing="1" cellpadding="0">
		<TR bgcolor="white">
		              <TD colspan="4" class="type-red">Select ASN below and 
                        click Create APN and ePedigree button</TD>
			<TD align="right" colSpan="5" class="td-typegray">Displaying 1 - 25 of 141 <a href="#" class="typegray-link">Prev</a> | <a href="#" class="typegray-link">Next </a>|<a href="#" class="typegray-link"> View All</a></TD>
		</TR>
		<tr class="tableRow_Header">
			
			          <td align="center" class="type-whrite">ASN #</td>
			<td width="86" class="type-whrite">Order Date</td>
			<TD align="center" class="type-whrite">Date Filled</TD>
			<TD align="center" class="type-whrite">Customer</TD>
			<td class="type-whrite">Products In Order</td>
			<TD align="center" class="type-whrite">Products In Inventory</TD>
			          <TD align="center"class="type-whrite">Order Fulfillment</TD>
			          <TD align="center"class="type-whrite">Select ASN's</TD>
		</tr>
		<TR class="tableRow_On">
			
			<TD class="td-content"><div align="center"><A href="ePedigree_Manager_Create4.html" target="_blank" class="type-red">1478</A></div></TD>
			<TD width="86" class="td-content">1/1/2005</TD>
			<TD class="td-content">
				1/7/2005</TD>
			<TD class="td-content">Amerisource Bergen </TD>
			<TD class="td-content">50</TD>
			<TD class="td-content"><STRONG><FONT color="#006600">200</FONT></STRONG></TD>
			<TD class="td-content"><STRONG><FONT color="#006600">Complete</FONT></STRONG></TD>
			<TD class="td-content">
				<P align="center"><INPUT id="Checkbox1" type="checkbox" name="Checkbox1"></P>
			</TD>
		</TR>
		<TR class="tableRow_Off" >
			
			<TD class="td-content"><div align="center"><A href="ePedigree_Manager_Create4.html" target="_blank" class="type-red">2569</A></div></TD>
			<TD width="86" class="td-content">1/2/2005</TD>
			<TD class="td-content">1/14/2005</TD>
			<TD class="td-content">Cardinal Health </TD>
			<TD class="td-content">3000</TD>
			<TD class="td-content"><STRONG><FONT color="#006600">3000</FONT></STRONG></TD>
			<TD class="td-content"><STRONG><FONT color="#006600">Complete</FONT></STRONG></TD>
			<TD class="td-content">
				<P align="center"><INPUT id="Checkbox2" type="checkbox" name="Checkbox2"></P>
			</TD>
		</TR>
		<TR class="tableRow_On">
			
			<TD class="td-content"><div align="center"><A href="ePedigree_Manager_Create4.html" target="_blank" class="type-red">3491</A></div></TD>
			<TD width="86" class="td-content">1/3/2005</TD>
			<TD class="td-content">
				1/15/2005</TD>
			<TD class="td-content">H.D. Smith Wholesale Drug Co. </TD>
			<TD class="td-content">4000</TD>
			<TD class="td-content"><STRONG><FONT color="#ff0033">3500</FONT></STRONG></TD>
			<TD class="td-content"><STRONG><FONT color="#ff0066">Partial</FONT></STRONG></TD>
			<TD class="td-content">
				<P align="center"><INPUT id="Checkbox3" type="checkbox" name="Checkbox3"></P>
			</TD>
		</TR>
		<TR class="tableRow_Off">
			
			<TD class="td-content"><div align="center"><A href="ePedigree_Manager_Create4.html" target="_blank" class="type-red">4141</A></div></TD>
			<TD width="86" class="td-content">1/4/2005</TD>
			<TD class="td-content">
				1/20/2005</TD>
			<TD class="td-content">McKesson Corporation </TD>
			<TD class="td-content">3000</TD>
			<TD class="td-content"><STRONG>3000</STRONG></TD>
			          <TD class="td-content"><STRONG><FONT color="#006600">Complete</FONT></STRONG></TD>
			<TD class="td-content">
				<P align="center"><INPUT id="Checkbox4" type="checkbox" name="Checkbox4"></P>
			</TD>
		</TR>
		<TR class="tableRow_On">
			
			<TD class="td-content"><div align="center"><A href="ePedigree_Manager_Create4.html" target="_blank" class="type-red">5176</A></div></TD>
			<TD width="86" class="td-content">1/5/2005</TD>
			<TD class="td-content">
				1/21/2005</TD>
			<TD class="td-content">Cardinal Health </TD>
			<TD class="td-content">1500</TD>
			<TD class="td-content"><STRONG><FONT color="#ff0033">1250</FONT></STRONG></TD>
			          <TD class="td-content"><STRONG><FONT color="#006600">Complete</FONT></STRONG></TD>
			<TD class="td-content">
				<P align="center"><INPUT id="Checkbox5" type="checkbox" name="Checkbox5"></P>
			</TD>
		</TR>
		<%=HRMLROW%>
		<TR class="tableRow_Header">
				<TD colspan="8" align="center">
				
					<INPUT type="submit" class="fButton_large" onClick="MM_goToURL('parent','ePedigree_Manager_Create2.html');return document.MM_returnValue" value="Create APN and ePedigree">
				</TD>
		</TR>
	</table>
 				</td>
			</tr>

		</table>
		</td>
    </tr>
  </table>
</div>

<div id="footer" class="td-menu" > 
  <table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
    <tr> 
      <td width="100%" height="24" bgcolor="#D0D0FF" class="td-menu" align="left">&nbsp;&nbsp;Copyright 2005. 
        Raining Data.</td>
    </tr>
  </table>
</div>
</body>
</html>
