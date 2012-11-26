package com.snipl.ice.utility.contacts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class YahooContacts {

	public ArrayList  getYahooContacts(String uname,String passwd) {
		
		URL u;
		ArrayList al=null;
		try {
				u = new URL("http://localhost:90/icealert/Fetch_Contacts/yahoo/yahoo.php?username="+uname+"&password="+passwd+"&domain=yahoo.com");
				HttpURLConnection urlcon =
			          (HttpURLConnection) u.openConnection(  );
				urlcon.setRequestMethod("POST");
			      urlcon.setRequestProperty("Content-type",
			          "application/x-www-form-urlencoded");
			      urlcon.setDoOutput(true);
			      urlcon.setDoInput(true);
			          if ( urlcon.getResponseCode(  ) == HttpURLConnection.HTTP_OK )
			              {
				        	  InputStream in = urlcon.getInputStream(  );
						      
						      BufferedReader bf=new BufferedReader (
								        new InputStreamReader( in ));
							String line="",contacts_xml="";
						    while ( (line = bf.readLine(  )) != null )
						    	contacts_xml+=line;
					        Document doc = null;
							DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					        DocumentBuilder builder;    
					        builder = factory.newDocumentBuilder();
							doc=builder.parse(new InputSource(new StringReader(contacts_xml)));
							
							al=extractXml(doc);
						
			              }
			          return al;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return al;
	    

	}
	public static ArrayList extractXml(Document doc)
	{
		ArrayList name_al=null,email_al=null,list_al=null;
		Element root = doc.getDocumentElement();
		
		NodeList contact=root.getElementsByTagName("contact");
		if(contact.getLength()!=0)
		{
			name_al=new ArrayList();
			email_al=new ArrayList();
			for (int i = 0; i < contact.getLength(); i++)
			{
				
				Element contactList = (Element) contact.item(i);
				NodeList email=contactList.getElementsByTagName("email");
				if(!email.item(0).getFirstChild().getNodeValue().equalsIgnoreCase(" "))
				{
					email_al.add( email.item(0).getFirstChild().getNodeValue());
					NodeList name=contactList.getElementsByTagName("name");
					name_al.add( name.item(0).getFirstChild().getNodeValue());
				}
				
			}
			if(email_al.size()!=0)
			{
				list_al=new ArrayList();
				list_al.add(0, name_al);
				list_al.add(1, email_al);
			}
		}
		return list_al;
	}

}
