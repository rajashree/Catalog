/********************************************************************************

* Raining Data Corp.

*

* Copyright (c) Raining Data Corp. All Rights Reserved.

*

* This software is confidential and proprietary information belonging to

* Raining Data Corp. It is the property of Raining Data Corp. and is protected

* under the Copyright Laws of the United States of America. No part of this

* software may be copied or used in any form without the prior

* written permission of Raining Data Corp.

*

*********************************************************************************/

 
package com.rdta.eag.signature.verify;

import java.io.File;
import java.io.*;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import org.apache.xml.security.keys.KeyInfo;

import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.utils.Constants;
import org.apache.xml.security.utils.XMLUtils;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Element;
import org.w3c.dom.*;



/**
 * 
 * 
 * 
 * 
 * @author asangha
 * 
 */
public class VerifySig {

    public static String verifySignedFile(String xmlSignedFile) {

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
                    boolean b1 = signature.checkSignatureValue(cert);
                    /*
                     * System.out .println("The XML signature in file " +
                     * f.toURL().toString() + " is " +
                     * (signature.checkSignatureValue(cert) ? "valid (good)" :
                     * "invalid !!!!! (bad)"));
                     */
                    // SignedInfo si = signature.getSignedInfo();
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
    public static void getTempFile(String xmlString) throws Exception {
        File f = new File("c:/security/data/tempdocv.xml");
        if (f.exists()) {
            f.delete();
        }
        f = new File("c:/security/data/tempdocv.xml");
        
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(xmlString.getBytes());
        
    }

    public static String verifySignedNode(String nodeSigned) {
        org.apache.xml.security.Init.init();
        // String signatureFileName =
        // "data/ie/baltimore/merlin-examples/merlin-xmldsig-fifteen/signature-enveloping-rsa.xml";
        // String signatureFileName = args[0];
        try {
            getTempFile(nodeSigned);
            String signatureFileName = "c:/security/data/tempdocv.xml";
            
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
                    boolean b1 = signature.checkSignatureValue(cert);
                    /*
                     * System.out .println("The XML signature in file " +
                     * f.toURL().toString() + " is " +
                     * (signature.checkSignatureValue(cert) ? "valid (good)" :
                     * "invalid !!!!! (bad)"));
                     */
                    // SignedInfo si = signature.getSignedInfo();
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
    static {
        org.apache.xml.security.Init.init();
    }
    public static void main(String[] args) {
        System.out.println(VerifySig.verifySignedFile("c:/signatureFile1.xml"));
    }

}
