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
Create Rule Set
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

<% 
String ruleSetID = (String)request.getAttribute("ruleSetID"); 
if (ruleSetID == null){ ruleSetID = ""; }
%>

<script language=javascript>
<!--
function deleteRule()
{
    frm = document.forms[0]
    frm.action = "/ePharma/dist/rules/CreateRuleSetAction.do?ruleSetID=<%=ruleSetID%>"
    frm.submit();
    return true;

}
-->
</script>

<body>

<%@include file='../epedigree/test.jsp'%>
<%@include file='../epedigree/topMenu.jsp'%>

<%

//get the attributes that are set
//String ruleSetID = (String)request.getAttribute("ruleSetID");
String contextSchema = (String)request.getAttribute("contextSchema");
String context = (String)request.getAttribute("context");
String ruleSetName = (String)request.getAttribute("ruleSetName");
String categoryCode = (String)request.getAttribute("categoryCode");

String rules = (String)request.getAttribute("rules");
String trueToLink = (String)request.getAttribute("trueToLink");
String falseToLink = (String)request.getAttribute("falseToLink");
String fromLink = (String)request.getAttribute("fromLink");

String rulesDisplay = (String)request.getAttribute("rulesDisplay");

String selcontextSchema = (String)request.getAttribute("selcontextSchema");
String selcontext = (String)request.getAttribute("selcontext");
String selcategoryCode = (String)request.getAttribute("selcategoryCode");

//if the value is null, set it as ""
//if (ruleSetID == null){ ruleSetID = ""; }
if (contextSchema == null){ contextSchema = ""; }
if (context == null){ context = ""; }
if (ruleSetName == null){ ruleSetName = ""; }
if (categoryCode == null){ categoryCode = ""; }
if (rules == null){ rules = ""; }
if (trueToLink == null){ trueToLink = ""; }
if (falseToLink == null){ falseToLink = ""; }
if (fromLink == null){ fromLink = ""; }
if (rulesDisplay == null){ rulesDisplay = ""; }

if ( selcontextSchema== null){ selcontextSchema = ""; }
if ( selcontext== null){ selcontext = ""; }
if ( selcategoryCode== null){ selcategoryCode = ""; }

%>

<FORM name="CreateRuleSetForm" ACTION="/ePharma/dist/rules/CreateRuleSetAction.do"  method="post" >

<div id="rightwhite">
<table width="100%" border="0" cellspacing="0" cellpadding="0">


  <tr> 
    <td align="left" rowspan="1">RuleSetID</td>    
    <td align="left" rowspan="1" ><INPUT id="ruleSetID" type="text" name="ruleSetID" value="<%=ruleSetID%>"></td>
  </tr>

  
  <tr> 
    <td align="left" rowspan="1">ContextSchema</td>    
    <td align="left" rowspan="1" >
    						<SELECT id="contextSchema" name="contextSchema">
    						<%	  if(!selcontextSchema.equals("")) {
    						%>
    						<option SELECTED value="<%=selcontextSchema%>"><%=selcontextSchema%></option>
    						<%=contextSchema%>
    						<% } else { %>    						
    							  <%=contextSchema%>
    						<% } %>	  
    						</SELECT>
    </td>
  </tr>
  
  <tr> 
    <td align="left" rowspan="1">Context</td>    
    <td align="left" rowspan="1" >
	
    						<SELECT id="context" name="context">
    						<%	  if(!selcontext.equals("")) {
    						%>
    						<option SELECTED value="<%=selcontext%>"><%=selcontext%></option>
    						<%=context%>
    						<% } else { %>    						
    							  <%=context%>
    						<% } %>	  
    						</SELECT>    							  
    </td>
  </tr>    
  
  <tr> 
    <td align="left" rowspan="1">RuleSetName</td>    
    <td align="left" rowspan="1" ><INPUT id="ruleSetName" type="text" name="ruleSetName" value="<%=ruleSetName%>"></td>
  </tr>  
  
  <tr> 
    <td align="left" rowspan="1">CategoryCode</td>    
    <td align="left" rowspan="1" >
    							  
    						<SELECT id="categoryCode" name="categoryCode">
    						<%	  if(!selcategoryCode.equals("")) {
    						%>
    						<option SELECTED value="<%=selcategoryCode%>"><%=selcategoryCode%></option>
    						<%=categoryCode%>
    						<% } else { %>    						
    							  <%=categoryCode%>
    						<% } %>	  
    						</SELECT>    							  
    </td>
  </tr>
  
  <tr><td>&nbsp;</td></tr>
  
  <tr>
  	<td align="left" rowspan="1">Rules</td>
  	<td align="left" rowspan="1">True ToLink</td>
  	<td align="left" rowspan="1">False ToLink</td>
  	<td align="left" rowspan="1">From Link</td>
  </tr>
  <tr>
    <td align="left" rowspan="1" ><SELECT id="rules" name="rules">
    							<%=rules%>
    							  </SELECT>   
    </td>	
  	<td align="left" rowspan="1"><INPUT id="trueToLink" type="text" name="trueToLink" value=""></td>
  	<td align="left" rowspan="1"><INPUT id="falseToLink" type="text" name="falseToLink" value=""></td>
	<td align="left" rowspan="1"><INPUT id="fromLink" type="text" name="fromLink" value=""></td>
  </tr>  
  
  <tr> <td> &nbsp; </td></tr>
  <tr>
  	<td align="left" rowspan="1"><INPUT id="save" type="submit" name="operationType" value="<%=OperationType.ADD%>"></td>
  </tr>
  <tr> <td> &nbsp; </td></tr>
  <tr> <td> &nbsp; </td></tr>
  <tr> <td> &nbsp; </td></tr>
  <tr> <td> <B><U>Rules Added </td></tr>
  <tr> <td> &nbsp; </td></tr>
  	<TR> 
  		<TD><B><i><U>Select</TD> <TD>&nbsp;  </TD>
  		<TD><B><U>RuleID</TD> <TD>&nbsp;  </TD>
		<TD><B><U>RuleName</TD> <TD>&nbsp;  </TD>
		<TD><B><U>Condition</TD><TD>&nbsp; </TD>
		<TD><B><U>TrueAction</TD><TD>&nbsp;  </TD>
		<TD><B><U>FalseAction</TD><TD>&nbsp;</TD>
		<TD><B><U>TrueToLink</TD> <TD>&nbsp;  </TD>
		<TD><B><U>FalseToLink</TD> <TD>&nbsp;  </TD>
		<TD><B><U>FromLinks</TD> 
	</TR>
  
  <!-- rulesDisplay -->
  
  	<%
		String s = (String)request.getAttribute("ruleSetXML");
		
		Node n = XMLUtil.parse(s);
		List list = XMLUtil.executeQuery(n,"Rules/Rule");
	%>

	<%
	for (int i=0 ; i<list.size(); i++)
	{
	    
		String ruleID = XMLUtil.getValue((Node)list.get(i),"RuleID");					
		String ruleName = XMLUtil.getValue((Node)list.get(i),"RuleName");
		String condition = XMLUtil.getValue((Node)list.get(i),"Condition");
		String trueAction = XMLUtil.getValue((Node)list.get(i),"Result/TrueAction");
		String falseAction = XMLUtil.getValue((Node)list.get(i),"Result/FalseAction");
		String trueToLink1 = XMLUtil.getValue((Node)list.get(i),"Links/TrueToLink");
		String falseToLink1 = XMLUtil.getValue((Node)list.get(i),"Links/FalseToLink");
		String fromLink1 = XMLUtil.getValue((Node)list.get(i),"Links/FromLink");
		
		if (trueToLink1 == null){ trueToLink1 = ""; }
		if (falseToLink1 == null){ falseToLink1 = ""; }
		if (fromLink1 == null){ fromLink1 = ""; }
		
		if (trueAction == null){ trueAction = ""; }
		if (falseAction == null){ falseAction = ""; }
	%>
	<TR>
		<td>
		<INPUT id="update" type="radio" name="radioRuleID" value="<%=ruleID%>" >
		</td>
		<TD> <%=ruleID%></TD><TD>&nbsp;  </TD>
		<TD> <%=ruleName%></TD><TD>&nbsp;  </TD>
		<TD> <%=condition%></TD><TD>&nbsp;  </TD>
		<TD> <%=trueAction%></TD><TD>&nbsp; </TD>
		<TD> <%=falseAction%></TD><TD>&nbsp;  </TD>
		<TD> <%=trueToLink1%></TD><TD>&nbsp; </TD>
		<TD> <%=falseToLink1%></TD><TD>&nbsp;  </TD>
		<TD> <%=fromLink1%></TD>
    </TR>
    <%
    }
    %>
    
    <%  if (!ruleSetID.equals("")) 
    	{
    %>	
    	<tr> <td> &nbsp; </td></tr>
	    <tr>
		<td> <INPUT id="update" type="submit" name="operationType" value="<%=OperationType.DELETE%>" onclick="return deleteRule();"></td>		  
		<td> <INPUT id="view" type="submit" name="operationType" value="<%=OperationType.VIEW%>" onclick="return viewRule();"></td>
		</tr>	
    <%	
    	}    
    %>
    
</table>
</div>

</FORM>

<jsp:include page="../globalIncludes/Footer.jsp" />

</body>
</head>
</html>