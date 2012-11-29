package com.java.xmlparsers;
import java.io.*;

import javax.xml.parsers.*;

//import org.apache.xml.serialize.OutputFormat;
//import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.*;
import org.xml.sax.ErrorHandler;


public class domtest {
  
  public static void extract(Document doc,String s[]) {

	  Element root = doc.getDocumentElement();
	  System.out.println("The root element is "+root.getNodeName());
	  
	  NodeList flowers=root.getElementsByTagName("Flower");
        
   
	  for (int i = 0; i < flowers.getLength(); i++) {
      
        Element flower = (Element) flowers.item(i);
        String att=flower.getAttribute("national");
        System.out.println(att);

                 
        NodeList names = flower.getElementsByTagName("name");
        Element name = (Element) names.item(0);
        System.out.println("Name  "+names.item(0).getFirstChild().getNodeValue());
       
        
        NodeList colors = flower.getElementsByTagName("color");
        Element color = (Element) colors.item(0);
        System.out.println("Color  "+colors.item(0).getFirstChild().getNodeValue());
      //  modify(name,color,flower,i,s);

      } 

  }
  
 /* public static void modify(Element name,Element color,Element flower,int i,String s[])
  {
	  if(i==0)
        {
        	
        name.setTextContent(s[i]);     
        flower.appendChild(name);
       	
        color.setTextContent(s[i+1]);     
        flower.appendChild(color);
        }
        else
       	{
        	int j=i+i;
        	name.setTextContent(s[j]);     
            flower.appendChild(name);
                       
            color.setTextContent(s[j+1]);     
            flower.appendChild(color);
        	
        }
  }
  */
  public static void upload(Document doc) {
           
        //OutputFormat format = new OutputFormat(doc);
       // format.setLineWidth(65);
       // format.setIndenting(true);
       // format.setIndent(2);
        
        
        File dir = new File("examples2/");
        dir.mkdirs();
        File file = new File(dir, "sing2.xml");
        System.out.println(file);
        try
        {
        FileOutputStream fout = new FileOutputStream(file);
        Writer out = new OutputStreamWriter(fout, "UTF-8");
        out = new BufferedWriter(out);

        /*XMLSerializer serializer = new XMLSerializer(out, format);
       serializer.serialize(doc);*/
             out.close();
        }
        catch(Exception e){}
    }
   public static void main(String args[]) {

   try {
      
	   DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	   factory.setValidating( true );
       factory.setNamespaceAware( true );    
       
       DocumentBuilder parser = factory.newDocumentBuilder();
       ErrorHandler handler = null;
       parser.setErrorHandler( handler ); 
       
       Document document = parser.parse(domtest.class.getResource("sing.xml").getPath());
           
       extract(document,args);
       
       upload(document);
       
      }
      catch (Exception e) {
      System.out.println(e);
    }
        
  } 
}


