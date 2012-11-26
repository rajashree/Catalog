
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


function goback() {

    document.UploadCatalogReview.action = "Catalog.do";
    document.UploadCatalogReview.operationType.value = "FIND";
    document.UploadCatalogReview.submit();

	return true;
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

	//since we are using form type as multipart/form-data
	//incase of schema creation from upload process
	//we are losing control of request paramerts
	//so populate catalogId so that tree will populate later
	String 	catalogGenId = (String) request.getAttribute("catalogGenId");
	String 	standardCatalogId =  (String)request.getAttribute("standardCatalogId");

	System.out.println("hai catalogGenId : " + catalogGenId + "  standardCatalogId:  " +  standardCatalogId);


  UploadCatalogContext uploadContext = (UploadCatalogContext)session.getAttribute(Constants.SESSION_CATALOG_UPLOAD_CONTEXT); 
  System.out.println("uploadContext"+uploadContext.getFilePath());
  String filePath = uploadContext.getFilePath();
  List 	reconcilableList = 	uploadContext.getReconcilableList();
  List notMatchSourceColList = uploadContext.getNotMatchSourceColumns();

  System.out.println(" We are here size  "  + notMatchSourceColList.size());

  System.out.println(" We are here reconcilableList  "  + reconcilableList.size());

  
%>

<div id="rightwhite">



<form action="FirstUploadMapValuesToStandards.do"  method="post" >

<html:hidden property="catalogGenId" value="<%=catalogGenId%>"/>
<html:hidden property="standardCatalogId" value="<%=standardCatalogId%>"/>
<html:hidden property="currAttrNo" value="0"/>
<html:hidden property="FromPage" value="First"/>
<html:hidden property="operationType" value=""/>


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
						  <td class="title">Catalog Review Details</td>
						</tr>	

							<TR class="tableRow_Off">
								<TD>No. of Attributes Needs Reconcilable: <%= reconcilableList.size() %></TD>
							
							</TR>

								<TR class="tableRow_On">
								    </td>
								</tr>


                <!-- <tr class="tableRow_Header"> 
                  <td class="title">Cardinal Health</td>
                </tr> -->
                <tr> 
                  <td align="left">
					<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">		
						
						<TR class="tableRow_Header">
								<TD> <b>Source Attribute </b></TD>
								<TD > <b> Values Need To be Mapped  </b> </TD>
							</TR>

							<%
								String sourceEleName = "";
								String targetEleName = "";
								for(int i=0; i< reconcilableList.size(); i++) {

									ReconcilableData data = (ReconcilableData)reconcilableList.get(i);
									sourceEleName = data.getSourceElementName();
									targetEleName= data.getTargetElementName();
									List values = data.getValuesList();
									StringBuffer buff = new StringBuffer();
									for(int j=0; j< values.size(); j++) {
										buff.append((String)values.get(j) );
										buff.append(";" );
									}

							%>

							<TR class="tableRow_On">
								<TD> <%= sourceEleName %></TD>
								<TD><%= buff.toString() %> </TD>
							</TR>

							<%
								}
							%>

							<TR class="tableRow_On">
								<TD colspan="2" align="center">

								<html:button property="button1" value="Cancel" onclick="return goback()"/>

								<html:submit value="Map To Standard Values" />
     								
								 </TD>
							</TR>
												

					</TABLE>
				 </td>
				</tr>
		  </table>
		  </td>
		  </tr>

	  </table>



</form>

</div>


<jsp:include page="../globalIncludes/Footer.jsp" />


</body>
</html:html>
