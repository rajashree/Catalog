
<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>


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

<%String tp_company_nm = request.getParameter("tp_company_nm");
    String pagenm =  request.getParameter("pagenm");
    String sessionID = (String)session.getAttribute("sessionID");
%>
<%

 String catalogId = request.getParameter("catalogGenId");

 if(catalogId == null ) {

	 //try from session since we are using form type as multipart/form-data
	 //in case of creating schema from upload process
	 if(session!= null) {

		 
		catalogId = (String) session.getAttribute("SESSION_CATALOGID");

	 }
 }

 


%>
<frameset border="1" rows="75,*" >
    <frame scrolling=no name="top" src="./SchemaElementNewTop.do?tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>" />
	<frameset border="1" cols="230,*">	

		<frame NORESIZE scrolling=auto name="menu" src="./SchemaElementTree.do?catalogGenId=<%=catalogId%>&fromModule=<%=request.getParameter("fromModule")%>&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%> ">
		<frame NORESIZE scrolling=auto name="main" src="./SchemaElementEdit.do?catalogGenId=<%=catalogId%>&fromModule=<%=request.getParameter("fromModule") %>&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>">
	</frameset>
</frameset>



</html>