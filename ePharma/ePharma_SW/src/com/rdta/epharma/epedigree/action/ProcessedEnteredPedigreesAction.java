
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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Node;

import com.rdta.catalog.Constants;
import com.rdta.commons.CommonUtil;
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;


public class ProcessedEnteredPedigreesAction  extends Action
{
	private static Log log=LogFactory.getLog(ProcessedEnteredPedigreesAction.class);
	private static final QueryRunner queryrunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		
		String faxName[] = request.getParameterValues("check");
		
		
		if(faxName != null){
		int size = faxName.length;
		 System.out.println("come into action prasanthi delete size :  "+size);
	        String buffer=null;
	        for(int i=0;i<size;i++){
	      	buffer="tlsp:deleteProcessedPedigree('"+faxName[i]+"')";
	      	queryrunner.executeQuery(buffer);
			System.out.println("Queryyyyyyyyyy: "+ buffer);
	        }
		}
		
		ProcessedEnteredPedigreesForm theForm = null;
		Collection colln= new ArrayList();
		
		log.info("Inside Action ProcessedEnteredPedigreesAction....... ");
		
		String offset = request.getParameter("offset");
		if(offset != null){
			int off = Integer.parseInt(offset);
			System.out.println("The value of offset in PedigreeSearchAction is :"+off);
		}else{
			offset="0";
		}
		
		request.setAttribute("offset", offset);	
		
		try{
            
			
			String query = "tlsp:GetProcessedEnteredPedigrees()";
			log.info("query for FaxDetails is:"+query);
			List enteredPedigrees = queryrunner.executeQuery(query);
			System.out.println("List size after FaxDetails is : "+enteredPedigrees.size());
			request.setAttribute("PedigreeDetails", enteredPedigrees);
			
			for(int i=0; i<enteredPedigrees.size(); i++){
				
				theForm = new ProcessedEnteredPedigreesForm();
				Node listNode = XMLUtil.parse((InputStream)enteredPedigrees.get(i));
				//Node listNode = XMLUtil.parse((String)res.get(i)); 
				
				theForm.setInvoiceNumber(CommonUtil.jspDisplayValue(listNode,"InvoiceNumber"));
				theForm.setPedigreeId(CommonUtil.jspDisplayValue(listNode,"pedigreeId"));
				System.out.println("Invoice Number is:"+theForm.getInvoiceNumber());
				theForm.setVendorName(CommonUtil.jspDisplayValue(listNode,"VendorName"));
				theForm.setDrugName(CommonUtil.jspDisplayValue(listNode,"DrugName"));
				theForm.setNdc(CommonUtil.jspDisplayValue(listNode,"NDC"));
				String lots=XMLUtil.getValue(listNode,"VendorLot");
				System.out.println(lots);
				int l= lots.length();
				lots = lots.substring(0,l-1);
				theForm.setVendorLot(lots);
				
				
				theForm.setProcessedDate(CommonUtil.jspDisplayValue(listNode,"processedDate"));
				colln.add(theForm);
			}
						
			request.setAttribute(Constants.ENTER_PEDIGREE_DETAILS,colln);
			
		}catch(PersistanceException e){
			log.error("Error in ProcessedEnteredPedigreesAction execute method........." +e);
			throw new PersistanceException(e);
		}
		catch(Exception ex){			
			ex.printStackTrace();
			log.error("Error in ProcessedEnteredPedigreesAction execute method........." +ex);
			throw new Exception(ex);
		}
		return mapping.findForward("success");
	}
	
}