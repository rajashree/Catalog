

<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.commons.xml.XMLUtil"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>



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
    <!-- Messaging -->
    <td width="99%" valign="middle" class="td-rightmenu"><table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td align="left"> 
            <!-- <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr> 
                  <td class="type-red">[View] </td>
                  <td><a href="#" class="typered-link">Create</a> </td>
                  <td><a href="#" class="typered-link">Delete</a> </td>
                  <td><a href="#" class="typered-link">Duplicate </a></td>
                  <td><a href="#" class="typered-link">Search </a></td>
                  <td><a href="#" class="typered-link">Audit </a></td>
                  <td><a href="#" class="typered-link">Trail</a></td>
                </tr>
              </table> -->
          </td>
          <td align="right">
            <!-- <img src="../../assets/images/3320.jpg" width="200" height="27"> -->
            <img src="./assets/images/space.gif" width="5"></td>
        </tr>
      </table></td>
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
                <tr bgcolor="white"> 
                  <td class="td-typeblack">List of Trading Partners</td>
                </tr>
                <tr> 
                  <td align="left">
					<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">				
					<tr class="tableRow_Header">
					<td class="type-whrite">Name</td>
					<td class="type-whrite">Address</td>
					<td class="type-whrite">Phone</td>
					<td class="type-whrite">Fax</td>
					<td class="type-whrite">Contact</td>
					<td class="type-whrite">Email</td>

				</tr>


				<%
					List list =  (List)request.getAttribute("TradingPartnerListInfo");
					String name = "";
					String genId = "";
					String address = "";
					String contact = "";
					String deaNumber = "";
					String phone =""; 
					String fax = "";
					String email = "";

					for(int i=0; i < list.size(); i++) {
					
						Node tradingPartnerNode = XMLUtil.parse((InputStream) list.get(i) );


						genId = CommonUtil.jspDisplayValue(tradingPartnerNode,"genId");

						name = CommonUtil.jspDisplayValue(tradingPartnerNode,"name");

						contact = CommonUtil.jspDisplayValue(tradingPartnerNode,"contact");
						phone = CommonUtil.jspDisplayValue(tradingPartnerNode,"phone");
						fax = CommonUtil.jspDisplayValue(tradingPartnerNode,"fax");
						email = CommonUtil.jspDisplayValue(tradingPartnerNode,"email");


						String line1 = CommonUtil.jspDisplayValue(tradingPartnerNode,"address/line1");
						String line2 = CommonUtil.jspDisplayValue(tradingPartnerNode,"address/line2");
						String city = CommonUtil.jspDisplayValue(tradingPartnerNode,"address/city");
						String state = CommonUtil.jspDisplayValue(tradingPartnerNode,"address/state");
						String country = CommonUtil.jspDisplayValue(tradingPartnerNode,"address/country");
						String zip = CommonUtil.jspDisplayValue(tradingPartnerNode,"address/zip");

						address = line1 + " "  + line2 + " " + city + " " + state + " "   + country + " " + zip; 
			   %>


				<tr class="tableRow_On">
					<td><a href="TradingPartnerView.do?operationType=FIND&tpGenId=<%= genId%>&tpName=<%=name%>" class="typered-bold-link"><%= name %></a></td>
					<td><%= address %></td>
					<td><%= phone %></td>
					<td><%= fax %></td>
					<td><%= contact %></td>
					<td><%= email %></td>
					
				</tr>

                <%

					}
                 %>
			
				</TABLE>
            </td>
        </tr>
      </table></div>



<jsp:include page="../includes/Footer.jsp" />


</body>
</html>
