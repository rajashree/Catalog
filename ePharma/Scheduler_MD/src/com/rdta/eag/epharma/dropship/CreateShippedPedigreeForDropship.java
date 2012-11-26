package com.rdta.eag.epharma.dropship;

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



import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import com.rdta.tlapi.xql.Connection;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.dom.DeferredElementImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.rdta.eag.epharma.commons.persistence.PersistanceException;
import com.rdta.eag.epharma.commons.persistence.QueryRunner;
import com.rdta.eag.epharma.commons.persistence.QueryRunnerFactory;
import com.rdta.eag.epharma.commons.xml.XMLUtil;


import com.rdta.eag.epharma.util.SendDHFormEmail;


public class CreateShippedPedigreeForDropship {
	
	private static Log log=LogFactory.getLog(CreateShippedPedigreeForDropship.class);
	static final QueryRunner queryRunner = QueryRunnerFactory.getInstance()
	.getDefaultQueryRunner();
	
	/**
	 * @param args
	 */
	
	public static String CreateShippedPedigree(String xmlString, String signerid, String deaNumber, String sourceRoutingCode, String fileName,Connection conn) throws Exception{
		// TODO Auto-generated method stub
		
		
		String qtoInsert = "";
		String envId = null;
		//xmlString = xmlString.replaceAll("&","&amp;");
		qtoInsert = "tlsp:CreateShippedPedigreeForDropShip_MD1("+xmlString+",'"+signerid+"','"+deaNumber+"','"+sourceRoutingCode+"')";
		log.info("Query for Create Pedigree : "+qtoInsert);
		System.out.println("Query for Create Pedigree : "+qtoInsert);
		qtoInsert = qtoInsert.replaceFirst("xmlns:pen", "xmlns");
		try {
			
			List data = queryRunner.returnExecuteQueryStringsNew(qtoInsert,conn);

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

				String query = "tlsp:InsertShippedPedigree("+XMLUtil.convertToString(ele,true)+",'"+fileName+"')";
				log.info("Query to insert ped envelope is: "+query.toString());
				envId = queryRunner.returnExecuteQueryStringsAsStringNew(query,conn);
				conn.commit();
			}


			return envId;

		} catch(PersistanceException e){
			log.error("Error in CreateShippedPedigree method........." +e);
			throw new PersistanceException(e);
		}catch (Exception ex) {
			ex.printStackTrace();
			log.error("Error in CreateShippedPedigree method........." +ex);
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



