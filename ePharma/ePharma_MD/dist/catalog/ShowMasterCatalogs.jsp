<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.List" %>
<%@ page import="com.rdta.catalog.Constants" %>
<%
	
	String tp_company_nm = request.getParameter("tp_company_nm");	
	if(tp_company_nm == null){
		tp_company_nm = (String)request.getAttribute("tp_company_nm");
		if(tp_company_nm == null) tp_company_nm="";
	}
	String pagenm = request.getParameter("pagenm");
	if(pagenm == null){
		pagenm = (String)request.getAttribute("pagenm");
		pagenm="Catalog";
	}	
	String sessionID = (String)session.getAttribute("sessionID");
	
	String createProduct = (String)request.getAttribute("createProduct");
	System.out.println("*****The createProduct in ShowMasterCatalogs.jsp is "+createProduct);
	
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Raining Data ePharma - GCPIM</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="./assets/epedigree1.css" rel="stylesheet" type="text/css">

</head>
<SCRIPT language='javascript'>
function AccessCheck(){
	var createProduct = "<%=(String)request.getAttribute("createProduct")%>";
	if( createProduct == "false" ){
		alert("Access Denied.....");
		return false;
	}
	return true;
}	

function showList(){

	var elements = MasterList.elements;
	var elementsLength = elements.length;
	for (var i = 0; i < elementsLength; i++) {
			if(elements[i].type == "radio" ){
				if(elements[i].checked){
					document.MasterList.catalogName.value =elements[i].value;
					document.MasterList.submit();
				}
			
			}
	}
	
		return true;
}




</script>
<body><form name="MasterList" action="DisplayProducts.do"   method="post" >
<%@include file='../epedigree/topMenu.jsp'%>
<input type = 'hidden' name='catalogName' value='' />
<div id="rightwhite">
       
<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
  <tr> 
    <td width="99%" valign="top" class="td-rightmenu" style="height: 0px;">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td align="left"></td>
			<td align="right"><img src="assets/images/space.gif" width="5"></td>
		</tr>
	</table>
    </td>
 </tr>
  
  <tr> 
    </div>
  
        <!-- Breadcrumb -->
            
	        <table width="100%" cellSpacing="0" cellPadding="0" align="left" border="0"
			bgcolor="white">
                <tr bgcolor="white"> 
                  <td class="td-typeblack">List of Master Catalogs:
                  Click on any of the Master Catalogs to Enter the Product Details</td>
                </tr>
                <tr> 
                  <td align="left">
					<TABLE cellSpacing="1" cellPadding="4" border="0" width="100%">				
					<tr class="tableRow_Header">
					<td class="type-whrite">Select</td>
					<td class="type-whrite">Name</td>
					<td class="type-whrite">Description</td>
					<td class="type-whrite">Updated Date</td>
				</tr>	
				<%
					List list = (List)request.getAttribute("List"); 
					System.out.println("The List object from ShowMasterCatalogsAction in ShowMasterCatalogs JSP is:"+list.toString()); 

			    %>
			   
			   <% if(list != null & list.size() > 0) { %>
			   <logic:iterate name="<%=Constants.MASTER_CATALOG_NAMES%>" id="masterCatalogName">
			   	<tr class="tableRow_Off">
					
					<td>
					<input type='radio' name='scatlog' value= <bean:write name="masterCatalogName" property="masterCatalogNames"/> >
					</td>	
					<td class='td-content'>
						<% if(createProduct.equals("true")){ %>
						 	<a onClick='return AccessCheck();' href="AddMasterCatalogDetails.do?tp_company_nm=<%=tp_company_nm%>&pagename=mandatory&pagenm=Catalog&catalogName=<bean:write name="masterCatalogName" property="masterCatalogNames"/>"><bean:write name="masterCatalogName" property="masterCatalogNames"/></a>
					 	<% }else { %>
					 		<a onClick='return AccessCheck();' href=""><bean:write name="masterCatalogName" property="masterCatalogNames"/></a>
					 	<% } %>
					</td>
					<td>
						<bean:write name="masterCatalogName" property="description"/>
					</td>
					<td>
						<bean:write name="masterCatalogName" property="updatedDate"/>
					</td>
			   	</logic:iterate>	
				</tr>
				<TD colspan="4" align="center">
					<input type='button' align="center" name="display" value="Display Product" onClick="return showList()" \>
          	</TD>
				<% }else {%>
				
				<TR class='tableRow_Off'><TD class='td-content' colspan='9' align='center'>There are no Master Catalogs...</TD></TR>
				
				<% } %>
				
				
				</TABLE>
			 </td>
        </tr>  
</table>
<jsp:include page="../globalIncludes/Footer.jsp" />
</form>
</body>
</html>




