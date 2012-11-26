package com.rdta.eag.epharma.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import javax.xml.namespace.QName;

public class SampleClientForPortalIntegration{

//	/**
//	 * @param args
//	 */
//	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			System.out.println("Inside Client");
			
			String endpoint = "http://localhost:8081/axis/services/PortalIntegration?wsdl";
			
			//Input params for getPedigreeForPONumberNDC Call
			
			
				 String poNumber = "37-6";
				 String deaNumber="RM0114481";
				 String ndcNumber="00409-1152-78";
				 String invoiceNumber="20070306";
				 String subscriberID = "757327325";
				 
		
			//Input params for getPedigreeForPONumber Call
						
			/* String poNumber = "37-6";
			 String deaNumber="RU0326240";
			 String invoiceNumber="37-6";
			 String subscriberID = "757327325";*/
			
			Service service = new Service(); 
			Call call = (Call) service.createCall(); 
			call.setTargetEndpointAddress(new java.net.URL(endpoint)); 
		
			/* //Call for getPedigreeForPONumberNDC
			call.setOperationName(new QName(endpoint,"getPedigreeForPONumberNDC"));
			String response = (String) call.invoke(new Object[]{subscriberID,poNumber,deaNumber,ndcNumber,invoiceNumber}); //Give subscriberID,invoiceID,pedigreeEnvelopID,pedigreeID as input parameters
     		*/
			
			//Call for getPedigreeForPONumber
			call.setOperationName(new QName(endpoint,"getPedigreeForPONumber"));
			String response = (String) call.invoke(new Object[]{subscriberID,poNumber,deaNumber,invoiceNumber}); //Give subscriberID,invoiceID,pedigreeEnvelopID,pedigreeID as input parameters
			
			System.out.println("****** Service Response ********* "); 
			System.out.println(response);           
			}catch(Exception ex){
				System.out.println("exception "+ex);
			 }
	}

}
