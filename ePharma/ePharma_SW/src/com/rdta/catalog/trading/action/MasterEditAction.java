/*
 * Created on Sep 29, 2005
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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.rdta.catalog.session.CatalogContext;
import com.rdta.catalog.trading.model.Catalog;

/**
 * @author Arun Kumar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MasterEditAction extends Action {
	
	 public ActionForward execute(ActionMapping mapping, ActionForm form,
			 HttpServletRequest request, HttpServletResponse response)
			 throws Exception {
		
	 	System.out.println("Arunkumar *****&&&&&&&&&&&&");
	 	String catalogGenId = request.getParameter("catalogGenId");
	 	CatalogContext catalogContext = new CatalogContext();
		Catalog catalog = Catalog.find(catalogGenId);
	    if(catalog != null){
	    	
	    	
	    }
	 
	 
	 				return mapping.findForward("success");
	 }
	

}
