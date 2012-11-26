<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.commons.xml.XMLUtil"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.*"%>
<%@ page import="com.rdta.catalog.session.TradingPartnerContext"%>
<%@ page import="com.rdta.catalog.session.CatalogContext"%>
<%@ page import="com.rdta.catalog.Constants"%>
<%@ page import="com.rdta.catalog.JavaScriptTree"%>


<%

		String catalogGenId = request.getParameter("catalogGenId");

		System.out.println(" catalogGenId  : " + catalogGenId);
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
		
		Node catalogNode = catalogContext.getCatalogNode(catalogGenId);
		Node collectionName = XMLUtil.getNode(catalogNode,"//collectionName");
		if(XMLUtil.getValue(collectionName).equals("System"))
        {
        System.out.println(" IN THE IF CONDITION ");
        isStandard = true;
        }
		
		
		System.out.println(" catalogNode XML String  : "  + XMLUtil.convertToString(catalogNode) );
       
       
       Node node = XMLUtil.getNode(catalogNode, "schema/*");
       Node temp=null;
       if(isStandard)
       {
        System.out.println("BEFORE THE LIST ");
       /* List li =(List)session.getAttribute(Constants.STD_CAT_TREE);
        
        display="left";
         System.out.println("After THE LIST "+li.size());
        for(int k=0;k<li.size();k++)
        {
        //System.out.println("HERE IS THE LIST FROM THE SESSION"+((Node)li.get(k)));
          System.out.println("HERE IS THE LIST FROM THE SESSION"+li);
         if((li.get(k))!= null){
         System.out.println("HERE IS THE LIST FROM THE SESSION Delete"+li.get(k));
         temp=XMLUtil.getNode(node,li.get(k).toString()+"/..");
         System.out.println(" GET NODE"+temp.getNodeName());
        temp.removeChild(XMLUtil.getNode(node,li.get(k).toString()+"/."));
        }
        }*/
       }
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

