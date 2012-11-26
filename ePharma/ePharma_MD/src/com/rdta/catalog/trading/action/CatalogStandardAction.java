/*
 * Created on Sep 4, 2005
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

 

package com.rdta.catalog.trading.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.rdta.catalog.Constants;
import com.rdta.catalog.OperationType;
import com.rdta.catalog.session.TradingPartnerContext;
import com.rdta.catalog.trading.model.Catalog;
import com.rdta.commons.xml.XMLUtil;

/**
 * @author Santosh Subramanya
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CatalogStandardAction extends Action {
	
	 private static Log log=LogFactory.getLog(CatalogStandardAction.class);
		
		
	    public ActionForward execute(ActionMapping mapping, ActionForm form,
									 HttpServletRequest request, HttpServletResponse response)
									 throws Exception {
	 	log.info("************In the CatalogStandardAction**********");
	    	
	    	log.info("Inside Action CatalogStandardAction....... ");
			log.info("Inside Action execute of CatalogStandardAction....");
			try{
			String operation = request.getParameter("operationType");
			log.debug("Before doing Operation : " + operation);
			Catalog catalog = null;
					
			log.info(" Befoe converting to form!!!! operation : " + operation);
							
			if(operation != null){
				if(operation.trim().equalsIgnoreCase(OperationType.ADD)) {
					
					log.info(" Inside ADD methods Befoe converting to form!!!! "); 
					catalog = new Catalog(request);
					HttpSession session = request.getSession(false);
					if(session != null) {
						TradingPartnerContext context = (TradingPartnerContext)session.getAttribute(Constants.SESSION_TP_CONTEXT);
						String tpName = context.getTpName();
						//store the name of the trading partner
						XMLUtil.putValue(catalog.getNode(),"tradingPartnerName",tpName);
						
						//Here is the code Where we pass the standard catalog ......
						catalog.insert(Constants.STD_TRADING_PARTNER_COLL,request.getParameter("tpGenId"));
					}
				
				} else if(operation.trim().equalsIgnoreCase(OperationType.UPDATE)) {
					log.debug(" Updating record generated value : " + request.getParameter("catalogGenId") );
					catalog = new Catalog(request);
								
					HttpSession session = request.getSession(false);
					if(session != null) {
						
						TradingPartnerContext context = (TradingPartnerContext)session.getAttribute(Constants.SESSION_TP_CONTEXT);
						String refId = context.getTpGenId();
						String tpName = context.getTpName();
						//store the name of the trading partner
						XMLUtil.putValue(catalog.getNode(),"tradingPartnerName",tpName);
						catalog.update(Constants.STD_TRADING_PARTNER_COLL,refId);
					}
					
				} else if(operation.trim().equalsIgnoreCase(OperationType.FIND)) {
					
					log.info("***************************CATALOGNAME IN FIND");
					
					catalog = Catalog.find(request.getParameter("catalogGenId"));
						log.info("CAtalog Name ="+catalog);
						log.info("CAtalog Name ="+catalog);
				}  else {
					log.warn(" Operation Type : " + operation + " not dealing in this action!");
				}
			}
			
			
			if(catalog != null) {
				//set the result here
					request.setAttribute("CatalogInfo",catalog.getNode());
				
			}

			log.info("before retuning Success! ");
			}
			catch(Exception ex){
				ex.printStackTrace();
				log.error("Error in CatalogStandardAction execute()" + ex);
				return mapping.findForward("exception");
			}
			return mapping.findForward("success");
	   }
		


}