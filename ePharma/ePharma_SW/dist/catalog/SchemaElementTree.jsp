<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.commons.xml.XMLUtil"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.catalog.session.TradingPartnerContext"%>
<%@ page import="com.rdta.catalog.session.CatalogContext"%>
<%@ page import="com.rdta.catalog.Constants"%>
<%@ page import="com.rdta.catalog.JavaScriptTree"%>
<%@ page import="com.rdta.Admin.Utility.Helper"%>
<%@ page import="com.rdta.tlapi.xql.Statement"%>
<%@ page import="com.rdta.tlapi.xql.Connection"%>


<%

 

		String catalogGenId = request.getParameter("catalogGenId");
		String fromModule = request.getParameter("fromModule");

		String targetUrl = request.getParameter("targetUrl");
		String display = request.getParameter("display");

		boolean isStandard = false;
		

		if(catalogGenId.startsWith("Standard")) {
				isStandard = true;
		}

		CatalogContext catalogContext = null;
		if(session != null) {
			/* if(fromModule != null && fromModule.equalsIgnoreCase("TP") ) {
				 TradingPartnerContext context = (TradingPartnerContext)session.getAttribute("TradingPartnerContext");
				 catalogContext = context.getCatalogContext();
			 } else {
				 catalogContext = (CatalogContext) session.getAttribute(Constants.SESSION_CATALOG_CONTEXT);
				 
			 }*/

			 catalogContext = (CatalogContext) session.getAttribute(Constants.SESSION_CATALOG_CONTEXT);
		} 
		
		if(catalogContext == null)
		{
			//System.out.println("Catalog context is null");
			}
		Node catalogNode = catalogContext.getCatalogNode(catalogGenId);

	//	System.out.println(" catalogNode XML String  : "  + XMLUtil.convertToString(catalogNode) );


		Node node = XMLUtil.getNode(catalogNode, "schema/*");

		String jsString = "";

		if(node == null) {
			jsString = "[ Attributes Undefined ]";
		} else {
			JavaScriptTree tree = null;
			
			if(targetUrl != null && !targetUrl.trim().equals("")) {
				targetUrl = targetUrl + "?display=" + display + "&";
				tree = new JavaScriptTree(node,catalogGenId,targetUrl);
			} else {
			  //schema edit functionality
			  tree = new JavaScriptTree(node,catalogGenId,"GCPIMMasterProduct.do?");
			  //tree = new JavaScriptTree(node,catalogGenId,"login-dist.html");
			}

			jsString = tree.toJSString();
		}

	//System.out.println(" JS TREE --- " + jsString);

%>


<TITLE>R & D - eList</TITLE>
<link href="assets/epedigree_edited.css" rel="stylesheet" type="text/css"><style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	background-color: #eeeeee;
}
-->
</style>
<body>
<script language="JavaScript" src="./js/tree.js"></script>
<script language="JavaScript" src="./js/tree_tpl.js"></script>
<br>
<table width="100%" border="0" align="left" cellpadding="7" cellspacing="1" bgcolor="#CCCCCC">
		
		<tr >

			<%
				if(isStandard) {
			%>
			<td  align="center" class="td-mapCatalogTreeTables"><FONT size="2" face="Arial" class="title" ><STRONG>Master Catalog Attributes</STRONG></FONT>			</td>

			<%
				} else {
			%>
				<td   class="td-mapCatalogTreeTables"><FONT size="2" face="Arial" class="title" >Standard Catalog Attributes</FONT>			</td>

			    <%
				}
			%>
		</tr>
			<tr bgcolor="#EEEEEE" >
			<td>
			<script language="JavaScript">
			<!--//

			var TREE_ITEMS7899 = [ [ "payload", "ManageElement.do?" , [ "amount", "ManageElement.do?", ] , [ "let1", "ManageElement.do?", ] , [ "let", "ManageElement.do?", ],],]

			var TREE_ITEMS = <%=jsString %>;

			new tree (TREE_ITEMS, TREE_TPL);
			//-->
			</script>
			</td>
			
			
		</tr>
		<tr >
		
		<tr >
			<td align="center" bgcolor="CCCCCC"><STRONG>Click on attribute to select</STRONG></td>
		</tr>
</table>
	
</body>

