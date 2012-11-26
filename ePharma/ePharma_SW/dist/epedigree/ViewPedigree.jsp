<%@include file='../../includes/jspinclude.jsp'%>

<%
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String PedDocNumber = request.getParameter("doc");
String HTMLROW = "";
String PedDocDT = "";
String FromAddr = "";
String ToAddr = "";
String SHIPROW = "";
String PRODS = "";
String MANUFACT = "";
byte[] xmlResults;

com.rdta.tlapi.xql.Connection connection = ConnectTL();
com.rdta.tlapi.xql.Statement statement = getStatement(connection);

String xQuery = "";	

xQuery = "for $b in collection('tig:///EAGRFID/APN')/APN/Pedigrees ";
xQuery = xQuery + "where $b/Pedigree/DocumentId = '"+PedDocNumber+"' ";
xQuery = xQuery + "return ";
xQuery = xQuery + "data($b/Pedigree/DateTime)";		

xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
PedDocDT = new String(xmlResults);
}

//GET Ship From Info
xQuery = "for $b in collection('tig:///EAGRFID/APN')/APN ";
xQuery = xQuery + "where $b/Pedigrees/Pedigree/DocumentId = '"+PedDocNumber+"' ";
xQuery = xQuery + "return ";
xQuery = xQuery + "<table border='0' cellpadding='0' cellspacing='0'><tr><td class='td-menu'>Company:</td> ";
xQuery = xQuery + "<td class='td-menu bold'>{data($b/From/Name)}</td></tr> ";
xQuery = xQuery + "<tr><td class='td-menu'>Address:</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($b/From/Address)}</td></tr> ";
xQuery = xQuery + "<tr><td class='td-menu'>Contact:</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($b/From/ContactName)}</td></tr> ";
xQuery = xQuery + "<tr><td class='td-menu'>Phone:</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($b/From/Phone)}</td></tr> ";
xQuery = xQuery + "<tr><td class='td-menu'>Fax:</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($b/From/Fax)}</td></tr> ";
xQuery = xQuery + "<tr><td class='td-menu'>Email:</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($b/From/Email)}</td></tr> ";
xQuery = xQuery + "<tr><td class='td-menu'>License:</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($b/From/LicenseNumber)}</td></tr></table> ";
						
xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
FromAddr = new String(xmlResults);
}

//GET Ship To Info
xQuery = "for $b in collection('tig:///EAGRFID/APN')/APN ";
xQuery = xQuery + "where $b/Pedigrees/Pedigree/DocumentId = '"+PedDocNumber+"' ";
xQuery = xQuery + "return ";
xQuery = xQuery + "<table border='0' cellpadding='0' cellspacing='0'><tr><td class='td-menu'>Company:</td> ";
xQuery = xQuery + "<td class='td-menu bold'>{data($b/To/Name)}</td></tr> ";
xQuery = xQuery + "<tr><td class='td-menu'>Address:</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($b/To/Address)}</td></tr> ";
xQuery = xQuery + "<tr><td class='td-menu'>Contact:</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($b/To/ContactName)}</td></tr> ";
xQuery = xQuery + "<tr><td class='td-menu'>Phone:</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($b/To/Phone)}</td></tr> ";
xQuery = xQuery + "<tr><td class='td-menu'>Fax:</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($b/To/Fax)}</td></tr> ";
xQuery = xQuery + "<tr><td class='td-menu'>Email:</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($b/To/Email)}</td></tr> ";
xQuery = xQuery + "<tr><td class='td-menu'>License:</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($b/To/LicenseNumber)}</td></tr></table> ";
						
xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
ToAddr = new String(xmlResults);
}

//GET LineItem Info
xQuery = "for $b in collection('tig:///EAGRFID/APN')/APN ";
xQuery = xQuery + "where $b/Pedigrees/Pedigree/DocumentId = '"+PedDocNumber+"'  ";
xQuery = xQuery + "return  ";
xQuery = xQuery + "let $prds := $b/Pedigrees/Pedigree/Products ";
xQuery = xQuery + "let $mftr := $b/Pedigrees/Pedigree/Manufacturer ";
xQuery = xQuery + "for $p in $prds/Product ";
xQuery = xQuery + "order by $p/BrandName ";
xQuery = xQuery + "return ";
xQuery = xQuery + "<tr class='tableRow_On'> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/BrandName)}</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($mftr[LicenseNumber=data($p/ManufacturerLicense)]/Name)}</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/LotNumber)}</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/LotExpireDate)}</td> ";
xQuery = xQuery + "</tr>";

xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
PRODS = new String(xmlResults);
}

xQuery = "for $p in collection('tig:///EAGRFID/APN')/APN/Pedigrees/Pedigree ";
xQuery = xQuery + "where $p/DocumentId = '"+PedDocNumber+"'  ";
xQuery = xQuery + "return ";
xQuery = xQuery + "<tr class='tableRow_On'> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/Manufacturer/Name)}</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/Manufacturer/Address)}</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/Manufacturer/Contact)}</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/Manufacturer/Phone)}</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/Manufacturer/ContactEmail)}</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/Manufacturer/LicenseNumber)}</td> ";
xQuery = xQuery + "</tr>";

xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
MANUFACT = new String(xmlResults);
}
	
CloseConnectionTL(connection);
%>


<html>
<head>
<title>Raining Data ePharma - ePedigree - APN Manager - Create APN</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">
<style type="text/css">

</style>
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}
//-->
</script>
<script language="JavaScript" type="text/JavaScript">
<!--


function MM_reloadPage(init) {  //reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
}
MM_reloadPage(true);

function MM_popupMsg(msg) { //v1.0
  alert(msg);
}
//-->
</script>
</head>

<body>

<!-- Top Header -->
<div id="bg">
	<div class="roleIcon-manufacturer">&nbsp;</div>
	<div class="navIcons">
		<a href="../../index.html"><img src="../../assets/images/home.gif" width="22" height="27" hspace="10" border="0"></a>
		<img src="../../assets/images/account.gif" width="41" height="27" hspace="10">
		<img src="../../assets/images/help.gif" width="21" height="27" hspace="10">
		<img src="../../assets/images/print.gif" width="27" height="27" hspace="10">
		<img src="../../assets/images/logout.gif" width="34" height="27" hspace="10">
		<img src="../../assets/images/space.gif" width="20">
	</div>
	
  <div class="logo"><img src="../../assets/images/logos_combined.jpg"></div>
</div>

<table border="0" cellspacing="0" cellpadding="0" width="100%">
          <tr> 
            <td><img src="../../assets/images/space.gif" width="30" height="20"></td>
            <td rowspan="2">&nbsp;</td>

          </tr>
          
          <tr>
		  <td> 
           <!-- info goes here --> 
		<table width="100%" id="Table1" cellSpacing="1" cellPadding="1" align="left" border="0" bgcolor="white">
			  <tr bgcolor="white"> 
                		<td class="td-typeblack" colspan="2">Pedigree Detail</td>

			  </tr>
				<tr class="tableRow_Header">
					<td class="type-whrite" width="50%">Pedigree ID : <%=PedDocNumber%></td>
					<td class="type-whrite">Issue Date : <%=PedDocDT%></td>
				</tr>
				<tr class="tableRow_On">
					<td class="td-menu bold">From</td>

					<td class="td-menu bold">To</td>
				</tr>
				<tr class="tableRow_Off">
				<td><%=FromAddr%></td>
				<td><%=ToAddr%></td>
			</tr>
			<tr>
				<td colspan="2"><br>
					<table border="0" cellpadding="0" cellspacing="1" width="100%">
					<tr>
					<td class="td-typeblack" colspan="5">Products</td>
					</tr>

					<tr class="tableRow_Header">
					<td class="type-whrite">Name</td>
					<td class="type-whrite">Manufacturer</td>
					<td class="type-whrite">Lot Number</td>
					<td class="type-whrite">Lot Expire Date</td>
					</tr>
					<%=PRODS%>
					</table>
				</td>
			</tr>
			<tr>
				<td colspan="2"><br>
					<table border="0" cellpadding="0" cellspacing="1" width="100%">
					<tr>
						<td class="td-typeblack" colspan="7">Manufacturer Information </td>
					</tr>					
					<tr class="tableRow_Header">
		<td class="type-whrite">Name</td>
		<td class="type-whrite">Address</td>
		<td class="type-whrite">Contact</td>
		<td class="type-whrite">Phone</td>
		<td class="type-whrite">Email</td>
		<td class="type-whrite">License</td>

	</tr>

	<%=MANUFACT%>

					</table>
				</td>
			</tr>
			<tr>
				<Td colspan="2"><br>
					<table border="0" cellpadding="0"
cellspacing="1" width="100%">
				
			
		</table> 
		</td>
    </tr>
  </table>

<div id="footer"> 
  <table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
    <tr> 
      <td width="100%" height="24" bgcolor="#D0D0FF" class="td-menu" align="left">&nbsp;&nbsp;Copyright 2005. 
        Raining Data.</td>
    </tr>
  </table>
</div>
</body>
</html>
