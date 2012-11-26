
package com.rdta.eag.epharma.commons.xml;

import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

public class XMLUtils
{

    public XMLUtils()
    {
    }

    public static Document createDocument()
        throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.newDocument();
    }

    public static Document createDocument(InputSource inputSource)
        throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(inputSource);
    }

    public static Document createDocument(InputStream inp)
        throws Exception
    {
        return createDocument(new InputSource(inp));
    }

    public static Document createDocument(String uri)
        throws Exception
    {
        InputSource ins = new InputSource(uri);
        return createDocument(ins);
    }

    public static String convertXMLDocumentToString(Document doc)
        throws Exception
    {
        OutputFormat of = new OutputFormat(doc);
        of.setOmitXMLDeclaration(true);
        StringWriter stringOut = new StringWriter();
        XMLSerializer serializer = new XMLSerializer(stringOut, of);
        serializer.serialize(doc);
        return stringOut.toString();
    }

    public static String convertXMLElementToString(Element ele)
        throws Exception
    {
        OutputFormat of = new OutputFormat();
        of.setOmitXMLDeclaration(true);
        StringWriter stringOut = new StringWriter();
        XMLSerializer serializer = new XMLSerializer(stringOut, of);
        serializer.serialize(ele);
        return stringOut.toString();
    }

    public static Document parseXML(InputSource inputSource, boolean namespaceAware)
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        dbf.setNamespaceAware(namespaceAware);
        Document document = null;
        try
        {
            DocumentBuilder db = dbf.newDocumentBuilder();
            document = db.parse(inputSource);
        }
        catch(Exception exp)
        {
            exp.printStackTrace();
        }
        return document;
    }

    public static Document parseXML(InputSource inputSource)
    {
        return parseXML(inputSource, false);
    }

    public static Document parseXML(InputStream inputStream, boolean namespaceAware)
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        dbf.setNamespaceAware(namespaceAware);
        Document document = null;
        try
        {
            DocumentBuilder db = dbf.newDocumentBuilder();
            document = db.parse(inputStream);
        }
        catch(Exception exp)
        {
            exp.printStackTrace();
        }
        return document;
    }

    public static Document parseXML(InputStream inputStream)
    {
        return parseXML(inputStream, false);
    }

    public static String getProperty(Node node, String name)
        throws Exception
    {
        return XPathAPI.selectSingleNode(node, name).getFirstChild().getNodeValue();
    }

    public static Node getNode(Node node, String name)
        throws Exception
    {
        return XPathAPI.selectSingleNode(node, name);
    }

    public static NodeList getChildNodeList(Node node, String name)
        throws Exception
    {
        return XPathAPI.selectNodeList(node, name);
    }

    public static void main(String args[])
        throws Exception
    {
        Document doc = createDocument();
        Element ele = doc.createElement("ABCD");
        ele.setAttribute("xyz", "sssss");
        doc.appendChild(ele);
        Element raju = doc.createElement("Raju");
        raju.setAttribute("KK1", "99");
        doc.getDocumentElement().appendChild(raju);
        String result = convertXMLDocumentToString(doc);
        System.out.print("Str : " + result);
    }

    private static String defaultEncoding = "UTF-8";

}