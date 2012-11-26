<%@ include file='../../includes/jspinclude.jsp'%>


<%
String sessionID = (String)session.getAttribute("sessionID");
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");
System.out.println("*************inside email success.jsp********************");
%>

<html>
	<head>
		<title>Raining Data ePharma - ePedigree</title><LINK href="../../assets/epedigree1.css" type="text/css" rel="stylesheet">
		<script language="javascript">
			function resize(){
				window.moveTo(0,0);
			}
			
		</script>
	</head>
	<body bgcolor="#C0C0C0" onResize="resize();">
		<br>
		
		<%
		  String mail = (String)session.getAttribute("mailid");
		  System.out.println("Mail id :" +mail); 
		  String trType = (String)session.getAttribute("trType");
		  String RefNum = (String)session.getAttribute("RefNum");
		 		  
		%>
		  
		 
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			
					
					<input type=hidden name='searchSelect' value='<%=trType%>' >
					<input type=hidden name='selectedRow' value='<%=RefNum%>' >
					
					<table border="0" cellspacing="0" cellpadding="0" width="80%" ID="Table1" align="center">
			<tr>
		
				<td>
					<!-- info goes here -->
					<table width="100%" id="Table2" cellSpacing="0" cellPadding="0" align="left" border="0"
						bgcolor="#C0C0C0">
						<tr bgcolor="#C0C0C0">
							<td class="td-typeblack"><center><STRONG>Email has been sent to <FONT color="RED"><U><STRONG><%= mail %></STRONG></U></FONT>  Successfully...!</STRONG></center></td>
						</tr>
						<tr bgcolor="#C0C0C0"><td></td></tr>
						<tr bgcolor="#C0C0C0">
							<td class="td-typeblack" align="center"><input type=button  value="Close" onclick="window.close()" ></td>
						</tr>
						<br><br>
						<Td colspan="2"><br>
						<table border="0" cellpadding="0" cellspacing="1" width="100%" ID="Table9">
						</table><br>
						
					

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