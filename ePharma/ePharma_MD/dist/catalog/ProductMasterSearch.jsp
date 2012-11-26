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
<%
	String sessionID = (String) session.getAttribute("sessionID");
	String pagenm = request.getParameter("pagenm");
	if(pagenm == null ) pagenm="Catalog";
	String tp_company_nm = request.getParameter("tp_company_nm");
	if(tp_company_nm == null )tp_company_nm="";
//	System.out.println("tpcompany_nm"+tp_company_nm);
//	System.out.println("sessionId"+sessionID);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Raining Data ePharma - GCPIM Product Search</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="./assets/epedigree1.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}

function submitform()
{
	document.ProductMasterSearchForm.submit();

	return true;
}

function clearForm() {

	document.ProductMasterSearchForm.ProductName.value ="";
	document.ProductMasterSearchForm.manufacturerName.value ="";
	document.ProductMasterSearchForm.lotNumber.value ="";
	document.ProductMasterSearchForm.NDC.value ="";
	document.ProductMasterSearchForm.GTIN.value = "";
	return true;
}
function selectAll(){
	frm=document.forms[0];	
	var elementslength = frm.elements.length;	
	for(i=0;i<elementslength;i++){		
		frm.check[i].checked=true;				
	}
}

function goBack(){
	frm = document.forms[0];
	//operationType=FIND&genId=500147468106681921981577965338572&productName=kit1&pagenm=Catalog&tp_company_nm=&sessionID=null&catalogName=
	frm.action="\ProductAddToKit.do?operationType=ADD";
	
	frm.submit();
	return true;

}
function addProducts(){
	frm=document.forms[0];
	if(validateCheckBox(frm)){
		frm.action="<html:rewrite action="/ProductAddToKit.do?operationType=ADD" />"
		frm.submit();
		return true;
	}
	return false;
}

	function validateCheckBox(frm) {
		var elements = frm.elements;
		var elementsLength = elements.length;
		var isSelected = false;
		var isChkdSelect = false;
		for (var i = 0; i < elementsLength; i++) {				
			if(elements[i].type=="checkbox" ){	
				if(elements[i].checked){				
					isChkdSelect = true;
					break;
				}
			}
		}	
		if (!isChkdSelect) {
			alert("Please select atleast one Product");	
			return false;
		}
		return true;
}


//-->
</script>
</head>
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
  <tr> 
    <td>&nbsp;</td>
    <td>
    <table border="0" cellspacing="0" cellpadding="0" width="100%">
  
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
	
	
	String from = (String) request.getAttribute("From") ;

	if(from == null) {
		 from = (String) request.getParameter("From") ;
	}

	String lotNumber = request.getParameter("lotNumber") == null ? "" : request.getParameter("lotNumber");
	String manufacturerName = request.getParameter("manufacturerName") == null?"" : request.getParameter("manufacturerName");
	String ProductName = request.getParameter("ProductName") == null?"" : request.getParameter("ProductName");
	String GTIN = request.getParameter("GTIN") == null?"" : request.getParameter("GTIN");
	String NDC = request.getParameter("NDC") == null?"" : request.getParameter("NDC");
	//from
	String showKits = request.getParameter("check1")==null?"":request.getParameter("check1");
	
	String kitName = request.getAttribute("kitName") == null?"" : (String)request.getAttribute("kitName");
	
	//end

%>

			<FORM name= "ProductMasterSearchForm" ACTION="ProductMasterSearch.do"  method="post" >
			<INPUT id="hidden1" type="hidden" name="operationType" value="<%=OperationType.SEARCH%>">
			<INPUT id="hidden2" type="hidden" name="tp_company_nm" value="<%=tp_company_nm%>">
			<INPUT id="hidden3" type="hidden" name="pagenm" value="<%=pagenm%>">
			<INPUT id="hidden4" type="hidden" name="kitName" value="<%=kitName%>">
	
			<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">	
			<tr class="tableRow_Header"> 
                  <td class="title" colspan="4">Product Search</td>
                </tr>	
					<TR class="tableRow_On">
						
						<td>Product Name:</td>
						<td><INPUT id="Text4" value="<%=  ProductName %>" name="ProductName"></td>
						<td>Manufacturer Name:</td>
						<td><INPUT id="Text5" value="<%=  manufacturerName %>"  name="manufacturerName"> </td>
					</TR>
					<TR class="tableRow_On">
						<TD>Lot Number:</td>
						<TD><INPUT id="dea number" value="<%=  lotNumber %>" name="lotNumber"></TD>
						<TD>NDC:</td>
						<TD><INPUT id="Text6" value="<%=  NDC %>" name="NDC"></TD>
							
					</TR>
					<TR class="tableRow_On">
						<TD>GTIN:</td>
						<TD><INPUT id="dea number" value="<%=  GTIN %>" name="GTIN"></TD>
						<TD>Product Type <select name="ProductType">
										<option selected>All</option>
										<option>Products Only</option>	
										<option>Kits Only</option>
										<option>Inside Kit</option>
						</select></TD><TD/>
						
						
												
					</TR>

					<TR >
							
						
							<TD align="center" colspan="4">
							<input type='button' value=" Go Back "  onClick="return goBack();" />
							<input type="button" value="Search" onClick="return submitform()" />
							<input type="button" value="Clear" onClick="return clearForm()" />   
							 </TD>
					
					</TR>

					<INPUT id="hidden188" type="hidden" name="From" value="<%=from %>">

				</TABLE>	
					


				<%
					List list =  (List)request.getAttribute("ProductMasterListInfo");
					
					
					if(list != null) {  %>
					
					

					
					<TABLE cellSpacing="1" cellPadding="4" border="0" width="100%">				
					<tr class="tableRow_Header">
						
						<td class="type-whrite"><a href="ProductMasterSearch.do?operationType=sortKit&tp_company_nm=<%=tp_company_nm%>&pagenm=Catalog">Kit</a></td>						
						<td class="type-whrite"><a href="ProductMasterSearch.do?operationType=sortProductName&tp_company_nm=<%=tp_company_nm%>&pagenm=Catalog">Product Name</a></td>
						<td class="type-whrite"><a href="ProductMasterSearch.do?operationType=sortNDC&tp_company_nm=<%=tp_company_nm%>&pagenm=Catalog">NDC</a></td>
						<td class="type-whrite"><a href="ProductMasterSearch.do?operationType=sortDosage&tp_company_nm=<%=tp_company_nm%>&pagenm=Catalog">Dosage</a></td>
						<td class="type-whrite"><a href="ProductMasterSearch.do?operationType=sortManufacturer&tp_company_nm=<%=tp_company_nm%>&pagenm=Catalog">Manufacturer Name</a></td>
						<td class="type-whrite"><a href=javascript:selectAll()>Select All</a></td>
						
					</tr>
				
				<%	
						String genId = "";
						ProductName = "";
						NDC = "";
						lotNumber = "";
						manufacturerName = "";
						String isKit = "No";
					
					for(int i=0; i < list.size(); i++) {
			//			System.out.println(list.size());
						Node productMasterNode = XMLUtil.parse((InputStream) list.get(i) );

						genId = CommonUtil.jspDisplayValue(productMasterNode,"genId");						
						ProductName = CommonUtil.jspDisplayValue(productMasterNode,"ProductName");
						
						if(kitName != null && kitName.equals(ProductName)){
					//		System.out.println("This is the kit name to which we are adding other kits or products....");
							continue;
						}							
						
						String dosage = CommonUtil.jspDisplayValue(productMasterNode,"DosageForm");
						NDC = CommonUtil.jspDisplayValue(productMasterNode,"NDC");
						lotNumber = CommonUtil.jspDisplayValue(productMasterNode,"LotNumber");

						manufacturerName = CommonUtil.jspDisplayValue(productMasterNode,"ManufacturerName");
				 
						isKit = CommonUtil.jspDisplayValue(productMasterNode,"isKit");
					%><TR class="tableRow_On">	
					<%	if(showKits.equals("true"))
						{
							if(isKit.equals("Yes")){					
			   				   
						
						if(from != null && from.trim().equals("selection") ) {
						out.println("<TD><img src=\"/ePharma/assets/images/kit.GIF\"//></TD>");
							
				%>
						
					<TD><a class="typered-bold-link" href="ProductAddToKit.do?operationType=ADD&productGenId=<%= genId%>&tp_company_nm=<%=tp_company_nm%>&pagenm='Catalog'"
						>
						<%= ProductName %></a></TD>
					
					<%
						} else {
						
							String url = "ShowMasterProductInfo.do";
							if(isKit.equals("Yes"))
								url= "Kit.do";
						
						if(isKit.equals("Yes"))
						out.println("<TD><img src=\"/ePharma/assets/images/kit.GIF\"//></TD>");%>
						
						<TD><%= ProductName %></TD>
					<%
						}
					%>


					<TD><%= NDC %></TD>
					<TD><%= lotNumber %></TD>
					<TD><%= manufacturerName %></TD>
				</TR>
		
                <%
						}//end of if
						
						%>
						<% }else{
						%>
						
					
					<%
						if(from != null && from.trim().equals("selection") ) {
					%>
			
					<TD><% if(isKit.equals("Yes"))
				{
					out.println("<img src=\"/ePharma/assets/images/kit.GIF\"//>");
					} %>	</TD>
					<TD><%= ProductName %></TD>

					

					<%
						} else {

							String url = "ShowMasterProductInfo.do";
							if(isKit.equals("Yes"))
								url= "Kit.do";
					%>

					<TD><% if(isKit.equals("Yes"))
				{
					out.println("<img src=\"/ePharma/assets/images/kit.GIF\"//>");
				
				
					} %></TD><TD><a href="<%=url%>?operationType=FIND&genId=<%= genId%>&tp_company_nm=<%=tp_company_nm%>&pagenm=Catalog&sessionId=<%=sessionID%>&prodName=<%=ProductName%>&catalogName="
					class="typered-bold-link">
					<%= ProductName %></a></TD>
						

					<%
						}
					%>

			
					<TD><%= NDC %></TD>
					<TD><%= dosage %></TD>
					<TD><%= manufacturerName %></TD>
					<TD><input type=checkbox name=check value="<%= genId%>"</TD>
							
				<%}%>
				</TR>
				<%		
						}//end of for loop
					}//end of if 
                 %>
			
				</TABLE>
				<%if(list != null) { %>
				<br>
				<tr>
				<td>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
															
				<html:button property="Button" value="Add Product" onclick="addProducts()"/>   
			    
				</td>
				</tr>                   
				<%
						}//end of if
						
				%>
	  </TD>
	  </TR>
	  </TABLE>

</FORM>		
</div>



<jsp:include page="../globalIncludes/Footer.jsp" />


</body>

</html>
