
<%@ include file='../../includes/jspinclude.jsp'%>
<%
String clientIP = request.getRemoteAddr();
//String sessionID =  request.getParameter("sessionID");
String sessionID = (String)session.getAttribute("sessionID");
String tp_company_nm = request.getParameter("tp_company_nm");
String pagenm = request.getParameter("pagenm");
String servPath = request.getParameter("servPath");
if(sessionID != null && tp_company_nm != null && pagenm != null) {

		System.out.println(" These are attributes set in Session : " + sessionID + " " + tp_company_nm + " " +  pagenm);
		session.setAttribute("r_sessionID",sessionID);
		session.setAttribute("r_tp_company_nm",tp_company_nm);
		session.setAttribute("r_pagenm",pagenm);
} else {

com.rdta.tlapi.xql.Connection connection = ConnectTL();
com.rdta.tlapi.xql.Statement statement = getStatement(connection);
sessionID = getSessionByClientIP(statement,clientIP);
CloseConnectionTL(connection);
	
	//sessionID = (String)session.getAttribute("r_sessionID");
	tp_company_nm  = (String)session.getAttribute("r_tp_company_nm");
	pagenm =  (String)session.getAttribute("r_pagenm");
}
String servPath = request.getContextPath();
%>

<!-- Top level nav layer 1 -->
<div id="menu" style="Z-INDEX: 101"> 
  <table width="100%" height="25" border="0" cellpadding="0" cellspacing="0" ID="Table1">
    <tr> 
      <td valign="middle" background="./assets/images/bg_menu.jpg"><table width="800" border="0" cellpadding="0" cellspacing="0">
          <tr> 
            <td align="center" class="primaryNav_Off"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''">
    <a href="<%=servPath%>/dist/admin/AdminMenu.jsp?sessionID=<%=sessionID%>&pagenm=epcadmin&tp_company_nm=<%=tp_company_nm%>"">Admin Console</a></td>
    

	
        
            <td><img src="./assets/images/menu_bg1.jpg" width="3" height="23"></td>
            <td align="center" class="primaryNav_Off" 
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="./dist/epedigree/epedigree.do?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>" class="menu-link" target="_parent">ePedigree</a></td>
    
    
     <td><img src="./assets/images/menu_bg1.jpg" width="3" height="23"></td>
     
    
    <td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
    								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="./dist/pedigreeBank/pedigree_Bank.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=pedBank" class="menu-link" target="_parent">Pedigree Bank 
								</a></td> 
								
           
            
           
            <td><img src="./assets/images/menu_bg1.jpg" width="3" height="23"></td>
            
            
            <td align="center" class="primaryNav_Off" 
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="./dist/trackTrace/index.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=track" class="menu-link" target="_parent">Track and Trace</a></td>
    
    
            <td><img src="./assets/images/menu_bg1.jpg" width="3" height="23"></td>
            
            <% if(pagenm.equals("reports")) { %>
				<td align="center" class="primaryNav_On" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
					onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="dist/reports/AdvancedReports.do?sessionID=<%=sessionID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=reports" class="menu-link" target="_parent">Reports</a></td>
			<% } else { %>
				<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
				onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="dist/reports/AdvancedReports.do?sessionID=<%=sessionID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=reports" class="menu-link" target="_parent">Reports</a></td>
			<% } %>
			
			<td><img src="./assets/images/menu_bg1.jpg" width="3" height="23"></td>							
            
            <% if(pagenm.equals("TPManager")) { %>

		     <td align="center" class="primaryNav_On"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="/ePharma/TradingPartnerList.do?tp_company_nm=<%=tp_company_nm%>&pagenm=TPManager" class="menu-link menuBlack" target="_parent">Trading Partner Manager</a></td>

	       <% } else { %>

		     <td align="center" class="primaryNav_Off"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="/ePharma/TradingPartnerList.do?tp_company_nm=<%=tp_company_nm%>&pagenm=TPManager" class="menu-link menuBlack" target="_parent">Trading Partner Manager</a></td>


		   	<% } %>
		   	<td><img src="./assets/images/menu_bg1.jpg" width="3" height="23"></td>
		   	
		 <% if(pagenm.equals("Catalog")) { %>
		 
		             
		             <td align="center" class="primaryNav_On"
		     onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="/ePharma/ProductMasterSearchEmpty.do?tp_company_nm=<%=tp_company_nm%>&pagenm=Catalog" class="menu-link" target="_parent">GCPIM</a></td>
		 
		 
		 	       <% } else { %>
		 			
		    			<td align="center" class="primaryNav_Off"
		     onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="/ePharma/ProductMasterSearchEmpty.do?tp_company_nm=<%=tp_company_nm%>&pagenm=Catalog" class="menu-link" target="_parent">GCPIM</a></td>
		 	   	
	   	<% } %>
	   	
		   	
       
<td><img src="./assets/images/menu_bg1.jpg" width="3" height="23"></td>
							

<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
								onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="./dist/returns/returns.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=returns" class="menu-link" target="_parent">Returns 
								</a></td>
		 </tr>
        </table>
        
        </td>
    </tr>
  </table>
</div>
