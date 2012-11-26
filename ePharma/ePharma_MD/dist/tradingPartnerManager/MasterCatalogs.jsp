<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.commons.xml.XMLUtil"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.catalog.session.TradingPartnerContext"%>
<%@ page import="com.rdta.catalog.Constants"%>


<%String tp_company_nm = request.getParameter("tp_company_nm");
    String pagenm =  request.getParameter("pagenm");
    String sessionID = (String)session.getAttribute("sessionID");
%>

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

<%@include file='../epedigree/topMenu.jsp'%>


<!--
<%

    TradingPartnerContext context = (TradingPartnerContext)session.getAttribute(Constants.SESSION_TP_CONTEXT); 
    String tpName = context.getTpName();
    String tpGenId = context.getTpGenId();
%>
-->


<%
	String leftCatalogGenId=(String)request.getParameter("leftCatalogGenId");
	 
%>	
	
<div id="rightwhite2">
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
                 <!-- <td class="td-typeblack">List of  Catalogs of the Trading Partner :<%=tpName%> </td>-->
	               <td><B>Select a Master Catalog to Map</B></td>
                </tr>
                <tr> 
                  <td align="center">
					<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">				
					<tr class="tableRow_Header">
					<td class="type-whrite">Name</td>
					<td class="type-whrite">Description</td>
					<td class="type-whrite">Updated Date</td>
					
							

				</tr>


				<%
					List list =  (List)request.getAttribute("StandardCatalogListInfo");

					String name = "";
					String genId = "";
					String description = "";
					String updatedDate = "";
				

					for(int i=0; i < list.size(); i++) {
					
						Node catalogNode = XMLUtil.parse((InputStream) list.get(i) );

						genId = CommonUtil.jspDisplayValue(catalogNode,"catalogID");

						name = CommonUtil.jspDisplayValue(catalogNode,"catalogName");
						description = CommonUtil.jspDisplayValue(catalogNode,"description");

						updatedDate = CommonUtil.jspDisplayValue(catalogNode,"//updated-timestamp");

			   %>


				<tr class="tableRow_On">
					<!--<td><a href="Catalog.do?operationType=FIND&catalogGenId=<%= genId%>" class="typered-bold-link">-->
					<td><a href="MapCatalogSchemas.do?leftCatalogGenId=<%=leftCatalogGenId%>&rightCatalogGenId=<%= genId%>&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>">	<%= name %> </a>
					</td>
					<td><%= description %></td>
					<td><%= updatedDate %></td>
					<!--<td><a href="OpenCatalogSchemaDef.do?fromModule=TP&catalogGenId=<%= genId%>" class="typered-bold-link">
					Schema Def</a>  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;-->
					
					<!--<a href="MapCatalogSchemas.do?leftCatalogGenId= <%=leftCatalogGenId%>&rightCatalogGenId=<%= genId%>
					">Select Standard Catalog to Map Attributes  </a>-->
					
					
					<!--Here Iam making changes--> 
                    <!--      
                         <a href="SelectMasterCatelog.do?leftCatalogGenId=<%= genId%>&rightCatalogGenId=StandardGenId1000 
                         ">Map With Standrad Catelog</a> 
                    -->   
					

					
	
				</tr>

                <%

					}
                 %>
			
				</TABLE>
            
        </tr>
      </table></div>



<jsp:include page="../globalIncludes/Footer.jsp" />


</body>
</html>