
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
import java.util.HashMap;
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



public class pedigreeEnvelopeAction extends Action{
	
	private static Log log=LogFactory.getLog(pedigreeEnvelopeAction.class);
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	String servIP = null;
	String clientIP = null;
	private static Map map = new HashMap();
	
	Connection conn; 
	Statement stmt;
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Collection colln= new ArrayList();		
		EnvelopeForm theForm = null; 
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
			
			String envelopeid = request.getParameter("envelopeId");
			sess.setAttribute("envId",envelopeid);
		
			String buff1 = "tlsp:ShippingMgnrEnvelopesDisplay('"+envelopeid+"')";
			System.out.println("Query for getting envelope details: "+buff1);
			String enveloped = "tlsp:ShippingMgnrEnvelopeDetails('"+envelopeid+"')";
			System.out.println("Query for getting envelope details: "+enveloped);
			List list1 = queryRunner.returnExecuteQueryStrings(buff1.toString());
			request.setAttribute("envDetails",list1);
			List list2 = queryRunner.returnExecuteQueryStrings(enveloped);
			request.setAttribute("envId",list2);
			colln = new ArrayList();
			theForm =(EnvelopeForm)form;
			Node listNode1 = XMLUtil.parse((String)list2.get(0));
			
			theForm.setEnvelopeID(CommonUtil.jspDisplayValue(listNode1,"EnvelopeID"));
			theForm.setDate(CommonUtil.jspDisplayValue(listNode1,"Date"));
			String transDateAndTime = CommonUtil.jspDisplayValue(listNode1,"Date");
			String result[]=StringUtils.split(transDateAndTime,"T");
				String date=result[0];
				request.setAttribute("date",date);
				if(result.length>1){
				String time=result[1];
				request.setAttribute("time",time);
				}else{
					String time="N/A";
					request.setAttribute("time",time);
				}

			theForm.setSource(CommonUtil.jspDisplayValue(listNode1,"source"));
			theForm.setDestination(CommonUtil.jspDisplayValue(listNode1,"destination"));
			
			for(int i=0; i<list1.size(); i++){
				theForm = new EnvelopeForm();
				Node listNode = XMLUtil.parse((String)list1.get(i));
				theForm.setContainercode(CommonUtil.jspDisplayValue(listNode,"containerCode"));
				theForm.setPedigreeID(CommonUtil.jspDisplayValue(listNode,"pedigreeid"));
				theForm.setQuantity(CommonUtil.jspDisplayValue(listNode,"Quantity"));
				theForm.setLotnumber(CommonUtil.jspDisplayValue(listNode,"LotNum"));
				theForm.setStatus(CommonUtil.jspDisplayValue(listNode,"status"));
				theForm.setTransactiondate(CommonUtil.jspDisplayValue(listNode,"TransactionDate"));
				String trandate=CommonUtil.jspDisplayValue(listNode,"TransactionDate");
				String trdate[]=StringUtils.split(trandate,"T");
				String trnsdate=trdate[0];
				request.setAttribute("trnsDate",trnsdate);
				if(trdate.length>1){
				  String trnstime=trdate[1];
				  request.setAttribute("transTime",trnstime);
				}else{
					String trnstime="N/A";
					request.setAttribute("transTime",trnstime);
				}
				theForm.setDrugname(CommonUtil.jspDisplayValue(listNode,"DrugName"));
				theForm.setDrugcode(CommonUtil.jspDisplayValue(listNode,"DrugCode"));
				theForm.setAttachment(CommonUtil.jspDisplayValue(listNode,"Attachement"));
				theForm.setCount(CommonUtil.jspDisplayValue(listNode,"count"));
				
				colln.add(theForm);
			
			}
			
			request.setAttribute(Constants.ENVELOPE_DETAILS,colln);
			sess.setAttribute("envId",envelopeid);
			
		}catch(Exception ex){
			
			ex.printStackTrace();
    		log.error("Error in OrderSearchAction execute method........." +ex);
    		return mapping.findForward("exception");
		}
		return mapping.findForward("success");
	}
}
