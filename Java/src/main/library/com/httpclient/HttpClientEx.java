package com.httpclient;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;



public class HttpClientEx {
	public String getBookmarkxml() {
//STaging
	//	String targetURL ="http://staging.athinline.org/overtheline/sessions";
	//	String targetURL = "http://staging.athinline.org/overtheline/stories";
	//	String targetURL = "http://staging.athinline.org/overtheline/stories/5/comments";
	//	String targetURL = "http://staging.athinline.org/overtheline/stories/5/rate/";
		
		//PROD
		
		String response = null;
	//String targetURL ="http://www.athinline.org/overtheline/sessions";
	//String targetURL = "http://www.athinline.org/overtheline/stories/10615/rate";	
	String targetURL = "http://www.athinline.org/overtheline/stories?page=1&feed=stories&sort=recent&u=292918";
		
		
		HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams().setConnectionTimeout(
					5000);
		GetMethod userGet = new GetMethod(targetURL);
		try {
			userGet.getParams().setBooleanParameter(
					HttpMethodParams.USE_EXPECT_CONTINUE, true);
			//client.getState().setCredentials(new AuthScope("sourcen.grouphub.com",443),new UsernamePasswordCredentials("devashree.meganathan", "sniplpass"));
			//userGet.setDoAuthentication(true);
			int status = client.executeMethod(userGet);
			if (status == HttpStatus.SC_OK) {
				response = userGet.getResponseBodyAsString();
			} else {
				System.out.println("SOME ERROR");
			}
		}catch(ConnectTimeoutException timeout){
			System.err.println("connection time out==> get new connection");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
		return response;
	}
	public static void main(String args[]){
		HttpClientEx obj = new HttpClientEx();
		System.out.println(":::::::::obj.getBookmarkxml():::::::::"+obj.getBookmarkxml());

	/*	ArrayList<String> groups = new ArrayList<String>();
		SAXReader reader = new SAXReader();
        try {
			Document document = reader.read("http://romewebt.apple.com/cgi-bin/WebObjects/AgentApp.woa/wa/processRequest");
	        Element root = document.getRootElement();
	        System.out.println(document.asXML());
	        for ( Iterator i = root.elementIterator(); i.hasNext(); ) {
	            Element element = (Element) i.next();
	            if(element.getName().equalsIgnoreCase("ACK"))
		            if(element.getText().equalsIgnoreCase("YES"))
		            {
		            	
		            	for ( Iterator i2 = root.elementIterator("AUDIENCES"); i2.hasNext(); ) {
		     	           Element audiences = (Element) i2.next();
		     	           for ( Iterator i3 = audiences.elementIterator("AUDIENCE"); i3.hasNext(); ) {
			     	           Element audience = (Element) i3.next();
			     	           if (audience.elementText("MEMBER").toString().equalsIgnoreCase("YES")) {
			     	        	   groups.add(audience.elementText("AUDIENCEID"));
			     	           }
			     	        }
		     	        }
		            }		
		            if(element.getText().equalsIgnoreCase("NO"))
		            {
		            	for ( Iterator i2 = root.elementIterator( "REASON" ); i2.hasNext(); ) {
		     	            Element failure = (Element) i2.next();
		     	            System.out.println(failure.asXML());
		     	        }
		            }		
		        
	        
	        }

	       Iterator it = groups.iterator();
	       while(it.hasNext()){
	    	   System.out.println(it.next().toString());
	       }
	       
	        System.out.println("SIZE "+groups.size());
	       
	        
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}
