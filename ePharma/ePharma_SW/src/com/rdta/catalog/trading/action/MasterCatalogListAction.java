/*
 * Created on Sep 2, 2005
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
import org.w3c.dom.Node;

import com.rdta.catalog.Constants;
import com.rdta.catalog.MappingNodeObject;
import com.rdta.catalog.XMLStructure;
import com.rdta.catalog.session.CatalogContext;
import com.rdta.catalog.trading.model.Catalog;
import com.rdta.catalog.trading.model.MappingCatalogs;

/**
 * @author Ajay Reddy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MasterCatalogListAction extends Action {
	
	private static Log log=LogFactory.getLog(MasterCatalogListAction.class);
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			 HttpServletRequest request, HttpServletResponse response)
			 throws Exception {


log.info(" Inside List Action, tpGenId: " + request.getParameter("tpGenId"));

List list = Catalog.getStandardCatalogList(Constants.CATALOG_COLL,request.getParameter("tpGenId"));

//Here is the code to get Trading Partner Catelog information

String leftCatalogGenId = request.getParameter("leftCatalogGenId");


log.debug(" leftCatalogGenId  : " + leftCatalogGenId );

request.setAttribute("StandardCatalogListInfo",list);

return mapping.findForward("success");


	}
}
