
package com.snipl.ice.config;

/**
* @Author Kamalakar Challa 
*   
*/

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.GenericServlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class InitConfig  extends GenericServlet{
	
	public static String path=null;
	private static final long serialVersionUID = 1L;

	public void init() {
        System.out.print("##### Initialization of ICE #### ");
        try {
			initializeICE();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	public void service(ServletRequest request, ServletResponse response) {
		 System.out.print(" No implementation in service method!");
    }	
	
	//Checking existance of Properties file
   public void initializeICE() throws IOException
   {
	   	Properties ICEpro = new Properties();
	   	path=getServletContext().getRealPath("/");
	   	FileInputStream fis=new FileInputStream(getServletContext().getRealPath("/")+"/config/ice.properties");
	    if (null == fis) {
	        System.out.println(" Input Stream is NULL");
	    }
	    try {
	    	ICEpro.load(fis);
	    }
	    catch (Exception e) {
	        throw new RuntimeException(e);
	    }
		ICEEnv.getInstance().initalize(ICEpro);
   }
}
