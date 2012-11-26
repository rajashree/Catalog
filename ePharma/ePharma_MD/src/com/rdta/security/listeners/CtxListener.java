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

 
package com.rdta.security.listeners;

import java.util.Hashtable;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rdta.Admin.User.AddUser;
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;

public class CtxListener implements ServletContextListener{
	
	static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	static final Log log = LogFactory.getLog(CtxListener.class);
	
	public void contextInitialized(ServletContextEvent ctxEvent){
		log.info("In contextInitialized method");
		ctxEvent.getServletContext().setAttribute("currentUsers", new Hashtable());
		
		try {
			log.info("Query:tlsp:deleteSessions_MD()");
            queryRunner.executeUpdate("tlsp:deleteSessions_MD()");
        } catch (PersistanceException e) {
        	log.error("error:"+e);
            e.printStackTrace();
        }
	}
	public void contextDestroyed(ServletContextEvent sce){
		
		log.info("In contextDestroyed method");
		try {
			log.info("Query:tlsp:deleteSessions_MD()");
            queryRunner.executeUpdate("tlsp:deleteSessions_MD()");
        } catch (PersistanceException e) {
        	log.error("error:"+e);
            e.printStackTrace();
        }
		
		
	}	
}

