
<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.catalog.session.TradingPartnerContext"%>
<%@ page import="com.rdta.catalog.Constants"%>
<%@ page import="com.rdta.commons.persistence.QueryRunner"%>
<%@ page import="com.rdta.commons.persistence.QueryRunnerFactory"%>
<%@ page import="java.util.List"%>


<%
QueryRunner queryrunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
String tp_company_nm = request.getParameter("tp_company_nm");
    String pagenm =  request.getParameter("pagenm");
    String sessionID = (String)session.getAttribute("sessionID");
    
%>


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

<%@include file='../epedigree/topMenu.jsp'%>

<%
    String tpGenId=(String)request.getParameter("tpGenId");
    if(tpGenId==null)tpGenId="";
    Node tradingPartnerNode =  (Node)request.getAttribute("TradingPartnerInfo");
	String imageLocation = CommonUtil.returnEmptyIfNull((String) request.getAttribute("ImageFileLocation"));

	String name = "";
	String partnerType ="";
	String businessId = "";
	String idType ="";
	String status = "";
	String webURL ="";

	String description = ""; 
	String contact = "";
	String deaNumber = "";

	String phone =""; 
	String fax = "";
	String email = "";
	String notifyURI ="";

	String line1 ="";
	String line2 ="";
	String city = "";
	String state ="";
	String country = "";
	String zip = "";


	 if(tradingPartnerNode != null ) {
	    tpGenId=CommonUtil.jspDisplayValue(tradingPartnerNode,"genId");

		name = CommonUtil.jspDisplayValue(tradingPartnerNode,"name");
		partnerType = CommonUtil.jspDisplayValue(tradingPartnerNode,"partnerType");
		businessId = CommonUtil.jspDisplayValue(tradingPartnerNode,"businessId");
		idType = CommonUtil.jspDisplayValue(tradingPartnerNode,"idType");
		status = CommonUtil.jspDisplayValue(tradingPartnerNode,"status");
		webURL = CommonUtil.jspDisplayValue(tradingPartnerNode,"webURL");

		description = CommonUtil.jspDisplayValue(tradingPartnerNode,"description");
		contact = CommonUtil.jspDisplayValue(tradingPartnerNode,"contact");

		deaNumber = CommonUtil.jspDisplayValue(tradingPartnerNode,"deaNumber");


		phone = CommonUtil.jspDisplayValue(tradingPartnerNode,"phone");
		fax = CommonUtil.jspDisplayValue(tradingPartnerNode,"fax");

		email = CommonUtil.jspDisplayValue(tradingPartnerNode,"email");
		notifyURI = CommonUtil.jspDisplayValue(tradingPartnerNode,"notifyURI");


		line1 = CommonUtil.jspDisplayValue(tradingPartnerNode,"address/line1");
		line2 = CommonUtil.jspDisplayValue(tradingPartnerNode,"address/line2");
		city = CommonUtil.jspDisplayValue(tradingPartnerNode,"address/city");
		state = CommonUtil.jspDisplayValue(tradingPartnerNode,"address/state");
		country = CommonUtil.jspDisplayValue(tradingPartnerNode,"address/country");
		zip = CommonUtil.jspDisplayValue(tradingPartnerNode,"address/zip");


	 }

%>

<FORM name= "TradingPartnerForm" ACTION="TradingPartner.do?&tpGenId=<%=tpGenId%>&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>&tpList=view"  method="post"  enctype="multipart/form-data" >


<div id="rightwhite2">



<% 

 if(tradingPartnerNode != null ) {
%>
<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
  <tr> 
    <td width="1%" valign="middle" class="td-rightmenu"><img src="./assets/images/space.gif" width="10" height="10"></td>
    <!-- Messaging -->
    <td width="99%" valign="middle" class="td-rightmenu">
	
			<jsp:include page="./TPHeader.jsp" />
	  </td>
  </tr>

<%
 }
else{

%>
<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
<tr> 
<td width="0%"></td>
    <td width="100%" valign="middle" class="td-rightmenu"><img src="./assets/images/space.gif" width="10" height="10"></td>
     

  </tr>
<%}%>
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
                  <td class="title" colspan="4">Vendor Details</td>
                </tr>		
					<TR class="tableRow_On">
						<TD>Name:</TD>
						<TD><INPUT id="Text1" value="<%= name %>" name="name"></TD>
						<TD>Partner Type:</TD>
						<TD>
							<SELECT id="Select4" name="partnerType">

							<option value="<%= partnerType %>"><%= partnerType %></option>
							<OPTION value="Manufacturer">Manufacturer</OPTION>
							<OPTION value="Distributor">Distributor</OPTION>
							<OPTION value="Pharmacy">Pharmacy</OPTION>
							<OPTION value="Shipper">Shipper</OPTION>
							</SELECT>
						</TD>
					</TR>
					<TR class="tableRow_Off">
						<TD>Business ID:</TD>
						<TD><INPUT id="Text2" value="<%= businessId %>" name="businessId"></TD>
						<TD>ID Type:</TD>
						<TD><INPUT id="Text3" value="<%= idType %>" name="idType"></TD>
					</TR>
					<TR class="tableRow_On">
						<TD>Description:</TD>
						<TD colSpan="3"><TEXTAREA id="Textarea1" name="description" rows="3" cols="60"><%= description %></TEXTAREA></TD>
					</TR>
					<TR class="tableRow_Off">
						<TD>Status:</td>
						<TD>
							<SELECT id="Select4" name="status">
								<OPTION value="<%= status %>"><%= status %></OPTION>
								<OPTION value="Active">Active</OPTION>
								<OPTION value="Inactive">Inactive</OPTION>							
							</SELECT>
						</TD>
						<TD>Web URL:</td>
						<TD><INPUT id="Text5" value="<%= webURL %>"  name="webURL"> </TD>
					</TR>
					<TR class="tableRow_On">
						<TD>DEA Number:</td>
						<TD><INPUT id="dea number" value="<%= deaNumber %>" name="deaNumber"></TD>
						<TD>Contact:</td>
						<TD><INPUT id="Text6" value="<%= contact %>" name="contact"></TD>
					</TR>
					<TR class="tableRow_Off">
						<TD>Phone:</td>
						<TD><INPUT id="Text7" value="<%= phone %>" name="phone"></TD>
						<TD>Fax:</td>
						<TD><INPUT id="Text8" value="<%= fax %>" name="fax"></TD>
					</TR>
					<TR class="tableRow_On">
						<TD>Email:</td>
						<TD><INPUT id="Text9" value="<%= email %>" name="email"></TD>
						<TD>Notify URI:</td>
						<TD><INPUT id="Text10" value="<%= notifyURI %>"  name="notifyURI"> </TD>
					</TR>

					<TR class="tableRow_Off">
						<TD>Address Line1:</td>
						<TD><INPUT id="Text13" value="<%= line1 %>" name="line1"></TD>
						<TD>Address Line2:</td>
						<TD><INPUT id="Text14" value="<%= line2 %>"  name="line2"> </TD>
					</TR>

					<TR class="tableRow_On">
						<TD>City:</td>
						<TD><INPUT id="Text15" value="<%= city %>" name="city"></TD>
						<TD>State:</td>
						<TD><INPUT id="Text16" value="<%= state %>"  name="state"> </TD>
					</TR>

					<TR class="tableRow_Off">
						<TD>Country:</td>
						<TD><INPUT id="Text17" value="<%= country %>" name="country"></TD>
						<TD>Zip:</td>
						<TD><INPUT id="Text18" value="<%= zip %>"  name="zip"> </TD>
					</TR>
				</TABLE>
				<%
		if(tradingPartnerNode != null ) {
	%>  
				<Center><INPUT id="button" type="submit" value="UPDATE" OnClick="return ViewUpdatePrivilage()"> </Center>
           
           <%
           }else{
           %> 
           <Center><INPUT id="button" type="submit" value="Save" OnClick="return ViewInsertPrivilage()"> </Center>
           <%
           }
           %></td>
        </tr>
      </table></div>





	
	

	<%
		if(tradingPartnerNode != null ) {
	%>
			<INPUT id="hidden1" type="hidden" name="operationType" value="<%=OperationType.UPDATE%>">
			<INPUT id="hidden2" type="hidden" name="tpGenId" value="<%= CommonUtil.jspDisplayValue(tradingPartnerNode,"genId") %>">
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

<script language="JavaScript" type="text/JavaScript">
<!--

function ViewUpdatePrivilage()
{

<%
		
        List accessList = queryrunner.returnExecuteQueryStrings("tlsp:validateAccess('"+sessionID+"','4.01','Update')");
		String viewStatus = accessList.get(0).toString();
		System.out.println("The UpdatePrivilage is "+viewStatus);
       if(viewStatus.equals("false"))
       {
%>

alert("Access Denied ....")
return false;
<%}
if(viewStatus.equals("true")){%>
return true;
<%}
 %>

}

function ViewInsertPrivilage()
{

<%
		
          accessList = queryrunner.returnExecuteQueryStrings("tlsp:validateAccess('"+sessionID+"','4.01','Insert')");
		  viewStatus = accessList.get(0).toString();
		System.out.println("The Insert Privilage is "+viewStatus);
       if(viewStatus.equals("false"))
       {
%>

alert("Access Denied ....")
return false;
<%}
if(viewStatus.equals("true")){%>
return true;
<%}
 %>

}




//-->
</script>
