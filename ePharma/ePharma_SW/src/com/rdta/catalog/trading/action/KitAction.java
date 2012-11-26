
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

import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rdta.Admin.Utility.Helper;
import com.rdta.catalog.trading.model.ProductMaster;
import com.rdta.catalog.trading.model.ImageStore;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.rdta.catalog.Constants;
import com.rdta.catalog.XMLStructure;
import com.rdta.catalog.OperationType;
import com.rdta.commons.CommonUtil;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;

import com.rdta.catalog.session.TradingPartnerContext;

import com.rdta.catalog.session.CatalogContext;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

/**
 * Product information collecting from the reques form.
 * 
 *  
 */
public class KitAction extends Action {
	private static Log log = LogFactory.getLog(KitAction.class);

	private static final QueryRunner queryrunner = QueryRunnerFactory
			.getInstance().getDefaultQueryRunner();

	Connection conn;

	Statement stmt;

	public void TLClose() {
		try {

			log	.info("Closing the TigerLogic Connection in KitAction..........");

			stmt.close();
			conn.logoff();
			conn.close();
			log.info("Connection Closed !!!!!!!!!!!!");
		} catch (com.rdta.tlapi.xql.XQLConnectionException e) {
			System.err.println(e);
		} catch (com.rdta.tlapi.xql.XQLException e) {
			System.err.println(e);
		}
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		try {

			System.out
					.println("****************crate new kit************************** ");

			log.info("Inside  CreateKit execute Method");
			log.info("Operation Type" + request.getParameter("operationType"));
			String opType = request.getParameter("operationType");
			System.out.println("OperationType " + opType);
			boolean flag = false;

			log.info(" Inside List Action ");
			HttpSession sess = request.getSession();
			Helper helper = new Helper();
			String clientIP = request.getRemoteAddr();

			conn = helper.ConnectTL();
			stmt = helper.getStatement(conn);
			log.info("Validating The Session");

			// Validating Session
			String sessionID = (String) sess.getAttribute("sessionID");
			log.info("sessionID in Action is :" + sessionID);
			String validateResult = helper.ValidateUserSession(stmt, sessionID,
					clientIP);
			log.info("ValidateResult *** =" + validateResult);
			if (!(validateResult.equals("VALID"))) {
				//return a forward to login page.
				TLClose();
				return mapping.findForward("loginPage");
			}

			TLClose();

			if (opType == null)
				opType = "";

	
			if (opType.equals("FIND")) {

				flag = true;
				request.setAttribute("fromproductsearch", "yes");
				List accessList = queryrunner
						.returnExecuteQueryStrings("tlsp:validateAccess('"
								+ sessionID + "','5.03','Read')");
				String readStatus = accessList.get(0).toString();

				accessList = queryrunner
						.returnExecuteQueryStrings("tlsp:validateAccess('"
								+ sessionID + "','5.03','Update')");
				String addproduct = accessList.get(0).toString();
				if (addproduct.equalsIgnoreCase("false")) {
					sess.setAttribute("addProducttokit", "false");
					sess.setAttribute("savingKit", "false");
				}
				accessList = queryrunner
						.returnExecuteQueryStrings("tlsp:validateAccess('"
								+ sessionID + "','5.03','Delete')");
				String rmProduct = accessList.get(0).toString();
				if (rmProduct.equalsIgnoreCase("false")) {
					sess.setAttribute("rproductFromKit", "false");
				}
			}

			String fromproductsearch = (String) request
					.getAttribute("fromproductsearch");
			if (fromproductsearch == null || fromproductsearch.equals(""))
				fromproductsearch = "";

			List accessList = null;
			if (!flag) {
				if (fromproductsearch.equals("")) {
					fromproductsearch = request
							.getParameter("fromproductsearch");
					if (fromproductsearch.equals("")) {
						accessList = queryrunner
								.returnExecuteQueryStrings("tlsp:validateAccess('"
										+ sessionID + "','5.03','Insert')");

					} else {
						accessList = queryrunner
								.returnExecuteQueryStrings("tlsp:validateAccess('"
										+ sessionID + "','5.03','Update')");
						request.setAttribute("fromproductsearch", "yes");
					}
				} else {
					accessList = queryrunner
							.returnExecuteQueryStrings("tlsp:validateAccess('"
									+ sessionID + "','5.03','Update')");
					request.setAttribute("fromproductsearch", "yes");
				}

			} else {
				accessList = queryrunner
						.returnExecuteQueryStrings("tlsp:validateAccess('"
								+ sessionID + "','5.03','Insert')");
			}
			log.info("Read Access inside Product Search is :"
					+ accessList.get(0));

			String insertStatus = accessList.get(0).toString();
			log.info("The insertStatus is " + insertStatus);
			if (insertStatus.equals("false")) {
				request.setAttribute("kitinsert", "false");
				request.setAttribute("gtin", request.getParameter("gtin"));

				request.setAttribute("ProductName", request
						.getParameter("ProductName"));
				request.setAttribute("Description", request
						.getParameter("Description"));

				return mapping.findForward("success");
			}

			request.setAttribute("kitinsert", "");

			String operation = request.getParameter("operationType");
			log.debug("kitAction class  Operation :" + operation);
			log.debug("Before doing Operation : " + operation);
			ProductMaster productMaster = null;

			String from = request.getParameter("From");
			Enumeration en = request.getParameterNames();
			String str = null;
			for (; en.hasMoreElements();) {
				str = (String) en.nextElement();

			}

			if (operation != null) {
				if(operation.trim().equalsIgnoreCase("SAVE")){
					
					 productMaster  = (ProductMaster) sess.getAttribute(Constants.SESSION_KITREF_CONTEXT);
					 if(productMaster != null){
					 	System.out.println(" I am in Save where ProductMaster != null");
					 	productMaster.upDateRequestInfo(request);
					 	if (sess.getAttribute("showKitnow") != null)
							productMaster.addKitInformation(request);
						System.out.println(" Request GenId ="+request.getParameter("productGenId"));
					 	if(ProductMaster.find(request.getParameter("productGenId"))!= null){
					 		
					 		System.out.println(" Request GenId ="+request.getParameter("genId"));
					 		System.out.println("going to update");
					 		productMaster.update();
					 	}
					 	else{
					 		    productMaster.insert();
					 	}
					 		    sess.setAttribute("showKitnow", null);
						sess.setAttribute(Constants.SESSION_KITREF_CONTEXT,
								productMaster);
					 	
					 }else{
						System.out.println(" I am in Save where ProductMaster == null");
						productMaster = new ProductMaster(request);
						productMaster.insert();
					 }
					
					
				}else if (operation.trim().equalsIgnoreCase(OperationType.ADD)) {
					System.out.println("************In operation Add*********");
					if (sess != null) {
						log.info("Session is not null");
						productMaster = (ProductMaster) sess
								.getAttribute(Constants.SESSION_KITREF_CONTEXT);
						if (productMaster != null) {
							//in case user first add products then hit the save
							//button
							System.out.println("************ product Master is not Null********");
							log.info("I came here in side Add if product not =null");
							productMaster.upDateRequestInfo(request);
							String productGenId = XMLUtil.getValue(
									productMaster.getNode(), "genId");
						//	productMaster.insert();
							System.out.println(" Add proudctMastere not null");
							//saveUploadedImages(form, productMaster);
						} else {
							System.out.println("************ product Master is Null********");
							productMaster = new ProductMaster(request);
							//productMaster.insert();
							//	saveUploadedImages(form, productMaster);
						}
					}

					//	productMaster.insert();

				} else if (operation.trim().equalsIgnoreCase(
						OperationType.UPDATE)) {
					log.info("this is the  Operationtype ***" + operation);
					System.out.println("update update************");

					if (sess != null) {
						productMaster = (ProductMaster) sess
								.getAttribute(Constants.SESSION_KITREF_CONTEXT);
						productMaster.upDateRequestInfo(request);
						String genId = XMLUtil.getValue(
								productMaster.getNode(), "genId");

						System.out.println("productMaster"
								+ XMLUtil.convertToString(productMaster
										.getNode()));
						if (genId == null
								|| genId.trim().equalsIgnoreCase("null")
								|| genId.trim().equalsIgnoreCase("")) {

							//	saveUploadedImages(form, productMaster);
							System.out.println("Going to insert the document ");
							productMaster.insert();
							sess.setAttribute(Constants.SESSION_KITREF_CONTEXT,
									productMaster);
							System.out.println("end update update************");

						} else {

							//saveUploadedImages(form, productMaster);
							//save the document
							//productMaster.addQuantity(request.getParameter("quantity"));

							if (sess.getAttribute("showKitnow") != null)
								productMaster.addKitInformation(request);

							productMaster.update();
							sess.setAttribute("showKitnow", null);
							sess.setAttribute(Constants.SESSION_KITREF_CONTEXT,
									productMaster);

						}
					}

				} else if (operation.trim()
						.equalsIgnoreCase(OperationType.FIND)) {
					log.info("Operationtype ***" + operation);
					log.debug(" finding record : "
							+ request.getParameter("genId"));
					productMaster = ProductMaster.find(request
							.getParameter("genId"));
				} else {
					log.warn(" Operation Type : " + operation
							+ " not dealing in this action!");
				}
			}

			if (productMaster != null) {
				if (sess != null) {
					sess.setAttribute(Constants.SESSION_KITREF_CONTEXT,
							productMaster);
				}
			}

			if (from != null && from.equalsIgnoreCase("selection")) {
				log.info(" Before forwarding to searchPage");

				request.setAttribute("From", from);
				request.setAttribute("prduct", "Yes");
				request.setAttribute("kitName", request
						.getParameter("ProductName"));

				log.info(" XML String Node"
						+ XMLUtil.convertToString(productMaster.getNode()));
				return mapping.findForward("searchPage");
			}

			log.info("before retuning Success! ");
			return mapping.findForward("success");

		} catch (Exception ex) {
			return mapping.findForward("exception");
		}

	}

	private void saveUploadedImages(ActionForm form, ProductMaster productMaster)
			throws Exception {

		FormFile packagingFormFile = null;
		FormFile productMarkingFormFile = null;

		ProductMasterForm theForm = (ProductMasterForm) form;
		if (theForm != null) {
			packagingFormFile = theForm.getPackagingFile();
			productMarkingFormFile = theForm.getProductMarkingFile();

			//save packaging image
			if (packagingFormFile != null
					&& packagingFormFile.getFileSize() > 0) {

				String genId = CommonUtil.getGUID();
				String fileName = genId + packagingFormFile.getFileName();
				InputStream stream = packagingFormFile.getInputStream();
				CommonUtil.saveImageFile(stream, fileName);

				XMLUtil.putValue(productMaster.getNode(),
						"Overt/Packagings/Packaging/ImageUrl", fileName);

			}

			if (productMarkingFormFile != null
					&& productMarkingFormFile.getFileSize() > 0) {

				String genId = CommonUtil.getGUID();
				String fileName = genId + productMarkingFormFile.getFileName();
				InputStream stream = productMarkingFormFile.getInputStream();
				CommonUtil.saveImageFile(stream, fileName);

				XMLUtil.putValue(productMaster.getNode(),
						"Overt/ProductMarkings/ProductMarking/ImageUrl",
						fileName);

			}

		}//end of theForm if
	}

}