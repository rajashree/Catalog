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


import java.util.ArrayList;
import java.util.HashMap;
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
import org.w3c.dom.Node;

import com.rdta.catalog.Constants;
import com.rdta.catalog.DataElementNode;
import com.rdta.catalog.MappingNodeObject;
import com.rdta.catalog.XMLStructure;
import com.rdta.catalog.session.CatalogContext;
import com.rdta.catalog.trading.model.Catalog;
import com.rdta.catalog.trading.model.MappingCatalogs;
import com.rdta.catalog.trading.model.MappingProducts;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;

/**
 * Catalog information collecting from the reques form.
 * 
 * 
 */
public class TradingPartnerMappingAction extends Action
{
	private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	private static Log log=LogFactory.getLog(TradingPartnerMappingAction.class);
	
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		
		String leftCatalogGenId = request.getParameter("leftCatalogGenId");
		String rightCatalogGenId = request.getParameter("rightCatalogGenId");
		request.setAttribute("standardCatalogId",rightCatalogGenId);
		
		
		log.debug(" leftCatalogGenId  : " + leftCatalogGenId + "  rightCatalogGenId : " + rightCatalogGenId );
		
		log.info(" leftCatalogGenId  : " + leftCatalogGenId + "  rightCatalogGenId : " + rightCatalogGenId  );
		
		
		//		 added on 28-09 here
		String query1="for $l in collection('tig:///CatalogManager/MappingCatalogs')";
		query1=query1+" where $l/*//source[catalogId='"+leftCatalogGenId+"']";
		query1=query1+"return ";
		query1=query1+"data($l/*//target/catalogId)";
		
		
		List list1=queryRunner.returnExecuteQueryStrings(query1);
		
		log.info("LIST1 SIZE"+list1.size());
		String temp1 = "1";
		for(int p=0;p<list1.size();p++)
		{	
		log.info("list1.get(p)"+(String)list1.get(p));
		String query2="for $l in collection('tig:///CatalogManager/Catalog')";
		query2=query2+" where $l//catalogID ='"+(String)list1.get(p)+"' ";
		query2=query2+"return ";
		query2=query2+"data($l//collectionName)";
		
		List nameList=queryRunner.returnExecuteQueryStrings(query2);
		
		 log.info("COLLECTION NAMELIST IS"+nameList.size());
		if(nameList.size()>0)
		{	
		 log.info("COLLECTION NAME IS"+(String)(nameList.get(0)));
		String temp = (String)(nameList.get(0));
		if(temp.equals("System"))
		{
			temp1 = (String) (list1.get(p));
		}
		}
		 
		}
		
		
		String query="for $l in collection('tig:///CatalogManager/MappingCatalogs')";
		query=query+" where $l/*//source[catalogId='"+rightCatalogGenId+"']";
		query=query+"return "; 
		query=query+"data($l/*//target/catalogId)";
		
		List list2=queryRunner.returnExecuteQueryStrings(query);
		log.info("LIST2 SIZE"+list2.size());
		String temp2 = "2";
		for(int q=0;q<list2.size();q++)
		{	
		log.info("(String)list2.get(q)"+(String)list2.get(q));
		String query3="for $l in collection('tig:///CatalogManager/Catalog')";
		query3=query3+" where $l//catalogID ='"+(String)list2.get(q)+"' ";
		query3=query3+"return ";
		query3=query3+"data($l//collectionName)";
		
		List nameList2=queryRunner.returnExecuteQueryStrings(query3);
		
		if(nameList2.size()>0)
		{	
	 	log.info("COLLECTION NAME IS"+(String) nameList2.get(0));
		
		String temp3=(String) nameList2.get(0);
		if(temp3.equals("System"))
		{
			temp2 = (String) list2.get(q);
		}
		}
		}
		
	//	log.info("temp1.equals(temp2)"+temp1.equals(temp2));
		if(temp1.equals(temp2))
		{
			
			
			
			
			//	HttpSession session = request.getSession(true);
			//	if(session != null) {
			
		    
		 	List mappedlist=new ArrayList();					 
			MappingCatalogs  mappingCatalog1 =MappingCatalogs.find(leftCatalogGenId,temp1);
			MappingCatalogs  mappingCatalog2 =MappingCatalogs.find(rightCatalogGenId,temp1);
			MappingNodeObject mappingNodeObj1 = new MappingNodeObject(mappingCatalog1.getNode());
			MappingNodeObject mappingNodeObj2 = new MappingNodeObject(mappingCatalog2.getNode());
			
			
			List datalist1 = mappingNodeObj1.getDataElementsList();
			List datalist2 = mappingNodeObj2.getDataElementsList();
			log.info("HEre is the element"+((DataElementNode)datalist1.get(0)).getTargetEleName());
			DataElementNode den1=null;
			DataElementNode den2=null;
			String s1="";
			String s2="";
			int k=0;
			Catalog leftCatalog = Catalog.find(leftCatalogGenId);

			Catalog rightCatalog = Catalog.find(rightCatalogGenId);
			CatalogContext catalogContext = new CatalogContext();
			catalogContext.setCatalogNode(leftCatalogGenId,leftCatalog.getNode());
			catalogContext.setCatalogNode(rightCatalogGenId,rightCatalog.getNode());
			MappingProducts  mappingProduct =MappingProducts.find(leftCatalogGenId,rightCatalogGenId);
			
			
			if(mappingProduct!=null)
			 { 
				MappingNodeObject mappingNodeObj = new MappingNodeObject(mappingProduct.getNode()); 
				HttpSession session = request.getSession(true);
				session.setAttribute(Constants.SESSION_CATALOG_MAPPING_CONTEXT,mappingNodeObj);
				session.setAttribute(Constants.SESSION_CATALOG_CONTEXT,catalogContext);
			 	 
			}
			else{
				 //if not found create new mapping node
				
			 	Node mappingNode = XMLStructure.getMappingProducts();
			 	mappingProduct = new MappingProducts(mappingNode);
			 	mappingProduct.insert(leftCatalogGenId,rightCatalogGenId);
				log.info("Inside the IF");
			 
			
		 	
			for(int i=0;i<datalist1.size();i++)
			{
				
				for(int j=0;j<datalist2.size();j++)
				{	
					
					den1=(DataElementNode) datalist1.get(i) ;
					
					 Node d = den1.getNode();
				     String x=XMLUtil.convertToString(d,true);
					
				 	log.info(""+x);
					s1=den1.getTargetEleName();
					
					log.info("Target Element1" +s1);
					
					den2=(DataElementNode) datalist2.get(j) ;
				//	log.info(""+XMLUtil.convertToString(den1,true));
					
					
					s2=den2.getTargetEleName();
					log.info("Target Elemetn 2" +s2);
					if(s1.equals(s2))
					{
						d = den1.getNode();
						List valueList1=XMLUtil.executeQuery(d,"values/value");
						List valueList2=XMLUtil.executeQuery(d,"values/value");
						log.info("VALUE LIST1"+valueList1.size());
						log.info("VALUE LIST1"+valueList2.size());
						Map map1 = new HashMap();
						for(int l=0;l<valueList1.size();l++)
						{	
							if(XMLUtil.getValue((Node)valueList1.get(l),"@targetValue")!=null && XMLUtil.getValue((Node)valueList1.get(l),"@sourceValue")!=null)
							{	
							map1.put(XMLUtil.getValue((Node)valueList1.get(l),"@targetValue"),XMLUtil.getValue((Node)valueList1.get(l),"@sourceValue"));
							}
						}
						Map map2 = new HashMap();
						for(int l=0;l<valueList1.size();l++)
						{
						if(XMLUtil.getValue((Node)valueList2.get(l),"@targetValue")!=null && XMLUtil.getValue((Node)valueList2.get(l),"@sourceValue")!=null)
						{	
						map2.put(XMLUtil.getValue((Node)valueList2.get(l),"@targetValue"),XMLUtil.getValue((Node)valueList2.get(l),"@sourceValue"));
						}
						}
						//log.info("VALUE LIST1"+XMLUtil.getValue((Node)valueList1.get(1),"@sourceValue"));
						//log.info("VALUE LIST1"+XMLUtil.getValue((Node)valueList2.get(1),"@sourceValue"));
						MappingNodeObject mappingNodeObj = new MappingNodeObject(mappingProduct.getNode());
						
						log.info("Strings are Equal"+s1);
						log.info("Inside the if");
					//	mappingNodeObj.createDataNode(den1.getAbsoluteSourceEleName(),den2.getAbsoluteSourceEleName(),XMLUtil.getValue((Node)valueList1.get(0),"@sourceValue"),XMLUtil.getValue((Node)valueList2.get(0),"@sourceValue"));
						mappingNodeObj.createDataNode(den1.getAbsoluteSourceEleName(),den2.getAbsoluteSourceEleName());
						//mappingNodeObj.createDataNode(den1.getAbsoluteSourceEleName(),den2.getAbsoluteSourceEleName(),map1,map2);						
						DataElementNode dataElementNode = mappingNodeObj.getDataElementNode(s1);
						Set set = map1.entrySet();
						Set set2 = map2.entrySet();
						Iterator iterator = set.iterator();
						for(int r=0;r<set2.size();r++)
						{
						while(iterator.hasNext()){
							Map.Entry entry = (Map.Entry)iterator.next();
							if(map2.containsKey(entry.getKey())){
								log.info("Map 1------>"+map1);
								log.info("Map 2------>"+map2);
								log.info("Key------>"+entry.getKey());
								log.info("KEY1 ======>"+map1.get(entry.getKey()));
								log.info("KEY2 ======>"+map2.get(entry.getKey()));
								dataElementNode.createNewValueMap(map1.get(entry.getKey()).toString(),map2.get(entry.getKey()).toString());
								
							}
						 
						}
						}
						
						
						
						mappedlist.add(k,s1);
						k++;
					} 
					
				}                
				
			}
			
			
			
		 mappingProduct.update();
			log.info("datalist 2" );
			
			 	 }		
			
			
			
		  	log.info("mappedlist"+mappedlist);		 
			
			
			return mapping.findForward("success");
		}
		
		
		
		return mapping.findForward("failure");
	}
	
}