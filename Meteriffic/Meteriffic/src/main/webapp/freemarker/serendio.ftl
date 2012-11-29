<body onload="alert('The page is loading... now!')">
<script>
	var xmlhttp
	
	function showHint()
	{
		alert("hji");
		xmlhttp=GetXmlHttpObject();
		if (xmlhttp==null)
		  {
		  alert ("Your browser does not support XMLHTTP!");
		  return;
		  }
		var url="http://voom.serendio.com/testvoomsrv//login?password=318775fb44e6f2e6388b719352e93ee8&user%5Fname=sourcen&login=1";
		xmlhttp.onreadystatechange=stateChanged;
		xmlhttp.open("GET",url,true);
		xmlhttp.send(null);
	}

	function stateChanged()
	{
		if (xmlhttp.readyState==4)
		  {
		  document.getElementById("txtHint").innerHTML=xmlhttp.responseText;
		  }
	}

	function GetXmlHttpObject()
	{
		if (window.XMLHttpRequest)
		  {
		  // code for IE7+, Firefox, Chrome, Opera, Safari
		  return new XMLHttpRequest();
		  }
		if (window.ActiveXObject)
		  {
		  // code for IE6, IE5
		  return new ActiveXObject("Microsoft.XMLHTTP");
		  }
		return null;
	}

</script>
<div id="txtHint">Hello</div>
<!--<div id="wrapper">
	<iframe width="1000px" height="600px" src="http://customers.serendio.com/sourcen/smartphones/#app=64e3&d147-selectedIndex=0"/>
</div>-->
</body>