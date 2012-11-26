
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

import com.rdta.catalog.Constants;
import com.rdta.commons.CommonUtil;
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.epharma.epedigree.action.ReceivingManagerAction;
public class POsSearchAction extends Action {
	
	private static Log log=LogFactory.getLog(POsSearchAction.class);
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	String servIP = null;
	String clientIP = null;
	String fromDT = null;
	String toDT = null;
	String lotNum = null;
	String prodNDC = null;
	String orderNum = null;
	String dispNum = null;
	String invoiceNum = null;
	String sessionID = null;
	String pagenm = null;
	String tp_company_nm = null;
	String SSCC = null;
	String apndocId = null;
	String tpName = null;
	String screenEnteredDate = null;
	String transactionType = null;
	String PONum = null;

	
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws PersistanceException {
		log.info("Inside Action Action....... ");
		log.info("Inside Action execute of Action....");
		Collection colln= new ArrayList();		
		
		POsSearchForm formbean = (POsSearchForm)form;
		
		servIP = request.getServerName();
		clientIP = request.getRemoteAddr();
		fromDT = request.getParameter("fromDT");
		toDT = request.getParameter("toDT");
		lotNum = request.getParameter("lotNum");
		prodNDC = request.getParameter("prodNDC");
		orderNum = request.getParameter("orderNum");
		dispNum = request.getParameter("poNum");
		invoiceNum = request.getParameter("invoiceNum");
		//sessionID = request.getParameter("sessionID");
		pagenm = request.getParameter("pagenm");
		tp_company_nm = request.getParameter("tp_company_nm");
		SSCC = request.getParameter("SSCC");
		apndocId = request.getParameter("apndocId");
		tpName = request.getParameter("tpName");
		PONum = formbean.getPONum();
		System.out.println("PO number:"+PONum);
		
		HttpSession sess = request.getSession();
		sessionID = (String)sess.getAttribute("sessionID");
		System.out.println("sess in receiving action:"+sessionID);
		if(orderNum == null) { orderNum = "";}
		if(dispNum == null) { dispNum = "";}
		if(invoiceNum == null) { invoiceNum = "";}
		if(lotNum == null) { lotNum = "";}
		if(prodNDC == null) { prodNDC = "";}
		if(SSCC == null) { SSCC = "";}
		if(fromDT == null) { fromDT = "";}
		if(toDT == null) { toDT = "";}
		if(pagenm == null) { pagenm = "";}
		if(tp_company_nm == null) { tp_company_nm = "";}
		if(apndocId == null) { apndocId = "";}
		if(tpName == null) { tpName = "";}
		if(transactionType == null) { transactionType = ""; }
		
		SimpleDateFormat df = new SimpleDateFormat();
		df.applyPattern("yyyy-MM-dd");
		
		if(fromDT.equals("")) {
			screenEnteredDate = df.format(new java.util.Date());
		} else {
			screenEnteredDate = fromDT+" to "+toDT; 
		}
		String idDate = df.format(new java.util.Date())+"T00:00:00";
		request.setAttribute("screenEnteredDate", screenEnteredDate);
		
		
		// code to search 
		
		//IF NOT SEARCHING BY order, po or invoice etc
		log.info("Querying for From Date to To Date.......");
		StringBuffer buff = new StringBuffer();
		StringBuffer buff1 = new StringBuffer();
		
		buff1.append("for $i in collection('tig:///ePharma/APN')/APN ");
		buff1.append("return <Name>{data($i/From/Name)}</Name>");
		
		List tpNames = queryRunner.executeQuery(buff1.toString());	
		log.info("Query for list of trading partner names in POsSearchAction contains object:"+tpNames);
		request.setAttribute("TPNames", tpNames);
// Changed from here		
	/*	for(int i=0; i<tpNames.size(); i++){			
			ReceivingManagerForm theForm = new  ReceivingManagerForm();
			Node listNode = XMLUtil.parse((InputStream)tpNames.get(i));
			
			//theForm.setTpNames(CommonUtil.jspDisplayValue(listNode,"Name"));
			colln.add(theForm);
		} */
		
// till here		
		if(orderNum.equals("") && dispNum.equals("") && invoiceNum.equals("") && lotNum.equals("") && prodNDC.equals("") && SSCC.equals("") && apndocId.equals("") && tpName.equals("")) {
			
			buff.append("for $b in collection('tig:///ePharma/APN')/APN");
			if(fromDT.equals("")) {
				buff.append(" where $b/DateTime >= '"+idDate+"' order by $b/From/Name ");
			} else {
				buff.append( " where $b/DateTime >= '"+fromDT+"T00:00:00' and $b/DateTime <= '"+toDT+"T00:00:00' order by $b/DateTime, $b/From/Name ");
			}
			
			log.info("Querying over for From Date - To Date !!!!");	
			
		}else {
			if(!lotNum.equals("")) { //by Lot Number
				buff.append("for $b in collection('tig:///ePharma/APN')/APN ");
				buff.append(" where $b/Pedigrees/Pedigree/Products/Product/LotNumber = '"+lotNum+"' order by $b/From/Name ");
			}else{
				if(!orderNum.equals("")) { //by ORDER
					buff.append("for $b in collection('tig:///ePharma/Orders')/Order, $b in collection('tig:///ePharma/APN')/APN ");
					buff.append(" where $b/To/TransactionNumber = $o/BuyersID and $o/BuyersID = '"+orderNum+"' order by $b/DateTime, $b/From/Name ");
				}else{
					if(!dispNum.equals("")) { //by DISPATCH ADVISE
						buff.append("for $o in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice, $b in collection('tig:///ePharma/APN')/APN ");
						buff.append(" where $b/To/TransactionNumber = $o/OrderReference/BuyersID and $o/OrderReference/BuyersID = '"+dispNum+"' order by $b/DateTime, $b/From/Name ");
					}else {
						if(!SSCC.equals("")) { //by SSCC
							buff.append("for $b in collection('tig:///ePharma/APN')/APN ");
							buff.append(" where $b/Pedigrees/Pedigree/Products/Product/ParentEPC = '"+SSCC+"' order by $b/DateTime, $b/From/Name ");
						}else { //INVOICE
							if(!invoiceNum.equals("")){
								buff.append("for $o in collection('tig:///ePharma/Invoices')/Invoice, $b in collection('tig:///ePharma/APN')/APN ");
								buff.append(" where $b/To/TransactionNumber = $o/OrderReference/BuyersID and $o/OrderReference/BuyersID = '"+invoiceNum+"' order by $b/DateTime, $b/From/Name ");
							}else { //INVOICE
								if(!prodNDC.equals("")){
									buff.append("for $b in collection('tig:///ePharma/APN')/APN");
									buff.append(" where $b/Pedigrees/Pedigree/Products/Product/NDC = '"+prodNDC+"' order by $b/DateTime, $b/From/Name ");		
								}else { //INVOICE
									if(!tpName.equals("")){
										buff.append("for $b in collection('tig:///ePharma/APN')/APN");
										buff.append(" where $b/From/Name = '"+tpName+"' order by $b/DateTime, $b/From/Name ");		
									}else { //INVOICE
										if(!apndocId.equals("")){
											buff.append("for $b in collection('tig:///ePharma/APN')/APN");
											buff.append(" where $b/DocumentId = '"+apndocId+"' order by $b/DateTime, $b/From/Name ");		
										}
									}	
								}	
							}
						}	
					}
				}		
			}
		}	
		
		buff.append(" return ");
		buff.append("<output>{ ");
		buff.append("<DocumentId>{data($b/DocumentId)}</DocumentId>, ");
		buff.append("<TransactionNumber>{data($b/To/TransactionNumber)}</TransactionNumber>, ");
		buff.append("<TransactionType>{data($b/To/TransactionType)}</TransactionType>, ");
		buff.append("<DateTime>{data($b/DateTime)}</DateTime>, ");
		buff.append("<Name>{data($b/From/Name)}</Name>, ");
		buff.append("<LegendDrugName>{data($b/Pedigrees/Pedigree/Products/Product/LegendDrugName)}</LegendDrugName>, ");
		buff.append("<Count>{count($b/Pedigrees/Pedigree/Products/Product)}</Count>, ");
		buff.append("<Status>{data($b/Pedigrees/Pedigree/PedigreeStatus/Status/Status)}</Status> ");
		buff.append("}</output>");
		
		
		List list = queryRunner.executeQuery(buff.toString());
		log.info("After running the query in POsSearchAction");
		log.info("The query contains the List object value:"+list.toString());
		
		request.setAttribute("List", list);
		
		for(int i=0; i<list.size(); i++){
			
			POsSearchForm theForm = new  POsSearchForm();
			Node listNode = XMLUtil.parse((InputStream)list.get(i));
			
			/*theForm.setCreatedBy(CommonUtil.jspDisplayValue(listNode,"createdBy"));
			log.info("createdBy is:"+theForm.getCreatedBy());*/
			theForm.setDataRcvd(CommonUtil.jspDisplayValue(listNode,"DateTime"));
			log.info("dataRcvd is:"+theForm.getDataRcvd());
			theForm.setOrderNum(CommonUtil.jspDisplayValue(listNode,"TransactionNumber"));
			log.info("orderNum is:"+theForm.getOrderNum());
			theForm.setPedigreeNum(CommonUtil.jspDisplayValue(listNode,"DocumentId	"));
			log.info("pedigreeNum is:"+theForm.getPedigreeNum());
			theForm.setProduct(CommonUtil.jspDisplayValue(listNode,"LegendDrugName"));
			log.info("product is:"+theForm.getProduct());
			theForm.setQuantity(CommonUtil.jspDisplayValue(listNode,"Count"));
			log.info("quantity is:"+theForm.getQuantity());
			//theForm.setSelect(CommonUtil.jspDisplayValue(listNode,"select"));
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
		
		
		log.info("Before returning to success");
		request.setAttribute(Constants.RCVNG_MNGR_DETAILS,colln);
		
		
												
		return mapping.findForward("success");
	}
}			
