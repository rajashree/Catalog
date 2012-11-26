
<%

//String sessionID =  request.getParameter("sessionID");
String sessionID = (String)session.getAttribute("sessionID");
String tp_company_nm = request.getParameter("tp_company_nm");
String pagenm = request.getParameter("pagenm");


if(sessionID != null && tp_company_nm != null && pagenm != null) {

		System.out.println(" These are attributes set in Session : " + sessionID + " " + tp_company_nm + " " +  pagenm);
		session.setAttribute("r_sessionID",sessionID);
		session.setAttribute("r_tp_company_nm",tp_company_nm);
		session.setAttribute("r_pagenm",pagenm);
} else {

	sessionID = (String)session.getAttribute("r_sessionID");
	tp_company_nm  = (String)session.getAttribute("r_tp_company_nm");
	pagenm =  (String)session.getAttribute("r_pagenm");
}

%>


<!-- Top level nav layer 2 -->
<div id="menu1"> 
  <table width="100%" height="25" border="0" cellpadding="0" cellspacing="0">
    <tr> 
      <td valign="bottom" background="../../assets/images/bg_menu.jpg"><table width="550" border="0" cellpadding="0" cellspacing="0">
          <tr> 

               <% if(pagenm.equals("Catalog")) { %>

            
            <td align="center" class="primaryNav_On"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="/ePharma/ProductMasterSearchEmpty.do?tp_company_nm=<%=tp_company_nm%>&pagenm=Catalog" class="menu-link" target="_parent">GCPIM</a></td>


	       <% } else { %>
			
   			<td align="center" class="primaryNav_Off"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="/ePharma/ProductMasterSearchEmpty.do?tp_company_nm=<%=tp_company_nm%>&pagenm=Catalog" class="menu-link" target="_parent">GCPIM</a></td>
	   	
	   	<% } %>
	   	
	   		<td><img src="./assets/images/menu_bg1.jpg" width="3" height="23"></td>	
            <td align="center" class="primaryNav_Off"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="./dist/productIntegrity/index.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=integrity" class="menu-link" target="_parent">Product Integrity</a></td>
            <td><img src="./assets/images/menu_bg1.jpg" width="3" height="23"></td>
            <td align="center" class="primaryNav_Off"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="./dist/reports/AdvancedReports.do?tp_company_nm=<%=tp_company_nm%>&pagenm=reports" class="menu-link" target="_parent">Reports</a></td>
            <td><img src="./assets/images/menu_bg1.jpg" width="3" height="23"></td>
            <td align="center" class="primaryNav_Off"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="./dist/kpiDashboard/index.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=kpidashboard" class="menu-link" target="_parent">KPI Dashboard</a></td>
            <td><img src="./assets/images/menu_bg1.jpg" width="3" height="23"></td>
								
			<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
	onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="./dist/pedigreeBank/pedigree_Bank.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=pedBank" class="menu-link" target="_parent">Pedigree Bank </a></td>
			<td><img src="./assets/images/menu_bg1.jpg" width="3" height="23"></td>													
	
	<% if(pagenm.equals("returns")) { %>
								<td align="center" class="primaryNav_On" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../returns/returns.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=returns" class="menu-link" target="_parent">Returns 
									</a></td>
							<% } else { %>
								<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../returns/returns.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=returns" class="menu-link" target="_parent">Returns 
								</a></td>														
							<% } %>
		   	<td><img src="./assets/images/menu_bg1.jpg" width="3" height="23"></td>
							

          </tr>
        </table></td>
    </tr>
  </table>
</div>
