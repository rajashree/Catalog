
<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.catalog.session.TradingPartnerContext"%>
<%@ page import="com.rdta.catalog.Constants"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.commons.persistence.QueryRunner"%>
<%@ page import="com.rdta.commons.persistence.QueryRunnerFactory"%>


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


//-->
</script>
</head>
<body>

<%@include file='../epedigree/topMenu.jsp'%>


<%
    Node catalogNode =  (Node)request.getAttribute("CatalogInfo");

	String name = "";
	String description = "";
	String createdDate ="";
	String updatedDate = "";

	String catalogGenId = "";
	
    String rightCatalogGenId=(String)session.getAttribute("rightCatalogGenId");
    System.out.println("inside Jsp rightCat"+rightCatalogGenId);
    session.setAttribute("standardCatalogId",rightCatalogGenId);

	 if(catalogNode != null ) {


		name = CommonUtil.jspDisplayValue(catalogNode,"catalogName");
		description = CommonUtil.jspDisplayValue(catalogNode,"description");
		createdDate = CommonUtil.jspDisplayValue(catalogNode,"//CreatedDate");
		updatedDate = CommonUtil.jspDisplayValue(catalogNode,"//updated-timestamp");
		catalogGenId = CommonUtil.jspDisplayValue(catalogNode,"catalogID");
		
	}

%>
<%

    TradingPartnerContext context = (TradingPartnerContext)session.getAttribute(Constants.SESSION_TP_CONTEXT); 
    String tpName = context.getTpName();
    String tpGenId = context.getTpGenId();
%>

<div id="rightwhite2">
<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
  <tr> 
    <td width="100%" valign="middle" class="td-rightmenu"><img src="./assets/images/space.gif" width="10" height="10"></td>
    <!-- Messaging -->
    
  </tr>
  <TR>
  
<FORM name= "CatalogForm" ACTION="Catalog.do?catalogGenId=<%=(String)session.getAttribute("cGenId")%>&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>&tpGenId=<%=tpGenId%>&tpList=view&tcList=view&catSchema=view"  method="post" >

            <!-- info goes here -->
             
					<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">		
					<tr class="tableRow_Header"> 
                  <td class="title" colspan="4">Catalog Details</td>
                </tr>	


		
				
				<tr class="tableRow_On"> 
                  <td  colspan="4" class="td-typeblack"> Trading Partner :<%=tpName%> </td>
				 </tr>

					<TR   class="tableRow_On">
						<TD>Catalog Name:</TD>
						<TD colspan="3"><INPUT id="Text1" value="<%= name %>" name="catalogName"></TD>
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
						<INPUT id="button" type="submit" value="Save" onClick="return CatalogInsertPrivilage()">
						</TD>
					
						<TD></TD>
						<TD></TD>
					</TR>

			
				</TABLE>
            </td>
        </tr>
      </table>

	<%
		//CommonUtil.jspDisplayValue(catalogNode,"catalogID")
		if(catalogNode != null ) {
	%>
			<INPUT id="hidden1" type="hidden" name="operationType" value="<%=OperationType.UPDATE%>">
			<INPUT id="hidden2" type="hidden" name="catalogGenId" value="<%= (String) request.getAttribute("catalogGenId") %>">
	<%
		} else {
	%>
	  
			<INPUT id="hidden10" type="hidden" name="operationType" value="<%=OperationType.ADD%>">
	  <%
			}
	  %>



</Form>



<% if(catalogNode != null ) { %>


<FORM name= "CreateCatalogSchemaFromFile" ACTION="CreateCatalogSchemaFromFile.do?&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>"  method="post" enctype="multipart/form-data" >

<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
<INPUT id="hidden2" type="hidden" name="catalogGenId" value="<%=catalogGenId%>">
<INPUT id="hidden2" type="hidden" name="operationType" value="createFromFile">
					
			    	<tr class="tableRow_Off">		
						<TD noWrap> Create Catalog Schema From File:</td>
						<TD colspan=2 align=left>
									<input type="file" name="catalogAddFile" size=30>
						</TD>
						<td><INPUT id="Reset1" type="submit" onClick="return CatalogInsertPrivilage();" value="Create Catalog Schema" name="Add" ></td>
					</tr>

					</table>

</form>

<form name="upLoadCatalog" method="post" enctype="multipart/form-data">

<!-- Add the data entry bits -->


<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
<INPUT id="hidden2" type="hidden" name="catalogGenId" value="<%= catalogGenId %>">
<INPUT id="hidden5" type="hidden" name="standardCatalogId" value="<%= session.getAttribute("rightCatalogGenId")%>">
					
		
					<tr class="tableRow_Off">		
						<TD noWrap> Submit Catalog:</td>
						<TD colspan=2 align=left>
									<INPUT type="file" name="catalogAddFile"/>
						</TD>
						<td><INPUT type="button" value="Add Catalog" name="Add"  onclick="return OnButton1();"></td>
					</tr>

					</table>


</form>




<% } %>
<jsp:include page="../globalIncludes/Footer.jsp" />

</TR>
</TABLE>

</div>





</body>
</html>



<script language=javascript>
<!--
function OnButton1()
{
if(CatalogUpdatePrivilage())
{
var url=document.upLoadCatalog.catalogAddFile.value;

var ThisPage=url.substring(url.lastIndexOf("."),url.length);




if(ThisPage==".csv")
{
document.upLoadCatalog.action = "UploadCatalog.do?&tpGenId=<%=tpGenId%>&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>&tpList=view&tcList=view"
		
	document.upLoadCatalog.submit();			// Submit the page
	return true;
}
else if(ThisPage==".xml")
{
document.upLoadCatalog.action = "UploadCatalogXML.do?&tpGenId=<%=tpGenId%>&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>&tpList=view&tcList=view&catSchema=view"
	
	document.upLoadCatalog.submit();			// Submit the page
	return true;
}
else {

alert("file should be of xml or csv format");
}
}

}


function CatalogInsertPrivilage()
{

<%
		QueryRunner queryrunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
        List accessList = queryrunner.returnExecuteQueryStrings("tlsp:validateAccess('"+sessionID+"','4.03','Insert')");
		String viewStatus = accessList.get(0).toString();
		System.out.println("The insert is "+viewStatus);
       if(viewStatus.equals("false"))
       {
%>

alert("Access Denied ....!")
return false;
<%}
if(viewStatus.equals("true")){


%>
return true;

<%}
 %>


}
function CatalogUpdatePrivilage()
{

<%
		queryrunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
        accessList = queryrunner.returnExecuteQueryStrings("tlsp:validateAccess('"+sessionID+"','4.03','Update')");
		viewStatus = accessList.get(0).toString();
		System.out.println("The Update is "+viewStatus);
       if(viewStatus.equals("false"))
       {
%>

alert("Access Denied ....")
return false;
<%}
if(viewStatus.equals("true")){

%>
return confirm('Are you sure to update');
 
<%}
 %>


}
function askuser()
  {
   var answer="   "
   var statement="Answer yes or no"
   var answer=prompt("Do you like to Update")
   if ( answer == "yes")
    {return true;}
   if(answer == "no")
    {return false;}
    
    }


//-->
</script>
