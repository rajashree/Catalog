<%
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String sessionID = request.getParameter("sessionID");
%>

<html>
<head>
	<title>EAG-EPCIS Administration Console</title>
<style>
 
	a, A:link, a:visited, a:active, A:hover
		{color: #000000; text-decoration: none; font-family: Tahoma, Verdana; font-size: 12px}
</style>

<link href="assets/epedigree_edited.css" rel="stylesheet" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"><style type="text/css">
<!--
body {
	background-color: #eeeeee;
}
-->
</style></head>

<body bottommargin="0" topmargin="0" leftmargin="0" rightmargin="0" marginheight="0" marginwidth="0">
<script language="JavaScript" src="js/tree.js"></script>
<script language="JavaScript" src="js/tree_tpl.js"></script>

<table width="100%" height="100%" border="0" cellpadding="7" cellspacing="1" bgcolor="#CCCCCC">
  <tr>
    <td height="28" class="td-mapCatalogTreeTables"><div align="left">Admin Console</div></td></tr>
<!--
For Groups and Users

<tr>
	<td align="left" valign="top">
	<script language="JavaScript">
	
	var TREE_ITEMS = [
		['Resources', './dist/admin/Introduction.html',
				['Users', './dist/admin/Introduction.html',
					['Add', 'AddUser.do?exec=AddNewUser&module=ADMIN_USERS&action=Insert'],
					['Edit', 'AddUser.do?exec=EditUserList&module=ADMIN_USERS'],
				]
				
			
				,
				 ['Groups', './dist/admin/Introduction.html',
					['Add', 'AddGroup.do?exec=AddNewGroup&module=ADMIN_GROUPS&action=Insert'],
					['Edit', 'AddGroup.do?exec=EditGroupList&module=ADMIN_GROUPS'],
				],
				
		],
];

		new tree (TREE_ITEMS, TREE_TPL);

	</script>	</td>
</tr>-->
<tr>
	<td align="left" valign="top">
	<script language="JavaScript">
	
	var TREE_ITEMS = [
		['Resources', './dist/admin/Introduction.html',
				['Users', './dist/admin/Introduction.html',
					['Add', 'AddUser.do?exec=AddNewUser&module=ADMIN_USERS&action=Insert'],
					['Edit', 'AddUser.do?exec=EditUserList&module=ADMIN_USERS'],
				]
			
		],
];

		new tree (TREE_ITEMS, TREE_TPL);

	</script>	</td>
</tr>
  <tr>
    <td class="tableRow_Off"><div align="center"><IMG src="assets/images/logo_poweredby.gif"/> </div></td>
	
  </tr>
</table>
</body>
</html>

