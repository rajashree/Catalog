package com.curl;

import org.curl.*;


class Bebo implements CurlWrite {
	static StringBuffer sbResponse = new StringBuffer(16384);
	public int handleString(byte s[])
    {	sbResponse.append(new String(s));
        return 0;
    }
	
    public static void main(String[] args)
    {
    	PatternMatchUtil pattern=new PatternMatchUtil();
		CurlGlue cg=new CurlGlue();
		Bebo cw=new Bebo();
		String postfields="";
		String[] albumId;
		String[] images;
		String albumNames;
		try {
			cg.setopt(CURL.OPT_WRITEFUNCTION, cw);
			cg.setopt(CURL.OPT_COOKIEJAR, "/tmp/cookiejar-$randnum");
        	cg.setopt(CURL.OPT_COOKIEFILE, "/tmp/cookiejar-$randnum");
        	cg.setopt(CURL.OPT_USERAGENT, "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.1) Gecko/20061204 Firefox/2.0.0.1");
        	cg.setopt(CURL.OPT_FOLLOWLOCATION, 1);
        	cg.setopt(CURL.OPT_POST, 0);
        	cg.setopt(CURL.OPT_URL,"http://www.bebo.com");
        	cg.perform();
        	postfields =null;
        	cg.setopt(CURL.OPT_URL,"http://www.bebo.com/SignIn.jsp");
        	cg.setopt(CURL.OPT_REFERER, "http://www.bebo.com");
        	cg.setopt(CURL.OPT_POST, 1);
        	postfields = "EmailUsername=" + java.net.URLEncoder.encode("Sourcen");
  			postfields += "&Password=" + java.net.URLEncoder.encode("snipl123");
  			cg.setopt( CURL.OPT_POST, 1);
  			cg.setopt(CURL.OPT_POSTFIELDS,postfields);
  			cg.perform();
  			String memberId=pattern.beboMemberID(sbResponse.toString());
  			//System.out.println(memberId);
  			
  			postfields =null;
        	sbResponse = new StringBuffer(16384);
        	int result =cg.setopt(CURL.OPT_URL,"http://www.bebo.com/PhotoAlbums.jsp?");
 			cg.setopt(CURL.OPT_REFERER, "http://www.bebo.com");
 			cg.setopt(CURL.OPT_POST, 1);
 			postfields = "MemberId=" +memberId;
  			cg.setopt(CURL.OPT_POSTFIELDS,postfields);
  			cg.perform();
  			
  			albumId=pattern.beboAlbumIds(sbResponse.toString());
  			for(int j=0;j<albumId.length;j++){
				System.out.print(albumId[j]+"---");
				postfields =null;
	        	sbResponse = new StringBuffer(16384);
	        	cg.setopt(CURL.OPT_URL,"http://www.bebo.com/PhotoAlbum.jsp?PhotoNbr=1");
	 			cg.setopt(CURL.OPT_REFERER, "http://www.bebo.com");
	 			cg.setopt(CURL.OPT_POST, 1);
	 			postfields = "&MemberId="  + java.net.URLEncoder.encode(memberId);
	 			postfields += "&PhotoAlbumId="  + java.net.URLEncoder.encode(albumId[j]);
	  			cg.setopt(CURL.OPT_POSTFIELDS,postfields);
	  			cg.perform();
	  			//System.out.println(sbResponse.toString());
	  			albumNames=pattern.beboAlbumName(sbResponse.toString());
	  			System.out.println(albumNames);
	  			images=pattern.beboImages(sbResponse.toString());
	  			
	  			for(int k=0;k<images.length;k++){
					System.out.println(images[k]);
					
		  			
				}
			}
  			/*	postfields =null;
        	sbResponse = new StringBuffer(16384);
        	int result =cg.setopt(CURL.OPT_URL,"http://www.bebo.com/PhotoAlbum.jsp?PhotoNbr=1");
 			cg.setopt(CURL.OPT_REFERER, "http://www.bebo.com");
 			cg.setopt(CURL.OPT_POST, 1);
 			postfields = "email=" + java.net.URLEncoder.encode("rajashree.meganathan@sourcen.com");
  			postfields += "&password=" + java.net.URLEncoder.encode("mjdjrmahav");
  			cg.setopt(CURL.OPT_POSTFIELDS,postfields);
  			result = cg.perform();
  			cg.finalize();
            
  			images=pattern.srcPattern(sbResponse.toString());
  			//
			for(int i=0;i<images.length;i++){
				System.out.println(images[i]);
			}*/
  			//System.out.println(sbResponse.toString());
	    } catch (Exception e) {
	    		e.printStackTrace();
			}
	 	}
}