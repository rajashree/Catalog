package com.curl;

import org.curl.*;

class test implements CurlWrite {
	//static String ca_cert_file = "C:\\eclipse\\plugins\\org.curl_1.0.0\\os\\win32\\x86\\ca-bundle.crt";
    static String ca_cert_file = "ca-bundle.crt";
	static StringBuffer sbResponse = new StringBuffer(16384);

    public int handleString(byte s[])
    {
        /* output everything */
//        System.out.println("IIIIIIIIIII -------------- OOOOOOOOOOOOOOOOOOO");
//        try {
    	System.out.println(s.length);
//    	String temp = new String(s);
        sbResponse.append(new String(s));

//        }
//        catch (java.io.IOException moo) {
          // nothing
//        }
        return 0;
    }

    public static void main(String[] args)
    {
		CurlGlue cg;

        try {
			test cw = null;

        	// Register callback write function
        	cg = new CurlGlue();
        	
        	// this will throw a ERROR_NO_CALLBACK_INSTANCE exception, non-fatal
			try {
				cg.setopt(CURL.OPT_WRITEFUNCTION, cw);
			} catch (CURLException e) {
				System.out.println("Wouldn't have thrown an error if we instantiated test() first!");
			}
        	
        	// we must first instantiate the CurlWrite interface
        	cw = new test();
        	
        	// set the callback function
        	cg.setopt(CURL.OPT_WRITEFUNCTION, cw);
        	
			cg.setopt(CURL.OPT_VERBOSE, 3);
			cg.setopt(CURL.OPT_FOLLOWLOCATION, 1);
//			cg.setopt(CURL.OPT_FRESH_CONNECT, 0);
			
			// I have not tested whether or not the verify peer functions 
			// work with my java implementation. I will be working on this.
			// Better to turn them off for now
			cg.setopt(CURL.OPT_SSL_VERIFYPEER, 0);
			cg.setopt(CURL.OPT_SSL_VERIFYHOST, 0);
			cg.setopt(CURL.OPT_SSLCERTTYPE, "PEM");
			cg.setopt(CURL.OPT_SSLVERSION, 3);
			
			int result = cg.setopt(CURL.OPT_URL, "https://www.verisign.com/");
			cg.setopt(CURL.OPT_HTTPGET, 1);
			result = cg.perform();
			
			// clean everything up.
			cg.finalize();
		} catch (Exception e) {
       		e.printStackTrace();
		}
		
		System.out.println("***** OUTPUT STRINGBUFFER *****");
		System.out.println("Bytes returned to SB: " + sbResponse.length());
		String[] splits = sbResponse.toString().split("\n");
		System.out.println("Num lines: " + splits.length);
		System.out.println(sbResponse.toString());
    }
}
