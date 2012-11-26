<%@include file='../../includes/jspinclude.jsp'%>

<%

String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String sessionID = request.getParameter("sessionID");
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");

String RECALLDETAILS = "";

byte[] xmlResults;

com.rdta.tlapi.xql.Connection connection = ConnectTL();
com.rdta.tlapi.xql.Statement statement = getStatement(connection);

String xQuery = "";

xQuery = "for $b in collection('tig:///ePharma/ProductRecall')/rss/channel/item  ";
xQuery = xQuery + "order by $b/pubDate ";
xQuery = xQuery + "return  ";
xQuery = xQuery + "<tr class='tableRow_On'>    ";
xQuery = xQuery + "<td>{data($b/pubDate)}</td> ";
xQuery = xQuery + "<td>{data($b/title)}</td> ";
xQuery = xQuery + "<td>{data($b/description)}</td>     ";
xQuery = xQuery + "<td><A HREF='{data($b/link)}' target='_new'>Recall Detail</A></td>   ";
xQuery = xQuery + "</tr> ";

xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	RECALLDETAILS = new String(xmlResults);
}

CloseConnectionTL(connection);

%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>Raining Data ePharma - Product Recall</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">
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
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="1%" valign="middle" class="td-rightmenu"><img src="../../assets/images/space.gif" width="10" height="10"></td>
					<!-- Messaging -->
					<td width="99%" valign="middle" class="td-rightmenu">
					
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td align="left">
								</td>
								<td align="right">
									<!-- <img src="../../assets/images/3320.jpg" width="200" height="27"> -->
									<img src="../../assets/images/space.gif" width="5"></td>
							</tr>
						</table>
					</td>
				</tr>
				<TR>
					<TD colspan="2" align=center> 
						<table width="100%" border="1" cellspacing="0" cellpadding="0">
						<tr class="tableRow_Header">
							<td class="type-whrite" align=center>Pub Date</td>
							<td class="type-whrite" align=center>Title</td>	
							<td class="type-whrite" align=center>Description</td>	
							<td class="type-whrite" align=center>Link</td>
						</tr>
						<%=RECALLDETAILS%>
						</table>
					</TD>
				</TR>
				
				
			</table>
		</div>
	</body>
</html>
