
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

import com.rdta.Admin.Utility.Helper;
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

public class CertifyReceiptDetailAction extends Action {
	
	private static Log log=LogFactory.getLog(CertifyReceiptDetailAction.class);
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	String pedigreeId;
	String clientIP = null;
	Connection conn; 
	Statement stmt;
	InsertDocToDB obj = new InsertDocToDB();
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
					
			try{	
				log.info("Inside CertifyReceiptDetailAction execute Method");
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
			
						
			//pedigreeId
		    pedigreeId = request.getParameter("pedigreeId");
			System.out.println("The pedigreeId from form in Certifying in detail screen is "+pedigreeId);
					
			//For Access Priveleges.
			String validate = "tlsp:validateAccess('"+sessionID+"','7.0','Insert')";
			log.info("Query for getting Access"+validate);
			List accessList = queryRunner.returnExecuteQueryStrings(validate);
			String insertStatus = accessList.get(0).toString();
			System.out.println("The insertStatus is "+insertStatus);
				
			if(insertStatus.equalsIgnoreCase("false")){
				return mapping.findForward("failure");
			}
							
			if(pedigreeId != null){
			
				String statusOfPedigrees ="tlsp:getStatusOfPedigree('"+pedigreeId+"')";
				log.info("Query for getting Status of Pedigree"+statusOfPedigrees);
				String pedigreestatus = queryRunner.returnExecuteQueryStringsAsString(statusOfPedigrees);
				System.out.println("The recvd pedigree is "+pedigreestatus);
				
				if(pedigreestatus.equalsIgnoreCase("Certified") ){
					return mapping.findForward("certified");					
				}else if(pedigreestatus.equalsIgnoreCase("Authenticated")){
					
					obj.createReceivedPedigree(pedigreeId,sessionID);
					//String receivedPedigreeForPedigrees = "tlsp:CreateReceivedPedigreeForPedigrees('"+pedigreeId+"','"+sessionID+"')";
					//log.info("The query for creating recvd pedigrees is "+receivedPedigreeForPedigrees);
					//List recvdPedigree = queryRunner.returnExecuteQueryStrings(receivedPedigreeForPedigrees);
					//System.out.println("The recvd pedigree is "+recvdPedigree);
					String changeStatus = "tlsp:InsertAndChangeStatus('"+pedigreeId+"','Certified','"+sessionID+"')";
					log.info("The query for InsertAndChangeStatus of pedigrees is "+changeStatus);
					List updateStatus = queryRunner.returnExecuteQueryStrings(changeStatus);
					System.out.println("The Document in pedigree status is "+updateStatus);
					
				}else //if(pedigreestatus.equalsIgnoreCase("Not Authenticated") ){
					return mapping.findForward("false");
				
//				List recvdPedigrees = queryRunner.returnExecuteQueryStrings("tlsp:CreateReceivedPedigreeForPedigrees('"+pedigreeId+"','"+sessionID+"')");
//				System.out.println("The recvd pedigrees are "+recvdPedigrees);
//				//List pedigreestatus = queryRunner.returnExecuteQueryStrings("tlsp:InsertAndChangeStatus('"+pedigreeId+"','Authenticated','"+sessionID+"')");
//				System.out.println("The Document in pedigree status is "+pedigreestatus);
				
			}
			
			}catch(PersistanceException e){    		
				log.error("Error in CertifyReceiptDetailAction execute method........." +e);
				throw new PersistanceException(e);
			}
			catch(Exception ex){ 
				ex.printStackTrace();
				log.error("Error in CertifyReceiptDetailAction execute method........." +ex);
				throw new Exception(ex);
				
			}
			return mapping.findForward("success");
		}
		
	}

