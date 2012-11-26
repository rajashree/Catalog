<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page session="true" %>
<%@ page import="com.rdta.tlapi.xql.*"%>
<%@ page import="com.rdta.tlapi.xql.Connection"%>
<%@ page import="com.rdta.tlapi.xql.Statement"%>
<%@ page import="com.rdta.tlapi.xql.DataSourceProperties"%>
<%@ page import="com.rdta.tlapi.xql.DataSourceFactory"%>
<%@ page import="com.rdta.Admin.Utility.Helper"%>
<%@ page import="java.io.ByteArrayInputStream"%>
<%@ page import="javax.xml.parsers.DocumentBuilderFactory"%>
<%@ page import="javax.xml.parsers.DocumentBuilder"%>
<%@ page import="java.util.*"%>

<%@include file='../../includes/jspinclude.jsp'%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<%			  
String key = (String)request.getAttribute("googleKey");
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
//String sessionID = request.getParameter("sessionID");
String sessionID = (String)session.getAttribute("sessionID");
String pagenm = request.getParameter("pagenm");
String TagID = (String)request.getAttribute("TagID");
String xmlResultString = "";
String redirURL = "LoginFailed.html";
String sessQuery = "";
String tp_company_nm = "";
String tp_company_id = "";
String servPath1 = request.getContextPath();
	servPath1 = "http://"+request.getServerName()+":"+request.getServerPort()+servPath1; 

com.rdta.tlapi.xql.Connection connection = ConnectTL();
com.rdta.tlapi.xql.Statement statement = getStatement(connection);

//CHECK SESSION IF IT IS A TRADE PARTNER
//IF IS - <div class="roleIcon-distributor"></div> CHANGE TO <div class="roleIcon-extranet"></div>
sessQuery = "for $b in collection('tig:///EAGRFID/SysSessions') ";
sessQuery = sessQuery + "where  $b/session/sessionid = '"+sessionID+"' ";
sessQuery = sessQuery + "return data($b/session/tp_company_nm)";
byte[] xmlResults = ReadTL(statement, sessQuery);
if(xmlResults != null) {
	tp_company_nm = new String(xmlResults);	
	sessQuery = "for $b in collection('tig:///EAGRFID/SysSessions') ";
	sessQuery = sessQuery + "where  $b/session/sessionid = '"+sessionID+"' ";
	sessQuery = sessQuery + "return data($b/session/tp_company_id)";
	xmlResults = ReadTL(statement, sessQuery);
	if(xmlResults != null) {
		tp_company_id = new String(xmlResults);	
	}
}

CloseConnectionTL(connection);

%>

<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
	<script src="http://maps.google.com/maps?file=api&v=1&key=<%=key%>" type="text/javascript"></script>  
		<title>Raining Data ePharma - Track and Trace</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		
		<link href="././assets/epedigree1.css" rel="stylesheet" type="text/css">
		<%
		String ipaddress = request.getServerName();
		int port = request.getServerPort();
		%>
	<script language="JavaScript" type="text/JavaScript">
<!--
var newMapButton = null;
function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}

function submitMapForm(){
	var frm = document.forms[0];	
	var serVal =frm.searchKey.value;		
	frm.action="<html:rewrite  action='/TrackMapsAction.do?pagenm=track&TagID='/>"+serVal;	
	frm.submit();	
		
}

function enableMap(){
	
	frm = document.forms[0];
	var serVal =frm.searchKey.value;
	var dynParentP = document.getElementById("dynMap");
	
	var bDisplay = false;
	
	if((serVal !="") && (serVal !="null") && (frm.criteria.value =='EPC' ) ){
		bDisplay = true;
	}

	if( bDisplay ){
		if( newMapButton == null ){
			var newElt = document.createElement("INPUT" );
			newElt.type="button";
			newElt.name="mapbutton";
			newElt.className="fButton";		
			newElt.id="mapbutton";
			newElt.value="MAP";
			newElt.onclick= function(){ submitMapForm() };	
			dynParentP.appendChild(newElt);		
			newMapButton = newElt;
		}
		else{
			newMapButton.style.visibility = 'visible';
		}

	}
	else{
		if( newMapButton != null ){
			newMapButton.style.visibility = 'hidden';
		}
	}
	
}	

//-->
			</script>
	</head>
	<body onload="onLoad();onLoad2();onLoad3();">
		<%@include file='../epedigree/topMenu.jsp'%>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr><td width="1%" valign="middle" class="td-rightmenu"><img 
src="<%=servPath%>/assets/images/space.gif" width="10" height="10"></td>
					<!-- Messaging -->
					<td width="99%" valign="middle" class="td-rightmenu"><table width="100%" border="0" 
cellspacing="0" cellpadding="0">
							<tr>
								<td align="left">
								</td>
								<td align="right"><img 
src="<%=servPath%>/assets/images/space.gif" width="5"></td>
							</tr>
						</table>
					</td>
				</tr>
					<tr>
					<td>&nbsp;</td>
					<td><table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
							<tr>
								<td>
																
	<!-- info goes here -->		
																
	<table width="100%" id="Table1" cellSpacing="1" cellPadding="1" align="center" border="0"
										bgcolor="white">
										<tr bgcolor="white">
											<td class="td-typeblack" colspan="3">	
												<table width="100%" 
id="Table1" cellSpacing="1" cellPadding="1" align="left" border="0"
													bgcolor="white">
													<tr bgcolor="white">
														<td 
class="td-typeblack" colspan="3">
															Real 
Time tracking</td>
													</tr>
													<tr 
bgcolor="#c8c8c8">
														<td 
align="left">
															
<TABLE id="Table3" cellSpacing="1" cellPadding="1" border="0" width="100%">

																
<FORM action="results.do" method="post">

																
<INPUT type="hidden" name="sessionID" value="<%=sessionID%>">
																
<INPUT type="hidden" name="pagenm" value="<%=pagenm%>">
																
<INPUT type="hidden" name="tp_company_nm" value="<%=tp_company_nm%>">
												
																
<TR bgcolor="#8494ca">
																
	<TD class="type-whrite"><strong>Search Value:</strong></TD>
																
				<TD align="left"><INPUT id="Text2" type="text" name="searchKey" value="" 
onchange="enableMap()" onkeypress=" setTimeout('enableMap()',100); "></TD>
																
				<TD class="type-whrite"><STRONG>Value Type:</STRONG></TD>
																
				<TD valign="middle" colspan=2>
																
				<SELECT id="Select1" name="criteria" onChange="enableMap()">
																
					<OPTION value="" selected>Select...</OPTION>
																
					<OPTION value="Pedigree">Pedigree Ref Num</OPTION>
																
					<OPTION value="SSCC">SSCC</OPTION>
																
					<OPTION value="EPC">EPC</OPTION>
																
					<OPTION value="SGTIN">SGTIN</OPTION>
																
					<OPTION value="OrderID">Order ID</OPTION>
																
					<option value="InvoiceNum">Invoice Num</option>
																
					<option value="DespatchAdv">Despatch Advice Num</option>
																
				</SELECT>
																
				</TD>
																
	
																
</TR>
																
<TR>
																
	<TD align="center" colSpan="5"><INPUT type="submit" class="fButton" value="LOCATE"></TD>
																
</TR>
																
</FORM>
																
																
																
<TR bgcolor="#dcdcdc">
																
	<TD align="center" colspan="2"><INPUT id="Reset1" type="button" class="fButton" value=" Cancel " name="Reset1"></TD>
																
	<TD align="center" colspan="3"><INPUT id="Submit1" type="submit" class="fButton" value="Done" name="Submit1"></TD>
																
															
																
	<TD align="center" colSpan="5"><span id='dynMap'></span></TD>
																
</TR>
															
</TABLE>
														</td>
													</tr>
												</table>
												</FORM>
											</td>
										</tr>
									</table>
									<DIV>

									<div id="map" style="width: 850px; height: 525px"></div>
<script type="text/javascript">
//<![CDATA[

function createMarker(point, html) {
  var marker = new GMarker(point);

  // Show this marker's index in the info window when it is clicked

  GEvent.addListener(marker, "click", function() {
    marker.openInfoWindowHtml(html);
  });

  return marker;
}

var points = [];
var map;

function onLoad(){

// Center the map 
map = new GMap(document.getElementById("map"));
map.addControl(new GSmallMapControl());
map.addControl(new GMapTypeControl());

<%
String zoom = request.getParameter("zoom"); 
%>

map.centerAndZoom(new GPoint(-121.99715, 37.34575), <%=zoom%>);

// Creates a marker whose info window displays the given number
}

</script> 

<logic:iterate name="TrackMapValue" id="mapid">

<table>
<tr>
<td> <bean:write name="mapid" property="name" /> </td>
<td> -- </td>
<td> <bean:write name="mapid" property="longitude" /> </td>
<td> -- </td>
<td> <bean:write name="mapid" property="latitude" /> </td>
<td> -- </td>
<td> <bean:write name="mapid" property="lastseentime" /> </td>

<tr>

</table>

<script>
	function onLoad2(){

	var point = new GPoint("<bean:write name="mapid" property="latitude" />","<bean:write name="mapid" 
property="longitude" />");
	
	var lst = "<bean:write name="mapid" property="lastseentime" />" ;
	var name = "<bean:write name="mapid" property="name" />" ;
	var address1 = "<bean:write name="mapid" property="address1" />" ;
	var address2 = "<bean:write name="mapid" property="address2" />" ;
	var city = "<bean:write name="mapid" property="city" />" ;
	var state = "<bean:write name="mapid" property="state" />" ;
	var zip = "<bean:write name="mapid" property="zip" />" ;
	var country = "<bean:write name="mapid" property="country" />" ;
	var phone = "<bean:write name="mapid" property="phone" />" ;
	var fax = "<bean:write name="mapid" property="fax" />" ;
	var ID = "<bean:write name="mapid" property="ID" />" ;
	
	
	var detail = "<B>" + name + "</B>";
	var detail = detail + "<B> Scan Time " + lst + "</B>";
	var detail = detail + "<P>" + "<U>Address</U>: " + address1 + ", " + address2 ;
	var detail = detail + "<BR>"+ city + ", " + state + ", " + zip +"</P>";

	
	if ("<%=zoom%>" != 14 )
	{
	<%
	String tagID=TagID.replace(' ','+');
	%>
	var zoomurl = 
"http://"+"<%=ipaddress%>"+":"+"<%=port%>"+"/ePharma/TrackMapsAction.do?zoom=14&pagenm=track&TagID=<%=tagID%>" ;
	var detail = detail + "<P>" + "<A href="+ zoomurl + "> zoom in +" + "</A>";
	}else
	{
	zoomurl = 
"http://"+"<%=ipaddress%>"+":"+"<%=port%>"+"/ePharma/TrackMapsAction.do?zoom=17&pagenm=track&TagID=<%=tagID%>" ;
	var detail = detail + "<P>" + "<A href="+ zoomurl + "> zoom out -" + "</A>";
	}	
	
   	var marker = createMarker(point,detail );
	map.addOverlay(marker);	
	
// Add a polyline with 4 random points. Sort the points by
// longitude so that the line does not intersect itself.

  points.push(new GPoint("<bean:write name="mapid" property="longitude" />","<bean:write name="mapid" property="latitude" 
/>"));



}

</script>

</tr>
</logic:iterate>    
      
<SCRIPT language="javascript">
function onLoad3(){
points.sort(function(p1, p2) { return p1.x - p2.x; });
map.addOverlay(new GPolyline(points));
}
</SCRIPT>   
									</DIV>
									<div id="footer" class="td-menu">
										<table width="100%" height="24" border="0" 
cellpadding="0" cellspacing="0">
											<tr>
												<td width="100%" height="24" 
bgcolor="#d0d0ff" class="td-menu" align="left">&nbsp;&nbsp;Copyright 
													2005. Raining 
Data.</td>
											</tr>
										</table>
									</div>
									
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</div>
		
	</body>
</html>