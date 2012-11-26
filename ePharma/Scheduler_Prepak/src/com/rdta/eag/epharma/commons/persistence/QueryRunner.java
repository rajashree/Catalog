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

 
package com.rdta.eag.epharma.commons.persistence;

//java imports
import java.io.InputStream;
import java.util.List;

//w3c imports
import org.w3c.dom.Node;


//TL imports
import com.rdta.tlapi.xql.Connection;


/**
 * Query Runner interface is used for querying database.
 * @author Srinivasa Raju
 * @version 1.0
 * @created 02-Jun-2005 7:14:38 PM
 */
public interface QueryRunner {

	/**
	 * Returns list of inputstreams.
	 * 
	 * @param xquery
	 */
	public List executeQuery(String xquery) throws PersistanceException;

	
	/**
	 * Executes query with input stream.
	 * 
	 * @param xquery
	 */
	public void executeQueryWithStream(String xquery, InputStream inputStream) throws PersistanceException;
	
	public String returnExecuteQueryStringsAsString(String xquery) throws PersistanceException;	
	
	public String returnExecuteQueryStringsAsStringNew(String xquery,Connection conn) throws PersistanceException;
	
	public String returnExecuteQueryStreamAsStringNew(String xquery,Connection conn, InputStream ins) throws PersistanceException;
	
		
	/**
	 * Run the query aganist TL and return result as list of strings.
	 * 
	 * @param xquery
	 * @return
	 * @throws PersistanceException
	 */
	public List returnExecuteQueryStrings(String xquery ) throws PersistanceException;
	
	public List returnExecuteQueryStringsNew(String xquery,Connection conn ) throws PersistanceException;
	
	public List returnExecuteQueryWithStreamStringsNew(String xquery,Connection conn, InputStream inputStream) throws PersistanceException;
	
	/**
	 * Returns max value of specified xpath in the collection.
	 * 
	 * @param colName
	 */
	public long executeMaxQuery(String dataBaseName,String colName,String xpath) throws PersistanceException;


	/**
	 * Returns numbers of documents inside specified collection 
	 *
	 * @param colName
	 */
	public int executeCountQuery(String dataBaseName,String colName) throws PersistanceException;;

	/**
	 * Execute specified query.
	 *
	 * @param xquery
	 */
	public void executeUpdate(String xquery) throws PersistanceException;

	/**
	 * Execute specified query.
	 *
	 * @param conn
	 * @param xquery
	 */
	public void executeUpdate(Connection conn, String xquery) throws PersistanceException;


}