<%@ page import="com.rdta.tlapi.xql.Statement"%>
<%@ page import="com.rdta.tlapi.xql.Connection"%>
<%@ page import="com.rdta.Admin.Utility.Helper"%>

<TITLE>R & D - eList</TITLE>
<HEAD>

<title>Raining Data ePharma - Trading Partner Manager -  Detail</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="./assets/epedigree1.css" rel="stylesheet" type="text/css">

<SCRIPT>

function redirect() {
	window.location = 'ManageElement.do?catalogGenID=""';
}

function submitform(operation)
{
	document.updateAndDelete.operationType.value = operation;

	//alert(document.updateAndDelete.operationType.value );
	//onClick="return submitform('UPDATE')
	// onClick="return submitform('DELETE')
	if ( document.updateAndDelete.name.value == "" )
	   {
	    		var opt ="<%= session.getAttribute("optype")%>";
	    		 
	    		if(opt == "edit"){ 
	    		 if(document.updateAndDelete.operationType.value == "SAVE" )
	    		 {
	    			 var k="<%= (String) session.getAttribute("updateStatus")%>";
	    			 if( k == "false" ){
	    			 		alert("Access Denied !...");
	    			 		return false;
	    			 	}else{
	    			 	    			 document.updateAndDelete.submit();
	    			 	    	return true;
	    			 	    }
	    			 	    			 
	    		 }
	    		 else{
	    		 alert( " Enter Attribute Name ");
				 return false; 
				 }
			}else if(opt == "manage"){
			
					 if(document.updateAndDelete.operationType.value == "SAVE" )
	    		 {
	    			 var k="<%= (String) session.getAttribute("manageUpdate")%>";
	    			 if( k == "false" ){
	    			 		alert("Access Denied !...");
	    			 		return false;
	    			 	}else{
	    			 	    			 document.updateAndDelete.submit();
	    			 	    	return true;
	    			 	    }
	    			 	    			 
	    		 }
	    		 else{
	    		 alert( " Enter Attribute Name ");
				 return false; 
				 }
			
			
			
			}	 
		}
	else{
			
		var k ="<%= session.getAttribute("optype")%>";
		
		if(k == "edit"){	
			if(operation == "ADD"){
			var i ="<%= (String) session.getAttribute("insertStatus")%>";
			if( i == "false" ){
			
			  alert("AccessDenaied");
			  return false;
			}else {
			document.updateAndDelete.submit();
			}
			}
			if(operation == "SAVE" ){
			var s="<%= (String) session.getAttribute("updateStatus")%>";
	    			 if( s == "false" ){
	    			 		alert("Access Denied !...");
	    			 		return false;
	    			 	}else{
	    			 	    			 document.updateAndDelete.submit();
	    			 	  }
	    }			 	    			 
			
	}else if ( k == "manage" ){
	
			
			if(operation == "ADD"){
		
			var i ="<%= (String) session.getAttribute("manageInsert")%>";
			if( i == "false" ){
			
			  alert("AccessDenaied");
			  return false;
			}else {
			document.updateAndDelete.submit();
			}
		
			}
			
		
			if(operation == "SAVE" ){
			var k="<%= (String) session.getAttribute("manageUpdate")%>";
	    			 if( k == "false" ){
	    			 		alert("Access Denied !...");
	    			 		return false;
	    			 	}else{
	    			 	    			 document.updateAndDelete.submit();
	    			 	  }
	    }			 	    			 
	
	
	
	
	
			
		}
	}	
	return true;
}








function goback(targetUrl)
{
	
	
	document.updateAndDelete.operationType.value =  "FIND";
    document.updateAndDelete.action = targetUrl;
	document.updateAndDelete.target="_top";
	document.updateAndDelete.submit();

	return true;
}


	
</SCRIPT>
</HEAD>


<%


	//GCPIMUpdateStandardSchemaElementTree
	//GCPIMStandardCatalogList.do
	String catalogGenId=request.getParameter("catalogGenId");
	String xpath= request.getParameter("xpath");
	String value = request.getParameter("attrname");
	if(value == null ) value = "";
	if ( xpath == null ) xpath="Product";
	String insertStatus = (String ) session.getAttribute("insertStatus");
	
	
String updateStatus = (String) session.getAttribute("updateStatus");
String deleteStatus = ( String) session.getAttribute("deleteStatus");

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
<body>
	<form name= "updateAndDelete" action="GCPIMUpdateSchemaElementTree.do" method="post" target="_top"  >
<%!
		static boolean flag=false;
%>



	<table width="90%" border="0" align="center" cellpadding="7" cellspacing="1" bgcolor="#CCCCCC">
		 <br>
		<tr>
			<td align="center" class="td-mapCatalogTreeTables">Help</td>
		</tr>
		<tr bgcolor="#DCDCDC">
			<td>
				<br>
						<br>
				<u>	<b> Welcome to the help screen ! </b> </u>
				<br>
				<br>
				<br>
				<u> Adding an Attribute to a selected Product: </u>
				<br>
				<br>
				<li> To Add an attribute to the Product, Click on Product in the left tree. </lit>
				<br>
				<br>
				<li> You can add any static attributes for the selected product. You can choose the attribute to be
					 either mandatory or optional by checking or unchecking the option box for that attribute 
				</li>	 
				<br>
				<br>
				<li> You can also choose the data type which reflects your attributes data type, by choosing the correct 
					 data type from the drop down box
				</li> 
				<br>
				<br>
				<li> You can also specify the display order manually for your attribute, which reflects your attributes order
					 of display in the schema tree
				</li>	 
				<br>
				<br>
				<u> Editing/Deleting an Attribute from a selected Product: </u>
				<br>
				<br>
				
				<li> To Edit or Delete any of the attributes of the selected Product, please click on required attribute, which shows
				 	 the details for the selected attribute. </li>
				<br>
				<br>
				<li> You can change the settings for the attribute chosen, by changing its configuration, which includes 
					 changing its mandatory, allowAlias, DataType and DisplayOrder fields in the screen.	
				</li>
				<br>
				<br>
				<li> After changing the configuration of the fields in the screen for the chosen attribute, please save the tree by 
					 clicking on the "Save Tree" button which saves your desired configuration.
				
				</li>
				<br>
				<br>	
				<b>*** Note: You cannot delete the ProductName, NDC, Manufacter Name, Lot Number and GTIN attributes, which are mandatory
					as per the system requirements. The system would not allow you to delete the same.</b>
		
		
					<input type="hidden" name="catalogGenId" value="<%=catalogGenId%>">
					<input type="hidden" name="xpath" value="<%=xpath%>">
					<input type="hidden" name="operationType" value="">
					<input type="hidden" name="tp_company_nm" value="" >
					<input type="hidden" name="pagenm" value="Catalog" >
					<INPUT id="hidden10" type="hidden" name="GenId" value="<%=request.getParameter("tpGenId")%>">
					
					
					<table width="100%"  align="center" cellPadding="3" cellSpacing="1" bgcolor ="#DCDCDC" >

					
					<tr>
						<td height="39"></td>
						<td height="39"></td>
	
						</tr>
						<tr>
						<td height="39"></td>
						<td height="39"></td>
	
						</tr>
						<tr>
						<td height="39"></td>
						<td height="39"></td>
	
						</tr>
						
						<tr>
							

							<td height="39">
							  <div align="center">
							    <%
								String name = request.getParameter("name");
								String operationType = request.getParameter("operationType");
							
							String strss =(String) session.getAttribute("typeop");
							
							if ( strss.equals("edit"))
							{ 
									%>
									<input type="button"  value="Go Back" onClick="javascript:history.back()" \>
						        
								     		
						   
						    <%}else{
						        	String visit=(String) session.getAttribute("visited");
						        	if(  visit == null ) visit="no"; 
						        	if(visit.equals("yes") ){session.setAttribute("visited","no");%>
						    		<input type="button"  value="Go Back" onClick="return goback('GCPIMUpdateSchemaElementTree.do')"\>
						        		
						    	<%}else {
						    		String newpage = (String) session.getAttribute("newpage");
						    		if(newpage == null )newpage="no";
						     		if(newpage.equals("no")){%>
						     		<input type="button"  value="Go Back" onClick="return goback('GCPIMStandardCatalogList.do')"\>
						     		<%}else{%>
						     				<input type="button"  value="Go Back" onClick="javascript:history.back()" \>
						        	
						       <%}}}%>		
					        </div></td>
						</tr>
			  </table>
				
			</td>
			
			
		</tr>
		
	</table>
		<%if( request.getParameter("exist") != null ){
								if(request.getParameter("exist").equals("true"))
								{
								%>
								<%@include file='../catalog/duptree.jsp'%>
								<%}}%>
								
	</form>
</body>