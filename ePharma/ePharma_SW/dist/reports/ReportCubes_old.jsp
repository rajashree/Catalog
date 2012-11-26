
<%try{%>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>



<html>
<head>
<title>Raining Data ePharma - Reports - Advanced</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}
//-->
function gotToFilterPage()
{
 var oneChecked = false;
 
 for(i=0;i<DisplayCubeForm.radio1.length;i++)
 {
 	if(DisplayCubeForm.radio1[i].checked)
 	oneChecked = true;
 }
 if(!oneChecked)
 {
 	alert('Please select a report cube');
 	return false;
 }
 DisplayCubeForm.submit();
}
</script>
</head>
<body>

<jsp:include page="../globalIncludes/TopHeader.jsp" />
<jsp:include page="../globalIncludes/TopLevelLayer1.jsp" />
<jsp:include page="../globalIncludes/TopLevelLayer2.jsp" />
<jsp:include page="./LeftNav.jsp" />

<form name= "DisplayCubeForm"  action="DisplayReportOutputFields.do">
<!-- Top Header -->
<div id="bg">
	<div class="roleIcon-manufacturer">&nbsp;</div>
	<div class="navIcons">
		<a href="../../landing.html"><img src="../../assets/images/home.gif" width="22" height="27" hspace="10" border="0"></a>
		<img src="../../assets/images/account.gif" width="41" height="27" hspace="10">
		<img src="../../assets/images/help.gif" width="21" height="27" hspace="10">
		<img src="../../assets/images/print.gif" width="27" height="27" hspace="10">
		<img src="../../assets/images/logout.gif" width="34" height="27" hspace="10">
		<img src="../../assets/images/space.gif" width="20">
	</div>
	
  <div class="logo"><img src="../../assets/images/logos_combined.jpg"></div>
</div>

<!-- Top level nav layer 1 -->

<!-- Left channel -->
<div id="rightwhite">
<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
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
              <table width="100%" id="Table1" cellSpacing="1" cellPadding="1" align="left" border="0"
			bgcolor="white" class="td-menu">
        <tr bgcolor="white"> 
                  <td class="td-typeblack">Please choose a Report Cube</td>
        </tr> 
        <tr> 
                  <td align="left">
        <table cellpadding="2" cellspacing="2" border="0" width="100%" id="table9">
        <%
        	String str = (String)request.getAttribute("htmlString");
        	System.out.println("-------str is---"+str);
        %>
        1212121<%=str%>
	<tr class="tableRow_Header">
		<td colspan="3" align="center">
		<INPUT name="Submit3" type="submit" class="fButton" id="Submit2" onClick="javascript:return gotToFilterPage();" value="Select">
		</td>
	</tr>
	</table>

</td>
        </tr>
      </table></div>
<div id="footer" class="td-menu" > 
  <table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
    <tr> 
      <td width="100%" height="24" bgcolor="#D0D0FF" class="td-menu" align="left">&nbsp;&nbsp;Copyright 
        2005. Raining Data.</td>
    </tr>
  </table>
</div>
</form>
</body>
</html>
<%}catch(Exception e){e.printStackTrace();}%>