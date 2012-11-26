 

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
	
<frameset name ="topf" border="1" rows="75,*" >
    <frame noresize scrolling=no name="top1" src="./SchemaElementNewTop.do?tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>" />
	 <frame noresize scrolling=no name="top2" src="./AccessDenied.do?tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>" />
</frameset>


 
</html>