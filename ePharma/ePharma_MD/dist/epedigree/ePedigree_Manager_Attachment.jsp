<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.catalog.Constants" %>
<%@ page import="com.rdta.epharma.epedigree.action.AttachmentForm"%>
<%@ include file='../../includes/jspinclude.jsp'%>
<SCRIPT LANGUAGE="javascript">

function newWindow(  htmFile  ) {
   msgWindow = window.open( htmFile, "WindowName", "left=20,top=20,width= 600,height=400,toolbar=0,scrollbar=1,resizable=1,location=0,status=0");
}
</SCRIPT>
<%
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");
String sessionID = (String)session.getAttribute("sessionID");

String pedigreeID =(String) request.getAttribute("PedigreeID");
String tdate =(String) request.getAttribute("date");
String ttime =(String) request.getAttribute("time");
if(pedigreeID == null )
pedigreeID = request.getParameter("PedigreeId");
if(pagenm == null) { pagenm = "";}
if(tp_company_nm == null) { tp_company_nm = "";}


%>


<html:html>
<head>
<title>Raining Data ePharma - ePedigree</title><LINK href="../../assets/epedigree1.css" type="text/css" rel="stylesheet">
</head>
<script>
 function newWindow1(  htmFile  ) {
		   msgWindow = window.open( htmFile, "WindowName", "left=20,top=20,width= 700,height=430,toolbar=0,scrollbar=1,resizable=1,location=0,status=0");
	  }
</script>
<body>

<%@include file='topMenu.jsp'%>
           <TD class="td-typeblack">
            <html:form action="/dist/epedigree/AttachementDetails.do" method="post">
                  		<table id="Table8" cellSpacing="0" cellPadding="0" width="100%" border="0">
							<tr>
							  <td colspan="2">
							        <tr>
									 <td class="td-typeblack" colspan="7">Pedigree Details  <FONT color="#336600"></FONT></td>
									</tr>
								    <tr>
											<td colspan="5">
												<table border="1" cellpadding="1" cellspacing="1" width="100%">
													<%@ include file="Head2.jsp"%>
											</td>
									</tr>
								</td>
							   	 </tr>
							   	 <logic:empty name="AttachmentForm" >
									       	<tr>       
									      		<td>NO DATA EXIST</td>
									         </tr>
                                   </logic:empty>
                                   <logic:notEmpty name="AttachmentForm" >
									      
									                          	
							             <table border="1" cellpadding="1" cellspacing="1" width="100%">  
							             <tr class="tableRow_Header">
									     	<td class="type-whrite">Pedigree ID : <br><STRONG><FONT color="#ffff00"><a href = "#" onclick="javascript:newWindow1('NewPedigreeDetails.do?PedigreeId=<bean:write name="AttachmentForm" property="pedigreeId"/>&pagenm=pedigree&tp_company_nm=&linkname=ShipSum')" class="typeblue3-link"><bean:write name="AttachmentForm" property="pedigreeId"/></FONT></STRONG></td>
									     										     	
									     	<td class="type-whrite">Transaction Date : 	<br><FONT color="#ffff00"><STRONG><%=tdate%></FONT></STRONG></td>
									     	<td class="type-whrite">Transaction Time : <br>	<FONT color="#ffff00"><STRONG><%=ttime%></FONT></STRONG></td>
									     	
									     	<td class="type-whrite">Transaction Type: <br><FONT color="#ffff00"><STRONG><bean:write name="AttachmentForm" property="transactionType"/></STRONG></FONT></td>
									     	<!--<td class="type-whrite">Transaction # : <br><STRONG><FONT color="#ffff00"><A HREF=" javascript:newWindow('ASN_Details.jsp?trType=DespatchAdvice&trNum=<bean:write name="AttachmentForm" property="transactionNo"/>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree')" class="typeblue3-link" ><bean:write name="AttachmentForm" property="transactionNo"/></A></FONT></STRONG></td>-->
									     </tr>
									      </table> 
								 		</logic:notEmpty>
								 		
									     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							             <tr>
							             
							                <logic:notEmpty property='mimeType' name="AttachmentForm" >
							                  
											<td>View as:
											<a href= "#" onclick="javascript:newWindow1('/ePharma/servlet/showImageFromTL1?dbname=ePharma&collname=ReceivedPedigree&mimetype=<bean:write name='AttachmentForm' property='mimeType'/>&nodename=data&topnode=shippedPedigree&*:documentInfo/*:serialNumber=<%=pedigreeID%>')">																
											<bean:write name='AttachmentForm' property='mimeType'/></a></td>
											</logic:notEmpty>
											 
											<logic:empty property='mimeType' name="AttachmentForm" >
												<TD class='td-content'><b>No Attachment Exists</b></TD>
											</logic:empty>
														
										 </tr>
										
										
								
							
			    </table>
			      </html:form>
           </TD>
          
           </tr>
           </table>
            <jsp:include page="../globalIncludes/Footer.jsp" />
</body>
</html:html>