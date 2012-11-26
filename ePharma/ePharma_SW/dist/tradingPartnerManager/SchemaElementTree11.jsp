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

<%@ page import="org.w3c.dom.NodeList"%>
<%@ page import="com.rdta.util.config.DomCompare"%>
<%

		String catalogGenId = request.getParameter("catalogGenId");

		System.out.println(" catalogGenId  : " + catalogGenId);
		String fromModule = request.getParameter("fromModule");

		String targetUrl = request.getParameter("targetUrl");
		String display = request.getParameter("display");
        String standardCatalogGenId = request.getParameter("standardCatalogGenId");
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
		
		Node catalogNode = catalogContext.getCatalogNode(catalogGenId);
		Node standardCatalogNode= catalogContext.getCatalogNode(standardCatalogGenId);
		
        System.out.println(" standardCatalogNode XML String  : "  + XMLUtil.convertToString(standardCatalogNode));
		System.out.println(" catalogNode XML String  : "  + XMLUtil.convertToString(catalogNode) );
       
        Node collectionName = XMLUtil.getNode(catalogNode,"//collectionName");
        
      
        System.out.println("Before IF CONDITION ");
        if(XMLUtil.getValue(collectionName).equals("System"))
        {
        System.out.println(" IN THE IF CONDITION ");
        	isStandard = true;
        }
        
         System.out.println(" After IF CONDITION" );
      
		Node node = XMLUtil.getNode(catalogNode, "schema/*");
        Node standardNode= XMLUtil.getNode(standardCatalogNode,"schema/*");
    
        NodeList standardList = standardNode.getChildNodes();
         NodeList tradingPartnerList = node.getChildNodes();
        System.out.println("Standard Node********After NOdeList");
        
         System.out.println("Standard NodeLIST SIZE********"+standardList.getLength());
        StringBuffer newTradingPartner = new StringBuffer("<root>");
        System.out.println("Before FOR LOOP in DOMCOMPARE*******");
        DomCompare dc = new DomCompare();
       for(int i=0;i<standardList.getLength();i++)
		{
		System.out.println(" IN LOOP in DOMCOMPARE*******");
		
		if(tradingPartnerList.item(i).hasChildNodes())
		{
		NodeList templist= tradingPartnerList.item(i).getChildNodes();
		for(int t=0;t<templist.getLength();t++)
		{
		
		System.out.println("INSIDE CHILD FIND MATCH RESULT"+XMLUtil.convertToString(dc.findMatch(standardList,templist.item(t)),true));
	    newTradingPartner.append(XMLUtil.convertToString(dc.findMatch(standardList,templist.item(t)),true));
				
		}
		
		System.out.println("IN SIDE IF CONDITION");
		}
		else
		{
		
		System.out.println("FIND MATCH RESULT"+XMLUtil.convertToString(dc.findMatch(standardList,tradingPartnerList.item(i)),true));
	    newTradingPartner.append(XMLUtil.convertToString(dc.findMatch(standardList,tradingPartnerList.item(i)),true));
		} 
		  } 
       newTradingPartner.append("</root>");
       
        System.out.println("After FOR LOOP  DOMCOMPARE*******" +newTradingPartner.toString());
        
		String jsString = "";

		if(node == null) {
			System.out.println(" Node schema is null");
			jsString = "[ Attributes Undefined ]";
		} else {
			System.out.println(" Node schema is not null");
			JavaScriptTree tree = null;

			if(targetUrl != null && !targetUrl.trim().equals("")) {
				//in case of mapping construct tree passing different target url
				targetUrl = targetUrl + "?display=" + display + "&";
				tree = new JavaScriptTree(node,catalogGenId,targetUrl);
			} else {
			  //schema edit functionality
			  tree = new JavaScriptTree(node,catalogGenId,"SchemaElementEdit.do?");
			}

			jsString = tree.toJSString();
		}

	System.out.println(" JS TREE --- " + jsString);

%>


<TITLE>R & D - eList</TITLE>

<body>
<script language="JavaScript" src="./js/tree.js"></script>
<script language="JavaScript" src="./js/tree_tpl.js"></script>
	<table align="center" border="1" cellspacing="1" cellpadding="1" bgColor="459EBE">
		
		<tr bgcolor="white">

			<%
				if(isStandard) {
			%>
			<td align="center"><FONT face="Arial" color="#cc0033" size="2"><STRONG>Master Catalog Attributes</STRONG></FONT>
			</td>

			<%
				} else {
			%>
				<td align="center"><FONT face="Arial" color="#cc0033" size="2"><STRONG>Trading Partner Catalog Attributes</STRONG></FONT>
			</td>

			<%
				}
			%>
		</tr>
		<tr bgcolor="D3E5ED">
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
		<tr bgcolor="white">
					<td align="center"> &nbsp;</td>
		</tr>
		<tr bgcolor="white">
			<td align="center"><STRONG>Click on attribute to select</STRONG></td>
		</tr>
	</table>
	
</body>

