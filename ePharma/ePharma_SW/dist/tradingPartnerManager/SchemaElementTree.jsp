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


<%String tp_company_nm = request.getParameter("tp_company_nm");
    String pagenm =  request.getParameter("pagenm");
    String sessionID = (String)session.getAttribute("sessionID");
    String tpName="";
    String tpGenId="";
    
%>
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
		    TradingPartnerContext tpContext = (TradingPartnerContext)session.getAttribute(Constants.SESSION_TP_CONTEXT); 
			if(tpContext != null) {
				tpName = tpContext.getTpName();
				tpGenId = tpContext.getTpGenId();
			}
		
		
			/* if(fromModule != null && fromModule.equalsIgnoreCase("TP") ) {
			
			
			
				 TradingPartnerContext context = (TradingPartnerContext)session.getAttribute("TradingPartnerContext");
				 catalogContext = context.getCatalogContext();
			 } else {
				 catalogContext = (CatalogContext) session.getAttribute(Constants.SESSION_CATALOG_CONTEXT);
				 
			 }*/

			 catalogContext = (CatalogContext) session.getAttribute(Constants.SESSION_CATALOG_CONTEXT);
		} 
		
		Node catalogNode = catalogContext.getCatalogNode(catalogGenId);
		String catalogName=XMLUtil.getValue(catalogNode,"//catalogName");
		Node collectionName = XMLUtil.getNode(catalogNode,"//collectionName");
		if(XMLUtil.getValue(collectionName).equals("System"))
        {
        
        isStandard = true;
        }
		
		
		
       
       
       Node node = XMLUtil.getNode(catalogNode, "schema/*");
       Node temp=null;
       String man="no";
       man=(String)session.getAttribute("man");
       if(isStandard)
       {
        
       
        man="man";
       /* List li =(List)session.getAttribute(Constants.STD_CAT_TREE);
        
        display="left";
         
        for(int k=0;k<li.size();k++)
        {
        //System.out.println("HERE IS THE LIST FROM THE SESSION"+((Node)li.get(k)));
           
         if((li.get(k))!= null){
          
         temp=XMLUtil.getNode(node,li.get(k).toString()+"/..");
          
        temp.removeChild(XMLUtil.getNode(node,li.get(k).toString()+"/."));
        }
        }*/
       }
		String jsString = "";
           
		if(node == null) {
			
			jsString = "[ Attributes Undefined ]";
		} else {
			
			JavaScriptTree tree = null;

			if(targetUrl != null && !targetUrl.trim().equals("")) {
			        int x=1;
			        if(isStandard){
			        x=2;
			        }
				//in case of mapping construct tree passing different target url
				targetUrl = targetUrl + "?display=" + display + "&operation="+x+ "&"+"tp_company_nm="+tp_company_nm+"&pagenm="+pagenm+"&";
				tree = new JavaScriptTree(node,catalogGenId,targetUrl);
				
			} else {
			  //schema edit functionality
			  tree = new JavaScriptTree(node,catalogGenId,"SchemaElementEdit.do?tp_company_nm="+tp_company_nm+"&pagenm="+pagenm+"&");
			}

			jsString = tree.toJSString();
		}

	System.out.println(" JS TREE --- " + jsString);

%>

<TITLE>R & D - eList</TITLE>
<link href="assets/epedigree_edited.css" rel="stylesheet" type="text/css"><style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	background-color: #dcdcdc;
}
-->
</style><body>
<script language="JavaScript" src="./js/tree.js"></script>
<script language="JavaScript" src="./js/tree_tpl.js"></script>
<table width="100%" border="0" align="left" cellpadding="5" cellspacing="5" class="td-left">
		
		<tr >
			<%
				if(isStandard) {
			%>
			<td width="100%" align="center" class="td-mapCatalogTreeTables"><FONT size="2" face="Arial" class="title" ><a target="_top" href="/ePharma/editMasterCatalog.do?catalogGenId=<%=catalogGenId%>&operationType=edit">Master Catalog Attributes</a></FONT><br>Catalog :<%=catalogName%></td>

			<%
				} else {
			%>
				<td width="100%" align="center" class="td-mapCatalogTreeTables"><FONT color="#cc0033" size="2" face="Arial" class="title"><STRONG><a target="_top" href="/ePharma/TradingPartnerView.do?operationType=FIND&tpGenId=<%=tpGenId%>&tpName=<%=tpName%>&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>&tpList=view">Trading Partner Catalog Attributes</a></STRONG></FONT><br>Catalog :<%=catalogName%>			</td>

			<%
				}
			%>
		</tr>
		<tr bgcolor="#EEEEEE" width="300">
			<td width="57%">
			<script language="JavaScript">
			<!--//

			var TREE_ITEMS7899 = [ [ "payload", "ManageElement.do?" , [ "amount", "ManageElement.do?", ] , [ "let1", "ManageElement.do?", ] , [ "let", "ManageElement.do?", ],],]

			var TREE_ITEMS = <%=jsString %>;

			new tree (TREE_ITEMS, TREE_TPL);
			//-->
			</script>
		  </td>
			
			
		</tr>
		
		<tr class="td-left">
			<td align="center"><STRONG class="td-left"> Click on attribute to select </STRONG></td>
	  </tr>
</table>
    </body>

