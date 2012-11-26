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


import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rdta.catalog.session.TradingPartnerContext;
import com.rdta.catalog.trading.model.Catalog;


import org.w3c.dom.Node;

import com.rdta.catalog.Constants;
import com.rdta.catalog.XMLStructure;
import com.rdta.catalog.OperationType;
import com.rdta.commons.xml.XMLUtil;

 

/**
 * Catalog information collecting from the reques form.
 * 
 * 
 */
public class CatalogAction1 extends Action
{
    private static Log log=LogFactory.getLog(CatalogAction1.class);
	
	
    public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response)
								 throws Exception {
 	
		String operation = request.getParameter("operationType");
		log.debug("Before doing Operation : " + operation);
		Catalog catalog = null;
				
		System.out.println(" Befoe converting to form!!!! operation : " + operation);
						
		if(operation != null){
			if(operation.trim().equalsIgnoreCase(OperationType.ADD)) {
				
				System.out.println(" Inside ADD methods Befoe converting to form!!!! "); 
				catalog = new Catalog(request);
				HttpSession session = request.getSession(false);
				if(session != null) {
					TradingPartnerContext context = (TradingPartnerContext)session.getAttribute(Constants.SESSION_TP_CONTEXT);
					String tpName = context.getTpName();
					//store the name of the trading partner
					XMLUtil.putValue(catalog.getNode(),"tradingPartnerName",tpName);
					catalog.insert(Constants.TRADING_PARTNER_COLL,request.getParameter("tpGenId"));
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
					catalog.update(Constants.TRADING_PARTNER_COLL,refId);
				}
				
			} else if(operation.trim().equalsIgnoreCase(OperationType.FIND)) {
				catalog = Catalog.find(request.getParameter("catalogGenId"));
			}  else {
				log.warn(" Operation Type : " + operation + " not dealing in this action!");
			}
		}
		
		
		if(catalog != null) {
			//set the result here
			request.setAttribute("CatalogInfo",catalog.getNode());
			
		}

		System.out.println("before retuning Success! ");
		return mapping.findForward("success");
   }
	

   
  
}