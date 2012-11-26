<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.commons.xml.XMLUtil"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>

<% 
String sessionID = (String)session.getAttribute("sessionID");
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");
%>

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

<%@include file='../epedigree/topMenu.jsp'%>


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
				<!--<td><a href="StandardCatalogList.do?tpGenId=<%= genId%>class="typered-bold-link"><%= name %></a></td>-->
				<td><a href="StandardCatalogList.do?operationType=FIND&GenId=<%=genId%>" class="typered-bold-link"><%= name %></a></td>

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
           </div>



<jsp:include page="../globalIncludes/Footer.jsp" />


</body>
</html>