
<%
try{
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String sessionID = request.getParameter("sessionID");
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");


%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
%@ page language="java" pageEncoding="UTF-8"%>
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
function goToFilterPage()
{
 
 	var field = ReportOutputForm.outputField;
 	var check=false;
 	
 	for(i=0;i<field.length;i++)
 	{
 		if(field[i].checked == true )
 		check=true;
 	
 	}
 	if(check){
 	ReportOutputForm.submit();
	}else{
		alert("Please Select atleast One OutPut field!..");
	}
}

var checkflag = "false";

function checkAll() 
{

var field = ReportOutputForm.outputField;
if (checkflag == "false") {
for (i = 0; i < field.length; i++) {
field[i].checked = true;}
checkflag = "true";
return "Uncheck All"; }
else {
for (i = 0; i < field.length; i++) {
field[i].checked = false; }
checkflag = "false";
return "Check All"; }
}


</script>
</head>
<body>


<form name="ReportOutputForm" action="DisplayReportFilters.do">

<input type=hidden name="sessionID" value="<%=sessionID%>">
<input type=hidden name="clientIP" value="<%=clientIP%>">
<input type=hidden name="pagenm" value="reports">
<input type=hidden name="tp_company_nm" value="<%=tp_company_nm%>">
<input type="hidden" name="cubeName" value="<%=(String)request.getAttribute("cubeName")%>">
<%@include file='../epedigree/topMenu.jsp'%>
       <table width="100%" id="Table1" cellSpacing="1" cellPadding="1" align="left" border="0">
       
	<tr bgcolor="white"> 
 		<td width="1%" valign="middle" class="td-rightmenu"><img src="../../assets/images/space.gif" width="10" height="10"></td>
</tr>   
			
 <tr bgcolor="white"> 
                  <td class="td-typeblack">Please choose the Report Format Output Fields</td>
                </tr> 
                <tr> 
                  <td align="left"> <table cellpadding="2" cellspacing="2" border="0" width="100%" id="table9">
	<tr class="tableRow_Off">
		<td>&nbsp;
		Select All:</td>
		<td colspan='3' align='left'><input type='checkbox' onClick="javascript:return checkAll();" name='selectAll' value='selectAll' />
		</td>
	</tr>
 
 	
 	<%boolean flag=true;%>
 
      <logic:iterate name="outPutFields" id="outField" type="com.rdta.epharma.reports.form.OutPutFieldForm">
      <% if(flag){ out.println("<tr class='tableRow_On' colspan='100%'>");}%>
       
	   
	    <td ><input type=checkbox name="outputField"  value=<bean:write name="outField" property="key"/> ></radio></td>
	   <td  ><bean:write name="outField" property="name"/></td>

	    <% if(!flag){ out.println("</tr>");}%>
	    
	   
	    
	    <% flag = !flag; %>
	   </logic:iterate>
       <%if(!flag)%>
       <td colspan='100%'></td></tr>
       
      
	<tr class="tableRow_Off">
		<td colspan="4">
		<INPUT name="back" type="button" class="fButton_large" id="back" onClick="javascript:history.back();" value="<< Back" >
		&nbsp;<INPUT name="Next" type="button" class="fButton_large" id="next" onClick="javascript:return goToFilterPage();" value="Next >>" ></td>
	</tr>	<tr>
		      <td >&nbsp;</td>
	</tr>

</table>

</form>
<jsp:include page="../globalIncludes/Footer.jsp" />
</body>
</html>
<%}catch(Exception e){e.printStackTrace();}%>