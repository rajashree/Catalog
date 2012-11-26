<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Raining Data ePharma - GCPIM</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">

</head>
<body>

<%


String tp_company_nm = (String)request.getParameter("tp_company_nm");
if(tp_company_nm==null)tp_company_nm="";
    String pagenm =  request.getParameter("pagenm");
    if(pagenm==null)pagenm="pedigree";
    String sessionID = (String)session.getAttribute("sessionID");
    String tpGenId = (String)session.getAttribute("tpGenId");
	 
%>
<%@include file='topMenu.jsp'%>
 
<table border="0" cellspacing="0" cellpadding="0" width="100%">
         
         
        <tr> 
          <td width="100%" valign="middle" class="td-rightmenu"><img src="../../assets/images/space.gif" width="10" height="10"></td>
          
         </tr> 		
            <!-- info goes here -->
              <table width="100%" cellSpacing="0" cellPadding="0" align="left" border="0"
			bgcolor="white">
              
                <tr> 
                  <td align="left">
					 
              


 
<br>
<br>
<br>
<br>
<font size="3"><center><b> Saved  Successfully   !!! </b></center></font>
<br>
 <center><input type="button" value="GoBack" onClick="window.history.back();"/>
<br>
<br>
<br>

      
</table>
</table>
<jsp:include page="../globalIncludes/Footer.jsp" />

</body>
</html>        