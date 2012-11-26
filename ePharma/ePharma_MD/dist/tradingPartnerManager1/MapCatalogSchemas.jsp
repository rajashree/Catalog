
<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>



<html>
<title>Attribute Mapping Manager</title>
<frameset border="1" rows="95,*" >
    <frame scrolling=auto name="top" src="./SchemaElementNewTop.do"/>

<frameset border="1" cols="230,*, 230">
<frame scrolling=auto name="Tomenu" src="./SchemaElementTree.do?targetUrl=CatalogMappingEdit.do&catalogGenId=<%=request.getParameter("rightCatalogGenId") %>&standardCatalogGenId=<%=request.getParameter("rightCatalogGenId") %>&display=left">
	
<frame scrolling=auto name="main" src="./CatalogMappingEdit.do?operationType=<%= request.getParameter("operationType") %>&catalogGenId=<%=request.getParameter("leftCatalogGenId") %>&standardCatalogGenId=<%=request.getParameter("rightCatalogGenId") %>">
<frame scrolling=auto name="Frommenu" src="./SchemaElementTree.do?targetUrl=CatalogMappingEdit.do&catalogGenId=<%=request.getParameter("leftCatalogGenId") %>&display=right">
</frameset>
</frameset>
</html>