
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


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

import com.rdta.Admin.Utility.Helper;
import com.rdta.catalog.Constants;
import com.rdta.catalog.OperationType;
import com.rdta.catalog.gcpim.action.UpdateSchemaTreeAction;
import com.rdta.catalog.trading.model.ProductMaster;
import com.rdta.commons.CommonUtil;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;
 

/**
 * Product information collecting from the request form.
 * 
 * 
 */
public class ProductAddToKitAction extends Action
{
 	
    private static Log log = LogFactory.getLog(ProductAddToKitAction.class);

	private static boolean flag = false;

	private static final QueryRunner queryrunner = QueryRunnerFactory
			.getInstance().getDefaultQueryRunner();

	Connection conn;

	Statement stmt;

	public void TLClose() {
		try {
			log.info("Closing the TigerLogic Connection in ProductAddToKitAction..........");
			stmt.close();
			conn.logoff();
			conn.close();
			log.info("Connection Closed !!!!!!!!!!!!");
		} catch (com.rdta.tlapi.xql.XQLConnectionException e) {
			System.err.println(e);
		} catch (com.rdta.tlapi.xql.XQLException e) {
			System.err.println(e);
		}
	}

	
    
    
    
	
    public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response)
								 throws Exception {
 	
    	
    	
    	
    	
    	try {
			log.info(" Inside GetAccess ");
			HttpSession sess = request.getSession();
			Helper helper = new Helper();
			String clientIP = request.getRemoteAddr();

			conn = helper.ConnectTL();
			stmt = helper.getStatement(conn);
			log.info("Validating The Session");

			// Validating Session
			String sessionID = (String) sess.getAttribute("sessionID");
			log.info("sessionID in Action is :" + sessionID);
			String validateResult = helper.ValidateUserSession(stmt, sessionID,
					clientIP);
			log.info("ValidateResult *** =" + validateResult);
			if (!(validateResult.equals("VALID"))) {
				//return a forward to login page.
				TLClose();
				return mapping.findForward("loginPage");
			}
			
			TLClose();
				
    	
    	
    	String operation = request.getParameter("operationType");
		String productGenId =  request.getParameter("productGenId");
		String[] productGenIds = request.getParameterValues("check");
		String from =  request.getParameter("From");
			
	
		log.debug(" ProductAddToKitAction Before doing operationType : " + operation);
		log.debug("ProductAddToKitAction Before doing productGenId : " + productGenId);
		log.debug("ProductAddToKitAction Before doing from : " + from);
		
		log.info(" ProductAddToKitAction Before doing Operation : " + operation);
		log.info("ProductAddToKitAction Before doing productGenId : " + productGenId);
		log.info("ProductAddToKitAction Before doing from : " + from);
		
		ProductMaster productMaster = null;
		
		HttpSession session = request.getSession(false);
		if(session != null) {
			productMaster = (ProductMaster)session.getAttribute(Constants.SESSION_KITREF_CONTEXT);
		}
		
			
					
		if(operation != null && productGenIds != null){
			
			if(operation.trim().equalsIgnoreCase(OperationType.ADD)) {
				  System.out.println("***************************");
				System.out.println("I am in Add to Product kit ");
				log.info(" Inside ADD methods Before converting to form!!!! "); 
				String genId = productMaster.getGenId();
				
				log.info("This is my productGenId"+productGenId);
				int pos;
				List list = new ArrayList();
				for(int i=0;i<productGenIds.length;i++){	
					productMaster.addProductRef(productGenIds[i]);
				    Node node = XMLUtil.parse((ByteArrayInputStream)productMaster.getAddList(productGenIds[i]).get(0));
				  
					list.add(i,node);
					
					
					
				}
				
				//		System.out.println("Product Name"+XMLUtil.getValue(node,"ProductName"));
			
				
				
	
				session.setAttribute("showKitnow",list);
				
				
				List productInf = productMaster.getKitProductList(genId);
			//	request.setAttribute("productInf",productInf);
			
				session.setAttribute(Constants.SESSION_KITREF_CONTEXT,productMaster);
			  
			    System.out.println("***************************");
			} else if(operation.trim().equalsIgnoreCase(OperationType.DELETE)) {
				log.debug(" Updating record generated value : " + request.getParameter("productGenId") );
				productMaster.deleteProductRef(productGenId);
			} else {
				log.warn(" Operation Type : " + operation + " not dealing in this action!");
			}
		}
		
		
		
		
		log.info(" XML String Node"  + 	XMLUtil.convertToString(productMaster.getNode()));

		log.info("before retuning Success! ");
		return mapping.findForward("success");
   }catch(Exception ex){
   			log.error(ex);
   			return mapping.findForward("exception");
   }
   	
   }
	

   
  
}