<%
//System.out.println("stepa");
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String sessionID = "";//request.getParameter("sessionID");
String pagenm = "";//request.getParameter("pagenm");
String tp_company_nm = "";//request.getParameter("tp_company_nm");
//System.out.println("step1" + servIP + clientIP + sessionID + pagenm + tp_company_nm);
%>
<html>
	<head>
		<title>Raining Data ePharma - Pedigree Bank</title>
		<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">
	    <script language="JavaScript" type="text/JavaScript">
<!--
function MM_goToURL(url) { //v3.0
    if (document.globalform.ndc.value == "") {
        document.MM_returnValue = true;
        alert ("NDC value cannot be empty");
        return;
    }
    url += "&ndc=" +  document.globalform.ndc.value;
    if (document.globalform.lot.value != "") {
        url += "&lot=" + document.globalform.lot.value;
    }

    document.globalform.action = url;
    document.globalform.submit();
    
}

function MM_openBrWindow(theURL,winName,features) { //v2.0
  window.open(theURL,winName,features);
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
								<td align="right"><!-- <img src="../../assets/images/3320.jpg" width="200" height="27"> --><img src="../assets/images/space.gif" width="5"></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><table border="0" cellspacing="0" cellpadding="0" width="100%">
							<tr>
								<td><img src="../../assets/images/space.gif" width="30" height="12"></td>
								<td rowspan="2">&nbsp;</td>
							</tr>
							<tr>
								<td>
									<!-- info goes here -->
									<table width="100%" id="Table1" cellSpacing="1" cellPadding="1" align="left" border="0"
										bgcolor="white">
										 <tr> 
      <td>&nbsp;</td>

      <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td><img src="../../assets/images/space.gif" width="30" height="12"></td>
            </tr>
		  
		  <!-- Breadcrumb -->
          <!-- <tr> 
            <td><a href="#" class="typegray1-link">Home</a><span class="td-typegray"> - </span><a href="#" class="typegray1-link">ePedigree</a></td>
          </tr> -->
          <tr class="tableRow_Header"> 
            <td class="type-whrite"><strong>Pedigree Bank Summary.</strong></td>
            <td align="right" class="type-whrite"><!-- Displaying 1 - 25 of 141 <a href="#" class="typegray-link">Prev</a> | <a href="#" class="typegray-link">Next </a>|<a href="#" class="typegray-link"> View All</a> --></td>
          </tr>
		  <tr>
		  	<td align="left">
              <form name="globalform" method="post">
				<table width="300"  border="0" cellspacing="2" cellpadding="1">
				  <tr>
					<td>Total Number of NDCs</td>
					<td><input name="ndc" type="text" size="15"></td>
				  </tr>
				  <tr>
					<td>Total Number of Pedigrees</td>
					<td><input name="lot" type="text" size="15"></td>
				  </tr>
				  <tr>
					<td></td>
                    <!-- 
					<td><input name="" type="button" class="fButton" onClick="MM_goToURL('parent','pedigree_Bank_Results.jsp?sessionID=<%=sessionID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedBank');return document.MM_returnValue" value="Search"></td>
                    -->
                    <td><input name="" type="button" class="fButton" onClick="MM_goToURL('pedigree_Bank_Results.jsp?sessionID=<%=sessionID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedBank');" value="Perform Search"></td>                    
				  </tr>
				</table>
              </form>
			</td>
		  </tr>
        </table></td>
		<td>&nbsp;</td>
    </tr>
										<tr>
											<td align="left">
											</td>
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
