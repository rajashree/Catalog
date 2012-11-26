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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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
public class PedigreeBankSearchAction extends Action
{
    private static Log log=LogFactory.getLog(PedigreeBankSearchAction.class);
	

	private static final QueryRunner queryrunner = QueryRunnerFactory
			.getInstance().getDefaultQueryRunner();
    public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response)
								 throws Exception {
    	log.info("Inside Action PedigreeBankSearchAction....... ");
		log.info("Inside Action execute of PedigreeBankSearchAction....");
		String forward="";
	        try{
			HttpSession session = request.getSession();
			String sessionID = (String)session.getAttribute("sessionID");
			String pagenm = request.getParameter("pagenm");
			String tp_company_nm = request.getParameter("tp_company_nm");
		    List accessList = queryrunner.returnExecuteQueryStrings("tlsp:validateAccess_MD('"+sessionID+"','9.0','Read')");
			String viewStatus = accessList.get(0).toString();
			 
	        if(viewStatus.equals("true"))
	        {
			String xQuery = "for $i in collection('tig:///ePharma_MD/PedigreeBank')/PedigreeBank/InventoryOnHand";  
			xQuery=xQuery+" return"; 
			xQuery=xQuery+"<TR class='tableRow_Off'>";
			xQuery=xQuery+"<TD height='16'  align='center'>{data($i/NDC)}</TD>";
			xQuery=xQuery+"<TD  height='16' align='center'>{data($i/TotalInventory)}</TD>";
			xQuery=xQuery+"<TD height='16' align='center'>NA</TD>";                                                                                    
			xQuery=xQuery+"<TD height='16' align='center'>{data($i/TotalInventory)}</TD>";
			xQuery=xQuery+"</TR>"; 
			 
			log.info(xQuery);
            String result=queryrunner.returnExecuteQueryStringsAsString(xQuery);
            session.setAttribute("result",result);
            forward="success";
	        }
	         
            else{
            	forward="accessDenied";
            }
           				
		log.info("before retuning Success! ");
		}catch(PersistanceException e){
			log.error("Error in PedigreeBankSearchAction execute method........." +e);
			throw new PersistanceException(e);
		}
		catch(Exception ex){			
			ex.printStackTrace();
    		log.error("Error in PedigreeBankSearchAction execute method........." +ex);
    		throw new Exception(ex);
		}
		return mapping.findForward(forward);
	  
		
   }
	
}
   
  
