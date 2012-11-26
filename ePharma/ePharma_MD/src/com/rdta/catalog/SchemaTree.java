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
 * SchemaTree Tree.
 * 
 * @author rsrinivasa
 *
 */
public class SchemaTree {


	private Node targetNode;
	public Node treeNode;
	//I added
	public static int count=0;
	public static int count1=0;
	static int disOrd=0;
	static int maxSize=0;
	static int ord=0;
	/**
	 * Constructs Java script tree as string.
	 * 
	 * @param node
	 */
	public SchemaTree(Node node) {
		
		if(node == null ) 
			throw new IllegalArgumentException("Given node should not be null");
		
		targetNode = node;
	}
	
	public int numOfNodes(){
		int i=0;
		maxSize = targetNode.getChildNodes().getLength();
		return maxSize;
	}
	
	
	/**
	 * Return node.
	 * 
	 */
	public Node getNode() {
		
		return targetNode;
	}
	public Node getTreeNode(){
		
		return treeNode;
	}
	
	/**
	 * Remove first element name from the xpath
	 * 
	 * @param xpath
	 * @return
	 */
	private String removeRootElementFromXPath(String xpath) {
		StringBuffer xpathBuff = new StringBuffer(xpath);
		int firstIndex = xpathBuff.indexOf("/");
		if(firstIndex > 0 ) {
			xpathBuff.delete(0,firstIndex+1);
		} else {
			//incase of parent node return current node itself
			xpathBuff = new StringBuffer(".");
		}
		
		return xpathBuff.toString();
	}
	private String removeRootElementFromXPath(String xpath,boolean flag) {
		StringBuffer xpathBuff = new StringBuffer(xpath);
		int firstIndex = xpathBuff.indexOf("/");
		if(firstIndex >=1  ) {
			xpathBuff.delete(0,firstIndex+1);
		} else {
			//incase of parent node return current node itself
			xpathBuff = new StringBuffer(".");
		}
		
		return xpathBuff.toString();
	}
	
	/**
	 * Set values attrbute value of the specified xpath.
	 * 
	 * @param xpath
	 * @param attrValue
	 */
	public void setValuesAttr(String xpath, String attrValue) {
		String xpathBuff = removeRootElementFromXPath(xpath);
		XMLUtil.putValue(targetNode,xpathBuff + "/@values"  , attrValue);
	}
	
	/**
	 * Set values attrbute value of the specified xpath.
	 * 
	 * @param xpath
	 * @param attrValue
	 */
	public void setValuesAttr(Node node, String attrValue) {
		
		if( attrValue != null)
			XMLUtil.putValue(node,"@values"  , attrValue);
	}
	public void setMandatoryAttr(Node node,String attValue){
		
		if(attValue != null)
			XMLUtil.putValue(node,"@mandatory",attValue);
		
	}
	public void setAlliasAttr(Node node,String attValue){
		
		if(attValue != null)
			XMLUtil.putValue(node,"@allowAllias",attValue);
		
	}
	
	/**
	 * Set id attribute of the specified xpath.
	 * 
	 * @param xpath
	 * @param attrValue
	 */
	public void setIdAttr(String xpath, String attrValue) {
		String xpathBuff = removeRootElementFromXPath(xpath);
		XMLUtil.putValue(targetNode,xpathBuff + "/@id"  , attrValue);
	}
	
	
	/**
	 * Returns newly created element.
	 *  
	 * @param elementName
	 * @param xpath
	 */
	
	public Node addElement(String elementName, String xpath,String values) {
		
		String xpathBuff = removeRootElementFromXPath(xpath);
		Node newElement = createNewSchmeaElement(elementName);
		
		if(newElement != null) {
			//update vlaues information
			setValuesAttr(newElement,values);
			Node parentNode = XMLUtil.getNode(targetNode,xpathBuff);
			return parentNode.appendChild(newElement);
		}
		return null;
	}
	
public int  replaceElemtntest(String xpath, String name,String values,String mandatory,String allowAllias,String datatype,String displayorder) {
		
			Document doc = targetNode.getOwnerDocument();
	//	System.out.println("***************************************************");	
				System.out.println("xPath "+xpath);
				System.out.println("newname "+name);
				System.out.println("displayOrder"+displayorder);

				String uxpath = xpath.substring(0, xpath.lastIndexOf("/"));
				String pname = xpath.substring(	xpath.lastIndexOf("/") + 1, xpath.length());	
		
		//		System.out.println("uxpath" +uxpath);
		//		System.out.println("pname "+pname);
				Node node =	XMLUtil.getNode(targetNode,pname);
				if(node != null){
					targetNode.removeChild(node);
		//			System.out.println("Element to be removed At path "+uxpath+"nodeName"+node.getNodeName());
		//			System.out.println("Element to be inserted"+name);
				}
			
				
	 // System.out.println("***************************************************");	
				
			return 1;
}	



public Node replaceElement(String elementName, String xpath,String values,String mandatory,String allowAllias,String datatype,String displayorder) {
		
		String xpathBuff = removeRootElementFromXPath(xpath,true);
		
		System.out.println("*************************");
		System.out.println(xpathBuff);
		System.out.println(xpath);
		System.out.println(XMLUtil.getNode(targetNode,xpathBuff).getNodeName());
		
		System.out.println("*************************");
		Node node3 = XMLUtil.getNode(targetNode,xpathBuff);
	
		System.out.println(node3.getParentNode().getNodeName());
		
		if ( Integer.parseInt(displayorder) <= 0 )
			return targetNode;
		
		
		String value = XMLUtil.getValue(node3,"@displayOrder");	
		if( value.equalsIgnoreCase(displayorder) && node3.getNodeName().equalsIgnoreCase(elementName)){
			
			XMLUtil.putValue(node3,"@mandatory",mandatory);
			XMLUtil.putValue(node3,"@values",values);
			XMLUtil.putValue(node3,"@dataType",datatype);
			XMLUtil.putValue(node3,"@allowAllias",allowAllias);
			return targetNode;
		}
		
	
				
		Document doc = targetNode.getOwnerDocument();
		Element ele = doc.createElement(elementName);
	    Attr val = doc.createAttribute("values");
	    val.setNodeValue(values);
	    ele.setAttributeNode(val);
	
	    Attr disp = doc.createAttribute("displayOrder");
	    disp.setNodeValue(displayorder);
		ele.setAttributeNode(disp);
	    Attr mand = doc.createAttribute("mandatory");
	    mand.setNodeValue(mandatory);
	    ele.setAttributeNode(mand);
	    Attr allow = doc.createAttribute("allowAllias");
	    allow.setNodeValue(allowAllias);
		ele.setAttributeNode(allow);
	    Attr datty = doc.createAttribute("dataType");
	    datty.setNodeValue(datatype);
	    ele.setAttributeNode(datty);
		
		if(node3.getParentNode().getNodeName().equals("Product")){
		
		if( node3.hasChildNodes()){
			NodeList childList = node3.getChildNodes();
			int childSize = childList.getLength();
			for ( int i =0 ;i<childSize;i++){
				ele.appendChild(childList.item(i));
			}
		}
			
		NodeList list =  targetNode.getChildNodes();
	    int size = list.getLength();
	    if( !elementName.equals(node3.getNodeName())){
	    for(int i=0;i<size;i++){
	    	Node dupNode = list.item(i);
	    	if(dupNode.getNodeName().equals(elementName)){
	    		return targetNode;
	    	}
			
	    }
	    }
	    int dOrder = Integer.parseInt(displayorder);
	    
		if( dOrder > size){
		
			
		//	System.out.println("DisplayOrder "+dOrder+" is greater than "+size);
			return targetNode;
		}else if( dOrder == size){
		     targetNode.removeChild(node3);
			 deleteNode(targetNode);
			 targetNode.appendChild(ele);
			 deleteNode(targetNode);
			 return targetNode;
		}
	   list =  targetNode.getChildNodes();
	   size = list.getLength();
	   String  dorder = XMLUtil.getValue(node3,"@displayOrder");
	   int j = Integer.parseInt(dorder);
	   int k = Integer.parseInt(displayorder);
	   
	   for ( int i=0;i<size;i++){
	    	Node temp = list.item(i);
	    	String tempval = XMLUtil.getValue(temp,"@displayOrder");
	    	
	    	if(tempval.equalsIgnoreCase(displayorder)){
	    		if(j > k){
	    		targetNode.insertBefore(ele,list.item(i));
	    		}else{
	    			targetNode.insertBefore(ele,list.item(i+1));
	    		}
	    	}
	    	
	    }
	    targetNode.removeChild(node3);
		
	    deleteNode(targetNode);
	     return targetNode;
		}else{
			
			
			/*	Node innerNode = XMLUtil.getNode(targetNode,xpathBuff);
				Node parentNode = innerNode.getParentNode();
				NodeList innerlist = parentNode.getChildNodes();
				int size = innerlist.getLength();
				for(int i=0;i<size;i++){
					Node temp = innerlist.item(i);
					if(temp.getNodeName().equalsIgnoreCase(elementName))
						return targetNode;
				}
			 	xpathBuff = xpathBuff.substring(xpathBuff.lastIndexOf("/")+1,xpathBuff.length());
			 	parentNode.removeChild(innerNode);
			
			 	
				XMLUtil.putNode(targetNode,xpathBuff,ele);
				*/
			
			Node innerNode = XMLUtil.getNode(targetNode,xpathBuff);
			String str = XMLUtil.getValue(innerNode,"@displayOrder");
			this.deleteElement(xpath,innerNode.getNodeName(),str);
			
			int index = xpath.lastIndexOf("/");
			xpath = xpath.substring(0,index);
		//	System.out.println("******************* "+xpath);
			index=xpathBuff.lastIndexOf("/");
			xpathBuff=xpathBuff.substring(0,index+1);
			xpathBuff=xpathBuff+elementName;
		//	System.out.println("*******xpathBuff "+xpathBuff);
			
			this.addElement(elementName,xpath,values, mandatory,allowAllias,datatype,str);
		
		//	System.out.println("showing the targetNode Here");
			
		//	System.out.println(" Node is "+XMLUtil.convertToString(targetNode));
			
			
			
			xpath=xpath+"/"+elementName;
			if ( innerNode.hasChildNodes()){
				changeNode(innerNode,str);
				 NodeList list1 = innerNode.getChildNodes();
				 int size1 = list1.getLength();
				 for(int p=0;p<size1;p++){
				   	XMLUtil.putNode(targetNode,xpathBuff,list1.item(p));
				   }
			}
				
			//deleteNode(targetNode);
			
			return targetNode;
			
		}
		
	}
	






public Node addElement(String elementName, String xpath,String values,String mandatory,String allowAlias,String datatype,String displayorder) {
	
	
	//System.out.println(" This is the xpath "+xpath);
	String xpathBuff = removeRootElementFromXPath(xpath,true);
	
	boolean flag=true;
	int max = numOfNodes();
	int display =  new Integer(displayorder).intValue();
	Node newElement =null; 
	if (display > max ){
		display = max + 1;
		newElement=createNewSchmeaElement(elementName,mandatory,allowAlias,values,datatype,Integer.toString(display));
		XMLUtil.putNode(targetNode,xpathBuff,newElement);
		deleteNode(targetNode);
		return targetNode;
	
	}else 
		if( max == 0){
		display=1;
	}
	newElement=createNewSchmeaElement(elementName,mandatory,allowAlias,values,datatype,Integer.toString(display));
	
	Node addNode = XMLUtil.getNode(targetNode,xpathBuff);
	if(addNode == null)
	System.out.println("");
	if(!xpathBuff.equals(".")){
	if(addNode != null){
			flag=false;
		
			if(addNode.getParentNode().getNodeName().equals("Product")){
					String str = XMLUtil.getValue(addNode,"@displayOrder");
					XMLUtil.putValue(newElement,"@displayOrder",str);
				//	System.out.println("xpathBuff ="+xpathBuff);
				//	System.out.println("new element "+newElement.getNodeName());
					XMLUtil.putNode(targetNode,xpathBuff,newElement);
					
			}else{
					String str = XMLUtil.getValue(addNode,"@displayOrder");
					XMLUtil.putValue(newElement,"@displayOrder",str);
					XMLUtil.putNode(targetNode,xpathBuff,newElement);
					
				}
		
	
		
	}
	}/*else{
		flag=false;
		System.out.println("ididicame here ");
		XMLUtil.putNode(targetNode,".",newElement);
	}*/
		if(flag)
		this.findNodetoInsert(newElement,display,xpath);
		
		return targetNode;

	
	

	
	
	
	
}

public Node deleteElement( String xpath,String eleName,String displayorder ){
	
	String xpathBuff = removeRootElementFromXPath(xpath,true);
	//System.out.println("Delete xpath Buff"+xpathBuff);
	Node node = XMLUtil.getNode(targetNode,xpathBuff);
	if(!xpathBuff.equals(".")){
	if ( node != null){
	//	System.out.println("Parent ="+ node.getParentNode().getNodeName());
	//	System.out.println("node name arunkumar" +node.getNodeName());
	}
	if (!node.getParentNode().getNodeName().equals("Product")){
			
			Node parantNode = node.getParentNode();
			parantNode.removeChild(node);
	}
	else{
			Node deleteNode = XMLUtil.getNode(targetNode,xpathBuff);
			if (deleteNode != null){
			if ( displayorder.equals(XMLUtil.getValue(deleteNode,"@displayOrder"))){
			Node parantNode = deleteNode.getParentNode();
			parantNode.removeChild(deleteNode);
			if(parantNode.getNodeName().equals("Product"));
				this.deleteNode(parantNode);
	
			}
			}
	}
	}else{
		
		
	
	}
	
	return targetNode;
	
	
}	
	
public Node deleteNode(Node node){
	
	NodeList list = node.getChildNodes();
	int size = list.getLength();
	for(int k=0;k<size;k++){
		Node node1 = list.item(k);
		XMLUtil.putValue(node1,"@displayOrder",Integer.toString(k+1));
		if(node1.hasChildNodes()){
			ord=k+1; 
			displayOrder(node1);
		}
	}
	
	
	return node;
}





	
public boolean findNodetoInsert(Node ele,int display,String xpath){
	
	
	
	
	Document doc = targetNode.getOwnerDocument();
	NodeList list = targetNode.getChildNodes();
		int listSize = list.getLength();
		
		if(listSize == 0)
			XMLUtil.putNode(targetNode,".",ele);
		for ( int i =0 ;i < listSize ; i ++){
			int order = Integer.parseInt(XMLUtil.getValue(list.item(i),"@displayOrder"));
			if ( order == display ){
					Node node3 = list.item(i);
				    targetNode.insertBefore(ele,node3);
				    for (i=i+1;i<=listSize;i++){
				    	
				    	 XMLUtil.putValue(list.item(i),"@displayOrder",Integer.toString(i+1)); 
				    	if (list.item(i).hasChildNodes()){
				    		ord=i+1;
				    		displayOrder(list.item(i));
				    	}
				    }
			
			 }
			
		}
	//	System.out.println("*****************************This is out put****************");
		
		System.out.println(XMLUtil.convertToString(targetNode));
		
	//	System.out.println("*****************************This is End****************");
		
		
		
		
	return true;
}

public static Node displayOrder(Node node){
	
			if ( node.hasChildNodes()){
				NodeList list = node.getChildNodes();
				int siz = list.getLength();
				for(int i=0;i<siz;i++){
					displayOrder(list.item(i));
				}
			}
			XMLUtil.putValue(node,"@displayOrder",Integer.toString(ord));
			return node;
	
}
	
	/*
	
	public Node addElement(String elementName, String xpath,String values,String mandatory,String allowAlias) {
		
		String xpathBuff = removeRootElementFromXPath(xpath);
		Node newElement = createNewSchmeaElement(elementName,mandatory,allowAlias,values);
		
		if(newElement != null) {
			//update vlaues information
			setValuesAttr(newElement,values);
			Node parentNode = XMLUtil.getNode(targetNode,xpathBuff);
			return parentNode.appendChild(newElement);
		}
		return null;
	}
	*/
public  Node createNewSchmeaElement(String elementNameSrc,String manVal,String alloVal,String values,String datatype,String displayorder) {
		
		if(elementNameSrc != null && !elementNameSrc.trim().equalsIgnoreCase(""))  {
		//	count
			//replace all spaces with empty strings
			String elementName = elementNameSrc.replaceAll(" ","");
			Document rootDocument = targetNode.getOwnerDocument();
			Element newElement = rootDocument.createElement(elementName);
			count = count +1;
			Attr idAttr = rootDocument.createAttribute("id");
			newElement.setAttributeNode(idAttr);
			Attr valuesAttr = rootDocument.createAttribute("values");
			valuesAttr.setValue(values);
			newElement.setAttributeNode(valuesAttr);
		
			Attr displayNameAttr = rootDocument.createAttribute("displayName");
			newElement.setAttributeNode(displayNameAttr);
	// I added this code
			Attr mandatory = rootDocument.createAttribute("mandatory");
	//		System.out.println( "Values are "+manVal+" :"+alloVal+":");
			mandatory.setValue(manVal);
			newElement.setAttributeNode(mandatory);
			
			Attr allowAllias = rootDocument.createAttribute("allowAllias");
			allowAllias.setValue(alloVal);
			newElement.setAttributeNode(allowAllias);
			
			Attr dataType = rootDocument.createAttribute("dataType");
			dataType.setValue(datatype);
			newElement.setAttributeNode(dataType);
			
			Attr displayOrder = rootDocument.createAttribute("displayOrder");
			displayOrder.setValue(displayorder);
			newElement.setAttributeNode(displayOrder);
			
			/*	Attr mandatoryAttr = rootDocument.createAttribute("mandatory");
			newElement.setAttributeNode(mandatoryAttr);
			*/
			
			
			
			
			return newElement;
			
		}else {
			return null;
		}
		
	}

	
	
	
	/**
	 * Rename speciifed xpath element with specified name.
	 * 
	 * @param xpath
	 */
	public Node replaceElement(String elementName, String xpath,String values) {
		
		String xpathBuff = removeRootElementFromXPath(xpath);
		
		Node newElement = createNewSchmeaElement(elementName);
		
		Node oldNode = XMLUtil.getNode(targetNode,xpathBuff);
		if(newElement != null ) {
		//	System.out.println(" Updated Node xpath :" + xpathBuff);
			XMLUtil.putValue(newElement,"@id", XMLUtil.getValue(oldNode,"@id"));
			XMLUtil.putValue(newElement,"@displayName", XMLUtil.getValue(oldNode,"@displayName"));
			XMLUtil.putValue(newElement,"@values", XMLUtil.getValue(oldNode,"@values"));
			XMLUtil.putValue(newElement,"@dataType", XMLUtil.getValue(oldNode,"@dataType"));
			XMLUtil.putValue(newElement,"@displayOrder", XMLUtil.getValue(oldNode,"@displayOrder"));
					
			if(oldNode.hasChildNodes()) {
				List list = XMLUtil.executeQuery(oldNode,"*");
				System.out.println(" No. of Childs " + list.size());
				for(int i=0; i<list.size(); i++) {
					newElement.appendChild((Node)list.get(i));
				}
			}
			
			//update vlaues information
			setValuesAttr(newElement,values);
			Node parentNode = oldNode.getParentNode();
			return parentNode.replaceChild(newElement,oldNode);
			
		} else {
			//update vlaues information with old node
			setValuesAttr(oldNode,values);
			return null;
		}
		
	}
	
	public Node replaceElement(String elementName, String xpath,String values,String mandatory,String allowAllias) {
		
		String xpathBuff = removeRootElementFromXPath(xpath);
		
		Node newElement = createNewSchmeaElement(elementName);
		
		Node oldNode = XMLUtil.getNode(targetNode,xpathBuff);
		if(newElement != null ) {
		//	System.out.println(" Updated Node xpath :" + xpathBuff);
			XMLUtil.putValue(newElement,"@id", XMLUtil.getValue(oldNode,"@id"));
			XMLUtil.putValue(newElement,"@displayName", XMLUtil.getValue(oldNode,"@displayName"));
			XMLUtil.putValue(newElement,"@values", values);
			XMLUtil.putValue(newElement,"@mandatory",mandatory);
			XMLUtil.putValue(newElement,"@allowAllias",allowAllias);
			if(oldNode.hasChildNodes()) {
				List list = XMLUtil.executeQuery(oldNode,"*");
		//		System.out.println(" No. of Childs " + list.size());
				for(int i=0; i<list.size(); i++) {
					newElement.appendChild((Node)list.get(i));
				}
			}
			
			//update vlaues information
			setValuesAttr(newElement,values);
			Node parentNode = oldNode.getParentNode();
			return parentNode.replaceChild(newElement,oldNode);
			
		} else {
			//update vlaues information with old node
			setValuesAttr(oldNode,values);
			return null;
		}
		
	}
	
	
	
	
	
	private Node createNewSchmeaElement(String elementNameSrc) {
		
		if(elementNameSrc != null && !elementNameSrc.trim().equalsIgnoreCase(""))  {
			
			//replace all spaces with empty strings
			String elementName = elementNameSrc.replaceAll(" ","");
			Document rootDocument = targetNode.getOwnerDocument();
			Element newElement = rootDocument.createElement(elementName);
			
			Attr idAttr = rootDocument.createAttribute("id");
			newElement.setAttributeNode(idAttr);
			Attr valuesAttr = rootDocument.createAttribute("values");
			newElement.setAttributeNode(valuesAttr);
		
			Attr displayNameAttr = rootDocument.createAttribute("displayName");
			newElement.setAttributeNode(displayNameAttr);
	// I added this code
			Attr mandatory = rootDocument.createAttribute("mandatory");
			mandatory.setValue("true");
			newElement.setAttributeNode(mandatory);
			
			Attr allowAllias = rootDocument.createAttribute("allowAllias");
			allowAllias.setValue("true");
			newElement.setAttributeNode(allowAllias);
			
			Attr dataType = rootDocument.createAttribute("dataType");
			dataType.setValue("string");
			newElement.setAttributeNode(dataType);
			
			Attr displayOrder = rootDocument.createAttribute("displayOrder");
	//		System.out.println(" count value is" + count);
			displayOrder.setValue(new Integer(count).toString());
			newElement.setAttributeNode(displayOrder);
			
			/*	Attr mandatoryAttr = rootDocument.createAttribute("mandatory");
			newElement.setAttributeNode(mandatoryAttr);
			*/
			
			
			
			
			return newElement;
			
		}else {
			return null;
		}
		
	}
	
	
	private Node createNewSchmeaElement(String elementNameSrc,String manVal,String alloVal,String values) {
		
		if(elementNameSrc != null && !elementNameSrc.trim().equalsIgnoreCase(""))  {
			
			//replace all spaces with empty strings
			String elementName = elementNameSrc.replaceAll(" ","");
			Document rootDocument = targetNode.getOwnerDocument();
			
			
			
			Element newElement = rootDocument.createElement(elementName);
		//	count = count +1;
			Attr idAttr = rootDocument.createAttribute("id");
			newElement.setAttributeNode(idAttr);
			Attr valuesAttr = rootDocument.createAttribute("values");
			newElement.setAttributeNode(valuesAttr);
		
			Attr displayNameAttr = rootDocument.createAttribute("displayName");
			newElement.setAttributeNode(displayNameAttr);
	// I added this code
			Attr mandatory = rootDocument.createAttribute("mandatory");
	//		System.out.println( "Values are "+manVal+" :"+alloVal+":");
			mandatory.setValue(manVal);
			newElement.setAttributeNode(mandatory);
			
			Attr allowAllias = rootDocument.createAttribute("allowAllias");
			allowAllias.setValue(alloVal);
			newElement.setAttributeNode(allowAllias);
			
			Attr dataType = rootDocument.createAttribute("dataType");
			dataType.setValue("string");
			newElement.setAttributeNode(dataType);
			
			Attr displayOrder = rootDocument.createAttribute("displayOrder");
			System.out.println("count value is sis is "+count);
			displayOrder.setValue(new Integer(count).toString());
			newElement.setAttributeNode(displayOrder);
			
			/*	Attr mandatoryAttr = rootDocument.createAttribute("mandatory");
			newElement.setAttributeNode(mandatoryAttr);
			*/
			
			
			
			
			return newElement;
			
		}else {
			return null;
		}
		
	}
	
	
	
	
	
	
	
	
	public boolean valuesAttrHasData(String xpath) {
		String values = XMLUtil.getValue(targetNode,xpath + "/@values");
		if(values != null && !values.trim().equals("")) {
			return true;
		} 
		return false;
	}
	
	public String getValues(String xpath) {
		return XMLUtil.getValue(targetNode,xpath + "/@values");
	}
	
	/**
	 * Returns true if specified xpath contains specified value.
	 * 
	 * @param xpath
	 * @param value
	 * @return boolean
	 */
	public boolean containsValue(String xpath, String value) {
				
		if(value == null )
			return true;
		
		
		
		//if list of values contains 
		String finalXpath = "contains("+ xpath + "/@values,'" + value+ ";')";
		String result = XMLUtil.getValue(targetNode,finalXpath);
		if(result != null) {
			if(result.equalsIgnoreCase("true")) {
				return true;
			} 
		}
			
		return false;
		
	}
	
	
	/**
	 * Deletes element from the specified path
	 * 
	 * @param xpath
	 * @return
	 */
	public Node deleteElement(String xpath) {
		String xpathBuff = removeRootElementFromXPath(xpath);
		Node deleteNode = XMLUtil.getNode(targetNode,xpathBuff);
		Node parantNode = deleteNode.getParentNode();
				
		return parantNode.removeChild(deleteNode);
	}
	
	public void insertAttribute(String elementName,String attributeName,String value){
		
	//   System.out.println("**********************************");
	//	System.out.println("node name"+XMLUtil.getNode(targetNode,"amount").getNodeName());
	//  System.out.println(elementName);
	//   System.out.println( XMLUtil.getNode(targetNode,elementName).getNodeName());
	   /* 	Document rootDocument = targetNode.getOwnerDocument();
		Element ele =(Element) XMLUtil.getNode(targetNode,elementName);
		Attr idAttr = rootDocument.createAttribute(attributeName);
		idAttr.setValue(value);
		ele.setAttributeNode(idAttr);
		
		*/
	//	System.out.println("**********************************");
	}
	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args)  {
		
	
		String xmlStr44 = "<payload><amount id=\"dddd\" ><due>  <xyz> <abc> </abc> </xyz> </due> </amount> <let1> </let1> <let> </let> </payload>";
		String xmlSter44 = "<payload><amount id=\"dddd\"  /> <let1> </let1> <let> </let> </payload>";
		String xmlStr = "<payload> </payload>";
		
		String xmlStr1 = "<Schema> <payload><amount displayName=\"\" id=\"\" values=\"\"/>	<let displayName=\"\" id=\"\" values=\"rtz;abc;\">	<abc displayName=\"\" id=\"\" values=\"pqz;sss;\"> <TQR> </TQR> </abc> </let> <let333 displayName=\"\" id=\"\" values=\"\"/></payload> </Schema>";
	//	System.out.println(" xmlStr :" + xmlStr1);
		
		Node node =  XMLUtil.parse(xmlStr1);
		
		String xpath = "amount";
		
	/*	String value = ";xyz;";
		String finalXpath = "contains("+ xpath + "/@values,'" + value+ "')";
		
		String values = XMLUtil.getValue(node,finalXpath);
		
		System.out.println(" Value :" + values);*/
		
			
		SchemaTree tree =  new SchemaTree(XMLUtil.getNode(node,"payload"));
/*		
		tree.addElement("amount","payload");
		
		tree.addElement("let","payload");
		
		tree.addElement("let1","payload");
		
		tree.addElement("let1222","payload/let1");*/
		tree.insertAttribute("amount","mandatory","On");
		
		tree.replaceElement("let100","payload/let","xyz;pqerr;");
		
		//tree.deleteElement("payload/let");
		
	//	System.out.println(" string :" + XMLUtil.convertToString(tree.getNode()) );
		
	}
	
	
	
	public static Element myfunction(Element ele){
		
		Document doc =ele.getOwnerDocument();
		Attr  id=doc.createAttribute("id");
		Attr  allowAllias= doc.createAttribute("allowAllias");
		Attr  mandatory = doc.createAttribute("mandatory");
		Attr  displayOrder = doc.createAttribute("displayOrder");
		Attr  dataType = doc.createAttribute("dataType");
		Attr  values  = doc.createAttribute("values");
		ele.setAttributeNode(id);
		ele.setAttributeNode(allowAllias);
		ele.setAttributeNode(mandatory);
		ele.setAttributeNode(displayOrder);
		ele.setAttributeNode(dataType);
		ele.setAttributeNode(values);
		Node  node1 =  ele.getParentNode();
		if(node1 != null){
			if(!node1.getNodeName().equals("Product")){
				String dorder= XMLUtil.getValue(node1,"@displayOrder");
				if(dorder == null){ dorder =""; }
				ele.setAttribute("displayOrder",dorder);
			
			}else{
				String temp=""+i++;
				ele.setAttribute("displayOrder",temp);
			}
		}
		if(ele.hasChildNodes()){
			NodeList list = ele.getChildNodes();
			int size =list.getLength();
			for(int i=0;i<size;i++){
				Element node=(Element) list.item(i);
				myfunction( node);
			}
		}
		return ele;
	}
	
	
	static int i=1;
	public static boolean removeNode(String name,Document doc){

		Element ele = (Element )doc.getFirstChild();
	//	if( ele == null){ System.out.println(" Node is null"); }else  System.out.println(node.getNodeName());
		Node node = XMLUtil.getNode(ele,name);
		i=1;
		ele.removeChild(node);
		myfunction(ele);
		return true;
	
	}
	

	
	public  static boolean updateNode(String oldname,String newname,String path,String order,Document  ele){
		
		
			Element  node = (Element )ele.getFirstChild();
			int size = (node.getChildNodes()).getLength();
		//  remove ing the node to be updated
			if(Integer.parseInt(order)> size){
				 return false;
			}
			
			removeNode(oldname,ele);
		//  identifying the size of tree
			 size = (node.getChildNodes()).getLength();
		// checking display order is greater than the total elements	
			if ( Integer.parseInt(order)> size){
					Element element = creatnewElement(newname,order,ele);
					node.appendChild(element);
					 i=1;
					myfunction(node);
			}else{
				Element element = creatnewElement(newname,order,ele);
				insertNodetest(ele,"Product",element,Integer.parseInt(order));
				i=1;
				myfunction(node);
			}
			
		return true;
	}
	public static Element creatnewElement(String elementName,String order,Document  doc){
		//Document doc = elem.getOwnerDocument();
		Element newele = doc.createElement(elementName);
		Attr attr = doc.createAttribute("displayOrder");
		attr.setNodeValue(order);
		newele.setAttributeNode(attr);
		return newele;
	}
	
	
	public static String xpath(String xpath){
		
		StringBuffer xpathBuff = new StringBuffer(xpath);
		int firstIndex = xpathBuff.indexOf("/");
		if(firstIndex > 0 ) {
			xpathBuff.delete(0,firstIndex+1);
		} else {
			//incase of parent node return current node itself
			xpathBuff = new StringBuffer(".");
		}
		
		return xpathBuff.toString();
		
	}
	
	
	
	public static boolean insertNodetest(Document doc,String xpath,Element ele,int order){
		
			Element root = (Element )doc.getFirstChild();
		    String path = xpath(xpath);
			NodeList list =null;
			if ( path.equalsIgnoreCase(".")){
				list = root.getChildNodes();
			}else{
				Element subnode =(Element) XMLUtil.getNode(root,path);
				if(subnode == null){
					return false;
				}else{
					list = subnode.getChildNodes();
					
				}
					
				
			}
				
		
			int size = list.getLength();
			if( order > size){
				XMLUtil.putNode(root,".",ele);
			}else{
			
			for( int i=0; i<size;i++){
				if(i == order-1){
					root.insertBefore(ele,list.item(i));
				}
			}
					
		}	
		
		return true;
	}
	
	
	public static Node changeNode ( Node node3,String str){
		if ( node3.hasChildNodes()){
			 NodeList list = node3.getChildNodes();
			 int size = list.getLength();
			 for(int k = 0;k<size;k++){
			 		changeNode(list.item(k),str);
			 }
			 
		}else{
			
			XMLUtil.putValue(node3,"@displayOrder",str);
		}
		
		
		return node3;
	}
	
}
