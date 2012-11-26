
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
public class SQLQueryRunnerFactory {
	
	/** The queuerunner is used for instance storage */
	private static SQLQueryRunnerFactory queuerunner;
	
	private SQLQueryRunnerFactory(){

	}
	
	/**
	 * Method to generate a <code>QueryRunner</code> object
	 */
	public QueryRunner getQueryRunner(String dbName){
		return new SQLQueryRunner();
	}
	
	

	/**
	 * Method to generate a <code>QueryRunner</code> object
	 */
	public QueryRunner getDefaultQueryRunner(){
		return new SQLQueryRunner();
	}
	/**
	 * Method to create a new instance of <code>QueryRunnerFactory</code> or send an
	 * existing instance if already created.
	 */
	public static SQLQueryRunnerFactory getInstance(){
		if (queuerunner == null){
			synchronized(SQLQueryRunnerFactory.class){
				queuerunner = new SQLQueryRunnerFactory();
			}
		}
		return queuerunner;
	}
// modified End
}