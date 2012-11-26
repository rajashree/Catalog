<%@ page session="true"%>
<%@ page autoFlush="true"%>
<%@ page import="com.rdta.rules.OperationType"%>
<%@ page import="com.rdta.rules.RuleSetCollection"%>
<%@ page import="com.rdta.commons.xml.XMLUtil"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="java.util.List"%>

<%@ page language="java"%>

<html>
<head>
<title>
Create Rule
</title>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">

</head>
<body>

<jsp:include page="../globalIncludes/TopHeader.jsp" />
<jsp:include page="../globalIncludes/TopLevelLayer1.jsp" />
<jsp:include page="../globalIncludes/TopLevelLayer2.jsp" />
<jsp:include page="./LeftNav.jsp" />

<%
String ruleSetID = request.getParameter("ruleSetID");
String ruleID = request.getParameter("radioRuleID");


String flag="";
if ( request.getAttribute("flag")!= null )
{
flag = (String)request.getAttribute("flag");
}


if (flag.equals("true")) {
ruleSetID = (String)request.getAttribute("ruleSetID");
ruleID = (String)request.getAttribute("ruleID");
}

RuleSetCollection rsc = new RuleSetCollection();
String rXML = rsc.getRuleFromRuleSetXML(ruleSetID,ruleID);

Node n = XMLUtil.parse(rXML);

String ruleName = XMLUtil.getValue(n,"RuleName");
String condition = XMLUtil.getValue(n,"Condition");

String contextSchema = XMLUtil.getValue(n,"ContextSchema");
String categoryCode = XMLUtil.getValue(n,"CategoryCode");

String trueAction = XMLUtil.getValue(n,"Result/TrueAction");
String falseAction = XMLUtil.getValue(n,"Result/FalseAction");

String trueToLink1 = XMLUtil.getValue(n,"Links/TrueToLink");
String falseToLink1 = XMLUtil.getValue(n,"Links/FalseToLink");
String fromLink = XMLUtil.getValue(n,"Links/FromLink");

%>

<FORM name="RS_RuleActionForm" ACTION="/ePharma/dist/rules/RS_RuleDtAction.do"  method="post" >

<div id="rightwhite">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	
  <tr> <td> &nbsp; </td></tr>	
  <tr class="tableRow_Header" > <td> <B><U> Update Rule In RuleSet </td></tr>	
  <tr class="tableRow_Header"> <td> &nbsp; </td></tr>	
  
  <tr class="tableRow_Off"> 
    <td align="left" rowspan="1">RuleSetID</td>    
    <td align="left" rowspan="1" ><INPUT  readonly="readonly" id="ruleSetID" type="text" name="ruleSetID" value="<%=ruleSetID%>"></td>
  </tr>  

  <tr class="tableRow_On" > 
    <td  align="left" rowspan="1">RuleID</td>    
    <td  align="left" rowspan="1" ><INPUT  id="ruleID" type="text" name="ruleID" value="<%=ruleID%>"></td>
  </tr>
  
  <tr class="tableRow_Off"> 
    <td  align="left" rowspan="1">RuleName</td>    
    <td  align="left" rowspan="1" ><INPUT id="ruleName" type="text" name="ruleName" value="<%=ruleName%>"></td>
  </tr>
  
  <tr class="tableRow_On"> 
    <td  align="left" rowspan="1">Condition</td>    
    <td  align="left" rowspan="1" ><INPUT id="condition" type="text" name="condition" value="<%=condition%>"></td>
  </tr>
  
  <tr class="tableRow_Off"> 
    <td  align="left" rowspan="1">Context Schema</td>    
    <td  align="left" rowspan="1" ><INPUT id="contextSchema" type="text" name="contextSchema" value="<%=contextSchema%>"></td>
  </tr>      
  
  <tr class="tableRow_Off"> 
    <td  align="left" rowspan="1">CategoryCode</td>    
    <td  align="left" rowspan="1" ><INPUT id="categoryCode" type="text" name="categoryCode" value="<%=categoryCode%>"></td>
  </tr>  

  <tr class="tableRow_On"> 
    <td  align="left" rowspan="1">True Action</td>    
    <td  align="left" rowspan="1" ><INPUT id="trueAction" type="text" name="trueAction" value="<%=trueAction%>"></td>
  </tr>
  
  <tr class="tableRow_Off"> 
    <td  align="left" rowspan="1">True Action</td>    
    <td  align="left" rowspan="1" ><INPUT id="falseAction" type="text" name="falseAction" value="<%=falseAction%>"></td>
  </tr>
  
  <tr class="tableRow_On"> 
    <td  align="left" rowspan="1">True To Link</td>    
    <td  align="left" rowspan="1" ><INPUT id="trueToLink1" type="text" name="trueToLink1" value="<%=trueToLink1%>"></td>
  </tr>    
  
  <tr class="tableRow_Off"> 
    <td  align="left" rowspan="1">False To Link</td>    
    <td  align="left" rowspan="1" ><INPUT id="falseToLink1" type="text" name="falseToLink1" value="<%=falseToLink1%>"></td>
  </tr>      
  
  <tr class="tableRow_On"> 
    <td  align="left" rowspan="1">From Link</td>    
    <td  align="left" rowspan="1" ><INPUT id="fromLink" type="text" name="fromLink" value="<%=fromLink%>"></td>
  </tr>   
  
  <tr class="tableRow_Header">
  <td align="left" rowspan="1">
  <INPUT id="save" type="submit" name="operationType" value="<%=OperationType.UPDATE%>">
  </td>
  </tr>
     
  
</table>
</div>

</FORM>

<jsp:include page="../globalIncludes/Footer.jsp" />

</body>
</head>
</html>