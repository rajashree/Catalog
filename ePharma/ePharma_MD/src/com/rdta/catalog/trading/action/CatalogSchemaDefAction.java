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

import com.rdta.catalog.session.TradingPartnerContext;
import com.rdta.catalog.trading.model.Catalog;


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
import com.rdta.commons.xml.XMLUtil;

import com.rdta.catalog.session.TradingPartnerContext;

import com.rdta.catalog.session.CatalogContext;
import com.rdta.catalog.JavaScriptTree;

/**
 * Catalog information collecting from the reques form.
 * 
 * 
 */
public class CatalogSchemaDefAction extends Action
{
    private static Log log=LogFactory.getLog(CatalogSchemaDefAction.class);
    public static String parentNode = new String();
	public Node node6;
	
    public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response)
								 throws Exception {
 	
    	log.info("Inside Action CatalogSchemaDefAction....... ");
		log.info("Inside Action execute of CatalogSchemaDefAction....");
	    try{
		String catalogGenId = request.getParameter("catalogGenId");
		String fromModule = request.getParameter("fromModule");
		String operationType = request.getParameter("operationType");
		
		
		
		
		
		log.debug(" CatalogGenId  : " + catalogGenId + "  fromModule : " + fromModule );

		System.out.println(" CatalogGenId  : " + catalogGenId + "  fromModule : " + fromModule );
		System.out.println(" operationType  : " + operationType );
		
		CatalogContext catalogContext = new CatalogContext();
		
		Catalog catalog = Catalog.find(catalogGenId);
		
		if(catalog != null) {
			
			Node catalogNode = catalog.getNode();
				 
			//if schema creation done from the file upload
			if(operationType != null && operationType.equalsIgnoreCase("createFromFile")) {
				
				CatalogUploadForm theForm =(CatalogUploadForm)form;
				//create schema from file
				String headerLine = getHeaderLineFromFile(theForm);
				String schemaStartName = XMLUtil.getValue(catalogNode, "catalogName");
				
				if(headerLine != null && !headerLine.equals("") && schemaStartName != null) {
					
					createSchema(catalogNode,headerLine,schemaStartName);
				}
				
			}
			
			
			catalogContext.setCatalogNode(catalogGenId,catalogNode);
			
			HttpSession session = request.getSession(false);
			if(session != null) {
			
				
				
				 session.setAttribute(Constants.SESSION_CATALOG_CONTEXT,catalogContext);
				 
				 	//since we are using form type as multipart/form-data
					//incase of schema creation from upload process
				 	//we are losing control of request paramerts
					//so populate catalogId so that tree will populate later
				 log.info(" Inside session setting ........" + catalogGenId );
				 session.setAttribute("SESSION_CATALOGID",catalogGenId );
					
			}
			
		}//not null catalog
		
		log.info(" Before returning...  :");	
	    }catch(Exception ex)
		{
	    	ex.printStackTrace();
			log.error("Error in CatalogSchemaDefAction execute()" + ex);
			return mapping.findForward("exception");
	
		}
		return mapping.findForward("success");
   }
    Node global=null;
    public void createSchema(Node catalogNode,String headerLine,String schemaStartName) {
		log.info(" schemaStartName : " + schemaStartName);
		 
		Node schemaNode = XMLUtil.getNode(catalogNode, "schema");
		Node schemaStartNode = XMLUtil.getNode(catalogNode, "schema/*");
		if(schemaNode!=null){
		log.info("schemaNode = "+schemaNode.getNodeName());
		}
	
		//remove already some information exist
		if(schemaStartNode != null) {
			schemaNode.removeChild(schemaStartNode);
		 
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
		 
			Node node = XMLUtil.parse(headerLine);
			log.info("node contenets are "+XMLUtil.convertToString(node));
			 
			log.info("NodeName ="+node.getNodeName());
	 	 
				
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
			 
			 
		}
		stream.close();
		log.info("First Line from Uploaded file : " + firstLine.toString());
		return   firstLine.toString();
	}
}
