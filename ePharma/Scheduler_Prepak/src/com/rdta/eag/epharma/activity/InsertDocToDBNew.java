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



import java.util.ArrayList;
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
import com.rdta.eag.epharma.commons.xml.XMLUtil;
import com.rdta.eag.epharma.util.SendDHFormEmail;
//import com.rdta.eag.security.CreateSigDOM;
import com.rdta.tlapi.xql.Connection;



public class InsertDocToDBNew {
	private static Log log=LogFactory.getLog(InsertDocToDBNew.class);
	static final QueryRunner queryRunner = QueryRunnerFactory.getInstance()
	.getDefaultQueryRunner();
	
	public static ArrayList ndcList=new ArrayList();
	public static ArrayList binNumList=new ArrayList();
	
	
	/**
	 * @param args
	 */
	
	private static boolean checkNDCExist(String ndc,Connection conn) throws PersistanceException {

		 
		String query="tlsp:ndcExists_MD('"+ndc+"')";
		log.info("check if NDC exists"+query);
		String result=queryRunner.returnExecuteQueryStringsAsStringNew(query,conn);
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
		log.info("******** Inside InsertDocToDB *********");
		String qtoInsert = "";
		String envId = null;
		String keyStoreFile ="C:/security/keys/SW_keystore";
		String keyStoreFilePassword = "md1841";
		String keyAlias = "MDAlias";
		String referenceId = "";
		String signatureId = "";
		
		log.info("ndcList size : "+ndcList.size()+" binNumList size : "+binNumList.size());
		StringBuffer excepString = new StringBuffer();
		excepString.append("<root>");
		for(int m=0;m<ndcList.size();m++){
			excepString.append("<NDCBIN>");
			excepString.append("<NDC>");
			excepString.append(ndcList.get(m));
			excepString.append("</NDC>");
			excepString.append("<BinNumber>");
			excepString.append(binNumList.get(m));
			excepString.append("</BinNumber>");
			excepString.append("</NDCBIN>");
		}
		excepString.append("</root>");
		
		//xmlString = xmlString.replaceAll("&","&amp;");
		qtoInsert = "tlsp:PEDSHIP_MD_1("+xmlString.toString().replaceAll("'","&apos;")+",'"+signerid+"','"+deaNumber+"','"+sourceRoutingCode+"',"+excepString.toString()+")";
		log.info("Query for Create Pedigree : "+qtoInsert);
		//qtoInsert = qtoInsert.replaceFirst("xmlns:pen", "xmlns");
		
		try {
			//List data = new ArrayList();
			//data.add(envelope);
			List data = queryRunner.returnExecuteQueryStringsNew(qtoInsert,conn);
			
			for(int m=0;m<data.size();m++){
				
				String dataString = data.get(m).toString();
				String msg = "";
				String subject = "";
				 if(dataString.startsWith("Insufficient Quantity")){
					 String error[] = dataString.split(",");
					 if(error.length >1){
						 subject = mail[5];
						 String line = getLine(error[3],xmlString,conn);
						 String binquery="tlsp:BinNumberExists_MD('"+error[1]+"','"+error[2]+"')";
							log.info("check if bin  exists"+binquery);
							String binExists=queryRunner.returnExecuteQueryStringsAsStringNew(binquery,conn);
							ndcList.add(error[1]);
							binNumList.add(error[2]);
							log.info(" Freeze status *********** "+error[5]);
							if(error[5].equalsIgnoreCase("Freezed")){
								subject = mail[6];
								msg = "Error for LineNumber "+error[3]  +". The NDC : " + error[1]+" and BinNumber :"+error[2]+" is Frozen. Line is "+line;
							}else
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
								subject,msg,mail[3], mail[4] );
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
			log.info("Number of Pedigrees in Envelope : "+i);
			for (int k = 0; k < i; k++) {
				if(myList.item(k).getParentNode().getNodeName().equalsIgnoreCase("pedigreeEnvelope")){
					Element e = doc.createElement("pedigree");
					Attr attr = doc.createAttribute("xmlns");
					attr.setValue("urn:epcGlobal:Pedigree:xsd:1");
					e.setAttributeNode(attr);
					Element pedNode = (Element) myList.item(k);
					System.out.println("pedigree Node :" +XMLUtil.convertToString(pedNode,true));
					NodeList childList = pedNode.getChildNodes();
					for (int p = 0; p < childList.getLength() ; p++) {
						Node shipp = childList.item(p);
						Node shippedPed = (Node) getNode(doc, shipp,"urn:epcGlobal:Pedigree:xsd:1");
						Node shippedNode = (Node)getNode(doc,shippedPed,"id",XMLUtil.getValue(shipp,"@id"));
						referenceId = "#"+XMLUtil.getValue(shipp,"@id");
						log.info("REference Id : "+referenceId);
						XMLUtil.putNode(e, ".", shippedNode);
					}
					
					ele.replaceChild(e, pedNode);
					
					StringBuffer buff = new StringBuffer();
					buff.append("import module namespace util = 'xquery:modules:util'; concat('_',util:create-uuid())");
					signatureId = queryRunner.returnExecuteQueryStringsAsStringNew(buff.toString(),conn);
					
					// Calling Create Signature Method .. Add Necessary digitalSignature jar and remove comments.
					
					
					/*String signNode = CreateSigDOM.signPedigreeString(XMLUtil.convertToString(e,true),keyStoreFile,keyStoreFilePassword,keyAlias,referenceId,signatureId);
					
				   // ele.removeChild(e);
					
					Node temp = XMLUtil.parse(signNode);
					
					Element ep = doc.createElement("pedigree");
					Attr attr1 = doc.createAttribute("xmlns");
					attr1.setValue("urn:epcGlobal:Pedigree:xsd:1");
					ep.setAttributeNode(attr1);
					Node shippedPedNode = XMLUtil.getNode(temp,"shippedPedigree");
					Node shippedPed = (Node) getNode(doc, shippedPedNode,"urn:epcGlobal:Pedigree:xsd:1");
					Node shippedNode = (Node)getNode(doc,shippedPed,"id",XMLUtil.getValue(shippedPedNode,"@id"));
					
					XMLUtil.putNode(ep, ".", shippedNode);
					XMLUtil.putNode(ep, ".", XMLUtil.getNode(temp,"Signature"));			
									
					//ep.appendChild(XMLUtil.parse(signNode));
					ele.insertBefore(ep,e);
					ele.removeChild(e);
					//ele.appendChild(ep);
					
					//doc.appendChild(ele);
					//Element sign = doc.createElement(pedigreeNodeAfterSign.getNodeName());
					//XMLUtil.putNode(sign,".",pedigreeNodeAfterSign);
				
					
					//ele.replaceChild(pedNode,sign);*/	
				}
			}
			
			log.info("EnvelopeNode after appending namespace and sign: "+XMLUtil.convertToString(ele,true));
			
			//Query for inserting PedigreeEnvelope document in ShippedPedigree collection
			String query = "tlsp:InsertShippedPedigree_1("+XMLUtil.convertToString(ele,true)+",'ss')";
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
	public static void main(String args[]){
		
		try{
				
		//InsertDocToDBNew.CreatePedigree("<pedigreeEnvelope><version>20060331</version><serialNumber>urn:uuid:fff36a4c-4bcd-11c0-c001-a4edefe46297</serialNumber><date>2006-06-06</date><sourceRoutingCode>MDW</sourceRoutingCode><destinationRoutingCode>DDC</destinationRoutingCode><container><containerCode>25472</containerCode><shipmentHandle>6326332</shipmentHandle><pedigreeHandle><serialNumber>urn:uuid:fff36a4c-4bcd-11c3-c001-a4edefe46297</serialNumber></pedigreeHandle></container><container><containerCode>25472</containerCode><shipmentHandle>6326332</shipmentHandle><pedigreeHandle><serialNumber>urn:uuid:fff36a4c-4bcd-1480-c001-a4edefe46297</serialNumber></pedigreeHandle></container><pedigree><shippedPedigree id=\"_fff36a4c-4bcd-11c4-c001-a4edefe46297\"><documentInfo><serialNumber>urn:uuid:fff36a4c-4bcd-11c3-c001-a4edefe46297</serialNumber><version>20060331</version></documentInfo><initialPedigree><productInfo><drugName>ZOCOR 10 MG TABLET</drugName><manufacturer>MERCK SHARP  DOHME #1  *CBEDI</manufacturer><productCode type=\"NDC\">00006-3435-99</productCode><dosageForm>TABLET</dosageForm><strength>10MG</strength><containerSize>30</containerSize></productInfo><itemInfo><lot>F105541</lot><expirationDate>2009-04-01</expirationDate><quantity>10</quantity></itemInfo><transactionInfo><senderInfo><businessAddress><businessName>MERCK SHARP  DOHME #1  *CBEDI</businessName><street1>*ORDER DEPT*</street1><street2>Not Available</street2><city>C/S REP WENDY  EXT #1+ 5852</city><stateOrRegion>NA</stateOrRegion><postalCode>760115648</postalCode><country>US</country></businessAddress><licenseNumber>PM0028515</licenseNumber><contactInfo><name>Not Available</name><telephone>8006372579</telephone><email>email@mfg.com</email></contactInfo></senderInfo><recipientInfo><businessAddress><businessName>Morris and Dickson Co., LLC</businessName><street1>410 Kay Lane</street1><street2></street2><city>Shreveport</city><stateOrRegion>LA</stateOrRegion><postalCode>71115</postalCode><country>USA</country></businessAddress><shippingAddress><businessName>Morris and Dickson Co., LLC</businessName><street1>10301 Highway 1 South</street1><street2></street2><city>Shreveport</city><stateOrRegion>LA</stateOrRegion><postalCode>71115</postalCode><country>USA</country></shippingAddress><licenseNumber>RM0314790</licenseNumber><contactInfo><name>Paul West</name><telephone>800-899-9228</telephone><email>pwest@MorrisDickson.com</email></contactInfo></recipientInfo><transactionIdentifier><identifier>14451</identifier><identifierType>PurchaseOrderNumber</identifierType></transactionIdentifier><transactionType>Sale</transactionType><transactionDate>2006-06-30</transactionDate></transactionInfo><receivingInfo><dateReceived>2006-07-06</dateReceived></receivingInfo></initialPedigree><itemInfo><lot>F105541</lot><expirationDate>2009-04-01</expirationDate><quantity>1</quantity></itemInfo><transactionInfo><senderInfo><businessAddress><businessName>Morris and Dickson Co., LLC</businessName><street1>410 Kay Lane</street1><street2></street2><city>Shreveport</city><stateOrRegion>LA</stateOrRegion><postalCode>71115</postalCode><country>USA</country></businessAddress><shippingAddress><businessName>Morris and Dickson Co., LLC</businessName><street1>10301 Highway 1 South</street1><street2></street2><city>Shreveport</city><stateOrRegion>LA</stateOrRegion><postalCode>71115</postalCode><country>USA</country></shippingAddress><licenseNumber>RM0314790</licenseNumber><contactInfo><name>Paul West</name><telephone>800-899-9228</telephone><email>pwest@MorrisDickson.com</email></contactInfo></senderInfo><recipientInfo><businessAddress><businessName>OAKVIEW PHARMACY</businessName><street1>1106 STUMPF BLVD.</street1><street2>Not Available</street2><city>GRETNA</city><stateOrRegion>LA</stateOrRegion><postalCode>70053</postalCode><country>US</country></businessAddress><shippingAddress><businessName>OAKVIEW PHARMACY</businessName><street1>1106 STUMPF BLVD.</street1><street2>Not Available</street2><city>GRETNA</city><stateOrRegion>LA</stateOrRegion><postalCode>70053</postalCode><country>US</country></shippingAddress><licenseNumber>AM3396430</licenseNumber></recipientInfo><transactionIdentifier><identifier>25472</identifier><identifierType>PurchaseOrderNumber</identifierType></transactionIdentifier><transactionType>Sale</transactionType><transactionDate>2006-07-07</transactionDate></transactionInfo><signatureInfo><signerInfo><name>Paul M.Dickson</name><title>COO</title><telephone>318-797-7900</telephone><email>PedigreeAdmin@MorrisDickson.com</email></signerInfo><signatureDate>2006-06-06T21:22:06</signatureDate><signatureMeaning>Certified</signatureMeaning></signatureInfo></shippedPedigree></pedigree><pedigree><shippedPedigree id=\"_fff36a4c-4bcd-1481-c001-a4edefe46297\"><documentInfo><serialNumber>urn:uuid:fff36a4c-4bcd-1480-c001-a4edefe46297</serialNumber><version>20060331</version></documentInfo><initialPedigree><productInfo><drugName>ZOCOR 10 MG TABLET</drugName><manufacturer>MERCK SHARP  DOHME #1  *CBEDI</manufacturer><productCode type=\"NDC\">00006-3435-99</productCode><dosageForm>TABLET</dosageForm><strength>10MG</strength><containerSize>30</containerSize></productInfo><itemInfo><lot>F10555</lot><expirationDate>2009-04-01</expirationDate><quantity>12</quantity></itemInfo><transactionInfo><senderInfo><businessAddress><businessName>MERCK SHARP  DOHME #1  *CBEDI</businessName><street1>*ORDER DEPT*</street1><street2>Not Available</street2><city>C/S REP WENDY  EXT #1+ 5852</city><stateOrRegion>NA</stateOrRegion><postalCode>760115648</postalCode><country>US</country></businessAddress><licenseNumber>PM0028515</licenseNumber><contactInfo><name>Not Available</name><telephone>8006372579</telephone><email>email@mfg.com</email></contactInfo></senderInfo><recipientInfo><businessAddress><businessName>Morris and Dickson Co., LLC</businessName><street1>410 Kay Lane</street1><street2></street2><city>Shreveport</city><stateOrRegion>LA</stateOrRegion><postalCode>71115</postalCode><country>USA</country></businessAddress><shippingAddress><businessName>Morris and Dickson Co., LLC</businessName><street1>10301 Highway 1 South</street1><street2></street2><city>Shreveport</city><stateOrRegion>LA</stateOrRegion><postalCode>71115</postalCode><country>USA</country></shippingAddress><licenseNumber>RM0314790</licenseNumber><contactInfo><name>Paul West</name><telephone>800-899-9228</telephone><email>pwest@MorrisDickson.com</email></contactInfo></recipientInfo><transactionIdentifier><identifier>14451</identifier><identifierType>PurchaseOrderNumber</identifierType></transactionIdentifier><transactionType>Sale</transactionType><transactionDate>2006-06-30</transactionDate></transactionInfo><receivingInfo><dateReceived>2006-07-06</dateReceived></receivingInfo></initialPedigree><itemInfo><lot>F10555</lot><expirationDate>2009-04-01</expirationDate><quantity>1</quantity></itemInfo><transactionInfo><senderInfo><businessAddress><businessName>Morris and Dickson Co., LLC</businessName><street1>410 Kay Lane</street1><street2></street2><city>Shreveport</city><stateOrRegion>LA</stateOrRegion><postalCode>71115</postalCode><country>USA</country></businessAddress><shippingAddress><businessName>Morris and Dickson Co., LLC</businessName><street1>10301 Highway 1 South</street1><street2></street2><city>Shreveport</city><stateOrRegion>LA</stateOrRegion><postalCode>71115</postalCode><country>USA</country></shippingAddress><licenseNumber>RM0314790</licenseNumber><contactInfo><name>Paul West</name><telephone>800-899-9228</telephone><email>pwest@MorrisDickson.com</email></contactInfo></senderInfo><recipientInfo><businessAddress><businessName>OAKVIEW PHARMACY</businessName><street1>1106 STUMPF BLVD.</street1><street2>Not Available</street2><city>GRETNA</city><stateOrRegion>LA</stateOrRegion><postalCode>70053</postalCode><country>US</country></businessAddress><shippingAddress><businessName>OAKVIEW PHARMACY</businessName><street1>1106 STUMPF BLVD.</street1><street2>Not Available</street2><city>GRETNA</city><stateOrRegion>LA</stateOrRegion><postalCode>70053</postalCode><country>US</country></shippingAddress><licenseNumber>AM3396430</licenseNumber></recipientInfo><transactionIdentifier><identifier>25472</identifier><identifierType>PurchaseOrderNumber</identifierType></transactionIdentifier><transactionType>Sale</transactionType><transactionDate>2006-07-07</transactionDate></transactionInfo><signatureInfo><signerInfo><name>Paul M.Dickson</name><title>COO</title><telephone>318-797-7900</telephone><email>PedigreeAdmin@MorrisDickson.com</email></signerInfo><signatureDate>2006-06-06T21:22:06</signatureDate><signatureMeaning>Certified</signatureMeaning></signatureInfo></shippedPedigree></pedigree></pedigreeEnvelope>");
			
		// Calling Create pedigree method	
		//InsertDocToDBNew.CreatePedigree("<root><pedshipData><pedship><RecordIdentifier>AP</RecordIdentifier><PullTimeMMDDYYhhmmss>2006-07-07 14:19:00</PullTimeMMDDYYhhmmss><QtyPulled>1</QtyPulled><NDC>00006-3435-99</NDC><InvoiceNo>6326332</InvoiceNo><InvoiceDate>2006-07-07</InvoiceDate><PONumber>25472</PONumber><CustomerName>OAKVIEW & PHARMACY</CustomerName><CustomerAddressLine1>1106 STUMPF BLVD.</CustomerAddressLine1><CustomerAddressLine2>Not Available</CustomerAddressLine2><CustomerCity>GRETNA</CustomerCity><State>LA</State><CustomerZip>70053</CustomerZip><Country>US</Country><ShipToCustomerName>OAKVIEW PHARMACY</ShipToCustomerName><ShipToCustomerAddressLine1>1106 STUMPF BLVD.</ShipToCustomerAddressLine1><ShipToCustomerAddressLine2>Not Available</ShipToCustomerAddressLine2><ShipToCustomerCity>GRETNA</ShipToCustomerCity><ShipToState>LA</ShipToState><ShipToCustomerZip>70053</ShipToCustomerZip><ShipToCountry>US</ShipToCountry><UniqueBoxID>26526001</UniqueBoxID><BinLocation>K4405</BinLocation><CustomerDea>AM3396430</CustomerDea></pedship><pedship><RecordIdentifier>AP</RecordIdentifier><PullTimeMMDDYYhhmmss>2006-07-07 14:19:00</PullTimeMMDDYYhhmmss><QtyPulled>1</QtyPulled><NDC>00006-3435-99</NDC><InvoiceNo>6326332</InvoiceNo><InvoiceDate>2006-07-07</InvoiceDate><PONumber>25472</PONumber><CustomerName>OAKVIEW PHARMACY</CustomerName><CustomerAddressLine1>1106 STUMPF BLVD.</CustomerAddressLine1><CustomerAddressLine2>Not Available</CustomerAddressLine2><CustomerCity>GRETNA</CustomerCity><State>LA</State><CustomerZip>70053</CustomerZip><Country>US</Country><ShipToCustomerName>OAKVIEW PHARMACY</ShipToCustomerName><ShipToCustomerAddressLine1>1106 STUMPF BLVD.</ShipToCustomerAddressLine1><ShipToCustomerAddressLine2>Not Available</ShipToCustomerAddressLine2><ShipToCustomerCity>GRETNA</ShipToCustomerCity><ShipToState>LA</ShipToState><ShipToCustomerZip>70053</ShipToCustomerZip><ShipToCountry>US</ShipToCountry><UniqueBoxID>26526001</UniqueBoxID><BinLocation>K4406</BinLocation><CustomerDea>AM3396430</CustomerDea></pedship><pedship><RecordIdentifier>AP</RecordIdentifier><PullTimeMMDDYYhhmmss>2006-07-07 14:19:00</PullTimeMMDDYYhhmmss><QtyPulled>1</QtyPulled><NDC>00002-3239-30</NDC><InvoiceNo>6326332</InvoiceNo><InvoiceDate>2006-07-07</InvoiceDate><PONumber>25472</PONumber><CustomerName>OAKVIEW PHARMACY</CustomerName><CustomerAddressLine1>1106 STUMPF BLVD.</CustomerAddressLine1><CustomerAddressLine2>Not Available</CustomerAddressLine2><CustomerCity>GRETNA</CustomerCity><State>LA</State><CustomerZip>70053</CustomerZip><Country>US</Country><ShipToCustomerName>OAKVIEW PHARMACY</ShipToCustomerName><ShipToCustomerAddressLine1>1106 STUMPF BLVD.</ShipToCustomerAddressLine1><ShipToCustomerAddressLine2>Not Available</ShipToCustomerAddressLine2><ShipToCustomerCity>GRETNA</ShipToCustomerCity><ShipToState>LA</ShipToState><ShipToCustomerZip>70053</ShipToCustomerZip><ShipToCountry>US</ShipToCountry><UniqueBoxID>26526001</UniqueBoxID><BinLocation>L4603</BinLocation><CustomerDea>AM3396430</CustomerDea></pedship></pedshipData></root>","23836883706277927282238626987996","RM0314790","MDW","df","sample.txt");
		
	}catch(Exception ex){
		System.out.println("Error : "+ex);
	}
	}
}



