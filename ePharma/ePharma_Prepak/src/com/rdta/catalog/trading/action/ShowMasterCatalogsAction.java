/*
 * Created on Oct 5, 2005
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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
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
import com.rdta.commons.CommonUtil;
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

/**
 * @author Santosh Subramanya
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ShowMasterCatalogsAction extends Action {
	
	 private static Log log = LogFactory.getLog(ShowMasterCatalogsAction.class);		
	 private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	 List xmlResults = null;
	 
	 StringBuffer buffer1 = null;
	 StringBuffer buffer2 = null;
	 
	 String clientIP = null;
	 Connection conn; 
	 Statement stmt;
	 
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
	 
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws PersistanceException {
		
		Collection colln = new ArrayList();
		
				
		try{	
					
			// session validation......
			HttpSession sess = request.getSession();
			Helper helper = new Helper();
			clientIP = request.getRemoteAddr(); 

			conn = helper.ConnectTL(); 
			stmt = helper.getStatement(conn);
			log.info("Validating The Session");

			// Validating Session
			String sessionID = (String)sess.getAttribute("sessionID");
			log.info("sessionID in Action is :"+sessionID);
			String validateResult = helper.ValidateUserSession(stmt, sessionID, clientIP);        

			if ( !validateResult.equals("VALID")){
			    //return a forward to login page.
			    return mapping.findForward("loginPage");
			}
			
			TLClose();
			
			String path = request.getRequestURI();
			log.info("The path from where I got here is: "+path);
			
			buffer2 = new StringBuffer();
			buffer2.append("tlsp:validateAccess('"+sessionID+"', '5.02', 'Read')");
			String readAccess = queryRunner.returnExecuteQueryStringsAsString(buffer2.toString());
			
			log.info("The read access in ShowMasterCatalogsAction is: "+readAccess);
			
			if(readAccess.equals("false")){				
				log.info("The user does not have READ Permission......");
				return mapping.findForward("NoReadAccess");
				
			}			
			
			
			buffer1 = new StringBuffer();
			buffer1.append(" tlsp:validateAccess('"+sessionID+"', '5.02', 'Insert')");
			String accessLevel = queryRunner.returnExecuteQueryStringsAsString(buffer1.toString());

			log.info("The access privilege to create new product is :"+accessLevel);
			
			request.setAttribute("createProduct", accessLevel);

			
			String tp_company_nm = request.getParameter("tp_company_nm");
			if(tp_company_nm == null){
				tp_company_nm = (String)request.getAttribute("tp_company_nm");
			}
			request.setAttribute("tp_company_nm", tp_company_nm);
		
			String pagenm = request.getParameter("pagenm");
			if(pagenm == null){
				pagenm = (String)request.getAttribute("pagenm");
			}
			request.setAttribute("pagenm", pagenm);				
		
			log.info("Inside ShowMasterCatalogsAction Action Class.........");
		
			StringBuffer buffer = new StringBuffer();
		
			buffer.append("tlsp:GetMasterCatalogNames()");					
			xmlResults = queryRunner.executeQuery(buffer.toString());
			log.info("After executing the query in ShowMasterCatalogsAction Action Class.....");
			log.info("The xmlResults after executing the query in ShowMasterCatalogsAction Action Class is :"+xmlResults);
		
			if(xmlResults != null){
				request.setAttribute("List", xmlResults);
			}	
		
			for(int i=0; i<xmlResults.size(); i++) {
						

		/*	log.info("Inside the for loop in ShowMasterCatalogsAction Action Class......");
			ShowMasterCatalogsForm theForm = new ShowMasterCatalogsForm();
			log.info("After creating the action form instance......");
			Node listNode = XMLUtil.parse((InputStream)xmlResults.get(i));
			log.info("After getting the root element....");
			theForm.setMasterCatalogNames(CommonUtil.jspDisplayValue(listNode,"Name"));
			log.info("The Master Catalog Name after setting the name in the ShowMasterCatalogsActionForm is :"+theForm.getMasterCatalogNames());
			theForm.setDescription(CommonUtil.jspDisplayValue(listNode,"Description"));
			log.info("The Master Catalog Description after setting the name in the ShowMasterCatalogsActionForm is :"+theForm.getDescription());
			theForm.setUpdatedDate(CommonUtil.jspDisplayValue(listNode,"UpdatedDate"));			
			log.info("The Master Catalog UpdatedDate after setting the name in the ShowMasterCatalogsActionForm is :"+theForm.getUpdatedDate());	
*/
				log.info("Inside the for loop in ShowMasterCatalogsAction Action Class......");
				ShowMasterCatalogsForm theForm = new ShowMasterCatalogsForm();
				log.info("After creating the action form instance......");
				Node listNode = XMLUtil.parse((InputStream)xmlResults.get(i));
				log.info("After getting the root element....");
				theForm.setMasterCatalogNames(CommonUtil.jspDisplayValue(listNode,"Name"));
				log.info("The Master Catalog Name after setting the name in the ShowMasterCatalogsActionForm is :"+theForm.getMasterCatalogNames());
				theForm.setDescription(CommonUtil.jspDisplayValue(listNode,"Description"));
				log.info("The Master Catalog Description after setting the name in the ShowMasterCatalogsActionForm is :"+theForm.getDescription());
				theForm.setUpdatedDate(CommonUtil.jspDisplayValue(listNode,"UpdatedDate"));
				
				log.info("The Master Catalog UpdatedDate after setting the name in the ShowMasterCatalogsActionForm is :"+theForm.getUpdatedDate());	

			
				colln.add(theForm);
			}
		
		}catch(Exception e){
			e.printStackTrace();
		}		
		
		log.info("Before returning success from ShowMasterCatalogsAction......");
		request.setAttribute(Constants.MASTER_CATALOG_NAMES,colln);
		return mapping.findForward("success");
	}
}	
