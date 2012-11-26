/*
 * Created on Oct 20, 2005
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

 package com.rdta.epharma.epedigree.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
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
import org.w3c.dom.Node;

import com.rdta.Admin.Utility.Helper;
import com.rdta.catalog.Constants;
import com.rdta.commons.CommonUtil;
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
public class ProductDisplayAction extends Action{
	private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	private static  Log logger = LogFactory.getLog(ProductDisplayAction.class);
	
	Connection Conn; 
	Statement stmt;
	String clientIP = null;
	
	  public ActionForward execute(ActionMapping mapping, ActionForm form,
			 HttpServletRequest request, HttpServletResponse response)
			 throws Exception {
			
	  logger.info("******Inside ProductDisplayAction...........");
	  ProductDisplayForm theform = null;
	  Collection colln= new ArrayList();	
	  
	  
	  String ndc = request.getParameter("NDC");
	  System.out.println("NDC : "+ndc);
	  String lotNumber = request.getParameter("lotnum");
	  String pedId = request.getParameter("PedigreeId");
	  String envId = (String)request.getAttribute("envId");
	  logger.info(" NDC ="+ndc);
	  logger.info("lotnumber ="+lotNumber);
	  

		try{	
			
			HttpSession sess = request.getSession();
			Helper helper = new Helper();
			clientIP = request.getRemoteAddr();		
			
			Conn = helper.ConnectTL(); 
			stmt = helper.getStatement(Conn);
			logger.info("Validating The Session");
			
			//Validating Session
			String sessionID = (String)sess.getAttribute("sessionID");
			logger.info("sessionID in ProductDisplayAction :"+sessionID);
			String validateResult = helper.ValidateUserSession(stmt, sessionID, clientIP);        
		
			if ( !validateResult.equals("VALID")){
			    //return a forward to invalid .
			    return mapping.findForward("loginPage");
			} 
	  
	  StringBuffer buf = new StringBuffer();
	 //buf.append("tlsp:GetProductId('','')");
	  buf.append("for $i in collection('tig:///ePharma/ShippedPedigree')/PedigreeEnvelope ");
	  buf.append("where $i/pedigree/shippedPedigree/documentInfo/serialNumber = '"+pedId+"' ");
	  buf.append("return <output> <ProductName>{data($i/pedigree/shippedPedigree/initialPedigree/productInfo/drugName)}</ProductName></output> ");
	  System.out.println("Query in product display action : "+buf.toString());
	  Statement Stmt = helper.getStatement(Conn);
	  
	  Enumeration enu = request.getParameterNames();
		while ( enu.hasMoreElements()){
			String str = (String) enu.nextElement();
			logger.info("Str ="+str+"="+request.getParameter(str));
		}
		
	  
	  List list = (List) queryRunner.executeQuery(buf.toString());
	  
	  if ( list.size() > 1){

	  	return mapping.findForward("successddd");
	  	
	  }else{
	  			Node node = XMLUtil.parse((ByteArrayInputStream)list.get(0));
	  			if ( node != null){ 
	  			
	  				String productName = XMLUtil.getValue(node,"ProductName");
	  				System.out.println("Product Name in action : "+productName);
	  				StringBuffer bfr= new StringBuffer();
	  				bfr.append(" declare namespace bin='http://www.rainingdata.com/TigerLogic/binary-support'; declare binary-encoding none; "); 
	  			   /* bfr.append(" for $k in collection('tig:///EAGRFID/Products')[ProductName =  "+productName+"]");
	  			    bfr.append(" for $k in collection ('tig:///EAGRFID/Products')/Product");
	  				bfr.append(" for $D in collection('tig:///EAGRFID/ProductImage')/Location");
	  			    bfr.append(" where data($D/ID) = data($k/genId) and data($k/ProductName) = '"+productName+"'");
	  			    bfr.append(" return $D/LocIMG/binary() ");
	  			   */
	  				
	  				bfr.append("for $K in collection('tig:///EAGRFID/Products')/Product[ProductName = '"+productName+"' ] ");
	  			    bfr.append(" for $D in collection('tig:///EAGRFID/ProductImage')/Product[ID = $K/ProductID]");
	  				bfr.append(" return  if( exists($D/ProdIMG/binary()) ) then $D/ProdIMG/binary() else   bin:base64-decode( $D/ProdIMG ) ");
					System.out.println("Query for getting image: "+bfr.toString());
	  				logger.info( " Query "+bfr.toString());
	  				logger.info("query ="+bfr);
	  			    
	  			    byte[] xmlResults = helper.ReadTL(Stmt,bfr.toString());
	  			    if(xmlResults != null){
	  	 			String str = sess.getServletContext().getRealPath(request.getContextPath());
	  				logger.info(str+"\\..");
	  				File file = new File(str+"\\..\\images\\"+productName);
	  				logger.info("Path ="+ file.getAbsolutePath() ); 
	  				System.out.println("Path : "+file.getAbsolutePath());
	  				FileOutputStream fos = new FileOutputStream(file);		
					fos.write(xmlResults);
					Thread.sleep(2000);
					fos.flush();
					fos.close();
	  			    }				
					request.setAttribute("imagefile","..\\..\\images\\"+productName);
	  			    
	  			    
	  			}
	  			request.setAttribute("productlists",node);
	  			StringBuffer buffer = new StringBuffer();
	  			buffer.append("tlsp:GetProductId('"+pedId+"')");
	  			System.out.println("Query for getting product id: "+buffer.toString());
	  			List result = queryRunner.returnExecuteQueryStrings(buffer.toString());
	  			if(result.size() > 0){
	  			String prodId = (String)result.get(0).toString();
	  			System.out.println("Product Id in action: "+prodId);
	  			request.setAttribute("ProdId",prodId);
	  			}
	  			StringBuffer buff = new StringBuffer();
	  			buff.append("tlsp:GetProductDetails('"+envId+"','"+pedId+"','"+ndc+"')");
	  			System.out.println("Query for getting product details: "+buff.toString());
	  			List res = queryRunner.executeQuery(buff.toString());
	  			
	  			
	  			colln = new ArrayList();
				for(int i=0; i<res.size(); i++){
				
					theform = new ProductDisplayForm();
					Node listNode = XMLUtil.parse((InputStream)res.get(i));
					System.out.println("listNode :"+listNode);
					
					theform.setDosageForm(CommonUtil.jspDisplayValue(listNode,"Product/DosageForm"));
					System.out.println("Dosage form: "+theform.getDosageForm());
					theform.setDosageStrength(CommonUtil.jspDisplayValue(listNode,"Product/DosageStrength"));
					theform.setEpc(CommonUtil.jspDisplayValue(listNode,"Product/EPC"));
					theform.setMfrDate(CommonUtil.jspDisplayValue(listNode,"Product/DosageForm"));
					theform.setMfrName(CommonUtil.jspDisplayValue(listNode,"Product/ManufacturerName"));
					theform.setNdc(CommonUtil.jspDisplayValue(listNode,"Product/NDC"));
					theform.setPbc(CommonUtil.jspDisplayValue(listNode,"Product/BarCode"));
					theform.setProdName(CommonUtil.jspDisplayValue(listNode,"Product/ProductName"));
					theform.setUpc(CommonUtil.jspDisplayValue(listNode,"Product/PackageUPC"));
					colln.add(theform);
					request.setAttribute("Result",listNode);
				}
				
				request.setAttribute(Constants.PRODUCTDISPLAY_DETAILS,colln);
	  			
	  			return mapping.findForward("success");
	  }
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Error in ProductDisplayAction  execute()" + ex);
			return mapping.findForward("exception");
		}
	  
	  }
}
