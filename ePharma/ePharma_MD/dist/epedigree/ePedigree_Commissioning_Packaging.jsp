
<%@include file='../../includes/jspinclude.jsp'%>

<%
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
//String sessionID = request.getParameter("sessionID");
String sessionID = (String)session.getAttribute("sessionID");
String pagenm = request.getParameter("pagenm");

String xmlResultString = "";
String redirURL = "LoginFailed.html";
String sessQuery = "";
String tp_company_nm = "";
String tp_company_id = "";
System.out.println("session id: "+sessionID);
if(pagenm == null) { pagenm = "pedigree"; }

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Raining Data ePharma - ePedigree - Commissioning Station - Item</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">
<style type="text/css">
<!--
.style1 {color: #E0DFE3}
-->
</style>
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

<%@include file='newtopMenu.jsp'%>

<!-- Left channel -->
<div id="leftgray"> 
  <table width="170" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr> 
      <td width="170" colspan="2" class="td-leftred">ePedigree</td>
    </tr>
    <tr> 
      <td width="10" valign="top" bgcolor="#DCDCDC"></td>
      <td valign="top" class="td-left"><br>
	  	<a href="index.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=epedigree" class="typeblue1-link">ePedigree</a><br>
        <!-- <a href="ePedigree.html" class="typeblue1-link-sub">Dashboard</a><br> -->
        <a href="ePedigree_Commissioning_Packaging.jsp?tp_company_nm=<%=tp_company_nm%>&pagenm=epedigree" class="typeblue1-link-sub">Packaging<br>
		<a href="ePedigree_Commissioning_Station_Item.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Commissioning Station<br></a>
        <a href="ePedigree_Commissioning_Repackaging.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Item</a><br>
		<a href="ePedigree_Commissioning_Station_Package.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Package</a><br>
		<a href="ePedigree_Commissioning_ProductionRun.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Production Run</a><br>
		<a href="ePedigree_Commissioning_AggregationStation.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Aggregation Station</a><br>
        <a href="ePedigree_Commissioning_View.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;View Commissioned Items</a><br>
		
        
	  </td>
    </tr>
    <tr valign="bottom"> 
      <td height="80" colspan="2" class="td-left"><img src="../../assets/images/logo_poweredby.gif" width="150" height="37"></td>
    </tr>
  </table>
</div>

<div id="rightwhite"> 
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr> 
      <td width="1%" valign="middle" class="td-rightmenu"><img src="../../assets/images/space.gif" width="10" height="10"></td>
      <!-- Messaging -->
      <td width="99%" valign="middle" class="td-rightmenu"><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td align="left"> <!-- <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr> 
                  <td class="type-red">[View] </td>
                  <td><a href="#" class="typered-link">Create</a> </td>
                  <td><a href="#" class="typered-link">Delete</a> </td>
                  <td><a href="#" class="typered-link">Duplicate </a></td>
                  <td><a href="#" class="typered-link">Search </a></td>
                  <td><a href="#" class="typered-link">Audit </a></td>
                  <td><a href="#" class="typered-link">Trail</a></td>
                </tr>
              </table> --></td>
            <td align="right"><!-- <img src="../../assets/images/3320.jpg" width="200" height="27"> --><img src="../../assets/images/space.gif" width="5"></td>
          </tr>
        </table></td>
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
		<table width="100%" id="Table1" cellSpacing="1" cellPadding="1" align="left" border="0" bgcolor="white">
			<tr bgcolor="white">
				<td class="td-typeblack" colspan="3">Commissioning Station - Repackaging </td>
				 
			</tr>
			<tr>
				<td align="left">
					<!-- Dashboard Start -->
			<table border="0" width="100%">
			<!-- <tr>
				<td width="50%"></td>
				<td align="right" class="td-typegray">Displaying 1 - 25 of 141 <a href="#" class="typegray-link">Prev</a> | <a href="#" class="typegray-link">Next </a>|<a href="#" class="typegray-link"> View All</a></td>
			</tr> -->
				<tr bgcolor="8494CA">
					<td class="type-whrite"><input type="text" name="T2" size="55">&nbsp;&nbsp;<INPUT name="Submit3" type="submit" class="fButton_large" id="Submit2" onClick="MM_goToURL('parent','ePedigree_Commissioning_Repackaging_Item2.html');return document.MM_returnValue" value="Product Search">
					&nbsp;&nbsp;<INPUT name="Submit3" type="submit" class="fButton" id="Submit2" value="Reset"></td>
				</tr>
			</table>
 				</td>
			</tr>

		</table>
		</td>
    </tr>
  </table>
</div>

<div id="footer"> 
  <table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
    <tr> 
      <td width="100%" height="24" bgcolor="#D0D0FF" class="td-menu" align="left">&nbsp;&nbsp;Copyright 2005. 
        Raining Data.</td>
    </tr>
  </table>
</div>
</body>
</html>
