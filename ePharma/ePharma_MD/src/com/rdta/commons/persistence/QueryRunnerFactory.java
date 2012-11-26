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

/**
 * QueryRunnerFactory class will return QueryRunner interface.
 * @author Srinivasa Raju - Leo Fernandez
 * @version 1.0
 * @created 02-Jun-2005 7:14:39 PM
 */
public class QueryRunnerFactory {
	
	/** The queuerunner is used for instance storage */
	private static QueryRunnerFactory queuerunner;
	
	private QueryRunnerFactory(){

	}
	
	/**
	 * Method to generate a <code>QueryRunner</code> object
	 */
	public QueryRunner getQueryRunner(String dbName){
		return new TLQueryRunner();
	}
	
	

	/**
	 * Method to generate a <code>QueryRunner</code> object
	 */
	public QueryRunner getDefaultQueryRunner(){
		return new TLQueryRunner();
	}
	/**
	 * Method to create a new instance of <code>QueryRunnerFactory</code> or send an
	 * existing instance if already created.
	 */
	public static QueryRunnerFactory getInstance(){
		if (queuerunner == null){
			synchronized(QueryRunnerFactory.class){
				queuerunner = new QueryRunnerFactory();
			}
		}
		return queuerunner;
	}
// modified End
}