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

 
package com.rdta.rules.Util;


import java.util.List;


//w3c imports
import org.w3c.dom.Node;

import com.rdta.commons.xml.XMLUtil;
import com.rdta.rules.CategoryCode;
import com.rdta.rules.Util.Constants;

import com.rdta.commons.persistence.PersistanceException;

import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Class has Perisitance utility methods.
 *
 * @author Srinivasa Raju
 * @version 1.0
 *
 */
public class PersistanceUtil {

	private static Log log = LogFactory.getLog(PersistanceUtil.class);
	
	private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	public static void insertDocument(Node theNode,String colletionName) throws PersistanceException {
		
		StringBuffer xQuery = new StringBuffer();
		xQuery.append("tig:insert-document('tig:///" + Constants.CATALOG_DB + "/" + colletionName);
		xQuery.append("',");
		xQuery.append(XMLUtil.convertToString(theNode,true) + ")");
		queryRunner.executeQuery(xQuery.toString());
		
	}
	
	
	public static void updateDocument(Node theNode,String colletionName, String condition) throws PersistanceException {
		
		StringBuffer xQuery = new StringBuffer();
		xQuery.append(" for $a in collection('tig:///" + Constants.CATALOG_DB + "/" + colletionName + "/')");
	    xQuery.append(" where " + condition);
		xQuery.append(" return tig:replace-document(document-uri( $a ), "+ XMLUtil.convertToString(theNode,true) +")");

		queryRunner.executeQuery(xQuery.toString());
	}
	
	
	public static List findDocument(String colletionName, String condition) throws PersistanceException {
		
		StringBuffer xQuery = new StringBuffer();
		xQuery.append(" for $a in collection('tig:///" + Constants.CATALOG_DB + "/" + colletionName + "/')");
	    xQuery.append(" where " + condition);
		xQuery.append(" return $a/*");

		return (List)queryRunner.executeQuery(xQuery.toString());
	}
	
}