<%@ include file='../../includes/jspinclude.jsp'%>

<%
String sessionID = (String)session.getAttribute("sessionID");
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");
String numNDCs=(String)request.getAttribute("TotalNDCs");
String numPedigrees="";
%>


<html>
	<head>
		<title>Raining Data ePharma - Pedigree Bank</title>
		<link href="../assets/epedigree1.css" rel="stylesheet" type="text/css">
	    <script language="JavaScript" type="text/JavaScript">
<!--
function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}


//-->

</script>

	</head>
	
	<body>
		<%@include file='../epedigree/topMenu.jsp'%>
		
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="1%" valign="middle" class="td-rightmenu"><img src="../assets/images/space.gif" width="10" height="10"></td>
					<!-- Messaging -->
					<td width="99%" valign="middle" class="td-rightmenu"><table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td align="left">
								</td>
								<td align="right"><!-- <img src="../assets/images/3320.jpg" width="200" height="27"> --><img src="../assets/images/space.gif" width="5"></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><table border="0" cellspacing="0" cellpadding="0" width="100%">
							<tr>
								<td><img src="../assets/images/space.gif" width="30" height="12"></td>
								<td rowspan="3">&nbsp;</td>
							</tr>
							<tr>
								<td>
									<!-- info goes here -->
									<table width="100%" id="Table1" cellSpacing="1" cellPadding="1" align="left" border="0"
										bgcolor="white">
										 <tr> 
      
      <table width="100%" border="0" cellspacing="1" cellpadding="3">
           
		  
           
          <tr class="tableRow_Header"> 
            <td class="type-whrite"><strong>PEDIGREE BANK SUMMARY</strong></td>
            <td align="right" class="type-whrite"><!-- Displaying 1 - 25 of 141 <a href="#" class="typegray-link">Prev</a> | <a href="#" class="typegray-link">Next </a>|<a href="#" class="typegray-link"> View All</a> --></td>
          </tr>
		  <tr>
		  	<td align="left">
				<table width="300"  border="0" cellspacing="2" cellpadding="1">
				  <tr>
					<td>Total Number of NDCs</td>
					<td><input name="" value="<%=numNDCs%>" type="text" size="15"></td>
				  </tr>
				  <tr>
					<td>Total Number of Pedigrees</td>
					<td><input name="" value="<%=numPedigrees%>" type="text" size="15"></td>
				  </tr>
				  <tr>
					<td></td>
					<td><input name="" type="button" onClick="MM_goToURL('parent','pedigreeBankResults.do?sessionID=<%=sessionID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedBank');return document.MM_returnValue" value="Perform Search"></td>
				  </tr>
				</table>

			
			</td>
		  </tr>
        </table></td>
		 
    </tr>
										<tr>
											 
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<DIV></DIV>
						<div id="footer" class="td-menu">
							<table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td width="100%" height="24" bgcolor="#d0d0ff" class="td-menu" align="left">&nbsp;&nbsp;Copyright 
										2005. Raining Data.</td>
								</tr>
							</table>
						</div>
					</td>
				</tr>
			</table>
	</div>
	</body>
</html>
