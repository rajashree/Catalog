<%@include file='../../includes/jspinclude.jsp'%>
<%
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String sessionID = request.getParameter("sessionID");
String pagenm = request.getParameter("pagenm");

String xmlResultString = "";
String redirURL = "LoginFailed.html";
String sessQuery = "";
String tp_company_nm = "";
String tp_company_id = "";
String ALERTLINES = "";
String xQuery  = "";
String userName = "";
System.out.println("session id: "+sessionID);


%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>Raining Data ePharma - KPI Dashboard</title>
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
		
		<%@include file='../epedigree/newtopMenu.jsp'%>
	
		<!-- Left channel -->
		<div id="leftgray">
			<table width="170" height="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="170" colspan="2" class="td-leftred">KPI Dashboard</td>
				</tr>
				<tr>
					<td width="10" valign="top" bgcolor="#dcdcdc"></td>
					<td valign="top" class="td-left"><br>
						<a href="index.jsp?sessionID=<%=sessionID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=kpidashboard" class="typeblue1-link">KPI Dashboard</a><br>
						<!-- <a href="ePedigree.html" class="typeblue1-link-sub">Dashboard</a><br> -->
						</A> <a href="KPIDashboard_KPIs.html" class="typeblue1-link-sub">KPI's</a><br>
						<a href="KPIDashboard_KPIs_Create.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Create</a><br>
						<a href="KPIDashboard_KPIs_ViewEdit.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;View/Edit</a><br>
						<a href="KPIDashboard_KPIs_Delete.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Delete</a><br>
						<a href="KPIDashboard_Configure.html" class="typeblue1-link-sub">Configure
							Dashboard </a>
						<br>
						<a href="ProductIntegrity_SensorViolations_Temperature.html" class="typeblue1-link-sub-sub">
							&nbsp;&nbsp;&nbsp;Choose Modules</a><br>
						<a href="ProductIntegrity_SensorViolations_Vibration.html" class="typeblue1-link-sub-sub">
							&nbsp;&nbsp;&nbsp;Choose Layout</a><br>
						<!--         <a href="ePedigree_Commissioning_Station_Item.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Item</a><br>
		<a href="ePedigree_Commissioning_Station_Package.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Package</a><br>
		<a href="ePedigree_Commissioning_ProductionRun.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Production Run</a><br>
		<a href="ePedigree_Commissioning_AggregationStation.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Aggregation Station</a><br>
        <a href="ePedigree_Commissioning_View.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;View Commissioned Items</a><br>

        <a href="ePedigree_Manager_Create.html" class="typeblue1-link-sub">ePedigree and APN Manager</a><br>
        <a href="ePedigree_Manager_Create.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Create APN</a><br>		<a href="ePedigree_Manager_ReconcileAPN.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Reconcile APN</a><br>        <a href="ePedigree_Manager_Search.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Search ePedigree</a><br>
        <a href="ePedigree_APNManager_Search.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Search APNs</a><br>

        <a href="ePedigree_Reports.html" class="typeblue1-link-sub">Reports</a><br>
        <a href="ePedigree_Reports.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;ePedigree</a><br>
        <a href="ePedigree_Reports_APN.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;APN</a><br>
        <a href="ePedigree_Reports_Commissioning.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Commissioning</a><br>

        <a href="ePedigree_Workflow.html" class="typeblue1-link-sub">Workflow</a>
		-->
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
						</table>
					</td>
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
									<table width="100%" id="Table1" cellSpacing="1" cellPadding="1" align="left" border="0"
										bgcolor="white">
										<tr bgcolor="white">
											<td class="td-typeblack" colspan="3">KPI Dashboard</td>
										</tr>
										<tr>
											<td align="left">
												<!-- Dashboard Start -->
												<table border="0" cellpadding="0" cellspacing="0" width="770">
													<tr>
														<!-- Begin charts -->
														<td width="365" valign="top">
															<img src="../../assets/images/EEImages/blank.gif" width="1" height="7" border="0" alt=""><BR>
															<table border="0" cellspacing="0" cellpadding="0" width="365">
																<tr>
																	<td><img src="../../assets/images/EEImages/blank.gif" width="16" height="1" border="0" alt=""></td>
																	<td valign="top">
																		<table border="0" cellspacing="0" cellpadding="0" width="348">
																			<tr>
																				<td width="348" colspan="3"><img src="../../assets/images/EEImages/dboard/chart_weeklysales_top.gif" width="348"
																						height="18" border="0" align="top" alt="Weekly Sales"></td>
																			</tr>
																			<tr>
																				<td valign="top" width="3"><img src="../../assets/images/EEImages/dboard/chart_left.gif" width="3" height="285"
																						border="0" align="top" alt=""></td>
																				<td valign="top" width="348">
																					<table width="338" cellpadding="0" cellspacing="0" border="0">
																						<tr>
																							<td colspan="5">
																								<img src="../../assets/images/EEImages/dboard/chart_weeklySales_base.gif" width="338"
																									height="234" border="0" align="top" alt=""></td>
																						</tr>
																						<tr>
																							<!-- Thumbnails -->
																							<td align="left" width="67"><a href="#"><img src="../../assets/images/EEImages/dboard/chart_bttm1_on.gif" width="67" height="51"
																										border="0" align="top" alt="Weekly Sales"></a></td>
																							<td width="68" class="thumbon"><a href="#"><img src="../../assets/images/EEImages/dboard/chart_bttm2_off.gif" width="68" height="51"
																										border="0" align="top" alt="Service Level Comparison"></a></td>
																							<td width="68" class="thumboff"><a href="#"><img src="../../assets/images/EEImages/dboard/chart_bttm3_off.gif" width="68" height="51"
																										border="0" align="top" alt="Inventory On Hand Snapshot"></a></td>
																							<td width="68" class="thumboff"><a href="#"><img src="../../assets/images/EEImages/dboard/chart_bttm4_off.gif" width="68" height="51"
																										border="0" align="top" alt="Purchasing On Time Shipments"></a></td>
																							<td align="left" width="67" class="thumboff"><a href="Dashboard_chart_inventory.html"><img src="../../assets/images/EEImages/dboard/chart_bttm5_off.gif" width="67" height="51"
																										border="0" align="top" alt="On Time Shipments"></a></td>
																						</tr>
																						<!-- Report name links -->
																						<tr>
																							<td align="left" class="chartNames" width="70" valign="top"><a href="#" class="typeblue1-link-sub-sub">APN
																									Verification Weekly Statistics</a></td>
																							<td align="left" class="chartNames" width="68"><a href="#" class="typeblue1-link-sub-sub">Trading
																									Partner Pedigree Exceptions</a></td>
																							<td align="left" class="chartNames" width="68"><a href="#" class="typeblue1-link-sub-sub">Exceptions
																									Per Reason Code</a></td>
																							<td align="left" class="chartNames" width="68"><a href="#" class="typeblue1-link-sub-sub">Orders
																									Per Trading Partner</a></td>
																							<td align="left" class="chartNames" width="67"><a href="#" class="typeblue1-link-sub-sub">Pedigree
																									Certification By User</a></td>
																						</tr>
																					</table>
																				</td>
																				<td valign="top" width="7"><img src="../../assets/images/EEImages/dboard/chart_right.gif" width="7" height="285"
																						border="0" align="top" alt=""></td>
																			</tr>
																		</table>
																	</td>
																</tr>
															</table>
														</td>
														<!-- End charts -->
														<!-- Begin KPIs -->
														<td width="265" valign="top">
															<img src="../../assets/images/EEImages/blank.gif" width="1" height="7" border="0" alt=""><BR>
															<table border="0" cellspacing="0" cellpadding="0" width="265">
																<tr>
																	<td width="10" valign="top"><img align="top" src="../../assets/images/EEImages/blank.gif" width="10" height="1" border="0"
																			alt=""></td>
																	<td valign="top" width="250">
																		<!----Start of table kpis---->
																		<table width="300" cellpadding="0" cellspacing="0" border="0">
																			<tr>
																				<td valign="top" height="18" width="255" bgcolor="#a29fcb" colspan="3"><img src="../../assets/images/EEImages/dboard/db_kpis_top.gif" width="250" height="18"
																						border="0" align="top" alt="Key Performance Indicators"></td>
																			</tr>
																			<tr>
																				<td align="left" width="1" bgcolor="#a29fcb"><img align="top" src="../../assets/images/EEImages/blank.gif" width="1" height="1" border="0"
																						alt=""></td>
																				<td valign="top" width="253">
																					<table width="300" cellpadding="0" cellspacing="0" border="0">
																						<tr>
																							<td colspan="6" valign="top" height="16" bgcolor="#dfdeed" width="255"><img src="../../assets/images/EEImages/dboard/db_kpis_orders.gif" width="248" height="16"
																									border="0" align="top" alt=""></td>
																						</tr>
																						<tr>
																							<td colspan="6" bgcolor="#dfdeed" valign="top" height="1" width="248"><img src="../../assets/images/EEImages/blank.gif" width="1" height="1" border="0" align="top"
																									alt=""></td>
																						</tr>
																						<tr>
																							<td class="menu" valign="top" width="186">
																								&nbsp; Exceptions per Order
																							</td>
																							<td bgcolor="#dfdeed" height="18" width="1"><img src="../../assets/images/EEImages/blank.gif" width="1" height="1" border="0" align="top"
																									alt=""></td>
																							<td valign="middle" class="kpis" width="16"><a href="javascript:openWindow('KPIs/KPI15.html','','yes','yes',255,450,100,100)">.04</a></td>
																							<td width="16"><img align="top" src="../../assets/images/EEImages/dboard/db_dot_green.gif" width="16"
																									height="18" border="0" alt=""></td>
																							<td width="16"><img align="top" src="../../assets/images/EEImages/dboard/db_dot_blank.gif" width="16"
																									height="18" border="0" alt=""></td>
																							<td width="16"><img src="../../assets/images/EEImages/dboard/db_dot_blank.gif" align="top" width="16"
																									height="18" border="0" alt=""></td>
																						</tr>
																						<tr>
																							<td colspan="6" bgcolor="#dfdeed" valign="top" height="1" width="248"><img src="../../assets/images/EEImages/blank.gif" width="1" height="1" border="0" align="top"
																									alt=""></td>
																						</tr>
																						<tr>
																							<td class="menu" valign="top" width="186">
																								&nbsp; Order Cancellation Rate
																							</td>
																							<td bgcolor="#dfdeed" height="16" width="1"><img src="../../assets/images/EEImages/blank.gif" width="1" height="1" border="0" align="top"
																									alt=""></td>
																							<td valign="middle" class="kpis" width="14"><a href="javascript:openWindow('KPIs/KPI16.html','','yes','yes',255,450,100,100)">.1%</a></td>
																							<td width="16"><img align="top" src="../../assets/images/EEImages/dboard/db_dot_green.gif" width="16"
																									height="18" border="0" alt=""></td>
																							<td width="16"><img align="top" src="../../assets/images/EEImages/dboard/db_dot_blank.gif" width="16"
																									height="18" border="0" alt=""></td>
																							<td width="16"><img src="../../assets/images/EEImages/dboard/db_dot_blank.gif" align="top" width="16"
																									height="18" border="0" alt=""></td>
																						</tr>
																						<tr>
																							<td colspan="6" bgcolor="#dfdeed" valign="top" height="1" width="253"><img src="../../assets/images/EEImages/blank.gif" width="1" height="1" border="0" align="top"
																									alt=""></td>
																						</tr>
																						<tr>
																							<td class="menu" valign="top" width="186">
																								&nbsp; Perfect Order Rate
																							</td>
																							<td bgcolor="#dfdeed" height="16" width="1"><img src="../../assets/images/EEImages/blank.gif" width="1" height="1" border="0" align="top"
																									alt=""></td>
																							<td valign="middle" class="kpis" width="14"><a href="javascript:openWindow('KPIs/KPI17.html','','yes','yes',255,450,100,100)">83%</a></td>
																							<td width="16"><img align="top" src="../../assets/images/EEImages/dboard/db_dot_blank.gif" width="16"
																									height="18" border="0" alt=""></td>
																							<td width="16"><img align="top" src="../../assets/images/EEImages/dboard/db_dot_yellow.gif" width="16"
																									height="18" border="0" alt=""></td>
																							<td width="16"><img src="../../assets/images/EEImages/dboard/db_dot_blank.gif" align="top" width="16"
																									height="18" border="0" alt=""></td>
																						</tr>
																						<tr>
																							<td colspan="6" bgcolor="#a29fcb" valign="top" height="1" width="253"><img src="../../assets/images/EEImages/blank.gif" width="1" height="1" border="0" align="top"
																									alt=""></td>
																						</tr>
																						<tr>
																							<td colspan="6" valign="top" height="16" bgcolor="#dfdeed" width="255"><img src="../../assets/images/EEImages/dboard/db_kpis_inventory.gif" width="248" height="16"
																									border="0" align="top" alt=""></td>
																						</tr>
																						<tr>
																							<td colspan="6" bgcolor="#dfdeed" valign="top" height="1" width="248"><img src="../../assets/images/EEImages/blank.gif" width="1" height="1" border="0" align="top"
																									alt=""></td>
																						</tr>
																						<tr>
																							<td class="menu" valign="top" width="186">
																								&nbsp; % RFID Tag Failure
																							</td>
																							<td bgcolor="#dfdeed" height="16" width="1"><img src="../../assets/images/EEImages/blank.gif" width="1" height="1" border="0" align="top"
																									alt=""></td>
																							<td valign="middle" class="kpis" width="14"><a href="javascript:openWindow('KPIs/KPI18.html','','yes','yes',255,450,100,100)">1.1%</a></td>
																							<td width="16"><img align="top" src="../../assets/images/EEImages/dboard/db_dot_green.gif" width="16"
																									height="18" border="0" alt=""></td>
																							<td width="16"><img align="top" src="../../assets/images/EEImages/dboard/db_dot_blank.gif" width="16"
																									height="18" border="0" alt=""></td>
																							<td width="16"><img src="../../assets/images/EEImages/dboard/db_dot_blank.gif" align="top" width="16"
																									height="18" border="0" alt=""></td>
																						</tr>
																						<tr>
																							<td colspan="6" bgcolor="#dfdeed" valign="top" height="1" width="248"><img src="../../assets/images/EEImages/blank.gif" width="1" height="1" border="0" align="top"
																									alt=""></td>
																						</tr>
																						<tr>
																							<td class="menu" valign="top" width="186">
																								&nbsp; % of Pedigree Verification Failures
																							</td>
																							<td bgcolor="#dfdeed" height="16" width="1"><img src="../../assets/images/EEImages/blank.gif" width="1" height="1" border="0" align="top"
																									alt=""></td>
																							<td valign="middle" class="kpis" width="14"><a href="javascript:openWindow('KPIs/KPI19.html','','yes','yes',255,450,100,100)">5%</a></td>
																							<td width="16"><img align="top" src="../../assets/images/EEImages/dboard/db_dot_blank.gif" width="16"
																									height="18" border="0" alt=""></td>
																							<td width="16"><img align="top" src="../../assets/images/EEImages/dboard/db_dot_blank.gif" width="16"
																									height="18" border="0" alt=""></td>
																							<td width="16"><img src="../../assets/images/EEImages/dboard/db_dot_red.gif" align="top" width="16"
																									height="18" border="0" alt=""></td>
																						</tr>
																						<tr>
																							<td colspan="6" bgcolor="#dfdeed" valign="top" height="1" width="248"><img src="../../assets/images/EEImages/blank.gif" width="1" height="1" border="0" align="top"
																									alt=""></td>
																						</tr>
																						<!-- <tr>
<td class="menu" valign="top" width="186">
&nbsp; Shrinkage Rate
</td>
<td bgcolor="#DFDEED" height="16" width="1"><img src="../../assets/images/EEImages/blank.gif" width="1" height="1" border="0" align="top" alt=""></td>
<td valign="middle" class="kpis" width=14><a href="javascript:openWindow('KPIs/KPI20.html','','yes','yes',255,450,100,100)">1.1%</a></td><td width="16"><img align="top" src="../../assets/images/EEImages/dboard/db_dot_blank.gif" width="16" height="18" border="0" alt=""></td><td width=16><img align="top" src="../../assets/images/EEImages/dboard/db_dot_yellow.gif" width="16" height="18" border="0" alt=""></td><td width=16><img src="../../assets/images/EEImages/dboard/db_dot_blank.gif" align="top" width="16" height="18" border="0" alt=""></td>
</tr>
<tr>
<td colspan="6" bgcolor="#DFDEED" valign="top" height="1" width="248"><img src="../../assets/images/EEImages/blank.gif" width="1" height="1" border="0" align="top" alt=""></td>
</tr> -->
																						<!-- <tr>
<td class="menu" valign="top" width="186">
&nbsp; Inv. Synch. Accuracy
</td>
<td bgcolor="#DFDEED" height="16" width="1"><img src="../../assets/images/EEImages/blank.gif" width="1" height="1" border="0" align="top" alt=""></td>
<td valign="middle" class="kpis" width=14><a href="javascript:openWindow('KPIs/KPI21.html','','yes','yes',255,450,100,100)">99%</a></td><td width="16"><img align="top" src="../../assets/images/EEImages/dboard/db_dot_green.gif" width="16" height="18" border="0" alt=""></td><td width=16><img align="top" src="../../assets/images/EEImages/dboard/db_dot_blank.gif" width="16" height="18" border="0" alt=""></td><td width=16><img src="../../assets/images/EEImages/dboard/db_dot_blank.gif" align="top" width="16" height="18" border="0" alt=""></td>
</tr> -->
																						<tr>
																							<td colspan="6" bgcolor="#a29fcb" valign="top" height="1" width="253"><img src="../../assets/images/EEImages/blank.gif" width="1" height="1" border="0" align="top"
																									alt=""></td>
																						</tr>
																						<tr>
																							<td colspan="6" valign="top" height="16" bgcolor="#dfdeed" width="255"><img src="../../assets/images/EEImages/dboard/db_kpis_pomanagement.gif" width="248" height="16"
																									border="0" align="top" alt=""></td>
																						</tr>
																						<tr>
																							<td colspan="6" bgcolor="#dfdeed" valign="top" height="1" width="248"><img src="../../assets/images/EEImages/blank.gif" width="1" height="1" border="0" align="top"
																									alt=""></td>
																						</tr>
																						<tr>
																							<td class="menu" valign="top" width="186">
																								&nbsp;Commission Failure Rate/Day
																							</td>
																							<td bgcolor="#dfdeed" height="16" width="1"><img src="../../assets/images/EEImages/blank.gif" width="1" height="1" border="0" align="top"
																									alt=""></td>
																							<td valign="middle" class="kpis" width="14"><a href="javascript:openWindow('KPIs/KPI22.html','','yes','yes',255,450,100,100)">1%</a></td>
																							<td width="16"><img align="top" src="../../assets/images/EEImages/dboard/db_dot_blank.gif" width="16"
																									height="18" border="0" alt=""></td>
																							<td width="16"><img align="top" src="../../assets/images/EEImages/dboard/db_dot_yellow.gif" width="16"
																									height="18" border="0" alt=""></td>
																							<td width="16"><img src="../../assets/images/EEImages/dboard/db_dot_blank.gif" align="top" width="16"
																									height="18" border="0" alt=""></td>
																						</tr>
																						<tr>
																							<td colspan="6" bgcolor="#dfdeed" valign="top" height="1" width="248"><img src="../../assets/images/EEImages/blank.gif" width="1" height="1" border="0" align="top"
																									alt=""></td>
																						</tr>
																						<tr>
																							<td colspan="6" valign="top" height="16" bgcolor="#dfdeed" width="255"><img src="../../assets/images/EEImages/dboard/db_kpis_fulfillment.gif" width="248" height="16"
																									border="0" align="top" alt=""></td>
																						</tr>
																						<tr>
																							<td colspan="6" bgcolor="#dfdeed" valign="top" height="1" width="248"><img src="../../assets/images/EEImages/blank.gif" width="1" height="1" border="0" align="top"
																									alt=""></td>
																						</tr>
																						<tr>
																							<td class="menu" valign="top" width="186">
																								&nbsp; Order Fill Rate
																							</td>
																							<td bgcolor="#dfdeed" height="16" width="1"><img src="../../assets/images/EEImages/blank.gif" width="1" height="1" border="0" align="top"
																									alt=""></td>
																							<td valign="middle" class="kpis" width="14"><a href="javascript:openWindow('KPIs/KPI23.html','','yes','yes',255,450,100,100)">99.2%</a></td>
																							<td width="16"><img align="top" src="../../assets/images/EEImages/dboard/db_dot_green.gif" width="16"
																									height="18" border="0" alt=""></td>
																							<td width="16"><img align="top" src="../../assets/images/EEImages/dboard/db_dot_blank.gif" width="16"
																									height="18" border="0" alt=""></td>
																							<td width="16"><img src="../../assets/images/EEImages/dboard/db_dot_blank.gif" align="top" width="16"
																									height="18" border="0" alt=""></td>
																						</tr>
																						<tr>
																							<td colspan="6" bgcolor="#dfdeed" valign="top" height="1" width="248"><img src="../../assets/images/EEImages/blank.gif" width="1" height="1" border="0" align="top"
																									alt=""></td>
																						</tr>
																						<tr>
																							<td class="menu" valign="top" width="186">
																								&nbsp; On Time Shipment
																							</td>
																							<td bgcolor="#dfdeed" height="16" width="1"><img src="../../assets/images/EEImages/blank.gif" width="1" height="1" border="0" align="top"
																									alt=""></td>
																							<td valign="middle" class="kpis" width="14"><a href="javascript:openWindow('KPIs/KPI24.html','','yes','yes',255,450,100,100)">90%</a></td>
																							<td width="16"><img align="top" src="../../assets/images/EEImages/dboard/db_dot_blank.gif" width="16"
																									height="18" border="0" alt=""></td>
																							<td width="16"><img align="top" src="../../assets/images/EEImages/dboard/db_dot_blank.gif" width="16"
																									height="18" border="0" alt=""></td>
																							<td width="16"><img src="../../assets/images/EEImages/dboard/db_dot_red.gif" align="top" width="16"
																									height="18" border="0" alt=""></td>
																						</tr>
																						<tr>
																							<td colspan="6" bgcolor="#a29fcb" valign="top" height="1" width="253"><img src="../../assets/images/EEImages/blank.gif" width="1" height="1" border="0" align="top"
																									alt=""></td>
																						</tr>
																						<tr>
																							<td colspan="6" valign="top" height="16" bgcolor="#dfdeed" width="255"><img src="../../assets/images/EEImages/dboard/db_kpis_delivery.gif" width="248" height="16"
																									border="0" align="top" alt=""></td>
																						</tr>
																						<tr>
																							<td colspan="6" bgcolor="#dfdeed" valign="top" height="1" width="248"><img src="../../assets/images/EEImages/blank.gif" width="1" height="1" border="0" align="top"
																									alt=""></td>
																						</tr>
																						<tr>
																							<td class="menu" valign="top" width="186">
																								&nbsp; On Time Delivery
																							</td>
																							<td bgcolor="#dfdeed" height="16" width="1"><img src="../../assets/images/EEImages/blank.gif" width="1" height="1" border="0" align="top"
																									alt=""></td>
																							<td valign="middle" class="kpis" width="14"><a href="javascript:openWindow('KPIs/KPI25.html','','yes','yes',255,450,100,100)">99.2%</a></td>
																							<td width="16"><img align="top" src="../../assets/images/EEImages/dboard/db_dot_green.gif" width="16"
																									height="18" border="0" alt=""></td>
																							<td width="16"><img align="top" src="../../assets/images/EEImages/dboard/db_dot_blank.gif" width="16"
																									height="18" border="0" alt=""></td>
																							<td width="16"><img src="../../assets/images/EEImages/dboard/db_dot_blank.gif" align="top" width="16"
																									height="18" border="0" alt=""></td>
																						</tr>
																						<tr>
																							<td colspan="6" bgcolor="#a29fcb" valign="top" height="1" width="253"><img src="../../assets/images/EEImages/blank.gif" width="1" height="1" border="0" align="top"
																									alt=""></td>
																						</tr>
																					</table>
																				</td>
																				<td width="1" bgcolor="#a29fcb">
																					<img align="top" src="../../assets/images/EEImages/blank.gif" width="1" height="1" border="0"
																						alt="">
																				</td>
																			</tr>
																			<tr>
																				<!-----------alerts------------------>
																				<td valign="top" colspan="3" width="300">
																					<table cellpadding="0" cellspacing="0" border="0" width="300">
																						<tr>
																							<td valign="top" height="35" width="300" colspan="3">
																								<img src="../../assets/images/EEImages/blank.gif" height="10" width="1"><br>
																								<img src="../../assets/images/EEImages/dboard/alerts_top.gif" width="300" height="25"
																									border="0" align="top" alt="Key Performance Indicators"></td>
																						</tr>
																						<tr>
																							<td height="24" width="33"><img src="../../assets/images/EEImages/dboard/alerts_icon_on.gif" width="33" height="24"
																									border="0" align="top" alt=""></td>
																							<td height="24" class="menu" width="221">Pedigree Verification Failed
																							</td>
																							<td align="right" height="24" width="1"><img src="../../assets/images/EEImages/dboard/alerts_bg_black.gif" width="1" height="24"
																									border="0" align="top" alt=""></td>
																						</tr>
																						<tr>
																							<td height="24" width="33"><img src="../../assets/images/EEImages/dboard/alerts_icon_off.gif" width="33" height="24"
																									border="0" align="top" alt=""></td>
																							<td height="24" width="221" class="menu">Unauthorized Drug Orders
																							</td>
																							<td align="right" height="24" width="1"><img align="top" src="../../assets/images/EEImages/dboard/alerts_bg_black.gif" height="24"
																									width="1"></td>
																						</tr>
																						<tr>
																							<td height="1" width="300" valign="top" colspan="3"><img align="top" src="../../assets/images/EEImages/dboard/alerts_bg_black.gif" height="1"
																									width="300"></td>
																						</tr>
																					</table>
																				</td>
																			</tr>
																		</table>
																	</td>
																</tr>
															</table>
														</td>
														<!-- End KPIs -->
													</tr>
												</table>
												<!-- End Dashboard-->
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<DIV></DIV>
						<div id="footer" class="td-menu">
							<table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td width="100%" height="24" bgcolor="#d0d0ff" class="td-menu" align="left">&nbsp;&nbsp;Copyright
										2005. Raining Data.</td>
								</tr>
							</table>
						</div>
				</tr>
			</table>
		</div>
	</body>
</html>
