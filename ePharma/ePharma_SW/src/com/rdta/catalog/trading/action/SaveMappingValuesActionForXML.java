/*
 * Created on Oct 14, 2005
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
import com.rdta.catalog.PersistanceUtil;
import com.rdta.catalog.session.UploadCatalogContext;
import com.rdta.catalog.trading.model.ProductMaster;

/**
 * @author Jagadish Pampatwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SaveMappingValuesActionForXML extends Action{
	private static Log log=LogFactory.getLog(SaveMappingValuesActionForXML.class);
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			 HttpServletRequest request, HttpServletResponse response)
			 throws Exception {
		
		log.info("Inside Action SaveMappingValuesAction....... ");
		log.info("Inside Action execute of SaveMappingValuesAction....");
		try{
		HttpSession session = request.getSession();
		UploadCatalogContext uploadContext = (UploadCatalogContext)session.getAttribute(Constants.SESSION_CATALOG_UPLOAD_CONTEXT);
		
		String catalogGenId=(String) session.getAttribute("catalogGenId");
		session.setAttribute("catalogGenId",catalogGenId);
		String standardCatalogId=(String)session.getAttribute("standardCatalogId");
		session.setAttribute("standardCatalogId",standardCatalogId);
		/*List qlist=(List)session.getAttribute("saveMapping");
		for(int i=0;i<qlist.size();i++)
		{
		ProductMaster product = (ProductMaster)qlist.get(0);
		product.insert();
		}*/
		List list1=(List)session.getAttribute("saveProduct1");
		if(list1.size()>0)
		{for(int i=0;i<list1.size();i++)
		{
			Node p=(Node)list1.get(i);
			PersistanceUtil.insertDocument(p,Constants.PRODUCT_MASTER_COLL);
		}
		}
		
		}catch(Exception ex)
		{
	    	ex.printStackTrace();
			log.error("Error in CatalogSchemaDefAction execute()" + ex);
			return mapping.findForward("exception");
	
		}
		return mapping.findForward("success");
	}
}
