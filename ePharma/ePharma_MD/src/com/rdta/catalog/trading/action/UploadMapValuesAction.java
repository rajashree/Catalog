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


import java.io.File;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rdta.catalog.session.ReconcilableData;
import com.rdta.catalog.session.TradingPartnerContext;
import com.rdta.catalog.session.UploadCatalogContext;
import com.rdta.catalog.trading.model.Catalog;
import com.rdta.catalog.trading.model.MappingCatalogs;


import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.rdta.catalog.Constants;
import com.rdta.catalog.DataElementNode;
import com.rdta.catalog.MappingNodeObject;
import com.rdta.catalog.SchemaTree;
import com.rdta.catalog.XMLStructure;
import com.rdta.catalog.OperationType;

import com.rdta.catalog.CVSFormatConversion;
import com.rdta.commons.CommonUtil;
import com.rdta.commons.xml.XMLUtil;

import com.rdta.catalog.session.TradingPartnerContext;

import com.rdta.catalog.session.CatalogContext;
import com.rdta.catalog.JavaScriptTree;

/**
 * Catalog information collecting from the reques form.
 * 
 * 
 */
public class UploadMapValuesAction extends Action
{
    private static Log log=LogFactory.getLog(UploadMapValuesAction.class);
	
	
    public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response)
								 throws Exception {
 	
		String catalogGenId = request.getParameter("catalogGenId");
		String standardCatalogId = request.getParameter("standardCatalogId");
		String currAttrNo = request.getParameter("currAttrNo");
		
				
		log.debug(" CatalogGenId  : " + catalogGenId);
		log.info(" CatalogGenId  : " + catalogGenId + " currAttrNo: " + currAttrNo );
		
		UploadCatalogContext uploadContext = null;

		HttpSession session = request.getSession(false);
		if(session != null) {
			log.info(" session is not null  : " );
			uploadContext = (UploadCatalogContext)session.getAttribute(Constants.SESSION_CATALOG_UPLOAD_CONTEXT);
		}
		
		
		if(uploadContext != null) {
			
			log.info(" uploadContext is not null  : " );
			
			int currAttrNoInt = Integer.parseInt(currAttrNo);
			List reconcilableList = uploadContext.getReconcilableList();
			MappingNodeObject  mappingNodeObj =	uploadContext.getMappingNodeObj();
		
			if(currAttrNoInt< reconcilableList.size()) {
				
				log.info(" Size of reconcilableList  : " +  reconcilableList.size());
						
				ReconcilableData currData = (ReconcilableData)reconcilableList.get(currAttrNoInt);
				String sourceEleName = currData.getSourceElementName();
				
				DataElementNode dataElementNode = mappingNodeObj.getDataElementNode(sourceEleName);
				
				
				String sourceValueName = "sourceValue";
				String targetValueName = "targetValue";
				String sourceValue = "";
				String targetValue = "";
				
				List values = currData.getValuesList();
				for(int i=0; i<values.size(); i++) {
					//read mapping values from the request parameter and update
					//mapping node object
					sourceValue = request.getParameter(sourceValueName+ i);
					targetValue = request.getParameter(targetValueName+i);
					dataElementNode.createNewValueMap(sourceValue,targetValue);
				}
				
				//increase one more number
				currAttrNoInt = currAttrNoInt+1;
				
				
			} 
			
			
			//after incrementing check whether size is increasing 
			//more if it more dont send to same page
			if(currAttrNoInt< reconcilableList.size()) {
				//get the mapped  values and set the curr 
				request.setAttribute("currAttrNo",""+ currAttrNoInt);
				log.info(" Before returning... to  success ");		
				return mapping.findForward("success");
				
			} else {
				
				//save the mapping object
				MappingCatalogs  catalogs = new MappingCatalogs(mappingNodeObj.getNode());
				catalogs.update();
				
				log.info(" Before returning to  startUploadProcess page" );
				return mapping.findForward("startUploadProcess");
			}
			
		}//not null mapping object
		
		return mapping.findForward("success");
		
   }
	
	
  
}