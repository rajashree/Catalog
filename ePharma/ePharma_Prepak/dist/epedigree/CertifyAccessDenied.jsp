<%@ include file='../../includes/jspinclude.jsp'%>


<%
String sessionID = (String)session.getAttribute("sessionID");
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");
System.out.println("********Inside access denied*******");
%>

<html>
	<head>
		<title>Raining Data ePharma - ePedigree</title><LINK href="../../assets/epedigree1.css" type="text/css" rel="stylesheet"></head>
	<body bgcolor="#DCDCDC">
<br><br><br>
					
					</td>
				</tr>		
			
					<table border="0" cellspacing="0" cellpadding="0" width="80%" ID="Table1" align="center">
			<tr>
				<td></td>
				<td rowspan="2">&nbsp;</td>
			</tr>
			<tr>
			<table border="0" cellpadding="0" cellspacing="0" width="100%" ID="Table9">
								</table>
				<td>
					<!-- info goes here -->
					<table width="100%" id="Table2" cellSpacing="0" cellPadding="0" align="left" border="0"
						bgcolor="white">
						<tr bgcolor="#DCDCDC">
							<td class="td-typeblack"><font size="3"><center><STRONG>Access Denied...!</STRONG></center></font></td>
						</tr>
						<tr bgcolor="#DCDCDC"><td></td></tr>
						<tr bgcolor="#DCDCDC">
							<td class="td-typeblack" align="center"><input type=button  value="Close" onclick="window.close()" ></td>
						</tr>
						<br><br>
						
										
		</table>
					
		<div id="footer" class="td-menu">
							<table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td width="100%" height="24" bgcolor="#d0d0ff" class="td-menu" align="left">&nbsp;&nbsp;Copyright 
										2005. Raining Data.</td>
								</tr>
							</table>
						</div>
	</body>
</html>