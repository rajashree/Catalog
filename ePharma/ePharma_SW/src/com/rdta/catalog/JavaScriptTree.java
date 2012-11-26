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



/**
 * Java Script Tree.
 * 
 * @author rsrinivasa
 *
 */
public class JavaScriptTree {

	private StringBuffer jsString = new StringBuffer();
	
	private StringBuffer xpath= new StringBuffer();
	
	private String catalogGenId;
	
	private Node targetNode;
	private String staticUrl= "SchemaElementEdit.do?";
	
	
	
	/**
	 * Constructs Java script tree as string.
	 * 
	 * @param node
	 */
	public JavaScriptTree(Node node, String catalogId,String url ) {
		
		if(node == null || catalogId == null) 
			throw new IllegalArgumentException("Given node should not be null");
		targetNode = node;
		catalogGenId = catalogId;
		staticUrl = url;
	}
	
	
	/**
	 * Return node.
	 * 
	 */
	public Node getNode() {
		
		return targetNode;
	}
	

	public String toJSString() {
		
		jsString.append("[");
		
		xpath.append(targetNode.getNodeName() );
		jsString.append(nodeJSString(targetNode));
		buildJSTree(targetNode);
		jsString.append(",]");
		
		//delete , in first place
		jsString.deleteCharAt(1);
			
		return jsString.toString();
	}
	
	public String toString() {
		return toJSString();
	}
	
	
	private void buildJSTree(Node node) {
					 
		 List childList = XMLUtil.executeQuery(node,"*");
		 		 
		 if(childList.size() <= 0 ) {
			 jsString.append( ",]");
			 //reduce xpath
			 removeXPathLastIndex();
		 } else {
			 for(int i=0; i<childList.size(); i++ ) {
			//	 System.out.println(" JS : " + i );
				 
				 Node currNode = (Node)childList.get(i);
				 switch (currNode.getNodeType()) {
				    case Node.ELEMENT_NODE:
						xpath.append("/");
						xpath.append(currNode.getNodeName());
						jsString.append(nodeJSString(currNode));
					//	System.out.println(" Attrvalue " + XMLUtil.getValue(currNode,"@id"));
						buildJSTree(currNode);
					    break;
					default:
				//		System.out.println(" default ");
				 }//end of switch
				 
			 } //end of for loop
			 
			 //reduce xpath
			 removeXPathLastIndex();
			
			 jsString.append( ",]");
		 }//end of if loop
	}
	
	
	private void removeXPathLastIndex() {
		 int index = xpath.lastIndexOf("/");
		 if(index > 0)
			 xpath.delete(index,xpath.length());
	}
	
	private String nodeJSString(Node node) {
						
		StringBuffer strBuff = new StringBuffer();
		strBuff.append(", [ \"");
		strBuff.append(node.getNodeName());
		strBuff.append("\", \"");
		strBuff.append(staticUrl );
		
		strBuff.append("xpath=");
		strBuff.append(xpath);
		strBuff.append("&catalogGenId=");
		strBuff.append(catalogGenId);
		
		strBuff.append("\"");
				
		return strBuff.toString();
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args)  {
		
	
		String xmlStr44 = "<payload><amount id=\"dddd\" ><due>  <xyz> <abc> </abc> </xyz> </due> </amount> <let1> </let1> <let> </let> </payload>";
		String xmlSter44 = "<payload><amount id=\"dddd\"  /> <let1> </let1> <let> </let> </payload>";
		String xmlStr = "<payload> <amount values=\"dddd;xyz;ssss\" /> </payload>";
		
		String xmlStr1 = "<Schema> <payload/> </Schema>";
	//	System.out.println(" xmlStr :" + xmlStr);
		
		Node node =  XMLUtil.parse(xmlStr);
		
		String xpath = "amount";
		
		String value = ";xyz;";
		String finalXpath = "contains("+ xpath + "/@values,'" + value+ "')";
		
		String values = XMLUtil.getValue(node,finalXpath);
		
	//	System.out.println(" Value :" + values);
		
			
		/*JavaScriptTree tree =  new JavaScriptTree(XMLUtil.getNode(node,"payload"), "9999");
		
		tree.addElement("amount","payload");
		
		tree.addElement("let","payload");
		
		tree.addElement("let1","payload");
		
		tree.replaceElement("let1000","payload/let1");
		
		tree.deleteElement("payload/let");
		
		System.out.println(" xmlStr :" +  tree.toJSString() );*/
	}
	
	
}
