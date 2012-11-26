/*
 * Created on Dec 29, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

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

import java.io.InputStream;
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
import com.rdta.commons.CommonUtil;
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;


public class ViewReceivingMfrDetailAction extends Action {
	
	private static Log log = LogFactory.getLog(ViewReceivingMfrDetailAction.class);
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	StringBuffer buffer = null;
	StringBuffer buffer1 = null;
	Connection conn; 
	Statement stmt;
	String clientIP = null;
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ShippingMfrDetailForm theform = null;
		Collection colln= new ArrayList();
		try{
			log.info("Inside ViewReceivingMfrDetailAction class");
			HttpSession sess = request.getSession();
			Helper helper = new Helper();
			clientIP = request.getRemoteAddr();		
			
			conn = helper.ConnectTL(); 
			stmt = helper.getStatement(conn);
			log.info("Validating The Session");
			
			//Validating Session
			String sessionID = request.getParameter("sessionID");
			if(sessionID == null){
				sessionID = (String)sess.getAttribute("sessionID");
			}	
			log.info("sessionID in ViewShippingMfrDetailAction :"+sessionID);
			String validateResult = helper.ValidateUserSession(stmt, sessionID, clientIP);        
		
			if ( !validateResult.equals("VALID")){
			    //return a forward to invalid .
			    return mapping.findForward("loginPage");
			}
			log.info("Inside Action ViewReceivingMfrDetailAction....... ");
			
			String pedigreeId = request.getParameter("PedigreeId");
			String pedt="tlsp:ShippedPedigreedetails('"+pedigreeId+"')";
			System.out.println("query for pedigree details in action: "+pedt);
			System.out.println("query for pedigree details in action: "+pedt);
			List result=queryRunner.executeQuery(pedt);
			System.out.println("the list of objects :"+result);
			request.setAttribute("res",result);
			
			
			StringBuffer buff = new StringBuffer();
			buff.append("tlsp:getManufacturerDetails('ReceivedPedigree','"+pedigreeId+"') ");
			System.out.println("Query for Getting Manufacturer details : "+buff.toString());
			log.info("Query for Getting Manufacturer details : "+buff.toString());
			List res = queryRunner.executeQuery(buff.toString());
			
			request.setAttribute("Result",res);
			
			for(int i=0; i<result.size(); i++){
				
				theform = new ShippingMfrDetailForm();
				Node listNode = XMLUtil.parse((InputStream)result.get(i));
				System.out.println("listNode :"+listNode);
				
				 theform.setPedigreeid(CommonUtil.jspDisplayValue(listNode,"pedigreeId"));
				 System.out.println("pedigre id :"+theform.getPedigreeid());
				 theform.setDate(CommonUtil.jspDisplayValue(listNode,"transactionDate"));
				 theform.setSource(CommonUtil.jspDisplayValue(listNode,"source"));
				 theform.setDestination(CommonUtil.jspDisplayValue(listNode,"destination"));
				 theform.setTransactiontype(CommonUtil.jspDisplayValue(listNode,"transactionType"));
				 theform.setTrnsaction(CommonUtil.jspDisplayValue(listNode,"transactionNo"));
				 System.out.println("transaction id :"+theform.getTrnsaction());
				 
				 if(res.size() > 0){
					 Node listnode = XMLUtil.parse((InputStream)res.get(i));
					 theform.setName(CommonUtil.jspDisplayValue(listnode,"Name"));
					 System.out.println("Manufactrer Name : "+theform.getName());
					 theform.setLine1(CommonUtil.jspDisplayValue(listnode,"Address/Line1"));
					 System.out.println("Address : "+theform.getLine1());
					 theform.setLine2(CommonUtil.jspDisplayValue(listnode,"Address/Line2"));
					 theform.setCity(CommonUtil.jspDisplayValue(listnode,"Address/City"));
					 theform.setState(CommonUtil.jspDisplayValue(listnode,"Address/State"));
					 theform.setZip(CommonUtil.jspDisplayValue(listnode,"Address/Zip"));
					 theform.setCountry(CommonUtil.jspDisplayValue(listnode,"Address/Country"));
					 System.out.println("Country : "+theform.getCountry());
					 theform.setContact(CommonUtil.jspDisplayValue(listnode,"Contact"));
					 theform.setEmail(CommonUtil.jspDisplayValue(listnode,"Email"));
					 theform.setPhone(CommonUtil.jspDisplayValue(listnode,"Phone"));
					 theform.setLicense(CommonUtil.jspDisplayValue(listnode,"License"));
				 }
				
				 colln.add(theform);
			}
			request.setAttribute(Constants.SHIPPED_DETAILS,colln);
				
			
			log.info("Before returning to success.........from ViewShippingMfrDetailAction.......");
			
		}catch(PersistanceException e){    		
			log.error("Error in ViewMessageAction execute method........." +e);
			throw new PersistanceException(e);
		}
		catch(Exception ex){ 
			ex.printStackTrace();
			log.error("Error in ViewMessageAction execute method........." +ex);
			throw new Exception(ex);
			
		}		
		return mapping.findForward("success");
	}
}
