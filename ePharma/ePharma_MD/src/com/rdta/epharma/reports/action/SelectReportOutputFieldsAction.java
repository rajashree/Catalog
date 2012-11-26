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
 * Created on Aug 10, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.rdta.epharma.reports.action;

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
import com.rdta.catalog.trading.action.LocationAction;
import com.rdta.catalog.trading.model.Location;
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.persistence.TLQueryRunner;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.epharma.reports.form.OutPutFieldForm;
import com.rdta.epharma.reports.form.PedigreeReportForm;
import com.rdta.epharma.reports.form.ReportCubesForm;

/**
 * @author mgambhir
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SelectReportOutputFieldsAction extends Action
{
    private static Log log=LogFactory.getLog(SelectReportOutputFieldsAction.class);
    private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();

	
    public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response)
								 throws Exception {
    	log.info("In execute ()  of SelectReportOutputFieldsAction");
		
    	String cubeName = request.getParameter("radio1");
		request.setAttribute("cubeName",cubeName);
		log.info("Selected cube name is "+cubeName);
		Collection outField = new ArrayList();
		HttpSession session = request.getSession(false);
		PedigreeReportForm pedigreeReportForm = (PedigreeReportForm) session.getAttribute("pedigreeReportForm");
		OutPutFieldForm  outPutField = new OutPutFieldForm();
		if(pedigreeReportForm==null)
			pedigreeReportForm = new PedigreeReportForm();

		try{

		//Retrieving out fields specific to cube Name
			
		List list = queryRunner.returnExecuteQueryStrings(getXQuery(cubeName));
		pedigreeReportForm.setCubeName(cubeName);
		session.setAttribute("pedigreeReportForm",pedigreeReportForm);
		
		log.info("Inserting output fiedls to the form");
		if(list!=null && list.size()>0)
		{	
			Node outInf = (Node) XMLUtil.parse((String)list.get(0));
			NodeList outList = outInf.getChildNodes();
			
			int length = outList.getLength();
			for(int i=0;i<length;i++){
					outPutField = new OutPutFieldForm();
					outPutField.setCubeName(cubeName);
					outPutField.setKey(XMLUtil.getValue(outList.item(i),"key"));
				    outPutField.setName(XMLUtil.getValue(outList.item(i),"Name"));
				    outField.add(outPutField);
			}
					session.setAttribute("outPutFields",outField);		
			
			}
		}catch(PersistanceException e)
		{
			log.error("Error in   SelectReportOutputFieldsAction.execute()" + e);
			throw new PersistanceException(e);	
		}
		return mapping.findForward("success");
   }
    
    public String getXQuery(String cubeName)
    {
     	String xQuery = "";
    	xQuery = "declare general-option 'experimental=true'; ";
    	xQuery = xQuery+ "tlsp:getReportOuputFields_MD('"+cubeName+"')";
    	log.info("execute the query "+xQuery);
     	return xQuery;
    }
}