<%@include file='../../includes/jspinclude.jsp'%>
<%@page import="com.rdta.tlapi.xql.Connection"%>
<%@page import="com.rdta.tlapi.xql.Statement"%>
<%@page import="com.rdta.Admin.Utility.Helper"%>

<% 
	String tp_company_nm = request.getParameter("tp_company_nm");
	String status = "";
	status = (String)request.getAttribute("status"); 
	String buttonname = (String)request.getAttribute("buttonname");
	System.out.println(" Status in Reconcile "+status);
 %>

<SCRIPT LANGUAGE="javascript">

function pedigreeform()
{
//   window.showModalDialog('CertifyPedigreeReceiving.do?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>','','dialogWidth:300px;dialogHeight:200px;scroll=no;status=no')
 //window.open('CertifyPedigreeReceiving.do?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>' ,'apnwin','width=300,height=150')	
 if (window.showModalDialog) {	
 		window.showModalDialog('CertifyPedigreeReceiving.do?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>','','dialogWidth:300px;dialogHeight:200px;scroll=no;status=no');
	 }else {
		window.open('CertifyPedigreeReceiving.do?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>','name','left=380,top=250,height=180,width=300,toolbar=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=no,modal=yes');
}
}

function receiptform()
{
   //window.showModalDialog('CertifyReceiptReceiving.do?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>','','dialogWidth:300px;dialogHeight:200px;scroll=no;status=no')
  //window.open('CertifyReceiptReceiving.do?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>' ,'apnwin','width=300,height=150')
  if (window.showModalDialog) {	
 		window.showModalDialog('CertifyReceiptReceiving.do?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>','','dialogWidth:300px;dialogHeight:200px;scroll=no;status=no');
	 }else {
		window.open('CertifyReceiptReceiving.do?pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>','name','left=380,top=250,height=180,width=300,toolbar=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=no,modal=yes');
}
}

function newWindow(  htmFile  ) {
   msgWindow = window.open( htmFile, "WindowName", "left=20,top=20,width= 700,height=500,toolbar=0,scrollbar=1,resizable=1,location=0,status=0");
}

function accessdenied() {
  var i = <%=status%>
  
  if(i)
  {	
  		alert("Access Denied.....!")
  	
  } 
}

</SCRIPT> 

<%
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String APNID = request.getParameter("pedid");
//String sessionID = request.getParameter("sessionID");
String sessionID = (String)session.getAttribute("sessionID");
String pagenm = request.getParameter("pagenm");
//String tp_company_nm = request.getParameter("tp_company_nm");
String PedigreeOrder = "x";

Connection conn; 
Statement stmt;
Helper helper = new Helper();

String PEDSigStatus="";
String PEDSigValid="";
String PEDSigInValid="";
String PEDSigStatusDate="";
System.out.println("APNID ="+APNID);

String MENSigStatus="";
String MENSigValid="";
String MENSigInValid="";
String MENSigStatusDate="";

String CustSigStatus="";
String CustigValid="";
String CustigInValid="";
String CustSigStatusDate="";

String ProductSigStatus="";
String ProductValid="";
String ProductInValid="";
String ProductSigStatusDate="";

	//Validating Session
			HttpSession sess = request.getSession();
			clientIP = request.getRemoteAddr();		
			conn = helper.ConnectTL(); 
			stmt = helper.getStatement(conn);
			System.out.println("Validating The Session");
			
			//Validating Session
				
			String validateResult = helper.ValidateUserSession(stmt, sessionID, clientIP);        
		
			if ( !validateResult.equals("VALID")){
			    //return a forward to invalid .
			   // request.getRequestDispatcher("/dist/login-dist.html").forward(request, response); 
			}

if(request.getParameter("selpedid") != null) {
//System.out.println("hello"+selpedid);
	PedigreeOrder = request.getParameter("selpedid");
	session.setAttribute("PedigreeOrder",PedigreeOrder);
	
} 

String HTMLROW = "";
String PedDocDT = "";
String FromAddr = "";
String ToAddr = "";
String SHIPROW = "";
String PRODS = "";
String MANUFACT = "";
String TOPLINE = "";
String CUSTODY = "";
String totProducts = "";
String totManufacturers = "";
String totCustodians = "";
String PedigId ="";
byte[] xmlResults;

com.rdta.tlapi.xql.Connection connection = ConnectTL();
com.rdta.tlapi.xql.Statement statement = getStatement(connection);

String xQuery = "";
String APNSigStatus = "";
String APNSigStatusDate = "";

if(PedigreeOrder.equals("x")) {

	xQuery = "for $b in collection('tig:///ePharma/APN')/APN  ";
	xQuery = xQuery + "where $b/DocumentId = '"+APNID+"' ";
	xQuery = xQuery + "return  max($b/Pedigrees/Pedigree/@order) ";
	xmlResults = ReadTL(statement, xQuery);
	if(xmlResults != null) {
		PedigreeOrder = new String(xmlResults);
		
		PedigreeOrder = replaceString(PedigreeOrder, ".0E0", "");
		session.setAttribute("PedigreeOrder",PedigreeOrder);
	}
	
	
}
System.out.println("PedigreeOrder "+PedigreeOrder);
	
xQuery = "for $b in collection('tig:///ePharma/APN')/APN[DocumentId = '"+APNID+"']/Pedigrees/Pedigree[@order='"+PedigreeOrder+"'] ";
	xQuery = xQuery +" return data($b/DocumentId)";
	xmlResults = ReadTL(statement, xQuery);
	PedigId=new String(xmlResults) ;
System.out.println("pedigreeorder is :"+ PedigreeOrder);

xQuery = "for $i in collection('tig:///ePharma/APNSignatureStatus')";
xQuery = xQuery + "where $i/verifySig/verifyLevel='APN' and $i/verifySig/verifyDocumentId = '"+APNID+"' ";
xQuery = xQuery + "return data($i/verifySig/verifyStatus)";

xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	APNSigStatus = new String(xmlResults);
}else{

	APNSigStatus="";
}	

xQuery = "for $i in collection('tig:///ePharma/APNSignatureStatus')";
xQuery = xQuery + "where $i/verifySig/verifyLevel='APN' and $i/verifySig/verifyDocumentId = '"+APNID+"' ";
xQuery = xQuery + "return data($i/verifySig/verifyDate)";

xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	APNSigStatusDate = new String(xmlResults);
	
}else{
	APNSigStatusDate="";
}	

xQuery = "for $i in collection('tig:///ePharma/APNSignatureStatus')";
xQuery = xQuery + "where ( $i/verifySig/verifyLevel='PEDIGREE' and $i/verifySig/verifyDocumentId = '"+APNID+"' and  $i/verifySig/verifyPedigreeDoctId = '"+PedigId+"'";
xQuery = xQuery + ") return data($i/verifySig/verifyStatus)";
System.out.println("PEDSigStatus  is "+PEDSigStatus);	
xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	   PEDSigStatus = new String(xmlResults);
	   System.out.println("PEDSigStatus  is "+PEDSigStatus);					
}else{
	   PEDSigStatus="";
}	

xQuery = "for $i in collection('tig:///ePharma/APNSignatureStatus')";
xQuery = xQuery + "where ( $i/verifySig/verifyLevel='PEDIGREE' and $i/verifySig/verifyDocumentId = '"+APNID+"' and  $i/verifySig/verifyPedigreeDoctId = '"+PedigId+"' )";
xQuery = xQuery + "return data($i/verifySig/verifyDate)";
xmlResults = ReadTL(statement, xQuery);
  if(xmlResults != null) {
	     PEDSigStatusDate = new String(xmlResults);
							
}else{
	     PEDSigStatusDate="";
}	

xQuery = "for $i in collection('tig:///ePharma/APNSignatureStatus')";
xQuery = xQuery + "where ( $i/verifySig/verifyLevel='Manfacture' and $i/verifySig/verifyDocumentId = '"+APNID+"' and  $i/verifySig/verifyPedigreeDoctId = '"+PedigId+"'";
xQuery = xQuery + ") return data($i/verifySig/verifyStatus)";
System.out.println("PEDSigStatus  is "+PEDSigStatus);	
xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	   MENSigStatus = new String(xmlResults);
		System.out.println("PEDSigStatus  is "+PEDSigStatus);					
}else{
	   MENSigStatus="";
}	

xQuery = "for $i in collection('tig:///ePharma/APNSignatureStatus')";
xQuery = xQuery + "where ( $i/verifySig/verifyLevel='Manfacture' and $i/verifySig/verifyDocumentId = '"+APNID+"' and  $i/verifySig/verifyPedigreeDoctId = '"+PedigId+"' )";
xQuery = xQuery + "return data($i/verifySig/verifyDate)";
xmlResults = ReadTL(statement, xQuery);
  if(xmlResults != null) {
	    MENSigStatusDate = new String(xmlResults);
							
}else{
	     MENSigStatusDate="";
}	



ProductSigStatus="";
ProductValid="";
ProductInValid="";
String ProductCount ="";
xQuery = "count (for $b in collection('tig:///ePharma/APN')/APN[DocumentId = '"+APNID+"']/Pedigrees/Pedigree[@order='"+PedigreeOrder+"']/Products ";
xQuery = xQuery +" return $b/Product )";
xmlResults = ReadTL(statement,xQuery);
if(xmlResults != null )
{
		ProductCount = new String(xmlResults);

}
xQuery = "count ( for $i in collection('tig:///ePharma/APNSignatureStatus')";
xQuery = xQuery + "where ( $i/verifySig/verifyLevel='Product' and $i/verifySig/verifyDocumentId = '"+APNID+"' and  $i/verifySig/verifyPedigreeDoctId = '"+PedigId+"'";
xQuery = xQuery + " and $i/verifySig/verifyStatus = 'SIG EMPTY') return $i/verifySig/verifyStatus )";
System.out.println("PEDSigStatus  is "+PEDSigStatus);	
xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	  ProductSigStatus = new String(xmlResults);
		 if(ProductSigStatus.equals("0"))ProductSigStatus="";else ProductSigStatus=ProductSigStatus+"SIG EMPTY";
	
		System.out.println("PEDSigStatus  is "+xQuery);					
}
xQuery = "count ( for $i in collection('tig:///ePharma/APNSignatureStatus')";
xQuery = xQuery + "where ( $i/verifySig/verifyLevel='Product' and $i/verifySig/verifyDocumentId = '"+APNID+"' and  $i/verifySig/verifyPedigreeDoctId = '"+PedigId+"'";
xQuery = xQuery + " and $i/verifySig/verifyStatus = 'VALID') return $i/verifySig/verifyStatus )";
	
xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	 ProductValid= new String(xmlResults);
	 if(ProductValid.equals("0"))ProductValid="";else ProductValid=ProductValid+" VALID";
		System.out.println("PEDSigStatus  is "+PEDSigStatus+xQuery);					
}
xQuery = "count ( for $i in collection('tig:///ePharma/APNSignatureStatus')";
xQuery = xQuery + "where ( $i/verifySig/verifyLevel='Product' and $i/verifySig/verifyDocumentId = '"+APNID+"' and  $i/verifySig/verifyPedigreeDoctId = '"+PedigId+"'";
xQuery = xQuery + " and $i/verifySig/verifyStatus = 'INVALID') return $i/verifySig/verifyStatus )";
System.out.println("PEDSigStatus  is "+PEDSigStatus);	
xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	  ProductInValid = new String(xmlResults);
	 if(ProductInValid.equals("0"))ProductInValid="";else ProductInValid=ProductInValid+" INVALID";
		System.out.println("PEDSigStatus  is "+PEDSigStatus+xQuery);					
}









xQuery = "for $i in collection('tig:///ePharma/APNSignatureStatus')";
xQuery = xQuery + "where ( $i/verifySig/verifyLevel='Custody' and $i/verifySig/verifyDocumentId = '"+APNID+"' and  $i/verifySig/verifyPedigreeDoctId = '"+PedigId+"'";
xQuery = xQuery + ") return data($i/verifySig/verifyStatus)";
System.out.println("PEDSigStatus  is "+PEDSigStatus);	
xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	   CustSigStatus = new String(xmlResults);
		System.out.println("PEDSigStatus  is "+CustSigStatus);					
}else{
	   CustSigStatus="";
}	

xQuery = "for $i in collection('tig:///ePharma/APNSignatureStatus')";
xQuery = xQuery + "where ( $i/verifySig/verifyLevel='Custody' and $i/verifySig/verifyDocumentId = '"+APNID+"' and  $i/verifySig/verifyPedigreeDoctId = '"+PedigId+"' )";
xQuery = xQuery + "return data($i/verifySig/verifyDate)";
xmlResults = ReadTL(statement, xQuery);
  if(xmlResults != null) {
	   
	    CustSigStatusDate = new String(xmlResults);
							
}else{
	  CustSigStatusDate="";
}	











xQuery = "for $b in collection('tig:///ePharma/APN')/APN  ";
xQuery = xQuery + "where $b/DocumentId = '"+APNID+"' ";
xQuery = xQuery + "return  ";
xQuery = xQuery + "<tr class='tableRow_Header'>    ";
xQuery = xQuery + "<td class='type-whrite'>Pedigree ID : <STRONG><FONT color='#ffff00'>"+APNID+"</FONT></STRONG></td> ";
xQuery = xQuery + "<td class='type-whrite'>Issue Date : <FONT color='#ffff00'><STRONG>{data($b/DateTime)}</STRONG></FONT></td>     ";
xQuery = xQuery + "<td class='type-whrite'>Transaction Type: <FONT color='#ffff00'><STRONG>{data($b/To/TransactionType)}</STRONG></FONT></td> ";
xQuery = xQuery + "<td class='type-whrite'>Transaction # : <STRONG><FONT color='#ffff00'>{data($b/To/TransactionNumber)}</FONT></STRONG></td>   ";
xQuery = xQuery + "<td class='type=whrite' bgcolor='#ccffff'> ";
xQuery = xQuery + "{  if(data($b/To/TransactionType) = 'Order') then  ";
xQuery = xQuery + "(  ";
xQuery = xQuery + " <A HREF =\"javascript:newWindow('ePedigree_ViewOrder.jsp?trType={data($b/To/TransactionType)}&amp;trNum={data($b/To/TransactionNumber)}&amp;tp_company_nm=" + tp_company_nm + "&amp;pagenm=" + pagenm + "')\" > ";
xQuery = xQuery + "<FONT color='#000099'><STRONG>View</STRONG></FONT> </A> ";
xQuery = xQuery + ") ";
xQuery = xQuery + "else (  if(data($b/To/TransactionType) = 'DespatchAdvice') then ";
xQuery = xQuery + " <A HREF = \" javascript:newWindow('ASN_Details.jsp?trType={data($b/To/TransactionType)}&amp;trNum={data($b/To/TransactionNumber)}&amp;tp_company_nm=" + tp_company_nm + "&amp;pagenm=" + pagenm + "')\" > ";
xQuery = xQuery + "<FONT color='#000099'><STRONG>View</STRONG></FONT> </A> ";
xQuery = xQuery + "else  ";
xQuery = xQuery + " <A HREF = \" javascript:newWindow('Invoice_Details.jsp?trType={data($b/To/TransactionType)}&amp;trNum={data($b/To/TransactionNumber)}&amp;tp_company_nm=" + tp_company_nm + "&amp;pagenm=" + pagenm + "')\" > ";
xQuery = xQuery + "<FONT color='#000099'><STRONG>View</STRONG></FONT> </A> ";
xQuery = xQuery + " ) } ";
xQuery = xQuery + "</td>  ";
xQuery = xQuery + "</tr> ";

xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	TOPLINE = new String(xmlResults);
}

//GET Ship From Info
xQuery = "for $b in collection('tig:///ePharma/APN')/APN ";
xQuery = xQuery + "where $b/DocumentId = '"+APNID+"' ";
xQuery = xQuery + "return ";
xQuery = xQuery + "<table border='0' cellpadding='0' cellspacing='0'><tr><td class='td-menu'>Company:</td> ";
xQuery = xQuery + "<td class='td-menu bold'>{data($b/From/Name)}</td></tr> ";
xQuery = xQuery + "<tr><td class='td-menu'>Address:</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($b/From/Address)}</td></tr> ";
xQuery = xQuery + "<tr><td class='td-menu'>Contact:</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($b/From/ContactName)}</td></tr> ";
xQuery = xQuery + "<tr><td class='td-menu'>Phone:</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($b/From/Phone)}</td></tr> ";
xQuery = xQuery + "<tr><td class='td-menu'>Fax:</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($b/From/Fax)}</td></tr> ";
xQuery = xQuery + "<tr><td class='td-menu'>Email:</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($b/From/Email)}</td></tr> ";
xQuery = xQuery + "<tr><td class='td-menu'>License:</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($b/From/LicenseNumber)}</td></tr></table> ";
						
xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
FromAddr = new String(xmlResults);
}

//GET Ship To Info
xQuery = "for $b in collection('tig:///ePharma/APN')/APN ";
xQuery = xQuery + "where $b/DocumentId = '"+APNID+"' ";
xQuery = xQuery + "return ";
xQuery = xQuery + "<table border='0' cellpadding='0' cellspacing='0'><tr><td class='td-menu'>Company:</td> ";
xQuery = xQuery + "<td class='td-menu bold'>{data($b/To/Name)}</td></tr> ";
xQuery = xQuery + "<tr><td class='td-menu'>Address:</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($b/To/Address)}</td></tr> ";
xQuery = xQuery + "<tr><td class='td-menu'>Contact:</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($b/To/ContactName)}</td></tr> ";
xQuery = xQuery + "<tr><td class='td-menu'>Phone:</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($b/To/Phone)}</td></tr> ";
xQuery = xQuery + "<tr><td class='td-menu'>Fax:</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($b/To/Fax)}</td></tr> ";
xQuery = xQuery + "<tr><td class='td-menu'>Email:</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($b/To/Email)}</td></tr> ";
xQuery = xQuery + "<tr><td class='td-menu'>License:</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($b/To/LicenseNumber)}</td></tr></table> ";
						
xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	ToAddr = new String(xmlResults);
}

//GET Total Products
xQuery = "for $b in collection('tig:///ePharma/APN')/APN ";
xQuery = xQuery + "where $b/DocumentId = '"+APNID+"' ";
xQuery = xQuery + "return  ";
xQuery = xQuery + "count($b/Pedigrees/Pedigree/Products/Product/ProductName)";

//xQuery = "for $b in collection('tig:///ePharma/APN')/APN[DocumentId = '"+APNID+"']/Pedigrees/Pedigree[@order='"+PedigreeOrder+"'] ";
//xQuery = xQuery + "return  ";
//xQuery = xQuery + "sum(xs:int($b/Products/Product/Quantity))";

xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	totProducts = new String(xmlResults);
}

//GET Product LineItem Info
xQuery = "for $b in collection('tig:///ePharma/APN')/APN[DocumentId = '"+APNID+"']/Pedigrees/Pedigree[@order='"+PedigreeOrder+"'] ";
xQuery = xQuery + "return  ";
xQuery = xQuery + "let $prds := $b/Products ";
xQuery = xQuery + "let $mftr := $b/Manufacturer ";
xQuery = xQuery + "for $p in $prds/Product ";
xQuery = xQuery + "order by $p/BrandName ";
xQuery = xQuery + "return ";
xQuery = xQuery + "<tr class='tableRow_On'> ";
xQuery = xQuery + "<td class='td-menu'><A HREF='ProductDisplay.do?APNID="+APNID+"&amp;prdid={data($p/NDC)}&amp;lotnum={data($p/LotNumber)}&amp;pedOrd="+PedigreeOrder+"&amp;tp_company_nm="+tp_company_nm+"&amp;pagenm="+pagenm+"&amp;pbc={data($p/ParentEPC)}'>{data($p/ProductName)}</A></td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/NDC)}</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($mftr[LicenseNumber=data($p/ManufacturerLicense)]/Name)}</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/Quantity)}</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/LotNumber)}</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/LotExpireDate)}</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/ParentEPC)}</td> ";
xQuery = xQuery + "</tr>";
									
xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	PRODS = new String(xmlResults);
}

//GET Total Manufacturers
xQuery = "for $b in collection('tig:///ePharma/APN')/APN[DocumentId = '"+APNID+"']/Pedigrees/Pedigree[@order='"+PedigreeOrder+"'] ";
xQuery = xQuery + "return  ";
xQuery = xQuery + "count($b/Manufacturer)";

xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	totManufacturers = new String(xmlResults);
}

xQuery = "for $p in collection('tig:///ePharma/APN')/APN[DocumentId = '"+APNID+"']/Pedigrees/Pedigree[@order='"+PedigreeOrder+"'] ";
xQuery = xQuery + "return ";
xQuery = xQuery + "<tr class='tableRow_On'> ";
xQuery = xQuery + "<td class='td-menu'><A href='ViewManufacturerDetail.do?selpedid="+PedigreeOrder+"{codepoints-to-string(38)}mfrLicNum={data($p/Manufacturer/LicenseNumber)}{codepoints-to-string(38)}pagenm="+pagenm+"{codepoints-to-string(38)}tp_company_nm="+tp_company_nm+"{codepoints-to-string(38)}pedId="+APNID+"{codepoints-to-string(38)}mfrName={data($p/Manufacturer/Name)}'>{data($p/Manufacturer/Name)}</A></td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/Manufacturer/Address)}</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/Manufacturer/Contact)}</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/Manufacturer/ContactPhone)}</td> ";
xQuery = xQuery + "<td class='td-menu'><a href='mailto:{data($p/Manufacturer/ContactEmail)}'>{data($p/Manufacturer/ContactEmail)}</a></td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/Manufacturer/LicenseNumber)}</td> ";
xQuery = xQuery + "</tr>";

xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	MANUFACT = new String(xmlResults);
}

//GET Total Custodians
xQuery = "for $b in collection('tig:///ePharma/APN')/APN ";
xQuery = xQuery + "where $b/DocumentId = '"+APNID+"'  ";
xQuery = xQuery + "return  ";
xQuery = xQuery + "count($b/Pedigrees/Pedigree/Custody)";

xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	totCustodians = new String(xmlResults);
}

xQuery = "for $p in collection('tig:///ePharma/APN')/APN ";
xQuery = xQuery + "where $p/DocumentId = '"+APNID+"'  ";
xQuery = xQuery + "return ";
xQuery = xQuery + "<tr class='tableRow_On'> ";
xQuery = xQuery + "<td class='td-menu'><A href='ViewReceivingCustodianDetail.do?selpedid="+PedigreeOrder+"{codepoints-to-string(38)}mfrLicNum={data($p/Manufacturer/LicenseNumber)}{codepoints-to-string(38)}pagenm="+pagenm+"{codepoints-to-string(38)}tp_company_nm="+tp_company_nm+"{codepoints-to-string(38)}pedId="+APNID+"{codepoints-to-string(38)}mfrName={data($p/Manufacturer/Name)}'>{data($p/Pedigrees/Pedigree/Custody/Name)}</A></td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/Pedigrees/Pedigree/Custody/Address)}</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/Pedigrees/Pedigree/Custody/ContactName)}</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/Pedigrees/Pedigree/Custody/AuthenticatorPhone)}</td> ";
xQuery = xQuery + "<td class='td-menu'><a href='mailto:{data($p/Pedigrees/Pedigree/Custody/AuthenticatorEmail)}'>{data($p/Pedigrees/Pedigree/Custody/AuthenticatorEmail)}</a></td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/Pedigrees/Pedigree/Custody/InCustodyFromDate)} - {data($p/Pedigrees/Pedigree/Custody/InCustodyToDate)}</td> ";
xQuery = xQuery + "</tr>";
System.out.println("Query for Custody: "+xQuery);
xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	CUSTODY = new String(xmlResults);
}

CloseConnectionTL(connection);
%>

<html>
	<head>
		<title>Raining Data ePharma - ePedigree - APN Manager - Create APN</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">
			<style type="text/css"></style>
			<script language="JavaScript" type="text/JavaScript">
<!--
function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}
//-->
						</script>
			<script language="JavaScript" type="text/JavaScript">
<!--
function MM_openBrWindow(theURL,winName,features) { //v2.0
  window.open(theURL,winName,features);
}

function MM_openOrder(theURL) { //v2.0
  window.open(theURL,'Transaction Order Detail','scrollbars=yes,resizable=yes,width=800,height=600');
}


function MM_reloadPage(init) {  //reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
}
MM_reloadPage(true);

function MM_popupMsg(msg) { //v1.0
  alert(msg);
}
//-->
			</script>
	</head>
	<body onload = "accessdenied()">
		<%@include file='topMenu.jsp'%>
			<table border="0" cellspacing="0" cellpadding="0" width="100%" ID="Table1">
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
								<td class="td-typeblack">Pedigree Detail</td>
								<td class="td-typeblack" align="right"><FONT color="#0000cc">Anti Counterfeit Measures</FONT>
								</td>
							</tr>
							<tr class="tableRow_Header">
								<td colspan="2">
									<table width="100%" id="Table2" cellSpacing="1" cellPadding="1" align="left" border="0"
										bgcolor="white">
										<tr>
											<td colspan="5">
												<table border="1" cellpadding="1" cellspacing="1" width="100%">
													<%@ include file="Head2.jsp"%>
												</table>
											</td>
										</tr>
										<%=TOPLINE%>
									</table>
								</td>
							</tr>
							<tr class="tableRow_On">
								<td class="td-menu bold">From</td>
								<td class="td-menu bold">To</td>
							</tr>
							<tr class="tableRow_Off">
								<td><%=FromAddr%>
								</td>
								<td><%=ToAddr%>
								</td>
							</tr>
							<tr>
								<td colspan="2"><br>
									<table border="0" cellpadding="0" cellspacing="1" width="100%" ID="Table5">
										<tr>
											<td class="td-typeblack" colspan="7">Products - <FONT color="#336600">Total: <%=totProducts%> </FONT>
											</td>
										</tr>
										<tr class="tableRow_Header">
											<td class="type-whrite">Name</td>
											<td class="type-whrite">NDC</td>
											<td class="type-whrite">Manufacturer</td>
											<td class="type-whrite">QTY</td>
											<td class="type-whrite">Lot Number</td>
											<td class="type-whrite">Lot Expire Date</td>
											<td class="type-whrite">PBC</td>
										</tr>
										<%=PRODS%>
									</table>
								</td>
							</tr>
							<tr>
								<td colspan="2"><br>
									<table border="0" cellpadding="0" cellspacing="1" width="100%" ID="Table6">
										<tr>
											<td class="td-typeblack" colspan="7">Manufacturers In Shipment - <FONT color="#336600">Total: 
													<%=totManufacturers%></FONT>
											</td>
										</tr>
										<tr class="tableRow_Header">
											<td class="type-whrite">Name</td>
											<td class="type-whrite">Address</td>
											<td class="type-whrite">Contact</td>
											<td class="type-whrite">Phone</td>
											<td class="type-whrite">Email</td>
											<td class="type-whrite">License</td>
										</tr>
										<%=MANUFACT%>
									</table>
								</td>
							</tr>
							<tr>
								<Td colspan="2"><br>
									<table border="0" cellpadding="0" cellspacing="1" width="100%" ID="Table7">
									</table>
									<TABLE id="Table9" cellSpacing="1" cellPadding="0" width="100%" border="0">
										<TR>
											<TD class="td-typeblack" colSpan="7">Drug Sales &amp; Distribution - <FONT color="#336600">
													Total: <%=totCustodians%></FONT></TD>
										</TR>
										<TR class="tableRow_Header">
											<TD class="type-whrite" height="13">Name</TD>
											<TD class="type-whrite" height="13">Address</TD>
											<TD class="type-whrite" height="13">Contact</TD>
											<TD class="type-whrite" height="13">Phone</TD>
											<TD class="type-whrite" height="13">Email</TD>
											<TD class="type-whrite" height="13">Dates in Custody</TD>
										</TR>
										<%=CUSTODY%>
									</TABLE>
								</Td>
							</tr>
							<TR>
								<TD colSpan="2"><BR>
									<TABLE id="Table10" cellSpacing="1" cellPadding="0" width="100%" border="0">
										<TR>
											<TD class="td-typeblack" colSpan="8">** Document Validity Status **</TD>
										</TR>
										<TR class="tableRow_Header">
											<TD class="tableRow_On"><STRONG><FONT color="#0000cc">&nbsp; LEVEL</FONT></STRONG></TD>
											<TD class="type-whrite" width="83">APN Message</TD>
											<TD class="type-whrite" width="84">Pedigrees (Total: 1)</TD>
											<TD class="type-whrite" width="91">Manufacturers (Total: 1)</TD>
											<TD class="type-whrite" width="84">Custodians (Total: 1)</TD>
											<TD class="type-whrite" width="83">Products (Total: <%=ProductCount%>)</TD>
											<TD class="type-whrite" align="center" valign="middle">
												<TABLE width="301" cellSpacing="1" cellPadding="1" border="1" height="20">
													<TR>
														<TD class="type-whrite" colspan="4" align="center">Product Verification</TD>
													</TR>
												</TABLE>
											</TD>
										</TR>
										
										<% 
										   //    APNSigStatus = (String) session.getAttribute("signstatus");
										      
										      
										    //   PEDSigStatus = (String ) session.getAttribute("pedsignstatus");
										    //   PEDSigValid = (String ) session.getAttribute("pedsignvalid");
										    //   PEDSigInValid = (String ) session.getAttribute("pedsignInvalid");
										      
										       System.out.println("Sig status "+ APNSigStatus );
										       if(APNSigStatus == null )
										       APNSigStatus="";
										       if(PEDSigStatus == null || PEDSigStatus.equals("0"))PEDSigStatus="";
										       if(PEDSigValid == null||PEDSigValid.equals("0"))PEDSigValid="";
										       if(PEDSigInValid == null || PEDSigInValid.equals("0"))PEDSigInValid="";
										       %>
										
										
										
										<TR class="tableRow_Off">
											<TD class="td-menu"><STRONG><FONT color="#3300cc">STATUS</FONT></STRONG></TD>
												<TD class="td-menu" width="83">
												<P align="center"><STRONG><FONT color="#009900"><%=APNSigStatus%></FONT></STRONG></P>
											</TD>
											<TD class="td-menu" width="84">
												<P align="center"><STRONG><FONT color="#009900"><%=PEDSigStatus%></FONT></STRONG></P>
												<P align="center"><STRONG><FONT color="#009900"><%=""%></FONT></STRONG></P>
												<P align="center"><STRONG><FONT color="#009900"><%=""%></FONT></STRONG></P>
											
											</TD>
									 	   <TD class="td-menu" width="85">
												<P align="center"><STRONG><FONT color="#009900"><%=MENSigStatus%></FONT></STRONG></P>
												<P align="center"><STRONG><FONT color="#009900"><%=""%></FONT></STRONG></P>
												<P align="center"><STRONG><FONT color="#009900"><%=""%></FONT></STRONG></P>
											</TD>
											<TD class="td-menu" width="86">
												<P align="center"><STRONG><FONT color="#009900"><%=CustSigStatus%></FONT></STRONG></P>
												<P align="center"><STRONG><FONT color="#009900"><%=""%></FONT></STRONG></P>
												<P align="center"><STRONG><FONT color="#009900"><%=""%></FONT></STRONG></P>
											</TD>
											<TD class="td-menu" width="87">
												<P align="center"><STRONG><FONT color="#009900"><%=ProductSigStatus%></FONT></STRONG></P>
												<P align="center"><STRONG><FONT color="#009900"><%=ProductValid%></FONT></STRONG></P>
												<P align="center"><STRONG><FONT color="#009900"><%=ProductInValid%></FONT></STRONG></P>
											</TD>
										
											<TD class="type-whrite" align="center" valign="middle">
												<TABLE width="301" cellSpacing="1" cellPadding="1" border="1" height="32">
													<TR class="tableRow_Header">
														<TD class="type-whrite" width="51">Threshold</TD>
														<TD class="type-whrite">
															<P align="center"><FONT color="#ffffff">Recall</FONT></P>
														</TD>
														<TD class="type-whrite">
															Content/Dosage</TD>
														<TD class="type-whrite">Signature</TD>
														
													</TR>
													<TR class="tableRow_On">
														<TD width="51">
															<P align="center"><STRONG><FONT color="#669933">&nbsp;</FONT></STRONG></P>
														</TD>
														<TD>
															<P align="center"><STRONG><FONT color="#669933">&nbsp;</FONT></STRONG></P>
														</TD>
														<TD>
															<P align="center"><STRONG><FONT color="#669933">&nbsp;</FONT></STRONG></P>
														</TD>
														<TD>
															<P align="center"><STRONG><FONT color="#669933">&nbsp;</FONT></STRONG></P>
														</TD>
														
													</TR>
												</TABLE>
											</TD>
										</TR>
										<TR class="tableRow_On">
											<TD class="td-menu" height="13"><STRONG><FONT color="#3300cc">DATE</FONT></STRONG></TD>
											<TD class="td-menu" width="83" height="13"><%=APNSigStatusDate%></TD>
											<TD class="td-menu" width="84" height="13"><%=PEDSigStatusDate%></TD>
											<TD class="td-menu" width="91" height="13"><%=MENSigStatusDate%></TD>
											<TD class="td-menu" width="84" height="13"><%=CustSigStatusDate%></TD>
											<TD class="td-menu" width="83" height="13">&nbsp;</TD>
											<TD class="td-menu" height="13">
												<P align="center">&nbsp;</P>
											</TD>
										</TR>
										
										<TR class="tableRow_Off">
											<TD class="td-menu"><STRONG><FONT color="#3300cc"></FONT></STRONG></TD>
											<TD class="td-menu" width="83"><STRONG><FONT color="#0000ff"><A HREF="ValidateSignature.do?pedid=<%=APNID%>&part=APN&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree">AUTHENTICATE</A></FONT></STRONG></TD>
											<TD class="td-menu" width="84"><STRONG><FONT color="#0000ff"><A HREF="ValidateSignature.do?pedid=<%=APNID%>&part=Pedigree&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree">AUTHENTICATE</A></FONT></STRONG></TD>
											<TD class="td-menu" width="91"><STRONG><FONT color="#0000ff"><A HREF="ValidateSignature.do?pedid=<%=APNID%>&part=Manufacturer&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree">AUTHENTICATE</A></FONT></STRONG></TD>
											<TD class="td-menu" width="84"><STRONG><FONT color="#0000ff"><A HREF="ValidateSignature.do?pedid=<%=APNID%>&part=Custody&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree">AUTHENTICATE</A></FONT></STRONG></TD>
											<TD class="td-menu" width="83"><STRONG><FONT color="#0000ff"><A HREF="ValidateSignature.do?pedid=<%=APNID%>&part=Product&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree">AUTHENTICATE</A></FONT></STRONG></TD>
											
											 <TD class="td-menu">
												<P align="center"><STRONG><FONT color="#0000ff">AUTHENTICATE</FONT></STRONG></P>
											</TD>
										
										</TR>
									 
									
									<!--	
										commented by arun
										<TR class="tableRow_Off">
											<TD class="td-menu"><STRONG><FONT color="#3300cc"></FONT></STRONG></TD>
											<TD class="td-menu" width="83"><STRONG><FONT color="#0000ff"><A HREF="ValidateSignature.jsp?pedid=<%=APNID%>&part=APN&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree">AUTHENTICATE</A></FONT></STRONG></TD>
											<TD class="td-menu" width="84"><STRONG><FONT color="#0000ff">AUTHENTICATE</FONT></STRONG></TD>
											<TD class="td-menu" width="91"><STRONG><FONT color="#0000ff">AUTHENTICATE</FONT></STRONG></TD>
											<TD class="td-menu" width="84"><STRONG><FONT color="#0000ff">AUTHENTICATE</FONT></STRONG></TD>
											<TD class="td-menu" width="83"><STRONG><FONT color="#0000ff">AUTHENTICATE</FONT></STRONG></TD>
											
											 <TD class="td-menu">
												<P align="center"><STRONG><FONT color="#0000ff">AUTHENTICATE</FONT></STRONG></P>
											</TD>
										
										</TR>  -->
									
										
										
									</TABLE>
								</TD>
							</TR>
							<TR>
							<form name="CertifyPedigree" action="CertifyPedigreeDetail.do?pagenm=pedigree&tp_company_nm=" method="POST">
									<TD colSpan="2"><BR>
									<A onclick="MM_openBrWindow('PrintPedigree.jsp?apnId=<%=APNID%>','APN','scrollbars=yes,resizable=yes,width=800,height=600')"
										href="#"><IMG height="27" hspace="10" src="../../assets/images/print.gif" width="27" border="0"></A>EXPORT 
									AS: <INPUT id="Submit1" type="submit" value="XML" name="Submit1">
									    <INPUT id="Button1" onclick="MM_openBrWindow('DH2129_PedigreeForm.pdf','APN','scrollbars=yes,resizable=yes,width=800,height=600')"
										type="button" value="PDF" name="Button1">&nbsp;&nbsp;&nbsp;
										<INPUT  onClick="window.open('GenerateAlertAccess.do?sessionID=<%=sessionID%>&pedid=<%=APNID%>','apnwin','width=600,height=500')"
										type="button" value="Generate Alert" name="Button1">
									
									<input type="hidden" name="pedid" value="<%=APNID%>">
									<INPUT id="Submit4" type="button" value="Certify Receipt of Goods" onClick = "return receiptform()" >
									<INPUT id="Submit5" type="button" value="Certify Pedigree(s)" onClick = "return pedigreeform()" >
																
									<input type="hidden" value="sessionID">
								</TD>
							</form>
							</TR>
						</table>
						<DIV></DIV>
						<div id="footer">
							<table width="100%" height="24" border="0" cellpadding="0" cellspacing="0" ID="Table8">
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
