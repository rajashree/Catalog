/*
 * Created on Sep 4, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 *//********************************************************************************

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

import java.util.Enumeration;
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
import com.rdta.catalog.trading.action.CatalogAction1;
import com.rdta.catalog.trading.model.Catalog;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

/**
 * @author Santosh Subramanya
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CatalogStandardAction extends Action {
	
	 private static Log log=LogFactory.getLog(CatalogStandardAction.class);
		private static final QueryRunner queryrunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();

		Connection conn;

		Statement stmt;

		public void TLClose() {
		try {
			log.info("Closing the TigerLogic Connection in CatalogStandardAction..........");
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

	    public ActionForward execute(ActionMapping mapping, ActionForm form,
									 HttpServletRequest request, HttpServletResponse response)
									 throws Exception {
	 	

	    	
	    	Enumeration enum = request.getParameterNames();
	    	while(enum.hasMoreElements()){
	    		String pName = (String )enum.nextElement();
	    	    log.info(" Parameter Name : "+pName);
	    		log.info(" Parameter Value :"+request.getParameter(pName));
	    		
	    		
	    	}
	    	
	    	
	    	
	    	
	    	
	    	

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
			
			String gen =(String) request.getAttribute("genId");
			log.info(" Generation Id is :"+gen);
			if( gen != null){
				log.info(" Genera tion Id is :"+gen);
				request.setAttribute("genId",gen);
			
			}
			
			
			/*	List InsertList = queryrunner.returnExecuteQueryStrings("tlsp:validateAccess('"+sessionID+"','5.05','Insert')");
			String insertStatus=InsertList.get(0).toString();
			request.setAttribute("manageinsertStatus",insertStatus);
			System.out.println("manageinsertStatus"+insertStatus);
			
			List deleteList = queryrunner.returnExecuteQueryStrings("tlsp:validateAccess('"+sessionID+"','5.05','Delete')");
			String deleteStatus=deleteList.get(0).toString();
			request.setAttribute("managedeleteStatus",deleteStatus);
			
			System.out.println("managedeleteStatus"+deleteStatus);
			
			List updateList = queryrunner.returnExecuteQueryStrings("tlsp:validateAccess('"+sessionID+"','5.05','Update')");
			String updateStatus=updateList.get(0).toString();
			request.setAttribute("manageupdateStatus",updateStatus);
			
			
			sess.setAttribute("deleteStatus",deleteStatus);
			sess.setAttribute("insertStatus",insertStatus);
			sess.setAttribute("updateStatus",updateStatus);
			
	    	*/
	    	
	    	
			sess.removeAttribute("savetree");
			sess.setAttribute("savetree","false");
		

	    	
	    	log.info("catalog Name "+request.getParameter("catalogName"));
	    
	    	String operation = request.getParameter("operationType");
	    	
			log.debug("Before doing Operation : " + operation);
			Catalog catalog = null;
					
			log.info(" Befoe converting to form!!!! operation : " + operation);
							
			if(operation != null){
				if(operation.trim().equalsIgnoreCase(OperationType.ADD)) {
					

					log.info(" Inside ADD methods Befoe converting to form!!!! "); 

					catalog = new Catalog(request);
				
					System.out.println("Inside Add operation of CataloStandardAction");
					if(sess != null) {
						
						sess.setAttribute("typeop","");
						TradingPartnerContext context = (TradingPartnerContext)sess.getAttribute(Constants.SESSION_TP_CONTEXT);
						String tpName = context.getTpName();
					//	store the name of the trading partner
						
						//System.out.println("TP NAME "+tpName);
						
						XMLUtil.putValue(catalog.getNode(),"tradingPartnerName",tpName);
						
						//Here is the code Where we pass the standard catalog ......
							
							request.setAttribute("catalogexist","");
					
						if (catalog.search(request.getParameter("catalogName"))){
							
							catalog.insert(Constants.STD_TRADING_PARTNER_COLL,request.getParameter("tpGenId"));
							String catgenid = XMLUtil.getValue(catalog.getNode(),"catalogID");
							request.setAttribute("cataloggenId",catgenid);
							
						}
						else{
							request.setAttribute("catalogexist","exist");
							request.setAttribute("CatExists","yes");
							catalog = Catalog.findByName(request.getParameter("catalogName"));
							String catgenid = XMLUtil.getValue(catalog.getNode(),"catalogID");
							request.setAttribute("cataloggenId",catgenid);
						}
					
					
					
					}
				
				} else if(operation.trim().equalsIgnoreCase(OperationType.UPDATE)) {
					
				
					log.debug(" Updating record generated value : " + request.getParameter("catalogGenId") );
					
					request.setAttribute("cataloggenId",request.getParameter("catalogGenId"));
					catalog = new Catalog(request);
									
					
					log.info(" CATALOG NODE : "+XMLUtil.convertToString(catalog.getNode())+"catalog genId :"+request.getParameter("catalogGenId"));
					
					
					if(sess != null) {
						
						TradingPartnerContext context = (TradingPartnerContext)sess.getAttribute(Constants.SESSION_TP_CONTEXT);
						String refId = context.getTpGenId();
						String tpName = context.getTpName();
						//store the name of the trading partner
						XMLUtil.putValue(catalog.getNode(),"tradingPartnerName",tpName);
						catalog.update(Constants.STD_TRADING_PARTNER_COLL,refId);
					}
					Node timestamp = XMLUtil.getNode(catalog.getNode(),"EAG-TimeStamp");
					String time = XMLUtil.convertToString(timestamp,true);
					log.info("time stamp");
					queryrunner.executeQuery("tlsp:updateDocument('"+request.getParameter("catalogGenId")+"',"+XMLUtil.convertToString(timestamp,true)+")");
					
				} else if(operation.trim().equalsIgnoreCase(OperationType.FIND)) {
					catalog = Catalog.find(request.getParameter("catalogGenId"));
					log.info("catalog genId  :::::: "+request.getParameter("catalogGenId"));
					request.setAttribute("genId",request.getParameter("catalogGenId"));
					if(catalog != null){
						request.setAttribute("CatExists","yes");
						request.setAttribute("cataloggenId",request.getParameter("catalogGenId"));
					}
				}  else {
					log.warn(" Operation Type : " + operation + " not dealing in this action!");
				}
			}
			
			
			if(catalog != null) {
				//set the result here
			
				request.setAttribute("CatalogInfo",catalog.getNode());
				
			}else{
				log.info("Catalog not created -- CatalogStandardAction");
			}

			log.info("before retuning Success! ");
			return mapping.findForward("success");
	   }
		


}