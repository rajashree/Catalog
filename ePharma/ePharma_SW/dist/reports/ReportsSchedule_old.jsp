
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
<form name="ScheduleReportResults" action="ReportsScheduleResult.do" method="post">


              <table width="100%" id="Table1" cellSpacing="1" cellPadding="1" align="left" border="0"
			bgcolor="white" class="td-menu">
 <tr bgcolor="white"> 
                  <td class="td-typeblack">Set Report Schedule and Notification Parameters</td>
                </tr> 
                <tr> 
                  <td align="left"> 
				  	<table border="0" width="100%" id="table5" cellpadding="2" cellspacing="2">
					<tr class="tableRow_On">
						<td class="bold">Enter Report Name:&nbsp;&nbsp;
						<input type="text" name="" value=""></td>						

					</tr>
					
					<tr class="tableRow_Off">
						<td class="bold"><br>Set Schedule</td>
					</tr>
					
					<tr>
						<td valign="top">Appointment time:&nbsp;&nbsp;Start&nbsp;<select size="1" name="D3">
						<option>12:00 PM</option>
						<option>01:00 AM</option>
						<option>02:00 AM</option>
						<option>03:00 AM</option>
						<option>04:00 AM</option>
						<option>05:00 AM</option>
						<option>06:00 AM</option>
						<option>07:00 AM</option>
						<option>08:00 AM</option>
						<option>09:00 AM</option>
						<option>10:00 AM</option>
						<option>11:00 AM</option>
						<option>12:00 AM</option>
						<option>01:00 PM</option>
						<option>02:00 PM</option>
						<option>03:00 PM</option>
						<option>04:00 PM</option>
						<option>05:00 PM</option>
						<option>06:00 PM</option>
						<option>07:00 PM</option>
						<option>08:00 PM</option>
						<option>09:00 PM</option>
						<option>10:00 PM</option>
						<option>11:00 PM</option>
						<option>12:00 PM</option>
			</select>&nbsp;&nbsp;End&nbsp;<select size="1" name="D3">
			<option>12:00 PM</option>
			<option>01:00 AM</option>
			<option>02:00 AM</option>
			<option>03:00 AM</option>
			<option>04:00 AM</option>
			<option>05:00 AM</option>
			<option>06:00 AM</option>
			<option>07:00 AM</option>
			<option>08:00 AM</option>
			<option>09:00 AM</option>
			<option>10:00 AM</option>
			<option>11:00 AM</option>
			<option>12:00 AM</option>
			<option>01:00 PM</option>
			<option>02:00 PM</option>
			<option>03:00 PM</option>
			<option>04:00 PM</option>
			<option>05:00 PM</option>
			<option>06:00 PM</option>
			<option>07:00 PM</option>
			<option>08:00 PM</option>
			<option>09:00 PM</option>
			<option>10:00 PM</option>
			<option>11:00 PM</option>
			<option>12:00 PM</option>
			</select>&nbsp;&nbsp;Duration&nbsp;<select size="1" name="D3">
			<option>1/2 hour</option>
			<option>1 hour</option>
			<option>5 hour</option>
			<option>10 hour</option>
			<option>1 day</option>
			</select>
			<p>
			Recurrance pattern:&nbsp;&nbsp;<input name="ON" type="radio" value="ON">&nbsp;Daily&nbsp;&nbsp;<input name="ON" type="radio" value="ON">&nbsp;Weekly&nbsp;&nbsp;<input name="ON" type="radio" value="ON">&nbsp;Monthly&nbsp;&nbsp;<input name="ON" type="radio" value="ON">&nbsp;Yearly
			<p>
			Recur every <input name="" type="text" size="2"> 1 week(s) on:<br>
			<input name="" type="checkbox" value="">Monday&nbsp;&nbsp;<input name="" type="checkbox" value="">Tuesday&nbsp;&nbsp;<input name="" type="checkbox" value="">
Wednesday&nbsp;&nbsp;<input name="" type="checkbox" value="">Thursday&nbsp;&nbsp;<input name="" type="checkbox" value="">Friday&nbsp;&nbsp;<input name="" type="checkbox" value="">Saturday&nbsp;&nbsp;<input name="" type="checkbox" value="">Sunday&nbsp;&nbsp;
						<p>Range of recurrence:&nbsp;&nbsp;Start&nbsp;<select size="1" name="D3">
			<option>Mon 8/5/2005</option>
			</select>&nbsp;&nbsp;<input type="radio" value="1" checked>
			&nbsp;No end date&nbsp;&nbsp;<input name="ON" type="radio" value="On">End after <input name="" type="text" size="2">occurences&nbsp;&nbsp;<input name="ON" type="radio" value="ON">
					</td>
					</tr>
					
					<tr class="tableRow_On">
						<td class="bold">Set Notification Method:&nbsp;&nbsp;<select size="1" name="D2">
			<option>Email</option>
			<option>SMS</option>
			<option>Send to Printer</option>
			<option>Web</option>
			</select></td>
					</tr>
					
					
<tr class="tableRow_Off">
						<td colspan="3"><INPUT name="Submit3" type="submit" class="fButton" id="Submit2" onClick="javascript:return submitReport();" value="Run Report"></td>
					</tr>
					</table>
		</td>
	</tr>

</table>
</form>
</body>
</html>
