package com.java.xmlparsers;

import javax.xml.parsers.*;
import org.w3c.dom.*;

public class Parse {
  
  public static void extract(Document doc) {

	  Element root = doc.getDocumentElement();
	  System.out.println("The root element is "+root.getNodeName());
	  
	  NodeList flowers=root.getElementsByTagName("Flower");
              
    
    for (int i = 0; i < flowers.getLength(); i++) {
      
        Element flower = (Element) flowers.item(i);
        System.out.println("Sub child:"+flower.getNodeName());
        System.out.println(flower.getAttribute("national"));
                
        NodeList names = flower.getElementsByTagName("name");
        Element name = (Element) names.item(0);
       
        System.out.println("Name  "+names.item(0).getFirstChild().getNodeValue());
       
        
      
        System.out.println("properly name:"+name.getNodeName());
        NodeList colors = flower.getElementsByTagName("color");
        Element color = (Element) colors.item(0);
        System.out.println("Name  "+colors.item(0).getFirstChild().getNodeValue());
        System.out.println("property color:"+color.getNodeName());
             
       
        
        
      } 

  }
  
  
  
  public static void main(String[] args) {

   try {
      
	   DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
       
        DocumentBuilder parser = factory.newDocumentBuilder();
           
        Document document = parser.parse(Parse.class.getResource("sing.xml").getPath());
     
        extract(document);

    }
    catch (Exception e) {
      System.out.println(e);
    }
        
  } 
}


