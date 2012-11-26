<%@include file='../../includes/jspinclude.jsp'%>

<%
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String PedDocID = request.getParameter("PedDocID");
String HTMLROW = "";
byte[] xmlResults;

com.rdta.tlapi.xql.Connection connection = ConnectTL();
com.rdta.tlapi.xql.Statement statement = getStatement(connection);

String xQuery = "";	

xQuery = "for $b in collection('tig:///EAGRFID/APN')/APN/Pedigrees ";
xQuery = xQuery + "where contains($b/Pedigree/DocumentId,'"+PedDocID+"') order by $b/Pedigree/DateTime ";
xQuery = xQuery + "return ";
xQuery = xQuery + "<tr bgcolor='C8C8C8'>	 ";		
xQuery = xQuery + "<TD class='td-menu'>";
xQuery = xQuery + "<A href='ViewPedigree.jsp?doc={data($b/Pedigree/DocumentId)}' target='_blank' class='type-red'>{data($b/Pedigree/DocumentId)}</A></TD> ";
xQuery = xQuery + "<TD class='td-menu'>{data($b/Pedigree/Manufacturer/Name)}</TD> ";
xQuery = xQuery + "<TD class='td-menu'>{count($b/Pedigree/Products/Product)}</TD> ";
xQuery = xQuery + "<TD class='td-menu'>{data($b/Pedigree/DateTime)}</TD> ";
xQuery = xQuery + "<TD class='td-menu'>{data($b/Pedigree/Custody/AuthenticatorName)}</TD> ";
xQuery = xQuery + "<TD class='td-menu'>Valid</TD> ";
xQuery = xQuery + "</tr> ";
          
xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
HTMLROW = new String(xmlResults);
}

CloseConnectionTL(connection);
%>



<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Raining Data ePharma - ePedigree Manager - Search Results</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">
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
				<td class="td-typeblack" colspan="1">Click on the ID to view ePedigree details.</td>
			</tr>
			<tr>
				<td align="left">
					<!-- Dashboard Start -->
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
<!-- 			<tr>
				<td align="right" class="td-typegray">Displaying 1 - 25 of 141 <a href="#" class="typegray-link">Prev</a> | <a href="#" class="typegray-link">Next </a>|<a href="#" class="typegray-link"> View All</a></td>
			</tr> -->
             <tr> 
                 <td align="left"><table width="100%" border="0" cellpadding="0" cellspacing="1">
          <tr bgcolor="8494CA">
            <td width="51" class="type-whrite">ID</td>
            <td width="139" class="type-whrite">From</td>
            <td width="110" class="type-whrite">Product Count</td>
            <td width="164" class="type-whrite">Created on</td>
            <td width="140" class="type-whrite">Authorized Person</td>
	    <td width="70" class="type-whrite">Status</td>
          </tr>
          <%=HTMLROW%>
 -->        </table>
				</td>
				</tr>
                
                    </TABLE></td>
                </tr>
              </table>
            </td>
        </tr>
      </table></div>
	  
<div id="footer"> 
  <table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
    <tr> 
      <td width="100%" height="24" bgcolor="#D0D0FF" class="td-menu" align="left">&nbsp;&nbsp;Copyright 2005. 
        Raining Data.</td>
    </tr>
  </table>
</div>
</body>
</html>
