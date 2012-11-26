<%@ page session="true"%>
<%@ page autoFlush="true"%>
<%@ page import="com.rdta.rules.OperationType"%>
<%@ page language="java"%>
<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="org.w3c.dom.Node"%>

<html>
<head>
<title>
Create Rule
</title>

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


<%
String servIP = "123";
String clientIP = "123";
String sessionID = "123";
String pagenm = "pedigree";
String tp_company_nm = "";
%>
<%@include file='../epedigree/topMenu.jsp'%>


<%
Node ccNode =  (Node)request.getAttribute("categoryInfo");

	String code = "";
	String parentCode = "";
	String description =""; 

	 if(ccNode != null ) 
	 {

		code = CommonUtil.jspDisplayValue(ccNode,"Code");
		parentCode = CommonUtil.jspDisplayValue(ccNode,"ParentCode");
		description = CommonUtil.jspDisplayValue(ccNode,"Description");				
	}
	String ro="";
	if ( !code.equals(""))
	{
		ro = "readonly=\"readonly\"";
	}

%>

<FORM name="CategoryCodeForm" ACTION="/ePharma/dist/rules/CreateCategoryCodeAction.do"  method="post" >

<input type=hidden name="sessionID" value="sessionID">
<input type=hidden name="clientIP" value="clientIP">
<input type=hidden name="pagenm" value="ePedigree">
<input type=hidden name="tp_company_nm" value="tp_company_nm">

<!--<div id="rightwhite">
    <table width="100%" border="0" cellspacing="0" cellpadding="0"> -->

<div id="rightwhite">
<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">	
  <tr class="tableRow_Off"> <td colspan="4">&nbsp; </td></tr>
  <tr class="tableRow_Header"> <td class="title" colspan="4">Create Category Code </td> </tr>
  <tr class="tableRow_Off"> <td colspan="4">&nbsp; </td></tr>
 

  <tr> 
    <td align="left" rowspan="1" class="tableRow_Off">code</td>    
    <td align="left" rowspan="1" class="tableRow_Off"><INPUT  id="code" type="text" name="code" value="<%=code%>"></td>
  </tr>  
  
  <%
	String parentCodeList = (String)request.getAttribute("parentCodeList");
	if (parentCodeList == null){ parentCodeList = ""; }
  %>
  <tr> 
  <td align="left" rowspan="1" class="tableRow_On">parentCode</td>  
  <td align="left" rowspan="1" class="tableRow_On"><SELECT id="parentCode" name="parentCode">
  							<OPTION value=""></OPTION>
							<%=parentCodeList%>
							  </SELECT>
 </td>
 </tr>
  
  <tr> 
    <td align="left" rowspan="1" class="tableRow_Off">description</td>    
    <td align="left" rowspan="1" class="tableRow_Off"><INPUT id="description" type="text" name="description" value="<%=description%>"></td>
  </tr>
  
  <tr> <td>&nbsp; </td></tr>
  
  <tr>
  	<td align="centre" rowspan="1" class="tableRow_On" colspan="4"><INPUT id="save" type="submit" name="operationType" value="<%=OperationType.ADD%>"></td>
  	<%
  		if (!code.equals(""))
  		{ %>
			<td align="left" rowspan="1" class="tableRow_On">
			<INPUT id="save" type="submit" name="operationType" value="<%=OperationType.UPDATE%>">
			</td>  		
  	<%	}
  	%>
  </tr>
  
</table>
</div>

</FORM>

<jsp:include page="../globalIncludes/Footer.jsp" />

</body>
</head>
</html>