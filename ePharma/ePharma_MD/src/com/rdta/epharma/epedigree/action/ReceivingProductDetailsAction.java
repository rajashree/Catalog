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

import java.io.InputStream;
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

import com.rdta.Admin.Utility.Helper;
import com.rdta.catalog.Constants;
import com.rdta.commons.CommonUtil;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

public class ReceivingProductDetailsAction extends Action{
	private static Log log=LogFactory.getLog(ReceivingProductDetailsAction.class);
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	String servIP = null;
	String clientIP = null;
	
	private static Map map = new HashMap();
	
	Connection conn; 
	Statement stmt;
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("Coming inside of the ReceivingProductDetailsAction");
		
		Recprddetails theform =null;
			
        try{
        	
        	HttpSession sess = request.getSession();
			Helper helper = new Helper();
			clientIP = request.getRemoteAddr();		
			
			conn = helper.ConnectTL(); 
			stmt = helper.getStatement(conn);
			log.info("Validating The Session");
			
			//Validating Session
			String sessionID = (String)sess.getAttribute("sessionID");
			log.info("sessionID in Action :"+sessionID);
			String validateResult = helper.ValidateUserSession(stmt, sessionID, clientIP);        
		
			if ( !validateResult.equals("VALID")){
			   // return a forward to invalid .
			    return mapping.findForward("loginPage");
			}
			String pedId = request.getParameter("PedigreeId");
			System.out.println("Received pedigree Products Pedigree Details"+pedId);

			String buff = "tlsp:RecProductDetails_MD('"+pedId+"')";
  			System.out.println("Query for getting product details: "+buff);

  			List res = queryRunner.returnExecuteQueryStrings(buff.toString());
  			System.out.println("Query for getting product details: "+res);
  			request.setAttribute("Results",res);	
				theform = (Recprddetails)form;
				Node listNode = XMLUtil.parse((String)res.get(0));
				System.out.println("listNode :"+listNode);
				
				theform.setDoseForm(CommonUtil.jspDisplayValue(listNode,"productInfo/dosageForm"));
				System.out.println("Dosage form: "+theform.getDoseForm());
				theform.setMfg(CommonUtil.jspDisplayValue(listNode,"productInfo/manufacturer"));
				theform.setDosestr(CommonUtil.jspDisplayValue(listNode,"productInfo/strength"));
				theform.setPaksize(CommonUtil.jspDisplayValue(listNode,"productInfo/containerSize"));
				theform.setNdc(CommonUtil.jspDisplayValue(listNode,"productInfo/productCode"));
				theform.setLot(CommonUtil.jspDisplayValue(listNode,"item/itemInfo/lot"));
				theform.setLotexpdate(CommonUtil.jspDisplayValue(listNode,"item/itemInfo/expirationDate"));
				theform.setQuantity(CommonUtil.jspDisplayValue(listNode,"item/itemInfo/quantity"));
				theform.setMakstatus(CommonUtil.jspDisplayValue(listNode,"root/Product/MarketingStatus"));
				theform.setPbc(CommonUtil.jspDisplayValue(listNode,"root/Product/BarCode"));
				theform.setEpc(CommonUtil.jspDisplayValue(listNode,"root/Product/EPC"));
				theform.setPdcexpdate(CommonUtil.jspDisplayValue(listNode,"root/Product/MarketingStatus"));
				theform.setUpc(CommonUtil.jspDisplayValue(listNode,"root/Product/PackageUPC"));
				theform.setDec(CommonUtil.jspDisplayValue(listNode,"products/Product/Description"));
				theform.setMfgdate(CommonUtil.jspDisplayValue(listNode,"products/Product/ManufacturedDate"));
				theform.setPdcId(CommonUtil.jspDisplayValue(listNode,"products/Product/ProductID"));
				theform.setDrugName(CommonUtil.jspDisplayValue(listNode,"productInfo/drugName"));
				System.out.println("PDC Id: "+theform.getPdcId());
			
			
  			
			
			
        }catch(Exception ex){
        	ex.printStackTrace();
   	     log.error("Error in OrderSearchAction execute method........." +ex);
   	        return mapping.findForward("exception");
        }
		
		return mapping.findForward("success");
	}
}
