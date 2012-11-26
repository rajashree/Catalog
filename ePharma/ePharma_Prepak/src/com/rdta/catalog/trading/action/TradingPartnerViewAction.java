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
import com.rdta.catalog.trading.model.TradingPartner;
import com.rdta.catalog.trading.model.ImageStore;


import org.w3c.dom.Node;

import com.rdta.catalog.Constants;
import com.rdta.catalog.XMLStructure;
import com.rdta.catalog.OperationType;
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.xml.XMLUtil;

 

/**
 * Trading Partner Form information collecting from the reques form.
 * 
 * 
 */
public class TradingPartnerViewAction extends Action
{
    private static Log log=LogFactory.getLog(TradingPartnerViewAction.class);
	
	
    public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response)
								 throws Exception {
    	log.info("Inside Action TradingPartnerViewAction....... ");
		log.info("Inside Action execute of TradingPartnerViewAction....");
		
		
		
		try{
 	
		String operation = request.getParameter("operationType");
		log.debug("Before doing Operation : " + operation);
		TradingPartner trading = null;
		
			
		if(operation != null){
			if(operation.trim().equalsIgnoreCase(OperationType.FIND)) {
				trading = TradingPartner.find(request.getParameter("tpGenId"));
			} else {
				log.warn(" Operation Type : " + operation + " not dealing in this action!");
			}
		}
		
		
		if(trading != null) {
			
			//set the result here
			request.setAttribute("TradingPartnerInfo",trading.getNode());
			ImageStore imageStoreDB = ImageStore.find(Constants.TRADING_PARTNER_COLL, trading.getGenId()  );
				
			if(imageStoreDB != null) {
				String fileName = imageStoreDB.saveAsFile();
				String fileURL = "http://"+request.getServerName()+":"+request.getServerPort()+"/"+ request.getContextPath()+"/images/"+fileName;
				log.info(" Befoe converting to form!!!! 8 fileURL " +  fileURL);
				request.setAttribute("ImageFileLocation",fileURL);
			}
			
			HttpSession session = request.getSession(false);
			if(session != null) {
				
				TradingPartnerContext context = (TradingPartnerContext)session.getAttribute(Constants.SESSION_TP_CONTEXT);
				if(context == null) {
					context = new TradingPartnerContext();
				}
				context.setTpGenId(XMLUtil.getValue(trading.getNode(),"genId"));
				context.setTpName( XMLUtil.getValue(trading.getNode(),"name"));
				//set to session
				session.setAttribute(Constants.SESSION_TP_CONTEXT,context );
			}

		}

		log.info("before retuning Success! ");
		
		}catch(PersistanceException e){
			log.error("Error in TradingPartnerViewAction execute method........." +e);
			throw new PersistanceException(e);
		}
		catch(Exception ex){			
			ex.printStackTrace();
    		log.error("Error in TradingPartnerViewAction execute method........." +ex);
    		throw new Exception(ex);
		}
		
		 return mapping.findForward("success");
   }
	

   
  
}