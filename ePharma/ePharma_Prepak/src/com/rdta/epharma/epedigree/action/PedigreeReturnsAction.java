
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
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

public class PedigreeReturnsAction extends Action {
	
	private static Log log=LogFactory.getLog(PedigreeReturnsAction.class);
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	String servIP = null;
	String clientIP = null;
	Connection conn; 
	Statement stmt;
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		String pedigreeId = request.getParameter("PedigreeId");
		PedigreeReturnsForm theform = null;
		Collection colln= new ArrayList();
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
			String query="tlsp:Shippedpedigreedetails('"+pedigreeId+"')";
			System.out.println("query for pedigree details in action: "+query);
			List result=queryRunner.executeQuery(query);
			System.out.println("the list of objects :"+result);
			request.setAttribute("res",result);
			
			StringBuffer buff = new StringBuffer();
			buff.append("tlsp:ReturnsDetails('"+pedigreeId+"')");
			System.out.println("Query for Returns :"+buff.toString());
			List res = queryRunner.executeQuery(buff.toString());
			System.out.println("Returns results: "+res);			
			
				
			for(int i=0; i<result.size(); i++){
					
				theform = new PedigreeReturnsForm();
				Node listNode = XMLUtil.parse((InputStream)result.get(i));
				System.out.println("listNode :"+listNode);
				
				 theform.setPedigreeid(CommonUtil.jspDisplayValue(listNode,"Pedigree"));
				 System.out.println("pedigre id :"+theform.getPedigreeid());
				 theform.setDate(CommonUtil.jspDisplayValue(listNode,"Date"));
				 theform.setSource(CommonUtil.jspDisplayValue(listNode,"source"));
				 theform.setDestination(CommonUtil.jspDisplayValue(listNode,"destination"));
				 theform.setTransactiontype(CommonUtil.jspDisplayValue(listNode,"TrnsactionType"));
				 theform.setTrnsaction(CommonUtil.jspDisplayValue(listNode,"Trnsaction"));
				 System.out.println("transaction id :"+theform.getTrnsaction());
				 
				/*Node listnode = XMLUtil.parse((InputStream)resStatus.get(i));
				System.out.println("List node inside status :"+listnode);
				
				theform.setNDC(CommonUtil.jspDisplayValue(listnode,"a"));*/
				 
				 colln.add(theform);
					
			}
						
			request.setAttribute(Constants.RETURNS_DETAILS,colln);
				
		}catch(Exception ex){
			
			ex.printStackTrace();
    		log.error("Error in PedigreeReturnsAction execute method........." +ex);
    		return mapping.findForward("exception");
		}	
		return mapping.findForward("success");
	}
}
