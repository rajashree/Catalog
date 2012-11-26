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

 
package com.rdta.epharma.epedigree.action;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.w3c.dom.Node;

import com.rdta.catalog.Constants;
import com.rdta.commons.CommonUtil;
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

public class ShippingManagerAttachmentAction extends Action{
	
	private static Log log=LogFactory.getLog(ShippingManagerAttachmentAction.class);
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	String servIP = null;
	String clientIP = null;
	Connection conn; 
	Statement stmt;
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		log.info("Inside ShippingManagerAttachmentAction....... ");
		
		PedigreeStatusForm formBean = (PedigreeStatusForm)form;
		String pedigreeId = request.getParameter("PedigreeId");
		request.setAttribute("PedigreeId",pedigreeId);
		PedigreeStatusForm theform = null;
		Collection colln= new ArrayList();
		String attachmenttype = "";
		FormFile file1 = formBean.getAttachFile();
		String reply = request.getParameter("reply");
		System.out.println("The reply value from Form is : "+reply);
		
		try{
			
			HttpSession sess = request.getSession();
			
			if(formBean.getAttachFile() != null)
			{
				System.out.println("req parameter FileSize:"+file1.getFileSize());
				if(file1.getFileSize() == 0) {
					file1 = (FormFile)sess.getAttribute("AttachFile");
				}
				if(file1 != null && file1.getFileSize() != 0) {
					sess.setAttribute("AttachFile",file1);
				}
				System.out.println("The FileName from bean is : "+file1);
				
			}
			
			String xQuery="tlsp:getAttachmentDetails_MD('ShippedPedigree','"+pedigreeId+"')";
			log.info("Attachment query:"+xQuery);
			System.out.println("Attachment query:"+xQuery);
			List res=queryRunner.returnExecuteQueryStrings(xQuery);
			log.info("AttachmentDetails query:"+res);
			log.info(" ClassnName   :"+res.get(0).getClass().getName());
			String str = (String)res.get(0);
			log.info(" String Name "+ str);
			Node listNode1 = XMLUtil.parse(str);
			String existatt=XMLUtil.getValue(listNode1,"true");
			if(existatt.equalsIgnoreCase("true")){
				attachmenttype=XMLUtil.getValue(listNode1,"mimeType");	
				System.out.println("mimeType is : "+attachmenttype);
			}
			
			String pedt="tlsp:ShippingManagerPedigreeDetails_MD('"+pedigreeId+"')";
			log.info("query for pedigree details in action: "+pedt);
			List result=queryRunner.executeQuery(pedt);
			log.info("the list of objects :"+result);
			request.setAttribute("res",result);
			
			for(int i=0; i<result.size(); i++){
				
				theform = new PedigreeStatusForm();
				Node listNode = XMLUtil.parse((InputStream)result.get(i));
				System.out.println("listNode :"+listNode);
				
				theform.setPedigreeid(CommonUtil.jspDisplayValue(listNode,"pedigreeId"));
				System.out.println("pedigre id :"+theform.getPedigreeid());
				theform.setDate(CommonUtil.jspDisplayValue(listNode,"transactionDate"));
				theform.setSource(CommonUtil.jspDisplayValue(listNode,"source"));
				theform.setDestination(CommonUtil.jspDisplayValue(listNode,"destination"));
				theform.setTransactiontype(CommonUtil.jspDisplayValue(listNode,"transactionType"));
				theform.setTrnsaction(CommonUtil.jspDisplayValue(listNode,"transactionNo"));
				theform.setMimeType(attachmenttype);
				System.out.println("transaction id :"+theform.getTrnsaction());
				System.out.println("MimeType from bean is :"+theform.getMimeType());
				colln.add(theform);
				
			}
			request.setAttribute(Constants.SHIPPED_DETAILS,colln);
			
			
			if(reply != null && !reply.equals("true")){
				
				String query1 = " for $i in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope/*:pedigree ";
				query1 = query1 + " where $i/*:shippedPedigree/*:documentInfo/*:serialNumber = '"+pedigreeId+"' ";
				query1 = query1 + " return exists($i//*:initialPedigree/*:attachment) ";
				String exists = queryRunner.returnExecuteQueryStringsAsString(query1);
				System.out.println("The exists value is : "+exists);
				
				if(exists != null && exists.equalsIgnoreCase("true")){
					request.setAttribute("exists","true");
					System.out.println("Attachment already exists");
					return mapping.findForward("success");
				}else{
					
					if(file1 != null)
					{
						InputStream ins = file1.getInputStream();
						System.out.println("Stream for updating attachment : "+ins);
						String fileType = file1.getContentType();
						System.out.println("The File Type is : "+fileType);
						String query = " declare namespace bin ='http://www.rainingdata.com/TigerLogic/binary-support'; ";
						query = query + "declare binary-encoding none; ";
						query = query + "update let $binData := binary{$1} ";
						query = query + "let $data1 := bin:base64-encode($binData) ";
						query = query + "for $i in collection('tig:///ePharma_MD/ShippedPedigree') ";
						query = query + "where $i/*:pedigreeEnvelope/*:pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber = '"+pedigreeId+"' ";
						//query = query + "insert <attachment><mimeType>"+fileType+"</mimeType><encoding>{bin:base64-encode($binData)}</encoding><data>{$binData}</data></attachment> ";
						query = query + "insert <attachment xmlns='urn:epcGlobal:Pedigree:xsd:1'><mimeType>"+fileType+"</mimeType><encoding>base64binary</encoding><data>{$data1}</data></attachment> ";
						query = query + " as last into $i//*:initialPedigree";
						log.info("Query for updating attachment : "+query);
						System.out.println("Query for updating attachment : "+query);
						queryRunner.executeQueryWithStream(query,ins);
						request.setAttribute("AttachStatus","true");
						
					}
					
				}
				
			}else if(reply != null && reply.equals("true")){
				
				System.out.println("Inside TRUE REPLY for updating attachment : ");
				
				if(file1 != null)
				{
					InputStream ins = file1.getInputStream();
					System.out.println("Stream for updating attachment : "+ins);
					String fileType = file1.getContentType();
					System.out.println("The File Type is : "+fileType);
					
					String query = "declare namespace bin ='http://www.rainingdata.com/TigerLogic/binary-support'; ";
					query = query + "declare binary-encoding none; ";
					query = query + "update let $binData := binary{$1} ";
					query = query + "let $data1 := bin:base64-encode($binData) ";
					query = query + "replace doc( for $i in collection('tig:///ePharma_MD/ShippedPedigree') ";
					query = query + "where $i/*:pedigreeEnvelope/*:pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber = '"+pedigreeId+"' ";
					query = query + "return  document-uri($i))//*:initialPedigree/*:attachment with <attachment xmlns='urn:epcGlobal:Pedigree:xsd:1'><mimeType>"+fileType+"</mimeType><encoding>base64binary</encoding><data>{$data1}</data></attachment> ";
					log.info("Query for updating altPedigree : "+query);
					queryRunner.executeQueryWithStream(query,ins);
					request.setAttribute("AttachStatus","true");
					
				}
				
			}
			
		}catch(PersistanceException e){
			log.error("Error in ShippingManagerAttachmentAction execute method........." +e);
			throw new PersistanceException(e);
		}
		catch(Exception ex){			
			ex.printStackTrace();
			log.error("Error in ShippingManagerAttachmentAction execute method........." +ex);
			throw new Exception(ex);
		}
		
		return mapping.findForward("success");
	}
	
}
