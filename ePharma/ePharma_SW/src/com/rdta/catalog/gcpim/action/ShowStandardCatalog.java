/*
 * Created on Sep 28, 2005
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

import com.rdta.Admin.Utility.Helper;
import com.rdta.catalog.Constants;
import com.rdta.catalog.trading.action.KitAction;
import com.rdta.catalog.trading.model.Catalog;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

/**
 * @author Arun Kumar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ShowStandardCatalog extends Action {
	private static Log log = LogFactory.getLog(ShowStandardCatalog.class);

	private static final QueryRunner queryrunner = QueryRunnerFactory
			.getInstance().getDefaultQueryRunner();

	Connection conn;

	Statement stmt;

	public void TLClose() {
		try {
			log.info("Closing the TigerLogic Connection in SaveDynamicFormAction..........");
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
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws Exception{
		try {
			log.info("Operation Type"+request.getParameter("operationType"));
			String opType = request.getParameter("operationType");
			boolean flag = false;
			
			log.info(" Inside List Action ");
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
			
			TLClose();
		
			if(opType == null )opType="";
				
				List accessList = queryrunner.returnExecuteQueryStrings("tlsp:validateAccess('"+sessionID+"','5.04','Read')");
				String readStatus=accessList.get(0).toString();
				log.info("readStatus"+readStatus);
				
				if(readStatus.equals("false")){
					
					return mapping.findForward("failure");
					
				}
				
				List InsertList = queryrunner.returnExecuteQueryStrings("tlsp:validateAccess('"+sessionID+"','5.04','Insert')");
				String insertStatus=InsertList.get(0).toString();
				log.info("insertStatus"+insertStatus);
				
				List deleteList = queryrunner.returnExecuteQueryStrings("tlsp:validateAccess('"+sessionID+"','5.04','Delete')");
				String deleteStatus=deleteList.get(0).toString();
				log.info("insertStatus"+deleteStatus);
				
				List updateList = queryrunner.returnExecuteQueryStrings("tlsp:validateAccess('"+sessionID+"','5.04','Update')");
				String updateStatus=updateList.get(0).toString();
				log.info("insertStatus"+updateList);
				if(updateStatus.equals("false")){
					request.setAttribute("editUpdate","No");
				}
				if(deleteStatus.equals("false")){
					request.setAttribute("editDelete","No");
				}
				if(insertStatus.equals("false")){
					request.setAttribute("editInsert","No");
				}
				
				if(readStatus.equalsIgnoreCase("false")){
					request.setAttribute("editRead","No");
				}
				
			

		List list = Catalog.getStandardCatalogList(Constants.CATALOG_COLL);
		request.setAttribute("mastercatalogList",list);
		return mapping.findForward("success");
	
		}catch(Exception ex){
			
			return mapping.findForward("exception");
		}
		
		}

}
