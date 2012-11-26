

<%@ page import="com.rdta.catalog.trading.model.ProductMaster"%>
<%@ page import="java.util.List" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.commons.xml.XMLUtil"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="com.rdta.commons.CommonUtil"%>
<html>

<head>

<title>Raining Data ePharma - Trading Partner Manager</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="./assets/epedigree1.css" rel="stylesheet" type="text/css">

</head>

<body>

<jsp:include page="../globalIncludes/TopHeader.jsp" />
<jsp:include page="../globalIncludes/TopLevelLayer1.jsp" />
<jsp:include page="../globalIncludes/TopLevelLayer2.jsp" />
<jsp:include page="../catalog/LeftNav.jsp" />
<div id="rightwhite">

<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
  <tr> 
   <!-- Messaging -->
   
  </tr>
  <tr>
  </tr>
  <tr> 
    <td>&nbsp;</td>
    <td><table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr> 
          <td><img src="./assets/images/space.gif" width="30" height="12"></td>
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
              <table width="100%" cellSpacing="0" cellPadding="0" align="left" border="0"
			bgcolor="white">
                <tr bgcolor="white"> 
                 <!-- <td class="td-typeblack">List of  Catalogs of the Trading Partner : </td>-->
	               <td><B>Select a Master Catalog to Map</B></td>
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
						List list = (List) request.getAttribute("mastercatalogList");
						String name = "";
						
						String description = "";
						String updatedDate = "";
						String genId="";
				

						for(int i=0; i < list.size(); i++) {
					
							Node catalogNode = XMLUtil.parse((InputStream) list.get(i) );
							name = CommonUtil.jspDisplayValue(catalogNode,"name");
							genId= CommonUtil.jspDisplayValue(catalogNode,"genId");
							description = CommonUtil.jspDisplayValue(catalogNode,"description");
							updatedDate = CommonUtil.jspDisplayValue(catalogNode,"updatedDate");

			   %>
				<tr class="tableRow_On">
					<td><a href="CreateStandardCatalogSchemaFromFile.do?catalogGenId=<%=genId%>&operationType=arun
					"><%= name %></a></td>
					<td><a href="xyz"><%= description %></a></td>
					<td><a href="xyz"><%= updatedDate %></a></td>
				</tr>
				<%
				}
				%>		

</table>
</table>
 
<jsp:include page="../includes/Footer.jsp" />
</div>




</body>

</html>