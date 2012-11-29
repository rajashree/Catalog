package com.java.xmlparsers;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.math.BigInteger;


public class XMLCreator {

  public static void main(String[] args) {

    try {

      
      DocumentBuilderFactory factory 
       = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      DocumentBuilder builder = factory.newDocumentBuilder();
      DOMImplementation impl = builder.getDOMImplementation();
            
      Document doc = impl.createDocument(null,"Fibonacci_Numbers", null);
       
      
      BigInteger low  = BigInteger.ONE;
      BigInteger high = BigInteger.ONE;

      Element root = doc.getDocumentElement();

      for (int i = 0; i < 10; i++) {
        Element number = doc.createElement("fibonacci");
        Text text = doc.createTextNode(low.toString());
        number.appendChild(text);
        root.appendChild(number);

        BigInteger temp = high;
        high = high.add(low);
        low = temp;
      }

      // Serialize the document
      OutputFormat format = new OutputFormat(doc);
      format.setLineWidth(65);
      format.setIndenting(true);
      format.setIndent(2);
      
      File dir = new File("C:/examples2/");
      dir.mkdirs();
      File file = new File(dir, "sing2.xml");
      System.out.println(file);
      FileOutputStream fout = new FileOutputStream(file);
      Writer out = new OutputStreamWriter(fout, "UTF-8");
      out = new BufferedWriter(out);

      XMLSerializer serializer = new XMLSerializer(out, format);
      
      serializer.serialize(doc);
      
    }
    catch (FactoryConfigurationError e) { 
      System.out.println("Could not locate a JAXP factory class"); 
    }
    catch (ParserConfigurationException e) { 
      System.out.println(
        "Could not locate a JAXP DocumentBuilder class"
      ); 
    }
    catch (DOMException e) {
      System.err.println(e); 
    }
    catch (IOException e) {
      System.err.println(e); 
    }
    
  }

}