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

 
/*
 * Created on Oct 24, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.rdta.epharma.epedigree.action;

import java.io.ByteArrayInputStream;
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
import org.w3c.dom.NodeList;

import test.Helper;

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
public class ProductList extends Action {
	
	private static Log log=LogFactory.getLog(ProductList.class);
	private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	Connection Conn; 
	Statement stmt;
	String clientIP = null;

	
	  public ActionForward execute(ActionMapping mapping, ActionForm form,
			 HttpServletRequest request, HttpServletResponse response)
			 throws Exception {
				
	  	log.info("******Inside ProductList class.......");
	  	
	  	try{	
			
			HttpSession sess = request.getSession();
			Helper helper = new Helper();
			clientIP = request.getRemoteAddr();		
			
			Conn = helper.ConnectTL(); 
			stmt = helper.getStatement(Conn);
			log.info("Validating The Session");
			
			//Validating Session
			String sessionID = (String)sess.getAttribute("sessionID");
			log.info("sessionID in GenerateAlertAction :"+sessionID);
			String validateResult = helper.ValidateUserSession(stmt, sessionID, clientIP);        
		
			if ( !validateResult.equals("VALID")){
			    //return a forward to invalid .
			    return mapping.findForward("loginPage");
			} 
	  				   Enumeration enu = request.getParameterNames();
	  					while ( enu.hasMoreElements()){
	  						
	  						String str = (String) enu.nextElement();
	  						
	  						System.out.println("Str ="+str+"="+request.getParameter(str));
	  					}
	  					 
	  					
	  					String apn = (String )request.getParameter("pedid");
	  					String lotnum= (String )request.getParameter("LotNum");
	  					String orderId=(String)request.getParameter("selpedid");
	  					
	  					StringBuffer xQuery = new StringBuffer();
	  					xQuery.append("<product>{ for $b in collection('tig:///ePharma_MD/APN')/APN[DocumentId = '"+apn);
	  					xQuery.append("']/Pedigrees/Pedigree[@order='"+orderId);
	  					xQuery.append("'] return  let $prds := $b/Products let $mftr := $b/Manufacturer for $p in $prds/Product order by $p/BrandName return ");
	  					xQuery.append(" <tr><ndc>{data($p/NDC)}</ndc><name>{data($p/ProductName)}</name> ");
	  					xQuery.append(" <LotNumber>{data($p/LotNumber)}</LotNumber>");
	  					xQuery.append(" <MANNAME>{data($mftr[LicenseNumber=data($p/ManufacturerLicense)]/Name)}</MANNAME>");
	  					xQuery.append(" <Quantity>{data($p/Quantity)}</Quantity>");
	  					xQuery.append(" <LotExpireDate>{data($p/LotExpireDate)}</LotExpireDate>");
	  					xQuery.append(" <EPC>{data($p/EPC)}</EPC>");
	  					xQuery.append(" </tr>}</product>");
	  					System.out.println("xQuery is ="+xQuery);
	  					
						List list = (List)  queryRunner.executeQuery(xQuery.toString());	
						Node node = XMLUtil.parse((ByteArrayInputStream)list.get(0));
	  		
						request.setAttribute("showProductLists",node);
						request.setAttribute("orderId",orderId);
						request.setAttribute("apn",apn);
					
	  	}catch(Exception ex){
	  		ex.printStackTrace();
	  		log.error("Error inside ProductList Class........."+ex);
	  		return mapping.findForward("exception");
	  	}
	  	
	  					return mapping.findForward("success");
	
	
	
	
	  }
}
