<%@ page session="true"%>
<%@ page autoFlush="true"%>
<%@ page import="com.rdta.rules.OperationType"%>
<%@ page language="java"%>
<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.commons.xml.XMLUtil"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="java.util.List"%>

<html>
<head>
<title>
Display Rule
</title>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">

</head>

<script language=javascript>
<!--
function displayRule()
{
    frm = document.forms[0]
    frm.action = "/ePharma/dist/rules/CreateRuleAction.do"
    frm.submit();
    return true;

}
-->
</script>

<body>

<%@include file='../epedigree/test.jsp'%>
<%@include file='../epedigree/topMenu.jsp'%>

<%
String ccList = (String)request.getAttribute("ccList");
%>

<FORM name="DisplayRuleForm" ACTION="/ePharma/dist/rules/DisplayRuleAction.do"  method="post" >

<div id="rightwhite">
			<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">	
			<tr class="tableRow_Off"> <td colspan="4">&nbsp; </td></tr>
			<tr class="tableRow_Header"> 
                  	<td class="title" colspan="4">Display Rule (search)</td>
                	</tr>
                	<tr class="tableRow_Off"> <td colspan="4">&nbsp; </td></tr>

  <tr> 
    <td align="left" rowspan="1" class="tableRow_On">CategoryCode</td>    
    <td align="left" rowspan="1" class="tableRow_On"><SELECT id="ccList" name="ccList">
    							  <%=ccList%>
    							  </SELECT>    
    </td>

  	<td align="left" rowspan="1" class="tableRow_Off"><INPUT id="save" type="submit" name="operationType" value="<%=OperationType.FIND%>"></td>
    </tr>
  
	<%
	String s = (String)request.getAttribute("ruleDisplay");
	
	Node n = XMLUtil.parse(s);
	List list = XMLUtil.executeQuery(n,"Rule"); 
	%>
	
	<%
	for (int i=0 ; i<list.size(); i++)
	{
		String ruleID = XMLUtil.getValue((Node)list.get(i),"RuleID");
		String ruleName = XMLUtil.getValue((Node)list.get(i),"RuleName");
		String contextSchema = XMLUtil.getValue((Node)list.get(i),"ContextSchema");
		String categoryCode = XMLUtil.getValue((Node)list.get(i),"CategoryCode");
		String condition = XMLUtil.getValue((Node)list.get(i),"Condition");
		String trueAction = XMLUtil.getValue((Node)list.get(i),"Result/TrueAction");
		String falseAction = XMLUtil.getValue((Node)list.get(i),"Result/FalseAction");
		String trueToLink = XMLUtil.getValue((Node)list.get(i),"Links/TrueToLink");
		String falseToLink = XMLUtil.getValue((Node)list.get(i),"Links/FalseToLink");				
		String fromLink = XMLUtil.getValue((Node)list.get(i),"Links/FromLink");	
		
		if (ruleID == null){ ruleID = ""; }
		if (ruleName == null){ ruleName = ""; }
		if (contextSchema == null){ contextSchema = "-"; }
		if (categoryCode == null){ categoryCode = "-"; }
		if (condition == null){ condition = "-"; }
		if (trueAction == null){ trueAction = "-"; }
		if (falseAction == null){ falseAction = "-"; }
		
		if (trueToLink == null){ trueToLink = "-"; }
		if (falseToLink == null){ falseToLink = "-"; }
		if (fromLink == null){ fromLink = "-"; }
		
		if (i==0) 
		{ %>
		    <tr><td>&nbsp;</td></tr>
			<tr>
			<td><B><U><I>Select</td>
			<td><B><U> RuleID </td>
			<td><B><U> RuleName </td>		
			<td><B><U> ContextSchema </td>		
			<td><B><U> CategoryCode </td>		
			<td><B><U> Condition </td>		
			<td><B><U> TrueAction </td>		
			<td><B><U> FalseAction </td>		
			<td><B><U> TrueToLink </td>		
			<td><B><U> FalseToLink </td>		
			<td><B><U> FromLink </td>	
			<tr><td>&nbsp;</td></tr>		
			</tr>
	<%	}					
	%>
	
		<tr>
		<td>
		<INPUT id="update" type="radio" name="radioRuleID" value="<%=ruleID%>" >
		</td>		
		
		<td> <%=ruleID%> </td>
		<td> <%=ruleName%> </td>		
		<td> <%=contextSchema%> </td>		
		<td> <%=categoryCode%> </td>		
		<td> <%=condition%> </td>		
		<td> <%=trueAction%> </td>		
		<td> <%=falseAction%> </td>		
		<td> <%=trueToLink%> </td>		
		<td> <%=falseToLink%> </td>		
		<td> <%=fromLink%> </td>		
	<%
	}
	%>  
	<tr>
	<td align="center" class="tableRow_On" colspan="4"> <INPUT id="update" type="submit" name="operationType" value="<%=OperationType.VIEW%>" onclick="return displayRule();"></td>		  
	</tr>
</table>
</div>

</FORM>

<jsp:include page="../globalIncludes/Footer.jsp" />

</body>
</head>
</html>