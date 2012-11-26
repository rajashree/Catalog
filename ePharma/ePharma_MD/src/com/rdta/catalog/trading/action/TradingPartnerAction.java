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

import com.rdta.catalog.trading.model.TradingPartner;
import com.rdta.catalog.trading.model.ImageStore;


import org.w3c.dom.Node;

import com.rdta.catalog.Constants;
import com.rdta.catalog.XMLStructure;
import com.rdta.catalog.OperationType;
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.xml.XMLUtil;

import com.rdta.catalog.session.TradingPartnerContext;

import com.rdta.catalog.session.CatalogContext;
 

/**
 * Trading Partner Form information collecting from the reques form.
 * 
 * 
 */
public class TradingPartnerAction extends Action
{
    private static Log log=LogFactory.getLog(TradingPartnerAction.class);
	
	
    public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response)
								 throws Exception {
    	log.info("Inside Action TradingPartnerAction....... ");
		log.info("Inside Action execute of TradingPartnerAction....");
		try{
 	
 	
		String operation = request.getParameter("operationType");
		log.debug("Before doing Operation : " + operation);
		TradingPartner trading = null;
		ImageStore imageStore = null;
		InputStream stream = null;
		
		log.info(" Befoe converting to form!!!! operation : " + operation);
		
		TradingPartnerForm theForm =(TradingPartnerForm)form;
		/*if(theForm != null) {	
			stream = theForm.getPictFile().getInputStream();
		}*/
		
		log.info(" Before converting to form!!!! operation : " + operation);
						
		if(operation != null){
			if(operation.trim().equalsIgnoreCase(OperationType.ADD)) {
				
				log.info(" Inside ADD methods Befoe converting to form!!!! "); 
				trading = new TradingPartner(request);
				trading.insert();
				
				//make this one as context information in session
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
				
				if(stream != null && stream.available()!= 0 ) {
					imageStore = new ImageStore(stream);
					imageStore.insert(Constants.TRADING_PARTNER_COLL, trading.getGenId());
				}
				log.info(" Before converting to form!!!!");
							
									
			} else if(operation.trim().equalsIgnoreCase(OperationType.UPDATE)) {
				log.debug(" Updating record generated value : " + request.getParameter("tpGenId") );
				trading = new TradingPartner(request);
				trading.update();
				
				if(stream != null && stream.available()!= 0 ) {
					ImageStore.delete(Constants.TRADING_PARTNER_COLL, trading.getGenId());
					imageStore = new ImageStore(stream);
					imageStore.insert(Constants.TRADING_PARTNER_COLL, trading.getGenId());
				}
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
		}

		log.info("before retuning Success! ");
		}catch(PersistanceException e){
			log.error("Error in TradingPartnerAction execute method........." +e);
			throw new PersistanceException(e);
		}
		catch(Exception ex){			
			ex.printStackTrace();
    		log.error("Error in TradingPartnerAction execute method........." +ex);
    		throw new Exception(ex);
		}
		return mapping.findForward("success");
   }
	

   
  
}