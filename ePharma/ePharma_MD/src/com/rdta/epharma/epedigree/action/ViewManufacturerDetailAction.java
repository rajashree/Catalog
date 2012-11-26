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
 * Created on Oct 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.rdta.epharma.epedigree.action;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
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
public class ViewManufacturerDetailAction extends Action {
	
	private static Log log = LogFactory.getLog(ViewManufacturerDetailAction.class);
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	StringBuffer buffer = null;
	StringBuffer buffer1 = null;
	Connection conn; 
	Statement stmt;
	String clientIP = null;
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		try{
			
			HttpSession sess = request.getSession();
			Helper helper = new Helper();
			clientIP = request.getRemoteAddr();		
			
			conn = helper.ConnectTL(); 
			stmt = helper.getStatement(conn);
			log.info("Validating The Session");
			
			//Validating Session
			String sessionID = (String)sess.getAttribute("sessionID");
			log.info("sessionID in EpedigreeAction :"+sessionID);
			String validateResult = helper.ValidateUserSession(stmt, sessionID, clientIP);        
		
			if ( !validateResult.equals("VALID")){
			    //return a forward to invalid .
			    return mapping.findForward("loginPage");
			}
			log.info("Inside Action ViewManufacturerDetailAction....... ");
			log.info("Inside Action execute of ViewManufacturerDetailAction....");
		
			String APNID = request.getParameter("pedId");
			log.info("The APNID inside ViewManufacturerDetailAction is :"+APNID);
		
			String tp_company_nm = request.getParameter("tp_company_nm");
			log.info("The tp_company_nm inside ViewManufacturerDetailAction is :"+tp_company_nm);
		
			String pagenm = request.getParameter("pagenm");
			log.info("The pagenm inside ViewManufacturerDetailAction is :"+pagenm);
		
			Collection colln= new ArrayList();
	
			String mfrName = request.getParameter("mfrName");
			log.info("The manufacturer name in ViewManufacturerDetailAction is :"+mfrName);
		
			String mfrLicNum = request.getParameter("mfrLicNum");
			log.info("The mfrLicNum in ViewManufacturerDetailAction is :"+mfrLicNum);
		
			String pedOrder = request.getParameter("selpedid");
			log.info("pedOrder in ViewManufacturerDetailAction is "+pedOrder);
		
			buffer = new StringBuffer();
		
			//buffer.append("tlsp:GetManufacturerDetails('"+mfrName+"')");
		
			buffer.append("for $b in collection('tig:///ePharma/APN')/APN ");
			buffer.append("where $b/DocumentId = '"+APNID+"'  ");
			buffer.append("return ");
			buffer.append("<tr class='tableRow_Header'> ");
			buffer.append("<td class='type-whrite'>Pedigree ID : <STRONG><FONT color='#ffff00'>"+APNID+"</FONT></STRONG></td> ");
			buffer.append("<td class='type-whrite'>Issue Date : <FONT color='#ffff00'><STRONG>{data($b/DateTime)}</STRONG></FONT></td> ");
			buffer.append("<td class='type-whrite'>Transaction Type: <FONT color='#ffff00'><STRONG>{data($b/To/TransactionType)}</STRONG></FONT></td> ");
			buffer.append("<td class='type-whrite'>Transaction # : <STRONG><FONT color='#ffff00'>{data($b/To/TransactionNumber)}</FONT></STRONG></td> ");
			buffer.append("<td class='type=whrite' bgcolor='#ccffff'> ");
			buffer.append("<A HREF='ePedigree_ViewOrder.jsp?trType={data($b/To/TransactionType)}&amp;trNum={data($b/To/TransactionNumber)}&amp;tp_company_nm="+tp_company_nm+"&amp;pagenm="+pagenm+"' target='_new'>  ");
			buffer.append("<FONT color='#000099'><STRONG>View</STRONG></FONT> ");
			buffer.append("</A></td> ");
			buffer.append("</tr>");
	
	
			log.info("The query in ViewManufacturerDetailAction is :"+buffer.toString());
		
			String topLine = queryRunner.returnExecuteQueryStringsAsString(buffer.toString());
		
			request.setAttribute("topLine", topLine);		
			log.info("Result after executing the query for topLine in ViewManufacturerDetailAction is :"+topLine);
		
			buffer1 = new StringBuffer();
		
			buffer1.append("for $b in collection('tig:///ePharma/APN')/APN[DocumentId='"+APNID+"'] ");
			buffer1.append("where $b/Pedigrees/Pedigree/Manufacturer/LicenseNumber='"+mfrLicNum+"' ");
			buffer1.append("return ");
			buffer1.append("<table border='0' cellpadding='0' cellspacing='0'><tr><td class='td-menu'>Name:</td> ");
			buffer1.append("<td class='td-menu bold'>{data($b/Pedigrees/Pedigree[@order='"+pedOrder+"']/Manufacturer/Name)}</td></tr> ");
			buffer1.append("<tr><td class='td-menu'>Address:</td> ");
			buffer1.append("<td class='td-menu bold'>{data($b/Pedigrees/Pedigree[@order='"+pedOrder+"']/Manufacturer/Address)}</td></tr> ");
			buffer1.append("<tr><td class='td-menu'>Contact:</td> ");
			buffer1.append("<td class='td-menu bold'>{data($b/Pedigrees/Pedigree[@order='"+pedOrder+"']/Manufacturer/Contact)}</td></tr> ");
			buffer1.append("<tr><td class='td-menu'>ContactPhone:</td> ");
			buffer1.append("<td class='td-menu bold'>{data($b/Pedigrees/Pedigree[@order='"+pedOrder+"']/Manufacturer/ContactPhone)}</td></tr> ");
			buffer1.append("<tr><td class='td-menu'>ContactEmail:</td> ");
			buffer1.append("<td class='td-menu bold'>{data($b/Pedigrees/Pedigree[@order='"+pedOrder+"']/Manufacturer/ContactEmail)}</td></tr> ");
			buffer1.append("<tr><td class='td-menu'>LicenseNumber:</td> ");
			buffer1.append("<td class='td-menu bold'>{data($b/Pedigrees/Pedigree[@order='"+pedOrder+"']/Manufacturer/LicenseNumber)}</td></tr></table> ");
		
			log.info("The query to retrieve the manufacturer details is :"+buffer1.toString());
			log.info("Before executing the query in ViewManufacturerDetailAction to get manufacturer details......");
			String mfrDetails = queryRunner.returnExecuteQueryStringsAsString(buffer1.toString());
			log.info("The manufacturer details is :"+mfrDetails);
			
			request.setAttribute("mfrDetails", mfrDetails);
			
			log.info("Before returning to success");
			
		}catch(Exception ex){
			ex.printStackTrace();
			log.error("Error inside ViewManufacturerDetailAction class......."+ex);
			return mapping.findForward("exception");
		}		
		return mapping.findForward("success");
	}
}
