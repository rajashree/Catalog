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
 * Created on Jan 7, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.rdta.pedigreebank;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.rdta.catalog.Constants;
import com.rdta.catalog.OperationType;
import com.rdta.catalog.session.TradingPartnerContext;
import com.rdta.catalog.trading.action.CatalogAction;
import com.rdta.catalog.trading.model.Catalog;
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;
/**
 * @author Jagadish Pampatwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PedigreeBankAction extends Action
{
    private static Log log=LogFactory.getLog(PedigreeBankAction.class);
	

	private static final QueryRunner queryrunner = QueryRunnerFactory
			.getInstance().getDefaultQueryRunner();
    public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response)
								 throws Exception {
    	log.info("Inside Action PedigreeBankAction....... ");
		log.info("Inside Action execute of PedigreeBankAction....");
		try{
			HttpSession session = request.getSession();

			String sessionID = (String)session.getAttribute("sessionID");
			System.out.println("****************"+sessionID);

		
			String pagenm = request.getParameter("pagenm");
			String tp_company_nm = request.getParameter("tp_company_nm");

			String xmlResult;
			String numNDCs = "";
			String numPedigrees = "";
			String xQueryNDCs = "let $sum as integer := count(collection('tig:///ePharma_MD/PedigreeBank')/PedigreeBank/count(distinct-values(fn:upper-case(NDC)))) return $sum";
			xmlResult = queryrunner.returnExecuteQueryStringsAsString(xQueryNDCs);
			if(xmlResult != null) {
				numNDCs = xmlResult;
			}
            request.setAttribute("TotalNDCs",numNDCs);
		/*	String xQueryQuantity = "let $sum as integer := sum(collection('tig:///ePharma/PedigreeBank')/Pedigree/Products/Product/xs:int(Quantity)) return $sum";
			//xmlResult = queryrunner.returnExecuteQueryStringsAsString(xQueryQuantity);
			if(xmlResult != null) {
				numPedigrees = new String(xmlResult);
			}*/

			
		log.info("before retuning Success! ");
		}catch(PersistanceException e){
			log.error("Error in PedigreeBankAction execute method........." +e);
			throw new PersistanceException(e);
		}
		catch(Exception ex){			
			ex.printStackTrace();
    		log.error("Error in PedigreeBankAction execute method........." +ex);
    		throw new Exception(ex);
		}
		return mapping.findForward("success");
   }
	
}
   
  
