/*
 * Created on Oct 21, 2005
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

 

package com.rdta.catalog.trading.action;

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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.rdta.Admin.Utility.Helper;
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

/**
 * @author Santosh Subramanya
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ShowMasterProductInfo extends Action {
	
	private static Log log = LogFactory.getLog(ShowMasterProductInfo.class);		
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	StringBuffer buffer = null;
	StringBuffer buffer1 = null;
	StringBuffer buffer2 = null;
	StringBuffer buffer3 = null;
	StringBuffer buffer4 = null;
	StringBuffer buff = null;
	StringBuffer read = null;	
	
	String[] nodeNames = null;
	String[] nodeValues = null;
	String[] mandoroptnal = null;
	String[] dataTypes = null; 
	
	String clientIP = null;
	Connection conn; 
	Statement stmt;
	
	public void TLClose() {
        try {
        	log.info("Closing the TigerLogic Connection in SaveDynamicFormAction..........");
        	stmt.close();
        	conn.logoff();
        	conn.close();
        	log.info("Connection Closed !!!!!!!!!!!!");
        }catch(com.rdta.tlapi.xql.XQLConnectionException e){
        		System.err.println(e);
        }catch(com.rdta.tlapi.xql.XQLException e){
        		System.err.println(e);  
        }
    }	
		
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		try{
			
		//	System.out.println("*****************************************comming");
			log.info("Inside ShowMasterProductInfo...........");
			
			HttpSession sess = request.getSession();
			Helper helper = new Helper();
			clientIP = request.getRemoteAddr(); 

			conn = helper.ConnectTL(); 
			stmt = helper.getStatement(conn);
			log.info("Validating The Session");

			// Validating Session
			String sessionID = (String)sess.getAttribute("sessionID");
			log.info("sessionID in Action is :"+sessionID);
			String validateResult = helper.ValidateUserSession(stmt, sessionID, clientIP);        

			if ( !validateResult.equals("VALID")){
			    //return a forward to login page.
			    return mapping.findForward("loginPage");
			}
			
			TLClose();
			
			read = new StringBuffer();
			read.append("tlsp:validateAccess('"+sessionID+"', '5.01', 'Read')");
			String readAccess = queryRunner.returnExecuteQueryStringsAsString(read.toString()); 
			log.info("The read access in ShowMasterProductInfo is: "+readAccess);
			
			if(readAccess.equals("false")){				
				log.info("The user does not have READ Permission......");
				return mapping.findForward("NoReadAccess");
				
			}			
			
		
			String genId = request.getParameter("genId");
			if(genId == null){
				genId = (String)request.getAttribute("genId");
			}
					
			String catalogName = request.getParameter("catalogName");
			if(catalogName == null){
				catalogName = (String)request.getAttribute("catalogName");
			}
			
			if(catalogName == null){				
				StringBuffer b = new StringBuffer();
				b.append("tlsp:GetCatalogName('"+genId+"')");
				log.info("Before query to stored procedure tlsp:GetCatalogName()..... ");
				catalogName = queryRunner.returnExecuteQueryStringsAsString(b.toString());				
				request.setAttribute("catalogName", catalogName);
			}
			
			String prodName = request.getParameter("prodName");
			if(prodName == null){
				prodName = (String)request.getAttribute("prodName");
			}
			log.info("The prodName in ShowMasterProductInfo is :"+prodName);
		
			/*String catalogId = request.getParameter("catalogId");
			if(catalogId == null){
				catalogId = (String)request.getAttribute("catalogId");
			}
			log.info("CatalogId in ShowMasterProductInfo is :"+catalogId);*/
						
			
		
			String pagenm = request.getParameter("pagenm");
			if(pagenm == null){
				pagenm = (String)request.getAttribute("pagenm");
			}
			log.info("pagenm in ShowMasterProductInfo is :"+pagenm);
			
			String tp_company_nm = request.getParameter("tp_company_nm");
			if(tp_company_nm == null){
				tp_company_nm = (String)request.getAttribute("tp_company_nm");
			}
			log.info("tp_company_nm in ShowMasterProductInfo is :"+tp_company_nm);
			
			//String sessionID = (String)session.getAttribute("sessionID");
			log.info("sessionID in ShowMasterProductInfo is :"+sessionID);
						
			buffer3 = new StringBuffer();
			buffer3.append(" tlsp:validateAccess('"+sessionID+"', '5.02', 'Insert')");
			String insertAccess = queryRunner.returnExecuteQueryStringsAsString(buffer3.toString());

			buffer4 = new StringBuffer();
			buffer4.append(" tlsp:validateAccess('"+sessionID+"', '5.02', 'Update')");
			String updateAccess = queryRunner.returnExecuteQueryStringsAsString(buffer4.toString());
			
			request.setAttribute("insertAccess", insertAccess);
			request.setAttribute("updateAccess", updateAccess);

			
			log.info("The insertAccess privilege to create new product is :"+insertAccess);
			log.info("The updateAccess privilege to create new product is :"+updateAccess);
						
			
			//changed from here on 15-11-2005 by Santosh
			
			buff = new StringBuffer();
			
			buff.append("for $i in collection('tig:///CatalogManager/ProductMaster')/Product where $i/genId = '"+genId+"' ");
			buff.append(" return data($i//catalogID)");
			
			String catalogId = queryRunner.returnExecuteQueryStringsAsString(buff.toString());			
			
			
			// till here
			
			
			buffer = new StringBuffer();
			/*
			buffer.append("<result>{ ");
			buffer.append("let $pmaster := collection('tig:///CatalogManager/ProductMaster')/Product[ProductName = '"+prodName+"']/descendant::*:*[position() gt 3] ");
			buffer.append("let $cm := collection('tig:///CatalogManager/Catalog')/Catalog[catalogID='"+catalogId+"']/schema/Product/descendant::*[@mandatory] ");
			buffer.append("for $c in $cm,$p in $pmaster ");
			buffer.append("where $c/name() eq $p/name() ");
			buffer.append("order by xs:integer($c/@displayOrder) ");
			buffer.append("return element { $c/name() } { ( attribute value{$p/text()} , attribute ismandatory{$c/@mandatory} , attribute datatype{$c/@dataType} ) } ");
			buffer.append("}</result>");
			*/
			
			buffer.append("tlsp:GetMasterProductInfo('"+catalogId+"','"+genId+"')");
			
			log.info("The query to get the Master Product Details from ShowMasterProductInfo is :"+buffer.toString());
			
			List masterProdDet = queryRunner.executeQuery(buffer.toString());
			log.info("The query contains object:"+masterProdDet);
			
			int length = masterProdDet.size();
			log.info("The size of length is :"+length);
		
			for (int i=0; i<length; i++){			
				
				log.info("Inside the for loop for i..........");
				
				Node listNode = XMLUtil.parse((InputStream)masterProdDet.get(i));
				log.info("The size of masterProdDet is :"+masterProdDet.size());
				log.info("Parent Node is "+ listNode);
				log.info("First Child Node count "+listNode.getChildNodes().getLength() + " val:" + listNode.getChildNodes().item(0).getNodeName());
			
				//List liss = XMLUtil.getNodes(listNode);
				NodeList liss = listNode.getChildNodes();
				log.info("My size is "+liss.getLength());
				nodeNames = new String[liss.getLength()];
				nodeValues = new String[liss.getLength()];
				mandoroptnal = new String[liss.getLength()];
				dataTypes = new String[liss.getLength()];
			
				int s=0;
				if(liss != null ){
					for(int j=0;j<liss.getLength();j++){	
											
						int index = j; 
						Node currNode = ((Node)liss.item(j));
						
					//	System.out.println("Node name"+currNode.getNodeName());
						nodeNames[index] = currNode.getNodeName();							
						log.info("Node Name "+j+ " is:"+currNode.getNodeName());							
						
						NamedNodeMap attributes = currNode.getAttributes();
						
						if( attributes == null){
							log.info("continuing as current attributes is null "+j);
							continue;
							
						}
						
						Node value = (Node)attributes.getNamedItem("value");
						String val = value.getNodeValue();
						log.info("Value of"+currNode.getNodeName()+" is :"+val);
						nodeValues[index] = val;
					
						Node ismandatory = (Node)attributes.getNamedItem("ismandatory");
						String ismand = ismandatory.getNodeValue();
						log.info("The ismandatory is :"+ismand);
						mandoroptnal[index] = ismand;
						
						Node datatype = (Node)attributes.getNamedItem("datatype");
						String dataType = datatype.getNodeValue();
						log.info("The dataType of "+currNode.getNodeName()+" is :"+dataType);
						dataTypes[index] = dataType;
					}
				}
			}
		
			request.setAttribute("nodeNames", nodeNames);
			request.setAttribute("nodeValues", nodeValues);
			request.setAttribute("mandoroptnal", mandoroptnal);
			request.setAttribute("dataTypes", dataTypes);
			request.setAttribute("catalogId", catalogId);
			request.setAttribute("pagenm", pagenm);
			request.setAttribute("dataTypes", dataTypes);
			request.setAttribute("prodName", prodName);
			request.setAttribute("tp_company_nm", tp_company_nm);
		
			}catch(Exception e){
				e.printStackTrace();
				log.error("An error occured in the ShowMasterProductInfo Action Class......."+e);				
				return mapping.findForward("exception");			
			}				
			return mapping.findForward("success");
		}
}
