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

 
/*
 * Created on Aug 12, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.rdta.epharma.reports.action;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.rdta.catalog.trading.action.LocationAction;
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.persistence.TLQueryRunner;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.epharma.reports.form.PedigreeReportForm;
import  com.rdta.epharma.reports.form.OutPutFieldPath;
import  com.rdta.epharma.reports.form.ShowReportFielterForm;
import com.rdta.epharma.reports.form.StatusMaintain;

/**
 * @author mgawbhir
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ShowReportFilterAction extends Action
{
    private static Log log=LogFactory.getLog(ShowReportFilterAction.class);
    private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
    
    
  
	
    public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response)
								 throws Exception {
    	String cubeName = request.getParameter("cubeName");
    	System.out.println("CubeName"+cubeName);
		HttpSession session = request.getSession(false);
		ShowReportFielterForm filterForm = new ShowReportFielterForm();
	/*	PedigreeReportForm pedigreeReportForm = (PedigreeReportForm) session.getAttribute("pedigreeReportForm");
		System.out.println("pedigreeReportForm..."+pedigreeReportForm.getCubeName());
		if(pedigreeReportForm==null)
			pedigreeReportForm = new PedigreeReportForm();
		*/
		try{
		
	log.info("Executing the execute() of ShowReportFilterAction Class");
		Collection coll = new ArrayList();
		Collection statColl = new ArrayList();
		String[] fields =request.getParameterValues("outputField");
	log.info("selected output Fields for the output");
		request.getParameterNames();
		int length= fields.length;
		HashMap fieldPathMap =	OutPutFieldPath.getShippedPath();
		String retClause="";
		for(int i=0;i<length;i++){
			retClause=retClause+"$i/"+(String)fieldPathMap.get(fields[i])+" ,";
			System.out.println((String)fields[i]);
		}
		
		log.info("retrieve  the Trading Partner List From the TradingParner collection ");
		List tpList = queryRunner.returnExecuteQueryStrings("tlsp:getTradingPartnerList_MD()");
	
		request.setAttribute("tradingList",tpList);
		
		
		
		
		List list = queryRunner.executeQuery(getXQuery(cubeName));
		log.info("retrieve  the Cube filters");
		
		session.setAttribute("returnclause",retClause);
		session.setAttribute("cubeName",cubeName);
		session.setAttribute("fields",fields);
	//	pedigreeReportForm.setOutputFieldList(fields);
		
		log.info("setting the form to show the filters ");
		if(list!=null && list.size()>0)
		{
			String str= "";
			int flength= list.size();
			for(int i=0;i<flength;i++)
			{
				Node temp = XMLUtil.parse((ByteArrayInputStream)list.get(i));
				filterForm = new ShowReportFielterForm();
				filterForm.setKey(XMLUtil.getValue(temp,"key"));
				filterForm.setFieldName(XMLUtil.getValue(temp,"filterName"));
				Node status = XMLUtil.getNode(temp,"status");
				if(status != null){
					NodeList statusList= status.getChildNodes();
					String [] tempStatus=new String[statusList.getLength()];
					int listLength= statusList.getLength();
					for(int k=0;k<listLength;k++){
						StatusMaintain currentStatus = new StatusMaintain();
						tempStatus[k] = XMLUtil.getValue(statusList.item(k));
						currentStatus.setStatus(tempStatus[k]);
						statColl.add(currentStatus);
					}
					
				}
				
				
				coll.add(filterForm);
				
			}
			System.out.println("str"+str);
			request.setAttribute("htmlString",str);
			request.setAttribute("filterForm",coll);
			request.setAttribute("statushow",statColl);
			log.info("go to return success ");
		}
		}catch(PersistanceException e)
		{
			log.error("Error in  ShowReportFilterAction .execute()" + e);
			throw new PersistanceException(e);	
		}
		return mapping.findForward("success");
   }
    
    public String getXQuery(String cubeName)
    {
     	String xQuery = "";
    	xQuery = "declare general-option 'experimental=true'; ";
    	xQuery = xQuery+ "tlsp:getReportFilters_MD('"+cubeName+"')";
    	
     	return xQuery;
    }
}
