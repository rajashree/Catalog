// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 10/5/2005 12:56:17 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CreateSignature.java

package com.rdta.eag.epharma.util;

import com.rdta.eag.epharma.busapi.BusinessAPI;
import com.rdta.eag.signature.OfflineResolver;
import com.rdta.eag.signature.XMLUtil;
import java.io.*;
import java.net.URL;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.xml.security.Init;
import org.apache.xml.security.signature.SignedInfo;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

// Referenced classes of package com.rdta.eag.epharma.util:
//            VerifySig

public class CreateSignature
{

    public CreateSignature()
    {
    }

    private static Document getDocument(String xmlString)
        throws Exception
    {
        Document docRet = null;
        DocumentBuilderFactory dbf1 = DocumentBuilderFactory.newInstance();
        dbf1.setNamespaceAware(true);
        DocumentBuilder db1 = dbf1.newDocumentBuilder();
        java.io.InputStream in = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(xmlString.getBytes());
        in = bis;
        docRet = db1.parse(in);
        return docRet;
    }

    private static Document getSignedDoc(String xmlString)
        throws Exception
    {
        Document docRet = getDocument(xmlString);
        sig1 = new XMLSignature(docRet, BaseURI, "http://www.w3.org/2000/09/xmldsig#dsa-sha1");
        Element rt = docRet.getDocumentElement();
        rt.appendChild(sig1.getElement());
        return docRet;
    }

    public static String signXMLDocument(String xmlString, String keyStoreFile, String keyStoreFilePassword, String keyAlias)
        throws Exception
    {
        Init.init();
        Constants.setSignatureSpecNSprefix("");
        String keystoreType = "JKS";
        String keystoreFile = keyStoreFile;
        String keystorePass = keyStoreFilePassword;
        String privateKeyAlias = keyAlias;
        String privateKeyPass = keyStoreFilePassword;
        String certificateAlias = keyAlias;
        signatureFile = new File("c:\\signatureFile.xml");
        BaseURI = signatureFile.toURL().toString();
        KeyStore ks = KeyStore.getInstance(keystoreType);
        FileInputStream fis = new FileInputStream(keystoreFile);
        ks.load(fis, keystorePass.toCharArray());
        PrivateKey privateKey = (PrivateKey)ks.getKey(privateKeyAlias, privateKeyPass.toCharArray());
        Document signedDoc = getSignedDoc(xmlString);
        sig1.getSignedInfo().addResourceResolver(new OfflineResolver());
        Transforms transforms1 = new Transforms(signedDoc);
        transforms1.addTransform("http://www.w3.org/2000/09/xmldsig#enveloped-signature");
        transforms1.addTransform("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments");
        sig1.addDocument("", transforms1, "http://www.w3.org/2000/09/xmldsig#sha1");
        sig1.addDocument("http://www.w3.org/TR/xml-stylesheet");
        sig1.addDocument("http://www.nue.et-inf.uni-siegen.de/index.html");
        X509Certificate cert = (X509Certificate)ks.getCertificate(certificateAlias);
        sig1.addKeyInfo(cert);
        sig1.addKeyInfo(cert.getPublicKey());
        sig1.sign(privateKey);
        String str = XMLUtil.convertToString(signedDoc);
        return str;
    }

    public static void main(String args[])
        throws Exception
    {
        org.w3c.dom.Node nd = XMLUtil.parseFile("c:/apn.xml");
        String apnXML = XMLUtil.convertToString(nd);
        String str = signXMLDocument(apnXML, "C:/security/keys/RDTA_keystore", "jasmine23", "RDTAClient");
        System.out.println(str);
        BusinessAPI busApi = new BusinessAPI();
        busApi.submitAPN(str.substring(38), "348702495", "a0e194946c2c3a8dbdd0598adf2da12d7a8236b1");
        String apnDoc = busApi.getAPNById("APN12346", "348702495", "a0e194946c2c3a8dbdd0598adf2da12d7a8236b1");
        System.out.println(VerifySig.verifySignedNode(apnDoc));
        FileOutputStream fos = new FileOutputStream(signatureFile);
        fos.write(str.getBytes());
        fos.close();
    }

    private static XMLSignature sig1 = null;
    private static File signatureFile = null;
    private static String BaseURI = null;

    static 
    {
        Init.init();
    }
}
