
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

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

import com.rdta.commons.persistence.TLDBConfig;
import com.rdta.tlapi.xql.DataSource;
import com.rdta.tlapi.xql.DataSourceFactory;
import com.rdta.tlapi.xql.Connection;

/**
 * Tiger Logic Connection pool class.
 * 
 * @author Srinivasa Raju Vegeraju - Leo Fernandez
 * 
 */
public class  TLConnectionPool
{
	public static final int DEFAULT_MAX_ACTIVE = 10;
	 
	public static final int DEFAULT_MAX_IDLE = 10; 
	 
	//wait for 60 sec before throwing exception
	public static final long DEFAULT_MAX_WAIT = 60000l;
	 
	private GenericObjectPool connPool;
	private static TLConnectionPool tlConnPool;
	
	/**
	 * Constructor
	 *
	 */
	private TLConnectionPool() {
		//TODO
		//get the connection parameter 
		
		//pass connection paramters dynamically
		TLConnectionFactory fact = new TLConnectionFactory();
		connPool = new GenericObjectPool(fact);
		
	}
		
	/**
	 * Return Tiger logic connection pool instance
	 * 
	 * @return
	 */
	public synchronized static TLConnectionPool getTLConnectionPool() {
		if ( tlConnPool == null ) {
			 tlConnPool = new TLConnectionPool ();
			 tlConnPool.setMaxActive(DEFAULT_MAX_ACTIVE);
			 tlConnPool.setMaxIdle(DEFAULT_MAX_IDLE);
			 tlConnPool.setMaxWait(DEFAULT_MAX_WAIT);
		}
		return tlConnPool;
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
	private class  TLConnectionFactory implements PoolableObjectFactory
	{
		private String hostName = null;   
		private String port 	= null;
	    private String userName = null;
	    private String password = null;
		
		private DataSource dataSource;
	
		public TLConnectionFactory(){
			TLDBConfig tldbconfig = TLDBConfig.getInstance();
			this.hostName = tldbconfig.getHostName();
			this.port 	  = tldbconfig.getPortNumber();
			this.userName = tldbconfig.getUserName();
			this.password = tldbconfig.getPassword();
		}
		
		public TLConnectionFactory(String hostName, String port, String userName, String password) {
			this.hostName = hostName;
			this.port 	  = port;
			this.userName = userName;
			this.password = password;
		}
		
		private String getDatabaseUrl() {
			
			StringBuffer buff= new StringBuffer("xql:rdtaxql://");
			buff.append(hostName);
			buff.append(":");
			buff.append(port);
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
			
			dataSource = DataSourceFactory.getDataSource(getDatabaseUrl(),null);
            return dataSource.getConnection(userName,password);
		}

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
