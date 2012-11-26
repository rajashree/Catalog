
<%
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String sessionID = request.getParameter("sessionID");
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");


%>


<html>
	<head>
		<title>Raining Data ePharma - Pedigree Bank</title>
		<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">
	    <script language="JavaScript" type="text/JavaScript">
<!--
function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
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
            <td rowspan="2">&nbsp;</td>
          </tr>
		  
		  <!-- Breadcrumb -->
          <!-- <tr> 
            <td><a href="#" class="typegray1-link">Home</a><span class="td-typegray"> - </span><a href="#" class="typegray1-link">ePedigree</a></td>
          </tr> -->
          <tr> 
            <td class="td-typeblack">NDC 3333-444-77</td>
            <td align="right" class="td-typegray"><!-- Displaying 1 - 25 of 141 <a href="#" class="typegray-link">Prev</a> | <a href="#" class="typegray-link">Next </a>|<a href="#" class="typegray-link"> View All</a> --></td>
          </tr>
		  <tr>
		  	<td align="left">
			<table id="Table12" cellSpacing="1" cellPadding="0" width="100%" align="center" border="0">
		<TBODY>
		<tr class="tableRow_Header">
			<td class="type-whrite" noWrap align="center">Pedigree</td>
			<td class="type-whrite" noWrap align="center">Order #</td>
			<TD class="type-whrite" noWrap align="center">Date Received</TD>
			<TD class="type-whrite" align="center">Trading Partner</TD>
			<TD class="type-whrite" align="center">Product</TD>
			<td class="type-whrite" noWrap align="center">Quantity</td>
		</tr>
		<TR class="tableRow_On">
			<TD class="td-content" align="center"><div align="center"><A class="type-red" href="../epedigree/ePedigree_Manager_Reconcile.jsp?pedid=1&sessionID=<%=sessionID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree">View Pedigree </A></div></TD>
			<TD class="td-content" align="center"><A class="type-red" href="../epedigree/ePedigree_ViewOrder.jsp?trType='Order'&trNum=PO45677" target="_new">PO45677</A></TD>
			<TD class="td-content" align="center">1/7/2005</TD>
			<TD class="td-content" align="center">Amerisource Bergen</TD>
			<TD class="td-content" align="center">Tylenol Extra Strength</TD>
			<TD class="td-content" align="center">110</TD>
			
		</TR>
		<tr class="tableRow_Header">
			<td colspan="4" align="center" valign="middle"><object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0" width="300" height="300">
              <param name="movie" value="../../assets/pedigree_bank_1478.swf">
              <param name="quality" value="high"><param name="LOOP" value="false">
              <param name="wmode" value="transparent"><param name="BGCOLOR" value="#8494CA">
              <embed src="../assets/pedigree_bank_1478.swf" width="300" height="300" loop="false" quality="high" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" wmode="transparent" bgcolor="#8494CA"></embed>
			  </object></td>
			  
			<td colspan=2 align=right>
				<table id="Table12" cellSpacing="1" cellPadding="0" width="80%" align="center" border="0">
					<TR class="tableRow_Header"><TH class="type-whrite">Type</TH><TH class="type-whrite">QTY</TH></TR>
					<tr class="tableRow_On">
						<td class="td-content">ORDER:</td><td class="td-content"><input type="text" size=8 name="destrqty"></td>
					</tr>
					<tr class="tableRow_On">
						<td class="td-content">In Inventory:</td><td class="td-content"><input type="text" size=8 name="invqty"></td>
					</tr>
					
					<tr class="tableRow_On">
						<td class="td-content">Returned:</td><td class="td-content"><input type="text" size=8 name="returnedqty"></td>					
					</tr>
					
					<tr class="tableRow_Off">
						<td class="td-content" colspan=2 align=center><input type="button" value="Add To Order"></td>
					</tr>
				</table>
			</td>
		</tr>
		
		<!-- <tr>
			<td colspan="10" align="right"><input name="" type="button" class="fButton_large" onClick="MM_goToURL('parent','pedigree_Certification_Manual.jsp?sessionID=<%=sessionID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedBank');return document.MM_returnValue" value="Certify Pedigree"></td>
		</tr> -->
		</table>
			
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
