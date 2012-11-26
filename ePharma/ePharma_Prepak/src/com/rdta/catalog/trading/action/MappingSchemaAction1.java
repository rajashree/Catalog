
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
public class MappingSchemaAction1 extends Action
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
    			System.out.println("I am in the static function"+abc);	
    			}
    				
    		}
    	}
    	
    	return abc;
    }
    public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response)
								 throws Exception {
 	
		String leftCatalogGenId = request.getParameter("leftCatalogGenId");
		String rightCatalogGenId = request.getParameter("rightCatalogGenId");
		request.setAttribute("standardCatalogId",rightCatalogGenId);
		
		
		log.debug(" leftCatalogGenId  : " + leftCatalogGenId + "  rightCatalogGenId : " + rightCatalogGenId );

		System.out.println(" leftCatalogGenId  : " + leftCatalogGenId + "  rightCatalogGenId : " + rightCatalogGenId  );
		
		CatalogContext catalogContext = new CatalogContext();
		
		
		
		
			
		Catalog leftCatalog = Catalog.find(leftCatalogGenId);
		
		Catalog rightCatalog = Catalog.find(rightCatalogGenId);

		if(leftCatalog != null && rightCatalog !=null) {
			HttpSession session = request.getSession(true);
			if(session != null) {
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
				 	MappingNodeObject mappingNodeObj = new MappingNodeObject(mappingCatalog.getNode());
					 session.setAttribute(Constants.SESSION_CATALOG_MAPPING_CONTEXT,mappingNodeObj);
					 
				 }
				 if(mappingCatalog == null) {
					 //if not found create new mapping node
					
				 	Node mappingNode = XMLStructure.getMappingCatalogs();
					mappingCatalog = new MappingCatalogs(mappingNode);
					mappingCatalog.insert(leftCatalogGenId,rightCatalogGenId);
					
					
				 
				 
				 
				    Node catalogNode = catalogContext.getCatalogNode(leftCatalogGenId);
				 	Node standardCatalogNode= catalogContext.getCatalogNode(rightCatalogGenId);
					
					System.out.println("CatalogNode"+ XMLUtil.convertToString(catalogNode));
					System.out.println("CatalogNode"+ XMLUtil.convertToString(standardCatalogNode));
					
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
				    
				    System.out.println("NodeNames"+ b);
				    Node standardNode= XMLUtil.getNode(standardCatalogNode,"schema/*");
				    NodeList nj= standardNode.getChildNodes();
				    StringBuffer c=new StringBuffer();
				    abc=new StringBuffer("");
				    parent1=null;
				    c=getNodeNames(standardNode);
				    String rtparent= standardNode.getNodeName(); 
				    String ltparent=tpnode.getNodeName();
				    String[] str1= new String(c).split(" ");
					System.out.println("str1 size ="+str1.length);
				    StringBuffer sb=new StringBuffer("");
					 for(int i=0;i<str1.length;i++)	{
						if(XMLUtil.evaluate(standardCatalogNode,"//"+str1[i]+"[@mandatory='true's]"))
			    		{
			    			System.out.println("HERE THERE IS MANDATORY ATTRIBUTE NOde"+str1[i]);
			    			sb.append(str1[i]+" ");
			    			
			    		}  
					 }
					 String mandatoryElements=sb.toString();
				 //set the object into session
			 	 MappingNodeObject mappingNodeObj = new MappingNodeObject(mappingCatalog.getNode());
				 
		
				    
     			// System.out.println("USING XMLUTIL ATTRIBUTE VALUE IS " +XMLUtil.getValue(standardNode,"/Arun/EPC/@values"));
     			// System.out.println("USING XMLUTIL ATTRIBUTE VALUE IS " +XMLUtil.getValue(tpnode,"//ParentEPC/@values"));
     			
     			List nodeList =new ArrayList();
     			
     			int j=0;
				 for(int i=0;i<str1.length;i++)	{
			    	if(set.contains(str1[i]))	{
			    					    	
			    		
			        	// String  targetValue=XMLUtil.getValue(standardNode,"//"+str1[i]+"/@values");
						 //String  sourceValue=XMLUtil.getValue(catalogNode,"//"+str1[i]+"/@values");						
			    		//System.out.println("sourceValue ====="+sourceValue);
			    		//System.out.println("targetValue ====="+targetValue);
			    		//mappingNodeObj.createDataNode(ltparent+"/"+str1[i],rtparent+"/"+str1[i],sourceValue,targetValue);
			    		 mappingNodeObj.createDataNode(ltparent+"/"+str1[i],rtparent+"/"+str1[i]);
			    		 nodeList.add(j,str1[i]);
			    		 j++;
			    		System.out.println(str1[i]+"***"+"Mapped");
			    	}
			    	else {
			    		
			    		System.out.println(str1[i]+"***"+"Not Mapped");
					}
			 }
				 
				 session.setAttribute(Constants.MANDATORY_ELEMENTS,mandatoryElements);
				 session.setAttribute(Constants.STD_CAT_TREE,nodeList);
				 session.setAttribute(Constants.SESSION_CATALOG_MAPPING_CONTEXT,mappingNodeObj);
			
				 }
				 }// end of session not null if
		}
			
		return mapping.findForward("success");
   }

  
}