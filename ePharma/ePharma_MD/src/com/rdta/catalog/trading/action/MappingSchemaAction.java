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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.rdta.catalog.Constants;
import com.rdta.catalog.MappingNodeObject;
import com.rdta.catalog.XMLStructure;
import com.rdta.catalog.session.CatalogContext;
import com.rdta.catalog.trading.model.Catalog;
import com.rdta.catalog.trading.model.MappingCatalogs;
import com.rdta.commons.xml.XMLUtil;

/**
 * Catalog information collecting from the reques form.
 * 
 * 
 */
public class MappingSchemaAction extends Action
{
	
	public static StringBuffer abc =new StringBuffer("");
	public static StringBuffer xyz=new StringBuffer("");
	public static String temp1="";
    private static Log log=LogFactory.getLog(MappingSchemaAction.class);
    static String  parent1=null;
    public static StringBuffer getNodeNames(Node node){
    	//String temp2="";
    	if(parent1 == null){
    		parent1=new String(node.getNodeName());
    	}
    	if(node.hasChildNodes())
    	{
    		
    		NodeList list = node.getChildNodes();
    		for(int i=0;i<list.getLength();i++)
    		{
    			if(list.item(i).hasChildNodes()){
    				//temp1="";
    				getNodeNames(list.item(i));
    			}else{

    				Node temp=list.item(i);
    				String temp3="";
    				while(!temp.getNodeName().equals(parent1)){
    				
    					temp3="/"+temp.getNodeName()+temp3;
    					temp=temp.getParentNode();
    				}
    				temp3=temp3.substring(1,temp3.length());
    			//	temp3="/"+temp.getNodeName()+temp3;
    				
    				abc.append(temp3+" ");
    				
    			}
    				
    		}
    	}
    	
    	return abc;
    }
    public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response)
								 throws Exception {
    	try{
        	
		String leftCatalogGenId = request.getParameter("leftCatalogGenId");
		String rightCatalogGenId = request.getParameter("rightCatalogGenId");
		
		request.setAttribute("standardCatalogId",rightCatalogGenId);
		
		
		log.debug(" leftCatalogGenId  : " + leftCatalogGenId + "  rightCatalogGenId : " + rightCatalogGenId );

		log.info(" leftCatalogGenId  : " + leftCatalogGenId + "  rightCatalogGenId : " + rightCatalogGenId  );
		
		CatalogContext catalogContext = new CatalogContext();
		
		
		
		
			
		Catalog leftCatalog = Catalog.find(leftCatalogGenId);
		
		Catalog rightCatalog = Catalog.find(rightCatalogGenId);

		if(leftCatalog != null && rightCatalog !=null) {
			HttpSession session = request.getSession(true);
			if(session != null) {
				session.removeAttribute(Constants.SESSION_CATALOG_MAPPING_CONTEXT);
				/* if(fromModule != null && fromModule.equalsIgnoreCase("TP") ) {
					 TradingPartnerContext context = (TradingPartnerContext)session.getAttribute("TradingPartnerContext");
					 context.setCatalogContext(catalogContext);
				 } else {
					 session.setAttribute(Constants.SESSION_CATALOG_CONTEXT,catalogContext); 
				 }*/

				 catalogContext.setCatalogNode(leftCatalogGenId,leftCatalog.getNode());
				 catalogContext.setCatalogNode(rightCatalogGenId,rightCatalog.getNode());
				 session.setAttribute(Constants.SESSION_CATALOG_CONTEXT,catalogContext);
				 session.setAttribute("rightCatalogGenId",rightCatalogGenId);
				 MappingCatalogs  mappingCatalog =MappingCatalogs.find(leftCatalogGenId,rightCatalogGenId);
				 if(mappingCatalog!=null)
				 {
				 	MappingNodeObject mapNode = new MappingNodeObject(mappingCatalog.getNode());
				 	List mapList=mapNode.getDataElementsList();
				 	if(mapList.size()==0)
				 	{
				 		mappingCatalog=null;
				 	}
				 }
				 
				 
				 
				 if(mappingCatalog!=null)
				 {
				 	
				 	
				 	MappingNodeObject mappingNodeObj = new MappingNodeObject(mappingCatalog.getNode());
				// 	Node catalogNode = catalogContext.getCatalogNode(leftCatalogGenId);
					Node standardCatalogNode= catalogContext.getCatalogNode(rightCatalogGenId);
					
				 	Node standardNode= XMLUtil.getNode(standardCatalogNode,"schema/*");
				    NodeList nj= standardNode.getChildNodes();
				    StringBuffer c=new StringBuffer();
				    abc=new StringBuffer("");
				    parent1=null;
				    c=getNodeNames(standardNode);
				    String rtparent= standardNode.getNodeName(); 
				//	Node tpnode = XMLUtil.getNode(catalogNode, "schema/*");
			    //	    String ltparent=tpnode.getNodeName();
				    String[] str1= new String(c).split(" ");
				//	log.info("str1 size ="+str1.length);
				    StringBuffer sb=new StringBuffer("");
				    StringBuffer allowAlia1 = new StringBuffer("");
					 for(int i=0;i<str1.length;i++)	{
						if(XMLUtil.evaluate(standardCatalogNode,"//"+str1[i]+"[@mandatory='true']"))
			    		{
			    			if(XMLUtil.evaluate(standardCatalogNode,"//"+str1[i]+"[@allowAllias='true']"))
			    			{
			    				allowAlia1.append(str1[i]+" ");
			    			}
						//	log.info("  MANDATORY ATTRIBUTE "+str1[i]);
			    			sb.append(str1[i]+" ");
			    			
			    		}  
					 }
					 String mandatoryElements=sb.toString();
					 String allowAlias=allowAlia1.toString();
					session.setAttribute(Constants.SESSION_CATALOG_MAPPING_CONTEXT,mappingNodeObj);
					log.info(" Mandatory Ele"+mandatoryElements); 
					log.info("allow alias"+allowAlias);
					session.setAttribute(Constants.MANDATORY_ELEMENTS,mandatoryElements);
					 session.setAttribute(Constants.ALIAS_ELEMENTS,allowAlias);
				 
				 }
				 else {
					 //if not found create new mapping node
					
				 	Node mappingNode = XMLStructure.getMappingCatalogs();
					mappingCatalog = new MappingCatalogs(mappingNode);
					mappingCatalog.insert(leftCatalogGenId,rightCatalogGenId);
					
				    Node catalogNode = catalogContext.getCatalogNode(leftCatalogGenId);
				 	Node standardCatalogNode= catalogContext.getCatalogNode(rightCatalogGenId);
					
					log.info("CatalogNode"+ XMLUtil.convertToString(catalogNode));
					log.info("CatalogNode"+ XMLUtil.convertToString(standardCatalogNode));
					
					Node tpnode = XMLUtil.getNode(catalogNode, "schema/*");
			 	    NodeList nl= tpnode.getChildNodes();
					abc= new StringBuffer("");
					 parent1=null;
				    StringBuffer b= getNodeNames(tpnode);
				    abc= new StringBuffer("");
				    String[] str= new String(b).split(" ");
				    Set set = new HashSet();
				    for(int i=0;i<str.length;i++) {
				    	set.add(str[i]);
				    		
				    
				    }
				    
				    
				    Node standardNode= XMLUtil.getNode(standardCatalogNode,"schema/*");
				    NodeList nj= standardNode.getChildNodes();
				    StringBuffer c=new StringBuffer();
				    abc=new StringBuffer("");
				    parent1=null;
				    c=getNodeNames(standardNode);
				    String rtparent= standardNode.getNodeName(); 
				    String ltparent=tpnode.getNodeName();
				    String[] str1= new String(c).split(" ");
					log.info("str1 size ="+str1.length);
				    StringBuffer sb=new StringBuffer("");
				    StringBuffer allowAlias = new StringBuffer("");
					 for(int i=0;i<str1.length;i++)	{
						if(XMLUtil.evaluate(standardCatalogNode,"//"+str1[i]+"[@mandatory='true']"))
			    		{
			    			if(XMLUtil.evaluate(standardCatalogNode,"//"+str1[i]+"[@allowAllias='true']"))
			    			{
			    				allowAlias.append(str1[i]+" ");
			    			}
							log.info("  MANDATORY ATTRIBUTE "+str1[i]);
			    			sb.append(str1[i]+" ");
			    			
			    		}  
					 }
					 String mandatoryElements=sb.toString();
				 //set the object into session
			 	 MappingNodeObject mappingNodeObj = new MappingNodeObject(mappingCatalog.getNode());
				 
		
				    
     			 
     			List nodeList =new ArrayList();
     			
     			int j=0;
				 for(int i=0;i<str1.length;i++)	{
			    	if(set.contains(str1[i]))	{
			    					    	
			    		
			        	 mappingNodeObj.createDataNode(ltparent+"/"+str1[i],rtparent+"/"+str1[i]);
			    		 nodeList.add(j,str1[i]);
			    		 j++;
			    		log.info(str1[i]+"***"+"Mapped");
			    	}
			    	else {
			    		
			    		log.info(str1[i]+"***"+"Not Mapped");
					}
			 }
				 
				 session.setAttribute(Constants.MANDATORY_ELEMENTS,mandatoryElements);
				 session.setAttribute(Constants.ALIAS_ELEMENTS,allowAlias);
				 session.setAttribute(Constants.STD_CAT_TREE,nodeList);
				 
				 session.setAttribute(Constants.SESSION_CATALOG_MAPPING_CONTEXT,mappingNodeObj);
			
				 }
				 }// end of session not null if
		}
    	}catch(Exception e){
    		e.printStackTrace();
    		log.error("Error in MappingSchemaAction Action class......");
    		mapping.findForward("exception");
    		
    	}
		return mapping.findForward("success");
   }

  
}