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

 
package com.rdta.catalog.trading.action;



import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rdta.catalog.trading.model.TradingPartner;


import org.w3c.dom.Node;
import com.rdta.catalog.XMLStructure;
import com.rdta.catalog.OperationType;


import com.rdta.catalog.Constants;
import com.rdta.commons.CommonUtil;
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;

/**
 * Trading Partner Form information collecting from the reques form.
 * 
 * 
 */
public class TradingPartnerListAction extends Action
{
    private static Log log=LogFactory.getLog(TradingPartnerListAction.class);
	private static final QueryRunner queryrunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();

    public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response)
								 throws Exception {
 
    	log.info("Inside Action TradingPartnerListAction....... ");
		log.info("Inside Action execute of TradingPartnerListAction....");
		
		TradingPartnerListForm theForm = null;
		Collection colln= new ArrayList();
		
		String offset = request.getParameter("offset");
		if(offset != null){
			int off = Integer.parseInt(offset);
			System.out.println("The value of offset in TradingPartnerListAction is :"+off);
		}else{
			offset="0";
		}
		
		request.setAttribute("offset", offset);	
		
		try{
		//set the session to null
		HttpSession session = request.getSession(false);
		if(session != null) {
			session.setAttribute(Constants.SESSION_TP_CONTEXT,null);
			
			String sessionID = (String)session.getAttribute("sessionID");
			log.info("query tlsp:validateAccess('"+sessionID+"','4.0','Read')");
			List accessList = queryrunner.returnExecuteQueryStrings("tlsp:validateAccess('"+sessionID+"','4.0','Read')");
			String insertStatus = accessList.get(0).toString();
			log.info("The insertStatus is "+insertStatus);
			if(insertStatus.equals("false")){
			
			    String CatalogAccess="false";
				request.setAttribute("CatalogAccess",CatalogAccess);
				log.info("No ACCESS");
				return mapping.findForward("failure");
			}
			
			
		}

		List list = TradingPartner.getTradingPartnerList();
		
		for(int i=0; i<list.size(); i++){
			
			theForm = new TradingPartnerListForm();
			Node listNode = XMLUtil.parse((InputStream)list.get(i));
			
			theForm.setName(CommonUtil.jspDisplayValue(listNode,"name"));
			System.out.println("Trading Partner name is: "+theForm.getName());
			
			String line1 = CommonUtil.jspDisplayValue(listNode,"address/line1");
			String line2 = CommonUtil.jspDisplayValue(listNode,"address/line2");
			String city = CommonUtil.jspDisplayValue(listNode,"address/city");
			String state = CommonUtil.jspDisplayValue(listNode,"address/state");
			String country = CommonUtil.jspDisplayValue(listNode,"address/country");
			String zip = CommonUtil.jspDisplayValue(listNode,"address/zip");

			String address = line1 + " "  + line2 + " " + city + " " + state + " "   + country + " " + zip; 
			
			theForm.setAddress(address);
			System.out.println("Trading Partner address is: "+theForm.getAddress());
			
			theForm.setPhone(CommonUtil.jspDisplayValue(listNode,"phone"));
			System.out.println("Trading Partner phone is: "+theForm.getPhone());
			
			theForm.setFax(CommonUtil.jspDisplayValue(listNode,"fax"));
			System.out.println("Trading Partner fax is: "+theForm.getFax());			
			
			theForm.setContact(CommonUtil.jspDisplayValue(listNode,"contact"));
			System.out.println("Trading Partner contact is: "+theForm.getContact());
			
			theForm.setEmail(CommonUtil.jspDisplayValue(listNode,"email"));
			System.out.println("Trading Partner email is: "+theForm.getEmail());
			
			theForm.setGenId(CommonUtil.jspDisplayValue(listNode,"genId"));
			System.out.println("Trading Partner genId is: "+theForm.getGenId());
			
			colln.add(theForm);
		}
		
		request.setAttribute(Constants.TP_DETAILS,colln);

		//set the result here
		request.setAttribute("TradingPartnerListInfo",list);
		}catch(PersistanceException e){
			log.error("Error in TradingPartnerList execute method........." +e);
			throw new PersistanceException(e);
		}
		catch(Exception ex){			
			ex.printStackTrace();
    		log.error("Error in TradingPartnerList execute method........." +ex);
    		throw new Exception(ex);
		}
		return mapping.findForward("success");
   }
	

   
  
}