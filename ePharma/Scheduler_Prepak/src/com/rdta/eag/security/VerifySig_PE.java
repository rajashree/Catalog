package com.rdta.eag.security;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.Principal;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.utils.Constants;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.rdta.eag.epharma.commons.xml.XMLUtils;

/**
 *
 *
 *
 *
 * @author asangha
 *
 */
public class VerifySig_PE extends VerifySig {

	private static boolean verifySignature(byte[] canonical) throws Exception {

		javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory
				.newInstance();
		dbf.setNamespaceAware(true);
		System.out.println("Inside verifySignature1");

		javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
		db
				.setErrorHandler(new org.apache.xml.security.utils.IgnoreAllErrorHandler());
		System.out.println("Inside verifySignature2");
		InputSource inputSource = new InputSource(new StringReader(new String(
				canonical)));
		Document doc1 = db.parse(inputSource);
		System.out.println("Inside verifySignature3");
		Element nscontext = org.apache.xml.security.utils.XMLUtils.createDSctx(
				doc1, "ds", Constants.SignatureSpecNS);

		Element sigElement = (Element) XPathAPI.selectSingleNode(doc1,
				"//ds:Signature[1]", nscontext);
		XMLSignature signature = new XMLSignature(sigElement, null);
		KeyInfo ki = signature.getKeyInfo();

		if (ki != null) {
			if (ki.containsX509Data()) {
				System.out
						.println("Could find a X509Data element in the KeyInfo");
			}

			X509Certificate cert = signature.getKeyInfo().getX509Certificate();

			if (cert != null) {
				System.out.println("The XML signature "
						+ (signature.checkSignatureValue(cert) ? "valid (good)"
								: "invalid !!!!! (bad)"));
				printCertificateInfo(cert);
				return signature.checkSignatureValue(cert);
			} else {
				System.out.println("Did not find a Certificate; Try to get public key.");

				PublicKey pk = signature.getKeyInfo().getPublicKey();

				if (pk != null) {
					System.out
							.println("The XML signature "
									+ (signature.checkSignatureValue(pk) ? "valid (good)"
											: "invalid !!!!! (bad)"));
					return signature.checkSignatureValue(pk);
				} else {
					System.out
							.println("Did not find a public key, so I can't check the signature");
					return false;
				}
			}
		} else {
			System.out.println("Did not find a KeyInfo");
			return false;
		}
	}

	/**
	 * Verify Pedigeree Envelope with 0 to n pedigrees
	 * @param pedigree -- String representing the pedigree envelope
	 * @return
	 * @throws Exception
	 */
	public static boolean verifyPedigreeEnvelopeString(String pedigreeEnvelope)
			throws Exception {
		try {

			Document signedEnvelope = XMLUtils.createDocument(new InputSource(new StringReader(pedigreeEnvelope)));
			NodeList pedigreesList = signedEnvelope.getDocumentElement().getElementsByTagName("pedigree");

			int pedNum = pedigreesList.getLength();
			System.out.println("# of pedigrees verifying " + pedNum);

			for (int i = 0 ; i < pedNum; i++) {
				// first get the pedigree node

				Node ndPedigree = pedigreesList.item(i);
				String pedigreeStr = XMLUtils.convertXMLElementToString((Element)ndPedigree);
				System.out.println(" Verifying " + (i + 1) + " pedigree ");// + pedigreeStr);
				
				// second do the canonicalization
				
				Canonicalizer canonicalizer = Canonicalizer
						.getInstance("http://www.w3.org/2001/10/xml-exc-c14n#");
				System.out.println("***** After ****");
				byte[] canonical = canonicalizer.canonicalize(pedigreeStr.getBytes());
				System.out.println("***** After2 ****");
				// finally, verify
				if (!verifySignature(canonical)) {
					System.out.println("failed " + (i + 1) + " pedigree");
					return false;
				}
				System.out.println("***** After3 ****");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				String logMessage = pedigreeEnvelope;

				StringWriter writer = new StringWriter();

				ex.printStackTrace(new PrintWriter(writer));

				logMessage = logMessage + "\n" + writer.toString();

				throw new Exception(logMessage);

			} catch (Exception e) {

			}

		}
		return true;

	}

	public static boolean verifyPedigreeNode(Node nodeSigned) throws Exception {

		try {

			Canonicalizer canonicalizer = Canonicalizer
					.getInstance("http://www.w3.org/2001/10/xml-exc-c14n#");
			byte[] canonical = canonicalizer.canonicalizeSubtree(nodeSigned);

			return verifySignature(canonical);

		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				String logMessage = XMLUtils
						.convertXMLElementToString((Element) nodeSigned);

				StringWriter writer = new StringWriter();

				ex.printStackTrace(new PrintWriter(writer));

				logMessage = logMessage + "\n" + writer.toString();

				throw new Exception(logMessage);

			} catch (Exception e) {

			}

		}
		return false;
	}
	public static void printCertificateInfo(X509Certificate cert) {
		Principal pr = cert.getSubjectDN();
		String distinguishedName = pr.getName();
		System.out.println("Distinguished Name: " + distinguishedName);
	}

	
	static {
		org.apache.xml.security.Init.init();
	}
}
