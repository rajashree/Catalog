
<%

String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String sessionID = request.getParameter("sessionID");
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");


%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
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
function submitReport()
{
 ScheduleReportResults.submit();
}

</script>
</head>
<body>

<%@include file='../epedigree/topMenu.jsp'%>
<form name="ReportResults" action="ReportsSchedule.do" method="post">
  
<input type=hidden name="sessionID" value="<%=sessionID%>">
<input type=hidden name="clientIP" value="<%=clientIP%>">
<input type=hidden name="pagenm" value="reports">
<input type=hidden name="tp_company_nm" value="<%=tp_company_nm%>">
  <%
                    	
	            	String str = (String)session.getAttribute("query");
	            	//System.out.println("-------str is---"+(String)request.getAttribute("cubeName"));
	            %>

              <table width="100%" id="Table1" cellSpacing="1" cellPadding="1" align="left" border="0"
			bgcolor="white" class="td-menu">
		 <tr bgcolor="white"> 
                  <td class="td-typeblack">Following Report has been successfuly set:</td>
                </tr> 
                <tr> 
                  <td align="left">
                  <table cellpadding="2" cellspacing="2" border="0" width="100%" id="table9">
                  
	    	<%=str%>

		</td></tr>
		    <tr> 
		      <td width="100%" height="24" bgcolor="#D0D0FF" class="td-menu" align="left">&nbsp;&nbsp;Copyright 
		        2005. Raining Data.</td>
		    </tr>

		</table>
</form>
</body>
</html>
