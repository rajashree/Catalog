/*
 * Created on Jan 7, 2006
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

import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
 
/**
 * @author Jagadish Pampatwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PedigreeBankListAction extends Action
{
    private static Log log=LogFactory.getLog(PedigreeBankListAction.class);
	

	private static final QueryRunner queryrunner = QueryRunnerFactory
			.getInstance().getDefaultQueryRunner();
    public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response)
								 throws Exception {
    	log.info("Inside Action PedigreeBankListAction....... ");
		log.info("Inside Action execute of PedigreeBankListAction....");
		try{
			HttpSession session = request.getSession();

			String sessionID = (String)session.getAttribute("sessionID");
			System.out.println("****************"+sessionID);

		
			String pagenm = request.getParameter("pagenm");
			String tp_company_nm = request.getParameter("tp_company_nm");
			String lotNo=(String)request.getParameter("lot");
			 String NDC=(String)request.getParameter("ndc");
			System.out.println("LotNum"+lotNo);
			
			
			  System.out.println(NDC);	
			
			
			  if(lotNo.equals("")&& !NDC.equals(""))
				 {
				 	 
				 	
				  
	          
	           System.out.println(NDC);
				
	           
				String xQuery="for $s in collection('tig:///ePharma/PedigreeBank')/PedigreeBank/InventoryOnHand[NDC='"+ NDC+"']/LotInfo";
				xQuery=xQuery+" return"; 
				xQuery=xQuery+"(";
				xQuery=xQuery+"<TR class='tableRow_Off'>";
				xQuery=xQuery+"<TD width='250'  align='center'>{data($s/SWLotNum)}</TD>";
				xQuery=xQuery+"<TD width='250' align='center'>{data($s/Quantity)}</TD>";
				xQuery=xQuery+"</TR>";
				xQuery=xQuery+")";
				System.out.println(""+xQuery);

				 String result=queryrunner.returnExecuteQueryStringsAsString(xQuery);
				 if(result.equals("")){
				 	result="<TR class='tableRow_Off' width='100%'> <TD   align='center'> No Data Exists	</TD><td/>	</TR>";
				 	 
				 }
				 request.setAttribute("result",result);
				 request.setAttribute("NDC",NDC);
				 }
			
			
			
			
			if(!lotNo.equals("")&& NDC.equals(""))
			 {
			 	PedigreeBankUtil pbu=new PedigreeBankUtil();
			 	NDC=pbu.getNDCforSWLotNumber(lotNo);
			 	
			 	
			  
           
            System.out.println(NDC);
			
            
			String xQuery="for $s in collection('tig:///ePharma/PedigreeBank')/PedigreeBank/InventoryOnHand/LotInfo[SWLotNum='"+ lotNo+"']";
			xQuery=xQuery+" return"; 
			xQuery=xQuery+"(";
			xQuery=xQuery+"<TR class='tableRow_Off'>";
			xQuery=xQuery+"<TD width='250'  align='center'>{data($s/SWLotNum)}</TD>";
			xQuery=xQuery+"<TD width='250' align='center'>{data($s/Quantity)}</TD>";
			xQuery=xQuery+"</TR>";
			xQuery=xQuery+")";
			System.out.println(""+xQuery);

			 String result=queryrunner.returnExecuteQueryStringsAsString(xQuery);
			 if(result.equals("")){
			 	result="<TR class='tableRow_Off' width='100%'> <TD   align='center'> No Data Exists	</TD><td/>	</TR>";
			 	 
			 }
			 request.setAttribute("result",result);
			 request.setAttribute("NDC",NDC);
			 }
			
			if(!lotNo.equals("")&&!NDC.equals(""))
			 {
			 	 
			 	
			  
          
           System.out.println(NDC);
			
           
			String xQuery="for $s in collection('tig:///ePharma/PedigreeBank')/PedigreeBank/InventoryOnHand/LotInfo[SWLotNum='"+ lotNo+"']";
			xQuery=xQuery+" return"; 
			xQuery=xQuery+"(";
			xQuery=xQuery+"<TR class='tableRow_Off'>";
			xQuery=xQuery+"<TD width='250'  align='center'>{data($s/SWLotNum)}</TD>";
			xQuery=xQuery+"<TD width='250' align='center'>{data($s/Quantity)}</TD>";
			xQuery=xQuery+"</TR>";
			xQuery=xQuery+")";
			System.out.println(""+xQuery);

			 String result=queryrunner.returnExecuteQueryStringsAsString(xQuery);
			 if(result.equals("")){
			 	result="<TR class='tableRow_Off' width='100%'> <TD   align='center'> No Data Exists	</TD><td/>	</TR>";
			 	 
			 }
			 request.setAttribute("result",result);
			 request.setAttribute("NDC",NDC);
			 }
			 
			 
		log.info("before retuning Success! ");
		}catch(PersistanceException e){
			log.error("Error in PedigreeBankListAction execute method........." +e);
			throw new PersistanceException(e);
		}
		catch(Exception ex){			
			ex.printStackTrace();
    		log.error("Error in PedigreeBankListAction execute method........." +ex);
    		throw new Exception(ex);
		}
		
		return mapping.findForward("success");
   }
	
}
   
  
