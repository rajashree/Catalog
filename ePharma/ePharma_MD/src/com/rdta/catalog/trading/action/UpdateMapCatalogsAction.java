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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rdta.catalog.session.TradingPartnerContext;
import com.rdta.catalog.trading.model.Catalog;
import com.rdta.catalog.trading.model.MappingCatalogs;


import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.rdta.catalog.Constants;
import com.rdta.catalog.MappingNodeObject;
import com.rdta.catalog.XMLStructure;
import com.rdta.catalog.OperationType;
import com.rdta.commons.xml.XMLUtil;

import com.rdta.catalog.session.TradingPartnerContext;

import com.rdta.catalog.PersistanceUtil;

import com.rdta.catalog.session.CatalogContext;
import com.rdta.catalog.SchemaTree;

/**
 * Catalog information collecting from the reques form.
 * 
 * 
 */
public class UpdateMapCatalogsAction extends Action
{
    private static Log log=LogFactory.getLog(UpdateMapCatalogsAction.class);
	
	
    public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response)
								 throws Exception {
    	log.info("Inside Action  UpdateMapCatalogsAction....... ");
		log.info("Inside Action execute of UpdateMapCatalogsAction....");
		
		try{
 	
		String xpath = request.getParameter("xpath");
		String display = request.getParameter("display");
		String operationType = request.getParameter("operationType");
		String operation=(String)request.getParameter("operation");
		String cv=(String)request.getParameter("checkedValue");
			
		
		System.out.println(" UpdateMapCatalogsAction  xpath " + xpath  );
		System.out.println(" operationType  : " + operationType + "  display : " + display  );
		
		MappingNodeObject mappingNodeObject = null;
		HttpSession session = request.getSession(false);
		if(session != null) {
			
			            
			int y=0;
			if(operation!=null)
			{	//x previous operation
			 String x=(String)session.getAttribute("first");
			
			 /*if(x==null)
			 {   
			  
			 		
				session.setAttribute("first",operation);
				
				 
				y = Integer.parseInt(operation);
				
				x=operation;
			 }*/
			// else
			// {
			 	 
				y = Integer.parseInt(operation);
			 	//int k=Integer.parseInt(x);//k contain x
			 	//int l=Integer.parseInt(operation);//l contain new operation
			 	
			 	
			 	/*if((k+l)%3==0)
			 	{
			 		y=Integer.parseInt(x);
			 		if(y==2)
			 		{
			 		y=3;	
			 		}
			 		else
			 		{
			 			y=4;
			 		}
			 		session.setAttribute("first",null);
			 		
			 	}
			 	if((k+l)%2==0)
			 	{
			 		
			 		y=5;
			 	}
			 	*/
			 	 
			 		 	
			 		 
				  
				
			// }
			// System.out.println("x="+x);
			// session.setAttribute("first",operation);
			
			}
			mappingNodeObject = (MappingNodeObject) session.getAttribute(Constants.SESSION_CATALOG_MAPPING_CONTEXT);
			Node node = mappingNodeObject.getNode();
			
			 
			if (operationType == null && (y==1))  {
				
				
				if(operation.equals(session.getAttribute("first")))
				{
					System.out.println("SSEEESSS");
				}
				
				//if no operation type then consider as add to mapping schema
				if(display != null && xpath != null ) {
					if(display.trim().equalsIgnoreCase("right"))  {
						
						//create data element with default value NEED_TO_BE_DEFINED_RAJU
						
						Node ne= mappingNodeObject.getNode();
						System.out.println("The string"+XMLUtil.convertToString(ne));
						request.setAttribute("checkMap","1");
						
						//List neList1=XMLUtil.executeQuery(ne,"/MappingCatalogs/dataList/data/elementName[@absoluteSourceEleName='"+xpath+"']");
						
						//if(neList1.size()>0)
						//{
						//	request.setAttribute("Rightexists","true");
						//}
						//else{
						 System.out.println("String "+ne.getNodeName());
						 
						 String checkBoxName = "check";
							List dataElements = XMLUtil.executeQuery(mappingNodeObject.getNode(),"dataList/data");
							
								String checkedBoxValue = (String)session.getAttribute("checkedValue");
								System.out.println(" checkedBoxValue :" + checkedBoxValue);
								if(checkedBoxValue==null)
								{
									request.setAttribute("selectCheckBox","true");
								}
								
								 if(checkedBoxValue != null ) {
								 	 
								 	System.out.println(" checkedBoxValue :" + checkedBoxValue);
									if(checkedBoxValue.startsWith("20000"))
									{
										mappingNodeObject.replaceDataNodeSource(checkedBoxValue.replaceAll("20000",""),xpath);
										session.removeAttribute("checkedValue");
										//mappingNodeObject.replaceDataNodeSourceEleDefaultValue(checkedBoxValue.replaceAll("20000",""));
									}
								 }
							
						 
//						 mappingNodeObject.replaceDataNodeSourceEleDefaultValue(xpath);
						
						 //	mappingNodeObject.createDataNode(xpath);
						 
						 
						//}
											
					} 
					
					System.out.println(" Raju usswrtytery e yeyeey  y uur ");
				}
				
			}else if (operationType== null && (y==2) )  {
			
				//if no operation type then consider as add to mapping schema
				if(display != null && xpath != null ) {
					if(display.trim().equalsIgnoreCase("left"))  {
						
						//create data element with default value NEED_TO_BE_DEFINED_RAJU
						Node ne= mappingNodeObject.getNode();
						System.out.println("The string"+XMLUtil.convertToString(ne));
						
						List neList=XMLUtil.executeQuery(ne,"/MappingCatalogs/dataList/data/elementName[@absoluteTargetEleName='"+xpath+"']");
						request.setAttribute("checkMap","2");
						if(neList.size()>0)
						{
							request.setAttribute("exists","true");
						}
						else{
						System.out.println("String "+ne.getNodeName());
					//	mappingNodeObject.replaceDataNodeTargetEleDefaultValue(xpath);
						
						mappingNodeObject.createDataNode1(xpath);
						
						
						}
						
						
						
						
					}
					
					
				}
				
			} 
			else if (operationType== null && (y==3) )  {
				
				//if no operation type then consider as add to mapping schema
				if(display != null && xpath != null ) {
					if(display.trim().equalsIgnoreCase("right"))  {
						
						//create data element with default value NEED_TO_BE_DEFINED_RAJU
						
						mappingNodeObject.replaceDataNodeSourceEleDefaultValue(xpath);
					}  
					
					System.out.println(" Raju usswrtytery e yeyeey  y uur ");
				}
				
			} 
			else if (operationType== null && (y==4) )  {
				
				//if no operation type then consider as add to mapping schema
				if(display != null && xpath != null ) {
					if(display.trim().equalsIgnoreCase("left"))  {
						
						//create data element with default value NEED_TO_BE_DEFINED_RAJU
						mappingNodeObject.replaceDataNodeTargetEleDefaultValue(xpath);
						
					}  
					
					
				}
				
			} 
			
			else if (operationType== null && (y==5) )  {
				
				//if no operation type then consider as add to mapping schema
				if(display != null && xpath != null ) {
					if(display.trim().equalsIgnoreCase("left"))  {
						
						//create data element with default value NEED_TO_BE_DEFINED_RAJU
						//mappingNodeObject.replaceDataNodeTargetEleDefaultValue(xpath);
						
					}  
					
					
				}
				
			} 
			
			else if(operationType.equalsIgnoreCase("SAVE")) {
				//udate catalog node in database with schema defination
				StringBuffer buff = new StringBuffer("$a/MappingCatalogs/genId='");
				buff.append( XMLUtil.getValue(node,"genId"));
				buff.append("'");
				
				boolean b = XMLUtil.evaluate(mappingNodeObject.getNode(),"//@absoluteSourceEleName='NEED_TO_BE_DEFINED_RAJU'");
				 if(b)
				 {
				 	request.setAttribute("mapNode","2");
				 }
				else{
							
				PersistanceUtil.updateDocument(node,Constants.MAPPING_CATALOGS_COLL,buff.toString());
				}
			}
			else if(operationType.equalsIgnoreCase("checked")) {
				
				//String checkedValue=(String)request.getAttribute("checkedValue");
				 session.setAttribute("checkedValue",cv);
				
			}
			
			else if(operationType.equalsIgnoreCase("DELETE") ) {
				
				System.out.println(" Inside Delete :"); 
				String checkBoxName = "check";
				List dataElements = XMLUtil.executeQuery(mappingNodeObject.getNode(),"dataList/data");
				 for(int i=0; i< dataElements.size(); i++) {
					String checkedBoxValue = request.getParameter( checkBoxName+ i);
					System.out.println(" checkedBoxValue :" + checkedBoxValue);
					
					
					 if(checkedBoxValue != null ) {
					 	 
					 	String cbv[]=checkedBoxValue.split("20000");
						System.out.println(" checkedBoxValue :" + checkedBoxValue);
						if(checkedBoxValue.endsWith("20000"))
						{
							mappingNodeObject.deleteSourceElementBasedOnAbsolutePath(checkedBoxValue.replaceAll("20000",""));
							session.removeAttribute("checkedValue");
						}
						else if(checkedBoxValue.startsWith("20000"))
						{
							mappingNodeObject.deleteTargetElementBasedOnAbsolutePath(checkedBoxValue.replaceAll("20000",""));
							session.removeAttribute("checkedValue");
							
						}
						else{

							System.out.println("cbv[1]"+cbv[1]);
							System.out.println("cbv[2]"+cbv[0]);
							mappingNodeObject.deleteElement(cbv[0],cbv[1]);	
							session.removeAttribute("checkedValue");
						}
					}
					 
				}
			} 
			
			System.out.println(" Raxvfgdd tee !!!!! ");
			
		}//end of session
			
		
		}catch(Exception ex)
		{
	    	ex.printStackTrace();
			log.error("Error in UpdateMapCatalogsAction execute()" + ex);
			return mapping.findForward("exception");
	
		}
		
		
		return mapping.findForward("success");
   }
	
}