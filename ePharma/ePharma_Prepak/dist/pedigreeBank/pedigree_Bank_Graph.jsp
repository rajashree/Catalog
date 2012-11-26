<%@ include file='../../includes/jspinclude.jsp'%>
 
<%

String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String sessionID = (String)session.getAttribute("sessionID");
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");
String barChartname=(String)request.getAttribute("barChartname");	
String barChartnameForLot=(String)request.getAttribute("barChartnameForLot");	
String pieChartname=(String)request.getAttribute("pieChartname");	
 String bargraphURL = request.getContextPath() + "/DisplayChart?filename=" + barChartname;
 String bargraphURLForLot = request.getContextPath() + "/DisplayChart?filename=" + barChartnameForLot;
 String piegraphURL = request.getContextPath() + "/DisplayChart?filename=" + pieChartname;
 
%>
<html>
<head>
    <title>Raining Data ePharma - Pedigree Bank</title>
    <meta http-equiv="Content-Type" content="image/svg">
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
var graph="";
for(i=0;i<radios.length;i++){
  if( radios[i].checked ){
  graph=radios[i].value;
   
  }
}
if(graph=="graph")
{
 document.PedigreeForm.action = "pedigreeGraph.do?&tp_company_nm=<%=tp_company_nm%>&pagenm=<%=pagenm%>";
 document.PedigreeForm.target="_self";
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
                                                    <input type="radio" name="dispChoice" value="graph">View As Graph</td>
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
                                                       String result=(String)session.getAttribute("result");
                                                      out.println(result);
                                                       %>
                                                      
            
                                                    </table>

                                                </td>
                                            </tr>
						
      <table width="100%" cellSpacing="0" cellPadding="0" align="left" border="0"
			bgcolor="white">
                <tr bgcolor="white"> 
                <%
                if(request.getAttribute("NDC")!=null){
                %>
                  <td class="td-typeblack" align = center>Bar Chart for NDC:<%out.println(request.getAttribute("NDC"));%></td>
             
              </tr>
                <tr> 
                  <td align="center">
						
					<%
					 if(bargraphURL!=null && ! barChartname.endsWith("public_error_500x300.png")){
					 %>		
					  
					 <img src="<%= bargraphURL %>" width=500 height=300 border=0 usemap="#<%= barChartname %>">
					  
							   
					 
					  <img src="<%= piegraphURL %>" width=500 height=300 border=0 usemap="#<%= pieChartname %>">
					 <%
					 }
					 
					 else{
					 %>
					 
					 There is no data for NDC
					  <%}
					  
					  %>
					
             
             
             
             
              <%}
              else{
              
              %><td class="td-typeblack" align = center>Bar Chart for LotNo:<%out.println(request.getAttribute("LotNo"));%></td>
                
                </tr>
                <tr> 
                  <td align=center>
						
					<%
					 if(bargraphURLForLot!=null && ! bargraphURLForLot.endsWith("public_error_500x300.png")){
					 %>		
					 
					 
					 <img  src="<%= bargraphURLForLot %>" width=500 height=300 border=0 usemap="#<%= barChartnameForLot %>">
					 <%
					 }else{
					 %>
					 <td>There is no data for lot number</td>
					<% 
					}
					 }
					 %>
					 
					  
					
					 
					 
					 </table>
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
