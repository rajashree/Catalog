/*
 * Created on Oct 11, 2005
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

 
package com.rdta.catalog.gcpim.action;

import java.io.InputStream;
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
import org.apache.struts.upload.FormFile;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.rdta.Admin.Utility.Helper;
import com.rdta.catalog.Constants;
import com.rdta.catalog.SchemaTree;
import com.rdta.catalog.session.CatalogContext;
import com.rdta.catalog.trading.action.CatalogSchemaDefAction;
import com.rdta.catalog.trading.action.CatalogUploadForm;
import com.rdta.catalog.trading.model.Catalog;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

/**
 * @author Arun Kumar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EditMasterCatalog extends Action{

	private static Log log=LogFactory.getLog(EditMasterCatalog.class);
    public static String parentNode = new String();
	public Node node6;
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

    public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response)
								 throws Exception {
    	
		try{

			
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
			sess.removeAttribute("optype");
			sess.removeAttribute("savetree");
			sess.setAttribute("savetree","false");
			sess.setAttribute("optype","edit");
			System.out.println("operation type = edit");
			request.setAttribute("help","true");
			sess.setAttribute("mandatory","true");
    	List InsertList = queryrunner.returnExecuteQueryStrings("tlsp:validateAccess('"+sessionID+"','5.04','Insert')");
		String insertStatus=InsertList.get(0).toString();
		request.setAttribute("insertStatus",insertStatus);
		log.info("insertStatus"+insertStatus);
		
		List deleteList = queryrunner.returnExecuteQueryStrings("tlsp:validateAccess('"+sessionID+"','5.04','Delete')");
		String deleteStatus=deleteList.get(0).toString();
		request.setAttribute("deleteStatus",deleteStatus);
		
		log.info("deleteStatus"+deleteStatus);
		
		List updateList = queryrunner.returnExecuteQueryStrings("tlsp:validateAccess('"+sessionID+"','5.04','Update')");
		String updateStatus=updateList.get(0).toString();
		request.setAttribute("updateStatus",updateStatus);
		
		
		sess.setAttribute("deleteStatus",deleteStatus);
		sess.setAttribute("insertStatus",insertStatus);
		sess.setAttribute("updateStatus",updateStatus);
		
		
		
		log.info("updateStatus"+updateList);
	
    	
    	String catalogGenId = request.getParameter("catalogGenId");
		String fromModule = request.getParameter("fromModule");
		String operationType = request.getParameter("operationType");
		String schemaStartName="";
		log.info("*************************Deepak*******************");
		
		
		
		
		log.debug(" CatalogGenId  : " + catalogGenId + "  fromModule : " + fromModule );

		log.info(" CatalogGenId  : " + catalogGenId + "  fromModule : " + fromModule );
		log.info(" operationType  : " + operationType );
		
		CatalogContext catalogContext = new CatalogContext();
		
		Catalog catalog = Catalog.find(catalogGenId);
		
		sess.setAttribute("typeop","edit");
		if(catalog != null) {
			log.info("catlog is not null");
			Node catalogNode = catalog.getNode();
				 if (catalogNode == null){
				 	log.info(" I am null");
				 }
			//if schema creation done from the file upload
			
			log.info("**********Schmea***********");
			
			if(operationType.equals("edit")){
			
				schemaStartName = XMLUtil.getValue(catalogNode,"name");
				log.info(schemaStartName);
				Node schemaStartNode = XMLUtil.getNode(catalogNode, "schema/*");
			    if(schemaStartNode == null){
			    	schemaStartNode = XMLUtil.getNode(catalogNode,"schema");
			    	
			    	Document doc = catalogNode.getOwnerDocument();
			    	Attr id = doc.createAttribute("id");
			    	Attr values = doc.createAttribute("values");
			    	Element ele = doc.createElement("Product");
			    	id.setValue("");
			    	values.setValue("");
			    	ele.setAttributeNode(id);
			    	ele.setAttributeNode(values);
			    	XMLUtil.putNode(catalogNode,"schema",ele);			    	
			    	
			    }
				
				SchemaTree tree= new SchemaTree(schemaStartNode);
			    log.info(catalogGenId);
				if(sess!=null){
					sess.setAttribute(Constants.SESSION_CATALOG_CONTEXT,catalogContext);
					 sess.setAttribute("SESSION_CATALOGID",catalogGenId );
						
				}
			}
			
			log.info("*************SchemaEnd********");
				 
				 
				 
				 if(operationType != null && operationType.equalsIgnoreCase("createFromFile")) {
				
				CatalogUploadForm theForm =(CatalogUploadForm)form;
				//create schema from file
				String headerLine = getHeaderLineFromFile(theForm);
				 schemaStartName = XMLUtil.getValue(catalogNode, "name");
				
				if(headerLine != null && !headerLine.equals("") && schemaStartName != null) {
					
					createSchema(catalogNode,headerLine,schemaStartName);
						
					
				}
				
			}
			
			
			catalogContext.setCatalogNode(catalogGenId,catalogNode);
			
			
			if(sess != null) {
				/* if(fromModule != null && fromModule.equalsIgnoreCase("TP") ) {
					 TradingPartnerContext context = (TradingPartnerContext)session.getAttribute("TradingPartnerContext");
					 context.setCatalogContext(catalogContext);
				 } else {
					 session.setAttribute(Constants.SESSION_CATALOG_CONTEXT,catalogContext); 
				 }*/
				
				log.info(" Inside session setting ........");
				 sess.setAttribute(Constants.SESSION_CATALOG_CONTEXT,catalogContext);
				 
				 	//since we are using form type as multipart/form-data
					//incase of schema creation from upload process
				 	//we are losing control of request paramerts
					//so populate catalogId so that tree will populate later
				 log.info(" Inside session setting ........" + catalogGenId );
				 sess.setAttribute("SESSION_CATALOGID",catalogGenId );
					
			}
			
		}//not null catalog
		
		log.info(" Before returning...  :");		
		return mapping.findForward("success");
  
    }catch(Exception ex){
    	
    	return mapping.findForward("exception");
    }
    
    
    
    
    
    }
    
    
    
    
    
    
    
    Node global=null;
    public void createSchema(Node catalogNode,String headerLine,String schemaStartName) {
		log.info(" schemaStartName : " + schemaStartName);
		log.info("Comming Here");
		Node schemaNode = XMLUtil.getNode(catalogNode, "schema");
		Node schemaStartNode = XMLUtil.getNode(catalogNode, "schema/*");
		if(schemaNode!=null){
		log.info("schemaNode = "+schemaNode.getNodeName());
		}
	
		//remove already some information exist
		if(schemaStartNode != null) {
			schemaNode.removeChild(schemaStartNode);
			log.info("Camae Here");
		}
		//create from uploaded infroamtion
		Document rootDocument = catalogNode.getOwnerDocument();
		log.info("root Document Name"+rootDocument.getNodeName());
		//remove the spaces since XML doen't accept spaces
		String removeSpaceSchmeStartName = schemaStartName.replaceAll(" ","");
		Node newSchemaRootElement = rootDocument.createElement(removeSpaceSchmeStartName);
	    schemaNode.appendChild(newSchemaRootElement);
		SchemaTree tree= new SchemaTree(newSchemaRootElement);
	
		
		
		if(headerLine.indexOf("<")!=-1){
			    

			String xy = XMLUtil.convertToString(tree.getNode());
			log.info("xy contains"+xy);
			
			headerLine = headerLine.replaceAll("<schema>","<schema><"+schemaStartName+">");
			log.info("Header Line... "+headerLine);
			headerLine = headerLine.replaceAll("</schema>","</"+schemaStartName+">"+"</"+"schema>");
			headerLine = headerLine.replaceAll("<Product displayName=\"\" id=\"\" values=\"\">","");
			headerLine = headerLine.replaceAll("</Product>","");
			headerLine = headerLine.trim();
			log.info(headerLine);
			Node node = XMLUtil.parse(headerLine);
			log.info("node contenets are "+XMLUtil.convertToString(node));
			log.info("######################################################################\n"+node.getNodeName());
			
		//	catalogNode.replaceChild(schemaNode,node);
				
			NodeList list1 =catalogNode.getChildNodes();
			log.info("The no of child nodes"+list1.getLength());
			Node mynode1=null;
			int size = list1.getLength();
			for(int i=0;i<size;i++)
			{
				if(list1.item(i).getNodeName().equals("schema"))
				{
					mynode1=list1.item(i);
				}
			}
			
		try {
					catalogNode.removeChild(mynode1);
			XMLUtil.putNode(catalogNode,"/Catalog",node);
			log.info(XMLUtil.convertToString(catalogNode));
				//Node abcdef=rootDocument.replaceChild(node,mynode1);
			
			} catch (DOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			log.info("After replacing the child");
			
		}else{
			
			String[] result = headerLine.split(",");
			log.info(" Length  : " + result.length);
			for(int i = 0; i<result.length; i++) {
			log.info(" Try to create element name : " + result[i] + " Num :" + i);
			tree.addElement( result[i].trim(),schemaStartName, null);
			}
			
			
			
			
		}
		
			
			log.info(".......Elements after the xml");
		
		//	tree.addNode(node6);
		
			log.info(" string :" + XMLUtil.convertToString(tree.getNode()) );
		
	}
		
		
	
    
    
    
    
	private String  getHeaderLineFromFile(CatalogUploadForm  theForm) throws Exception {
		StringBuffer firstLine = new StringBuffer();
		InputStream stream = null;
		log.info("In the getHeaderLIneForm of EditMaster Catalog");
		if(theForm != null) {	
			FormFile formFile = theForm.getCatalogAddFile();
			if(formFile != null) {
				stream = formFile.getInputStream();
//Creating Dom Struture
//converting , separated line if the document contians chaild emements it will add -

				
				
				if (formFile.getFileName().endsWith(".xml"))
				{
			//Parsing the uploaded document		
					Node nodes = XMLUtil.parse(stream);
			//getting child nodes
					NodeList list =nodes.getChildNodes();
					int size = list.getLength();
			//finding the element named as schema and converting that as node and treating it 
			//as header line
					for(int i=0;i<size;i++){
			
						if(list.item(i).getNodeName().equals("schema"))
						firstLine.append(XMLUtil.convertToString(list.item(i)).trim());
							
					}
				
				}else{
					int k;
				while( (k = stream.read()) != '\n') {
					firstLine.append((char)k);
				}				
			}
			}
			
			log.info("after the getHeaderLIneForm ");
		}
		stream.close();
		log.info(" Uploaded file : " + firstLine.toString());
		return   firstLine.toString();
	}
	
	
	
	
}
