/*
 * Created on Oct 28, 2005
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

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.Admin.Utility.Helper;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;




/**
 * @author Santosh Subramanya
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GetImage extends HttpServlet {
	
	private static Log log = LogFactory.getLog(ShowMasterProductInfo.class);		
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	
	private boolean isMandatoryParam(String param){
		
		log.info("Inside isMandatoryParam.... in GetImage servlet.........");
		String dontCheck[] = {"collname", "dbname", "imagetype", "nodename", "topnode" };
		for(int i=0; i<dontCheck.length; i++){
			
			if(param.equals(dontCheck[i])){
				
				return true;
			}
		}
		return false;
	}
	
	private String getNodeConditions(HttpServletRequest request){
		
		log.info("Inside getNodeConditions.... in GetImage servlet.........");
		StringBuffer query = new StringBuffer();
		Enumeration enumParams = request.getParameterNames();
		while(enumParams.hasMoreElements()){
			String paramName = (String)enumParams.nextElement();
			if(!isMandatoryParam(paramName)){
				String value = request.getParameter(paramName); // Get the parameter
				if(query.length() > 0) {
					query.append(" and ");
				}
				if(value == null || value.length() == 0){
					
					value = "''";
				}
				query.append(paramName).append("='").append(value).append("'");
			}
		}
		return query.toString();
		
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		String collName = request.getParameter("collname");
		if(collName == null || collName.length() == 0){
			
			collName = "ProductMaster";
		}
		
		String dbName = request.getParameter("dbname");
		if(dbName == null || dbName.length() == 0){
			
			dbName = "CatalogManager";
		}
		
		String imageType = request.getParameter("imagetype");
		if(imageType == null || imageType.length() == 0){
			imageType = "";
		}
		
		String nodeName = request.getParameter("nodename");
		if(nodeName == null || nodeName.length() == 0){
			nodeName = "";
		}	
		
		String topNode = request.getParameter("topnode");
		if(topNode == null || topNode.length() == 0){
			topNode = "Product"; // customized to ProductMaster collection
		}
		
		Helper helper =new Helper();
		Connection Conn = helper.ConnectTL();
		Statement Stmt = helper.getStatement(Conn);		
		
		String conditionQuery = getNodeConditions(request);
		
		StringBuffer actualQuery = new StringBuffer();
		actualQuery.append("declare namespace bin ='http://www.rainingdata.com/TigerLogic/binary-support'; declare binary-encoding none; ");
		actualQuery.append(" for $i in collection('tig:///").append(dbName).append("/").append(collName).append("')//").append(topNode);
		
		if(conditionQuery.length() > 0){
			actualQuery.append("[").append(conditionQuery).append("]");
		}		
		actualQuery.append("/descendant::*:*");
		actualQuery.append(" where $i/name()='").append(nodeName).append("'");
		
		if(imageType.equals("")){
			StringBuffer imageTypeQuery = new StringBuffer();
			
			imageTypeQuery.append(actualQuery);
			imageTypeQuery.append(" return data($i/@type)");
			log.info("Query to retrieve image type is :"+imageTypeQuery.toString());
			
			byte[] imageResults = helper.ReadTL(Stmt, imageTypeQuery.toString());
									
			if(imageResults != null && imageResults.length  > 0){
				imageType = new String(imageResults);
			}else{
				imageType = "jpg";
				log.info("Image Type not found............");
			}			
		}
		
		if(imageType.equals("jpg")){
			imageType = "jpeg";
		}
		
		StringBuffer imageQuery = new StringBuffer();
		//imageQuery.append(actualQuery).append("return  if( exists( $i/binary() ) ) then $i/binary() else  bin:base64-decode($i/text())");
		imageQuery.append(actualQuery).append(" return  if( exists( $i/binary() ) ) then $i/binary() else tlsp:GetBinaryImage(binary{$i},'").append(nodeName).append("')");
		log.info("The query in GetImage servlet is "+imageQuery.toString());	
				
		try{
			
			byte[] xmlResults = helper.ReadTL(Stmt, imageQuery.toString()); 
			if(xmlResults != null && xmlResults.length!=0)
			{
			ServletOutputStream outer = response.getOutputStream();	
			response.setContentType("image/"+imageType);
			response.setContentLength(xmlResults.length);
					
	    	outer.write(xmlResults);
	    	outer.flush();
			}else{
				ServletOutputStream outer = response.getOutputStream();	
				outer.println("No Image Found");
			}
	    	//log.info("11Size of the image is :"+xmlResults.length);
	    	
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
}
