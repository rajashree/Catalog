

<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.commons.xml.XMLUtil"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>



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

<%String tp_company_nm = request.getParameter("tp_company_nm");
    String pagenm =  request.getParameter("pagenm");
    String sessionID = (String)session.getAttribute("sessionID");
    String tpGenId=(String)request.getParameter("tpGenId");
%>
<%@include file='../epedigree/topMenu.jsp'%>

<div id="rightwhite2">
<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
  <tr> 
    <td width="100%" valign="middle" class="td-rightmenu"><img src="./assets/images/space.gif" width="10" height="10"></td>
    <!-- Messaging -->
    <td width="0%" valign="middle" class="td-rightmenu">
	
			 
	  </td>
  </tr>
<table width="100%" cellSpacing="0" cellPadding="0" align="left" border="0"
			bgcolor="white">
                <tr bgcolor="white" align="center"> 
                  <td class="td-typeblack">Catalog value Mapping Summary</td>
                </tr>
                <tr> 
                  <td align="left">
					<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">				
					<tr class="tableRow_Header">
					<td class="type-whrite">Successfully Mapped</td>
					<td class="type-whrite">Exceptions</td>
				</tr>
                <tr>
                <td><%=session.getAttribute("mapped") %> </td>
                <td><%=session.getAttribute("exceptions") %> </td>
                </tr>
                <tr>
                <td><a href="ViewMappedDetails.do?tpGenId=<%=tpGenId%>&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>&tpList=view&tcList=view" target="_blank">View Details</a> </td>
                <td><a href="ViewExceptionDetails.do?tpGenId=<%=tpGenId%>&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>&tpList=view&tcList=view" target="_blank">View Details</a></td>
                </tr>
                
                 <tr>
                <td><input type="button" name="cancel" value="Cancel" onClick="window.location='Catalog.do?tpGenId=<%=tpGenId%>&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>&operationType=FIND&catalogGenId=<%=session.getAttribute("catalogGenId")%>&tpList=view&tcList=view&catSchema=view'"/> </td>
                <td><input type="button" name="cancel" value="Save As Mapped" onClick="window.location='SaveMappingValues.do?tpGenId=<%=tpGenId%>&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>&tpList=view&tcList=view'"/></td>
                </tr>
                
      </table></div>

<% 
session.setAttribute("standardCatalogId",session.getAttribute("standardCatalogId"));

%>

<jsp:include page="../globalIncludes/Footer.jsp" />


</body>
</html>
