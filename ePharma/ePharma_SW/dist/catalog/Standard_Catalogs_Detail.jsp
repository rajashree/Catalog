<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.commons.xml.XMLUtil"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.catalog.session.TradingPartnerContext"%>
<%@ page import="com.rdta.catalog.Constants"%>

<%
	String tp_company_nm = request.getParameter("tp_company_nm");
	if(tp_company_nm == null) tp_company_nm="";
	String pagenm = request.getParameter("pagenm");
	if(pagenm == null ) pagenm="Catalog";
	String sessionID = (String)session.getAttribute("sessionID");
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Raining Data ePharma - GCPIM </title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="./assets/epedigree1.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}
function checkStatus(){

	alert("AccessDenied !..");
	return false;
}

function checkAccess(){
	alert("AccessDenied !..");
	return false;

}

//-->
</script>
</head>
<body>

<%@include file='../epedigree/topMenu.jsp'%>
<%	TradingPartnerContext context = (TradingPartnerContext)session.getAttribute(Constants.SESSION_TP_CONTEXT); 
  //  System.out.println("Context Object *********"+context);
   // String tpName = context.getTpName();
   // String tpGenId = context.getTpGenId();
   //System.out.println("TP Name is*****"+tpName);
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


 
  
       
            <!-- info goes here -->
           
           
           
           
           
	        <table width="100%" cellSpacing="0" cellPadding="0" align="left" border="0" bgcolor="white">
                <tr bgcolor="white"> 
                  <td class="td-typeblack">List of Standard Catalogs of Cardinal Health</td>
				<%   String newstr = (String) session.getAttribute("manageInsert");
					if(newstr.equals("true")){%>
		         <%}else{%>
                <%}%>
                
                </tr>
                <tr> 
                  <td align="left">
					<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">				
					<tr class="tableRow_Header">
					<td class="type-whrite">Name</td>
					<td class="type-whrite">Description</td>
					<td class="type-whrite">Updated Date</td>

							

				</tr>


				
				
				<!--Here the problem is existing-->
				
				<%
					List list =  (List)request.getAttribute("StandardCatalogListInfo");

					String catalogName = "";
					String genId = "";
					String description = "";
					String updatedDate = "";
				
					int size = list.size();
					if( size > 0 ){
					for(int i=0; i < size; i++) {
					//	System.out.println("******************Startcataloginformation*************");
						Node catalogNode = XMLUtil.parse((InputStream) list.get(i) );

						genId = CommonUtil.jspDisplayValue(catalogNode,"catalogID");

						catalogName = CommonUtil.jspDisplayValue(catalogNode,"catalogName");
						description = CommonUtil.jspDisplayValue(catalogNode,"description");
						updatedDate = CommonUtil.jspDisplayValue(catalogNode,"//updated-timestamp");
					//	System.out.println("******************ENDcataloginformation*************");

			   %>

				<tr class="tableRow_On">
				
				<% String str = (String )session.getAttribute("manageRead");
					if( str.equals("true")) {%>
					<td><a href="GCPIMStandardCatalog.do?operationType=FIND&catalogGenId=<%= genId%>&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>" class="typered-bold-link">
					<%= catalogName %></a></td>
					<%}else{%>
					<td><a onClick =" return checkStatus();" href=""><%= catalogName %></a></td> 
					<%}%>
					<td><%= description %></td>
					<td><%= updatedDate %></td>
					<!--
					<td><a href="OpenCatalogSchemaDef.do?fromModule=TP&catalogGenId=<%= genId%>" class="typered-bold-link">
					Schema Def</a>  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
					<a href="MapCatalogSchemas.do?leftCatalogGenId=<%= genId%>&rightCatalogGenId=StandardGenId1000
					">Map With Standrad Schema</a></td>
					-->
					<!--Here Iam making changes-->
					<!--
					<a href="SelectMasterCatalog.do?operationType=FIND&leftCatalogGenId=<%= genId%>&rightCatalogGenId=StandardGenId1000
					">Map With Standrad Catelog</a>
					</td>-->

					
	
				</tr>
				
				
                <%

					}
					}else{
				//	System.out.println("");
                 %>
				
							<tr class="tableRow_On"><td colspan='3'  align='center'>There are no catalogs.... </td></tr>
				<%}%>
			
				</TABLE>
           
           
           
            </td>
        </tr>

 

</table> 
<jsp:include page="../globalIncludes/Footer.jsp" />

</table>
</div>  

</body>
</html>




