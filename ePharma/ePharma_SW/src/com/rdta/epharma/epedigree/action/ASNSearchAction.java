
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
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


public class ASNSearchAction extends Action{
	private static Log log=LogFactory.getLog(ASNSearchAction.class);
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	Connection conn; 
	Statement stmt;
	String clientIP = null;
	Helper helper = new Helper();
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		log.info("Inside ASNSearchAction class....... ");
		log.info("Inside ASNSearchAction class execute()....... ");
	
	try{	
		
		HttpSession sess = request.getSession();
		clientIP = request.getRemoteAddr();		
		conn = helper.ConnectTL(); 
		stmt = helper.getStatement(conn);
		log.info("Validating The Session");
		
		//Validating Session
		String sessionID = (String)sess.getAttribute("sessionID");
		log.info("sessionID in ASNSearchAction :"+sessionID);
		String validateResult = helper.ValidateUserSession(stmt, sessionID, clientIP);        
	
		if ( !validateResult.equals("VALID")){
		    //return a forward to invalid .
		    return mapping.findForward("loginPage");
		}
		
		StringBuffer buf = new StringBuffer();
		buf.append("for $i in collection('tig:///CatalogManager/TradingPartner')/TradingPartner ");
		buf.append("return data($i/name) ");
		List tpNames = queryRunner.returnExecuteQueryStrings(buf.toString());
		System.out.println("TP Names : "+tpNames.toString());
		sess.setAttribute("tpNames",tpNames);
		
		String accessLevel = null;
		accessLevel = request.getParameter("accesslevel");
		if(accessLevel.equalsIgnoreCase("asnsearch")|| accessLevel != null){
		List accessList = queryRunner.returnExecuteQueryStrings("tlsp:validateAccess('"+sessionID+"','2.12','Read')");
		String readStatus = accessList.get(0).toString();
		log.info("The readStatus is : "+readStatus);
		if(readStatus.equalsIgnoreCase("false")){
			request.setAttribute("status","false");
			//request.setAttribute("buttonname",buttonname);
			return mapping.findForward("failure");
		}
		}
		
		String fromDT = request.getParameter("fromDT");
		String toDT = request.getParameter("toDT");
		String sscc = request.getParameter("SSCC");
		String lotNum = request.getParameter("lotNum");
		String prodNDC = request.getParameter("prodNDC");
		String asnNum = request.getParameter("asnNum");
		String apndocId = request.getParameter("apndocId");
		String tpName = request.getParameter("tpName");
		if(tpName == null || tpName.equalsIgnoreCase("SelectOne..") ) { tpName = "";}
		
		Collection colln= new ArrayList();
		
		log.info("  -------- fromDT:   " + fromDT + "  ---toDT :  "  + toDT  + "  ---- lotNum: " + lotNum );
		log.info("  -------- prodNDC:   " + prodNDC + "  ---asnNum :  "  + asnNum  + "  ---- apndocId: " + apndocId  + "---- tpName: " + tpName);
		
		boolean apnQueryFlag = false;
		
		StringBuffer dispatchAdviceCriteria = new StringBuffer();
		
		StringBuffer apnCriteria = new StringBuffer();
		
		//if apn docid entered we need to get the order id from the 
		//apn document
	
		if(apndocId!=null && !apndocId.trim().equalsIgnoreCase("")) {
			//apnQueryFlag = true;
			
			//add to apn criteria 
			String transactionNumber = queryRunner.returnExecuteQueryStringsAsString("data(tlsp:getSearchResults2(('PedID'),('"+apndocId+"'))/Record/transactionNumber)");
			System.out.println("The transaction Number is :"+transactionNumber);
			
			apnCriteria.append(" $k/DocumentId='");
			apnCriteria.append(apndocId);	
			apnCriteria.append("' ");
			
			//add to order criteria also
			dispatchAdviceCriteria.append("and ");
			//dispatchAdviceCriteria.append(" $a/ID= $k/To/TransactionNumber ");
			dispatchAdviceCriteria.append(" $a/ID= '"+transactionNumber+"' ");
			
		} else {
			
			if(asnNum!=null && !asnNum.trim().equalsIgnoreCase("")) {
				dispatchAdviceCriteria.append("and ");
				dispatchAdviceCriteria.append("$a/ID='");
				dispatchAdviceCriteria.append(asnNum);	
				dispatchAdviceCriteria.append("' ");
			}
		}
		
		if(prodNDC!=null && !prodNDC.trim().equalsIgnoreCase("")) {
			dispatchAdviceCriteria.append("and ");
			dispatchAdviceCriteria.append(" $a/DespatchLine/Item/SellersItemIdentification/ID='");
			dispatchAdviceCriteria.append(prodNDC);	
			dispatchAdviceCriteria.append("' ");
		}
		
		if(sscc!=null && !sscc.trim().equalsIgnoreCase("")) {
			dispatchAdviceCriteria.append("and ");
			dispatchAdviceCriteria.append(" $a/DespatchLine/Item/AdditionalItemIdentification/ID='");
			dispatchAdviceCriteria.append(sscc);	
			dispatchAdviceCriteria.append("' ");
		}
		
		if(tpName!=null && !tpName.trim().equalsIgnoreCase("")) {
			dispatchAdviceCriteria.append("and ");
			dispatchAdviceCriteria.append(" $a/BuyerParty/Party/PartyName/Name='");
			dispatchAdviceCriteria.append(tpName);	
			dispatchAdviceCriteria.append("' ");
		}
		
		if(toDT!=null && !toDT.trim().equalsIgnoreCase("")) {
			dispatchAdviceCriteria.append("and ");
			
			if(fromDT == null || fromDT.trim().equalsIgnoreCase("")) {
				fromDT = df.format(new java.util.Date());
			}
					
			dispatchAdviceCriteria.append("  $a/IssueDate >='");
			dispatchAdviceCriteria.append(fromDT +"T00:00:00");	
			dispatchAdviceCriteria.append("' ");
			
			dispatchAdviceCriteria.append(" and $a/IssueDate <='");
			dispatchAdviceCriteria.append(toDT +"T23:59:59");	
			dispatchAdviceCriteria.append("' ");
		}
		
		if(lotNum!=null && !lotNum.trim().equalsIgnoreCase("")) {
			dispatchAdviceCriteria.append("and ");
			dispatchAdviceCriteria.append(" $a/InvoiceLine/Item/LotIdentification='");
			dispatchAdviceCriteria.append(lotNum);	
			dispatchAdviceCriteria.append("' ");
		}
		
		
		String dispatchAdviceCriteriaStr = dispatchAdviceCriteria.toString().trim();
		log.info(" criteriaStr :" + dispatchAdviceCriteriaStr );
		if( dispatchAdviceCriteriaStr.startsWith("and") ) {
			//remove 'and' 
			dispatchAdviceCriteriaStr = dispatchAdviceCriteriaStr.substring(3,dispatchAdviceCriteriaStr.length());
		}
		
		
		//out put XML string 
		StringBuffer outputQuery = new StringBuffer();
				
		outputQuery.append(" return <Output> ");
		outputQuery.append(" <ID> {data($a/ID)}  </ID> ");
		outputQuery.append(" <IssueDate> {data($a/IssueDate)}  </IssueDate> ");
		outputQuery.append(" <TradingPartner>{ data($a/BuyerParty/Party/PartyName/Name) } </TradingPartner> ");
		outputQuery.append(" <TotalNumOfNDCs> {count(distinct-values($a/DespatchLine/Item/SellersItemIdentification/ID)) }</TotalNumOfNDCs>");
		outputQuery.append(" <TotalLineItems>{count($a/DespatchLine/Item)} </TotalLineItems>" );
		outputQuery.append(" <Amount> { data( $a/DespatchLine/Item/BasePrice/PriceAmount)}  </Amount>");
		outputQuery.append(" {$a/BuyerParty/Party/Address }");
		outputQuery.append(" </Output>");
		
		
		//construct final query with all part queries
		StringBuffer finalQuery = new StringBuffer();
		
//		if(apnQueryFlag) {
//			finalQuery.append("for $k in collection('tig:///ePharma/APN')/APN");
//			finalQuery.append(" where ");
//			finalQuery.append(apnCriteria.toString());
//			finalQuery.append(" return ");
//		} 
		
		//constructs DespatchAdvice query
		StringBuffer dispatchAdviceQuery = new StringBuffer();
		dispatchAdviceQuery.append(" for $a in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice ");
		 
		if(dispatchAdviceCriteriaStr.length() > 0) {
			dispatchAdviceQuery.append( " where " );
			dispatchAdviceQuery.append( dispatchAdviceCriteriaStr ); 
		}else{
			dispatchAdviceQuery.append( " where $a/ID = '' " );
		}
		
		//add order part of teh query
		finalQuery.append(dispatchAdviceQuery.toString());
		
		//add output query
		finalQuery.append(outputQuery.toString());
		
		
		log.info(" Query in ASNSearchAction : " + finalQuery.toString() );
		
		List list = queryRunner.executeQuery(finalQuery.toString());
		
		//set the results
		request.setAttribute("List", list);
		
		
		for(int i=0; i<list.size(); i++){
			
			ASNSearchForm theForm =new ASNSearchForm();
			Node listNode = XMLUtil.parse((InputStream)list.get(i));
			
			
			theForm.setASNNum(CommonUtil.jspDisplayValue(listNode,"ID"));
			//theForm.setOrderNum(CommonUtil.jspDisplayValue(listNode,"orderNum"));
			log.info("Invoice NUmber is:"+theForm.getASNNum());
			theForm.setIssueDate(CommonUtil.jspDisplayValue(listNode,"IssueDate"));
			log.info("Issue Date is:"+theForm.getIssueDate());
			theForm.setTradingPartner(CommonUtil.jspDisplayValue(listNode,"TradingPartner"));
			log.info("TradingPartner is:"+theForm.getTradingPartner());
			theForm.setNumOfNDCs(CommonUtil.jspDisplayValue(listNode,"TotalNumOfNDCs"));
			log.info("Number of NDCs is:"+theForm.getNumOfNDCs());
			theForm.setNumOfLineItems(CommonUtil.jspDisplayValue(listNode,"TotalLineItems"));
			log.info("Number of LineItems is:"+theForm.getNumOfLineItems());
			theForm.setAmount(CommonUtil.jspDisplayValue(listNode,"Amount"));
			log.info("Invoice Amount is:"+theForm.getAmount());
			theForm.setAddress(CommonUtil.jspDisplayValue(listNode,"Address"));
			log.info("Address is:"+theForm.getAddress());
			
			
			colln.add(theForm);
		}
		
		
		log.info("Before returning to success");
		request.setAttribute(Constants.ASN_DETAILS,colln);
		
		
	}catch(Exception ex){
		ex.printStackTrace();
		log.error("Error in ASNSearchAction execute method.........");
		return mapping.findForward("exception");
	}	finally{
		//close the connection
    	helper.CloseConnectionTL(conn);
	}
		return mapping.findForward("success");
	 }

}
