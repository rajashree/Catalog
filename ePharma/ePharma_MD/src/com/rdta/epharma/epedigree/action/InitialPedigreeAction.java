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

import com.rdta.catalog.trading.model.TradingPartner;
//import com.rdta.commons.CommonUtil;
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

public class InitialPedigreeAction extends Action {
	private static Log log = LogFactory.getLog(InitialPedigreeAction.class);

	private static final QueryRunner queryRunner = QueryRunnerFactory
			.getInstance().getDefaultQueryRunner();

	String servIP = null;

	String clientIP = null;

	Connection conn;

	Statement stmt;

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		System.out.println("InitialPedigreeAction");
		HttpSession ses = request.getSession();
		String opr = request.getParameter("operationType");

		if (opr == null)
			opr = "";
		if (opr.equalsIgnoreCase("processedFax")) {

			InitialPedigreeForm formbean = (InitialPedigreeForm) form;
			String faxName = request.getParameter("faxName");
			String docId = getdocIDForFaxName(faxName);
			System.out.println("InitialPedigree ID" + docId);

			if (docId != null || !docId.equalsIgnoreCase("")) {
				StringBuffer buf = new StringBuffer();
				buf
						.append("for $i in collection('tig:///ePharma_MD/PaperPedigree')/initialPedigree");
				buf.append(" where $i/DocumentInfo/serialNumber='" + docId
						+ "' return $i");

				String result = queryRunner
						.returnExecuteQueryStringsAsString(buf.toString());
				if (result != null) {
					Node n1 = XMLUtil.parse(result);
					Node n2 = XMLUtil.getNode(n1, "/initialPedigree");

					formbean
							.setTpName(XMLUtil
									.getValue(n2,
											"transactionInfo/senderInfo/businessAddress/businessName"));
					formbean
							.setAddress1(XMLUtil
									.getValue(n2,
											"transactionInfo/senderInfo/businessAddress/street1"));
					formbean
							.setAddress2(XMLUtil
									.getValue(n2,
											"transactionInfo/senderInfo/businessAddress/street2"));
					formbean.setCity(XMLUtil.getValue(n2,
							"transactionInfo/senderInfo/businessAddress/city"));
					formbean
							.setZipCode(XMLUtil
									.getValue(n2,
											"transactionInfo/senderInfo/businessAddress/postalCode"));

					formbean
							.setState(XMLUtil
									.getValue(n2,
											"transactionInfo/senderInfo/businessAddress/stateOrRegion"));
					formbean.setContactName(XMLUtil.getValue(n2,
							"transactionInfo/senderInfo/contactInfo/name"));
					formbean.setContainerSize(XMLUtil.getValue(n2,
							"productInfo/containerSize"));
					formbean
							.setCountry(XMLUtil
									.getValue(n2,
											"transactionInfo/senderInfo/businessAddress/country"));
					formbean.setDosageForm(XMLUtil.getValue(n2,
							"productInfo/dosageForm"));

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
							formbean.setLotNumber(XMLUtil.getValue(itemInfo,
									"lot"));
							formbean.setQuantity(XMLUtil.getValue(itemInfo,
									"quantity"));
							formbean.setItemSerialNumber(XMLUtil.getValue(
									itemInfo, "itemSerialNumber"));

							formbean.setExpirationDate(XMLUtil.getValue(
									itemInfo, "expirationDate"));

							lotNos[i] = XMLUtil.getValue(itemInfo, "lot");

							quantity[i] = XMLUtil
									.getValue(itemInfo, "quantity");
							itemSerialNumber[i] = XMLUtil.getValue(itemInfo,
									"itemSerialNumber");
							expirationDate[i] = XMLUtil.getValue(itemInfo,
									"expirationDate");
						} else {

							lotNos[i] = XMLUtil.getValue(itemInfo, "lot");
							quantity[i] = XMLUtil
									.getValue(itemInfo, "quantity");
							itemSerialNumber[i] = XMLUtil.getValue(itemInfo,
									"itemSerialNumber");
							expirationDate[i] = XMLUtil.getValue(itemInfo,
									"expirationDate");

						}

					}

					ses.setAttribute("lotNos", lotNos);
					ses.setAttribute("quantity", quantity);
					ses.setAttribute("itemSerialNumber", itemSerialNumber);
					ses.setAttribute("expirationDate", expirationDate);
					ses.setAttribute("operationType", "processed");
					formbean.setManufacturer(XMLUtil.getValue(n2,
							"productInfo/manufacturer"));
					formbean.setName(XMLUtil.getValue(n2,
							"productInfo/drugName"));
					formbean.setProductCode(XMLUtil.getValue(n2,
							"productInfo/productCode"));
					formbean.setStrength(XMLUtil.getValue(n2,
							"productInfo/strength"));

					formbean.setTransactionDate(XMLUtil.getValue(n2,
							"transactionInfo/transactionDate"));
					formbean
							.setTransactionId(XMLUtil
									.getValue(n2,
											"transactionInfo/transactionIdentifier/identifier"));
					formbean
							.setTransactionType(XMLUtil
									.getValue(n2,
											"transactionInfo/transactionIdentifier/identifierType"));
					formbean.setDocumentID(docId);
					ses.setAttribute("InitialPedigreeForm", formbean);
					ses.setAttribute("DocId", docId);

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
			InitialPedigreeForm formbean = (InitialPedigreeForm) form;
			formbean.setDocumentID(docId);
			String str = createDocument(formbean, request, docId);
			String pedDataxml = createPedINString(formbean, request, docId);
			System.out.println(pedDataxml);
			if(((String)ses.getAttribute("TPNameNotValid")).equals("true"))
			{
				
			return mapping.findForward("frame1");
			}
			 
			StringBuffer xQuery = new StringBuffer();
			xQuery.append("tig:insert-document('tig:///" + "ePharma_MD" + "/"
					+ "PaperPedigree");
			xQuery.append("',");
			xQuery.append(str + ")");
			queryRunner.executeQuery(xQuery.toString());

			xQuery = new StringBuffer("");
			xQuery.append("tlsp:ChangeFaxStatus_MD('" + docId + "')");
			queryRunner.executeQuery(xQuery.toString());

			ses.setAttribute("DocId", docId);

			String check = request.getParameter("check");
			System.out.println("Check value" + check);
			boolean flag = true;
			if (check != null) {
				InputStream stream = formbean.getAttachment().getInputStream();

				String query = "declare binary-encoding none; declare namespace bin ='http://www.rainingdata.com/TigerLogic/binary-support';";

				query = query + "update let $binData := binary{$1} ";
				query = query
						+ "replace doc( for $i in collection('tig:///ePharma_MD/PaperPedigree') ";
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
						.append("for $i in collection('tig:///ePharma_MD/ReceivedFax')//InboundPostRequest[InitialPedigreeId='"
								+ docId + "']");
				buf
						.append("  return tlsp:repnode(data($i/FaxControl/FileContents),'"
								+ docId + "')");
				System.out.println("bufer" + buf.toString());
				queryRunner.executeQuery(buf.toString());

			}

			//callPEDIN(pedDataxml);
			return mapping.findForward("frame1");
		}

		if (opr.equals("Update")) {

			InitialPedigreeForm formbean = (InitialPedigreeForm) form;
			String docId = "";
			String flag = "true";
			String altString = "false";
			String binaryString = "";
			String rcv = request.getParameter("receivedFax");
			if (formbean.getDocumentID() != null) {
				docId = formbean.getDocumentID();
			}
			
			 String pedDataxml=createPedINString(formbean,request,docId);
			 if(((String)ses.getAttribute("TPNameNotValid")).equals("true"))
				{
					
				 return	mapping.findForward("frame1");
				}
			 
			StringBuffer b = new StringBuffer();
			b
					.append("for $i in collection('tig:///ePharma_MD/PaperPedigree')/initialPedigree[DocumentInfo/serialNumber='"
							+ docId + "']");
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
						.append("for $i in collection('tig:///ePharma_MD/PaperPedigree')/initialPedigree[DocumentInfo/serialNumber='"
								+ docId + "']");
				b1.append(" return $i/altPedigree/data");
				binaryString = queryRunner.returnExecuteQueryStringsAsString(b1
						.toString());
				List list = queryRunner.executeQuery(b1.toString());

				System.out.println("ranesgsgsg "
						+ list.get(0).getClass().getName());
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
			
			 
			Node theNode = XMLUtil.parse(str);
			StringBuffer buff = new StringBuffer(
					"$a//DocumentInfo/serialNumber='");
			buff.append(docId);
			buff.append("'");
			StringBuffer xQuery = new StringBuffer();
			xQuery
					.append(" for $a in collection('tig:///ePharma_MD/PaperPedigree/')");
			xQuery.append(" where " + buff.toString());
			xQuery.append(" return tig:replace-document(document-uri( $a ), "
					+ str + ")");

			queryRunner.executeQuery(xQuery.toString());

			String check = request.getParameter("check");
			System.out.println("Check value" + check);
			if (check != null) {
				InputStream stream = formbean.getAttachment().getInputStream();

				String query = " declare namespace bin ='http://www.rainingdata.com/TigerLogic/binary-support';declare binary-encoding none;";

				query = query + "update let $binData := binary{$1} ";
				query = query
						+ "replace doc( for $i in collection('tig:///ePharma_MD/PaperPedigree') ";
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

				String query = " tlsp:repEncoding_MD('" + docId + "')";

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
						+ "replace doc( for $i in collection('tig:///ePharma_MD/PaperPedigree') ";
				query = query
						+ "where $i/initialPedigree/DocumentInfo/serialNumber = '"
						+ docId + "' ";
				query = query
						+ "return  document-uri($i))//altPedigree with <altPedigree><mimeType>application/pdf</mimeType><encoding>base64binary</encoding><data>{bin:base64-encode($binData)}</data></altPedigree>";
				System.out.println("Query for updating altPedigree : " + query);

				queryRunner.executeQueryWithStream(query, altPedigree);

			}
			 //callPEDIN(pedDataxml);
			return mapping.findForward("frame1");
		}
		if (opr.equalsIgnoreCase("receivedFax")) {

			ses.removeAttribute("InitialPedigreeForm");

			ses.removeAttribute("lotNos");
			ses.removeAttribute("quantity");
			ses.removeAttribute("itemSerialNumber");
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
			formbean.setTransactionType("");
			formbean.setZipCode("");
			formbean.setStrength("");
			formbean.setState("");

			formbean.setDocumentID(null);

			ses.setAttribute("DocId", docId);
			ses.setAttribute("test", "test");
			ses.setAttribute("InitialPedigreeForm", formbean);
			ses.setAttribute("operationType", "received");
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

			return mapping.findForward("success");
		}
		ses.removeAttribute("TPNameNotValid");
		ses.removeAttribute("InitialPedigreeForm");
		ses.removeAttribute("DocId");
		ses.removeAttribute("operationType");
		ses.removeAttribute("lotNos");
		ses.removeAttribute("quantity");
		ses.removeAttribute("itemSerialNumber");
		ses.removeAttribute("expirationDate");
		return mapping.findForward("success");

	};

	public void callPEDIN(String xmlString) throws PersistanceException {

		String callPedin = "tlsp:Ped-In_MD(" + xmlString + ")";

		List li = queryRunner.executeQuery(callPedin);

	}

	public String getdocIDForFaxName(String faxName)
			throws PersistanceException {
		String id = "";
		StringBuffer buffer = new StringBuffer();
		buffer
				.append("for $i in collection('tig:///ePharma_MD/ReceivedFax')/InboundPostRequest");
		buffer.append(" where $i/FaxControl/FaxName = '" + faxName + "'");
		buffer.append(" return data( $i/InitialPedigreeId)");
		log.info("query is" + buffer.toString());
		id = queryRunner.returnExecuteQueryStringsAsString(buffer.toString());
		log.info("Pedigree ID" + id);

		return id;

	}

	public String createDocument(InitialPedigreeForm form,
			HttpServletRequest request, String docId) {

		StringBuffer buffer = new StringBuffer("<initialPedigree>");
		buffer.append("<DocumentInfo><serialNumber>" + docId
				+ "</serialNumber><version>1</version></DocumentInfo>");
		buffer.append("<productInfo>");
		buffer.append("<drugName>" + form.getName() + "</drugName>");
		buffer.append("<manufacturer>" + form.getManufacturer()
				+ "</manufacturer>");
		buffer.append("<productCode>" + form.getProductCode()
				+ "</productCode>");
		buffer.append("<dosageForm>" + form.getDosageForm() + "</dosageForm>");
		buffer.append("<strength>" + form.getStrength() + "</strength>");
		buffer.append("<containerSize>" + form.getContainerSize()
				+ "</containerSize>");
		buffer.append("</productInfo>");

		// Here need to append iteminfo
		String lotNos[] = request.getParameterValues("lotNumber");
		String quantity[] = request.getParameterValues("quantity");
		String itemSerialNumber[] = request
				.getParameterValues("itemSerialNumber");
		String expirationDate[] = request.getParameterValues("expirationDate");

		HttpSession session = request.getSession();
		session.setAttribute("lotNos", lotNos);
		session.setAttribute("quantity", quantity);
		session.setAttribute("itemSerialNumber", itemSerialNumber);
		session.setAttribute("expirationDate", expirationDate);
		for (int it = 0; lotNos != null && it < lotNos.length; it++) {
			buffer.append("<itemInfo>");
			buffer.append("<lot>" + lotNos[it] + "</lot><expirationDate>"
					+ expirationDate[it] + "</expirationDate>");
			buffer.append("<quantity>" + quantity[it]
					+ "</quantity><itemSerialNumber>" + itemSerialNumber[it]
					+ "</itemSerialNumber></itemInfo>");
		}

		buffer.append("<transactionInfo>");

		buffer.append("<senderInfo><businessAddress>");
		buffer.append("<businessName>" + form.getTpName() + "</businessName>");
		buffer.append("<street1>" + form.getAddress1() + "</street1><street2>"
				+ form.getAddress2() + "</street2>");
		buffer.append("<city>" + form.getCity() + "</city><stateOrRegion>"
				+ form.getState() + "</stateOrRegion>");
		buffer.append("<postalCode>" + form.getZipCode()
				+ "</postalCode><country>" + form.getCountry() + "</country>");
		buffer.append("</businessAddress><contactInfo><name>"
				+ form.getContactName() + "</name><title> </title>");
		buffer
				.append("<telephone> </telephone><email></email><url/></contactInfo>");
		buffer.append("</senderInfo>");

		buffer.append("<recipientInfo><businessAddress>");
		buffer.append("<businessName>Southwood</businessName>");
		buffer.append("<street1>street1</street1><street2>street2</street2>");
		buffer
				.append("<city>Francisco</city><stateOrRegion>CA</stateOrRegion>");
		buffer.append("<postalCode>100000</postalCode><country>USA</country>");
		buffer.append("</businessAddress></recipientInfo>");

		buffer.append("<transactionIdentifier>");
		buffer.append("<identifier>" + form.getTransactionId()
				+ "</identifier>");
		buffer.append("<identifierType>" + form.getTransactionType()
				+ "</identifierType>");
		buffer.append("</transactionIdentifier>");
		buffer.append("<transactionType>Other</transactionType>");
		buffer.append("<transactionDate>" + form.getTransactionDate()
				+ "</transactionDate>");
		buffer.append("</transactionInfo>");

		buffer
				.append("<altPedigree><mimeType>application/pdf</mimeType><encoding/><data/></altPedigree>");

		buffer.append("</initialPedigree>");

		System.out.println("Buffer value ::::::::" + buffer.toString());
		return buffer.toString();
	}

	public String createPedINString(InitialPedigreeForm form,
			HttpServletRequest request, String docId) {

		String tpName = form.getTpName();

		StringBuffer buffer = new StringBuffer("<PedigreeData>");
		buffer.append("<messageNm>PED_IN</messageNm>");
		buffer.append("<pedigreeId>" + docId + "</pedigreeId>");

		buffer.append("<ProductInfo>");
		buffer.append("<drugName>" + form.getName() + "</drugName>");
		buffer.append("<manufacturer>" + form.getManufacturer()
				+ "</manufacturer>");
		buffer.append("<productCode>" + form.getProductCode()
				+ "</productCode>");
		buffer.append("<dosageForm>" + form.getDosageForm() + "</dosageForm>");
		buffer.append("<strength>" + form.getStrength() + "</strength>");
		buffer.append("<containerSize>" + form.getContainerSize()
				+ "</containerSize>");
		buffer.append("</ProductInfo>");

		// Here need to append iteminfo
		String lotNos[] = request.getParameterValues("lotNumber");
		String quantity[] = request.getParameterValues("quantity");
		String itemSerialNumber[] = request
				.getParameterValues("itemSerialNumber");
		String expirationDate[] = request.getParameterValues("expirationDate");

		HttpSession session = request.getSession();
		session.setAttribute("lotNos", lotNos);
		session.setAttribute("quantity", quantity);
		session.setAttribute("itemSerialNumber", itemSerialNumber);
		session.setAttribute("expirationDate", expirationDate);
		for (int it = 0; it < lotNos.length; it++) {
			buffer.append("<ItemInfo>");
			buffer.append("<lot>" + lotNos[it] + "</lot><quantity>"
					+ quantity[it] + "</quantity>");
			buffer.append(" <expirationDate>" + expirationDate[it]
					+ "T00:00:00" + "</expirationDate></ItemInfo>");
		}

		StringBuffer b = new StringBuffer();

		b
				.append("for $i in collection('tig:///CatalogManager/TradingPartner')/TradingPartner[name='"
						+ form.getTpName() + "']");

		b.append(" return data($i/businessId)");
		String businessID = null;
		try {

			businessID = queryRunner.returnExecuteQueryStringsAsString(b.toString());
			if(businessID==null || businessID.equals("")){
				session.setAttribute("TPNameNotValid","true");
			}
			else{
				session.setAttribute("TPNameNotValid","false");
				
			}
			
			
		} catch (PersistanceException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
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

		buffer.append("<TransactionId>" + form.getTransactionId()
				+ "</TransactionId>");

		buffer.append("<TransactionType>" + form.getTransactionType()
				+ "</TransactionType>");
		buffer.append("<TransactionDate>" + form.getTransactionDate()
				+ "T00:00:00" + "</TransactionDate>");
		buffer.append("</TransactionInfo>");
		buffer.append("</PedigreeData>");

		return buffer.toString();
	}

	private static String getUUID() throws PersistanceException {
		StringBuffer buff = new StringBuffer();
		buff.append("import module namespace util = 'xquery:modules:util';");
		buff.append("let $docId := util:create-uuid() return $docId");
		return (queryRunner.returnExecuteQueryStringsAsString(buff.toString()));
	}

}
