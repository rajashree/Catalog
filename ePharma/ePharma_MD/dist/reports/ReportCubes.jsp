
<%

String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String sessionID = request.getParameter("sessionID");
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");


%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType = "text/html; charset=iso-8859-1" %>

<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>



<html>
<head>
<title>Raining Data ePharma - Reports</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}
//-->
function gotToFilterPage()
{
 var oneChecked = false;
 
 for(i=0;i<DisplayCubeForm.radio1.length;i++)
 {
 	if(DisplayCubeForm.radio1[i].checked)
 	oneChecked = true;
 }
 if(!oneChecked)
 {
 	alert('Please select a report cube');
 	return false;
 }
 //alert('value of action--'+<%=tp_company_nm%>);
 //document.DisplayCubeForm.action = "DisplayReportOutputFields.do?sessionID="+<%=sessionID%>+"&tp_company_nm="+ <%=tp_company_nm%>+"&pagenm=reports";
 
 document.DisplayCubeForm.submit();
 return false;
 
}

</script>
</head>
<body>

<%@include file='../epedigree/topMenu.jsp'%>
<form name= "DisplayCubeForm"  action="DisplayReportOutputFields.do">
<input type=hidden name="sessionID" value="<%=sessionID%>">
<input type=hidden name="clientIP" value="<%=clientIP%>">
<input type=hidden name="pagenm" value="reports">
<input type=hidden name="tp_company_nm" value="<%=tp_company_nm%>">


  


<table width="100%" id="Table1" cellSpacing="1" cellPadding="1" align="left" border="0"
			bgcolor="white" class="td-menu">
   	
<tr><td width="1%" valign="middle" class="td-rightmenu"><img src="../../assets/images/space.gif" width="10" height="10"></td>
</tr>   	
       

   	
   	<tr bgcolor="white"> 
                  <td class="td-typeblack">Please choose a Report Cube</td>
        </tr> 
        <tr> 
                  <td align="left">

        <table cellpadding="2" cellspacing="2" border="0" width="100%" id="table9">
       
        <logic:iterate name="reportCube" id="repCube" type="com.rdta.epharma.reports.form.ReportCubesForm">
	   <tr class='tableRow_On'>
	    <td width='10%' ><input type=radio name="radio1"  value='<bean:write name="repCube" property="key"/>' ></radio></td>
	   <td width='90%' ><bean:write name="repCube" property="name"/></td>
	    </tr>
	    </logic:iterate>
       
       
       
       <tr class="tableRow_Header">
		<td colspan="3" align="center">
		<INPUT name="Submit3" type="submit" class="fButton" id="Submit2" onClick="javascript:return gotToFilterPage();" value="Select">
		</td>
	</tr>

	</table>

	</td></tr>	
	<tr>
		      <td >&nbsp;</td>
	</tr>
</table>
</form>
<jsp:include page="../globalIncludes/Footer.jsp" />
</body>
</html>
