package com.rdta.eag.epharma.api;

import org.w3c.dom.Node;

import com.rdta.eag.epharma.commons.xml.XMLUtil;

public class RecvParseXML {
	
	public static void parseXML(String xmlInput){

		Node n1=XMLUtil.parse(xmlInput);
		
		String pedigreeID = XMLUtil.getValue(n1,"/PED-RCV/PedigreeID");
		String signerID = XMLUtil.getValue(n1,"/PED-RCV/SignerID");
		String SWPLotNumber = XMLUtil.getValue(n1,"/PED-RCV/SWPLotNumber");
		String SWPStockCode = XMLUtil.getValue(n1,"/PED-RCV/SWPStockCode");
		
		System.out.println(pedigreeID);
		System.out.println(signerID);
		System.out.println(SWPLotNumber);
		System.out.println(SWPStockCode);
		
		if(pedigreeID != null && signerID != null 
		        && SWPLotNumber != null && SWPStockCode != null){
			//call PED_RCV WS			
		}else if(pedigreeID != null && signerID != null){
			//call PED_BADRCV			
		}		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		parseXML("abcd");
		

	}

}
