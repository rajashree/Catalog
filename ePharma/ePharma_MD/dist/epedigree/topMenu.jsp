
<%	
	//GET PATH TO SERVER SO CAN DYNAMICALLY CREATE HREFS
	String servPath = request.getContextPath();
	servPath = "http://"+request.getServerName()+":"+request.getServerPort()+servPath;     
	System.out.println(" tp_company_nm in toMenu.jsp: "+ tp_company_nm);
	String tpList = request.getParameter("tpList");
	if(tpList == null) tpList = "";
%>
<%!

		String genId="";
%>

 <% 
   			String linkName =(String) request.getParameter("linkName"); 
   			if(linkName == null )linkName=(String) session.getAttribute("linkName");
   			else session.setAttribute("linkName",linkName);
   			if(linkName == null ) linkName="";
   			System.out.println("LInk Name : "+linkName);
   			
 %>

<!-- Top Header -->
<div id="bg" style="Z-INDEX: 100">
	<div class="roleIcon-southwood"></div>
	<div class="navIcons">
		<a href="<%=servPath%>/logout.jsp" target="_top"><img src="<%=servPath%>/assets/images/logout.gif" width="34" height="27" hspace="10" border="0"></a>
<!--	<a href="<%=servPath%>/dist/epedigree/epedigree.do?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>&linkName=eped" target="_top"><IMG height="27" hspace="10" src="<%=servPath%>/assets/images/inbox.gif" border="0"></a>-->
		<img src="<%=servPath%>/assets/images/space.gif" width="20">
	</div>
	
	<div class="logo"><img src="<%=servPath%>/assets/images/logos_combined.jpg"></div>
</div>

<% String SessionID = (String)session.getAttribute("sessionID");
System.out.println("inside topmenu.jsp");
%>
	
	<div id="menu" style="Z-INDEX: 101">
		<table width="100%" height="25" border="0" cellpadding="0" cellspacing="0" ID="Table1">
			<tr>
				<td valign="middle" background="<%=servPath%>/assets/images/bg_menu.jpg"><table width="400" border="0" cellpadding="0" cellspacing="0" ID="Table2">
						<tr>
							<% if(pagenm.equals("epcadmin")) { %>
								<td width="200" align="center" class="primaryNav_On" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="<%=servPath%>/AdminUser.do?pagenm=epcadmin&tp_company_nm=<%=tp_company_nm%>"" class="menu-link menuBlack" target="_parent">Admin Console</a></td>
								
							<% } else { %>
								<td  width="200" align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
									onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="<%=servPath%>/AdminUser.do?pagenm=epcadmin&tp_company_nm=<%=tp_company_nm%>"" class="menu-link menuBlack" target="_parent">Admin Console</a></td>
							<% } %>										
							<td><img src="<%=servPath%>/assets/images/menu_bg1.jpg" width="3" height="23"></td>
							<% if(pagenm.equals("pedigree")) { %>
								<td  width="200" align="center" class="primaryNav_On" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="<%=servPath%>/dist/epedigree/PedigreeTradingPartner.do?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>&accesslevel=PTP&linkName=PTP" class="menu-link menuBlack" target="_parent">ePedigree</a></td>
								
							<% } else { %>
								<td width="200" align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="<%=servPath%>/dist/epedigree/PedigreeTradingPartner.do?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>&accesslevel=PTP&linkName=PTP"  class="menu-link menuBlack" target="_parent">ePedigree</a></td>
							<% } %>						
							<!--<td><img src="<%=servPath%>/assets/images/menu_bg1.jpg" width="3" height="23"></td>
							
							<% if(pagenm.equals("pedBank")) { %>
								<td align="center" class="primaryNav_On" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="<%=servPath%>/dist/pedigreeBankResults.do?tp_company_nm=<%=tp_company_nm%>&pagenm=pedBank" class="menu-link" target="_parent">Pedigree Bank 
									</a></td>
							<% } else { %>
								<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="<%=servPath%>/dist/pedigreeBankResults.do?tp_company_nm=<%=tp_company_nm%>&pagenm=pedBank" class="menu-link" target="_parent">Pedigree Bank 
								</a></td> <% } %>
							-->
							<td><img src="<%=servPath%>/assets/images/menu_bg1.jpg" width="3" height="23"></td>
							
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
	
	<% if(pagenm.equals("logist")) { %>
	
	<!-- Left channel -->
	<div id="leftgray2" style="Z-INDEX: 104"> 
	  <table width="170" height="100%" border="0" cellpadding="0" cellspacing="0">
	    <tr> 
	      <td width="170" colspan="2" class="td-leftred">Reports</td>
	    </tr>
	    <tr> 
	      <td width="10" valign="top" bgcolor="#DCDCDC"></td>
	      <td valign="top" class="td-left"><p><br>
		  <a href="index.html" class="typeblue1-link">Reports</a><br>
		  <!-- <a href="ePedigree.html" class="typeblue1-link-sub">Dashboard</a><br> --></a> 
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
	      <td height="80" colspan="2" class="td-left"><img src="<%=servPath%>/assets/images/logo_poweredby.gif" width="150" height="37"></td>
	    </tr>
	  </table>
	</div>

	<div id="rightwhite"> 

	<% } else {    
	
	if(pagenm.equals("recall")) {     %>  
		<!-- Left channel -->
			<div id="leftgray2" style="Z-INDEX: 104">
				<table width="170" border="0" cellpadding="0" cellspacing="0" ID="Table5">
					<tr>
						<td width="170" colspan="2" class="td-leftred">ePedigree Manager</td>
					</tr>
					
				</table>
			</div>
			<div id="rightwhite">
	
	<% } else { 
	
	if(pagenm.equals("busintel")) {     %>  
		<!-- Left channel -->
		<div id="leftgray2" style="Z-INDEX: 104"> 
		  <table width="170" height="100%" border="0" cellpadding="0" cellspacing="0">
		    <tr> 
		      <td width="170" colspan="2" class="td-leftred">Business Intelligence</td>
		    </tr>
		    <tr> 
		      <td width="10" valign="top" bgcolor="#DCDCDC"></td>
		      <td valign="top" class="td-left"><br>
			<a href="index.html" class="typeblue1-link">Business Intelligence</a><br>
			<!-- <a href="ePedigree.html" class="typeblue1-link-sub">Dashboard</a><br> --></a> 
			<a href="BizIntel_Alerts.html" class="typeblue1-link-sub">Alerts</a><br>
				<a href="BizIntel_Alerts_DuplicateTags.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Duplicate Tags</a><br>
				<a href="BizIntel_Alerts_ExchangedTags.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Exchanged Tags</a><br>
				<a href="BizIntel_Alerts_DecommissionedTags.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Decommissioned Tags Observations</a><br>
				<a href="BizIntel_Alerts_UnusualPurchaseOrders.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Unusual Purchase Orders</a><br>
			<a href="BizIntel_Alerts_FailedEPedigrees.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Failed 
			ePedigrees </a><br>
			<a href="BizIntel_Statistics.html" class="typeblue1-link-sub">Statistics</a><br>
				<a href="BizIntel_Statistics_ePedigreeVerification.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;ePedigree Verification</a><br>
				<a href="BizIntel_Statistics_ePedigreeFailures.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;ePedigree Failures</a><br>
				<a href="BizIntel_Statistics_Shipment.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Shipment</a><br>
				<a href="BizIntel_Statistics_Revenue.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Revenue</a><br>
				<a href="BizIntel_Statistics_ExpiredProducts.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Expired Products</a><br>
				<a href="BizIntel_Statistics_RecallProducts.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Recall Products</a><br>


			<a href="BizIntel_Sales.html" class="typeblue1-link-sub">Sales<br>
			</a>
				<a href="BizIntel_Sales_Geography.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Sales by Geography</a><br>
				<a href="BizIntel_Sales_Period.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Sales per Period</a><br>
				<a href="BizIntel_Sales_Product.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Sales per product</a><br>

			    <a href="BizIntel_NewProduct.html" class="typeblue1-link-sub">New Product Introduction<br>
			</a>
				<a href="BizIntel_NewProduct_Geography.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Sales by Geography</a><br>
				<a href="BizIntel_NewProduct_Period.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Sales per Period</a><br>
				<a href="BizIntel_NewProduct_Product.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Sales per product</a><br>

			</td>
		    </tr>
		    <tr valign="bottom"> 
		      <td height="80" colspan="2" class="td-left"><img src="<%=servPath%>/assets/images/logo_poweredby.gif" width="150" height="37"></td>
		    </tr>
		  </table>
		</div>
		<div id="rightwhite">
	
	<% } else {  
	
	if(pagenm.equals("diversion")) {     %>  
	
	<!-- Left channel -->
	<div id="leftgray2" style="Z-INDEX: 104"> 
	  <table width="170" height="100%" border="0" cellpadding="0" cellspacing="0">
	    <tr> 
	      <td width="170" colspan="2" class="td-leftred">Diversion</td>
	    </tr>
	    <tr> 
	      <td width="10" valign="top" bgcolor="#DCDCDC"></td>
	      <td valign="top" class="td-left"><br>
	        <a href="index.html" class="typeblue1-link">Diversion</a><br>
	        <!-- <a href="ePedigree.html" class="typeblue1-link-sub">Dashboard</a><br> --></a> 
	        <a href="Diversion_Search.html" class="typeblue1-link-sub">Search</a><br>
			<a href="Diversion_Search_Pedigree.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Pedigree</a><br>
			<a href="Diversion_Search_EPC.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;EPC</a><br>
			
	        <a href="Diversion_Discrepancy.html" class="typeblue1-link-sub">ASN/RM Discrepancy</a><br>
			<a href="Diversion_Discrepancy_3PL.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;3PL Carrier</a><br>
			<a href="Diversion_Discrepancy_ShipFrom.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Ship from Location</a><br>
			<a href="Diversion_Discrepancy_ShipTo.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Ship to Location</a><br>
	
	
	        <a href="Diversion_TheftAlerts.html" class="typeblue1-link-sub">Theft Alerts<br>
	        </a>
			<a href="Diversion_TheftAlerts_ViewEPC.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;View EPC</a><br>
			<a href="Diversion_TheftAlerts_ViewPedigree.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;View Pedigree</a><br>
			<a href="Diversion_TheftAlerts_ViewAgencyReport.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;View Agency Report</a><br>
			<a href="Diversion_TheftAlerts_ViewLocations.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;View Locations</a><br>
	       
		    <a href="Diversion_PatientInfo.html" class="typeblue1-link-sub">Patient Information Search<br>
	        </a>
			<a href="Diversion_PatientInfo_Name.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Patient Name</a><br>
			<a href="Diversion_PatientInfo_ID.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Patient ID</a><br>
			<a href="Diversion_PatientInfo_Perscriptions.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Perscriptions</a><br>
	
			
			</td>
	    </tr>
	    <tr valign="bottom"> 
	      <td height="80" colspan="2" class="td-left"><img src="<%=servPath%>/assets/images/logo_poweredby.gif" width="150" height="37"></td>
	    </tr>
	  </table>
	</div>
	
	<div id="rightwhite">
	
	
	<% } else { 
	if(pagenm.equals("integrity")) {     %> 
	
		<!-- Left channel -->
		<div id="leftgray2" style="Z-INDEX: 104"> 
		  <table width="170" height="100%" border="0" cellpadding="0" cellspacing="0">
		    <tr> 
		      <td width="170" colspan="2" class="td-leftred">Product Integrity</td>
		    </tr>
		    <tr> 
		      <td width="10" valign="top" bgcolor="#DCDCDC"></td>
		      <td valign="top" class="td-left"><br>
			<a href="index.html" class="typeblue1-link">Product Integrity</a><br>
			<!-- <a href="ePedigree.html" class="typeblue1-link-sub">Dashboard</a><br> --></a> 
			<a href="ProductIntegrity_Search.html" class="typeblue1-link-sub">Search</a><br>
				<a href="ProductIntegrity_Search_EPC.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;EPC</a><br>
				<a href="ProductIntegrity_Search_Pedigree.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Pedigree</a><br>
			<a href="ProductIntegrity_SensorViolations.html" class="typeblue1-link-sub">Sensor 
			Violations </a><br>
				<a href="ProductIntegrity_SensorViolations_Temperature.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Temperature</a><br>
				<a href="ProductIntegrity_SensorViolations_Vibration.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Vibration</a><br>
				<a href="ProductIntegrity_SensorViolations_Humidity.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Humidity</a><br>
			<a href="ProductIntegrity_Reports.html" class="typeblue1-link-sub">Reports<br>
			</a>
				<a href="ProductIntegrity_Reports_TradingPartner.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Sensor Violations per Trading Partner</a><br>
				<a href="ProductIntegrity_Reports_3PL.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Sensor Violations per 3PL</a><br>
			<a href="ProductIntegrity_Reports_Product.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Sensor 
			Violations per Product</a><br>
				</td>
		    </tr>
		    <tr valign="bottom"> 
		      <td height="80" colspan="2" class="td-left"><img src="<%=servPath%>/assets/images/logo_poweredby.gif" width="150" height="37"></td>
		    </tr>
		  </table>
		</div>

		<div id="rightwhite">
	
	<% } else { 
	
	if(pagenm.equals("reports")) { %>
		<!-- Left channel -->
		<div id="leftgray2" style="Z-INDEX: 104">
			<table width="170" height="100%" border="0" cellpadding="0" cellspacing="0" ID="Table5">
				<tr>
					<td width="170" colspan="2" class="td-leftred">Reports</td>			
				</tr>
				<tr>
					<td width="10" valign="top" bgcolor="#dcdcdc"></td>
					
				</tr>
				<tr valign="bottom">
					<td height="80" colspan="2" class="td-left"><img src="<%=servPath%>/assets/images/logo_poweredby.gif" width="150" height="37"></td>
				</tr>
			</table>
		</div>
		<div id="rightwhite2">
		
	<% }  else { 
	
	if(pagenm.equals("pedBank")) { %>
		<!-- Left channel -->
			<div id="leftgray2" style="Z-INDEX: 104">
				<table width="170" height="100%" border="0" cellpadding="0" cellspacing="0" ID="Table5">
					<tr>
						<td width="170" colspan="2" class="td-leftred">ePedigree Manager</td>
					</tr>
					
              <tr valign="bottom">
					<td   colspan="2" class="td-left"><img src="<%=servPath%>/assets/images/logo_poweredby.gif" width="150" height="37"></td>
				</tr>
				</table>
			</div>
			<div id="rightwhite2">
	
	<% } else {  
		if(pagenm.equals("track")) { %>
		<!-- Left channel -->
			<div id="leftgray2" style="Z-INDEX: 104">
				<table width="170" border="0" cellpadding="0" cellspacing="0" ID="Table5">
					<tr>
						<td width="170" colspan="2" class="td-leftred" align="center">Track and Trace</td>
					</tr>

				</table>
			</div>
			<div id="rightwhite2"></div>
	<% }  
		if(pagenm.equals("returns")) { %>
			<div id="leftgray2" style="Z-INDEX: 104"> 
			  <table width="170" height="100%" border="0" cellpadding="0" cellspacing="0">
			    <tr> 
			      <td width="170" colspan="2" class="td-leftred">Returns</td>
			    </tr>
			    <tr> 
			      <td width="10" valign="top" bgcolor="#DCDCDC"></td>
			      <td valign="top" class="td-left"><br>
				  	<a href="../returns/returns.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=returns" class="typeblue1-link">ITEM</a><br>
					<a href="../returns/returns_EPC.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=returns" class="typeblue1-link">CASE</a><br>
			        <!-- <a href="ePedigree.html" class="typeblue1-link-sub">Dashboard</a><br> -->        </td>
			    </tr>
			    <tr valign="bottom"> 
			      <td height="80" colspan="2" class="td-left"><img src="../assets/images/logo_poweredby.gif" width="150" height="37"></td>
			    </tr>
			  </table>
			</div>
			
		<div id="rightwhite2"> 
		
	<% } else {  %>
	<% System.out.println("************************************************************Inside Top menu left jsp********");%>
	<% if(pagenm.equals("TPManager")) { %>
		<!-- Left channel -->
		<div id="leftgray2" style="Z-INDEX: 104"> 
		  <table width="170" height="100%" border="0" cellpadding="0" cellspacing="0" ID="Table5">
		    <tr> 
		      <td width="170" colspan="2" class="td-leftred">Vendor Information Manager</td>
		    </tr>
		    <tr> 
		      <td width="10" valign="top" bgcolor="#DCDCDC"></td>
		      <td valign="top" class="td-left"><br>
		        <a href="TradingPartnerList.do?tp_company_nm=<%=tp_company_nm%>&pagenm=TPManager&linkName=tpmanager" class=<%=(linkName.equals("tpmanager"))?"typered4-link":"typeblue3-link"%>><strong> Vendor Information Manager</strong></a><br> 
		        <% if(tpList.equalsIgnoreCase("view")&&tpList != null){
		          String tpGenId1=request.getParameter("tpGenId");
String  tpName1=request.getParameter("tpName");
		        if(tpGenId1==null)tpGenId1="";
		        if(tpName1==null)tpName1="";
		        
		        
		         %>
		     &nbsp;&nbsp;&nbsp;&nbsp;   <A href="TradingPartnerView.do?operationType=FIND&tpGenId=<%=tpGenId1%>&tpName=<%=tpName1%>&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>&tpList=view&linkName=tpmain" class=<%=(linkName.equals("tpmain"))?"typered4-link":"typeblue3-link"%>><strong>Main </strong></A>&nbsp;&nbsp;&nbsp;&nbsp;<br/>
			 &nbsp;&nbsp;&nbsp;&nbsp;	<A href="TPLocationList.do?tpGenId=<%=tpGenId1%>&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>&tpList=view&tlList=view&linkName=tpLocation" class=<%=(linkName.equals("tpLocation"))?"typered4-link":"typeblue3-link"%> onClick="return LocationReadPrivilage();"><strong>Locations </strong></A>&nbsp;&nbsp;&nbsp;&nbsp;<br/>
			          <%
			          String tlList1=request.getParameter("tlList");
			          if(tlList1==null)tlList1="";
			          if(tlList1.equalsIgnoreCase("view"))
			          {
			          %>
			  &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;         <a href="LocationNew.do?tpGenId=<%=tpGenId1%>&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>&tlList=view&tpList=view&linkName=createLoc" class=<%=(linkName.equals("createLoc"))?"typered4-link":"typeblue3-link"%> >Create New Location</a><br> 
			
			          <%}%>
			 
			 <%
			 
			 String tcList1=request.getParameter("tcList");
			 if(tcList1==null)tcList1="";
			 if(tcList1.equalsIgnoreCase("view"))
			 {
			 %>
			  &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;    <a href="CatalogNew.do?tpGenId=<%=tpGenId1%>&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>&tpList=view&tcList=view&linkName=tpNewCatalog" class=<%=(linkName.equals("tpNewCatalog"))?"typered4-link":"typeblue3-link"%> >Create New Catalog</a><br>
			 <%}
			 %>
			 
			 <%
			 String catSchema=request.getParameter("catSchema");
			 if(catSchema==null)catSchema="";
			 if(catSchema.equalsIgnoreCase("view"))
			 {
			 
			 genId=request.getParameter("catalogGenId");
			  if(genId==null)genId="";
			 %>
		 &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; 	 <a href="OpenCatalogSchemaDef.do?fromModule=TP&catalogGenId=<%= genId%>&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>&tpList=view&tcList=view&linkName=catSchemaDef" class=<%=(linkName.equals("catSchemaDef"))?"typered4-link":"typeblue3-link"%> onClick="return CatalogAccessPrivilage()">
					Schema Def</a><br>
				 &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;	  <a href="SelectMasterCatalog.do?leftCatalogGenId=<%= genId%>&tpGenId=<%=tpGenId1%>&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>&tpList=view&tcList=view&linkName=MapToStandard" class=<%=(linkName.equals("MapToStandard"))?"typered4-link":"typeblue3-link"%> onClick="return CatalogAccessPrivilage()">Map To Standard </a> <br>
			 
			 <%
			 }
			 %>
			 
		  		<% } %>
			<a href="TradingPartnerNew.do?tp_company_nm=<%=tp_company_nm%>&pagenm=TPManager&linkName=createTP" class=<%=(linkName.equals("createTP"))?"typered4-link":"typeblue3-link"%> >Create New Vendor</a><br>
		      </td>
		    </tr>
		    <tr valign="bottom"> 
		      <td height="80" colspan="2" class="td-left"><img src="./assets/images/logo_poweredby.gif" width="150" height="37"></td>
		    </tr>
		  </table>
		</div>
	<% } else if(pagenm.equals("Catalog")) {  %>
	
	
	<div id="leftgray4"> 
  <table width="170" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr> 
      <td width="170" colspan="2" class="td-leftred">GCPIM</td>
    </tr>
    <tr> 
      <td width="10" valign="top" bgcolor="#DCDCDC"></td>
      <td valign="top" class="td-left" ><br>
  
        <a href="ProductMasterSearchEmpty.do?tp_company_nm=<%=tp_company_nm%>&pagenm=Catalog&linkName=pSearch" class=<%=(linkName.equals("pSearch"))?"typered4-link":"typeblue3-link"%>>Product Search</a><br> 
		<a href="ShowMasterCatalogs.do?tp_company_nm=<%=tp_company_nm%>&pagenm=Catalog&linkName=newProduct" class=<%=(linkName.equals("newProduct"))?"typered4-link":"typeblue3-link"%>>Create New Product</a><br>
		<a href="KitNew.do?tp_company_nm=<%=tp_company_nm%>&pagenm=Catalog&linkName=newKit" class=<%=(linkName.equals("newKit"))?"typered4-link":"typeblue3-link"%>>Create New Kit</a><br>
		<a href="GCPIMOpenCatalogSchemaDef.do?tp_company_nm=<%=tp_company_nm%>&pagenm=Catalog&linkName=editMaster" class=<%=(linkName.equals("editMaster"))?"typered4-link":"typeblue3-link"%>>Edit Master Product Schema</a><br>
     	
     	<% if(linkName.equals("manageStandard")||linkName.equals("newCatalog")){ %>
				<a href="GCPIMStandardCatalogList.do?tp_company_nm=<%=tp_company_nm%>&pagenm=Catalog&linkName=manageStandard" class=<%=(linkName.equals("manageStandard"))?"typered4-link":"typeblue3-link"%>>Manage Standard Catalogs</a><br>
		
			&nbsp;&nbsp;&nbsp;&nbsp;<a href="GCPIMStandardCatalogNew.do?operationType=ADD&tp_company_nm=<%=tp_company_nm%>&pagenm=Catalog&linkName=newCatalog" class=<%=(linkName.equals("newCatalog"))?"typered4-link":"typeblue3-link"%>>Create New Catalog</a>
			<%}else{%>
			
				<a href="GCPIMStandardCatalogList.do?tp_company_nm=<%=tp_company_nm%>&pagenm=Catalog&linkName=manageStandard" class=<%=(linkName.equals("manageStandard"))?"typered4-link":"typeblue3-link"%>>Manage Standard Catalogs</a><br>
			<%}%>
	  </td>
    </tr>
    <tr valign="bottom"> 
      <td height="80" colspan="2" class="td-left"><img src="./assets/images/logo_poweredby.gif" width="150" height="37"></td>
    </tr>
  </table>
</div>
	
	
	
	<%}else{ %>
	
	
	<% if(pagenm.equals("pedigree")) { %>
	<!-- Left channel -->
	<div id="leftgray2" style="Z-INDEX: 104">
		<table width="170" height="100%" border="0" cellpadding="0" cellspacing="0" ID="Table5">
			<tr>
				<td width="170" colspan="2" class="td-leftred">ePedigree Manager</td>
			</tr>
			<tr>
				<td width="10" valign="top" bgcolor="#dcdcdc"></td>
				<% //if (accessLevel.equals("true")){%>
				<td valign="top" class="td-left"><br>
					
				<!--	<a href="<%=servPath%>/dist/epedigree/ShippingManagerSearchEmpty.do?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>&linkName=shippingmang" class=<%=(linkName.equals("shippingmang"))?"typered4-link":"typeblue3-link"%>>Shipping 
						Manager<br>
					</a>
					<br>
					
					 <a href="#" class="typeblue3-link">Find</a><br> 
					
					<a href="../epedigree/PedigreeSearch.do?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>&accesslevel=apnsearch&linkName=apnsear" class=<%=(linkName.equals("apnsear"))?"typered4-link":"typeblue3-link"%>>Search 
						Pedigrees</a><br>
						<br>-->
					<a href="../epedigree/PedigreeTradingPartner.do?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>&accesslevel=PTP&linkName=PTP" class=<%=(linkName.equals("PTP"))?"typered4-link":"typeblue3-link"%>>Add Pedigree Trading Partner 
						</a><br><br>
						<a href="../epedigree/ListPedigreeTradingPartner.do?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>&accesslevel=PTP&linkName=ListPTP" class=<%=(linkName.equals("ListPTP"))?"typered4-link":"typeblue3-link"%>>List Pedigree Trading Partner 
						</a><br>
						
					
					
					
					
					<%//}%>
			</tr>
			<tr valign="bottom">
				<td height="80" colspan="2" class="td-left"><img src="<%=servPath%>/assets/images/logo_poweredby.gif" width="150" height="37"></td>
			</tr>
			<% System.out.println("************************************************************After Top menu left jsp********");%>
		</table>
	</div>
	<% } %>
	<div id="rightwhite2">

	<% } } } } } } } } }%>



