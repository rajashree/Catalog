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
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

public class ShippingManagerDetailsAction extends Action{
	private static Log log=LogFactory.getLog(PedigreeDetailsAction.class);
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	String servIP = null;
	String clientIP = null;
	
	
	Connection conn; 
	Statement stmt;
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		log.info("****Inside ShippingManagerDetailsAction class.........");
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
			String pid = (String)sess.getAttribute("ShipPedId");
			String envid = (String)sess.getAttribute("envId");
			System.out.println("ship id in action : "+pid);
			String pedigreeid = request.getParameter("PedigreeId");
			String pedt="";
			
			String name = request.getParameter("name");
			String pedType = request.getParameter("pedType");
			
			if(name != null && name.equalsIgnoreCase("pedDetails") && pedType.equalsIgnoreCase("Shipped-Received")){
				pedt="tlsp:ShippedORReceivedPedigreeDetails_MD('ShippedPedigree','"+pedigreeid+"','"+pid+"')";
			}else if(pedType != null && pedType.equalsIgnoreCase("Shipped-Initial")) 
				pedt="tlsp:InitialPedigreeDetails_MD('ShippedPedigree','"+pedigreeid+"','"+envid+"')"; 
			else
				pedt="tlsp:ShippingPedigreeDetails_MD('"+pedigreeid+"')";
			
			log.info("Query for getting Pedigree Details: "+pedt);
			System.out.println("Query for getting Pedigree Details: "+pedt);
			List pedl=queryRunner.returnExecuteQueryStrings(pedt.toString());
			log.info("the list of objects"+pedl);
			request.setAttribute("sped",pedl);
			request.setAttribute("PedigreeID",pedigreeid);
			String envId = (String)sess.getAttribute("envId");
									
			for(int i=0; i<pedl.size(); i++){
				
				theForm = new PedigreeDetailsForm();
				Node listNode = XMLUtil.parse((String)pedl.get(i));
								
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
				   System.out.println("License from form bean :" + theForm.getRePack());
				   
				   
				colln.add(theForm);
				
			}
			request.setAttribute(Constants.SHIPPEDPEDIGREE_DETAILS,colln);
				
		}catch(PersistanceException e){
			log.error("Error in ShippingManagerDetailsAction execute method........." +e);
			throw new PersistanceException(e);
		}
		catch(Exception ex){			
			ex.printStackTrace();
    		log.error("Error in ShippingManagerDetailsAction execute method........." +ex);
    		throw new Exception(ex);
		}
		return mapping.findForward("success");
		
		
	}	
}




