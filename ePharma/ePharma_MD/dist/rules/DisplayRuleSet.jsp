<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.commons.xml.XMLUtil"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.rules.OperationType"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Raining Data ePharma - Trading Partner Manager</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/JavaScript">
<!--

function displayRuleSet()
{
    frm = document.forms[0]
    frm.action = "CreateRuleSetAction.do"
    frm.submit();
    return true;

}
//-->
</script>
</head>
<body>

<%@include file='../epedigree/test.jsp'%>
<%@include file='../epedigree/topMenu.jsp'%>


<div id="rightwhite">
<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
  <tr> 
    <td width="1%" valign="middle" class="td-rightmenu"><img src="./assets/images/space.gif" width="10" height="10"></td>
    
  </tr>
  <tr> 
    <td>&nbsp;</td>
    <td><table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr> 
          <td><img src="./assets/images/space.gif" width="30" height="12"></td>
          <td rowspan="2">&nbsp;</td>
        </tr>

        <tr> 
          <td> 
            <!-- info goes here -->
              <table width="100%" cellSpacing="0" cellPadding="0" align="left" border="0"
			bgcolor="white">

			<tr>
			<td align="left">



<%

	//String ccList = request.getParameter("ccList") == null?"" : request.getParameter("ccList");
	String ccList =(String) request.getAttribute("ccList");

%>

			<FORM name= "DisplayRuleSetForm" ACTION="DisplayRuleSetAction.do"  method="post" >

			<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">	
			<tr class="tableRow_Header"> 
                  	<td class="title" colspan="4">RuleSet Search</td>
                	</tr>	
					<TR class="tableRow_Off">
						<TD>Category Code:</td>
						<td align="left" rowspan="1" >
						      <SELECT id="ccList" name="ccList">
    						  <%=ccList%>
    						  </SELECT>    
    					</td>
					</TR>
					
					<TR >						
						<TD><INPUT id="find" type="submit" name="operationType" value="<%=OperationType.FIND%>"> </TD>
					</TR>


			</td>
			</tr>
               
                <tr> 
                  <td align="left">
								
					<tr class="tableRow_Header">
						<td class="type-whrite">Select</td>
						<td class="type-whrite">RuleSetID</td>
						<td class="type-whrite">RuleSetName</td>
						<td class="type-whrite">CategoryCode</td>
					</tr>
	<%
	String s = (String)request.getAttribute("ruleSetDisplay");
	
	Node n = XMLUtil.parse(s);
	List list = XMLUtil.executeQuery(n,"RuleSet"); 
	
	if (list != null) {
		String ruleSetID = "";
		String ruleSetName = "";
		String category = "";
	}
	%>
	
	<%
	for (int i=0 ; i<list.size(); i++)
	{
		String ruleSetID = XMLUtil.getValue((Node)list.get(i),"RuleSetID");
		String ruleSetName = XMLUtil.getValue((Node)list.get(i),"RuleSetName");
		String categoryCode = XMLUtil.getValue((Node)list.get(i),"CategoryCode");
	
		
		if (ruleSetID == null){ ruleSetID = ""; }
		if (ruleSetName == null){ ruleSetName = ""; }
		if (categoryCode == null){ categoryCode = "-"; }

	%>
	
		<tr class="tableRow_On">
		<td class="typered-bold-link">
		<INPUT id="update" type="radio" name="radioRuleSetID" value="<%=ruleSetID%>" >
		</td>		
		
		<td class="typered-bold-link"> <%=ruleSetID%> </td>
		<td class="typered-bold-link"> <%=ruleSetName%> </td>		
		<td class="typered-bold-link"> <%=categoryCode%> </td>		
	<%
	}
	%>  
	<tr>
	<td> <INPUT type="submit" name="operationType" value="VIEW" onclick="return displayRuleSet();"></td>		  
	</tr>


	  </td>
	  </tr>
	  </table>

	</FORM>

</div>



<jsp:include page="../includes/Footer.jsp" />


</body>
</html>
