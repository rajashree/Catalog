
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
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.commons.persistence.QueryRunner"%>
<%@ page import="com.rdta.commons.persistence.QueryRunnerFactory"%>

<TITLE>R & D - eList</TITLE>
<HEAD>

<%String tp_company_nm = request.getParameter("tp_company_nm");
    String pagenm =  request.getParameter("pagenm");
    String sessionID = (String)session.getAttribute("sessionID");
%>
<title>Raining Data ePharma - Trading Partner Manager -  Detail</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="./assets/epedigree1.css" rel="stylesheet" type="text/css">

<SCRIPT>

function redirect() {
	window.location = 'ManageElement.do?tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>&catalogGenID=""';
}

function submitform(operation)
{
	if(CatalogUpdatePrivilage())
	{
	document.updateAndDelete.operationType.value = operation;

	//alert(document.updateAndDelete.operationType.value );
	document.updateAndDelete.submit();

	return t;}
	else{
	alert("Access Denied...!")
	}
}


function goback(targetUrl)
{

	document.updateAndDelete.operationType.value =  "FIND";
    document.updateAndDelete.action = targetUrl;
	document.updateAndDelete.target="_top";
	document.updateAndDelete.submit();

	return true;
}
 
function CatalogUpdatePrivilage()
{

<%
 	QueryRunner queryrunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
        List accessList = queryrunner.returnExecuteQueryStrings("tlsp:validateAccess('"+sessionID+"','4.03','Update')");
		String updateStatus = accessList.get(0).toString();
		System.out.println("The Update is "+updateStatus);
       if(updateStatus.equals("false"))
       {
%>
 
return false;
<%}
if(updateStatus.equals("true")){%>
return true;
<%}
 %>

}
 
	
</SCRIPT>
</HEAD>


<%

		String tpName = "";
		String tpGenId  = "";

		boolean isStandard = false;
		String catalogGenId = request.getParameter("catalogGenId");

		if(catalogGenId.startsWith("Standard")) {
				isStandard = true;
		}

		CatalogContext catalogContext = null;
		if(session != null) {
			catalogContext = (CatalogContext) session.getAttribute(Constants.SESSION_CATALOG_CONTEXT);

		   TradingPartnerContext tpContext =(TradingPartnerContext)session.getAttribute(Constants.SESSION_TP_CONTEXT); 
			if(tpContext != null) {
				tpName = tpContext.getTpName();
				tpGenId = tpContext.getTpGenId();
			}
		} 
		

	Node catalogNode = catalogContext.getCatalogNode(catalogGenId);
		


	String values = "";
	String currSelectionXpath = request.getParameter("xpath") ;

	String currSelectionNodeName = "";

	//is it root element selection
	boolean rootSelection = true;
	Node node = XMLUtil.getNode(catalogNode, "schema/*");
	if(node != null ) {
		if(currSelectionXpath == null ) {
			currSelectionXpath = node.getNodeName();
			currSelectionNodeName = node.getNodeName();
		} else {
			 StringBuffer buff = new StringBuffer(currSelectionXpath);
			 int index = buff.lastIndexOf("/");
			 if(index > 0) {
				rootSelection =	false;
				currSelectionNodeName = currSelectionXpath.substring(index+1, currSelectionXpath.length());
			 }
		}

	} else {
		//if there is no schema under some element
		//then make it empty
		currSelectionXpath = "NEW TREE CREATION";
	}


    //get the values info
	if(!rootSelection) {
		Node schemaNode = XMLUtil.getNode(catalogNode, "schema");
		if(schemaNode != null) {
			values = XMLUtil.getValue(schemaNode,currSelectionXpath+"/@values");
			if(values ==null)
				values ="";
		}
	}

%>


<body>
	<table align="center" border="1" cellspacing="1" cellpadding="1" width="90%">
		 <br>
		<tr bgcolor="white">
			<td align="center"><FONT face="Arial" color="#cc0033" size="2"><STRONG>Selected Attribute</STRONG></FONT></td>
		</tr>
		<tr bgcolor="D3E5ED">
			<td>
				<br>
				
				<form name= "updateAndDelete" action="UpdateSchemaElementTree.do?tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>" method="post" target="_top"  >

					<input type="hidden" name="catalogGenId" value="<%=request.getParameter("catalogGenId") %>">
					<input type="hidden" name="xpath" value="<%= currSelectionXpath %>">
					<input type="hidden" name="operationType" value="">
					<INPUT id="hidden10" type="hidden" name="tpGenId" value="<%=tpGenId%>">
					
					<table align="center" cellSpacing="1" cellPadding="3" >

						<tr class="titleBlack">
							<td  colspan="4"> <b>Selected Attribute Location : <%= currSelectionXpath%> </b></td>
						</tr>
						<tr>

							<TD>Attribute Name:</TD>
							<td colspan="3"><input type="text" name="name" value="<%=currSelectionNodeName %>" size='30'></td>
						</tr>

					<% if(!rootSelection) {%>
						<tr>
							<TD>**Attribute Values:</TD>
							<td colspan="3" ><TEXTAREA id="Textarea1" name="values" rows="3" cols="60"><%= values %></TEXTAREA></td>
						</tr>

						<tr>
							<TD colspan="4">**Note :Attribute values should be comma separated.</TD>
						</tr>

						<% } %>
						
						<tr>
							

							<td colspan="4">

							<%
								if(isStandard ) {
							%>
									<input type="button"  value="Go Back" onClick="return goback('ProductMasterSearchEmpty.do?tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>')">

							<%
								} else {

							%>
								<input type="button"  value="Go Back" onClick="return goback('TPCatalogList.do?tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>&tpList=view&tcList=view')">

							<%
								}
							%>

							<input type="button" value="ADD" onClick="return submitform('ADD')">
							
							<%	if(node != null ) { %>

							 <input type="button" value="Update" onClick="return submitform('UPDATE')">

							<input type="button" value="Delete" onClick="return submitform('DELETE')">

							<input type="button"  value="Save Tree" onClick="return submitform('SAVE')">
							
							<% } 	%>										
						</td>
						</tr>

						

					</table>
				</form>
			</td>
			
			
		</tr>
		
	</table>
	
	
</body>

