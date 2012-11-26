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
	String sessionID = request.getParameter("sessionID");
	if(sessionID == null){
		sessionID = (String)request.getAttribute("sessionID");
	}
	String pagenm = request.getParameter("pagenm");
	String tp_company_nm =request.getParameter("tp_company_nm");
	if(tp_company_nm == null)tp_company_nm="";
	//System.out.println("tp_company_nm"+tp_company_nm);
	//System.out.println("sessionId"+sessionID);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Raining Data ePharma - GCPIM Product Search</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="./assets/epedigree1.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/JavaScript">

function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}
function AccessCheck(){

		var i = "<%= (String )request.getAttribute("search")%>";
		
		if( i == "No" ){
		
				alert("You Don't Have Permission to Perform Search Operation");
		return false;
		}
	
	

return true;
}
function checkAccess(){


		var j = "<%= (String )request.getAttribute("kitread")%>";
		if( j == "No"){
		
				alert("Access Denied !....");
			
				return false;
		}
		
		
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

}
function selectAll(){
	frm=document.forms[0];	
	var elementslength = frm.elements.length;	
	for(i=0;i<elementslength;i++){		
		frm.check[i].checked=true;				
	}
}
function addProducts(){
	frm=document.forms[0];
	if(validateCheckBox(frm)){
		frm.action="<html:rewrite action="/ProductAddToKit.do?operationType=ADD" />"
		frm.submit();
	}
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



</script>
</head>


		
<%@include file='../epedigree/topMenu.jsp'%>

<body onLoad="AccessCheck();">
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
	//end
	String pName =  request.getParameter("ProductName") == null?"" : request.getParameter("ProductName");
//	System.out.println("This is the product Name "+ProductName);
%>

			<FORM name= "ProductMasterSearchForm" ACTION="ProductMasterSearch1.do"  method="post" >


			<INPUT id="hidden1" type="hidden" name="operationType" value="<%=OperationType.SEARCH%>">
			<INPUT id="hidden2" type="hidden" name="tp_company_nm" value="<%=tp_company_nm%>">
			<INPUT id="hidden3" type="hidden" name="pagenm" value="<%=pagenm%>">
			&nbsp;
			<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">	
			<tr class="tableRow_Header"> 
                  <td class="title" colspan="4">Product Search</td>
                </tr>	
					<TR class="tableRow_On">
						
						<td>Product Name:</td>
						<td><INPUT id="Text4" value="<%=ProductName %>" name="ProductName"></td>
						<td>Manufacturer Name:</td>
						<td><INPUT id="Text5" value="<%=manufacturerName %>"  name="manufacturerName"> </td>
					</TR>
					<TR class="tableRow_On">
						<TD>Lot Number:</td>
						<TD><INPUT id="dea number" value="<%=lotNumber %>" name="lotNumber"></TD>
						<TD>NDC:</td>
						<TD><INPUT id="Text6" value="<%=NDC %>" name="NDC"></TD>
							
					</TR>
					<TR class="tableRow_On">
						<TD>GTIN:</td>
						<TD><INPUT id="dea number" value="<%=GTIN %>" name="GTIN"></TD>
						<TD>Product Type <select name="ProductType">
										<option selected>All</option>
										<option>Products Only</option>	
										<option>Kits Only</option>
										<option>Inside Kit</option>
										</select></TD><TD/>
						
												
					</TR>

					<TR >
							
						
							<TD align="center" colspan="4">
							<input type="button" value="Search" onClick="return submitform()">
							<input type="button" value="Clear" onClick="return clearForm()">   
							 </TD>
					
					</TR>

					<INPUT id="hidden188" type="hidden" name="From" value="<%=from %>">

				</TABLE>	
					


				<%
					List list =  (List)request.getAttribute("ProductMasterListInfo");
			
					if(list != null) { %>
					
					<TABLE cellSpacing="1" cellPadding="4" border="0" width="100%">				
					<tr class="tableRow_Header">
						
						<td class="type-whrite">Kit</td>						
						<td class="type-whrite">Product Name</td>
						<td class="type-whrite">NDC</td>
						<td class="type-whrite">Dosage</td>
						<td class="type-whrite">Manufacturer Name</td>
						
					</tr>
				
				<%	
						String genId = "";
						ProductName = "";
						NDC = "";
						lotNumber = "";
						manufacturerName = "";
						String isKit = "No";
						String prodName = null;
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
				 
						isKit = CommonUtil.jspDisplayValue(productMasterNode,"isKit");
					%><TR class="tableRow_On">	
					<%	if(showKits.equals("true"))
						{
							if(isKit.equals("Yes")){					
			   				   
						
						if(from != null && from.trim().equals("selection") ) {
							out.println("<TD><img src=\"/ePharma/assets/images/kit.GIF\"//></TD>");
							
				%>
						
					<TD><%= ProductName %></TD>
					
					<%
						} else {
						
							String url = "ProductMaster.do";
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
					<TD><a href="ProductAddToKit.do?operationType=ADD&productGenId=<%=genId%>"
						class="typered-bold-link">
						<%= ProductName %></a></TD>

					

					<%
						} else {

							String url = "ShowMasterProductInfo.do";
							if(isKit.equals("Yes"))
								url= "Kit.do";
					%>

					<TD><% if(isKit.equals("Yes"))
				{
					out.println("<img src=\"/ePharma/assets/images/kit.GIF\"//>");
					} 
					
					if(isKit.equals("Yes"))	{				
					%>					
					</TD><TD><% String href = (String) request.getAttribute("kitread");
					if(href == null ) href="";
					if(href.equals("No")){%>
							<a onClick = 'return checkAccess();' href="">				
				<% }else { %>
					<a onClick = 'return checkAccess();' href="<%=url%>?operationType=FIND&genId=<%=genId%>&productName=<%=ProductName%>&pagenm=<%=pagenm%>&tp_company_nm=<%=tp_company_nm%>&sessionID=<%=sessionID%>&catalogName=" class="typered-bold-link">					
		
					<%} }else{ %>
				<%	
					//String href1 = (String) request.getAttribute("kitread");
					//if(href1 == null ) href1="";
					//if(href1.equals("No")){
				//								<a onClick = 'return checkAccess();' href="">				
				%>
				<%//} 
				//else { %>
					</TD><TD><a href="<%=url%>?operationType=FIND&genId=<%=genId%>&prodName=<%=ProductName%>&pagenm=<%=pagenm%>&tp_company_nm=<%=tp_company_nm%>&sessionID=<%=sessionID%>" class="typered-bold-link">										
					<% //  }
					 }%>
					
					<%= ProductName %></a></TD>
					
					<%
						}
					%>


					<TD><%= NDC %></TD>
					<TD><%= dosage %></TD>
					<TD><%= manufacturerName %></TD>
					
					<%}%>
				</TR>
				<%		
						}//end of for loop
				}else { //System.out.println("helollllllllllllllll");%>
						<tr class="tableRow_On"><td colspan='5' align='center' >There are no Product And Kits </td> </tr>
				<%}		
					}//end of if 
                 %>
				</TABLE>
				<%if(list != null) { %>
				            
				<%
						}//end of if
						
				%>
	  </TD>
	  </TR>
	  </TABLE>
 <jsp:include page="../globalIncludes/Footer.jsp" />
</FORM>		
</div>

<!-- <jsp:include page="../globalIncludes/Footer.jsp" /> -->


</body>

</html>
