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

 
//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_4.1.0/xslt/JavaClass.xsl

package com.rdta.epharma.epedigree.action.print;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.w3c.dom.Node;

import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;

/** 
 * MyEclipse Struts
 * Creation date: 04-28-2006
 * 
 * XDoclet definition:
 * @struts.action path="/pedigree2129" name="Pedigree2129Form" scope="request" validate="true"
 * @struts.action-forward name="success" path="/html/pedigree2129Form.jsp"
 */
public class Pedigree2129Action extends Action {

	static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	static final Log log = LogFactory.getLog(Pedigree2129Action.class);
	// --------------------------------------------------------- Instance Variables

	// --------------------------------------------------------- Methods

	/** 
	 * Method execute
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) throws Exception {
		//Unknown Pedigree2129Form = (Unknown) form;
		log.info("Inside method execute");
		
		Pedigree2129Form theForm = (Pedigree2129Form)form;
		
		String pedigreeID = request.getParameter("pedigreeID");
		try {
			log.info("Querytlsp:getRepackagedInfo('"+pedigreeID+"')");
			String str=queryRunner.returnExecuteQueryStringsAsString("tlsp:getRepackagedInfo('"+pedigreeID+"')");
			log.info("QueryResult"+str);
			
			Node n1=XMLUtil.parse(str);
			Node n2=XMLUtil.getNode(n1,"/shippedPedigree");
			
			theForm.setBusinessName(XMLUtil.getValue(n2,"transactionInfo/senderInfo/businessAddress/businessName"));
			
			Node n3 = XMLUtil.getNode(n2,"descendant::initialPedigree");
			
			theForm.setDrugName(XMLUtil.getValue(n3,"productInfo/drugName"));
			//System.out.println("DrugName***"+theForm.getDrugName());
			theForm.setStrength(XMLUtil.getValue(n3,"productInfo/strength"));
			theForm.setDosageForm(XMLUtil.getValue(n3,"productInfo/dosageForm"));
			theForm.setContainerSize(XMLUtil.getValue(n3,"productInfo/containerSize"));
			
			//theForm.setManufacturer(XMLUtil.getValue(n2,"descendant::*/initialPedigree/productInfo/manufacturer"));
			theForm.setProductCode(XMLUtil.getValue(n3,"productInfo/productCode"));
			
			
			List ls= new ArrayList();
			Iterator it=XMLUtil.executeQuery(n2,"itemInfo").iterator();
			//Iterator it=XMLUtil.executeQuery(n3,"itemInfo").iterator();
			while(it.hasNext()){
				
				Node n4= (Node)it.next();
				
				ItemInfo itemObj = new ItemInfo();
				itemObj.setLot(XMLUtil.getValue(n4,"lot"));
				itemObj.setQuantity(XMLUtil.getValue(n4,"quantity"));
				itemObj.setSerialNumber(XMLUtil.getValue(n4,"itemSerialNumber"));
				
				
				
				ls.add(itemObj);
			}
			
			//adding new code here...
			
			if(ls.size()==0){
				Node shippedNode = XMLUtil.getNode(n2,"child::*/child::shippedPedigree");
				
				while(true){
					
					
					if(shippedNode!=null){
						Iterator itemIterator = XMLUtil.executeQuery(shippedNode,"itemInfo").iterator();
						//Iterator it=XMLUtil.executeQuery(n3,"itemInfo").iterator();
						while(it.hasNext()){
							
							Node itemNode= (Node)itemIterator.next();
							
							ItemInfo itemObj = new ItemInfo();
							itemObj.setLot(XMLUtil.getValue(itemNode,"lot"));
							itemObj.setQuantity(XMLUtil.getValue(itemNode,"quantity"));
							itemObj.setSerialNumber(XMLUtil.getValue(itemNode,"itemSerialNumber"));
							
							
							
							ls.add(itemObj);
						}
						shippedNode = XMLUtil.getNode(shippedNode,"child::*/child::shippedPedigree");
					}
					
					if(ls.size()>0 || shippedNode == null)break;
				}
				
				if(ls.size()==0){
					
					
					Iterator initialItemInfoIt=XMLUtil.executeQuery(n3,"itemInfo").iterator();
					//Iterator it=XMLUtil.executeQuery(n3,"itemInfo").iterator();
					while(initialItemInfoIt.hasNext()){
						
						Node n4= (Node)initialItemInfoIt.next();
						
						ItemInfo itemObj = new ItemInfo();
						itemObj.setLot(XMLUtil.getValue(n4,"lot"));
						itemObj.setQuantity(XMLUtil.getValue(n4,"quantity"));
						itemObj.setSerialNumber(XMLUtil.getValue(n4,"itemSerialNumber"));
						
						
						
						ls.add(itemObj);
					}
					
					
				}
			}
			
			
			
			
			theForm.setItemInfoList(ls);
			
			
			theForm.setTransactionDate(getDate(XMLUtil.getValue(n2,"transactionInfo/transactionDate")));
			theForm.setIdentifier(XMLUtil.getValue(n2,"transactionInfo/transactionIdentifier/identifier"));
			theForm.setIdentifierType(XMLUtil.getValue(n2,"transactionInfo/transactionIdentifier/identifierType"));
			
			
			theForm.setManufacturerName(XMLUtil.getValue(n3,"productInfo/manufacturer"));
			theForm.setManufacturerContactName(XMLUtil.getValue(n3,"productInfo/manufacturer"));
			
			List custodyInfoList= new ArrayList();
			CustodyInfo info;
			String serialNumber="";
			
			System.out.println("**********n3 value\n"+n3);
			
			Node n5 = XMLUtil.getNode(n3,"descendant::transactionInfo");
			System.out.println("**********n5 value\n"+n5);
			
			if(n3!=null && n5 != null){
				
				info = new CustodyInfo();
				boolean flag = compare(n3);
				//Setting the business Address
				info.setBusinessName(XMLUtil.getValue(n3,"transactionInfo/senderInfo/businessAddress/businessName"));
				
				info.setBusinessAddressStreet1(XMLUtil.getValue(n3,"transactionInfo/senderInfo/businessAddress/street1"));
				info.setBusinessAddressStreet2(XMLUtil.getValue(n3,"transactionInfo/senderInfo/businessAddress/street2"));
				info.setBusinessAddressCity(XMLUtil.getValue(n3,"transactionInfo/senderInfo/businessAddress/city"));
				info.setBusinessAddressStateOrRegion(XMLUtil.getValue(n3,"transactionInfo/senderInfo/businessAddress/stateOrRegion"));
				
				info.setBusinessAddressPostalCode(XMLUtil.getValue(n3,"transactionInfo/senderInfo/businessAddress/postalCode"));
				info.setBusinessAddressCountry(XMLUtil.getValue(n3,"transactionInfo/senderInfo/businessAddress/country"));
				
				
				info.setIdentifier(XMLUtil.getValue(n3,"transactionInfo/transactionIdentifier/identifier"));
				info.setIdentifierType(XMLUtil.getValue(n3,"transactionInfo/transactionIdentifier/identifierType"));
				info.setTransactionDate(getDate(XMLUtil.getValue(n3,"transactionInfo/transactionDate")));
				
				info.setTransactionName(XMLUtil.getValue(n3,"transactionInfo/senderInfo/contactInfo/name"));
				
				//making changes here for getting signatureInfo of recipient and authenticator
				
				info.setAuthenticatorName(XMLUtil.getValue(n3,"transactionInfo/senderInfo/contactInfo/name"));
				info.setRecipientName(XMLUtil.getValue(n3,"transactionInfo/recipientInfo/contactInfo/name"));
				
				info.setName(XMLUtil.getValue(n3,"transactionInfo/senderInfo/contactInfo/name"));
				info.setTelphone(XMLUtil.getValue(n3,"transactionInfo/senderInfo/contactInfo/telephone"));
				info.setEmail(XMLUtil.getValue(n3,"transactionInfo/senderInfo/contactInfo/email"));
				
				//Setting the Shipping Address
				if(!flag){
				
					info.setShippingBusinessName(XMLUtil.getValue(n3,"transactionInfo/senderInfo/shippingAddress/businessName"));
				
					info.setShippingBusinessAddressStreet1(XMLUtil.getValue(n3,"transactionInfo/senderInfo/shippingAddress/street1"));
					info.setShippingBusinessAddressStreet2(XMLUtil.getValue(n3,"transactionInfo/senderInfo/shippingAddress/street2"));
					info.setShippingBusinessAddressCity(XMLUtil.getValue(n3,"transactionInfo/senderInfo/shippingAddress/city"));
					info.setShippingBusinessAddressStateOrRegion(XMLUtil.getValue(n3,"transactionInfo/senderInfo/shippingAddress/stateOrRegion"));
				
					info.setShippingBusinessAddressPostalCode(XMLUtil.getValue(n3,"transactionInfo/senderInfo/shippingAddress/postalCode"));
					info.setShippingBusinessAddressCountry(XMLUtil.getValue(n3,"transactionInfo/senderInfo/shippingAddress/country"));
				
				
					info.setShippingIdentifier(XMLUtil.getValue(n3,"transactionInfo/transactionIdentifier/identifier"));
					info.setShippingIdentifierType(XMLUtil.getValue(n3,"transactionInfo/transactionIdentifier/identifierType"));
					info.setShippingTransactionDate(getDate(XMLUtil.getValue(n3,"transactionInfo/transactionDate")));
				
					info.setShippingTransactionName(XMLUtil.getValue(n3,"transactionInfo/senderInfo/contactInfo/name"));
				
					info.setShippingAuthenticatorName(XMLUtil.getValue(n3,"transactionInfo/senderInfo/contactInfo/name"));
					info.setShippingRecipientName(XMLUtil.getValue(n3,"transactionInfo/recipientInfo/contactInfo/name"));
					
					
					info.setShippingName(XMLUtil.getValue(n3,"transactionInfo/senderInfo/contactInfo/name"));
					info.setShippingTelphone(XMLUtil.getValue(n3,"transactionInfo/senderInfo/contactInfo/telephone"));
					info.setShippingEmail(XMLUtil.getValue(n3,"transactionInfo/senderInfo/contactInfo/email"));
				}
				custodyInfoList.add(info);
			}
			
			
			
			
			Node intialShipped = XMLUtil.getNode(n2,"descendant-or-self::*[initialPedigree]");
			
			
			if(intialShipped!=null){
				
				boolean flag = compare(intialShipped);
				
				serialNumber =  XMLUtil.getValue(intialShipped,"documentInfo/serialNumber");
				info = new CustodyInfo();
				System.out.println("Serial NO:::"+serialNumber);
				
				info.setPedigreeID(serialNumber);
				//Setting the business Address
				info.setBusinessName(XMLUtil.getValue(intialShipped,"transactionInfo/senderInfo/businessAddress/businessName"));
				
				info.setBusinessAddressStreet1(XMLUtil.getValue(intialShipped,"transactionInfo/senderInfo/businessAddress/street1"));
				info.setBusinessAddressStreet2(XMLUtil.getValue(intialShipped,"transactionInfo/senderInfo/businessAddress/street2"));
				info.setBusinessAddressCity(XMLUtil.getValue(intialShipped,"transactionInfo/senderInfo/businessAddress/city"));
				info.setBusinessAddressStateOrRegion(XMLUtil.getValue(intialShipped,"transactionInfo/senderInfo/businessAddress/stateOrRegion"));
				
				info.setBusinessAddressPostalCode(XMLUtil.getValue(intialShipped,"transactionInfo/senderInfo/businessAddress/postalCode"));
				info.setBusinessAddressCountry(XMLUtil.getValue(intialShipped,"transactionInfo/senderInfo/businessAddress/country"));
				
				
				/*info.setIdentifier(XMLUtil.getValue(n3,"transactionInfo/transactionIdentifier/identifier"));
				info.setIdentifierType(XMLUtil.getValue(n3,"transactionInfo/transactionIdentifier/identifierType"));
				info.setTransactionDate(getDate(XMLUtil.getValue(n3,"transactionInfo/transactionDate")));
				*/
				
				info.setIdentifier(XMLUtil.getValue(intialShipped,"transactionInfo/transactionIdentifier/identifier"));
				info.setIdentifierType(XMLUtil.getValue(intialShipped,"transactionInfo/transactionIdentifier/identifierType"));
				info.setTransactionDate(getDate(XMLUtil.getValue(intialShipped,"transactionInfo/transactionDate")));
				
				info.setTransactionName(XMLUtil.getValue(intialShipped,"transactionInfo/senderInfo/contactInfo/name"));
				
				//making changes here for getting signatureInfo of recipient and authenticator
				
				info.setAuthenticatorName(XMLUtil.getValue(intialShipped,"signatureInfo/signerInfo/name"));
				info.setRecipientName(XMLUtil.getValue(intialShipped,"transactionInfo/recipientInfo/contactInfo/name"));
				
				
				info.setName(XMLUtil.getValue(intialShipped,"transactionInfo/senderInfo/contactInfo/name"));
				info.setTelphone(XMLUtil.getValue(intialShipped,"transactionInfo/senderInfo/contactInfo/telephone"));
				info.setEmail(XMLUtil.getValue(intialShipped,"transactionInfo/senderInfo/contactInfo/email"));
				
				//Setting the Shipping Address
				
				if(!flag){
					info.setShippingBusinessName(XMLUtil.getValue(intialShipped,"transactionInfo/senderInfo/shippingAddress/businessName"));
				
					info.setShippingBusinessAddressStreet1(XMLUtil.getValue(intialShipped,"transactionInfo/senderInfo/shippingAddress/street1"));
					info.setShippingBusinessAddressStreet2(XMLUtil.getValue(intialShipped,"transactionInfo/senderInfo/shippingAddress/street2"));
					info.setShippingBusinessAddressCity(XMLUtil.getValue(intialShipped,"transactionInfo/senderInfo/shippingAddress/city"));
					info.setShippingBusinessAddressStateOrRegion(XMLUtil.getValue(intialShipped,"transactionInfo/senderInfo/shippingAddress/stateOrRegion"));
				
					info.setShippingBusinessAddressPostalCode(XMLUtil.getValue(intialShipped,"transactionInfo/senderInfo/shippingAddress/postalCode"));
					info.setShippingBusinessAddressCountry(XMLUtil.getValue(intialShipped,"transactionInfo/senderInfo/shippingAddress/country"));
				
				
					/*info.setShippingIdentifier(XMLUtil.getValue(n3,"transactionInfo/transactionIdentifier/identifier"));
					info.setShippingIdentifierType(XMLUtil.getValue(n3,"transactionInfo/transactionIdentifier/identifierType"));
					info.setShippingTransactionDate(getDate(XMLUtil.getValue(n3,"transactionInfo/transactionDate")));
				*/
					
					info.setShippingIdentifier(XMLUtil.getValue(intialShipped,"transactionInfo/transactionIdentifier/identifier"));
					info.setShippingIdentifierType(XMLUtil.getValue(intialShipped,"transactionInfo/transactionIdentifier/identifierType"));
					info.setShippingTransactionDate(getDate(XMLUtil.getValue(intialShipped,"transactionInfo/transactionDate")));
					
					info.setShippingTransactionName(XMLUtil.getValue(intialShipped,"transactionInfo/senderInfo/contactInfo/name"));
				
					info.setShippingAuthenticatorName(XMLUtil.getValue(intialShipped,"signatureInfo/signerInfo/name"));
					info.setShippingRecipientName(XMLUtil.getValue(intialShipped,"transactionInfo/recipientInfo/contactInfo/name"));
					
				
					info.setShippingName(XMLUtil.getValue(intialShipped,"transactionInfo/senderInfo/contactInfo/name"));
					info.setShippingTelphone(XMLUtil.getValue(intialShipped,"transactionInfo/senderInfo/contactInfo/telephone"));
					info.setShippingEmail(XMLUtil.getValue(intialShipped,"transactionInfo/senderInfo/contactInfo/email"));
				}
				custodyInfoList.add(info);
			}
			if(!(pedigreeID.equals(serialNumber))){
				
				Node shippedNode = XMLUtil.getNode(intialShipped,"ancestor::shippedPedigree");
				Node testNode = intialShipped;
				
				
			while(true){
				
				//Node shippedNode = XMLUtil.getNode(n2,".[descendant-or-self::*/shippedPedigree/documentInfo/serialNumber='"+serialNumber+"']");
				
				if(shippedNode!=null){
					boolean flag = compare(shippedNode);
					serialNumber =  XMLUtil.getValue(shippedNode,"documentInfo/serialNumber");
					System.out.println("Serial NO:::"+serialNumber);
					info = new CustodyInfo();
					
					info.setPedigreeID(serialNumber);
					//Setting the business Address
					info.setBusinessName(XMLUtil.getValue(shippedNode,"transactionInfo/senderInfo/businessAddress/businessName"));
					
					info.setBusinessAddressStreet1(XMLUtil.getValue(shippedNode,"transactionInfo/senderInfo/businessAddress/street1"));
					info.setBusinessAddressStreet2(XMLUtil.getValue(shippedNode,"transactionInfo/senderInfo/businessAddress/street2"));
					info.setBusinessAddressCity(XMLUtil.getValue(shippedNode,"transactionInfo/senderInfo/businessAddress/city"));
					info.setBusinessAddressStateOrRegion(XMLUtil.getValue(shippedNode,"transactionInfo/senderInfo/businessAddress/stateOrRegion"));
					
					info.setBusinessAddressPostalCode(XMLUtil.getValue(shippedNode,"transactionInfo/senderInfo/businessAddress/postalCode"));
					info.setBusinessAddressCountry(XMLUtil.getValue(shippedNode,"transactionInfo/senderInfo/businessAddress/country"));
					
					
					/*info.setIdentifier(XMLUtil.getValue(testNode,"transactionInfo/transactionIdentifier/identifier"));
					info.setIdentifierType(XMLUtil.getValue(testNode,"transactionInfo/transactionIdentifier/identifierType"));
					info.setTransactionDate(getDate(XMLUtil.getValue(testNode,"transactionInfo/transactionDate")));
					*/
					
					
					info.setIdentifier(XMLUtil.getValue(shippedNode,"transactionInfo/transactionIdentifier/identifier"));
					info.setIdentifierType(XMLUtil.getValue(shippedNode,"transactionInfo/transactionIdentifier/identifierType"));
					info.setTransactionDate(getDate(XMLUtil.getValue(shippedNode,"transactionInfo/transactionDate")));
					
					
					info.setTransactionName(XMLUtil.getValue(shippedNode,"transactionInfo/senderInfo/contactInfo/name"));
					
//					making changes here for getting signatureInfo of recipient and authenticator
					
					info.setAuthenticatorName(XMLUtil.getValue(shippedNode,"signatureInfo/signerInfo/name"));
					info.setRecipientName(XMLUtil.getValue(shippedNode,"transactionInfo/recipientInfo/contactInfo/name"));
					
					
					info.setName(XMLUtil.getValue(shippedNode,"transactionInfo/senderInfo/contactInfo/name"));
					info.setTelphone(XMLUtil.getValue(shippedNode,"transactionInfo/senderInfo/contactInfo/telephone"));
					info.setEmail(XMLUtil.getValue(shippedNode,"transactionInfo/senderInfo/contactInfo/email"));
					
					//Setting the Shipping Address
					
					if(!flag){
						info.setShippingBusinessName(XMLUtil.getValue(shippedNode,"transactionInfo/senderInfo/shippingAddress/businessName"));
					
						info.setShippingBusinessAddressStreet1(XMLUtil.getValue(shippedNode,"transactionInfo/senderInfo/shippingAddress/street1"));
						info.setShippingBusinessAddressStreet2(XMLUtil.getValue(shippedNode,"transactionInfo/senderInfo/shippingAddress/street2"));
						info.setShippingBusinessAddressCity(XMLUtil.getValue(shippedNode,"transactionInfo/senderInfo/shippingAddress/city"));
						info.setShippingBusinessAddressStateOrRegion(XMLUtil.getValue(shippedNode,"transactionInfo/senderInfo/shippingAddress/stateOrRegion"));
					
						info.setShippingBusinessAddressPostalCode(XMLUtil.getValue(shippedNode,"transactionInfo/senderInfo/shippingAddress/postalCode"));
						info.setShippingBusinessAddressCountry(XMLUtil.getValue(shippedNode,"transactionInfo/senderInfo/shippingAddress/country"));
					
					
						/*info.setShippingIdentifier(XMLUtil.getValue(testNode,"transactionInfo/transactionIdentifier/identifier"));
						info.setShippingIdentifierType(XMLUtil.getValue(testNode,"transactionInfo/transactionIdentifier/identifierType"));
						info.setShippingTransactionDate(getDate(XMLUtil.getValue(testNode,"transactionInfo/transactionDate")));
						*/
						info.setShippingIdentifier(XMLUtil.getValue(shippedNode,"transactionInfo/transactionIdentifier/identifier"));
						info.setShippingIdentifierType(XMLUtil.getValue(shippedNode,"transactionInfo/transactionIdentifier/identifierType"));
						info.setShippingTransactionDate(getDate(XMLUtil.getValue(shippedNode,"transactionInfo/transactionDate")));
					
						
						info.setShippingTransactionName(XMLUtil.getValue(shippedNode,"transactionInfo/senderInfo/contactInfo/name"));
					
						info.setShippingAuthenticatorName(XMLUtil.getValue(shippedNode,"signatureInfo/signerInfo/name"));
						info.setShippingRecipientName(XMLUtil.getValue(shippedNode,"transactionInfo/recipientInfo/contactInfo/name"));
						
						
						info.setShippingName(XMLUtil.getValue(shippedNode,"transactionInfo/senderInfo/contactInfo/name"));
						info.setShippingTelphone(XMLUtil.getValue(shippedNode,"transactionInfo/senderInfo/contactInfo/telephone"));
						info.setShippingEmail(XMLUtil.getValue(shippedNode,"transactionInfo/senderInfo/contactInfo/email"));
					}
					custodyInfoList.add(info);
					testNode = shippedNode;
					shippedNode = XMLUtil.getNode(shippedNode,"ancestor::shippedPedigree");
				}
				
				if(pedigreeID.equals(serialNumber) || shippedNode==null)break;
			}
			}	
			
			theForm.setCustodyInfoList(custodyInfoList);
			System.out.println("Size of List is*********************\n\n\n\n"+custodyInfoList.size());
			
			theForm.setSignerName(XMLUtil.getValue(n2,"signatureInfo/signerInfo/name"));
			theForm.setSignerTitle(XMLUtil.getValue(n2,"signatureInfo/signerInfo/title"));
			String signEmail=XMLUtil.getValue(n2,"signatureInfo/signerInfo/email");
			//theForm.setManufacturer(XMLUtil.getValue(n2,"descendant::*/initialPedigree/productInfo/manufacturer"));
			
			StringBuffer bfr = new StringBuffer();
			bfr.append("let $userId := (for $i in collection('tig:///EAGRFID/SysUsers')");
			bfr.append(" where $i/User/FirstName='"+theForm.getSignerName()+"'and $i/User/Email='"+signEmail+"'");
			bfr.append(" return data($i/User/UserID))");
			bfr.append("for $k in collection('tig:///EAGRFID/UserSign')/User");
			bfr.append(" where $k/UserID = $userId");
			bfr.append(" return data($k/UserID)");
			log.info("Query :"+bfr.toString());
			String SignId=queryRunner.returnExecuteQueryStringsAsString(bfr.toString()); 
			log.info("Query Result :"+SignId);
			theForm.setSignatureId(SignId);
			
			
			
			/*theForm.setInitialBusinessName(XMLUtil.getValue(n2,"transactionInfo/recipientInfo/businessAddress/businessName"));
			
			theForm.setInitialBusinessAddressStreet1(XMLUtil.getValue(n2,"transactionInfo/recipientInfo/businessAddress/street1"));
			theForm.setInitialBusinessAddressStreet2(XMLUtil.getValue(n2,"transactionInfo/recipientInfo/businessAddress/street2"));
			theForm.setInitialBusinessAddressCity(XMLUtil.getValue(n2,"transactionInfo/recipientInfo/businessAddress/city"));
			theForm.setInitialBusinessAddressStateOrRegion(XMLUtil.getValue(n2,"transactionInfo/recipientInfo/businessAddress/stateOrRegion"));
			
			theForm.setInitialBusinessAddressPostalCode(XMLUtil.getValue(n2,"transactionInfo/recipientInfo/businessAddress/postalCode"));
			theForm.setInitialBusinessAddressCountry(XMLUtil.getValue(n2,"transactionInfo/recipientInfo/businessAddress/country"));
			
			
			theForm.setInitialIdentifier(XMLUtil.getValue(n2,"transactionInfo/transactionIdentifier/identifier"));
			theForm.setInitialIdentifierType(XMLUtil.getValue(n2,"transactionInfo/transactionIdentifier/identifierType"));
			theForm.setInitialTransactionDate(XMLUtil.getValue(n2,"transactionInfo/transactionDate"));
			
			theForm.setInitialTransactionName(XMLUtil.getValue(n2,"transactionInfo/recipientInfo/contactInfo/name"));
			
			
			theForm.setInitialName(XMLUtil.getValue(n2,"transactionInfo/recipientInfo/contactInfo/name"));
			theForm.setInitialTelphone(XMLUtil.getValue(n2,"transactionInfo/recipientInfo/contactInfo/telephone"));
			theForm.setInitialEmail(XMLUtil.getValue(n2,"transactionInfo/recipientInfo/contactInfo/email"));
			*/
			
			if(n3!=null){
				Node altPedigree = XMLUtil.getNode(n3,"altPedigree");
				if(altPedigree!=null){
					request.setAttribute("altPedigree",altPedigree);
					request.getSession(false).setAttribute("DocId",pedigreeID);
				}
			}
		} catch (PersistanceException e) {
			log.error("Error in execute()" + e);
			throw new PersistanceException(e);	
		}
		
		
		
		
		// TODO Auto-generated method stub
		return mapping.findForward("success");
		
		
		
		
	}
	
	public String getDate(String dateTime){
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
		Date d;
		try {
			d = format.parse(dateTime);
		} catch (Exception e) {
			return dateTime;
			// TODO Auto-generated catch block
			
		}
		format = new SimpleDateFormat("MM/dd/yyyy");
		return format.format(d);
		
	}
	
	public boolean compare(Node n1){
		
		Node businessAddress = XMLUtil.getNode(n1,"transactionInfo/senderInfo/businessAddress");
		Node shippingAddress = XMLUtil.getNode(n1,"transactionInfo/senderInfo/shippingAddress");
		
		if(businessAddress!=null && shippingAddress != null){
			if(XMLUtil.getValue(businessAddress,"businessName").equals(XMLUtil.getValue(shippingAddress,"businessName"))){
				if(XMLUtil.getValue(businessAddress,"street1").equals(XMLUtil.getValue(shippingAddress,"street1"))){
					if(XMLUtil.getValue(businessAddress,"street2").equals(XMLUtil.getValue(shippingAddress,"street2"))){
						if(XMLUtil.getValue(businessAddress,"city").equals(XMLUtil.getValue(shippingAddress,"city"))){
							if(XMLUtil.getValue(businessAddress,"stateOrRegion").equals(XMLUtil.getValue(shippingAddress,"stateOrRegion"))){
								if(XMLUtil.getValue(businessAddress,"postalCode").equals(XMLUtil.getValue(shippingAddress,"postalCode"))){
									if(XMLUtil.getValue(businessAddress,"country").equals(XMLUtil.getValue(shippingAddress,"country"))){
										return true;
									}
								}
								
							}
						}
					}
				}
			}
			
		}
		return false;
	}

}

