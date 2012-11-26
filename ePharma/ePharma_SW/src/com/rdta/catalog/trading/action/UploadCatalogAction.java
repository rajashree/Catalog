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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.rdta.catalog.XMLStructure;
import com.rdta.catalog.session.UploadCatalogContext;
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
public class UploadCatalogAction extends Action
{
    private static Log log=LogFactory.getLog(UploadCatalogAction.class);
    private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
    public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response)
								 throws Exception {
    	try{
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
		log.info("uploaded FilePath"+uploadedFilePath);
		UploadCatalogContext uploadContext = new  UploadCatalogContext();
		MappingCatalogs  mappingCatalog = MappingCatalogs.find(catalogGenId,standardCatalogId);
		int columnOfNDC=0;
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
			
			CVSFormatConversion cvsConversion = new CVSFormatConversion(uploadedFilePath,mappingNodeObj);
			log.info("LINE COUNT"+cvsConversion.getLineCount());
			Map mp = cvsConversion.getHeaderMap();
		    Set se= mp.entrySet();
		    Iterator ie=se.iterator();
		  
		    while(ie.hasNext())
		    {
		    	Map.Entry me=(Map.Entry)ie.next();
		    	if(((DataElementNode)me.getValue()).getSourceEleName().equalsIgnoreCase("NDC"))
				{
					columnOfNDC=Integer.parseInt(me.getKey().toString());
					log.info("Column of NDC"+columnOfNDC);
					break;
				}
		    	log.info("HEAD MAP"+me.getKey()+me.getValue().toString());
		    }
			
	      
			HttpSession session = request.getSession(false);
			String mandatory= session.getAttribute(Constants.MANDATORY_ELEMENTS).toString();
		
			String manList[]=mandatory.split(" ");
			log.info("mansld"+manList[0]);
			//DataElementNode Den=(mappingNodeObj.getDataElementNode(manList[0]));
		//	String colName=Den.getTargetEleName();
		//	System.out.println(colName);
			String alias= session.getAttribute(Constants.ALIAS_ELEMENTS).toString();
			log.info("aaaaaaaaa"+alias);
		 	String aliasColList[]=(alias).split(" ");
		   log.info("mansld"+aliasColList);
           List lis=cvsConversion.getListOfValueColIndexes();
           log.info("LIST OF COL VALS"+lis);
           String line=cvsConversion.getNextLine();
           log.info("FIRSTLINE"+line);
           
          // ((DataElementNode)cvsConversion.getDataElementOfColIndex()).getSourceEleName();
              //System.out.println("LIST OF COLUMN"+lis);
				/*Collection set =mp.values();
			   	
				int l = set.size();
				Iterator iter = set.iterator();
				while(iter.hasNext()){
					System.out.println(((DataElementNode)iter.next()).getAbsoluteSourceEleName());
				}*/
			
			// added by Jagadish
          List allowAliasColList=new ArrayList();
           List mandatoryColList=new ArrayList();
           for(int x=0;x<aliasColList.length;x++)
            for(int s=0;s<lis.size();s++) 
            {
            	
            	log.info("test");
            	if(lis.get(s)!=null)
            	{
            	DataElementNode daEle=cvsConversion.getDataElementOfColIndex((String)lis.get(s));
            	
            	 //System.out.println("sss"+daEle);

            	log.info(aliasColList[x]);
            	if((daEle!=null) && daEle.getTargetEleName().equals(aliasColList[x]))
            	{
            		allowAliasColList.add(x,new Integer((String)lis.get(s)));
            		log.info("ALIS COL"+lis.get(s).toString());
            	}
            	}
            	
            }
           
           for(int x=0;x<manList.length;x++)
            for(int s=0;s<lis.size();s++) 
            {
            	
            	if(lis.get(s)!=null)
            	{
            	DataElementNode daEle=cvsConversion.getDataElementOfColIndex((String)lis.get(s));
            	
            	//System.out.println(daEle.getTargetEleName());

            	//System.out.println(aliasColList[x]);
            	if((daEle!=null) && (daEle.getTargetEleName().equals(manList[x])))
            	{
            		mandatoryColList.add(x,new Integer((String)lis.get(s)));
            		log.info("Mandatory COL"+s);
            	}
            	}
            	
            }
           
          	List mappedList=new ArrayList();
			List exceptionList=new ArrayList();
			int el=0;
			int ma=0;
			List qList=new ArrayList();
			
			
		
			for(int i=0 ;!(line==null);i++)
			{
				log.info(line);
				String queryString="for $i in collection('tig:///CatalogManager/ProductMaster/') where";
				
				
				
				
	            String columnValue[]= line.split(",");
	            	            
			    Iterator iterator =lis.iterator();
			    
			   // System.out.println(columnValue[Integer.parseInt((String)lis.get(0))]);
			    String q="for $i in collection('tig:///CatalogManager/ProductMaster/') where $i//NDC='"+columnValue[columnOfNDC]+"'";
				q=q+" return 'true'";
			    log.info(q);
			    List res=queryRunner.returnExecuteQueryStrings(q);
			  /*  if(res.size()>0)
			    { 	
			    	mappedList.add(ma,line);
			    	mapped++;
			    }
			    else
			    {
			    	exceptionList.add(el,line);
			    	exceptions++;
			    	
			    }*/
				if(res.size()>0)
			    { 	
			    /*for(int j=0;j<mandatoryColList.size();j++)
				{   
				if(j==0)
				{
				queryString=queryString+" $i//"+(cvsConversion.getDataElementOfColIndex(mandatoryColList.get(j).toString()).getTargetEleName());
			    queryString=queryString+"='"+columnValue[Integer.parseInt(mandatoryColList.get(j).toString())]+"'";
				}
				else{
				queryString=queryString+" and $i//"+(cvsConversion.getDataElementOfColIndex(mandatoryColList.get(j).toString()).getSourceEleName());
				queryString=queryString+"='"+columnValue[Integer.parseInt(mandatoryColList.get(j).toString())]+"'";
				}
				
				//form the dynamic query
				
				}
			    
				queryString = queryString+ " return '"+"true'";				
			*/
                 				
			// 	List queryList=queryRunner.returnExecuteQueryStrings(queryString);
			     
			 	
				/* if(queryList.size()==0)
				 {
				  	exceptionList.add(el,line);
				  el++;
				 	//System.out.println("EXCEPTIONLIST"+(exceptionList.get(0).toString()));
				 	queryString="";
				  	exceptions++;
				 }
			 	else
				 {*/
			 		                  
			 		
                    mapped++;
                    mappedList.add(ma,line);
                    
                  
                    log.info("MappedList"+(mappedList.get(0).toString()));
					Node product=XMLStructure.getProductNode();
					XMLUtil.putValue(product,"genId",CommonUtil.getGUID());
					XMLUtil.putValue(product,"catalogId",catalogGenId);
					for(int j=0;j<lis.size();j++)
					{   
						if(cvsConversion.getDataElementOfColIndex((String)lis.get(j))!=null)
						{
					XMLUtil.putValue(product,(cvsConversion.getDataElementOfColIndex((String)lis.get(j)).getTargetEleName()),columnValue[Integer.parseInt((String)lis.get(j))]);
						}
						}
					qList.add(ma,product); 
					ma++;
				//	PersistanceUtil.insertDocument(product,Constants.PRODUCT_MASTER_COLL);
				// }
			    }
				else
				{
					exceptionList.add(el,line);
					  el++;
					 	//System.out.println("EXCEPTIONLIST"+(exceptionList.get(0).toString()));
					 	queryString="";
					  	exceptions++;
				}
				 
				
				
				line=cvsConversion.getNextLine();
					
		
			}
			CVSFormatConversion cvsConversion1 = new CVSFormatConversion(uploadedFilePath,mappingNodeObj);
			List reconcilableList=new ArrayList();
			if(allowAliasColList.size()>0)
		  	{	
		    reconcilableList = cvsConversion1.getAliasReconcilableData(allowAliasColList);
		  	
			//System.out.println(reconcilableList.get(0).toString());
		  	}
			uploadContext.setMappingNodeObj(mappingNodeObj);
			uploadContext.setFilePath(uploadedFilePath);
			uploadContext.setNoOfRecordsToLoad(cvsConversion.getLineCount()-1);
			
			if( !reconcilableList.isEmpty()) {
				List notMatchSourceColList = cvsConversion.getNotMatchSourceColumns();
				uploadContext.setReconcilableList(reconcilableList);
				uploadContext.setNotMatchSourceColumns(notMatchSourceColList);
			} else {
				nextPage = "summary";
			}
			
			
			if(session != null) {
				log.info("exceptons"+exceptionList.size());
				log.info("mapped"+mappedList.size());
				session.setAttribute(Constants.SESSION_CATALOG_UPLOAD_CONTEXT, uploadContext);
				session.setAttribute("exceptions",new Integer(exceptions));
				session.setAttribute("mapped",new Integer(mapped));
		     	session.setAttribute("exceptionList",exceptionList);
				session.setAttribute("mappedList",mappedList);
				session.setAttribute("saveProduct1",qList);
				session.setAttribute("standardCatalogId",standardCatalogId );
				session.setAttribute("catalogGenId",catalogGenId );
				session.setAttribute("MandatorColumns",mandatoryColList);
				session.setAttribute("AliasColumns",allowAliasColList);
			//	nextPage="summary";
			}
			
		}//not null mapping object
		log.info(" Before returning...  nextPage : " + nextPage);		
		return mapping.findForward(nextPage);
    	}catch(Exception e){
    		return mapping.findForward("exception");
    	}
		
   }
	
	
	

	
	
  
}