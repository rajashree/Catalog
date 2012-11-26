/*
 * Created on Oct 31, 2005
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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.w3c.dom.Node;

import com.rdta.catalog.Constants;
import com.rdta.catalog.DataElementNode;
import com.rdta.catalog.MappingNodeObject;
import com.rdta.catalog.XMLStructure;
import com.rdta.catalog.session.ReconcilableData;
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;

/**
 * @author Jagadish Pampatwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UploadCatalogXMLAction  {
	
	private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	  private static Log log=LogFactory.getLog(UploadCatalogXMLAction.class);
	public UploadCatalogXMLAction(MappingNodeObject mappingNodeObj,String filePath,HttpServletRequest request) throws FileNotFoundException, PersistanceException
	{
		Node n=XMLUtil.parse(new File(filePath));
		log.info("THIS IS THE PRODUCTS STRING"+XMLUtil.convertToString(n));
		log.info("THIS IS THE MappingNodeObj STRING"+XMLUtil.convertToString(mappingNodeObj.getNode()));
		List list=XMLUtil.executeQuery(n,"Product");
		
		
		log.info("Total no of PRODUCTS "+list.size());
		
		HttpSession session = request.getSession(false);
		String mandatory= session.getAttribute(Constants.MANDATORY_ELEMENTS).toString();
		
		String manList[]=mandatory.split(" ");
	  
		String alias= session.getAttribute(Constants.ALIAS_ELEMENTS).toString();
		log.info("aliaslist"+alias);
		String aliasColList[]=(alias).split(" ");
		log.info("columnlist"+aliasColList);
		List mappedList=new ArrayList();
		List exceptionList=new ArrayList();
		List reconcilableList=new ArrayList();
		List subReconcilableList=new ArrayList();
		int ma=0;
		int el=0;
		
		List dataElementList=mappingNodeObj.getDataElementsList();
		List result=new ArrayList();
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
					for(int y=0;y<manList.length;y++)
					{
						
						if(manList[y].equalsIgnoreCase(dataElementNode.getTargetEleName()))
						{
							log.info("manList"+manList[y]);
							String sVal=XMLUtil.getValue(product,manList[y]);
							String tVal=XMLUtil.getValue(masterProduct,manList[y]);
							if(sVal.equalsIgnoreCase(tVal))
							{
								XMLUtil.putValue(newProduct,dataElementNode.getAbsoluteSourceEleName(),sVal);
								log.info("Here Is the update of product"+XMLUtil.convertToString(newProduct));
								XMLUtil.putValue(newProduct,dataElementNode.getSourceEleName(),sVal);
								log.info("Here Is the update of product"+XMLUtil.convertToString(newProduct));
								dataElementNode.createNewValueMap(sVal,tVal);
							}
							else
							{
								
							}
						}
						
					} 
					for(int y=0;y<aliasColList.length;y++)
					{
						
						if(aliasColList[y].equalsIgnoreCase(dataElementNode.getTargetEleName()))
						{
							String sVal=XMLUtil.getValue(product,aliasColList[y]);
							String tVal=XMLUtil.getValue(product,aliasColList[y]);
							if(sVal.equalsIgnoreCase(tVal))
							{
								
								XMLUtil.putValue(newProduct,dataElementNode.getAbsoluteSourceEleName(),sVal);
								log.info("Here Is the update of product"+XMLUtil.convertToString(newProduct));
								dataElementNode.createNewValueMap(sVal,tVal);
							}
							else
							{
								List valueList=XMLUtil.executeQuery(product,dataElementNode.getSourceEleName()+"/@values");
								
							}
						}
						
					} 
					
					
				}
				
				mappedList.add(ma,newProduct);
				ma++;
			}
			else{
				el++;
				exceptionList.add(el,product);
				
			}
			
		}
		
		
		
		
	}
	
	
	
}
