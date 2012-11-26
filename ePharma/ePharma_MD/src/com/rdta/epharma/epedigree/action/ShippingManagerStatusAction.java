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
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

public class ShippingManagerStatusAction extends Action{
	
	private static Log log=LogFactory.getLog(ShippingManagerStatusAction.class);
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	String servIP = null;
	String clientIP = null;
	Connection conn; 
	Statement stmt;
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		log.info("****Inside ShippingManagerStatusAction class.........");
		String pedigreeId = request.getParameter("PedigreeId");
		PedigreeStatusForm theform = null;
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
			String pedt="tlsp:ShippingManagerPedigreeDetails_MD('"+pedigreeId+"')";
			log.info("query for pedigree details in action: "+pedt);
			List result=queryRunner.executeQuery(pedt);
			log.info("the list of objects :"+result);
			request.setAttribute("res",result);
			
			StringBuffer buffer = new StringBuffer();
			buffer.append("tlsp:GetStatusDetails_MD('"+pedigreeId+"')");
			/*buffer.append("for $ps in collection('tig:///ePharma/PedigreeStatus')/PedigreeStatus[PedigreeID = '"+pedigreeId+"'] ");
			buffer.append("for $status in $ps/Status  ");
			buffer.append(" where $status/StatusChangedOn  = $ps/TimeStamp[1] ");
			buffer.append("return $status ");*/
			log.info("Query for getting latest status: "+buffer.toString());
			List resStatus = queryRunner.executeQuery(buffer.toString());
			log.info("Query for getting latest status: "+resStatus);
			request.setAttribute("StatusRes",resStatus);
			
			for(int i=0; i<result.size(); i++){
					
				theform = new PedigreeStatusForm();
				Node listNode = XMLUtil.parse((InputStream)result.get(i));
				System.out.println("listNode :"+listNode);
				
				 theform.setPedigreeid(CommonUtil.jspDisplayValue(listNode,"pedigreeId"));
				 log.info("pedigre id :"+theform.getPedigreeid());
				 theform.setDate(CommonUtil.jspDisplayValue(listNode,"transactionDate"));
				 theform.setSource(CommonUtil.jspDisplayValue(listNode,"source"));
				 theform.setDestination(CommonUtil.jspDisplayValue(listNode,"destination"));
				 theform.setTransactiontype(CommonUtil.jspDisplayValue(listNode,"transactionType"));
				 theform.setTrnsaction(CommonUtil.jspDisplayValue(listNode,"transactionNo"));
				 log.info("transaction id :"+theform.getTrnsaction());
				if(resStatus.size() > 0){
					Node listnode = XMLUtil.parse((InputStream)resStatus.get(i));
					theform.setStatusDate(CommonUtil.jspDisplayValue(listnode,"StatusChangedOn"));
					theform.setStatusValue(CommonUtil.jspDisplayValue(listnode,"StatusChangedTo"));
					theform.setUserId(CommonUtil.jspDisplayValue(listnode,"UserId"));
				
			}
				 colln.add(theform);
					
			}
			request.setAttribute(Constants.SHIPPED_DETAILS,colln);
				
		}catch(PersistanceException e){
			log.error("Error in ShippingManagerStatusAction execute method........." +e);
			throw new PersistanceException(e);
		}
		catch(Exception ex){			
			ex.printStackTrace();
    		log.error("Error in ShippingManagerStatusAction execute method........." +ex);
    		throw new Exception(ex);
		}
		return mapping.findForward("success");
	}

}
