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

 
package com.rdta.epharma.epedigree.action;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rdta.catalog.trading.model.Catalog;
import com.rdta.catalog.trading.model.ProductMaster;

import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import org.w3c.dom.Node;

import test.Helper;

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
public class SSCCNumberAction extends Action
{
	    private static Log log=LogFactory.getLog(SSCCNumberAction.class);
		private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
		
		Connection Conn; 
		Statement stmt;
		String clientIP = null;
		
	  public ActionForward execute(ActionMapping mapping, ActionForm form,
									 HttpServletRequest request, HttpServletResponse response)
									 throws Exception {
	 
			log.info("****Inside SSCCNumberAction class......");
			log.info(" Inside List Action ");
			
			try{	
				
				HttpSession sess = request.getSession();
				Helper helper = new Helper();
				clientIP = request.getRemoteAddr();		
				
				Conn = helper.ConnectTL(); 
				stmt = helper.getStatement(Conn);
				log.info("Validating The Session");
				
				//Validating Session
				String sessionID = (String)sess.getAttribute("sessionID");
				log.info("sessionID in GenerateAlertAction :"+sessionID);
				String validateResult = helper.ValidateUserSession(stmt, sessionID, clientIP);        
			
				if ( !validateResult.equals("VALID")){
				    //return a forward to invalid .
				    return mapping.findForward("loginPage");
				} 
			
			String listOfSSCCNumbers = request.getParameter("listOfSSCCNumbers");
			StringBuffer result = new StringBuffer();
		
			if(listOfSSCCNumbers != null) {
				result.append(listOfSSCCNumbers);
			}
			
			String operationType = request.getParameter("OperationType");
			log.info(" operationType:  " + operationType + " listOfSSCCNumbers: "  + listOfSSCCNumbers);
					
			if(operationType != null) {
				
				if(operationType.trim().equalsIgnoreCase("ADD")) {
				
					log.info(" Inside  ADD Action ");
					String newValue =  request.getParameter("ssccNum");
					
					if(newValue != null && !newValue.equalsIgnoreCase("")) {
						result.append(newValue);
						result.append(";");
					}
										
				} else if(operationType.trim().equalsIgnoreCase("DONE")) {
					log.info(" Inside DONE Action ");
				}
			}
			//set the result here
			request.setAttribute("listOfSSCCNumbers",result.toString());
			}catch(Exception ex){
		  		ex.printStackTrace();
		  		log.error("Error inside ProductList Class........."+ex);
		  		return mapping.findForward("exception");
		  	}
			return mapping.findForward("success");
	   }//end of method
}//end of class