

<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.commons.xml.XMLUtil"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.catalog.session.TradingPartnerContext"%>
<%@ page import="com.rdta.catalog.Constants"%>



<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Raining Data ePharma - Trading Partner Manager</title>
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


//-->
</script>
</head>
<body>

<jsp:include page="../globalIncludes/TopHeader.jsp" />
<jsp:include page="../globalIncludes/TopLevelLayer1.jsp" />
<jsp:include page="../globalIncludes/TopLevelLayer2.jsp" />
<jsp:include page="./LeftNav.jsp" />


<div id="rightwhite">
<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
  <tr> 
    <td width="1%" valign="middle" class="td-rightmenu"><img src="./assets/images/space.gif" width="10" height="10"></td>
    
  </tr>
  <tr> 
    <td>&nbsp;</td>
    <td><table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr> 
          <td><img src="./assets/images/space.gif" width="30" height="12"></td>
          <td rowspan="2">&nbsp;</td>
        </tr>
        <!-- Breadcrumb -->
        <!-- <tr> 
            <td><a href="#" class="typegray1-link">Home</a><span class="td-typegray"> 
              - </span><a href="#" class="typegray1-link">ePedigree</a></td>
          </tr> -->
        <tr> 
          <td> 
            <!-- info goes here -->
              <table width="100%" cellSpacing="0" cellPadding="0" align="left" border="0"
			bgcolor="white">

			<tr>
			<td align="left">



<%

	String LotNumberForm = request.getParameter("LotNumber") == null ? "" : request.getParameter("LotNumber");
	String ManufacturerNameForm = request.getParameter("ManufacturerName") == null?"" : request.getParameter("ManufacturerName");
	String LegendDrugNameForm = request.getParameter("LegendDrugName") == null?"" : request.getParameter("LegendDrugName");
	String NDCForm = request.getParameter("NDC") == null?"" : request.getParameter("NDC");

%>

			<FORM name= "ProductMasterSearchForm" ACTION="ProductMasterSearch.do"  method="post" >

			<INPUT id="hidden1" type="hidden" name="operationType" value="<%=OperationType.SEARCH%>">

			<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">	
					<TR class="tableRow_Off">
						<TD>Legend Drug Name:</td>
						<TD><INPUT id="Text4" value="<%=  LegendDrugNameForm %>" name="LegendDrugName"></TD>
						<TD>Manufacturer Name:</td>
						<TD><INPUT id="Text5" value="<%=  ManufacturerNameForm %>"  name="ManufacturerName"> </TD>
					</TR>
					<TR class="tableRow_On">
						<TD>Lot Number:</td>
						<TD><INPUT id="dea number" value="<%=  LotNumberForm %>" name="LotNumber"></TD>
						<TD>NDC:</td>
						<TD><INPUT id="Text6" value="<%=  NDCForm %>" name="NDC"></TD>
					</TR>

					<TR >
						
							<td align="center" colspan="4"><input type="button" value="Search" onClick="return submitform()"><input type="reset" value="Clear" ></td>
					
					</TR>

				</Table>
			</FORM>
			</td>
			</tr>
               <%	//System.out.println("I am coming here also");
                <tr> 
                  <td align="left">
					<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">				
					<tr class="tableRow_Header">
						<td class="type-whrite">Legend Drug Name</td>
						<td class="type-whrite">NDC</td>
						<td class="type-whrite">Lot Number</td>
						<td class="type-whrite">Manufacturer Name</td>
					</tr>


				<%
					List list =  (List)request.getAttribute("ProductMasterListInfo");


					if(list != null ) {
					String genId = "";
					String LegendDrugName = "";
					String NDC = "";
					String LotNumber = "";
					String ManufacturerName = "";
				

					for(int i=0; i < list.size(); i++) {
					
						Node productMasterNode = XMLUtil.parse((InputStream) list.get(i) );

						genId = CommonUtil.jspDisplayValue(productMasterNode,"genId");

						LegendDrugName = CommonUtil.jspDisplayValue(productMasterNode,"LegendDrugName");

						NDC = CommonUtil.jspDisplayValue(productMasterNode,"NDC");
						LotNumber = CommonUtil.jspDisplayValue(productMasterNode,"LotNumber");

						ManufacturerName = CommonUtil.jspDisplayValue(productMasterNode,"ManufacturerName");

			   %>


				<tr class="tableRow_On">
					<td><a href="ProductAddToKit.do?operationType=ADD&productGenId=<%= genId%>" class="typered-bold-link">
					<%= LegendDrugName %></a></td>
					<td><%= NDC %></td>
					<td><%= LotNumber %></td>
					<td><%= ManufacturerName %></td>


				</tr>

                <%

						}//end of for loop
					}//end of if 
                 %>
			
				</TABLE>
            </td>
        </tr>
      </table>

	  </td>
	  </tr>
	  </table>

</div>



<jsp:include page="../includes/Footer.jsp" />


</body>
</html>
