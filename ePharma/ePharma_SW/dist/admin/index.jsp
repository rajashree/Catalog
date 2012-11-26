<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@include file='../../includes/jspinclude.jsp'%>
<%
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String sessionID = request.getParameter("sessionID");
String pagenm = request.getParameter("pagenm");

String xmlResultString = "";
String redirURL = "LoginFailed.html";
String sessQuery = "";
String tp_company_nm = "";
String tp_company_id = "";
String ALERTLINES = "";
String xQuery  = "";
String userName = "";
System.out.println("session id: "+sessionID);


%>

<html>
<head>
<title>Raining Data ePharma - Admin Console</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">
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
<%@include file='../epedigree/newtopMenu.jsp'%>

<!-- Left channel -->
<div id="leftgray"> 
  <table width="170" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr> 
      <td width="170" colspan="2" class="td-leftred">Admin Console</td>
    </tr>
    <tr> 
      <td width="10" valign="top" bgcolor="#DCDCDC"></td>
      <td valign="top" class="td-left"><br>
        <a href="#" class="typeblue1-link">Grid Manager</a><br>
        <a href="#" class="typeblue1-link-sub">Locations</a><br>
		<a href="#" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Add</a><br>
		<a href="#" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Edit</a><br>
		<a href="#" class="typeblue1-link-sub-sub bold">&nbsp;&nbsp;&nbsp;Build Location</a><br>
		<a href="#" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Add</a><br>
		<a href="#" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Edit</a><br>
		<a href="#" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Location Filters</a><br>
		<a href="#" class="typeblue1-link-sub">Readers</a><br>
		<a href="#" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Add</a><br>
		<a href="#" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Edit</a><br><br>
		
		<a href="#" class="typeblue1-link">Repositories</a><br>
        <a href="#" class="typeblue1-link-sub">Add</a><br>
		<a href="#" class="typeblue1-link-sub">Edit</a><br>
		<a href="#" class="typeblue1-link-sub">Resources</a><br>
		<a href="UserDetail.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Users & Groups</a><br>
		<a href="#" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;DB Structures</a><br>
		<a href="#" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;DB Procedures</a><br>
		<a href="#" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;ONS</a><br>
		<a href="#" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Replication</a><br>
		<a href="#" class="typeblue1-link-sub">Software Components</a><br>
		<a href="#" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Deploy New</a><br>
		<a href="#" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;View Existing</a><br>
		<a href="#" class="typeblue1-link-sub">Subscriptions</a><br>
		<a href="#" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Add</a><br>
		<a href="#" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Edit</a><br><br>
		
		<a href="#" class="typeblue1-link">System Setup</a><br>
        <a href="#" class="typeblue1-link-sub">Events</a><br>
		<a href="#" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Add</a><br>
		<a href="#" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Edit</a><br>
		<a href="#" class="typeblue1-link-sub">Rules</a><br>
		<a href="#" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Add</a><br>
		<a href="#" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Edit</a><br>
		<a href="#" class="typeblue1-link-sub">Filters</a><br>
		<a href="#" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Add</a><br>
		<a href="#" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Edit</a><br>
		<a href="#" class="typeblue1-link-sub">Exceptions</a><br>
		<a href="#" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Add</a><br>
		<a href="#" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Edit</a><br>
		<a href="#" class="typeblue1-link-sub">Vocabularies</a><br>
		<a href="#" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Add</a><br>
		<a href="#" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Edit</a><br><br>
		
		<a href="#" class="typeblue1-link">KPI Dashboard</a><br>
        <a href="#" class="typeblue1-link-sub">Configure</a><br>
		<a href="#" class="typeblue1-link-sub">Run</a><br><br>
		
		<a href="#" class="typeblue1-link">EPC Tracking</a><br>
		<a href="#" class="typeblue1-link-sub">Run</a><br>
		</td>
    </tr>
    <tr valign="bottom"> 
      <td height="80" colspan="2" class="td-left"><img src="../../assets/images/logo_poweredby.gif" width="150" height="37"></td>
    </tr>
  </table>
</div>

<div id="rightwhite"> <table width="100%" border="0" cellspacing="0" cellpadding="0"> 
  <tr> 
    <td width="1%" valign="middle" class="td-rightmenu"><img src="../../assets/images/space.gif" width="10" height="10"></td>
    <!-- Messaging -->
    <td width="99%" valign="middle" class="td-rightmenu"><table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td align="left"> 
            <!-- <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr> 
                  <td class="type-red">[View] </td>
                  <td><a href="#" class="typered-link">Create</a> </td>
                  <td><a href="#" class="typered-link">Delete</a> </td>
                  <td><a href="#" class="typered-link">Duplicate </a></td>
                  <td><a href="#" class="typered-link">Search </a></td>
                  <td><a href="#" class="typered-link">Audit </a></td>
                  <td><a href="#" class="typered-link">Trail</a></td>
                </tr>
              </table> -->
          </td>
          <td align="right">
            <!-- <img src="../../assets/images/3320.jpg" width="200" height="27"> -->
            <img src="../../assets/images/space.gif" width="5"></td>
        </tr>
      </table></td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
    <td> 
      <!--  href="#" class="typegray1-link">ePedigree</a></td>
          </tr> 
		          <tr> 
          <td> 
           
            <form action="ePedigree_Commissioning_ProductRun3.html" method="post" ID="Form1">
              <table width="100%" id="Table1" cellSpacing="1" cellPadding="1" align="left" border="0"
			bgcolor="white" class="td-menu">
                <tr bgcolor="white"> 
                  <td class="td-typeblack">You can enter multiple values separated by comma.</td>
                </tr>
                <tr> 
                  <td align="left"> 
				  <TABLE id="Table3" cellSpacing="1" cellPadding="1" border="0" width="100%" class="td-menu">

             <tr>
			 	<td bgcolor="white">
				
					<TABLE id="Table3" cellSpacing="1" cellPadding="1" border="0" class="td-menu" align="center" width="100%">
						<TR class="tableRow_Header">
							<TD class="type-whrite" align="center">
								<STRONG>SEARCH ON</STRONG>
							</TD>
							<TD class="type-whrite" align="center">

								<STRONG>VALUE</STRONG>
							</TD>
							<TD class="type-whrite" align="center">
								<STRONG>CRITERIA</STRONG>
							</TD>
						</TR>
						<TR class="tableRow_On">
							<TD><STRONG>Date Received (yyyy-mm-dd):</STRONG></TD>

							<TD>From:<INPUT id="Text4" type="text" name="fromDtReceived" size="10">&nbsp;To: <INPUT id="Text1" type="text" size="10" name="toDtReceived"></TD>
							<TD><SELECT id="Select4" name="dateReceivedAndOr">
									<OPTION value="AND" selected>AND</OPTION>
									<OPTION value="OR">OR</OPTION>
								</SELECT></TD>
						</TR>
						<TR class="tableRow_Off">

							<TD><STRONG>Pedigree Reference #:</STRONG></TD>
							<TD><INPUT id="Text2" type="text" size="24" name="fromDtPublished">&nbsp;</TD>
							<TD><SELECT id="Select5" name="datePublishedAndOr">
									<OPTION value="AND" selected>AND</OPTION>
									<OPTION value="OR">OR</OPTION>
								</SELECT></TD>
						</TR>

						<TR class="tableRow_On">
							<TD><STRONG>From Name/Company:</STRONG></TD>
							<TD><INPUT id="Text3" type="text" size="24" name="fromDtPublished"></TD>
							<TD><SELECT id="Select6" name="msStageAndOr">
									<OPTION value="AND" selected>AND</OPTION>
									<OPTION value="OR">OR</OPTION>
								</SELECT></TD>

						</TR>
						<TR class="tableRow_Off">
							<TD><STRONG><STRONG>To&nbsp;Name/Company:</STRONG></STRONG></TD>
							<TD><INPUT id="Text5" type="text" size="24" name="fromDtPublished"></TD>
							<TD><SELECT id="Select7" name="msTypeAndOr">
									<OPTION value="AND" selected>AND</OPTION>
									<OPTION value="OR">OR</OPTION>

								</SELECT></TD>
						</TR>
						<TR class="tableRow_On">
							  <TD><STRONG> Manufacturing Facility:</STRONG></TD>
							<TD><INPUT id="Text9" type="text" size="24" name="fromDtPublished"></TD>
							<TD><SELECT id="Select2" name="msBilledAndOr">
									<OPTION value="AND" selected>AND</OPTION>

									<OPTION value="OR">OR</OPTION>
								</SELECT></TD>
						</TR>
						<TR class="tableRow_Off">
							<TD><STRONG>License Number:</STRONG></TD>
							<TD><INPUT id="Text6" type="text" size="30" name="keywords"></TD>
							<TD><SELECT id="Select8" name="keywordsAndOr">
									<OPTION value="AND" selected>AND</OPTION>

									<OPTION value="OR">OR</OPTION>
								</SELECT></TD>
						</TR>
						<TR class="tableRow_On">
							<TD><STRONG>Date In Custody:</STRONG></TD>
							<TD>From:<INPUT id="Text7" type="text" size="10" name="fromDtReceived">&nbsp;To: <INPUT id="Text10" type="text" size="10" name="toDtReceived"></TD>
							<TD><SELECT id="Select9" name="authLastNameAndOr">

									<OPTION value="AND" selected>AND</OPTION>
									<OPTION value="OR">OR</OPTION>
								</SELECT></TD>
						</TR>
						<TR class="tableRow_Off">
							<TD><STRONG> Product EPC:</STRONG>
							</TD>

							<TD><INPUT id="Text8" type="text" name="coAuthorLastName" size="30"></TD>
							<TD><SELECT id="Select10" name="coAuthorLastNameAndOr">
									<OPTION value="AND" selected>AND</OPTION>
									<OPTION value="OR">OR</OPTION>
								</SELECT></TD>
						</TR>
						<TR class="tableRow_On">
							<TD><STRONG>Parent EPC:</STRONG></TD>

							<TD><INPUT id="Text11" type="text" size="30" name="coAuthorLastName"></TD>
							<TD><SELECT id="Select12" name="acodeAndOr">
									<OPTION value="AND" selected>AND</OPTION>
									<OPTION value="OR">OR</OPTION>
								</SELECT></TD>
						</TR>
						<TR class="tableRow_Off">
							<TD><STRONG>Bar Code:</STRONG></TD>

							<TD><INPUT id="Text18" type="text" size="30" name="coAuthorLastName"></TD>

							<TD><SELECT id="Select13" name="acodeAndOr">
									<OPTION value="AND" selected>AND</OPTION>
									<OPTION value="OR">OR</OPTION>
								</SELECT></TD>
						</TR>
						<TR class="tableRow_On">
							<TD><STRONG>Brand Name:</STRONG></TD>

							<TD><INPUT id="Text12" type="text" size="30" name="coAuthorLastName"></TD>
							<TD><SELECT id="Select15" name="departmentAndOr">
									<OPTION value="AND" selected>AND</OPTION>
									<OPTION value="OR">OR</OPTION>
								</SELECT></TD>
						</TR>
						<TR class="tableRow_Off">
							<TD><STRONG>Lot Number:</STRONG></TD>

							<TD><INPUT id="Text17" type="text" size="30" name="coAuthorLastName"></TD>
							<TD><SELECT id="Select1" name="departmentAndOr">
									<OPTION value="AND" selected>AND</OPTION>
									<OPTION value="OR">OR</OPTION>
								</SELECT></TD>
						</TR>
						<TR class="tableRow_On">
							<TD><STRONG>Lot Expiration Date:</STRONG></TD>

							<TD>From:<INPUT id="Text13" type="text" size="10" name="fromDtReceived">&nbsp;To: <INPUT id="Text14" type="text" size="10" name="toDtReceived"></TD>
							<TD><SELECT id="Select3" name="departmentAndOr">
									<OPTION value="AND" selected>AND</OPTION>
									<OPTION value="OR">OR</OPTION>
								</SELECT></TD>
						</TR>
						<TR class="tableRow_Off">

							<TD><STRONG>Product Expiration Date:</STRONG></TD>
							<TD>From:<INPUT id="Text15" type="text" size="10" name="fromDtReceived">&nbsp;To: <INPUT id="Text16" type="text" size="10" name="toDtReceived"></TD>
							<TD><SELECT id="Select11" name="departmentAndOr">
									<OPTION value="AND" selected>AND</OPTION>
									<OPTION value="OR">OR</OPTION>
								</SELECT></TD>

						</TR>
						<TR class="tableRow_On">
							<TD colspan="3"><STRONG>Show All: <INPUT id="Checkbox2" type="checkbox" value="showAll" name="showAll"></STRONG>
								| <STRONG>Show Verified:</STRONG><input type="checkbox" name="showAll" value="showAll" ID="Checkbox1">&nbsp;|
								<STRONG>Show Pending:</STRONG> <INPUT id="Checkbox3" type="checkbox" value="showAll" name="showAll">&nbsp;|
								<STRONG>Show Violations:</STRONG> <INPUT id="Checkbox4" type="checkbox" value="showAll" name="showAll">

							</TD>
						</TR>
						<tr class="tableRow_Header">
						<td colspan="3" align="center">
						<INPUT name="Submit3" type="submit" class="fButton" id="Submit2" onClick="MM_goToURL('parent','ePedigree_Manager_AdvancedSearchResults.html');return document.MM_returnValue" value="Search">
						</td>
						</tr>
					</TABLE>
				</td>
			</tr>
                
                    </TABLE></td>
                </tr>
              </table>
            </form></td>
        </tr>
      </table> -->
  </div>
<div id="footer" class="td-menu" > 
  <table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
    <tr> 
      <td width="100%" height="24" bgcolor="#D0D0FF" class="td-menu" align="left">&nbsp;&nbsp;Copyright 
        2005. Raining Data.</td>
    </tr>
  </table>
</div>
</body>
</html>
