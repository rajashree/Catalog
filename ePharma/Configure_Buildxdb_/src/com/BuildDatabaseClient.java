
/***********************************************************************
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
************************************************************************/
package com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import com.rdta.eag.commons.persistence.TLDBConfig;

import com.rdta.eag.commons.EXBEnv;

public class BuildDatabaseClient {

    /**
     * Inovkes a particular method on the object 'ESBTigerLogicDB' based on the
     * command line arguments.
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
	
			System.out.println(" args Length " + args.length ); 
					
			// Process the arguments.
			String filePath = args[0];
	        String operation = args[1];
	        String propertiesFile = args[2];
	     
			System.out.println("XDB Config File Path : " + args[0]);
			System.out.println("Operation : " + args[1]);
			System.out.println("ENV Properties file(TIger Logic Connection Parameters) : " + args[2]);
			
			if( filePath == null || operation == null || propertiesFile == null) {
				printUsageAndTerminate();
				return;
			}
			
			filePath = filePath.trim();
			operation = operation.trim();
			propertiesFile = propertiesFile.trim();
		
			if( filePath.equals("") || operation.equals("") || propertiesFile.equals("") ) {
				printUsageAndTerminate();
				return;
			}
			
			Properties exbEnvProps = new Properties();  	 
			String	envFilePath =  System.getProperty("user.dir") + File.separator + propertiesFile;
	        //Loading the Properties file
	        InputStream is = null;
			try {				

				
				System.out.println(" Read properties file from : " + envFilePath);

				is = new FileInputStream(new File(envFilePath));
				if(is != null)
		        {
					exbEnvProps.load(is);
					EXBEnv env =  EXBEnv.getInstance();
					env.initalize(exbEnvProps);
					//just initialized TLConfig object
					TLDBConfig.getInstance().initalize(env.getTlHostName(),env.getTlPort(),env.getTlUserName(),env.getTlPassWord());
					
				} 
				else
				{
		           System.err.println(" Input Stream is NULL");
				}


			} 
			catch (FileNotFoundException e1) 
			{
				e1.printStackTrace();
				throw new RuntimeException("File Not found " + envFilePath,e1); 
			}
			catch(Exception e)
	        {
				e.printStackTrace();
	           throw new RuntimeException("EXBEnv Initalization Exception ",e); 
	        }
			finally
			{
				if(is != null )
				{
					is.close();
				}
			}
					
	        
		
			BuildDatabase db = new BuildDatabase(filePath);
	        // Build the call.
	        if (operation.equals("load")) {
	            if (args.length != 3) {
	                printUsageAndTerminate();
	            }
	            db.create();
                db.load();
                db.loadSPsToDB();
	        } else if (operation.equals("deleteDB")) {
	            if (args.length != 4) {
	                printUsageAndTerminate();
	            }
	            db.deleteDatabase(args[3]);
				System.out.println("Successfully deleted Database :" + args[3]);
				
	        } else if (operation.equals("deleteColl")) {
	            if (args.length != 5) {
	                printUsageAndTerminate();
	            }
	            db.deleteCollection(args[3], args[4]);
				
				System.out.println("Successfully deleted collection :" + args[4] + " in database: " +  args[3]);
				
	        }  else if( operation.equals("loadSPs") ){
	        	if (args.length != 2) {
	                printUsageAndTerminate();
	                return;
	            }
	        	db.loadSPsToDB();
	        }  else {
	            printUsageAndTerminate();
	        }
	}

    /**
     * Prints the Useage on the console.
     *
     */
    private static void printUsageAndTerminate() {
        System.err.println("Usage: java "
                + BuildDatabaseClient.class.getName()
                + " xdb/esbdbconfig.xml  load|deleteDB|deletColl <dbName> <collName>");
        System.exit(1);
    }
}