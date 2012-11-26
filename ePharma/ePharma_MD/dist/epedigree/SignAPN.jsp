<%@include file='../../includes/jspinclude.jsp'%>
<%@ page import="com.rdta.eag.signature.CreateSig"%>
<%
String path = request.getContextPath();
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String APNID = request.getParameter("APNID");
String sessionID = request.getParameter("sessionID");
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");
String sect = request.getParameter("sect");

String redirURL = "";

if(sect.equals("fromto")) {
	redirURL = "http://"+servIP+":8080/ePharma/dist/epedigree/ePedigree_ShipManager_FromTo.jsp?pedid="+APNID+"&sessionID="+sessionID+"&tp_company_nm="+tp_company_nm+"&pagenm=pedigree";
	
}

if(sect.equals("summary")) {
	redirURL = "http://"+servIP+":8080/ePharma/dist/epedigree/ePedigree_ShipManager_Reconcile.jsp?pedid="+APNID+"&sessionID="+sessionID+"&tp_company_nm="+tp_company_nm+"&pagenm=pedigree";
	
}

if(sect.equals("products")) {
	redirURL = "http://"+servIP+":8080/ePharma/dist/epedigree/ePedigree_ShipManager_Products.jsp?pedid="+APNID+"&sessionID="+sessionID+"&tp_company_nm="+tp_company_nm+"&pagenm=pedigree";
	
}

if(sect.equals("manu")) {
	redirURL = "http://"+servIP+":8080/ePharma/dist/epedigree/ePedigree_ShipManager_Manufacturer.jsp?pedid="+APNID+"&sessionID="+sessionID+"&tp_company_nm="+tp_company_nm+"&pagenm=pedigree";
	
}

if(sect.equals("custody")) {
	redirURL = "http://"+servIP+":8080/ePharma/dist/epedigree/ePedigree_ShipManager_Custody.jsp?pedid="+APNID+"&sessionID="+sessionID+"&tp_company_nm="+tp_company_nm+"&pagenm=pedigree";
	
}


//String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String apnDoc = "";
String signedAPN = "";

byte[] xmlResults;

com.rdta.tlapi.xql.Connection connection = ConnectTL();
com.rdta.tlapi.xql.Statement statement = getStatement(connection);

String xQuery = "";

xQuery = "for $b in collection('tig:///ePharma/APN')/APN  ";
xQuery = xQuery + "where $b/DocumentId = '"+APNID+"' ";
xQuery = xQuery + "return $b ";

xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	apnDoc = new String(xmlResults);
}

signedAPN = CreateSig.signXMLDoc(apnDoc, "c:/security/keys/RDTA_keystore", "jasmine23", "RDTAClient");

ByteArrayInputStream bais = new ByteArrayInputStream(signedAPN.getBytes());

xQuery = "for $b in collection('tig:///ePharma/APN')  ";
xQuery = xQuery + "where $b/APN/DocumentId = '"+APNID+"' ";
xQuery = xQuery + " return tig:delete-document(document-uri($b)) ";

xmlResults = ReadTL(statement, xQuery);

xQuery = " tig:insert-document('tig:///ePharma/APN', $1) ";
executeStatementStream(statement, xQuery,  bais); //insert signed doc

CloseConnectionTL(connection);

%>

<%
String getCustURL = redirURL;
response.setContentType("text/html; charset=ISO-8859-1");
response.setHeader("Location", getCustURL);
response.setStatus(303);
%>