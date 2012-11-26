
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.catalog.session.UploadCatalogContext"%>
<%@ page import="com.rdta.catalog.Constants"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.catalog.session.ReconcilableData"%>


<%String tp_company_nm = request.getParameter("tp_company_nm");
    String pagenm =  request.getParameter("pagenm");
    String sessionID = (String)session.getAttribute("sessionID");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
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
	
    document.StartUploadProcessForm.action = "Catalog.do?tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>";
	document.StartUploadProcessForm.operationType.value = "FIND";
	document.StartUploadProcessForm.submit();

	return true;
}


//-->
</script>
</head>
<body>

<%@include file='../epedigree/topMenu.jsp'%>

<%


    System.out.println(" Start upload process " );


	//since we are using form type as multipart/form-data
	//incase of schema creation from upload process
	//we are losing control of request paramerts
	//so populate catalogId so that tree will populate later
	String 	catalogGenId =  (String)request.getAttribute("catalogGenId");
	String 	standardCatalogId =  (String)request.getAttribute("standardCatalogId");


	//if it is coming from after mappping the values screens these values will come as a parameter values
	if(catalogGenId == null ) {
		catalogGenId = request.getParameter("catalogGenId");
	}

	if(standardCatalogId == null ) {
		standardCatalogId = request.getParameter("standardCatalogId");
	}

  System.out.println(" catalogGenId : " + catalogGenId + "  standardCatalogId:  " +  standardCatalogId);
  System.out.println(" Start upload process " );

  UploadCatalogContext uploadContext = (UploadCatalogContext)session.getAttribute(Constants.SESSION_CATALOG_UPLOAD_CONTEXT); 
  String filePath = uploadContext.getFilePath();
  String totalRecordCount = "" + uploadContext.getNoOfRecordsToLoad();
  
%>

<div id="rightwhite2">




<html:form action="UploadLoadForXML.do"  method="post" >

<html:hidden property="catalogGenId" value="<%=catalogGenId%>"/>
<html:hidden property="standardCatalogId" value="<%=standardCatalogId%>"/>
<html:hidden property="operationType" value=""/>
<html:hidden property="tp_company_nm" value="<%=tp_company_nm%>"/>
<html:hidden property="pagenm" value="<%=pagenm%>"/>

<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
  <tr> 
    <td width="1%" valign="middle" class="td-rightmenu"><html:img src="./assets/images/space.gif" width="10" height="10"/></td>
    <!-- Messaging -->
    <td width="99%" valign="middle" class="td-rightmenu">
	
			<jsp:include page="./TPHeader.jsp" />
	  </td>
  </tr>

   
  <tr> 
    <td>&nbsp;</td>
    <td><table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr> 
          <td><html:img src="./assets/images/space.gif" width="30" height="12"/></td>
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
						  <td class="title">Catalog Review Details</td>
						</tr>	

							<TR class="tableRow_Off">
								<TD class="titleBlack"> Total Number Of Catalog Prodcts To Be Loaded : <%= totalRecordCount %></TD>
							
							</TR>

						
						<TR class="tableRow_On">
								<TD colspan="2" align="center">

								<html:button property="button1" value="Cancel" onclick="return goback()"/>
								<html:submit value="Load" />

								
     								
								 </TD>
						</TR>


               
		  </table>
		  </td>
		  </tr>

	  </table>



</html:form>

</div>


<jsp:include page="../globalIncludes/Footer.jsp" />


</body>
</html:html>
