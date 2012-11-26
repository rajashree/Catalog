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
			
			String endpoint = "http://192.168.254.4:8080/axis/services/PortalIntegration?wsdl";
			/*String pedigreeID = "S00111234567890";
			String pedigreeEnvelopID = "urn:uuid:fff36ed7-ec23-1182-c001-ca17b0d55709";
			String invoiceID = "CO00021";
			String subscriberID = "631648173";
			
		
			String endpoint = "http://localhost:8080/axis/services/PortalIntegration?wsdl";
			String pedigreeEnvelopID = "fff36ad9-6808-1740-c001-9b625c14376b";
			String invoiceID = "141";
			String subscriberID = "288581223";
			
			String endpoint = "http://localhost:8080/axis/services/PortalIntegration?wsdl";
			String pedigreeEnvelopID = "fff36abf-0bb6-1a84-c001-d9e9a933df35";
			String pedigreeID = "fff36abf-0bb6-1140-c001-d9e9a933df35";
			String invoiceID = "141";
			String subscriberID = "288581223";*/
			
			/*String subscriberID = "631648173";
			String deaNumber = "RU0326240";
			String ndcNumber ="55513-267-01";
			String invoiceNumber ="CO00001";*/
			String subscriberID = "1463391996";
			String deaNumber = "RD-0311100";
			String ndcNumber ="67544-0832-32";
			String invoiceNumber ="9783";
			Service service = new Service(); 
			Call call = (Call) service.createCall(); 
			call.setTargetEndpointAddress(new java.net.URL(endpoint)); 
		
			//Call for SubmitPedigreeAPI
			/*call.setOperationName(new QName(endpoint,"getPedigreeEnvelopeDocument"));
			System.out.println("****** Service Response ********* "); 
			call.invoke(new Object[]{subscriberID,invoiceID,pedigreeEnvelopID}); //Give subscriberID,invoiceID,pedigreeEnvelopID,pedigreeID as input parameters
     		
			
			call.setOperationName(new QName(endpoint,"getPedigreeDocument"));
			System.out.println("****** Service Response ********* "); 
			call.invoke(new Object[]{subscriberID,invoiceID,pedigreeEnvelopID,pedigreeID}); //Give subscriberID,invoiceID,pedigreeEnvelopID,pedigreeID as input parameters
			//call.invoke(new Object[]{subscriberID,invoiceID,pedigreeEnvelopID}); //Give subscriberID,invoiceID,pedigreeEnvelopID,pedigreeID as input parameters
			//getPedigreeDocument("285815143","141","fff36ad9-6808-1740-c001-9b625c14376b","fff36ad9-680b-1c80-c001-9b625c14376b");
			  */
			
			call.setOperationName(new QName(endpoint,"getPedigreeGivenInvoiceNumberNDC"));
			System.out.println("****** Service Response ********* "); 
			call.invoke(new Object[]{subscriberID,deaNumber,ndcNumber,invoiceNumber});
			
			
			}catch(Exception ex){
				System.out.println("exception "+ex);
			 }
	}

}
