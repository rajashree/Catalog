
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

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
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

import sun.rmi.transport.proxy.HttpReceiveSocket;

import com.rdta.catalog.Constants;
import com.rdta.catalog.PersistanceUtil;
import com.rdta.catalog.trading.model.TradingPartner;
//import com.rdta.commons.CommonUtil;
import com.rdta.commons.CommonUtil;
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

public class InitialPedigreeAction extends Action {
	private static Log log = LogFactory.getLog(InitialPedigreeAction.class);
	
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	String servIP = null;
	String clientIP = null;
	Connection conn;
	Statement stmt;
	
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		try{
			
			System.out.println("InitialPedigreeAction");
			HttpSession ses = request.getSession();
			String opr = request.getParameter("operationType");
			String tpname = request.getParameter("tpName");
			String productcode = request.getParameter("productCode");
			System.out.println(" tpName  is : "+tpname);
			System.out.println(" operation Type is : "+opr);
			
			StringBuffer buf1 = new StringBuffer();
			buf1.append("for $i in collection('tig:///CatalogManager/TradingPartner')/TradingPartner ");
			buf1.append("order by $i/name ascending return data($i/name) ");
			log.info("Query for Getting Trading partners in InitialPedigreeAction "+buf1.toString());
			List tpNames = queryRunner.returnExecuteQueryStrings(buf1.toString());
			System.out.println("TP Names : "+tpNames.toString());
			ses.setAttribute("tpNames",tpNames);
			
			if (opr == null)
				opr = "";
			if (opr.equals("TPDetails")) {
				
				InitialPedigreeForm formbean = (InitialPedigreeForm) form;
				System.out.println("InitialPedigree inside TPName " + opr);
				StringBuffer buff = new StringBuffer();
				buff.append("for $i in collection('tig:///CatalogManager/TradingPartner') ");
				buff.append("where $i/TradingPartner/name =  '"+CommonUtil.normalize(tpname)+"' ");

				buff.append("return $i ");
				System.out.println("Query is : " + buff.toString());
				
				String result = queryRunner.returnExecuteQueryStringsAsString(buff.toString());
				if (result != null) {
					Node n1 = XMLUtil.parse(result);
					Node n2 = XMLUtil.getNode(n1, "/TradingPartner");
					
					formbean.setTpName(XMLUtil.getValue(n2,	"name"));
					System.out.println("getTpName is : " + formbean.getTpName());
					formbean.setAddress1(XMLUtil.getValue(n2,"address/line1"));
					formbean.setAddress2(XMLUtil.getValue(n2,"address/line2"));
					formbean.setCity(XMLUtil.getValue(n2,"address/city"));
					formbean.setState(XMLUtil.getValue(n2,"address/state"));
					formbean.setCountry(XMLUtil.getValue(n2,"address/country"));
					formbean.setZipCode(XMLUtil.getValue(n2,"address/zip"));
					
					formbean.setContactName(XMLUtil.getValue(n2,"contact"));
					formbean.setContactTitle(XMLUtil.getValue(n2,"title"));
					formbean.setContactPhone(XMLUtil.getValue(n2,"phone"));
					formbean.setContactEmail(XMLUtil.getValue(n2,"email"));
					
					
					System.out.println("getRecvdFax is : " + formbean.getReceivedFax());
					if(formbean.getReceivedFax().equalsIgnoreCase("received")){
						formbean.setDocumentID(null);
					}
					
					if(formbean.getReceivedFax().equalsIgnoreCase("processed")){
						
					}
					if(formbean.getReceivedFax() == null || formbean.getReceivedFax().equalsIgnoreCase("null")){
						formbean.setDocumentID(null);
						
					}
					System.out.println("getContactEmail is : " + formbean.getContactEmail());
					
				}
				ses.setAttribute("TPName","true");
				ses.setAttribute("InitialPedigreeForm", formbean);
				return mapping.findForward("success");
			}
			
			if (opr.equals("lookupNDC")) {
				
				InitialPedigreeForm formbean = (InitialPedigreeForm) form;
				System.out.println("opreration type inside lookupNDC " + opr);
				
				StringBuffer buff = new StringBuffer();
				buff.append(" tlsp:GetProductCodeDetails('"+productcode+"') ");
				System.out.println("Query is : " + buff.toString());
				
				String result = queryRunner.returnExecuteQueryStringsAsString(buff.toString());
				System.out.println("result is : " + result);
				
				if (result != null && !result.equalsIgnoreCase("")) {
					
					Node n1 = XMLUtil.parse(result);
					Node n2 = XMLUtil.getNode(n1, "/Product");
					
					formbean.setName(XMLUtil.getValue(n2,"ProductName"));
					formbean.setStrength(XMLUtil.getValue(n2,"DosageStrength"));
					formbean.setDosageForm(XMLUtil.getValue(n2,"DosageForm"));
					formbean.setContainerSize(XMLUtil.getValue(n2,"ContainerSize"));
					formbean.setManufacturer(XMLUtil.getValue(n2,"ManufacturerName"));
					System.out.println("getRecvdFax is : " + formbean.getReceivedFax());
					if(formbean.getReceivedFax().equalsIgnoreCase("received")){
						formbean.setDocumentID(null);
					}
					
					if(formbean.getReceivedFax().equalsIgnoreCase("processed")){
						
					}
					if(formbean.getReceivedFax() == null || formbean.getReceivedFax().equalsIgnoreCase("null")){
						formbean.setDocumentID(null);
						
					}
					System.out.println("getContainerSize is : " + formbean.getContainerSize());
					ses.setAttribute("Lookup","true");
					
				}else{
					ses.setAttribute("Lookup","false");
					formbean.setName(null);
					formbean.setStrength(null);
					formbean.setDosageForm(null);
					formbean.setContainerSize(null);
					formbean.setManufacturer(null);
					formbean.setDocumentID(null);
				}
				ses.setAttribute("TPName","true");
				ses.setAttribute("InitialPedigreeForm", formbean);
				return mapping.findForward("success");
			}
			
			if (opr.equalsIgnoreCase("processedFax")) {
				
				InitialPedigreeForm formbean = (InitialPedigreeForm) form;
				String faxName = request.getParameter("faxName");
				
				String docId = getdocIDForFaxName(faxName);
				System.out.println("InitialPedigree ID" + docId);
				
				if (docId != null || !docId.equalsIgnoreCase("")) {
					StringBuffer buf = new StringBuffer();
					buf.append("for $i in collection('tig:///ePharma/PaperPedigree')/initialPedigree");
					buf.append(" where $i/DocumentInfo/serialNumber='" + docId
							+ "' return $i");
					
					String result = queryRunner.returnExecuteQueryStringsAsString(buf.toString());
					if (result != null) {
						Node n1 = (Node)XMLUtil.parse(result);
						Node n2 = XMLUtil.getNode(n1, "/initialPedigree");
						
						formbean.setTpName(XMLUtil.getValue(n2,"transactionInfo/senderInfo/businessAddress/businessName"));
						formbean.setAddress1(XMLUtil.getValue(n2,"transactionInfo/senderInfo/businessAddress/street1"));
						formbean.setAddress2(XMLUtil.getValue(n2,"transactionInfo/senderInfo/businessAddress/street2"));
						formbean.setCity(XMLUtil.getValue(n2,"transactionInfo/senderInfo/businessAddress/city"));
						formbean.setZipCode(XMLUtil.getValue(n2,"transactionInfo/senderInfo/businessAddress/postalCode"));
						formbean.setState(XMLUtil.getValue(n2,"transactionInfo/senderInfo/businessAddress/stateOrRegion"));
						formbean.setContactName(XMLUtil.getValue(n2,"transactionInfo/senderInfo/contactInfo/name"));
						formbean.setContainerSize(XMLUtil.getValue(n2,"productInfo/containerSize"));
						formbean.setCountry(XMLUtil.getValue(n2,"transactionInfo/senderInfo/businessAddress/country"));
						formbean.setDosageForm(XMLUtil.getValue(n2,"productInfo/dosageForm"));
						formbean.setContactPhone(XMLUtil.getValue(n2,"transactionInfo/senderInfo/contactInfo/telephone"));
						formbean.setContactEmail(XMLUtil.getValue(n2,"transactionInfo/senderInfo/contactInfo/email"));
						formbean.setContactTitle(XMLUtil.getValue(n2,"transactionInfo/senderInfo/contactInfo/title"));
						
						List item = XMLUtil.executeQuery(n2, "itemInfo");
						String[] lotNos = new String[item.size()];
						String[] quantity = new String[item.size()];
						;
						String[] itemSerialNumber = new String[item.size()];
						;
						String[] expirationDate = new String[item.size()];
						;
						for (int i = 0; i < item.size(); i++) {
							Node itemInfo = (Node) item.get(i);
							System.out.println(XMLUtil.convertToString(itemInfo));
							
							if (i == 0) {
								formbean.setLotNumber(XMLUtil.getValue(itemInfo,"lot"));
								formbean.setQuantity(XMLUtil.getValue(itemInfo,"quantity"));
								formbean.setItemSerialNumber(XMLUtil.getValue(itemInfo,"itemSerialNumber"));
								String expDate = XMLUtil.getValue(itemInfo,"expirationDate");
								System.out.println("The EXP DATE is :"+expDate);
								if(expDate != null && !expDate.equals(""))
									formbean.setExpirationDate(changeExpirationFormat(expDate));
								else formbean.setExpirationDate(expDate); 
								lotNos[i] = XMLUtil.getValue(itemInfo, "lot");							
								quantity[i] = XMLUtil.getValue(itemInfo, "quantity");
								itemSerialNumber[i] = XMLUtil.getValue(itemInfo,"itemSerialNumber");
								expirationDate[i] = XMLUtil.getValue(itemInfo,"expirationDate");
							} else {
								
								lotNos[i] = XMLUtil.getValue(itemInfo, "lot");
								quantity[i] = XMLUtil.getValue(itemInfo, "quantity");
								itemSerialNumber[i] = XMLUtil.getValue(itemInfo,"itemSerialNumber");
								String expDate = XMLUtil.getValue(itemInfo,"expirationDate");
								if(expDate != null && !expDate.equals(""))
									expirationDate[i] = (changeExpirationFormat(expDate));
								else expirationDate[i] = XMLUtil.getValue(itemInfo,"expirationDate");
								
							}
							
						}
						ses.setAttribute("faxName",faxName);
						ses.setAttribute("lotNos", lotNos);
						ses.setAttribute("quantity", quantity);
						ses.setAttribute("itemSerialNumber", itemSerialNumber);
						ses.setAttribute("expirationDate", expirationDate);
						ses.setAttribute("operationType", "processed");
						formbean.setManufacturer(XMLUtil.getValue(n2,"productInfo/manufacturer"));
						formbean.setName(XMLUtil.getValue(n2,"productInfo/drugName"));
						formbean.setProductCode(XMLUtil.getValue(n2,"productInfo/productCode"));
						formbean.setStrength(XMLUtil.getValue(n2,"productInfo/strength"));
						formbean.setTransactionDate(XMLUtil.getValue(n2,"transactionInfo/transactionDate"));
						formbean.setTransactionId(XMLUtil.getValue(n2,"transactionInfo/transactionIdentifier/identifier"));
						formbean.setTransactionType(changeTransactionTypeFormat(XMLUtil.getValue(n2,"transactionInfo/transactionIdentifier/identifierType")));
						formbean.setDocumentID(docId);
						ses.setAttribute("InitialPedigreeForm", formbean);
						ses.setAttribute("DocId", docId);
						ses.setAttribute("test","process");
						
					}
					
				}
				
				return mapping.findForward("success");
			}
			
			if (opr.equalsIgnoreCase("processedEnteredPedigrees")) {
				
				InitialPedigreeForm formbean = (InitialPedigreeForm) form;
				
				String docId = request.getParameter("pedigreeId");
				System.out.println("InitialPedigree ID" + docId);
				
				if (docId != null || !docId.equalsIgnoreCase("")) {
					StringBuffer buf = new StringBuffer();
					buf.append("for $i in collection('tig:///ePharma/PaperPedigree')/initialPedigree");
					buf.append(" where $i/DocumentInfo/serialNumber='" + docId
							+ "' return $i");
					System.out.println("InitialPedigree Query :" + buf.toString());
					String result = queryRunner.returnExecuteQueryStringsAsString(buf.toString());
					if (result != null) {
						Node n1 = XMLUtil.parse(result);
						Node n2 = XMLUtil.getNode(n1, "/initialPedigree");
						
						formbean.setTpName(XMLUtil.getValue(n2,"transactionInfo/senderInfo/businessAddress/businessName"));
						formbean.setAddress1(XMLUtil.getValue(n2,"transactionInfo/senderInfo/businessAddress/street1"));
						formbean.setAddress2(XMLUtil.getValue(n2,"transactionInfo/senderInfo/businessAddress/street2"));
						formbean.setCity(XMLUtil.getValue(n2,"transactionInfo/senderInfo/businessAddress/city"));
						formbean.setZipCode(XMLUtil.getValue(n2,"transactionInfo/senderInfo/businessAddress/postalCode"));
						formbean.setState(XMLUtil.getValue(n2,"transactionInfo/senderInfo/businessAddress/stateOrRegion"));
						formbean.setContactName(XMLUtil.getValue(n2,"transactionInfo/senderInfo/contactInfo/name"));
						formbean.setContainerSize(XMLUtil.getValue(n2,"productInfo/containerSize"));
						formbean.setCountry(XMLUtil.getValue(n2,"transactionInfo/senderInfo/businessAddress/country"));
						formbean.setDosageForm(XMLUtil.getValue(n2,"productInfo/dosageForm"));
						formbean.setContactPhone(XMLUtil.getValue(n2,"transactionInfo/senderInfo/contactInfo/telephone"));
						formbean.setContactEmail(XMLUtil.getValue(n2,"transactionInfo/senderInfo/contactInfo/email"));
						formbean.setContactTitle(XMLUtil.getValue(n2,"transactionInfo/senderInfo/contactInfo/title"));
						
						List item = XMLUtil.executeQuery(n2, "itemInfo");
						String[] lotNos = new String[item.size()];
						String[] quantity = new String[item.size()];
						;
						String[] itemSerialNumber = new String[item.size()];
						;
						String[] expirationDate = new String[item.size()];
						;
						for (int i = 0; i < item.size(); i++) {
							Node itemInfo = (Node) item.get(i);
							System.out.println(XMLUtil.convertToString(itemInfo));
							
							if (i == 0) {
								formbean.setLotNumber(XMLUtil.getValue(itemInfo,"lot"));
								formbean.setQuantity(XMLUtil.getValue(itemInfo,"quantity"));
								formbean.setItemSerialNumber(XMLUtil.getValue(itemInfo,"itemSerialNumber"));
								
								String expDate = XMLUtil.getValue(itemInfo,"expirationDate");
								System.out.println("The EXP DATE is :"+expDate);
								if(expDate != null && !expDate.equals(""))
									formbean.setExpirationDate(changeExpirationFormat(expDate));
								else formbean.setExpirationDate(expDate);
								
								lotNos[i] = XMLUtil.getValue(itemInfo, "lot");							
								quantity[i] = XMLUtil.getValue(itemInfo, "quantity");
								itemSerialNumber[i] = XMLUtil.getValue(itemInfo,"itemSerialNumber");
								expirationDate[i] = XMLUtil.getValue(itemInfo,"expirationDate");
							} else {
								
								lotNos[i] = XMLUtil.getValue(itemInfo, "lot");
								quantity[i] = XMLUtil.getValue(itemInfo, "quantity");
								itemSerialNumber[i] = XMLUtil.getValue(itemInfo,"itemSerialNumber");
								String expDate = XMLUtil.getValue(itemInfo,"expirationDate");
								if(expDate != null && !expDate.equals(""))
									expirationDate[i] = (changeExpirationFormat(expDate));
								else expirationDate[i] = XMLUtil.getValue(itemInfo,"expirationDate");
								
							}
							
						}
						
						ses.setAttribute("lotNos", lotNos);
						ses.setAttribute("quantity", quantity);
						ses.setAttribute("itemSerialNumber", itemSerialNumber);
						ses.setAttribute("expirationDate", expirationDate);
						ses.setAttribute("operationType", "processed");
						formbean.setManufacturer(XMLUtil.getValue(n2,"productInfo/manufacturer"));
						formbean.setName(XMLUtil.getValue(n2,"productInfo/drugName"));
						formbean.setProductCode(XMLUtil.getValue(n2,"productInfo/productCode"));
						formbean.setStrength(XMLUtil.getValue(n2,"productInfo/strength"));
						formbean.setTransactionDate(XMLUtil.getValue(n2,"transactionInfo/transactionDate"));
						formbean.setTransactionId(XMLUtil.getValue(n2,"transactionInfo/transactionIdentifier/identifier"));
						formbean.setTransactionType(changeTransactionTypeFormat(XMLUtil.getValue(n2,"transactionInfo/transactionIdentifier/identifierType")));
						formbean.setDocumentID(docId);
						ses.setAttribute("InitialPedigreeForm", formbean);
						ses.setAttribute("DocId", docId);
						ses.setAttribute("test","process");
						ses.removeAttribute("faxName");
						
					}
					
				}
				
				return mapping.findForward("success");
			}
			
			if (opr.equalsIgnoreCase("Save")) {
				String docId = "";
				System.out.println(request.getParameter("receivedFax"));
				String rcv = request.getParameter("receivedFax");
				
				if (!rcv.equals("null")) {
					docId = (String) ses.getAttribute("DocId");
				} else {
					docId = com.rdta.commons.PedigreeUtil.createPedId();
				}
				System.out.println("SAVE");
				InitialPedigreeForm formbean = (InitialPedigreeForm) form;
				formbean.setDocumentID(docId);
				String str = createDocument(formbean, request, docId);
				String pedDataxml = createPedINString(formbean, request, docId);
				System.out.println("ped in xml string\n\n\n\n"+pedDataxml);
				
				StringBuffer xQuery = new StringBuffer();
				xQuery.append("tig:insert-document('tig:///" + "ePharma" + "/"
						+ "PaperPedigree");
				xQuery.append("',");
				xQuery.append(str + ")");
				queryRunner.executeQuery(xQuery.toString());
				
				xQuery = new StringBuffer("");
				xQuery.append("tlsp:ChangeFaxStatus('" + docId + "')");
				queryRunner.executeQuery(xQuery.toString());
				
				//ses.setAttribute("DocId", docId);
				boolean flag = true;
				
				if (formbean.getAttachment() != null && formbean.getAttachment().getFileSize() != 0 && !rcv.equals("null") && rcv != null) {
					InputStream stream = formbean.getAttachment().getInputStream();
					
					String query = "declare binary-encoding none; declare namespace bin ='http://www.rainingdata.com/TigerLogic/binary-support';";
					
					query = query + "update let $binData := binary{$1} ";
					query = query
					+ "replace doc( for $i in collection('tig:///ePharma/PaperPedigree') ";
					query = query
					+ "where $i/initialPedigree/DocumentInfo/serialNumber = '"
					+ docId + "' ";
					query = query
					+ "return  document-uri($i))//altPedigree with <altPedigree><mimeType>application/pdf</mimeType><encoding>base64binary</encoding><data>{bin:base64-encode($binData)}</data></altPedigree>";
					System.out.println("Query for updating altPedigree : " + query);
					
					queryRunner.executeQueryWithStream(query, stream);
					flag = false;
					
					request.setAttribute("pagenm","pedigree");
					request.setAttribute("tp_company_nm",request.getParameter("tp_company_nm"));
					callPEDIN(pedDataxml);
					return mapping.findForward("receivedfaxes");
					
				}
				
				if (formbean.getAttachment() != null && formbean.getAttachment().getFileSize() != 0) {
					InputStream stream = formbean.getAttachment().getInputStream();
					
					String query = "declare binary-encoding none; declare namespace bin ='http://www.rainingdata.com/TigerLogic/binary-support';";
					
					query = query + "update let $binData := binary{$1} ";
					query = query
					+ "replace doc( for $i in collection('tig:///ePharma/PaperPedigree') ";
					query = query
					+ "where $i/initialPedigree/DocumentInfo/serialNumber = '"
					+ docId + "' ";
					query = query
					+ "return  document-uri($i))//altPedigree with <altPedigree><mimeType>application/pdf</mimeType><encoding>base64binary</encoding><data>{bin:base64-encode($binData)}</data></altPedigree>";
					System.out.println("Query for updating altPedigree : " + query);
					
					queryRunner.executeQueryWithStream(query, stream);
					flag = false;
					
				}
				
				
				
				if (!rcv.equals("null") && flag == true) {
					
					StringBuffer buf = new StringBuffer();
					
					buf
					.append("for $i in collection('tig:///ePharma/ReceivedFax')//InboundPostRequest[InitialPedigreeId='"
							+ docId + "']");
					buf
					.append("  return tlsp:repnode(data($i/FaxControl/FileContents),'"
							+ docId + "')");
					System.out.println("bufer" + buf.toString());
					queryRunner.executeQuery(buf.toString());
					clearFormBean(formbean,request);
					request.setAttribute("pagenm","pedigree");
					request.setAttribute("tp_company_nm",request.getParameter("tp_company_nm"));
					callPEDIN(pedDataxml);
					return mapping.findForward("receivedfaxes");
					
				}
				
				callPEDIN(pedDataxml);
				clearFormBean(formbean,request);
				//formbean.reset(mapping,request);
				return mapping.findForward("success");
				
			}
			
			if (opr.equals("Update")) {
				
				InitialPedigreeForm formbean = (InitialPedigreeForm) form;
				String docId = "";
				String flag = "true";
				String altString = "false";
				String binaryString = "";
				String rcv = request.getParameter("receivedFax");
				String olddocId="";
				String faxid = (String) ses.getAttribute("faxName");
				if (formbean.getDocumentID() != null) {
					
					
					docId = com.rdta.commons.PedigreeUtil.createPedId();
					if(faxid != null ){
						faxid = docId;
					}
					olddocId=formbean.getDocumentID();
				}
				
				String pedDataxml=createPedINString(formbean,request,docId);
				
				StringBuffer b = new StringBuffer();
				b
				.append("for $i in collection('tig:///ePharma/PaperPedigree')/initialPedigree[DocumentInfo/serialNumber='"
						+ olddocId + "']");
				b.append(" return $i/altPedigree/data/binary()");
				
				List alt = queryRunner.executeQuery(b.toString());
				System.out.println("altttt" + alt.size());
				
				
				
				InputStream altPedigree = null;
				if (alt.size() > 0 && alt.get(0) != null) {
					altPedigree = (InputStream) alt.get(0);
				}
				
				if (alt.size() == 0) {
					StringBuffer b1 = new StringBuffer();
					b1
					.append("for $i in collection('tig:///ePharma/PaperPedigree')/initialPedigree[DocumentInfo/serialNumber='"
							+ olddocId + "']");
					b1.append(" return $i/altPedigree/data");
					binaryString = queryRunner.returnExecuteQueryStringsAsString(b1
							.toString());
					List list = queryRunner.executeQuery(b1.toString());
					
//					System.out.println("ranesgsgsg "
//							+ list.get(0).getClass().getName());
					InputStream stream = (ByteArrayInputStream) list.get(0);
					byte[] data = new byte[128];
					File file = new File("c:\\temp\\abc.xml");
					file.createNewFile();
					FileOutputStream fos = new FileOutputStream(file);
					
					DataOutputStream dos = new DataOutputStream(fos);
					int x = stream.read();
					while (x != -1) {
						
						dos.write(x);
						// data= new byte[128];
						x = stream.read();
					}
					stream.close();
					dos.close();
					
					altString = "true";
					
				}
				
				String str = createDocument(formbean, request, docId);
				StringBuffer xQuery = new StringBuffer();
				xQuery.append("tig:insert-document('tig:///" + "ePharma" + "/"
						+ "PaperPedigree");
				xQuery.append("',");
				xQuery.append(str + ")");
				System.out.println("The Query is :"+xQuery.toString());
				queryRunner.executeQuery(xQuery.toString());
				
				Node theNode = XMLUtil.parse(str);
				StringBuffer buff = new StringBuffer(
				"$a//DocumentInfo/serialNumber='");
				buff.append(docId);
				buff.append("'");
				/*xQuery = new StringBuffer();
				 xQuery
				 .append(" for $a in collection('tig:///ePharma/PaperPedigree/')");
				 xQuery.append(" where " + buff.toString());
				 xQuery.append(" return tig:replace-document(document-uri( $a ), "
				 + str + ")");
				 
				 queryRunner.executeQuery(xQuery.toString());*/
				
				String check = request.getParameter("check");
				if (formbean.getAttachment() != null && formbean.getAttachment().getFileSize()>0) {
					System.out.println("Check value" + formbean.getAttachment().getFileSize());
					
					InputStream stream = formbean.getAttachment().getInputStream();
					
					String query = " declare namespace bin ='http://www.rainingdata.com/TigerLogic/binary-support';declare binary-encoding none;";
					
					query = query + "update let $binData := binary{$1} ";
					query = query
					+ "replace doc( for $i in collection('tig:///ePharma/PaperPedigree') ";
					query = query
					+ "where $i/initialPedigree/DocumentInfo/serialNumber = '"
					+ docId + "' ";
					query = query
					+ "return  document-uri($i))//altPedigree with <altPedigree><mimeType>application/pdf</mimeType><encoding>base64binary</encoding><data>{bin:base64-encode($binData)}</data></altPedigree>";
					System.out.println("Query for updating altPedigree : " + query);
					
					queryRunner.executeQueryWithStream(query, stream);
					flag = "false";
				}
				
				/*
				 * if(rcv.equals("processed")&& flag.equalsIgnoreCase("true")){
				 * String cid=(String)ses.getAttribute("DocId"); StringBuffer buf=
				 * new StringBuffer(); buf.append("for $i in
				 * collection('tig:///ePharma/ReceivedFax')//InboundPostRequest[InitialPedigreeId='"+cid+"']");
				 * buf.append(" return
				 * tlsp:repnode($i/FaxControl/FileContents/string(),'"+docId+"')");
				 * queryRunner.executeQuery(buf.toString());
				 * ses.setAttribute("operationType","processed"); flag="false"; }
				 */
				if (flag.equalsIgnoreCase("true") && altString.equals("true")) {
					
					String query = " tlsp:repEncoding('" + docId + "')";
					
					System.out.println("Query for updating altPedigree : " + query);
					
					// queryRunner.executeQueryWithStream(query, new FileInputStream
					// (new File("c:\\temp\\abc.xml")));
					queryRunner.executeQuery(query);
					flag = "false";
					
					
				}
				if (flag.equalsIgnoreCase("true")) {
					
					String query = "declare binary-encoding none; declare namespace bin ='http://www.rainingdata.com/TigerLogic/binary-support';";
					
					query = query + "update let $binData := binary{$1} ";
					query = query
					+ "replace doc( for $i in collection('tig:///ePharma/PaperPedigree') ";
					query = query
					+ "where $i/initialPedigree/DocumentInfo/serialNumber = '"
					+ docId + "' ";
					query = query
					+ "return  document-uri($i))//altPedigree with <altPedigree><mimeType>application/pdf</mimeType><encoding>base64binary</encoding><data>{bin:base64-encode($binData)}</data></altPedigree>";
					System.out.println("Query for updating altPedigree : " + query);
					
					queryRunner.executeQueryWithStream(query, altPedigree);
					
				}
				
				if(faxid != null ){
					StringBuffer xQuery1 = new StringBuffer();
					xQuery1.append(" for $a in collection('tig:///ePharma/ReceivedFax')/InboundPostRequest");
				    xQuery1.append(" where $a/InitialPedigreeId = '"+olddocId+"' ");
					xQuery1.append(" return $a ");
					System.out.println("The Query is :"+xQuery1.toString());
					String forParse = queryRunner.returnExecuteQueryStringsAsString(xQuery1.toString());
					Node n = XMLUtil.parse(forParse);
					XMLUtil.putValue(n,"/InboundPostRequest/InitialPedigreeId",docId);
					StringBuffer xQuery2 = new StringBuffer();
					xQuery2.append(" for $a in collection('tig:///ePharma/ReceivedFax')");
				    xQuery2.append(" where $a/InboundPostRequest/InitialPedigreeId = '"+olddocId+"' ");
					xQuery2.append(" return tig:replace-document(document-uri( $a ), "+ XMLUtil.convertToString(n,true) +")");
					queryRunner.executeQuery(xQuery2.toString());
				}
				callPEDIN(pedDataxml);
				request.setAttribute("updateSuccess","true");
				return mapping.findForward("success");
				
				
			}
			
			
			if (opr.equalsIgnoreCase("receivedFax")) {
				
				ses.removeAttribute("InitialPedigreeForm");
				ses.removeAttribute("lotNos");
				ses.removeAttribute("quantity");
				ses.removeAttribute("expirationDate");
				
				String faxName = request.getParameter("faxName");
				String docId = getdocIDForFaxName(faxName);
				System.out.println("InitialPedigree ID" + docId);
				
				InitialPedigreeForm formbean = (InitialPedigreeForm) form;
				System.out.println("FFF" + formbean);
				
				formbean.setAddress1("");
				formbean.setAddress2("");
				formbean.setCity("");
				formbean.setContactName("");
				formbean.setContactEmail("");
				formbean.setContactPhone("");
				formbean.setContactTitle("");
				formbean.setContainerSize("");
				formbean.setCountry("");
				formbean.setDosageForm("");
				formbean.setDosageForm("");
				formbean.setExpirationDate("");
				formbean.setItemSerialNumber("");
				formbean.setLotNumber("");
				formbean.setManufacturer("");
				formbean.setName("");
				formbean.setProductCode("");
				formbean.setQuantity("");
				formbean.setTpName("");
				formbean.setTransactionDate("");
				formbean.setTransactionId("");
				formbean.setTransactionType(null);
				formbean.setZipCode("");
				formbean.setStrength("");
				formbean.setState("");
				formbean.setDocumentID(null);
				
				ses.setAttribute("DocId", docId);
				ses.setAttribute("test", "received");
				ses.setAttribute("InitialPedigreeForm", formbean);
				ses.setAttribute("operationType", "received");
				
				
				return mapping.findForward("success");
			}
			/*
			 * StringBuffer sb=new StringBuffer(); sb.append("for $i in
			 * collection('tig:///ePharma/ReceivedFax')//InboundPostRequest[FaxControl/CSID='"+csid+"']");
			 * sb.append("return data( $i/InitialPedigreeId)"); String
			 * docId=queryRunner.returnExecuteQueryStringsAsString(sb.toString());
			 */
			
			/*
			 * String str= createDocument(formbean, request,docId); String
			 * pedDataxml=createPedINString(formbean,request,docId);
			 * callPEDIN(pedDataxml); StringBuffer xQuery = new StringBuffer();
			 * xQuery.append("tig:insert-document('tig:///" + "ePharma" + "/" +
			 * "PaperPedigree"); xQuery.append("',"); xQuery.append(str + ")");
			 * queryRunner.executeQuery(xQuery.toString());
			 * 
			 * StringBuffer buf= new StringBuffer();
			 * 
			 * buf.append("for $i in
			 * collection('tig:///ePharma/ReceivedFax')//InboundPostRequest[FaxControl/CSID='"+csid+"']");
			 * buf.append(" return
			 * tlsp:repnode($i/FaxControl/FileContents/string(),'"+docId+"')");
			 * queryRunner.executeQuery(buf.toString());
			 */
			
			
			
			
			ses.removeAttribute("TPName");
			ses.removeAttribute("InitialPedigreeForm");
			ses.removeAttribute("DocId");
			ses.removeAttribute("operationType");
			ses.removeAttribute("lotNos");
			ses.removeAttribute("quantity");
			ses.removeAttribute("expirationDate");
			ses.removeAttribute("Lookup");
			ses.removeAttribute("faxName");
			
		}catch(PersistanceException e){    		
			log.error("Error in InitialPedigreeAction execute method........." +e);
			throw new PersistanceException(e);
		}catch(Exception ex){
			ex.printStackTrace();
			log.error("Error in InitialPedigreeAction execute method........." +ex);
			throw new Exception(ex);
		}	
		return mapping.findForward("success");
	};
	
	
	
	public void callPEDIN(String xmlString) throws Exception {
		try{
			
			String callPedin = "tlsp:Ped-In(" + xmlString + ")";
			System.out.println("callPedin"+callPedin);
			List li = queryRunner.executeQuery(callPedin);
			System.out.println("List result"+li);
			
		}catch(PersistanceException e){    		
			log.error("Error in InitialPedigreeAction execute method........." +e);
			throw new PersistanceException(e);
		}catch(Exception ex){
			ex.printStackTrace();
			log.error("Error in InitialPedigreeAction execute method........." +ex);
			throw new Exception(ex);
		}
	}
	
	public String getdocIDForFaxName(String faxName) throws Exception {
		try{
			
			String id = "";
			StringBuffer buffer = new StringBuffer();
			buffer
			.append("for $i in collection('tig:///ePharma/ReceivedFax')/InboundPostRequest");
			buffer.append(" where $i/FaxControl/FaxName = '" + faxName + "'");
			buffer.append(" return data( $i/InitialPedigreeId)");
			log.info("query is" + buffer.toString());
			id = queryRunner.returnExecuteQueryStringsAsString(buffer.toString());
			log.info("Pedigree ID" + id);
			
			return id;
			
		}catch(PersistanceException e){    		
			log.error("Error in InitialPedigreeAction execute method........." +e);
			throw new PersistanceException(e);
		}catch(Exception ex){
			ex.printStackTrace();
			log.error("Error in InitialPedigreeAction execute method........." +ex);
			throw new Exception(ex);
		}
		
	}
	
	public String createDocument(InitialPedigreeForm form,
			HttpServletRequest request, String docId) throws Exception{
		try{
			
			StringBuffer buffer = new StringBuffer("<initialPedigree>");
			buffer.append("<DocumentInfo><serialNumber>" + docId
					+ "</serialNumber><version>1</version></DocumentInfo>");
			buffer.append("<productInfo>");
			buffer.append("<drugName>" + CommonUtil.normalize(form.getName().toUpperCase()) + "</drugName>");
			System.out.println("M/F is :"+form.getManufacturer());
			System.out.println("M/F is :"+CommonUtil.normalize(form.getManufacturer()));
			buffer.append("<manufacturer>"+CommonUtil.normalize(form.getManufacturer().toUpperCase())+ "</manufacturer>");
			
			//buffer.append("<manufacturer>"+ form.getManufacturer().toUpperCase()+ "</manufacturer>");
			buffer.append("<productCode>" + CommonUtil.normalize(form.getProductCode())+ "</productCode>");

			buffer.append("<dosageForm>" +  CommonUtil.normalize(form.getDosageForm().toUpperCase()) + "</dosageForm>");

			buffer.append("<strength>" + CommonUtil.normalize(form.getStrength().toUpperCase()) + "</strength>");

			buffer.append("<containerSize>" +  CommonUtil.normalize(form.getContainerSize()) + "</containerSize>");

			buffer.append("</productInfo>");
			
			// Here need to append iteminfo
			String lotNos[] = request.getParameterValues("lotNumber");
			String quantity[] = request.getParameterValues("quantity");
			//String itemSerialNumber[] = request.getParameterValues("itemSerialNumber");
			String expirationDate[] = request.getParameterValues("expirationDate");
			
			//	HttpSession session = request.getSession();
			//	session.setAttribute("lotNos", lotNos);
			//	session.setAttribute("quantity", quantity);
			//session.setAttribute("itemSerialNumber", itemSerialNumber);
			//	session.setAttribute("expirationDate", expirationDate);
			
			for (int it = 0; lotNos != null && it < lotNos.length; it++) {
				String date = "";
				buffer.append("<itemInfo>");
				if(expirationDate[it].length() != 0){
					date = getExpirationDate(expirationDate[it]);
					System.out.println("date is : " + date);
				}else date = ""; 
				buffer.append("<lot>" + CommonUtil.normalize(lotNos[it].toUpperCase())  + "</lot><expirationDate>"
						+ date + "</expirationDate>");
				buffer.append("<quantity>" + quantity[it]
				                                      + "</quantity><itemSerialNumber></itemSerialNumber></itemInfo>");
			}
			
			
			buffer.append("<transactionInfo>");
			
			buffer.append("<senderInfo><businessAddress>");
			buffer.append("<businessName>" + CommonUtil.normalize(form.getTpName()) + "</businessName>");
			buffer.append("<street1>" +  CommonUtil.normalize(form.getAddress1())  + "</street1><street2>"
					+ CommonUtil.normalize(form.getAddress2())  + "</street2>");
			buffer.append("<city>" +  CommonUtil.normalize(form.getCity())  + "</city><stateOrRegion>"
					+  CommonUtil.normalize(form.getState()) + "</stateOrRegion>");
			buffer.append("<postalCode>" +  CommonUtil.normalize(form.getZipCode())

					+ "</postalCode><country>" + CommonUtil.normalize(form.getCountry())  + "</country>");
			buffer.append("</businessAddress><contactInfo><name>"
					+  CommonUtil.normalize(form.getContactName())  + "</name><title>"+ CommonUtil.normalize(form.getContactTitle())+  "</title>");
			buffer
			.append("<telephone>"+form.getContactPhone()+"</telephone><email>"+ CommonUtil.normalize(form.getContactEmail())+"</email><url/></contactInfo>");
			buffer.append("</senderInfo>");
			String name = "",street1= "",street2= "",city= "",stateOrRegion= "",country= "",postalCode = "";
			
			
			String sname = "SOUTHWOOD PHARMACEUTICALS, INC.";
			StringBuffer buff = new StringBuffer();
			buff.append("for $i in collection('tig:///CatalogManager/TradingPartner') ");
			buff.append("where $i/TradingPartner/name = '"+sname+"' ");
			buff.append("return $i ");
			System.out.println("Query is : " + buff.toString());
			
			String result = queryRunner.returnExecuteQueryStringsAsString(buff.toString());
			if (result != null) {
				Node n1 = XMLUtil.parse(result);
				Node n2 = XMLUtil.getNode(n1, "/TradingPartner");
				
				name = XMLUtil.getValue(n2,	"name");
				if(name == null) name ="";
				street1 = XMLUtil.getValue(n2,"address/line1");
				if(street1 == null) street1 ="";
				street2 = XMLUtil.getValue(n2,"address/line2");
				if(street2 == null) street2 ="";
				city = XMLUtil.getValue(n2,"address/city");
				if(city == null) city ="";
				stateOrRegion = XMLUtil.getValue(n2,"address/state");
				if(stateOrRegion == null) stateOrRegion ="";
				country = XMLUtil.getValue(n2,"address/country");
				if(country == null) country ="";
				postalCode = XMLUtil.getValue(n2,"address/zip");
				if(postalCode == null) postalCode ="";
			}
			
			
			
			buffer.append("<recipientInfo><businessAddress>");
			buffer.append("<businessName>"+name+"</businessName>");
			buffer.append("<street1>"+street1+"</street1><street2>"+street2+"</street2>");
			buffer.append("<city>"+city+"</city><stateOrRegion>"+stateOrRegion+"</stateOrRegion>");
			buffer.append("<postalCode>"+postalCode+"</postalCode><country>"+country+"</country>");
			buffer.append("</businessAddress></recipientInfo>");
			
			buffer.append("<transactionIdentifier>");
			buffer.append("<identifier>" + CommonUtil.normalize(form.getTransactionId().toUpperCase())

					+ "</identifier>");
			buffer.append("<identifierType>" + changeTransactionTypeFormat(form.getTransactionType())
					+ "</identifierType>");
			buffer.append("</transactionIdentifier>");
			buffer.append("<transactionType>Sale</transactionType>");
			buffer.append("<transactionDate>" + form.getTransactionDate()
					+ "</transactionDate>");
			buffer.append("</transactionInfo>");
			buffer.append("<altPedigree><mimeType>application/pdf</mimeType><encoding/><data/></altPedigree>");
			
			buffer.append("</initialPedigree>");
			System.out.println("Buffer value ::::::::" + buffer.toString());
			return buffer.toString();
			
		}catch(PersistanceException e){    		
			log.error("Error in InitialPedigreeAction execute method........." +e);
			throw new PersistanceException(e);
		}catch(Exception ex){
			ex.printStackTrace();
			log.error("Error in InitialPedigreeAction execute method........." +ex);
			throw new Exception(ex);
		}	
	}
	
	public String createPedINString(InitialPedigreeForm form,
			HttpServletRequest request, String docId) throws Exception{
		try{
			
			String tpName = form.getTpName();
			
			StringBuffer buffer = new StringBuffer("<PedigreeData>");
			buffer.append("<messageNm>PED_IN</messageNm>");
			buffer.append("<pedigreeId>" + docId + "</pedigreeId>");
			
			buffer.append("<ProductInfo>");
			buffer.append("<drugName>" + form.getName().toUpperCase() + "</drugName>");
			buffer.append("<manufacturer>" + CommonUtil.normalize(form.getManufacturer().toUpperCase())
					+ "</manufacturer>");
			buffer.append("<productCode>" + form.getProductCode()
					+ "</productCode>");
			buffer.append("<dosageForm>" + form.getDosageForm().toUpperCase() + "</dosageForm>");
			buffer.append("<strength>" + form.getStrength().toUpperCase() + "</strength>");
			buffer.append("<containerSize>" + form.getContainerSize()
					+ "</containerSize>");
			buffer.append("</ProductInfo>");
			
			// Here need to append iteminfo 75837-421-77
			String lotNos[] = request.getParameterValues("lotNumber");
			String quantity[] = request.getParameterValues("quantity");
			String itemSerialNumber[] = request.getParameterValues("itemSerialNumber");
			String expirationDate[] = request.getParameterValues("expirationDate");
			
			HttpSession session = request.getSession();
			session.setAttribute("lotNos", lotNos);
			session.setAttribute("quantity", quantity);
			session.setAttribute("itemSerialNumber", itemSerialNumber);
			session.setAttribute("expirationDate", expirationDate);
			for (int it = 0; it < lotNos.length; it++) {
				buffer.append("<ItemInfo>");
				buffer.append("<lot>" + lotNos[it].toUpperCase() + "</lot><quantity>"
						+ quantity[it] + "</quantity>");
				String date = "";
				if(expirationDate[it].length() != 0){
					date = getExpirationDate(expirationDate[it]);
					buffer.append(" <expirationDate>" + date + "T00:00:00" + "</expirationDate></ItemInfo>");
				}else {
					date = "";
					buffer.append(" <expirationDate>" + date + "</expirationDate></ItemInfo>");
				}
				
			}
			
			StringBuffer b = new StringBuffer();
			
			b
			.append("for $i in collection('tig:///CatalogManager/TradingPartner')/TradingPartner[name='"
					+ form.getTpName() + "']");
			
			b.append(" return data($i/businessId)");
			String businessID = null;
			
			
			businessID = queryRunner.returnExecuteQueryStringsAsString(b.toString());
			if(businessID==null || businessID.equals("")){
				session.setAttribute("TPNameNotValid","true");
			}
			else{
				session.setAttribute("TPNameNotValid","false");
				
			}
			
			
			buffer.append("<TransactionInfo>");
			
			buffer.append("<Address>");
			buffer.append("<businessId>" + businessID + "</businessId>");
			buffer.append("<businessName>" + form.getTpName() + "</businessName>");
			buffer.append("<street1>" + form.getAddress1() + "</street1><street2>"
					+ form.getAddress2() + "</street2>");
			buffer.append("<city>" + form.getCity() + "</city><stateOrRegion>"
					+ form.getState() + "</stateOrRegion>");
			buffer.append("<postalCode>" + form.getZipCode()
					+ "</postalCode><country>" + form.getCountry() + "</country>");
			buffer.append("</Address>");
			
			buffer.append("<TransactionId>" + form.getTransactionId().toUpperCase()
					+ "</TransactionId>");
			
			buffer.append("<TransactionType>" + changeTransactionTypeFormat(form.getTransactionType())
					+ "</TransactionType>");
			buffer.append("<TransactionDate>" + form.getTransactionDate()
					+ "T00:00:00" + "</TransactionDate>");
			buffer.append("</TransactionInfo>");
			buffer.append("</PedigreeData>");
			
			return buffer.toString();
			
		}catch(PersistanceException e){    		
			log.error("Error in InitialPedigreeAction execute method........." +e);
			throw new PersistanceException(e);
		}catch(Exception ex){
			ex.printStackTrace();
			log.error("Error in InitialPedigreeAction execute method........." +ex);
			throw new Exception(ex);
		}	
		
	}
	
	private static String getUUID() throws PersistanceException {
		StringBuffer buff = new StringBuffer();
		buff.append("import module namespace util = 'xquery:modules:util';");
		buff.append("let $docId := util:create-uuid() return $docId");
		return (queryRunner.returnExecuteQueryStringsAsString(buff.toString()));
	}
	
	public String getExpirationDate(String expdate){
		
			String monthYear[] = expdate.split("/");
			String date = "";
			String month = monthYear[0];
			String year = monthYear[1];
			
			if(month.equals("01") || month.equals("1") || month.equals("03") || month.equals("3") || month.equals("05") ||  month.equals("5") || month.equals("07") || month.equals("7") || month.equals("08") || month.equals("8") || month.equals("10") || month.equals("12"))
				date = "31";
			if(month.equals("04") || month.equals("4") || month.equals("06") || month.equals("6") || month.equals("09") || month.equals("9") || month.equals("11"))
				date = "30";
			if(month.equals("02"))
				date = "28";
			
			Calendar c = Calendar.getInstance();
			String century = String.valueOf( c.get(Calendar.YEAR) /100 ); 
			
			String fullDate = century+year+"-"+month+"-"+date;
			System.out.println("fullDate is : "+fullDate);
			return fullDate;
			
	}
	
	public String changeExpirationFormat(String expdate){
		
		String date= expdate;
		
		String dates [] = date.split("-");
		String year = dates[0].substring(2,4);
		String month = dates[1];
		
		String newFormat = month+"/"+year;
		System.out.println("newFormat :"+newFormat);
		return newFormat;
		
		
	}
	
	public String changeTransactionTypeFormat(String type){
		
		String newFormat = "";
		if(type.equals("Invoice")) newFormat = "InvoiceNumber";
		if(type.equals("PurchaseOrder")) newFormat = "PurchaseOrderNumber";
		if(type.equals("InvoiceNumber")) newFormat = "Invoice";
		if(type.equals("PurchaseOrderNumber")) newFormat = "PurchaseOrder";
		System.out.println("newFormat :"+newFormat);
		return newFormat;
	}
	
	public void clearFormBean(InitialPedigreeForm form, HttpServletRequest request){
		
		InitialPedigreeForm formbean = (InitialPedigreeForm) form;
		System.out.println("FFF" + formbean);
		formbean.setDocumentID(null);
		formbean.setAddress1("");
		formbean.setAddress2("");
		formbean.setCity("");
		formbean.setContactName("");
		formbean.setContactEmail("");
		formbean.setContactPhone("");
		formbean.setContactTitle("");
		formbean.setContainerSize("");
		formbean.setCountry("");
		formbean.setDosageForm("");
		formbean.setDosageForm("");
		formbean.setExpirationDate("");
		formbean.setItemSerialNumber("");
		formbean.setLotNumber("");
		formbean.setManufacturer("");
		formbean.setName("");
		formbean.setProductCode("");
		formbean.setQuantity("");
		formbean.setTpName("");
		formbean.setTransactionDate("");
		formbean.setTransactionId("");
		formbean.setTransactionType(null);
		formbean.setZipCode("");
		formbean.setStrength("");
		formbean.setState("");
		HttpSession ses= request.getSession();
		ses.removeAttribute("lotNos");
		ses.removeAttribute("quantity");
		ses.removeAttribute("expirationDate");
		ses.removeAttribute("faxName");
	}

	
}


