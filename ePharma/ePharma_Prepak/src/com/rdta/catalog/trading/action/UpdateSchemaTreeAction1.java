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


import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.rdta.catalog.Constants;
import com.rdta.catalog.XMLStructure;
import com.rdta.catalog.OperationType;
import com.rdta.commons.xml.XMLUtil;

import com.rdta.catalog.session.TradingPartnerContext;

import com.rdta.catalog.PersistanceUtil;

import com.rdta.catalog.session.CatalogContext;
import com.rdta.catalog.SchemaTree;

/**
 * Catalog information collecting from the reques form.
 * 
 * 
 */
public class UpdateSchemaTreeAction1 extends Action
{
    private static Log log=LogFactory.getLog(UpdateSchemaTreeAction.class);
	
	
    public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response)
								 throws Exception {
 	
		String catalogGenId = request.getParameter("catalogGenId");
		String fromModule = request.getParameter("fromModule");
		String xpath = request.getParameter("xpath");
		String name = request.getParameter("name");
		String values = request.getParameter("values");
		String operationType = request.getParameter("operationType");
		String tpGenId =request.getParameter("GenId");
		log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%Trading Partner GenID"+tpGenId);
		
		log.debug(" CatalogGenId  : " + catalogGenId + "  fromModule : " + fromModule );
		
		
		log.info(" UpdateSchemaTreeAction CatalogGenId  : " + catalogGenId + "  fromModule : " + fromModule + " xpath " + xpath  );
		log.info(" operationType  : " + operationType + "  name : " + name  );
		log.info(" values  : " + values  );
		
		CatalogContext catalogContext = null;
		HttpSession session = request.getSession(false);
		if(session != null) {
			 
			if(tpGenId!=null){
			request.setAttribute("tpGenId",tpGenId);
			}
			catalogContext = (CatalogContext) session.getAttribute(Constants.SESSION_CATALOG_CONTEXT);
		 	Node catalogNode = catalogContext.getCatalogNode(catalogGenId);
			
			
			//Here Iam cheking for the catelogNode
			 
			Node node = XMLUtil.getNode(catalogNode, "schema/*");
			if(node != null) {
				SchemaTree tree = new SchemaTree(node);
				if(operationType.equalsIgnoreCase("ADD") ) {
					 tree.addElement(name,xpath,values );
				} else if(operationType.equalsIgnoreCase("UPDATE") ) {
					 tree.replaceElement(name,xpath,values);
    			} else if(operationType.equalsIgnoreCase("DELETE") ) {
					tree.deleteElement(xpath );
				} else if(operationType.equalsIgnoreCase("SAVE")) {
					//udate catalog node in database with schema defination
					StringBuffer buff = new StringBuffer("$a/Catalog/genId='");
					buff.append( XMLUtil.getValue(catalogNode,"genId"));
					buff.append("'");
					PersistanceUtil.updateDocument(catalogNode,Constants.CATALOG_COLL,buff.toString());
				}
			} else {
				//in case schema not found first time
				Document rootDocument = catalogNode.getOwnerDocument();
				Node newElement = rootDocument.createElement(name);
				Node parentNode = XMLUtil.getNode(catalogNode,"schema");
				parentNode.appendChild(newElement);
			}
			
		}//end of session
			
		return mapping.findForward("success");
   }
	
}