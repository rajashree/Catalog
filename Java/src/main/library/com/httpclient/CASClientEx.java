package com.httpclient;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;



public class CASClientEx {
	public String getBookmarkxml() {
	String response = null;
	//String targetURL = "http://mobiledata1.calacademy.org/services/rest/service_ufg_api/systemGetConfig";
	/*Response : <?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple Computer//DTD PLIST 1.0//EN" "http://www.apple.
com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0"><dict><key>sessionID</key><string>84e22dd5ce1ce7ceea278f410
689559c</string><key>apiKey</key><string>e8e7de6b49fcb0161b80078b16d7c242</strin
g></dict></plist>
	*/
	
	
	//String targetURL = "http://mobiledata1.calacademy.org/services/plist/service_ufg_api/systemConnect";
	String targetURL = "http://mobiledata1.calacademy.org/services/rest/service_ufg_api/homeGetHome";
	PostMethod userPost = new PostMethod(targetURL);
		try {
			userPost.getParams().setBooleanParameter(
					HttpMethodParams.USE_EXPECT_CONTINUE, true);
			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams().setConnectionTimeout(
					5000);
			userPost.setRequestHeader("Accept", "application/plist+xml");
			//userPost.setRequestHeader("Content-Type", "application/plist+xml");
			
		//	NameValuePair nvp1 = new NameValuePair("api_key", "e8e7de6b49fcb0161b80078b16d7c242");
		//	NameValuePair nvp2 = new NameValuePair("secret_key", "0");
			
		//	userPost.setQueryString(new NameValuePair[] {nvp1,nvp2});

			//userPost.setQueryString("<plist><dict> <key>device_id</key><string>D9A5CBC6EC38</string></dict></plist>");
			userPost.setQueryString("<plist version='1.0'><dict><key>api_key</key><string>e8e7de6b49fcb0161b80078b16d7c242</string><key>secret_key</key><string>0</string></dict></plist>");
			
			int status = client.executeMethod(userPost);
			if (status == HttpStatus.SC_OK) {
				response = userPost.getResponseBodyAsString();
			} else {
				
				System.out.println("SOME ERROR"+status);
			}
		}catch(ConnectTimeoutException timeout){
			System.err.println("connection time out==> get new connection");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		/*	HttpClient client = new HttpClient();
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
		
		*/
		return response;
	}
	public static void main(String args[]){
		CASClientEx obj = new CASClientEx();
		System.out.println(":::::::::obj.getBookmarkxml():::::::::"+obj.getBookmarkxml());
	}
}
