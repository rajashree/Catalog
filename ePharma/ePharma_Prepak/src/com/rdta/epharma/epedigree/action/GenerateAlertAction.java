
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
import java.io.InputStream;
import java.io.ObjectInputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.rdta.Admin.Utility.Helper;
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.commons.CommonUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;



public class GenerateAlertAction  extends Action
{
	
	String CreatedDate = null;
	String CreatedBy = null;
	String MessageID = null;
	String RelatedProcess = null;
	String Status = null;
	String xml = null;
	String userID = null;
	String uID = null;
	Connection conn; 
	Statement stmt;
	String clientIP = null;
	Helper helper = new Helper();
	
	private static  Log logger = LogFactory.getLog(GenerateAlertAction.class);
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
		
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			 HttpServletRequest request, HttpServletResponse response)
			 throws Exception {
		
		logger.info("Inside GenerateAlertAction....... ");
		logger.info("Inside GenerateAlertAction execute method....... ");
		GenerateAlertFormBean formbean=(GenerateAlertFormBean)form;
		
		try{	
			
			HttpSession sess = request.getSession();
			clientIP = request.getRemoteAddr();		
			
			conn = helper.ConnectTL(); 
			stmt = helper.getStatement(conn);
			logger.info("Validating The Session");
			
			//Validating Session
			String sessionID = (String)sess.getAttribute("sessionID");
			logger.info("sessionID in GenerateAlertAction :"+sessionID);
			String validateResult = helper.ValidateUserSession(stmt, sessionID, clientIP);        
		
			if ( !validateResult.equals("VALID")){
			    //return a forward to invalid .
			    return mapping.findForward("loginPage");
			}
		
		SimpleDateFormat df = new SimpleDateFormat();
	
		df.applyPattern("yyyy-MM-dd");
		String tmDate = df.format(new java.util.Date());
		df.applyPattern("hh:mm:ss");
		String tmTime = df.format(new java.util.Date());
		String CreatedDate = tmDate + "T" + tmTime;
		MessageID = CommonUtil.getGUID();
		uID = request.getParameter("userName");
		System.out.println("User ID selected : "+uID);
		String DocType=request.getParameter("DocType");
		
		
		String DocID = (String)formbean.getAPNID();
		
		String Title = (String)formbean.getTitle();
		String Comments = (String)formbean.getComments();
		String Exceptions = (String)formbean.getExceptions();
		String Roles = (String)formbean.getSystemroles();
		String SeverityLevel = (String)formbean.getSeveritylevel();
		String RequiredAction = (String)formbean.getAction();
		int Level=Integer.valueOf(SeverityLevel).intValue();
				
		StringBuffer buff = new StringBuffer();
		
		String query ="for $x in collection('tig:///EAGRFID/SysSessions')/session ";
			   query = query + " where $x/sessionid = '"+sessionID+"'";
			   query = query + " return <root>{ concat(data($x/userid),\",\",$x/username)}</root> ";
			  
		logger.info("Query for User Info........"+query);
		List list1 = queryRunner.executeQuery(query);
		Node node = XMLUtil.parse((ByteArrayInputStream)list1.get(0));
		String temp= XMLUtil.getValue(node);
		int k = 0;
		userID = temp.substring(0,(k=temp.indexOf(",")));
		CreatedBy = temp.substring(k+1,temp.length());
			 		   
		String query1 = "for $x in collection('tig:///EAGRFID/SystemVocabulary')/SysVocabulary/Category/Terms/Term ";
		       query1 = query1 + "where $x/TermName = '"+Exceptions+"'  ";
			   query1 = query1 + "return  <root> { concat(data($x/Status),',',$x/RelatedEvents/RelatedEvent/EventName)}</root> ";
					
        List list2 = queryRunner.executeQuery(query1);
        if(list2 != null){
		Node node1 = XMLUtil.parse((ByteArrayInputStream)list2.get(0));
		String temp1= XMLUtil.getValue(node1);
		int m=0;
		String Status = temp1.substring(0,(m=temp1.indexOf(",")));
		RelatedProcess=temp1.substring(m+1,temp1.length());
					
		buff.append("tlsp:CreateMessage('"+CreatedDate+"','"+CreatedBy+"','"+MessageID+"','"+Roles+"'," +
				"'"+uID+"','"+RelatedProcess+"','"+Title+"','"+DocType+"','"+DocID+"','"+Level+"','"+RequiredAction+"','"+Comments+"','"+Status+"') ");
		
		logger.info("query for insert message: "+buff);
		List list3 = queryRunner.executeQuery(buff.toString());
        }
		}catch(PersistanceException e){
			logger.error("Error in GenerateAlertAction execute method........." +e);
			throw new PersistanceException(e);
		}
		catch(Exception ex){			
			ex.printStackTrace();
    		logger.error("Error in GenerateAlertAction execute method........." +ex);
    		throw new Exception(ex);
		}finally{
			//close the connection
	    	helper.CloseConnectionTL(conn);
		}
			
		return mapping.findForward("success");
   }

	
	
}
	
	

		

