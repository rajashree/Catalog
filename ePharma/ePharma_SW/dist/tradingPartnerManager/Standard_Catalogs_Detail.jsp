<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.commons.xml.XMLUtil"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.catalog.session.TradingPartnerContext"%>
<%@ page import="com.rdta.catalog.Constants"%>



<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Raining Data ePharma - Trading Partner Manager</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="./assets/epedigree1.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}


//-->
</script>
</head>
<body>

<jsp:include page="../globalIncludes/TopHeader.jsp" />
<jsp:include page="../globalIncludes/TopLevelLayer1.jsp" />
<jsp:include page="../globalIncludes/TopLevelLayer2.jsp" />
<jsp:include page="./LeftNav.jsp" />

<%	TradingPartnerContext context = (TradingPartnerContext)session.getAttribute(Constants.SESSION_TP_CONTEXT); 
    System.out.println("Context Object *********"+context);
   // String tpName = context.getTpName();
   // String tpGenId = context.getTpGenId();
  
%>
<div id="rightwhite">
<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
  <tr> 
    <td width="1%" valign="middle" class="td-rightmenu"><img src="./assets/images/space.gif" width="10" height="10"></td>
    <!-- Messaging -->
    <td width="99%" valign="middle" class="td-rightmenu">
	<jsp:include page="./TPHeader.jsp" />
	</td>
  </tr>
  
  <tr> 
    <td>&nbsp;</td>
    <td><table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr> 
          <td><img src="./assets/images/space.gif" width="30" height="12"></td>
          <td rowspan="2">&nbsp;</td>
        </tr>
        </table></div>
  
  
  
  
  
  
        <!-- Breadcrumb -->
        <!-- <tr> 
            <td><a href="#" class="typegray1-link">Home</a><span class="td-typegray"> 
              - </span><a href="#" class="typegray1-link">ePedigree</a></td>
          </tr> -->
            <!-- info goes here -->
           
           
           
           
           
	        <table width="100%" cellSpacing="0" cellPadding="0" align="left" border="0"
			bgcolor="white">
                <tr bgcolor="white"> 
                  <td class="td-typeblack">List of Standard Catalogs of the Trading Partner : </td>
					
				  <td><a href="StandardCatalogNew.do" class="typered-bold-link">Create New Standard Catalog</a></td>
                </tr>
                <tr> 
                  <td align="left">
					<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">				
					<tr class="tableRow_Header">
					<td class="type-whrite">Name</td>
					<td class="type-whrite">Description</td>
					<td class="type-whrite">Updated Date</td>

							

				</tr>


				
				
				<!--Here the problem is existing-->
				
				<%
					List list =  (List)request.getAttribute("StandardCatalogListInfo");

					String name = "";
					String genId = "";
					String description = "";
					String updatedDate = "";
				

					for(int i=0; i < list.size(); i++) {
					
						Node catalogNode = XMLUtil.parse((InputStream) list.get(i) );

						genId = CommonUtil.jspDisplayValue(catalogNode,"genId");

						name = CommonUtil.jspDisplayValue(catalogNode,"name");
						description = CommonUtil.jspDisplayValue(catalogNode,"description");

						updatedDate = CommonUtil.jspDisplayValue(catalogNode,"updatedDate");

			   %>

				<tr class="tableRow_On">
					<td><a href="StandardCatalog.do?operationType=FIND&catalogGenId=<%= genId%>" class="typered-bold-link">
					<%= name %></a></td>
					<td><%= description %></td>
					<td><%= updatedDate %></td>
					<!--
					<td><a href="OpenCatalogSchemaDef.do?fromModule=TP&catalogGenId=<%= genId%>" class="typered-bold-link">
					Schema Def</a>  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
					<a href="MapCatalogSchemas.do?leftCatalogGenId=<%= genId%>&rightCatalogGenId=StandardGenId1000
					">Map With Standrad Schema</a></td>
					-->
					<!--Here Iam making changes-->
					<!--
					<a href="SelectMasterCatalog.do?operationType=FIND&leftCatalogGenId=<%= genId%>&rightCatalogGenId=StandardGenId1000
					">Map With Standrad Catelog</a>
					</td>-->

					
	
				</tr>
				
				
                <%

					}
                 %>
				
				
				
			
				</TABLE>
           
           
           
            </td>
        </tr>
  
  


<jsp:include page="../globalIncludes/Footer.jsp" />


</body>
</html>




