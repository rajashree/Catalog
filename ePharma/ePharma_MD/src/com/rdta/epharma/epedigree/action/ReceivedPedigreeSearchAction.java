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
*
* TODO To change the template for this generated file go to
* Window - Preferences - Java - Code Style - Code Templates
*/
package com.rdta.epharma.epedigree.action;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

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


public class ReceivedPedigreeSearchAction extends Action {
	
	private static Log log=LogFactory.getLog(ReceivedPedigreeSearchAction.class);
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	String servIP = null;
	String clientIP = null;
	String fromDT = null;
	String toDT = null;
	String lotNum = null;
	String prodNDC = null;
	String trNum = null;
	String sessionID = null;
	String tp_company_nm = null;
	String SSCC = null;
	String apndocId = null;
	String tpName = null;
	String screenEnteredDate = null;
	String transactionType = null;
	String status = null;
	Connection conn; 
	Statement stmt;
	Helper helper = new Helper();
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("Inside Action ReceivedPedigreeSearchAction....... ");
		log.info("Inside Action execute of ReceivedPedigreeSearchAction....");
		
				
		Vector eltNames = new Vector();
		Vector eltValues = new Vector();
		Collection colln= new ArrayList();		
		ReceivedPedigreeSearchFormBean theForm = null; 
		servIP = request.getServerName().trim();
		clientIP = request.getRemoteAddr().trim();
		fromDT = request.getParameter("fromDT").trim();
		toDT = request.getParameter("toDT").trim();
		lotNum = request.getParameter("lotNum").trim();
		prodNDC = request.getParameter("prodNDC").trim();
		trNum = request.getParameter("trNum").trim();
		tp_company_nm = request.getParameter("tp_company_nm").trim();
		SSCC = request.getParameter("SSCC").trim();
		apndocId = request.getParameter("apndocId").trim();
		tpName = request.getParameter("trdPrtnr").trim();
		status = request.getParameter("status").trim();
		String buttonvalue =request.getParameter("Submit1").trim();
		request.setAttribute("buttonValue",buttonvalue);
		
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
			    return mapping.findForward("loginPage");
			}
			
		if(trNum == null) { trNum = "";}
		if(lotNum == null) { lotNum = "";}
		if(prodNDC == null) { prodNDC = "";}
		if(SSCC == null) { SSCC = "";}
		if(fromDT == null) { fromDT = "";}
		if(toDT == null) { toDT = "";}
		if(tp_company_nm == null) { tp_company_nm = "";}
		if(apndocId == null) { apndocId = "";}
		if(tpName == null) { tpName = "";}
		if(transactionType == null) { transactionType = ""; }
		if(status.equalsIgnoreCase("Select....")){ status = "";}
		
		SimpleDateFormat df = new SimpleDateFormat();
		df.applyPattern("yyyy-MM-dd");
		
		if(!fromDT.equals("")) {
			screenEnteredDate = fromDT+" to "+toDT; 
			request.setAttribute("screenEnteredDate", screenEnteredDate);
			log.info("The Screen Enterdate is"+screenEnteredDate);
		}
		String idDate = df.format(new java.util.Date());
		StringBuffer buff = new StringBuffer();
		
		if(trNum.equals("")  && lotNum.equals("") && prodNDC.equals("") && SSCC.equals("") && apndocId.equals("") && tpName.equals("") && fromDT.equals("") && toDT.equals("") && status.equals("")) {
			buff.append("for $b in collection('tig:///ePharma_MD/ShippedPedigree')/PedigreeEnvelope");
			request.setAttribute("idDate", idDate);
			buff.append(" where $b/date >= 'null' order by $b/date return $b");
			List res = queryRunner.executeQuery(buff.toString());
			sess.setAttribute("RPList", res);
		}else {
				
			 populateVectors("FromDate",fromDT,eltNames,eltValues);
			 populateVectors("ToDate",toDT,eltNames,eltValues);
			 populateVectors("ContainerCode",SSCC,eltNames,eltValues);
			 populateVectors("NDC",prodNDC,eltNames,eltValues);
			 populateVectors("LotNumber",lotNum,eltNames,eltValues);
			 populateVectors("TransNo",trNum,eltNames,eltValues);
			 populateVectors("PedID",apndocId,eltNames,eltValues);
			 populateVectors("TPName",tpName,eltNames,eltValues);
			 populateVectors("Status",status,eltNames,eltValues);
			 System.out.println(" The values are "+eltNames);
			 System.out.println(" The values are "+eltValues);
			 String names = returnVectorValues(eltNames);	
			 String values = returnVectorValues(eltValues);
			 StringBuffer buff2 = new StringBuffer();
			 buff2.append("tlsp:ShippingEnvelopeSearch_MD("+names+","+values+")");
			 log.info("query for search: "+buff2.toString());
			 List res = queryRunner.executeQuery(buff2.toString());
			 log.info("result  : "+res);
			 sess.setAttribute("RPList", res);
	
			 for(int i=0; i<res.size(); i++){
			
			theForm = new ReceivedPedigreeSearchFormBean();
			Node listNode = XMLUtil.parse((InputStream)res.get(i));
		
			theForm.setDataRcvd(CommonUtil.jspDisplayValue(listNode,"DateTime"));
			log.info("dataRcvd is:"+theForm.getDataRcvd());
			System.out.println("date: "+theForm.getDataRcvd());
			theForm.settrNum(CommonUtil.jspDisplayValue(listNode,"TransactionNumber"));
			log.info(" is:"+theForm.gettrNum());
			System.out.println(" is:"+theForm.gettrNum());
			theForm.setPedigreeNum(CommonUtil.jspDisplayValue(listNode,"DocumentId"));
			log.info("pedigreeNum is:"+theForm.getPedigreeNum());
			theForm.setNumOfPedigrees(CommonUtil.jspDisplayValue(listNode,"count"));
			log.info("NumOfPedigrees is:"+theForm.getNumOfPedigrees());
			theForm.setTrType(CommonUtil.jspDisplayValue(listNode,"TransactionType"));
			log.info("TransactionType is:"+theForm.getTrType());
			theForm.setStatus(CommonUtil.jspDisplayValue(listNode,"Status"));
			log.info("status is:"+theForm.getStatus());
			theForm.setTrdPrtnr(CommonUtil.jspDisplayValue(listNode,"Name"));
			theForm.setPedId(CommonUtil.jspDisplayValue(listNode,"DocumentId"));
			log.info("pedId is:"+theForm.getPedId());
			theForm.setCreatedBy("System");
			theForm.setTransactionType(CommonUtil.jspDisplayValue(listNode,"TransactionType"));
			theForm.setScreenEnteredDate(screenEnteredDate);
			colln.add(theForm);
			
		}
		}
		log.info("Before returning to success");
		request.setAttribute(Constants.PEDIGREE_DETAILS,colln);
		request.setAttribute("buttonname","find");
		}catch(PersistanceException e){
			log.error("Error in ReceivedPedigreeSearchAction execute method........." +e);
			throw new PersistanceException(e);
		}
		catch(Exception ex){			
			ex.printStackTrace();
    		log.error("Error in ReceivedPedigreeSearchAction execute method........." +ex);
    		throw new Exception(ex);
		}
	return mapping.findForward("success");
	}
	

	public String returnVectorValues(Vector elements) {
		
		Iterator iterator = elements.iterator();
		StringBuffer values = new StringBuffer();
		System.out.println("vector length: "+elements.size());
		int i =0;
		values.append("(");
		while(iterator.hasNext()){
			values.append("'");
			values.append(iterator.next());
			values.append("'");
			if(i<(elements.size()-1)){
			values.append(",");
			i++;
			}
		}values.append(")");
		System.out.println(" The values are "+values.toString());
		return values.toString();
	}
	
	public void populateVectors(String name, String value, Vector eltNames, Vector eltValues) {
		
		if( value == null || value.length() == 0) return;
		else{
			
			eltNames.addElement(name);
			eltValues.addElement(value);
		}
	}
}



