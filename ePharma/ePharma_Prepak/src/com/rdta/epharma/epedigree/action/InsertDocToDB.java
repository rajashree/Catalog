
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
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.dom.DeferredElementImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;

public class InsertDocToDB {
	private static Log log=LogFactory.getLog(ShippingManagerAction.class);
	static final QueryRunner queryRunner = QueryRunnerFactory.getInstance()
	.getDefaultQueryRunner();
	
	/**
	 * @param args
	 */
	public static void CreatePedigree(String RefNum,String orderNo,String trType,String flag,String pedID,String sessionID) throws Exception{
		// TODO Auto-generated method stub
		String qtoInsert = "";
		if(trType.equalsIgnoreCase("PurchaseOrderNumber")){
			qtoInsert = "tlsp:CreateShippedPedigreeForOrders('"+RefNum+"','"+orderNo+"','"+trType+"','"+flag+"','"+pedID+"','"+sessionID+"')";
		}else
			qtoInsert = "tlsp:CreateShippedPedigreenew('"+RefNum+"','"+trType+"','"+flag+"','"+pedID+"','"+sessionID+"')";
		System.out.println("Query for Create Pedigree : "+qtoInsert);
		qtoInsert = qtoInsert.replaceFirst("xmlns:pen", "xmlns");
		try {
			List data = queryRunner.returnExecuteQueryStrings(qtoInsert);
			String dataString = (String) data.get(0);
			System.out.println(dataString);
			dataString = dataString
			.replaceFirst(
					"<pedigreeEnvelope>",
					"<pedigreeEnvelope xmlns='urn:epcGlobal:PedigreeEnvelope:xsd:1' xmlns:ped='urn:epcGlobal:Pedigree:xsd:1' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:schemaLocation='urn:epcGlobal:PedigreeEnvelope:xsd:1"
					+ "PedigreeEnvelope_20060331.xsd'>");
			
			Node rootNode = XMLUtil.parse(dataString);
			Document doc = rootNode.getOwnerDocument();
			Element ele = doc.getDocumentElement();
			NodeList myList = ele.getElementsByTagName("pedigree");
			int i = myList.getLength();
			for (int k = 0; k < i; k++) {
				if(myList.item(k).getParentNode().getNodeName().equalsIgnoreCase("pedigreeEnvelope")){
					Element e = doc.createElement("pedigree");
					Attr attr = doc.createAttribute("xmlns");
					attr.setValue("urn:epcGlobal:Pedigree:xsd:1");
					e.setAttributeNode(attr);
					Element pedNode = (Element) myList.item(k);
					NodeList childList = pedNode.getChildNodes();
					for (int p = 0; p < childList.getLength() - 1; p++) {
						Node shipp = childList.item(p);
						XMLUtil.putNode(e, ".", shipp);
					}
					Node sig = (Node) getNode(doc, childList.item(1),
					"http://www.w3.org/2000/09/xmldsig#");
					XMLUtil.putNode(e, ".", sig);
					ele.replaceChild(e, pedNode);
				}
			}
			String query = "tig:insert-document('tig:///ePharma/ShippedPedigree',"+XMLUtil.convertToString(ele,true)+")";
			queryRunner.executeQuery(query);
			
		} catch(PersistanceException e){
			log.error("Error in InsertDocToDB method........." +e);
			throw new PersistanceException(e);
		}catch (Exception ex) {
			ex.printStackTrace();
    		log.error("Error in InsertDocToDB method........." +ex);
    		throw new Exception(ex);
		}
		
	}
	
	
	public void createReceivedPedigree(String pedId,String sessionId) throws Exception{
		//public static void main(String [] args){
		
		// TODO Auto-generated method stub
		String qtoInsert = "";
		
		qtoInsert = "tlsp:CreateReceivedPedigreeForPedigreesNew('"+pedId+"','"+sessionId+"')";
		//qtoInsert = "tlsp:CreateReceivedPedigreeForPedigreesNew('S1148462576593','12700120060526115359')";
		
		qtoInsert = qtoInsert.replaceFirst("xmlns:pen", "xmlns");
		try {
			List data = queryRunner.returnExecuteQueryStrings(qtoInsert);
			String dataString = (String) data.get(0);
			
			System.out.println(dataString);
			dataString = dataString
			.replaceFirst(
					"<pedigree>",
			"<pedigree xmlns='urn:epcGlobal:Pedigree:xsd:1' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:schemaLocation='urn:epcGlobal:Pedigree:xsd:1 PedigreeSchema_20060331.xsd'>");
			
			Node rootNode = XMLUtil.parse(dataString);
			
			System.out.println(" The Node for inserting is : "+XMLUtil.convertToString(rootNode,true));
			String pedigree = XMLUtil.convertToString(rootNode,true);
			/*String query = "tlsp:PedigreeBankUpdateAfterReceivedPedigree('"+pedigree+"')";
			queryRunner.executeQuery(query);*/
			
			String query = "tig:insert-document('tig:///ePharma/ShippedPedigree',"+XMLUtil.convertToString(rootNode,true)+")";
			queryRunner.returnExecuteQueryStringsAsString(query);
			
			StringBuffer buff = new StringBuffer();
			buff.append("for $i in collection('tig:///ePharma/ShippedPedigree')/*:pedigree/*:receivedPedigree ");
			buff.append(" where $i/*:pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber = '"+pedId+"' " );
			buff.append(" return data($i/*:documentInfo/*:serialNumber) ");
			String recvid = queryRunner.returnExecuteQueryStringsAsString(buff.toString());
			
			String query1 = "tlsp:CreateSignatureToReceivedPedigree('"+recvid+"')";
			queryRunner.executeQuery(query1);
			
			
		} catch(PersistanceException e){
			log.error("Error in InsertDocToDB method........." +e);
			throw new PersistanceException(e);
		}catch (Exception ex) {
			ex.printStackTrace();
    		log.error("Error in InsertDocToDB method........." +ex);
    		throw new Exception(ex);
		}
		
	}
	
	
	public static Element getNode(Document doc1, Node temp1, String value) {
		Element temp = doc1.createElement(temp1.getNodeName());
		Attr attr = doc1.createAttribute("xmlns");
		attr.setValue(value);
		temp.setAttributeNode(attr);
		NodeList sigList = temp1.getChildNodes();
		int size = sigList.getLength();
		for (int i = 0; i < size; i++) {
			// temp.appendChild(sigList.item(i));
			XMLUtil.putNode(temp, ".", sigList.item(i));
		}
		
		return temp;
		
	}
}
