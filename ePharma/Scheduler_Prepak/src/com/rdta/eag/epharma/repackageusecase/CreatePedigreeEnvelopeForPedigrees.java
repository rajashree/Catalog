package com.rdta.eag.epharma.repackageusecase;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.rdta.eag.epharma.commons.persistence.PersistanceException;
import com.rdta.eag.epharma.commons.persistence.QueryRunner;
import com.rdta.eag.epharma.commons.persistence.QueryRunnerFactory;
import com.rdta.eag.epharma.commons.persistence.pool.TLConnectionPool;
import com.rdta.eag.epharma.commons.xml.XMLUtil;
import com.rdta.tlapi.xql.Connection;

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

public class CreatePedigreeEnvelopeForPedigrees {
	
	private static Log log=LogFactory.getLog(CreatePedigreeEnvelopeForPedigrees.class);
	static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	static Connection conn = null;
	
	public static String preparePedigreeEnvelope(String xmlNode, String destRoutingCode) throws Exception{
		
		conn = TLConnectionPool.getTLConnectionPool().borrowConnection();
		conn.setAutoCommit(false);
		
		System.out.println("***Inside method ******");
		StringBuffer buff = new StringBuffer();
		String envId = "";
		try{
		buff.append("tlsp:createPedigreeEnvelopeForRepackage("+xmlNode+",'"+destRoutingCode+"')");
		//String envDoc = queryRunner.returnExecuteQueryStringsAsStringNew(buff.toString(),conn);
				
		List data = queryRunner.returnExecuteQueryStringsNew(buff.toString(),conn);


		for(int m=0;m<data.size();m++){
			String dataString = data.get(m).toString();
			log.info("REsult Envelope NOde Before IF : "+dataString);

			log.info("REsult Envelope NOde : "+dataString);
			dataString = dataString
			.replaceFirst(
					"<pedigreeEnvelope>",
					"<pedigreeEnvelope xmlns='urn:epcGlobal:PedigreeEnvelope:xsd:1'>");

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

						Node shippedPed = (Node) getNode(doc, shipp,"urn:epcGlobal:Pedigree:xsd:1");
						Node shippedNode = (Node)getNode(doc,shippedPed,"id",XMLUtil.getValue(shipp,"@id"));


						XMLUtil.putNode(e, ".", shippedNode);
					}

					Node sig = (Node) getNode(doc, childList.item(1),"http://www.w3.org/2000/09/xmldsig#");
					XMLUtil.putNode(e, ".", sig);
					ele.replaceChild(e, pedNode);
				}
			}
			log.info("EnvelopeNode after appending namespace : "+XMLUtil.convertToString(ele,true));
			
			String query = "tlsp:InsertShippedPedigreeRepackage("+XMLUtil.convertToString(ele,true)+")";
			log.info("Query to insert ped envelope is: "+query.toString());
			envId = queryRunner.returnExecuteQueryStringsAsStringNew(query,conn);		
			conn.commit();
			
		}
		}catch(Exception ex){
			conn.rollback();
			throw ex;
		}
		finally{
			try {
			log.error("returning the connection to pool in finally");
				TLConnectionPool.getTLConnectionPool().returnConnection(conn);

		}catch(Exception e)
		{
			log.error("error in returning cvonnection to pool "+e);
			throw e;
		}

		}
		return envId;
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

		public static Element getNode(Document doc1, Node temp1,String attrName, String value) {


			Element temp = doc1.createElement(temp1.getNodeName());
			String exist =XMLUtil.getValue(temp1,"@xmlns");
			if(exist != null ){
				Attr attr = doc1.createAttribute("xmlns");
				attr.setValue(exist);
				temp.setAttributeNode(attr);
			}
			Attr attr = doc1.createAttribute(attrName);
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
