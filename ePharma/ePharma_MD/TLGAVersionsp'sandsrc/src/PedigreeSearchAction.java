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
import com.rdta.Admin.servlet.RepConstants;
import com.rdta.catalog.Constants;
import com.rdta.commons.CommonUtil;
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;


public class PedigreeSearchAction extends Action{
	private static Log log=LogFactory.getLog(PedigreeSearchAction.class);
	
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	String servIP = null;
	String clientIP = null;
	String fromDT = null;
	String toDT = null;
	String lotNum = null;
	String prodNDC = null;
	String trNum = null;
	String sessionID = null;
	String pagenm = null;
	String tp_company_nm = null;
	String SSCC = null;
	String apndocId = null;
	String tpName = null;
	String pedigreeState = null;
	String screenEnteredDate = null;
	String transactionType = null;
	Connection conn; 
	Statement stmt;
	Helper helper = new Helper();
	
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		log.info("Inside Action PedigreeSearchAction....... ");
		System.out.println("Inside Action execute of Pedigree SearchAction....");
		APNSearchForm theForm = null;
		Collection colln= new ArrayList();		
		Vector eltNames = new Vector();
		Vector eltValues = new Vector();
		
		servIP = request.getServerName().trim();
		clientIP = request.getRemoteAddr().trim();
		fromDT = request.getParameter("fromDT");
		toDT = request.getParameter("toDT");
		lotNum = request.getParameter("lotNum");
		prodNDC = request.getParameter("prodNDC");
		trNum = request.getParameter("trNum");
		pagenm = request.getParameter("pagenm");
		tp_company_nm = request.getParameter("tp_company_nm");
		SSCC = request.getParameter("sscc");
		System.out.println("SSCC value : "+SSCC);
		apndocId = request.getParameter("apndocId");
		tpName = request.getParameter("tpName");
		pedigreeState = request.getParameter("pedstate");
		System.out.println("Pedigree State value in action : "+pedigreeState);
		System.out.println("values are : "+fromDT + "  "+SSCC+" "+" "+lotNum+" "+trNum+ " "+apndocId);
		String listsize = null;
		try{	
			
			HttpSession sess = request.getSession();
			clientIP = request.getRemoteAddr();		
			conn = helper.ConnectTL(); 
			stmt = helper.getStatement(conn);
			log.info("Validating The Session");
			
			//Validating Session
			String sessionID = (String)sess.getAttribute("sessionID");
			System.out.println("sessionID in Action :"+sessionID);
			log.info("sessionID in Action :"+sessionID);
			String validateResult = helper.ValidateUserSession(stmt, sessionID, clientIP);        
		
			if ( !validateResult.equals("VALID")){
			    //return a forward to invalid .
			    return mapping.findForward("loginPage");
			}
			String accessLevel = null;
			accessLevel = request.getParameter("accesslevel");
			if(accessLevel.equalsIgnoreCase("apnsearch")|| accessLevel != null){
				
			String validate = "tlsp:validateAccess_MD('"+sessionID+"','2.10','Insert')";
			log.info("Query for getting Access"+validate);
			List accessList = queryRunner.returnExecuteQueryStrings(validate);
			String readStatus = accessList.get(0).toString();
			log.info("The readStatus is : "+readStatus);
			if(readStatus.equalsIgnoreCase("false")){
				request.setAttribute("status","false");
				return mapping.findForward("failure");
			}
			}
			
								
		if(trNum == null) { trNum = "";}
		
		if(lotNum == null) { lotNum = "";}
		if(prodNDC == null) { prodNDC = "";}
		if(SSCC == null) { SSCC = "";}
		if(fromDT == null) { fromDT = "";}
		if(toDT == null) { toDT = "";}
		if(pagenm == null) { pagenm = "";}
		if(tp_company_nm == null) { tp_company_nm = "";}
		if(apndocId == null) { apndocId = "";}
		if(tpName == null || tpName.equalsIgnoreCase("SelectOne..") ) { tpName = "";}
		if(pedigreeState == null || pedigreeState.equalsIgnoreCase("SelectOne..") ) { pedigreeState = "";}
		if(transactionType == null) { transactionType = ""; }
		
		SimpleDateFormat df = new SimpleDateFormat();
		df.applyPattern("yyyy-MM-dd");
		
	
		 if(!fromDT.equals("")) {
			screenEnteredDate = fromDT+" to "+toDT; 
		request.setAttribute("screenEnteredDate", screenEnteredDate);
		log.info("Screen Entered Date  : "+screenEnteredDate);
		}
		 
		 String idDate = "";
		 idDate = df.format(new java.util.Date())+"T00:00:00";
			
		//IF NOT SEARCHING BY order, po or invoice etc
		log.info("Querying for From Date to To Date.......");
		
		
		StringBuffer buf = new StringBuffer();
		buf.append("for $i in collection('tig:///CatalogManager/TradingPartner')/TradingPartner ");
		buf.append("return data($i/name) ");
		log.info("Query for Getting Trading partners in PedigreeSearchAction"+buf.toString());
		List tpNames = queryRunner.returnExecuteQueryStrings(buf.toString());
		System.out.println("TP Names : "+tpNames.toString());
		sess.setAttribute("tpNames",tpNames);
		

		if(fromDT.equals("")&& toDT.equals("")&& trNum.equals("")  && lotNum.equals("") && prodNDC.equals("") && SSCC.equals("") && apndocId.equals("") && tpName.equals("") && pedigreeState.equals("")) {
			listsize = "0";
			request.setAttribute("listsize",listsize);
			return mapping.findForward("success");

		}else {
			
			 System.out.println(" ELSE Inside Action execute of APNSearchAction.... ");
			
			 populateVectors("FromDate",fromDT,eltNames,eltValues);
			 populateVectors("ToDate",toDT,eltNames,eltValues);
			 populateVectors("ContainerCode",SSCC,eltNames,eltValues);
			 populateVectors("NDC",prodNDC,eltNames,eltValues);
			 populateVectors("LotNumber",lotNum,eltNames,eltValues);
			 populateVectors("TransNo",trNum,eltNames,eltValues);
			 populateVectors("PedID",apndocId,eltNames,eltValues);
			 populateVectors("TPName",tpName,eltNames,eltValues);
			 populateVectors("Status",pedigreeState,eltNames,eltValues);
			 
			 System.out.println(" The values are "+eltNames);
			 System.out.println(" The values are "+eltValues);
			 
			 String names = returnVectorValues(eltNames);	
			 String values = returnVectorValues(eltValues);
			 StringBuffer buff2 = new StringBuffer();
			 
			
			 buff2.append("tlsp:getSearchQueriesNew2R_MD("+names+","+values+")");
			 System.out.println("query for search: "+buff2.toString());
			 System.out.println("query for pedigree search: "+buff2.toString());
			 String res1 = queryRunner.returnExecuteQueryStringsAsString(buff2.toString());
			 System.out.println("res1"+res1);
			 List res = queryRunner.executeQuery(res1);
			 
		/*	 
		buff2.append("tlsp:getSearchQueriesNew2R_MD("+names+","+values+")");
			 System.out.println("query for search: "+buff2.toString());
			 log.info("query for pedigree search: "+buff2.toString());
			 String res1 = queryRunner.returnExecuteQueryStringsAsString(buff2.toString());
			 System.out.println("res1"+res1);
			 List res = queryRunner.executeQuery(res1);*/
			 System.out.println("List size after execution of search SP is : "+res.size());
			 request.setAttribute("List", res);
			 
			 
			 
			 
			 System.out.println("List size after execution of search SP is : "+res.size());
			 request.setAttribute("List", res);
			 
			 if(res.size() == 0) {
				 listsize = "0";
				 request.setAttribute("listsize",listsize);
			 }
			 
		
		for(int i=0; i<res.size(); i++){
			
			theForm = new APNSearchForm();
			Node listNode = XMLUtil.parse((InputStream)res.get(i));
			//Node listNode = XMLUtil.parse((String)res.get(i));
			
			theForm.setPedigreeId(CommonUtil.jspDisplayValue(listNode,"pedigreeID"));
			log.info("pedigree ID is:"+theForm.getPedigreeId());
			System.out.println("pedigree ID is:"+theForm.getPedigreeId());
			
			theForm.setEnvelopeId(CommonUtil.jspDisplayValue(listNode,"envelopID"));
			log.info("Envelope ID is:"+theForm.getEnvelopeId());
			System.out.println("Envelope ID is:"+theForm.getEnvelopeId());
			
			theForm.setDataRcvd(CommonUtil.jspDisplayValue(listNode,"dateRecieved"));
			System.out.println("dataRcvd is:"+theForm.getDataRcvd());
			log.info("dataRcvd is:"+theForm.getDataRcvd());
			
			theForm.setTrdPrtnr(CommonUtil.jspDisplayValue(listNode,"tradingPartner"));
			log.info("Trading partner name is:"+theForm.getTrdPrtnr());
			System.out.println("Trading partner name is:"+theForm.getTrdPrtnr());
			
			theForm.settrNum(CommonUtil.jspDisplayValue(listNode,"transactionNumber"));
			log.info(" Transaction Number is :"+theForm.gettrNum());
			System.out.println("dataRcvd is:"+theForm.gettrNum());
			
			theForm.setStatus(CommonUtil.jspDisplayValue(listNode,"status"));
			log.info("status is:"+theForm.getStatus());
			System.out.println("Transaction Number is :"+theForm.getStatus());
			
			theForm.setCreatedBy(CommonUtil.jspDisplayValue(listNode,"createdBy"));
			log.info("Created By :"+theForm.getCreatedBy());
			System.out.println("Created By :"+theForm.getCreatedBy());
			
			theForm.setScreenEnteredDate(screenEnteredDate);
			colln.add(theForm);
			
		}
		}
		log.info("Before returning to success");
		request.setAttribute(Constants.APN_DETAILS,colln);
		}catch(PersistanceException e){    		
			log.error("Error in PedigreeSearchAction execute method........." +e);
			throw new PersistanceException(e);
		}
		catch(Exception ex){ 
			ex.printStackTrace();
			log.error("Error in PedigreeSearchAction execute method........." +ex);
			throw new Exception(ex);
			
		}	
		finally{
			//close the connection
	    	helper.CloseConnectionTL(conn);
		}
												
		return mapping.findForward("success");
	}
	
	public String returnVectorValues(Vector elements) throws Exception{
		
		try{
			
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
		}catch(Exception ex){ 
			ex.printStackTrace();
			log.error("Error in PedigreeSearchAction execute method........." +ex);
			throw new Exception(ex);
			
		}	
	}
	
	public void populateVectors(String name, String value, Vector eltNames, Vector eltValues) throws Exception {
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
