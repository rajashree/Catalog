<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>


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
//-->
</script>
</head>


<%
 //String tpGenId=request.getParameter("tpGenId");
	String tpGenId=(String)request.getAttribute("tpGenId");
// System.out.println("***************_____________--------------tpGenId"+request.getAttribute("tpGenId"));
 String catalogId = request.getParameter("catalogGenId");

 
 
 
 if(catalogId == null ) {

	 //try from session since we are using form type as multipart/form-data
	 //in case of creating schema from upload process
	 if(session!= null) {

	//	 System.out.println(" Inside session " );
		catalogId = (String) session.getAttribute("SESSION_CATALOGID");
		

	 }
 }







String  typeop =(String) request.getAttribute("typeop");
if ( typeop == null ) typeop=""; 
//System.out.println("typeop raa edi "+typeop);

String cexists = (String) request.getAttribute("dupexists");

if( cexists == null )
cexists="false";
else if( cexists.equals("true"))
{ System.out.println("DUP EXISTS"); }

String xpath = request.getParameter("xpath");
String catalogGenId =request.getParameter("catalogGenId");
String optype=request.getParameter("operationType");
String savetree =  (String) session.getAttribute("savetree");

if( savetree == null )savetree="false";
request.setAttribute("savetree",savetree);

System.out.println("Name  of the element ="+request.getParameter("name"));
String path1= (String) request.getAttribute("path1");
String testOrder = (String) request.getAttribute("testOrder");
if(testOrder == null ) testOrder="";
String help = (String) request.getAttribute("help");
if(help == null) help ="";
%>

<frameset border="1" rows="75,*"> 
<frame  scrolling = 'no' name="top" src="./editCatalogNewTop.do?tp_company_nm=&pagenm=Catalog" />
<frameset border="1" cols="230,*" frameborder="10">	
	<frame  NORESIZE scrolling=auto name="menu" src="./GCPIMSchemaElementTree.do?tp_company_nm=&pagenm=Catalog&catalogGenId=<%=catalogId%>&tpGenId=<%=tpGenId%>&fromModule=<%=request.getParameter("fromModule")%> ">
	<% if(!help.equalsIgnoreCase("true")){%>
		<frame   NORESIZE scrolling=auto name="main" src="./GCPIMMasterProduct.do?optype=<%=optype%>&savetree=<%=savetree%>&name=<%=request.getParameter("name")%>&xpath=<%="Product"%>&exist=<%=cexists%>&tp_company_nm=&pagenm=Catalog&catalogGenId=<%=catalogId%>&tpGenId=<%=tpGenId%>&fromModule=<%=request.getParameter("fromModule")%>&xpath=<%= path1 %>&typeop=<%=typeop%>">
	<%}else{%>
			<frame   NORESIZE scrolling=auto name="main" src="./GCPIMStandardSchemaElementEdit.do?exist=<%=cexists%>&tp_company_nm=&pagenm=Catalog&catalogGenId=<%=catalogId%>&tpGenId=<%=tpGenId%>&fromModule=<%=request.getParameter("fromModule") %>&typeop=<%=typeop%>">
	<%}%>
</frameset>
</frameset>

</html>