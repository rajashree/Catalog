/*
 * Created on Sep 2, 2005
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
import org.w3c.dom.Node;

import com.rdta.catalog.Constants;
import com.rdta.catalog.trading.model.TradingPartner;
import com.rdta.commons.CommonUtil;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;

/**
 * @author Santosh Subramanya
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StandardCatalogAction extends Action {
	
	private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	private static Log log = LogFactory.getLog(StandardCatalogAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(false);
		if(session != null) {
			session.setAttribute(Constants.SESSION_TP_CONTEXT,null);
		}

			List list = TradingPartner.getTradingPartnerList();

		//set the result here
			request.setAttribute("TradingPartnerListInfo",list);
		
			return mapping.findForward("success");
		
	}
		
	/*	
		StringBuffer xQuery = new StringBuffer();
		
		xQuery.append("for $i in collection('tig:///CatalogManager/Catalog') " );
		xQuery.append(" where $i/Catalog/keyRef/collectionName = 'system' ");
		xQuery.append("return data($i/Catalog/tradingPartnerName)");
		
		StandardCatalogForm catForm =(StandardCatalogForm)form;
		List result = queryRunner.returnExecuteQueryStrings(xQuery.toString());
		
	    catForm.setResult(result);
		log.info("List inside StandardCatalogAction contains: "+ result.get(1));
		
		
		List list =  (List)request.getAttribute("CatalogListInfo");
		
		
		String name = "";
		
		for(int i=0; i < list.size(); i++) {
		
		 	Node tradingPartnerName =  (Node) list.get(i) ;
							
		 	name = CommonUtil.jspDisplayValue(tradingPartnerName,"tradingPartnerName");
		}	
			
		log.info("Before returning success from StandardCatalogAction");
		
		return mapping.findForward("success");*/
	
	
	

}
