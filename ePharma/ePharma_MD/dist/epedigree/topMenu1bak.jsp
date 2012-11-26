
<!-- Top Header -->
<div id="bg" style="Z-INDEX: 100">
	<% 
	if(!tp_company_nm.equals(""))	{	//EXTRANET %>
	<div class="roleIcon-extranet"></div>
	<% } else {	//INTERNAL USER %>
	<div class="roleIcon-distributor"></div>
	<% } %>
	<div class="navIcons">
		<a href="../../landing.html" target="_top"><img src="../../assets/images/home.gif" width="22" height="27" hspace="10" border="0"></a>
		<img src="../../assets/images/account.gif" width="41" height="27" hspace="10"> <img src="../../assets/images/help.gif" width="21" height="27" hspace="10">
		<img src="../../assets/images/print.gif" width="27" height="27" hspace="10"> <img src="../../assets/images/logout.gif" width="34" height="27" hspace="10">
		<a href="/ePharma/dist/epedigree/index.jsp?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>" target="_top"><IMG height="27" hspace="10" src="../../assets/images/inbox.gif" border="0"></a>
		<img src="../../assets/images/space.gif" width="20">
	</div>
	<div class="logo"><img src="../../assets/images/logos_combined.jpg"></div>
</div>

<% String SessionID = (String)session.getAttribute("sessionID");
System.out.println("inside topmenu");
if(tp_company_nm.equals(""))	{	//INTERNAL USER  %>

	<!-- Top level nav layer 1 -->
	<div id="menu" style="Z-INDEX: 101">
		<table width="100%" height="25" border="0" cellpadding="0" cellspacing="0" ID="Table1">
			<tr>
				<td valign="middle" background="../../assets/images/bg_menu.jpg"><table width="800" border="0" cellpadding="0" cellspacing="0" ID="Table2">
						<tr>
							<% if(pagenm.equals("epcadmin")) { %>
								<td align="center" class="primaryNav_On" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../admin/index.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=epcadmin" class="menu-link" target="_parent">Admin 
									Console</a></td>
							<% } else { %>
								<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
									onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../admin/index.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=epcadmin" class="menu-link" target="_parent">Admin 
									Console</a></td>
							<% } %>										
							<td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
							<% if(pagenm.equals("pedigree")) { %>
								<td align="center" class="primaryNav_On" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../epedigree/index.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree" class="menu-link menuBlack" target="_parent">ePedigree</a></td>
							<% } else { %>
								<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../epedigree/index.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree" class="menu-link menuBlack" target="_parent">ePedigree</a></td>
							<% } %>						
							<td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
							
							<% if(pagenm.equals("pedBank")) { %>
								<td align="center" class="primaryNav_On" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../pedigreeBank/pedigree_Bank.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=pedBank" class="menu-link" target="_parent">Pedigree Bank 
									</a></td>
							<% } else { %>
								<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../pedigreeBank/pedigree_Bank.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=pedBank" class="menu-link" target="_parent">Pedigree Bank 
								</a></td> <% } %>
							<td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
							<% if(pagenm.equals("track")) { %>
								<td align="center" class="primaryNav_On" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
									onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../trackTrace/index.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=track" class="menu-link" target=_top>Track 
									and Trace</a></td>
							<% } else { %>
								<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
									onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../trackTrace/index.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=track" class="menu-link" target=_top>Track 
									and Trace</a></td>
							<% } %>
							<td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>

							<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="/ePharma/TradingPartnerList.do?tp_company_nm=<%=tp_company_nm%>&pagenm=TPManager" class="menu-link" target="_parent">Trading 
									Partner Manager</a></td>
							
						<td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
						<% if(pagenm.equals("Catalog")) { %>
								<td align="center" class="primaryNav_On" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="/ePharma/ProductMasterSearchEmpty.do?tp_company_nm=<%=tp_company_nm%>&pagenm=Catalog" class="menu-link" target="_parent">GCPIM</a></td>
							<% } else { %>
								<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="/ePharma/ProductMasterSearchEmpty.do?tp_company_nm=<%=tp_company_nm%>&pagenm=Catalog" class="menu-link" target="_parent">GCPIM</a></td>				
							<% } %>	
			            
    						
							
							<td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
							<% if(pagenm.equals("returns")) { %>
								<td align="center" class="primaryNav_On" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../returns/returns.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=returns" class="menu-link" target="_parent">Returns 
									</a></td>
							<% } else { %>
								<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../returns/returns.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=returns" class="menu-link" target="_parent">Returns 
								</a></td>														
							<% } %>
							<td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>	
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
	      <td height="80" colspan="2" class="td-left"><img src="../../assets/images/logo_poweredby.gif" width="150" height="37"></td>
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
		      <td height="80" colspan="2" class="td-left"><img src="../../assets/images/logo_poweredby.gif" width="150" height="37"></td>
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
	      <td height="80" colspan="2" class="td-left"><img src="../../assets/images/logo_poweredby.gif" width="150" height="37"></td>
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
		      <td height="80" colspan="2" class="td-left"><img src="../../assets/images/logo_poweredby.gif" width="150" height="37"></td>
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
					<td width="170" colspan="2" class="td-leftred">Extranet</td>			
				</tr>
				<tr>
					<td width="10" valign="top" bgcolor="#dcdcdc"></td>
					<td valign="top" class="td-left"><br>
						<a href="ePedigree_ReceivingManager.do?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>" class="typeblue1-link-sub">Receiving 
							Manager<br>
						</a>
						<br>
						<br>
						<a href="#" class="typeblue1-link-sub">Find</a><br>
						<a href="../epedigree/ePedigree_Manager_Search.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Search 
							Orders</a><br>
						<a href="../epedigree/ePedigree_APNManager_Search.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Search 
							ePedigre</a>
						<br>
						<br>
						<a href="../epedigree/ePedigree_Reports.html" class="typeblue1-link-sub">Reporting</a><br>
						<a href="../epedigree/ePedigree_Reports.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;ePedigree</a><br>
						<a href="../epedigree/ePedigree_Reports_APN.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;APN</a><br>
						<a href="../epedigree/ePedigree_Reports_Repackaging.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Repackaging</a><br>
	
					</td>
				</tr>
				<tr valign="bottom">
					<td height="80" colspan="2" class="td-left"><img src="../../assets/images/logo_poweredby.gif" width="150" height="37"></td>
				</tr>
			</table>
		</div>
		<div id="rightwhite2">
		
	<% }  else { 
	
	if(pagenm.equals("pedBank")) { %>
		<!-- Left channel -->
			<div id="leftgray2" style="Z-INDEX: 104">
				<table width="170" border="0" cellpadding="0" cellspacing="0" ID="Table5">
					<tr>
						<td width="170" colspan="2" class="td-leftred">ePedigree Manager</td>
					</tr>

				</table>
			</div>
			<div id="rightwhite2">
	
	<% } else {  
	
		if(pagenm.equals("returns")) { %>
			<div id="leftgray" style="Z-INDEX: 104"> 
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
	<!-- Left channel -->
	<div id="leftgray2" style="Z-INDEX: 104">
		<table width="170" height="100%" border="0" cellpadding="0" cellspacing="0" ID="Table5">
			<tr>
				<td width="170" colspan="2" class="td-leftred">ePedigree Manager</td>
			</tr>
			<tr>
				<td width="10" valign="top" bgcolor="#dcdcdc"></td>
				<td valign="top" class="td-left"><br>
					<a href="/ePharma/dist/epedigree/ePedigree_ReceivingManager.do?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>" class="typeblue1-link-sub">Receiving 
						Manager<br>
					</a>
					<br>
					<a href="/ePharma/dist/epedigree/ShippingManagerSearchEmpty.do?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>" class="typeblue1-link-sub">Shipping 
						Manager<br>
					</a>
					<br>
					
					<a href="../epedigree/index.jsp" class="typeblue1-link-sub">Find</a><br>
					
					<a href="../epedigree/APNSearch.do?pagenm=pedigree&tp_company_nm=" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Search 
						APNs</a><br>


					<a href="../epedigree/OrderSearch.do?pagenm=pedigree&tp_company_nm=" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Search 
						Orders</a><br>
						
						
						
					<a href="../epedigree/SearchInvoices.do?pagenm=pedigree&tp_company_nm=" class="typeblue1-link-sub-sub">&nbsp;&nbsp;Search 
						Invoices</a><br>
					
					<a href="../epedigree/ASNSearch.do?pagenm=pedigree&tp_company_nm=" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Search 
						ASNs</a><br>
					<br>
					
					
			</tr>
			<tr valign="bottom">
				<td height="80" colspan="2" class="td-left"><img src="../../assets/images/logo_poweredby.gif" width="150" height="37"></td>
			</tr>
			<% System.out.println("************************************************************After Top menu left jsp********");%>
		</table>
	</div>
	<div id="rightwhite2">

	<% } } } } } } } } %>

<% } else {	//START EXTRANET ----------------------------------------------------------------------------------------

%>

	<!-- Top level nav layer 1 -->
	<div id="menu" style="Z-INDEX: 101">
		<table width="100%" height="25" border="0" cellpadding="0" cellspacing="0" ID="Table1">
			<tr>
				<td valign="middle" background="../../assets/images/bg_menu.jpg"><table width="800" border="0" cellpadding="0" cellspacing="0" ID="Table2">
						<tr>	
							<% if(pagenm.equals("admin")) { %>
								<td align="center" class="primaryNav_On" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
									onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../admin/extindex.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=admin" class="menu-link" target=_top>Admin 
										Console</a></td>
							<% } else { %>
								<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
										onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../admin/extindex.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=admin" class="menu-link" target=_top>Admin 
									Console</a></td>
							<% } %>
							<td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
							<% if(pagenm.equals("pedigree")) { %>
								<td align="center" class="primaryNav_On" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
									onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../epedigree/index.jsp?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>" class="menu-link menuBlack" target=_top>ePedigree</a></td>
							<% } else { %>
								<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../epedigree/index.jsp?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>" class="menu-link menuBlack" target=_top>ePedigree</a></td>
							<% } %>
							
							<td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
							<% if(pagenm.equals("track")) { %>
								<td align="center" class="primaryNav_On" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
									onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../trackTrace/index.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=track" class="menu-link" target=_top>Track 
									and Trace</a></td>
							<% } else { %>
								<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
									onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../trackTrace/index.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=track" class="menu-link" target=_top>Track 
									and Trace</a></td>
							<% } %>
							<td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
							<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../catalog/index.html" class="menu-link" target=_top>Catalog</a></td>
							
							
							<td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
							<% if(pagenm.equals("returns")) { %>
								<td align="center" class="primaryNav_On" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../returns/returns.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=returns" class="menu-link" target="_parent">Returns 
									</a></td>
							<% } else { %>
								<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../returns/returns.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=returns" class="menu-link" target="_parent">Returns 
								</a></td>														<% } %>
							<td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>

	<% if(!pagenm.equals("reports")) { %>
	<!-- Left channel -->
	<div id="leftgray2" style="Z-INDEX: 104">
		<table width="170" height="100%" border="0" cellpadding="0" cellspacing="0" ID="Table5">
			<tr>
				<td width="170" colspan="2" class="td-leftred">Extranet</td>			
			</tr>
			
			<tr>
							<td width="10" valign="top" bgcolor="#dcdcdc"></td>
							<td valign="top" class="td-left"><br>
								<a href="/ePharma/dist/epedigree/ePedigree_ReceivingManager.do?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>" class="typeblue1-link-sub">Receiving 
									Manager<br>
								</a>
								<br>
								<a href="/ePharma/dist/epedigree/ShippingManagerSearchEmpty.do?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>" class="typeblue1-link-sub">Shipping 
									Manager<br>
								</a>
								<br>
								
								<a href="../epedigree/index.jsp" class="typeblue1-link-sub">Find</a><br>
								
								<a href="../epedigree/APNSearch.do?pagenm=pedigree&tp_company_nm=" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Search 
									APNs</a><br>
			
			
								<a href="../epedigree/OrderSearch.do?pagenm=pedigree&tp_company_nm=" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Search 
									Orders</a><br>
									
									
									
								<a href="../epedigree/SearchInvoices.do?pagenm=pedigree&tp_company_nm=" class="typeblue1-link-sub-sub">&nbsp;&nbsp;Search 
									Invoices</a><br>
								
								<a href="../epedigree/ASNSearch.do?pagenm=pedigree&tp_company_nm=" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Search 
									ASNs</a><br>
								<br>
								
								
			</tr>
			<tr valign="bottom">
				<td height="80" colspan="2" class="td-left"><img src="../../assets/images/logo_poweredby.gif" width="150" height="37"></td>
			</tr>
		</table>
	</div>
	<div id="rightwhite2">
	<% }  else { %>

	<!-- Left channel -->
	<div id="leftgray2"> 
	  <table width="170" height="100%" border="0" cellpadding="0" cellspacing="0">
	    <tr> 
	      <td width="170" colspan="2" class="td-leftred">Logistics</td>
	    </tr>
	    <tr> 
	      <td width="10" valign="top" bgcolor="#DCDCDC"></td>
	      <td valign="top" class="td-left"><br>
			<a href="index.html" class="typeblue1-link">Logistics</a><br>
		<!-- <a href="ePedigree.html" class="typeblue1-link-sub">Dashboard</a><br> -->
		<a href="logistics_Shipment.html" class="typeblue1-link-sub">Shipment<br>
		</a>
		<a href="logistics_Receiving.html" class="typeblue1-link-sub">Receiving<br>
		</a>
		<a href="logistics_Reconciliation.html" class="typeblue1-link-sub">Reconciliation<br>
		</a>
		<a href="logistics_Deductions.html" class="typeblue1-link-sub">Deductions<br>
		</a>
		<a href="logistics_Returns.html" class="typeblue1-link-sub">Returns/Reverse Logistics<br>
		</a>
	    </tr>
	    <tr valign="bottom"> 
	      <td height="80" colspan="2" class="td-left"><img src="../../assets/images/logo_poweredby.gif" width="150" height="37"></td>
	    </tr>
	  </table>
	</div>

	<div id="rightwhite2">
	<% } %>

<% } %>