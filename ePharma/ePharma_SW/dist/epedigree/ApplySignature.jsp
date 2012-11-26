<%@include file='../../includes/jspinclude.jsp'%>

<%
String path = request.getContextPath();
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String APNID = request.getParameter("pedid");
//String sessionID = (String)session.getAttribute("sessionID");
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");
String sect = request.getParameter("part");

String redirURL = "";

if(sect.equals("APN")) {
	redirURL = "http://"+servIP+":8080/ePharma/dist/epedigree/ePedigree_Manager_Reconcile.jsp?pedid="+APNID+"&tp_company_nm="+tp_company_nm+"&pagenm=pedigree";
}

//String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String verifyResponse = "";
String signedAPN = "";

byte[] xmlResults;

com.rdta.tlapi.xql.Connection connection = ConnectTL();
com.rdta.tlapi.xql.Statement statement = getStatement(connection);

String xQuery = "";
String SigExists = "";
String verifyEntry = "";
String contentToSign = "";

if(sect.equals("APN")) {
	xQuery = "for $j in collection('tig:///ePharma/APN/') ";
	xQuery = xQuery + "where $j/APN/DocumentId = '"+APNID+"' ";
	xQuery = xQuery + "return fn:boolean($j/APN/Signature) ";

	xmlResults = ReadTL(statement, xQuery);
	if(xmlResults != null) {
		SigExists = new String(xmlResults);
	}

	if(SigExists.equals("false")) {

		xQuery = "for $j in collection('tig:///ePharma/APN/') ";
		xQuery = xQuery + "where $j/APN/DocumentId = '"+APNID+"' ";
		xQuery = xQuery + "return $j/APN ";

		xmlResults = ReadTL(statement, xQuery);
		if(xmlResults != null) {
			contentToSign = new String(xmlResults);
		}
		
		if(!contentToSign.equals("")) {
			xQuery = "tlsp:CreateSignature("+contentToSign+",'C:/security/keys/RDTA_keystore','jasmine23', 'RDTAClient')";

			xmlResults = ReadTL(statement, xQuery);
			if(xmlResults != null) {
				
				xQuery = "for $i in collection('tig:///ePharma/APN')";
				xQuery = xQuery + "where $i/APN/DocumentId = '"+APNID+"' ";
				xQuery = xQuery + "return tig:delete-document(document-uri( $i )), ";
				xQuery = xQuery + "tig:insert-document( 'tig:///ePharma/APN/', $1 )";
				executeStatementStream(statement, xQuery, new ByteArrayInputStream (xmlResults));
			}
		}
	}

	
}

CloseConnectionTL(connection);

%>

<%
String getCustURL = redirURL;
response.setContentType("text/html; charset=ISO-8859-1");
response.setHeader("Location", getCustURL);
response.setStatus(303);
%>