<%@ include file='../../includes/jspinclude.jsp'%>

<%
String sessionID = (String)session.getAttribute("sessionID");
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");
String apnId = request.getParameter("pedid");
System.out.println("The APNID from form is "+apnId); 
System.out.println("inside access denied");
%>

<html>
	<head>
		<title>Raining Data ePharma - ePedigree</title><LINK href="../../assets/epedigree1.css" type="text/css" rel="stylesheet"></head>
	<body>
		<br><br><br>
		<%@include file='topMenu.jsp'%>
		
			<form action="ePedigree_Manager_Reconcile.jsp?pedid=<%=apnId%>&pagenm=<%=pagenm%>&tp_company_nm=<%=tp_company_nm%>" method="post">
					
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