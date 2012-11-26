

<%@ page import="com.rdta.catalog.trading.model.ProductMaster"%>
<%@ page import="java.util.List" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.commons.xml.XMLUtil"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="com.rdta.commons.CommonUtil"%>
<html>

<head>

<title>Raining Data ePharma - GCPIM </title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="./assets/epedigree1.css" rel="stylesheet" type="text/css">

</head>
<SCRIPT language='javascript'>
function checkAccess(){

	var i = "<%= (String )request.getAttribute("editRead") %>";
	

	if( i == "No" ){
		alert("Access Denied !..");
		return false;
		}
return true;
}
</SCRIPT>



<body>
<%
	
	String tp_company_nm = request.getParameter("tp_company_nm");
	System.out.println("TpcompanyName ="+tp_company_nm);
	
	String pagenm = request.getParameter("pagenm");
	System.out.println("PageName "+pagenm);
	String sessionID = (String)session.getAttribute("sessionID");
	System.out.println("SessionID "+sessionID);
%>

<%@include file='../epedigree/topMenu.jsp'%>
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
</table>
  
  
        <!-- Breadcrumb -->
        
        <tr> 
          <td> 
            <!-- info goes here -->
              <table width="100%" cellSpacing="0" cellPadding="0" align="left" border="0"
			bgcolor="white">
				<tr><td>&nbsp;</td></tr>
                <tr bgcolor="white">                    
	               <td><B>Select a Catalog to Edit</B></td>
                </tr>
                <tr> 
                  <td align="left">
					<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">				
					<tr class="tableRow_Header">
					<td class="type-whrite">Name</td>
					<td class="type-whrite">Description</td>
					<td class="type-whrite">Updated Date</td>
				</tr>
				
				<%
					System.out.println("coming here ");
						List list = (List) request.getAttribute("mastercatalogList");
						String name = "";
						
						String description = "";
						String updatedDate = "";
						String genId="";
						
						System.out.println("list size   :"+list.size());
						if (list != null ){
						int size = list.size();
						if ( size != 0 ){
						for(int i=0; i < size; i++) {
					
							Node catalogNode = XMLUtil.parse((InputStream) list.get(i) );
							name = CommonUtil.jspDisplayValue(catalogNode,"catalogName");
							genId= CommonUtil.jspDisplayValue(catalogNode,"catalogID");
							description = CommonUtil.jspDisplayValue(catalogNode,"description");
							updatedDate = CommonUtil.jspDisplayValue(catalogNode,"//updated-timestamp");

			   %>
				<tr class="tableRow_On">
					<% String str= (String) request.getAttribute("editRead");
						if(str == null ) str="";
					if(str.equals("No")){%>
					<td><a onClick = "return checkAccess();" href=""><%= name%></a></td>
					<%}else {%>	
					<td><a onClick = "return checkAccess();" href="editMasterCatalog.do?catalogGenId=<%=genId%>&operationType=edit
					"><%= name %></a></td>
					<%}%>
					<td><%= description %></td>
					<td><%= updatedDate %></td>
				</tr>
				<%
				}
				}else{%>
						<tr class="tableRow_On"><td colspan='3'   align='center'>There are no catalogs.... </td> </tr>
				<%}
				}
				%>		

			</table>

		</table>
<jsp:include page="../globalIncludes/Footer.jsp" />
</div>
</table>



</body>

</html>