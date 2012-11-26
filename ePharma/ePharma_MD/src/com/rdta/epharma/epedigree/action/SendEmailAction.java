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

import java.io.File;
import java.io.FileOutputStream;
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

import com.rdta.Admin.Utility.Helper;
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.eag.epharma.util.SmtpClientUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

public class SendEmailAction extends Action {
	
	private static Log log=LogFactory.getLog(SendEmailAction.class);
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	String DocumentId = null;
	String clientIP = null;
	Connection conn; 
	Statement stmt;
	Helper helper = new Helper();
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
	 
	log.info("****Inside SendEmailAction class.........");
	
	try{ 
	HttpSession sess = request.getSession();
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
			List accessList = queryRunner.returnExecuteQueryStrings("tlsp:validateAccess_MD('"+sessionID+"','2.16','Insert')");
			String insertStatus = accessList.get(0).toString();
			log.info("The insertStatus is : "+insertStatus);
			if(insertStatus.equalsIgnoreCase("false")){
				request.setAttribute("status","true");
				return mapping.findForward("failure");
			}
	
	
	 DocumentId = request.getParameter("check");
	 System.out.println("Pedigree id in sendemailaction : "+DocumentId);
	 String toPass ="";
	 String trType = (String)sess.getAttribute("trType");
	 String RefNum = (String)sess.getAttribute("RefNum");
	
	 
	//Here we have to provide username and password - Should have a layer of abstraction to get email and password
	 String uName = "testepharma@sourcen.com";
	 String pwd = "sniplpass";
	 
     log.info("The pedigreeId selected is "+DocumentId);
	 StringBuffer buffer = new StringBuffer();
	//For Checking Signature exists or not
	 String xQuery= " exists(collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope[*:serialNumber='"+DocumentId+"']/*:*/*:Signature) ";
		log.info("Query for checking signature :"+xQuery);
		
		List list = (List)queryRunner.returnExecuteQueryStrings(xQuery);
		System.out.println("signature status : "+list);
		if(list.contains("false")){ 
			request.setAttribute("Sig","nosign");
			return mapping.findForward("signexists");
		}
	 buffer.append("tlsp:TPEmailID_MD('"+DocumentId+"')");
	 log.info("The query in SendEmailAction is :"+buffer.toString());
	 System.out.println("The query in SendEmailAction is :"+buffer.toString());	 	 
	 List result = queryRunner.returnExecuteQueryStrings(buffer.toString());
	 if (result.size()!=0){
	     toPass = (String)result.get(0).toString();
	     log.info("The email is "+toPass);
	     System.out.println("email id: "+toPass);
	       
	     StringBuffer buffer1 = new StringBuffer();
         
	     buffer1.append("tlsp:SendPedigree_MD('"+DocumentId+"','"+toPass+"','"+uName+"', '"+pwd+"')"); 
	     log.info("stored procedure in emailapn: "+buffer1.toString());
	     System.out.println("stored procedure in emailapn: "+buffer1.toString());        
	     List results = queryRunner.returnExecuteQueryStrings(buffer1.toString());
	     System.out.println("result after sending mail : "+results);
	     String s = "";
	     if(results != null)
	      s = (String)results.get(0).toString();
	     log.info("after stored procedure : "+s);
	     
	     StringBuffer buff = new StringBuffer();
	     buff.append("for $i in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope ");
	     buff.append("where $i/*:serialNumber = '"+DocumentId+"' ");
	     buff.append("return data($i/*:pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber ) ");
	     log.info("Query : "+buff.toString());
	     List result1 = queryRunner.returnExecuteQueryStrings(buff.toString());
		 sess.setAttribute("mailid",toPass);
		 if(s.equalsIgnoreCase("Success")){
			 
		     for(int i=0;i<result1.size();i++){
		    	 String SPId = result1.get(i).toString();
		    	 System.out.println("Result after inserting status :"+queryRunner.returnExecuteQueryStrings("tlsp:InsertAndChangeStatus_MD('"+SPId+"','Sent','"+sessionID+"') "));
		     }
			/* String query = "tlsp:InsertAndChangeStatus('"+DocumentId+"','Sent','"+sessionID+"') ";
			 List res = queryRunner.returnExecuteQueryStrings(query);*/
			 return mapping.findForward("true");
		 }else { 
			 for(int i=0;i<result1.size();i++){
		    	 String SPId = result1.get(i).toString();
		    	 System.out.println("Result for SP :"+SPId);
		    	 System.out.println("Result after inserting status :"+queryRunner.returnExecuteQueryStrings("tlsp:InsertAndChangeStatus_MD('"+SPId+"','Sent Problem','"+sessionID+"') "));
		     }
			/* String query = "tlsp:InsertAndChangeStatus('"+DocumentId+"','Sent Problem','"+sessionID+"') ";
			 List res = queryRunner.returnExecuteQueryStrings(query);*/
			 return mapping.findForward("false");
		 }
		 
	 }
	 else {
		 return mapping.findForward("false");
	 }
	 }catch(PersistanceException e){
			log.error("Error in SendEmailAction execute method........." +e);
			throw new PersistanceException(e);
		}
		catch(Exception ex){			
			ex.printStackTrace();
    		log.error("Error in SendEmailAction execute method........." +ex);
    		throw new Exception(ex);
		}
	 finally{
			//close the connection
	    	helper.CloseConnectionTL(conn);
		}
	}

}
