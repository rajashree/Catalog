<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.catalog.Constants" %>
<%@ include file='../../includes/jspinclude.jsp'%>
<%@ page import="com.rdta.epharma.epedigree.action.PedigreeStatusForm"%>

<%
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");
String sessionID = (String)session.getAttribute("sessionID");
String pedigreeID =(String) request.getAttribute("PedigreeID");
String envId = request.getParameter("envId");
request.setAttribute("envId",envId);
if(pedigreeID == null )
pedigreeID = request.getParameter("PedigreeId");

System.out.println("Pedigree ID is "+pedigreeID);
session.setAttribute("pedigreeID",pedigreeID);

if(pagenm == null) { pagenm = "";}
if(tp_company_nm == null) { tp_company_nm = "";}

String pedigree="";
String pedId = (String)request.getAttribute("pedId");

%>

<script>

function newWindow(  htmFile  ) {
   msgWindow = window.open( htmFile, "WindowName", "left=20,top=20,width= 600,height=400,toolbar=0,scrollbar=1,resizable=1,location=0,status=0");
}

function MM_openBrWindow(theURL,winName,features) { 
alert
  window.open(theURL,winName,features);
}

function newWindow1(  htmFile  ) {
		   msgWindow = window.open( htmFile, "WindowName", "left=20,top=20,width= 700,height=430,toolbar=0,scrollbar=1,resizable=1,location=0,status=0");
	  }
</script>

<html:html>
<head>
<title>Raining Data ePharma - ePedigree</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<LINK href="../../assets/epedigree1.css" type="text/css" rel="stylesheet">
</head>
<body>
<%@include file='topMenu.jsp'%>
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
								
								</td>
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
											   if(date.length>1){
												   String time = date[1];
												   String t[] = time.split("\\.");
											%>
									     	<%=date[0]%>
									     	</STRONG></FONT></td>
									     	<td class="type-whrite">Transaction Time: <br><FONT color="#ffff00"><STRONG>
									     	<%= t[0] %>
									     	</STRONG></FONT></td>
									     	
									     	<% }else { %>	<%=date[0]%></td><td class="type-whrite">Transaction Time:  <br>	<FONT color="#ffff00"><STRONG>
												N/A
											</STRONG></FONT></td>
									     	<% } %>
									     	
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
							
					<form name="attachmentForm" action="Shipping_Attachment.do?PedigreeId=<%=pedigreeID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree&linkname=Attachment" method="post" enctype="multipart/form-data">
							
							<INPUT type="hidden" name="reply" value="">
							<Td colspan="2"><br>
									<table border="0" cellpadding="0" cellspacing="1" width="100%" ID="Table9">
									</table>
									<TABLE id="Table10" cellSpacing="1" cellPadding="0" width="100%" border="0">
										<TR>
											<TD class="td-typeblack" colSpan="4">Upload Documents :</TD>
										</TR>
										<TR class="tableRow_Off">
										<!--	<TD class="td-menu">Attach File : &nbsp;&nbsp;&nbsp;<input type="text" size="20" name="docName1"></TD> -->	
											<TD class="td-menu">Attach Document : </TD>
											<TD class="td-menu"><input type="file" name="attachFile"></TD>											
											<TD class="td-menu"><input type="submit" size="20" value="Submit Attachment" ></TD>											
										</TR>
										
										<TR>
										<TD>
										<br>
										
										<A HREF = "javascript:newWindow1('/ePharma/servlet/showImageFromTL1?dbname=ePharma&collname=ShippedPedigree&mimetype=application/pdf&nodename=data&topnode=shippedPedigree&*:documentInfo/*:serialNumber=<%=pedigreeID%>')" class="typeblue3-link">View Attachment</A>
										<!-- <logic:iterate name="<%=Constants.SHIPPED_DETAILS%>" id="env" type= "com.rdta.epharma.epedigree.action.PedigreeStatusForm">   
										</logic:iterate> -->	
										<TD>
										</TR>
										
																															
										</TR>
										
								<!--	<TR></TR>
										<TR class="tableRow_Off">
											<TD class="td-menu">Document Name/Type : &nbsp;&nbsp;&nbsp;<input type="text" size="20" name="docName2"></TD>
											<TD class="td-menu"><input type="file" name="file2"></TD>										
											<TD class="td-menu"><input type="submit" size="20" value="Submit Attachement"></TD>												
										</TR>
								-->		
					</form>
					<%@include file='onLoad.jsp'%>
</body>
</html:html>