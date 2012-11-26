package com.curl;

import org.curl.CURL;
import org.curl.CurlGlue;
import org.curl.CurlWrite;

class MySpace implements CurlWrite {
	static StringBuffer sbResponse = new StringBuffer(16384);
	public int handleString(byte s[])
    {	sbResponse.append(new String(s));
        return 0;
    }
	
    public static void main(String[] args)
    {
    	PatternMatchUtil pattern=new PatternMatchUtil();
		CurlGlue cg=new CurlGlue();
		MySpace cw=new MySpace();
		String postfields="";
		String[] images;
		try {
			
			
			cg.setopt(CURL.OPT_WRITEFUNCTION, cw);
			cg.setopt(CURL.OPT_COOKIEJAR, "/tmp/cookiejar-$randnum");
        	cg.setopt(CURL.OPT_COOKIEFILE, "/tmp/cookiejar-$randnum");
        	cg.setopt(CURL.OPT_USERAGENT, "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.1) Gecko/20061204 Firefox/2.0.0.1");
        	cg.setopt(CURL.OPT_FOLLOWLOCATION, 1);
        	cg.setopt(CURL.OPT_POST, 0);
        	cg.setopt(CURL.OPT_URL,"http://viewmorepics.myspace.com/index.cfm?fuseaction=user.editAlbumPhotos&albumID=0&MyToken=0b6bc7e5-3e04-4cc7-9ef6-6944fab98b35");
        	cg.perform();
        	postfields =null;
        	cg.setopt(CURL.OPT_URL,"http://www.socializr.com/do/login");
        	cg.setopt(CURL.OPT_REFERER, "http://www.socializr.com");
        	cg.setopt(CURL.OPT_POST, 1);
        	postfields = "email=" + java.net.URLEncoder.encode("rajashree.meganathan@sourcen.com");
  			postfields += "&password=" + java.net.URLEncoder.encode("mjdjrmahav");
  			cg.setopt( CURL.OPT_POST, 1);
  			cg.setopt(CURL.OPT_POSTFIELDS,postfields);
  			cg.perform();
        	postfields =null;
        	sbResponse = new StringBuffer(16384);
        	int result =cg.setopt(CURL.OPT_URL,"http://www.socializr.com/userphotos");
 			cg.setopt(CURL.OPT_REFERER, "http://www.socializr.com");
 			cg.setopt(CURL.OPT_POST, 1);
 			postfields = "email=" + java.net.URLEncoder.encode("rajashree.meganathan@sourcen.com");
  			postfields += "&password=" + java.net.URLEncoder.encode("mjdjrmahav");
  			cg.setopt(CURL.OPT_POSTFIELDS,postfields);
  			result = cg.perform();
  			cg.finalize();
            
  			images=pattern.srcPattern(sbResponse.toString());
  			//System.out.println(sbResponse.toString());
			for(int i=0;i<images.length;i++){
				System.out.println(images[i]);
			}
			
	    } catch (Exception e) {
	    		e.printStackTrace();
			}
		
	 	}
}