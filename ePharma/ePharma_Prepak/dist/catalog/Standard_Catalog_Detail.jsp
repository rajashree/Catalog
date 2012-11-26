
<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.catalog.session.TradingPartnerContext"%>
<%@ page import="com.rdta.catalog.Constants"%>



<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Raining Data ePharma - GCPIM -  Detail</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="./assets/epedigree1.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}

function callSave(){
 

 		if( document.CatalogForm.catalogName.value == "" ){
 				alert("Enter Catalog Name :");
 				return false;
 		}else if(document.CatalogForm.description.value =="" ){
 				alert("Enter Description :");
 				return false;
 		}else {
 	 		document.CatalogForm.submit();
 			return true;
 	    }
 }


function goback(abc)
{

if ( document.CreateCatalogSchemaFromFile.catalogAddFile.value == "" ){


	alert("Please Enter file Name");
	return false;


}
 




if(document.CreateCatalogSchemaFromFile.CatExists.value == "yes" )
	{	    	
			var answer = confirm("Catalog is Already Existing Do you want to update ?");
			if( answer ){
			
				var i ="<%= (String) session.getAttribute("managefileUpdate")%>";
				
				if( i == "false"){
						alert("AccessDenied!..");
						return false;
				}
			
				return true;
			}else
			return false;
	}else{
			var i ="<%= (String) session.getAttribute("managefileInsert")%>";

			if( i == "false"){
				alert("AccessDenied!..");
					return false;
			}
	
			return true;
	}



}

//-->
</script>
</head>
<body>

<%
	String tp_company_nm = request.getParameter("tp_company_nm");
	if(tp_company_nm == null) tp_company_nm="";
	String pagenm = request.getParameter("pagenm");
	if(pagenm == null ) pagenm="Catalog";
	String sessionID = (String)session.getAttribute("sessionID");
%>
<%@include file='../epedigree/topMenu.jsp'%>


<%
	//String tpGenId=request.getParameter("tpGenId");
    Node catalogNode =  (Node)request.getAttribute("CatalogInfo");
	String catalogName = "";
	String description = "";
	String createdDate ="";
	String updatedDate = "";
	String catalogGenId = "";
  
   if(catalogNode != null ) {
//	System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		catalogName = CommonUtil.jspDisplayValue(catalogNode,"catalogName");
		description = CommonUtil.jspDisplayValue(catalogNode,"description");
		createdDate = CommonUtil.jspDisplayValue(catalogNode,"//CreatedDate");
		updatedDate = CommonUtil.jspDisplayValue(catalogNode,"//updated-timestamp");
		catalogGenId = CommonUtil.jspDisplayValue(catalogNode,"catalogID");
		System.out.println("Catalog GenId ="+catalogGenId);
//	System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");	
		
	}

%>

<div id="rightwhite">
<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
  <tr> 
    <td width="99%" valign="top" class="td-rightmenu" style="height: 0px;">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td align="left"></td>
			<td align="right"><img src="assets/images/space.gif" width="5"></td>
		</tr>
	</table>
    </td>
 </tr>
							  	
<FORM name= "CatalogForm" ACTION="GCPIMStandardCatalog.do"  method="post" >


<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
  <tr> 
    <td width="1%" valign="middle" class="td-rightmenu"></td>
    <!-- Messaging -->
    <td width="99%" valign="middle" class="td-rightmenu">
	
			<jsp:include page="./TPHeader.jsp" />
	  </td>
  </tr>


  <tr> 
    <td>&nbsp;</td>
    <td><table border="0" cellspacing="0" cellpadding="0" width="100%">
        
        <!-- Breadcrumb -->
        <!-- <tr> 
            <td><a href="#" class="typegray1-link">Home</a><span class="td-typegray"> 
              - </span><a href="#" class="typegray1-link">ePedigree</a></td>
          </tr> -->
        <tr> 
          <td> 
            <!-- info goes here -->
              <table width="100%" cellSpacing="0" cellPadding="0" align="left" border="0"
			bgcolor="white">
                <!-- <tr class="tableRow_Header"> 
                  <td class="title">Cardinal Health</td>
                </tr> -->
                <tr> 
                  <td align="left">
					<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">		
					<tr class="tableRow_Header"> 
                  <td class="title" colspan="4">Catalog Details</td>
                </tr>	


		<%

    TradingPartnerContext context = (TradingPartnerContext)session.getAttribute(Constants.SESSION_TP_CONTEXT); 
    String tpName = context.getTpName();
    String tpGenId = context.getTpGenId();
    String exist = (String) request.getAttribute("catalogexist");
    
    
    
    if(exist == null )exist="";
  //  System.out.println("**********************  "+exist+"********************************************tpGenId"+tpGenId);
%>
				<input type="hidden" name="catalogexist" value="<%=exist%>" />
					<TR   class="tableRow_On">
						<TD>Catalog Name:</TD>
					<% String catalogType = (String) request.getAttribute("newcalog");
					   if (catalogType == null ) catalogType="";
					   if( !catalogType.equals("true")){ %>
						<TD colspan="3"><INPUT id="Text1" value="<%= catalogName %>" readonly name="catalogName"></TD>
					</TR>
					<TR class="tableRow_Off">
						<TD>Description:</TD>
						<TD colSpan="3"><TEXTAREA id="Textarea1" name="description" rows="3" readonly cols="60"><%= description %></TEXTAREA></TD>
					</TR>
					<%} else { %>
						<TD colspan="3"><INPUT id="Text1" value="<%= catalogName %>"  name="catalogName"></TD>
					</TR>
					<TR class="tableRow_Off">
						<TD>Description:</TD>
						<TD colSpan="3"><TEXTAREA id="Textarea1" name="description" rows="3"  cols="60"><%= description %></TEXTAREA></TD>
					</TR>
					
					<%}%>
					
					<TR class="tableRow_On">
						<TD>Created Date:</td>
						<TD><INPUT id="Text4" name="createDate"  value="<%= createdDate %>" readonly ="createdDate"></TD>
						<TD>Updated Date:</td>
						<TD><INPUT id="Text5" name="updatedDate" value="<%= updatedDate %>" readonly ="updatedDate"> </TD>
					</TR>

					<TR class="tableRow_Off">
						<TD></TD>
						<TD align="center">
						<INPUT id="button" type="button" value="Save" onClick="callSave();" >
						</TD>
					
						<TD></TD>
						<TD></TD>
					</TR>

			
				</TABLE>
            </td>
        </tr>
      </table>

	  </table>

	<%
		
	//	System.out.println("  CatalogGenId ***&&^^ := "+catalogGenId);
		String str = (String) request.getAttribute("cataloggenId");
		if(str == null ) str="";
		if(catalogNode != null ) {
					%>
			<INPUT id="hidden1" type="hidden" name="operationType" value="<%=OperationType.UPDATE%>">
			<INPUT id="hidden2" type="hidden" name="catalogGenId" value="<%=str%>">
	<%
		} else {
		
		
	%>
	  		<INPUT id="hidden2" type="hidden" name="catalogGenId" value="<%=str%>">
			<INPUT id="hidden10" type="hidden" name="operationType" value="<%=OperationType.ADD%>">
	  <%
			}
	  %>



</Form>



<% if(catalogNode != null ) { %>


<FORM name= "CreateCatalogSchemaFromFile" ACTION="GCPIMCreateStandardCatalogSchemaFromFile.do"  method="post" enctype="multipart/form-data" >

<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
<INPUT id="hidden2" type="hidden" name="catalogGenId" value="<%=catalogGenId%>">
<INPUT type="hidden" ="tpGenId" value="<%=tpGenId%>">
<INPUT id="hidden2" type="hidden" name="operationType" value="createFromFile">
<% String catexist = (String) request.getAttribute("CatExists"); %>
<INPUT id = "hidden6" type="hidden" name="CatExists" value ="<%=catexist%>" >
					
			    	<tr class="tableRow_Off">		
						<TD noWrap> Create Catalog Schema From File:</td>
						<TD colspan=2 align=left>
									<input type="file" name="catalogAddFile" size=30>
						</TD>
						<td><INPUT id="Reset1" type="submit" value="Create Catalog Schema" name="Add"  onClick="return goback('catalog')" ></td>
					</tr>

					</table>

</form>



<FORM = "upLoadCatalog" ACTION="GCPIMUploadCatalog.do"  method="post" enctype="multipart/form-data" >

<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
<INPUT id="hidden2" type="hidden" ="catalogGenId" value="<%= catalogGenId %>">
<INPUT type="hidden" ="tpGenId" value="<%=tpGenId%>">
<INPUT id="hidden5" type="hidden" ="standardCatalogId" value="StandardGenId1000">
					
				<!--	<TR class="tableRow_On">
						<TD colSpan="4" align=center><STRONG><FONT color="#000099">New Catalog Submission Settings</FONT></STRONG></TD>
					</TR>


					<TR class="tableRow_Off">
						<TD align=center>
							<input type="radio"  name="NewCatalogMode" checked value="override">Override
						</TD>
						<TD align=center>
							<input type="radio" name="NewCatalogMode" value="dups">Allow Dups
						</TD>
						<TD colSpan="2" align=center>
							<input type="radio"  name="NewCatalogMode" value="dif">Diff From New
						</TD>
					</TR>   -->

		
					<tr class="tableRow_Off">		
						<TD noWrap> Submit Catalog:</td>
						<TD colspan=2 align=left>
									<input type="file" ="catalogAddFile" size=30>
						</TD>
						<td><INPUT id="Reset1" type="submit" value="Add Catalog" name="Add"></td>
					</tr>

					</table>

</form>

<% } %>

</div>


<jsp:include page="../globalIncludes/Footer.jsp" />


</body>
</html>
