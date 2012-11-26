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
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
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
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

public class PedigreeDetailsAction extends Action{
	private static Log log=LogFactory.getLog(PedigreeDetailsAction.class);
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	String servIP = null;
	String clientIP = null;
	
	
	Connection conn; 
	Statement stmt;
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("Coming Inside of the Pedigree Details Action");
		Collection colln= new ArrayList();		
		PedigreeDetailsForm theForm = null; 
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
			String pid = (String)sess.getAttribute("ShipPedId");
			String envid = (String)sess.getAttribute("envId");
			System.out.println("ship id in action : "+pid);
			System.out.println("Pedigree Details"+pedigreeid);
			String pedt="";
			String name = request.getParameter("name");
			String pedType = request.getParameter("pedType");
			System.out.println("PEdigree type in action :" +pedType + "  "+name);
			if(name != null && name.equalsIgnoreCase("pedDetails") && pedType.equalsIgnoreCase("Shipped-Received")){
				pedt="tlsp:ShippedORReceivedPedigreeDetailsRP_MD('ReceivedPedigree','"+pedigreeid+"','"+pid+"')";
			}else if(pedType != null && pedType.equalsIgnoreCase("Shipped-Initial")) 
				pedt="tlsp:InitialPedigreeDetailsRP_MD('ReceivedPedigree','"+pedigreeid+"','"+envid+"')"; 
			else
				pedt="tlsp:ShippedPedigreedetails_MD('"+pedigreeid+"')";
			System.out.println("query in action : "+pedt);
			
			List pedl=queryRunner.returnExecuteQueryStrings(pedt.toString());
			System.out.println("the list of objects"+pedl);
			request.setAttribute("sped",pedl);
			request.setAttribute("PedigreeID",pedigreeid);
			
			for(int i=0; i<pedl.size(); i++){
				
				System.out.println("Pedigree search size :" +pedl.size());
				theForm = new PedigreeDetailsForm();
				Node listNode = XMLUtil.parse((String)pedl.get(i));
				System.out.println("listNode :"+listNode);
				
				   theForm.setPedigreeId(CommonUtil.jspDisplayValue(listNode,"pedigreeId"));
				   System.out.println("Pedigree Id from form bean :" + theForm.getPedigreeId());
				   String pedId = theForm.getPedigreeId();
				   request.setAttribute("pedId",pedId);
				   theForm.setTransactionDate(CommonUtil.jspDisplayValue(listNode,"transactionDate"));
				   theForm.setFromCompany(CommonUtil.jspDisplayValue(listNode,"fromCompany"));
				   theForm.setToCompany(CommonUtil.jspDisplayValue(listNode,"toCompany"));
				   theForm.setTransactionType(CommonUtil.jspDisplayValue(listNode,"transactionType"));
				   theForm.setTransactionNo(CommonUtil.jspDisplayValue(listNode,"transactionNo"));
				   theForm.setDrugName(CommonUtil.jspDisplayValue(listNode,"drugName"));
				   theForm.setProductCode(CommonUtil.jspDisplayValue(listNode,"productCode"));
				   theForm.setCodeType(CommonUtil.jspDisplayValue(listNode,"codeType"));
				   theForm.setManufacturer(CommonUtil.jspDisplayValue(listNode,"manufacturer"));
				   theForm.setQuantity(CommonUtil.jspDisplayValue(listNode,"quantity"));
				   theForm.setDosageForm(CommonUtil.jspDisplayValue(listNode,"dosageForm"));
				   theForm.setStrength(CommonUtil.jspDisplayValue(listNode,"strength"));
				   theForm.setContainerSize(CommonUtil.jspDisplayValue(listNode,"containerSize"));
				   theForm.setCustName(CommonUtil.jspDisplayValue(listNode,"custName"));
				   theForm.setCustAddress(CommonUtil.jspDisplayValue(listNode,"custAddress"));
				   theForm.setCustContact(CommonUtil.jspDisplayValue(listNode,"custContact"));
				   theForm.setCustPhone(CommonUtil.jspDisplayValue(listNode,"custPhone"));
				   theForm.setCustEmail(CommonUtil.jspDisplayValue(listNode,"custEmail"));
				   theForm.setDatesInCustody(CommonUtil.jspDisplayValue(listNode,"datesInCustody"));
				   theForm.setSignatureInfoName(CommonUtil.jspDisplayValue(listNode,"signatureInfoName"));
				   theForm.setSignatureInfoTitle(CommonUtil.jspDisplayValue(listNode,"signatureInfoTitle"));
				   theForm.setSignatureInfoTelephone(CommonUtil.jspDisplayValue(listNode,"signatureInfoTelephone"));
				   theForm.setSignatureInfoEmail(CommonUtil.jspDisplayValue(listNode,"signatureInfoEmail"));
				   theForm.setSignatureInfoUrl(CommonUtil.jspDisplayValue(listNode,"signatureInfoUrl"));
				   theForm.setSignatureInfoDate(CommonUtil.jspDisplayValue(listNode,"signatureInfoDate"));
				   
				   theForm.setFromShipAddress(CommonUtil.jspDisplayValue(listNode,"fromShipAddress"));
				   theForm.setFromBillAddress(CommonUtil.jspDisplayValue(listNode,"fromBillAddress"));
				   theForm.setFromContact(CommonUtil.jspDisplayValue(listNode,"fromContact"));
				   theForm.setFromTitle(CommonUtil.jspDisplayValue(listNode,"fromTitle"));
				   theForm.setFromPhone(CommonUtil.jspDisplayValue(listNode,"fromPhone"));
				   theForm.setFromEmail(CommonUtil.jspDisplayValue(listNode,"fromEmail"));
				   theForm.setFromLicense(CommonUtil.jspDisplayValue(listNode,"fromLicense"));
				   theForm.setToShipAddress(CommonUtil.jspDisplayValue(listNode,"toShipAddress"));
				   theForm.setToBillAddress(CommonUtil.jspDisplayValue(listNode,"toBillAddress"));
				   theForm.setToContact(CommonUtil.jspDisplayValue(listNode,"toContact"));
				   theForm.setToTitle(CommonUtil.jspDisplayValue(listNode,"toTitle"));
				   theForm.setToPhone(CommonUtil.jspDisplayValue(listNode,"toPhone"));
				   theForm.setToEmail(CommonUtil.jspDisplayValue(listNode,"toEmail"));
				   theForm.setToLicense(CommonUtil.jspDisplayValue(listNode,"toLicense"));
				   theForm.setRePack(CommonUtil.jspDisplayValue(listNode,"repackage"));
				   System.out.println("License from form bean :" + theForm.getToLicense());
				   
				   
				colln.add(theForm);
				
			}
			
			String statusOfPedigrees ="tlsp:getAuthPedStatus_MD('"+ pedigreeid+"')";
			String pedigreestatus = queryRunner.returnExecuteQueryStringsAsString(statusOfPedigrees);
			if(pedigreestatus.length()!=0){
			Node statusNode = XMLUtil.parse(pedigreestatus);
			sess.setAttribute("pedstatus", XMLUtil.getValue(statusNode,"status"));
			sess.setAttribute("pedtime", XMLUtil.getValue(statusNode,"time"));
			sess.setAttribute("peddate",XMLUtil.getValue(statusNode,"date"));
			}else{
			sess.setAttribute("pedstatus", "AUTHENTICATION REQUIRED");
			sess.setAttribute("pedtime","N/A");
			sess.setAttribute("peddate","N/A");
			}
			request.setAttribute(Constants.SHIPPEDPEDIGREE_DETAILS,colln);
				
		}catch(Exception ex){
			
			ex.printStackTrace();
    		log.error("Error in OrderSearchAction execute method........." +ex);
    		return mapping.findForward("exception");
		}
		return mapping.findForward("success");
		
		
	}	
}




