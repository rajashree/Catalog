/********************************************************************************

* Raining Data Corp.

*

* Copyright (c) Raining Data Corp. All Rights Reserved.

*

* This software is confidential and proprietary information belonging to

* Raining Data Corp. It is the property of Raining Data Corp. and is protected

* under the Copyright Laws of the United States of America. No part of this

* software may be copied or used in any form without the prior

* written permission of Raining Data Corp.

*

*********************************************************************************/

 
package com.rdta.commons.persistence;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import com.rdta.commons.xml.XMLUtil;


/**
 * Tiger Logic Data Base Config provider. It is implemented as singleton object.
 * @author Srinivasa Raju - Leo Fernandez
 * @version 1.0
 * @created 02-Jun-2005 7:14:41 PM
 */
public class TLDBConfig {
	
	/** The tldbconf is used to for current instance storage. */
	private static TLDBConfig tldbconf;
	
	/** The <code>log</code> is used for Log storage. */
	private static final Logger log = Logger.getLogger(TLDBConfig.class);
	

	public String hostAddress = null;
	public String port  	  = null;
	public String userName    = null;
	public String userPword   = null;
	
	private static final String XPATH_ESBDB_HOST 	= "/dbConfig/@hostName";
    private static final String XPATH_ESBDB_PORT 	= "/dbConfig/@port";
    private static final String XPATH_ESBDB_USER 	= "/dbConfig/@userName";
    private static final String XPATH_ESBDB_PWD  	= "/dbConfig/@password";
   
    	
	private TLDBConfig(){
			
/*		Node esbconfig = XMLUtil.parse(AppResourceUtil.getDatabaseConfigFile());
 		if(esbconfig!= null) {	 	
	 		hostAddress = XMLUtil.getValue(esbconfig,TLDBConfig.XPATH_ESBDB_HOST);
	 		port 		= XMLUtil.getValue(esbconfig,TLDBConfig.XPATH_ESBDB_PORT);
	 		userName 	= XMLUtil.getValue(esbconfig,TLDBConfig.XPATH_ESBDB_USER);
	 		userPword 	= XMLUtil.getValue(esbconfig,TLDBConfig.XPATH_ESBDB_PWD);
		} else {
			log.warn("DB Configuration file is not found. Please check !!!");
			log.warn("Loading ESB Default DB Parms...");*/
			
			//Add all default values
			hostAddress = "localhost";
	 		port 		= "3408";
	 		userName 	= "admin";
	 		userPword 	= "admin";
	// 	}
	 					
	 	log.info(" TLDBConfig all values:  "+hostAddress+" \n "+port +" \n "+ userName+" \n "+userPword+" \n ");
	}  

	
	public String getHostName(){
		return hostAddress;
	}

	public String getPortNumber(){
		return port;
	}

	public String getUserName(){
		return userName;
	}

	public String getPassword(){
		return userPword;
	}


	/**
	 * Method to create a new instance of <code>TLDBConfig</code> or send an
	 * existing instance if already created.
	 * @throws Exception
	 */
	public static TLDBConfig getInstance () {
		if ( tldbconf == null ) {
			synchronized ( TLDBConfig.class ) {
				tldbconf = new TLDBConfig ();
			}
		}
		return tldbconf;
	}

}