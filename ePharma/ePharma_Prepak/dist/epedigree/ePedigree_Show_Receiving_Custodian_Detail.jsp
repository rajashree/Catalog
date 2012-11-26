<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<%@ page import="com.rdta.catalog.Constants" %>

<html:html>
	<head>
		<title>Raining Data ePharma - ePedigree - Trading Partner Details</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">
			<style type="text/css"></style>
			
		<SCRIPT LANGUAGE="javascript">

			function newWindow(  htmFile  ) {
		   		msgWindow = window.open( htmFile, "WindowName", "left=20,top=20,width= 700,height=500,toolbar=0,scrollbar=1,resizable=1,location=0,status=0");
			}
		</SCRIPT>
	</head>
	
	<%
	
	
		String topLine = (String)request.getAttribute("topLine");
		String custodianDet = (String)request.getAttribute("custodianDet");
				
		String sessionID = (String)request.getAttribute("sessionId");
		
		System.out.println("sessionID in ePedigree_Show_Receiving_Custodian_Detail.jsp is "+sessionID);
		
		String PedigreeOrder = request.getParameter("PedigreeOrder");
		
		if(PedigreeOrder == null){
			PedigreeOrder = "0";
		}
		String APNID = request.getParameter("pedId");		
				
		String pagenm = request.getParameter("pagenm");
		String tp_company_nm = request.getParameter("tp_company_nm");		
		
		if(APNID == null){
			APNID = "";
		}	
		
		if(pagenm == null){
			pagenm = "";
		}
		if(tp_company_nm == null){
			tp_company_nm = "";
		}
		
		String mfrLicNum = request.getParameter("mfrLicNum");
		
	 %>
	<body>
		<%@include file='topMenu.jsp'%>
			<table border="0" cellspacing="0" cellpadding="0" width="100%" ID="Table1">
				<tr>
					<td></td>
					<td rowspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td>
						<!-- info goes here -->
						<table width="100%" id="Table1" cellSpacing="1" cellPadding="1" align="left" border="0"
							bgcolor="white">
							<tr bgcolor="white">
								<td class="td-typeblack">Pedigree Detail</td>
								<td class="td-typeblack" align="right"><FONT color="#0000cc">Anti Counterfeit Measures</FONT>
								</td>
							</tr>
							<tr class="tableRow_Header">
								<td colspan="2">
									<table width="100%" id="Table2" cellSpacing="1" cellPadding="1" align="left" border="0"
										bgcolor="white">
										<tr>
											<td colspan="5">
												<table border="1" cellpadding="1" cellspacing="1" width="100%">
											                                                     <TR bgcolor="#ccffff">			
									                    <TD class="type-whrite"  align="center"><STRONG><A href="ePedigree_Manager_Reconcile.jsp?pedid=<%=APNID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree&selpedid=<%=(String)session.getAttribute("PedigreeOrder")%>"><FONT color="#000099">Summary</FONT></A></STRONG></TD>
														<TD class="type-whrite" align="center"><STRONG><a href="ePedigree_Manager_ReconcileFromTo.jsp?pedId=<%=APNID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree&selpedid=<%=(String)session.getAttribute("PedigreeOrder")%>"><FONT color="#000099">From-To</FONT></a></STRONG>
														</TD>
														<TD class="type-whrite" align="center"><STRONG><a href="ProductList.do?pedid=<%=APNID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree&selpedid=<%=(String)session.getAttribute("PedigreeOrder")%>"><FONT color="#000099">Products</FONT></a></STRONG>
														</TD>
														<TD class="type-whrite" align="center"><STRONG><a href="ePedigree_Manager_Reconcile_Manufacturer.jsp?pedid=<%=APNID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree&selpedid=<%=(String)session.getAttribute("PedigreeOrder")%>"><FONT color="#000099">Manufacturer(s)</FONT></a></STRONG>
														</TD>
														<TD class="type-whrite" bgcolor="gold" align="center"><STRONG><a href="ePedigree_Manager_Reconcile_Custody.jsp?pedid=<%=APNID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree&selpedid=<%=(String)session.getAttribute("PedigreeOrder")%>"><FONT color="#000099">Custody</FONT></a></STRONG>
														</TD>
														<TD class="type-whrite" align="center"><STRONG><a href="ePedigree_Manager_Reconcile_Status.jsp?pedid=<%=APNID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree&selpedid=<%=(String)session.getAttribute("PedigreeOrder")%>"><FONT color="#000099">Status 
																		History</FONT></a></STRONG>
														</TD>
														<TD class="type-whrite" align="center"><STRONG><a href="ePedigree_Manager_Reconcile_Returns.jsp?pedid=<%=APNID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree&selpedid=<%=(String)session.getAttribute("PedigreeOrder")%>"><FONT color="#000099">Returns</FONT></a></STRONG>
														</TD>
														<TD class="type-whrite" align="center"><STRONG><a href="ePedigree_Manager_Reconcile_Trail.jsp?pedid=<%=APNID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree"><FONT color="#000099">Audit Trail</FONT></a></STRONG>
														</TD>
													</TR>
												</table>
											</td>
										</tr>
										
										<%=topLine%>
										
										<tr class="tableRow_On">
											<td class="td-menu bold">Custodian</td>
											<td class="td-menu bold"></td>
											<td class="td-menu bold"></td>
											<td class="td-menu bold"></td>
											<td class="td-menu bold"></td>
										</tr>
										<tr class="tableRow_Off">
											<td>
												<%=custodianDet%>
											</td>
											<td/>
											<td/>								
											<td/>								
											<td/>								
											<td/>																
										</tr>
										
										
									</table>
								<%@include file="Footer.jsp"%>
							
							
							
													
							
</html:html>							
