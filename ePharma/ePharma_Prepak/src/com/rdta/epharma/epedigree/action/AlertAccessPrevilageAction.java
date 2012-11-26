
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.w3c.dom.Node;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import com.rdta.Admin.Utility.Helper;
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

public class AlertAccessPrevilageAction extends Action{
	
	private static Log log = LogFactory.getLog(AlertAccessPrevilageAction.class);
	private static final QueryRunner queryrunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	Connection conn; 
	Statement stmt;
	String clientIP = null;
	Helper helper = new Helper();
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
	
		log.info("********Inside AlertAccessPrevilegeAction************");
		try{	
			
			HttpSession sess = request.getSession();
			clientIP = request.getRemoteAddr();		
			conn = helper.ConnectTL(); 
			stmt = helper.getStatement(conn);
			log.info("Validating The Session");
			String envId = request.getParameter("envelopeId");
			
			String sid = request.getParameter("sessionID");
			
			//Validating Session
			String sessionID = (String)sess.getAttribute("sessionID");
			log.info("sessionID in AlertAccessAction class :"+sessionID);
			String validateResult = helper.ValidateUserSession(stmt, sessionID, clientIP);        
		
			if ( !validateResult.equals("VALID")){
			    //return a forward to invalid .
			    return mapping.findForward("loginPage");
			}
		
		List accessList = queryrunner.returnExecuteQueryStrings("tlsp:validateAccess('"+sessionID+"','2.05','Insert')");
		String insertStatus = accessList.get(0).toString();
		System.out.println("The insertStatus is "+insertStatus);
		if(insertStatus.equals("true")){
			StringBuffer buffer = new StringBuffer();
			buffer.append("tlsp:GetUserInfo('"+sessionID +"')");
			log.info("Query for getting user Info : "+buffer.toString());
			List res = queryrunner.returnExecuteQueryStrings(buffer.toString());
			if(res != null)
				log.info("Group ID : "+res.get(0).toString());
			request.setAttribute("res",res);
			for(int i=0;i<res.size();i++){
				
			}
		return mapping.findForward("success");
		}else{
			request.setAttribute("status","false");
			return mapping.findForward("failure");
		}
		}catch(PersistanceException e){
			log.error("Error in AlertAccessPrevilegeAction execute method........." +e);
			throw new PersistanceException(e);
		}
		catch(Exception ex){			
			ex.printStackTrace();
    		log.error("Error in AlertAccessPrevilegeAction execute method........." +ex);
    		throw new Exception(ex);
		}finally{
			//close the connection
	    	helper.CloseConnectionTL(conn);
		}
	}
	

}
