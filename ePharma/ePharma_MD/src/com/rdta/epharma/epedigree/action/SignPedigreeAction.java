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
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

public class SignPedigreeAction extends Action{
	
	private static Log log=LogFactory.getLog(SignPedigreeAction.class);
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	String DocumentId = null;
	String clientIP = null;
	Connection conn; 
	Statement stmt;
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
	 log.info("****Inside SignPedigreeAction class.........");
     DocumentId = request.getParameter("check");
     log.info("The PedigreeId selected is "+DocumentId);
     StringBuffer buffer = new StringBuffer();
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

			 DocumentId = request.getParameter("check");
			 String button = request.getParameter("Submit5");
			 log.info("The button value is:"+button);
			 String trType = (String)sess.getAttribute("trType");
			 log.info("The Transaction type is"+trType);
			 String RefNum = (String)sess.getAttribute("RefNum");
			 String buttonvalue1 = (String)sess.getAttribute("buttonvalue");
			 log.info("the reference no is:"+RefNum);
			 List accessList = queryRunner.returnExecuteQueryStrings("tlsp:validateAccess_MD('"+sessionID+"','2.17','Insert')");
				String insertStatus = accessList.get(0).toString();
				log.info("The insertStatus is : "+insertStatus);
				if(insertStatus.equalsIgnoreCase("false")){
					request.setAttribute("status","true");
					return mapping.findForward("failure");
				}
				
							
		//String xQuery= " exists(collection('tig:///ePharma/ShippedPedigree')/PedigreeEnvelope[serialNumber='"+DocumentId+"']/*:Signature) ";
        String xQuery = "for $i in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope[*:serialNumber = '"+DocumentId+"']/*:pedigree return if(string-length($i/*:Signature/*:SignatureValue/text())>0) then 'true' else 'false'";
		System.out.println("Query for checking signature :"+xQuery);
		List list = (List)queryRunner.returnExecuteQueryStrings(xQuery);
		log.info("The Signature"+list);
		if(list.contains("false"))  {
     buffer.append("tlsp:pedigreeSignature_MD('"+DocumentId+"')");
     log.info("Query for creating Signature: "+buffer.toString());
     List res = queryRunner.returnExecuteQueryStrings(buffer.toString());
     //Newly added code
     
     StringBuffer buff = new StringBuffer();
     buff.append("for $i in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope ");
     buff.append("where $i/*:serialNumber = '"+DocumentId+"' ");
     buff.append("return data($i/*:pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber ) ");
     List result = queryRunner.returnExecuteQueryStrings(buff.toString());
     for(int i=0;i<result.size();i++){
    	 String SPId = result.get(i).toString();
    	 log.info("REsult for SP :"+SPId);
    	 log.info("result after inserting status :"+queryRunner.returnExecuteQueryStrings("tlsp:InsertAndChangeStatus_MD('"+SPId+"','Created Signed','"+sessionID+"') "));
     }
     request.setAttribute("Sig","false");
     return mapping.findForward("true");
	 }
		else  if(list.contains("true")){
			   request.setAttribute("Sig","true");
			   System.out.println("signature"+list);
			   return mapping.findForward("true");
			   
		   }else
			   return mapping.findForward("false");
		}
	 	catch(PersistanceException e){
			log.error("Error in SignPedigreeAction execute method........." +e);
			throw new PersistanceException(e);
		}
		catch(Exception ex){			
			ex.printStackTrace();
			log.error("Error in SignPedigreeAction execute method........." +ex);
			throw new Exception(ex);
		}
	 
	 
	}
}
