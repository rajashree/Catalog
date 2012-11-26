/*
 * Created on Aug 10, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

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

package com.rdta.epharma.reports.action;

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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.rdta.catalog.Constants;
import com.rdta.catalog.OperationType;
import com.rdta.catalog.trading.action.LocationAction;
import com.rdta.catalog.trading.model.Location;
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.persistence.TLQueryRunner;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.epharma.reports.form.ReportCubesForm;
import com.rdta.tlapi.xql.Connection;

/**
 * @author mgambhir
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ShowReportCubesAction extends Action
{
    private static Log log=LogFactory.getLog(ShowReportCubesAction.class);
    private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();

	
    public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response)
								 throws Exception {
    	log.info("Executing execute Method of ShowReportCubesAction");
		try{
		
			Collection repCube = new ArrayList();
		
			//TLQueryRunner queryRunner = new TLQueryRunner();
		String sessionID = request.getParameter("sessionID");
		log.info("getting the User Info...");
		String userInfo = getUserInfo(sessionID);
		List list = queryRunner.returnExecuteQueryStrings(getXQuery());
		
		log.info("After executing the tlsp:getReportCubes() stored procedure ");
		if(list!=null && list.size()>0)
		{	
			Node repInf = (Node) XMLUtil.parse((String)list.get(0));
			NodeList repList = repInf.getChildNodes();
			
			int length = repList.getLength();
			for(int i=0;i<length;i++){
				
				ReportCubesForm repForm = new ReportCubesForm();
				
				repForm.setKey(XMLUtil.getValue(repList.item(i),"key"));
				repForm.setName(XMLUtil.getValue(repList.item(i),"Name"));
				
				repCube.add(repForm);
			}
			
			
			request.setAttribute("reportCube",repCube);
			
		}else{
			log.info(" No Data is there in ReportCubes collection");
		}
		}catch(PersistanceException e)
		{
			log.error("Error in   ShowReportCubesAction.execute()" + e);
			throw new PersistanceException(e);	
		}
		return mapping.findForward("success");
   }
    
    public String getXQuery()
    {
     	String xQuery = "";
    	xQuery = "declare general-option 'experimental=true'; ";
    	xQuery = xQuery+ "tlsp:getReportCubes()";

     	return xQuery;
    }
    public String getUserInfo(String sessionId)
    {
     	String xQuery = "";
     	//int sessionid = new Integer(sessionId).intValue();
    	xQuery = xQuery+ "tlsp:GetUserInfo('+sessionId+')";
    	TLQueryRunner queryRunner = new TLQueryRunner();
    	String str= "";
    	try
		{
    	str = queryRunner.returnExecuteQueryStringsAsString(xQuery);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
	 	return str;
    }
}