<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<%@include file='../../includes/jspinclude.jsp'%>
<%@ page import="com.rdta.catalog.Constants" %>
<%@ page import="com.rdta.epharma.epedigree.action.PedigreeStatusForm"%>
<%@ page import="org.w3c.dom.Node" %>
<%@ page import="java.util.List" %>

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

		function newWindow(  htmFile  ) {
		   msgWindow = window.open( htmFile, "WindowName", "left=20,top=20,width= 600,height=400,toolbar=0,scrollbar=1,resizable=1,location=0,status=0");
		}

		function newWindow1(  htmFile  ) {
   			msgWindow = window.open( htmFile, "WindowName", "left=20,top=20,width= 700,height=430,toolbar=0,scrollbar=1,resizable=1,location=0,status=0");
		}

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
								<td class="td-typeblack">Pedigree Detail</td>
								<td class="td-typeblack" align="right"><FONT color="#0000cc"></FONT>
								</td>
							</tr>
	  
							<tr class="tableRow_Header">
								<td colspan="2">
									<table width="100%" id="Table3" cellSpacing="1" cellPadding="1" align="left" border="0"
										bgcolor="white">
										<tr>
											<td colspan="5">
												<table border="1" cellpadding="1" cellspacing="1" width="100%">
                                                       <%@ include file='Head2.jsp'%>									                                                               
												</table>
											</td>
										</tr></td>
							   	 </tr>
							     <tr class="tableRow_Header">
							     <logic:iterate name="<%=Constants.SHIPPED_DETAILS%>" id="env" type= "com.rdta.epharma.epedigree.action.PedigreeStatusForm">
								<tr class="tableRow_Header">
									     	<td class="type-whrite">Pedigree ID : <br><a href = "#" onclick="javascript:newWindow1('NewPedigreeDetails.do?PedigreeId=<bean:write name="env" property="pedigreeid"/>&pagenm=pedigree&tp_company_nm=&linkname=ShipSum')" class="typeblue3-link"><STRONG><bean:write name="env" property="pedigreeid"/></STRONG></td>
									     	<td class="type-whrite">Transaction Date :<br> <FONT color="#ffff00"><STRONG>
									     	<% String DateAndTime = env.getDate();
											   String date[] = DateAndTime.split("T"); 
											   if(date.length>1){
												   String time = date[1];
												   String t[] = time.split("\\.");
											%>
									     	<%=date[0]%>
									     	</STRONG></FONT></td>
									     	
									     	<% }else { %>	<%=date[0]%></td><td class="type-whrite">Transaction Time:  <br>	<FONT color="#ffff00"><STRONG>
												N/A
											</STRONG></FONT></td>
									     	<% } %>
									     	
									     	<td class="type-whrite">Transaction Type: <br><FONT color="#ffff00"><STRONG><bean:write name="env" property="transactiontype"/></STRONG></FONT></td>
									     	<!-- <td class="type-whrite">Transaction # : <br><STRONG><FONT color="#ffff00">
									     	<% String trNum = env.getTrnsaction(); 
									     	String trType = env.getTransactiontype();
									     	System.out.println("trType in jsp: "+trType);
									     	if(trType.equals("PurchaseOrderNumber")){%>
									     	<A HREF=" javascript:newWindow('ePedigree_ViewOrder.jsp?trType=PurchaseOrder&trNum=<bean:write name="env" property="trnsaction"/>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree')" class="typeblue3-link" ><bean:write name="env" property="trnsaction"/></A></FONT></STRONG></td>
									     	<%} else{ %>
									     	<A HREF=" javascript:newWindow('ASN_Details.jsp?trType=DespatchAdvice&trNum=<%=trNum%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree')" class="typeblue3-link" >
									     	<bean:write name="env" property="trnsaction"/></A></FONT></STRONG></td> 
									     	<%}%>-->
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
									<!--<A onclick="MM_openBrWindow('PrintPedigree.jsp?apnId=<%=APNID%>','APN','scrollbars=yes,resizable=yes,width=800,height=600')"
										href="#"><IMG height="27" hspace="10" src="../../assets/images/print.gif" width="27" border="0"></A>EXPORT 
									AS: <INPUT id="Submit1" type="button" value="XML" name="Submit1">&nbsp;&nbsp;
									 <INPUT id="Submit2" type="button" value="CSV" name="Submit1">&nbsp;&nbsp;&nbsp;
									 <INPUT id="Submit3" type="button" value="EDI" name="Submit1">&nbsp;&nbsp;
									<INPUT id="Button1" onclick="MM_openBrWindow('DH2129_PedigreeForm.pdf','APN','scrollbars=yes,resizable=yes,width=800,height=600')"
										type="button" value="PDF" name="Button1">&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;-->
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									    <!-- <INPUT  type="button" value="UPDATE">  -->
									    
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
