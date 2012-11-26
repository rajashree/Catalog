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
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

public class AuthenticatePedigreesDetailAction extends Action {
	
	private static Log log = LogFactory.getLog(AuthenticatePedigreesDetailAction.class);
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	String pedigreeId;
	String clientIP = null;
	Connection conn; 
	Statement stmt;
	String authenticateStatusInDetail;
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		
		try{	
			log.info("Inside AuthenticatePedigreesDetailAction execute Method");
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
			String time="";
			String date="";
			
			if (!validateResult.equals("VALID")){
				//return a forward to invalid .
				return mapping.findForward("loginPage");
			}
			
			//pedigreeId
			pedigreeId = request.getParameter("PedigreeId");
			System.out.println("The pedigreeId from form in Certifying in detail screen is "+pedigreeId);
			
			//For Access Priveleges.
			String validate = "tlsp:validateAccess_MD('"+sessionID+"','8.0','Insert')";
			log.info("Query for getting Access"+validate);
			List accessList = queryRunner.returnExecuteQueryStrings(validate);
			String insertStatus = accessList.get(0).toString();
			System.out.println("The insertStatus is "+insertStatus);
			
			if(insertStatus.equalsIgnoreCase("false")){
			return mapping.findForward("failure");
			}
			
			
			if(pedigreeId != null){
				
				//String statusOfPedigrees ="tlsp:getStatusOfPedigree('"+pedigreeId+"')";
				String statusOfPedigrees ="tlsp:getAuthPedStatus_MD('"+pedigreeId+"')";
				
				log.info("Query for getting Status of Pedigree"+statusOfPedigrees);
				String pedigreestatus = queryRunner.returnExecuteQueryStringsAsString(statusOfPedigrees);
				
				if(pedigreestatus.length()!= 0){
					Node authNode = XMLUtil.parse(pedigreestatus);
					pedigreestatus= XMLUtil.getValue(authNode,"status");
					time=XMLUtil.getValue(authNode,"time");
					date=XMLUtil.getValue(authNode,"date");
					
					sess.setAttribute("pedstatus",pedigreestatus);
					sess.setAttribute("pedtime",time);
					sess.setAttribute("peddate",date);
					
				}
				
				
				System.out.println("Pedigrees status is : "+pedigreestatus);
				
				if(pedigreestatus.equalsIgnoreCase("Authenticated") || pedigreestatus.equalsIgnoreCase("Not Authenticated") || pedigreestatus.equalsIgnoreCase("Certified")){
					authenticateStatusInDetail = "AlreadyAuthenticated";
	  			    request.setAttribute("AuthenticateStatusInDetail",authenticateStatusInDetail);
	   				//return mapping.findForward("alreadyAuthenticated");
				}
				else{
					String changeStatus = "tlsp:InsertAndChangeStatus_MD('"+pedigreeId+"','Authenticated','"+sessionID+"')";
					log.info("The query for InsertAndChangeStatus of pedigrees is "+changeStatus);
					List updatestatus = queryRunner.returnExecuteQueryStrings(changeStatus);
					System.out.println("The Document in pedigree status is "+updatestatus);
					authenticateStatusInDetail = "AuthenticatedSuccessfully";
					
					pedigreestatus = queryRunner.returnExecuteQueryStringsAsString(statusOfPedigrees);
					if(pedigreestatus.length()!= 0){
						Node authNode = XMLUtil.parse(pedigreestatus);
						pedigreestatus= XMLUtil.getValue(authNode,"status");
						time=XMLUtil.getValue(authNode,"time");
						date=XMLUtil.getValue(authNode,"date");
						
						sess.setAttribute("pedstatus",pedigreestatus);
						sess.setAttribute("pedtime",time);
						sess.setAttribute("peddate",date);
						
					}
					request.setAttribute("AuthenticateStatusInDetail",authenticateStatusInDetail);
				}
				
			}
			
		}catch(PersistanceException e){    		
			log.error("Error in AuthenticatePedigreesDetailAction execute method........." +e);
			throw new PersistanceException(e);
		}
		catch(Exception ex){ 
			ex.printStackTrace();
			log.error("Error in AuthenticatePedigreesDetailAction execute method........." +ex);
			throw new Exception(ex);
			
		}
		return mapping.findForward("success");
	}
	
	
}

