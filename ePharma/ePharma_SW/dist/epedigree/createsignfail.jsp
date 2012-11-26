<%@ include file='../../includes/jspinclude.jsp'%>


<%
String tp_company_nm = "";
String pagenm = null;
String sessionID = (String)session.getAttribute("sessionID");
pagenm = request.getParameter("pagenm");
if(pagenm == null) { pagenm = "pedigree"; }
tp_company_nm = (String)request.getAttribute("tp_company_nm");
if(tp_company_nm == null) tp_company_nm = "" ;
String access = (String)request.getAttribute("statusc");
//system.out.println("cccccccccccccccccccccc"+access);

%>

<html>
	<head>
		<title>Raining Data ePharma - ePedigree</title><LINK href="../../assets/epedigree1.css" type="text/css" rel="stylesheet"></head>
	<body>
		<%@include file='topMenu.jsp'%>		
		
		
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
			<td width="1%" valign="middle" class="td-rightmenu">
			<img src="../../assets/images/space.gif" width="10" height="10"></td>
					<!-- Messaging -->
					<td width="99%" valign="middle" class="td-rightmenu"><table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td align="left">
								</td>
								<td align="right"><img src="../../assets/images/space.gif" width="5"></td>
							</tr>
						</table>
					</td>
				</tr>	
				<%
		 
		  
		  
		  String APNID = (String)session.getAttribute("APNID");
		  System.out.println("AAAAAAAAAAAAAAAAAAAAA"+APNID);
		 		  
		%>	
			<form action="/ePharma/dist/epedigree/ePedigree_ShipManager_Reconcile.jsp?pagenm=pedigree&pedId=<%=APNID%>&tp_company_nm=<%=tp_company_nm%>" method="post">
				<input type=hidden name='APN' value='<%=APNID%>' >
				
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
							<td class="td-typeblack"><font size="3"><center><STRONG>Signature is not created...!</STRONG></center></font></td>
						</tr>
						<tr bgcolor="white"><td></td></tr>
						<tr bgcolor="white">
							<td class="td-typeblack" align="center"><input type=submit name="submit" value="Go Back"  ></td>
						</tr>
						<br><br>
						<Td colspan="2"><br>
						<table border="0" cellpadding="0" cellspacing="1" width="100%" ID="Table9">
						</table><br>
						<TABLE id="Table10" cellSpacing="1" cellPadding="0" width="100%" border="0">
									
	    
						<TR class="tableRow-On">
							<TD class="td-menu"></TD>
						
						</TR>
										
		</table>
					
		
			</form>
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