
<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.catalog.session.TradingPartnerContext"%>
<%@ page import="com.rdta.catalog.Constants"%>



<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Raining Data ePharma - Trading Partner Manager -  Detail</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="./assets/epedigree1.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}
//-->
</script>
</head>
<body>

<jsp:include page="../globalIncludes/TopHeader.jsp" />
<jsp:include page="../globalIncludes/TopLevelLayer1.jsp" />
<jsp:include page="../globalIncludes/TopLevelLayer2.jsp" />
<jsp:include page="./LeftNav.jsp" />

<%
    Node productMasterNode =  (Node)request.getAttribute("ProductMasterInfo");

	String imageURL = "http://"+request.getServerName()+":"+request.getServerPort()+"/"+ request.getContextPath()+"/images/";

	String EPC = "";
	String ParentEPC ="";
	String BarCode = "";
	String NDC ="";
	String LegendDrugName = "";
	String DosageForm ="";

	String DosageStrength = ""; 
	String ContainerSize = "";
	String LotNumber = "";

	String LotExpireDate =""; 
	String PackageUPC = "";
	String MarketingStatus = "";
	String Description ="";

	String Quantity ="";
	String quantityUnitCode ="";
	String ManufacturerName = "";
	String ManufacturerLicense ="";
	String CustodyLicenseNumber = "";
	String TagKillCode = "";
	String EnvironmentalConstraints = "";

	String ProductMarkingLogoLocation = "";
	String PackagingLogoLocation = "";

	String OvertProductMarkingInfo = "";
	String OvertPackagingInfo = "";

	String PackagingImageFileName = "";
	String ProductMarkingImageFileName = "";



	 if(productMasterNode != null ) {

		EPC = CommonUtil.jspDisplayValue(productMasterNode,"EPC");
		ParentEPC = CommonUtil.jspDisplayValue(productMasterNode,"ParentEPC");
		BarCode = CommonUtil.jspDisplayValue(productMasterNode,"BarCode");
		NDC = CommonUtil.jspDisplayValue(productMasterNode,"NDC");
		LegendDrugName = CommonUtil.jspDisplayValue(productMasterNode,"LegendDrugName");
		DosageForm = CommonUtil.jspDisplayValue(productMasterNode,"DosageForm");

		DosageStrength = CommonUtil.jspDisplayValue(productMasterNode,"DosageStrength");
		ContainerSize = CommonUtil.jspDisplayValue(productMasterNode,"ContainerSize");

		LotNumber = CommonUtil.jspDisplayValue(productMasterNode,"LotNumber");


		LotExpireDate = CommonUtil.jspDisplayValue(productMasterNode,"LotExpireDate");
		PackageUPC = CommonUtil.jspDisplayValue(productMasterNode,"PackageUPC");

		MarketingStatus = CommonUtil.jspDisplayValue(productMasterNode,"MarketingStatus");
		Description = CommonUtil.jspDisplayValue(productMasterNode,"Description");


		Quantity = CommonUtil.jspDisplayValue(productMasterNode,"Quantity");
		quantityUnitCode = CommonUtil.jspDisplayValue(productMasterNode,"quantityUnitCode");
		ManufacturerName = CommonUtil.jspDisplayValue(productMasterNode,"ManufacturerName");
		ManufacturerLicense = CommonUtil.jspDisplayValue(productMasterNode,"ManufacturerLicense");
		CustodyLicenseNumber = CommonUtil.jspDisplayValue(productMasterNode,"CustodyLicenseNumber");
		TagKillCode = CommonUtil.jspDisplayValue(productMasterNode,"TagKillCode");
		EnvironmentalConstraints = CommonUtil.jspDisplayValue(productMasterNode,"EnvironmentalConstraints");

		OvertPackagingInfo =  CommonUtil.jspDisplayValue(productMasterNode,"Overt/Packagings/Packaging/Info");

		PackagingImageFileName = CommonUtil.jspDisplayValue(productMasterNode,"Overt/Packagings/Packaging/ImageUrl");



		PackagingLogoLocation = imageURL + PackagingImageFileName ;


		OvertProductMarkingInfo = CommonUtil.jspDisplayValue(productMasterNode,"Overt/ProductMarkings/ProductMarking/Info");

		ProductMarkingImageFileName = CommonUtil.jspDisplayValue(productMasterNode,"Overt/ProductMarkings/ProductMarking/ImageUrl");

		ProductMarkingLogoLocation  = imageURL +  ProductMarkingImageFileName;

	 }

%>

<FORM name= "ProductMasterForm" ACTION="ProductMaster.do"  method="post"  enctype="multipart/form-data" >


<div id="rightwhite">
<table width="100%" border="0" cellspacing="0" cellpadding="0"> 


  <tr> 
    <td width="1%" valign="middle" class="td-rightmenu"><img src="./assets/images/space.gif" width="10" height="10"></td>
    <!-- Messaging -->
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
                <!-- <tr class="tableRow_Header"> 
                  <td class="title">Cardinal Health</td>
                </tr> -->
                <tr> 
                  <td align="left">
					<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">		
					<tr class="tableRow_Header"> 
                  <td class="title" colspan="4">Master Product Details</td>
                </tr>		
					<TR class="tableRow_On">
						<TD>EPC:</TD>
						<TD><INPUT id="Text1" value="<%= EPC %>" name="EPC"></TD>
						<TD>Parent EPC:</TD>
						<TD><INPUT id="Text1" value="<%= ParentEPC %>" name="ParentEPC"></TD>
					</TR>
					<TR class="tableRow_Off">
						<TD>Bar Code:</TD>
						<TD><INPUT id="Text2" value="<%= BarCode %>" name="BarCode"></TD>
						<TD>NDC:</TD>
						<TD><INPUT id="Text3" value="<%= NDC %>" name="NDC"></TD>
					</TR>
					<TR class="tableRow_On">
						<TD>Description:</TD>
						<TD colSpan="3"><TEXTAREA id="Textarea1" name="Description" rows="3" cols="60"><%= Description %></TEXTAREA></TD>
					</TR>
					<TR class="tableRow_Off">
						<TD>Legend Drug Name:</td>
						<TD><INPUT id="Text4" value="<%= LegendDrugName %>" name="LegendDrugName"></TD>
						<TD>Dosage Form:</td>
						<TD><INPUT id="Text5" value="<%= DosageForm %>"  name="DosageForm"> </TD>
					</TR>
					<TR class="tableRow_On">
						<TD>Dosage Strength:</td>
						<TD><INPUT id="dea number" value="<%= DosageStrength %>" name="DosageStrength"></TD>
						<TD>Container Size:</td>
						<TD><INPUT id="Text6" value="<%= ContainerSize %>" name="ContainerSize"></TD>
					</TR>

					<TR class="tableRow_Off">
						<TD>Lot Number:</td>
						<TD><INPUT id="Text7" value="<%= LotNumber %>" name="LotNumber"></TD>
						<TD>Lot Expire Date:</td>
						<TD><INPUT id="Text8" value="<%= LotExpireDate %>" name="LotExpireDate"></TD>
					</TR>
					<TR class="tableRow_On">
						<TD>Package UPC:</td>
						<TD><INPUT id="Text9" value="<%= PackageUPC %>" name="PackageUPC"></TD>
						<TD>Marketing Status:</td>
						<TD><INPUT id="Text10" value="<%= MarketingStatus %>"  name="MarketingStatus"> </TD>
					</TR>

					<TR class="tableRow_Off">
						<TD>Quantity:</td>
						<TD><INPUT id="Text13" value="<%= Quantity %>" name="Quantity"> 
						<INPUT id="Text130" value="<%= quantityUnitCode %>" size="5" name="quantityUnitCode"></TD>
						<TD>Manufacturer Name:</td>
						<TD><INPUT id="Text14" value="<%= ManufacturerName %>"  name="ManufacturerName"> </TD>
					</TR>

					<TR class="tableRow_On">
						<TD>Manufacturer License:</td>
						<TD><INPUT id="Text15" value="<%= ManufacturerLicense %>" name="ManufacturerLicense"></TD>
						<TD>Custody License Number:</td>
						<TD><INPUT id="Text16" value="<%= CustodyLicenseNumber %>"  name="CustodyLicenseNumber"> </TD>
					</TR>

					<TR class="tableRow_Off">
						<TD>Tag Kill Code:</td>
						<TD><INPUT id="Text17" value="<%= TagKillCode %>" name="TagKillCode"></TD>
						<TD>EnvironmentalConstraints:</td>
						<TD><INPUT id="Text18" value="<%= EnvironmentalConstraints %>"  name="EnvironmentalConstraints"> </TD>
					</TR>


					<TR class="tableRow_On">
						<TD noWrap>Overt Packaging Info:</TD>
						<TD align="left"><INPUT id="Text222" value="<%= OvertPackagingInfo %>" name="OvertPackagingInfo"></TD>
						<TD align="center" colSpan="2"> </TD>
					</TR>


					
					<TR class="tableRow_Off">
						<TD noWrap>Overt Packaging Logo:</TD>
						<TD align="left">
						
						
						<INPUT id="File21" type="file" name="packagingFile">
						<INPUT id="File221" type="hidden" name="previousPackagingImageFileName" value="<%= PackagingImageFileName%>" >


						</TD>
						<TD align="center" colSpan="2">
						<img src="<%= PackagingLogoLocation %>">
						</TD>
					</TR>

					<TR class="tableRow_On">
						<TD noWrap>Overt Product Marking Info:</TD>
						<TD align="left"><INPUT id="Text224" value="<%= OvertProductMarkingInfo %>" name="OvertProductMarkingInfo">
						
						
						</TD>
						<TD align="center" colSpan="2"> </TD>
					</TR>

					<TR class="tableRow_Off">
						<TD noWrap>Overt Product Marking Logo:</TD>
						<TD align="left"><INPUT id="File16" type="file" name="productMarkingFile">
						
						
						<INPUT id="File241" type="hidden" name="previousProductMarkingImageFileName" value="<%= ProductMarkingImageFileName%>" >

						</TD>
						<TD align="center" colSpan="2">
						<img src="<%= ProductMarkingLogoLocation %>">
						</TD>
					</TR>



				
					<TR class="tableRow_On">
					
						<TD colspan="4" align="center"><INPUT id="button" type="submit" value="Save"></TD>
				
					</TR>
				</TABLE>
            </td>
        </tr>
      </table></div>

	<INPUT id="hidden81" type="hidden" name="isKit" value="No">
	
	<%
		if(productMasterNode != null ) {
	%>
			<INPUT id="hidden1" type="hidden" name="operationType" value="<%=OperationType.UPDATE%>">
			<INPUT id="hidden2" type="hidden" name="productGenId" value="<%= CommonUtil.jspDisplayValue(productMasterNode,"genId") %>">
	<%
		} else {
	%>
	  
			<INPUT id="hidden10" type="hidden" name="operationType" value="<%=OperationType.ADD%>">
	  <%
			}
	  %>

</Form>

<jsp:include page="../globalIncludes/Footer.jsp" />


</body>
</html>
