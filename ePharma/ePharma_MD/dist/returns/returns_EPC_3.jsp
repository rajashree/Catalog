
<%
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String sessionID = request.getParameter("sessionID");
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");


%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Raining Data ePharma - APN - Reports - Date</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}

function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}
function MM_openBrWindow(theURL,winName,features) { //v2.0
  window.open(theURL,winName,features);
}
//-->
</script>
</head>

<body leftmargin="0" topmargin="0" rightmargin="0" marginwidth="0" marginheight="0">

<%@include file='../epedigree/topMenu.jsp'%>

  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr> 
      <td width="1%" valign="middle" class="td-rightmenu"><img src="../../assets/images/space.gif" width="10" height="10"></td>
      <!-- Messaging -->
      <td width="99%" valign="middle" class="td-rightmenu"><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td align="left"> <!-- <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr> 
                  <td class="type-red">[View] </td>
                  <td><a href="#" class="typered-link">Create</a> </td>
                  <td><a href="#" class="typered-link">Delete</a> </td>
                  <td><a href="#" class="typered-link">Duplicate </a></td>
                  <td><a href="#" class="typered-link">Search </a></td>
                  <td><a href="#" class="typered-link">Audit </a></td>
                  <td><a href="#" class="typered-link">Trail</a></td>
                </tr>
              </table> --></td>
            <td align="right"><!-- <img src="../../../assets/images/3320.jpg" width="200" height="27"> --><img src="../../assets/images/space.gif" width="5"></td>
          </tr>
        </table></td>
    </tr>
    <tr> 
      <td>&nbsp;</td>
      <td><table border="0" cellspacing="0" cellpadding="0" width="100%">
          <tr> 
            <td><img src="../../assets/images/space.gif" width="30" height="12"></td>
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
		<table width="100%" id="Table1" cellSpacing="1" cellPadding="1" align="left" border="0" bgcolor="white">
			<tr bgcolor="white">
				<td class="td-typeblack" colspan="3">EPC/Barcode</td>
			</tr>
			<tr>
				<td align="left">
					<!-- Dashboard Start -->
				<table border="0" cellpadding="2" cellspacing="1" width="100%">
				 <tr class="tableRow_Header">
						<td colspan=2 class="type-whrite">&nbsp;</td>
					</tr>
					<tr>
						<td colspan="2"><TABLE border=0 cellPadding=2 cellSpacing=1 width="100%">
				<tr>

					<TD width=20% nowrap>Scan EPC/Barcode</TD>
					<TD class=tableData valign=baseline><INPUT TYPE=TEXT VALUE='1234567890' NAME='eDate' SIZE=15></td>
				</tr>
				<tr>
					<TD class=tableLabel valign=baseline nowrap colspan=2 align=left><img src="../../assets/images/magicbarcode2.bmp"></td>
					
				</tr> 
				
				<Tr>
					<Td colspan="2"><input name="Submit3" type="submit" class="fButton_xlarge" id="Submit2" onClick="MM_goToURL('parent','returns_EPC_3.jsp?sessionID=<%=sessionID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=returns');return document.MM_returnValue" value="Sena RMD Msg & ePedigree to CH"></Td>
				</Tr>
				</table></td>
					</tr>	
				  
</table>
</td></tr>
</table>

		</table>
		</td>
    </tr>
  </table>
</div>

<div id="footer" class="td-menu" > 
  <table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
    <tr> 
      <td width="100%" height="24" bgcolor="#D0D0FF" class="td-menu" align="left">&nbsp;&nbsp;Copyright 2005. 
        Raining Data.</td>
    </tr>
  </table>
</div>
</body>
</html>
