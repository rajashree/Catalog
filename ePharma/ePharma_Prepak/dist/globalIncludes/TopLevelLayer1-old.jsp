

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

<!-- Top level nav layer 1 -->
<div id="menu"> 
  <table width="100%" height="25" border="0" cellpadding="0" cellspacing="0">
    <tr> 
      <td valign="middle" background="./assets/images/bg_menu.jpg"><table width="800" border="0" cellpadding="0" cellspacing="0">
          <tr> 
            <td align="center" class="primaryNav_Off"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="./dist/admin/index.jsp?tp_company_nm=<%= tp_company_nm%>&pagenm=epcadmin" class="menu-link" target="_parent">Admin Console</a></td>

	
        
            <td><img src="./assets/images/menu_bg1.jpg" width="3" height="23"></td>
            <td align="center" class="primaryNav_Off" 
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="./dist/epedigree/index.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree" class="menu-link" target="_parent">ePedigree</a></td>
            <td><img src="./assets/images/menu_bg1.jpg" width="3" height="23"></td>
            <td align="center" class="primaryNav_Off" 
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="./dist/prodRecall/index.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=recall" class="menu-link" target="_parent">Product Recall</a></td>
            <td><img src="./assets/images/menu_bg1.jpg" width="3" height="23"></td>
            <td align="center" class="primaryNav_Off" 
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="./dist/trackTrace/index.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=track" class="menu-link" target="_parent">Track and Trace</a></td>
            <td><img src="./assets/images/menu_bg1.jpg" width="3" height="23"></td>
            <% if(pagenm.equals("TPManager")) { %>

		     <td align="center" class="primaryNav_On"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="/ePharma/TradingPartnerList.do?tp_company_nm=<%=tp_company_nm%>&pagenm=TPManager" class="menu-link menuBlack" target="_parent">Trading Partner Manager</a></td>

	       <% } else { %>

		     <td align="center" class="primaryNav_Off"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="/ePharma/TradingPartnerList.do?tp_company_nm=<%=tp_company_nm%>&pagenm=TPManager" class="menu-link menuBlack" target="_parent">Trading Partner Manager</a></td>


		   	<% } %>
		   	<td><img src="./assets/images/menu_bg1.jpg" width="3" height="23"></td>
            <td align="center" class="primaryNav_Off" 
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="./dist/epedigree/ePedigree_Commissioning_Packaging.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=packaging" class="menu-link menuBlack" target="_parent">Packaging</a></td>
		   	<td><img src="./assets/images/menu_bg1.jpg" width="3" height="23"></td>
              



          </tr>
        </table></td>
    </tr>
  </table>
</div>
