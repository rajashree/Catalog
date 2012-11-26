 

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
						
						 <form action="/ePharma/dist/login-dist.html" target="_top" method="post">
							<td class="td-typeblack" align="center"><input type="submit"  value="Go Back"></td>
						</form>
						</tr>
						<br><br>
						<Td colspan="2"><br>
						<table border="0" cellpadding="0" cellspacing="1" width="100%" ID="Table9">
						</table><br>
						<TABLE id="Table10" cellSpacing="1" cellPadding="0" width="100%" border="0">
									
	    
					</table>
							
			
		
	</body>
</html>