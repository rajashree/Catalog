
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

 package com.rdta.commons;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;

public class PedigreeUtil {

	private static Log log = LogFactory.getLog(PedigreeUtil.class);
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	public static String createPedId() throws PersistanceException{
		
		log.info("Inside PedigreeUtil Class............ ");		
		StringBuffer buffer = new StringBuffer();
		buffer.append("tlsp:PedIdForFax()");
		String pedId = queryRunner.returnExecuteQueryStringsAsString(buffer.toString());
		System.out.println("pedId inside PedigreeUtil Class is :"+pedId);
		log.info("pedId inside PedigreeUtil Class is :"+pedId);
		return pedId;
	}
}
