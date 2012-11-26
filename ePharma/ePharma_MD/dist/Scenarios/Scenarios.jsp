
<%@ include file='../../includes/jspinclude.jsp'%>



<%
String pagenm = request.getParameter("rsAction");

if(rsAction == null) { rsAction = "";}

com.rdta.tlapi.xql.Connection connection = ConnectTL();
com.rdta.tlapi.xql.Statement statement = getStatement(connection);

String query = null;
String tpNames = null;
byte[] xmlResult;

if(rsAction.equal("1a")) {   //1a - Receive Pedigree From RxPak
	query = "tlsp:SubmitPedigree(<APN>
	<DocumentId>10120512345</DocumentId>
	<DateTime>2005-10-13T09:30:47</DateTime>
	<From>
		<Name>McKesson RxPak Division</Name>
		<Address>1221 Carol Lane, 
Dallas,52001,TX</Address>
		<LicenseNumber>MCRxPak</LicenseNumber>
		<PartyType>Distributor</PartyType>
		<ContactName>Jane Doe</ContactName>
		<Phone>(715) 983-8306</Phone>
		<Fax>(715) 983-8305</Fax>
		<Email>jane.doe@mckesson.com</Email>
	</From>
	<To>
		<Name>McKesson RDC</Name>
		<Address>123 Main Street, LakeLand FL, 60022</Address>
		<LicenseNumber>543609912313343</LicenseNumber>
		<PartyType>Distributor</PartyType>
		<ContactName>Melanie Farber</ContactName>
		<Phone>(315) 345-2306</Phone>
		<Fax>(315) 345-2308</Fax>
		<Email>melanie.farber@mcrx.com</Email>
		<TransactionType>despatchAdvice</TransactionType>
		<TransactionNumber>77723145</TransactionNumber>
	</To>
	<Pedigrees>
		<Pedigree order="0">
			<DocumentId>P234572222</DocumentId>
			<IssueDate>2005-10-13T09:30:47</IssueDate>
			<Products>
				<Product>
					<EPC>00103003339876543218</EPC>
					<ParentEPC>00103003339876543218</ParentEPC>
					<NDC>NDC 67158-157-23</NDC>
					<LegendDrugName>Lipitor</LegendDrugName>
					<DosageForm>Tablet</DosageForm>
					<DosageStrength>40mg</DosageStrength>
					<ContainerSize>Bottle</ContainerSize>
					<LotNumber>5C017</LotNumber>
					<LotExpireDate>2009-06-01T09:30:47</LotExpireDate>
					<Quantity quantityUnitCode="CASES">1</Quantity>
					<ManufacturerLicense>ML222333421</ManufacturerLicense>
					<CustodyLicenseNumber>MCRxPak</CustodyLicenseNumber>
				</Product>
			</Products>
			<Manufacturer>
				<Name>Pfizer</Name>
				<Address>123 First st, New York, NY 11770</Address>
				<Contact>George Doe</Contact>
				<ContactPhone>(212) 341-1567</ContactPhone>
				<ContactEmail>george.doe@pfizer.com</ContactEmail>
				<LicenseNumber>ML222333421</LicenseNumber>
			</Manufacturer>
			<Custody order="0" type="Distributor">
				<Name>McKesson RxPak Division</Name>
				<Address>1221 Carol Lane, 
Dallas,52001,TX</Address>
				<Contact>Jane Doe</Contact>
				<TransactionDate>2006-07-17T09:30:47</TransactionDate>
				<TransactionType>despatchAdvice</TransactionType>
				<TransactionNumber>DA23145</TransactionNumber>
				<AuthenticatorName>Jane Doe</AuthenticatorName>
				<AuthenticatorEmail>jane.doe@mckesson.com</AuthenticatorEmail>
				<AuthenticatorPhone>(715) 983-8306</AuthenticatorPhone>
				<LicenseNumber>MCRxPak</LicenseNumber>
				<InCustodyFromDate>2005-09-30T09:30:47</InCustodyFromDate>
				<InCustodyToDate>2005-10-13T09:30:47</InCustodyToDate>
			</Custody>
			<PedigreeStatus>
				<Status>
					<StatusChangedOn>2005-09-30T015:30:47</StatusChangedOn>
					<Status>created</Status>
				</Status>
			</PedigreeStatus>
		</Pedigree>
	</Pedigrees>
	<Signature xmlns="http://www.w3.org/2000/09/xmldsig#">
		<SignedInfo xmlns="http://www.w3.org/2000/09/xmldsig#">
			<CanonicalizationMethod xmlns="http://www.w3.org/2000/09/xmldsig#" Algorithm="http://www.w3.org/TR/2001/REC-xml-c14n-20010315"/>
			<SignatureMethod xmlns="http://www.w3.org/2000/09/xmldsig#" Algorithm="http://www.w3.org/2000/09/xmldsig#dsa-sha1"/>
			<Reference xmlns="http://www.w3.org/2000/09/xmldsig#" URI="">
				<Transforms xmlns="http://www.w3.org/2000/09/xmldsig#">
					<Transform xmlns="http://www.w3.org/2000/09/xmldsig#" Algorithm="http://www.w3.org/2000/09/xmldsig#enveloped-signature"/>
					<Transform xmlns="http://www.w3.org/2000/09/xmldsig#" Algorithm="http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments"/>
				</Transforms>
				<DigestMethod xmlns="http://www.w3.org/2000/09/xmldsig#" Algorithm="http://www.w3.org/2000/09/xmldsig#sha1"/>
				<DigestValue xmlns="http://www.w3.org/2000/09/xmldsig#">z3PT1oDeYIw/UnztmvKQI6/CdmQ=</DigestValue>
			</Reference>
			<Reference xmlns="http://www.w3.org/2000/09/xmldsig#" URI="http://www.w3.org/TR/xml-stylesheet">
				<DigestMethod xmlns="http://www.w3.org/2000/09/xmldsig#" Algorithm="http://www.w3.org/2000/09/xmldsig#sha1"/>
				<DigestValue xmlns="http://www.w3.org/2000/09/xmldsig#">RFJNR7jm3subSypvlLe4iwd2fAI=</DigestValue>
			</Reference>
			<Reference xmlns="http://www.w3.org/2000/09/xmldsig#" URI="http://www.nue.et-inf.uni-siegen.de/index.html">
				<DigestMethod xmlns="http://www.w3.org/2000/09/xmldsig#" Algorithm="http://www.w3.org/2000/09/xmldsig#sha1"/>
				<DigestValue xmlns="http://www.w3.org/2000/09/xmldsig#">Hpg+6h1k1jYY5yr3TRzDZzw23CQ=</DigestValue>
			</Reference>
		</SignedInfo>
		<SignatureValue xmlns="http://www.w3.org/2000/09/xmldsig#">W1j2BEh/TfBSXr8EcYdu9f8QMT0WKj5bZeBqTLXX0Sg+ACm5/ZVh+w==</SignatureValue>
		<KeyInfo xmlns="http://www.w3.org/2000/09/xmldsig#">
			<X509Data xmlns="http://www.w3.org/2000/09/xmldsig#">
				<X509Certificate xmlns="http://www.w3.org/2000/09/xmldsig#">
MIIDLDCCAuoCBELwQzkwCwYHKoZIzjgEAwUAMHwxCzAJBgNVBAYTAlVTMQswCQYDVQQIEwJDQTER
MA8GA1UEBxMIU2FuIEpvc2UxFTATBgNVBAoTDFJhaW5pbmcgRGF0YTEUMBIGA1UECxMLRW5naW5l
ZXJpbmcxIDAeBgNVBAMTF0FTYW5naGEgTWlkZGxld2FyZSBUZWFtMB4XDTA1MDgwMzA0MDgyNVoX
DTE1MDgwMTA0MDgyNVowfDELMAkGA1UEBhMCVVMxCzAJBgNVBAgTAkNBMREwDwYDVQQHEwhTYW4g
Sm9zZTEVMBMGA1UEChMMUmFpbmluZyBEYXRhMRQwEgYDVQQLEwtFbmdpbmVlcmluZzEgMB4GA1UE
AxMXQVNhbmdoYSBNaWRkbGV3YXJlIFRlYW0wggG3MIIBLAYHKoZIzjgEATCCAR8CgYEA/X9TgR11
EilS30qcLuzk5/YRt1I870QAwx4/gLZRJmlFXUAiUftZPY1Y+r/F9bow9subVWzXgTuAHTRv8mZg
t2uZUKWkn5/oBHsQIsJPu6nX/rfGG/g7V+fGqKYVDwT7g/bTxR7DAjVUE1oWkTL2dfOuK2HXKu/y
IgMZndFIAccCFQCXYFCPFSMLzLKSuYKi64QL8Fgc9QKBgQD34aCF1ps93su8q1w2uFe5eZSvu/o6
6oL5V0wLPQeCZ1FZV4661FlP5nEHEIGAtEkWcSPoTCgWE7fPCTKMyKbhPBZ6i1R8jSjgo64eK7Om
dZFuo38L+iE1YvH7YnoBJDvMpPG+qFGQiaiD3+Fa5Z8GkotmXoB7VSVkAUw7/s9JKgOBhAACgYB4
bCKptwOSfyYQpfEe9hfvWeYXhg6gg2HU3TL1OWqWQuTTZKgNRTZ0AjEtth2D9BNVD4UT6qj1c1Te
YmZl+sUHOhxB0ss5xrX+7mDzJGfrG2mfjY/EMQJdMwCkSGMbrlSUOJmiNME0HP+YN37hmtwZYIOW
M/cd/44XCw0CIDAMZDALBgcqhkjOOAQDBQADLwAwLAIUEj9NGCgobCiNeRb1+QzfdO/SKjkCFBGi
S2WzCn4Hix1OhsRoD3jeCqSS
</X509Certificate>
			</X509Data>
			<KeyValue xmlns="http://www.w3.org/2000/09/xmldsig#">
				<DSAKeyValue xmlns="http://www.w3.org/2000/09/xmldsig#">
					<P xmlns="http://www.w3.org/2000/09/xmldsig#">
/X9TgR11EilS30qcLuzk5/YRt1I870QAwx4/gLZRJmlFXUAiUftZPY1Y+r/F9bow9subVWzXgTuA
HTRv8mZgt2uZUKWkn5/oBHsQIsJPu6nX/rfGG/g7V+fGqKYVDwT7g/bTxR7DAjVUE1oWkTL2dfOu
K2HXKu/yIgMZndFIAcc=
</P>
					<Q xmlns="http://www.w3.org/2000/09/xmldsig#">l2BQjxUjC8yykrmCouuEC/BYHPU=</Q>
					<G xmlns="http://www.w3.org/2000/09/xmldsig#">
9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3
zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKL
Zl6Ae1UlZAFMO/7PSSo=
</G>
					<Y xmlns="http://www.w3.org/2000/09/xmldsig#">
eGwiqbcDkn8mEKXxHvYX71nmF4YOoINh1N0y9TlqlkLk02SoDUU2dAIxLbYdg/QTVQ+FE+qo9XNU
3mJmZfrFBzocQdLLOca1/u5g8yRn6xtpn42PxDECXTMApEhjG65UlDiZojTBNBz/mDd+4ZrcGWCD
ljP3Hf+OFwsNAiAwDGQ=
</Y>
				</DSAKeyValue>
			</KeyValue>
		</KeyInfo>
	</Signature>
</APN>
)";
	xmlResult = ReadTL(statement, query);
	//if(xmlResult != null) {
	//	tpNames = new String(xmlResult);
	//}

}



CloseConnectionTL(connection);
%>
<html>
	<head>
		<title>Raining Data ePharma - McKEsson Scenarios</title>
	<body>
				<P>&nbsp;</P>
				<P>McKesson Scenarios:</P>
				<TABLE id="Table1" cellSpacing="1" cellPadding="1" width="100%" align="center" border="1">
					<TR>
						<TD>1a - Receive Pedigree From RxPak</TD>
						<TD><A HREF="Scenarios.jsp?rsAction=1a">EXECUTE</A>/TD>
					</TR>
					<TR>
						<TD>1b - RDC Login / View Pedigree</TD>
						<TD></TD>
					</TR>
					<TR>
						<TD>1c - Submit PBC</TD>
						<TD><A HREF="Scenarios.jsp?rsAction=1c">EXECUTE</A></TD>
					</TR>
					<TR>
						<TD>1d - Pick and Scan PBC</TD>
						<TD></TD>
					</TR>
					<TR>
						<TD>1e - Submit Despatch Advice + Submit Shipment PBC Info</TD>
						<TD><A HREF="Scenarios.jsp?rsAction=1c">EXECUTE</A></TD>
					</TR>
					<TR>
						<TD>1f - Locate Pedigree &amp; Sign</TD>
						<TD></TD>
					</TR>
					<TR>
						<TD></TD>
						<TD></TD>
					</TR>
					<TR>
						<TD></TD>
						<TD></TD>
					</TR>
					<TR>
						<TD></TD>
						<TD></TD>
					</TR>
				</TABLE>
	</body>
</html>

