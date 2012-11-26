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

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.transform.OutputKeys;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.Writer;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import java.util.List;

/**
 * XMLUtil represents a utility class that provides xml convenience methods.
 * 
 * @version 1.0
 * @author Srinivasa Raju Vegeraju
 */
public class XMLUtil {

    // jaxen xpath
    private static final JaxenXPath jaxenXPath = new JaxenXPath();

    // document builder factory
    private static DocumentBuilderFactory dbf = null;

    // identify transformer factory
    private static TransformerFactory itf = null;

    static {
        // create a document builder factory
        dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(false);
        dbf.setValidating(false);
        dbf.setIgnoringComments(true);

        // create a identity transformer factory
        itf = new org.apache.xalan.processor.TransformerFactoryImpl();
    }

    /**
     * Class constructor
     */
    private XMLUtil() {
    }

    /**
     * Gets the DOM Document Builder Factory
     * 
     * @return DocumentBuilderFactory
     */
    public static DocumentBuilderFactory getDocumentBuilderFactory() {
        return dbf;
    }

    /**
     * Gets the DOM Document Builder Factory
     * 
     * @return DocumentBuilderFactory
     */
    public static JaxenXPath getJaxenXPath() {
        return jaxenXPath;
    }

    /**
     * Gets a new instance of an identity Transformer
     * 
     * @return Transformer
     */
    public static Transformer getTransformer() {

        Transformer transformer = null;

        try {
            transformer = itf.newTransformer();
        } catch (TransformerConfigurationException tce) {
            tce.printStackTrace();
        }

        return transformer;

    }

    /**
     * Gets the value from a node using XPath expression.
     * 
     * @param contextNode
     *            Node to be searched for the value
     * @param xpath
     *            query path to the node that has the value
     * @return value of the node pointed to by the xpath expression.
     */
    public static String getValue(Node contextNode, String xpath) {
        if (contextNode == null) {
            return null;
        }

        String value = null;
        try {
            value = jaxenXPath.getStringValue(contextNode, xpath);
        } catch (Exception e) {
        }

        if (value != null) {
            value = value.trim();
            if (value.length() == 0) {
                value = null;
            }
        }

        return value;

    }

    /**
     * Construct a node's value recursively.
     * 
     * @param node
     *            context Node .
     * @param buffer
     *            StringBuffer to contain the value.
     * @param deep -
     *            If true, recursively get value from the subtree
     * @return StringBuffer containing node's value
     */
    private static StringBuffer getValue(Node node, StringBuffer buffer,
            boolean deep) {

        if (isText(node)) {
            // text or cdata node. get the node value
            buffer.append(node.getNodeValue());
        } else {
            // element node. construct value from child text nodes
            NodeList childNodes = node.getChildNodes();
            int length = childNodes.getLength();
            for (int i = 0; i < length; i++) {
                Node child = childNodes.item(i);
                if (deep) {
                    getValue(child, buffer, deep);
                } else {
                    if (isText(child)) {
                        // text or cdata node. get the node value
                        buffer.append(child.getNodeValue());
                    }
                }
            }
        }

        return buffer;
    }

    /**
     * Gets the value from a node.
     * 
     * @param node
     *            Node to be searched for the value
     * @return value of the node.
     */
    public static String getValue(Node node) {
        return getValue(node, false);
    }

    /**
     * Gets the value from a node.
     * 
     * @param node
     *            Node to be searched for the value
     * @param deep -
     *            If true, recursively get value from the subtree
     * @return value of the node.
     */
    public static String getValue(Node node, boolean deep) {

        if (node == null) {
            return null;
        }

        String value = null;

        switch (node.getNodeType()) {
        case Node.TEXT_NODE:
        case Node.CDATA_SECTION_NODE:
            value = node.getNodeValue();
            break;

        case Node.ATTRIBUTE_NODE:
            value = ((Attr) node).getValue();
            break;

        case Node.ELEMENT_NODE:
            value = getValue(node, new StringBuffer(), deep).toString();
            break;
        }

        if (value != null) {
            value = value.trim();
            if (value.length() == 0) {
                value = null;
            }
        }

        return value;

    }

    /**
     * Updates a node identified by xpath with a value.
     * 
     * @param contextNode
     *            Node to be searched for the node
     * @param xpath
     *            query path to the node that has to be updated with the value
     * @param value
     *            value for the node
     */
    public static void putValue(Node contextNode, String xpath, String value) {

        if (contextNode == null || value == null) {
            return;
        }

        Document doc = contextNode.getOwnerDocument();

        if (doc == null) {
            return;
        }

        try {
            Node node = jaxenXPath.getSingleNode(contextNode, xpath);

            if (node != null) {
                switch (node.getNodeType()) {
                case Node.TEXT_NODE:
                case Node.CDATA_SECTION_NODE:
                    break;

                case Node.ATTRIBUTE_NODE:
                    ((Attr) node).setValue(value.trim());
                    break;

                case Node.ELEMENT_NODE:
                    // if node has no text nodes, create it
                    if (!node.hasChildNodes()) {
                        node.appendChild(doc.createTextNode(value.trim()));
                    } else {
                        NodeList childNodes = node.getChildNodes();
                        int length = childNodes.getLength();
                        for (int i = 0; i < length; i++) {
                            Node child = childNodes.item(i);
                            if (isText(child)) {
                                child.setNodeValue(value.trim());
                                return;
                            }
                        }

                        node.appendChild(doc.createTextNode(value.trim()));
                    }
                    break;
                }
            }
        } catch (Exception e) {
        }

    }

    /**
     * Add an element to a path
     * 
     * @param node -
     *            Node value
     * @param xpath -
     *            path of the place it will be added.
     * @param elementName -
     *            element name to add.
     * @author - Anthony Sangha
     */
    public static void putElement(Node node, String xpath, String elementName) {
        Document doc = node.getOwnerDocument();
        Node nd = jaxenXPath.getSingleNode(node, xpath);
        nd.appendChild(doc.createElement(elementName));
    }

    /**
     * Put a node inside another node at a given path.
     * 
     * @param node
     * @param childNode
     */
    public static void putNode(Node node, String xpath, Node childNode) {
        if (node == null || childNode == null) {
            return;
        }

        Document doc = node.getOwnerDocument();

        if (doc == null) {
            return;
        }

        switch (node.getNodeType()) {
        case Node.TEXT_NODE:
        case Node.CDATA_SECTION_NODE:
        case Node.ATTRIBUTE_NODE:
            break;
        case Node.ELEMENT_NODE:
            Node nd = jaxenXPath.getSingleNode(node, xpath);
            nd.appendChild(doc.importNode(childNode, true));
            break;
        }

    }

    /**
     * Updates a node with a value.
     * 
     * @param node
     *            Node to be updated with the value
     * @param value
     *            value for the node
     */
    public static void putValue(Node node, String value) {

        if (node == null || value == null) {
            return;
        }

        Document doc = node.getOwnerDocument();

        if (doc == null) {
            return;
        }

        switch (node.getNodeType()) {
        case Node.TEXT_NODE:
        case Node.CDATA_SECTION_NODE:
            break;

        case Node.ATTRIBUTE_NODE:
            ((Attr) node).setValue(value.trim());
            break;

        case Node.ELEMENT_NODE:
            // if node has no text nodes, create it
            if (!node.hasChildNodes()) {
                node.appendChild(doc.createTextNode(value.trim()));
            } else {
                NodeList childNodes = node.getChildNodes();
                int length = childNodes.getLength();
                for (int i = 0; i < length; i++) {
                    Node child = childNodes.item(i);
                    if (isText(child)) {
                        child.setNodeValue(value.trim());
                        return;
                    }
                }

                node.appendChild(doc.createTextNode(value.trim()));
            }

            break;
        }
    }

    /**
     * Checks if the given node is text.
     * 
     * @param node
     *            the Node to check.
     * @return true if text, else false .
     */
    public static boolean isText(Node node) {

        switch (node.getNodeType()) {
        case Node.TEXT_NODE:
        case Node.CDATA_SECTION_NODE:
            return true;
        default:
            return false;
        }
    }

    /**
     * Gets the node by evaluating the xpath
     * 
     * @param context
     *            Node representing the node to be queried
     * @param xpath
     *            Xpath string
     * @return Node which is the result of the query
     */
    public static Node getNode(Node context, String xpath) {

        Node node = null;

        List nodeList = executeQuery(context, xpath);

        if (nodeList.size() > 0) {
            node = (Node) nodeList.get(0);
        }

        return node;
    }

    /**
     * Executes the query against the instance and returns results of the query
     * as a NodeList .
     * 
     * @param instance
     *            <code <Node against which the query is to be run
     * @param xpath
     *            Xpath query string
     * @return NodeList representing the results of the query
     */
    public static List executeQuery(Node instance, String xpath) {

        List nodeList = null;

        try {
            nodeList = jaxenXPath.getNodes(instance, xpath);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return nodeList;

    }

    /**
     * Removes all child nodes from the context node.
     * 
     * @param contextNode
     *            Node whose children needs to be removed
     */
    public static void removeChildNodes(Node contextNode) {

        try {
            NodeList nodeList = contextNode.getChildNodes();

            while (nodeList.getLength() > 0) {
                contextNode.removeChild(nodeList.item(0));
            }
        } catch (DOMException de) {
        }
    }

    /**
     * Gets a List of nodes found by traversing the context node
     * 
     * @param context
     *            Node containing child nodes
     * @return List of nodes found by traversing the context node
     */
    public static List getNodes(Node context) {

        Document doc = context.getOwnerDocument();

        // create node iterators to traverse the subtrees allowing only element
        // nodes
        NodeIterator source = ((DocumentTraversal) doc).createNodeIterator(
                context, NodeFilter.SHOW_ELEMENT, null, false);

        List nodes = getNodes(source);

        source.detach();

        return nodes;
    }

    /**
     * Gets a List of nodes found by traversing the node iterator
     * 
     * @param source
     *            NodeIterator
     * @return List of nodes found by traversing the node iterator
     */
    public static List getNodes(NodeIterator source) {

        List nodes = new ArrayList();

        Node sourceNode = null;

        // iterate over the source nodes and add them to the map
        while ((sourceNode = source.nextNode()) != null) {

            // add the node to the list
            nodes.add(sourceNode);
        }

        return nodes;
    }

    /**
     * Gets a List of nodes found by parsing the given xml string
     * 
     * @param xmlString
     *            string containing xml data
     * @return List of nodes found parsing the given xml string
     */
    public static List getNodes(String xmlString) {

        List nodes = new ArrayList();

        try {

            DocumentBuilder db = dbf.newDocumentBuilder();
            StringReader reader = new StringReader(xmlString);
            Document doc = db.parse(new InputSource(reader));
            Node root = doc.getDocumentElement();
            NodeList nodeList = root.getChildNodes();
            int length = nodeList.getLength();

            for (int i = 0; i < length; i++) {
                Node node = nodeList.item(i);
                nodes.add(node);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return nodes;
    }

	

    /**
     * Evaluates the expression against the context node.
     * @param contextNode  node to be tested for the expression
     * @param expression    xpath expression
     * @return true if condition is true
     */
    public static boolean evaluate(Node contextNode, String expression) {

        if (contextNode == null || expression == null) {
            return false;
        }

        boolean success = false;

        try {
            success = jaxenXPath.exists(contextNode, expression);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return success;

    }
	
	
	
	
	
	
	
    /**
     * Parse a file with provide fileName. (eg. c:/xmlfile.xml)
     * 
     * @param fileName
     * @return Node representing a file
     */
    public static Node parseFile(String fileName) {
        File f = new File(fileName);
        return XMLUtil.parse(f);
    }

    /**
     * Gets a node after parsing the file.
     * 
     * @param file
     *            File that contains the node
     * @return Node contained in the file.
     */
    public static Node parse(File file) {

        InputStream in = null;

        try {
            in = new FileInputStream(file);
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }

        Node node = null;

        try {

            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(in);
            node = doc.getDocumentElement();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            // close the input stream
            try {
                in.close();
            } catch (Throwable te) {
                // ignore
            }
        }

        return node;
    }

    /**
     * Returns a Document instance that is normalized by parsing the filename
     * represented by the filename.
     * 
     * @param file
     *            the File to parse.
     * @return the parsed Document
     */
    public static Document parseAndNormalize(File file) {
        Document doc = parse(file).getOwnerDocument();
        doc.normalize();
        return doc;
    }

    /**
     * Gets a Node after by parsing the given xml string
     * 
     * @param xmlString
     *            string containing xml data
     * @return Node found after parsing the given xml string
     */
    public static Node parse(String xmlString) {

        Node node = null;

        try {
            StringReader reader = new StringReader(xmlString);

            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(reader));
            node = doc.getDocumentElement();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return node;
    }

    /**
     * Similar to getNode but parses an xmlString and returns node for the path
     * 
     * @param xmlString
     * @param xpath
     * @return
     */
    public static Node parse(String xmlString, String xpath) {
        Node node = null;
        try {
            Node nd = XMLUtil.parse(xmlString);
            node = jaxenXPath.getSingleNode(nd, xpath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return node;
    }

    /**
     * Gets a Node after by parsing the given xml string
     * 
     * @param in
     *            the InputStream to parse.
     * @return Node found after parsing the given xml string
     */
    public static Node parse(InputStream in) {

        Node node = null;
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(in);
            node = doc.getDocumentElement();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return node;
    }

    /**
     * Serializes a DOM Node into a ByteArrayOutputStream.
     * 
     * @param node
     *            Node
     * @return ByteArrayOutputStream
     */
    public static ByteArrayOutputStream getOutputStream(Node node) {

        Transformer transformer = getTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "no");

        // create a StreamResult to hold the results of the transformation
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        BufferedOutputStream bous = new BufferedOutputStream(bout);
        StreamResult result = new StreamResult(bous);

        try {
            transformer.transform(new DOMSource(node), result);
        } catch (TransformerException te) {
            te.printStackTrace();
        }

        return bout;
    }

    /**
     * Serializes a DOM Node into a ByteArrayInputStream.
     * 
     * @param node
     *            Node
     * @return ByteArrayInputStream
     */
    public static ByteArrayInputStream getInputStream(Node node) {
        return new ByteArrayInputStream(getOutputStream(node).toByteArray());
    }

    /**
     * Serializes a DOM Node into a String.
     * 
     * @param node
     *            Node
     * @return String
     */
    public static String convertToString(Node node) {
        // create a new string writer
        StringWriter writer = new StringWriter();
        // serialize the node to the writer
        serialize(node, writer);

        return writer.toString();
    }

    /**
     * Serializes a DOM Node into a String.
     * 
     * @param node
     *            Node
     * @param omitDecl
     *            omit xml declaration
     * @return String
     */
    public static String convertToString(Node node, boolean omitDecl) {
        // create a new string writer
        StringWriter writer = new StringWriter();
        // serialize the node to the writer
        serialize(node, writer, omitDecl);

        return writer.toString();
    }

    /**
     * Serializes the given node and writes the output to the given file.
     * 
     * @param node
     *            Node to serialize
     * @param fileURL
     *            identifying the file where the output should be written.
     * @return true if serialization was successful, else false .
     */
    public static boolean serialize(Node node, String fileURL) {

        try {

            Transformer serializer = getTransformer();

            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            // serializer.setOutputProperty(OutputProperties.S_KEY_INDENT_AMOUNT,
            // "3");
            // serializer.setOutputProperty(OutputPropertiesFactory.S_KEY_INDENT_AMOUNT,
            // "3");
            // serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
            // "yes");
            serializer
                    .transform(new DOMSource(node), new StreamResult(fileURL));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Serializes the given node and writes the output to the given
     * outputstream.
     * 
     * @param node
     *            Node to serialize
     * @param out
     *            OutputStream to write the output to.
     * @return true if serialization was successful, else false .
     */
    public static boolean serialize(Node node, OutputStream out) {

        try {

            Transformer serializer = getTransformer();
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.transform(new DOMSource(node), new StreamResult(out));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Serialize the given Node and writes content into the given writer
     */
    public static boolean serialize(Node node, Writer writer) {
        return serialize(node, writer, false);
    }

    /**
     * Serialize Node and writes content into the given writer
     */
    public static boolean serialize(Node node, Writer writer, boolean omitDecl) {
        return serialize(node, writer, omitDecl, false);
    }

    /**
     * Serialize Node and writes content into the given writer
     */
    public static boolean serialize(Node node, Writer writer, boolean omitDecl,
            boolean indent) {

        try {
            Transformer serializer = getTransformer();
            if (omitDecl) {
                serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
                        "yes");
            }
            if (indent) {
                serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            }
            serializer.transform(new DOMSource(node), new StreamResult(writer));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
