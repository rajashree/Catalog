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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rdta.Admin.Utility.Helper;


import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import org.w3c.dom.Node;

import com.rdta.catalog.Constants;
import com.rdta.commons.CommonUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

import java.io.*;



public class ShippingManagerAction1 extends Action
{
    private static Log log=LogFactory.getLog(ShippingManagerAction.class);

	private static Map desAdviceXpathMap = new HashMap();
	private static Map orderXpathMap = new HashMap();
	
	private String RefNum = null;
	String clientIP = null;
	Connection conn; 
	Statement stmt;
	 //String HttpServletRequest = "";
	 HttpServletRequest request;
	 HttpSession sess = request.getSession();
	 String sessionID = (String)sess.getAttribute("sessionID");
	 
	private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	static {
		
		desAdviceXpathMap.put("refNumber","$a/DespatchAdvice/ID='");
		desAdviceXpathMap.put("fromDtReceived"," and $a/DespatchAdvice/Delivery/ActualDeliveryDateTime");
		desAdviceXpathMap.put("toDtReceived"," and $a/DespatchAdvice/Delivery/ActualDeliveryDateTime");
		desAdviceXpathMap.put("fromCompany"," and $a/DespatchAdvice/SellerParty/Party/PartyName/Name='");
		desAdviceXpathMap.put("ndc"," and $a/DespatchAdvice/DespatchLine/Item/SellersItemIdentification/ID='");
		
		orderXpathMap.put("refNumber"," and $a/Order/BuyersID='");
		orderXpathMap.put("fromDtReceived"," and $a/Order/Delivery/RequestedDeliveryDateTime");
		orderXpathMap.put("toDtReceived"," and $a/Order/Delivery/RequestedDeliveryDateTime");
		orderXpathMap.put("fromCompany"," and $a/Order/SellerParty/Party/PartyName/Name='");
		orderXpathMap.put("ndc"," and $a/Order/OrderLine/LineItem/Item/SellersItemIdentification/ID='");
		
			
		}
	
	
    public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response)
								 throws Exception {
        Collection colln= new ArrayList();
		log.info("****Inside ShippingManagerAction class...........");
		log.info("Inside ShippingManagerAction execute method..");
    	log.info(" Inside List Action ");
    	
    	try{
		
			StringBuffer buffer5 = new StringBuffer();
			String documentId = request.getParameter("check");
			System.out.println("The APN Doc ID is"+documentId);
			buffer5.append("tlsp:Signature('"+documentId+"')");
			List result4 = queryRunner.returnExecuteQueryStrings(buffer5.toString());
			System.out.println("this query is"+buffer5);
			System.out.println("The Signature is"+result4);
		
  	
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
		
			
		String searchSelect = request.getParameter("searchSelect");
		System.out.println("ooooooooooooooooooooooooooooooooo"+searchSelect);
		
		String trType = searchSelect;
		System.out.println("OOOOOOOOOoooooooooooooooo"+trType);
		
		RefNum = request.getParameter("selectedRow");
		System.out.println("OOOOOOOOORRRRRRRRRRRRRRRRR"+RefNum);
		String buttonvalue = request.getParameter("Submit3");
		System.out.println("OOOOOOOOObbbbbbbbbb Search"+buttonvalue);
		if(buttonvalue == null){
			buttonvalue= request.getParameter("x");
			System.out.println("next Search"+buttonvalue);
			
			
		}
		//starts
		
		  String s5 = "tlsp:validateAccess('"+sessionID+"','2.15','Insert')" ;
		  
			List  accessList2 = queryRunner.returnExecuteQueryStrings(s5);
			 System.out.println("this is the CreateAPN"+accessList2 );
			String s4= accessList2.get(0).toString();
			System.out.println("tsdgfdsgdfgdfg"+s4);
			if(s4.equalsIgnoreCase("false")){
				request.setAttribute("statusc","false");
				
			}else
				sess.setAttribute("statusc","true");
		
		//ends
		
		sess.setAttribute("trType",trType);
		sess.setAttribute("RefNum",RefNum);
		sess.setAttribute("buttonvalue",buttonvalue);
		 

		
		
		//starts
		if(buttonvalue!=null && buttonvalue.equalsIgnoreCase("Search")){
			System.out.println("#############################################ARUN");
			 String s = "tlsp:validateAccess('"+sessionID+"','2.14','Read')" ;
			   
				List  accessLists = queryRunner.returnExecuteQueryStrings(s);
				 System.out.println("aaaaaaaaaadaddd"+accessLists );
				String ss= accessLists.get(0).toString();
				if(ss.equalsIgnoreCase("false")){
					request.setAttribute("statuss","false");
				}else
				{
					request.setAttribute("statuss","true");
			
		List list = null;
		if(searchSelect != null && ss.equals("true")){
			if(searchSelect.trim().equalsIgnoreCase("Order")) {
				//do some thing for search screen...
				log.info(" Order search option ");
				
				String criteriaStr = createXquery(orderXpathMap,request);
				StringBuffer buff = new StringBuffer();
				if(criteriaStr.length() > 0) {
					buff.append("for $a in collection('tig:///"+ Constants.EPHARMA_DB  +"/" + Constants.ORDER_COLL + "')");
					buff.append(" where " + criteriaStr);
					buff.append(" return $a/* ");
				} else {
					buff.append("for $a in collection('tig:///"+ Constants.EPHARMA_DB  +"/" + Constants.ORDER_COLL + "')");
					buff.append(" return $a/* ");
				}
				
				list =  queryRunner.executeQuery(buff.toString());
				
			} else if(searchSelect.trim().equalsIgnoreCase("DespatchAdvice")) { 
				log.info(" DespatchAdvice search option ");
				String criteriaStr = createXquery(desAdviceXpathMap,request);
				
				StringBuffer buff = new StringBuffer();
				if(criteriaStr.length() > 0) {
					buff.append("for $a in collection('tig:///"+ Constants.EPHARMA_DB  +"/" + Constants.DESPATH_ADVICE_COLL + "')");
					buff.append(" where " + criteriaStr);
					buff.append(" return $a/* ");
				} else {
					buff.append("for $a in collection('tig:///"+ Constants.EPHARMA_DB  +"/" + Constants.DESPATH_ADVICE_COLL + "')");
					buff.append(" return $a/* ");
				}
				log.info("\nxxxxxxxxxxxxxxxxxxxxxxxxxx"+buff.toString());
				
				list =  queryRunner.executeQuery(buff.toString());
				
			}
			
		} 
				

		//set the result here
		request.setAttribute("SearchResult",list);
				}
		}
		
		
		if(RefNum==null){
		
				return mapping.findForward("success");
				
    }
		
	   
			else if( RefNum != null){
								
				
			  
		  String status1 = (String)request.getAttribute("statusc");
		  System.out.println("this is from setattribute"+status1);
		    StringBuffer buffer = new StringBuffer(); 
		    System.out.println("this is in action class"+RefNum);
		    String sb=request.getParameter("y");
			   System.out.println("@@@@@@@@@@@@@@@"+sb);
		        if(sb!=null&& sb.equalsIgnoreCase("CreateAPN") ){
		        buffer.append("tlsp:createNewAPNFromDAOnShipment('"+RefNum+"','"+trType+"',0,'') ") ;
		    	log.info("Query*******************"+buffer);
		 List 	list = queryRunner.returnExecuteQueryStrings(buffer.toString());
		    	log.info("REsult&&&&&&&&&&&&&&&&&"+list);
		  
				
		    	
	           if(trType.equalsIgnoreCase("DespatchAdvice")){
	           	   String buff1 = " tlsp:APNDetails('"+RefNum+"') " ;
			    	list = queryRunner.returnExecuteQueryStrings(buff1);
	     		    	log.info("Query************APNNNNNNNNNNNNNNNNNNN*******"+buff1);
	               
	           }	 
		    	else{
		    	
		    	    String buff2 = " tlsp:APNORDetails('"+RefNum+"') " ;
			    	list = queryRunner.returnExecuteQueryStrings(buff2);
			    	log.info("Query************APNNNNNNNNNNNNNNNNNNN*******"+buff2);
		    	}
		            
		 
			    
				
		  
		        for(int j=0; j<list.size(); j++){
		            
					System.out.println("============START FOR LOOP===================");
					ShipingManagerForm theForm = new  ShipingManagerForm();
					Node listNode = XMLUtil.parse((String)list.get(j));
					
					
					theForm.setDataRcvd(CommonUtil.jspDisplayValue(listNode,"DateTime"));
					log.info("dataRcvd is:"+theForm.getDataRcvd());
					theForm.settrNum(CommonUtil.jspDisplayValue(listNode,"TransactionNumber"));
					log.info("orderNum is:"+theForm.gettrNum());
					theForm.setPedigreeNum(CommonUtil.jspDisplayValue(listNode,"DocumentId"));
					log.info("pedigreeNum is:"+theForm.getPedigreeNum());
					theForm.setTrdPrtnr(CommonUtil.jspDisplayValue(listNode,"Name"));
					log.info("product is:"+theForm.getTrdPrtnr());
					theForm.setQuantity(CommonUtil.jspDisplayValue(listNode,"Count"));
					log.info("quantity is:"+theForm.getQuantity());
					theForm.setStatus(CommonUtil.jspDisplayValue(listNode,"Status"));
					log.info("status is:"+theForm.getStatus());
					
					
					colln.add(theForm);
					
					System.out.println("============END FOR LOOP===================");
		       
			  
				request.setAttribute(Constants.Ship_MNGR_DETAILS,colln);
		        }
		     
				}	
			}
				
    	}catch(Exception ex){
    		ex.printStackTrace();
    		log.error("Error in OrderSearchAction execute method........." +ex);
    		return mapping.findForward("exception");
    	}
    	

				return mapping.findForward("success1");
				
				
		}

    
	public String createXquery(Map map, HttpServletRequest request) {
		
		StringBuffer criteria = new StringBuffer();
		String refNumber = request.getParameter("refNumber");
		
		try{
		
		if(refNumber!=null && !refNumber.trim().equalsIgnoreCase("")) {
			criteria.append(map.get("refNumber"));
			criteria.append(refNumber);
			criteria.append("'");
			
		}
		
		String fromCompany = request.getParameter("fromCompany");
		if(fromCompany!=null && !fromCompany.trim().equalsIgnoreCase("")) {
			
			criteria.append(map.get("fromCompany"));
			criteria.append(fromCompany);
			criteria.append("'");
			
		}
		
		String ndc = request.getParameter("ndc");
		if(ndc!=null && !ndc.trim().equalsIgnoreCase("")) {
			criteria.append(map.get("ndc"));
			criteria.append(ndc);
			criteria.append("'");
			
		}
		
		String fromDtReceived = request.getParameter("fromDtReceived");
		String toDtReceived = request.getParameter("toDtReceived");
		if(fromDtReceived!=null && !fromDtReceived.trim().equalsIgnoreCase("")) {
			criteria.append(map.get("fromDtReceived"));
			criteria.append(" >= '");
			criteria.append(fromDtReceived + "T00:00:00");
			criteria.append("'");
			
			if(toDtReceived!=null && !toDtReceived.trim().equalsIgnoreCase("")) 
{
				criteria.append(map.get("toDtReceived"));
				criteria.append(" <= '");
				criteria.append(toDtReceived + "T00:00:00");
				criteria.append("'");
			}
		}
		
		String criteriaStr  = criteria.toString();
		if( criteriaStr.trim().startsWith("and") ) {
			//remove add before that
			criteriaStr = criteriaStr.substring(4,criteriaStr.length());
		}
		
		return  criteriaStr;

		}catch(Exception ex){
    		ex.printStackTrace();
    		log.error("Error in OrderSearchAction execute method........." +ex);
    		return "exception";
		}
		
	}
  
}

