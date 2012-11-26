
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

public class AuthenticatePedigreesReceivingAction extends Action {
	
	private static Log log = LogFactory.getLog(AuthenticatePedigreesReceivingAction.class);
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	String envelopeId;
	String clientIP = null;
	Connection conn; 
	Statement stmt;
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		
		try{	
			log.info("Inside AuthenticatePedigreesReceivingAction execute Method");
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
			String validate = "tlsp:validateAccess('"+sessionID+"','8.0','Insert')";
			log.info("Query for getting Access"+validate);
			List accessList = queryRunner.returnExecuteQueryStrings(validate);
			String insertStatus = accessList.get(0).toString();
			System.out.println("The insertStatus is "+insertStatus);
			
			if(insertStatus.equalsIgnoreCase("false")){
			return mapping.findForward("failure");
			}
			
			
			if(envelopeId != null){
				
				boolean authentic = true;
				String statusOfPedigrees ="tlsp:getStatusOfPedigrees('"+envelopeId+"')";
				log.info("Query for getting Status of Pedigrees"+statusOfPedigrees);
				List pedigreesStatus = queryRunner.returnExecuteQueryStrings(statusOfPedigrees);
				System.out.println("Pedigrees status list is "+pedigreesStatus+" its size is "+pedigreesStatus.size());
				if(pedigreesStatus.size() == 0){
					
					StringBuffer buff = new StringBuffer();
					buff.append("for $ped in collection ('tig:///ePharma/ReceivedPedigree')"+"/*:pedigreeEnvelope[*:serialNumber = '"+envelopeId+"'] ");
					buff.append("return data($ped/*:pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber)");
					log.info("The query for getting pedigrees is "+buff.toString());
					System.out.println("The query for getting pedigrees is "+buff.toString());
					List pedigrees = queryRunner.returnExecuteQueryStrings(buff.toString());
					System.out.println("The pedigrees list size is "+pedigrees.size());
					
					for(int i=0; i<pedigrees.size();i++){
						String pedigreeId = (String)pedigrees.get(i);
						String changeStatus = "tlsp:InsertAndChangeStatus('"+pedigreeId+"','Authenticated','"+sessionID+"')";
						log.info("The query for InsertAndChangeStatus of pedigrees is "+changeStatus);
						List pedigreestatus = queryRunner.returnExecuteQueryStrings(changeStatus);
						System.out.println("The Document in pedigree status is "+pedigreestatus);
						
					}
				}
				
				else {
					
					authentic = authenticateStatus(pedigreesStatus);
					System.out.println("Authentic value is: "+authentic);
					if(authentic == true){
						return mapping.findForward("alreadyAuthenticated");
					}
					else {
						return mapping.findForward("alreadyAuthenticated");
					}
					
				}
				
		  }
			
		}catch(PersistanceException e){    		
			log.error("Error in AuthenticatePedigreesReceivingAction execute method........." +e);
			throw new PersistanceException(e);
		}
		catch(Exception ex){ 
			ex.printStackTrace();
			log.error("Error in AuthenticatePedigreesReceivingAction execute method........." +ex);
			throw new Exception(ex);
			
		}
		return mapping.findForward("success");
	}
	
	
	public boolean authenticateStatus(List pedigreesStatus) throws Exception{
		
		try{
			
			boolean authentic = false;
			
			for(int i=0; i<pedigreesStatus.size();i++){
				String status =  (String)pedigreesStatus.get(i);
				if(status.equalsIgnoreCase("Authenticated")){
					authentic = true;
					
				}else if(status.equalsIgnoreCase("Not Authenticated")){
					return false;
					
				}else {
					return false;
					
				}
				
			}
			return authentic;
		}catch(Exception ex){ 
			ex.printStackTrace();
			log.error("Error in AuthenticatePedigreesReceivingAction execute method........." +ex);
			//return mapping.findForward("exception");
			throw new Exception(ex);
		}
		
	}
	
}

