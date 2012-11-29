package com.java.xmlparsers;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class DOMNew {
  static public void main(String[] arg) {
    if (arg.length != 1) {
      System.err.println("Usage: DOMNew <outfile>");
      System.exit(1);
    }
    DOMNew dc = new DOMNew();
   
  }

  

  public void buildTree(Document doc) {
    
    
    Element root = doc.createElement("places");
    doc.appendChild(root);
   
    
    NodeList nodelist = null ;
    root.appendChild(nodelist.item(0));
   
   
    
  }
}