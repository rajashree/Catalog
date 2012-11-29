package com.java.xmlparsers;

import java.math.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import org.apache.xml.serialize.*;


public class XMLCreator1 {


	public static void main(String[] args){
		try{
			DocumentBuilderFactory factory =DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder =factory.newDocumentBuilder();
			DOMImplementation impl = builder.getDOMImplementation();
			Document document=impl.createDocument(null,"Fibonacci_Numbers",null);
			BigInteger low =BigInteger.ONE;
			BigInteger high = BigInteger.ONE;
			Element root = document.getDocumentElement();
			
			for (int i=0;i<100;i++){
				Element number = document.createElement("fibonacci");
				Text test = document.createTextNode(low.toString());
				number.appendChild(test);
				root.appendChild(number);
				BigInteger temp = high;
				high =high.add(low);
				low =temp;
				
				
			}
		
			OutputFormat format = new OutputFormat(document);
			format.setLineWidth(65);
			format.setIndenting(true);
			format.setIndent(2); 
			
			File dir = new File("C:/examples/");
			dir.mkdirs();
			File file = new File(dir,"Exampledoc.xml");
			System.out.println(file);
			FileOutputStream fout=new FileOutputStream(file);
			Writer out = new OutputStreamWriter(fout,"UTF-8");
			out = new BufferedWriter(out);
			XMLSerializer serializer = new XMLSerializer(out,format);
			serializer.serialize(document);
			
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