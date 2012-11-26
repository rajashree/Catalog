
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

public class InvoiceDetailsAction extends Action{
	private static Log log=LogFactory.getLog(InvoiceDetailsAction.class);
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	String servIP = null;
	String clientIP = null;
	Connection conn; 
	Statement stmt;
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		log.info("Coming Inside of the Invoice Details Action");
		Collection colln= new ArrayList();		
		InvoiceDetailsForm theForm = null; 
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
			
			String invoiceId = request.getParameter("trNum");
			System.out.println("invoice Details"+invoiceId);
			String inv="tlsp:InvoiceDetails('"+invoiceId+"')";
			log.info("Query for Getting InvoiceDetails"+inv);
			List invDetails=queryRunner.returnExecuteQueryStrings(inv.toString());
			System.out.println("the list of objects"+invDetails);
			request.setAttribute("InvDetails",invDetails);
			request.setAttribute("InvoiceId",invoiceId);
			
			for(int i=0; i<invDetails.size(); i++){
				
				System.out.println("Invoive search size :" +invDetails.size());
				theForm = new InvoiceDetailsForm();
				Node listNode = XMLUtil.parse((String)invDetails.get(i));
				System.out.println("listNode :"+listNode);
				
				   theForm.setInvoiceNumber(CommonUtil.jspDisplayValue(listNode,"InvoiceNumber"));
				   System.out.println("InvoiceNumber from form bean :" + theForm.getInvoiceNumber());
				   theForm.setSellersID(CommonUtil.jspDisplayValue(listNode,"SellersID"));
				   theForm.setInvoiceDate(CommonUtil.jspDisplayValue(listNode,"InvoiceDate"));
				   theForm.setRequestedDeliveryDate(CommonUtil.jspDisplayValue(listNode,"RequestedDeliveryDate"));
				   theForm.setBuyerPartyName(CommonUtil.jspDisplayValue(listNode,"BuyerPartyName"));
				   theForm.setBuyerAddress(CommonUtil.jspDisplayValue(listNode,"BuyerAddress"));
				   theForm.setBuyerContact(CommonUtil.jspDisplayValue(listNode,"BuyerContact"));
				   theForm.setSellerPartyName(CommonUtil.jspDisplayValue(listNode,"SellerPartyName"));
				   theForm.setSellerAddress(CommonUtil.jspDisplayValue(listNode,"SellerAddress"));
				   theForm.setItemIdentificationNumber(CommonUtil.jspDisplayValue(listNode,"ItemIdentificationNumber"));
				   theForm.setItemDescription(CommonUtil.jspDisplayValue(listNode,"ItemDescription"));
				   theForm.setQuantityOrdered(CommonUtil.jspDisplayValue(listNode,"QuantityOrdered"));
				   theForm.setInvoiceAmount(CommonUtil.jspDisplayValue(listNode,"InvoiceAmount"));
				    			   
				colln.add(theForm);
				
			}
			request.setAttribute(Constants.INVOICE_DETAILS,colln);
				
		}catch(PersistanceException e){    		
			log.error("Error in PedigreeSearchAction execute method........." +e);
			throw new PersistanceException(e);
		}
		catch(Exception ex){ 
			ex.printStackTrace();
			log.error("Error in PedigreeSearchAction execute method........." +ex);
			throw new Exception(ex);
			
		}	
		return mapping.findForward("success");
		
		
	}	
}

