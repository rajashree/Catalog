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

import java.io.PrintWriter;
import com.rdta.pedigreebank.PedigreeBankUtil;
 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
 
 
/**
 * @author Jagadish Pampatwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PedigreeBankGraphAction extends Action
{
    private static Log log=LogFactory.getLog(PedigreeBankGraphAction.class);
	

	private static final QueryRunner queryrunner = QueryRunnerFactory
			.getInstance().getDefaultQueryRunner();
	
	
	
    public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response)
								 throws Exception {
    	log.info("Inside Action PedigreeBankAction....... ");
		log.info("Inside Action execute of PedigreeBankAction....");
		try{
			PedigreeBankUtil pbu= new PedigreeBankUtil();
			HttpSession session = request.getSession();
 
			String sessionID = (String)session.getAttribute("sessionID");
			System.out.println("****************"+sessionID);
			String pagenm = request.getParameter("pagenm");
			String tp_company_nm = request.getParameter("tp_company_nm");

		    String NDC=(String)request.getParameter("ndc");
		    String LotNo=(String)request.getParameter("lot");
		    String pieChartname="";
		    String barChartname="";
		    PrintWriter pw = new PrintWriter(System.out);
		    if(!LotNo.equals("")){
		    	//pieChartname=pbu.getlotQuantity(LotNo);
				barChartname=pbu.generateBarChartForLotNumber(LotNo,session,pw);
				//request.setAttribute("pieChartname",pieChartname);
			    request.setAttribute("barChartnameForLot",barChartname);
			    
			    request.setAttribute("LotNo",LotNo);
		    }
            if(!NDC.equals("")){
		    pieChartname=pbu.generatePieChart(NDC,session,pw);
		   barChartname=pbu.generateBarChart(NDC,session,pw);
		   
		    request.setAttribute("pieChartname",pieChartname);
		   request.setAttribute("barChartname",barChartname);
			request.setAttribute("NDC",NDC);
            }
		log.info("before retuning Success! ");
		return mapping.findForward("success");
		} catch(Exception ex){			
			ex.printStackTrace();
    		log.error("Error in PedigreeBankListAction execute method........." +ex);
    		throw new Exception(ex);
		}
		
   }
	
}
   
  
