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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.rdta.tlapi.xql.XQLException;

public class ShippingManagerAction extends Action{
	private static Log log=LogFactory.getLog(ShippingManagerAction.class);
	String clientIP = null;
	Connection conn; 
	Statement stmt;
	String flag = "1"; 
	private static Map desAdviceXpathMap = new HashMap();
	private static Map orderXpathMap = new HashMap();
	private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	Helper helper = new Helper();
	private String RefNum = null;
	InsertDocToDB obj = new InsertDocToDB();
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
		 log.info("****Inside ShippingManagerAction class.........");
		 Collection colln= new ArrayList();
		 
		 try{
		
			 Enumeration enum = request.getParameterNames();
			 while ( enum.hasMoreElements()){
				 String strname = (String) enum.nextElement();
				 System.out.println("parameter Name "+strname +"and value ="+request.getParameter(strname));
				 
			 }
			
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

		    String s = "tlsp:validateAccess_MD('"+sessionID+"','2.13','Read')" ;
			List  accessLists = queryRunner.returnExecuteQueryStrings(s);
			 log.info("Cheking the Access Previliges on Shping Manager"+accessLists );
			String ss= accessLists.get(0).toString();
			if(ss.equalsIgnoreCase("false")){
				request.setAttribute("statusace","false");
				return mapping.findForward("failure");
			}else
				request.setAttribute("statusace","true");
			 String sca = "tlsp:validateAccess_MD('"+sessionID+"','2.15','Insert')" ;
				List  accessList2 = queryRunner.returnExecuteQueryStrings(sca);
				String shc= accessList2.get(0).toString();
				if(shc.equalsIgnoreCase("false")){
					request.setAttribute("statusc","false");
				}else
					request.setAttribute("statusc","true");
			String searchSelect = request.getParameter("searchSelect");
			String trType = searchSelect;
			System.out.println("SEarch select value : "+searchSelect);
			
			String buttonname = "";
			buttonname = request.getParameter("Submit3");
			
			StringBuffer buf = new StringBuffer();
			buf.append("for $i in collection('tig:///CatalogManager/TradingPartner')/TradingPartner ");
			buf.append("return data($i/name) ");
			List tpNames = queryRunner.returnExecuteQueryStrings(buf.toString());
			System.out.println("TP Names : "+tpNames.toString());
			sess.setAttribute("tpNames",tpNames);
			
			String sb=request.getParameter("y");
			System.out.println("button name : "+buttonname+ " sb value: "+sb);
			if(sb == null) { sb="";}
			if(sb.equalsIgnoreCase("CreatePedigree")){buttonname ="Search";}
			if(buttonname == null) buttonname = "";
			
			request.setAttribute("buttonValue",buttonname);
			RefNum = request.getParameter("selectedRow");
			log.info("The Transaction No is:"+RefNum);
			String reply = request.getParameter("res");
			if(reply == null || reply.equalsIgnoreCase("false"))reply = "false";
			System.out.println("ref number in action class: "+RefNum+ "reply value: "+reply);
			sess.setAttribute("RefNum",RefNum);
				 String se = "tlsp:validateAccess_MD('"+sessionID+"','2.14','Read')" ;
				 List  accessListse = queryRunner.returnExecuteQueryStrings(se);
				 log.info("Cheking the AcessPreviliges on Search Button:"+accessListse );
				 String sse= accessListse.get(0).toString();
				if(sse.equalsIgnoreCase("false")){
					request.setAttribute("statuss","false");
				}else
					request.setAttribute("statuss","true");
			List list = null;
			
			if(searchSelect != null){
				if(searchSelect.trim().equalsIgnoreCase("Order")) {
					//do some thing for search screen...
					System.out.println(" Order search option ");
					
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
					request.setAttribute("SearchResult",list);	
					
				} else
					if(searchSelect.trim().equalsIgnoreCase("DespatchAdvice") && buttonname.equalsIgnoreCase("Search")) { 
					searchSelect = "DespatchAdvice";
					request.setAttribute("searchSelect",searchSelect);
					log.info(" DespatchAdvice search option ");
					String criteriaStr = createXquery(desAdviceXpathMap,request);
					System.out.println("Query for DA : "+criteriaStr);		
					StringBuffer buff = new StringBuffer();
					if(criteriaStr.length() > 0) {
						buff.append("for $a in collection('tig:///"+ Constants.EPHARMA_DB  +"/" + Constants.DESPATH_ADVICE_COLL + "')");
						buff.append(" where " + criteriaStr);
						buff.append(" return $a/* ");
					} else {
						buff.append("for $a in collection('tig:///"+ Constants.EPHARMA_DB  +"/" + Constants.DESPATH_ADVICE_COLL + "')");
						buff.append(" return $a/* ");
					}
					System.out.println("Query in shipping action :"+buff.toString());
					list =  queryRunner.executeQuery(buff.toString());
					System.out.println("DA Result in action class : "+list);
					request.setAttribute("SearchResult",list);			
					
				}
				//request.setAttribute("SearchResult",list);
			//set the result here
			if(RefNum==null){
				System.out.println("**********Before Success***********");
				return mapping.findForward("success");
			}
			else if( RefNum != null){
						if(shc.equalsIgnoreCase("true")){ 
							//Create Pedigree for ORDERS
							if(searchSelect.equals("Order")){
								StringBuffer buffer = new StringBuffer(); 
								trType = "PurchaseOrder";
						
								if(sb!=null&& sb.equalsIgnoreCase("CreatePedigree") ){
									
								StringBuffer buff = new StringBuffer();
								buff.append("for $i in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope ");
								buff.append("where $i/*:pedigree/*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifier = '"+RefNum+"' ");
								buff.append("return count($i) ");
								List res = queryRunner.returnExecuteQueryStrings(buff.toString());
								if(res.size() >= 1 && reply.equalsIgnoreCase("false")){
									request.setAttribute("Pedenv","true");
									System.out.println("Pedigree already exists");
									return mapping.findForward("success");
								}else{
									StringBuffer buf1 = new StringBuffer();
									buf1.append("for $i in collection('tig:///ePharma_MD/DespatchAdvice')/DespatchAdvice[OrderReference/BuyersID = 'PO45677'] ");
									buf1.append("return data($i/ID) ");
									String transNo = queryRunner.returnExecuteQueryStringsAsString(buf1.toString());
									System.out.println("transNo using PO number : "+transNo.length());
									
								String query = "tlsp:PedigreeBankCheck_MD('"+RefNum+"') ";
								List PBList = queryRunner.returnExecuteQueryStrings(query);
								if(PBList.size() > 0){
									flag = "4";
									String pedID = PBList.get(0).toString();
									//buffer.append("tlsp:CreateShippedPedigreeForOrders('"+transNo+"','"+RefNum+"','PurchaseOrderNumber','"+flag+"','"+pedID+"','"+sessionID+"') ") ;
									obj.CreatePedigree(transNo,RefNum,"PurchaseOrderNumber",flag,pedID,sessionID);
								}else {flag = "1";
								System.out.println("Flag value: "+flag);
						        //buffer.append("tlsp:CreateShippedPedigreeForOrders('"+transNo+"','"+RefNum+"','PurchaseOrderNumber','"+flag+"','','"+sessionID+"')") ;
								obj.CreatePedigree(transNo,RefNum,"PurchaseOrderNumber",flag,"",sessionID);
						       	}
						        log.info("query for create pedigree: "+buffer.toString());
						    	//list = queryRunner.returnExecuteQueryStrings(buffer.toString());
						    	//log.info("The Results are:"+list);
						    	
						    	StringBuffer sb1 = new StringBuffer();
						    	sb1.append("for $i in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope " );
						    	sb1.append("where $i/*:pedigree/*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifier = '"+RefNum+"'  order by $i/date  descending " );
						    	sb1.append("return data($i/*:serialNumber)");
						    	List PElist = queryRunner.returnExecuteQueryStrings(sb1.toString());
						    	String PEId = "";
						    	String PrevPEId = "";
						    	if(PElist != null)
						    	PEId = PElist.get(0).toString();
						    	if(PElist.size() > 1)
						    		PrevPEId = PElist.get(1).toString();
						    	System.out.println("Env ids : "+PEId + "  "+PrevPEId);
						    	 if(reply.equalsIgnoreCase("true")){
						        	
						    		 StringBuffer quer = new StringBuffer();
							    	 quer.append("for $i in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope ");
							    	 quer.append("where $i/*:serialNumber = '"+PrevPEId+"' ");
							    	 quer.append("return data($i/*:pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber ) ");
							       
							         List result = queryRunner.returnExecuteQueryStrings(quer.toString());
							         for(int i=0;i<result.size();i++){
							        	 String SPId = result.get(i).toString();
							        	 System.out.println("REsult for SP :"+SPId);
							        	 System.out.println("result after inserting status :"+queryRunner.returnExecuteQueryStrings("tlsp:InsertAndChangeStatus_MD('"+SPId+"','Cancel','"+sessionID+"') "));
							         }
						        }
						    	/*String xquery = "tlsp:InsertAndChangeStatus('"+PEId+"','Created Unsigned','"+sessionID+"')";
						    	List statuslist = queryRunner.returnExecuteQueryStrings(xquery);*/
						    	 
						    	 StringBuffer quer = new StringBuffer();
						    	 quer.append("for $i in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope ");
						    	 quer.append("where $i/*:serialNumber = '"+PEId+"' ");
						    	 quer.append("return data($i/*:pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber ) ");
						         System.out.println("Query : "+quer.toString());
						         List result = queryRunner.returnExecuteQueryStrings(quer.toString());
						         for(int i=0;i<result.size();i++){
						        	 String SPId = result.get(i).toString();
						        	 System.out.println("REsult for SP :"+SPId);
						        	 System.out.println("result after inserting status :"+queryRunner.returnExecuteQueryStrings("tlsp:InsertAndChangeStatus_MD('"+SPId+"','Created Unsigned','"+sessionID+"') "));
						         }
						  
								if(trType.equalsIgnoreCase("PurchaseOrder")){
					        	   String buff1 = " tlsp:PedigreeDetails_MD('"+RefNum+"') " ;
							       list = queryRunner.returnExecuteQueryStrings(buff1);
					     		   log.info("To view the Pedigree Details:"+buff1);
					     		   System.out.println("Query for Pedigree details: "+buff1);
					           }	 
								 for(int j=0; j<list.size(); j++){
							            
										log.info("============START FOR LOOP===================");
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
										log.info("============END FOR LOOP===================");
							       request.setAttribute(Constants.Ship_MNGR_DETAILS,colln);
							        }
								
								}}
							}else{
							
			//Create Pedigree for DA				
					StringBuffer buffer = new StringBuffer(); 
							
					if(sb!=null&& sb.equalsIgnoreCase("CreatePedigree") ){
						
					StringBuffer buff = new StringBuffer();
					buff.append("for $i in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope ");
					buff.append("where $i/*:pedigree/*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifier = '"+RefNum+"' ");
					buff.append("return count($i) ");
					List res = queryRunner.returnExecuteQueryStrings(buff.toString());
					System.out.println("result for existing pedigrees :"+res.size());
					if(res.size() >= 1 && reply.equalsIgnoreCase("false")){
						request.setAttribute("Pedenv","true");
						System.out.println("Pedigree already exists");
						return mapping.findForward("success");
					}else{
						/*StringBuffer buf1 = new StringBuffer();
						buf1.append("for $i in collection('tig:///ePharma_MD/DespatchAdvice')/DespatchAdvice[OrderReference/BuyersID = 'PO45677'] ");
						buf1.append("return data($i/ID) ");
						String transNo = queryRunner.returnExecuteQueryStringsAsString(buf1.toString());
						System.out.println("transNo using PO number : "+transNo.length());
						if(transNo.length() != 0) RefNum = transNo ;*/
					String query = "tlsp:PedigreeBankCheck_MD('"+RefNum+"') ";
					List PBList = queryRunner.returnExecuteQueryStrings(query);
					if(PBList.size() > 0){
						flag = "4";
						String pedID = PBList.get(0).toString();
						//buffer.append("tlsp:CreateShippedPedigreenew('"+RefNum+"','InvoiceNumber','"+flag+"','"+pedID+"','"+sessionID+"') ") ;
						obj.CreatePedigree(RefNum,"","InvoiceNumber",flag,pedID,sessionID);
					}else {flag = "1";
					log.info("Flag value: "+flag);
					
					obj.CreatePedigree(RefNum,"","InvoiceNumber",flag,"",sessionID);
			        //buffer.append("tlsp:CreateShippedPedigreenew('"+RefNum+"','InvoiceNumber','"+flag+"','','"+sessionID+"')") ;
			       	}
			        //log.info("query for create pedigree:  "+buffer.toString());
			        //System.out.println("query for create pedigree:  "+buffer.toString());
			        //list = queryRunner.returnExecuteQueryStrings(buffer.toString());
			    	//log.info("The Results are:"+list);
			    	
			    	StringBuffer sb1 = new StringBuffer();
			    	sb1.append("for $i in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope " );
			    	sb1.append("where $i/*:pedigree/*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifier = '"+RefNum+"'  order by $i/*:serialNumber  descending " );
			    	sb1.append("return data($i/*:serialNumber)");
			    	List PElist = queryRunner.returnExecuteQueryStrings(sb1.toString());
			    	String PEId = "";
			    	String PrevPEId = "";
			    	PEId = PElist.get(0).toString();
			    	if(PElist.size() > 1)
			    		PrevPEId = PElist.get(1).toString();
			    	
			    	 if(reply.equalsIgnoreCase("true")){
			        	
			    		 StringBuffer quer = new StringBuffer();
				    	 quer.append("for $i in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope ");
				    	 quer.append("where $i/*:serialNumber = '"+PrevPEId+"' ");
				    	 quer.append("return data($i/*:pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber ) ");
				         System.out.println("Query : "+quer.toString());
				         List result = queryRunner.returnExecuteQueryStrings(quer.toString());
				         for(int i=0;i<result.size();i++){
				        	 String SPId = result.get(i).toString();
				        	 System.out.println("REsult for SP :"+SPId);
				        	 System.out.println("result after inserting status :"+queryRunner.returnExecuteQueryStrings("tlsp:InsertAndChangeStatus_MD('"+SPId+"','Cancel','"+sessionID+"') "));
				         }
			        }
			    	/*String xquery = "tlsp:InsertAndChangeStatus('"+PEId+"','Created Unsigned','"+sessionID+"')";
			    	List statuslist = queryRunner.returnExecuteQueryStrings(xquery);*/
			    	 
			    	 StringBuffer quer = new StringBuffer();
			    	 quer.append("for $i in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope ");
			    	 quer.append("where $i/*:serialNumber = '"+PEId+"' ");
			    	 quer.append("return data($i/*:pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber ) ");
			         System.out.println("Query : "+quer.toString());
			         List result = queryRunner.returnExecuteQueryStrings(quer.toString());
			         for(int i=0;i<result.size();i++){
			        	 String SPId = result.get(i).toString();
			        	 System.out.println("REsult for SP :"+SPId);
			        	 System.out.println("result after inserting status :"+queryRunner.returnExecuteQueryStrings("tlsp:InsertAndChangeStatus_MD('"+SPId+"','Created Unsigned','"+sessionID+"') "));
			         }
			  
					if(trType.equalsIgnoreCase("DespatchAdvice")){
		        	   String buff1 = " tlsp:PedigreeDetails_MD('"+RefNum+"') " ;
		        	   System.out.println("Query for pedigreeDetails : "+buff1.toString());
				       list = queryRunner.returnExecuteQueryStrings(buff1);
		     		   log.info("To view the Pedigree Details:"+buff1);
		     		  
		           }	 
			       for(int j=0; j<list.size(); j++){
			            
						log.info("============START FOR LOOP===================");
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
						log.info("============END FOR LOOP===================");
			       request.setAttribute(Constants.Ship_MNGR_DETAILS,colln);
			        }
			       }
					}
				}}
				}
			}
		 }catch(PersistanceException e){
				log.error("Error in ShippingManagerAction execute method........." +e);
				throw new PersistanceException(e);
			}
			catch(Exception ex){			
				ex.printStackTrace();
	    		log.error("Error in ShippingManagerAction execute method........." +ex);
	    		throw new Exception(ex);
			}
					return mapping.findForward("success");
}
 public String createXquery(Map map, HttpServletRequest request) throws Exception{
				
				StringBuffer criteria = new StringBuffer();
				String refNumber = request.getParameter("refNumber").trim();
				try{
				if(refNumber!=null && !refNumber.trim().equalsIgnoreCase("")) {
					criteria.append(map.get("refNumber"));
					criteria.append(refNumber);
					criteria.append("'");
				}
				String fromCompany = request.getParameter("fromCompany").trim();
				if(fromCompany!=null && !fromCompany.trim().equalsIgnoreCase("")) {
					criteria.append(map.get("fromCompany"));
					criteria.append(fromCompany);
					criteria.append("'");
				}
				
				String ndc = request.getParameter("ndc").trim();
				if(ndc!=null && !ndc.trim().equalsIgnoreCase("")) {
					criteria.append(map.get("ndc"));
					criteria.append(ndc);
					criteria.append("'");
					
				}
				
				String fromDtReceived = request.getParameter("fromDtReceived").trim();
				String toDtReceived = request.getParameter("toDtReceived").trim();
				if(fromDtReceived!=null && !fromDtReceived.trim().equalsIgnoreCase("")) {
					criteria.append(map.get("fromDtReceived"));
					criteria.append(" >= '");
					criteria.append(fromDtReceived + "T00:00:00");
					criteria.append("'");
					request.setAttribute("fromDtReceived",fromDtReceived);
					
					if(toDtReceived!=null && !toDtReceived.trim().equalsIgnoreCase("")) 
			          {
						criteria.append(map.get("toDtReceived"));
						criteria.append(" <= '");
						criteria.append(toDtReceived + "T00:00:00");
						criteria.append("'");
						request.setAttribute("toDtReceived",toDtReceived);
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
		    		log.error("Error in ShippingManagerAction execute method........." +ex);
		    		throw new Exception(ex);
				}
			}
	
			}




