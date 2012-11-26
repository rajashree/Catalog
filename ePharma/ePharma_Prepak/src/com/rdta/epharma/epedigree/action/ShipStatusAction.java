
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




import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import test.Helper;


import java.text.SimpleDateFormat;
import java.util.List;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;
/**
 *
 *
 *
 */
public class ShipStatusAction extends Action
{
	private static Log logger = LogFactory.getLog(ShipStatusAction.class);
	private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	Connection Conn; 
	Statement stmt;
	String clientIP = null;
	
    public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response)
								 throws Exception {
    	logger.info("***Inside ShipStatusAction class.......");
    	
    	try{	
			
			HttpSession sess = request.getSession();
			Helper helper = new Helper();
			clientIP = request.getRemoteAddr();		
			
			Conn = helper.ConnectTL(); 
			stmt = helper.getStatement(Conn);
			logger.info("Validating The Session");
			
			//Validating Session
			String sessionID = (String)sess.getAttribute("sessionID");
			logger.info("sessionID in GenerateAlertAction :"+sessionID);
			String validateResult = helper.ValidateUserSession(stmt, sessionID, clientIP);        
		
			if ( !validateResult.equals("VALID")){
			    //return a forward to invalid .
			    return mapping.findForward("loginPage");
			} 
			
    	ShipStatusFormBean formbean=(ShipStatusFormBean)form;
    	
    	
    	SimpleDateFormat df = new SimpleDateFormat();
    	
    		df.applyPattern("yyyy-MM-dd");
    		String tmDate = df.format(new java.util.Date());
    		df.applyPattern("hh:mm:ss");
    		String tmTime = df.format(new java.util.Date());
    		logger.info("Time:"+tmTime);
		    String Shipstatus1=(String)formbean.getShipstatus1();
		    String APNID=(String)formbean.getAPNID();
		    APNID=request.getParameter("pedid");
		    Shipstatus1=request.getParameter("Shipstatus1");
		    logger.info("WE ARE know HERE"+Shipstatus1);
		
		
		String xQuery="replace doc(for $b in collection('tig:///ePharma/PedigreeStatus') ";
		
		xQuery = xQuery +"return document-uri($b))//PedigreeStatus with <PedigreeStatus><PedigreeDocID>"+APNID;
		xQuery = xQuery +"</PedigreeDocID><StatusType>"+Shipstatus1+"</StatusType><StatusDescription></StatusDescription><CreatedBy>System</CreatedBy><CreatedOn>{current-dateTime()}</CreatedOn></PedigreeStatus>";
						
		List list1= queryRunner.executeQuery(xQuery);
    	}catch(Exception ex){
	  		ex.printStackTrace();
	  		logger.error("Error inside ProductList Class........."+ex);
	  		return mapping.findForward("exception");
	  	}
				
		return mapping.findForward("success");
	}

    
	

}