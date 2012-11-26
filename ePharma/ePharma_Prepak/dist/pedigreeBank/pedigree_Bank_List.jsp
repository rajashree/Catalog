<%@ include file='../../includes/jspinclude.jsp'%>
 
<%

String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String sessionID = (String)session.getAttribute("sessionID");
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");

%>
<html>
<head>
    <title>Raining Data ePharma - Pedigree Bank</title>
    <link href="../assets/epedigree1.css" rel="stylesheet" type="text/css">
      <script language="JavaScript" type="text/JavaScript">
<!--
function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}

function submitform()
{

var radios=document.PedigreeForm.dispChoice;
var graph="list";
for(i=0;i<radios.length;i++){
  if( radios[i].checked ){
  graph=radios[i].value;
   
  }
}
if(graph=="graph")
{
 document.PedigreeForm.action = "pedigreeGraph.do?&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>";
 
if(document.PedigreeForm.ndc.value)
document.PedigreeForm.submit();
else 
if(document.PedigreeForm.lot.value)

document.PedigreeForm.submit();
else
alert("Please Enter NDC or Lot value");
}
if(graph=="list")
{
//alert(document.PedigreeForm.lot.value)
document.PedigreeForm.action = "pedigreeList.do?&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>";

if(document.PedigreeForm.ndc.value)
document.PedigreeForm.submit();
else 
if(document.PedigreeForm.lot.value)
document.PedigreeForm.submit();
else
alert("Please Enter NDC or Lot value");
}
}

//-->
</script>
</head>
    <body>
    
        <%@include file='../epedigree/topMenu.jsp'%>
    	 <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td width="99%" valign="middle" class="td-rightmenu" colspan=2>
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td align="left"></td>
                        <td align="right"><!-- <img src="../assets/images/3320.jpg" width="200" height="27"> -->
                            <img src="../assets/images/space.gif" width="5"></td>
                    </tr>
                </table>
            </td>
        </tr>
        
        <tr>
                                    
                                    <td colspan=4 align=center>
                                    <form name="PedigreeForm" method="post" target="_self">
                                    <input type='hidden' name='pagenm' value='<%=pagenm%>'>
                                    <input type='hidden' name='sessionID' value='<%=sessionID%>'>
                                    <input type='hidden' name='tp_company_nm' value='<%=tp_company_nm%>'>
                                   
                                        <table width="100%" border="0" cellspacing="0" cellpadding="1" align="center">
                                            <tr>
                                            <td>
                                            	<table width="100%" border="0" cellspacing="0" cellpadding="1" align="center">
                                            	<tr>
                                                <td class="td-typeblack" align="right" >***NDC:</td>
                                                <td class="td-typeblack" align="left" ><input type="text"  name="ndc"></td>
                                            	<td class="td-typeblack" align="right" >LOT Number:</td>
												<td class="td-typeblack" align="left" ><input type="text"  name="lot"></td>
                                                
                                            	</tr>
                                            	</table>
                                            </td>
                                            </tr>
                                            
                                              <tr class="td-typeblack">
                                                <td colspan="2" align="center">
                                                    <input type="radio" name="dispChoice" value="list" checked>View As List
                                                    <!--<input type="radio" name="dispChoice" value="graph">View As Graph</td>-->
                                            </tr>
                                             <tr>
						<td align="center" ><input type=button class="fButton" value="FIND" onClick="return submitform();"/></td>
						</tr>
						       <tr>
                                                <td align="left">
                                                    <table id="Table12" cellSpacing="1" cellPadding="0" width="100%" align="center" >
                                                        <tr>
                                                        <TD align="left" vAlign="top" width="255" bgColor="#a29fcb" colSpan="4" height="18"><STRONG><FONT color="#ffffff">Pedigree Bank</FONT></STRONG></TD>
                                                        </tr>
                                                        <tr >
                                                            <TD vAlign="top" bgColor="#dfdeed" height="18" align="center" colspan="1" width="250"><STRONG><U>NDC</U></STRONG></TD>
                                                             <TD vAlign="top" bgColor="#dfdeed" height="18" align="center" colspan="1" width="250"><STRONG><U>Regular Inventory</U></STRONG></TD>
                                                              <TD vAlign="top" bgColor="#dfdeed" height="18" align="center" colspan="1" width="250"><STRONG><U>Returned Inventory</U></STRONG></TD>
                                                               <TD vAlign="top" bgColor="#dfdeed" height="18" align="center" colspan="1" width="250"><STRONG><U>Total</U></STRONG></TD>
                                                                                                                 
                                                        </tr>
                                                       
                                                      <%
                                                       String result1=(String)session.getAttribute("result");
                                                      out.println(result1);
                                                       %>
                                                      
            
                                                    </table>

                                                </td>
                                            </tr>
						
        
     <tr>
     

      <table width="100%" cellSpacing="0" cellPadding="0" align="left" border="0"
			bgcolor="white">
                <tr bgcolor="white"> 
                  <td class="td-typeblack" align = center></td>
                </tr>
                <tr> 
                  <td align="left">
					<table cellSpacing="1" cellPadding="2" border="0" width="100%">		
					<tr>
                    <TD align="left" vAlign="top" width="255" bgColor="#a29fcb" colSpan="2" height="18"><STRONG><FONT color="#ffffff">List of Lots for NDC:<%out.println(request.getAttribute("NDC"));%></FONT></STRONG></TD>
                    </tr>
					<tr >
					<TD vAlign="top" bgColor="#dfdeed" height="18" align="center" colspan="0" width="250"><STRONG><u>Lot Number</u></STRONG></TD>
					<TD vAlign="top" bgColor="#dfdeed" height="18" align="center" colspan="0" width="250"><STRONG><u>Quantity</u></STRONG></TD>
					</tr>
     <%
String result=(String)request.getAttribute("result");
System.out.println(""+result);
out.println(result);
%>                               
 </table></table>
                <DIV>
                </DIV>
                
                
                
                <div id="footer" class="td-menu">
                    <table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
                        <tr>
                            <td width="100%" height="24" bgcolor="#d0d0ff" class="td-menu" align="left">&nbsp;&nbsp;Copyright 
                            2005. Raining Data.</td>
                        </tr>
                    </table>
                </div>
            </td>
        </tr>
    </table>
    </div>
    </body>
</html>
