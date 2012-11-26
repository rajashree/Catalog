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

public class CertifyReceiptReceivingAction extends Action {
	
	private static Log log = LogFactory.getLog(NotAuthenticAction.class);
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	String envelopeId;
	String clientIP = null;
	Connection conn; 
	Statement stmt;
	List pedigreesStatus;
	InsertDocToDB obj = new InsertDocToDB();
		
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		
		try{	
			log.info("Inside CertifyReceiptReceivingAction execute Method");
			HttpSession sess = request.getSession();
			Helper helper = new Helper();
			clientIP = request.getRemoteAddr();		
			
			conn = helper.ConnectTL(); 
			stmt = helper.getStatement(conn);
						
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
		System.out.println("The envelopeId from form in Certifying is "+envelopeId);
				
		//For Access Priveleges.
		String validate = "tlsp:validateAccess_MD('"+sessionID+"','7.0','Insert')";
		log.info("Query for getting Access"+validate);
		List accessList = queryRunner.returnExecuteQueryStrings(validate);
		String insertStatus = accessList.get(0).toString();
		System.out.println("The insertStatus is "+insertStatus);
			
		if(insertStatus.equalsIgnoreCase("false")){
			return mapping.findForward("failure");
		}
						
		if(envelopeId != null){
			
			boolean certify = true;
			boolean authentic = true;
			String statusOfPedigrees ="tlsp:getStatusOfPedigrees_MD('"+envelopeId+"')";
			log.info("Query for getting Status of Pedigrees"+statusOfPedigrees);
			pedigreesStatus = queryRunner.returnExecuteQueryStrings(statusOfPedigrees);
			System.out.println("Pedigrees status list is "+pedigreesStatus+" its size is "+pedigreesStatus.size());
			if(pedigreesStatus.size() == 0){
				return mapping.findForward("false");
			}
			
			for(int i=0; i<pedigreesStatus.size();i++){
				String status =  (String)pedigreesStatus.get(i);
				if(status.equalsIgnoreCase("Certified")){
					certify = true;
					System.out.println("The status of pedigrees inside envelope "+envelopeId+" is Certified");
				}else{
					certify = false;
					authentic = authenticateStatus();
					System.out.println("Authentic value is: "+authentic);
					if(authentic == true){
						
						StringBuffer buff = new StringBuffer();
						buff.append("for $ped in collection ('tig:///ePharma_MD/ReceivedPedigree')"+"/*:pedigreeEnvelope[*:serialNumber = '"+envelopeId+"'] ");
						buff.append("return data($ped/*:pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber)");
						log.info("The query for getting pedigrees is "+buff.toString());
						System.out.println("The query for getting pedigrees is "+buff.toString());
						List pedigrees = queryRunner.returnExecuteQueryStrings(buff.toString());
						System.out.println("The pedigrees list size is "+pedigrees.size());
						
						for(int j=0; j<pedigrees.size();j++){
							String pedigreeId = (String)pedigrees.get(j);
							System.out.println("The pedigreeId is "+pedigreeId);
							String statusOfPedigree = "tlsp:getStatusOfPedigree_MD('"+pedigreeId+"')";
							log.info("The Query for pedigree status is "+statusOfPedigree);
							String pedigreestatus = queryRunner.returnExecuteQueryStringsAsString(statusOfPedigree);
							System.out.println("The  pedigree status is "+pedigreestatus);
							if(pedigreestatus.equalsIgnoreCase("Authenticated") && !(pedigreestatus.equalsIgnoreCase("Certified")) ){
								
								obj.createReceivedPedigree(pedigreeId,sessionID);
								//String receivedPedigreeForPedigrees = "tlsp:CreateReceivedPedigreeForPedigrees('"+pedigreeId+"','"+sessionID+"')";
								//log.info("The query for creating recvd pedigrees is "+receivedPedigreeForPedigrees);
								//List recvdPedigree = queryRunner.returnExecuteQueryStrings(receivedPedigreeForPedigrees);
								//System.out.println("The recvd pedigree created is "+recvdPedigree);
								String changeStatus = "tlsp:InsertAndChangeStatus_MD('"+pedigreeId+"','Certified','"+sessionID+"')";
								log.info("The query for InsertAndChangeStatus of pedigrees is "+changeStatus);
								List updateStatus = queryRunner.returnExecuteQueryStrings(changeStatus);
								System.out.println("The Document in pedigree status is "+updateStatus);
								
							}
						}
					}
				}
			}
		
			
			if(certify == true){
				return mapping.findForward("certified");
				
			}else if(authentic == false)
				return mapping.findForward("false");
			else 
				return mapping.findForward("success");
				
//				List recvdPedigrees = queryRunner.returnExecuteQueryStrings("tlsp:CreateReceivedPedigreeForEnvelopes('"+envelopeId+"','"+sessionID+"')");
//				System.out.println("The recvd pedigrees are "+recvdPedigrees);
//				
//				StringBuffer buff = new StringBuffer();
//				buff.append("for $ped in collection ('tig:///ePharma/ReceivedPedigree')"+"/PedigreeEnvelope[serialNumber = '"+envelopeId+"'] ");
//				buff.append("return data($ped/pedigree/shippedPedigree/documentInfo/serialNumber)");
//				System.out.println("The query for getting pedigrees is "+buff.toString());
//				List pedigrees = queryRunner.returnExecuteQueryStrings(buff.toString());
//				System.out.println("The pedigrees list size is "+pedigrees.size());
				
//				for(int i=0; i<pedigrees.size();i++){
//					String pedigreeId = (String)pedigrees.get(i);
//					
//					List pedigreestatus = queryRunner.returnExecuteQueryStrings("tlsp:InsertAndChangeStatus('"+pedigreeId+"','Certified','"+sessionID+"')");
//					System.out.println("The Document in pedigree status is "+pedigreestatus);	
//					
//				}
			
		}	
		
		}catch(PersistanceException e){    		
			log.error("Error in CertifyReceiptReceivingAction execute method........." +e);
			throw new PersistanceException(e);
		}
		catch(Exception ex){ 
			ex.printStackTrace();
			log.error("Error in CertifyReceiptReceivingAction execute method........." +ex);
			throw new Exception(ex);
			
		}
		return mapping.findForward("success");
	}
	
	public boolean authenticateStatus() throws Exception {
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
			log.error("Error in CertifyReceiptReceivingAction execute method........." +ex);
			//return mapping.findForward("exception");
			throw new Exception(ex);
		}
		
	}
	
}
