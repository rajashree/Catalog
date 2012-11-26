/*
 * Created on Oct 14, 2005
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

 

package com.rdta.catalog.trading.action;

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

import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;

/**
 * @author Jagadish Pampatwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ViewMappedDetailsAction extends Action{
	 private static Log log=LogFactory.getLog(ViewMappedDetailsAction.class);
	  private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			 HttpServletRequest request, HttpServletResponse response)
			 throws Exception {
		log.info("Inside Action ViewMappedDetailsAction....... ");
		log.info("Inside Action execute of ViewMappedDetailsAction....");
		try{
		HttpSession session=request.getSession();
		String standardCatalogId=(String)session.getAttribute("standardCatalogId");
		log.info("standardCatalogId "+standardCatalogId);
		String query="";
		query="for $i in collection('tig:///CatalogManager/ProductMaster/')";
		query=query+" where $i//catalogID='"+standardCatalogId+"' ";
		query=query+" return concat(data($i//NDC),',',data($i//ProductName),',',data($i//ManufacturerName),',',data($i//Description))";
		List strList=queryRunner.returnExecuteQueryStrings(query);
		request.setAttribute("MasterList",strList);
		List list=(List)session.getAttribute("mappedList");
		log.info(list.get(0));
		}catch(Exception ex)
		{
	    	ex.printStackTrace();
			log.error("Error in ViewMappedDetailsAction execute()" + ex);
			return mapping.findForward("exception");
	
		}
		return mapping.findForward("success");
	}


}
