/*
 * Created on Jan 15, 2006
 *
 * FIXME To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
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

 

package com.rdta.catalog.trading.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.rdta.Admin.Utility.Helper;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

/**
 * @author Arun Kumar
 *
 * FIXME To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class DisplayProducts extends Action{
	private static Log log = LogFactory.getLog(DisplayProducts.class);

	private static final QueryRunner queryrunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();

	Connection conn;

	Statement stmt;

	public void TLClose() {
	try {
		log	.info("Closing the TigerLogic Connection in CatalogNewAction..........");
		stmt.close();
		conn.logoff();
		conn.close();
		log.info("Connection Closed !!!!!!!!!!!!");
	} catch (com.rdta.tlapi.xql.XQLConnectionException e) {
		log.error(e);
	} catch (com.rdta.tlapi.xql.XQLException e) {
			log.error(e);
	}
	}

	
	
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	
		
		HttpSession sess = request.getSession();
		Helper helper = new Helper();
		String clientIP = request.getRemoteAddr();
		
		conn = helper.ConnectTL();
		stmt = helper.getStatement(conn);
		log.info("Validating The Session");

		// Validating Session
		String sessionID = (String) sess.getAttribute("sessionID");
		log.info("sessionID in Action is :" + sessionID);
		String validateResult = helper.ValidateUserSession(stmt, sessionID,
				clientIP);
		log.info("ValidateResult *** =" + validateResult);
		if (!(validateResult.equals("VALID"))) {
			//return a forward to login page.
			TLClose();
			return mapping.findForward("loginPage");
		}	
		
		
		
						String catalogName = request.getParameter("catalogName");
					     List  showList =	queryrunner.executeQuery(" tlsp:getProductList ( '"+catalogName+"')" );
					     request.setAttribute("catalogName",catalogName);
					     if(showList != null){
					  request.setAttribute("ProductMasterListInfo",showList);
					   }
					   
						return mapping.findForward("success");
	}
	

}
