<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.commons.xml.XMLUtil"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.catalog.session.TradingPartnerContext"%>
<%@ page import="com.rdta.catalog.Constants"%>
<%@ page import="com.rdta.catalog.trading.model.ProductMaster"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title>Raining Data ePharma - GCPIM Product List</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="./assets/epedigree1.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/JavaScript">
function function1(){

	alert("Need to be done");
	return false;
} 
</script>
</head>
<%

	String sessionID = request.getParameter("sessionID");
	String pagenm = request.getParameter("pagenm");
	String tp_company_nm =request.getParameter("tp_company_nm");
	
	if(tp_company_nm == null)tp_company_nm="";
	
	if(pagenm == null) pagenm="Catalog";
	if(sessionID == null ) sessionID="";
	
	String lotNumber = request.getParameter("lotNumber") == null ? "" : request.getParameter("lotNumber");
	String manufacturerName = request.getParameter("manufacturerName") == null?"" : request.getParameter("manufacturerName");
	String ProductName = request.getParameter("ProductName") == null?"" : request.getParameter("ProductName");
	String GTIN = request.getParameter("GTIN") == null?"" : request.getParameter("GTIN");
	String NDC = request.getParameter("NDC") == null?"" : request.getParameter("NDC");
	//from
	String showKits = request.getParameter("check1")==null?"":request.getParameter("check1");	 
	//end
	String pName =  request.getParameter("ProductName") == null?"" : request.getParameter("ProductName");
	
	String catName= (String) request.getAttribute("catalogName");
	
	
%>



<body>

<%@include file='../epedigree/topMenu.jsp'%>
<div id="rightwhite">
<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
  <tr> 
    <td width="99%" valign="top" class="td-rightmenu" style="height: 0px;">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td align="left"></td>
			<td align="right"><img src="assets/images/space.gif" width="5"></td>
		</tr>
	</table>
    </td>
 </tr>
 </table>
<TABLE cellSpacing="1" cellPadding="4" border="0" width="100%">				
					<tr>
			<td align="left"></td>
			<td align="right"><img src="assets/images/space.gif" width="5"></td>
		</tr>
					<tr><td>Catalog Name :<%=catName%></td></tr>
					<tr class="tableRow_Header">
						
											
						<td class="type-whrite">Product Name</td>
						<td class="type-whrite">NDC</td>
						<td class="type-whrite">Dosage</td>
						<td class="type-whrite">Manufacturer Name</td>
						
					</tr>
					<%  List list =  (List)request.getAttribute("ProductMasterListInfo");
						int size = list.size();
					if ( size > 0 ){
					for(int i=0; i < list.size(); i++) {
				//		System.out.println(list.size());
						Node productMasterNode = XMLUtil.parse((InputStream) list.get(i) );

						genId = CommonUtil.jspDisplayValue(productMasterNode,"genId");						
						ProductName = CommonUtil.jspDisplayValue(productMasterNode,"ProductName");
						String dosage = CommonUtil.jspDisplayValue(productMasterNode,"DosageForm");
						NDC = CommonUtil.jspDisplayValue(productMasterNode,"NDC");
						lotNumber = CommonUtil.jspDisplayValue(productMasterNode,"LotNumber");

						manufacturerName = CommonUtil.jspDisplayValue(productMasterNode,"ManufacturerName");
				 
					
					
					
					 %>
					<TR class="tableRow_On">	
					<TD><a  href="ShowMasterProductInfo.do?operationType=FIND&genId=<%=genId%>&productName=<%=ProductName%>&pagenm=<%=pagenm%>&tp_company_nm=<%=tp_company_nm%>&sessionID=<%=sessionID%>&catalogName=&prodName=<%=ProductName%>" class="typered-bold-link">					
		<%=ProductName%></TD>
					<TD><%= NDC %></TD>
					<TD><%= lotNumber %></TD>
					<TD><%= manufacturerName %></TD>
				</TR>

<%}}else{%>
	<TR class="tableRow_On"  align='center' ><td colspan='5'> There Are No Products </td> </TR>
		<%}%>			
<TR></TR>
</TABLE>
</div>
</body>
</html>