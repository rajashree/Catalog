package com.java.xmlparsers;

import javax.xml.parsers.*;

import org.w3c.dom.*;

public class Parse1 {

	/**
	 * @param doc
	 */
	public static void extract(Document doc){
	
		Element root = doc.getDocumentElement();
		System.out.println("The root element "+root.getNodeName());
		NodeList flowers = root.getElementsByTagName("Flower");
		for(int i=0;i<flowers.getLength();i++){
			Element flower = (Element) flowers.item(i);
			System.out.println("Sub Child name is "+flower.getNodeName());
			System.out.println("The attribute is"+flower.getAttribute("national"));
			
			NodeList names = flower.getElementsByTagName("name");
			Element name = (Element) names.item(0);
			System.out.println("The property name is"+name.getNodeName());
			System.out.println("The property value is"+names.item(0).getFirstChild().getNodeValue());
			NodeList colors = flower.getElementsByTagName("color");
			Element color = (Element) colors.item(0);
			System.out.println("The property name is "+color.getNodeName());
			System.out.println("The property value is "+colors.item(0).getFirstChild().getNodeValue());
			
		}
	}
	
	public static void main(String args[]) throws Exception{
		
		System.out.println("Inside main");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db=dbf.newDocumentBuilder();
		Document doc =  db.parse(Parse1.class.getResource("sing.xml").getPath()) ;
		extract(doc);
	}
	}
	
	