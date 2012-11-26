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



import java.util.Enumeration;
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

import com.rdta.Admin.Utility.Helper;
import com.rdta.catalog.trading.model.Catalog;
import com.rdta.catalog.trading.model.ProductMaster;

import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import org.w3c.dom.Node;
import com.rdta.catalog.XMLStructure;
import com.rdta.catalog.OperationType;
import com.rdta.catalog.Constants;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

/**
 * 
 * 
 * 
 */
public class ProductMasterSearchAction extends Action {
    
	private static Log log=LogFactory.getLog(ProductMasterSearchAction.class);
	private static final QueryRunner queryrunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	Connection conn ;
	Statement  stmt ;
	
	public void TLClose() {
        try {
        log.info("Closing the TigerLogic Connection in SaveDynamicFormAction..........");
        stmt.close();
        conn.logoff();
        conn.close();
        log.info("Connection Closed !!!!!!!!!!!!");
        }catch(com.rdta.tlapi.xql.XQLConnectionException e){
        System.err.println(e);
        }catch(com.rdta.tlapi.xql.XQLException e){
        System.err.println(e);  
        }
	}     
	
	
	
	
	
    public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response)
								 throws Exception {
 
    	
 		
    	try{
    				
    		
    	log.info(" Inside List Action ");
    	HttpSession sess = request.getSession();
    	Helper helper = new Helper();
    	String clientIP = request.getRemoteAddr(); 

    	conn = helper.ConnectTL(); 
    	stmt = helper.getStatement(conn);
    	log.info("Validating The Session");

//    	 Validating Session
    	String sessionID = (String)sess.getAttribute("sessionID");
    	
    	
    	log.info("sessionID in Action is :"+sessionID);
    	String validateResult = helper.ValidateUserSession(stmt, sessionID, clientIP);        
    	log.info("ValidateResult *** ="+validateResult);
    	if (!(validateResult.equals("VALID"))){
    	    //return a forward to login page.
    	 TLClose();
    		return mapping.findForward("loginPage");
    	}
    //	System.out.println("came here also");
    	TLClose(); 	
    	
    	
    	
		String operation = request.getParameter("operationType");
		String kitCheck = request.getParameter("check1")==null?"":request.getParameter("check1");
		String  productType = request.getParameter("ProductType");
    	if(productType != null){
    		if(productType.equals("All")){
    			kitCheck="";
    		}else if(productType.equals("Products Only")){
    			kitCheck="Off";
    		}else if(productType.equals("Inside Kit")){
    			kitCheck="On";
    		}else if( productType.equals("Kits Only")){
    			kitCheck = "kit";
    		}
    		
    	}
		String kitName = request.getParameter("kitName");
		log.info("Kit Name is :"+kitName);
		
		String fromAddKit = request.getParameter("fromAddKit");
		log.info("fromAddKit is :"+fromAddKit);
		
		
		log.info("operation inside ProductMasterSearchAction is :"+operation);
		log.info("kitCheck is :"+kitCheck);

	//This is for two read the kit information			
		List accessLis1 = queryrunner.returnExecuteQueryStrings("tlsp:validateAccess('"+sessionID+"','5.03','Read')");
		String readKitStatus = accessLis1.get(0).toString();
		if(readKitStatus.equals("false")){
			request.setAttribute("kitread","No");
		}else{
			request.setAttribute("kitread","");
		}
	//	System.out.println("came here also");	

		List accessList = queryrunner.returnExecuteQueryStrings("tlsp:validateAccess('"+sessionID+"','5.01','Read')");
	
		log.info("Read Access inside Product Search is :"+accessList.get(0));
		
		String readStatus = accessList.get(0).toString();
	//	System.out.println("The insertStatus is "+readStatus);
		if(readStatus.equals("false")){
			request.setAttribute("search","No");
			return mapping.findForward("failure");			
		}else{
		
			request.setAttribute("search","");
		Enumeration enum = request.getParameterNames();
		int i=0;
		String field=null;
		String query=null;
		String operator = null;
		String xpath="";
		String finalquery = null;
		boolean flag=false;
		
		if (kitCheck.equalsIgnoreCase("On")){
			query = new String("( $a/Product/isKit = 'Yes' ) ");
			xpath="$a/Product/IncludeProducts/IncludeProduct/";
			operator = new String(" and ");
		}else if(kitCheck.equalsIgnoreCase("Off")){
			query = new String(" not( $a/Product/isKit = 'Yes' )");
			operator = new String(" and ");
			xpath="$a/Product/";
			System.out.println(query + operator + xpath);
			
			
		}else if (kitCheck.equalsIgnoreCase("kit")){
			query = new String("( $a/Product/isKit = 'Yes' ) ");
			operator = new String(" and  ");
			xpath="$a/Product/";
			
		}else{
			query = new String("");
			operator = new String(" and ");
			xpath="$a/Product/";
		}
		
		boolean flag1=false;
		while ( enum.hasMoreElements()){
			String string = (String)enum.nextElement();
			if(string != null){
				field = request.getParameter(string);
				if (string.equalsIgnoreCase("ProductName")){
					string="ProductName";
					flag1=true;
				}
				else if(string.equalsIgnoreCase("manufacturerName")){
					string="ManufacturerName";
					flag1=true;
				}else if(string.equalsIgnoreCase("ndc")){
					string="NDC";
					flag1=true;
				}else if(string.equalsIgnoreCase("lotNumber")){
					string="LotNumber";
					flag1=true;
				}else if(string.equalsIgnoreCase("GTIN")){
					string="GTIN";
					flag1=true;
				}else{
					flag1=false;
				}
			}
			
			log.info( "Element Name "+string);
			if(flag1){
			//System.out.println( "Element Name "+string);
				
				//field = request.getParameter(string);
				
			
				if(field != null && field.length() >0){
					flag=true;
					if(i == 0) {
						//finalquery= " ( contains ("+xpath+string +", '"+field+"')";
						finalquery= " ( "+xpath+string +" = '"+field+"'";
					i++;
					}else{
						finalquery = finalquery +operator +" ("+xpath+string+" = '"+field+"')"; 
					}
				}
			}
		     	
		}
	//	System.out.println("This is "+finalquery);
		List list = null;
		
		System.out.println( " flag satus"+flag);
		
		if(flag == true){
			if (kitCheck.equalsIgnoreCase("On")){
				System.out.println(" final query "+finalquery);   
				query =  query +" and "+finalquery+ ")";
		//		   System.out.println("Query =" +query);
				   list =  ProductMaster.getProductList(query);
			}
			else if(kitCheck.equalsIgnoreCase("Off")){
				 query = query +" and "+finalquery+ ")";
				 list =  ProductMaster.getProductList(query);
			}else if(kitCheck.equalsIgnoreCase("kit")){
				if(finalquery != null)
				query = query +" and "+finalquery+ ")";
				list = ProductMaster.getProductList(query);
			
			}else{
				
				query = query + finalquery +" )";
				
				list =  ProductMaster.getProductList(query);
			}
		}else{
			
			if (kitCheck.equalsIgnoreCase("On")){
				list = ProductMaster.getProductList(query);
			}else if(kitCheck.equalsIgnoreCase("Off")){
				list = ProductMaster.getProductList(query);
			}else if(kitCheck.equalsIgnoreCase("kit")){
				if(finalquery != null)
				query = query +" and "+finalquery+ ")";
				list = ProductMaster.getProductList(query);
			}
			else{
				list =  ProductMaster.getProductList();
			}
			
			
		}
		
		log.info("Final query is "+query);
		
		
		
	/*	if(operation != null){
			if(operation.trim().equalsIgnoreCase(OperationType.SEARCH)) {
				String operator = "";
				StringBuffer criteria = new StringBuffer();
				
				if(kitCheck.equals("on")){					
					operator = "or";
					//criteria.append("contains(($a/Product/isKit), 'Yes') and ");			
				}else{
					operator = "and";
				}
				
				String NDC = request.getParameter("NDC");
				log.info("NDC number is :"+NDC);
				
				if(NDC!=null && !NDC.trim().equalsIgnoreCase("")) {
					log.info("Inside NDC!=null && !NDC.trim().equalsIgnoreCase()");
					criteria.append("contains(($a/Product/NDC), ");
					criteria.append("'"+NDC+"')");					
				}
				String productName = request.getParameter("productName");
				log.info("productName name is :"+productName);
				
				if(productName!=null && !productName.trim().equalsIgnoreCase("")) {
					log.info("productName!=null && !productName.trim().equalsIgnoreCase()");
					criteria.append(operator+" contains(($a/Product/LegendDrugName), ");
					criteria.append("'"+productName+"')");					
				}
				
				String lotNumber = request.getParameter("lotNumber");
				log.info("lotNumber number is :"+lotNumber);
				if(lotNumber!=null && !lotNumber.trim().equalsIgnoreCase("")) {
					log.info("lotNumber!=null && !lotNumber.trim().equalsIgnoreCase()");
					criteria.append(operator+" contains(($a/LotNumber/NDC), ");
					criteria.append("'"+lotNumber+"')");
				}
				
				String manufacturerName = request.getParameter("manufacturerName");			
				log.info("manufacturerName number is :"+manufacturerName);
				if(manufacturerName!=null && !manufacturerName.trim().equalsIgnoreCase("")) {
					log.info("manufacturerName!=null && !manufacturerName.trim().equalsIgnoreCase()");
					criteria.append(operator+" contains(($a/Product/ManufacturerName), ");
					criteria.append("'"+manufacturerName+"')");					
				}
				
				String GTIN = request.getParameter("GTIN");
				log.info("GTIN number is :"+GTIN);
				if(GTIN != null && !GTIN.trim().equalsIgnoreCase("")) {
					log.info("GTIN != null && !GTIN.trim().equalsIgnoreCase()");
					criteria.append(operator+" contains(($a/Product/BarCode), ");
					criteria.append("'"+GTIN+"')");
				}
				
				if(kitCheck.equals("on")){					
					//operator = "or";
					criteria.append("and contains(($a/Product/isKit), 'Yes') ");
				}else{
					operator = "and";
				}
				
				
				String criteriaStr  = criteria.toString();
				//there is no criteria speciifed by user 
				if(criteriaStr.trim().equals("")) {
					log.info("Inside criteriaStr.trim().equals(\"\") ");
					list =  ProductMaster.getProductList();
				} else {
					if( criteriaStr.trim().startsWith("and") || criteriaStr.trim().startsWith("or") ) {
						//remove 'add' and 'or' before that
						criteriaStr = criteriaStr.substring(3,criteriaStr.length());
					}
					
					System.out.println(" Criteria String is " + criteriaStr );
					list =  ProductMaster.getProductList(criteriaStr);					
				}
				
			} else {
				System.out.println(" Operation type not specified : " + operation);
			}
			
		} else {
			list =  ProductMaster.getProductList();
		}
	
		//set the result here*/
	
		
		if(list != null){
			request.setAttribute("ProductMasterListInfo",list);
		}
		request.setAttribute("prduct","Yes");
		request.setAttribute("kitName", request.getParameter("kitName"));
		return mapping.findForward("success");
		}
		}
    	catch(Exception ex){
    		return mapping.findForward("exception");
    	}
    	}
    
 
	

   
  
}