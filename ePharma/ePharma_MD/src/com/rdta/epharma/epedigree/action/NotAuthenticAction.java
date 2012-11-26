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

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import com.rdta.Admin.Utility.Helper;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

public class NotAuthenticAction extends Action {
	
	private static Log log = LogFactory.getLog(NotAuthenticAction.class);
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	String envelopeId;
	String clientIP = null;
	Connection conn; 
	Statement stmt;
	
		public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
		{
					
			try{	
				
				HttpSession sess = request.getSession();
				Helper helper = new Helper();
				clientIP = request.getRemoteAddr();		
				
				conn = helper.ConnectTL(); 
				stmt = helper.getStatement(conn);
				System.out.println("validating The Session");
				
				//Validating Session
				String sessionID = (String)sess.getAttribute("sessionID");
				System.out.println("sessionID in Action :"+sessionID);
				String validateResult = helper.ValidateUserSession(stmt, sessionID, clientIP);        
			
				if (!validateResult.equals("VALID")){
				    //return a forward to invalid .
				    return mapping.findForward("loginPage");
				}
			
						
			//envelopeId
				envelopeId = request.getParameter("check");
				System.out.println("The envelopeId from form in Not Authentic is "+envelopeId);
					
			//For Access Priveleges.
			List accessList = queryRunner.returnExecuteQueryStrings("tlsp:validateAccess_MD('"+sessionID+"','8.0','Insert')");
			String insertStatus = accessList.get(0).toString();
			System.out.println("The insertStatus is "+insertStatus);
				
			if(insertStatus.equalsIgnoreCase("false")){
				return mapping.findForward("failure");
			}
							
			if(envelopeId != null){
			
				StringBuffer buff = new StringBuffer();
				buff.append("for $ped in collection ('tig:///ePharma_MD/ReceivedPedigree')"+"/PedigreeEnvelope[serialNumber = '"+envelopeId+"'] ");
				buff.append("return data($ped/pedigree/shippedPedigree/documentInfo/serialNumber)");
				System.out.println("The query for getting pedigrees is "+buff.toString());
				List pedigrees = queryRunner.returnExecuteQueryStrings(buff.toString());
				System.out.println("The pedigrees list size is "+pedigrees.size());
				
//				for(int i=0; i<pedigrees.size();i++){
//					String pedigreeId = (String)pedigrees.get(i);
//					List pedigreestatus = queryRunner.returnExecuteQueryStrings("tlsp:InsertAndChangeStatus('"+pedigreeId+"','Authentication Failed','"+sessionID+"')");
//					System.out.println("The Document in pedigree status is "+pedigreestatus);
//
//				}
				
			}
			
			}catch(Exception ex){
	    		ex.printStackTrace();
	    		log.error("Error in OrderSearchAction execute method........." +ex);
	    		return mapping.findForward("exception");
			}
			return mapping.findForward("success");
		}
		
	}

