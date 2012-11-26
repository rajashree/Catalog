/*
 * Created on Oct 9, 2005
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
import org.w3c.dom.NodeList;

import com.rdta.Admin.Utility.Helper;
import com.rdta.catalog.Constants;
import com.rdta.catalog.trading.model.ProductMaster;
import com.rdta.commons.CommonUtil;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

/**
 * @author Arun Kumar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RemoveProduct extends Action {
	
	private static Log log = LogFactory.getLog(RemoveProduct.class);

	private static final QueryRunner queryrunner = QueryRunnerFactory
			.getInstance().getDefaultQueryRunner();
	
	
	Connection conn;

	Statement stmt;

	public void TLClose() {
		try {
			log.info("Closing the TigerLogic Connection in SaveDynamicFormAction..........");
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
			//to fetch the kit Record
		    //to fetch the Include Product Name
		
		try {

			log.info(" Inside List Action ");
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
			List accessList = queryrunner.returnExecuteQueryStrings("tlsp:validateAccess('"+sessionID+"','5.03','Delete')");
			
				log.info("Delete Access inside Product Search is :"+accessList.get(0));
				
				String readStatus = accessList.get(0).toString();
				log.info("The Delete Status "+readStatus);
			/*	if(readStatus.equals("false")){
					request.setAttribute("productDelete","false");
					request.setAttribute("gtin",request.getParameter("gtin"));
					request.setAttribute("ProductName",request.getParameter("ProductName"));
					request.setAttribute("Description",request.getParameter("Description"));
					System.out.println("hello");
					return mapping.findForward("failure");			
				}
				*/
					request.setAttribute("ProductDelete","true");

		     
			
			ProductMaster productMaster = (ProductMaster)sess.getAttribute(Constants.SESSION_KITREF_CONTEXT);
		    if ( productMaster != null){
		    	
		        String kitName = request.getParameter("ProductName");
		        String ProductName= request.getParameter("includeName");
				
		    	log.info("In the RemovePoduct from kit"+ProductName);
		    	Node node = XMLUtil.getNode( productMaster.getNode(),"IncludeProducts");
		        
		    	NodeList nodeList = node.getChildNodes();
		    	  
		    	NodeList subList=null;
		    	
		    	if ( nodeList != null){
		    		int length = nodeList.getLength();
		    		  
			    
		    	    Node subNode= null;
		    		for ( int i=0; i<length;++i){
		   			subNode = nodeList.item(i);
		   			
		   		//    subList = subNode.getChildNodes();
		   		   	if( ProductName.equals(XMLUtil.getValue(subNode,"ProductName"))){
		   		   		node.removeChild(subNode);	
		   		   			log.info(XMLUtil.convertToString(productMaster.getNode()));
		   		   	}
		    			
		   		 sess.setAttribute(Constants.SESSION_KITREF_CONTEXT,productMaster);
		    	
		    		
		    		
		    	} 
		    		
			
		    	}
			
		
		    }
		
		
		
			return mapping.findForward("success");
		}catch(Exception ex){
			
			
			return mapping.findForward("exception");
		}
	}
	

}
