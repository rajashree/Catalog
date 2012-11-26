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

 
/*
 * Created on Nov 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.rdta.epharma.epedigree.action;

import java.text.SimpleDateFormat;
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

/**
 * @author vijayalakshmi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SignPedigreeShip extends Action{
	private static Log log=LogFactory.getLog(EpedigreeAction.class);
    private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
    Connection conn; 
	Statement stmt;
	String clientIP = null;
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
			 HttpServletRequest request, HttpServletResponse response)
			 throws Exception {
        //comment to test
    	log.info("****Inside SignPedigreeShip class.........");
    	try{	
			
			HttpSession sess = request.getSession();
			Helper helper = new Helper();
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
	
	    String pedId = request.getParameter("PedigreeId");
        String envId = (String)sess.getAttribute("envId");
        if(envId == null) envId = (String)sess.getAttribute("envId");
       
        sess.setAttribute("APNID",pedId);
      
        String partID = "APN";
        String xQuery = "";
       
        SimpleDateFormat df = new SimpleDateFormat();
    	
    		df.applyPattern("yyyy-MM-dd:hh:mm:ss");
    		
    		df.applyPattern("yyyy-MM-dd");
    		String tmDate = df.format(new java.util.Date());
    		df.applyPattern("hh:mm:ss");
    		String tmTime = df.format(new java.util.Date());
    		String date = tmDate + "T" + tmTime;
    		log.info("DATE:"+date);
    		sess.setAttribute("Date", date);
    	
    		StringBuffer buffer5 = new StringBuffer();
			 if(partID.equalsIgnoreCase("APN")){
				 String acess = "tlsp:validateAccess_MD('"+sessionID+"','2.17','Insert')" ;
				 List  accessList2 = queryRunner.returnExecuteQueryStrings(acess);
				 log.info("this is the Acess Previliges"+accessList2 );
				 String shc= accessList2.get(0).toString();
				 if(shc.equalsIgnoreCase("false")){
						request.setAttribute("statusc","false");
						
				 }else
						request.setAttribute("statusc","true");
				 if(shc.equalsIgnoreCase("true")){
		           // xQuery= " exists(collection('tig:///ePharma/ShippedPedigree')/PedigreeEnvelope[serialNumber='"+envId+"']//*:Signature)";
					 xQuery = "for $i in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope[*:serialNumber = '"+envId+"']/*:pedigree return if(string-length($i/*:Signature/*:SignatureValue/text())>0) then 'true' else 'false'";
		            log.info("Query for checking Sign : "+xQuery);
					List list = (List)queryRunner.returnExecuteQueryStrings(xQuery);
					
	  			  if(list.contains("true")){
	  				    String APNSigStatus ="SignExists";
	  			   		request.setAttribute("APNDate",date );
	  			   	    request.setAttribute("APNSigStatus",APNSigStatus );
	   				    log.info("Signature is Exists");
	  			   }
	  				else{
		            StringBuffer buffer8 = new StringBuffer();
		    		buffer8.append(" tlsp:pedigreeLevelSignature_MD('"+envId+"','"+pedId+"')");
		    		System.out.println("Query for sign shipped pedigree :"+buffer8.toString());
		    		log.info("Query for sign shipped pedigree :"+buffer8.toString());
		    		List result8 = queryRunner.returnExecuteQueryStrings(buffer8.toString());
		    		/*String query1 = "tlsp:InsertAndChangeStatus('"+envId+"','Created Signed','"+sessionID+"') ";
		    	     List res1 = queryRunner.returnExecuteQueryStrings(query1);*/
		    		
		    		String query = "tlsp:InsertAndChangeStatus_MD('"+pedId+"','Created Signed','"+sessionID+"') ";
		    	    List res = queryRunner.returnExecuteQueryStrings(query);
		    		
		    		df.applyPattern("yyyy-MM-dd");
					request.setAttribute("APNDate",date );
		    		String APNSigStatus ="SignCreated";
		    		request.setAttribute("APNSigStatus",APNSigStatus );
					log.info("Signature is  there"+APNSigStatus);
				}
	  			   
		        }
					else
						 return mapping.findForward("false");
				 }
			 }catch(PersistanceException e){
					log.error("Error in SignPedigreeShip execute method........." +e);
					throw new PersistanceException(e);
				}
				catch(Exception ex){			
					ex.printStackTrace();
		    		log.error("Error in SignPedigreeShip execute method........." +ex);
		    		throw new Exception(ex);
				}
        
        return mapping.findForward("success");
    }
}


