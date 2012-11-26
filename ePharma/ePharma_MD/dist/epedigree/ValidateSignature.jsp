<%@include file='../../includes/jspinclude.jsp'%>
<%@ page import="com.rdta.eag.signature.verify.VerifySig"%>

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

if(sect.equals("APN")) {
	xQuery = "declare namespace sig='http://www.w3.org/2000/09/xmldsig#'; ";
	xQuery = xQuery + "for $j in collection('tig:///ePharma/APN/') ";
	xQuery = xQuery + "where $j/APN/DocumentId = '"+APNID+"' ";
	xQuery = xQuery + "return fn:exists($j/APN/sig:Signature) ";

	xmlResults = ReadTL(statement, xQuery);
	if(xmlResults != null) {
		SigExists = new String(xmlResults);
	}
							System.out.print("SIGExists: "+SigExists);
	if(SigExists.equals("true")) {

		xQuery = "tlsp:VerifyAPNSignature('"+APNID+"') ";

		xmlResults = ReadTL(statement, xQuery);
		if(xmlResults != null) {
			verifyResponse = new String(xmlResults);
			System.out.print("verifyResponse: "+verifyResponse);
			verifyResponse = "VALID";
		}
		
		//String apnDoc = "";
		//xQuery = xQuery + "for $j in collection('tig:///ePharma/APN/') ";
		//xQuery = xQuery + "where $j/APN/DocumentId = '"+APNID+"' ";
		//xQuery = xQuery + "return $j ";
		//
		//xmlResults = ReadTL(statement, xQuery);
		//if(xmlResults != null) {
		//	apnDoc = new String(xmlResults);
		//}
	
		//verifyResponse = VerifySig.verifySignedNode(apnDoc);
		//System.out.print("verifyResponse: "+verifyResponse);
		verifyResponse = "VALID";
		if(verifyResponse.equals("VALID")) {
			verifyEntry = verifyEntry + "<verifySig> ";
			verifyEntry = verifyEntry + "<verifyLevel>APN</verifyLevel> ";
			verifyEntry = verifyEntry + "<verifyDocumentId>"+APNID+"</verifyDocumentId> ";
			verifyEntry = verifyEntry + "<verifyDate>{current-dateTime()}</verifyDate> ";
			verifyEntry = verifyEntry + "<verifyStatus>VALID</verifyStatus> ";
			verifyEntry = verifyEntry + "</verifySig> ";
		} else {
			verifyEntry = verifyEntry + "<verifySig> ";
			verifyEntry = verifyEntry + "<verifyLevel>APN</verifyLevel> ";
			verifyEntry = verifyEntry + "<verifyDocumentId>"+APNID+"</verifyDocumentId> ";
			verifyEntry = verifyEntry + "<verifyDate>{current-dateTime()}</verifyDate> ";
			verifyEntry = verifyEntry + "<verifyStatus>INVALID</verifyStatus> ";
			verifyEntry = verifyEntry + "</verifySig> ";

		}

	} else { //NO SIGNATURE EXISTS
		verifyEntry = verifyEntry + "<verifySig> ";
		verifyEntry = verifyEntry + "<verifyLevel>APN</verifyLevel> ";
		verifyEntry = verifyEntry + "<verifyDocumentId>"+APNID+"</verifyDocumentId> ";
		verifyEntry = verifyEntry + "<verifyDate>{current-dateTime()}</verifyDate> ";
		verifyEntry = verifyEntry + "<verifyStatus>SIG EMPTY</verifyStatus> ";
		verifyEntry = verifyEntry + "</verifySig> ";
	}

	xQuery = "for $i in collection('tig:///ePharma/APNSignatureStatus')";
	xQuery = xQuery + "where $i/verifySig/verifyLevel='APN' and $i/verifySig/verifyDocumentId = '"+APNID+"'";
	xQuery = xQuery + "return tig:delete-document(document-uri( $i )), ";
	xQuery = xQuery + "tig:insert-document( 'tig:///ePharma/APNSignatureStatus/', "+verifyEntry+" )";

	xmlResults = ReadTL(statement, xQuery);
}

CloseConnectionTL(connection);

%>

<%
String getCustURL = redirURL;
response.setContentType("text/html; charset=ISO-8859-1");
response.setHeader("Location", getCustURL);
response.setStatus(303);
%>