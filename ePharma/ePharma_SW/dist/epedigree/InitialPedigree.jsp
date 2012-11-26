<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html >
<head>
<title>Raining Data ePharma - Trading Partner Manager -  Detail</title>


<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}

-->
</script>

<%
 System.out.println("Initial Pedigreeeeeeeee");
String tp_company_nm = (String)request.getParameter("tp_company_nm");
if(tp_company_nm==null)tp_company_nm="";
    String pagenm =  request.getParameter("pagenm");
    if(pagenm==null)pagenm="pedigree";
    String sessionID = (String)session.getAttribute("sessionID");
    
  
%>
<%@include file='topMenu.jsp'%>



<body >

<iframe   name="frame1" src="InitialPedigreeFrame1.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>" height=50% width=100% scrolling=auto align='top' ></iframe>
<iframe   name="frame2" height=50% width=100% scrolling=auto   src="InitialPedigreeFrame2.jsp" height=50% width=100% scrolling=auto align='top' ></iframe>
			
</body>
</html >	