
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


<style type="text/css">@import "../../dist/epedigree/calendar-win2k-1.css";</style>

<script language="JavaScript" src="../../dist/epedigree/calendar.js"></script>
<script language="JavaScript" src="../../dist/epedigree/calendar-en.js"></script>
<script language="JavaScript" src="../../dist/epedigree/calendar-setup.js"></script>
<script language="JavaScript" src="../../dist/epedigree/callCalendar.js"></script>	



<script language="JavaScript" type="text/JavaScript">
<!--
function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}
//-->
function submitPage()
{
 
 if(DisplayReportFiltersForm.from.value != "" && DisplayReportFiltersForm.to.value == "" ){
 alert("Please Enter To Date !..");
 	return false;
 }else if(DisplayReportFiltersForm.from.value == "" && DisplayReportFiltersForm.to.value != ""  ){
 alert("Please Enter From Date !..");
 return false;
 }
 
 
 
 DisplayReportFiltersForm.submit();
}

function clearPage(){
	
	DisplayReportFiltersForm.from.value="";
	DisplayReportFiltersForm.to.value="";
	DisplayReportFiltersForm.key.value="";
	
	
	return false;
}

</script>
</head>
<body>
<%@include file='../epedigree/topMenu.jsp'%>

<form name= "DisplayReportFiltersForm"  action="SubmitReportFilters.do">
<input type=hidden name="sessionID" value="<%=sessionID%>">
<input type=hidden name="clientIP" value="<%=clientIP%>">
<input type=hidden name="pagenm" value="reports">
<input type=hidden name="tp_company_nm" value="<%=tp_company_nm%>">
          <table width="100%" id="Table1" cellSpacing="1" cellPadding="1" align="left" border="0"
			bgcolor="white" class="td-menu">
 		
 		<tr bgcolor="white"> 
 		<td width="1%" valign="middle" class="td-rightmenu"><img src="../../assets/images/space.gif" width="10" height="10"></td>
</tr>   
                  <td class="td-typeblack">Create Report Filter</td>
                <tr> 
                  <td align="left"> <TABLE id="Table3" cellSpacing="2" cellPadding="2" border="0" align="center" width="100%" class="td-menu">
						<TR class="tableRow_Header">
							  <TD class="type-whrite" align="center"> <STRONG>SEARCH</STRONG></TD>
							<TD class="type-whrite" align="center">

								<STRONG>VALUE</STRONG>
							</TD>
							<TD class="type-whrite" align="center">
								<STRONG>CRITERIA</STRONG>
							</TD>
						</TR>
		<%boolean flag=false;%>				
		<logic:iterate name="filterForm" id="filterForm" type="com.rdta.epharma.reports.form.ShowReportFielterForm">
	<%	if(flag){%> 		<td><select name="select">
 			<option value="or" selected >OR</option>
 			<option value="and">AND</option>
 			
 			</select>
 		</td>
 		</tr>
	<%}else{flag=true;}%>
	
	   <tr class='tableRow_On'>
	    <td><bean:write name="filterForm" property="fieldName"/></td>
 		<input type='hidden' name="elements" value='<bean:write name="filterForm" property="fieldName"/>'\>
 		<input type='hidden' name="keyelements" value='<bean:write name="filterForm" property="key"/>'\>
 	
 	<logic:equal name="filterForm" property="fieldName" value='<%="Drug Name"%>' >
 	<td><input type="text" name="key"  /></td>
 	</logic:equal>
 	
 		<logic:equal name="filterForm" property="fieldName" value='<%="To Name/Company"%>' >
 		<td><select name="key">
 		<option value=""></option>
 		<logic:iterate name="tradingList" id="tlist">
 		<option value='<bean:write name="tlist" />'><bean:write name="tlist" /></option>
 		</logic:iterate>
 		</select></td>
 		
 		</logic:equal>
 		
 		
 		
 		
 		
 		<logic:equal name="filterForm" property="fieldName" value='<%="Date Received(yyyy-mm-dd):"%>' >
 		<td>From:
 		<input type="text" name="from" maxlength="22" size="20" readonly="true"><img src="<html:rewrite page='/IMG/img.gif' />" width="16" height="16" onclick="return showCalendar('from', '%Y-%m-%d', '24', true);">		
 		 To :
 		 <input type="text" name="to" maxlength="22" size="20" readonly="true"><img src="<html:rewrite page='/IMG/img.gif' />" width="16" height="16" onclick="return showCalendar('to', '%Y-%m-%d', '24', true);">		</td>
 		</logic:equal>
 		
 		<logic:equal name="filterForm" property="fieldName" value='<%="Date Status Changed(yyyy-mm-dd)"%>' >
 		<td>From:
 		<input type="text" name="from" maxlength="22" size="20" readonly="true"><img src="<html:rewrite page='/IMG/img.gif' />" width="16" height="16" onclick="return showCalendar('from', '%Y-%m-%d', '24', true);">		
 		 To :
 		 <input type="text" name="to" maxlength="22" size="20" readonly="true"><img src="<html:rewrite page='/IMG/img.gif' />" width="16" height="16" onclick="return showCalendar('to', '%Y-%m-%d', '24', true);">		</td>
 		
 		</logic:equal>
 	
 		<logic:equal name="filterForm" property="fieldName" value='<%="Status"%>' >
 		
 		<td><select name="selectstatus">
 			<option value=""></option>
 			<logic:iterate name="statushow" id="statushow" >
 			<option value='<bean:write name="statushow" property="status"/>'><bean:write name="statushow" property="status" /></option>
 			</logic:iterate>
 			</select></td>
 		
 		
 		
 		</logic:equal>
 	
	
	    
	    </logic:iterate>
					<td><td></tr>	
						
						
        				<tr class="tableRow_Header">
						<td colspan="3" align="center">
						<INPUT name="Submit3" type="submit" class="fButton" id="Submit2" onClick="javascript:return submitPage();" value="Report">
						
						&nbsp;&nbsp;&nbsp;&nbsp;<INPUT name="Submit3" type="reset" class="fButton_off"   value="Reset">
						</td>
						</tr>
<tr>
		      <td >&nbsp;</td>
	</tr>					
					</TABLE>

</td>
        </tr>
      </table>
</form>

<jsp:include page="../globalIncludes/Footer.jsp" />



</body>
</html>
