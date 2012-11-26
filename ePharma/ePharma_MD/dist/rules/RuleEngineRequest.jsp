<%@ page session="true"%>
<%@ page autoFlush="true"%>
<%@ page import="com.rdta.rules.*"%>
<%@ page language="java"%>

<html>
<head>
<title>
Create Rule
</title>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="./assets/epedigree1.css" rel="stylesheet" type="text/css">

</head>
<body>

<jsp:include page="../globalIncludes/TopHeader.jsp" />
<jsp:include page="../globalIncludes/TopLevelLayer1.jsp" />
<jsp:include page="../globalIncludes/TopLevelLayer2.jsp" />
<jsp:include page="./LeftNav.jsp" />



<FORM name="StartRuleForm" ACTION="StartRuleEngine.do"  method="post" >

<div id="rightwhite">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	
  <tr> <td> &nbsp; </td></tr>	
  <tr> <td> <B><U> Submit Request to Start Rule Engine </td></tr>	
  <tr> <td> &nbsp; </td></tr>	

  <tr> 
    <td align="left" rowspan="1">RuleSetID</td>    
    <td align="left" rowspan="1" ><INPUT  id="ruleID" type="text" name="rulesetID" value=""></td>
  </tr>
  
  <tr> 
    <td align="left" rowspan="1">Payload</td>    
    <td align="left" rowspan="1" ><INPUT id="ruleName" type="text" name="payload" value=""></td>
  </tr>  
  

  <tr>
  	
  		<td align="left" rowspan="1"><INPUT id="save" type="submit" name="Submit" value="submit"></td>
 
	  </tr>
  
</table>
</div>

</FORM>

<jsp:include page="../globalIncludes/Footer.jsp" />

</body>
</head>
</html>