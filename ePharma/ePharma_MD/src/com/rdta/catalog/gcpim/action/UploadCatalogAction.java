
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

 
package com.rdta.catalog.gcpim.action;


import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.w3c.dom.Node;

import com.rdta.Admin.Utility.Helper;
import com.rdta.catalog.CVSFormatConversion;
import com.rdta.catalog.Constants;
import com.rdta.catalog.MappingNodeObject;
import com.rdta.catalog.PersistanceUtil;
import com.rdta.catalog.XMLStructure;
import com.rdta.catalog.session.UploadCatalogContext;
import com.rdta.catalog.trading.action.CatalogUploadForm;
import com.rdta.catalog.trading.model.MappingCatalogs;
import com.rdta.commons.CommonUtil;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

/**
 * Catalog information collecting from the reques form.
 * 
 * 
 */
public class UploadCatalogAction extends Action
{
    private static Log log=LogFactory.getLog(UploadCatalogAction.class);
    private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	Connection conn;

	Statement stmt;

	public void TLClose() {
		try {
			log
					.info("Closing the TigerLogic Connection in SaveDynamicFormAction..........");
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
				
    	
    	
    	int exceptions=0;
    	 int mapped=0;
		String catalogGenId = request.getParameter("catalogGenId");
		
		
		String standardCatalogId = request.getParameter("standardCatalogId");
		
		//since we are using form type as multipart/form-data
		//incase of schema creation from upload process
	 	//we are losing control of request paramerts
		//so populate catalogId so that tree will populate later
		 request.setAttribute("catalogGenId",catalogGenId );
		 request.setAttribute("standardCatalogId",standardCatalogId );
		 
		
		log.debug(" CatalogGenId  : " + catalogGenId);
		log.info(" CatalogGenId  : " + catalogGenId);
		
		return mapping.findForward("startUploadProcess");
   
    	}catch(Exception ex){
    		log.info("Exceptin ");
    		return mapping.findForward("exception");
    	}
    	
    	}
	
	
	

	
	
  
}