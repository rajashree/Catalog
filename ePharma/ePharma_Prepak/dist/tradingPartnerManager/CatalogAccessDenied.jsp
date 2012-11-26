 

<%
String sessionID = (String)session.getAttribute("sessionID");
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");
System.out.println("inside access denied");
%>

<html>
	<head>
		<title>Raining Data ePharma - Trading Partner Manager</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="./assets/epedigree1.css" rel="stylesheet" type="text/css">
<body>
		<br><br><br>
	
<%@include file='../epedigree/topMenu.jsp'%>
		
			<form action="/ePharma/TradingPartnerList.do?tp_company_nm=&pagenm=TPManager" method="post">
					
					<table border="0" cellspacing="0" cellpadding="0" width="80%" ID="Table1" align="center">
			<tr>
				<td></td>
				<td rowspan="2">&nbsp;</td>
			</tr>
			<tr>
			<table border="0" cellpadding="0" cellspacing="1" width="100%" ID="Table9">
								</table>
				<td>
					<!-- info goes here -->
					<table width="100%" id="Table2" cellSpacing="1" cellPadding="1" align="left" border="0"
						bgcolor="white">
						<tr bgcolor="white">
							<td class="td-typeblack"><center><STRONG>ACCESS DENIED...!</STRONG></center></td>
						</tr>
						<tr bgcolor="white"><td></td></tr>
						<tr bgcolor="white">
							<td class="td-typeblack" align="center"><input type="submit" name="submit1" value="Go Back"></td>
						</tr>
						<br><br>
						<Td colspan="2"><br>
						<table border="0" cellpadding="0" cellspacing="1" width="100%" ID="Table9">
						</table><br>
						<TABLE id="Table10" cellSpacing="1" cellPadding="0" width="100%" border="0">
									
	    
						<TR class="tableRow-On">
							<TD class="td-menu"></TD>
							<TD class="td-menu"><center><html:button property="button1" value="Go Back" onclick="window.close()" /></center></TD>
						</TR>
					</table>
							
			</form>
		
	</body>
</html>