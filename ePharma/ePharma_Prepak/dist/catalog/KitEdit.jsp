
<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="org.w3c.dom.NodeList"%>
<%@ page import="com.rdta.catalog.session.TradingPartnerContext"%>
<%@ page import="com.rdta.catalog.Constants"%>
<%@ page import="java.io.ByteArrayInputStream"%>
<%@ page import="java.io.InputStream"%>

<%@ page import="java.util.*" %> 

<%@ page import="com.rdta.catalog.trading.model.ProductMaster"%>

<%@ page import="com.rdta.commons.persistence.QueryRunnerFactory"%>
<%@ page import="com.rdta.commons.persistence.QueryRunner"%>

<%@ page import="java.util.*"%>

<%@ page import="com.rdta.commons.xml.XMLUtil" %>

<%
	String tp_company_nm = request.getParameter("tp_company_nm");
	
	if ( tp_company_nm == null) tp_company_nm="";
	String pagenm = request.getParameter("pagenm");
	if(pagenm == null ) pagenm="Catalog";
	String sessionID = (String)session.getAttribute("sessionID");

//	System.out.println("*************************KITEDIT.JSP****"+tp_company_nm+"******"+pagenm+"*****"+sessionID+"***");
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Raining Data ePharma - GCPIM </title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="./assets/epedigree1.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}
function AccessCheck(){

		var i = "<%= (String )request.getAttribute("kitinsert")%>";
		var j = "<%= (String) request.getAttribute("productDelete")%>";
		
		if( i == "false" ){
				document.KitForm.gtin.value="<%=(String) request.getAttribute("gtin")%>"
				document.KitForm.ProductName.value="<%=(String) request.getAttribute("ProductName")%>"
				document.KitForm.Description.value="<%=(String) request.getAttribute("Description")%>"
				alert(" Access Denied.....!  ");
				return false;
		}
		
		if( j=="false"){
		
			document.KitForm.gtin.value="<%=(String) request.getAttribute("gtin")%>"
				document.KitForm.ProductName.value="<%=(String) request.getAttribute("ProductName")%>"
				document.KitForm.Description.value="<%=(String) request.getAttribute("Description")%>"
				alert(" Access Denied.....!  ");
				return false;
		
		}
		
}
function submitform(opt)
{
	
	
	if( document.KitForm.gtin.value == "")
	{
		
		alert("Please Enter GTIN ");
		return false;
	}
	if( document.KitForm.ProductName.value == "")
	{
		alert("Please Enter ProductName ");
		return false;
	}
	if( document.KitForm.Description.value == "")
	{
		alert("Please Enter Description ");
		return false;
	}
	
	if ( opt == "Add" ){
	var j = "<%= (String ) session.getAttribute("addProducttokit")%>";
	if ( j == "false" ){
		alert("Access Denied !..");
		return false;
	}
	
	}
	if( opt == "Remove" ){
	var k ="<%=(String) session.getAttribute("rproductFromKit")%>";
	
	if( k == "false" )
	 {
			alert("Access Denied !..");
			return false;
	}
	}
	document.KitForm.From.value = "selection";
	
	document.KitForm.submit();

	return true;
}

function submitUploadForm()
{
	
	if( document.KitForm.gtin.value == "")
	{
		alert("Please Enter GTIN ");
		return false;
	}
	if( document.KitForm.ProductName.value == "")
	{
		alert("Please Enter ProductName ");
		return false;
	}
	if( document.KitForm.Description.value == "")
	{
		alert("Please Enter Description ");
		return false;
	}
	var i = "<%= (String) session.getAttribute("savingKit")%>";
	
	if( i == "false" )
	{
		alert("Access Denied ! ..");
		return false;
	}
	document.KitForm.operationType.value = 'SAVE';
	
	frm=document.forms[0];
	document.KitForm.submit();
	return true;
}

function validateTextBox(frm) {
		var elements = frm.elements;
		var elementsLength = elements.length;
		var j=0;
		for (var i = 0; i < elementsLength; i++) {				
			if(elements[i].name=="text9" ){	
				j=j+1;
			} 
		}	
			alert("Please select atleast one Product"+j);	
			return false;
	
		return true;
}

//-->
</script>
</head>
<body>

<%@include file='../epedigree/topMenu.jsp'%>

<%
	String sessionStatus =  request.getParameter("sessionStatus");
//	System.out.println("sessitonStatus is this "+sessionStatus );
	
/*	if( sessionStatus != null && sessionStatus.trim().equals("clean")) {

			 //clean session information for reference kit info
			System.out.println(Constants.SESSION_KITREF_CONTEXT);
			if(session != null ) {
			System.out.println("Every Time I am coming here");
               System.out.println(" Clean the session for reference kit ");
			   session.setAttribute(Constants.SESSION_KITREF_CONTEXT,null);
			  session.setAttribute("productInf",null);
			 request.setAttribute("showKit",null);
			}
	}
*/
	// System.out.println("to check this attribute "+Constants.SESSION_KITREF_CONTEXT );
    ProductMaster productMasterObj =  (ProductMaster)session.getAttribute(Constants.SESSION_KITREF_CONTEXT);
    Node productMasterNode = null;

	if(productMasterObj != null) {
//	System.out.println("It is null");
		productMasterNode = productMasterObj.getNode();
	}

	String gtin = "";	
	String ProductName = "";
	String description = "";
	
	List resultOfHtml = new ArrayList();
	
	if(productMasterNode != null ) {

		
		gtin = CommonUtil.jspDisplayValue(productMasterNode,"GTIN");		
		ProductName = CommonUtil.jspDisplayValue(productMasterNode,"ProductName");		
		description = CommonUtil.jspDisplayValue(productMasterNode,"Description");
		}

%>


<FORM name= "KitForm" ACTION="Kit.do"  method="post"   enctype = "multipart/form-data" >

<% String fps = (String)request.getAttribute("fromproductsearch"); 
	if ( fps == null ) fps="";
	%>
<input id="hidden776" type="hidden" name="fromproductsearch" value="<%=fps%>">
<INPUT id="hidden7771" type="hidden" name="From" value="">
<div id="rightwhite">

<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
  <tr> 
    <td width="99%" valign="top" class="td-rightmenu" style="height: 0px;">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td align="left"></td>
			<td align="right"></td>
		</tr>
	</table>
    </td>
 </tr>
 
 

        
        <!-- Breadcrumb -->
       
        <tr> 
          <td> 
            <!-- info goes here -->
              <table width="100%" cellSpacing="0" cellPadding="0" align="left" border="0"
			bgcolor="white">
                
                &nbsp;

                <tr> 
                  <td align="left">
					<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">		
					<tr class="tableRow_Header"> 
                  <td class="title" colspan="4">Kit Details</td>
                </tr>		
					
					<TR class="tableRow_Off">
						<TD>GTIN:</TD>
						<TD><INPUT id="Text2" value="<%= gtin %>" name="gtin"></TD>						
					</TR>
					
					<TR class="tableRow_Off">
						<TD>Kit Name:</td>
						<TD><INPUT id="Text4" value="<%= ProductName %>" name="ProductName"></TD>						
					</TR>
										
					<TR class="tableRow_On">
						<TD>Description:</TD>
						<TD colSpan="3"><TEXTAREA id="Textarea1" name="Description" rows="3" cols="60"><%= description %></TEXTAREA></TD>
					</TR>
					
					<TR class="tableRow_On">

							<table cellSpacing="1" cellPadding="3" border="0" width="100%">
							   <tr class="tableRow_Off" >
							      <td colspan="6"> 

							<INPUT id="button" type="button" value="Add Products" onClick="return submitform('Add')">
								 </tr>
								 <tr class="tableRow_Header" width='100%'>
								 <%
							//	   System.out.println("Product Name is "+ProductName);
								   
								 %>
								 	<td class="type-whrite" align="left" width="80"><a href="SortProducts.do?operationType=ProductName&ProductName=<%=ProductName%>&tp_company_name=&pagenm=Catalog">Product Name</a></td>
									<td class="type-whrite" align="left" width="80"><a href="SortProducts.do?operationType=NDC&ProductName=<%=ProductName%>&tp_company_name=&pagenm=Catalog">NDC</a></td>
									<td class="type-whrite" align="left" width="80"><a href="SortProducts.do?operationType=Dosage&ProductName=<%=ProductName%>&tp_company_name=&pagenm=Catalog">Dosage</a></td>
									<td class="type-whrite" align="left" width="80"><a href="SortProducts.do?operationType=Manufacturer&ProductName=<%=ProductName%>&tp_company_name=&pagenm=Catalog">Manufacturer</a></td>
									<td class="type-whrite" align="left" width="80"><a href="SortProducts.do?operationType=Quantity&ProductName=<%=ProductName%>&tp_company_name=&pagenm=Catalog">Quantity</a></td>
									<td class="type-whrite" align="left" width="80"></td>															
																
								</tr>

								<%
							//	   System.out.println("KitEdit going to show Products");
								   String tablerow="";
								   String pName="";
								   String ndc="";
								   String dosage="";
								   String manufacturer=""; 
								   Node listProduct=null;
								   if ( request.getAttribute("sortedProducts") == null ){
								   		 listProduct = XMLUtil.getNode(productMasterNode,"IncludeProducts");
								   }else
								   {
								   		listProduct =(Node) request.getAttribute("sortedProducts");
								   }
								   if ( listProduct != null){
								   		NodeList productList = listProduct.getChildNodes();
								   		
								  		if(productList != null ){
								//   		System.out.println("list length = "+productList.getLength());
								   	
								   		for (int i=0;i<productList.getLength();i++){
								   		Node listNode = productList.item(i);
								   		if(listNode.hasChildNodes() && (listNode.getChildNodes()).getLength()>2){
							//	   		System.out.println("the Produc Name "+listNode.getNodeName());
							//	   		System.out.println("****************^^^^^^^^");
							//	   		System.out.println("ProductName :" +CommonUtil.jspDisplayValue(listNode,"ProductName"));
							//	   		System.out.println("ProductName1 :" +ProductName+ProductName);
								   		
							//	   		System.out.println("****************^^^^^^^^");
								   		
								   		tablerow=tablerow+"<tr class=\"tableRow_On\"  width='100%' >";
								   		tablerow=tablerow+"<TD class='type-black' align='left'  width='80'>"+CommonUtil.jspDisplayValue(listNode,"ProductName")+"</TD>";
										tablerow=tablerow+"<td class='type-black' align='left'  width='80'>"+CommonUtil.jspDisplayValue(listNode,"NDC").trim()+"</td>";
										tablerow=tablerow+"<td class='type-black' align='left'  width='80'>"+CommonUtil.jspDisplayValue(listNode,"Dosage").trim()+"</td>";
										tablerow=tablerow+"<td class='type-black' align='left' width='80'>"+CommonUtil.jspDisplayValue(listNode,"Manufacturer").trim()+"</td>";
										tablerow=tablerow+"<td class='type-black' align='left' width='80'>"+CommonUtil.jspDisplayValue(listNode,"Quantity").trim()+"</td>";
										String stat = (String) session.getAttribute("rproductFromKit");
										if(stat == null) stat="false";
										if(stat.equals("true")){
										
										tablerow=tablerow+"<td class='type-black' align='center' width='80'><a class='typeblue2-link onClick =\"return submitform('Remove');\" href =removeProduct.do?ProductName="+java.net.URLEncoder.encode(ProductName)+"&gtin="+java.net.URLEncoder.encode(gtin)+"&Description="+java.net.URLEncoder.encode(description)+"&includeName="+java.net.URLEncoder.encode(CommonUtil.jspDisplayValue(listNode,"ProductName"))+"&tp_company_nm=&pagenm=Catalog"+">"+"Remove"+"</a></td><tr>";
								   		}
								   		else
								   		tablerow=tablerow+"<td class='type-black' align='center' width='80'><a class='typeblue2-link' onClick = \"return submitform('Remove');\" href =''"+">"+"Remove"+"</a></td><tr>";
								   	
								   	}
								   }
								   }
								   			   
								    List list= (List) session.getAttribute("showKitnow");
								    if(list != null ){
								    int length = list.size();
								//    System.out.println( "Size ="+length);
								    for ( int k=0;k<length;k++){
								     	Node listNode =(Node) list.get(k);
										
											 
								     	tablerow=tablerow+"<tr class=\"tableRow_On\"  width='100%' >";
								   		tablerow=tablerow+"<TD class='type-black' align='left'  width='80'>"+CommonUtil.jspDisplayValue(listNode,"ProductName").trim()+"</TD>";
										tablerow=tablerow+"<td class='type-black' align='left'  width='80'>"+CommonUtil.jspDisplayValue(listNode,"NDC").trim()+"</td>";
										tablerow=tablerow+"<td class='type-black' align='left'  width='80'>"+CommonUtil.jspDisplayValue(listNode,"Dosage").trim()+"</td>";
										tablerow=tablerow+"<td class='type-black' align='left' width='80'>"+CommonUtil.jspDisplayValue(listNode,"Manufacturer").trim()+"</td>";
										tablerow=tablerow+"<td class='type-black' align='left' width='100'><Input type='text' name='text9'></td><td></td></tr>";
									
							//	    	System.out.println("****&&&Product Name is "+CommonUtil.jspDisplayValue(listNode,"ProductName"));
								    
								    }
								}
								
								}
								 %>
											
										<input type='hidden' name='pName' value="<%=pName%>"/>
										<input type='hidden' name='ndc' value="<%=ndc%>"/>
										<input type='hidden' name='dosage' value="<%=dosage%>"/>
										<input type='hidden' name='manufacturer' value="<%=manufacturer%>"/>							
								   		<INPUT id="hidden" type="hidden" name="tp_company_nm" value="<%=tp_company_nm%>">
											<INPUT id="hidden" type="hidden" name="pagenm" value="<%="Catalog"%>">
						
										<%= tablerow%>			
								
							
							</table>
					 
					</TR>
					<TR class="tableRow_Off">
						<TD colspan="4" align="center">
						
						<INPUT id="button" type="button" value="Save" onClick="return submitUploadForm()">
											
						</TD>
					
					</TR>
				</TABLE>
            </td>
        </tr>
      </table>

	<INPUT id="hidden81" type="hidden" name="isKit" value="Yes">
	
	<%
		if(productMasterNode != null ) {
	%>
			
			<INPUT id="hidden1" type="hidden" name="operationType" value="<%=OperationType.UPDATE%>">
			<INPUT id="hidden3" type="hidden" name="tp_company_nm" value="<%=tp_company_nm%>">
			<INPUT id="hidden4" type="hidden" name="pagenm" value="<%="Catalog"%>">
			<INPUT id="hidden2" type="hidden" name="productGenId" value="<%= CommonUtil.jspDisplayValue(productMasterNode,"genId") %>">
	<%
		} else {
	%>
	  
			<INPUT id="hidden10" type="hidden" name="operationType" value="<%=OperationType.ADD%>">
	  <%
			}
	  %>
	 
<jsp:include page="../globalIncludes/Footer.jsp" />
</Form>
</div>


</body>
</html>
