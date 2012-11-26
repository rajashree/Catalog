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

import com.rdta.eag.epharma.commons.persistence.pool.TLConnectionPool;
import com.rdta.eag.epharma.commons.persistence.PersistanceException;
import com.rdta.eag.epharma.commons.persistence.QueryRunner;
import com.rdta.eag.epharma.commons.persistence.QueryRunnerFactory;
import com.rdta.eag.epharma.commons.xml.XMLUtil;


import com.rdta.eag.epharma.util.SendDHFormEmail;


public class CreatePedigreeEnvelopeForDropship {
	
	private static Log log=LogFactory.getLog(CreatePedigreeEnvelopeForDropship.class);
	static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	static Connection conn = null;
	/**
	 * @param args
	 */
	
	public static String CreatePedigreeEnvelope(String dropShipXmlId, String signerid, String deaNumber, String sourceRoutingCode, String fileName) throws Exception{
		// TODO Auto-generated method stub
				
		conn = TLConnectionPool.getTLConnectionPool().borrowConnection();
		conn.setAutoCommit(false);
	
		/*Node listNode = XMLUtil.parse(signerid);
		String pedBankId= XMLUtil.getValue(listNode,"pedBankId");
		*/
		
		
		List envIds = new ArrayList();
		/*xmlString = xmlString.replaceAll("&","&amp;");*/
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("<root>{for $i in collection('tig:///ePharma_MD/DropShip')/result[Id = '"+dropShipXmlId+"']/DropShip return $i}</root>");
		System.out.println("Query :: "+buffer.toString());
				
		String xmlString = queryRunner.returnExecuteQueryStringsAsStringNew(buffer.toString(),conn);	
		
		Node no_of_dropShips = XMLUtil.parse(xmlString);
		
		NodeList children = no_of_dropShips.getChildNodes();
		System.out.println("The number of DropShip nodes is :"+XMLUtil.convertToString(children.item(0),true));
				
		
		for(int count=0; count<children.getLength(); count++){
			
			String envId = CreateShippedPedigreeForDropship.CreateShippedPedigree(XMLUtil.convertToString(children.item(0),true),signerid,deaNumber,sourceRoutingCode,"",conn);
			envIds.add(envId);
		}
		conn.commit();
		StringBuffer res = new StringBuffer();
		res.append("<Output>");
		for (int i=0;i<envIds.size();i++){
			res.append("<envId>");
			res.append(envIds.get(i));
			res.append("</envId>");
		}
		res.append("<exception>");
		res.append("false");
		res.append("</exception>");
		res.append("</Output>");
		System.out.println("REsult : "+res.toString());
		//PedEnvTransmissionHandler.receivePedigreeEnvelopes(res.toString());
		
		return res.toString();
	}
	
	
	public static void main(String [] args) throws Exception{
		
		CreatePedigreeEnvelopeForDropship.CreatePedigreeEnvelope("fff36d82-bfd7-1d00-c001-1875e97d5d40", "23836883706277927282238626987996", "RM0314790", "MDW", "");
	}
	
	
}



