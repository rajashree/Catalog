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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.w3c.dom.Node;

import com.rdta.catalog.CVSFormatConversion;
import com.rdta.catalog.Constants;
import com.rdta.catalog.DataElementNode;
import com.rdta.catalog.MappingNodeObject;
import com.rdta.catalog.PersistanceUtil;
import com.rdta.catalog.XMLStructure;
import com.rdta.catalog.session.ReconcilableData;
import com.rdta.catalog.session.UploadCatalogContext;
import com.rdta.catalog.trading.action.CatalogUploadForm;
import com.rdta.catalog.trading.model.MappingCatalogs;
import com.rdta.commons.CommonUtil;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;

/**
 * Catalog information collecting from the reques form.
 * 
 * 
 */
public class UploadCatalogXML extends Action
{
	private static Log log=LogFactory.getLog(UploadCatalogXML.class);
	private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		int exceptions=0;
		int mapped=0;
		String catalogGenId = request.getParameter("catalogGenId");
		String standardCatalogId = request.getParameter("standardCatalogId");
		
		//since we are using form type as multipart/form-data
		//incase of schema creation from upload process
		//we are losing control of request paramerts
		//so populate catalogId so that tree will populate later
		request.setAttribute("catalogGenId",catalogGenId );
		request.setAttribute("standardCatalogId",standardCatalogId );
		
		
		log.debug(" CatalogGenId  : " + catalogGenId);
		log.info(" CatalogGenId  : " + catalogGenId);
		
		//next forwarding page
		String nextPage = "reconcilableReview";
		
		String uploadedFilePath = CommonUtil.getUploadedFilePath();
		
		UploadCatalogContext uploadContext = new  UploadCatalogContext();
		MappingCatalogs  mappingCatalog = MappingCatalogs.find(catalogGenId,standardCatalogId);
		
		if(mappingCatalog != null) {
			CatalogUploadForm theForm =(CatalogUploadForm)form;
			InputStream stream = null;
			if(theForm != null) {	
				FormFile formFile = theForm.getCatalogAddFile();
				if(formFile != null) {
					stream = formFile.getInputStream();
					CommonUtil.saveUploadedFile(stream,formFile.getFileName());	
					uploadedFilePath = uploadedFilePath + File.separator + formFile.getFileName();
				}
			}
			
			MappingNodeObject mappingNodeObj = new MappingNodeObject(mappingCatalog.getNode());
			Node Products=XMLUtil.parse(new File(uploadedFilePath));
			log.info("THIS IS THE PRODUCTS STRING"+XMLUtil.convertToString(Products));
			log.info("THIS IS THE MappingNodeObj STRING"+XMLUtil.convertToString(mappingNodeObj.getNode()));
			List list=XMLUtil.executeQuery(Products,"Product");
			
			
			log.info("Total no of PRODUCTS "+list.size());
			
			
			HttpSession session = request.getSession(false);
			String mandatory= session.getAttribute(Constants.MANDATORY_ELEMENTS).toString();
			
			String manList[]=mandatory.split(" ");
			log.info("mansld"+manList[0]);
			String alias= session.getAttribute(Constants.ALIAS_ELEMENTS).toString();
			log.info("alias"+alias);
			String aliasColList[]=(alias).split(" ");
			log.info("mansld"+aliasColList.length);
			List mappedList=new ArrayList();
			List exceptionList=new ArrayList();
			int el=0;
			int ma=0;
			List qList=new ArrayList();
			List dataElementList=mappingNodeObj.getDataElementsList();
			List reconcilableList=new ArrayList();
			List notMatchSourceColList = new ArrayList();
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
					Node masterProduct=XMLUtil.parse((String)res.get(0));
				 
					
					Node newProduct=XMLStructure.getProductNode();
					for(int x=0;x<dataElementList.size();x++)
					{   
						DataElementNode dataElementNode = (DataElementNode)dataElementList.get(x);
						log.info("name1"+dataElementNode.getSourceEleName());
						log.info("name2"+dataElementNode.getTargetEleName());
						
						String sVal=XMLUtil.getValue(product,dataElementNode.getSourceEleName());
						String tVal=XMLUtil.getValue(masterProduct,dataElementNode.getTargetEleName());
						XMLUtil.putValue(newProduct,dataElementNode.getAbsoluteTargetEleName(),sVal);
						log.info("Here Is the update of product"+XMLUtil.convertToString(newProduct));
						XMLUtil.putValue(newProduct,dataElementNode.getTargetEleName(),sVal);
						log.info("Here Is the update of product"+XMLUtil.convertToString(newProduct));
						//dataElementNode.createNewValueMap(sVal,tVal);
					
						for(int y=0;y<manList.length;y++)
						{
							
							if(manList[y].equalsIgnoreCase(dataElementNode.getTargetEleName()))
							{
								log.info("manList"+manList[y]);

								sVal=XMLUtil.getValue(product,dataElementNode.getSourceEleName());
								tVal=XMLUtil.getValue(masterProduct,manList[y]);
								if(sVal.equalsIgnoreCase(tVal))
								{
									XMLUtil.putValue(newProduct,dataElementNode.getTargetEleName(),sVal);
									log.info("Here Is the update of product"+XMLUtil.convertToString(newProduct));
									XMLUtil.putValue(newProduct,dataElementNode.getTargetEleName(),sVal);
									log.info("Here Is the update of product"+XMLUtil.convertToString(newProduct));
									//dataElementNode.createNewValueMap(sVal,tVal);
								}
								else
								{
									// 	subReconcilableList.add(manList[y]);
								}
							}
							
						} 
						for(int y=0;y<aliasColList.length;y++)
						{
							
							if(aliasColList[y].equalsIgnoreCase(dataElementNode.getTargetEleName()))
							{
								 sVal=XMLUtil.getValue(product,dataElementNode.getSourceEleName());
								 tVal=XMLUtil.getValue(masterProduct,aliasColList[y]);
								if(sVal.equalsIgnoreCase(tVal))
								{
									
									XMLUtil.putValue(newProduct,dataElementNode.getTargetEleName(),sVal);
									log.info("Here Is the update of product"+XMLUtil.convertToString(newProduct));
									//dataElementNode.createNewValueMap(sVal,tVal);
								}
								else
								{
									List valueList=new ArrayList();
									valueList.add(XMLUtil.getValue(product,dataElementNode.getSourceEleName()));
									ReconcilableData data= new ReconcilableData();
									data.setSourceElementName(dataElementNode.getSourceEleName());
									data.setTargetElementName(dataElementNode.getTargetEleName());
									data.setValuesList(valueList);
									data.setTotalNumOfValues(valueList.size());
									notMatchSourceColList.add(dataElementNode.getTargetEleName());
									reconcilableList.add(data); 
									log.info("HEre we have one reconcileable item");
									
								}
							}
							
						}     
						
					}
					qList.add(ma,newProduct); 
					mappedList.add(ma,newProduct);
					ma++;
					mapped++;
				}
				else{
					
					exceptionList.add(el,product);
					el++;
					exceptions++;
				}
				
			}
			
			uploadContext.setMappingNodeObj(mappingNodeObj);
			uploadContext.setFilePath(uploadedFilePath);
			uploadContext.setNoOfRecordsToLoad(mapped);
			if( !reconcilableList.isEmpty()) {
				log.info("Length"+reconcilableList.size());
				
				uploadContext.setReconcilableList(reconcilableList);
				uploadContext.setNotMatchSourceColumns(notMatchSourceColList);
				
				
			} else {
				nextPage = "summary";
			}
			
			
			
			if(session != null) {
				log.info("exceptions"+exceptionList.size());
				log.info("mapped"+mappedList.size());
				session.setAttribute(Constants.SESSION_CATALOG_UPLOAD_CONTEXT, uploadContext);
				session.setAttribute("exceptions",new Integer(exceptions));
				session.setAttribute("mapped",new Integer(mapped));
				session.setAttribute("exceptionList",exceptionList);
				session.setAttribute("mappedList",mappedList);
				session.setAttribute("saveProduct1",qList);
				session.setAttribute("standardCatalogId",standardCatalogId );
				session.setAttribute("catalogGenId",catalogGenId );
				
				//	nextPage="summary";
			}
			
		}//not null mapping object
		
		log.info(" Before returning...  nextPage : " + nextPage);		
		return mapping.findForward(nextPage);
	}
	
	
	
	
	
	
	
}