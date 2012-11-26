
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

import com.rdta.Admin.Utility.Helper;
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;
import com.rdta.commons.CommonUtil;
import com.rdta.commons.xml.XMLUtil;


public class RepackProducts extends Action {
	private static Log log=LogFactory.getLog(RepackProducts.class);
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();

	Connection conn; 
	Statement stmt;
	String servIP = null;
	String clientIP = null;
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
	try{
		HttpSession sess = request.getSession();
		Helper helper = new Helper();
		String sessionID = (String)sess.getAttribute("sessionID");
		log.info("sessionID in Action :"+sessionID);
		String validateResult = helper.ValidateUserSession(stmt, sessionID, clientIP);        
		Collection colln= new ArrayList();	
		Collection products = new ArrayList();
			clientIP = request.getRemoteAddr();		
		
			log.info("Validating The Session");
			
			RepackedForm theForm= (RepackedForm)form;
			PreviousProductForm productForm=null;
			
			
			//String envId = request.getParameter("envId");
			//Validating Session
					
			String pedId = request.getParameter("PedigreeId");
			String collection = request.getParameter("collection");
			String strProc = "tlsp:getPreviousProducts('"+collection+"','"+pedId+"')";
			
			System.out.println("Query for getting Previous products :"+strProc);
			List list2 = queryRunner.returnExecuteQueryStrings(strProc);;
			
			System.out.println("Output from Query :"+(String)list2.get(0));		
			Node repackedNode = (Node) XMLUtil.parse((String)list2.get(0));
			Node previousProducts= XMLUtil.getNode(repackedNode,"previousProducts");
			Node productInfo = XMLUtil.getNode(repackedNode,"productInfo/productInfo");
			Node ItemInfo = XMLUtil.getNode(repackedNode,"itemInfo");
			NodeList prList = previousProducts.getChildNodes();
			int length = prList.getLength();
			for ( int i=0;i<length;i++){
				Node preProductInf = prList.item(i);
				productForm =  new PreviousProductForm();
			System.out.println("Maunfacture"+XMLUtil.getValue(preProductInf,"previousProductInfo/manufacturer"));
				productForm.setManuFacture(XMLUtil.getValue(preProductInf,"previousProductInfo/manufacturer"));
				
				System.out.println("Manf name test :"+productForm.getManuFacture());
				
				productForm.setPreviousExpDate(XMLUtil.getValue(preProductInf,"itemInfo/expirationDate"));
				productForm.setQuantity(XMLUtil.getValue(preProductInf,"itemInfo/quantity"));
				productForm.setPreviousLot(XMLUtil.getValue(preProductInf,"itemInfo/lot"));
				productForm.setProductCode(XMLUtil.getValue(preProductInf,"previousProductInfo/productCode"));
				colln.add(productForm);
			}
			System.out.println("Size of collection::::"+colln.size());
			
			request.setAttribute("previousProducts11",colln);
			theForm.setCurrentManufacture(XMLUtil.getValue(productInfo,"manufacturer"));
			theForm.setDosageForm(XMLUtil.getValue(productInfo,"dosageForm"));
			theForm.setDrugName(XMLUtil.getValue(productInfo,"drugName"));
			theForm.setNewNDC(XMLUtil.getValue(productInfo,"productCode"));
			theForm.setStrength(XMLUtil.getValue(productInfo,"strength"));
			theForm.setContainerSize(XMLUtil.getValue(productInfo,"containerSize"));
			theForm.setLotNo(XMLUtil.getValue(ItemInfo,"itemInfo/lot"));
			theForm.setQuantity(XMLUtil.getValue(ItemInfo,"itemInfo/quantity"));
			theForm.setExpDate(XMLUtil.getValue(ItemInfo,"itemInfo/expirationDate"));
			products.add(theForm);
			
			request.setAttribute("productInfo",products);
			
			return  mapping.findForward("success");	
			
	
	}catch ( Exception ex){
	
		log.error("Error in execute()" + ex);
		throw new PersistanceException(ex);	
	}
	
	}
	
	
}
