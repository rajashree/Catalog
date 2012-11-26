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

import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;

public class PedigreeTradingPartnerDetailsAction extends Action {
	
	private static Log log=LogFactory.getLog(PedigreeTradingPartnerDetailsAction.class);
	private static final QueryRunner queryrunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		log.info("Inside the Action Class PedigreeTradingPartnerDetailsAction......");	
			
		PedigreeTradingPartnerForm theForm;
		
		try{
			
		String deaNumber = request.getParameter("deaNumber");
		StringBuffer query = new StringBuffer();
		query.append("tlsp:PedigreeTradingPartnersDetails('"+deaNumber+"')");
		
		List result = queryrunner.returnExecuteQueryStrings(query.toString());
		for(int i=0; i<result.size(); i++){
			
			theForm = (PedigreeTradingPartnerForm)form;
			Node n1 = XMLUtil.parse(result.get(i).toString());
		
			theForm.setName(XMLUtil.getValue(n1,"name"));	
			theForm.setDeaNumber(XMLUtil.getValue(n1, "deaNumber"));
			theForm.setNotificationDescr(XMLUtil.getValue(n1,"notificationDescription"));
			theForm.setNotificationURI(XMLUtil.getValue(n1,"notificationURI"));
			theForm.setLocalFolder(XMLUtil.getValue(n1,"localFolder"));
			theForm.setDestination(XMLUtil.getValue(n1,"destinationRoutingCode"));
			theForm.setNotifyURI(XMLUtil.getValue(n1,"notificationInfo/notifyURI"));
			theForm.setUserName(XMLUtil.getValue(n1,"notificationInfo/username"));
			theForm.setPwd(XMLUtil.getValue(n1,"notificationInfo/password"));
			theForm.setContainerCodeMU(XMLUtil.getValue(n1,"configurationElements/manualusecase/config[1]/value"));
			theForm.setShipmentHandleMU(XMLUtil.getValue(n1,"configurationElements/manualusecase/config[2]/value"));
			theForm.setContainerCodeDU(XMLUtil.getValue(n1,"configurationElements/dropshipusecase/config[1]/value"));
			theForm.setShipmentHandleDU(XMLUtil.getValue(n1,"configurationElements/dropshipusecase/config[2]/value"));
			
			request.setAttribute("UpdatePTP","true");
		}	
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
