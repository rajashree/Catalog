<%@ page session="true"%>
<%@ page autoFlush="true"%>
<%@ page import="com.rdta.rules.OperationType"%>
<%@ page language="java"%>

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

<%@include file='../epedigree/test.jsp'%>
<%@include file='../epedigree/topMenu.jsp'%>

<%

//get the attributes that are set
String ruleID = (String)request.getAttribute("ruleID");
String ruleName = (String)request.getAttribute("ruleName");
String contextSchema = (String)request.getAttribute("contextSchema");
String categoryCode = (String)request.getAttribute("categoryCode");
String condition = (String)request.getAttribute("condition");
String trueAction = (String)request.getAttribute("trueAction");
String falseAction = (String)request.getAttribute("falseAction");

String truetoLink = (String)request.getAttribute("truetoLink");
String falsetoLink = (String)request.getAttribute("falsetoLink");
String fromLink = (String)request.getAttribute("fromLink");

String status = (String)request.getAttribute("status");

String selcontextSchema = (String)request.getAttribute("selcontextSchema");
String selcategoryCode = (String)request.getAttribute("selcategoryCode");
String seltrueAction = (String)request.getAttribute("seltrueAction");
String selfalseAction = (String)request.getAttribute("selfalseAction");
String selcondition = (String)request.getAttribute("selcondition");


//if the value is null, set it as ""
if (ruleID == null){ ruleID = ""; }
if (ruleName == null){ ruleName = ""; }
if (contextSchema == null){ contextSchema = ""; }
if (categoryCode == null){ categoryCode = ""; }
if (condition == null){ condition = ""; }
if (trueAction == null){ trueAction = ""; }
if (falseAction == null){ falseAction = ""; }

if (truetoLink == null){ truetoLink = ""; }
if (falsetoLink == null){ falsetoLink = ""; }
if (fromLink == null){ fromLink = ""; }

if (status == null){ status = ""; }

if (selcontextSchema == null){ selcontextSchema = ""; }
if (selcategoryCode == null){ selcategoryCode = ""; }
if (seltrueAction == null){ seltrueAction = ""; }
if (selfalseAction == null){ selfalseAction = ""; }
if (selcondition == null){ selcondition = ""; }


%>

<FORM name="CreateRuleForm" ACTION="/ePharma/dist/rules/CreateRuleAction.do"  method="post" >

<div id="rightwhite">
<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">	
  <tr class="tableRow_Off"> <td colspan="4">&nbsp; </td></tr>
  <tr class="tableRow_Header"> <td class="title" colspan="4">Create Rule </td> </tr>
  <tr class="tableRow_Off"> <td colspan="4">&nbsp; </td></tr>	

  <tr> 
    <td align="left" rowspan="1" class="tableRow_Off">RuleID</td>    
    <td align="left" rowspan="1" class="tableRow_Off"><INPUT  id="ruleID" type="text" name="ruleID" value="<%=ruleID%>"></td>
  </tr>
  
  <tr> 
    <td align="left" rowspan="1" class="tableRow_On">RuleName</td>    
    <td align="left" rowspan="1" class="tableRow_On"><INPUT id="ruleName" type="text" name="ruleName" value="<%=ruleName%>"></td>
  </tr>  
  
  <tr> 
    <td align="left" rowspan="1" class="tableRow_Off">ContextSchema</td>    
    <td align="left" rowspan="1" class="tableRow_Off">
	
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
    <td align="left" rowspan="1" class="tableRow_On">CategoryCode</td>    
    <td align="left" rowspan="1" class="tableRow_On">  
    							  
    						<SELECT id="categoryCode" name="categoryCode">
    						<%	  if(!selcategoryCode.equals("")) {
    						%>
    						<option SELECTED  value="<%=selcategoryCode%>"><%=selcategoryCode%></option>
    						<%=categoryCode%>
    						<% } else { %>    						
    							  <%=categoryCode%>
    						<% } %>	  
    						</SELECT>        							  
    							   
    </td>
  </tr>
  
  <!--
  <tr> 
    <td align="left" rowspan="1">Condition</td>    
    <td align="left" rowspan="1" ><INPUT id="condition" type="text" name="condition" value="<%=condition%>"</td>
  </tr> -->
  
  <tr> 
    <td align="left" rowspan="1" class="tableRow_Off">Condition</td>    
    <td align="left" rowspan="1" class="tableRow_Off">
    						
    						<% 	if(!selcondition.equals("")) { %>
    							  						
    							<SELECT id="condition" name="condition" >    							
    						    <OPTION SELECTED  value="<%=selcondition%>" selected><%=selcondition%></OPTION>
    						<% } else { %>
    							
    							<SELECT id="condition" name="condition" >
    						<% } %>

    						<%=condition%>
    						</SELECT>    
    </td>
  </tr>  
  
  <tr> 
    <td align="left" rowspan="1" class="tableRow_On">TrueAction</td>    
    <td align="left" rowspan="1" class="tableRow_On">
    						<SELECT id="trueAction" name="trueAction">
    						<%	  if(!seltrueAction.equals("")) {
    						%>
    						<option SELECTED value="<%=seltrueAction%>"><%=seltrueAction%></option>
    						<%=trueAction%>
    						<% } else { %>    						
    							  <%=trueAction%>
    						<% } %>	  
    						</SELECT>    
    </td>
  </tr>
  
  <tr> 
    <td align="left" rowspan="1" class="tableRow_Off">FalseAction</td>    
    <td align="left" rowspan="1" class="tableRow_Off">
        						<SELECT id="falseAction" name="falseAction">
    						<%	  if(!selfalseAction.equals("")) {
    						%>
    						<option SELECTED value="<%=selfalseAction%>"><%=selfalseAction%></option>
    						<%=falseAction%>
    						<% } else { %>    						
    							  <%=falseAction%>
    						<% } %>	  
    						</SELECT>
    </td>
  </tr>
  
  <!--
  <tr> 
    <td align="left" rowspan="1">True ToLink</td>    
    <td align="left" rowspan="1" ><INPUT id="truetoLink" type="text" name="truetoLink" value="<%=truetoLink%>"</td>
  </tr>
  
  <tr> 
    <td align="left" rowspan="1">False ToLink</td>    
    <td align="left" rowspan="1" ><INPUT id="falsetoLink" type="text" name="falsetoLink" value="<%=falsetoLink%>"</td>
  </tr>  
  
  <tr> 
    <td align="left" rowspan="1">FromLink</td>    
    <td align="left" rowspan="1" ><INPUT id="fromLink" type="text" name="fromLink" value="<%=fromLink%>"</td>
  </tr>       
  
  <tr> 
    <td align="left" rowspan="1">Status</td>    
    <td align="left" rowspan="1" ><INPUT id="status" type="text" name="status" value="<%=status%>"</td>
  </tr>   -->    
  <tr> <td> &nbsp; </td></tr>	
  <tr>
  	<td align="center" rowspan="1"  class="tableRow_On" colspan="4"><INPUT id="save" type="submit" name="operationType" value="<%=OperationType.ADD%>"></td>
  
  <%
  	if (!ruleID.equals("")) 
  	{ 
  %>
  		<tr>
  		<td align="center" rowspan="1" class="tableRow_Off" colspan="4"><INPUT id="save" type="submit" name="operationType" value="<%=OperationType.UPDATE%>"></td>
  		</tr>
  <%
  	}
  %>
	  </tr>
  
</table>
</div>

</FORM>

<jsp:include page="../globalIncludes/Footer.jsp" />

</body>
</head>
</html>