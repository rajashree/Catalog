
<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.catalog.session.TradingPartnerContext"%>
<%@ page import="com.rdta.catalog.Constants"%>



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


//-->
</script>
</head>
<body>

<jsp:include page="../globalIncludes/TopHeader.jsp" />
<jsp:include page="../globalIncludes/TopLevelLayer1.jsp" />
<jsp:include page="../globalIncludes/TopLevelLayer2.jsp" />
<jsp:include page="./LeftNav.jsp" />

<%
	//String tpGenId=request.getParameter("tpGenId");
    Node catalogNode =  (Node)request.getAttribute("CatalogInfo");

	String name = "";
	String description = "";
	String createdDate ="";
	String updatedDate = "";

	String catalogGenId = "";
	


	 if(catalogNode != null ) {


		name = CommonUtil.jspDisplayValue(catalogNode,"name");
		description = CommonUtil.jspDisplayValue(catalogNode,"description");
		createdDate = CommonUtil.jspDisplayValue(catalogNode,"createdDate");
		updatedDate = CommonUtil.jspDisplayValue(catalogNode,"updatedDate");
		catalogGenId = CommonUtil.jspDisplayValue(catalogNode,"genId");
		
	}

%>

<div id="rightwhite">

<FORM name= "CatalogForm" ACTION="StandardCatalog.do"  method="post" >


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
    System.out.println("tpGenId"+tpGenId);
%>
				
				<tr class="tableRow_On"> 
                  <td  colspan="4" class="td-typeblack"> Trading Partner :<%=tpName%> </td>
				 </tr>

					<TR   class="tableRow_On">
						<TD>Catalog Name:</TD>
						<TD colspan="3"><INPUT id="Text1" value="<%= name %>" name="name"></TD>
					</TR>
					<TR class="tableRow_Off">
						<TD>Comments:</TD>
						<TD colSpan="3"><TEXTAREA id="Textarea1" name="description" rows="3" cols="60"><%= description %></TEXTAREA></TD>
					</TR>
					<TR class="tableRow_On">
						<TD>Created Date:</td>
						<TD><INPUT id="Text4" value="<%= createdDate %>" readonly name="createdDate"></TD>
						<TD>Updated Date:</td>
						<TD><INPUT id="Text5" value="<%= updatedDate %>" readonly name="updatedDate"> </TD>
					</TR>

					<TR class="tableRow_Off">
						<TD></TD>
						<TD align="center">
						<INPUT id="button" type="submit" value="Save">
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
		if(catalogNode != null ) {
	%>
			<INPUT id="hidden1" type="hidden" name="operationType" value="<%=OperationType.UPDATE%>">
			<INPUT id="hidden2" type="hidden" name="catalogGenId" value="<%= CommonUtil.jspDisplayValue(catalogNode,"genId") %>">
	<%
		} else {
	%>
	  
			<INPUT id="hidden10" type="hidden" name="operationType" value="<%=OperationType.ADD%>">
	  <%
			}
	  %>



</Form>



<% if(catalogNode != null ) { %>


<FORM name= "CreateCatalogSchemaFromFile" ACTION="CreateStandardCatalogSchemaFromFile.do"  method="post" enctype="multipart/form-data" >

<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
<INPUT id="hidden2" type="hidden" name="catalogGenId" value="<%=catalogGenId%>">
<INPUT type="hidden" name="tpGenId" value="<%=tpGenId%>">
<INPUT id="hidden2" type="hidden" name="operationType" value="createFromFile">
					
			    	<tr class="tableRow_Off">		
						<TD noWrap> Create Catalog Schema From File:</td>
						<TD colspan=2 align=left>
									<input type="file" name="catalogAddFile" size=30>
						</TD>
						<td><INPUT id="Reset1" type="submit" value="Create Catalog Schema" name="Add"></td>
					</tr>

					</table>

</form>



<FORM name= "upLoadCatalog" ACTION="UploadCatalog.do"  method="post" enctype="multipart/form-data" >

<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
<INPUT id="hidden2" type="hidden" name="catalogGenId" value="<%= catalogGenId %>">
<INPUT type="hidden" name="tpGenId" value="<%=tpGenId%>">
<INPUT id="hidden5" type="hidden" name="standardCatalogId" value="StandardGenId1000">
					
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
									<input type="file" name="catalogAddFile" size=30>
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
