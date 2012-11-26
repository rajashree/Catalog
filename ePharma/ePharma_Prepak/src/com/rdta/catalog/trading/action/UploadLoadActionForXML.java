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
import java.util.ArrayList;
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
import com.rdta.catalog.trading.model.ProductMaster;


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
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;

import com.rdta.catalog.session.TradingPartnerContext;

import com.rdta.catalog.session.CatalogContext;
import com.rdta.catalog.JavaScriptTree;

/**
 * Catalog information collecting from the reques form.
 * 
 * 
 */
public class UploadLoadActionForXML extends Action
{
    private static Log log=LogFactory.getLog(UploadLoadActionForXML.class);
    private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
    public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response)
								 throws Exception {
    	
    	log.info("Inside Action UploadLoadActionForXML....... ");
		log.info("Inside Action execute of UploadLoadActionForXML....");
		try{
    	StartUploadProcessFormBean formbean=( StartUploadProcessFormBean)form;
    	
		String catalogGenId = (String)formbean.getCatalogGenId();
		String standardCatalogId = (String)formbean.getStandardCatalogId();
		
				
		log.debug(" CatalogGenId  : " + catalogGenId);
		log.info(" CatalogGenId  : " + catalogGenId);
		
	
		
		UploadCatalogContext uploadContext = null;
		
		HttpSession session = request.getSession(false);
		if(session != null) {
			log.info(" session is not null  : " );
			
			
			uploadContext = (UploadCatalogContext)session.getAttribute(Constants.SESSION_CATALOG_UPLOAD_CONTEXT);
		}
		 
		if(uploadContext != null) {
			
			String uploadedFilePath = uploadContext.getFilePath();
			MappingNodeObject  mappingNodeObj =	uploadContext.getMappingNodeObj();
			
			 
			
			List saveList=new ArrayList();
			 
			int count = 0;
			 List mappedList=new ArrayList();
			 List exceptionList=new ArrayList();
				int el=0;
				Node Products=XMLUtil.parse(new File(uploadedFilePath));
				List list=XMLUtil.executeQuery(Products,"Product");
				List dataElementList=mappingNodeObj.getDataElementsList();
				for(int i=0;i<list.size();i++)
				{
					Node product=(Node)list.get(i);
					log.info("NDC"+XMLUtil.getValue(product,"NDC"));
					String Ndc=XMLUtil.getValue(product,"NDC");
					String q="for $i in collection('tig:///CatalogManager/ProductMaster/') where $i//NDC='"+XMLUtil.getValue(product,"NDC")+"'";
					q=q+" return $i/Product";
					log.info(q);
					List res=queryRunner.returnExecuteQueryStrings(q);
					
					if(res.size()>0)
					{ 	
						log.info(res.get(0).toString());
						
						 
						
						count++;
						Node newProduct=XMLStructure.getProductNode();
						XMLUtil.putValue(newProduct,"genId",CommonUtil.getGUID());
						XMLUtil.putValue(newProduct,"catalogId",catalogGenId);
						for(int x=0;x<dataElementList.size();x++)
						{   
							DataElementNode dataElementNode = (DataElementNode)dataElementList.get(x);
							log.info("name1"+dataElementNode.getSourceEleName());
							log.info("name2"+dataElementNode.getTargetEleName());
							
							String sVal=XMLUtil.getValue(product,dataElementNode.getTargetEleName());
							String tVal=XMLUtil.getValue(product,dataElementNode.getTargetEleName());
							//XMLUtil.putValue(newProduct,dataElementNode.getAbsoluteTargetEleName(),sVal);
							//System.out.println("Here Is the update of product"+XMLUtil.convertToString(newProduct));
							XMLUtil.putValue(newProduct,dataElementNode.getTargetEleName(),sVal);
							log.info("Here Is the update of product"+XMLUtil.convertToString(newProduct));
							
						}
							
							
						saveList.add(newProduct); 
						mappedList.add(newProduct);
						
					}
					else{
						
						exceptionList.add(el,product);
				
					}
					
				}
				

	
			if(saveList.size()>0)
			{
			session.setAttribute("saveProduct",saveList);
			session.setAttribute("mapped",new Integer(saveList.size()));
			session.setAttribute("mappedList",mappedList);
			}
			log.info("exceptions"+exceptionList.size());
			
			session.setAttribute("exceptionList",exceptionList);
			session.setAttribute("exceptions",new Integer(exceptionList.size()));
			request.setAttribute("TotalNumOfLoadedRecords",""+count);
			
		}//not null mapping object
		
		log.info(" Before returning...");
		}catch(Exception ex)
		{
	    	ex.printStackTrace();
			log.error("Error in UploadLoadAction execute()" + ex);
			return mapping.findForward("exception");
	
		}
		return mapping.findForward("success");
   }
	
	
	

	
	
  
}