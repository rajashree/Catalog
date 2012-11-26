

<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.commons.xml.XMLUtil"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>



<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Raining Data ePharma - Trading Partner Manager</title>
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
<table width="100%" cellSpacing="0" cellPadding="0" align="left" border="0"
			bgcolor="white">
                <tr bgcolor="white" align="center"> 
                  <td class="td-typeblack">Catalog value Mapping Summary</td>
                </tr>
                <tr> 
                  <td align="left">
					<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">				
					<tr class="tableRow_Header">
					<td class="type-whrite">Successfully Mapped</td>
					<td class="type-whrite">Exceptions</td>
				</tr>
                <tr>
                <td><%=session.getAttribute("mapped") %> </td>
                <td><%=session.getAttribute("exceptions") %> </td>
                </tr>
                <tr>
                <td><a href="">View Details</a> </td>
                <td><a href="">View Details</a></td>
                </tr>
                
                 <tr>
                <td><input type="button" name="cancel" value="Cancel"/> </td>
                <td><input type="button" name="cancel" value="Save As Mapped"/></td>
                </tr>
                
      </table></div>



<jsp:include page="../includes/Footer.jsp" />


</body>
</html>
