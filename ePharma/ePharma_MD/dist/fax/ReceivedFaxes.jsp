<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.catalog.Constants" %>
<%@ page import="com.rdta.efax.ReceivedFaxesForm"%>
<%@ include file='../../includes/jspinclude.jsp'%>

<script language="JavaScript" type="text/JavaScript">
function callAction(off){	
	var frm = document.forms[0];		
	frm.action = "<html:rewrite action='./dist/epedigree/ReceivedFaxes.do'/>";
	document.getElementsByName("offset")[0].value = off;
	frm.submit();
}
</script>

<%
String tranType = request.getParameter("trType");
String tranNumber = request.getParameter("trNum");
String tp_company_nm=request.getParameter("tp_company_nm");
String pagenm=request.getParameter("pagenm");
String sessionID = (String)session.getAttribute("sessionID");

System.out.println("order no:"+tranNumber);

List list = (List)request.getAttribute("FaxDetails");
String recs = com.rdta.catalog.Constants.NO_OF_RECORDS;
%>

<%
		String offset = (String)request.getAttribute("offset");
		int intOffSet = -1;
		if(offset == null){
			offset = "0";
		}else{
			intOffSet = Integer.parseInt(offset);
			System.out.println("intOffSet in jsp is :"+intOffSet);
		}
		
		int totCount=0;
			
			if( list !=  null ){				
				if(list.size()==0){
					totCount = 0;			
				}else{
					totCount = list.size();
					System.out.println("The totCount is :"+totCount);
				}	
			
			}
			
	%>
	
	<% 
			int c = 0;
			for(int count=0 ; count<totCount; count++){
				 c = totCount/2;
			 	 int extraCount = totCount - c*2;
				 if(extraCount > 0){
			 		c += 1;
			 		System.out.println("c = "+c);
			 	 } 	 
			} 																		
			int intRecs = Integer.parseInt(recs);
			int dispLast = totCount/intRecs;
			if( totCount % intRecs > 0 ) {
				 dispLast++;
			}
			System.out.println("dispLast in jsp is :"+dispLast);
		 %>


<html:html>
<head>
<title>Raining Data ePharma - ePedigree</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">
<style type="text/css">

</style>
<script language="JavaScript" type="text/JavaScript">

</script>
</head>

<body>
<form>
	<%@include file='../epedigree/topMenu.jsp'%>
	<html:hidden property="pagenm" value="<%=pagenm%>" />
	<html:hidden property="offset" value="<%=pagenm%>" />
	
	<table width="100%" id="Table1" cellSpacing="1" cellPadding="1" align="left" border="0" bgcolor="white">
	<tr>
		<td class="td-rightmenu" vAlign="middle" width="1%"><IMG height="10" src="../../assets/images/space.gif" width="10"></td>
		<!-- Messaging -->
			<td class="td-rightmenu" vAlign="middle" width="99%">
				<table id="Table7" cellSpacing="0" cellPadding="0" width="100%" border="0">
					<tr>
						<td align="left"></td>
						<td align="right"><!-- <img src="../../assets/images/3320.jpg" width="200" height="27"> --><IMG src="../../assets/images/space.gif" width="5"></td>
					</tr>
				</table>
			</td>
	</tr>
	<tr bgcolor="white">
		<td class="type-red" colspan="2">RECEIVED FAXES DETAILS  </td>
	</tr>
	<tr > <td height="15">&nbsp;</td>
    </tr>
	
	<tr>
	<Td colspan="2">
	<table border="0" cellpadding="0" cellspacing="1" width="100%">
	
	<tr class="tableRow_Header">

	<td class="type-whrite" width="25%">Fax Name:</td>
	<td class="type-whrite" width="25%">CSID:</td>
	<td class="type-whrite" width="25%">ANI:</td>
	<td class="type-whrite" width="25%">Date Received:</td>
	</tr>
	<logic:empty  name="FaxDetails" >
	   
	  	 <TR class='tableRow_Off'><TD class='td-content' colspan='9' align='center'>There are no Received Faxes...</TD>
	   </TR>
	</logic:empty >
	
 	<logic:notEqual name="FaxDetails" value="0">
	<logic:iterate name="<%=Constants.FAX_DETAILS%>" id="envd" length="<%=recs%>" offset="<%=offset%>">
	<tr class="tableRow_Off">
 	<td><a href='InitialPedigree.do?operationType=receivedFax&&faxName=<bean:write name="envd" property="faxName"/>'> <bean:write name="envd" property="faxName"/></a>  </td>
 	<td><bean:write name="envd" property="CSID"/></td>
 	<td><bean:write name="envd" property="ANI"/></td>
 	<td><bean:write name="envd" property="dateReceived"/></td>
 	</tr>
 	</logic:iterate>	
 	<TR class="tableRow_On">
	</TR>
 	<% if(totCount > Integer.parseInt(recs)) {%>
		<a href="javascript:callAction(0)">First</a>
		<a href="javascript:callAction(<%=(dispLast-1)*intRecs%>)">Last</a>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<% if(intOffSet+intRecs < totCount){	%>																				 
				<a href="javascript:callAction(<%=intOffSet+intRecs%>)">Next</a>
			<% } %>	
			<% if(intOffSet > 0) { %>
				<a href="javascript:callAction(<%=intOffSet-intRecs%>)">Previous</a>
			<% } %>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			(Displaying records <%=intOffSet+1%> to <%= (intOffSet+intRecs) > totCount ? totCount : (intOffSet+intRecs)%> of <%=totCount%> matching the search criteria)
			<br>
			<br>
	<% } %>	
 	
 	</logic:notEqual>
 	</Td>
	</tr> 	
		
  </table>
  
  <br>
  <br>
  <% if(totCount > Integer.parseInt(recs)) {%>
		<a href="javascript:callAction(0)">First</a>
		<a href="javascript:callAction(<%=(dispLast-1)*intRecs%>)">Last</a>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<% if(intOffSet+intRecs < totCount){	%>																				 
				<a href="javascript:callAction(<%=intOffSet+intRecs%>)">Next</a>
			<% } %>	
			<% if(intOffSet > 0) { %>
				<a href="javascript:callAction(<%=intOffSet-intRecs%>)">Previous</a>
			<% } %>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			(Displaying records <%=intOffSet+1%> to <%= (intOffSet+intRecs) > totCount ? totCount : (intOffSet+intRecs)%> of <%=totCount%> matching the search criteria)
	<% } %>	
  

<div id="footer"> 
  <table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
    <tr> 
      <td width="100%" height="24" bgcolor="#D0D0FF" class="td-menu" align="left">&nbsp;&nbsp;Copyright 2005. 
        Raining Data.</td>
    </tr>
  </table>
</div>
</form>
</body>
</html:html>
