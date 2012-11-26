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

 


package com.rdta.eag.commons.xml;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jaxen.SimpleNamespaceContext;
import org.jaxen.XPath;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Node;

/**
 * JaxenXPath represents an implementation to evaluate XPath queries against  DOM  nodes.
 *
 * @version 1.0
 * @author Srinivasa Raju Vegeraju
 */
public class JaxenXPath {

	// state
    private SimpleNamespaceContext nsContext = null;

    /**
     * Class constructor.
     */
    public JaxenXPath() {
        nsContext = new SimpleNamespaceContext();
    }

	/**
	 * Class constructor.
	 *  
	 * @param namespaces
	 */
	public JaxenXPath(Map namespaces) {
		 nsContext = new SimpleNamespaceContext(namespaces);
	}


    /**
     * Adds a namespace declaration with the specified prefix and uri to namespace context.
     * @param   prefix  the namespace prefix.
     * @param   uri     the namespace uri.
     */
    public void addNamespaceDeclaration(String prefix, String uri) {
        nsContext.addNamespace(prefix,uri);
    }

    /**
     * Uses an Xpath query string to select the boolean value from a node.
     * @param   context     contextNode
     * @param   xpath       query string
     * @return   true  if the node value is true, else  false .
     */
    public boolean getBooleanValue(Node context, String xpath) {
   
        boolean value = false;

        try {
            XPath path = new DOMXPath(xpath);
            // set the namespace context
            path.setNamespaceContext(nsContext);
            value = path.booleanValueOf(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    /**
     * Uses an Xpath query string to select the numeric value from a node. 
     * @param   context     contextNode
     * @param   xpath       query string
     * @return  numeric value from a node.
     */
    public double getNumberValue(Node context, String xpath) {
      
        Number number = null;

        try {
            XPath path = new DOMXPath(xpath);
            // set the namespace context
            path.setNamespaceContext(nsContext);
            number = path.numberValueOf(context);
        } catch (Exception e) {
			e.printStackTrace();
        }

        return (number != null ? number.doubleValue() : 0.0);
    }

    /**
     * Uses an Xpath query string to select the string value from a node. 
     * @param   context     contextnode
     * @param   xpath       query string
     * @return  string value from a node.
     */
    public String getStringValue(Node context, String xpath) {
       
        String value = null;

        try {
            XPath path = new DOMXPath(xpath);
            // set the namespace context
            path.setNamespaceContext(nsContext);
            value = path.stringValueOf(context);
        } catch (Exception e) {
			e.printStackTrace();

        }

        return value;

    }

    /**
     * Uses an Xpath query string to select a single node. XPath namespace
     * prefixes are resolved from the context node.
     * @param   context     The  Node  to start searching from
     * @param   xpath       Xpath query string
     * @return  The first  Node  that matches the XPath, or null.
     */
    public Node getSingleNode(Node context, String xpath) {
     
        Node node = null;

        try {
            XPath path = new DOMXPath(xpath);
            // set the namespace context
            path.setNamespaceContext(nsContext);
            node = (Node) path.selectSingleNode(context);
        } catch (Exception e) {
			e.printStackTrace();
        }

        return node;
    }
	
	
	  /**
     * Uses an Xpath query string to select a nodelist. XPath namespace
     * prefixes are resolved from the namespace node.
     * @param   context     The  Node  to start searching from
     * @param   xpath       Xpath query string
     * @param   namespace   The  Node  from which prefixes in the XPath
     *                      will be resolved to namespaces.
     * @return   List  containing nodes that matches the XPath.
     */
    public List getNodes(Node context, String xpath) {

        List nodeList = new ArrayList();

		try {
            XPath path = new DOMXPath(xpath);
            // set the namespace context
            path.setNamespaceContext(nsContext);
            List nodes = path.selectNodes(context);
            if (nodes != null) {
                // The list of nodes may contain non Node instances such as
                // Boolean in the case of an xpath predicate expression
                // eval, so we must filter these out before populating the
                // nodeList.  Otherwise, a ClassCastException occurs when
                // attempts are made to iterate through the list.  JT 04-17-03
                Iterator iterator = nodes.iterator();
                while (iterator.hasNext()) {
                   Object node = iterator.next();
                   if (node instanceof Node) 
					   nodeList.add(node);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } 

        return nodeList;
    }
	

  
    /**
     * Tests for the existence of nodes that match the Xpath query string. XPath namespace
     * prefixes are resolved from the context node.
     * @param   context     The  Node  to start searching from
     * @param   xpath       Xpath query string
     * @return   true  there are nodes that matches the XPath, else  false .
     */
    public boolean exists(Node context, String xpath) {

        boolean exists = false;

        try {
            XPath path = new DOMXPath(xpath);
            // set the namespace context
            path.setNamespaceContext(nsContext);
            List nodes = path.selectNodes(context);
            if (nodes != null && !nodes.isEmpty()) {
                // check if the result is of type boolean or a node
                Object item = nodes.get(0);
                if (Boolean.class.isInstance(item)) {
                    exists = ((Boolean) item).booleanValue();
                } else {
                    exists = true;
                }
            }

        } catch (Exception e) {
			e.printStackTrace();
        }

        return exists;
    }

}
