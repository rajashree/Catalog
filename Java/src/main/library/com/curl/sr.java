package com.curl;

import org.curl.CURL;
import org.curl.CurlGlue;
import org.curl.CurlWrite;

public class sr implements CurlWrite {

	static StringBuffer sbResponse = new StringBuffer(16384);
	public static void main(String[] args) {
		CurlGlue cg;
        int page;
        String postfields ;
        try {
        	cg = new CurlGlue();
        	
			cg.setopt( CURL.OPT_COOKIEJAR, "/tmp/cookiejar-$randnum");
		    cg.setopt( CURL.OPT_COOKIEFILE, "/tmp/cookiejar-$randnum");
		    cg.setopt( CURL.OPT_USERAGENT, "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.1) Gecko/20061204 Firefox/2.0.0.1");
		    cg.setopt( CURL.OPT_FOLLOWLOCATION, 1);
		    cg.setopt( CURL.OPT_POST, 0);
			
			// get homepage for login page token
		    cg.setopt(CURL.OPT_URL,"http://www.socializr.com");
		    cg.setopt(CURL.OPT_HTTPGET, 1);
		    cg.perform();
		   // page = 
		    
		    //cg.setopt(CURL.OPT_URL,"http://www.google.com");
		    cg.setopt(CURL.OPT_URL,"http://www.socializr.com/do/login");
		    cg.setopt(CURL.OPT_REFERER, "http://www.socializr.com");
		    cg.setopt( CURL.OPT_HTTPHEADER, "Content-Type: application/x-www-form-urlencoded");
		    cg.setopt( CURL.OPT_POST, 1);
		    //postfields = "email=" + java.net.URLEncoder.encode("harini.tirumuru@sourcen.com");
			//postfields += "&password=" + java.net.URLEncoder.encode("sairam");
			
			//cg.setopt(CURL.OPT_POSTFIELDS,postfields);
			page = cg.perform();
			
			
		/*	cg.setopt(CURL.OPT_URL,"http://www.socializr.com/userphotos");
			cg.setopt(CURL.OPT_REFERER, "http://www.socializr.com");
			cg.setopt(CURL.OPT_HTTPHEADER,"Content-Type: application/x-www-form-urlencoded");
			cg.setopt(CURL.OPT_POST, 1);
			
			postfields = "email=" + java.net.URLEncoder.encode("harini.tirumuru@sourcen.com");
			postfields += "&password=" + java.net.URLEncoder.encode("sairam");
			
			cg.setopt(CURL.OPT_POSTFIELDS,postfields);
			page = cg.perform();*/
		    
		    
		    
            cg.finalize();
		} catch (Exception e) {
       		e.printStackTrace();
		}
	
   
	}

	public int handleString(byte[] s) {
		System.out.println(s.length);
        sbResponse.append(new String(s));
		return 0;
	}

}
