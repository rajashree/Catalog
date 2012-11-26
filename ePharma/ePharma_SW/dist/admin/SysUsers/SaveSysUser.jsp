<%@include file='../../../includes/jspinclude.jsp'%>


<%
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String userName = request.getParameter("username");
String userPassword = request.getParameter("syspassword");
String firstName = request.getParameter("firstname");
String lastName = request.getParameter("lastname");
String access = request.getParameter("accesslevel");
String userid = request.getParameter("userid");
String sessionID = request.getParameter("sessionID");
String email = request.getParameter("email");
String tpid = "";
String tpnm = request.getParameter("tpnm");
String tprole = request.getParameter("tprole");
String disabled = request.getParameter("disabled");
String tp_company_nm = request.getParameter("tp_company_nm");

if(disabled == null) { disabled = "False"; } 


String hostname = "localhost";
String xmlResultString = "";
String xQuery = "";
byte[] xmlResults;

String redirectURL = "";

com.rdta.tlapi.xql.Connection connection = ConnectTL();
com.rdta.tlapi.xql.Statement statement = getStatement(connection);


String curUserID = "";

if(userid.equals("0")) {	//IF USER ID IS BLANK, NEW USER, CREATE ONE
	curUserID = getUniqueID(clientIP);
} else {
	curUserID = userid;
}

xQuery = "for $b in collection('tig:///CatalogManager/TradingPartner')/TradingPartner ";
xQuery = xQuery + " where $b/name = '"+tpnm+"' return data($b/genId)";

xmlResults = ReadTL(statement, xQuery);
if(xmlResults == null) {
	tpid = "0"; 
} else { tpid = new String(xmlResults); }

String xmlString = "<user>";
xmlString = xmlString + "<id>"+curUserID+"</id>";
xmlString = xmlString + "<firstname>"+firstName+"</firstname>";
xmlString = xmlString + "<lastname>"+lastName+"</lastname>";
xmlString = xmlString + "<username>"+userName+"</username>";
xmlString = xmlString + "<password>"+userPassword+"</password>";
xmlString = xmlString + "<accesslevel>"+access+"</accesslevel> ";
xmlString = xmlString + "<email>"+email+"</email>";
xmlString = xmlString + "<tp_company_id>"+tpid+"</tp_company_id> ";
xmlString = xmlString + "<tp_company_nm>"+tpnm+"</tp_company_nm> ";
xmlString = xmlString + "<tp_company_role>"+tprole+"</tp_company_role> ";
xmlString = xmlString + "<disabled>"+disabled+"</disabled> ";
xmlString = xmlString + "</user>";

//make sure there are no other users with same name and password
xQuery = "count(collection('tig:///EAGRFID/SysUsers')/user[username = '"+userName+"' and id != '"+curUserID+"'])";
xmlResults = ReadTL(statement, xQuery);
String foundUserCount = new String(xmlResults);

if(foundUserCount.equals("0")) {

	if(userid.equals("")) {
		//INSERT NEW USER
		xQuery = "tig:insert-document( 'tig:///EAGRFID/SysUsers/', "+xmlString+" )";
	} else {
		//UPDATE EXISTING
		xQuery = "for $i in collection('tig:///EAGRFID/SysUsers')";
		xQuery = xQuery + "where $i/user/id = '"+curUserID+"'";
		xQuery = xQuery + "return tig:delete-document(document-uri( $i )), ";
		xQuery = xQuery + "tig:insert-document( 'tig://root/EAGRFID/SysUsers/', "+xmlString+" )";
	}


	xmlResults = ReadTL(statement, xQuery);

	CloseConnectionTL(connection);

	String getCustURL = "ViewUsers.jsp?sessionID="+sessionID+"&tp_company_nm="+tp_company_nm;
	response.setContentType("text/html; charset=ISO-8859-1");
	response.setHeader("Location", getCustURL);
	response.setStatus(303);

} else { //User with same user name exists

	String getCustURL = "UserUpdateProblem.html";
	response.setContentType("text/html; charset=ISO-8859-1");
	response.setHeader("Location", getCustURL);
	response.setStatus(303);
}
	
%>