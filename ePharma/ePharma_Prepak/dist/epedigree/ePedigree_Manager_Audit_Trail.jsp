<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.catalog.Constants" %>
<%@ include file='../../includes/jspinclude.jsp'%>
<% String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");
String sessionID = (String)session.getAttribute("sessionID");
String pedigreeID = request.getParameter("PedigreeId");
if(pedigreeID == null )
pedigreeID = request.getParameter("PedigreeId");
if(pagenm == null) { pagenm = "";}
if(tp_company_nm == null) { tp_company_nm = "";}

String nodeName = "";
nodeName = (String)session.getAttribute("NodeName");
%>
<html:html>
   <head>
   
   <script>
 	  function newWindow1(  htmFile  ) {
		   msgWindow = window.open( htmFile, "WindowName", "left=20,top=20,width= 700,height=430,toolbar=0,scrollbar=1,resizable=1,location=0,status=0");
	  }
   </script>
      <title>Raining Data ePharma - ePedigree</title><LINK href="../../assets/epedigree1.css" type="text/css" rel="stylesheet">
   </head>
	<body>
	  <%@include file='topMenu.jsp'%>
	  <%List list=(List)request.getAttribute("AUDTL"); %>
		
           <form action="/dist/epedigree/AuditTrail.do"  method="post">
             <bean:size id="aud" name="<%=Constants.AUDITTRAIL_DETAILS %>" />
	         <input type="hidden" name="aud" value="<%=aud%>">
            <tr>
							 <td colspan="2">
							   <table id="Table11" cellSpacing="1" cellPadding="1" width="100%" align="left" bgColor="white" border="0">
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
							   	 </table>
							   	
													                 
				                
								<tr>
					<Td colspan="2"><br>
				    	<table border="0" cellpadding="0" cellspacing="1" width="100%" ID="Table7">
						</table>
						
						
						
						
				<TABLE id="Table9" cellSpacing="1" cellPadding="0" width="100%" border="0">
				                <tr class="tableRow_Header">
								<TR class="tableRow_Header">
											
											<TD class="type-whrite" height="13">Pedigree ID</TD>
											<TD class="type-whrite" height="13">Pedigree Type</TD>
											<TD class="type-whrite" height="13">Transaction Date</TD>
											<TD class="type-whrite" height="13">Transaction Type</TD>
											<TD class="type-whrite" height="13">Product QTY</TD>											
											<TD class="type-whrite" height="13">Signature</TD>
										</TR>
										                         <logic:equal name="aud" value="0">
									                          			<tr>       
									                          			<td>No records</td>
									                          			</tr>
									                                 </logic:equal>
									                                 <logic:notEqual name="aud" value="0">
									                                  <logic:iterate name="<%=Constants.AUDITTRAIL_DETAILS%>" id="at">
									                                 	
																	<tr class="tableRow_Off">
																		 <td>  	
																		<a href = "#" onclick="javascript:newWindow1('NewPedigreeDetails.do?PedigreeId=<bean:write name="at" property="pedigreeId"/>&name=pedDetails&pedType=<bean:write name="at" property="pedigreetype"/>')" class="typeblue3-link"><bean:write name="at" property="pedigreeId"/></a>
																		</td>
																		<td>
																			<bean:write name="at" property="pedigreetype"/>
																		  </td>
																		
																		<td>
																			<bean:write name="at" property="trasactiondate"/>
																		</td>
																		<td>
																			<bean:write name="at" property="trnsactiontype"/>
																		</td>
																		<td>
																			<bean:write name="at" property="productqty"/>
																		</td>																		
																			<td>
																			<bean:write name="at" property="signature"/>
																		  </td>
																		
																																				
																		
																		
																	</tr>
																	</logic:iterate>							
																	</logic:notEqual> 
																	
										
								</tr>
								</table>
								</td>
								
				                </tr>
				                <TR>
								<TD colSpan="2"><BR>
									<!--<A onclick="MM_openBrWindow('PrintPedigree.jsp?apnId=  ','APN','scrollbars=yes,resizable=yes,width=800,height=600')"
										href="#"><IMG height="27" hspace="10" src="../../assets/images/print.gif" width="27" border="0"></A>EXPORT 
									AS: <INPUT id="Submit1" type="submit" value="XML" name="Submit1">&nbsp;&nbsp; <INPUT id="Submit2" type="submit" value="CSV" name="Submit1">&nbsp;&nbsp;&nbsp;<INPUT id="Submit3" type="submit" value="EDI" name="Submit1">&nbsp;&nbsp;
									<INPUT id="Button1" onclick="MM_openBrWindow('DH2129_PedigreeForm.pdf','APN','scrollbars=yes,resizable=yes,width=800,height=600')"
										type="button" value="PDF" name="Button1">&nbsp;&nbsp;-->
								</TD>
							</TR>
				               
			 </form>
			   </td>
			</tr>
	    </TABLE>
	    <jsp:include page="../globalIncludes/Footer.jsp" />
	  </body>
</html:html>