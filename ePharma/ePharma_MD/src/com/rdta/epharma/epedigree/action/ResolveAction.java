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

import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.rdta.Admin.Utility.Helper;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

public class ResolveAction extends Action{
	
	private static Log logger = LogFactory.getLog(ResolveAction.class);
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	Connection conn; 
	Statement stmt;
	String clientIP = null;
	Helper helper = new Helper();
	
	 public ActionForward execute(ActionMapping mapping, ActionForm form,
			 HttpServletRequest request, HttpServletResponse response)
			 throws Exception {
		 
		 logger.info("******Inside ResolveAction class...........");
		 
		 try{	
			
			HttpSession sess = request.getSession();
			clientIP = request.getRemoteAddr();		
			
			conn = helper.ConnectTL(); 
			stmt = helper.getStatement(conn);
			logger.info("Validating The Session");
			
			//Validating Session
			String sessionID = (String)sess.getAttribute("sessionID");
			logger.info("sessionID in GenerateAlertAction :"+sessionID);
			String validateResult = helper.ValidateUserSession(stmt, sessionID, clientIP);        
		
			if ( !validateResult.equals("VALID")){
			    //return a forward to invalid .
			    return mapping.findForward("loginPage");
			}
		
			
		 
		 String MessageID[] = request.getParameterValues("check");
		
		 String buttonname=request.getParameter("submit");//name of submit button
		 
		 logger.info("button name*********"+buttonname);
		 logger.info("message id:"+MessageID);
		 
	
			 if(buttonname.equalsIgnoreCase("Change Status")){
				 logger.info("*******Inside ChangeStatus.....");
				 List accessList = queryRunner.returnExecuteQueryStrings("tlsp:validateAccess_MD('"+sessionID+"','2.20','Update')");
					String updateStatus = accessList.get(0).toString();
					logger.info("The deleteStatus is : "+updateStatus);
					if(updateStatus.equalsIgnoreCase("false")){
						request.setAttribute("updateStatus","false");
						return mapping.findForward("failure");
					}else{
						
				
				for(int i=0; i<MessageID.length;i++){		
				 String Status = request.getParameter("StatusList"+MessageID[i]);
				 System.out.println("Status in action class : "+Status);
				 logger.info("status value:"+Status);
				 String query = "replace doc( for $i in collection('tig:///ePharma_MD/Alerts') ";
			 		query = query + "where $i/AlertMessage/MessageID = '"+MessageID[i]+"' ";
			 		query = query + "return document-uri($i))//AlertMessage/Status with <Status>"+Status+"</Status>";
			 logger.info("Query for updating status: "+query);
			 List result = queryRunner.executeQuery(query);
			 }
					}
				
			 }
			
			 if(buttonname.equalsIgnoreCase("Resolve")){
				 List accessList = queryRunner.returnExecuteQueryStrings("tlsp:validateAccess_MD('"+sessionID+"','2.19','Delete')");
					String deleteStatus = accessList.get(0).toString();
					logger.info("The deleteStatus is : "+deleteStatus);
					if(deleteStatus.equalsIgnoreCase("false")){
						request.setAttribute("deleteStatus","false");
						return mapping.findForward("failure");
					}else{
				 
				 for(int i=0;i<MessageID.length;i++){
				 String xQuery = "tlsp:DeleteMessage_MD('"+MessageID[i]+"')";
				 logger.info("Query in Resolve action: "+xQuery);
				 List list = queryRunner.executeQuery(xQuery);
				 }
					}
			 }

				
		 
		 }catch(Exception e){
			e.printStackTrace();
			logger.error("Error in ResolveAction execute() method.........."+e);	
			return mapping.findForward("exception");
			}finally{
				//close the connection
		    	helper.CloseConnectionTL(conn);
			}
					
		 return mapping.findForward("success");
	 }
}
