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

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rdta.Admin.Utility.Helper;
import com.rdta.catalog.session.TradingPartnerContext;
import com.rdta.catalog.trading.action.CatalogUploadForm;
import com.rdta.catalog.trading.model.Catalog;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.rdta.catalog.Constants;
import com.rdta.catalog.SchemaTree;
import com.rdta.catalog.XMLStructure;
import com.rdta.catalog.OperationType;
import com.rdta.commons.CommonUtil;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;

import com.rdta.catalog.session.TradingPartnerContext;

import com.rdta.catalog.session.CatalogContext;
import com.rdta.catalog.JavaScriptTree;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

/**
 * Catalog information collecting from the reques form.
 * 
 * 
 */
public class CatalogSchemaDefAction extends Action {
	 private static Log log=LogFactory.getLog(CatalogSchemaDefAction.class);
		private static final QueryRunner queryrunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();

		Connection conn;

		Statement stmt;

		public void TLClose() {
		try {
			log.info("Closing the TigerLogic Connection in SaveDynamicFormAction..........");
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
	public static String parentNode = new String();
	static int order =1;
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		
		
		try{
		
			String catalogGenId = request.getParameter("catalogGenId");
			String fromModule = request.getParameter("fromModule");
			String operationType = request.getParameter("operationType");
			log.info(" CatalogGenId  : " + catalogGenId + "  fromModule : "
			+ fromModule);
			log.info(" operationType  : " + operationType);
			CatalogContext catalogContext = new CatalogContext();
			Catalog catalog = Catalog.find(catalogGenId);
			
	    	HttpSession sess = request.getSession();
			Helper helper = new Helper();
			String clientIP = request.getRemoteAddr();
			request.setAttribute("help","true");
			sess.setAttribute("mandatory","true");
			conn = helper.ConnectTL();
			stmt = helper.getStatement(conn);
			log.info("Validating The Session");
			sess.setAttribute("visited","no");
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
			
			
			if (catalog != null) {

			Node catalogNode = catalog.getNode();
			log.info(" Is Catalog exist "+request.getParameter("CatExists"));
			String catExist = request.getParameter("CatExists");
			if(catExist == null)catExist="";
			if(catExist.equals("yes")){
				
				queryrunner.executeQuery("tlsp:deleteMapping('"+catalogGenId+"')");
				log.info("deleted mappings if any");
			}
			//if schema creation done from the file upload
			if (operationType != null
					&& operationType.equalsIgnoreCase("createFromFile")) {

				CatalogUploadForm theForm = (CatalogUploadForm) form;
				//create schema from file
				String headerLine = getHeaderLineFromFile(theForm);
				String schemaStartName = XMLUtil.getValue(catalogNode,
						"catalogName");

				if (headerLine != null && !headerLine.equals("")
						&& schemaStartName != null) {

					createSchema(catalogNode, headerLine, schemaStartName);
				}

			}
			catalogContext.setCatalogNode(catalogGenId, catalogNode);

		
			if (sess != null) {
				/* if(fromModule != null && fromModule.equalsIgnoreCase("TP") ) {
				 TradingPartnerContext context = (TradingPartnerContext)session.getAttribute("TradingPartnerContext");
				 context.setCatalogContext(catalogContext);
				 } else {
				 session.setAttribute(Constants.SESSION_CATALOG_CONTEXT,catalogContext); 
				 }*/
				
				log.info(" Inside session setting ........");
				sess.setAttribute(Constants.SESSION_CATALOG_CONTEXT,
						catalogContext);

				//since we are using form type as multipart/form-data
				//incase of schema creation from upload process
				//we are losing control of request paramerts
				//so populate catalogId so that tree will populate later
			log.info(" Inside session setting ........"
						+ catalogGenId);
				sess.setAttribute("SESSION_CATALOGID", catalogGenId);

			}

		}//not null catalog

		log.info(" Before returning...  :");
		return mapping.findForward("success");
		}
		catch(Exception ex){
			
			ex.printStackTrace();
			return mapping.findForward("exception");
			
		}
	}
	Node global = null;
	public void createSchema(Node catalogNode, String headerLine,
			String schemaStartName) {
		log.info(" schemaStartName : " + schemaStartName);
		
		Node schemaNode = XMLUtil.getNode(catalogNode, "schema");
		Node schemaStartNode = XMLUtil.getNode(catalogNode, "schema/*");
		if (schemaNode != null) {
			log.info("schemaNode = " + schemaNode.getNodeName());
		}
		//remove already some information exist
		
		
		if (schemaStartNode != null) {
			schemaNode.removeChild(schemaStartNode);
		}
		//create from uploaded infroamtion
		Document rootDocument = catalogNode.getOwnerDocument();
		//remove the spaces since XML doen't accept spaces
		String removeSpaceSchmeStartName = schemaStartName.replaceAll(" ", "");
		/*Node newSchemaRootElement = rootDocument
				.createElement(removeSpaceSchmeStartName);
		schemaNode.appendChild(newSchemaRootElement);
		SchemaTree tree = new SchemaTree(newSchemaRootElement);
		*/
		if (headerLine.indexOf("<") != -1) {
		
			
			
		//	Node newSchemaRootElement = rootDocument
		//	.createElement(removeSpaceSchmeStartName);
		
			Node newSchemaRootElement = rootDocument
				.createElement("Product");
			
			schemaNode.appendChild(newSchemaRootElement);
			
			log.info("Appended");
			SchemaTree tree = new SchemaTree(newSchemaRootElement);

			String xy = XMLUtil.convertToString(tree.getNode());
		//	headerLine = headerLine.replaceAll("<schema>", "<schema><"
		//		+ schemaStartName + ">");
		log.info("Header Line... " + headerLine);
		//	headerLine = headerLine.replaceAll("</schema>", "</"
		//			+ schemaStartName + ">" + "</" + "schema>");
		//	headerLine = headerLine.replaceAll(
		//			"<Product displayName=\"\" id=\"\" values=\"\">", "");
		//	headerLine = headerLine.replaceAll("</Product>", "");
			
			headerLine = headerLine.trim();
			log.info(headerLine);
			Node node = XMLUtil.parse(headerLine);
			//	catalogNode.replaceChild(schemaNode,node);
			NodeList list1 = catalogNode.getChildNodes();
			
			Node mynode1 = null;
			int size = list1.getLength();
			for (int i = 0; i < size; i++) {
				if (list1.item(i).getNodeName().equals("schema")) {
					mynode1 = list1.item(i);
				}
			}
			Document doc = node.getOwnerDocument();
			Element ele = (Element )(doc.getFirstChild()).getFirstChild();
		
			try {
				order=1;
				myfunction(ele);
				XMLUtil.removeChildNodes(XMLUtil.getNode(catalogNode,"schema"));
				XMLUtil.putNode(catalogNode,"/Catalog/schema",ele);
				
				log.info(XMLUtil.convertToString(doc.getFirstChild()));//catalogNode));
				//Node abcdef=rootDocument.replaceChild(node,mynode1);
				log.info("*********Appending Over********");
				
				log.info(XMLUtil.convertToString(catalogNode));//catalogNode));
			log.info("*********CatalogNode Over********");
				
			} catch (DOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			log.info("After replacing the child");

		} else {

			Node newSchemaRootElement = rootDocument
			.createElement("Product");
			schemaNode.appendChild(newSchemaRootElement);
			SchemaTree tree = new SchemaTree(newSchemaRootElement);
		
			String[] result = headerLine.split(",");
			log.info(" Length  : " + result.length);
			//tree.addElement("Product",schemaStartName,null);
			//tree.addElement("Product","schema",null);
			
			
			order=1;
			SchemaTree.count =0;
			for (int i = 0; i < result.length; i++) {
				SchemaTree.count=i+1;
				log.info(" Try to create element name : " + result[i]
						+ " Num :" + i);
				 tree.addElement(result[i].trim(),"/Product", null);
				//XMLUtil.putValue(node,"@displayOrder",Integer.toString(i+1));
				//tree.addElement(result[i].trim(),"schema/Product","","true","false","string",Integer.toString(i+1));
			}
			SchemaTree.count =0;
			Element ele =(Element)  tree.getNode();	
			myfunction(ele);
			log.info("*******************This is csv ************************* " +tree.getNode().getNodeName());
			
			
			System.out.println(" string :"
					+ XMLUtil.convertToString(tree.getNode()));
		
		}

	
	

		

	}

	public static Element myfunction(Element ele){
		
		Document doc =ele.getOwnerDocument();
		
		Attr  id=doc.createAttribute("id");
		Attr  values = doc.createAttribute("values");
		Attr  allowAllias= doc.createAttribute("allowAllias");
		Attr  mandatory = doc.createAttribute("mandatory");
		Attr  displayOrder = doc.createAttribute("displayOrder");
		Attr  dataType = doc.createAttribute("dataType");
		if(!ele.getNodeName().equals("Product")&& !ele.getParentNode().getNodeName().equals("schema")){
		String attrvalue="";
		
		attrvalue = XMLUtil.getValue(ele,"@id");
		if(attrvalue == null)attrvalue="";
		ele.setAttributeNode(id);
		ele.setAttribute("id",attrvalue);
		
		attrvalue = XMLUtil.getValue(ele,"allowAllias");
		if(attrvalue == null)attrvalue="false";
		ele.setAttributeNode(allowAllias);
		ele.setAttribute("allowAllias",attrvalue);
		
		attrvalue = XMLUtil.getValue(ele,"mandatory");
		if(attrvalue == null)attrvalue="true";
		ele.setAttributeNode(mandatory);
		ele.setAttribute("mandatory",attrvalue);
		
		
		attrvalue = XMLUtil.getValue(ele,"displayOrder");
		if(attrvalue == null)attrvalue="";
		ele.setAttributeNode(displayOrder);
		ele.setAttribute("displayOrder",attrvalue);
		
		attrvalue = XMLUtil.getValue(ele,"dataType");
		if(attrvalue == null)attrvalue="string";
		ele.setAttributeNode(dataType);
		ele.setAttribute("dataType",attrvalue);
		
		attrvalue = XMLUtil.getValue(ele,"values");
		if(attrvalue == null)attrvalue="";
		ele.setAttributeNode(values);
		ele.setAttribute("values",attrvalue);
		
		
	//	ele.setAttributeNode(displayOrder);
	//	ele.setAttributeNode(dataType);
	//	ele.setAttributeNode(values);
		
		Node  node1 =  ele.getParentNode();
		if(node1 != null){
		//	String temp="";
			if(!node1.getNodeName().equals("Product")){
				String dorder= XMLUtil.getValue(node1,"@displayOrder");
				//temp = XMLUtil.getValue(node1,"@displayOrder");
				if(dorder == null){ dorder =""; }
				ele.setAttribute("displayOrder",dorder);
				System.out.println(dorder);
			}else{
				String temp=""+order++;
				ele.setAttribute("displayOrder",temp);
			}
		}
		}
		if(ele.hasChildNodes()){
			NodeList list = ele.getChildNodes();
			int size =list.getLength();
			for(int i=0;i<size;i++){
				Element node=(Element) list.item(i);
				myfunction( node);
			}
		}
		
		return ele;
	}
	
	
	
	
	
	private String getHeaderLineFromFile(CatalogUploadForm theForm)
			throws Exception {
		StringBuffer firstLine = new StringBuffer();
		InputStream stream = null;
		
		if (theForm != null) {
			FormFile formFile = theForm.getCatalogAddFile();
			if (formFile != null) {
				stream = formFile.getInputStream();
				//Creating Dom Struture
				//converting , separated line if the document contians chaild emements it will add -

				
				if (formFile.getFileName().endsWith(".xml")) {
					//Parsing the uploaded document		
					Node nodes = XMLUtil.parse(stream);
					//getting child nodes
					NodeList list = nodes.getChildNodes();
					int size = list.getLength();
					//finding the element named as schema and converting that as node and treating it 
					//as header line
					for (int i = 0; i < size; i++) {

						if (list.item(i).getNodeName().equals("schema"))
							firstLine.append(XMLUtil.convertToString(
									list.item(i)).trim());

					}

				} else {
					int k;
					while ((k = stream.read()) != '\n') {
						firstLine.append((char) k);
					}
				}
			}
		
		}
		stream.close();
	log.info("First Line from Uploaded file : "
				+ firstLine.toString());
		return firstLine.toString();
	}
}
