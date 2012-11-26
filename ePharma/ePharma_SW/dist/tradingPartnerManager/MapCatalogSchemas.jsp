
<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>


<%String tp_company_nm = request.getParameter("tp_company_nm");
    String pagenm =  request.getParameter("pagenm");
    String sessionID = (String)session.getAttribute("sessionID");
%>

<html>
<head>
<title>Attribute Mapping Manager</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link href="../../assets/epedigree_edited.css" rel="stylesheet" type="text/css" />
</head>
<frameset border="0" rows="75,*" >
    <frame scrolling=no name="top" target="_self" src="SchemaElementNewTop.do?tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>"/>

<frameset border="0" cols="192,*,200">
<frame NORESIZE scrolling=auto name="Tomenu" src="SchemaElementTree.do?targetUrl=CatalogMappingEdit.do&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>&catalogGenId=<%=request.getParameter("rightCatalogGenId") %>&standardCatalogGenId=<%=request.getParameter("rightCatalogGenId") %>&display=left">
	
<frame NORESIZE scrolling=auto name="main" src="CatalogMappingEdit.do?&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>&operationType=<%= request.getParameter("operationType") %>&catalogGenId=<%=request.getParameter("leftCatalogGenId") %>&standardCatalogGenId=<%=request.getParameter("rightCatalogGenId") %>">
<frame NORESIZE scrolling=auto name="Frommenu" src="SchemaElementTree.do?targetUrl=CatalogMappingEdit.do&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>&catalogGenId=<%=request.getParameter("leftCatalogGenId") %>&display=right">
</frameset>
</frameset><noframes></noframes>
</html>