
<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.catalog.session.UploadCatalogContext"%>
<%@ page import="com.rdta.catalog.Constants"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.catalog.session.ReconcilableData"%>

<%@ page import="com.rdta.catalog.MappingNodeObject"%>
<%@ page import="com.rdta.catalog.SchemaTree"%>

<%@ page import="com.rdta.catalog.trading.model.Catalog"%>



<%String tp_company_nm = request.getParameter("tp_company_nm");
    String pagenm =  request.getParameter("pagenm");
    String sessionID = (String)session.getAttribute("sessionID");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Raining Data ePharma - Trading Partner Manager -  Detail</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="./assets/epedigree1.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}


function goback() {

	document.CancelForm.operationType.value = "FIND";
	document.CancelForm.submit();

	return true;
}



function submitToStartUpload() {

}


//-->
</script>
</head>
<body>


<%@include file='../epedigree/topMenu.jsp'%>>


<%

    String tpGenId=request.getParameter("tpGenId");

	String 	catalogGenId = request.getParameter("catalogGenId");
	String	standardCatalogId = request.getParameter("standardCatalogId");

	String	FromPage = request.getParameter("FromPage");

	String	currAttrNo = "";

	if(FromPage == null ) {
		currAttrNo = (String) request.getAttribute("currAttrNo");
	} else  {
		currAttrNo = request.getParameter("currAttrNo");
	}
	

  System.out.println(" catalogGenId : " + catalogGenId + "  standardCatalogId:  " +  standardCatalogId);

 

  int currAttrNoInt = Integer.parseInt(currAttrNo);
	
  UploadCatalogContext uploadContext = (UploadCatalogContext)session.getAttribute(Constants.SESSION_CATALOG_UPLOAD_CONTEXT); 

	List reconcilableList = uploadContext.getReconcilableList();
	ReconcilableData currData = (ReconcilableData)reconcilableList.get(currAttrNoInt);
	String sourceEleName = currData.getSourceElementName();
	String targetEleName = currData.getTargetElementName();

	List sourceValues = currData.getValuesList();

	String values = "";
	
	try {
		MappingNodeObject  mappingNodeObj =	uploadContext.getMappingNodeObj();
		Catalog catalog = mappingNodeObj.getTargetCatalog();
		SchemaTree schemaTree = catalog.getSchemaTree();		
		values = schemaTree.getValues(targetEleName);
	} catch(Exception e) {
		 
	}

	
  
%>



<FORM name= "CancelForm" ACTION="Catalog.do?&tpGenId=<%=tpGenId%>&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>&tpList=view&tcList=view&catSchema=view"  method="post" >

<INPUT id="hidden10" type="hidden" name="catalogGenId" value="<%=catalogGenId%>">
<INPUT id="hidden13" type="hidden" name="operationType" value="">

</FORM>



<FORM name= "UploadMapValuesToStandard" ACTION="UploadMapValuesToStandards.do?tpGenId=<%=tpGenId%>&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>&tpList=view&tcList=view&catSchema=view"  method="post" >

<INPUT id="hidden10" type="hidden" name="catalogGenId" value="<%=catalogGenId%>">
<INPUT id="hidden11" type="hidden" name="standardCatalogId" value="<%=standardCatalogId%>">
<INPUT id="hidden11" type="hidden" name="currAttrNo" value="<%=currAttrNo%>">

<div id="rightwhite2">
<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
  <tr> 
    <td width="1%" valign="middle" class="td-rightmenu"><img src="./assets/images/space.gif" width="10" height="10"></td>
    <!-- Messaging -->
    <td width="99%" valign="middle" class="td-rightmenu">
	
			<jsp:include page="./TPHeader.jsp" />
	  </td>
  </tr>

   
  <tr> 
    <td>&nbsp;</td>
    <td><table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr> 
          <td><img src="./assets/images/space.gif" width="30" height="12"></td>
          <td rowspan="2">&nbsp;</td>
        </tr>
        <!-- Breadcrumb -->
        <!-- <tr> 
            <td><a href="#" class="typegray1-link">Home</a><span class="td-typegray"> 
              - </span><a href="#" class="typegray1-link">ePedigree</a></td>
          </tr> -->
        <tr> 
          <td> 
            <!-- info goes here -->
              <table width="100%" cellSpacing="1" cellPadding="3" align="left" border="0"
			bgcolor="white">

						<tr class="tableRow_Header"> 
						  <td  colspan="2" class="title"> Map Attribute Values </td>
						</tr>	

							<TR class="tableRow_Off">
								<TD colspan="2" class="titleBlack"> <center> Attribute Name: <%= sourceEleName %> </center></TD>
							
							</TR>


						<%
							String sourceValue = "";
							String sourceValueName = "";
							String targetValueName = "" ;

							for(int j=0; j< sourceValues.size(); j++) {

								sourceValue = (String)sourceValues.get(j);
								sourceValueName  = "sourceValue" + j;
								targetValueName = "targetValue" + j;

						%>

						

							<TR class="tableRow_On">
								
								<TD>
								   <INPUT id="hidden13" type="hidden" name="<%= sourceValueName%>" value="<%= sourceValue %>">
								   <%= sourceValue %>
								
								</TD>


								


								<TD>
								   <SELECT id="Select4" name="<%= targetValueName%>">
								<%
										String[] value = values.split(";");

										for(int i=0;i < value.length; i++) {
								%>

											<OPTION value="<%= value[i]%>"><%= value[i]%></OPTION>
												

								<%
										}//end of for loop
								%>

									</SELECT>
								</TD>
								    
							</TR>

							

						<%
							}	
						%>
						

						<TR class="tableRow_On">
								<TD colspan="2" align="center">

								<INPUT id="button17" type="button" value="Cancel" onClick="return goback()">
								<INPUT id="button1" type="submit" value="Next" >

								
     								
								 </TD>
						</TR>

               
		  </table>
		  </td>
		  </tr>

	  </table>



</Form>

</div>


<jsp:include page="../globalIncludes/Footer.jsp" />


</body>
</html>
