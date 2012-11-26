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
public class UploadLoadAction extends Action
{
    private static Log log=LogFactory.getLog(UploadLoadAction.class);
    private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
    public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response)
								 throws Exception {
    	
    	log.info("Inside Action UploadLoadAction....... ");
		log.info("Inside Action execute of UploadLoadAction....");
		try{
    	StartUploadProcessFormBean formbean=( StartUploadProcessFormBean)form;
    	
		String catalogGenId = (String)formbean.getCatalogGenId();
		String standardCatalogId = (String)formbean.getStandardCatalogId();
		
				
		log.debug(" CatalogGenId  : " + catalogGenId);
		System.out.println(" CatalogGenId  : " + catalogGenId);
		
	
		
		UploadCatalogContext uploadContext = null;
		List mandatoryCol=new ArrayList();
		List aliasCol=new ArrayList();
		HttpSession session = request.getSession(false);
		if(session != null) {
			System.out.println(" session is not null  : " );
			mandatoryCol=(List)session.getAttribute("MandatorColumns");
			aliasCol=(List)session.getAttribute("AliasColumns");
			System.out.println("      mandatory col"+mandatoryCol);
			System.out.println("      alias col"+aliasCol.get(0));
			uploadContext = (UploadCatalogContext)session.getAttribute(Constants.SESSION_CATALOG_UPLOAD_CONTEXT);
		}
		 
		if(uploadContext != null) {
			
			String uploadedFilePath = uploadContext.getFilePath();
			MappingNodeObject  mappingNodeObj =	uploadContext.getMappingNodeObj();
			CVSFormatConversion cvsConversion = new CVSFormatConversion(uploadedFilePath,mappingNodeObj);
			 
			
			List saveList=new ArrayList();
		//	Node nextRecord  = cvsConversion.getNextRecord();
			/*while(nextRecord != null)
			{
				nextRecord=cvsConversion.getNextRecord();
			System.out.println(XMLUtil.convertToString(nextRecord)); 
			
			}*/
			 
			int count = 0;
			 List lis=cvsConversion.getListOfValueColIndexes();
			 List mappedList=new ArrayList();
			 List exceptionList=new ArrayList();
				int el=0;
			 String line = cvsConversion.getNextLine();
			 
			while(line != null) {
				System.out.println(line);
				String columnValue[]= line.split(","); 
				String q="for $i in collection('tig:///CatalogManager/ProductMaster/') where $i//NDC='"+columnValue[Integer.parseInt((String)lis.get(0))]+"'";
				q=q+" return 'true'";
				System.out.println(q);
				List res=queryRunner.returnExecuteQueryStrings(q);
				if(res.size()>0)
				    { 	
					mappedList.add(line);
					Node nextRecord =cvsConversion.getNextRecord(line);
					//ProductMaster product = new ProductMaster(nextRecord);
					
				//	Node product=XMLStructure.getProductNode();
					XMLUtil.putValue(nextRecord,"genId",CommonUtil.getGUID());
					XMLUtil.putValue(nextRecord,"catalogId",catalogGenId);
					for(int j=0;j<mandatoryCol.size();j++)
					{   
						if(cvsConversion.getDataElementOfColIndex(mandatoryCol.get(j).toString())!=null)
						{
					XMLUtil.putValue(nextRecord,(cvsConversion.getDataElementOfColIndex(mandatoryCol.get(j).toString()).getTargetEleName()),columnValue[Integer.parseInt(mandatoryCol.get(j).toString())]);
					}
											
					}
					
					for(int j=0;j<aliasCol.size();j++)
					{   
						if(cvsConversion.getDataElementOfColIndex(aliasCol.get(j).toString())!=null)
						{    DataElementNode Dn= cvsConversion.getDataElementOfColIndex(aliasCol.get(j).toString());
							
							String tname=(cvsConversion.getDataElementOfColIndex(aliasCol.get(j).toString()).getTargetEleName());
							String tval= XMLUtil.getValue(nextRecord,tname);
							String sval=Dn.getTargetValue(tval);
								//XMLUtil.getValue(Dn.getNode(),"//value[@targetVal='"+tval+"'"+"/@sourceVal");
								//(cvsConversion.getDataElementOfColIndex(aliasCol.get(j).toString()).getSourceValue(tval));
				     	XMLUtil.putValue(nextRecord,(cvsConversion.getDataElementOfColIndex(aliasCol.get(j).toString()).getTargetEleName()),sval);
					   }
						
					}
					
					
					System.out.println(XMLUtil.convertToString(nextRecord));
					ProductMaster product = new ProductMaster(nextRecord);
				
						saveList.add(product);
						count = count + 1;
						
				    }else{
				
				exceptionList.add(el,line);
				el++;}
			 	line=cvsConversion.getNextLine();
			 	System.out.println(line);
			 	 
				
			}
			
			cvsConversion.closeStream();
			if(saveList.size()>0)
			{
			session.setAttribute("saveProduct",saveList);
			session.setAttribute("mapped",new Integer(saveList.size()));
			session.setAttribute("mappedList",mappedList);
			}
			System.out.println("exceptions"+exceptionList.size());
			
			session.setAttribute("exceptionList",exceptionList);
			session.setAttribute("exceptions",new Integer(exceptionList.size()));
			request.setAttribute("TotalNumOfLoadedRecords",""+count);
			
		}//not null mapping object
		
		System.out.println(" Before returning...");
		}catch(Exception ex)
		{
	    	ex.printStackTrace();
			log.error("Error in UploadLoadAction execute()" + ex);
			return mapping.findForward("exception");
	
		}
		return mapping.findForward("success");
   }
	
	
	

	
	
  
}