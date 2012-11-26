// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 10/5/2005 12:58:40 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   VerifySig.java

package com.rdta.eag.epharma.util;

import com.rdta.eag.signature.OfflineResolver;
import java.io.*;
import java.net.URL;
import java.security.cert.X509Certificate;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.xml.security.Init;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.utils.IgnoreAllErrorHandler;
import org.apache.xml.security.utils.XMLUtils;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Element;

public class VerifySig
{

    public VerifySig()
    {
    }

    public static String verifySignedFile(String xmlSignedFile)
    {
        Init.init();
        try
        {
            String signatureFileName = xmlSignedFile;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            dbf.setAttribute("http://xml.org/sax/features/namespaces", Boolean.TRUE);
            File f = new File(signatureFileName);
            System.out.println("Try to verify " + f.toURL().toString());
            DocumentBuilder db = dbf.newDocumentBuilder();
            db.setErrorHandler(new IgnoreAllErrorHandler());
            org.w3c.dom.Document doc = db.parse(new FileInputStream(f));
            Element nscontext = XMLUtils.createDSctx(doc, "ds", "http://www.w3.org/2000/09/xmldsig#");
            Element sigElement = (Element)XPathAPI.selectSingleNode(doc, "//ds:Signature[1]", nscontext);
            XMLSignature signature = new XMLSignature(sigElement, f.toURL().toString());
            signature.addResourceResolver(new OfflineResolver());
            KeyInfo ki = signature.getKeyInfo();
            if(ki != null)
            {
                if(ki.containsX509Data())
                    System.out.println("Could find a X509Data element in the KeyInfo");
                X509Certificate cert = signature.getKeyInfo().getX509Certificate();
                if(cert != null)
                {
                    boolean b1 = signature.checkSignatureValue(cert);
                    System.out.println("certificate information");
                    System.out.println("Distinguish name" + cert.getIssuerDN() + " Certificate Algorithm -" + cert.getSigAlgName() + " Serial Number-" + cert.getSerialNumber());
                    System.out.println(cert.toString());
                    boolean b = signature.checkSignatureValue(cert);
                    return (new Boolean(b)).toString();
                }
                System.out.println("Did not find a Certificate");
                java.security.PublicKey pk = signature.getKeyInfo().getPublicKey();
                if(pk != null)
                {
                    System.out.println("The XML signature in file " + f.toURL().toString() + " is " + (signature.checkSignatureValue(pk) ? "valid (good)" : "invalid !!!!! (bad)"));
                    boolean b = signature.checkSignatureValue(pk);
                    return (new Boolean(b)).toString();
                } else
                {
                    System.out.println("Did not find a public key, so I can't check the signature");
                    return "false";
                }
            } else
            {
                System.out.println("Did not find a KeyInfo");
                return "false";
            }
        }
        catch(Exception ex)
        {
            return ex.toString();
        }
    }

    public static void getTempFile(String xmlString)
        throws Exception
    {
        File f = new File("c:/security/data/tempdocv.xml");
        if(f.exists())
            f.delete();
        f = new File("c:/security/data/tempdocv.xml");
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(xmlString.getBytes());
    }

    public static String verifySignedNode(String xmlString)
    {
        Init.init();
        try
        {
            getTempFile(xmlString);
            String signatureFileName = "c:/security/data/tempdocv.xml";
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            dbf.setAttribute("http://xml.org/sax/features/namespaces", Boolean.TRUE);
            File f = new File(signatureFileName);
            System.out.println("Try to verify " + f.toURL().toString());
            DocumentBuilder db = dbf.newDocumentBuilder();
            db.setErrorHandler(new IgnoreAllErrorHandler());
            org.w3c.dom.Document doc = db.parse(new FileInputStream(f));
            Element nscontext = XMLUtils.createDSctx(doc, "ds", "http://www.w3.org/2000/09/xmldsig#");
            Element sigElement = (Element)XPathAPI.selectSingleNode(doc, "//ds:Signature[1]", nscontext);
            XMLSignature signature = new XMLSignature(sigElement, f.toURL().toString());
            signature.addResourceResolver(new OfflineResolver());
            KeyInfo ki = signature.getKeyInfo();
            if(ki != null)
            {
                if(ki.containsX509Data())
                    System.out.println("Could find a X509Data element in the KeyInfo");
                X509Certificate cert = signature.getKeyInfo().getX509Certificate();
                if(cert != null)
                {
                    boolean b1 = signature.checkSignatureValue(cert);
                    System.out.println("certificate information");
                    System.out.println("Distinguish name" + cert.getIssuerDN() + " Certificate Algorithm -" + cert.getSigAlgName() + " Serial Number-" + cert.getSerialNumber());
                    System.out.println(cert.toString());
                    boolean b = signature.checkSignatureValue(cert);
                    return (new Boolean(b)).toString();
                }
                System.out.println("Did not find a Certificate");
                java.security.PublicKey pk = signature.getKeyInfo().getPublicKey();
                if(pk != null)
                {
                    System.out.println("The XML signature in file " + f.toURL().toString() + " is " + (signature.checkSignatureValue(pk) ? "valid (good)" : "invalid !!!!! (bad)"));
                    boolean b = signature.checkSignatureValue(pk);
                    return (new Boolean(b)).toString();
                } else
                {
                    System.out.println("Did not find a public key, so I can't check the signature");
                    return "false";
                }
            } else
            {
                System.out.println("Did not find a KeyInfo");
                return "false";
            }
        }
        catch(Exception ex)
        {
            return ex.toString();
        }
    }

    public static void main(String args[])
    {
        System.out.println(verifySignedFile("c:/signatureFile.xml"));
    }

    static 
    {
        Init.init();
    }
}