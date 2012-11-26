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


public class ProcessedFaxesAction  extends Action
{
	private static Log log=LogFactory.getLog(ProcessedFaxesAction.class);
	private static final QueryRunner queryrunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		
		ReceivedFaxesForm theForm = null;
		Collection colln= new ArrayList();
		
		log.info("Inside Action ProcessedFaxesAction....... ");
		
		String offset = request.getParameter("offset");
		if(offset != null){
			int off = Integer.parseInt(offset);
			System.out.println("The value of offset in PedigreeSearchAction is :"+off);
		}else{
			offset="0";
		}
		
		request.setAttribute("offset", offset);	
		
		try{
			
			String query = "tlsp:FaxDetails_MD('1')";
			log.info("query for FaxDetails is:"+query);
			List faxes = queryrunner.executeQuery(query);
			System.out.println("List size after FaxDetails is : "+faxes.size());
			request.setAttribute("FaxDetails", faxes);
			
			for(int i=0; i<faxes.size(); i++){
				
				theForm = new ReceivedFaxesForm();
				Node listNode = XMLUtil.parse((InputStream)faxes.get(i));
				//Node listNode = XMLUtil.parse((String)res.get(i)); ee sari nenu raanu antunnaanu
				
				theForm.setFaxName(CommonUtil.jspDisplayValue(listNode,"FaxName"));
				log.info("FaxName is:"+theForm.getFaxName());
				System.out.println("FaxName is:"+theForm.getFaxName());
				theForm.setCSID(CommonUtil.jspDisplayValue(listNode,"CSID"));
				theForm.setANI(CommonUtil.jspDisplayValue(listNode,"ANI"));
				theForm.setDateReceived(CommonUtil.jspDisplayValue(listNode,"DateReceived"));
				colln.add(theForm);
			}
			
			
			request.setAttribute(Constants.FAX_DETAILS,colln);
		}catch(PersistanceException e){
			log.error("Error in ProcessedFaxesAction execute method........." +e);
			throw new PersistanceException(e);
		}
		catch(Exception ex){			
			ex.printStackTrace();
			log.error("Error in ProcessedFaxesAction execute method........." +ex);
			throw new Exception(ex);
		}
		return mapping.findForward("success");
	}
	
}