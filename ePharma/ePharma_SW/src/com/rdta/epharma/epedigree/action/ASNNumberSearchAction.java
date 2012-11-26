
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

import com.rdta.Admin.Utility.Helper;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.commons.CommonUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

import org.w3c.dom.Node;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ASNNumberSearchAction extends Action{
	
	private static Log logger=LogFactory.getLog(ASNNumberSearchAction.class);
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	String OrderNum = null;
	String ASNNum = null;
	Connection conn; 
	Statement stmt;
	String servIP = null;
	String clientIP = null;
	String sessionID = null;
	
		
	 public ActionForward execute(ActionMapping mapping, ActionForm form,
			 HttpServletRequest request, HttpServletResponse response)
			 throws Exception {
		 
		 logger.info("Inside ASNNumberSearch Action.........");
		 logger.info("Inside ASNNumberSearch Action execute method.........");
	
	try{ 
	
		HttpSession sess = request.getSession();
		Helper helper = new Helper();
		clientIP = request.getRemoteAddr();		
			
		conn = helper.ConnectTL(); 
		stmt = helper.getStatement(conn);
		logger.info("Validating The Session");
//		Validating Session
		sessionID = (String)sess.getAttribute("sessionID");
		logger.info("sessionID in AsNNumberSearchAction :"+sessionID);
		String validateResult = helper.ValidateUserSession(stmt, sessionID, clientIP);        
	
		if ( !validateResult.equals("VALID")){
		    //return a forward to invalid .
		    return mapping.findForward("loginPage");
		}
			
		 
		 if (ASNNum == null) { 
			 ASNNum ="" ;
		 }
		 
		 ASNNum = request.getParameter("ASNNum").trim();
		 logger.info("ASN ref number in ASNNumberSearchAction : "+ASNNum);
		 //String OrderNum = formbean.getOrderNum();
		 
		 if (ASNNum != ""){
				StringBuffer buff =new StringBuffer();
				
				buff = buff.append("tlsp:BuyersID('"+ASNNum+"') ");
								
				List list1 = queryRunner.returnExecuteQueryStrings(buff.toString());
				if(list1.size()!=0){
				OrderNum = (String)list1.get(0).toString();
				}
				
				logger.info("ordernumber inside ASNNumberSearchAction : "+OrderNum);
				}
		 
		 request.setAttribute("trNum",OrderNum);
	}catch(Exception ex){
		ex.printStackTrace();
		logger.error("Error in ASNNumberSearchAction execute method......." +ex);
	}

		 return mapping.findForward("success");
	 }
}
