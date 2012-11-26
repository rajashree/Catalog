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
import java.util.List;

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
import com.rdta.catalog.trading.model.MappingCatalogs;


import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.rdta.catalog.Constants;
import com.rdta.catalog.MappingNodeObject;
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
public class TradingPartnerUpdateMapCatalogsAction extends Action
{
    private static Log log=LogFactory.getLog(TradingPartnerUpdateMapCatalogsAction.class);
	
	
    public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response)
								 throws Exception {
 	
		String xpath = request.getParameter("xpath");
		String display = request.getParameter("display");
		String operationType = request.getParameter("operationType");
			
		
		log.info(" UpdateMapCatalogsAction  xpath " + xpath  );
		log.info(" operationType  : " + operationType + "  display : " + display  );
		
		MappingNodeObject mappingNodeObject = null;
		HttpSession session = request.getSession(false);
		if(session != null) {
			
			mappingNodeObject = (MappingNodeObject) session.getAttribute(Constants.SESSION_CATALOG_MAPPING_CONTEXT);
			Node node = mappingNodeObject.getNode();
			
			
			if (operationType == null )  {
				
				//if no operation type then consider as add to mapping schema
				if(display != null && xpath != null ) {
					if(display.trim().equalsIgnoreCase("right"))  {
						
						//create data element with default value NEED_TO_BE_DEFINED_RAJU
						
						mappingNodeObject.createDataNode(xpath);
					} else if(display.trim().equalsIgnoreCase("left")) {
						//replace first default value
						mappingNodeObject.replaceDataNodeTargetEleDefaultValue(xpath);
					}
					
					
				}
				
			} else if(operationType.equalsIgnoreCase("SAVE")) {
				//udate catalog node in database with schema defination
				StringBuffer buff = new StringBuffer("$a/MappingCatalogs/genId='");
				buff.append( XMLUtil.getValue(node,"genId"));
				buff.append("'");
				PersistanceUtil.updateDocument(node,Constants.MAPPING_CATALOGS_COLL,buff.toString());
				
			} else if(operationType.equalsIgnoreCase("DELETE") ) {
				
				log.info(" Inside Delete :"); 
				String checkBoxName = "check";
				List dataElements = XMLUtil.executeQuery(mappingNodeObject.getNode(),"dataList/data");
				for(int i=0; i< dataElements.size(); i++) {
					String checkedBoxValue = request.getParameter( checkBoxName+ i);
					
					log.info(" checkedBoxValue :" + checkedBoxValue);
					if(checkedBoxValue != null) {
						mappingNodeObject.deleteSourceElementBasedOnAbsolutePath(checkedBoxValue);
					}
				}
			} 
			
			
			
		}//end of session
			
		
		return mapping.findForward("success");
   }
	
}