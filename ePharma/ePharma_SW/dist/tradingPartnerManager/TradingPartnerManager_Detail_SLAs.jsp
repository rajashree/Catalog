<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Raining Data ePharma - Trading Partner Manager - Detail - SLAs</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="./assets/epedigree1.css" rel="stylesheet" type="text/css">
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

<jsp:include page="../globalIncludes/TopHeader.jsp" />
<jsp:include page="../globalIncludes/TopLevelLayer1.jsp" />
<jsp:include page="../globalIncludes/TopLevelLayer2.jsp" />
<jsp:include page="./LeftNav.jsp" />


<div id="rightwhite">
<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
  <tr> 
    <td width="1%" valign="middle" class="td-rightmenu"><img src="./assets/images/space.gif" width="10" height="10"></td>
    <!-- Messaging -->
    <td width="99%" valign="middle" class="td-rightmenu">
	
	
		<jsp:include page="./TPHeader.jsp" />
	  
	  </td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
    <td><table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr> 
          <td><img src="../../assets/images/space.gif" width="30" height="12"></td>
          <td rowspan="2">&nbsp;</td>
        </tr>
        <!-- Breadcrumb -->
        <!-- <tr> 
            <td><a href="#" class="typegray1-link">Home</a><span class="td-typegray"> 
              - </span><a href="#" class="typegray1-link">ePedigree</a></td>
          </tr> -->
        <tr> 
          <td> 
            <!-- info goes here -->
              <table width="100%" cellSpacing="0" cellPadding="0" align="left" border="0"
			bgcolor="white">
               <!-- <tr bgcolor="white"> 
                  <td class="td-typeblack">Service Level Agreement</td>
                </tr> -->
                <tr> 
                  <td align="left">
					<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">		
					<tr class="tableRow_Header"> 
                  <td class="title" colspan="4">Cardinal Health</td>
				  <!-- <td class="title" colspan="2">Status: August 15, 2005, 12:30</td> -->
                </tr>		
					<TR class="tableRow_On">
						<TD colspan="2">Name:<br><INPUT name="pName" id="Text1" value="" size="60"></TD>
						<td>Percentage:<br><INPUT name="pName" id="Text1" value="" size="30"></td>
						<td>ID:<br><INPUT name="pName" id="Text1" value="98" size="5"></td>
					</tr>
					<TR class="tableRow_Off">
						<TD colSpan="3">Description:<br>
						<TEXTAREA id="Textarea1" name="pDescript" rows="3" cols="60"></TEXTAREA></TD>
						<td align="center"><input name="setPeriod" type="button" class="fButton" value="Set Period"></td>
					</TR>
					<TR class="tableRow_On">
						<TD colspan="1">Status: </td>
						<TD colspan="3" class="type-red"><strong>August 15, 2005, 12:30</strong></TD>
						
					</TR>
					<tr class="tableRow_Off">
						<TD>Period</td>
						<TD colspan="2"><INPUT name="pStatus" id="Text4" value="August 15, 2005 - December 15, 2005 (30 days)" size="60"></TD>
						<td>Scope</td>
					</tr>
					<tr class="tableRow_On">
						<TD>Compliance calculations</td>
						<td>Achieved: <INPUT name="pName" id="Text1" value="98.29" size="4">%</td>
						<TD>Expected: <INPUT name="pName" id="Text1" value="98.00" size="4">%</TD>
						<td>Trend</td>
					</tr>
					<tr class="tableRow_Off">
						<TD>Trend Analysis</td>
						<td colspan="2"><INPUT name="pName" class="td-menu bold" id="Text1" value="Will not breach at the current level!" size="60"></td>
						<td align="center"><input name="setPeriod" type="button" class="fButton" value="History"></td>
					</tr></table>
					<br>
					<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">		
					<tr> 
                  <td class="type-whrite" colspan="4"><a href="#" class="typered-link">Objectives (SLO)</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" class="typered-link">Alarm Notification</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" class="typered-link">Notes</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" class="typered-link">FTP</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" class="typered-link">Calculations</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" class="typered-link">Excluded Periods</a></td>
				  <!-- <td class="title" colspan="2">Status: August 15, 2005, 12:30</td> -->
                </tr></table>
				<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">		
					<TR class="tableRow_Header">
						<TD width="25%" class="type-whrite">Name</td>
						<TD width="40%" class="type-whrite">Description</TD>
						<TD class="type-whrite">Weight (%)</td>
						<TD class="type-whrite">Fulfilled (%)</TD>
						<td class="type-whrite"></td>
					</TR>
					<TR class="tableRow_On">
						<TD>Shipment time
						<TD>This SLA monitors shipment time per ASN</TD>
						<TD>10
						<TD>100</TD>
						<td rowspan="2" align="center" class="tableRow_Off"><input name="setPeriod" type="button" class="fButton" value="New"><br>
						<input name="setPeriod" type="button" class="fButton" value="Edit"><br>
						<input name="setPeriod" type="button" class="fButton" value="Delete"><br>
						<input name="setPeriod" type="button" class="fButton" value="Weight"></td>
					</TR>
					<TR class="tableRow_On">
						<TD>Order fulfillment rate
						<TD>This SLA monitors order fulfillment rate</TD>
						<TD>5
						<TD>100</TD>
					</TR>
				</TABLE>
            </td>
        </tr>
      </table></div>


<jsp:include page="../globalIncludes/Footer.jsp" />


</body>
</html>
