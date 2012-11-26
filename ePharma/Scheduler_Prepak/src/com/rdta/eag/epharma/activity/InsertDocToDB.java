package com.rdta.eag.epharma.activity;

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
import java.io.StringBufferInputStream;
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


public class InsertDocToDB {
	private static Log log=LogFactory.getLog(InsertDocToDB.class);
	static final QueryRunner queryRunner = QueryRunnerFactory.getInstance()
	.getDefaultQueryRunner();
	static String envId = null;
	
	/**
	 * @param args
	 */
	
	private static boolean checkNDCExist(String ndc,Connection conn) throws PersistanceException {

		 
		String q="tlsp:ndcExists_MD('"+ndc+"')";
		log.info("check if NDC exists"+q);
		String result=queryRunner.returnExecuteQueryStringsAsStringNew(q,conn);
		if(result.equalsIgnoreCase("true"))
		{
			return true;
		}
		else{
			 return false;
		}


	}
	private static String getLine(String lineNo,String xml,Connection conn) throws PersistanceException {

		String quer="let $i :="+xml+" return $i/pedshipData/pedship[LineNo='"+lineNo+"']";
		String res=queryRunner.returnExecuteQueryStringsAsStringNew(quer,conn); 
		 return res;


	}
	public static String CreatePedigree(String xmlString,String signerid,String deaNumber,String sourceRoutingCode,String[] mail,String fileName,Connection conn) throws Exception{
		// TODO Auto-generated method stub
		String qtoInsert = "";
	//	xmlString = xmlString.replaceAll("&","&amp;amp;");
		qtoInsert = "tlsp:PEDSHIP_MD("+xmlString.toString().replaceAll("'","&apos;")+",'"+signerid+"','"+deaNumber+"','"+sourceRoutingCode+"')";
		log.info("Query for Create Pedigree : "+qtoInsert);
		qtoInsert = qtoInsert.replaceFirst("xmlns:pen", "xmlns");
		try {
			//List data = queryRunner.returnExecuteQueryStrings(qtoInsert);
			//String dataString = (String) data.get(0);
			List data = queryRunner.returnExecuteQueryStringsNew(qtoInsert,conn);


			for(int m=0;m<data.size();m++){
				String dataString = data.get(m).toString();
				String msg = "";
				 if(dataString.startsWith("Insufficient Quantity")){
					 String error[] = dataString.split(",");
					 if(error.length >1){
						
						 String line = getLine(error[3],xmlString,conn);
						 String binquery="tlsp:BinNumberExists_MD('"+error[1]+"','"+error[2]+"')";
							log.info("check if bin  exists"+binquery);
							String binExists=queryRunner.returnExecuteQueryStringsAsStringNew(binquery,conn);
							
						 if(checkNDCExist(error[1],conn)&& binExists.equals("true"))
						 {
							 String query1="for $i in collection('tig:///ePharma_MD/PedigreeBank')/PedigreeBank/InventoryOnHand";
							 query1=query1+" where $i/NDC='"+error[1]+"' and $i/BinInfo/BinNumber='"+error[2]+"'  return xs:integer(sum($i/BinInfo/LotInfo/Quantity) )";
							 String  quantityPresent=queryRunner.returnExecuteQueryStringsAsStringNew(query1,conn);
							 msg = "Error for LineNumber "+error[3]  +"  Quantity is insufficient for NDC : " + error[1]+" and BinNumber :"+error[2]+" Quantity Needed is "+error[4] +" Quantity Present "+quantityPresent+ "Line is "+line;
						 }
						 else if(checkNDCExist(error[1],conn)&& binExists.equals(""))
						 {
							 msg = "Error for LineNumber "+error[3]  +" NDC  " + error[1]+" and Bin number "+error[2]+" is not present. Quantity Needed is "+error[4] + "Line is "+line;
						 }
						 else{
							  
							 msg = "Error for LineNumber  "+error[3]  +"NDC  "+ error[1]+" does not exist in pedigree bank Line is "+line ;
							 
						 }
					  

					 }else msg = "Insufficient Quantity";
					 	log.info(msg);
					 SendDHFormEmail.sendMailToSupport(mail[0], mail[1] , mail[2],
						"ePharma Morris & Dickson Error",msg,mail[3], mail[4] );

					 }else{
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
		/*	for (int k = 0; k < i; k++) {
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
			} */

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
			//String query = "tig:insert-document('tig:///ePharma_MD/ShippedPedigree',"+XMLUtil.convertToString(ele,true)+")";
			//queryRunner.executeQuery(query);
			String query = "tlsp:InsertShippedPedigree("+XMLUtil.convertToString(ele,true)+",'"+fileName+"')";
			
			System.out.println("StoredProcedure is .....MMMM"+query);
			envId = queryRunner.returnExecuteQueryStringsAsStringNew(query,conn);

			conn.commit();
		   	 }
				 }

			return envId;

		} catch(PersistanceException e){
			log.error("Error in InsertDocToDB method........." +e);
			throw new PersistanceException(e);
		}catch (Exception ex) {
			ex.printStackTrace();
    		log.error("Error in InsertDocToDB method........." +ex);
    		throw new Exception(ex);
		}
		/*finally{
			TLConnectionPool.getTLConnectionPool().returnConnection(conn);
		}*/
	}

	public static String CreateManualPedigreeEnvelopeForPP(String xmlString,String signerid,String deaNumber,String sourceRoutingCode,String fileName,Connection conn) throws Exception{
		// TODO Auto-generated method stub
		
		
		
		String qtoInsert="";
		xmlString = xmlString.toString().replaceAll("'","&apos;");
		qtoInsert = "tlsp:PEDSHIPManual_PP($1,'"+signerid+"','"+deaNumber+"','"+sourceRoutingCode+"')";
		qtoInsert = qtoInsert.replaceFirst("xmlns:pen", "xmlns");
		StringBufferInputStream sbIns = new StringBufferInputStream(xmlString);
		//qtoInsert = qtoInsert.replaceFirst("xmlns:pen", "xmlns");
//		String qtoInsert = "";
//		qtoInsert = "tlsp:PEDSHIPManual_PP("+xmlString.toString().replaceAll("'","&apos;")+",'"+signerid+"','"+deaNumber+"','"+sourceRoutingCode+"')";
//		log.info("Query for Create Pedigree : "+qtoInsert);
//		qtoInsert = qtoInsert.replaceFirst("xmlns:pen", "xmlns");
		try {
			//List data = queryRunner.returnExecuteQueryStrings(qtoInsert);
			//String dataString = (String) data.get(0);
			//List data = queryRunner.returnExecuteQueryStringsNew(qtoInsert,conn);
			List data = queryRunner.returnExecuteQueryWithStreamStringsNew(qtoInsert,conn, sbIns);
			System.out.println("size **** : "+data.size());
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

			/*for (int k = 0; k < i; k++) {
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
			*/
			log.info("EnvelopeNode after appending namespace : "+XMLUtil.convertToString(ele,true));
			//String query = "tig:insert-document('tig:///ePharma_MD/ShippedPedigree',"+XMLUtil.convertToString(ele,true)+")";
			//queryRunner.executeQuery(query);
			//String query = "tlsp:InsertShippedPedigree("+XMLUtil.convertToString(ele,true)+",'"+fileName+"')";
			//queryRunner.executeQuery(query);
//			System.out.println("filename filename"+fileName);
//			System.out.println("convert to string"+XMLUtil.convertToString(ele,true));
//			String query = "tlsp:InsertShippedPedigree("+XMLUtil.convertToString(ele,true)+",'"+fileName+"')";
//			System.out.println("queryqueryqueryqueryquery"+query);
//			envId = queryRunner.returnExecuteQueryStringsAsStringNew(query,conn);
//			conn.commit();
			
			String query = "tlsp:InsertShippedPedigree($1,'"+fileName+"')";
			System.out.println("queryqueryqueryqueryquery"+query);

			sbIns = 
			    new StringBufferInputStream(XMLUtil.convertToString(ele,true));
			envId = queryRunner.returnExecuteQueryStreamAsStringNew(query,conn, sbIns);
			conn.commit();
			}
			System.out.println("The list of envelopes are:::"+envId);
			
			return envId;

		} catch(PersistanceException e){
			log.error("Error in InsertDocToDB method........." +e);
			throw new PersistanceException(e);
		}catch (Exception ex) {
			ex.printStackTrace();
			
			log.error("Error in InsertDocToDB method........." +ex);
			throw new Exception(ex);
		}

	}

	public static List CreateRepackManualPedigreeEnvelopeForPP(String xmlString,String signerid,String deaNumber,String sourceRoutingCode,String fileName,Connection conn) throws Exception{
		// TODO Auto-generated method stub
		List list=new ArrayList();
		xmlString = xmlString.toString().replaceAll("'","&apos;");
		
		String qtoInsert = "";
		qtoInsert = "tlsp:PEDSHIPRepackManual_PP($1,'"+signerid+"','"+deaNumber+"','"+sourceRoutingCode+"')";
		log.info("Query for Create Pedigree : "+qtoInsert);
		System.out.println("Query for Create Pedigree : "+qtoInsert);
		qtoInsert = qtoInsert.replaceFirst("xmlns:pen", "xmlns");
		
		StringBufferInputStream sbIns = new StringBufferInputStream(xmlString);
		
		try {
			
			 List data = queryRunner.returnExecuteQueryWithStreamStringsNew(qtoInsert,conn, sbIns);
			//List data = queryRunner.returnExecuteQueryStringsNew(qtoInsert,conn);
			for(int m=0;m<data.size();m++){
				String dataString = data.get(m).toString();
				if(dataString.equalsIgnoreCase("NOPTPExists")){
					envId = "NOPTPExists";
					list.add(envId);
					
				}
				else{
					
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
			System.out.println("filename filename"+fileName);
			System.out.println("convert to string"+XMLUtil.convertToString(ele,true));
			//String query = "tlsp:InsertShippedPedigree("+XMLUtil.convertToString(ele,true)+",'"+fileName+"')";
			//System.out.println("queryqueryqueryqueryquery"+query);
			
			
			
			String query = "tlsp:InsertShippedPedigree($1,'"+fileName+"')";
//			System.out.println("queryqueryqueryqueryquery"+query);
//
			sbIns = 
			    new StringBufferInputStream(XMLUtil.convertToString(ele,true));
			envId = queryRunner.returnExecuteQueryStreamAsStringNew(query,conn, sbIns);
			//envId = queryRunner.returnExecuteQueryStringsAsStringNew(query,conn);
			list.add(envId);
			conn.commit();
			
			}
			}
				
			return list;
			
			

		} catch(PersistanceException e){
			log.error("Error in InsertDocToDB method........." +e);
			throw new PersistanceException(e);
		}catch (Exception ex) {
			ex.printStackTrace();
			
			log.error("Error in InsertDocToDB method........." +ex);
			throw new Exception(ex);
		}

	}
	public static String CreatePedigreeEnvelopeForDropShip(String xmlString, String signerid, String deaNumber, String sourceRoutingCode, String[] mail, String fileName,Connection conn) throws Exception{
		// TODO Auto-generated method stub
		String qtoInsert = "";
		xmlString = xmlString.replaceAll("&","&amp;");
		qtoInsert = "tlsp:CreateShippedPedigreeForDropShip_MD1("+xmlString.toString().replaceAll("'","&apos;")+",'"+signerid+"','"+deaNumber+"','"+sourceRoutingCode+"')";
		log.info("Query for Create Pedigree : "+qtoInsert);
		qtoInsert = qtoInsert.replaceFirst("xmlns:pen", "xmlns");
		try {
			//List data = queryRunner.returnExecuteQueryStrings(qtoInsert);
			//String dataString = (String) data.get(0);
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

				/*for (int k = 0; k < i; k++) {
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
				}*/
				log.info("EnvelopeNode after appending namespace : "+XMLUtil.convertToString(ele,true));
				//String query = "tig:insert-document('tig:///ePharma_MD/ShippedPedigree',"+XMLUtil.convertToString(ele,true)+")";
				//queryRunner.executeQuery(query);
				/*String query = "tlsp:InsertShippedPedigree("+XMLUtil.convertToString(ele,true)+",'"+fileName+"')";
				 queryRunner.executeQuery(query); */

				String query = "tlsp:InsertShippedPedigree("+XMLUtil.convertToString(ele,true)+",'"+fileName+"')";
				log.info("Query to insert ped envelope is: "+query.toString());
				envId = queryRunner.returnExecuteQueryStringsAsStringNew(query,conn);
				conn.commit();
			}


			return envId;

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



