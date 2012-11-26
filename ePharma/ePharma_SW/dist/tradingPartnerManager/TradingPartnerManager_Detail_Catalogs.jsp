<%@ taglib uri="/struts-html" prefix="html" %>

<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.commons.xml.XMLUtil"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.catalog.session.TradingPartnerContext"%>
<%@ page import="com.rdta.catalog.Constants"%>
<%@ page import="com.rdta.commons.persistence.QueryRunner"%>
<%@ page import="com.rdta.commons.persistence.QueryRunnerFactory"%>



<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
<head>
<title>Raining Data ePharma - Trading Partner Manager</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="./assets/epedigree1.css" rel="stylesheet" type="text/css">
<%

    TradingPartnerContext context = (TradingPartnerContext)session.getAttribute(Constants.SESSION_TP_CONTEXT); 
    String tpName = context.getTpName();
    String tpGenId = context.getTpGenId();
    String tp_company_nm = request.getParameter("tp_company_nm");
    String pagenm =  request.getParameter("pagenm");
    String sessionID = (String)session.getAttribute("sessionID");
%>
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}


function SchemaDef(frm)
{
if(CatalogAccessPrivilage())
{
  
   var allchecks = document.getElementsByName('catalogGenId');

   var checkVal ='';
   var checkSel =false;
    
  for(i=0;i<allchecks.length;i++){
   if( allchecks[i].checked ){
     
     checkSel=true; 
     checkVal = ( checkVal.length == 0 ? '' : '&' ) +  allchecks[i].name + "=" + allchecks[i].value;
     
      document.catalogDetails.action = "OpenCatalogSchemaDef.do?fromModule=TP&&tp_company_nm="+"<%=tp_company_nm%>"+"&pagenm="+"<%=pagenm%>"+"&tpList=view&tcList=view&"+checkVal;
	 
	  document.catalogDetails.submit();
     
    }
    
   
 }
    
     if(checkSel==false)
    {alert("Please select one catalog");
    }
   }
 
}
function MapAttributes(frm)
{
if(CatalogAccessPrivilage())
{
  
   var allchecks = document.getElementsByName('catalogGenId');

   var checkVal ='';
   var checkSel =false;
    
  for(i=0;i<allchecks.length;i++){
   if( allchecks[i].checked ){
     checkSel=true; 
     checkVal = ( checkVal.length == 0 ? '' : '&' ) +  "leftCatalogGenId" + "=" + allchecks[i].value;
     
     document.catalogDetails.action = "SelectMasterCatalog.do?fromModule=TP&tpGenId=<%=tpGenId%>&tp_company_nm="+"<%=tp_company_nm%>"+"&pagenm="+"<%=pagenm%>"+"&tpList=view&tcList=view&"+checkVal;
	 
	document.catalogDetails.submit();
     
    }
     
 }
    if(checkSel==false)
    {alert("Please select one catalog");
    }
 }   
    
 
}

function myfunction1()
{

<%
String Access=(String)request.getAttribute("CatalogAccess");
System.out.println("ACCESS"+Access);
if( Access == null )Access ="";
if(Access.equals("false")){%>
alert("Access Denied ...!");
window.open("TPCatalogList.do?tpGenId=<%=tpGenId%>&tp_company_nm=&pagenm=TPManager","_self");
<%} else{%>


<%}%>
return true;
}
//-->
</script>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
-->
</style></head>
<body onLoad="myfunction1()">

<%@include file='../epedigree/topMenu.jsp'%>



<div id="rightwhite2">
<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
  <tr> 
    <!-- Messaging -->
    <td width="100%" valign="middle" class="td-rightmenu">
	
	<jsp:include page="./TPHeader.jsp" />
	  
	  
	  </td>
  </tr>
  <tr> 
    
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
              <table width="100%" cellSpacing="0" cellPadding="0" align="left" border="0"
			bgcolor="white">
                <tr bgcolor="white"> 
                  <td class="td-typeblack">List of Catalogs of the Trading Partner :<%=tpName%> </td>

				  <td><!-- <a href="CatalogNew.do?tpGenId=<%=tpGenId%>&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>" class="typered-bold-link" >Create New Catalog</a>
				  --></td>
                </tr>
                <tr> 
                  <td align="left">
					<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">				
					<tr class="tableRow_Header">
					<td width="4%" class="type-whrite"><div align="left">Select</div></td>
					<td width="22%" class="type-whrite"><div align="center">Name</div></td>
					<td width="41%" class="type-whrite"><div align="center">Description</div></td>
					<td width="24%" class="type-whrite"><div align="center">Updated Date</div></td>
				</tr>


				<%
					List list =  (List)request.getAttribute("CatalogListInfo");

					String name = "";
					String genId = "";
					String description = "";
					String updatedDate = "";
					if(list.size()==0)
					{
					
					%>
				<tr class="tableRow_On">
					 
					<td  colspan="4" class='td-content' align="center" > There are no Catalogs</td>
				</tr>
                
                <%}else
                {
					for(int i=0; i < list.size(); i++) {
					
						Node catalogNode = XMLUtil.parse((InputStream) list.get(i) );

						genId = CommonUtil.jspDisplayValue(catalogNode,"catalogID");

						name = CommonUtil.jspDisplayValue(catalogNode,"catalogName");
						description = CommonUtil.jspDisplayValue(catalogNode,"description");

						updatedDate = CommonUtil.jspDisplayValue(catalogNode,"//updated-timestamp");

			   %>


				<tr class="tableRow_On">
				
				    <form name="catalogDetails" method="post">
				      <div align="center"></div>
				  
				    <td><div align="center">
				      <input type="radio" name="catalogGenId" value="<%= genId%>" />				    
			        </div></td>
					<td><div align="center"><a href="Catalog.do?operationType=FIND&catalogGenId=<%= genId%>&tpGenId=<%=tpGenId%>&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>&tpList=view&tcList=view&catSchema=view" class="typered-bold-link" onClick="return CatalogAccessPrivilage()">
					  <%= name %></a></div></td>
					<td><div align="center"><%= description %></div></td>
					<td width="9%"><div align="center"><%= updatedDate %></div></td>
<!-- 
					<td>
					
					<a href="OpenCatalogSchemaDef.do?fromModule=TP&catalogGenId=<%= genId%>&tp_company_nm=<%=tp_company_nm%>&pagenm=TPManager&tpList=view&tcList=view" class="typered-bold-link" onClick="return CatalogAccessPrivilage()">
					Schema Def</a>  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
			         <a href="SelectMasterCatalog.do?leftCatalogGenId=<%= genId%>&tpGenId=<%=tpGenId%>&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>&tpList=view&tcList=view" onClick="return CatalogAccessPrivilage()">Map Attributes To Standard Catalog</a> 
                       
					</td>
                   
					 -->
	
				
				

                <%

					}
			%>		
					</TABLE> 
					<div align="center"><br>
					  <INPUT type="button" value="Schema Def" onClick="return SchemaDef(this);">
					  &nbsp;&nbsp;
					  <INPUT type="button" value="Map Attributes To Standard Catalog" onClick="MapAttributes(this)"> 
					  </form>
					  <%	}
                 %>
					  
			              </div></td>
        </tr>
      </table>



              <jsp:include page="../globalIncludes/Footer.jsp" />


</body>
</html:html>

<script language="JavaScript" type="text/JavaScript">
<!--


function CatalogAccessPrivilage()
{

<%
 String sessionID = (String)session.getAttribute("sessionID");
		QueryRunner queryrunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
        List accessList = queryrunner.returnExecuteQueryStrings("tlsp:validateAccess('"+sessionID+"','4.03','Read')");
		String viewStatus = accessList.get(0).toString();
		System.out.println("The Read is "+viewStatus);
       if(viewStatus.equals("false"))
       {
%>

alert("Access Denied ....")
return false;
<%}
if(viewStatus.equals("true")){%>
return true;
<%}
 %>

}

function CatalogUpdatePrivilage()
{

<%
  	    accessList = queryrunner.returnExecuteQueryStrings("tlsp:validateAccess('"+sessionID+"','4.03','Update')");
		viewStatus = accessList.get(0).toString();
		System.out.println("The Update is "+viewStatus);
       if(viewStatus.equals("false"))
       {
%>

alert("Access Denied ....")
return false;
<%}
if(viewStatus.equals("true")){%>
return true;
<%}
 %>

}


//-->
</script>
