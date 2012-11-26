
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
import com.rdta.catalog.trading.model.Location;


import org.w3c.dom.Node;

import com.rdta.catalog.Constants;
import com.rdta.catalog.XMLStructure;
import com.rdta.catalog.OperationType;
import com.rdta.commons.persistence.PersistanceException;

 

/**
 * Location information collecting from the reques form.
 * 
 * 
 */
public class LocationAction extends Action
{
    private static Log log=LogFactory.getLog(LocationAction.class);
	
	
    public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response)
								 throws Exception {
 	
    	log.info("********Inside Action LocationAction....... ");
		log.info("Inside Action execute of LocationAction....");
		String operation = request.getParameter("operationType");
		log.debug("Before doing Operation : " + operation);
		Location location = null;
				
		log.info(" Before converting to form!!!! operation : " + operation);
		try{
			
		 
		if(operation != null){
			if(operation.trim().equalsIgnoreCase(OperationType.ADD)) {
				
				log.info(" Inside ADD methods Before converting to form!!!! "); 
				location = new Location(request);
				location.insert(Constants.TRADING_PARTNER_COLL,request.getParameter("tpGenId"));
				request.setAttribute("saveSuccess","true");
			} else if(operation.trim().equalsIgnoreCase(OperationType.UPDATE)) {
				log.debug(" Updating record generated value : " + request.getParameter("locationGenId") );
				location = new Location(request);
				HttpSession session = request.getSession(false);
				if(session != null) {
					TradingPartnerContext context = (TradingPartnerContext)session.getAttribute(Constants.SESSION_TP_CONTEXT);
					String refId = context.getTpGenId();
					location.update(Constants.TRADING_PARTNER_COLL,refId);
					request.setAttribute("updateSuccess","true");
				}
				
			} else if(operation.trim().equalsIgnoreCase(OperationType.FIND)) {
				location = Location.find(request.getParameter("locationGenId"));
			}  else {
				log.warn(" Operation Type : " + operation + " not dealing in this action!");
			}
		}
		
		
		if(location != null) {
			//set the result here
			request.setAttribute("LocationInfo",location.getNode());
			
		}

		log.info("before retuning Success! ");
		return mapping.findForward("success");
		 
    }catch(PersistanceException e){
		log.error("Error in LocationAction execute method........." +e);
		throw new PersistanceException(e);
	}
	catch(Exception ex){			
		ex.printStackTrace();
		log.error("Error in LocationAction execute method........." +ex);
		throw new Exception(ex);
	}
   }
	

   
  
}