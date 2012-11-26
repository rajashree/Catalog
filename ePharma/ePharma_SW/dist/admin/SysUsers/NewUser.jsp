<%@include file='../../../includes/jspinclude.jsp'%>
<%@ page import='java.util.Properties' %>
<%@ page import='java.io.*' %>
<%@ page import='java.util.*' %>
<%@ page import='org.apache.jsp.*' %>
<%@ page import="java.net.*" %>
<%@ page import="org.xml.sax.InputSource" %>
<%@ page import="javax.xml.parsers.DocumentBuilderFactory,javax.xml.parsers.DocumentBuilder,org.w3c.dom.*" %>
<%@ page import='java.text.SimpleDateFormat' %>

<%
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String userID = request.getParameter("id");
String sessionID = request.getParameter("sessionID");
String tp_company_nm = request.getParameter("tp_company_nm");

String nodeName = "";
String firstName = "";
String lastName = "";
String accessLevel = "";
String acode  = "";
String national  = "";
String region  = "";
String facility  = "";
String department  = "";
String username  = "";
String password = "";
String tp_company_id  = "";
String tp_company_role  = "";
String disabled  = "";
String email = "";
String chlocations = "";

com.rdta.tlapi.xql.Connection connection = ConnectTL();
com.rdta.tlapi.xql.Statement statement = getStatement(connection);

byte[] xmlResults;
String xQuery = "";

xQuery = "for $b in collection('tig:///EAGRFID/LocationBuildGroup')/LocationGroupDef/LocationGroupDetail ";
xQuery = xQuery + " order by $b/GroupName return <OPTION value='{data($b/GroupName)}'>{data($b/GroupName)}</OPTION> ";

xmlResults = ReadTL(statement, xQuery);
chlocations = new String(xmlResults);
chlocations = replaceString(chlocations, "value=", "selected value=");

if((userID == null) || (userID.equals("0"))){
	userID = "0";
	accessLevel = "Select";
} else {
	//get user info for display
	String xmlResultString = "";
	String redirURL = "LoginFailed.html";
	String sessQuery = "";

	xQuery = "for $b in collection('tig:///EAGRFID/SysUsers') ";
	xQuery = xQuery + " return $b/user[id='"+userID+"'] ";

	xmlResults = ReadTL(statement, xQuery);
	java.io.InputStream isResult = new ByteArrayInputStream(xmlResults);

	//build dom
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	DocumentBuilder dbb = dbf.newDocumentBuilder();

	org.w3c.dom.Document doc = dbb.parse(isResult);
	NodeList foundusers = doc.getElementsByTagName("user");

	Node usernode;
	Node userchildnode;

	NodeList userchildnodes;
	int numOfResults = foundusers.getLength();

	if(numOfResults>0) {
		for(int i=0;i<numOfResults;i++)  {
			usernode = foundusers.item(i); //get user Node
			nodeName = usernode.getNodeName();

			if(nodeName.equals("user")) {

				userchildnodes=usernode.getChildNodes();
				for(int j=0;j<userchildnodes.getLength();j++)
				{
					userchildnode =  userchildnodes.item(j);
					if (userchildnode != null) {

						nodeName = userchildnode.getNodeName();

						if(nodeName.equals("id")) {
							if(userchildnode.getFirstChild()!=null) {
							if(userchildnode.getFirstChild().getNodeValue()!=null) {
							userID  = userchildnode.getFirstChild().getNodeValue();
							}}
						}
						
						if(nodeName.equals("email")) {
							if(userchildnode.getFirstChild()!=null) {
							if(userchildnode.getFirstChild().getNodeValue()!=null) {
							email  = userchildnode.getFirstChild().getNodeValue();
							}}
						}
						
						if(nodeName.equals("firstname")) {
							if(userchildnode.getFirstChild()!=null) {
							if(userchildnode.getFirstChild().getNodeValue()!=null) {
							firstName  = userchildnode.getFirstChild().getNodeValue();
							}}
						}

						if(nodeName.equals("lastname")) {
							if(userchildnode.getFirstChild()!=null) {
							if(userchildnode.getFirstChild().getNodeValue()!=null) {
							lastName  = userchildnode.getFirstChild().getNodeValue();
							}}
						}

						if(nodeName.equals("accesslevel")) {
							if(userchildnode.getFirstChild()!=null) {
							if(userchildnode.getFirstChild().getNodeValue()!=null) {
							accessLevel  = userchildnode.getFirstChild().getNodeValue();
							}}
						}

						if(nodeName.equals("username")) {
							if(userchildnode.getFirstChild()!=null) {
							if(userchildnode.getFirstChild().getNodeValue()!=null) {
							username  = userchildnode.getFirstChild().getNodeValue();
							}}
						}

						if(nodeName.equals("password")) {
							if(userchildnode.getFirstChild()!=null) {
							if(userchildnode.getFirstChild().getNodeValue()!=null) {
							password  = userchildnode.getFirstChild().getNodeValue();
							}}
						}

						if(nodeName.equals("tp_company_id")) {
							if(userchildnode.getFirstChild()!=null) {
							if(userchildnode.getFirstChild().getNodeValue()!=null) {
							tp_company_id  = userchildnode.getFirstChild().getNodeValue();
							}}
						}

						if(nodeName.equals("tp_company_nm")) {
							if(userchildnode.getFirstChild()!=null) {
							if(userchildnode.getFirstChild().getNodeValue()!=null) {
							tp_company_nm  = userchildnode.getFirstChild().getNodeValue();
							}}
						}

						if(nodeName.equals("tp_company_role")) {
							if(userchildnode.getFirstChild()!=null) {
							if(userchildnode.getFirstChild().getNodeValue()!=null) {
							tp_company_role  = userchildnode.getFirstChild().getNodeValue();
							}}
						}

						if(nodeName.equals("disabled")) {
							if(userchildnode.getFirstChild()!=null) {
							if(userchildnode.getFirstChild().getNodeValue()!=null) {
							disabled  = userchildnode.getFirstChild().getNodeValue();
							}}
						}

					} // END IF USER CHILD NODE NOT NULL
				} //END FOR USER CHILD NODES
			}  //END IF USER NODE
		} //END FOR USER NODES
	} //END IF GOT RESULTS
}


String tpSelect = "";


CloseConnectionTL(connection);
%>
<HTML>
<style type="text/css" media="all"> @import "../includes/nav.css"; @import "../../../assets/epedigree1.css"; 
	</style>
<TITLE>R & D- eList</TITLE>
<body><BR><BR><BR>
	<form action="SaveSysUser.jsp" method="post">
	<input type=hidden value="<%=userID%>" name="userid">
	<input type=hidden value="<%=tp_company_nm%>" name="tp_company_nm">
	<input type=hidden name="sessionID" value="<%=sessionID%>">
		<table id="Table1" cellSpacing="1" cellPadding="1" align="center" border="1" >
			<tr class="tableRow_Header">
				<td colSpan="4">
					<table id="Table2" align="center" border="0">
						<tr>
							<td align="center"><STRONG>System Account Detail</STRONG></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr bgColor="white">
				<td align="center" colSpan="4"><STRONG><FONT color="#000099">User Definition</FONT></STRONG>
				</td>
			</tr>
			<tr class="tableRow_On">
				<td><STRONG>*First Name:</STRONG></td>
				<td><INPUT id="Text1" type="text" name="firstname" value="<%=firstName%>"></td>
				<TD><STRONG>*Sys. User Name:</STRONG></TD>
				<td><INPUT id="Text5" type="text" name="username" value="<%=username%>"></td>
			</tr>
			<tr class="tableRow_Off">
				<td><STRONG>*Last Name:</STRONG></td>
				<td><INPUT id="Text2" type="text" name="lastname" value="<%=lastName%>"></td>
				<TD><STRONG>*Sys. Password:</STRONG></TD>
				<td><INPUT id="Password1" type="password" name="syspassword" value="<%=password%>"></td>
			</tr>
			<tr class="tableRow_On">
				<td><STRONG>Email:</STRONG></td>
				<td><INPUT type="text" name="email" value="<%=email%>"></td></td>
				<TD><STRONG>*Access Level:</STRONG></TD>
				<td><SELECT name="accesslevel">				
						<OPTION value="<%=accessLevel%>" selected><%=accessLevel%></OPTION>
						<OPTION value="Administrator">Administrator</OPTION>
						<OPTION value="Manager">Manager</OPTION>
						<OPTION value="Clinical Trials">Clinical Trials</OPTION>
						<OPTION value="Recall Manager">Recall Manager</OPTION>
						<OPTION value="Trade Partner Manager">Trade Partner Manager</OPTION>
						<OPTION value="Shipping">Shipping</OPTION>
						<OPTION value="Receiving">Receiving</OPTION>
						<OPTION value="Repackager">Repackager</OPTION>
						<OPTION value="Trading Partner">Trading Partner</OPTION>
						<OPTION value="User">User</OPTION>
					</SELECT></td>
			</tr>
			<tr class="tableRow_On">
				<TD><STRONG>Trade Partner Name:</STRONG></TD>
				<td>	<SELECT name="tpnm">
					<OPTION value="<%=tp_company_nm%>" selected><%=tp_company_nm%></OPTION>
					<%=tpSelect%>
					</SELECT>
				</td>
				<td><STRONG>Role:</STRONG></td>
				<td><SELECT name="tprole">	
					<OPTION value="<%=tp_company_role%>" selected><%=tp_company_role%></OPTION>
					<OPTION value="Distributor">Distributor</OPTION>
					<OPTION value="Hospital">Hospital</OPTION>
					<OPTION value="Manufacturer">Manufacturer</OPTION>
					<OPTION value="MailPharmacy">MailPharmacy</OPTION>
					<OPTION value="Pharmacy">Pharmacy</OPTION>
					<OPTION value="Repackager">Repackager</OPTION>
				
				</SELECT>
				</td>
				
			</tr>
			<tr class="tableRow_On">
				
				<TD><STRONG>Account Disabled:</STRONG></TD>
				<td align=left colspan=3>	<% if (disabled.equals("True")) { %>
					<INPUT type="checkbox" name="disabled" value="<%=disabled%>" checked>
					<% } else { %>
					<INPUT type="checkbox" name="disabled" value="<%=disabled%>">
					<% } %>
				</td>
				
			</tr>
			<TR bgColor="white">
				<TD colSpan="4" align=center><STRONG><FONT color="#000099">Select CH Locations & Trading Partners This Account Can Access</FONT></STRONG></TD>
			</TR>
			<TR class="tableRow_On">
				<TD>CH Locations:</TD>
				<TD>
					<SELECT multiple size="3" name="chlocations">
						<OPTION value='None'>None</OPTION>
						<%=chlocations%>
					</SELECT>
				</TD>
				<TD>Trading Partners:</TD>
				<TD>
					<SELECT multiple size="3" name="tplist">
						<OPTION value='<%=tp_company_nm%>' selected><%=tp_company_nm%></OPTION>
						
					</SELECT>
				</TD>
			</TR>
			<TR class="tableRow_Off">
				<TD colSpan="4">&nbsp;</TD>
			</TR>
			<TR>
						<TD><STRONG>Signature:</STRONG></TD>
						<td align=left colspan=1><INPUT type="file" name="signatureFile"></td>	
						<td colspan="1" align="center"></td> 
						
			
						<td>
						<% if(userID.equals("0")) { %>
							<img src="" width="144" height="50">
						<% } else { %>
							<img src="signfolder/sign2.jpg" width="144" height="50">
						<% } %>
						
						</td>			
			</TR>
			<TR class="tableRow_On">
				<TD align="center" colSpan="2"><INPUT id="Reset1" type="button" value=" Back " name="Reset1" ONCLICK="history.go(-1)"></TD>
				<TD align="center" colSpan="2"><INPUT id="Submit1" type="submit" value="Submit" name="Submit1"></TD>
			</TR>
		</table>
		<P class="MsoNormal" style="mso-list: l0 level1 lfo1; tab-stops: list .5in">&nbsp;</P>
	</form>
	
	
</body>
</HTML>