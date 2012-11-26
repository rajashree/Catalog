package com.rdta.eag.signature;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import org.apache.xml.security.keys.KeyInfo;
//import org.apache.xml.security.samples.utils.resolver.OfflineResolver;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.utils.Constants;
import org.apache.xml.security.utils.XMLUtils;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Element;
import org.w3c.dom.*;
//import com.rdta.commons.xml.XMLUtil;
import org.apache.xml.security.signature.*;

/**
 * 
 * 
 * 
 * 
 * @author asangha
 * 
 */
public class VerifySig {

    public static String verifySig(String xmlSignedFile) {
        org.apache.xml.security.Init.init();
        // String signatureFileName =
        // "data/ie/baltimore/merlin-examples/merlin-xmldsig-fifteen/signature-enveloping-rsa.xml";
        // String signatureFileName = args[0];
        try {
            String signatureFileName = xmlSignedFile;

            javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory
                    .newInstance();

            dbf.setNamespaceAware(true);
            dbf.setAttribute("http://xml.org/sax/features/namespaces",
                    Boolean.TRUE);

            // File f = new File("signature.xml");
            File f = new File(signatureFileName);

            System.out.println("Try to verify " + f.toURL().toString());

            javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();

            db
                    .setErrorHandler(new org.apache.xml.security.utils.IgnoreAllErrorHandler());

            org.w3c.dom.Document doc = db.parse(new java.io.FileInputStream(f));

            Element nscontext = XMLUtils.createDSctx(doc, "ds",
                    Constants.SignatureSpecNS);
            // System.out.println(XMLUtil.convertToString(nscontext));
            Element sigElement = (Element) XPathAPI.selectSingleNode(doc,
                    "//ds:Signature[1]", nscontext);
            // System.out.println(XMLUtil.convertToString(sigElement));
            XMLSignature signature = new XMLSignature(sigElement, f.toURL()
                    .toString());

            signature.addResourceResolver(new OfflineResolver());

            // XMLUtils.outputDOMc14nWithComments(signature.getElement(),
            // System.out);
            KeyInfo ki = signature.getKeyInfo();

            if (ki != null) {
                if (ki.containsX509Data()) {
                    System.out
                            .println("Could find a X509Data element in the KeyInfo");
                }

                X509Certificate cert = signature.getKeyInfo()
                        .getX509Certificate();

                if (cert != null) {
                    /*
                     * System.out.println( "I try to verify the signature using
                     * the X509 Certificate: " + cert);
                     */
                    System.out
                            .println("The XML signature in file "
                                    + f.toURL().toString()
                                    + " is "
                                    + (signature.checkSignatureValue(cert) ? "valid (good)"
                                            : "invalid !!!!! (bad)"));
                    //SignedInfo si = signature.getSignedInfo();

                    System.out.println("certificate information");
                    System.out.println("Distinguish name" + cert.getIssuerDN()
                            + " Certificate Algorithm -" + cert.getSigAlgName()
                            + " Serial Number-" + cert.getSerialNumber());
                    System.out.println(cert.toString());
                    boolean b = signature.checkSignatureValue(cert);
                    return new Boolean(b).toString();
                } else {
                    System.out.println("Did not find a Certificate");

                    PublicKey pk = signature.getKeyInfo().getPublicKey();

                    if (pk != null) {
                        /*
                         * System.out.println( "I try to verify the signature
                         * using the public key: " + pk);
                         */
                        System.out
                                .println("The XML signature in file "
                                        + f.toURL().toString()
                                        + " is "
                                        + (signature.checkSignatureValue(pk) ? "valid (good)"
                                                : "invalid !!!!! (bad)"));
                        boolean b = signature.checkSignatureValue(pk);
                        return new Boolean(b).toString();
                    } else {
                        System.out
                                .println("Did not find a public key, so I can't check the signature");
                        return "false";
                    }
                }
            } else {
                System.out.println("Did not find a KeyInfo");
                return "false";
            }
        } catch (Exception ex) {
            return ex.toString();

        }

    }

    public static boolean verifySignature(Node nodeSigned) {
        org.apache.xml.security.Init.init();

        javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory
                .newInstance();

        dbf.setNamespaceAware(true);
        dbf
                .setAttribute("http://xml.org/sax/features/namespaces",
                        Boolean.TRUE);

        try {

            javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
            // db.setErrorHandler(new org.apache.xml.security.utils
            // .IgnoreAllErrorHandler());

            Document doc1 = db.newDocument();

            doc1.appendChild(doc1.importNode(nodeSigned, true));
            System.out.println(XMLUtil.convertToString(doc1));

            // org.w3c.dom.Document doc = db.parse(new
            // java.io.FileInputStream(f));
            Element nscontext = XMLUtils.createDSctx(doc1, "ds",
                    Constants.SignatureSpecNS);
            // System.out.println(XMLUtil.convertToString(nscontext));

            Element sigElement = (Element) XPathAPI.selectSingleNode(doc1,
                    "//ds:Signature[1]", nscontext);
            XMLSignature signature = new XMLSignature(sigElement, "Signed");
            // f.toURL().toString());

            signature.addResourceResolver(new OfflineResolver());

            // XMLUtils.outputDOMc14nWithComments(signature.getElement(),
            // System.out);
            KeyInfo ki = signature.getKeyInfo();

            if (ki != null) {
                if (ki.containsX509Data()) {
                    System.out
                            .println("Could find a X509Data element in the KeyInfo");
                }

                X509Certificate cert = signature.getKeyInfo()
                        .getX509Certificate();

                if (cert != null) {
                    /*
                     * System.out.println( "I try to verify the signature using
                     * the X509 Certificate: " + cert);
                     */
                    System.out
                            .println("The XML signature "
                                    + (signature.checkSignatureValue(cert) ? "valid (good)"
                                            : "invalid !!!!! (bad)"));

                    return signature.checkSignatureValue(cert);
                } else {
                    System.out.println("Did not find a Certificate");

                    PublicKey pk = signature.getKeyInfo().getPublicKey();

                    if (pk != null) {
                        /*
                         * System.out.println( "I try to verify the signature
                         * using the public key: " + pk);
                         */
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
        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return false;
    }

    public static String testVerifySig(String str) {
        return "hereeeeeeee dude " + str;
    }

    public static boolean testVerifySig1(String str) {
        return true;
    }

    static {

    }
}
