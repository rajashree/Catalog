package com.curl;

import org.curl.CURL;
import org.curl.CurlGlue;
import org.curl.CurlWrite;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class flickr implements CurlWrite {
	static StringBuffer sbResponse = new StringBuffer(16384);
	 static String[] farm=null;
     static String[] server=null;
     static String[] id=null;
     static String[] secret=null;
	public int handleString(byte s[])
    {	sbResponse.append(new String(s));
        return 0;
    }
	public static String  userID(Document doc) {

		  Element root = doc.getDocumentElement();
		  NodeList users=root.getElementsByTagName("user");
		  Element user = (Element) users.item(0); 
		  String att=user.getAttribute("id");
		  return att;
		 
	} 
	public int df()
	{
		return 123;
	}
	public static String  getCount(Document doc) {

		  Element root = doc.getDocumentElement();
		  NodeList users=root.getElementsByTagName("photos");
		  Element user = (Element) users.item(0); 
		  String att=user.getAttribute("total");
		  return att;
		 
	}
	public static void  getPhotoList(Document doc) {

		  Element root = doc.getDocumentElement();
		  NodeList photos=root.getElementsByTagName("photos");
		  
		  Element user = (Element) photos.item(0);
		  NodeList photo = user.getElementsByTagName("photo");
		  
		  farm=new String[photo.getLength()];
	      server=new String[photo.getLength()];
	      id=new String[photo.getLength()];
	      secret=new String[photo.getLength()];
		 
		  for (int j = 0; j < photo.getLength(); j++) {
			Element p = (Element) photo.item(j);
			farm[j]=p.getAttribute("farm");
			server[j]=p.getAttribute("server");
			id[j]=p.getAttribute("id");
			secret[j]=p.getAttribute("secret");
	     } 
	      	 
	}
	public static Document simplexml(String sdoc) throws ParserConfigurationException, SAXException, IOException 
	{
		DocumentBuilderFactory factory =
		DocumentBuilderFactory.newInstance();
		DocumentBuilder db = factory.newDocumentBuilder();
		InputSource inStream = new InputSource();
		inStream.setCharacterStream(new StringReader(sdoc));
		Document doc1 = db.parse(inStream);
		return doc1;
	}
	public static List flickrPhotos()
	{
		List list=new ArrayList();
		CurlGlue cg=new CurlGlue();
		flickr cw=new flickr();
		String postfields="";
		String[] images;
		
		String url;
		String api_key = "048f15dc21e59c1396b4567b001a70b3";
		String username="snipl_harini_tirumuru";
		String method="flickr.people.findByUsername";

		int count = 0;
		try {
			 
			
			cg.setopt(CURL.OPT_WRITEFUNCTION, cw);
			cg.setopt(CURL.OPT_COOKIEJAR, "/tmp/cookiejar-$randnum");
        	cg.setopt(CURL.OPT_COOKIEFILE, "/tmp/cookiejar-$randnum");
        	cg.setopt(CURL.OPT_USERAGENT, "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.1) Gecko/20061204 Firefox/2.0.0.1");
        	cg.setopt(CURL.OPT_FOLLOWLOCATION, 1);
        	cg.setopt(CURL.OPT_POST, 0);
        	cg.setopt(CURL.OPT_URL,"http://flickr.com/");
        	cg.perform();
        	postfields =null;
        	
        	sbResponse = new StringBuffer(16384);
        	cg.setopt(CURL.OPT_URL,"http://api.flickr.com/services/rest/?");
        	cg.setopt(CURL.OPT_REFERER, "http://flickr.com/");
        	cg.setopt(CURL.OPT_POST, 1);
        	postfields = "api_key=" + java.net.URLEncoder.encode(api_key);
	  	    postfields += "&method=" + java.net.URLEncoder.encode(method);
	  		postfields += "&username="+username;
	  			
  			cg.setopt( CURL.OPT_POST, 1);
  			cg.setopt(CURL.OPT_POSTFIELDS,postfields);
  			cg.perform();
  			Document doc=simplexml(sbResponse.toString());
  			String user_id=userID(doc);
        	
  			
  			sbResponse = new StringBuffer(16384);
  			method="flickr.photos.search";
  			cg.setopt(CURL.OPT_URL,"http://api.flickr.com/services/rest/?");
        	cg.setopt(CURL.OPT_REFERER, "http://flickr.com/");
        	cg.setopt(CURL.OPT_POST, 1);
        	postfields = "method=" + java.net.URLEncoder.encode(method);
        	postfields += "&api_key=" + java.net.URLEncoder.encode(api_key);
        	postfields += "&user_id=" + java.net.URLEncoder.encode(user_id);
	  	   	
  			cg.setopt( CURL.OPT_POST, 1);
  			cg.setopt(CURL.OPT_POSTFIELDS,postfields);
  			cg.perform();
  			Document doc1=simplexml(sbResponse.toString());
  			int total_count=Integer.parseInt(getCount(doc1));
  			getPhotoList(doc1);
  			FlickrObject fo=new FlickrObject();
  			for(int i=0;i<total_count;i++)
  			{
  				fo.setPhotoURL("http://farm"+farm[i]+".static.flickr.com/"+server[i]+"/"+id[i]+"_"+secret[i]+"_m.jpg");
  				list.add(fo);
  			}	
  			
			
	    } catch (Exception e) {
	    		e.printStackTrace();
			}
		return list;
		
	 }
		
	
    public static void main(String[] args)
    {
    	Collection l=flickrPhotos();
    	Iterator i=l.iterator();
    	FlickrObject fo=new FlickrObject();
    	while(i.hasNext())
    	{
    		
    		fo=(FlickrObject) i.next();
    		System.out.println(fo.getPhotoURL());
    	}
    	
    }	
}