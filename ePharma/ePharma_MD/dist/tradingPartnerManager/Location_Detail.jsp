
<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.catalog.session.TradingPartnerContext"%>
<%@ page import="com.rdta.catalog.Constants"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.commons.persistence.QueryRunner"%>
<%@ page import="com.rdta.commons.persistence.QueryRunnerFactory"%>

<%
String isSave=(String)request.getAttribute("saveSuccess");
if(isSave==null)isSave="";
System.out.println("isSave"+isSave);
String isUpdate=(String)request.getAttribute("updateSuccess");
if(isUpdate==null)isUpdate="";
System.out.println("isUpdate"+isUpdate);
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
<body >
<%
QueryRunner queryrunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
String tp_company_nm = request.getParameter("tp_company_nm");
    String pagenm =  request.getParameter("pagenm");
    String sessionID = (String)session.getAttribute("sessionID");
%>

<%@include file='../epedigree/topMenu.jsp'%>

<%

    Node locationNode =  (Node)request.getAttribute("LocationInfo");

	String name = "";
	String GLN ="";
	String description = "";
	String GIAI ="";
	String GPS = "";
	String type ="";

	String event = ""; 
	String latitude = "";
	String longitude = "";

	String phone =""; 
	String fax = "";

	String line1 ="";
	String line2 ="";
	String city = "";
	String state ="";
	String country = "";
	String zip = "";

	String inFormat =""; 
	String outFormat = "";


	 if(locationNode != null ) {


		name = CommonUtil.jspDisplayValue(locationNode,"name");
		GLN = CommonUtil.jspDisplayValue(locationNode,"GLN");
		description = CommonUtil.jspDisplayValue(locationNode,"description");
		GIAI = CommonUtil.jspDisplayValue(locationNode,"GIAI");
		GPS = CommonUtil.jspDisplayValue(locationNode,"GPS");
		type = CommonUtil.jspDisplayValue(locationNode,"type");

		event = CommonUtil.jspDisplayValue(locationNode,"event");
		latitude = CommonUtil.jspDisplayValue(locationNode,"latitude");
		longitude = CommonUtil.jspDisplayValue(locationNode,"longitude");


		phone = CommonUtil.jspDisplayValue(locationNode,"phone");
		fax = CommonUtil.jspDisplayValue(locationNode,"fax");




		line1 = CommonUtil.jspDisplayValue(locationNode,"address/line1");
		line2 = CommonUtil.jspDisplayValue(locationNode,"address/line2");
		city = CommonUtil.jspDisplayValue(locationNode,"address/city");
		state = CommonUtil.jspDisplayValue(locationNode,"address/state");
		country = CommonUtil.jspDisplayValue(locationNode,"address/country");
		zip = CommonUtil.jspDisplayValue(locationNode,"address/zip");

		inFormat = CommonUtil.jspDisplayValue(locationNode,"inFormat");
		outFormat = CommonUtil.jspDisplayValue(locationNode,"outFormat");


	 }

%>

<FORM name= "LocationForm" ACTION="Location.do?&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>&tpList=view&tlList=view"  method="post" >


<div id="rightwhite2">
<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
  <tr> 
    <td width="1%" valign="middle" class="td-rightmenu"><img src="./assets/images/space.gif" width="10" height="10"></td>
    <!-- Messaging -->
    <td width="99%" valign="middle" class="td-rightmenu">
	
			<jsp:include page="./TPHeader.jsp" />
	  </td>
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
                  <td class="title" colspan="4">Location Details</td>
                </tr>	

	<%

    TradingPartnerContext context = (TradingPartnerContext)session.getAttribute(Constants.SESSION_TP_CONTEXT); 
    String tpName = context.getTpName();
    String tpGenId = context.getTpGenId();
%>
				
				<tr class="tableRow_On"> 
                  <td  colspan="4" class="td-typeblack"> Trading Partner :<%=tpName%> </td>
				 </tr>

					<TR class="tableRow_On">
						<TD>Location Name:</TD>
						<TD><INPUT id="Text1" value="<%= name %>" name="name"></TD>
						<TD>Location GLN:</TD>
						<TD><INPUT id="Text21" value="<%= GLN %>" name="GLN"></TD>
						
					</TR>
					<TR class="tableRow_Off">
						<TD>Location GIAI:</TD>
						<TD><INPUT id="Text2" value="<%= GIAI %>" name="GIAI"></TD>
						<TD>Location GPS:</TD>
						<TD><INPUT id="Text3" value="<%= GPS %>" name="GPS"></TD>
					</TR>
					<TR class="tableRow_On">
						<TD>Description:</TD>
						<TD colSpan="3"><TEXTAREA id="Textarea1" name="description" rows="3" cols="60"><%= description %></TEXTAREA></TD>
					</TR>
					<TR class="tableRow_Off">
						<TD>Location Type:</td>
						<TD><select name="type" size="1">
						
						<option value="<%= type %>"><%= type %></option>
						<option value="Manufacturer">Manufacturer</option>
						<option value="Retailer">Retailer</option>
						<option value="WH">Ware House</option>
						<option value="Truck">Truck</option>
						<option value="Boat">Boat</option>
						<option value="Dock Doot">Dock Door</option>
						<option value="Door">Door</option>
						<option value="Shelf">Shelf</option>
						<option value="Garage">Garage</option>
						<option value="Other">Other</option></select></TD>

						
						<TD>Location Event:</td>
						<TD><INPUT id="Text5" value="<%= event %>"  name="event"> </TD>
					</TR>
					<TR class="tableRow_On">
						<TD>Location Latitude:</td>
						<TD><INPUT id="Text4" value="<%= latitude %>" name="latitude"></TD> 
						<TD> Location Longitude:</td>
						<TD><INPUT id="Text6" value="<%= longitude %>" name="longitude"></TD>
					</TR>

					<TR class="tableRow_Off">
						<TD>Phone:</td>
						<TD><INPUT id="Text7" value="<%= phone %>" name="phone"></TD>
						<TD>Fax:</td>
						<TD><INPUT id="Text8" value="<%= fax %>" name="fax"></TD>
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

					<TR class="tableRow_Off">
						<TD>Inbound Format:
						<TD><INPUT id="Text11" value="CSV" name="inFormat"></TD>
						<TD>Outbound Format:
						<TD><INPUT id="Text12" value="XML" name="outFormat"></TD>
					</TR>

				

					<TR class="tableRow_On">
						<TD></TD>
						
						
		
						<TD align="center">
						<%
		if(locationNode != null ) {
	%>  
				<Center><INPUT id="button" type="submit" value="UPDATE" OnClick="return LocationAccessPrivilage()"> </Center>
           
           <%
           }else{
           %> 
           <Center><INPUT id="button" type="submit" value="Save" OnClick="return LocationAccessPrivilage()"> </Center>
           <%
           }
           %>

</TD>
						<TD></TD>
					</TR>
				</TABLE>
            </td>
        </tr>
      </table></div>





	
	

	<%
		if(locationNode != null ) {
	%>
			<INPUT id="hidden1" type="hidden" name="operationType" value="<%=OperationType.UPDATE%>">
			<INPUT id="hidden2" type="hidden" name="locationGenId" value="<%= CommonUtil.jspDisplayValue(locationNode,"genId") %>">
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

 
function LocationAccessPrivilage()
{

<%
		
        List accessList = queryrunner.returnExecuteQueryStrings("tlsp:validateAccess('"+sessionID+"','4.02','Update')");
		String viewStatus = accessList.get(0).toString();
		System.out.println("The Read is "+viewStatus);
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


<%

if(isSave.equals("true"))
{%>
<script language="JavaScript" type="text/JavaScript">
<!--
 window.onload = function() {
	  window.setTimeout( "alert('Saved Successfully')",10);
	 }
 
//-->
</script>
<%}%>

<%
if(isUpdate.equals("true"))
{%>
<script language="JavaScript" type="text/JavaScript">
<!--
 window.onload = function() {
	  window.setTimeout( "alert('Updated Successfully')",10);
	 }
//-->
</script>
<%
request.removeAttribute("updateSuccess");
}%>
