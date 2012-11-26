
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
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

public class ShippingManagerAuditTrailAction extends Action{
	
	private static Log log=LogFactory.getLog(ShippingManagerAuditTrailAction.class);
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	String servIP = null;
	String clientIP = null;
	
	
	Connection conn; 
	Statement stmt;
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		log.info("Inside ShippingManagerAuditTrailAction....... ");
		Collection colln= new ArrayList();		
        AuditTrailForm theForm = null; 
        
        String collection="ShippedPedigree";
		try{
			
			HttpSession sess = request.getSession();
			Helper helper = new Helper();
			clientIP = request.getRemoteAddr();		
			
			conn = helper.ConnectTL(); 
			stmt = helper.getStatement(conn);
			log.info("Validating The Session");
			
			//Validating Session
			String sessionID = (String)sess.getAttribute("sessionID");
			log.info("sessionID in Action :"+sessionID);
			String validateResult = helper.ValidateUserSession(stmt, sessionID, clientIP);        
		
			if ( !validateResult.equals("VALID")){
			   // return a forward to invalid .
			    return mapping.findForward("loginPage");
			}
			
			String pedigreeid = request.getParameter("PedigreeId");
			sess.setAttribute("ShipPedId",pedigreeid);
			
			String audit="tlsp:getAuditTrailDetails('"+collection+"','"+pedigreeid+"')";
			log.info("Audit trail query:"+audit);
			List auditlist=queryRunner.returnExecuteQueryStrings(audit);
			String query = "tlsp:getStatusOfPedigree('"+pedigreeid+"')";
			log.info("Query for status : "+query);
			List res = queryRunner.returnExecuteQueryStrings(query);
			log.info("Status of pedigree : "+res);
			request.setAttribute("Status",res);
			request.setAttribute("AUDTL",auditlist);
			int size = auditlist.size();
			String temp="";
			String quantity="";
			String date="";
			String transactionType="";
			String signature="";
			String serialNumber="";
			String pedigreetype="";
			String transactionNo="";
			for ( int k=0;k<size;k++){
				
				Node node = XMLUtil.parse((String) auditlist.get(k));
				String nodename=node.getNodeName();
				System.out.println("*********"+node.getNodeName()+"***********");
				Node initial = XMLUtil.getNode(node,"initialPedigree");
				
				if(initial != null ){
				
				}
				 serialNumber=XMLUtil.getValue(node,"./documentInfo/serialNumber");
				 quantity=XMLUtil.getValue(node,"./itemInfo/quantity");
				 date=XMLUtil.getValue(node,"./transactionInfo/transactionDate");
				 transactionType=XMLUtil.getValue(node,"./transactionInfo/transactionIdentifier/identifierType");
				 signature=XMLUtil.getValue(node,"./signatureInfo/signatureMeaning");
				 transactionNo=XMLUtil.getValue(node,"./transactionInfo/transactionIdentifier/identifier");
				 if(nodename.equalsIgnoreCase("receivedPedigree")){
					 	pedigreetype = "Shipped-Received";
						date=XMLUtil.getValue(node,"./receivingInfo/dateReceived");
						quantity=XMLUtil.getValue(node,".//itemInfo/quantity");
						transactionType=XMLUtil.getValue(node,".//transactionInfo/transactionIdentifier/identifierType");
					}
				 if(nodename.equalsIgnoreCase("shippedPedigree")){
					 
					Node innerNode = XMLUtil.getNode(node,"pedigree");
					System.out.println("innernode 1: "+innerNode);
					if(innerNode == null ){
						 innerNode = XMLUtil.getNode(node,"initialPedigree");
						 System.out.println("innernode 2: "+innerNode);
					}if(innerNode == null ){
						 innerNode = XMLUtil.getNode(node,"repackagedPedigree");
						 System.out.println("innernode 3: "+innerNode);
					}
					
					String inPed = innerNode.getNodeName();
					System.out.println("inped:"+inPed);
					
					if(inPed.equals("pedigree")){
						pedigreetype = "Shipped";
						quantity=XMLUtil.getValue(node,".//itemInfo/quantity");
					}else{
						if(inPed.equals("repackagedPedigree")){
							pedigreetype = "Shipped-repacked";
							quantity=XMLUtil.getValue(node,".//itemInfo/quantity");
						}else{
						pedigreetype="Shipped-Initial";	
						quantity=XMLUtil.getValue(node,".//itemInfo/quantity");
						}
					}
					
					 System.out.println(" NODE NAME :"+innerNode.getNodeName());
				
					}
					if(serialNumber == null)
							serialNumber = temp;
					 
				 theForm =new AuditTrailForm();
					theForm.setPedigreeId(serialNumber);
					theForm.setProductqty(quantity);
					theForm.setSignature(signature);
					theForm.setPedigreetype(pedigreetype);
					theForm.setTrnsactiontype(transactionType);
					theForm.setTrasactiondate(date);
					
				 colln.add(theForm);
				System.out.println("details of:"+serialNumber +" "+quantity +" "+date+" "+transactionType+""+pedigreetype+""+transactionNo);
					temp=serialNumber;
			}
			    
			request.setAttribute(Constants.AUDITTRAIL_DETAILS,colln);
				
		}catch(PersistanceException e){
			log.error("Error in ShippingManagerAuditTrailAction execute method........." +e);
			throw new PersistanceException(e);
		}
		catch(Exception ex){			
			ex.printStackTrace();
    		log.error("Error in ShippingManagerAuditTrailAction execute method........." +ex);
    		throw new Exception(ex);
		}
		return mapping.findForward("success");
		
		
	}	
}
