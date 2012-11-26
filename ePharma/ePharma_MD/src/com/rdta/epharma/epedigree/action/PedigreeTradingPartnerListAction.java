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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.w3c.dom.Node;

import com.rdta.catalog.Constants;
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;


public class PedigreeTradingPartnerListAction extends Action {
	
	private static Log log=LogFactory.getLog(PedigreeTradingPartnerListAction.class);
	private static final QueryRunner queryrunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		log.info("Inside the Action Class PedigreeTradingPartnerListAction......");	
		
		Collection colln= new ArrayList();
		PedigreeTradingPartnerListForm theForm;
		
		
		String PTP[] = request.getParameterValues("check");
		System.out.println("Query "+ PTP);
		
		if(PTP != null){
		int size = PTP.length;
		 System.out.println("delete size :  "+size);
	        String buffer=null;
	        for(int i=0;i<size;i++){
	      	buffer="tlsp:deletePTP('"+PTP[i]+"')";
	      	queryrunner.executeQuery(buffer);
			System.out.println("Query "+ buffer);
	        }
		}
		
		
		String offset = request.getParameter("offset");
		if(offset != null){
			int off = Integer.parseInt(offset);
			
		}else{
			offset="0";
		}	
		request.setAttribute("offset", offset);	
		
		try{
			
			StringBuffer buffer = new StringBuffer();		
			buffer.append("tlsp:ListPedigreeTradingPartners()");
			List result = queryrunner.executeQuery(buffer.toString());
			request.setAttribute("result", result);
			
			log.info("The list size of the query is :"+result.size());
					
			for(int i=0; i<result.size(); i++){
				
				theForm = new PedigreeTradingPartnerListForm();
				Node n1 = XMLUtil.parse((InputStream)result.get(i));
			
				theForm.setTradingPartnerName(XMLUtil.getValue(n1,"TradingPartner"));
				log.info("The Trading Partner name is :"+theForm.getTradingPartnerName());
			
				theForm.setDeaNumber(XMLUtil.getValue(n1, "DEANumber"));
				log.info("The DEANumber is :"+theForm.getDeaNumber());
			
				colln.add(theForm);
			}	
			
			request.setAttribute(Constants.PTP_DETAILS,colln);
		}catch(PersistanceException e){
			log.error("Error in PedigreeTradingPartnerListAction execute method........." +e);
			throw new PersistanceException(e);
			
		}catch(Exception ex){			
			ex.printStackTrace();
			log.error("Error in PedigreeTradingPartnerListAction execute method........." +ex);
			throw new Exception(ex);
		}		
		return mapping.findForward("success");		
	}
}
