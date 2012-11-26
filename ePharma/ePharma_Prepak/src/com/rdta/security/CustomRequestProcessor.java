/*
 * Created on Jan 6, 2006
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

package com.rdta.security;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.RequestProcessor;

import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.security.listeners.SessionListener;

/**
 * @author Ajay Reddy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CustomRequestProcessor extends RequestProcessor {
    static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
    
    static final Log log = LogFactory.getLog(CustomRequestProcessor.class);
    
    protected boolean processPreprocess (
            HttpServletRequest request,
            HttpServletResponse response){
        
    	log.info("In processPreprocess method::Requested action::"+request.getContextPath());
    	
        if( request.getServletPath().equals("/doUserLogin.do")) return true;
        response.setBufferSize(100*1024);
        
        HttpSession session = request.getSession(false);
        
        String sessionID =(String) session.getAttribute("sessionID");
        
          
        if(session == null || sessionID == null){
        	try {
				response.sendRedirect("/"+request.getContextPath()+"/dist/error-pages/sessionTimeout.jsp");
				return false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }else{
        	
        	String str = "";
        	try {
				 str= queryRunner.returnExecuteQueryStringsAsString("tlsp:CheckSessionExisting('"+sessionID+"')");
			} catch (PersistanceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(str!=null && !(str.trim().equals(""))){
				updateSession(sessionID);
				return true;
			}else{
				session.invalidate();
				try {
					response.sendRedirect("/"+request.getContextPath()+"/dist/error-pages/sessionTimeout.jsp");
					return false;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
        }
        return true;
    }
    
    
    private void updateSession(String sessionID) {
        // TODO Auto-generated method stub
        SimpleDateFormat df = new SimpleDateFormat();
        df.applyPattern("yyyy-MM-dd");
		String screenEnteredDate = df.format(new java.util.Date());
		df.applyPattern("HH:mm:ss");
		String screenEnteredTime = df.format(new java.util.Date());
		String screenEnteredDT = screenEnteredDate+"T"+screenEnteredTime;
   
        try {
            String query3="tlsp:updateLastUseInLoggin('"+sessionID+"','"+screenEnteredDT+"')";
            queryRunner.executeUpdate(query3);
        } catch (PersistanceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
    }
  

}
