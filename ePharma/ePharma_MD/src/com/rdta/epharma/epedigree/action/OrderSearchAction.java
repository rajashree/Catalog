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


public class OrderSearchAction extends Action{
	private static Log log=LogFactory.getLog(OrderSearchAction.class);
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	Connection conn; 
	Statement stmt;
	String clientIP = null;
	Helper helper = new Helper();
		
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws PersistanceException {
		
		log.info("Inside Action OrderSearchAction class....... ");
		
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
			if(accessLevel.equalsIgnoreCase("ordersearch")|| accessLevel != null){
			List accessList = queryRunner.returnExecuteQueryStrings("tlsp:validateAccess_MD('"+sessionID+"','2.07','Read')");
			String readStatus = accessList.get(0).toString();
			log.info("The readStatus is : "+readStatus);
			if(readStatus.equalsIgnoreCase("false")){
				request.setAttribute("status","false");
				return mapping.findForward("failure");
			}
			}
		
		String fromDT = request.getParameter("fromDT");
		String toDT = request.getParameter("toDT");
		String lotNum = request.getParameter("lotNum");
		String prodNDC = request.getParameter("prodNDC");
		String orderNum = request.getParameter("orderNum");
		log.info("ORderNumber entered is :  "+orderNum);
		String apndocId = request.getParameter("apndocId");
		String tpName = request.getParameter("tpName");
		if(tpName == null || tpName.equalsIgnoreCase("SelectOne..") ) { tpName = "";}
		Collection colln= new ArrayList();
		
		log.info("  -------- fromDT:   " + fromDT + "  ---toDT :  "  + toDT  + "  ---- lotNum: " + lotNum );
		log.info("  -------- prodNDC:   " + prodNDC + "  ---orderNum :  "  + orderNum  + "  ---- apndocId: " + apndocId  + "---- tpName: " + tpName);
		
		boolean apnQueryFlag = false;
		
		StringBuffer orderCriteria = new StringBuffer();
		
		StringBuffer apnCriteria = new StringBuffer();
		
		//if apn docid entered we need to get the order id from the 
		//apn document

		if(apndocId!=null && !apndocId.trim().equalsIgnoreCase("")) {
//			apnQueryFlag = true;
			
			String buyersId = queryRunner.returnExecuteQueryStringsAsString("tlsp:getBuyersId_MD('"+apndocId+"')");
			System.out.println("The transaction Number is :"+buyersId);
			
			//add to apn criteria 
			apnCriteria.append(" $k/DocumentId='");
			apnCriteria.append(apndocId);	
			apnCriteria.append("' ");
			
			//add to order criteria also
			orderCriteria.append("and ");
			orderCriteria.append(" $a/BuyersID = '"+buyersId+"' ");
			
		} else {
			
			if(orderNum!=null && !orderNum.trim().equalsIgnoreCase("")) {
				orderCriteria.append("and ");
				orderCriteria.append("$a/BuyersID='");
				orderCriteria.append(orderNum);	
				orderCriteria.append("' ");
			}
		}
		
		if(prodNDC!=null && !prodNDC.trim().equalsIgnoreCase("")) {
			orderCriteria.append("and ");
			orderCriteria.append(" $a/OrderLine/LineItem/Item/SellersItemIdentification/ID='");
			orderCriteria.append(prodNDC);	
			orderCriteria.append("' ");
		}
		
		if(tpName!=null && !tpName.trim().equalsIgnoreCase("")) {
			orderCriteria.append("and ");
			orderCriteria.append(" $a/BuyerParty/PartyName/Name='");
			orderCriteria.append(tpName);	
			orderCriteria.append("' ");
		}
		
		if(toDT!=null && !toDT.trim().equalsIgnoreCase("")) {
			orderCriteria.append("and ");
			
			if(fromDT == null || fromDT.trim().equalsIgnoreCase("")) {
				fromDT = df.format(new java.util.Date());
			}
					
			orderCriteria.append("  $a/IssueDate >='");
			orderCriteria.append(fromDT +"T00:00:00");	
			orderCriteria.append("' ");
			
			orderCriteria.append(" and $a/IssueDate <='");
			orderCriteria.append(toDT +"T23:59:59");	
			orderCriteria.append("' ");
		}
		
		if(lotNum!=null && !lotNum.trim().equalsIgnoreCase("")) {
			orderCriteria.append("and ");
			orderCriteria.append(" $a/OrderLine/LineItem/Item/LotIdentification='");
			orderCriteria.append(lotNum);	
			orderCriteria.append("' ");
		}
		
		
		String orderCriteriaStr = orderCriteria.toString().trim();
		
		log.info(" criteriaStr :" + orderCriteriaStr );
		if( orderCriteriaStr.startsWith("and") ) {
			//remove 'and' 
			orderCriteriaStr = orderCriteriaStr.substring(3,orderCriteriaStr.length());
		}
		
		
			
		
		//out put XML string 
		StringBuffer outputQuery = new StringBuffer();
				
		outputQuery.append(" return <Output> ");
		outputQuery.append(" <BuyersID> {data($a/BuyersID)}  </BuyersID> ");
		outputQuery.append(" <IssueDate> {data($a/IssueDate)}  </IssueDate> ");
		outputQuery.append(" <TradingPartner>{ data($a/BuyerParty/PartyName/Name) } </TradingPartner> ");
		outputQuery.append(" <TotalNumOfNDCs> {count(distinct-values($a/OrderLine/LineItem/Item/SellersItemIdentification/ID)) }</TotalNumOfNDCs>");
		outputQuery.append(" <TotalLineItems>{count($a/OrderLine/LineItem)} </TotalLineItems>" );
		outputQuery.append(" <OrderAmount> { data($a/LineExtensionTotalAmount)}  </OrderAmount>");
		outputQuery.append(" {$a/BuyerParty/Address }");
		outputQuery.append(" </Output>");
		
		
		//construct final query with all part queries
		StringBuffer finalQuery = new StringBuffer();
		
//		if(apnQueryFlag) {
//			finalQuery.append("for $k in collection('tig:///ePharma/APN')/APN");
//			finalQuery.append(" where ");
//			finalQuery.append(apnCriteria.toString());
//			finalQuery.append(" return ");
//		} 
		
		//constructs Order query
		StringBuffer orderquery = new StringBuffer();
		orderquery.append(" for $a in collection('tig:///ePharma_MD/Orders')/Order ");
		 
		if(orderCriteriaStr.length() > 0) {
			orderquery.append( " where  " );
			orderquery.append( orderCriteriaStr ); 
		}else{
			orderquery.append("where $a/BuyersID = '' ");
		}
		
		//add order part of teh query
		finalQuery.append(orderquery.toString());
		
		//add output query
		finalQuery.append(outputQuery.toString());
		
		
		log.info(" Query in OrderSearchAction : " + finalQuery.toString() );
		
		List list = queryRunner.executeQuery(finalQuery.toString());
		log.info("Result in OrderSearchAction.........."+list.size());
		
		//set the results
		sess.setAttribute("List", list);
		
		
		for(int i=0; i<list.size(); i++){
			
			OrderSearchForm theForm =new OrderSearchForm();
			Node listNode = XMLUtil.parse((InputStream)list.get(i));
			
			
			theForm.setOrderNum(CommonUtil.jspDisplayValue(listNode,"BuyersID"));
			//theForm.setOrderNum(CommonUtil.jspDisplayValue(listNode,"orderNum"));
			log.info("Order NUmber is:"+theForm.getOrderNum());
			theForm.setIssueDate(CommonUtil.jspDisplayValue(listNode,"IssueDate"));
			log.info("Issue Date is:"+theForm.getIssueDate());
			theForm.setTradingPartner(CommonUtil.jspDisplayValue(listNode,"TradingPartner"));
			log.info("TradingPartner is:"+theForm.getTradingPartner());
			theForm.setNumOfNDCs(CommonUtil.jspDisplayValue(listNode,"TotalNumOfNDCs"));
			log.info("Number of NDCs is:"+theForm.getNumOfNDCs());
			theForm.setNumOfLineItems(CommonUtil.jspDisplayValue(listNode,"TotalLineItems"));
			log.info("Number of LineItems is:"+theForm.getNumOfLineItems());
			theForm.setAmount(CommonUtil.jspDisplayValue(listNode,"OrderAmount"));
			log.info("Order Amount is:"+theForm.getAmount());
			theForm.setAddress(CommonUtil.jspDisplayValue(listNode,"Address"));
			log.info("Address is:"+theForm.getAddress());
			
			//theForm.setScreenEnteredDate(screenEnteredDate);
			colln.add(theForm);
		}
		
		
		log.info("Before returning to success");
		request.setAttribute(Constants.ORDER_DETAILS,colln);
		
	}catch(Exception ex){
		ex.printStackTrace();
		log.error("Error in OrderSearchAction execute method........." +ex);
		return mapping.findForward("exception");
	}finally{
		//close the connection
    	helper.CloseConnectionTL(conn);
	}
		return mapping.findForward("success");
	 }

}
