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

//java imports
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.sql.ResultSetMetaData;
import java.util.List;
import java.util.ArrayList;

//w3c imports
import org.w3c.dom.Node;

//TL imports
import com.rdta.commons.persistence.pool.SQLConnectionPool;
import com.rdta.commons.persistence.pool.TLConnectionPool;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.QueryOption;
import com.rdta.tlapi.xql.ResultSet;
import com.rdta.tlapi.xql.Statement;
import com.rdta.tlapi.xql.XQLException;
import com.rdta.util.io.StreamHelper;

/**
 * TLQuery Runner class used for querying Tiger Logic database.
 * 
 * @author Srinivasa Raju Implemented By Muhammed Yaseen
 * @version 1.0
 * @created 02-Jun-2005 7:14:41 PM
 */
public class SQLQueryRunner implements QueryRunner {

	/**
	 * Run the query aganist TL and return result as list of strings.
	 * 
	 * @param xquery
	 * @return
	 * @throws PersistanceException
	 */
	public List returnExecuteQueryStrings(String xquery)
			throws PersistanceException {

		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		List list = new ArrayList(5);
		try {
			conn = TLConnectionPool.getTLConnectionPool().borrowConnection();
			statement = conn.createStatement();
			statement.setQueryOption(QueryOption.XMLSPACE, "preserve");
			resultSet = statement.execute(xquery);
			while (resultSet.next()) {
				list.add(resultSet.getString());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new PersistanceException(ex);
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (statement != null)
					statement.close();
				TLConnectionPool.getTLConnectionPool().returnConnection(conn);
			} catch (Exception ex) {
			}
		}
		return list;
	}

	public List returnExecuteQueryStringsNew(String xquery,Connection conn)
	throws PersistanceException {

//Connection conn = null;
Statement statement = null;
ResultSet resultSet = null;
List list = new ArrayList(5);
try {
	//conn = TLConnectionPool.getTLConnectionPool().borrowConnection();
	statement = conn.createStatement();
	statement.setQueryOption(QueryOption.XMLSPACE, "preserve");
	resultSet = statement.execute(xquery);
	while (resultSet.next()) {
		list.add(resultSet.getString());
	}
} catch (Exception ex) {
	ex.printStackTrace();
	throw new PersistanceException(ex);
} finally {
	try {
		if (resultSet != null)
			resultSet.close();
		if (statement != null)
			statement.close();
		//TLConnectionPool.getTLConnectionPool().returnConnection(conn);
	} catch (Exception ex) {
	}
}
return list;
}
	
	public String returnExecuteQueryStringsAsString(String xquery)
			throws PersistanceException {

		java.sql.Connection conn = null;
		java.sql.Statement statement = null;
		java.sql.ResultSet rs = null;
		List list = new ArrayList(5);
		StringBuffer buf = new StringBuffer();
		StringBuffer result = new StringBuffer();
		try {
			conn = SQLConnectionPool.getSQLConnectionPool().borrowConnection();
			statement = conn.createStatement();
			//statement.setQueryOption(QueryOption.XMLSPACE, "preserve");
			//rs = statement.executeQuery(xquery);
			rs = statement.executeQuery(xquery);
			ResultSetMetaData rsmd = rs.getMetaData();
			int numberOfColumns = rsmd.getColumnCount();
			boolean b = rsmd.isSearchable(1);
			System.out.println("result set : " + rs);
			int i = 0;

			while (rs.next()) {

				for (int k = 1; k <= numberOfColumns; k++) {
					StringBuffer strb = new StringBuffer();
					//strb.append( new BufferedReader( new InputStreamReader(
					// rs.getAsciiStream( 1 ) ) ).readLine() );
					strb.append(rs.getString(rsmd.getColumnName(k)));
					result.append(strb.toString());
				}

			}
			System.out.println("result in SQLQueryRunner : " + result.toString());

			//			Statement statement = con.createStatement(
			// ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY );
			//            rs = statement.executeQuery( FORCLAUSEQUERY );
			//            while( rs.next() )
			//            {
			//                StringBuffer strb = new StringBuffer();
			//                strb.append( new BufferedReader( new InputStreamReader(
			// rs.getAsciiStream( 1 ) ) ).readLine() );
			//                System.out.print( strb.toString() );
			//            }

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new PersistanceException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (statement != null)
					statement.close();
				SQLConnectionPool.getSQLConnectionPool().returnConnection(conn);
			} catch (Exception ex) {
			}
		}
		return result.toString();
	}

	public String returnExecuteQueryStringsAsStringNew(String xquery,Connection conn1)
	throws PersistanceException {

java.sql.Connection conn = null;
java.sql.Statement statement = null;
java.sql.ResultSet rs = null;
List list = new ArrayList(5);
StringBuffer buf = new StringBuffer();
StringBuffer result = new StringBuffer();
try {
	conn = SQLConnectionPool.getSQLConnectionPool().borrowConnection();
	statement = conn.createStatement();
	//statement.setQueryOption(QueryOption.XMLSPACE, "preserve");
	//rs = statement.executeQuery(xquery);
	rs = statement.executeQuery(xquery);
	ResultSetMetaData rsmd = rs.getMetaData();
	int numberOfColumns = rsmd.getColumnCount();
	boolean b = rsmd.isSearchable(1);
	System.out.println("result set : " + rs);
	int i = 0;

	while (rs.next()) {

		for (int k = 1; k <= numberOfColumns; k++) {
			StringBuffer strb = new StringBuffer();
			//strb.append( new BufferedReader( new InputStreamReader(
			// rs.getAsciiStream( 1 ) ) ).readLine() );
			strb.append(rs.getString(rsmd.getColumnName(k)));
			result.append(strb.toString());
		}

	}
	System.out.println("result in SQLQueryRunner : " + result.toString());

	//			Statement statement = con.createStatement(
	// ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY );
	//            rs = statement.executeQuery( FORCLAUSEQUERY );
	//            while( rs.next() )
	//            {
	//                StringBuffer strb = new StringBuffer();
	//                strb.append( new BufferedReader( new InputStreamReader(
	// rs.getAsciiStream( 1 ) ) ).readLine() );
	//                System.out.print( strb.toString() );
	//            }

} catch (Exception ex) {
	ex.printStackTrace();
	throw new PersistanceException(ex);
} finally {
	try {
		if (rs != null)
			rs.close();
		if (statement != null)
			statement.close();
		SQLConnectionPool.getSQLConnectionPool().returnConnection(conn);
	} catch (Exception ex) {
	}
}
return result.toString();
}
	/**
	 * Executes query with input stream.
	 * 
	 * @param xquery
	 */
	public void executeQueryWithStream(String xquery, InputStream inputStream)
			throws PersistanceException {

		Connection conn = null;
		Statement statement = null;

		List arrayList = new ArrayList(5);

		System.out.println(" runQuery : " + xquery);
		try {
			conn = TLConnectionPool.getTLConnectionPool().borrowConnection();
			statement = conn.createStatement();
			statement.setQueryOption(QueryOption.XMLSPACE, "preserve");
			statement.execute(xquery, inputStream);

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new PersistanceException(ex);
		} finally {
			try {
				if (statement != null)
					statement.close();
				TLConnectionPool.getTLConnectionPool().returnConnection(conn);
			} catch (Exception ex) {
			}
		}

	}

	/**
	 * Returns result as a list of inputstreams.
	 * 
	 * @param xquery
	 */
	public List executeQuery(String xquery) throws PersistanceException {

		InputStream stream = null;
		ResultSet resultSet = null;
		Connection conn = null;
		Statement statement = null;
		byte[] data = null;

		List arrayList = new ArrayList();

		System.out.println(" runQuery : " + xquery);
		try {
			conn = TLConnectionPool.getTLConnectionPool().borrowConnection();
			statement = conn.createStatement();
			statement.setQueryOption(QueryOption.XMLSPACE, "preserve");
			resultSet = statement.execute(xquery);
			//we need to call next before taking
			//out any result from result set
			while (resultSet.next()) {
				data = StreamHelper.copy(resultSet.getStream());
				stream = new ByteArrayInputStream(data);
				arrayList.add(stream);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new PersistanceException(ex);
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (statement != null)
					statement.close();
				TLConnectionPool.getTLConnectionPool().returnConnection(conn);
			} catch (Exception ex) {
			}
		}

		return arrayList;
	}

	/**
	 * Returns max value of specified xpath in the collection.
	 * 
	 * @param colName
	 */
	public long executeMaxQuery(String dataBaseName, String colName,
			String xpath) throws PersistanceException {

		ResultSet resultSet = null;
		Connection conn = null;
		Statement statement = null;
		long i = 0;

		try {
			conn = TLConnectionPool.getTLConnectionPool().borrowConnection();
			statement = conn.createStatement();
			statement.setQueryOption(QueryOption.XMLSPACE, "preserve");
			String xquery = "max( collection('tig:///" + dataBaseName + "/"
					+ colName + "')/" + xpath + ")";
			resultSet = statement.execute(xquery);
			//we need to call next before taking
			//out any result from result set
			while (resultSet.next()) {
				i = (long) resultSet.getFloat();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new PersistanceException(ex);
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (statement != null)
					statement.close();
				TLConnectionPool.getTLConnectionPool().returnConnection(conn);
			} catch (Exception e) {
			}
		}

		return i;
	}

	/**
	 * Returns numbers of documents inside specified collection
	 * 
	 * @param colName
	 */
	public int executeCountQuery(String dataBaseName, String colName)
			throws PersistanceException {

		ResultSet resultSet = null;
		Connection conn = null;
		Statement statement = null;
		int i = 0;

		try {
			conn = TLConnectionPool.getTLConnectionPool().borrowConnection();
			statement = conn.createStatement();
			statement.setQueryOption(QueryOption.XMLSPACE, "preserve");
			String xquery = "count( collection('tig:///" + dataBaseName + "/"
					+ colName + "') )";
			resultSet = statement.execute(xquery);
			//we need to call next before taking
			//out any result from result set
			while (resultSet.next()) {
				i = resultSet.getInt();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new PersistanceException(ex);
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (statement != null)
					statement.close();
				TLConnectionPool.getTLConnectionPool().returnConnection(conn);
			} catch (Exception e) {
			}
		}

		return i;
	}

	/**
	 * Execute specified query in the Tiger Logic.
	 * 
	 * @param xquery
	 */
	public void executeUpdate(String xquery) throws PersistanceException {
		Connection conn = null;
		try {
			conn = TLConnectionPool.getTLConnectionPool().borrowConnection();
			executeUpdate(conn, xquery);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new PersistanceException(ex);
		} finally {
			try {
				TLConnectionPool.getTLConnectionPool().returnConnection(conn);
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * Executes update query aganist Tiger Logic.
	 * 
	 * @param conn
	 * @param xquery
	 */
	public void executeUpdate(Connection conn, String xquery)
			throws PersistanceException {

		//	System.out.println(" UpdateQuery : " + xquery);
		Statement statement = null;
		try {
			statement = conn.createStatement();
			statement.setQueryOption(QueryOption.XMLSPACE, "preserve");
			statement.executeUpdate(xquery);
		} catch (XQLException ex) {
			ex.printFullLog();
			throw new PersistanceException(ex.getCause());
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (Exception e) {
			}
		}
	}

}