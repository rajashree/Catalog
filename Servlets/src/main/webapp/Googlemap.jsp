<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn:schemas-microsoft-com:vml">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <title>Google Maps JavaScript API Example: 	Extraction of Geocoding Data</title>
    <script src="http://maps.google.com/maps?file=api&amp;v=2.x&amp;key=ABQIAAAAzr2EBOXUKnm_jVnk0OJI7xSosDVG8KKPE1-m51RBrvYughuyMxQ-i1QfUnH94QxWIa6N4U6MouMmBA" 
            type="text/javascript"></script>
    
     <script type="text/javascript">

		var map;
		var geocoder;
		
		function initialize(zipcode) {
		  
		  map = new GMap2(document.getElementById("map_canvas"));
		  map.setCenter(new GLatLng(34, 0), 1);
		  geocoder = new GClientGeocoder();
		  geocoder.getLocations(zipcode, addAddressToMap);
		  
		}

		function addAddressToMap(response) {
		  map.clearOverlays();
		  if (!response || response.Status.code != 200) {
			alert("Sorry, we were unable to geocode that address");
		  } else {
			place = response.Placemark[0];
			point = new GLatLng(place.Point.coordinates[1],
								place.Point.coordinates[0]);
			marker = new GMarker(point);
			map.addOverlay(marker);
			marker.openInfoWindowHtml(place.address + '<br>' +
			  '<b>Country code:</b> ' + place.AddressDetails.Country.CountryNameCode);
		  }
		}
	</script>
  </head>

  <body>
     	<% String zipcode = request.getParameter("zipcode"); %>
      	<a href="javascript:initialize('<%=zipcode%>')" onclick="javascript:initialize('<%=zipcode%>')">Hi</a>
    	<div id="map_canvas" style="width: 500px; height: 300px" ></div>
 
  </body>
</html>
