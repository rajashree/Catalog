<!-- Tabs  -->


<% 

	String hideMandatory = "false";
	hideMandatory = (String)request.getAttribute("hideMandatory");
	
	if(hideMandatory == null){
		hideMandatory = request.getParameter("hideMandatory");
	}
	
	System.out.println("The value of hideMandatory is ..... "+hideMandatory);
	
	if(hideMandatory == null || hideMandatory.length() <= 0){
		hideMandatory = "false";
	}
	
	String pagenm = request.getParameter("pagenm");
	if(pagenm == null){
		pagenm = (String)request.getAttribute("pagenm");
	}
	
	String catalogName = (String)request.getAttribute("catalogName");
	if(catalogName == null){
		catalogName = (String)request.getAttribute("catalogName");
	}
	
	String genId = (String)request.getAttribute("genId");
	if(genId == null){
		genId = (String)request.getAttribute("genId");
	}
	
	String tp_company_nm = request.getParameter("tp_company_nm");
	
	String pagename = request.getParameter("pagename");
	if(pagename == null){
		pagename = (String)request.getAttribute("pagename");
	}
	
	System.out.println("pagename in tab.jsp is " + pagename);
	
%>

<div id="menu"> 
  <table width="100%" height="25" border="0" cellpadding="0" cellspacing="0">
    <tr> 
      <td valign="middle" background="./assets/images/bg_menu.jpg"><table width="800" border="0" cellpadding="0" cellspacing="0">
          <tr> 
            
            
            <% if(pagename.equals("mandatory")) { %>

		     <td align="center" class="primaryNav_On"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="AddMasterCatalogDetails.do?pagename=mandatory&pagenm=<%=pagenm%>&catalogName=<%=catalogName%>&genId=<%=genId%>&tp_company_nm=<%=tp_company_nm%>" class="menu-link menuBlack" target="_parent">Mandatory</a></td>

			<td><img src="./assets/images/menu_bg1.jpg" width="1" height="23"></td>
			<td align="center" class="primaryNav_Off" 
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="AddMasterCatalogDetails.do?pagename=optional&pagenm=<%=pagenm%>&catalogName=<%=catalogName%>&genId=<%=genId%>&tp_company_nm=<%=tp_company_nm%>" class="menu-link" target="_parent">Optional</a></td>
            <td><img src="./assets/images/menu_bg1.jpg" width="1" height="23"></td>	
	       <% } else {
	       			if(!hideMandatory.equals("true")){	       
	        %>			
			<td align="center" class="primaryNav_Off"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="AddMasterCatalogDetails.do?pagename=mandatory&pagenm=<%=pagenm%>&catalogName=<%=catalogName%>&genId=<%=genId%>&tp_company_nm=<%=tp_company_nm%>" class="menu-link" target="_parent">Mandatory</a></td>	
    		<td><img src="./assets/images/menu_bg1.jpg" width="1" height="23"></td>	
			<td align="center" class="primaryNav_On"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="AddMasterCatalogDetails.do?pagename=optional&pagenm=<%=pagenm%>&catalogName=<%=catalogName%>&genId=<%=genId%>&tp_company_nm=<%=tp_company_nm%>" class="menu-link menuBlack" target="_parent">Optional</a></td>
		   	<% 		}else{  %>
		   				<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''">Mandatory</a></td>	
    					<td><img src="./assets/images/menu_bg1.jpg" width="1" height="23"></td>	
						<td align="center" class="primaryNav_On" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="AddMasterCatalogDetails.do?hideMandatory=true&pagename=optional&pagenm=<%=pagenm%>&catalogName=<%=catalogName%>&genId=<%=genId%>&tp_company_nm=<%=tp_company_nm%>" class="menu-link menuBlack" target="_parent">Optional</a></td>
		   	
		   	<%		}
		   		}   	
		   	
		   	 %>          



          </tr>
        </table></td>
    </tr>
  </table>
</div>


