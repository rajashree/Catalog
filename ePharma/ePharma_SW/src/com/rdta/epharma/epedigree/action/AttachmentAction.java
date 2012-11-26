
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
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

public class AttachmentAction extends Action{
	private static Log log=LogFactory.getLog(AttachmentAction.class);
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	String servIP = null;
	String clientIP = null;
	Connection conn; 
	Statement stmt;
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("Coming Inside of the AttachmentAction");
		Collection colln= new ArrayList();		
		AttachmentForm theForm = null; 
		String collection ="ReceivedPedigree";
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
			
		String pedigreeid=request.getParameter("PedigreeId");
		log.info("pedigreeid"+pedigreeid);
		String xQuery="tlsp:getAttachmentDetails('"+collection+"','"+pedigreeid+"')";
		log.info("Audit trail query:"+xQuery);
		List res=queryRunner.returnExecuteQueryStrings(xQuery);
		log.info("Audit trail query:"+res);
		theForm =(AttachmentForm)form;
		log.info(" ClassnName   :"+res.get(0).getClass().getName());
		String str = (String)res.get(0);
		log.info(" String Name "+ str);
		Node listNode = XMLUtil.parse(str);
		String existatt=XMLUtil.getValue(listNode,"true");
		//request.setAttribute("existatt",existatt);
		if(existatt.equalsIgnoreCase("true")){
		String attachmenttype=XMLUtil.getValue(listNode,"mimeType");			
		//request.setAttribute("mimetype",attachmenttype);
		System.out.println("ATTACHMENT  "+attachmenttype);
		}else{
			String attachmenttype="NoAttachmentsExist";
			//request.setAttribute("mimetype",attachmenttype);
			
		}
		
		log.info("existatt  "+existatt);
		String transDateAndTime = CommonUtil.jspDisplayValue(listNode,"Date");
		String result[]=StringUtils.split(transDateAndTime,"T");
		log.info("result: "+result.length);
			String date=result[0];
			request.setAttribute("date",date);
			if(result.length>1){
			String time=result[1];
			request.setAttribute("time",time);
			}else{
				String time="N/A";
				request.setAttribute("time",time);
			}
		log.info("Date is:"+theForm.getTransactionDate());
		theForm.setTransactionType(CommonUtil.jspDisplayValue(listNode,"TransactionType"));
		theForm.setTransactionNo(CommonUtil.jspDisplayValue(listNode,"TransactionNo"));
		theForm.setPedigreeId(CommonUtil.jspDisplayValue(listNode,"PedigreeID"));
		theForm.setMimeType(CommonUtil.jspDisplayValue(listNode,"mimeType"));
		System.out.println("Exists "+CommonUtil.jspDisplayValue(listNode,"true"));
		}catch (PersistanceException e) {
	    	log.error("Error in  ReceivingManagerAttachmentAction  execute mathod ......" + e);
	    	throw new PersistanceException(e);	
		}catch(Exception ex){ 
			ex.printStackTrace();
		    log.error("Error in  ReceivingManagerAttachmentAction execute method........." +ex);
		   throw new Exception(ex);
		}
		return mapping.findForward("success");
		
		
	}	

}
