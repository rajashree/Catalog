
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


import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rdta.catalog.trading.model.Catalog;

import com.rdta.commons.xml.XMLUtil;
import org.w3c.dom.Node;
import com.rdta.catalog.XMLStructure;
import com.rdta.catalog.OperationType;
import com.rdta.catalog.Constants;

/**
 * 
 * 
 * 
 */
public class CatalogListAction extends Action
{
    private static Log log=LogFactory.getLog(CatalogListAction.class);
	
    public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response)
								 throws Exception {
 
    	log.info("Inside Action CatalogListAction....... ");
		log.info("Inside Action execute of CatalogListAction....");
		try{
		
		log.info(" Inside List Action, tpGenId: " + request.getParameter("tpGenId"));
		
		List list = Catalog.getList(Constants.TRADING_PARTNER_COLL,request.getParameter("tpGenId"));

		//set the result here
		request.setAttribute("CatalogListInfo",list);
		}
		catch(Exception ex){
			ex.printStackTrace();
			log.error("Error in CatalogListAction execute()" + ex);
			return mapping.findForward("exception");
		}
		return mapping.findForward("success");
   }
	

   
  
}