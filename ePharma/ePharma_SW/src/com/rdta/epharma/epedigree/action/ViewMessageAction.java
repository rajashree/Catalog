
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

public class ViewMessageAction extends Action{
	private static Log log=LogFactory.getLog(ViewMessageAction.class);
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	String servIP = null;
	String clientIP = null;
	Connection conn; 
	Statement stmt;
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		log.info("Inside ViewMessageAction execute Method");
		Collection colln= new ArrayList();		
		ViewMessageForm theForm = null; 
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
			
			String messageId = request.getParameter("MessageID");
			System.out.println("Message Details"+messageId);
			String msg="tlsp:MessageDetails('"+messageId+"')";
			log.info("Query for getting MessageDetails"+msg);
			List msgDetails=queryRunner.returnExecuteQueryStrings(msg);
			System.out.println("the list of objects"+msgDetails);
			request.setAttribute("MsgDetails",msgDetails);
						
			for(int i=0; i<msgDetails.size(); i++){
				
				System.out.println("Invoive search size :" +msgDetails.size());
				theForm = new ViewMessageForm();
				Node listNode = XMLUtil.parse((String)msgDetails.get(i));
				System.out.println("listNode :"+listNode);
				
				theForm.setCreatedBy(CommonUtil.jspDisplayValue(listNode,"CreatedBy"));
				System.out.println("CreatedBy from form bean :" + theForm.getCreatedBy());
				theForm.setMessageTitle(CommonUtil.jspDisplayValue(listNode,"MessageTitle"));
				theForm.setRequiredAction(CommonUtil.jspDisplayValue(listNode,"RequiredAction"));
				theForm.setComments(CommonUtil.jspDisplayValue(listNode,"Comments"));
				theForm.setPriorityLevel(CommonUtil.jspDisplayValue(listNode,"PriorityLevel"));
				theForm.setFlag(CommonUtil.jspDisplayValue(listNode,"Flag"));
				theForm.setDocId(CommonUtil.jspDisplayValue(listNode,"DocId"));
				colln.add(theForm);
				
			}
			request.setAttribute(Constants.MESSAGE_DETAILS,colln);
				
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

