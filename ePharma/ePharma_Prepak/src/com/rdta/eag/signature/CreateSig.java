
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

 

package com.rdta.eag.signature;

 

import java.io.File;

import java.io.FileInputStream;

import java.io.FileOutputStream;

import java.io.InputStream;

import java.security.KeyStore;

import java.security.PrivateKey;

import java.security.cert.X509Certificate;

 

import javax.xml.parsers.DocumentBuilder;

 

import org.apache.xml.security.signature.XMLSignature;

import org.apache.xml.security.transforms.Transforms;

import org.apache.xml.security.utils.Constants;

import org.apache.xml.security.utils.XMLUtils;

import org.w3c.dom.Document;

import org.w3c.dom.Element;

import org.w3c.dom.Node;

 

/**

 * Signs a document with user's private key

 * 

 * @author asangha

 */

public class CreateSig {

    private static Document doc1 = null;

 

    private static XMLSignature sig1 = null;

 

    private static File signatureFile1 = null;

 

    private static String BaseURI1 = null;

    /**

     * get a signed document from a local file

     * @param fileName

     * @throws Exception

     */

    private static void getSignedDoc(String fileName) throws Exception {

        javax.xml.parsers.DocumentBuilderFactory dbf1 = javax.xml.parsers.DocumentBuilderFactory

                                                        .newInstance();

 

        // XML Signature needs to be namespace aware

        dbf1.setNamespaceAware(true);

 

        DocumentBuilder db1 = dbf1.newDocumentBuilder();

        InputStream in = null;

        File f = new File(fileName);

        in = new FileInputStream(f);

        doc1 = db1.parse(in);

        sig1 = new XMLSignature(doc1, BaseURI1,

                                XMLSignature.ALGO_ID_SIGNATURE_DSA);

        Element rt = doc1.getDocumentElement();

        rt.appendChild(sig1.getElement());

      //  doc1.appendChild(doc1.createComment(" Comment after "));

    }

 

    /**

     * get a signed document of an XML node.

     * @param ndXMLFile

     * @throws Exception

     */

    private static void getSignedDoc(Node ndXMLFile) throws Exception {

        javax.xml.parsers.DocumentBuilderFactory dbf1 = javax.xml.parsers.DocumentBuilderFactory

                                                        .newInstance();

 

        // XML Signature needs to be namespace aware

        dbf1.setNamespaceAware(true);

 

        DocumentBuilder db1 = dbf1.newDocumentBuilder();

        doc1 = db1.newDocument();

 

        Element rt = doc1.getDocumentElement();

        doc1.appendChild(doc1.importNode(ndXMLFile, true));

 

        sig1 = new XMLSignature(doc1, BaseURI1,

                                XMLSignature.ALGO_ID_SIGNATURE_DSA);

        rt = doc1.getDocumentElement();

        rt.appendChild(sig1.getElement());

        //doc1.appendChild(doc1.createComment(" Comment after "));

    }

 

    /**

     * 

     * @param xmlFileName --

     *            path to the xml file on hard drive

     * @param keyStoreFile --

     *            path to the keystore file

     * @param keyStoreFilePassword --

     *            password to the keystore

     * @return String -- representing signed XML document (currently returning

     *         signatureFile1.xml)

     */

    public static String signXMLDocument(String xmlFileName,

                                         String keyStoreFile, String keyStoreFilePassword, String keyAlias)

    throws Exception {

 

        org.apache.xml.security.Init.init();

        Constants.setSignatureSpecNSprefix("");

 

        // J-

        // All the parameters for the keystore

        String keystoreType = "JKS";

        String keystoreFile = keyStoreFile;// "RDTA_keystore";

        String keystorePass = keyStoreFilePassword;// "jasmine23";

        String privateKeyAlias = keyAlias;// "RDTAClient";

        String privateKeyPass = keyStoreFilePassword;// "jasmine23";

        String certificateAlias = keyAlias;// "RDTAClient";

        signatureFile1 = new File("c:\\signatureFile1.xml");

 

        // The BaseURI is the URI that's used to prepend to relative URIs

        BaseURI1 = signatureFile1.toURL().toString();

        // J+

        KeyStore ks = KeyStore.getInstance(keystoreType);

        FileInputStream fis = new FileInputStream(keystoreFile);

 

        // load the keystore

        ks.load(fis, keystorePass.toCharArray());

 

        // get the private key for signing.

        PrivateKey privateKey = (PrivateKey) ks.getKey(privateKeyAlias,

                                                       privateKeyPass.toCharArray());

        getSignedDoc(xmlFileName);

        sig1.getSignedInfo().addResourceResolver(new OfflineResolver());

 

        {

            Transforms transforms1 = new Transforms(doc1);

 

            transforms1.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);

 

            transforms1.addTransform(Transforms.TRANSFORM_C14N_WITH_COMMENTS);

            sig1.addDocument("", transforms1, Constants.ALGO_ID_DIGEST_SHA1);

        }

        {

            sig1.addDocument("http://www.w3.org/TR/xml-stylesheet");

            sig1.addDocument("http://www.nue.et-inf.uni-siegen.de/index.html");

        }

        {

            // Add in the KeyInfo for the certificate that we used the private

            // key of

            X509Certificate cert = (X509Certificate) ks

                                   .getCertificate(certificateAlias);

 

            sig1.addKeyInfo(cert);

            sig1.addKeyInfo(cert.getPublicKey());

            sig1.sign(privateKey);

 

        }

        FileOutputStream fos = new FileOutputStream(signatureFile1);

        XMLUtils.outputDOMc14nWithComments(doc1, fos);

        fos.close();

        String str1 = XMLUtil.convertToString(doc1);

 

        return str1;

    }

    

    public static void getTempFile(String xmlString) throws Exception {

        File f = new File("c:/security/data/tempdoc.xml");

        if (f.exists()) {

            f.delete();

        }

        f = new File("c:/security/data/tempdoc.xml");

        

        FileOutputStream fos = new FileOutputStream(f);

        fos.write(xmlString.getBytes());

        

    }

 

    public static String signXMLDoc(String xmlString,

                                    String keyStoreFilePath, String keyStoreFilePassword, String keyAlias)

    throws Exception {

 

        org.apache.xml.security.Init.init();

        Constants.setSignatureSpecNSprefix("");

 

        // J-

        // All the parameters for the keystore

        String keystoreType = "JKS";

        String keystoreFile = keyStoreFilePath;// "RDTA_keystore";

        String keystorePass = keyStoreFilePassword;// "jasmine23";

        String privateKeyAlias = keyAlias;// "RDTAClient";

        String privateKeyPass = keyStoreFilePassword;// "jasmine23";

        String certificateAlias = keyAlias;// "RDTAClient";

        signatureFile1 = new File("c:\\signatureFile1.xml");

 

        // The BaseURI is the URI that's used to prepend to relative URIs

        BaseURI1 = signatureFile1.toURL().toString();

        // J+

        KeyStore ks = KeyStore.getInstance(keystoreType);

        FileInputStream fis = new FileInputStream(keystoreFile);

 

        // load the keystore

        ks.load(fis, keystorePass.toCharArray());

 

        // get the private key for signing.

        PrivateKey privateKey = (PrivateKey) ks.getKey(privateKeyAlias,

                                                       privateKeyPass.toCharArray());

        

        getTempFile(xmlString);

        

        //String xmlFileName = "c:/security/data/xmldoc.xml";

        String xmlFileName = "c:/security/data/tempdoc.xml";

        getSignedDoc(xmlFileName);

        sig1.getSignedInfo().addResourceResolver(new OfflineResolver());

 

        {

            Transforms transforms1 = new Transforms(doc1);

 

            transforms1.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);

 

            transforms1.addTransform(Transforms.TRANSFORM_C14N_WITH_COMMENTS);

            sig1.addDocument("", transforms1, Constants.ALGO_ID_DIGEST_SHA1);

        }

        {

            sig1.addDocument("http://www.w3.org/TR/xml-stylesheet");

            sig1.addDocument("http://www.nue.et-inf.uni-siegen.de/index.html");

        }

        {

            // Add in the KeyInfo for the certificate that we used the private

            // key of

            X509Certificate cert = (X509Certificate) ks

                                   .getCertificate(certificateAlias);

 

            sig1.addKeyInfo(cert);

            sig1.addKeyInfo(cert.getPublicKey());

            sig1.sign(privateKey);

 

        }

        /*

        FileOutputStream fos = new FileOutputStream(signatureFile1);

        XMLUtils.outputDOMc14nWithComments(doc1, fos);

        fos.close();

        */

        String str = XMLUtil.convertToString(doc1);

        //System.out.println(str);

        return str;

        

    }

 

    static {

        org.apache.xml.security.Init.init();

    }

    public static void main(String[] args) throws Exception {

        System.out.println(CreateSig.signXMLDoc("<hello>there</hello>",

                "c:/security/keys/RDTA_keystore",

                "jasmine23", "RDTAClient"

                ));

        

    }

}

 
