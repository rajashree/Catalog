/*
 * Created on Sep 3, 2005
 *
 * TODO To change the template for this generated file go to
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

 

package com.rdta.catalog.gcpim.action;

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
import org.w3c.dom.Node;

import com.rdta.Admin.Utility.Helper;
import com.rdta.catalog.Constants;
import com.rdta.catalog.OperationType;
import com.rdta.catalog.session.TradingPartnerContext;
import com.rdta.catalog.trading.model.Catalog;
import com.rdta.catalog.trading.model.ImageStore;
import com.rdta.catalog.trading.model.TradingPartner;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

/**
 * @author Santosh Subramanya
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class StandardCatalogListAction extends Action {
	private static Log log = LogFactory.getLog(StandardCatalogListAction.class);

	public static String parentNode = new String();

	public Node node6;

	private static final QueryRunner queryrunner = QueryRunnerFactory
			.getInstance().getDefaultQueryRunner();

	Connection conn;

	Statement stmt;

	public void TLClose() {
		try {
			log
					.info("Closing the TigerLogic Connection in SaveDynamicFormAction..........");
			stmt.close();
			conn.logoff();
			conn.close();
			log.info("Connection Closed !!!!!!!!!!!!");
		} catch (com.rdta.tlapi.xql.XQLConnectionException e) {
			System.err.println(e);
		} catch (com.rdta.tlapi.xql.XQLException e) {
			System.err.println(e);
		}
	}

	static int i = 1;

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		log.info("StandardCatalogListAction execute method");
		//	    	set the session to null

		try {
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
			sess.removeAttribute("optype");
			sess.setAttribute("optype","manage");	
			System.out.println("optype is manage");
			List accessList = queryrunner
					.returnExecuteQueryStrings("tlsp:validateAccess('"
							+ sessionID + "','5.05','Read')");
			String readStatus = accessList.get(0).toString();
			if( readStatus.equalsIgnoreCase("false")){
				return mapping.findForward("failure");
			}
			
			
			log.info("readStatus" + readStatus);
			
			List InsertList = queryrunner
					.returnExecuteQueryStrings("tlsp:validateAccess('"
							+ sessionID + "','5.05','Insert')");
			String insertStatus = InsertList.get(0).toString();
			log.info("insertStatus" + insertStatus);
			
			List deleteList = queryrunner
					.returnExecuteQueryStrings("tlsp:validateAccess('"
							+ sessionID + "','5.05','Delete')");
			String deleteStatus = deleteList.get(0).toString();
			log.info("insertStatus" + deleteStatus);
			List updateList = queryrunner
					.returnExecuteQueryStrings("tlsp:validateAccess('"
							+ sessionID + "','5.05','Update')");
			String updateStatus = updateList.get(0).toString();

			List accessList1 = queryrunner
					.returnExecuteQueryStrings("tlsp:validateAccess('"
							+ sessionID + "','5.06','Read')");
			String readStatus1 = accessList1.get(0).toString();
			log.info("readStatus" + readStatus);
			List InsertList1 = queryrunner
					.returnExecuteQueryStrings("tlsp:validateAccess('"
							+ sessionID + "','5.06','Insert')");
			String insertStatus1 = InsertList1.get(0).toString();
			log.info("insertStatus" + insertStatus);
			List deleteList1 = queryrunner
					.returnExecuteQueryStrings("tlsp:validateAccess('"
							+ sessionID + "','5.06','Delete')");
			String deleteStatus1 = deleteList1.get(0).toString();
			log.info("insertStatus" + deleteStatus);
			List updateList1 = queryrunner
					.returnExecuteQueryStrings("tlsp:validateAccess('"
							+ sessionID + "','5.06','Update')");
			String updateStatus1 = updateList1.get(0).toString();

			if (readStatus1.equals("true")) {
				sess.setAttribute("managefileRead", "true");
			} else {
				sess.setAttribute("managefileRead", "false");
			}

			if (insertStatus1.equals("true")) {
				sess.setAttribute("managefileInsert", "true");
			} else {
				sess.setAttribute("managefileInsert", "false");
			}

			if (deleteStatus1.equals("true")) {
				sess.setAttribute("managefileDelete", "true");
			} else {
				sess.setAttribute("managefileDelete", "false");
			}
			if (updateStatus1.equals("true")) {
				log.info("Status ="+updateStatus1);
				sess.setAttribute("managefileUpdate", "true");
			} else {
				sess.setAttribute("managefileUpdate", "false");
			}

			log.info("insertStatus" + updateList);

			if (readStatus.equals("true")) {
				sess.setAttribute("manageRead", "true");
			} else {
				sess.setAttribute("manageRead", "false");
			}

			if (insertStatus.equals("true")) {
				sess.setAttribute("manageInsert", "true");
			} else {
				sess.setAttribute("manageInsert", "false");
			}

			if (deleteStatus.equals("true")) {
				sess.setAttribute("manageDelete", "true");
			} else {
				sess.setAttribute("manageDelete", "false");
			}
			if (updateStatus.equals("true")) {
				sess.setAttribute("manageUpdate", "true");
			} else {
				sess.setAttribute("manageUpdate", "false");
			}

			//	log.info(" Inside Standard Catalog List Action,
			// tpGenId: " + request.getParameter("GenId"));

			//List list =
			// Catalog.getStandardCatalogList(Constants.TRADING_PARTNER_COLL,
			// request.getParameter("GenId") );
			List list = Catalog.getStandardCatalogList(
					Constants.STD_TRADING_PARTNER_COLL, null);

			//set the result here
			
			request.setAttribute("StandardCatalogListInfo", list);

			if (sess != null) {
				sess.setAttribute("typeop", "");
				TradingPartnerContext context = (TradingPartnerContext) sess
						.getAttribute(Constants.SESSION_TP_CONTEXT);
				if (context == null) {
					context = new TradingPartnerContext();
				}
				if (context == null) {
					log.info("Context is NULL ");
				}

				//set to session
				sess.setAttribute(Constants.SESSION_TP_CONTEXT, context);
			}

			System.out
					.println("*********************************Before Return from execute method"
							+ list.size());
			sess.setAttribute("newpage","no");
			return mapping.findForward("success");
		} catch (Exception ex) {
			ex.printStackTrace();
			log.info("Exception in StandardCatalogListAction class");
			return mapping.findForward("exception");

		}
	}
}
