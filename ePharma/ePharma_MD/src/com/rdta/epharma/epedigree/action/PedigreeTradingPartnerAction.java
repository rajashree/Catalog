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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;

public class PedigreeTradingPartnerAction extends Action{
	
	private static Log log=LogFactory.getLog(PedigreeTradingPartnerAction.class);
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		log.info("Inside the Action Class PedigreeTradingPartnerAction......");	
		
		try{
			
		PedigreeTradingPartnerForm formBean = (PedigreeTradingPartnerForm)form;
		
		String name = formBean.getName();
		String deaNumber = formBean.getDeaNumber();
		String notificationDesc = formBean.getNotificationDescr();
		String notificationURI = formBean.getNotificationURI();
		String destination = formBean.getDestination();
		String localFolder = formBean.getLocalFolder();
		String notifyURI = formBean.getNotifyURI();
		String userName = formBean.getUserName();
		String pwd = formBean.getPwd();
		
		String containerCodeMU = formBean.getContainerCodeMU();
		String shipmentHandleMU = formBean.getShipmentHandleMU();
		String containerCodeDU = formBean.getContainerCodeDU();
		String shipmentHandleDU = formBean.getShipmentHandleDU();
		String buttonName = request.getParameter("buttonName");
		String status = "";
		System.out.println("button name :"+buttonName);
		StringBuffer query = new StringBuffer();
		
		if(buttonName !=  null && buttonName.equalsIgnoreCase("Save")){
			query.append("tlsp:InsertPTPDocument('"+name+"','"+deaNumber+"','"+notificationDesc+"','"+notificationURI+"','"+destination+"','"+localFolder+"','"+notifyURI+"','"+userName+"','"+pwd+"','"+containerCodeMU+"','"+shipmentHandleMU+"','"+containerCodeDU+"','"+shipmentHandleDU+"')");
			queryRunner.executeQuery(query.toString());
			status ="Saved";
			request.setAttribute("UpdatePTP","true");
			System.out.println("TP Name: "+formBean.getNotificationDescr());
		}
		if(buttonName !=  null && buttonName.equalsIgnoreCase("Update")){
			query.append("tlsp:UpdatePTPDocument('"+name+"','"+deaNumber+"','"+notificationDesc+"','"+notificationURI+"','"+destination+"','"+localFolder+"','"+notifyURI+"','"+userName+"','"+pwd+"','"+containerCodeMU+"','"+shipmentHandleMU+"','"+containerCodeDU+"','"+shipmentHandleDU+"')");
			queryRunner.executeQuery(query.toString());
			status = "Updated";
			request.setAttribute("UpdatePTP","true");
			System.out.println("TP Name: "+formBean.getNotificationDescr());
		}
		request.setAttribute("Status",status);
		
		}catch(PersistanceException e){
			log.error("Error in PedigreeTradingPartnerAction execute method........." +e);
			throw new PersistanceException(e);
		}catch(Exception ex){
			ex.printStackTrace();
    		log.error("Error in PedigreeTradingPartnerAction execute method........." +ex);
    		throw new Exception(ex);
		}
		return mapping.findForward("success");
	}
}
