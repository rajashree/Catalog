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

 
package com.rdta.catalog.gcpim.action;

import java.util.Enumeration;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.rdta.Admin.Utility.Helper;
import com.rdta.catalog.Constants;
import com.rdta.catalog.PersistanceUtil;
import com.rdta.catalog.SchemaTree;
import com.rdta.catalog.session.CatalogContext;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

/**
 * Catalog information collecting from the reques form.
 * 
 *  
 */
public class UpdateSchemaTreeAction extends Action {
	private static Log log = LogFactory.getLog(UpdateSchemaTreeAction.class);

	private static boolean flag = false;

	private static final QueryRunner queryrunner = QueryRunnerFactory
			.getInstance().getDefaultQueryRunner();

	Connection conn;

	Statement stmt;
	
	public void TLClose() {
		try {
			log
					.info("Closing the TigerLogic Connection in SaveDynamicFormAction..........");
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

	public static Node checkNode(Node node, String name) {

		if (node.hasChildNodes()) {
			NodeList list = node.getChildNodes();
			Node checknode = XMLUtil.getNode(node, name);
			if (checknode != null) {
				flag = true;
			}
			int size = list.getLength();
			for (int n = 0; n < size; n++) {
				checkNode(list.item(n), name);
			}
		}

		return node;
	}
    
		
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

							try {
								System.out.println("*****************UpdateSchemaTree Action Called*************");
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
			
			/*
			  
			 List accessList = queryrunner.returnExecuteQueryStrings("tlsp:validateAccess('"+sessionID+"','5.03','Read')");
				String readStatus=accessList.get(0).toString();
				if(readStatus.equals("false")){
					request.setAttribute("kitread","No");
					String pName = request.getParameter("ProductName");
					return mapping.findForward("readfailure");	
				}
			   
			 */
			String operationType = request.getParameter("operationType");
			
			if(operationType.equalsIgnoreCase("Help")){
				System.out.println("Operation Type :"+operationType);
				
				request.setAttribute("help","true");
				System.out.println("Help came");
				return mapping.findForward("success");
			}
			
			Enumeration enum = request.getAttributeNames();

			String Mandatory = request.getParameter("Mandatory");
			String allowAllias = request.getParameter("allowAllias");
			String dataType = request.getParameter("dataType");
			String displayOrder = request.getParameter("displayOrder");
			String testOrder ="";
			if(displayOrder == null){
			System.out.println("checking here displayOrder");
				testOrder = "screen2";
			}
			System.out.println("Testing displayOrder"+displayOrder);
			String catalogGenId = request.getParameter("catalogGenId");
			String fromModule = request.getParameter("fromModule");

			String xpath = request.getParameter("xpath");
			String name = request.getParameter("name");

			String values = request.getParameter("values");
		
			String tpGenId = request.getParameter("GenId");
		
			log.debug(" CatalogGenId  : " + catalogGenId + "  fromModule : "
					+ fromModule);
			log.info(" UpdateSchemaTreeAction CatalogGenId  : "
					+ catalogGenId + "  fromModule : " + fromModule + " xpath "
					+ xpath);
			log.info(" operationType  : " + operationType
					+ "  name : " + name);
			log.info(" values  : " + values);

			
			System.out.println("***************Operation Type :"+operationType+"CatalogGenId :"+catalogGenId);
			
	
			
			
			CatalogContext catalogContext = null;
		
			if (Mandatory == null) {
				Mandatory = "false";
			} else {
				Mandatory = "true";
			}
			if (allowAllias == null) {
				allowAllias = "false";
			} else {
				allowAllias = "true";
			}
			if (values == null) {
				values = "";
			}
			if (dataType == null) {
				dataType = "string";
			}
			if (displayOrder == null || displayOrder.equals("")) {
				System.out.println("checking displayOrder");
				displayOrder = "1";
			}
			
			if (sess != null) {
				/*
				 * if(fromModule != null && fromModule.equalsIgnoreCase("TP") ) {
				 * TradingPartnerContext context =
				 * (TradingPartnerContext)session.getAttribute("TradingPartnerContext");
				 * catalogContext = context.getCatalogContext(); } else {
				 * catalogContext = (CatalogContext)
				 * session.getAttribute(Constants.SESSION_CATALOG_CONTEXT);
				 *  }
				 */
				if (tpGenId != null) {
					request.setAttribute("tpGenId", tpGenId);
				}
			
				sess.setAttribute("mandatory","true");
				catalogContext = (CatalogContext) sess
						.getAttribute(Constants.SESSION_CATALOG_CONTEXT);
				Node catalogNode = catalogContext.getCatalogNode(catalogGenId);
				Node node = XMLUtil.getNode(catalogNode, "schema/*");
				Node subNode = XMLUtil.getNode(catalogNode, "schema");
				
				
				if (node != null) {
					SchemaTree tree = new SchemaTree(node);

					
					log.info("Number of Nodes "+tree.numOfNodes());
					int numEle = Integer.parseInt(displayOrder);
					
				/*	if( numEle > tree.numOfNodes()){
						
						return  mapping.findForward("success");
					} */
				
					

					if (operationType.equalsIgnoreCase("ADD")) {
									
						//	UpdateSchemaTreeAction.flag = false;
						//	UpdateSchemaTreeAction.checkNode(node,name);
						log.info("********In the  Add Method *******");
						
						Node mynode = XMLUtil.getNode(subNode, xpath);
						if (mynode != null) {
								System.out.println("I am exists"+mynode.getNodeName());
								mynode = XMLUtil.getNode(mynode, name);
								
						}
						if (mynode == null) {
				
							tree.addElement(name, xpath, values, Mandatory,
									allowAllias, dataType, displayOrder);
							sess.setAttribute("savetree","true");
							request.setAttribute("dupexists","false");
							
						}else{
							
							System.out.println("displayOrder"+displayOrder);
							request.setAttribute("testOrder",testOrder);
							request.setAttribute("path1",xpath);
							request.setAttribute("showscreen",xpath);
							request.setAttribute("dupexists","true");
							return mapping.findForward("success");
						}

					} else if (operationType.equalsIgnoreCase("UPDATE")) {

/*						
					//	tree.replaceElement(name, xpath, values, Mandatory,
					
						int numElements = tree.numOfNodes();
						if (numElements>=Integer.parseInt(displayOrder))
	*/			
						System.out.println("XPATH = "+xpath);
					
						
						int numElements = tree.numOfNodes();
						
						
						
						if (numElements>=Integer.parseInt(displayOrder))
						{	
							
							int lasIndex = xpath.lastIndexOf("/");
							
							String newPath = xpath.substring(0,lasIndex)+"/"+name;
							String oldName = xpath.substring(lasIndex+1,xpath.length());
						
							Node pNode = null;
						    if (!oldName.equals(name))
							pNode =XMLUtil.getNode(subNode,newPath);
							
								
								if(pNode == null){
								tree.replaceElement(name, xpath, values, Mandatory,
									allowAllias, dataType, displayOrder);
								sess.setAttribute("savetree","true");
								}else{
								request.setAttribute("testOrder",testOrder);
								request.setAttribute("path1",xpath);
								request.setAttribute("showscreen",xpath);
								request.setAttribute("dupexists","true");
								return mapping.findForward("success");
							}
					
						}
					
					} else if (operationType.equalsIgnoreCase("DELETE")) {
						tree.deleteElement(xpath, name, displayOrder);
						sess.setAttribute("savetree","true");
					} else if (operationType.equalsIgnoreCase("SAVE")|| operationType.equalsIgnoreCase("FIND")) {
						//udate catalog node in database with schema defination
						boolean flag=true;
						System.out.println("catal"+node.getNodeName());
						if(XMLUtil.getNode(node,"ProductName")== null)
						{
								flag=false;
						}else{
								if(XMLUtil.getValue(XMLUtil.getNode(node,"ProductName"),"@mandatory").equals("false"))
									flag=false;
						}
						if(XMLUtil.getNode(node,"ManufacturerName")==null){
							System.out.println("ManufacturerName null");
							flag=false;
						}else{
							if(XMLUtil.getValue(XMLUtil.getNode(node,"ManufacturerName"),"@mandatory").equals("false"))
								flag=false;
						}
						if(XMLUtil.getNode(node,"LotNumber")==null){
							System.out.println("ManufacturerName null");
							
							flag=false;
						}else{
							if(XMLUtil.getValue(XMLUtil.getNode(node,"LotNumber"),"@mandatory").equals("false"))
								flag=false;
						}
						if(XMLUtil.getNode(node,"NDC")==null)
						{
									flag=false;
						}else{
							
							if(XMLUtil.getValue(XMLUtil.getNode(node,"NDC"),"@mandatory").equals("false"))
								flag=false;
						}
						if(XMLUtil.getNode(node,"GTIN")==null){
							System.out.println("GTIN null");
							flag=false;
						}else{
							if(XMLUtil.getValue(XMLUtil.getNode(node,"GTIN"),"@mandatory").equals("false"))
								flag=false;
						}
						if(flag == false){
							sess.setAttribute("mandatory","false");
								return mapping.findForward("success");
						}else{
							sess.setAttribute("mandatory","true");
						}
						
						log.info("in SAVE method UpdateSchema Tree");
						StringBuffer buff = new StringBuffer(
								"$a/Catalog/catalogID='");
						buff.append(catalogGenId);
						buff.append("'");
					//	Product Name , ManufacturerName, Lot Number, NDC ,GTIN 
						String time = helper.updateePharmaEAGTimeStamp(XMLUtil
								.getValue(catalogNode, "catalogID"));
						log.info("Update =" + time);
						Node updatedNode = XMLUtil.parse(time);
						Node currentNode = XMLUtil.getNode(catalogNode,
								"EAG-TimeStamp");
						XMLUtil.putValue(catalogNode,
								"EAG-TimeStamp/CreatedDate", XMLUtil.getValue(
										updatedNode, "CreatedDate"));
						XMLUtil.putValue(catalogNode,
								"EAG-TimeStamp/updated-timestamp", XMLUtil
										.getValue(updatedNode,
												"updated-timestamp"));
						XMLUtil.putValue(catalogNode,
								"EAG-TimeStamp/origin-serverID", XMLUtil
										.getValue(updatedNode,
												"origin-serverID"));
						XMLUtil.putValue(catalogNode,
								"EAG-TimeStamp/updated-serverID", XMLUtil
										.getValue(updatedNode,
												"updated-serverID"));
						XMLUtil.putValue(catalogNode, "EAG-TimeStamp/reported",
								XMLUtil.getValue(updatedNode, "reported"));

						PersistanceUtil.updateDocument(catalogNode,
								Constants.CATALOG_COLL, buff.toString());
					
						sess.removeAttribute("savetree");
						sess.setAttribute("savetree","false");
						
						if(operationType.equalsIgnoreCase("FIND")){
							System.out.println("**************************save1 ");
							String targetUrl = request.getParameter("targetUrl");
							request.setAttribute("targetUrl",targetUrl);
							return mapping.findForward("goback");
						}
					}
				} else{
					
				
					Document rootDocument = catalogNode.getOwnerDocument();
					Node newElement = rootDocument.createElement("Product");
					Node parentNode = XMLUtil.getNode(catalogNode, "schema");
					parentNode.appendChild(newElement);
				}

				TLClose();

			}
			return mapping.findForward("success");
		} catch (Exception ex) {
			return mapping.findForward("exception");
		}
	
			
	}
}