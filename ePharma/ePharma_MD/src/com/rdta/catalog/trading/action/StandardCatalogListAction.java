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

 

package com.rdta.catalog.trading.action;

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

import com.rdta.catalog.Constants;
import com.rdta.catalog.OperationType;
import com.rdta.catalog.session.TradingPartnerContext;
import com.rdta.catalog.trading.model.Catalog;
import com.rdta.catalog.trading.model.ImageStore;
import com.rdta.catalog.trading.model.TradingPartner;
import com.rdta.commons.xml.XMLUtil;

/**
 * @author Santosh Subramanya
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StandardCatalogListAction extends Action {
	 private static Log log=LogFactory.getLog(StandardCatalogListAction.class);
	
	    public ActionForward execute(ActionMapping mapping, ActionForm form,
									 HttpServletRequest request, HttpServletResponse response)
									 throws Exception {
	 
	    	
	    
//	    	set the session to null
			HttpSession session = request.getSession(false);
		

		//	System.out.println(" Inside Standard Catalog List Action, tpGenId: " + request.getParameter("GenId"));
			
			//List list = Catalog.getStandardCatalogList(Constants.TRADING_PARTNER_COLL, request.getParameter("GenId") );
			List list = Catalog.getStandardCatalogList(Constants.STD_TRADING_PARTNER_COLL, null );

			//set the result here
			request.setAttribute("StandardCatalogListInfo",list);
			
			
				
				
				if(session != null) {
					
					TradingPartnerContext context = (TradingPartnerContext)session.getAttribute(Constants.SESSION_TP_CONTEXT);
					if(context == null) {
						context = new TradingPartnerContext();
					}
					
					//set to session
					session.setAttribute(Constants.SESSION_TP_CONTEXT,context );
				}

			
			
			
			System.out.println("*********************************Before Return from execute method"+list.size());
			
			return mapping.findForward("success");
	    }
}
