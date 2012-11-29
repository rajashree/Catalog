package com.xtream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

class AuthServerConfig {
    private int port;
    private String server;
    public String getServer(){
    	return server;
    }
}


public class XStreamEx1 {
    private static String xml =
          "<authServer>"
        + "  <port>1080</port>"
        + "    <server>localhost</server>"
        + "</authServer>";


    public static void main(String []args) {
    	//XStream xs = new XStream(new DomDriver()); //XPP3.jar is not required
    	
    	XStream xs = new XStream(); //XPP3.jar is required
        xs.alias("authServer", AuthServerConfig.class);

        AuthServerConfig asc = (AuthServerConfig) xs.fromXML(xml);
        System.out.println("from xml to object : "+asc);
        System.out.println("from xml to object : "+asc.getServer());
        System.out.println("from object to xml :\n"+xs.toXML(asc));
    }
}
