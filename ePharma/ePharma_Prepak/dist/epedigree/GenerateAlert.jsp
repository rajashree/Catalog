<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@include file='../../includes/jspinclude.jsp'%>

<html:html>
	<head>
		<title>Raining Data ePharma - ePedigree - APN Manager - Generate Alert</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">
			<style type="text/css"></style>
	</head>
	

<script language="JavaScript" type="text/JavaScript">
function submitform(operation)
{
	document.updateAndDelete.operationType.value = operation;

	//alert(document.updateAndDelete.operationType.value );
	document.updateAndDelete.submit();

	return t;
}

function submitform(){
	var i ;
	i = document.forms[0].title.value
	var j = document.forms[0].userName.value
	
	if(!j){
		alert("Please Select User Name");
		return false
		}
	else
	if(!i) {
		alert("Please Enter The Message Title");
		return false
	}	
	else
		document.forms[0].submit;
}
</script>

<%
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String APNID = request.getParameter("envelopeId");
String pedID = request.getParameter("PedigreeId");
System.out.println("shippedpedigree id in generate alert.jsp: "+pedID);
if(APNID ==null) APNID = pedID;
List result = (List)request.getAttribute("res");
String groupId = "";
if(result != null) groupId = result.get(0).toString();
String sessionID = (String)session.getAttribute("sessionID");
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");

System.out.println("sessionid in Generate Alert.jsp "+sessionID);


String TOPLINE = "";
String EXCEPTIONVALUES = "";
String SYSROLESVALUES="";
String DocType="";
String DocType1="";
byte[] xmlResults;
String CreatedBy="";
String userNames = "";

com.rdta.tlapi.xql.Connection connection = ConnectTL();
com.rdta.tlapi.xql.Statement statement = getStatement(connection);

String xQuery = "";

xQuery = "for $i in collection('tig:///EAGRFID/SystemVocabulary')/SysVocabulary ";
xQuery = xQuery + " where $i/Category/CategoryName = 'PedigreeExceptions' ";
xQuery = xQuery + " return for $k in data($i/Category/Terms/Term/TermName)  ";
xQuery = xQuery + " return <option>{$k}</option> ";

System.out.println("my query for exceptions  "+xQuery);
xmlResults=ReadTL(statement,xQuery);
if(xmlResults != null){
	EXCEPTIONVALUES=new String(xmlResults);
	System.out.println("my query for exceptions  "+EXCEPTIONVALUES);
}

xQuery = "for $i in collection('tig:///EAGRFID/SysGroups')/Group  ";
xQuery = xQuery + " return <option value = '{data($i/GroupName)}'>{data($i/GroupName)}</option> ";

System.out.println(xQuery+"is my query");
xmlResults=ReadTL(statement,xQuery);
if(xmlResults != null){
	SYSROLESVALUES=new String(xmlResults);
}

xQuery = "for $i in collection('tig:///EAGRFID/SysUsers')/User ";
xQuery = xQuery + "where $i/AccessLevel/Access = '"+groupId+"' ";
xQuery = xQuery + "return <option value = '{data($i/UserID)}'>{data($i/FirstName)}</option> ";
System.out.println("query for getting user names: "+xQuery);
xmlResults=ReadTL(statement,xQuery);
if(xmlResults!=null){
	userNames=new String(xmlResults);
	System.out.println("Query for Doc Type: "+userNames);
}

xQuery = "for $b in collection('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope  ";
xQuery = xQuery + "where $b/serialNumber = '"+APNID+"' ";
xQuery = xQuery + "return data($b/pedigree/shippedPedigree/transactionInfo/transactionIdentifier/identifierType) ";

System.out.println("Query for Doc Type: "+xQuery);
xmlResults=ReadTL(statement,xQuery);
if(xmlResults!=null){
	DocType=new String(xmlResults);
	System.out.println("Query for Doc Type: "+DocType);
}


xQuery = "for $b in collection('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope  ";
xQuery = xQuery + "where $b/pedigree/shippedPedigree/documentInfo/serialNumber = '"+APNID+"' ";
xQuery = xQuery + "return data($b/pedigree/shippedPedigree/transactionInfo/transactionIdentifier/identifierType) ";

System.out.println("Query for Doc Type: "+xQuery);
xmlResults=ReadTL(statement,xQuery);
if(xmlResults!=null){
	DocType1=new String(xmlResults);
}

String query =" for $x in collection('tig:///EAGRFID/SysSessions')/session ";
			   query = query + " where $x/sessionid = '"+sessionID+"' ";
			   query = query + " return data($x/username) ";
			   xmlResults = ReadTL(statement,query);
			   if(xmlResults!=null){
			   		CreatedBy = new String(xmlResults);
			   		System.out.println("UserName *******"+CreatedBy);
			   }			   		

CloseConnectionTL(connection);
%>

<body>
		<br>
		<br>
		<table border="0" cellspacing="0" cellpadding="0" width="80%" ID="Table1" align="center">
			<tr>
				<td></td>
				<td rowspan="2">&nbsp;</td>
			</tr>
			<tr>
			
				<td>
					<!-- info goes here -->
					<table width="100%" id="Table2" cellSpacing="1" cellPadding="1" align="left" border="0"
						bgcolor="white">
						<tr bgcolor="white">
							<td class="td-typeblack">Pedigree - Generate Alert</td>
						</tr>
						<tr>
						<html:form  action="GenerateAlert.do" method="post">
							<Td colspan="2">
														
								<TABLE id="Table10" cellSpacing="1" cellPadding="0" width="100%" border="0">
									<TR class="tableRow_Header">
										<TD class="type-whrite" height="13"></TD>
										<TD class="type-whrite" height="13"></TD>
									</TR>
									<TR class="tableRow_On">
										<TD class="td-menu"><STRONG>Pedigree ID :</STRONG></TD>
										<TD class="td-menu"><FONT color="#009900"><%=APNID%></FONT></TD>
									</TR>
									<TR class="tableRow_On">
										<TD class="td-menu"><STRONG>User Name :</STRONG></TD>
										<TD class="td-menu"><FONT color="#009900"><%=CreatedBy%></FONT></TD>
									</TR>
								
									<TR class="tableRow_On">
										<TD class="td-menu"><STRONG>Pedigree Exceptions:</STRONG></TD>
										<TD class="td-menu"><html:select  property="exceptions">
															<%= EXCEPTIONVALUES %>
															</html:select></TD>
									</TR>
									<TR class="tableRow_On">
										<TD class="td-menu"><STRONG>User Groups:</STRONG></TD>
										<TD class="td-menu"><html:select property="systemroles">
																<%=SYSROLESVALUES%>
															</html:select></TD>
									</TR>
									<TR class="tableRow_On">
										<TD class="td-menu"><STRONG>User Names:</STRONG></TD>
										<TD class="td-menu"><html:select property="userName">
																<option value="">Select One</option>
																<%=userNames%>
															</html:select></TD>
									</TR>
									<TR class="tableRow_On">
										<TD class="td-menu"><STRONG>Message Title:</STRONG></a></TD>
										<TD class="td-menu"><html:text property="title" size="20" /></TD>
									</TR>
									
									<TR class="tableRow_On">
										<TD class="td-menu"><STRONG>Required Action:</STRONG></TD>
										<TD class="td-menu"><html:textarea property="action" rows="2" cols="25"></html:textarea></TD>
									</TR>
									
									<TR class="tableRow_On">
										<TD class="td-menu"><STRONG>Comments:</STRONG></TD>
										<TD class="td-menu"><html:textarea  property="comments" rows="3" cols="40"></html:textarea></TD>
									</TR>
									
									<TR class="tableRow_On">
										<TD class="td-menu"><STRONG>Priority Level:</STRONG></TD>
										<TD class="td-menu"><html:select  property="severitylevel">
																<html:option value="1">1</html:option>
																<html:option value="2">2</html:option>
																<html:option value="3">3</html:option>
																<html:option value="4">4</html:option>
															</html:select></TD>
									</TR>
									<BR><BR>
									<TR class="tableRow-On">
										<html:hidden property="sessionID" value="<%=sessionID%>" />
										<html:hidden property="pagenm" value="<%=pagenm%>" />
										<html:hidden property="tp_company_nm" value="<%=tp_company_nm%>" />
										<html:hidden property="APNID" value="<%=APNID%>" />
										<% if(DocType == "") DocType = DocType1;%>
										<html:hidden property="DocType" value="<%=DocType%>" />
										<TD class="td-menu" ></TD>
										<TD class="td-menu" align="center"><html:submit property="ADD" value="SEND" onclick="return submitform()"/></TD>
									</TR>
									
									</TABLE>
									</html:form>
							</Td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		
	</body>

	</html:html>