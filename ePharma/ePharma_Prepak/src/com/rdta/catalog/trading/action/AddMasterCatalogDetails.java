/*
 * Created on Oct 6, 2005
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.rdta.Admin.Utility.Helper;
import com.rdta.commons.GUID;
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.commons.GUID;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

/**
 * @author Santosh Subramanya
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AddMasterCatalogDetails extends Action {
	
	private static Log log = LogFactory.getLog(AddMasterCatalogDetails.class);		
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	String catalogName = null;
	String[] elements = null;
	String[] elements1 = null;
	StringBuffer buffer = null;
	
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
			
			log.info("Inside AddMasterCatalogDetails......");
			
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
							
			String genId = null;
			genId = request.getParameter("genId");
			if(genId == null){
				genId = (String)request.getAttribute("genId");
			}
			if( genId == null) {
				GUID guid = new GUID();		
				genId = guid.getGUID();				
			}	
			request.setAttribute("genId", genId);
		
			log.info("Inside Action Class AddMasterCatalogDetails..........");
			catalogName = request.getParameter("catalogName");	
			if(catalogName == null){
				catalogName = (String)request.getAttribute("catalogName");
				log.info("catalogName from attribute is ..... "+catalogName);			
			}
			request.setAttribute("catalogName", catalogName);
			StringBuffer buff = new StringBuffer();
			StringBuffer buff1 = new StringBuffer();
			String pagenm = request.getParameter("pagenm");			
		
			if(pagenm == null){
				pagenm = (String)request.getAttribute("pagenm");
			}
			
			String pagename = request.getParameter("pagename");
			if(pagename == null){
				pagename = (String)request.getAttribute("pagename");
			}			
		
			request.setAttribute("pagenm", pagenm);
			request.setAttribute("pagename", pagename);
			
			StringBuffer countMandatoryElm = new StringBuffer();
			countMandatoryElm.append("count(tlsp:GetMandatoryElements('"+catalogName+"')/child::*:*)");
			String count = queryRunner.returnExecuteQueryStringsAsString(countMandatoryElm.toString());
			log.info("The count of Mandatory elements inside the else part is :"+countMandatoryElm.toString()+ " count:"+count);
			request.setAttribute("CountMandElms", count);
			
			
			
			if(pagename.equals("mandatory")){
			
				log.info("Inside call to get Mandatory Master Catalog Elements......");
				buff.append("tlsp:GetMandatoryElements('"+catalogName+"')");			
			
				List result = queryRunner.executeQuery(buff.toString());
				log.info("After executing the query in AddMasterCatalogDetails......");
				log.info("The result after executing the query in AddMasterCatalogDetails is :"+result);	
			
			
				for(int i=0; i<result.size(); i++) {
					log.info("Inside the for loop........");
					Node listNode = XMLUtil.parse((InputStream)result.get(i));
					log.info("Parent Node is "+ listNode.toString() );
					//log.info("First Child Node count "+listNode.getChildNodes().getLength() + " val:" + listNode.getChildNodes().item(0).getNodeName());
					
					List liss = XMLUtil.getNodes(listNode);
					log.info("My size is "+liss.size());
					elements = new String[liss.size()-1];
				
					int s=0;
					if(liss != null ){
						for(int j=1;j<liss.size();j++){					
							elements[s++] = ((Node)liss.get(j)).getFirstChild().getNodeValue();							
							//log.info("Node Value is:"+j+((Node)liss.get(j)).getFirstChild().getNodeValue());
						}
					}
				}
			
				buff1.append("tlsp:GetDataTypes('"+catalogName+"', 'true')");
				
				List result1 = queryRunner.executeQuery(buff1.toString());
				log.info("After executing the query1 in AddMasterCatalogDetails......");
				log.info("The result after executing the query1 in AddMasterCatalogDetails is :"+result1);	
			
				for(int k=0; k<result1.size(); k++) {
					log.info("Inside the for loop........");
					Node listNode1 = XMLUtil.parse((InputStream)result1.get(k));
					List liss1 = XMLUtil.getNodes(listNode1);
					System.out.println("My size is "+liss1.size());
					elements1 = new String[liss1.size()-1];
					int s1=0;
					if(liss1 != null ){
						for(int l=1;l<liss1.size();l++){
							elements1[s1++] = ((Node)liss1.get(l)).getFirstChild().getNodeValue();					
							//log.info("Node Value is:"+l+((Node)liss1.get(l)).getFirstChild().getNodeValue());
						}
					}
				}
			}else {		
				
				StringBuffer CheckIfMandSaved = new StringBuffer();
				CheckIfMandSaved.append("tlsp:CheckIfMandatoryElementsSaved('"+genId+"')");
				String returnType = queryRunner.returnExecuteQueryStringsAsString(CheckIfMandSaved.toString());
				
				log.info("The Mandatory elements whether saved is :"+returnType);
				request.setAttribute("mandsavedornot", returnType);
				
								
				log.info("Inside call to get Optional Master Catalog Elements......");
				buff.append("tlsp:GetOptionalElements('"+catalogName+"')");		
				
				List result = queryRunner.executeQuery(buff.toString());
				log.info("After executing the query in AddMasterCatalogDetails to get the optional elements......");
				log.info("The result after executing the query in AddMasterCatalogDetails is :"+result);
				log.info("The size of the optional elements is :"+result.size());
			
				for(int i=0; i<result.size(); i++) {
					log.info("Inside the for loop........");
					Node listNode = XMLUtil.parse((InputStream)result.get(i));
					List liss = XMLUtil.getNodes(listNode);					
					elements = new String[liss.size()-1];
					int s=0;
					if(liss != null ){
						for(int j=1;j<liss.size();j++){
							elements[s++] = ((Node)liss.get(j)).getFirstChild().getNodeValue();					
							//log.info("Node Value is:"+j+((Node)liss.get(j)).getFirstChild().getNodeValue());
						}
					}
				}
		
				buff1.append("tlsp:GetDataTypes('"+catalogName+"', 'false')");
				
				List result1 = queryRunner.executeQuery(buff1.toString());
				log.info("After executing the query1 in AddMasterCatalogDetails......");
				log.info("The result after executing the query to get data types in AddMasterCatalogDetails is :"+result1);	
			
				for(int k=0; k<result1.size(); k++) {
					log.info("Inside the for loop........");
					Node listNode1 = XMLUtil.parse((InputStream)result1.get(k));
					List liss1 = XMLUtil.getNodes(listNode1);					
					elements1 = new String[liss1.size()-1];
					int s1=0;
					if(liss1 != null ){
						for(int l=1;l<liss1.size();l++){
							elements1[s1++] = ((Node)liss1.get(l)).getFirstChild().getNodeValue();					
							log.info("Node Value is:"+l+((Node)liss1.get(l)).getFirstChild().getNodeValue());
						}
					}
				}
			}
		}catch(Exception e){
			log.info("I am in the AddMasterCatalogDetails error........45877");
			e.printStackTrace();
			log.info("CheckError", e);
			
			log.error("An error occured in the AddMasterCatalogDetails Action Class......."+e);
			return mapping.findForward("exception");
		}
		request.setAttribute("parsed", elements);
		request.setAttribute("parsed1", elements1);
		log.info("Before returning to success from AddMasterCatalogDetails.......");	
		return mapping.findForward("success");
	}
	
}
