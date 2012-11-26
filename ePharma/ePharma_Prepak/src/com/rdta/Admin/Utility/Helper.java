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

 
package com.rdta.Admin.Utility;

import java.sql.* ;
import java.util.* ;
import java.io.BufferedInputStream ;
import java.io.ByteArrayInputStream ;
import java.io.ByteArrayOutputStream ;
import java.io.IOException ;
import java.io.InputStream ;
import java.util.Properties ;
import org.xml.sax.InputSource ;

import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.tlapi.xql.* ;
import com.rdta.util.io.StreamHelper ;
import com.rdta.tlapi.xql.Connection ;
import com.rdta.tlapi.xql.Statement ;
import com.rdta.tlapi.xql.DataSourceProperties ;
import com.rdta.tlapi.xql.DataSourceFactory ;
import java.text.SimpleDateFormat ;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.* ;

public class Helper {
	private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	public com.rdta.tlapi.xql.Connection ConnectTL() {
		com.rdta.tlapi.xql.Connection connection = null;
		
		String hostname = "localhost";
		//String hostname = "172.16.40.110"; //for testing
		
		
		try {
			// Create database connection
			DataSourceProperties properties = DataSourceFactory.getDataSourceProperties();
			properties.getProperty(DataSourceProperties.REMOTE_HOST).setValue( hostname );
			properties.getProperty(DataSourceProperties.REMOTE_PORT).setValue( "3408" );
			properties.getProperty(DataSourceProperties.LISTEN_PORT).setValue( "3444" );
		
			DataSource dataSource = DataSourceFactory.getDataSource(properties);
			connection = dataSource.getConnection("admin", "admin");
			//statement = connection.createStatement();
			return connection;
		}
		catch(com.rdta.tlapi.xql.XQLException ex) { 
			System.out.print(ex); 
			return connection;
		}
	}
	
	public com.rdta.tlapi.xql.Statement getStatement(com.rdta.tlapi.xql.Connection connection) {

		com.rdta.tlapi.xql.Statement statement = null;
		try {
			statement = connection.createStatement();
			return statement;
		} catch(com.rdta.tlapi.xql.XQLException ex) { 
				System.out.print(ex); 
				return statement;
		}

	}

	public void CloseConnectionTL(com.rdta.tlapi.xql.Connection connection) {
		try {	connection.close();  } catch(XQLException e)   {}
	}
	
	public String ValidateUserSession(com.rdta.tlapi.xql.Statement statement, String sessionID, String clientIP) {

/*		String sessStatus = "";
		byte[] xmlResults = null;
		String strResult = "";

		String xQuery = "for $s in collection('tig://root/EAGRFID/SysSessions/') ";
		xQuery = xQuery + "where $s/session/sessionid = '"+sessionID+"' and $s/session/userip = '"+clientIP+"' ";
		//xQuery = xQuery + "where $s/session/sessionid = '"+sessionID+"'";
		xQuery = xQuery + "return fn:get-minutes-from-dayTimeDuration ";
		xQuery = xQuery + "(xs:dateTime(fn:current-dateTime())- xs:dateTime($s/session/lastuse)) ";
			System.out.println("%%%%%%%%%%%%%%%" +xQuery);
		
		
		xmlResults = ReadTL(statement, xQuery);
		System.out.println("%%%%%%%%%%%%%%%" +xQuery);
		
		if(xmlResults != null) {
			strResult = new String(xmlResults);

			if(Integer.parseInt(strResult)<1) { //IF LAST USED LESS THAN 15 MINUTES SESSION IS OK

				//MAKE SESSION ID
				SimpleDateFormat df = new SimpleDateFormat();

				//SESSION TIME FORMAT: 2000-01-20T12:00:00 
				df.applyPattern("yyyy-MM-dd");
				String screenEnteredDate = df.format(new java.util.Date());
				df.applyPattern("hh:mm:ss");
				String screenEnteredTime = df.format(new java.util.Date());
				String screenEnteredDT = screenEnteredDate+"T"+screenEnteredTime;

				//UPDATE SESSION ENTRY - REFRESH LAST USE ELEMENT
				xQuery = "for $d in collection('tig://root/EAGRFID/SysSessions/') ";
				xQuery = xQuery+"where $d/session[sessionid/text() = '"+sessionID+"'] ";
				xQuery = xQuery+"return ";
				xQuery = xQuery+"let $newDoc := ( ";
				xQuery = xQuery+"<session> ";
				xQuery = xQuery+"  {$d/session/*:*[ . << $d//lastuse]} ";
				xQuery = xQuery+"  <lastuse>"+screenEnteredDT+"</lastuse> ";
				xQuery = xQuery+"  {$d/session/*:*[ . >> $d//lastuse]} ";
				xQuery = xQuery+"</session> ) ";
				xQuery = xQuery+"return tig:replace-document(document-uri( $d ), $newDoc) ";
				System.out.println("%%%%%%%%%%%%%%%" +xQuery);
				xmlResults = ReadTL(statement, xQuery);
				sessStatus = "VALID";
			} else { //SESSION EXPIRED - RELOGIN
				sessStatus = "EXPIRED";
			}
		} else { sessStatus = "EXPIRED"; }*/

		return "VALID";
		}
	public byte[] ReadTL(com.rdta.tlapi.xql.Statement statement, String xQuery) {

		byte[] data = null;

		try {
			com.rdta.tlapi.xql.ResultSet resultSet;

			   resultSet = statement.execute(xQuery);
			   if ( resultSet.next() )
			   {
			       InputStream resultStream = resultSet.getBinaryStream();
			       
			       data = StreamHelper.copy(resultStream);  // get the data

			       resultStream.close();
			       resultSet.close();

			   }
			   else
			   {
			       System.out.println( "Query returned no result." );
			       System.out.println("Query: "+xQuery);
			   }
			   
			  // System.out.println("___xQuery___"+xQuery);

		}
	        catch(com.rdta.tlapi.xql.XQLException ex) { 
	        	System.out.print(ex); 
	        	System.out.print(xQuery);
	        	//data = ex.toString().getBytes(); 
	        }
	        
	        catch(java.io.IOException ioex) {  
	        	System.out.print(ioex); 
	        	System.out.print(xQuery);
	        	//data = ioex.toString().getBytes();	
	        }	 	
	 	return data;       
	}	
	public byte[] ReadTL(InputStream inpst,com.rdta.tlapi.xql.Statement statement, String xQuery) {

		byte[] data = null;

		try {
			com.rdta.tlapi.xql.ResultSet resultSet;

			   resultSet = statement.execute(xQuery,inpst);
			   if ( resultSet.next() )
			   {
			       InputStream resultStream = resultSet.getBinaryStream();
			       
			       data = StreamHelper.copy(resultStream);  // get the data

			       resultStream.close();
			       resultSet.close();

			   }
			   else
			   {
			       System.out.println( "Query returned no result." );
			       System.out.println("Query: "+xQuery);
			   }
			   
			  // System.out.println("___xQuery___"+xQuery);

		}
	        catch(com.rdta.tlapi.xql.XQLException ex) { 
	        	System.out.print(ex); 
	        	System.out.print(xQuery);
	        	//data = ex.toString().getBytes(); 
	        }
	        
	        catch(java.io.IOException ioex) {  
	        	System.out.print(ioex); 
	        	System.out.print(xQuery);
	        	//data = ioex.toString().getBytes();	
	        }	 	
	 	return data;       
	}	
	
	/*
	//add with image
	public byte[] ReadTL(InputStream inpst,com.rdta.tlapi.xql.Statement statement, String xQuery) {

		byte[] data = null;

		try {
			   com.rdta.tlapi.xql.ResultSet resultSet;
			   //resultSet = statement.execute(xQuery);
			   return executeStatementStream(xQuery, inpst,statement);
			}
	        catch(com.rdta.tlapi.xql.XQLException ex) { 
	        	System.out.print(ex); 
	        	System.out.print(xQuery);
	        	//data = ex.toString().getBytes(); 
	        }
	        
	        catch(java.io.IOException ioex) {  
	        	System.out.print(ioex); 
	        	System.out.print(xQuery);
	        	//data = ioex.toString().getBytes();	
	        }
	        catch(Exception ioex) {  
	        	System.out.print(ioex); 
	        	System.out.print(xQuery);
	        	//data = ioex.toString().getBytes();	
	        }	        
	 	return data;       
	}	
	*/
	public void executeStatementStream(com.rdta.tlapi.xql.Statement statement, String query,  ByteArrayInputStream stream) throws Exception
    {
        com.rdta.tlapi.xql.ResultSet resultSet;
        byte[] data = null;
        InputStream resultStream = null;

        resultSet = statement.execute(query, stream);

        if ( resultSet.next() )
        {
            // get the data
            //data = StreamHelper.copy(resultStream);
            //resultStream.close();
            resultSet.close();
        }
        else
        {
            System.out.println( "Query returned no result." );
            System.out.println("Query: "+query);
        }
        //java.io.InputStream isResult = new ByteArrayInputStream(data);
        
    }

	public String ParseUsersForViewUserScreen(byte[] xmlResults, String sessionID) {

		String nodeName = "";
		String userID = "";
		String firstName = "";
		String lastName = "";
		String accessLevel = "";
		String Name = "";
		String ACode = "";
		String Department = "";
		String Facility = "";
		String HTMLROW = "";
		String xmlResultString = "";
			
		try {
			java.io.InputStream isResult = new ByteArrayInputStream(xmlResults);

			//build dom
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder dbb = dbf.newDocumentBuilder();

			org.w3c.dom.Document doc = dbb.parse(isResult);
			NodeList foundusers = doc.getElementsByTagName("user");

			Node usernode;
			Node userchildnode;

			NodeList userchildnodes;
			int numOfResults = foundusers.getLength();

			if(numOfResults>0) {
				for(int i=0;i<numOfResults;i++)  {
					usernode = foundusers.item(i); //get user Node
					nodeName = usernode.getNodeName();

					if(nodeName.equals("user")) {

						userchildnodes=usernode.getChildNodes();
						for(int j=0;j<userchildnodes.getLength();j++)
						{
							userchildnode =  userchildnodes.item(j);
							if (userchildnode != null) {

								nodeName = userchildnode.getNodeName();

								if(nodeName.equals("id")) {
									userID  = userchildnode.getFirstChild().getNodeValue();
								}

								if(nodeName.equals("firstname")) {
									firstName  = userchildnode.getFirstChild().getNodeValue();
								}

								if(nodeName.equals("lastname")) {
									lastName  = userchildnode.getFirstChild().getNodeValue();
								}

								if(nodeName.equals("accesslevel")) {
									accessLevel  = userchildnode.getFirstChild().getNodeValue();
								}

							} // END IF USER CHILD NODE NOT NULL
						} //END FOR USER CHILD NODES
					}  //END IF USER NODE

					//BUILD HTML ROW
					HTMLROW = HTMLROW +"<TR bgcolor=D3E5ED>";
					HTMLROW = HTMLROW + "<TD><A href='NewUser.jsp?userid="+userID+"&sessionID="+sessionID+"'>"+firstName+" "+lastName+"</A></TD>";
					HTMLROW = HTMLROW + "<TD>"+accessLevel+"</TD>";
					HTMLROW = HTMLROW + "</TR>";

				} //END FOR USER NODES

				if( (!userID.equals("")) & (!firstName.equals("")) & (!lastName.equals("")) & (!accessLevel.equals("")) ) {
					// GOT VALID USER INFO, PROCEED TO MAIN PAGE
					xmlResultString = "userID = "+userID+", firstName = "+firstName+", lastName = "+lastName+", accessLevel = "+accessLevel;
				} else {
					// DIDNT GET USER INFO, REDIRECT TO LOGIN FAILED
					xmlResultString = "Parsed but got bad data.";
				}

			} else {
				//redirect URL to LOGIN FAILED
				xmlResultString = "Got not data from TL.";
			}
			
			return HTMLROW;
			
		} catch (javax.xml.parsers.ParserConfigurationException pce) { System.out.print(pce); return HTMLROW; }
			catch (org.xml.sax.SAXException pce) { System.out.print(pce); return HTMLROW; }
			catch (java.io.IOException ioe) { System.out.print(ioe); return HTMLROW; }
		
	}
    public boolean writePicToTL(InputStream fileIS, String locID)
    {
        try {

        String insXML = "<root>";
        insXML = insXML +"<document>{binary {$1}}</document>";
        insXML = insXML +"<locationID>"+ locID + "</locationID>";
        insXML = insXML +"</root>";

        String FullQuery = "declare binary-encoding none; tig:insert-document( 'tig://root/EAGRFID/LocationImage/', "+insXML+" )";
        //executeStatementStreamPic(FullQuery, fileIS);

         return true;

        }    catch(Exception e) {
                    System.err.println(e);
                    return false;
        }
    }

        public void executeStatementStreamPic(String query,  InputStream stream) throws Exception
        {
        
            Connection connection = ConnectTL();
        	Statement statement = getStatement(connection);
        	
            com.rdta.tlapi.xql.ResultSet resultSet;
            byte[] data = null;
            InputStream resultStream = null;
            QueryOption qo = QueryOption.XMLSPACE;
            statement.setQueryOption(qo, "preserve");
            resultSet = statement.execute(query, stream);

            if ( resultSet.next() )
            {
                // get the data
                data = StreamHelper.copy(resultStream);
                resultStream.close();
                resultSet.close();
            }
            else
            {
                System.out.println( "Query returned no result." );
            }
        }	
        
        
    public boolean writeToTL(InputStream fileIS, String locationID,Statement statement)
    {
        try {

        String insXML = "<root>";
        insXML = insXML +"<row>";
        insXML = insXML +"<IMG>"+ "{binary {$1}}"+"</IMG>";
        insXML = insXML +"<locationID>"+locationID+"</locationID>";
        insXML = insXML +"</row>";
        insXML = insXML +"</root>";
        
        String FullQuery = "declare binary-encoding none; tig:insert-document( 'tig://root/EAGRFID/LocationImage/', "+insXML+" )";
        executeStatementStream(FullQuery, fileIS,statement);
        
        return true;

        }    catch(Exception e) {
                    System.out.println("ERROR " + e);
                    return false;
        }

    }   
    
    public boolean writePrdImgToTL(InputStream fileIS, String productID,Statement statement)
    {
        try {
        	String insXML = "<root>";
        	insXML = insXML +"<row>";
        	//String insXML = "<Product>";        	
        	insXML = insXML +"<IMG>"+ "{binary {$1}}"+"</IMG>"; 
        	insXML = insXML +"<ProductID>"+productID+"</ProductID>";
        	//insXML = insXML+"</Product>";
        	insXML = insXML +"</row>";
            insXML = insXML +"</root>";
        
        String FullQuery = "declare binary-encoding none; tig:insert-document( 'tig://root/EAGRFID/ProductImage/', "+insXML+" )";
        executeStatementStream(FullQuery, fileIS,statement);
        
        return true;

        }    catch(Exception e) {
                    System.out.println("ERROR " + e);
                    return false;
        }

    } 
    
    public boolean writeTo2TL(InputStream fileIS, String productID,String productName,String tagID,
    		String tagType,String fromRange,String toRange,String barCode,String manufacturerID,String productCategory,
			String description,int qTYperUnit,float unitPrice,String tlTimestamp,String thresholdRows, Statement statement)
    {
        try {
        	InputStream $1 = fileIS;
        	String xmlF = "<Product>";
        		
    		xmlF = xmlF + "<ProductID>"+productID+"</ProductID>";  
    		xmlF = xmlF + "<ProductName>"+productName+"</ProductName>";
    		xmlF = xmlF + "<TagID>"+tagID+"</TagID>";
    		xmlF = xmlF + "<TagType>"+tagType+"</TagType>";
    		xmlF = xmlF + "<EPCTagRange>";
    		xmlF = xmlF + "<FromRange>"+fromRange+"</FromRange>";
    		xmlF = xmlF + "<ToRange>"+toRange+"</ToRange>";
    		xmlF = xmlF + "</EPCTagRange>";
    		xmlF = xmlF + "<BarCode>"+barCode+"</BarCode>";
    		xmlF = xmlF + "<ManufacturerID>"+manufacturerID+"</ManufacturerID>";
    		xmlF = xmlF + "<ManufacturedDate>2002-10-10T12:00:00-05:00</ManufacturedDate>";
    		xmlF = xmlF + "<ProductCategory>"+productCategory+"</ProductCategory>";
    		xmlF = xmlF + "<Description>"+description+"</Description>";
    		//xmlF = xmlF + "<Threshold>"+description+"</Threshold>";
    		xmlF = xmlF + "<ProdIMG>{binary {$1}}</ProdIMG>";
    		xmlF = xmlF + "<QTYperUnit>"+qTYperUnit+"</QTYperUnit>";
    		xmlF = xmlF + "<UnitPrice>"+unitPrice+"</UnitPrice>";
    		xmlF = xmlF + "<extensions> ";
    		xmlF = xmlF + "	<ext> ";
    		xmlF = xmlF + "	<extType>" + "extType" + "</extType> ";
    		xmlF = xmlF + "	<extValue>" + "extType" + "</extValue> ";
    		xmlF = xmlF + "	</ext> ";
    		xmlF = xmlF + "</extensions> ";
    		xmlF = xmlF + "<EAG-TimeStamp> ";
    		xmlF = xmlF + "	<origin-serverID>1</origin-serverID> ";
    		xmlF = xmlF + "	<updated-timestamp>" + tlTimestamp+ "</updated-timestamp> ";
    		xmlF = xmlF + "	<updated-serverID>0</updated-serverID> ";
    		xmlF = xmlF + "	<reported>true</reported> ";
    		xmlF = xmlF + "</EAG-TimeStamp> ";
    		xmlF = xmlF + "</Product>";
    		
       
        
        String FullQuery = "declare binary-encoding none; tig:insert-document( 'tig://root/EAGRFID/Products/', "+xmlF+" )";
        executeStatementStream(FullQuery, fileIS,statement);
        
        return true;

        }    catch(Exception e) {
                    System.out.println("ERROR " + e);
                    return false;
        }

    }   
    public boolean writeToProdctTL(InputStream fileIS,Map map,String  thresholdRows,Statement statement)
    {
        try {
        	//String xmlF="";
           //  map.size();
            System.out.println(map.get("ProductID"));
           
            
            
            
            // xmlF ="<ProductID>"+map.get("ProductID")+"</ProductId>";
             
     String xmlF = "<Product>";
    		xmlF = xmlF + "<ProductID>"+map.get("ProductID")+"</ProductID>";  
    		xmlF = xmlF + "<ProductName>"+map.get("ProductName")+"</ProductName>";
    		xmlF = xmlF + "<TagID>"+map.get("TagID")+"</TagID>";
    		xmlF = xmlF + "<TagType>"+map.get("TagType")+"</TagType>";
    		xmlF = xmlF + "<EPCTagRange>";
    		xmlF = xmlF + "<FromRange>"+map.get("FromRange")+"</FromRange>";
    		xmlF = xmlF + "<ToRange>"+map.get("ToRange")+"</ToRange>";
    		xmlF = xmlF + "</EPCTagRange>";
    		xmlF = xmlF + "<BarCode>"+map.get("BarCode")+"</BarCode>";
    		xmlF = xmlF + "<ManufacturerID>"+map.get("ManufacturerID")+"</ManufacturerID>";
    		xmlF = xmlF + "<ManufacturedDate>2002-10-10T12:00:00-05:00</ManufacturedDate>";
    		xmlF = xmlF + "<ProductCategory>"+map.get("ProductCategory")+"</ProductCategory>";
    	
    		xmlF = xmlF + "<Description>"+map.get("Description")+"</Description>";
    		//xmlF = xmlF +thresholdRows;
    		xmlF = xmlF + "<ProdIMG>{binary {$1}}</ProdIMG>";	
    		xmlF = xmlF + "<QTYperUnit>"+map.get("QTYperUnit")+"</QTYperUnit>";
    		xmlF = xmlF + "<UnitPrice>"+map.get("UnitPrice")+"</UnitPrice>";
    		xmlF = xmlF + "<extensions> ";
    		xmlF = xmlF + "	<ext> ";
    		xmlF = xmlF + "	<extType>" + "extType" + "</extType> ";
    		xmlF = xmlF + "	<extValue>" + "extType" + "</extValue> ";
    		xmlF = xmlF + "	</ext> ";
    		xmlF = xmlF + "</extensions> ";
    		xmlF = xmlF + "<EAG-TimeStamp> ";
    		xmlF = xmlF + "	<origin-serverID>1</origin-serverID> ";
    		xmlF = xmlF + "	<updated-timestamp>" + map.get("TlTimestamp")+ "</updated-timestamp> ";
    		xmlF = xmlF + "	<updated-serverID>0</updated-serverID> ";
    		xmlF = xmlF + "	<reported>true</reported> ";
    		xmlF = xmlF + "</EAG-TimeStamp> ";
    		xmlF = xmlF + "</Product>";
    		
      
        
        String FullQuery = "declare binary-encoding none; tig:insert-document( 'tig://root/EAGRFID/Products/', "+xmlF+" )";
        executeStatementStream(FullQuery, fileIS,statement);
        
        return true;

        }    catch(Exception e) {
                    System.out.println("ERROR " + e);
                    return false;
        }

    }   
    	
    	
   
   
    
    public boolean writePToTL(InputStream fileIS, String locationID,Statement statement)
    {
        try {

        String insXML = "<root>";
        insXML = insXML +"<row>";
        insXML = insXML +"<IMG>"+ "{binary {$1}}"+"</IMG>";
        insXML = insXML +"</row>";
        insXML = insXML +"</root>";
        
        String FullQuery = "declare binary-encoding none; tig:insert-document( 'tig://root/EAGRFID/Products/', "+insXML+" )";
        executeStatementStream(FullQuery, fileIS,statement);
        
        return true;

        }    catch(Exception e) {
                    System.out.println("ERROR " + e);
                    return false;
        }

    }
    public boolean checkPic(String locationID,Statement statement)
    {
        try {

        String FullQuery = "for $l in collection('tig:///EAGRFID/LocationImage')/root/row ";
        	   FullQuery = FullQuery + " where $l/locationID = '"+ locationID + "'" ;
               FullQuery = FullQuery + " return $l/locationID ";
        System.out.println("FullQuery -> "+FullQuery);
        return executeStatementStream(FullQuery,statement);
       // CloseConnectionTL(connection);
        //return true;

        }    catch(Exception e) {
                    System.out.println("ERROR 2 " + e);
                    return false;
        }

    }    
   
    public boolean checkProdPic(String productID,Statement statement)
    {
        try {
        //root/row	
        String FullQuery = "for $l in collection('tig:///EAGRFID/ProductImage')/root/row ";
        	   FullQuery = FullQuery + " where $l/ProductID ='"+productID+"'" ;
               FullQuery = FullQuery + " return $l/ProductID ";
        System.out.println("FullQuery -> "+FullQuery);
        return executeStatementStream(FullQuery,statement);
       // CloseConnectionTL(connection);
        //return true;

        }    catch(Exception e) {
                    System.out.println("ERROR 2 " + e);
                    return false;
        }

    }    
    
    
//check        
    public boolean executeStatementStream(String query, Statement statement) throws Exception
    {
        String resultString = null;
    	try
		{
            com.rdta.tlapi.xql.ResultSet resultSet;
            byte[] data = null;
            QueryOption qo = QueryOption.XMLSPACE;
            statement.setQueryOption(qo, "preserve");
            resultSet = statement.execute(query);            
            if ( resultSet.next() )
            {
            	resultString = resultSet.getString();
                resultSet.close();
            }
            else
            {
                System.out.println( "Query returned no result." );
            }
		}catch(Exception e)
		{
			System.out.println("Error 3 "+e);
			return false;
		}
        if (resultString != null){
        	return true;
        }else {
        	return false;
        }		
    }
    
        public byte[] executeStatementStream(String query,  InputStream stream, Statement statement) throws Exception
        {
        	byte[] data = null;
        	try
			{
	            com.rdta.tlapi.xql.ResultSet resultSet;
	            InputStream resultStream = null;
	            QueryOption qo = QueryOption.XMLSPACE;
	            statement.setQueryOption(qo, "preserve");
	            System.out.println( "Query returned no result." +query);
	            resultSet = statement.execute(query, stream);
	
	            if ( resultSet.next() )
	            {
	            	resultStream = resultSet.getBinaryStream();
	                // get the data
	                data = StreamHelper.copy(resultStream);
	                resultStream.close();
	                resultSet.close();
	            }
	            else
	            {
	                System.out.println( "Query returned no result." );
	            }
	            return data;
			}catch(Exception e)
			{
				System.out.println("Error  "+e);
				return data;
			}        	
        }            
        
        public boolean checkUserPic(String userID,Statement statement)
        {
            try {

            String FullQuery = "for $l in collection('tig:///EAGRFID/UserSign')/User ";
            	   FullQuery = FullQuery + " where $l/UserID = '"+ userID + "'" ;
                   FullQuery = FullQuery + " return $l/UserID ";
            System.out.println("FullQuery -> "+FullQuery);
            return executeStatementStream(FullQuery,statement);
           // CloseConnectionTL(connection);
            //return true;

            }    catch(Exception e) {
                        System.out.println("ERROR 2 " + e);
                        return false;
            }

        }   
        public String getStoredProcs() throws PersistanceException{
    		StringBuffer bfr = new StringBuffer();
    		bfr.append("declare general-option 'experimental=true';");
    		bfr.append(" let $k :=data(tig:list-stored-procedures()/function/@name) ");
			bfr.append(" for $i in $k return ");
			bfr.append(" <option value ='{substring($i,6,string-length($i))}'>{substring($i,6,string-length($i))}</option> ");
    		
    		String xmlResults =queryRunner.returnExecuteQueryStringsAsString(bfr.toString());	
    		return xmlResults;
    	}
    	
        
    	public String getVocabDetails(String category) throws PersistanceException{
    		StringBuffer bfr = new StringBuffer();
    		bfr.append("declare general-option 'experimental=true';");
    		bfr.append("tlsp:Vocabulary-CategoryDetails('"+category+"')");
    		String xmlResults =queryRunner.returnExecuteQueryStringsAsString(bfr.toString());	
    		return xmlResults;
    	}
    	
    	public String getVocabEventDetails(String category,String termName) throws PersistanceException{
    		StringBuffer bfr = new StringBuffer();
    		bfr.append("declare general-option 'experimental=true';");
    		bfr.append("tlsp:Location-Events('"+category+"','"+termName+"')");
    		String xmlResults =queryRunner.returnExecuteQueryStringsAsString(bfr.toString());	
    		return xmlResults;
    	}
    	
    	public String getEAGTimeStamp() throws PersistanceException{
    		StringBuffer bfr = new StringBuffer();
    		bfr.append("declare general-option 'experimental=true';");
    		bfr.append("tlsp:CreateEAG-TimeStampType()");
    		String xmlResults =queryRunner.returnExecuteQueryStringsAsString(bfr.toString());	
    		return xmlResults;
    	}
    	
    	//TODO
    	public String updateEAGTimeStamp(String orgServerId) throws PersistanceException{
    		StringBuffer bfr = new StringBuffer();
    		bfr.append("declare general-option 'experimental=true';");
    		bfr.append("tlsp:GetEAG-TimeStampTypeForRepository('"+orgServerId+"')");
    		String xmlResults =queryRunner.returnExecuteQueryStringsAsString(bfr.toString());	
    		return xmlResults;
    	}
    	
    	public String getePharmaEAGTimeStamp() throws PersistanceException{
    		StringBuffer bfr = new StringBuffer();
    		bfr.append("declare general-option 'experimental=true';");
    		bfr.append("tlsp:CreateEAG-TimeStampTypeForePharma()");
    		String xmlResults =queryRunner.returnExecuteQueryStringsAsString(bfr.toString());	
    		return xmlResults;
    	}
    	
    	//TODO
    	public String updateePharmaEAGTimeStamp(String orgServerId) throws PersistanceException{
    		StringBuffer bfr = new StringBuffer();
    		bfr.append("declare general-option 'experimental=true';");
    		bfr.append("tlsp:GetEAG-TimeStampTypeForePharma('"+orgServerId+"')");
    		String xmlResults =queryRunner.returnExecuteQueryStringsAsString(bfr.toString());	
    		return xmlResults;
    	}
    	
    	public String validateAccess(String SessionID,String accesslevel,String type)throws PersistanceException{       		    		
    		System.out.println("tlsp:validateAccess('"+SessionID+"','"+accesslevel+"','"+type+"')");
    		List accessList = queryRunner.returnExecuteQueryStrings("tlsp:validateAccess('"+SessionID+"','"+accesslevel+"','"+type+"')");    																				
    		String readStatus = accessList.get(0).toString();
    		System.out.println("validateAccess :"+readStatus);    		
    		return readStatus;
    	}
    	
        
}
