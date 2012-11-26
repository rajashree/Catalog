<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<%@include file='../../includes/jspinclude.jsp'%>
<%@ page import="com.rdta.catalog.Constants" %>
<%@ page import="com.rdta.epharma.epedigree.action.PedigreeStatusForm"%>
<%@ page import="org.w3c.dom.Node" %>
<%@ page import="java.util.List" %>

<SCRIPT LANGUAGE="javascript">

function newWindow(  htmFile  ) {
   msgWindow = window.open( htmFile, "WindowName", "left=20,top=20,width= 600,height=400,toolbar=0,scrollbar=1,resizable=1,location=0,status=0");
}

function newWindow1(  htmFile  ) {
		   msgWindow = window.open( htmFile, "WindowName", "left=20,top=20,width= 700,height=430,toolbar=0,scrollbar=1,resizable=1,location=0,status=0");
}

</SCRIPT>

<%
String pedigreeID = request.getParameter("PedigreeId");
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String APNID = request.getParameter("pedid");
//String sessionID = request.getParameter("sessionID");
String sessionID = (String)session.getAttribute("sessionID");
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");

%>

<html:html>
	<head>
		<title>Raining Data ePharma - ePedigree - Status History</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">
			<style type="text/css"></style>
			<script language="JavaScript" type="text/JavaScript">
<!--
function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}
//-->
			</script>
			<script language="JavaScript" type="text/JavaScript">
<!--
function MM_openBrWindow(theURL,winName,features) { //v2.0
  window.open(theURL,winName,features);
}

function MM_reloadPage(init) {  //reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
}
MM_reloadPage(true);

function MM_popupMsg(msg) { //v1.0
  alert(msg);
}
//-->
			</script>
	</head>
	<body>
		<%@include file='topMenu.jsp' %> 
		
		<%List lists=(List)request.getAttribute("res"); 
		System.out.println("inside body "+lists); %>
		
			<table border="0" cellspacing="0" cellpadding="0" width="100%" ID="Table1">
				<tr>
					<td></td>
					<td rowspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td>
						<!-- info goes here -->
						<table width="100%" id="Table2" cellSpacing="1" cellPadding="1" align="left" border="0"
							bgcolor="white">
							<tr bgcolor="white">
								<td class="td-typeblack">Pedigree Details </td>
								<!-- <td class="td-typeblack" align="right"><FONT color="#0000cc">Anti Counterfeit Measures</FONT>
								</td>-->
							</tr>
	  
							<tr class="tableRow_Header">
								<td colspan="2">
									<table width="100%" id="Table3" cellSpacing="1" cellPadding="1" align="left" border="0"
										bgcolor="white">
										<tr>
											<td colspan="5">
												<table border="1" cellpadding="1" cellspacing="1" width="100%">
                                                       <%@ include file='Header.jsp'%>									                                                               
												</table>
											</td>
										</tr></td>
							   	 </tr>
							     <tr class="tableRow_Header">
							     <logic:iterate name="<%=Constants.SHIPPED_DETAILS%>" id="env" type= "com.rdta.epharma.epedigree.action.PedigreeStatusForm">
								<tr class="tableRow_Header">
									     	<td class="type-whrite">Pedigree ID : <br><STRONG><FONT color="#ffff00"><a href = "#" onclick="javascript:newWindow1('NewShipping_PedigreeDetails.do?PedigreeId=<bean:write name="env" property="pedigreeid"/>')" class="typeblue3-link"><bean:write name="env" property="pedigreeid"/></FONT></STRONG></td>
									     	<td class="type-whrite">Transaction Date : <br><FONT color="#ffff00"><STRONG>
									     	<% String DateAndTime = env.getDate();
											   String date[] = DateAndTime.split("T"); 
											   if(date.length>0){
											%>
									     	<%=date[0]%>
									     	</STRONG></FONT></td>
									     	<td class="type-whrite">Transaction Time: <br><FONT color="#ffff00"><STRONG>
											<%if(date.length>1){
												   String time = date[1];
												   String t[] = time.split("\\."); %>
									     	<%= t[0] %>
									     	<% } else{%>N/A <%}}%>
									     	</STRONG></FONT></td>

									     	
									     	<td class="type-whrite">Transaction Type: <br><FONT color="#ffff00"><STRONG><bean:write name="env" property="transactiontype"/></STRONG></FONT></td>
									     	<!-- <td class="type-whrite">Transaction # : <br><STRONG><FONT color="#ffff00">
									     	<% String trNum = env.getTrnsaction(); 
									     	   String trType = env.getTransactiontype();if(trType.equals("PurchaseOrderNumber")){
									     	%>
									     	<A HREF=" javascript:newWindow('ePedigree_ViewOrder.jsp?trType=PurchaseOrder&trNum=<%=trNum%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree')" class="typeblue3-link" >
									     	<%}else{%>
									     	<A HREF=" javascript:newWindow('ASN_Details.jsp?trType=DespatchAdvice&trNum=<%=trNum%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree')" class="typeblue3-link" >
									     	<% } %>

									     	<bean:write name="env" property="trnsaction"/></A></FONT></STRONG></td> -->
									     </tr>
								</tr>
								                        
									 </logic:iterate>							
																 
									</table>
								</td>
							</tr>
							<tr>
							<form name= "status1" action="Update.do?pedid=<%=APNID%>&tp_company_nm=&pagenm=pedigree"  method="post" >
						       							
								<Td colspan="2"><br>
									<table border="0" cellpadding="0" cellspacing="1" width="100%" ID="Table9">
									</table>
									<TABLE id="Table10" cellSpacing="1" cellPadding="0" width="100%" border="0">
										<TR>
											<TD class="td-typeblack" colSpan="4">Status History</TD>
										</TR>
										
										<TR class="tableRow_Header">
											<TD class="type-whrite" height="13">Date</TD>
											<TD class="type-whrite" height="13">Status</TD>
											<TD class="type-whrite" height="13">User</TD>
											<TD class="type-whrite" height="13">Comments</TD>
										</TR>
							<% List statusList = (List)request.getAttribute("StatusRes");
							   System.out.println("List results : "+statusList.size()); 
							   if(statusList.size() == 0) { %>
							  <TR class="tableRow_Off" >
											<TD class='td-content' colspan='9' align='center'>No Status...</TD></TR>
								<% } else {%>
							   
          					<logic:iterate name="<%=Constants.SHIPPED_DETAILS%>" id="status" >
                  
										<TR class="tableRow_Off">
											<TD class="td-menu"><bean:write name="status" property="statusDate"/></TD>
											<TD class="td-menu"><bean:write name="status" property="statusValue"/></TD>
											<TD class="td-menu"><bean:write name="status" property="userId"/></TD>
											<TD class="td-menu">None</TD>
										</TR>
										
						  </logic:iterate>
						  <% } %>
									</TABLE>
								</Td>
							</tr>
							<TR>
								<TD colSpan="2"><BR>
									
									    
									     <INPUT id="hidden" type="hidden" name="APNID" value="<%=APNID%>"> 
									     <INPUT id="hidden" type="hidden" name="sessionID" value="<%=sessionID%>">              								
									     
								</TD>
							</TR>
						</table>
						<DIV></DIV>
						<div id="footer">
							<table width="100%" height="24" border="0" cellpadding="0" cellspacing="0" ID="Table19">
								<tr>
									<td width="100%" height="24" bgcolor="#d0d0ff" class="td-menu" align="left">&nbsp;&nbsp;Copyright 
										2005. Raining Data.</td>
								</tr>
							
							</table>
						</div>
					</td></form>
				</tr>
			</table>
		</div>
		
	</body>
</html:html>
