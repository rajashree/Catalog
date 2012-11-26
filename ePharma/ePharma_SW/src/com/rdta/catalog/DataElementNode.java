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

 
package com.rdta.catalog;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;



import java.util.List;

import org.jaxen.XPath;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Attr;
import org.w3c.dom.NodeList;



import org.jaxen.SimpleNamespaceContext;
import org.jaxen.XPath;
import org.jaxen.dom.DOMXPath;

import org.w3c.dom.NamedNodeMap;

import com.rdta.commons.xml.XMLUtil;


public class DataElementNode {
	
	private Node elementNode ;
	private boolean hasValueMap = false;	
		
	public DataElementNode(Node node) {
		
		if(node == null)
			throw new IllegalArgumentException("DataElementNode: Node is null");
		
		elementNode = node;
		Node value = XMLUtil.getNode(elementNode,"values/value");
		//find any values are there or not
		if(value != null)
			hasValueMap = true;
	}
	
	
	public Node getNode() {
		return elementNode;
	}
	
	
	
	
	public String getSourceEleName() {
		return XMLUtil.getValue(elementNode, "@sourceEleName");
	}
	
	public String getTargetEleName() {
		return XMLUtil.getValue(elementNode, "@targetEleName");
	}
	
	public String getAbsoluteSourceEleName() {
		return XMLUtil.getValue(elementNode, "@absoluteSourceEleName");
	}
	
	public String getAbsoluteTargetEleName() {
		return XMLUtil.getValue(elementNode, "@absoluteTargetEleName");
	}
	
	
	public boolean hasValueMap() {
		return hasValueMap;
	}
	
		
	public void createNewValueMap(String sourceValue, String targetValue) {
		Document rootDocument = elementNode.getOwnerDocument();
		Node values = XMLUtil.getNode(elementNode,"values");
		
		//create new value element
		Element newValueEle = rootDocument.createElement("value");
		
		//attribute mappings
		Attr sourceAttr = rootDocument.createAttribute("sourceValue");
		sourceAttr.setValue(sourceValue);
		newValueEle.setAttributeNode(sourceAttr);
		
		Attr targetAttr = rootDocument.createAttribute("targetValue");
		targetAttr.setValue(targetValue);
		newValueEle.setAttributeNode(targetAttr);
		
		//add value to values node
		values.appendChild(newValueEle);
	}
	
	public boolean containsSourceValue(String sourceValue ) {
		
		Node node = XMLUtil.getNode(elementNode, "values//value[@sourceValue = '" + sourceValue + "']");
		if(node !=null) {
			return true;
		}
		
		return false;
	}
	
	public String getTargetValue(String sourceValue ) {
		
		if(hasValueMap())
			return XMLUtil.getValue(elementNode, "values/value[@sourceValue = '" + sourceValue + "']/@targetValue");
		else
			return "";
	}
	
	public String getSourceValue(String targetValue ) {
		
		if(hasValueMap())
			return XMLUtil.getValue(elementNode, "values/value[@targetValue = '" + targetValue + "']/@sourceValue");
		else
			return "";
	}
	
}