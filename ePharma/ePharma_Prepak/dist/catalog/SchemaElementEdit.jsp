
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

<TITLE>R & D - eList</TITLE>
<HEAD>

<title>Raining Data ePharma - Trading Partner Manager -  Detail</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">


<%  System.out.println("SAVE TREE OPTION IS = "+request.getParameter("savetree")); %>

<SCRIPT>

function redirect() {
	window.location = 'ManageElement.do?catalogGenID=""';
}

function submitform(operation)
{
	document.updateAndDelete.operationType.value = operation;
	if(document.updateAndDelete.operationType.value == "Help"){
	
			document.updateAndDelete.submit();
			return true;
	}
	var opt="<%=(String) session.getAttribute("optype")%>";
	
	
	if(opt=="manage"){
	if ( document.updateAndDelete.operationType.value == "SAVE" ){
				var i ="<%= (String) session.getAttribute("manageUpdate")%>";
				if( i == "false")
				{
					alert("Access Denied !..");
					return false;
				}
				document.updateAndDelete.submit();
				return true;
	
	
	}
	}else{

	if ( document.updateAndDelete.operationType.value == "SAVE" ){
				var i ="<%= (String) session.getAttribute("updateStatus")%>";
				if( i == "false")
				{
					alert("Access Denied !..");
					return false;
				}
				
		document.updateAndDelete.submit();
		return true;
	}
	}
				
	if ( document.updateAndDelete.displayOrder.value == ""){
				alert("Enter Display Order");
				return false;
	}
	if( document.updateAndDelete.name.value == "" )
	{	alert("Attribute Name must not be empty");
		return false;
	}

				
		if(opt == "manage"){
					if(document.updateAndDelete.operationType.value == "ADD"){
				var i ="<%= (String) session.getAttribute("manageRead")%>";
				if( i == "false")
				{
					alert("Access Denied !..");
					return false;
				}
			}else if ( document.updateAndDelete.operationType.value == "UPDATE" ){
			
				var i ="<%= (String) session.getAttribute("manageUpdate")%>";
				if( i == "false")
				{
					alert("Access Denied !..");
					return false;
				}
			
			}else if ( document.updateAndDelete.operationType.value == "DELETE" ){
			
				var i ="<%= (String) session.getAttribute("manageDelete")%>";
				if( i == "false")
				{
					alert("Access Denied !..");
					return false;
				}
			
			}else if ( document.updateAndDelete.operationType.value == "SAVE" ){
				var i ="<%= (String) session.getAttribute("manageUpdate")%>";
				if( i == "false")
				{
					alert("Access Denied !..");
					return false;
				}
			
			}
				
				
				
				
		}else if( opt = "edit" ){
		
				
				if(document.updateAndDelete.operationType.value == "ADD"){
				var i ="<%= (String) session.getAttribute("insertStatus")%>";
				if( i == "false")
				{
					alert("Access Denied !..");
					return false;
				}
			}else if ( document.updateAndDelete.operationType.value == "UPDATE" ){
			
				var i ="<%= (String) session.getAttribute("updateStatus")%>";
				
				if( i == "false")
				{
					alert("Access Denied !..");
					return false;
				}
			
			}else if ( document.updateAndDelete.operationType.value == "DELETE" ){
			
				var i ="<%= (String) session.getAttribute("deleteStatus")%>";
				if( i == "false")
				{
					alert("Access Denied !..");
					return false;
				}
			
			}else if ( document.updateAndDelete.operationType.value == "SAVE" ){
				
				var i ="<%= (String) session.getAttribute("updateStatus")%>";
				if( i == "false")
				{
					alert("Access Denied !..");
					return false;
				}
			
			}
	}
	
		document.updateAndDelete.submit();
			
	return true;
	
}





function goback(targetUrl)
{
		var tree ="<%=(String) session.getAttribute("savetree")%>";
		var catalog="<%= request.getParameter("catalogGenId")%>";
		document.updateAndDelete.targetUrl.value = targetUrl;
		
		if(tree == "true"){
					
			var answer =confirm("Save the tree before exiting?");
					
					if(answer){
					
					<% System.out.println(" GO ING TO CALL GCPIMUpdateSchemaElementTree.do Action ");  %>
					document.updateAndDelete.action="GCPIMUpdateSchemaElementTree.do";
					document.updateAndDelete.method="post";
					document.updateAndDelete.operationType.value = "FIND";
					document.updateAndDelete.target="_top";
					document.updateAndDelete.submit();
					return true;
				}else{
					document.updateAndDelete.operationType.value =  "FIND";
    				document.updateAndDelete.action = targetUrl;
					document.updateAndDelete.target="_top";
					document.updateAndDelete.submit();
					return true;
			}
			
			
		}else{	
			document.updateAndDelete.operationType.value =  "FIND";
    				document.updateAndDelete.action = targetUrl;
					document.updateAndDelete.target="_top";
					document.updateAndDelete.submit();
			return true;
		}	
			
}



</SCRIPT>
</HEAD>


<%
 

		String tpName = "";
		String tpGenId  = "";
		boolean isStandard = false;
		String catalogGenId = request.getParameter("catalogGenId");
		if(catalogGenId.startsWith("Standard"))
			isStandard = true;
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
	String mandatory="";
	String allowalias="";
	String dataType="";
	String displayOrder="";
	String currSelectionXpath =request.getParameter("xpath") ;
//	System.out.println("CurrSelection Xpath ="+currSelectionXpath);
	String currSelectionNodeName = "";

	//is it root element selection
	boolean rootSelection = true;
	Node node = XMLUtil.getNode(catalogNode, "schema/Product");
//	System.out.println("NodeName = "+node.getNodeName());
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
		//		System.out.println("currselection Node Name"+currSelectionNodeName);
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
	//	System.out.println(" The value of EPC is "+ XMLUtil.getValue(schemaNode,currSelectionXpath+"/@displayOrder"));
			values = XMLUtil.getValue(schemaNode,currSelectionXpath+"/@values");
			mandatory=XMLUtil.getValue(schemaNode,currSelectionXpath+"/@mandatory");
			allowalias=XMLUtil.getValue(schemaNode,currSelectionXpath+"/@allowAllias");
			dataType=XMLUtil.getValue(schemaNode,currSelectionXpath+"/@dataType");
			displayOrder=XMLUtil.getValue(schemaNode,currSelectionXpath+"/@displayOrder");
	//		System.out.println("The value is "+allowalias);
	//		System.out.println("The Mandatory is"+mandatory);
			if(mandatory != null)
			{
				if (mandatory.equals("true")){ mandatory = "checked"; }else mandatory="";
				
			}else {mandatory ="";}
			if(allowalias != null)
			{
				if ( allowalias.equals("true")){ allowalias ="checked";}else allowalias="";
			}else{allowalias="";}
		//	System.out.println("These are the values"+values);
		//	System.out.println("The value is "+allowalias);
		//	System.out.println("The Mandatory is"+mandatory);
			if(values ==null)
			values ="";
		}else{
		//	System.out.println(" It is null ");
		}
	}

%>

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
<body >
		<form name= "updateAndDelete" action="GCPIMUpdateSchemaElementTree.do" method="post" target="_top"  >
		<input type="hidden" name="targetUrl" value="" />
		
	<table width="90%" border="0" align="center" cellpadding="5" cellspacing="1" bgcolor="#CCCCCC">
		 <br>
		<tr bgcolor="#DCDCDC">
			<td align="center" bgcolor="#DCDCDC" class="td-mapCatalogTreeTables"><FONT color="#cc0033" size="2" face="Arial" class="title">Selected Attribute</FONT></td>
		</tr>
		<tr bgcolor="#DCDCDC">
		  <td>
				<br>
				
				
					<input type="hidden" name="catalogGenId" value="<%=request.getParameter("catalogGenId") %>">
					<input type="hidden" name="xpath" value="<%= currSelectionXpath %>">
					<input type="hidden" name="operationType" value="">
					<input id="hidden10" type="hidden" name="tpGenId" value="<%=tpGenId%>">
					<table width="90%" align="center" cellpadding="3" cellspacing="1" >
                      <% //System.out.println("Current Selection Xpath ="+ currSelectionXpath ); %>
                      <tr class="titleBlack">
                        <td width="1291"  colspan="4"><b>Selected Attribute Location : <%= currSelectionXpath%> </b></td>
                      </tr>
                      
                      <tr>
                        <td colspan="4"><table width="100%" border="0" cellspacing="0" cellpadding="02">
                          <tr>
                            <td width="19%">Attribute Name: </td>
                            <td width="81%"><input type="text" name="name" value="<%=currSelectionNodeName %>" size='30'></td>
                          </tr>
                          <tr>
                            <td colspan="2"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                              <tr>
                                <td>Attributes :                                </td>
                                <td><input type='checkbox' width=10 name='Mandatory'   <%=mandatory %> >
Mandatory</td>
                                <td><input type='checkbox' width=10 name='allowAllias' <%=allowalias %> >
allowAllias</td>
                                <td>DataType
                                  <select name="dataType" >
                                    <%	if(!dataType.equalsIgnoreCase("")){ %>
                                    <option selected><%=dataType %></option>
                                    <%}%>
                                    <%	if ( dataType.equalsIgnoreCase("string")){//System.out.println("Rob asking what is the datatype"+dataType); %>
                                    <option  value="number">number</option>
                                    <%}else if (dataType.equalsIgnoreCase("number")){%>
                                    <option  value="string">string</option>
                                    <%}else{%>
                                    <option  value="number">number</option>
                                    <option  value="string">string</option>
                                    <%}%>
                                  </select></td>
                                <td>DisplayOrder
                                <input type='text' size=10  name='displayOrder' value="<%=displayOrder%>"></td>
                              </tr>
                            </table></td>
                          </tr>
                          
                          <tr>
                            <td colspan="2"><div align="center">
                              <table width="100%" border="0" cellspacing="0" cellpadding="5">
                                <tr>
                                  <td width="24%"><div align="right">** Attribute Values : </div></td>
                                  <td width="76%"><textarea id="Textarea1" name="values" rows="3" cols="52"><%= values %></textarea></td>
                                </tr>
                              </table>
                              </div></td>
                          </tr>
                          
                        </table></td>
                      </tr>
                      
                      <% if(true) {%>
                      
                      <tr>
                        <td colspan="4">**Note :Attribute values should be comma separated.</td>
                      </tr>
                      <% } %>
                      <tr>
                        <input type="hidden" value="" name="tp_company_nm"/>
                        <input type="hidden" value="Catalog" name="pagenm"/>
                        <td colspan="4"><%
								String str= (String) session.getAttribute("optype");
								if(str== null ) str="";
							
							
								if(str.equals("edit")) {
								System.out.println("Operation Type ==="+str);
							%>
                               
                               <input name="button" type="button" onClick=" return goback('GCPIMOpenCatalogSchemaDef.do');"  value="Go Back">
                            <%
								} else {
									String newpage = (String)session.getAttribute("newpage");
									System.out.println("************  new page status "+newpage);
									if(newpage == null ) newpage="no";
									if(!newpage.equals("yes")){
							%>
                            <input name="button" type="button" onClick="return  goback('GCPIMStandardCatalogList.do?');"  value="Go Back">
                            <%}else {%>
                                        <input name="button" type="button" onClick="return  goback('GCPIMStandardCatalogNew.do?');"  value="Go Back">
                 
							<%}	}
							%>
                            <input name="button" type="button" onClick="return submitform('ADD')" value="ADD">
                            <%	if(node != null ) { %>
                            <%	if(!rootSelection){%>
                            <input name="button" type="button" onClick="return submitform('UPDATE')" value="Update">
                            <%	} %>
                            <input name="button" type="button" onClick="return submitform('DELETE')" value="Delete">
                            <input name="button" type="button" onClick="return submitform('SAVE')"  value="Save Tree">
                            <input name="button" type="button" onClick="return submitform('Help')" value="Help">
                            <% }System.out.println("*&&((");	%>                        </td>
                      </tr>
                    </table></td>
		</tr>
	</table>
	
								<%@include file='../catalog/duplicate.jsp'%>
								
</form>
        <p>&nbsp;</p>
</body>

