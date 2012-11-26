
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

import com.rdta.catalog.session.TradingPartnerContext;

import com.rdta.catalog.session.CatalogContext;
import com.rdta.catalog.JavaScriptTree;

/**
 * Catalog information collecting from the reques form.
 * 
 * 
 */
public class SchemaManageElementAction extends Action
{
    private static Log log=LogFactory.getLog(SchemaManageElementAction.class);
	
	
    public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response)
								 throws Exception {
 	    log.info("Inside SchemaManageElementAction");
 	    try{
 	    	
 	    
		String catalogGenId = request.getParameter("catalogGenId");
		String fromModule = request.getParameter("fromModule");
		
		log.debug(" CatalogGenId  : " + catalogGenId + "  fromModule : " + fromModule );
		log.info(" SchemaManageElementAction CatalogGenId  : " + catalogGenId + "  fromModule : " + fromModule );
			
				
		
		
		return mapping.findForward("success");
 	    }
 	    catch(Exception e)
		{
 	    	e.printStackTrace();
 	    	return mapping.findForward("exception");
		}
   }
	

   
  
}