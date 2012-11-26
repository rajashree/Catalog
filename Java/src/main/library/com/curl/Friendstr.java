package com.curl;

import org.curl.CURL;
import org.curl.CurlGlue;
import org.curl.CurlWrite;

class Friendstr implements CurlWrite {
	static StringBuffer sbResponse = new StringBuffer(16384);
	public int handleString(byte s[])
    {	sbResponse.append(new String(s));
        return 0;
    }
	
    public static void main(String[] args)
    {
    	PatternMatchUtil pattern=new PatternMatchUtil();
		CurlGlue cg=new CurlGlue();
		Friendstr cw=new Friendstr();
		String postfields="";
		String[] images;
		try {
			
			
			cg.setopt(CURL.OPT_WRITEFUNCTION, cw);
			cg.setopt(CURL.OPT_COOKIEJAR, "/tmp/cookiejar-$randnum");
        	cg.setopt(CURL.OPT_COOKIEFILE, "/tmp/cookiejar-$randnum");
        	cg.setopt(CURL.OPT_USERAGENT, "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.1) Gecko/20061204 Firefox/2.0.0.1");
        	cg.setopt(CURL.OPT_FOLLOWLOCATION, 1);
        	cg.setopt(CURL.OPT_POST, 0);
        	cg.setopt(CURL.OPT_URL,"http://www.friendster.com");
        	cg.perform();
        	postfields =null;
        	cg.setopt(CURL.OPT_URL,"http://www.friendster.com/login.php");
        	cg.setopt(CURL.OPT_REFERER, "http://www.friendster.com");
        	cg.setopt(CURL.OPT_POST, 1);
        	postfields = "email=" + java.net.URLEncoder.encode("sourcen123@gmail.com");
  			postfields += "&password=" + java.net.URLEncoder.encode("snipl123");
  			postfields += "&_submitted=1";
  			postfields += "&next=/";
  			postfields += "&tzoffset=-330";
  			cg.setopt( CURL.OPT_POST, 1);
  			cg.setopt(CURL.OPT_POSTFIELDS,postfields);
  			cg.perform();
        	postfields =null;
        	sbResponse = new StringBuffer(16384);
        	int result =cg.setopt(CURL.OPT_URL,"http://www.friendster.com/viewphotos.php?");
 			cg.setopt(CURL.OPT_REFERER, "http://www.friendster.com");
 			cg.setopt(CURL.OPT_POST, 1);
 			postfields = "email=" + java.net.URLEncoder.encode("sourcen123@gmail.com");
  			postfields += "&password=" + java.net.URLEncoder.encode("snipl123");
  			cg.setopt(CURL.OPT_POSTFIELDS,postfields);
  			result = cg.perform();
  			cg.finalize();
            
  			images=pattern.friendsrcPattern(sbResponse.toString());
  			//System.out.println(sbResponse.toString());
			for(int i=0;i<images.length;i++){
				System.out.println(images[i]);
			}
			
	    } catch (Exception e) {
	    		e.printStackTrace();
			}
		
	 	}
}