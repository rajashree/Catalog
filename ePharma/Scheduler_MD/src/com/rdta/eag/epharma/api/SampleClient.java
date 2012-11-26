package com.rdta.eag.epharma.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import javax.xml.namespace.QName;

/**
* Sample Client for Portal Integration
**/
public class SampleClient{

	// webservice URI
	String endpoint = "http://localhost:8080/axis/services/PortalIntegration?wsdl";

	// input parameters for the calls
	String poNumber = "37-6";
	String deaNumber="RU0326240";
	String ndcNumber="00409-1152-78";
	String invoiceNumber="11987";
	String subscriberID = "757327325";

	//Call for getPedigreeForPONumberNDC
	private String getPedigreeForPONumberNDC(){
		String response = null;
		try{
			Service service = new Service();
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress(new java.net.URL(endpoint));

			call.setOperationName(new QName(endpoint,"getPedigreeForPONumberNDC"));
			response = (String) call.invoke(new Object[]{
							subscriberID,poNumber,deaNumber,ndcNumber,invoiceNumber});
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return response;
	}


	//Call for getPedigreeForPONumber
	private String getPedigreeForPONumber(){

		String response = null;
		try{
			Service service = new Service();
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress(new java.net.URL(endpoint));

			call.setOperationName(new QName(endpoint,"getPedigreeForPONumber"));
			response = (String) call.invoke(new Object[]{
							subscriberID,poNumber,deaNumber,invoiceNumber});

		}catch(Exception ex){
			ex.printStackTrace();
		}
		return response;
	}

	public static void main(String[] args) {

		try{
			SampleClient client = new SampleClient();
			String response = client.getPedigreeForPONumberNDC();
			//String response = client.getPedigreeForPONumber();
			System.out.println("Response::::" + response);
		}catch(Exception ex){
			System.out.println("exception "+ex);
		}
	}

}
