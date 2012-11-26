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

 


package com.rdta.efax;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.efaxdeveloper.util.inbound.InboundClient;
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.GUID;
import com.rdta.commons.PedigreeUtil;

/**
 * @author Santosh Subramanya
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class InboundFaxClient extends HttpServlet {
	
	private static Log log = LogFactory.getLog(InboundFaxClient.class);		
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doPost(request, response);		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
				
		String inboundXML = request.getParameter("xml");
		
		System.out.println("The XML is :"+inboundXML);
		System.out.println("Inside the POST method of InboundFaxClient servlet .............");
		
		try {
			 /*String filepath = "C:\\Documents and Settings\\Santosh Subramanya\\Desktop\\efax(demo)\\samples\\inbound\\Sample.xml";
			  File file = new File(filepath);*/
			InboundClient inclient = new InboundClient(inboundXML);
			
			System.out.println("The CSID is :"+inclient.getCSID());		
			
			String result = insertToDB(inboundXML);
			System.out.println("The document ID inserted is :"+result);
			
			System.out.println("Inserting the pedigree Id in to the document.....");
			
			//GUID guid = new GUID();	
			String pedId = PedigreeUtil.createPedId();
			System.out.println("pedId to be inserted in to the ReceivedFax collection is :"+pedId);
			
			String update = updateDB(pedId, result);
			System.out.println("***** Update successful*******"+update);
			
			PrintWriter out = response.getWriter();
			out.println("Post Successful");
			out.flush();
			out.close();
			
		} catch (Exception e) {
			System.out.println("An error occured in the InboundFaxClient servlet........");
			e.printStackTrace();			
		}	
	}
	
	private static String insertToDB(String wrapInfo) throws PersistanceException{
			
		String endXML = getPrologRemoved(wrapInfo);
		
		StringBuffer buff = new StringBuffer();
		buff.append("tig:insert-document('tig:///ePharma_MD/ReceivedFax',"+endXML+")");	
		System.out.println(buff.toString());
		return(queryRunner.returnExecuteQueryStringsAsString(buff.toString()));
	}
	
	private static String getPrologRemoved(String xml){
		int pos = xml.indexOf("<?xml version");
		int last = xml.indexOf("?>");
		
		if(pos < 0 || last < 0) {
			log.info("Prolog node not found.....");
			return xml;
		}else {
			log.info("Prolog found....");
			return xml.substring(last+3);
		}
	}
	
	private static String updateDB(String pedId, String docId) throws PersistanceException{
		
		System.out.println("Inside the updateDB metod of InboundFaxClient servlet......");
		
		StringBuffer buff1 = new StringBuffer();
		buff1.append("tlsp:InsertPedIdInefax_MD('"+pedId+"', '"+docId+"')");
		System.out.println(buff1.toString());
		return(queryRunner.returnExecuteQueryStringsAsString(buff1.toString()));		
	}
}
