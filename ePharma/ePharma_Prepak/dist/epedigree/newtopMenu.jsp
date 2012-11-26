
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

<% 

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
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../epedigree/epedigree.do?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>" class="menu-link menuBlack" target="_parent">ePedigree</a></td>
							<% } else { %>
								<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../epedigree/epedigree.do?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>" class="menu-link menuBlack" target="_parent">ePedigree</a></td>
							<% } %>
							<td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
							<% if(pagenm.equals("recall")) { %>
								<td align="center" class="primaryNav_On" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../prodRecall/index.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=recall" class="menu-link" target="_parent">Product 
									Recall</a></td>
							<% } else { %>
								<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../prodRecall/index.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=recall" class="menu-link" target="_parent">Product 
								Recall</a></td>
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
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="/ePharma/TradingPartnerList.do?tp_company_nm=<%=tp_company_nm%>&pagenm=TPManager" class="menu-link" target="_parent">Trading 
									Partner Manager</a></td>
							
						<td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
							
			            <% if(pagenm.equals("packaging")) { %>
								<td align="center" class="primaryNav_On" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../epedigree/ePedigree_Commissioning_Packaging.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=packaging" class="menu-link menuBlack" target="_parent">Packaging</a></td>
							<% } else { %>
								<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../epedigree/ePedigree_Commissioning_Packaging.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=packaging" class="menu-link menuBlack" target="_parent">Packaging</a></td>
							<% } %>
    						<td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
							
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
	<!-- Top level nav layer 2 -->
	<div id="menu1" style="Z-INDEX: 102">
		<table width="100%" height="25" border="0" cellpadding="0" cellspacing="0" ID="Table3">
			<tr>
				<td valign="bottom" background="../../assets/images/bg_menu.jpg"><table width="550" border="0" cellpadding="0" cellspacing="0" ID="Table4">
						<tr>
							<% if(pagenm.equals("Catalog")) { %>
								<td align="center" class="primaryNav_On" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="/ePharma/ProductMasterSearchEmpty.do?tp_company_nm=<%=tp_company_nm%>&pagenm=Catalog" class="menu-link" target="_parent">GCPIM</a></td>
							<% } else { %>
								<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="/ePharma/ProductMasterSearchEmpty.do?tp_company_nm=<%=tp_company_nm%>&pagenm=Catalog" class="menu-link" target="_parent">GCPIM</a></td>				
							<% } %>
							<td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
							
							<% if(pagenm.equals("integrity")) { %>
								<td align="center" class="primaryNav_On" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../productIntegrity/index.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=integrity" class="menu-link" target="_parent">Product 
									Integrity</a></td>
							<% } else { %>
								<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
									onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../productIntegrity/index.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=integrity" class="menu-link" target="_parent">Product 
									Integrity</a></td>
							<% } %>
							<td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
							<% if(pagenm.equals("reports")) { %>
								<td align="center" class="primaryNav_On" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../reports/AdvancedReports.do?tp_company_nm=<%=tp_company_nm%>&pagenm=reports" class="menu-link" target="_parent">Reports</a></td>
							<% } else { %>
								<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../reports/AdvancedReports.do?tp_company_nm=<%=tp_company_nm%>&pagenm=reports" class="menu-link" target="_parent">Reports</a></td>
							<% } %>
							<td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
							<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../kpiDashboard/index.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=kpidashboard" class="menu-link" target="_parent">KPI 
									Dashboard</a></td>
							<td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
							<% if(pagenm.equals("pedBank")) { %>
								<td align="center" class="primaryNav_On" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../pedigreeBank/pedigree_Bank.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=pedBank" class="menu-link" target="_parent">Pedigree Bank 
									</a></td>
							<% } else { %>
								<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../pedigreeBank/pedigree_Bank.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=pedBank" class="menu-link" target="_parent">Pedigree Bank 
								</a></td>														<% } %>
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

<% } %>

</body>
</html>