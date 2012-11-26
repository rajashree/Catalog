
package com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import org.w3c.dom.Node;


import com.rdta.eag.commons.persistence.QueryRunner;
import com.rdta.eag.commons.persistence.QueryRunnerFactory;

import com.rdta.eag.commons.xml.XMLUtil;

import com.rdta.eag.commons.persistence.ESBTigerLogicDB;
import com.rdta.eag.commons.persistence.PersistanceException;
import com.rdta.eag.commons.persistence.pool.TLConnectionPool;


import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.PreparedStatement;



public class BuildDatabase {

	private static final String XPATH_ESBDB_DATASRC = "@dataSource";
	private Node esbconfig;
	private String dataSource = null;
	private String xQuery = "";
    
    private static final String STOREDPROC_DIR = "/_tlsp/input";

	private QueryRunner queryRunner ;

	/**
	 * Constructor
	 */
	public BuildDatabase(String configFilePath) {
	
		File configFile = FileUtility.getFile(configFilePath);
		esbconfig = XMLUtil.parse(configFile);
		if(esbconfig != null)
			dataSource = XMLUtil.getValue(esbconfig,BuildDatabase.XPATH_ESBDB_DATASRC);
		else
			throw new RuntimeException(" Specified File Path : " + configFilePath +" not found! " );
		//		create query runner object now. It will use internally
		queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	}
    
  	
	/**
	 * Creates Databases. Creates Collections under their respective Database as
	 * mentioned in the esbDBconfig.xml file.
	 */
	public void create() throws  PersistanceException {
		
			//get the database name to create from config file
			List results = XMLUtil.executeQuery(esbconfig,"database[collection/@load='true']/@name");
			System.out.println("size:::: "+results.size());
			Iterator iterator = results.iterator();
			while (iterator.hasNext()) {
				Node result = (Node) iterator.next();
				String dbname = result.getNodeValue();

				System.out.println("\n creating Database:   "+ dbname);
				createDatabase(dbname, "");

				//get the collection names to create in the db
				List collections = XMLUtil.executeQuery(esbconfig,
						"database[@name='" + dbname
								+ "']/collection[@load='true']/name");
				System.out.println("\n  No. of collection needs to be created: "+ collections.size());
				Iterator itr = collections.iterator();

				while (itr.hasNext()) {
					Node result1 = (Node) itr.next();
					String collection = XMLUtil.getValue(result1);
					createCollection(dbname, collection, "");
				}
			}
	}

	/**
	 * Creates a Collection in TigerLogic
	 *
	 * @param dataSource -
	 *            Name of the DataSource.
	 * @param DBname -
	 *            Name of the Database which needs to be created.
	 * @param CollName -
	 *            Name of the Collection which needs to be created.
	 * @param description -
	 *            Description of the database, this element is optional.
	 */

	public void createCollection(String DBName,String CollName, String description) throws  PersistanceException {
		xQuery = " let $config := <ConfigurationStorageData><compression>1</compression></ConfigurationStorageData> ";
		xQuery = xQuery + "return ";

		xQuery = xQuery + " if (count (for $i in tig:list-collections()";
		xQuery = xQuery + " where $i/tig:collection-descriptor/@parent-database = '" + DBName + "' ";
		xQuery = xQuery + " and $i/tig:collection-descriptor/tig:name = '"+ CollName +"'";
		xQuery = xQuery + " return $i) <= 0) then ";
		xQuery = xQuery + " tig:create-collection('" + dataSource + "','"
				+ CollName + "','" + DBName + "','" + description
				+ "',$config)";
		xQuery = xQuery + "	 else 'false' ";

		queryRunner.executeUpdate(xQuery);
		
	}

	/**
	 * Creates a Database in TigerLogic
	 *
	 * @param DBname -
	 *            Name of the Database which needs to be created
	 * @param description -
	 *            Description of the database, this element is optional
	 */

	public void createDatabase(String DBname, String description) throws PersistanceException {
		xQuery="";
		xQuery  = xQuery  + " if (count( for $i in tig:list-databases()" ;
		xQuery  = xQuery  + " where $i/tig:database-descriptor/tig:name = '" + DBname + "'";
		xQuery  = xQuery  + " return $i) <= 0) then ";
		xQuery  = xQuery  + " tig:create-database('" + DBname + "','" + description + "')";
		xQuery  = xQuery  + " else 'false'";

		queryRunner.executeUpdate(xQuery);
		System.out.println("XQuery is " + xQuery);

	}

	/**
	 * Deletes a Collection under a particular DataBase.
	 *
	 * @param DBname -
	 *            Name of the DataBase.
	 * @param CollName -
	 *            Name of the Collection which needs to be deleted
	 */
	public void deleteCollection(String DBname, String CollName) throws PersistanceException{

		xQuery = "tig:drop-collection( 'tig:///" + DBname + "/" + CollName + "' )";
		queryRunner.executeUpdate(xQuery);
	}

	/**
	 * Deletes a particular Database
	 *
	 * @param DBname -
	 *            Name of the DataBase which needs to be deleted in TigerLogic.
	 */
	public void deleteDatabase(String DBname) throws PersistanceException {
		xQuery = "tig:drop-database( '" + DBname + "')";
		queryRunner.executeUpdate(xQuery);
	}

	/**
	 * Deletes a particular Document in a given DB-Collection
	 *
	 * @param DBname -
	 *            Name of the DataBase.
	 * @param CollName -
	 *            Name of the Collection under which the document exits.
	 * @param pageNM -
	 *            Name of the Document which needs to be deleted.
	 */

	public void deleteDoc(String DBName, String CollName, String pageNM) throws PersistanceException {
    	xQuery = "declare base-uri 'tig:///" + DBName + "/'; ";
		xQuery = xQuery + "tig:delete-document( '" + CollName + "/"
				+ pageNM + "' )";
		queryRunner.executeUpdate(xQuery);
	}


	/**
	 * Loads the XML Documents from "ESB/config/data/init" FileSystem into
	 * TigerLogic under respective collections based on 'load' and 'status'
	 * attributes as mentioned in the esbDBConfig.xml
	 *
	 */
	public void load() throws PersistanceException,FileNotFoundException {

		System.out.println(" \n  \n Started loading init data....");

		//find the list of databases to load init data information
		System.out.println("Loading...........");
		List results = XMLUtil.executeQuery(esbconfig,"database[collection/data/input/@load='true']/@name");
		System.out.println("results are "+results);
		Iterator iterator = results.iterator();
		//iterate database list
		while (iterator.hasNext()) {
			Node result = (Node) iterator.next();
			String dbname = result.getNodeValue();

			//based on datbase name get the collection list to load init data information
			List collections = XMLUtil.executeQuery(esbconfig,"database[@name='" + dbname
					+ "']/collection[data/input/@load='true']/name");
			
			System.out.println(" No. of collections to load : " + collections.size());
			Iterator itr = collections.iterator();

			//iterate collection list to load data
			while (itr.hasNext()) {
				Node result1 = (Node) itr.next();
				String collection = XMLUtil.getValue(result1);
				System.out.println(" Name of collection loading init data: " + collection);

				//source of input documents
				String inputsource = XMLUtil.getValue(esbconfig,"database[@name='" + dbname
						+ "']/collection[name='" + collection
						+ "']/data/input/@source");
				System.out.println("Input source is "+inputsource);

				if (inputsource.equalsIgnoreCase("all")) { //insert all document from directory
					System.out.println("\n ###### Inserting all the documents!!! ######");
					ArrayList filelist = (ArrayList) FileUtility.getInitDataFiles(dbname,collection);
					System.out.println("File list is "+filelist);
					for (int j = 0; j < filelist.size(); j++) {
						File file = (File) filelist.get(j);
						insertInDB(file,dbname,collection);
					}
				} else if (inputsource.equalsIgnoreCase("listed")) { //insert only list document inside config file

					System.out.println("\n ###### Inserting only listed documents!!! ######");

					List inputxmls = XMLUtil.executeQuery(esbconfig,"database[@name='" + dbname
							+ "']/collection[name='" + collection
							+ "']/data/input[@load='true']/doc");
					Iterator itre = inputxmls.iterator();
					//iterate all specified documents inside config file
					while (itre.hasNext()) {
						Node result2 = (Node) itre.next();
						String docName = XMLUtil.getValue(result2);
						File file = FileUtility.getInitDataFile(collection,docName);
						insertInDB(file,dbname,collection);
					}
				}
			}// while $collections exists

		}//while $data/input/@load='true'
	}


	
	/**
	 * Store input xml file into Data base.
	 *
	 * @param file
	 */
    private void insertInDB(File file, String dbName, String collection) throws FileNotFoundException, PersistanceException {

		if(file != null && (!("vssver.scc".equals(file.getName())))) {
			InputStream ins = new FileInputStream(file);
			String docName = FileUtility.getFileNameWithOutExtension(file);
			//System.out.println("***docName" +docName );
			Node xml = XMLUtil.parse(ins);

			xQuery = "";
			xQuery = xQuery + " tig:insert-document('tig:///" + dbName;
			xQuery = xQuery +  "/" + collection;

			xQuery = xQuery +   "/" + docName;

			xQuery = xQuery + "', ";
			xQuery = xQuery +  XMLUtil.convertToString(xml,true) + " )";

			queryRunner.executeUpdate(xQuery);

		} else {
			System.out.println("Input File doen't have XML Extension. File Name: " + file.getName() );
		}
	}



    /**
     * Called by Client to load the sps into db.
     * Checks the XML Structure in the config file and calls the helper functions accordingly
     * 
     */
    public void loadSPsToDB()  {
        //Check whether the load attribute is set to true
        boolean bLoad = XMLUtil.evaluate(esbconfig,"StoredProcedures/input[@load='true']");
        
       
        // Do the loading process only if true		
        if ( bLoad ) {
         
             //Get the value of recreate attribute 
            boolean bRecreate = ! XMLUtil.evaluate(esbconfig,"StoredProcedures/input[@recreate='false']");
            int numSPs = 0;
            
            
            //Check whether the source attribute is listed
            boolean bListedOnly = XMLUtil.evaluate(esbconfig,"StoredProcedures/input[@source='listed']");
            //Get the procedure names to be loaded from the configuration file  if @source is listed			
            if ( bListedOnly ) {
                              
                List results = XMLUtil.executeQuery(esbconfig,"StoredProcedures/ProceduresList/Name/text()");
                Iterator it = results.iterator();
                while ( it.hasNext() ) {
                    Node node = (Node) it.next();
                    String docName = XMLUtil.getValue(node);
                    File file = FileUtility.getInitDataFile(STOREDPROC_DIR,docName);
                    createSPsInDB(file,bRecreate);
                    numSPs++;
                 }
            }
            else
            {
                ArrayList filelist = (ArrayList) FileUtility.getInitDataFiles(STOREDPROC_DIR);
                for (int j = 0; j < filelist.size(); j++) {
                    File file = (File) filelist.get(j);
                    createSPsInDB(file,bRecreate);
                    numSPs++;
                }
            }
            
            System.out.println("No. of SPs created :" + numSPs );

        } else {
        	System.out.println("Not loading the SPs as load attribute is false or not found");
        }

    }
  
   /**
    * 
    * @param lstFiles - List of File Objets in the source directory that are to be uploaded in the db
    * @param reCreate - Drop and create the sp if it is already present in the database.
    */
   private void createSPsInDB( File cf,boolean reCreate){
     
       Connection con = null; 
       PreparedStatement pstmt = null;

       try {
           // Query checks the existance of the stored procedure
           // If Recreate value is true and the sp exists drops the sp
           // Create the SP using the inputstream passed.
           // Note : If the $dropped is not used in the return clause, the drop statement is not getting executed.
           StringBuffer xQuery = new StringBuffer("declare variable $spname as xs:string external;");
           xQuery.append("declare variable $bRecreate as xs:boolean external;");
           xQuery.append(" let $bexists as xs:boolean :=  exists(tig:list-stored-procedures()/function[@name= concat('tlsp:',$spname)]) ");
           xQuery.append(" let $dropped as xs:boolean := if( $bRecreate eq true() and $bexists eq true() ) then tig:drop-stored-procedure($spname) else false() ");
           xQuery.append(" return ( $dropped , if( ( $bRecreate eq true() ) or ( $bRecreate eq false() and $bexists eq false() ) ) then tig:create-stored-procedure($spname, input(1,'text/plain') ) else false() )");

           // Get the connection object from ConnectionPool
           // Create the PreparedStatement object from the connection 
           con = TLConnectionPool.getTLConnectionPool().borrowConnection();
           pstmt = con.createPreparedStatement(xQuery.toString());

            //Get the name of the file without extensions. This will be used as the name of the sp.
           String cfName = FileUtility.getFileNameWithOutExtension(cf);
           System.out.println("Checking/Creating the sp : " + cfName);
         
           pstmt.setStringVariable("spname", cfName ); // The name of the database to be created.
           pstmt.setBooleanVariable("bRecreate",reCreate); // If the sp has to dropped and created
           pstmt.executeUpdate( new FileInputStream(cf) ); // The actual body of the procedure. Should not contain, create-stored.... statements
           pstmt.clearVariables();
           
       } catch ( Exception e ) {
           System.out.println("Exception occurred while in TL ");
           e.printStackTrace();
       } finally {
           if ( con != null ) {
               try {
                   if ( pstmt != null ) {
                       pstmt.close();
                   }
                   TLConnectionPool.getTLConnectionPool().returnConnection(con);
               } catch ( Exception e ) {
                   System.out.println("Exception while returning the coneection to pool");
               }
           }
       }
   }


		  
     public static void main(String[] arg) {
    	ESBTigerLogicDB esbdb;
	
			esbdb = new ESBTigerLogicDB("config/xdb/esbdbconfig.xml");
	
    	try {
    		esbdb.create();
    		esbdb.load();
    	} catch (Exception e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
     }


}