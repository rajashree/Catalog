
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
import org.apache.struts.upload.FormFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rdta.catalog.trading.model.ProductMaster;
import com.rdta.catalog.trading.model.ImageStore;


import org.w3c.dom.Node;

import com.rdta.catalog.Constants;
import com.rdta.catalog.XMLStructure;
import com.rdta.catalog.OperationType;
import com.rdta.commons.CommonUtil;
import com.rdta.commons.xml.XMLUtil;

import com.rdta.catalog.session.TradingPartnerContext;

import com.rdta.catalog.session.CatalogContext;
 

/**
 * Product information collecting from the reques form.
 * 
 * 
 */
public class ProductMasterAction extends Action
{
    private static Log log=LogFactory.getLog(ProductMasterAction.class);
	
	
    public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response)
								 throws Exception {
 	
		String operation = request.getParameter("operationType");
		log.debug("Before doing Operation : " + operation);
		ProductMaster productMaster = null;
		

		
	log.info(" Befoe converting to form!!!! operation : " + operation);
		
		
		
	
		if(operation != null){
			if(operation.trim().equalsIgnoreCase(OperationType.ADD)) {
				log.info(" Inside ADD methods Befoe converting to form!!!! "); 
				productMaster = new ProductMaster(request);
				saveUploadedImages(request,form,productMaster);
				productMaster.insert();
			} else if(operation.trim().equalsIgnoreCase(OperationType.UPDATE)) {
				log.debug(" Updating record generated value : " + request.getParameter("productGenId") );
				productMaster = new ProductMaster(request);
				saveUploadedImages(request,form,productMaster);
				productMaster.update();
			} else if(operation.trim().equalsIgnoreCase(OperationType.FIND)) {
				log.debug(" finding record : " + request.getParameter("productGenId") );
				productMaster = ProductMaster.find(request.getParameter("productGenId"));
			} else {
				log.info(" Operation Type : " + operation + " not dealing in this action!");
			}
		}
		
		
		if(productMaster != null) {
			//set the result here
			request.setAttribute("ProductMasterInfo",productMaster.getNode());
		}

		log.info("before retuning Success! ");
		return mapping.findForward("success");
   }
	
	
	private void saveUploadedImages(HttpServletRequest request,ActionForm form,ProductMaster productMaster)  throws Exception {
		
		FormFile packagingFormFile = null;
		FormFile productMarkingFormFile = null;
		
		ProductMasterForm theForm =(ProductMasterForm)form;
		if(theForm != null) {	
			packagingFormFile = theForm.getPackagingFile();
			productMarkingFormFile = theForm.getProductMarkingFile();
			
			
				
			//save packaging image
			if(packagingFormFile !=null && packagingFormFile.getFileSize() > 0) {
				String genId = CommonUtil.getGUID();
			    String fileName = 	genId + packagingFormFile.getFileName();
				InputStream stream = packagingFormFile.getInputStream();
				CommonUtil.saveImageFile(stream,fileName );
				
				XMLUtil.putValue(productMaster.getNode(),"Overt/Packagings/Packaging/ImageUrl", fileName);
			} else {
				
				String previousPackagingImageFileName = request.getParameter("previousPackagingImageFileName");
				XMLUtil.putValue(productMaster.getNode(),"Overt/Packagings/Packaging/ImageUrl", previousPackagingImageFileName);
				
			}
			
			if(productMarkingFormFile != null && productMarkingFormFile.getFileSize() > 0) {
				log.info(" Itis able to save marking image");
				
				String genId = CommonUtil.getGUID();
			    String fileName = 	genId + productMarkingFormFile.getFileName();
				InputStream stream = productMarkingFormFile.getInputStream();
				CommonUtil.saveImageFile(stream,fileName );
				
				XMLUtil.putValue(productMaster.getNode(),"Overt/ProductMarkings/ProductMarking/ImageUrl", fileName);
		
			} else {
				
				String previousProductMarkingImageFileName = request.getParameter("previousProductMarkingImageFileName");
				XMLUtil.putValue(productMaster.getNode(),"Overt/ProductMarkings/ProductMarking/ImageUrl", previousProductMarkingImageFileName);
				
			}
			
		}//end of theForm if 
	}

   
  
}