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

import com.rdta.catalog.trading.model.Catalog;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.catalog.XMLStructure;

public class MappingNodeObject {
	
	private Node mappingNode ;
	
	
	public MappingNodeObject(Node node) {
		
		if(node == null)
			throw new IllegalArgumentException("MappingNodeObject: Node is null");
		
		mappingNode = node;
		
		System.out.println("dddddddddd " + XMLUtil.convertToString(node));
	}

	
	public Node getNode() {
		return mappingNode;
	}
	
	public Node getTargetNodeStructure() {
		return XMLStructure.getProductNode();
	}
	
	public Catalog getTargetCatalog() throws Exception {
		return Catalog.find(XMLUtil.getValue(mappingNode,"headerInfo/target/catalogId"));
	}
	
	public Catalog getSourceCatalog() throws Exception {
		return Catalog.find(XMLUtil.getValue(mappingNode,"headerInfo/source/catalogId"));
	}
	
	public List  getDataElementsList() {
		
		List result = new ArrayList();
		
		List dataElementNode = XMLUtil.executeQuery(mappingNode,"dataList/data/elementName");
		for(int i=0;i<dataElementNode.size(); i++) {
			
			result.add(new DataElementNode((Node)dataElementNode.get(i)));
		}
		
		return result;
		
	}
	
	public DataElementNode getDataElementNode(String sourceElement) {
		Node node =  XMLUtil.getNode(mappingNode, "dataList/data/elementName[@sourceEleName= '" + sourceElement + "']");
		
		if(node != null)  {
			return new DataElementNode(node);
		} 
		
		return null;
	}
	
	
	public DataElementNode getDataElementNodeFromAbsoluteSourceName(String sourceElement) {
		Node node =  XMLUtil.getNode(mappingNode, "dataList/data/elementName[@absoluteSourceEleName= '" + sourceElement + "']");
		return new DataElementNode(node);
	}
	
	
	public void deleteSourceElementBasedOnAbsolutePath(String sourceElement) {
		
		Node dataNode =  XMLUtil.getNode(mappingNode, "dataList/data[elementName/@absoluteSourceEleName= '"+ sourceElement + "']");
		if(dataNode != null) {
			Node parentNode = dataNode.getParentNode();
			parentNode.removeChild(dataNode);
		}
		
	}
	
	
	public void deleteElement(String sourceElement,String targetElement) {
		
		Node dataNode =  XMLUtil.getNode(mappingNode, "dataList/data[elementName/@absoluteSourceEleName= '"+ sourceElement + "' and elementName/@absoluteTargetEleName= '"+ targetElement + "']");
		if(dataNode != null) {
			Node parentNode = dataNode.getParentNode();
			parentNode.removeChild(dataNode);
		}
		
	}
	public void deleteTargetElementBasedOnAbsolutePath(String targetElement) {
		
		Node dataNode =  XMLUtil.getNode(mappingNode, "dataList/data[elementName/@absoluteTargetEleName= '"+ targetElement + "']");
		if(dataNode != null) {
			Node parentNode = dataNode.getParentNode();
			parentNode.removeChild(dataNode);
		}
		
	}
	
	public void replaceDataNodeTargetEleDefaultValue(String targetName) {
		
		Node node =  XMLUtil.getNode(mappingNode, "dataList/data/elementName[@absoluteTargetEleName= 'NEED_TO_BE_DEFINED_RAJU']");
		if(node != null) {
			XMLUtil.putValue(node, "@absoluteTargetEleName", targetName);
			if(targetName != null) {
				StringBuffer targetBuff = new StringBuffer(targetName);
				int index = targetBuff.indexOf("/");
				if(index > 0) {
					XMLUtil.putValue(node, "@targetEleName", targetBuff.substring(index+1,targetBuff.length()));
				}
			}
		}
	}
	
	public void replaceDataNodeSourceEleDefaultValue(String sourceName) {
		
		Node node =  XMLUtil.getNode(mappingNode, "dataList/data/elementName[@absoluteSourceEleName= 'NEED_TO_BE_DEFINED_RAJU']");
		if(node != null) {
			XMLUtil.putValue(node, "@absoluteSourceEleName", sourceName);
			if(sourceName != null) {
				StringBuffer targetBuff = new StringBuffer(sourceName);
				int index = targetBuff.indexOf("/");
				if(index > 0) {
					XMLUtil.putValue(node, "@sourceEleName", targetBuff.substring(index+1,targetBuff.length()));
				}
			}
		}
	}
	
	
	public void replaceDataNodeSource(String targetName,String sourceName) {
		
		Node node =  XMLUtil.getNode(mappingNode, "dataList/data/elementName[@absoluteTargetEleName= '"+targetName+"']");
		if(node != null) {
			XMLUtil.putValue(node, "@absoluteSourceEleName", sourceName);
			if(sourceName != null) {
				StringBuffer targetBuff = new StringBuffer(sourceName);
				int index = targetBuff.indexOf("/");
				if(index > 0) {
					XMLUtil.putValue(node, "@sourceEleName", targetBuff.substring(index+1,targetBuff.length()));
				}
			}
		}
	}
	
	
	
	public Node createDataNode(String sourceName) {
		
		 return createDataNode(sourceName,"NEED_TO_BE_DEFINED_RAJU");
	}
	
	public Node createDataNode1(String targetName) {
		
		 return createDataNode1("NEED_TO_BE_DEFINED_RAJU",targetName);
	}
	public Node createDataNode1(String sourceName, String targetName) {
		 System.out.println("soucen Name"+sourceName);
		return createDataNode1(sourceName,targetName,"sourceVal","targetVal");
	}
	public Node createDataNode(String sourceName, String targetName) {
		
		
		/*
		
		System.out.println(" Raju us here !!!!! ");
		Node node = createEmptyDataNode();
		
		System.out.println(" Raju us here !!!!! ");
		
		XMLUtil.putValue(node, "elementName/@absoluteSourceEleName", sourceName);
		XMLUtil.putValue(node, "elementName/@absoluteTargetEleName", targetName);
		
		//remove root element from the xpath
		//since this element information we won't get it from 
		//the input file
		if(sourceName != null) {
			StringBuffer sourceBuff = new StringBuffer(sourceName);
			int index = sourceBuff.indexOf("/");
			if(index > 0) {
				XMLUtil.putValue(node, "elementName/@sourceEleName", sourceBuff.substring(index+1,sourceBuff.length()));
			   // XMLUtil.putValue(node,"elementName/values/value/@sourceValue",sourceVal);
			
			}
			
		}
		
		System.out.println(" Raju us here !!!!! ");
				
		if(targetName != null) {
			StringBuffer targetBuff = new StringBuffer(targetName);
			int index = targetBuff.indexOf("/");
			if(index > 0) {
				XMLUtil.putValue(node, "elementName/@targetEleName", targetBuff.substring(index+1,targetBuff.length()));
				//XMLUtil.putValue(node,"elementName/values/value/@targetValue",targetVal);
			
			}
		}
		
	 	System.out.println(" Raju us here !!!!! ");
		
		Node dataList = XMLUtil.getNode(mappingNode,"dataList");
		dataList.appendChild(node);
		 
		System.out.println(" Raju us here !!!!! ");
				
		return node;
	*/
		
	return createDataNode(sourceName,targetName,"sourceVal","targetVal");
	}
	
	
	public Node createDataNode1(String sourceName, String targetName,String sourceVal, String targetVal) {
		
		System.out.println(" Raju us here !!!!! ");
		Node node = createEmptyDataNode();
		
		System.out.println(" Raju us here !!!!! "+sourceName);
		
		XMLUtil.putValue(node, "elementName/@absoluteSourceEleName", sourceName);
		XMLUtil.putValue(node, "elementName/@absoluteTargetEleName", targetName);
		
		//remove root element from the xpath
		//since this element information we won't get it from 
		//the input file
		if(sourceName != null) {
			StringBuffer sourceBuff = new StringBuffer(sourceName);
			int index = sourceBuff.indexOf("/");
			if(index > 0) {
				XMLUtil.putValue(node, "elementName/@sourceEleName", sourceBuff.substring(index+1,sourceBuff.length()));
			  			
			}
			
		}
		
		System.out.println(" Raju us here !!!!! ");
				
		if(targetName != null) {
			StringBuffer targetBuff = new StringBuffer(targetName);
			int index = targetBuff.indexOf("/");
			if(index > 0) {
				XMLUtil.putValue(node, "elementName/@targetEleName", targetBuff.substring(index+1,targetBuff.length()));
							
			}
		}
		
	 	System.out.println(" Raju us here !!!!! ");
		
		Node dataList = XMLUtil.getNode(mappingNode,"dataList");
		dataList.appendChild(node);
		 
		System.out.println(" Raju us here !!!!! ");
				
		return node;
	}
	
	
	
	public Node createDataNode(String sourceName, String targetName,String sourceVal, String targetVal) {
		
		System.out.println(" Raju us here !!!!! ");
		Node node = createEmptyDataNode();
		
		System.out.println(" Raju us here !!!!! ");
		
		XMLUtil.putValue(node, "elementName/@absoluteSourceEleName", sourceName);
		XMLUtil.putValue(node, "elementName/@absoluteTargetEleName", targetName);
		
		//remove root element from the xpath
		//since this element information we won't get it from 
		//the input file
		if(sourceName != null) {
			StringBuffer sourceBuff = new StringBuffer(sourceName);
			int index = sourceBuff.indexOf("/");
			if(index > 0) {
				XMLUtil.putValue(node, "elementName/@sourceEleName", sourceBuff.substring(index+1,sourceBuff.length()));
			   XMLUtil.putValue(node,"elementName/values/value/@sourceValue",sourceVal);
			
			}
			
		}
		
		System.out.println(" Raju us here !!!!! ");
				
		if(targetName != null) {
			StringBuffer targetBuff = new StringBuffer(targetName);
			int index = targetBuff.indexOf("/");
			if(index > 0) {
				XMLUtil.putValue(node, "elementName/@targetEleName", targetBuff.substring(index+1,targetBuff.length()));
				XMLUtil.putValue(node,"elementName/values/value/@targetValue",targetVal);
			
			}
		}
		
	 	System.out.println(" Raju us here !!!!! ");
		
		Node dataList = XMLUtil.getNode(mappingNode,"dataList");
		dataList.appendChild(node);
		 
		System.out.println(" Raju us here !!!!! ");
				
		return node;
	}
	
	
	
	private Node createEmptyDataNode() {

		Document rootDocument = mappingNode.getOwnerDocument();
		
		
		Element newElementName = rootDocument.createElement("elementName");
				
		//attribute mappings
		Attr sourceAttr = rootDocument.createAttribute("sourceEleName");
		newElementName.setAttributeNode(sourceAttr);
		
		Attr targetAttr = rootDocument.createAttribute("targetEleName");
		newElementName.setAttributeNode(targetAttr);

		Attr absoluteSourceAttr = rootDocument.createAttribute("absoluteSourceEleName");
		newElementName.setAttributeNode(absoluteSourceAttr);
		
		Attr absoluteTargetAttr = rootDocument.createAttribute("absoluteTargetEleName");
		newElementName.setAttributeNode(absoluteTargetAttr);
		
		
		Attr sourceVal = rootDocument.createAttribute("sourceValue");
		Attr targetVal = rootDocument.createAttribute("targetValue");
		//value mappings
		Element values = rootDocument.createElement("values");
		Element valuesElement = rootDocument.createElement("value");
		valuesElement.setAttributeNode(sourceVal);
		valuesElement.setAttributeNode(targetVal);
		values.appendChild(valuesElement);
		
		//append values to new element name element
		newElementName.appendChild(values);
		
		
		//create data element and add to datalist
		Element newElement = rootDocument.createElement("data");
		newElement.appendChild(newElementName);
		
		return newElement;
			
	}
}