<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.catalog.session.UploadCatalogContext"%>
<%@ page import="com.rdta.catalog.Constants"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.catalog.session.ReconcilableData"%>




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




//-->
</script>
</head>
<body>

<jsp:include page="../globalIncludes/TopHeader.jsp" />
<jsp:include page="../globalIncludes/TopLevelLayer1.jsp" />
<jsp:include page="../globalIncludes/TopLevelLayer2.jsp" />
<jsp:include page="./LeftNav.jsp" />


<%


    System.out.println(" Start upload process " );
	String	catalogGenId = request.getParameter("catalogGenId");
	String	standardCatalogId = request.getParameter("standardCatalogId");

  System.out.println(" catalogGenId : " + catalogGenId + "  standardCatalogId:  " +  standardCatalogId);


			
  String totalRecordCount =  (String)request.getAttribute("TotalNumOfLoadedRecords");
  
%>

<div id="rightwhite">




<form action="Catalog.do"  method="post" >

<html:hidden property="catalogGenId" value="<%=catalogGenId%>"/>
<html:hidden property="standardCatalogId" value="<%=standardCatalogId%>"/>
<html:hidden property="operationType" value="FIND"/>


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
						  <td class="title">Catalog Upload Status</td>
						</tr>	

							<TR class="tableRow_Off">
								<TD class="titleBlack"> Total Number Of Catalog Products Loaded : <%= totalRecordCount %></TD>
							
							</TR>

							<br>

							<TR class="tableRow_On">
								<TD align="center"> Successfully Loaded all uploaded Products. </TD>
							
							</TR>

						
						<TR class="tableRow_On">
								<TD colspan="2" align="center">

								<html:submit value="Continue" />
								
     								
								 </TD>
						</TR>


               
		  </table>
		  </td>
		  </tr>

	  </table>



<form>

</div>


<jsp:include page="../globalIncludes/Footer.jsp" />


</body>
</html:html>

