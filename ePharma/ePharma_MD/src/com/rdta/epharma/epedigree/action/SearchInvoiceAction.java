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
       
      
       public class SearchInvoiceAction extends Action{

    	   private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
           private static Log log = LogFactory.getLog(SearchInvoiceAction.class);
           private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
           Connection conn; 
       	   Statement stmt;
       	   String servIP = null;
       	   String clientIP = null;
       	   Helper helper = new Helper();
                   
           public ActionForward execute(ActionMapping mapping, 
                   ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
           	
           	log.info("*****Inside SearchInvoiceAction class........");
           	servIP = request.getServerName();
    		clientIP = request.getRemoteAddr();    		
    		
    		String offset = request.getParameter("offset");
    		if(offset != null){
    			int off = Integer.parseInt(offset);
    			System.out.println("The value of offset in SearchInvoiceAction is :"+off);
    		}else{
    			offset="0";
    		}
    		
    		request.setAttribute("offset", offset);	
    		
           
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
			log.info("Query for Getting Trading partners in SearchInvoiceAction"+buf.toString());
			List tpNames = queryRunner.returnExecuteQueryStrings(buf.toString());
			System.out.println("TP Names : "+tpNames.toString());
			sess.setAttribute("tpNames",tpNames);
			
			String accessLevel = null;
			accessLevel = request.getParameter("accesslevel");
			//if(accessLevel.equalsIgnoreCase("invoicesearch")|| accessLevel != null){
			
			String validate = "tlsp:validateAccess('"+sessionID+"','2.11','Insert')";
			log.info("Query for getting Access"+validate);
			List accessList = queryRunner.returnExecuteQueryStrings(validate);
			String readStatus = accessList.get(0).toString();
			log.info("The readStatus is : "+readStatus);
			if(readStatus.equalsIgnoreCase("false")){
				request.setAttribute("status","false");
				return mapping.findForward("failure");
			}
			//}
			
           	String fromDT = request.getParameter("fromDT");
    		String toDT = request.getParameter("toDT");
    		String lotNum = request.getParameter("lotNum");
    		String prodNDC = request.getParameter("prodNDC");
    		String invoiceId = request.getParameter("invoiceID");
    		String apndocId = request.getParameter("apndocId");
    		String tpName = request.getParameter("tpName");
    		if(tpName == null || tpName.equalsIgnoreCase("SelectOne..") ) { tpName = "";}
    		Collection colln = new ArrayList();
    		
    		log.info("  -------- fromDT:   " + fromDT + "  ---toDT :  "  + toDT  + "  ---- lotNum: " + lotNum );
    		log.info("  -------- prodNDC:   " + prodNDC + "  ---invoiceId :  "  +invoiceId+ "  ---- apndocId: " + apndocId  + "---- tpName: " + tpName);
    		
    		boolean apnQueryFlag = false;
    		
    		StringBuffer invoiceCriteria = new StringBuffer();
    		
    		StringBuffer apnCriteria = new StringBuffer();
    		
    		//if apn docid entered we need to get the order id from the 
    		//apn document
    	
    		if(apndocId!=null && !apndocId.trim().equalsIgnoreCase("")) {
    			apnQueryFlag = true;
    			
    			String invoiceID = queryRunner.returnExecuteQueryStringsAsString("tlsp:getInvoiceId('"+apndocId+"')");
    			System.out.println("The transaction Number is :"+invoiceID);
    			
    			//add to apn criteria 
    			apnCriteria.append(" $k/DocumentId='");
    			apnCriteria.append(apndocId);	
    			apnCriteria.append("' ");
    			
    			//add to order criteria also
    			invoiceCriteria.append("and ");
    			invoiceCriteria.append(" $a/ID= '"+invoiceID+"' ");
    			
    		} else {
    			
    			if(invoiceId!=null && !invoiceId.trim().equalsIgnoreCase("")) {
    				invoiceCriteria.append("and ");
    				invoiceCriteria.append("$a/ID='"+invoiceId+"'");
    				//invoiceCriteria.append(invoiceId);	
    				//invoiceCriteria.append("' ");
    			}
    		}
    		
    		if(prodNDC!=null && !prodNDC.trim().equalsIgnoreCase("")) {
    			invoiceCriteria.append("and ");
    			invoiceCriteria.append(" $a/InvoiceLine/Item/SellersItemIdentification/ID='");
    			invoiceCriteria.append(prodNDC);	
    			invoiceCriteria.append("' ");
    		}
    		
    		if(tpName!=null && !tpName.trim().equalsIgnoreCase("")) {
    			invoiceCriteria.append("and ");
    			invoiceCriteria.append(" $a/BuyerParty/Party/PartyName/Name='");
    			invoiceCriteria.append(tpName);	
    			invoiceCriteria.append("' ");
    		}
    		
    		if(toDT!=null && !toDT.trim().equalsIgnoreCase("")) {
    			invoiceCriteria.append("and ");
    			
    			if(fromDT == null || fromDT.trim().equalsIgnoreCase("")) {
    				fromDT = df.format(new java.util.Date());
    			}
    					
    			invoiceCriteria.append("  $a/IssueDate >='");
    			invoiceCriteria.append(fromDT +"T00:00:00");	
    			invoiceCriteria.append("' ");
    			
    			invoiceCriteria.append(" and $a/IssueDate <='");
    			invoiceCriteria.append(toDT +"T23:59:59");	
    			invoiceCriteria.append("' ");
    		}
    		
    		if(lotNum!=null && !lotNum.trim().equalsIgnoreCase("")) {
    			invoiceCriteria.append("and ");
    			invoiceCriteria.append(" $a/InvoiceLine/Item/LotIdentification='");
    			invoiceCriteria.append(lotNum);	
    			invoiceCriteria.append("' ");
    		}
    		
    		
    		String invoiceCriteriaStr = invoiceCriteria.toString().trim();
    		log.info(" criteriaStr :" + invoiceCriteriaStr );
    		if( invoiceCriteriaStr.startsWith("and") ) {
    			//remove 'and' 
    			invoiceCriteriaStr = invoiceCriteriaStr.substring(3,invoiceCriteriaStr.length());
    		}
    		
    		
    			
    		
    		//out put XML string 
    		StringBuffer outputQuery = new StringBuffer();
    				
    		outputQuery.append(" return <Output> ");
    		outputQuery.append(" <ID> {data($a/ID)}  </ID> ");
    		outputQuery.append(" <IssueDate> {data($a/IssueDate)}  </IssueDate> ");
    		outputQuery.append(" <TradingPartner>{ data($a/BuyerParty/Party/PartyName/Name) } </TradingPartner> ");
    		outputQuery.append(" <TotalNumOfNDCs> {count(distinct-values($a/InvoiceLine/Item/SellersItemIdentification/ID)) }</TotalNumOfNDCs>");
    		outputQuery.append(" <TotalLineItems>{count($a/InvoiceLine/Item)} </TotalLineItems>" );
    		outputQuery.append(" <Amount> { data( $a/InvoiceLine/Item/BasePrice/PriceAmount)}  </Amount>");
    		outputQuery.append(" {$a/BuyerParty/Party/Address }");
    		outputQuery.append(" </Output>");
    		
    		
    		//construct final query with all part queries
    		StringBuffer finalQuery = new StringBuffer();
    		
//    		if(apnQueryFlag) {
//    			finalQuery.append("for $k in collection('tig:///ePharma/APN')/APN");
//    			finalQuery.append(" where ");
//    			finalQuery.append(apnCriteria.toString());
//    			finalQuery.append(" return ");
//    		} 
    		
    		//constructs invoice query
    		StringBuffer invoicequery = new StringBuffer();
    		invoicequery.append(" for $a in collection('tig:///ePharma/Invoices')/Invoice ");
    		 
    		if(invoiceCriteriaStr.length() > 0) {
    			invoicequery.append( " where " );
    			invoicequery.append( invoiceCriteriaStr ); 
    		}else{
    			invoicequery.append(" where $a/ID = '' ");
    		}
    		
    		//add order part of teh query
    		finalQuery.append(invoicequery.toString());
    		
    		//add output query
    		finalQuery.append(outputQuery.toString());
    		
    		
    		log.info(" Query  for Search Inovoices is : " + finalQuery.toString() );
    		
    		List list = queryRunner.executeQuery(finalQuery.toString());
    		
    		//set the results
    		request.setAttribute("List", list);
    	
    		for(int i=0; i<list.size(); i++){
    			
    			SearchInvoicesForm theForm =new SearchInvoicesForm();
    			Node listNode = XMLUtil.parse((InputStream)list.get(i));
    			
    			
    			theForm.setInvoiceNum(CommonUtil.jspDisplayValue(listNode,"ID"));
    			//theForm.setOrderNum(CommonUtil.jspDisplayValue(listNode,"orderNum"));
    			log.info("Invoice NUmber is:"+theForm.getInvoiceNum());
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
    		  
    		request.setAttribute(Constants.INVOICE_DETAILS,colln);
    		
    		
    	}catch(PersistanceException e){    		
			log.error("Error in ViewMessageAction execute method........." +e);
			throw new PersistanceException(e);
		}
		catch(Exception ex){ 
			ex.printStackTrace();
			log.error("Error in ViewMessageAction execute method........." +ex);
			throw new Exception(ex);
			
		}finally{
    		//close the connection
        	helper.CloseConnectionTL(conn);
    	}
    		return mapping.findForward("success");
   }
   
}
