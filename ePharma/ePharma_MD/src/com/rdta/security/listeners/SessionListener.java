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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.security.User;

public class SessionListener implements HttpSessionListener{
	
	static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	static final Log log = LogFactory.getLog(SessionListener.class);
	
	public void sessionCreated(HttpSessionEvent se){
	
	}
	
	public void sessionDestroyed(HttpSessionEvent se){
		
		log.info("In sessionDestroyed method");
		
		HttpSession session = se.getSession();
		
		try {
			log.info("Query:tlsp:deleteInvlidSessionID_MD('"+session.getAttribute("sessionID")+"')");
			queryRunner.executeUpdate("tlsp:deleteInvlidSessionID_MD('"+session.getAttribute("sessionID")+"')");
        } catch (PersistanceException e) {
        	log.error("error:"+e);
            e.printStackTrace();
        }
		
		
		//remove user from list of users logged in
		ServletContext ctx = session.getServletContext();
		Hashtable currentUsers = (Hashtable)ctx.getAttribute("currentUsers");
		currentUsers.remove(session.getAttribute("web_user"));
		
		
	}
	
	
}

