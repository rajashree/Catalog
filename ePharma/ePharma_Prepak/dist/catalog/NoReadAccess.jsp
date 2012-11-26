<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Raining Data ePharma - GCPIM</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="./assets/epedigree1.css" rel="stylesheet" type="text/css">

</head>
<body>

<%
	String tp_company_nm = request.getParameter("tp_company_nm");
	if(tp_company_nm == null){
		tp_company_nm = (String)request.getAttribute("tp_company_nm");
	}	
	request.setAttribute("tp_company_nm", tp_company_nm);
	
	if ( tp_company_nm == null) tp_company_nm="";
	
	String pagenm = request.getParameter("pagenm");
	if(pagenm == null){
		pagenm = (String)request.getAttribute("pagenm");		
	}	
	request.setAttribute("pagenm", pagenm);
	
	String sessionID = (String)session.getAttribute("sessionID");
%>
<%@include file='../epedigree/topMenu.jsp'%>
<div id="rightwhite">
<table width="100%" border="0">
<tr> 
    <td width="100%" valign="top" class="td-rightmenu" style="height:0px;">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td align="left"></td>
			<td align="right"><img src="assets/images/space.gif" width="3"></td>
		</tr>
	</table>
    </td>
 </tr>
</table>


<form> 

<br>
<br>
<br>
<br>
<font size="3"><center><b>    Access Denied !!!...... </b></center></font>
<br>
<center><input type="button" value="Go Back" onclick="javascript:history.back()"></center>
<br>
<br>
<br>

<tr>
<tr>
<tr>        
               
<jsp:include page="../globalIncludes/Footer.jsp" />

</body>
</html>        