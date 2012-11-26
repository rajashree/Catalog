
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

public class ReceivedPedigreeAction extends Action{
	
	private static Log log=LogFactory.getLog(ReceivedPedigreeAction.class);
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	String servIP = null;
	String clientIP = null;
	
	
	Connection conn; 
	Statement stmt;
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("Coming Inside of the ReceivedPedigree Details Action");
		Collection colln= new ArrayList();		
		ReceivedPedigreeForm theForm = null; 
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
			
			String pedigreeid = request.getParameter("PedigreeId");
			System.out.println("Pedigree Id : "+pedigreeid);
			String pedt="tlsp:ReceivedPedigreeDetails('"+pedigreeid+"')";
			List recped=queryRunner.returnExecuteQueryStrings(pedt.toString());
			System.out.println("the list of objects"+recped);
			request.setAttribute("receiveped",recped);
			request.setAttribute("PedigreeID",pedigreeid);
			
			for(int i=0; i<recped.size(); i++){
				
				System.out.println("Received Pedigree list size :" +recped.size());
				theForm = new ReceivedPedigreeForm();
				Node listNode = XMLUtil.parse((String)recped.get(i));
				System.out.println("listNode :"+listNode);
				
				theForm.setPedigreeid(CommonUtil.jspDisplayValue(listNode,"pedigreeid"));
				System.out.println("pedigree is from FORM bean is : "+theForm.getPedigreeid());
				theForm.setDaterecvd(CommonUtil.jspDisplayValue(listNode,"daterecvd"));
				theForm.setLotnum(CommonUtil.jspDisplayValue(listNode,"lotnum"));
				theForm.setQuantity(CommonUtil.jspDisplayValue(listNode,"quantity"));
				System.out.println("Quantity from FORM bean is : "+theForm.getQuantity());
				theForm.setExpirationdate(CommonUtil.jspDisplayValue(listNode,"expirationdate"));
				theForm.setItemserialnumber(CommonUtil.jspDisplayValue(listNode,"itemserialnumber"));
				theForm.setSigname(CommonUtil.jspDisplayValue(listNode,"signame"));
				theForm.setTitle(CommonUtil.jspDisplayValue(listNode,"title"));
				theForm.setTelephone(CommonUtil.jspDisplayValue(listNode,"telephone"));
				theForm.setSigemail(CommonUtil.jspDisplayValue(listNode,"sigemail"));
				theForm.setUrl(CommonUtil.jspDisplayValue(listNode,"url"));
				theForm.setSignaturedate(CommonUtil.jspDisplayValue(listNode,"signaturedate"));
				   
				colln.add(theForm);
				
			}
			
			
			request.setAttribute(Constants.RECEIVEDPEDIGREE_DETAILS,colln);
				
		}catch(Exception ex){
			
			ex.printStackTrace();
    		log.error("Error in OrderSearchAction execute method........." +ex);
    		return mapping.findForward("exception");
		}
		return mapping.findForward("success");
		
		
	}	

}
