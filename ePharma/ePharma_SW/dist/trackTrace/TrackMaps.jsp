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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>


<%
//String key = "ABQIAAAAy0XSTn-tWGlVjEJZZLBilxT_b40cc3Xxw-qM0eEe7lzWJ0FIgBTeA9CZy3sif6TcezM7LNBVusEFbA";
String key = "";
key = request.getParameter("googleKey");
%>

<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
	<script src="http://maps.google.com/maps?file=api&v=1&key=<%=key%>" type="text/javascript"></script>  
 </head>

<body> 



 <div id="map" style="width: 850px; height: 525px"></div>
    <script type="text/javascript">
//<![CDATA[

// Center the map 
var map = new GMap(document.getElementById("map"));
map.addControl(new GSmallMapControl());
map.addControl(new GMapTypeControl());

<%
String zoom = request.getParameter("zoom"); 
String TagID = request.getParameter("TagID"); 
//String zoom = "17"; 

String ipaddress = request.getServerName();
int port = request.getServerPort();

%>

map.centerAndZoom(new GPoint(-121.99715, 37.34575), <%=zoom%>);

// Creates a marker whose info window displays the given number
function createMarker(point, html) {
  var marker = new GMarker(point);

  // Show this marker's index in the info window when it is clicked

  GEvent.addListener(marker, "click", function() {
    marker.openInfoWindowHtml(html);
  });

  return marker;
}

var points = [];

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
	var point = new GPoint("<bean:write name="mapid" property="longitude" />","<bean:write name="mapid" property="latitude" />");
	
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
	var url = "http://"+"<%=ipaddress%>"+":"+"<%=port%>"+"/EPCRepository/Location.do?exec=fetchDetails&parent=&locationID="+ID ;
	
	var detail = "<B>" + name + "</B>";
	var detail = detail + "<B> Scan Time " + lst + "</B>";
	var detail = detail + "<P>" + "<U>Address</U>: " + address1 + ", " + address2 ;
	var detail = detail + "<BR>"+ city + ", " + state + ", " + zip +"</P>";
	var detail = detail + "<P>"+ "<A href="+ url + "> Location details" + "</A>" + "</P>";
	
	if ("<%=zoom%>" != 14 )
	{
	var zoomurl = "http://"+"<%=ipaddress%>"+":"+"<%=port%>"+"/EPCRepository/TrackMapsAction.do?zoom=14&TagID=<%=TagID%>" ;
	var detail = detail + "<P>" + "<A href="+ zoomurl + "> zoom in +" + "</A>";
	}else
	{
	zoomurl = "http://"+"<%=ipaddress%>"+":"+"<%=port%>"+"/EPCRepository/TrackMapsAction.do?zoom=17&TagID=<%=TagID%>" ;
	var detail = detail + "<P>" + "<A href="+ zoomurl + "> zoom out -" + "</A>";
	}	
	
   	var marker = createMarker(point,detail );
	map.addOverlay(marker);	
	

// Add a polyline with 4 random points. Sort the points by
// longitude so that the line does not intersect itself.


  points.push(new GPoint("<bean:write name="mapid" property="longitude" />","<bean:write name="mapid" property="latitude" />"));

</script>

</tr>
</logic:iterate>    
      
<script>
points.sort(function(p1, p2) { return p1.x - p2.x; });
map.addOverlay(new GPolyline(points));

//]]>
</script>   
    
</body>


</html>