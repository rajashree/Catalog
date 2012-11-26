<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>

<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.commons.xml.XMLUtil"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>

<%@ page import="com.rdta.commons.persistence.QueryRunner"%>
<%@ page import="com.rdta.commons.persistence.QueryRunnerFactory"%>
<%@ page import="com.rdta.catalog.Constants" %>

<script language="JavaScript" type="text/JavaScript">
function callAction(off){	
	var frm = document.forms[0];		
	frm.action = "<html:rewrite action='/TradingPartnerList.do'/>";
	document.getElementsByName("offset")[0].value = off;
	frm.submit();
}
</script>



<% 
QueryRunner queryrunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String sessionID = (String)session.getAttribute("sessionID");
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");

session.setAttribute("sessionID",sessionID);

List list =  (List)request.getAttribute("TradingPartnerListInfo");
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
function myfunction()
{

<%
String Access=(String)request.getAttribute("Access");
if( Access == null )Access ="";
if(Access.equals("false")){%>
alert("Access Denied...!");
window.open("TradingPartnerList.do?tp_company_nm=&pagenm=TPManager","_self");
<%} else{%>

<%}%>
}





//-->
</script>
</head>


<body onLoad="myfunction();">

<%@include file='../epedigree/topMenu.jsp'%>
<form>
<html:hidden property="pagenm" value="<%=pagenm%>" />
<html:hidden property="offset" value="<%=offset%>" />
<html:hidden property="tp_company_nm" value="<%=tp_company_nm%>" />	

<div id="rightwhite3">
<table width="100%" border="0">
<tr> 
    <td width="100%" valign="top" class="td-rightmenu" style="height:0px;" >
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
			<td align="left"></td>
			<td align="right"><img src="assets/images/space.gif" width="5"></td>
		</tr>	
	</table>
    </td>
</tr>


<% if(tp_company_nm.equals("")) { %>
	<div id="rightwhite">
<% } %>
       

            <!-- info goes here -->
              <table width="100%" cellSpacing="0" cellPadding="0" align="left" border="0"
			bgcolor="white">
                <tr bgcolor="white"> 
                  <td class="td-typeblack">List of Trading Partners</td>
                </tr>
                <tr> 
                  <td align="left">
					<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">				
					<tr class="tableRow_Header">
					<td class="type-whrite">Name</td>
					<td class="type-whrite">Address</td>
					<td class="type-whrite">Phone</td>
					<td class="type-whrite">Fax</td>
					<td class="type-whrite">Contact</td>
					<td class="type-whrite">Email</td>

				</tr>


				<%
					
					String name = "";
					String genId = "";
					String address = "";
					String contact = "";
					String deaNumber = "";
					String phone =""; 
					String fax = "";
					String email = "";
              if(list.size()==0){
              %>
              
              <tr class="tableRow_On">
					 
					<td  colspan="6" class='td-content' align="center" > There are no Trading Partners...</td>
					 
					
				</tr>
              <%
					}
					else{ 
					
					if(totCount > Integer.parseInt(recs)) {%>
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
						(Displaying records <%=intOffSet+1%> to <%= (intOffSet+intRecs) > totCount ? totCount : (intOffSet+intRecs)%> of <%=totCount%>)
						<br>
						<br>
					<% } 	
					
					
					
					%>
					
					<logic:iterate name="<%=Constants.TP_DETAILS%>" id="TPDet" length="<%=recs%>" offset="<%=offset%>">
						<tr class="tableRow_Off">
 							<td><a href="TradingPartnerView.do?operationType=FIND&tpGenId=<bean:write name="TPDet" property="genId"/>&tpName=<bean:write name="TPDet" property="name"/>&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>&tpList=view" class="typered-bold-link" onClick="return ViewAccessPrivilage()"><bean:write name="TPDet" property="name"/></a></td>
 							<td><bean:write name="TPDet" property="address"/></td>
 							<td><bean:write name="TPDet" property="phone"/></td>
 							<td><bean:write name="TPDet" property="fax"/></td>
 							<td><bean:write name="TPDet" property="contact"/></td>
 							<td><bean:write name="TPDet" property="email"/></td>
 						</tr>
 					</logic:iterate>
					
					
					
					
					
					
			<!--		for(int i=0; i < list.size(); i++) {
					
						Node tradingPartnerNode = XMLUtil.parse((InputStream) list.get(i) );


						genId = CommonUtil.jspDisplayValue(tradingPartnerNode,"genId");

						name = CommonUtil.jspDisplayValue(tradingPartnerNode,"name");

						contact = CommonUtil.jspDisplayValue(tradingPartnerNode,"contact");
						phone = CommonUtil.jspDisplayValue(tradingPartnerNode,"phone");
						fax = CommonUtil.jspDisplayValue(tradingPartnerNode,"fax");
						email = CommonUtil.jspDisplayValue(tradingPartnerNode,"email");


						String line1 = CommonUtil.jspDisplayValue(tradingPartnerNode,"address/line1");
						String line2 = CommonUtil.jspDisplayValue(tradingPartnerNode,"address/line2");
						String city = CommonUtil.jspDisplayValue(tradingPartnerNode,"address/city");
						String state = CommonUtil.jspDisplayValue(tradingPartnerNode,"address/state");
						String country = CommonUtil.jspDisplayValue(tradingPartnerNode,"address/country");
						String zip = CommonUtil.jspDisplayValue(tradingPartnerNode,"address/zip");

						address = line1 + " "  + line2 + " " + city + " " + state + " "   + country + " " + zip; 
			   %>


				<tr class="tableRow_On">
					<td><a href="TradingPartnerView.do?operationType=FIND&tpGenId=<%= genId%>&tpName=<%=name%>&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>&tpList=view" class="typered-bold-link" onClick="return ViewAccessPrivilage()"><%= name %></a></td>
					<td><%= address %></td>
					<td><%= phone %></td>
					<td><%= fax %></td>
					<td><%= contact %></td>
					<td><%= email %></td>
				</tr>

                

					} -->
					
				 <%	
					}
                 %> 
				
			
				</TABLE>
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
			(Displaying records <%=intOffSet+1%> to <%= (intOffSet+intRecs) > totCount ? totCount : (intOffSet+intRecs)%> of <%=totCount%>)
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
</html>

<script language="JavaScript" type="text/JavaScript">
<!--

function ViewAccessPrivilage()
{

<%
		
        List accessList = queryrunner.returnExecuteQueryStrings("tlsp:validateAccess('"+sessionID+"','4.01','Read')");
		String viewStatus = accessList.get(0).toString();
		System.out.println("The Read is "+viewStatus);
       if(viewStatus.equals("false"))
       {
%>

alert("Access Denied ....")
return false;
<%}
if(viewStatus.equals("true")){%>
return true;
<%}
 %>

}

//-->
</script>