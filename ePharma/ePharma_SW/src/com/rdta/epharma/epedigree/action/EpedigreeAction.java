
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
import java.text.SimpleDateFormat;
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

public class EpedigreeAction extends Action{
	private static Log log=LogFactory.getLog(EpedigreeAction.class);
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	Connection conn; 
	Statement stmt;
	String clientIP = null;
	Helper helper = new Helper();
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		log.info("********Inside Action EpedigreeAction....... ");
		log.info("Inside Action execute of EpedigreeAction....");
		String xQuery = "";
		Collection colln= new ArrayList();
			
		try{	
			
			HttpSession sess = request.getSession();
			clientIP = request.getRemoteAddr();		
			conn = helper.ConnectTL(); 
			stmt = helper.getStatement(conn);
			log.info("Validating The Session");
			
			//Validating Session
			String sessionID = (String)sess.getAttribute("sessionID");
			log.info("sessionID in EpedigreeAction :"+sessionID);
			String validateResult = helper.ValidateUserSession(stmt, sessionID, clientIP);        
		
			if ( !validateResult.equals("VALID")){
			    //return a forward to invalid .
			    return mapping.findForward("loginPage");
			}
						
			List accessList = queryRunner.returnExecuteQueryStrings("tlsp:validateAccess('"+sessionID+"','2.0','Read')");
			String readStatus = accessList.get(0).toString();
			log.info("The readStatus is : "+readStatus);
			if(readStatus.equalsIgnoreCase("false")){
				request.setAttribute("status","false");
				return mapping.findForward("failure");
			}else request.setAttribute("status","true");
			
		xQuery = "tlsp:DisplayMessage('"+ sessionID+"') ";
		log.info("My Query in EpedigreeAction class....."+xQuery);
		System.out.println("****Query for display message:  "+xQuery);
		
		List list = queryRunner.executeQuery(xQuery.toString());
		request.setAttribute("List", list);
		
		for(int i=0; i<list.size(); i++){

			EpedigreeForm theForm = new  EpedigreeForm();
			Node listNode = XMLUtil.parse((InputStream)list.get(i));

			theForm.setMessageTitle(CommonUtil.jspDisplayValue(listNode,"MessageTitle"));
			log.info("MessageTitle is:"+theForm.getMessageTitle());
			theForm.setCreatedBy(CommonUtil.jspDisplayValue(listNode,"CreatedBy"));
			log.info("CreatedBy is:"+theForm.getCreatedBy());
			theForm.setCreatedDate(CommonUtil.jspDisplayValue(listNode,"CreatedDate"));
			log.info("CreatedDate is:"+theForm.getCreatedDate());
			theForm.setMessageID(CommonUtil.jspDisplayValue(listNode,"MessageID"));
			log.info("MessageID is:"+theForm.getMessageID());
			theForm.setRelatedProcess(CommonUtil.jspDisplayValue(listNode,"RelatedProcess"));
			log.info("RelatedProcess is:"+theForm.getRelatedProcess());
			theForm.setSeverityLevel(CommonUtil.jspDisplayValue(listNode,"SeverityLevel"));
			log.info("SeverityLevel is:"+theForm.getSeverityLevel());
			theForm.setStatus(CommonUtil.jspDisplayValue(listNode,"Status"));
			log.info("status is:"+theForm.getStatus());
							
			colln.add(theForm);
		}
		
		log.info("Before returning to success");
		request.setAttribute(Constants.ALERT_MSG_DETAILS,colln);
		
		}catch(PersistanceException e){
			log.error("Error in EpedigreeAction execute method........." +e);
			throw new PersistanceException(e);
		}
		catch(Exception ex){			
			ex.printStackTrace();
    		log.error("Error in EpedigreeAction execute method........." +ex);
    		throw new Exception(ex);
		}finally{
			//close the connection
	    	helper.CloseConnectionTL(conn);
		}
		
		return mapping.findForward("success");
	}

}
