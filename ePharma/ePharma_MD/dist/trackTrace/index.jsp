<%@include file='../../includes/jspinclude.jsp'%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
//String sessionID = request.getParameter("sessionID");
String sessionID = (String)session.getAttribute("sessionID");
String pagenm = request.getParameter("pagenm");

String xmlResultString = "";
String redirURL = "LoginFailed.html";
String sessQuery = "";
String tp_company_nm = "";
String tp_company_id = "";

com.rdta.tlapi.xql.Connection connection = ConnectTL();
com.rdta.tlapi.xql.Statement statement = getStatement(connection);

//CHECK SESSION IF IT IS A TRADE PARTNER
//IF IS - <div class="roleIcon-distributor"></div> CHANGE TO <div class="roleIcon-extranet"></div>
sessQuery = "for $b in collection('tig:///EAGRFID/SysSessions') ";
sessQuery = sessQuery + "where  $b/session/sessionid = '"+sessionID+"' ";
sessQuery = sessQuery + "return data($b/session/tp_company_nm)";
byte[] xmlResults = ReadTL(statement, sessQuery);
if(xmlResults != null) {
	tp_company_nm = new String(xmlResults);	
	sessQuery = "for $b in collection('tig:///EAGRFID/SysSessions') ";
	sessQuery = sessQuery + "where  $b/session/sessionid = '"+sessionID+"' ";
	sessQuery = sessQuery + "return data($b/session/tp_company_id)";
	xmlResults = ReadTL(statement, sessQuery);
	if(xmlResults != null) {
		tp_company_id = new String(xmlResults);	
	}
}

CloseConnectionTL(connection);

%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>Raining Data ePharma - Track and Trace</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">
		<%
		String ipaddress = request.getServerName();
		int port = request.getServerPort();
		%>
	<script language="JavaScript" type="text/JavaScript">
<!--
var newMapButton = null;
function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}

function submitMapForm(){
	var frm = document.forms[0];	
	var serVal =frm.searchKey.value;		
	frm.action="<html:rewrite  action='/TrackMapsAction.do?pagenm=track&TagID='/>"+serVal+"&zoom="+17;	
	frm.submit();	
		
}

function enableMap(){
	
	frm = document.forms[0];
	var serVal =frm.searchKey.value;
	var dynParentP = document.getElementById("dynMap");
	
	var bDisplay = false;
	
	if((serVal !="") && (serVal !="null") && (frm.criteria.value =='EPC' ) ){
		bDisplay = true;
	}

	if( bDisplay ){
		if( newMapButton == null ){
			var newElt = document.createElement("INPUT" );
			newElt.type="button";
			newElt.name="mapbutton";
			newElt.className="fButton";		
			newElt.id="mapbutton";
			newElt.value="MAP";
			newElt.onclick= function(){ submitMapForm() };	
			dynParentP.appendChild(newElt);		
			newMapButton = newElt;
		}
		else{
			newMapButton.style.visibility = 'visible';
		}

	}
	else{
		if( newMapButton != null ){
			newMapButton.style.visibility = 'hidden';
		}
	}
	
}	

//-->
			</script>
	</head>
	<body>
		<%@include file='../epedigree/topMenu.jsp'%>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr><td width="1%" valign="middle" class="td-rightmenu"><img src="../../assets/images/space.gif" width="10" height="10"></td>
					<!-- Messaging -->
					<td width="99%" valign="middle" class="td-rightmenu"><table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td align="left">
								</td>
								<td align="right"><img src="../../assets/images/space.gif" width="5"></td>
							</tr>
						</table>
					</td>
				</tr>
					<tr>
					<td>&nbsp;</td>
					<td><table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
							<tr>
								<td>
																	<!-- info goes here -->		
																	<table width="100%" id="Table1" cellSpacing="1" cellPadding="1" align="center" border="0"
										bgcolor="white">
										<tr bgcolor="white">
											<td class="td-typeblack" colspan="3">	
												<table width="100%" id="Table1" cellSpacing="1" cellPadding="1" align="left" border="0"
													bgcolor="white">
													<tr bgcolor="white">
														<td class="td-typeblack" colspan="3">
															Real Time tracking</td>
													</tr>
													<tr bgcolor="#c8c8c8">
														<td align="left">
															<TABLE id="Table3" cellSpacing="1" cellPadding="1" border="0" width="100%">
																<FORM action="results.jsp" method="post">
																<INPUT type="hidden" name="sessionID" value="<%=sessionID%>">
																<INPUT type="hidden" name="pagenm" value="<%=pagenm%>">
																<INPUT type="hidden" name="tp_company_nm" value="<%=tp_company_nm%>">
												
																<TR bgcolor="#8494ca">
																	<TD class="type-whrite"><strong>Search Value:</strong></TD>
																				<TD align="left"><INPUT id="Text2" type="text" name="searchKey" value="" onchange="enableMap()" onkeypress=" setTimeout('enableMap()',100); "></TD>
																				<TD class="type-whrite"><STRONG>Value Type:</STRONG></TD>
																				<TD valign="middle" colspan=2>
																				<SELECT id="Select1" name="criteria" onChange="enableMap()">
																					<OPTION value="" selected>Select...</OPTION>
																					<OPTION value="Pedigree">Pedigree Ref Num</OPTION>
																					<OPTION value="SSCC">SSCC</OPTION>
																					<OPTION value="EPC">EPC</OPTION>
																					<OPTION value="SGTIN">SGTIN</OPTION>
																					<OPTION value="OrderID">Order ID</OPTION>
																					<option value="InvoiceNum">Invoice Num</option>
																					<option value="DespatchAdv">Despatch Advice Num</option>
																				</SELECT>
																				</TD>
																	
																</TR>
																<TR>
																	<TD align="center" colSpan="5"><INPUT type="submit" class="fButton" value="LOCATE"></TD>
																</TR>
																</FORM>
																
																
																<TR bgcolor="#dcdcdc">
																	<TD align="center" colspan="2"><INPUT id="Reset1" type="button" class="fButton" value=" Cancel " name="Reset1"></TD>
																	<TD align="center" colspan="3"><INPUT id="Submit1" type="submit" class="fButton" value="Done" name="Submit1"></TD>
																
															
																	<TD align="center" colSpan="5"><span id='dynMap'></span></TD>
																</TR>
															</TABLE>
														</td>
													</tr>
												</table>
												</FORM>
											</td>
										</tr>
									</table>
									<DIV></DIV>
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
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>
