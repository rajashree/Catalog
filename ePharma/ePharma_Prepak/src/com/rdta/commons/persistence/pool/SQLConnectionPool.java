
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

 package com.rdta.commons.persistence.pool;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.sql.DataSource;

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import com.microsoft.jdbcx.sqlserver.SQLServerDataSource;

import com.rdta.commons.persistence.SQLDBConfig;
//import com.rdta.tlapi.xql.DataSource;
//import com.rdta.tlapi.xql.DataSourceFactory;
//import com.rdta.tlapi.xql.Connection;

/**
 * Tiger Logic Connection pool class.
 * 
 * @author Srinivasa Raju Vegeraju - Leo Fernandez
 * 
 */
public class  SQLConnectionPool
{
	public static final int DEFAULT_MAX_ACTIVE = 10;
	 
	public static final int DEFAULT_MAX_IDLE = 10; 
	 
	//wait for 60 sec before throwing exception
	public static final long DEFAULT_MAX_WAIT = 60000l;
	 
	private GenericObjectPool connPool;
	private static SQLConnectionPool sqlConnPool;
	
	/**
	 * Constructor
	 *
	 */
	private SQLConnectionPool() {
		//TODO
		//get the connection parameter 
		
		//pass connection paramters dynamically
		SQLConnectionFactory fact = new SQLConnectionFactory();
		connPool = new GenericObjectPool(fact);
		
	}
		
	/**
	 * Return Tiger logic connection pool instance
	 * 
	 * @return
	 */
	public synchronized static SQLConnectionPool getSQLConnectionPool() {
		if ( sqlConnPool == null ) {
			sqlConnPool = new SQLConnectionPool ();
			sqlConnPool.setMaxActive(DEFAULT_MAX_ACTIVE);
			sqlConnPool.setMaxIdle(DEFAULT_MAX_IDLE);
			sqlConnPool.setMaxWait(DEFAULT_MAX_WAIT);
		}
		return sqlConnPool;
	}

	/**
	 * Borrow connection from the pool
	 * 
	 * @return Connection
	 * @throws Exception
	 */
	public Connection borrowConnection() throws Exception {
		
		//TODO
		//Just before returning make proxy of the connection..
		//so that close method on proxy won't close the actuval connection.
		return (Connection)connPool.borrowObject();
	}
		
	/**
	 * Return connection to the pool
	 * 
	 * @param Connection
	 * @throws Exception
	 */
	public void returnConnection(Connection con) throws Exception {
		//TODO
		//when returning proxy connection remove proxy and return
		if ( con != null ) { // added a check for CON
			connPool.returnObject(con);
		}	
	}
	
	/**
	 * Get maximum connections should active at a time.
	 * 
	 * @param active
	 */
	public int getMaxActive() {
		return connPool.getMaxActive();
	}


	/**
	 * Set maximum connections should active at a time.
	 * 
	 * @param active
	 */
	public void setMaxActive(int active) {
		connPool.setMaxActive(active);
	}
	

	/**
	 * Get maximum idle connections.
	 * 
	 * @param idle
	 */
	public int getMaxIdle() {
		return connPool.getMaxIdle();
	}

	
	/**
	 * Set maximum idle connections.
	 * 
	 * @param idle
	 */
	public void setMaxIdle(int idle) {
		connPool.setMaxIdle(idle);
	}


	/**
	 * Gets the maximum amount of time (in milliseconds) the borrowObject()
	 * method should block before throwing an exception 
	 * 
	 * @param wait
	 */
	public long getMaxWait() {
		return connPool.getMaxWait();
	}

	
	
	/**
	 * Sets the maximum amount of time (in milliseconds) the borrowObject()
	 * method should block before throwing an exception 
	 * 
	 * @param wait
	 */
	public void setMaxWait(long wait) {
		connPool.setMaxWait(wait);
	}
	
	
	/**
	 * Factory of Tiger Logic connections
	 * 
	 * @author srinivasa raju vegeraju
	 *
	 */
	private class  SQLConnectionFactory implements PoolableObjectFactory
	{
		private String hostName = null;   
		private String port 	= null;
	    private String userName = null;
	    private String password = null;
	    private String databaseName = null;
		
		private DataSource dataSource;
	
		public SQLConnectionFactory(){
			SQLDBConfig sqldbconfig = SQLDBConfig.getInstance();
			this.hostName = sqldbconfig.getHostName();
			this.port 	  = sqldbconfig.getPortNumber();
			this.userName = sqldbconfig.getUserName();
			this.password = sqldbconfig.getPassword();
			this.databaseName = sqldbconfig.getDatabaseName();
		}
		
		public SQLConnectionFactory(String hostName, String port, String userName, String password, String databaseName) {
			this.hostName = hostName;
			this.port 	  = port;
			this.userName = userName;
			this.password = password;
			this.databaseName = databaseName;
		}
		
		private String getDatabaseUrl() {
			
			StringBuffer buff= new StringBuffer("jdbc:microsoft:sqlserver://");
			buff.append(hostName);
			buff.append(":");
			buff.append(port);
			buff.append(";");
			buff.append("user=");
			buff.append(userName);
			buff.append(";");
			buff.append("password=");
			buff.append(password);
			buff.append(";");
			buff.append("DatabaseName=");
			buff.append(databaseName);
			return buff.toString();
		}
		
		
        /**
         * Destroy connection from the pool
         * 
         */  
	    public void destroyObject(Object obj) throws Exception {
			//call close on connection
			((Connection)obj).close();
		}
		
		
		/**
		 * Creates new connection.
		 */
	    public Object makeObject() throws Exception {
	    	
	    	Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
            Connection con =  DriverManager.getConnection(getDatabaseUrl());
			return con;
		}
	    
	    //"jdbc:microsoft:sqlserver://66.43.93.185:1433;user=sa;password=tigerlogic;DatabaseName=pubs")
	    
		public void activateObject(Object obj) throws Exception {
			//nothing
		}

	    public void passivateObject(Object obj) throws Exception {
			//nothing
		}
	       
	   public boolean validateObject(Object obj) {
			return obj != null;
	   }
	}


	public static void main(String args[]) throws Exception{
		
		
	}
}
