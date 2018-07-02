package com;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class HelloWorld {
	private static final String PROPS_CONFIG = "Properties.properties";
	static Logger mylogger = Logger.getLogger(HelloWorld.class.getName());
	    
	public void HelloWorldMethod(String str){
		mylogger.info(" Result "+str);
		
	}
	
	public static void main(String args[]) throws IOException{
		java.util.Properties properties = new java.util.Properties();
		
		
		InputStream inputstream = HelloWorld.class.getResourceAsStream(PROPS_CONFIG);
		properties.load(inputstream);
		String logConfigureFileUrl = properties.getProperty("log.ConfigurationFileUrl");
		System.out.println(logConfigureFileUrl);
		
		PropertyConfigurator.configure(logConfigureFileUrl);
        
		String param = " Hello World !!! ";
		HelloWorld obj = new HelloWorld();
		obj.HelloWorldMethod(param);
		
	}
}
