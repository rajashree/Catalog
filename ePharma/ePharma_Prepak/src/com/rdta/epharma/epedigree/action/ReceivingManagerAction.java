/*
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


public class ReceivingManagerAction extends Action {
	
	private static Log log=LogFactory.getLog(ReceivingManagerAction.class);
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	String servIP = null;
	String clientIP = null;
	String tp_company_nm = null;
	String fromDt = null;
	String toDt=null;	
	String containerCode = null;
	String prodNDC = null;
	String lotNum = null;
	String trNum = null;
	String pedId = null;
	String envelopeId =null;
	String status = null;
    String tpName = null;
    String pedigreeState = null;
	Connection conn; 
	Statement stmt;
	Helper helper = new Helper();
	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		log.info("Inside Action ReceivingManagerAction....... ");
		
		String offset = (String)request.getParameter("offset");
		log.info("Hi..................*********** offset"+offset);
		if(offset != null && offset.trim().length() > 0){
			int off = Integer.parseInt(offset);
			log.info("The value of offset in SearchInvoiceAction is :"+off);
		}else{
			offset="0";
		}
			log.info("Hi..................*********** off :"+request.getParameter("off"));
		request.setAttribute("offset", offset);	
				
		ReceivingManagerForms theForm = null; 
		servIP = request.getServerName();
		clientIP = request.getRemoteAddr();
		fromDt = request.getParameter("fromDt");
		toDt = request.getParameter("toDt");
		containerCode = request.getParameter("containerCode");
		prodNDC = request.getParameter("prodNDC");
		lotNum = request.getParameter("lotNum");
		trNum = request.getParameter("trNum");
		pedId = request.getParameter("pedId");
		envelopeId = request.getParameter("envelopeId");
		pedigreeState = request.getParameter("status");
		//request.setAttribute("status",pedigreeState);
		tpName = request.getParameter("tpName");
		Vector eltNames = new Vector();
		Vector eltValues = new Vector();
		Collection colln= new ArrayList();
		tp_company_nm = request.getParameter("tp_company_nm");
			
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
			String accesslevel = "";
			accesslevel = request.getParameter("accesslevel");
			log.info("****** "+accesslevel);
			if(accesslevel != null){
			if(accesslevel.equalsIgnoreCase("recvng")){
			String s2 = "tlsp:validateAccess('"+sessionID+"','2.01','Read')" ;
			List  xQuery = queryRunner.returnExecuteQueryStrings(s2);
			String res= xQuery.get(0).toString();
			log.info("Query"+xQuery);
			if(res.equalsIgnoreCase("false")){
				request.setAttribute("statusr","false");
				return mapping.findForward("failure");
			}else

				request.setAttribute("statusr","true");
			}
				}
			
			
		
				
		if(tp_company_nm == null) { tp_company_nm = "";}
		
		if(fromDt == null) { fromDt = "";}
		if(toDt == null) { toDt = "";}
		if(containerCode == null) { containerCode = "";}
		if(prodNDC == null) { prodNDC = "";}
		if(lotNum == null) { lotNum = "";}
		if(trNum == null) { trNum = ""; }
		if(pedId == null) { pedId = "";}
		if(envelopeId == null) { envelopeId = "";}
		if(status == null) { status = "";}
		if(tpName == null) { tpName = "";}
		if(pedigreeState == null || pedigreeState.equalsIgnoreCase("Select....") ) { pedigreeState = "";}

		
		String s = "tlsp:validateAccess('"+sessionID+"','2.02','Read')" ;
		List  accessList1 = queryRunner.returnExecuteQueryStrings(s);
		String s3= accessList1.get(0).toString();
		log.info("the string value"+s3);
		if(s3.equalsIgnoreCase("false")){
			request.setAttribute("accessfind","false");
		}else
			request.setAttribute("accessfind","true");
		
		SimpleDateFormat df = new SimpleDateFormat();
		df.applyPattern("yyyy-MM-dd");
		String idDate = df.format(new java.util.Date());	
		
		StringBuffer buf = new StringBuffer();
		buf.append("for $i in collection('tig:///CatalogManager/TradingPartner')/TradingPartner ");
		buf.append("return data($i/name) ");
		List tpNames = queryRunner.returnExecuteQueryStrings(buf.toString());
		log.info("TP Names : "+tpNames.toString());
		request.setAttribute("tpNames",tpNames);
		if(fromDt.equals("") && toDt.equals("")  && containerCode.equals("") && prodNDC.equals("") && lotNum.equals("") && trNum.equals("") &&pedId.equals("") &&  envelopeId.equals("") &&  pedigreeState.equals("") && tpName.equals("")  ) {
			StringBuffer buff1 = new StringBuffer();
			
			buff1.append("for $b in collection('tig:///ePharma/ReceivedPedigree')/PedigreeEnvelope");

			buff1.append(" where $b/date >= '"+idDate+"' order by $b/date return $b");

		
			List res = queryRunner.executeQuery(buff1.toString());
			request.setAttribute("List", res);
			
		}else {
			
			 populateVectors("FromDate",fromDt,eltNames,eltValues);
			 populateVectors("ToDate",toDt,eltNames,eltValues);
			 populateVectors("ContainerCode",containerCode,eltNames,eltValues);
			 populateVectors("NDC",prodNDC,eltNames,eltValues);
			 populateVectors("LotNumber",lotNum,eltNames,eltValues);
			 populateVectors("TransNo",trNum,eltNames,eltValues);
			 populateVectors("PedID",envelopeId,eltNames,eltValues);
			 populateVectors("TPName",tpName,eltNames,eltValues);
			 //populateVectors("Status",pedigreeState,eltNames,eltValues);

			 
			 if(pedigreeState.equalsIgnoreCase("Received")){
				populateVectors("",status,eltNames,eltValues);
			}else{
				 
			populateVectors("Status",pedigreeState,eltNames,eltValues);
			 }
		 
			 log.info(" The values are "+eltNames);
			 log.info(" The values are "+eltValues);
			 String names = returnVectorValues(eltNames);	
			 String values = returnVectorValues(eltValues);
			 StringBuffer xQueryBuffer = new StringBuffer();

			 xQueryBuffer.append("tlsp:EnvelopeSearch("+names+","+values+")");
			 log.info("Results list: "+xQueryBuffer.toString());
			 List res = queryRunner.executeQuery(xQueryBuffer.toString());
			 log.info("Results list: "+res);
			 request.setAttribute("List", res);

			 for(int i=0; i<res.size(); i++){
					
					
					theForm = new ReceivingManagerForms();
					Node listNode = XMLUtil.parse((InputStream)res.get(i));
					
					
					theForm.setEnvelopeId(CommonUtil.jspDisplayValue(listNode,"envelopeId"));
					log.info("The EnvolopeNo is:"+theForm.getEnvelopeId());
					theForm.setDateReceived(CommonUtil.jspDisplayValue(listNode,"dateRecieved"));
					theForm.setSource(CommonUtil.jspDisplayValue(listNode,"source"));
					log.info("Trading Partner Source is:"+theForm.getSource());
					theForm.setDestination(CommonUtil.jspDisplayValue(listNode,"destination"));
					log.info("Trading Partner Destination is:"+theForm.getDestination());
					theForm.setNumOfPedigrees(CommonUtil.jspDisplayValue(listNode,"count"));
					theForm.setPedId(CommonUtil.jspDisplayValue(listNode,"pedigreeID"));
					theForm.setStatus(CommonUtil.jspDisplayValue(listNode,"status"));
					theForm.setTpName(CommonUtil.jspDisplayValue(listNode,"tradingPartner"));
					theForm.setTrNum(CommonUtil.jspDisplayValue(listNode,"transactionNumber"));
					log.info("ped id is:"+theForm.getPedId());
					
					
					colln.add(theForm);
				}
		}
		log.info("Before returning to success");
		request.setAttribute(Constants.RCVNG_MNGR_DETAILS,colln);
			

		}catch (PersistanceException e) {
	    	log.error("Error in  ReceivingManagerAction  execute mathod ......" + e);
	    	throw new PersistanceException(e);	
		}catch(Exception ex){ 
			ex.printStackTrace();
		    log.error("Error in  ReceivingManagerAction execute method........." +ex);
		     throw new Exception(ex);
		}
	return mapping.findForward("success");
	}
public String returnVectorValues(Vector elements)throws Exception{
		try{
			
			Iterator iterator = elements.iterator();
			StringBuffer values = new StringBuffer();
			log.info("vector length: "+elements.size());
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
			log.info(" The values are "+values.toString());
			return values.toString();
			
			
		}catch(Exception ex){ 
			ex.printStackTrace();
			log.error("Error in PedigreeSearchAction execute method........." +ex);
			throw new Exception(ex);
			
		}	
}
	
	
	public void populateVectors(String name, String value, Vector eltNames, Vector eltValues) throws Exception{
		try{
			if( value == null || value.length() == 0) return;
			else{
				
				eltNames.addElement(name);
				eltValues.addElement(value);
				
			}
		}catch(Exception ex){ 
			ex.printStackTrace();
			log.error("Error in PedigreeSearchAction execute method........." +ex);
			throw new Exception(ex);
			
		}	
	}

	
	}




