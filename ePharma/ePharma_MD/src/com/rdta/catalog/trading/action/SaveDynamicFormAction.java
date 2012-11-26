/*
 * Created on Oct 10, 2005
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
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
public class SaveDynamicFormAction extends Action{
	
	private static Log log = LogFactory.getLog(SaveDynamicFormAction.class);		
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	//StringBuffer buffer = null;
	StringBuffer buffer1 = null;
	StringBuffer buffer2 = null;
	StringBuffer buffer3 = null;
	StringBuffer buffer4 = null; 
	StringBuffer buffer5 = null;
	StringBuffer buffer6 = null;
	StringBuffer buffer7 = null;
	StringBuffer buffer8 = null;
	
	HashMap hashRequestData;
	int imageCount = 0;
	ArrayList imageList = null;
	HashMap replace = null;
	
	String clientIP = null;
	Connection conn; 
	Statement stmt;
	
	public StringBuffer getMyString(Node myNode) throws IOException{
		StringBuffer myRetString=new StringBuffer();
		StringBuffer strAttributes = new StringBuffer();
		String name = myNode.getNodeName();
		log.info("Get the string for "+name);
		
		NamedNodeMap attributes = myNode.getAttributes();
		
		Node dataType = attributes.getNamedItem("dataType");
		String datatype = dataType.getNodeValue();
		log.info("datatype inside SaveDynamicFormAction is :"+datatype);
		String userTypedVal = null;
		if(datatype.equalsIgnoreCase("image")){
			
			FileItem item = (FileItem)hashRequestData.get("save~"+name);
			
			if(item == null){
				userTypedVal = "";
    		}else{
    			
    			if(item.isFormField()){
					log.info("Image data not received properly for "+name);		
					userTypedVal = "";
				}else{
					if(imageList == null){
						imageList = new ArrayList();
					}	
										
					//Get the Image path from here.
					InputStream ins = item.getInputStream(); 
					log.info("InputStream ins "+ins.available());
					if(ins.available() > 0){
						
						String path = getRequestStringParameter("imagePath"+name);
						
						if(path != null && path.length() > 0 ){
							try{
							String type = path.substring( path.lastIndexOf(".")+1 );
							if( type.length() > 0 ){
								strAttributes.append(" type='").append(type).append("'");
							}
								
							}catch(StringIndexOutOfBoundsException e){
								log.info("Exception while extracting image type"+e);
							}
						}
						
						imageList.add(ins); 
						
						imageCount++;
						userTypedVal = "{binary{$"+imageCount+"}}";
						log.info("Image inputstream added......."+imageCount);	
						
					}
								
				}    			
    		}				
		}
		else{
			userTypedVal = getRequestStringParameter("save~"+name);
			if( userTypedVal == null  ){
				log.info("Null value for " + name);
				userTypedVal = "";
			}
		}
		StringBuffer myChildStr=null;
		if( myNode.hasChildNodes()  ){
			myChildStr = new StringBuffer();
			NodeList children = myNode.getChildNodes();
			for(int i=0;i<children.getLength();i++){
				myChildStr.append( getMyString(children.item(i)));
			}
		}
		
		myRetString.append("<").append(name).append(strAttributes).append(">").append(userTypedVal).append( myChildStr == null ? "" : myChildStr.toString()  );
		myRetString.append("</").append(name).append(">");
		log.info("Returning "+myRetString+" for "+name);
		return myRetString;
	}
	
	private void initializeUpload(HttpServletRequest req) throws FileUploadException{
		imageList = null;
		DiskFileUpload upload = new DiskFileUpload();
		List items = upload.parseRequest(req);
		hashRequestData = new HashMap();
		replace = null;
		imageCount = 0;
		
		Iterator it=items.iterator();
		while(it.hasNext()){
			FileItem item=(FileItem)it.next();
			String name=item.getFieldName();			
			log.info("initializeUpload Request "+name+": "+item.getString());
			hashRequestData.put(name, item);
		}
		
		imageCount=0;
				
	}
	
	private String getRequestStringParameter(String pname){
		
		FileItem item = (FileItem)hashRequestData.get(pname);
		if(item == null){return null;}
		
		return item.getString();
		
	}
	
	private boolean isImage(String name) {
		FileItem item = (FileItem)hashRequestData.get(name);
		if(item == null){
			return false;			
		}
		
		if(item.isFormField()){
			return false;
		}else return true;
	}
	
	private String replaceCDATA(String str){
		log.info("Inside method replaceCDATA..............");
		if(replace == null || replace.isEmpty()){
			log.info("Inside method replaceCDATA ..... Not clipped..............");
			return str;
		}else{
			
			StringBuffer sb = new StringBuffer(str);
			Set set = replace.keySet();
			
			Iterator it = set.iterator();
			while(it.hasNext()){
				
				String name =(String)it.next();
				String value = (String)replace.get(name);
				
				String clip = "<![CDATA["+value+"]]>";
				log.info("Searching for......."+clip);
				int pos = sb.indexOf(clip);
				log.info("pos is "+pos);
				
				if(pos >= 0){
					log.info("Inside clipping of CDATA.......");
					sb.replace(pos,pos+clip.length(), value);
				}
				
			}
			return sb.toString();
		}
	}
	
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
			log.info("Inside SaveDynamicFormAction...........");	
			
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
			
			String edited = "false";
		
			initializeUpload(request);
		
			String catalogName = getRequestStringParameter("catalogName");
			String genId = getRequestStringParameter("genId");
			log.info("catalog name in SaveDynamicFormAction is :"+catalogName);
			request.setAttribute("catalogName", catalogName);
			String prodName = getRequestStringParameter("prodName");
			System.out.println("*****************prodName is :"+prodName);
			String catalogId = getRequestStringParameter("catalogId");
			String pagenm = getRequestStringParameter("pagenm");
			
			request.setAttribute("pagenm", pagenm);
			
			String tp_company_nm = getRequestStringParameter("tp_company_nm");
			String pagename = getRequestStringParameter("pagename");
			System.out.println("The pagename inside SaveDynamicFormAction is :"+pagename);
			request.setAttribute("pagename", pagename);
		
			buffer7 = new StringBuffer();
			buffer7.append(" tlsp:validateAccess('"+sessionID+"', '5.02', 'Insert')");
			String accessLevel = queryRunner.returnExecuteQueryStringsAsString(buffer7.toString());

			log.info("The access privilege to create new product is :"+accessLevel);
			
			request.setAttribute("createProduct", "+accessLevel+");
			
			/*if(accessLevel.equalsIgnoreCase("false")){
				return mapping.findForward("denied");				
			}*/			
			
			edited = getRequestStringParameter("edited");
		
			if(edited == null || edited.length() == 0){
				edited = "false";			
			}
		
			buffer1 = new StringBuffer();
			buffer1.append("tlsp:GetMasterCatalogStructure('"+catalogName+"')");
			log.info("The query tlsp:GetMasterCatalogStructure() is :"+buffer1.toString());
			List structure = queryRunner.executeQuery(buffer1.toString());	
		
			log.info("After executing the query in SaveDynamicFormAction to get GetMasterCatalogStructure........");
			log.info("The result in the call to GetMasterCatalogStructure contains :"+structure);
			
			buffer3 = new StringBuffer();
			buffer3.append("tlsp:GetCatalogID('"+catalogName+"')");
			String catalogID = queryRunner.returnExecuteQueryStringsAsString(buffer3.toString());
			log.info("The catalogID in SaveDynamicFormAction is :"+catalogID);
		
			buffer4 = new StringBuffer();
			buffer4.append("tlsp:CheckGenID('"+genId+"')");
			log.info("Before calling the stored procedure tlsp:CheckGenID()........");
			List bool = queryRunner.executeQuery(buffer4.toString());
			log.info("After calling the stored procedure tlsp:CheckGenID(), the result is :"+bool);
		
			boolean bFirstTime=false;
		
			if(bool.size() == 0) {
				bFirstTime = true;
				StringBuffer result = new StringBuffer();
				result.append("<Product>");
				result.append("<genId>").append(genId).append("</genId>").append(
				"<refKey>").append("<catalogID>").append(catalogID).append(
				"</catalogID>").append("</refKey>");
			
				for (int i = 0; i < structure.size(); i++) {
					
					log.info("Inside the for loop after getting the result from the query GetMasterCatalogStructure()........");
					Node listNode = XMLUtil.parse((InputStream) structure.get(i));
					
					List list = XMLUtil.getNodes(listNode);
					Node lastNode = null;
				
					for (int j=1; j<list.size(); j++) {
						Node currentNode = (Node) list.get(j);
						if (lastNode != null && currentNode.getParentNode() == lastNode) {
							lastNode = currentNode;
							continue;
						}
						log.info(" Calling for " + currentNode.getNodeName());
						
						result.append(getMyString(currentNode));
						lastNode = currentNode;
					}				
				}
			
				result.append("</Product>");
				
				log.info("Result is :" + result);
				
				buffer2 = new StringBuffer();
				
				if(imageCount > 0){				
					buffer2.append("declare binary-encoding none;");
					log.info("ImageCount is "+imageCount);
				}
			
				buffer2.append("tig:insert-document('tig:///CatalogManager/ProductMaster',");
				buffer2.append(result + ")");
				log.info("Before saving the XML in the ProductMaster collection......"+buffer2);
			
				if(imageCount > 0){
					queryRunner.executeQueryWithStream(buffer2.toString(), (InputStream)imageList.get(0));
				}else{
					String success = queryRunner.returnExecuteQueryStringsAsString(buffer2.toString());
				}
				log.info("After saving the XML in the ProductMaster collection......");
			
			} else { // To update an existing document on entering the optional elements or while editing from Product Search
			
				log.info("Inside the else part in SaveDynamicFormAction........");			
						
				for(int k=0; k<bool.size(); k++){
				
					log.info("Inside the for in the else part........");
					Node listNode1 = XMLUtil.parse((InputStream)bool.get(k));
				
					List list1 = XMLUtil.getNodes(listNode1);
				
					for(int l=1; l<list1.size(); l++){
					
						Node currNode = (Node) list1.get(l);
						String name = currNode.getNodeName();
					
						log.info("Current Node inside else part is :"+name);
					
						if(isImage("save~"+name)){
						
							FileItem item = (FileItem)hashRequestData.get("save~"+name);
							if(item != null){
							
								String type = "";							
								InputStream ins = item.getInputStream(); 
								log.info("InputStream ins while editing "+ins.available());
								if(ins.available() > 0){
									String path = getRequestStringParameter("imagePath"+name);
								
									if(path != null && path.length() > 0 ){
										try{
											type = path.substring( path.lastIndexOf(".")+1 );
										}catch(StringIndexOutOfBoundsException e){
											log.info("Exception while extracting image type"+e);
										}
									}
									if( imageList == null ){imageList = new ArrayList();}
									if(replace == null){replace = new HashMap();}
									imageList.add(ins);
									log.info("InputStream is added....");
									imageCount++;
								
									NamedNodeMap attrs = currNode.getAttributes();
									Node attrib = attrs.getNamedItem("type");
									attrib.setNodeValue(type);								
								
									XMLUtil.putValue(currNode, "binary{{"+imageCount+"");
									replace.put(name, "binary{{"+imageCount+"");
									
									
								}							
							}
						
						}else{
							log.info(name+" not an image ");
							String value = getRequestStringParameter("save~"+name);
							if(value == null) 
								continue;
							log.info("CurrNode is :"+name+" and value is :"+value);
							XMLUtil.putValue(currNode, value);	
						}					
					
					}				
					/*		
					String converted = getPrologRemoved( XMLUtil.convertToString(listNode1) );
					log.info("The structure is :"+converted);
						
					if( imageCount <= 0){
						
						buffer5 = new StringBuffer();
					
						buffer5.append("declare binary-encoding none;tlsp:UpdateCatalog('"+genId+"',"+converted+")");
						log.info("Update Query "+buffer5);
						queryRunner.returnExecuteQueryStringsAsString(buffer5.toString()); 
					}else{
																						
						String result = XMLUtil.convertToString(listNode1, true);
						log.info("Before replacing "+result);
						result = replaceCDATA(result); 
						StringBuffer bufr = new StringBuffer();
						log.info("After replacing "+result);
					
						bufr.append("let $documentId := (for $i in collection('tig:///CatalogManager/ProductMaster') where ");
						bufr.append("$i/Product/genId = '"+genId+"' ");
						bufr.append("return document-uri($i)) ");
						bufr.append("return $documentId");
					
						log.info("Query to get Document-Uri in the else part is :"+bufr.toString());
					
						String docId = queryRunner.returnExecuteQueryStringsAsString(bufr.toString());
					
						StringBuffer buffer2 = new StringBuffer();
						buffer2.append("declare binary-encoding none; tig:replace-document('"+docId+"',");
						buffer2.append(result + ")");
					
						log.info("new Query is ***************"+buffer2.toString());
						queryRunner.executeQueryWithStream(buffer2.toString(), (InputStream) imageList.get(0) );
						
						
					}	
					*/
						
						
						
						
						replaceImageNodes(listNode1,catalogID);
						String result = XMLUtil.convertToString(listNode1, true);
						result  = replaceCDATA(result);
						log.info("After replacing binary data1: "+result);
						buffer8 = new StringBuffer();
						buffer8.append("tlsp:UpdateNonImageProducts("+result+",'"+genId+"')");
						log.info("Stored procedure calling......"+buffer8.toString());
						
						
						String num = queryRunner.returnExecuteQueryStringsAsString(buffer8.toString());
						log.info("The result is :"+num);
						
						
						
						if( imageCount > 0){
														
							String[] imageNodes = getImageNodeNames(catalogID); // Get all the image names 
							for(int i=0; i<imageNodes.length;i++){ // For each image mame
								log.info("UPdateImage : Searching for "+imageNodes[i]);
								Node imgNode = getNodeWithName(listNode1,imageNodes[i]); // Get the image node
								if( imgNode == null  ) { log.info("UpdateImage: null found");  continue;}
								String imgNodeVal = XMLUtil.getValue(imgNode); // Get the value of that node
								log.info("Value is "+imgNodeVal);
								if( imgNodeVal != null && imgNodeVal.indexOf("binary{") >=0  ){ // If the value contains binary{, then we need to update it
									buffer8 = new StringBuffer();
									buffer8.append("declare binary-encoding none;");
									buffer8.append("replace value of collection('tig:///CatalogManager/ProductMaster')/Product[genId='").append(genId).append("']//").append(imageNodes[i]);
									buffer8.append(" with binary{$1}");
									log.info("Query to update image:"+buffer8);
									//Error can occur if multiple images are there , check 4 the index i in imageList
									queryRunner.executeQueryWithStream(buffer8.toString(), (InputStream) imageList.get(i) );
									log.info("Image Updated...............");
								}
							}
																					
							
						}
						
					
				}				
					
					log.info("After updating the XML document in ProductMaster........");
				
				}			
		
		
			if( imageList !=null){
				for(int i=0;i<imageList.size();i++){
					InputStream ins = (InputStream) imageList.get(i);
					if(ins!=null){
						ins.close();
					}
				}
			}
		
			if( bFirstTime ){
				//Redirect to the optional page
				//RequestDispatcher view = request.getRequestDispatcher("AddMasterCatalogDetails.do?pagenm=optional&catalogName="+catalogName);
				pagename = "optional";
				
				//view.forward(request, response);
				request.setAttribute("genId", genId);
				request.setAttribute("pagename", pagename);
				request.setAttribute("catalogName", catalogName);
				request.setAttribute("pagenm", pagenm);
				log.info("Before forwarding to optional .......");
				log.info("Checking to see if optional elements are there in the catalog or not.....");
				
				buffer6 = new StringBuffer();
				buffer6.append("tlsp:GetOptionalElements('"+catalogName+"')/child::*");	
				List res = queryRunner.executeQuery(buffer6.toString());
				log.info("The size of res is :"+res.size());
				
				if(res.size() == 0){
					log.info("No optional elements found in the catalog.....");
					log.info("Forwarding to success.jsp......");
					return mapping.findForward("success");
				}else					
					return mapping.findForward("optional");
			}else{
				log.info("Value of edited in SaveDynamicFormAction is "+edited);
				if(edited.equals("true")){				
					request.setAttribute("genId", genId);
					log.info("genId before sending to Edited.jsp is :"+genId);
					request.setAttribute("catalogName", catalogName);
					log.info("catalogName before sending to Edited.jsp is :"+catalogName);
					request.setAttribute("prodName", prodName);
					log.info("prodName before sending to Edited.jsp is :"+prodName);
					request.setAttribute("catalogId", catalogId);
					log.info("catalogId before sending to Edited.jsp is :"+catalogId);
					request.setAttribute("pagenm", pagenm);
					log.info("pagenm before sending to Edited.jsp is :"+pagenm);
					request.setAttribute("tp_company_nm", tp_company_nm);
					log.info("tp_company_nm before sending to Edited.jsp is :"+tp_company_nm);
					
					return mapping.findForward("edited");
				}else{
					request.setAttribute("pagenm", pagenm);
					request.setAttribute("tp_company_nm", tp_company_nm);
					return mapping.findForward("success");
				}	
			}
			}catch(Exception e){			
				e.printStackTrace();
				log.error("An error occured in the SaveDynamicFormAction Action Class......."+e);
				return mapping.findForward("exception");
			}
		}
	
		String getPrologRemoved(String xml){
			int pos = xml.indexOf("<?xml version");
			int last = xml.indexOf("?>");
			
			if(pos < 0 || last < 0) {
				log.info("Prolog node not found.....");
				return xml;
			}else {
				log.info("Prolog found....");
				return xml.substring(last+3);
			}
		}
		
		private Node getNodeWithName(Node node,String name){
			if( node.getNodeName().equalsIgnoreCase(name)){
				return node;
			}
			NodeList list = node.getChildNodes();
			if( list == null || list.getLength() == 0 ){
				return null;
			}
			Node retNode = null;
			for(int i=0;i<list.getLength();i++){
				retNode = getNodeWithName(list.item(i), name);
				if(retNode != null) break;
			}
			return retNode;
		}
		
		private String[] getImageNodeNames(String catalogId){
			String [] results = new String[]{"Drug"};
			String query = "tlsp:GetAllImageNamesFromCatalog('" + catalogId + "',',')";
			try{
			log.info("Query to retrieve imageNames "+ query);
			String all_names = queryRunner.returnExecuteQueryStringsAsString(query);
			Vector vect = new Vector();
			StringTokenizer st = new StringTokenizer(all_names,",");
			
			while( st.hasMoreTokens() ){
				vect.add(st.nextToken());
			}
			
			results = new String[ vect.size() ];
			for(int i=0;i<results.length;i++){
				results[i] = (String) vect.get(i);
			}
			log.info("results length "+results.length);
			}catch(Exception e){
				log.info("Exception while retrieving image Nodes");
				e.printStackTrace();
			}
			return results; // Change it later
		}
		
		private boolean replaceImageNodes(Node node,String catalogId){
			String [] imageNodeNames = getImageNodeNames(catalogId); 
			boolean atLeastOneReplaced = false;
			for(int i=0;i<imageNodeNames.length;i++){
				Node imgNode = getNodeWithName(node,imageNodeNames[i]);
				if( imgNode != null){
					String val = XMLUtil.getValue(imgNode);
					if( val == null || val.indexOf("binary{") < 0 ){	
						XMLUtil.putValue(imgNode,"PreserveBinaryData");
						atLeastOneReplaced = true;
						log.info("Replaced");
					}
				}
			}
			return atLeastOneReplaced;
		}
		
}
