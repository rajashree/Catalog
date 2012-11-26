<%@ page import="java.util.ArrayList;"%>


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
function goToSchedulePage()
{
ReportResults.submit();
}
function back(){

 document.ReportResults.action ="DisplayReportFilters.do?sessionID&clientIP&pagenm=reports&tp_company_nm=Southwood&cubeName=shippedPedigreeByTradingPartnerPerDateSpan&outputField=PedigreeID&outputField=Manufacturer";
document.ReportResults.submit();
 return true;


}
function runFunction(option){

	document.ReportResults.option.value=option;
	document.ReportResults.submit();
	
	return true;
}
function showPage(row){
	var frm = document.forms[0];
	frm.submit();
	
}

function callAction(off){	
	var frm = document.forms[0];		
	frm.action = "<html:rewrite action='./dist/epedigree/ReceivedFaxes.do'/>";
	document.getElementsByName("offset")[0].value = off;
	frm.submit();
}


</script>
</head>
<body>

<%@include file='../epedigree/topMenu.jsp'%>



<form name="ReportResults" action="Pagenation.do" method="post">
<input type=hidden name="sessionID" value="<%=sessionID%>">
<input type=hidden name="clientIP" value="<%=clientIP%>">
<input type=hidden name="pagenm" value="reports">
<input type=hidden name="tp_company_nm" value="<%=tp_company_nm%>">

              <table width="100%" id="Table1" cellSpacing="1" cellPadding="1" align="left" border="0"
			bgcolor="white" class="td-menu">
				<tr bgcolor="white"> 
 		<td width="1%" valign="middle" class="td-rightmenu"><img src="../../assets/images/space.gif" width="10" height="10"></td>
</tr>   
			
			
		 
		 <tr bgcolor="white"> 
                  <td class="td-typeblack">Results</td>
                </tr> 
                <tr> 
                  <td align="left">
                  <table align ="center" cellpadding="2" cellspacing="1" border="0" width="100%" id="table9">
                 <tr>
                     </tr>       
                 <%
                 ArrayList reportList = (ArrayList)session.getAttribute("showReport");
                 int length= reportList.size();
                 boolean flag=true;
               	 String temp[] =(String[])reportList.get(0);
               	 request.setAttribute("collNames",temp);
                %>
                	<tr class="tableRow_Header">
                	
                	
                	<logic:iterate id="dbName" name="collNames" type="java.lang.String" >
                	<td align='center'><bean:write name="dbName" /></td>
                	</logic:iterate>
                	</tr>
                <%	
                	request.removeAttribute("collNames");
                	String  fromString=(String) request.getAttribute("from");
                	String toString = (String) request.getAttribute("to");
                	if(fromString == null ) fromString="1";
                	if(toString == null ) toString ="16" ;
                	int from = Integer.parseInt(fromString);
                	int to = Integer.parseInt(toString);
                 if(length <15)
                 to=length;
                 if(length!=1){
                	for(int i=from;i<to;i++) {
                	temp= (String[])reportList.get(i); 
                	request.setAttribute("collNames",temp);
               	
                	%>
                <tr class='tableRow_On'>
               		<logic:iterate id="dbName" name="collNames" type="java.lang.String" >
                	<td align=center><bean:write name="dbName" /></td>
                	</logic:iterate>
            	</tr>
               <% 
               	request.removeAttribute("collNames");
                }
               }else if(length==1){%>
               <tr class="tableRow_Off" align="right"><td  colspan="100%" align="center">No Reports</td></tr>
                <%}
                 if((length-1)>16 ){ 
                		String hide = (String) request.getAttribute("Hide");
                		if(hide == null ) hide="";
                		if(hide.equals("")||hide.equals("Previous")){
                		
                %>
                	   <tr class="tableRow_On"><td colspan="100%" ><a href="#" onClick= "return runFunction(0);">First</a>&nbsp;&nbsp;<a href="#" onClick= "return runFunction(1);">Next</a> &nbsp;&nbsp;<a href="#" onClick= "return runFunction(3);">Last</a>&nbsp;&nbsp;</td></tr>
                	<%} else if (hide.equals("Next")){ %>
                 	   <tr class="tableRow_On"><td colspan="100%" ><a href="#" onClick= "return runFunction(0);">First</a>&nbsp;&nbsp;<a href="#" onClick= "return runFunction(2);">Previous</a>&nbsp;&nbsp;</tr>
                	<%}else {%>
                   <tr class="tableRow_On"><td colspan="100%" ><a href="#" onClick= "return runFunction(0);">First</a>&nbsp;&nbsp;<a href="#" onClick= "return runFunction(1);">Next</a> &nbsp;&nbsp;<a href="#" onClick= "return runFunction(2);">Previous</a>&nbsp;&nbsp;<a href="#" onClick= "return runFunction(3);">Last</a>&nbsp;&nbsp;</td></tr>
                	
              <% }} 
                %>
			
	    		 <tr  class="tableRow_Off" >
				<td  colspan="100%" align="center">
				
			<input type='button' name='Back' value='Back' onclick="return back();" />
			
				</td>
			</tr>

  
           
  
  
  
		</td></tr></table>
		<input type="hidden" name="pageNumber" value="0" />
		<input type="hidden" name="option" value="0" />
		<input type="hidden" name="from" value=<%=from%> />
		<input type="hidden" name="to" value=<%=to%> />
		<input type="hidden" name="length" value=<%=length%> />
		
		
</form>


 
	<jsp:include page="../globalIncludes/Footer.jsp" />
			
		



</body>
</html>
